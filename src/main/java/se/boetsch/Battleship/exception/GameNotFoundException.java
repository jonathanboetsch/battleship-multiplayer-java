package se.boetsch.Battleship.exception;

public class GameNotFoundException extends GameException {
    public GameNotFoundException(long gameId) {
        super("Game with id " + gameId + " not found");
    }
}
