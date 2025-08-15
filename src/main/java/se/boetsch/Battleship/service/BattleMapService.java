package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.GameShip;
import se.boetsch.Battleship.domain.PlayerSet;
import se.boetsch.Battleship.entity.ShipCoordinates;

import java.util.Optional;

@Service
public class BattleMapService {

    public Optional<GameShip> getShipAtCoordinatesForPlayerSet(ShipCoordinates shipCoordinates, PlayerSet playerSet) {
        return Optional.ofNullable(playerSet.getBattleMap().getMap()[shipCoordinates.getX()][shipCoordinates.getY()]);
    }

    public void setShipAtCoordinatesForPlayerSet(GameShip ship, int x, int y, PlayerSet playerSet) {
        playerSet.getBattleMap().setShipAtCoordinates(ship, x, y);
    }

    public void setShipAtCoordinatesForPlayerSet(GameShip ship, ShipCoordinates coordinates, PlayerSet playerSet) {
        playerSet.getBattleMap().setShipAtCoordinates(ship, coordinates);
    }

}
