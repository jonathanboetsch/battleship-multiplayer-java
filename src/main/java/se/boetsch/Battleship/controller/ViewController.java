package se.boetsch.Battleship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/{playerName}/game/{gameId}")
@Controller
public class ViewController {

    @Autowired
    GameService gameService;
    @Autowired
    ShipPlacementService shipPlacementService;
    @Autowired
    ShipService shipService;

    private static Game getGameWithId(Integer gameId) {
        return GameService.findGameWithId(gameId);
    }

    @GetMapping("/ships/view")
    public String viewShips(
            @PathVariable String playerName,
            @PathVariable int gameId,
            Model model
    ) {
        CurrentPlayer player = populatePlayerWithName(playerName);
        Game game = getGameWithId(gameId);
        PlayerSet playerSet = game.getPlayerSets().get(player);

        // DTO with following Javabean convention required to display in a Thymeleaf template
        Set<GameShip> ships = playerSet.getPlacedShips();

        Map<String, String> coordinateIcons = new HashMap<>();
        for (GameShip ship : ships) {
            String icon = iconForShip(ship.getName());
            ShipWithPlacement placement = ship.getShipWithPlacement();
            int size = placement.getShip()
                    .getSize();

            for (int i = 0; i < size; i++) {
                ShipCoordinates coord = shipPlacementService.updateExaminedCoordinates(placement, i);
                char rowChar = (char) ('A' + coord.getY());   // Y = row (0-indexed), A-J
                int colNum = coord.getX() + 1;                // X = column (0-indexed), 1-10
                String coordKey = "" + rowChar + colNum;
                coordinateIcons.put(coordKey, icon);
            }
        }

        model.addAttribute("coordinateIcons", coordinateIcons);


        List<String> rowLabels = IntStream.rangeClosed('A', 'J')
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.toList());
        model.addAttribute("rowLabels", rowLabels);

        return "placedShips";
    }

    private CurrentPlayer populatePlayerWithName(String playerName) {
        return gameService.retrieveOrCreatePlayerWithName(playerName);
    }

    /**
     * Show the ship placement page with current game and player info.
     */
    @GetMapping("/ship-placement")
    public String showShipPlacementPage(@PathVariable String playerName,
                                        @PathVariable Integer gameId,
                                        @RequestParam(required = false) String message,
                                        Model model) {
        CurrentPlayer player = gameService.retrieveOrCreatePlayerWithName(playerName);
        if (player.getName() == null) throw new RuntimeException("Player name is null");
        Game game = GameService.findGameWithId(gameId);
        gameService.addPlayerToGameIfRelevant(player, game);
        PlayerSet playerSet = game.getPlayerSetForPlayer(player);

        // Add player and game info to the model
        model.addAttribute("player", player);
        model.addAttribute("game", game);
        if (message != null && message.equalsIgnoreCase("success"))
            model.addAttribute("message", "Ship placed successfully!");

        // Example ship placement (to show on the page for guidance)
        ShipWithPlacement shipWithPlacement = new ShipWithPlacement(
                shipService.getShipByName("BATTLESHIP"),
                new ShipCoordinates(2, 2),
                ShipOrientation.HORIZONTAL);
        model.addAttribute("shipWithPlacement", shipWithPlacement);
        model.addAttribute("remainingShips", gameService.getShipsToPlace(playerSet));

        // Could add more attributes here like remaining ships, etc.

        return "place-ship";  // Thymeleaf template: place-ship.html
    }

    /**
     * Handle form submission or ajax to place a ship.
     */
    @PostMapping("/ship-placement")
    public String placeShip(@PathVariable String playerName,
                            @PathVariable Integer gameId,
                            @ModelAttribute ShipWithPlacement shipWithPlacement, BindingResult br) {
        CurrentPlayer player = gameService.retrieveOrCreatePlayerWithName(playerName);
        Game game = GameService.findGameWithId(gameId);
        PlayerSet playerSet = game.getPlayerSetForPlayer(player);
        shipPlacementService.populateShip(shipWithPlacement);
        shipPlacementService.placeShipInPlayerSet(shipWithPlacement, playerSet);

        return "redirect:/" + playerName + "/game/" + gameId + "/ship-placement?message=success";
    }

    private String iconForShip(String shipName) {
        return switch (shipName.toUpperCase()) {
            case "CARRIER" -> "🛳️";
            case "BATTLESHIP" -> "🚢";
            case "CRUISER" -> "⛴️";
            case "SUBMARINE" -> "🤿";
            case "DESTROYER" -> "🛥️";
            default -> "❓";
        };
    }


}

