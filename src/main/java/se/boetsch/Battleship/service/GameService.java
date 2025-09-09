package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.*;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.exception.GameException;
import se.boetsch.Battleship.exception.PlayerRetrievalException;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private ConcurrentHashMap<Long, Game> games;
    private static ConcurrentHashMap<Long, Game> gamesById = new ConcurrentHashMap<>();
    private final
    CurrentPlayerService currentPlayerService;
    private final
    BattleMapService battleMapService;
    private final GameMapper gameMapper;
    private final ShipPlacementService shipPlacementService;

    public GameService(CurrentPlayerService currentPlayerService, BattleMapService battleMapService, GameMapper gameMapper, ShipPlacementService shipPlacementService) {
        this.currentPlayerService = currentPlayerService;
        this.battleMapService = battleMapService;
        this.gameMapper = gameMapper;
        this.shipPlacementService = shipPlacementService;
        this.games = new ConcurrentHashMap<>();
    }

    public Game findGameWithId(long gameId) {
        return Optional.ofNullable(gamesById.get(gameId)).orElseThrow(() -> new GameException("Game with id " + gameId + " not found"));
    }

    public Game findGameWithId(int gameId) {
        return games.values().stream()
                .filter(g -> g.getSessionId().getId() == gameId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No game found with the id: " + gameId));
    }

    public Optional<CurrentPlayer> findPlayerWithName(String playerName) {
        return currentPlayerService.findByName(playerName);
    }

    public CurrentPlayer retrieveCurrentPlayerWithName(String playerName) {
        return findPlayerWithName(playerName).orElseThrow(() -> new PlayerRetrievalException("Did not found player with name: " + playerName));
    }

    private void registerFiredPoint(PlayerSet playerSet, ShipCoordinates coordinates, FireResult fireResult) {
        playerSet.registerFireAt(coordinates, fireResult);
    }

    public CurrentPlayer createCurrentPlayerWithName(String playerName) {
        return currentPlayerService.create(playerName);
    }

    private CurrentPlayer getOtherPlayerForGame(CurrentPlayer player, Game game) {
        long adversaryId = game.getOtherPlayerId(player.getPlayer().getId());
        return currentPlayerService.findById(adversaryId).orElseThrow(() -> new PlayerRetrievalException("Player with id " + adversaryId + " not found"));
    }

    public PlayerSet startNewGame(CurrentPlayer player1) {
        Game newGame = new Game(player1.getPlayer().getId());
        games.put(newGame.getSessionId().getId(), newGame);
        newGame.addPlayer(player1);
        return newGame.getPlayerSetForPlayer(player1);
    }

    public void createNewPlayerForGame(String name, Game game) {
        game.addPlayer(currentPlayerService.create(name));
    }

    public CurrentPlayer retrieveOrCreatePlayerWithName(String playerName) {

        Optional<CurrentPlayer> playerToBeFound = findPlayerWithName(playerName);

        return playerToBeFound.orElseGet(() -> currentPlayerService.create(playerName));
    }

    // TODO: store hits/misses on a map to help the player for the next moves
    public String makePlayerFireAt(Game game, CurrentPlayer player, ShipCoordinates coordinates) {
        if (game.isFinished()) throw new RuntimeException("Game already finished");
        else if (game.getPlayerIds().size() < 2)
            throw new RuntimeException("Cannot play: another player must be registered");
        else if (!game.getPlayerIds().contains(player))
            throw new RuntimeException("Cannot play: already 2 players in the game");
        else if (game.getPlayersTurn().get(player) == false)
            throw new RuntimeException("Its not your turn");
        else if (!game.getPlayerSets().get(player).isShipsPlacementComplete())
            throw new RuntimeException("You must finish placement of your ships");

        CurrentPlayer adversary = getOtherPlayerForGame(player, game);
        if (!game.getPlayerSets().get(adversary.getPlayer().getId()).isShipsPlacementComplete())
            throw new RuntimeException("Other player must place its ships");

        Optional<GameShip> shipToBeFound = battleMapService.getShipAtCoordinatesForPlayerSet(
                coordinates,
                game.getPlayerSetForPlayer(adversary)
        );
        try {
            // TODO @Refactor [3h]: Break down GameService into subcomponents
            //    GameManager → responsible for starting and finding games
            //    TurnService → validate and advance turns
            //    FireService → handle logic of firing
            //    GameValidator → enforce business rules
            if (shipToBeFound.isPresent()) {
                registerFiredPoint(game.getPlayerSetForPlayer(player), coordinates, FireResult.HIT);
                shipToBeFound.get().takeOneLife(); // Returns an Exception if the ship is already destroyed (!isAlive() == true)
                if (shipToBeFound.get().isAlive()) {
                    game.changeTurn();
                    return "HIT";
                } else {
                    if (game.isFinished()) return "YOU WON !!!";
                    game.changeTurn();
                    return " BOAT SUNK!"; // !isAlive(): ship just destroyed
                }
            } else {
                game.changeTurn();
                registerFiredPoint(game.getPlayerSetForPlayer(player), coordinates, FireResult.MISS);
                return "MISS";
            }
        } catch (Exception e) {
            throw new RuntimeException("Error when determining hit or miss");
        }
    }

    public void addPlayerToGameIfRelevant(CurrentPlayer player, Game game) {
        if (!game.getPlayerIds().contains(player.getPlayer().getId())) {
            game.addPlayer(player);
        }
    }

    public PlayerSet getPlayerSetForGameAndPlayerId(long sessionId, long playerId) {
        return games.get(sessionId).getPlayerSetForPlayer(playerId);
    }

    // Todo: create a Builder so that the Game cannot be instantiated before two players are in.

}
