package se.boetsch.Battleship.exception;

public class PlayerRetrievalException extends WrongInputException {
    public PlayerRetrievalException(String name) {
        super("Failed to fetch player with name: " + name);
    }
}
