package se.boetsch.Battleship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import se.boetsch.Battleship.domain.PlacedShip;
import se.boetsch.Battleship.service.BattleMap;

import java.util.List;
import java.util.Set;

@Entity
public class Player {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String name;

    private long score;

    private int numberOfWonGames;

}
