package se.boetsch.Battleship.service;

import jakarta.persistence.*;
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
    private int[][] map;

    public BattleMap() {
        map = new int[10][10];
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public void setCoordinatesActive(int x, int y) {
        map[x][y] = 1;
    }

    public void setCoordinatesActive(ShipCoordinates coordinates) {
        map[coordinates.getHorizontalPos()][coordinates.getVerticalPos()] = 1;
    }
}
