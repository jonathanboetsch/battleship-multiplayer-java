package se.boetsch.Battleship.dto;

import jakarta.validation.constraints.NotEmpty;

public record PlayerDTO (@NotEmpty String name, long id) {
}
