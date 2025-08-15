package se.boetsch.Battleship.dto;

import se.boetsch.Battleship.domain.GameShip;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlacedShipsDTO {

    private Set<GameShip> ships = ConcurrentHashMap.newKeySet();

    public PlacedShipsDTO() {
    }

    public PlacedShipsDTO(Set<GameShip> ships) {
        this.ships = ships;
    }

    public Set<GameShip> getShips() {
        return ships;
    }

    public void setShips(Set<GameShip> ships) {
        this.ships = ships;
    }
}
