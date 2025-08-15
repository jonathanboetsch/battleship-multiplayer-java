package se.boetsch.Battleship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipOrientation;
import se.boetsch.Battleship.entity.ShipPlacement;
import se.boetsch.Battleship.service.GameService;
import se.boetsch.Battleship.service.ShipPlacementService;
import se.boetsch.Battleship.service.ShipService;
import jakarta.validation.Valid;

import java.security.Principal;

@RequestMapping("/game")
@RestController
public class GameController {

    @Autowired
    GameService gameService;
    @Autowired
    ShipPlacementService shipPlacementService;
    @Autowired
    ShipService shipService;

    @PostMapping("/place-ship")
    public ResponseEntity<?> placeShip(@Valid @RequestBody ShipPlacement shipPlacement, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            shipPlacement.setShip(shipService.getShipByName(shipPlacement.getShip().getName()));
            return ResponseEntity.ok().body(shipPlacementService.placeShip(shipPlacement));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/placement-example")
    public ResponseEntity<ShipPlacement> getShipPlacementModel() {
        return ResponseEntity.ok().body(
                new ShipPlacement(
                        shipService.battleship,
                        new ShipCoordinates(2,2),
                        ShipOrientation.HORIZONTAL
                )
        );
    }

    // TODO: detect who is firing to determine the map to fire at
    @PostMapping("/fire/{coordinates}")
    public ResponseEntity<?> fireAtCoordinates(
            Principal principal,
            @RequestBody ShipCoordinates coordinates
    ){

    }

}
