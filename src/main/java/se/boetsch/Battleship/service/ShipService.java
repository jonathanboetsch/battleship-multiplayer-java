package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.entity.ShipModel;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShipService {

    public final ShipModel carrier = new ShipModel("Carrier", 5);
    public final ShipModel battleship = new ShipModel("Battleship", 4);
    public final ShipModel destroyer = new ShipModel("Destroyer", 3);
    public final ShipModel submarine = new ShipModel("Submarine", 3);
    public final ShipModel patrolBoat = new ShipModel("Patrol Boat", 2);

    private Set<ShipModel> shipModels = new HashSet<>();

    public ShipService() {
        shipModels.add(carrier);
        shipModels.add(battleship);
        shipModels.add(destroyer);
        shipModels.add(submarine);
        shipModels.add(patrolBoat);
    }

    public ShipModel getShipByName(String name) {
        return shipModels.stream()
                .filter(shipModel -> shipModel.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Wrong ship name"));
    }

}
