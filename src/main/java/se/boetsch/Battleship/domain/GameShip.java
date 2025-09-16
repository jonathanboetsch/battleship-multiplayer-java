package se.boetsch.Battleship.domain;

import lombok.Getter;
import lombok.Setter;
import se.boetsch.Battleship.exception.FiringException;

@Getter
@Setter
public class GameShip {

    private ShipWithPlacement shipWithPlacement;
    private boolean isAlive = true;
    private int lives;

    public GameShip(ShipWithPlacement shipWithPlacement) {
        this.shipWithPlacement = shipWithPlacement;
        this.lives = this.shipWithPlacement.getShip().getSize();
    }

    public void takeOneLife() {
        if (this.lives > 0) {
            this.lives -= 1;
            if (lives == 0) setAlive(false);
        } else {
            throw new FiringException("Ship already sunk");
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
