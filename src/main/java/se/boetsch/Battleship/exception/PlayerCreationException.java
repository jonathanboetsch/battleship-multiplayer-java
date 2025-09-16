package se.boetsch.Battleship.exception;

public class PlayerCreationException extends RuntimeException {
    public PlayerCreationException(String name) {
        super("Failed to create player with name: " + name);
    }
}
