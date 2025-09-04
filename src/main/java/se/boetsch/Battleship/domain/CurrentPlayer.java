package se.boetsch.Battleship.domain;

import lombok.Getter;
import lombok.Setter;
import se.boetsch.Battleship.entity.Player;

@Setter
@Getter
public class CurrentPlayer {

    private Player player;

    public CurrentPlayer(Player player) {
        this.player = player;
    }

    public String getName() {
        return this.player.getName();
    }


}
