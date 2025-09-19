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
import se.boetsch.Battleship.entity.ShipCoordinates;
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
    final ShipWithPlacementMapper shipWithPlacementMapper;
    private final GameShipMapper gameShipMapper;
    private final FireResultMapper fireResultMapper;

    public GameControllerV1(GameService gameService,
                            ShipPlacementService shipPlacementService,
                            ShipService shipService,
                            GameMapper gameMapper,
                            PlayerMapper playerMapper, ShipWithPlacementMapper shipWithPlacementMapper, GameShipMapper gameShipMapper, FireResultMapper fireResultMapper) {
        this.gameService = gameService;
        this.shipPlacementService = shipPlacementService;
        this.shipService = shipService;
        this.gameMapper = gameMapper;
        this.playerMapper = playerMapper;
        this.shipWithPlacementMapper = shipWithPlacementMapper;
        this.gameShipMapper = gameShipMapper;
        this.fireResultMapper = fireResultMapper;
    }

    @PostMapping
    public ResponseEntity<?> newGame(@Valid @RequestBody PlayerDTO user, ServerHttpRequest request) {

        CurrentPlayer player1 = gameService.createCurrentPlayerWithName(user.name());
        Game newGame = gameService.startNewGame(player1);
        GameStatusDTO g = gameMapper.toSummary(newGame);

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
        Game g = gameService.findGameWithId((long) gameId);
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
        return ResponseEntity.ok(Map.of("success",
                gameShipMapper.toOwnedShipDTO(gameService.placeShip(ship, playerId, gameId)))); // TODO: more info on the response (ship, placement?)
    }

    @PostMapping("/{gameId}/players/{playerId}/fire")
    public ResponseEntity<?> fireAtCoordinates(
            @PathVariable long gameId,
            @PathVariable long playerId,
            @Valid @RequestBody ShipCoordinates coordinates) {
        return ResponseEntity.ok(Map.of("success",
                fireResultMapper.toDTO(gameService.makePlayerFireAt(playerId, gameId, coordinates))));
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<?> joinGame(@Valid @RequestBody PlayerDTO player,
                                      @PathVariable long gameId) {
        return ResponseEntity.ok(
                Map.of(
                        "success",
                        gameMapper.toDTO(gameService.joinGame(gameId, player.name()))
                )
        );
    }

}
