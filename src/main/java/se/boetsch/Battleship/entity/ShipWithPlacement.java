package se.boetsch.Battleship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ShipPlacement implements Serializable {

    @JsonIgnore
    private ShipModel shipModel;

    private ShipCoordinates startPosition;

    @Enumerated(EnumType.STRING)
    private ShipOrientation orientation;

    public ShipPlacement(ShipModel shipModel, ShipCoordinates startPosition, ShipOrientation orientation) {
        this.shipModel = shipModel;
        this.startPosition = startPosition;
        this.orientation = orientation;
        int addHorizontal = orientation.equals(ShipOrientation.HORIZONTAL) ? shipModel.getSize() : 0;
        int addVertical = orientation.equals(ShipOrientation.VERTICAL) ? shipModel.getSize() : 0;
    }

    public ShipModel getShip() {
        return shipModel;
    }

    public void setShip(ShipModel shipModel) {
        try {
            this.shipModel = shipModel;
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
}
