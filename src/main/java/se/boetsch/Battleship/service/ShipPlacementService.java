package se.boetsch.Battleship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipOrientation;
import se.boetsch.Battleship.entity.ShipPlacement;

import java.util.Optional;

@Service
public class ShipPlacementService {

    @Autowired
    BattleMapService battlemapService;

    // coordinates set to
    // 0 if under examination before placement
    // 1 if a boat is positioned
    private final BattleMap battleMap;

    public ShipPlacementService(BattleMap battleMap) {
        this.battleMap = battleMap;
    }

    private static boolean isShipPlacedOutsideTheMap(ShipPlacement shipPlacement) {
        return shipPlacement.getStartPosition().getHorizontalPos() + shipPlacement.getShip().getSize() > 10;
    }

    public ShipPlacement placeShip(ShipPlacement shipPlacement) {
        if (isShipPlacedOutsideTheMap(shipPlacement)) {
            throw new RuntimeException("Ship positions outside the map !");
        } else if (isShipPlacementConflicting(shipPlacement)) {
            throw new RuntimeException("Conflicts with an already placed ship !");
        } else {
            try {
                for (int i = 0; i < shipPlacement.getShip().getSize() ; i++) {
                    ShipCoordinates examinedCoordinates = updateExaminedCoordinates(shipPlacement, i);
                    battleMap.setCoordinatesActive(examinedCoordinates);
                }
            } catch (Exception e) {
                throw new RuntimeException("Couldn't determine placement conflicts");
            }
            return shipPlacement;
        }
    }

    private boolean isShipPlacementConflicting(ShipPlacement shipPlacement) {
        return getConflictingCoordinates(shipPlacement).isPresent();
    }

    private Optional<ShipCoordinates> getConflictingCoordinates(ShipPlacement shipPlacement) {
        for (int i = 0; i < shipPlacement.getShip().getSize(); i++) {
            ShipCoordinates examinedCoordinates = updateExaminedCoordinates(shipPlacement, i);
            int valueAtPoint = battlemapService.getValueAtCoordinates(examinedCoordinates);
            if (valueAtPoint == 1) return Optional.of(examinedCoordinates);
        }
        return Optional.empty();
    }

    private ShipCoordinates updateExaminedCoordinates(ShipPlacement shipPlacement, int i) {
        return new ShipCoordinates(
                shipPlacement.getStartPosition().getHorizontalPos() + i * horizontalCoeff(shipPlacement),
                shipPlacement.getStartPosition().getVerticalPos() + i * verticalCoeff(shipPlacement)
        );
    }

    private int horizontalCoeff(ShipPlacement shipPlacement) {
        if (shipPlacement.getOrientation().equals(ShipOrientation.HORIZONTAL)) {
            return 1;
        } else if (shipPlacement.getOrientation().equals(ShipOrientation.VERTICAL)) {
            return 0;
        } else {
            throw new RuntimeException("Wrong value orientation");
        }
    }

    private int verticalCoeff(ShipPlacement shipPlacement) {
        return Math.abs(horizontalCoeff(shipPlacement) - 1); // returns opposite value of horizontalCoeff()
    }

}
