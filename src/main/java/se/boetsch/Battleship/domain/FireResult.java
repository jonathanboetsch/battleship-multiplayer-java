package se.boetsch.Battleship.domain;

import se.boetsch.Battleship.entity.ShipCoordinates;

public record FireResult(FireStatus status, ShipCoordinates coordinates) {
}
