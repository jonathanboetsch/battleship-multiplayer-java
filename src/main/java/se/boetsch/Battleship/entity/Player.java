package se.boetsch.Battleship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Player {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(unique = true)
    private String name;

    private long score;
    private int numberOfWonGames;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getNumberOfWonGames() {
        return numberOfWonGames;
    }

    public void setNumberOfWonGames(int numberOfWonGames) {
        this.numberOfWonGames = numberOfWonGames;
    }

}
