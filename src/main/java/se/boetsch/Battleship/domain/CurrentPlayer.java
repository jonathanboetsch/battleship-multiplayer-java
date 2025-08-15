package se.boetsch.Battleship.domain;

import se.boetsch.Battleship.entity.Player;

import java.util.Set;

public class CurrentPlayer {

    private String name;

    private Player player;

    private Set<GameShip> ships;

    public CurrentPlayer(Player player) {
        this.player = player;
        this.name = player.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<GameShip> getShips() {
        return ships;
    }

    public void setShips(Set<GameShip> ships) {
        this.ships = ships;
    }

    public String getName() {
        return this.player.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

}
