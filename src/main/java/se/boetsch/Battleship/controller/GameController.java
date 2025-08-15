package se.boetsch.Battleship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.boetsch.Battleship.domain.CurrentPlayer;
import se.boetsch.Battleship.domain.Game;
import se.boetsch.Battleship.domain.GameShip;
import se.boetsch.Battleship.domain.PlayerSet;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipOrientation;
import se.boetsch.Battleship.entity.ShipWithPlacement;
import se.boetsch.Battleship.service.GameService;
import se.boetsch.Battleship.service.ShipPlacementService;
import se.boetsch.Battleship.service.ShipService;

import java.util.Map;

@RequestMapping("/{playerName}/game/")
@RestController
public class GameController {

    @Autowired
    GameService gameService;
    @Autowired
    ShipPlacementService shipPlacementService;
    @Autowired
    ShipService shipService;

    /// Endpoint to check if the server is responding
    @RestController
    @RequestMapping("/health")
    class HealthController {
        @GetMapping public ResponseEntity<?> ok() { return ResponseEntity.ok().body("OK"); }
    }

    /// method used to retrieve the Game instance every time an API call is made
    private static Game getGameWithId(Integer gameId) {
        return GameService.findGameWithId(gameId);
    }

        /// Instantiates a new Game with a Player.
        /// @param playerName is the name the player will use for the future API calls (placement, fires, ...)
        /// @return the newly created PlayerSet with the sessionId required to start the game (for both players)
    @GetMapping("/new")
    public ResponseEntity<?> newGame(@PathVariable String playerName) {
        try {
            CurrentPlayer player1 = populatePlayerWithName(playerName);
            return ResponseEntity.ok().body(gameService.startNewGame(player1));
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }


    @PostMapping("/{gameId}/place-ship")
    public ResponseEntity<?> placeShip(
            @PathVariable Integer gameId,
            @PathVariable String playerName,
            @RequestBody ShipWithPlacement shipWithPlacement) {

        CurrentPlayer player;
        Game game;
        PlayerSet playerSet;

        try {
            player = populatePlayerWithName(playerName);
            game = getGameWithId(gameId);
            if (!game.getPlayers().contains(player)) {
                game.addPlayer(player);
            }
            playerSet = game.getPlayerSetForPlayer(player);

            shipPlacementService.populateShip(shipWithPlacement);
            GameShip ship = shipPlacementService.placeShipInPlayerSet(shipWithPlacement, playerSet);
            return ResponseEntity.ok().body(ship);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

    }

    private CurrentPlayer populatePlayerWithName(String playerName) {
        return gameService.retrieveOrCreatePlayerWithName(playerName);
    }

    // ShipWithPlacement is placed with the higher left start point
    @GetMapping("/placement-example")
    public ResponseEntity<ShipWithPlacement> getShipPlacementModel() {
        return ResponseEntity.ok().body(new ShipWithPlacement(shipService.getShipByName("BATTLESHIP"), new ShipCoordinates(2, 2), ShipOrientation.HORIZONTAL));
    }

    @PostMapping("/{gameId}/fire")
    public ResponseEntity<?> fireAtCoordinates(
            @PathVariable Integer gameId,
            @PathVariable String playerName,
            @RequestBody ShipCoordinates coordinates) {
        try {
            CurrentPlayer player = populatePlayerWithName(playerName);
            Game game = getGameWithId(gameId);
            return ResponseEntity.ok().body(gameService.makePlayerFireAt(game, player, coordinates));
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }

    private static ResponseEntity<String> badRequest(String message) {
        return ResponseEntity.badRequest().body(message);
    }

    /// REST endpoint that returns, for a given player name and game id, a list of ships that remain to place
    ///   in order to be able to start to fire (both players need to have placed their respective ships before).
    /// Nota: the local variable names have been shortened in order to limit mental overhead, after APoSD books tips.
    @GetMapping("/{gameId}/remaining-ships")
    public ResponseEntity<?> getShipsToPlace(@PathVariable int gameId, @PathVariable String playerName) {
        CurrentPlayer p;
        Game g;
        PlayerSet ps;
        try {
            p = populatePlayerWithName(playerName);
            g = getGameWithId(gameId);
            ps = g.getPlayerSetForPlayer(p);
            return ResponseEntity.ok(
                    Map.of("remainingToPlace", shipPlacementService.getRemainingShipModelsToPlace(ps))
            );

        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

    }


}
