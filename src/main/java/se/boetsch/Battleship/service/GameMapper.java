package se.boetsch.Battleship.service;

import org.springframework.stereotype.Component;
import se.boetsch.Battleship.domain.Game;
import se.boetsch.Battleship.dto.GameDTO;
import se.boetsch.Battleship.dto.GameStatusDTO;

@Component
public class GameMapper {

    private final PlayerMapper playerMapper;

    public GameMapper(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    public GameDTO toDTO(Game g) {
        return new GameDTO(
                g.getSessionId().getId(),
                g.getStatus(),
                g.getPlayers().stream().map(playerMapper::toDTO).toList()
        );

    }

    public GameStatusDTO toSummary(Game g) {
        return new GameStatusDTO(
                g.getSessionId().getId(),
                g.getStatus()
        );
    }
}
