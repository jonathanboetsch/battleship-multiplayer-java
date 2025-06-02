package se.boetsch.Battleship.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Player player;

    // TODO: add sessionUUID;

}
