package se.boetsch.Battleship.service;

import org.springframework.stereotype.Component;
import se.boetsch.Battleship.domain.CurrentPlayer;
import se.boetsch.Battleship.dto.PlayerDTO;

@Component
public class PlayerMapper {
    public PlayerDTO toDTO(CurrentPlayer player) {
        return new PlayerDTO(player.getName(), player.getPlayer().getId());
    }
}
