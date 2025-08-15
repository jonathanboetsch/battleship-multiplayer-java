package se.boetsch.Battleship.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import se.boetsch.Battleship.entity.Player;
import se.boetsch.Battleship.entity.ShipCoordinates;

import java.util.HashSet;
import java.util.Set;

public class PlayerGame {

    private final CurrentPlayer player;

    private final Set<GameShip> ships;

    @JsonIgnore
    private final Set<ShipCoordinates> fires;
    private SessionId sessionId;

    public PlayerGame(Game game, CurrentPlayer player) {
        this.player = player;

        if (game.getPlacedShips().containsKey(player)) this.ships = game.getPlacedShips().get(player);
        else this.ships = new HashSet<>();

        if (game.getFiresForPlayer(player).isEmpty()) this.fires = new HashSet<>();
        else this.fires = game.getFiresForPlayer(player);

    }

    public CurrentPlayer getPlayer() {
        return player;
    }

    public Set<GameShip> getShips() {
        return ships;
    }

    public Set<ShipCoordinates> getFires() {
        return fires;
    }

    public SessionId getSessionId() {
        return sessionId;
    }
}
