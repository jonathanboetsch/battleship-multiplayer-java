package se.boetsch.Battleship.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipOrientation;

import java.io.Serializable;

public record ShipWithPlacementDTO(
        @JsonIgnore ShipToPlaceDTO ship,
        String name,
        @NotEmpty
        ShipCoordinates startPosition,
        @NotEmpty
        ShipOrientation orientation) implements Serializable {
    public ShipWithPlacementDTO(ShipToPlaceDTO ship,
                                ShipCoordinates startPosition,
                                ShipOrientation orientation) {
        this(ship, ship != null ? ship.name() : null, startPosition, orientation);
    }

    // Overload without ship if you only want to pass name + coords + orientation
    public ShipWithPlacementDTO(String name,
                                ShipCoordinates startPosition,
                                ShipOrientation orientation) {
        this(null, name, startPosition, orientation);
    }
}
