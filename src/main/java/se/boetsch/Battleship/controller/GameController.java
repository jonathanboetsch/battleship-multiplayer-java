package se.boetsch.Battleship.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.boetsch.Battleship.domain.CurrentPlayer;
import se.boetsch.Battleship.domain.Game;
import se.boetsch.Battleship.domain.GameShip;
import se.boetsch.Battleship.domain.PlayerSet;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipOrientation;
import se.boetsch.Battleship.exception.PlayerRetrievalException;
import se.boetsch.Battleship.service.GameService;
import se.boetsch.Battleship.service.ShipPlacementService;
import se.boetsch.Battleship.service.ShipService;

import java.util.Map;

@RequestMapping("/{playerName}/game/")
@RestController
public class GameController {

    final
    GameService gameService;
    final
    ShipPlacementService shipPlacementService;
    final
    ShipService shipService;

    public GameController(GameService gameService, ShipPlacementService shipPlacementService, ShipService shipService) {
        this.gameService = gameService;
        this.shipPlacementService = shipPlacementService;
        this.shipService = shipService;
    }

    /// method used to retrieve the Game instance every time an API call is made
    private static Game getGameWithId(Integer gameId) {
        return GameService.findGameWithId(gameId);
    }

    private static ResponseEntity<String> badRequest(String message) {
        return ResponseEntity.badRequest().body(message);
    }

    /// Instantiates a new Game with a Player.
    ///
    /// @param playerName is the name the player will use for the future API calls (placement, fires, ...)
    /// @return the newly created PlayerSet with the sessionId required to start the game (for both players)
    @GetMapping("/new")
    public ResponseEntity<?> newGame(@PathVariable String playerName) {
        try {
            CurrentPlayer player1 = gameService.createNewCurrentPlayer(playerName);
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
            try {
                player = gameService.findPlayerWithName(playerName);
            } catch (PlayerRetrievalException e) {
                player = gameService.createNewCurrentPlayer(playerName);
            }
            game = getGameWithId(gameId);
            if (!game.getPlayerIds().contains(player)) {
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
            CurrentPlayer player = gameService.findPlayerWithName(playerName);
            Game game = getGameWithId(gameId);
            return ResponseEntity.ok().body(gameService.makePlayerFireAt(game, player, coordinates));
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
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
            p = gameService.findPlayerWithName(playerName); // enforces necessity for the player to exist
            g = getGameWithId(gameId);
            ps = g.getPlayerSetForPlayer(p);
            return ResponseEntity.ok(
                    Map.of("remainingToPlace", shipPlacementService.getRemainingShipsToPlace(ps))
            );

        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

    }


}
