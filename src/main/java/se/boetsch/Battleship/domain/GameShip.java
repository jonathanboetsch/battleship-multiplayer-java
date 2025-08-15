package se.boetsch.Battleship.domain;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.entity.Player;
import se.boetsch.Battleship.entity.ShipWithPlacement;

@Service
public class PlacedShip {

    private ShipWithPlacement shipWithPlacement;
    private Player player;
    private boolean isAlive = true;
    private int lifes;

    public PlacedShip(ShipWithPlacement shipWithPlacement, Player player) {
        this.shipWithPlacement = shipWithPlacement;
        this.player = player;
        this.lifes = this.shipWithPlacement.getShip().getSize();
    }

    public ShipWithPlacement getShipWithPlacement() {
        return shipWithPlacement;
    }

    public void setShipWithPlacement(ShipWithPlacement shipWithPlacement) {
        this.shipWithPlacement = shipWithPlacement;
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

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public void takeOneLife() {
        if (this.lifes > 0) {
            this.lifes -= 1;
        } else {
            throw new RuntimeException("Ship already sunk");
        }
    }

    // TODO: add sessionUUID;

}
