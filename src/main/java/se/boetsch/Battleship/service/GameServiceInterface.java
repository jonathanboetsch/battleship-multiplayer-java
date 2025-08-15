package se.boetsch.Battleship.service;

import se.boetsch.Battleship.domain.CurrentPlayer;

import java.util.Optional;

public interface GameServiceInterface {

    // Assumes that a Game instance is set in the implementation

    void startNewGame();

    void addPlayer(String name);

    void checkIfPlayersPresent();

    void setSessionId(Long sessionId);

    Optional<Long> getSessionId();

    void incrementNbOfTurnsForPlayer(CurrentPlayer player);



}
