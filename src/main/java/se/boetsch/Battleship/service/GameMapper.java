package se.boetsch.Battleship.service;

import org.springframework.stereotype.Component;
import se.boetsch.Battleship.domain.Game;
import se.boetsch.Battleship.dto.GameDTO;
import se.boetsch.Battleship.dto.GameStatusDTO;

@Component
public class GameMapper {

    private final PlayerMapper playerMapper;
    private final PlayerService playerService;
    private final CurrentPlayerService currentPlayerService;

    public GameMapper(PlayerMapper playerMapper, PlayerService playerService, CurrentPlayerService currentPlayerService) {
        this.playerMapper = playerMapper;
        this.playerService = playerService;
        this.currentPlayerService = currentPlayerService;
    }

    public GameDTO toDTO(Game g) {
        return new GameDTO(
                g.getSessionId().getId(),
                g.getStatus(),
                g.getPlayerIds().stream().map(currentPlayerService::findById).map(p -> playerMapper.toDTO(p.get())).toList()
        );

    }

    public GameStatusDTO toSummary(Game g) {
        return new GameStatusDTO(
                g.getSessionId().getId(),
                g.getStatus()
        );
    }
}
