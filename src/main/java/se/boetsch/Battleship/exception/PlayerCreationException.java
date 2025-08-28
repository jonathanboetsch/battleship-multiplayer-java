package se.boetsch.Battleship.exception;

public class PlayerCreationException extends RuntimeException {
    public PlayerCreationException(String name, Throwable cause) {
        super("Failed to create player with name: " + name, cause);
    }
}
