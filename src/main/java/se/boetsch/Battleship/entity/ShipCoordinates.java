package se.boetsch.Battleship.entity;

import jakarta.validation.constraints.Min;

import java.io.Serializable;

public class ShipCoordinates implements Serializable {

    @Min(0)
    int x;

    @Min(0)
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
                "X=" + x +
                ", Y=" + y +
                '}';
    }
}
