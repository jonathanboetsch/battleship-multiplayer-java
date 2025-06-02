package se.boetsch.Battleship.domain;

import jakarta.persistence.*;
import se.boetsch.Battleship.entity.Player;
import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipModel;
import se.boetsch.Battleship.entity.ShipOrientation;

@Entity
public class PlacedShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private ShipModel shipModel;
    private ShipCoordinates shipCoordinates;
    private ShipOrientation orientation;
    @ManyToMany
    @JoinTable(name = "ships_player")
    private Player player;
    private boolean isAlive = true;

    public PlacedShip(ShipModel shipModel, ShipCoordinates shipCoordinates, ShipOrientation orientation, Player player) {
        this.shipModel = shipModel;
        this.shipCoordinates = shipCoordinates;
        this.orientation = orientation;
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public ShipModel getShipModel() {
        return shipModel;
    }

    public void setShipModel(ShipModel shipModel) {
        this.shipModel = shipModel;
    }

    public ShipCoordinates getShipCoordinates() {
        return shipCoordinates;
    }

    public void setShipCoordinates(ShipCoordinates shipCoordinates) {
        this.shipCoordinates = shipCoordinates;
    }

    public ShipOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(ShipOrientation orientation) {
        this.orientation = orientation;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    // TODO: add sessionUUID;

}
