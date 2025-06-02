package se.boetsch.Battleship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class ShipPlacement implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private ShipModel shipModel;

    private ShipCoordinates startPosition;

    @JsonIgnore
    private ShipCoordinates endPosition;

    @Enumerated(EnumType.STRING)
    private ShipOrientation orientation;

    public ShipPlacement(ShipModel shipModel, ShipCoordinates startPosition, ShipOrientation orientation) {
        this.shipModel = shipModel;
        this.startPosition = startPosition;
        this.orientation = orientation;
        int addHorizontal = orientation.equals(ShipOrientation.HORIZONTAL) ? shipModel.getSize() : 0;
        int addVertical = orientation.equals(ShipOrientation.VERTICAL) ? shipModel.getSize() : 0;
        this.endPosition = new ShipCoordinates(
                startPosition.getHorizontalPos() + addHorizontal,
                startPosition.getVerticalPos() + addVertical
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ShipCoordinates getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(ShipCoordinates endPosition) {
        this.endPosition = endPosition;
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
