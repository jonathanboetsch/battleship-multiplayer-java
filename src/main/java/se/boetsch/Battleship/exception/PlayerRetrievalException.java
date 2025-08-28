package se.boetsch.Battleship.exception;

public class PlayerRetrievalException extends RuntimeException {
    public PlayerRetrievalException(String name, Throwable cause) {
        super("Failed to fetch player with name: " + name, cause);
    }
    public PlayerRetrievalException(long id) {
        super("Failed to fetch player with id: " + id);
    }
}
