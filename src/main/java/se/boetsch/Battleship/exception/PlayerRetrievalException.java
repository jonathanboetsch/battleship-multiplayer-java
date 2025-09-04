package se.boetsch.Battleship.exception;

public class PlayerRetrievalException extends RuntimeException {
    public PlayerRetrievalException(String name) {
        super("Failed to fetch player with name: " + name);
    }
    public PlayerRetrievalException(long id) {
        super("Failed to fetch player with id: " + id);
    }
}
