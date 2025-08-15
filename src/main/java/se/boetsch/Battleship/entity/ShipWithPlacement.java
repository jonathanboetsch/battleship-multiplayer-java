package se.boetsch.Battleship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class ShipWithPlacement implements Serializable {

    @JsonIgnore
    private ShipModel ship;

    private String name;

    private ShipCoordinates startPosition;

    private ShipOrientation orientation;

    public ShipWithPlacement() {
    }

    public ShipWithPlacement(ShipModel ship, ShipCoordinates startPosition, ShipOrientation orientation) {
        this.ship = ship;
        this.name = ship.getName();
        this.startPosition = startPosition;
        this.orientation = orientation;
    }

    public ShipWithPlacement(String name, ShipCoordinates startPosition, ShipOrientation orientation) {
        this.name = name;
        this.startPosition = startPosition;
        this.orientation = orientation;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShipModel getShip() {
        return ship;
    }

    public void setShip(ShipModel shipModel) {
        try {
            this.ship = shipModel;
        } catch (Exception e) {
            throw new RuntimeException("Problem with ship instantiation");
        }
    }

    public ShipCoordinates getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(ShipCoordinates startPosition) {
        this.startPosition = startPosition;
    }

    public ShipOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(ShipOrientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return "ShipWithPlacement{" +
                "shipModel=" + ship +
                ", startPosition=" + startPosition +
                ", orientation=" + orientation +
                '}';
    }
}
