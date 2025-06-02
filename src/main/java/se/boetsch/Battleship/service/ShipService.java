package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.entity.Ship;
import se.boetsch.Battleship.entity.ShipPlacement;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShipService {

    public final Ship carrier = new Ship("Carrier", 5);
    public final Ship battleship = new Ship("Battleship", 4);
    public final Ship destroyer = new Ship("Destroyer", 3);
    public final Ship submarine = new Ship("Submarine", 3);
    public final Ship patrolBoat = new Ship("Patrol Boat", 2);

    private Set<Ship> ships = new HashSet<>();

    public ShipService() {
        ships.add(carrier);
        ships.add(battleship);
        ships.add(destroyer);
        ships.add(submarine);
        ships.add(patrolBoat);
    }

    public Ship getShipByName(String name) {
        return ships.stream()
                .filter(ship -> ship.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Wrong ship name"));
    }

}
