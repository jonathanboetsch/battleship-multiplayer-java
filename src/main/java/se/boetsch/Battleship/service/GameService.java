package se.boetsch.Battleship.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.*;
import se.boetsch.Battleship.domain.FireResult;
import se.boetsch.Battleship.dto.ShipToPlaceDTO;
import se.boetsch.Battleship.dto.ShipWithPlacementDTO;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.exception.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final ShipWithPlacementMapper shipWithPlacementMapper;
    private final GameShipMapper gameShipMapper;
    private ConcurrentHashMap<Long, Game> games;
    private ConcurrentHashMap<Long, Game> gamesById;
    private final
    CurrentPlayerService currentPlayerService;
    private final
    BattleMapService battleMapService;
    private final GameMapper gameMapper;
    private final ShipPlacementService shipPlacementService;
    private final ShipService shipService;
    private final ShipModelMapper shipModelMapper;

    public GameService(CurrentPlayerService currentPlayerService, BattleMapService battleMapService, GameMapper gameMapper, ShipPlacementService shipPlacementService, ShipService shipService, ShipModelMapper shipModelMapper, ShipWithPlacementMapper shipWithPlacementMapper, GameShipMapper gameShipMapper) {
        this.currentPlayerService = currentPlayerService;
        this.battleMapService = battleMapService;
        this.gameMapper = gameMapper;
        this.shipPlacementService = shipPlacementService;
        this.shipService = shipService;
        this.shipModelMapper = shipModelMapper;
        this.shipWithPlacementMapper = shipWithPlacementMapper;
        this.gameShipMapper = gameShipMapper;
        this.games = new ConcurrentHashMap<>(2);
        this.gamesById = new ConcurrentHashMap<>(2);
    }

    public Game findGameWithId(long gameId) {
        return Optional.ofNullable(gamesById.get(gameId)).orElseThrow(() -> new GameException("Game with id " + gameId + " not found"));
    }

    public Game findGameWithId(int gameId) {
        return games.values().stream()
                .filter(g -> g.getSessionId().getId() == gameId)
                .findFirst()
                .orElseThrow(() -> new WrongInputException("No game found with the id: " + gameId));
    }

    public Optional<CurrentPlayer> findPlayerWithName(String playerName) {
        return currentPlayerService.findByName(playerName);
    }

    public CurrentPlayer retrieveCurrentPlayerWithName(String playerName) {
        return findPlayerWithName(playerName).orElseThrow(() -> new PlayerRetrievalException("Did not found player with name: " + playerName));
    }

    private void registerFiredPoint(PlayerSet playerSet, ShipCoordinates coordinates, FireStatus fireStatus) {
        playerSet.registerFireAt(coordinates, fireStatus);
    }

    public CurrentPlayer createCurrentPlayerWithName(String playerName) {
        CurrentPlayer pl = currentPlayerService.create(playerName);
        currentPlayerService.addPlayer(pl);
        return pl;
    }

    private CurrentPlayer getOtherPlayerForGame(CurrentPlayer player, Game game) {
        long adversaryId = game.getOtherPlayerId(player.getPlayer().getId());
        return currentPlayerService.findById(adversaryId).orElseThrow(() -> new PlayerRetrievalException("Player with id " + adversaryId + " not found"));
    }

    public Game startNewGame(CurrentPlayer player1) {
        Game newGame = new Game(player1.getPlayer().getId());
        games.put(newGame.getSessionId().getId(), newGame);
        currentPlayerService.addPlayer(player1);
        return newGame;
    }

    public FireResult makePlayerFireAt(Game game, CurrentPlayer player, ShipCoordinates coordinates) {
        CurrentPlayer adversary = checkPreconditions(game, player);

        Optional<GameShip> shipToBeFound = resolveFire(game, adversary, coordinates);

        // Core logic of firing handling from orchestrator's standpoint
        // (GameService
        //  -> PlayerSet
        //      .registerFiredPoint() -> PlayerSet
        //  -> GameShip
        //      .takeOneLife()
        //      .isAlive()
        //  -> Game
        //      .changeTurn()
        //      .isFinished() )
        try {
            if (shipToBeFound.isPresent()) {
                return handleHit(game, player, coordinates, shipToBeFound);
            } else {
                return handleMiss(game, player, coordinates);
            }
        } catch (Exception e) {
            throw new GameLogicException("Error when determining hit or miss");
        }
    }

    public FireResult handleMiss(Game game, CurrentPlayer player, ShipCoordinates coordinates) {
        game.changeTurn();
        registerFiredPoint(game.getPlayerSetForPlayer(player), coordinates, FireStatus.MISS);
        return new FireResult(FireStatus.MISS, coordinates);
    }

    public FireResult handleHit(Game game, CurrentPlayer player, ShipCoordinates coordinates, Optional<GameShip> shipToBeFound) {
        registerFiredPoint(game.getPlayerSetForPlayer(player), coordinates, FireStatus.HIT);
        shipToBeFound.get().takeOneLife(); // Returns an Exception if the ship is already destroyed (!isAlive() == true)
        if (shipToBeFound.get().isAlive()) {
            game.changeTurn();
            return new FireResult(FireStatus.HIT, coordinates);
        } else {
            if (game.isFinished()) return new FireResult(FireStatus.WIN, coordinates);
            game.changeTurn();
            return new FireResult(FireStatus.HIT_AND_SUNK, coordinates);
        }
    }

    /**
     * Looks for a potential **ship already placed** at the fire coordinates
     * @param game
     * @param adversary
     * @param coordinates
     * @return an Optional containing either the GameShip instance (i,e, hit) or empty() (i.e. miss)
     */
    public Optional<GameShip> resolveFire(Game game, CurrentPlayer adversary, ShipCoordinates coordinates) {
        Optional<GameShip> shipToBeFound = battleMapService.getShipAtCoordinatesForPlayerSet(
                coordinates,
                game.getPlayerSetForPlayer(adversary)
        );
        return shipToBeFound;
    }

    /**
     * Defensive checks to ensure the player can fire without breaking the game logic
     * @param game: the game instance being concerned
     * @param player the player that want to fire at a point
     * @return a CurrentPlayer instance of the adversary, required for the core firing logic
     */
    public CurrentPlayer checkPreconditions(Game game, CurrentPlayer player) {
        long playerId = player.getPlayer().getId();

        // Defensive programming to early catch potential exceptions
        if (game.isFinished()) throw new RuntimeException("Game already finished");
        else if (game.getPlayerIds().size() < 2)
            throw new GameException("Cannot play: another player must be registered");
        else if (!game.getPlayerIds().contains(playerId))
            throw new GameException("Cannot play: already 2 players in the game");
        else if (!game.getPlayersTurn().get(playerId))
            throw new GameException("Its not your turn");
        else if (!game.getPlayerSets().get(playerId).isShipsPlacementComplete())
            throw new GameException("You must finish placement of your ships");

        CurrentPlayer adversary = getOtherPlayerForGame(player, game);

        if (!game.getPlayerSets().get(adversary.getPlayer().getId()).isShipsPlacementComplete())
            throw new GameException("Other player must place its ships");
        return adversary;
    }

    public void addPlayerToGameIfRelevant(CurrentPlayer player, Game game) {
        if (!game.getPlayerIds().contains(player.getPlayer().getId())) {
            game.addPlayer(player);
        }
    }

    public PlayerSet getPlayerSetForGameAndPlayerId(long sessionId, long playerId) {
        return games.get(sessionId).getPlayerSetForPlayerWithId(playerId);
    }

    public Set<ShipToPlaceDTO> getShipsToPlaceForGameIdAndPlayerId(long gameId, long playerId) {
        return new HashSet<>(shipPlacementService.getRemainingShipsToPlace(games.get(gameId).getPlayerSetForPlayerWithId(playerId)));
    }

    public GameShip placeShip(@Valid ShipWithPlacementDTO ship, long playerId, long gameId) {
        return shipPlacementService.placeShipInPlayerSet(shipWithPlacementMapper.toDomain(ship),
                games.get(gameId).getPlayerSetForPlayerWithId(playerId));
    }

    // Todo: create a Builder so that the Game cannot be instantiated before two players are in.

    public FireResult makePlayerFireAt(long playerId, long gameId, ShipCoordinates coordinates) {
        CurrentPlayer player = retrieveCurrentPlayerWithId(playerId);
        Game game = retrieveGameById(gameId);
        return makePlayerFireAt(game, player, coordinates);
    }

    private Game retrieveGameById(long gameId) {
        return Optional.ofNullable(games.get(gameId))
                .orElseThrow(() -> new GameException("Game with id " + gameId + " not found"));
    }

    private CurrentPlayer retrieveCurrentPlayerWithId(long playerId) {
        return currentPlayerService.findById(playerId).orElseThrow(() -> new PlayerRetrievalException("Player with id " + playerId + " not found"));
    }

    public Game joinGame(long gameId, String playerName) {
        CurrentPlayer player = findPlayerWithName(playerName).orElseGet(() -> createCurrentPlayerWithName(playerName));
        Game game = Optional.ofNullable(games.get(gameId))
                .orElseThrow(() -> new GameNotFoundException(gameId));
        addPlayerToGame(game, player);
        return game;
    }

    private void addPlayerToGame(Game game, CurrentPlayer player) {
        game.addPlayer(player);
        currentPlayerService.addPlayer(player);
    }

}
