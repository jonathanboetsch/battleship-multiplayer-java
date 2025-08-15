package se.boetsch.Battleship.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.GameShip;
import se.boetsch.Battleship.domain.PlayerSet;
import se.boetsch.Battleship.domain.ShipToPlaceDto;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipOrientation;
import se.boetsch.Battleship.entity.ShipWithPlacement;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShipPlacementService {

    @Autowired
    BattleMapService battleMapService;
    @Autowired
    ShipService shipService;

    // coordinates set to
    // 0 if under examination before placement
    // 1 if a boat is positioned

    private static boolean isShipPlacedOutsideTheMap(ShipWithPlacement shipWithPlacement) {
        return shipWithPlacement.getStartPosition().getX() + shipWithPlacement.getShip().getSize() > 10;
    }

    private static boolean isPlayerSetContainingShip(PlayerSet playerSet, String shipName) {
        return playerSet.getPlacedShips()
                .stream()
                .anyMatch(existingShip -> existingShip.getName().equals(shipName));
    }

    @Transactional
    public GameShip placeShipInPlayerSet(ShipWithPlacement shipWithPlacement, PlayerSet playerSet) {
        String shipName = shipWithPlacement.getShip().getName();
        if (isShipPlacedOutsideTheMap(shipWithPlacement)) {
            throw new RuntimeException("Ship positions outside the map !");

        } else if (isPlayerSetContainingShip(playerSet, shipName)) {
            List<ShipToPlaceDto> remainingShips = getRemainingShipModelsToPlace(playerSet);
            throw new RuntimeException("Ship already placed, remaining:" + remainingShips);

        } else {
            Optional<ShipCoordinates> shipToBeFound = getConflictingCoordinates(shipWithPlacement, playerSet);
            if (shipToBeFound.isPresent())
                throw new RuntimeException(
                        "Conflicts with " +
                                battleMapService
                                        .getShipAtCoordinatesForPlayerSet(shipToBeFound.get(), playerSet)
                                        .orElseThrow(RuntimeException::new) +
                                " at: " + shipToBeFound.get());

            GameShip shipToPlace;
            try {
                shipToPlace = new GameShip(shipWithPlacement);
                playerSet.addPlacedShip(shipToPlace);

                for (int i = 0; i < shipWithPlacement.getShip().getSize(); i++) {
                    ShipCoordinates examinedCoordinates = updateExaminedCoordinates(shipWithPlacement, i);
                    battleMapService.setShipAtCoordinatesForPlayerSet(shipToPlace, examinedCoordinates, playerSet);
                }

            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
            playerSet.setShipsPlacementComplete(playerSet.getShips().size() == shipService.getShipModels().size());
            return shipToPlace;
        }
    }

    public Set<String> getAllOccupiedCoordinatesForShips(Set<GameShip> ships) {
        Set<String> occupied = new HashSet<>();
        for (GameShip ship : ships) {
            ShipWithPlacement placement = ship.getShipWithPlacement();
            int size = placement.getShip().getSize();

            for (int i = 0; i < size; i++) {
                ShipCoordinates coord = updateExaminedCoordinates(placement, i);
                char rowChar = (char) ('A' + coord.getY());
                int colNum = coord.getX() + 1;
                occupied.add("" + rowChar + colNum);
            }
        }
        return occupied;
    }

    public List<ShipToPlaceDto> getRemainingShipModelsToPlace(PlayerSet ps) {
        return shipService.getShipModels().stream()
                .filter(model -> ps.getPlacedShips().stream()
                        .noneMatch(m -> m.getName().equalsIgnoreCase(model.getName())))
                .map(model -> new ShipToPlaceDto(model.getName(), model.getSize()))
                .toList();
    }

    private Optional<ShipCoordinates> getConflictingCoordinates(ShipWithPlacement shipWithPlacement, PlayerSet playerSet) {
        for (int i = 0; i < shipWithPlacement.getShip().getSize(); i++) {
            ShipCoordinates examinedCoordinates = updateExaminedCoordinates(shipWithPlacement, i);
            Optional<GameShip> shipAtCoordinates = battleMapService.getShipAtCoordinatesForPlayerSet(examinedCoordinates, playerSet);
            if (shipAtCoordinates.isPresent()) return Optional.of(examinedCoordinates);
        }
        return Optional.empty();
    }

    public ShipCoordinates updateExaminedCoordinates(ShipWithPlacement shipWithPlacement, int i) {
        return new ShipCoordinates(
                shipWithPlacement.getStartPosition().getX() + i * horizontalCoeff(shipWithPlacement),
                shipWithPlacement.getStartPosition().getY() + i * verticalCoeff(shipWithPlacement)
        );
    }

    private int horizontalCoeff(ShipWithPlacement shipWithPlacement) {
        if (shipWithPlacement.getOrientation().equals(ShipOrientation.HORIZONTAL)) {
            return 1;
        } else if (shipWithPlacement.getOrientation().equals(ShipOrientation.VERTICAL)) {
            return 0;
        } else {
            throw new RuntimeException("Wrong value orientation");
        }
    }

    private int verticalCoeff(ShipWithPlacement shipWithPlacement) {
        return Math.abs(horizontalCoeff(shipWithPlacement) - 1); // returns opposite value of horizontalCoeff()
    }

    public void populateShip(ShipWithPlacement shipWithPlacement) {
        if (shipWithPlacement.getName() == null && (shipWithPlacement.getShip() == null || shipWithPlacement.getShip().getName() == null)) { throw new RuntimeException("Ship should not be null"); }
        shipWithPlacement.setShip(
                shipService.getShipByName(
                        shipWithPlacement.getName() == null ? shipWithPlacement.getShip().getName() : shipWithPlacement.getName()
                )
        );
    }
}
