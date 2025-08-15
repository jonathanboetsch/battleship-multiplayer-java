package se.boetsch.Battleship.domain;

import se.boetsch.Battleship.entity.ShipWithPlacement;

public class GameShip {

    private ShipWithPlacement shipWithPlacement;
    private boolean isAlive = true;
    private int lives;

    public GameShip(ShipWithPlacement shipWithPlacement) {
        this.shipWithPlacement = shipWithPlacement;
        this.lives = this.shipWithPlacement.getShip().getSize();
    }

    public ShipWithPlacement getShipWithPlacement() {
        return shipWithPlacement;
    }

    public void setShipWithPlacement(ShipWithPlacement shipWithPlacement) {
        this.shipWithPlacement = shipWithPlacement;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void takeOneLife() {
        if (this.lives > 0) {
            this.lives -= 1;
            if (lives == 0) setAlive(false);
        } else {
            throw new RuntimeException("Ship already sunk");
        }
    }

    public String getName() {
        return this.shipWithPlacement.getShip().getName();
    }

    @Override
    public String toString() {
        return "GameShip{" +
                "shipWithPlacement=" + shipWithPlacement +
                '}';
    }
}
