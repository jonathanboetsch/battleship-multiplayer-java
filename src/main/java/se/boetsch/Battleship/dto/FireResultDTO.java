package se.boetsch.Battleship.dto;

import se.boetsch.Battleship.domain.FireStatus;
import se.boetsch.Battleship.entity.ShipCoordinates;

public record FireResultDTO(FireStatus status, CoordinatesDTO coordinates) {
}
