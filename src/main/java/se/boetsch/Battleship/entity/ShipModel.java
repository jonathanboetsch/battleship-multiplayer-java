package se.boetsch.Battleship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class ShipModel implements Serializable {

    private String name;

    @JsonIgnore
    private int size;

    public ShipModel(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public ShipModel() {
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setName(String name) {
        this.name = name;
    }
}
