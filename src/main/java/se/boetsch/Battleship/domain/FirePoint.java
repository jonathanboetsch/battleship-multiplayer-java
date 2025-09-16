package se.boetsch.Battleship.domain;

import se.boetsch.Battleship.entity.ShipCoordinates;

import java.util.Optional;

public class FirePoint {

    private ShipCoordinates coordinates;
    private FireStatus fireStatus;
    private Optional<ShipModel> sunkShip;

    public FirePoint(ShipCoordinates coordinates, FireStatus fireStatus) {
        this.coordinates = coordinates;
        this.fireStatus = fireStatus;
        this.sunkShip = Optional.empty();
    }

    public FirePoint(ShipCoordinates coordinates, FireStatus fireStatus, ShipModel sunkShip) {
        this(coordinates, fireStatus);
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

    public FireStatus getFireResult() {
        return fireStatus;
    }

    public void setFireResult(FireStatus fireStatus) {
        this.fireStatus = fireStatus;
    }

    public Optional<ShipModel> getSunkShip() {
        return sunkShip;
    }

    public void setSunkShip(Optional<ShipModel> sunkShip) {
        this.sunkShip = sunkShip;
    }
}
