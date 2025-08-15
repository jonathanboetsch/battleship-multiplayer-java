package se.boetsch.Battleship.entity;

import java.io.Serializable;

public class ShipCoordinates implements Serializable {

    int x;

    int y;

    public ShipCoordinates() {
    }

    public ShipCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ShipCoordinates{" +
                "horizontalPos=" + x +
                ", verticalPos=" + y +
                '}';
    }
}
