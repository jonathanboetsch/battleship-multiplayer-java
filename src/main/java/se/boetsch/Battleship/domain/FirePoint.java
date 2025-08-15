package se.boetsch.Battleship.domain;

import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipModel;

import java.util.Optional;

public class FirePoint {

    private ShipCoordinates coordinates;
    private FireResult fireResult;
    private Optional<ShipModel> sunkShip;

    public FirePoint(ShipCoordinates coordinates, FireResult fireResult) {
        this.coordinates = coordinates;
        this.fireResult = fireResult;
        this.sunkShip = Optional.empty();
    }

    public FirePoint(ShipCoordinates coordinates, FireResult fireResult, ShipModel sunkShip) {
        this(coordinates, fireResult);
        this.sunkShip = Optional.of(sunkShip);
    }

    public FirePoint() {
    }

    public ShipCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ShipCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public FireResult getFireResult() {
        return fireResult;
    }

    public void setFireResult(FireResult fireResult) {
        this.fireResult = fireResult;
    }

    public Optional<ShipModel> getSunkShip() {
        return sunkShip;
    }

    public void setSunkShip(Optional<ShipModel> sunkShip) {
        this.sunkShip = sunkShip;
    }
}
