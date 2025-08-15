package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.entity.ShipModel;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShipService {

    public final ShipModel CARRIER = new ShipModel("Carrier", 5);
    public final ShipModel BATTLESHIP = new ShipModel("Battleship", 4);
    public final ShipModel DESTROYER = new ShipModel("Destroyer", 3);
    public final ShipModel SUBMARINE = new ShipModel("Submarine", 3);
    public final ShipModel PATROL_BOAT = new ShipModel("Patrol Boat", 2);
    private final Set<ShipModel> shipModels = new HashSet<>();

    public ShipService() {
        shipModels.add(CARRIER);
        shipModels.add(BATTLESHIP);
        shipModels.add(DESTROYER);
        shipModels.add(SUBMARINE);
        shipModels.add(PATROL_BOAT);
    }

    public Set<ShipModel> getShipModels() {
        return shipModels;
    }

    public ShipModel getShipByName(String name) {
        if (name == null) {throw new RuntimeException("Name cannot be null");}
        return shipModels.stream()
                .filter(shipModel -> shipModel.getName().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Wrong ship name"));
    }

}
