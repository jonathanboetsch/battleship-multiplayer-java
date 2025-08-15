package se.boetsch.Battleship.service;

import jakarta.persistence.*;
import se.boetsch.Battleship.domain.GameShip;
import se.boetsch.Battleship.entity.Player;
import se.boetsch.Battleship.entity.ShipCoordinates;


@Entity
public class BattleMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    Player player;

    // map is set at [x][y] if there is a boat placed at the coordinates x,y;
    // otherwise it is null
    // allows to check quickly for conflicting placement as well as determine if a player has fired at a ship
    private GameShip[][] map;

    public BattleMap() {
        map = new GameShip[10][10];
    }

    public GameShip[][] getMap() {
        return map;
    }

    public void setMap(GameShip[][] map) {
        this.map = map;
    }

    public void setShipAtCoordinates(GameShip ship, int x, int y) {
        map[x][y] = ship;
    }

    public void setShipAtCoordinates(GameShip ship, ShipCoordinates coordinates) {
        try {
            map[coordinates.getX()][coordinates.getY()] = ship;
        } catch (Exception e) {
            throw new RuntimeException("Error at ship placement inside the BattleMap instance");
        }
    }
}
