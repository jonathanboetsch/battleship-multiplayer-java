package se.boetsch.Battleship.dto;

import java.io.Serializable;

public record ShipToPlaceDTO(String name, int size) implements Serializable {

}
