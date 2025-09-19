package se.boetsch.Battleship.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipOrientation;

import java.io.Serializable;

@Getter
@Setter
public class ShipWithPlacement implements Serializable {

    @JsonIgnore
    private ShipModel ship;

    @NotEmpty
    private String name;

    @NotEmpty
    private ShipCoordinates startPosition;

    @NotEmpty
    private ShipOrientation orientation;

    public ShipWithPlacement() {
    }

    public ShipWithPlacement(ShipModel ship, ShipCoordinates startPosition, ShipOrientation orientation) {
        this.ship = ship;
        this.name = ship.getName();
        this.startPosition = startPosition;
        this.orientation = orientation;
    }

    public void setShip(ShipModel shipModel) {
        try {
            this.ship = shipModel;
        } catch (Exception e) {
            throw new RuntimeException("Problem with ship instantiation");
        }
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