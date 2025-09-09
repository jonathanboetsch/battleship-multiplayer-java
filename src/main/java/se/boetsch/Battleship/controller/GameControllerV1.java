package se.boetsch.Battleship.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ForwardedHeaderUtils;
import se.boetsch.Battleship.domain.CurrentPlayer;
import se.boetsch.Battleship.domain.Game;
import se.boetsch.Battleship.dto.GameStatusDTO;
import se.boetsch.Battleship.dto.PlayerDTO;
import se.boetsch.Battleship.dto.ShipWithPlacementDTO;
import se.boetsch.Battleship.dto.UserDTO;
import se.boetsch.Battleship.service.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/games")
public class GameControllerV1 {

    final
    GameService gameService;
    final
    ShipPlacementService shipPlacementService;
    final
    ShipService shipService;
    final GameMapper gameMapper;
    final PlayerMapper playerMapper;

    public GameControllerV1(GameService gameService,
                            ShipPlacementService shipPlacementService,
                            ShipService shipService,
                            GameMapper gameMapper,
                            PlayerMapper playerMapper) {
        this.gameService = gameService;
        this.shipPlacementService = shipPlacementService;
        this.shipService = shipService;
        this.gameMapper = gameMapper;
        this.playerMapper = playerMapper;
    }

    @PostMapping
    public ResponseEntity<?> newGame(@Valid @RequestBody UserDTO user, ServerHttpRequest request) {

        CurrentPlayer player1 = gameService.createNewCurrentPlayer(user.getPlayerName());
        GameStatusDTO g = gameService.startNewGame(player1);

        return ResponseEntity.created(ForwardedHeaderUtils. adaptFromForwardedHeaders(request.getURI(), request.getHeaders()) // returns the path of the request
                                .path("/{id}") // appends the new game's id
                                .buildAndExpand(g.id()) // replaces {id} above with its computed value
                                .toUri())
                                .body(Map.of(
                                        "game", g,
                                        "player", playerMapper.toDTO(player1)));
    }

    @GetMapping("{gameId}")
    public ResponseEntity<?> getGameStatus(@Valid @RequestBody PlayerDTO inputPlayer, @PathVariable int gameId) {
        Game g = gameService.findGameWithId(gameId);
        return ResponseEntity.ok(gameMapper.toDTO(g));
    }

    @GetMapping("{gameId}/players/{playerId}/ships/remaining-to-place")
    public ResponseEntity<?> getShipsRemainingToPlace(@PathVariable long gameId, @PathVariable long playerId) {
        return ResponseEntity.ok(Map.of("remainingToPlace", gameService.getShipsToPlaceForGameIdAndPlayerId(gameId, playerId)));
    }

    @PostMapping("/{gameId}/players/{playerId}/place-ship")
    public ResponseEntity<?> placeShip(
            @PathVariable long gameId,
            @PathVariable long playerId,
            @Valid @RequestBody ShipWithPlacementDTO ship) {
        return ResponseEntity.ok(Map.of("success", gameService.placeShip(ship, playerId, gameId))); // TODO: more info on the response (ship, placement?)
    }


}
