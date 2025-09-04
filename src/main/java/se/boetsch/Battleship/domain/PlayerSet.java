package se.boetsch.Battleship.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import se.boetsch.Battleship.dto.ShipToPlaceDTO;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.service.BattleMap;
import se.boetsch.Battleship.service.ShipService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerSet {

    private static final ShipService shipService = new ShipService();
    @Setter
    @Getter
    private SessionId sessionId;
    @Setter
    @Getter
    private Set<GameShip> placedShips;
    private Set<FirePoint> fires;
    @Getter
    @Setter
    private boolean isShipsPlacementComplete;
    @Setter
    @JsonIgnore
    private BattleMap battleMap;

    public PlayerSet(SessionId sessionId) {
        this.fires = new HashSet<>();
        this.placedShips = new HashSet<>();
        this.sessionId = sessionId;
        this.battleMap = new BattleMap();
        isShipsPlacementComplete = false;
    }
    public PlayerSet() {
    }

    private static void checkNumberOfFires(int nbOfFires) {
        if (nbOfFires > 100) throw new RuntimeException("Too many fires: cannot be more than 10x10");
    }

    private static void checkNumberOfShips(int numberOfShips) {
        int numberOfModelShips = shipService.getShipModels().size();
        if (numberOfShips > numberOfModelShips)
            throw new RuntimeException("Too many ships, should be no more than " + numberOfModelShips);
    }

    public static int getNumberOfPlacedShips(PlayerSet playerSet) {
        return playerSet.getPlacedShips().size();
    }

    public BattleMap getBattleMap() {
        try {
            return battleMap;
        } catch (Exception e) {
            throw new RuntimeException("Error when fetching the battle map");
        }
    }

    public Set<FirePoint> getFiresForPlayer(CurrentPlayer player) {
        return this.fires;
    }

    public void addPlacedShip(GameShip ship) {
        checkNumberOfShips(placedShips.size() + 1);
        placedShips.add(ship);
    }

    public void registerFireAt(ShipCoordinates coordinates, FireResult result) {
        FirePoint firePoint = null;
        try {
            firePoint = new FirePoint(coordinates, result);
        } catch (Exception e) {
            throw new RuntimeException("Error when registering hit or miss");
        }
        if (fires.contains(firePoint)) throw new RuntimeException("Coordinates already fired");
        this.fires.add(firePoint);
    }


    public List<ShipToPlaceDTO> getRemainingShipModelsToPlace() {
        return ShipService
                .getShipModels().stream()
                .filter(model -> getPlacedShips().stream()
                        .noneMatch(m -> m.getName().equalsIgnoreCase(model.getName())))
                .map(model -> new ShipToPlaceDTO(model.getName(), model.getSize()))
                .toList();
    }
}
