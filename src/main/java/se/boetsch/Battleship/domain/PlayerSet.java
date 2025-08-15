package se.boetsch.Battleship.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.service.BattleMap;
import se.boetsch.Battleship.service.ShipService;

import java.util.HashSet;
import java.util.Set;

public class PlayerSet {

    private static final ShipService shipService = new ShipService();
    private SessionId sessionId;
    private Set<GameShip> ships;
    private Set<FirePoint> fires;
    private boolean isShipsPlacementComplete;
    @JsonIgnore
    private BattleMap battleMap;

    public PlayerSet(SessionId sessionId) {
        this.fires = new HashSet<>();
        this.ships = new HashSet<>();
        this.sessionId = sessionId;
        this.battleMap = new BattleMap();
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

    public boolean isShipsPlacementComplete() {
        return isShipsPlacementComplete;
    }

    public void setShipsPlacementComplete(boolean shipsPlacementComplete) {
        isShipsPlacementComplete = shipsPlacementComplete;
    }

    public BattleMap getBattleMap() {
        try {
            return battleMap;
        } catch (Exception e) {
            throw new RuntimeException("Error when fetching the battle map");
        }
    }

    public void setBattleMap(BattleMap battleMap) {
        this.battleMap = battleMap;
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public void setSessionId(SessionId sessionId) {
        this.sessionId = sessionId;
    }

    public Set<GameShip> getShips() {
        return ships;
    }

    public void setShips(Set<GameShip> ships) {
        this.ships = ships;
    }

    public Set<FirePoint> getFires() {
        return fires;
    }

    public void setFires(Set<FirePoint> fires) {
        checkNumberOfFires(fires.size());
        this.fires = fires;

    }

    public Set<GameShip> getPlacedShips() {
        return this.ships;
    }

    public void setPlacedShips(Set<GameShip> placedShips) {
        checkNumberOfShips(placedShips.size());
        this.ships = placedShips;
    }

    public Set<FirePoint> getFiresForPlayer(CurrentPlayer player) {
        return this.fires;
    }

    public void addPlacedShip(GameShip ship) {
        checkNumberOfShips(ships.size() + 1);
        ships.add(ship);
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


}
