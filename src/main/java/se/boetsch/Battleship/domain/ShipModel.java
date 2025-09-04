package se.boetsch.Battleship.domain;

import lombok.Getter;

@Getter
public class ShipModel {
    private final String name;
    private final int size;

    public ShipModel(String name, int size) {
        this.name = name;
        this.size = size;
    }

}
