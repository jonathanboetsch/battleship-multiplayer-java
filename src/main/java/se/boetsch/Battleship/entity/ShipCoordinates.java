package se.boetsch.Battleship.entity;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ShipCoordinates implements Serializable {

    int horizontalPos;

    int verticalPos;

    public ShipCoordinates(int horizontalPos, int verticalPos) {
        this.horizontalPos = horizontalPos;
        this.verticalPos = verticalPos;
    }

    public int getHorizontalPos() {
        return horizontalPos;
    }

    public void setHorizontalPos(int horizontalPos) {
        this.horizontalPos = horizontalPos;
    }

    public int getVerticalPos() {
        return verticalPos;
    }

    public void setVerticalPos(int verticalPos) {
        this.verticalPos = verticalPos;
    }
}
