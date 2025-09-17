package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.GameShip;
import se.boetsch.Battleship.dto.OwnedShipDTO;

/**
 * Mapper component responsible for converting {@link GameShip} domain entities
 * into Data Transfer Objects (DTOs).
 * <p>
 * This class isolates mapping logic so that domain models do not leak into
 * external layers (e.g. REST controllers). By centralizing conversions here,
 * it also keeps DTO creation consistent and easier to maintain.
 * </p>
 *
 * Typical usage:
 * <pre>{@code OwnedShipDTO dto = gameShipMapper.toOwnedShipDTO(gameShip);}</pre>
 */
@Service
public class GameShipMapper {
    public OwnedShipDTO toOwnedShipDTO(GameShip s) {
        return new OwnedShipDTO(s.getName(), s
                .getShipWithPlacement()
                .getStartPosition(), s
                .getShipWithPlacement()
                .getOrientation(), s.getLives());
    }
}
