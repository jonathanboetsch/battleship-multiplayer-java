package se.boetsch.Battleship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.*;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private static final Set<Game> games = ConcurrentHashMap.newKeySet();
    @Autowired
    CurrentPlayerService currentPlayerService;
    @Autowired
    BattleMapService battleMapService;
    @Autowired
    ShipService shipService;

    public GameService() {
    }

    public static Game findGameWithId(int gameId) {
        return games.stream()
                .filter(g -> g.getSessionId().getId() == gameId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No game found with the id: " + gameId));
    }

    public static Optional<CurrentPlayer> findPlayerWithName(String playerName) {
        return CurrentPlayerService.findByName(playerName);
    }

    private static void registerFiredPoint(PlayerSet playerSet, ShipCoordinates coordinates, FireResult fireResult) {
        playerSet.registerFireAt(coordinates, fireResult);
    }

    private static CurrentPlayer getOtherPlayerForGame(CurrentPlayer player, Game game) {
        return game.getPlayerSets().keySet().stream()
                .filter(s -> !s.equals(player))
                .findAny().orElseThrow(() -> new RuntimeException("Player not found in the game with id " + game.getSessionId()));
    }

    public List<ShipModel> getShipsToPlace(PlayerSet playerSet) {
        return shipService.getShipModels()
                .stream()
                .filter(m -> !playerSet.getPlacedShips()
                        .stream()
                        .map(s -> s.getShipWithPlacement()
                                .getShip())
                        .toList()
                        .contains(m)).toList();
    }

    public PlayerSet startNewGame(CurrentPlayer player1) {
        Game newGame = new Game(player1);
        games.add(newGame);
        newGame.setNumberOfRemainingShips(shipService.getShipModels().size());
        newGame.addPlayer(player1);
        return newGame.getPlayerSetForPlayer(player1);
    }

    public void createNewPlayerForGame(String name, Game game) {
        game.addPlayer(currentPlayerService.create(name));
    }

    public CurrentPlayer retrieveOrCreatePlayerWithName(String playerName) {

        Optional<CurrentPlayer> playerToBeFound = GameService.findPlayerWithName(playerName);

        if (playerToBeFound.isPresent()) return playerToBeFound.get();

        else {
            return currentPlayerService.create(playerName);
        }
    }

    // TODO: store hits/misses on a map to help the player for the next moves
    public String makePlayerFireAt(Game game, CurrentPlayer player, ShipCoordinates coordinates) {
        if (game.isFinished()) throw new RuntimeException("Game already finished");
        else if (game.getPlayers().size() < 2)
            throw new RuntimeException("Cannot play: another player must be registered");
        else if (!game.getPlayers().contains(player))
            throw new RuntimeException("Cannot play: already 2 players in the game");
        else if (game.getPlayersTurn().get(player) == false)
            throw new RuntimeException("Its not your turn");
        else if (!game.getPlayerSets().get(player).isShipsPlacementComplete())
            throw new RuntimeException("You must finish placement of your ships");

        CurrentPlayer adversary = getOtherPlayerForGame(player, game);
        if (!game.getPlayerSets().get(adversary).isShipsPlacementComplete())
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
        if (!game.getPlayers().contains(player)) {
            game.addPlayer(player);
        }
    }

    // Todo: create a Builder so that the Game cannot be instantiated before two players are in.

}
