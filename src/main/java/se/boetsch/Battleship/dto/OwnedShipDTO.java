package se.boetsch.Battleship.dto;

import se.boetsch.Battleship.entity.ShipCoordinates;
import se.boetsch.Battleship.entity.ShipOrientation;

/**
 * Data Transfer Object representing a ship owned by a player in a game.
 * <p>
 * This DTO is used to expose the state of a ship to clients without leaking
 * internal domain objects. It is typically produced by {@link se.boetsch.Battleship.service.GameShipMapper}
 * when converting from a {@code GameShip} entity.
 * </p>
 *
 * @param name          the type/name of the ship (e.g. "Battleship")
 * @param startPosition the starting coordinates of the ship on the board
 * @param orientation   the orientation of the ship (horizontal/vertical)
 * @param lives         the number of remaining hit points for this ship
 */
public record OwnedShipDTO(
        String name,
        ShipCoordinates startPosition,
        ShipOrientation orientation,
        int lives
) {
}
