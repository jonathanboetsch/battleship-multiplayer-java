package se.boetsch.Battleship.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CoordinatesDTO(@NotNull @Min(0) int x, @NotNull @Min(0) int y) {
}
