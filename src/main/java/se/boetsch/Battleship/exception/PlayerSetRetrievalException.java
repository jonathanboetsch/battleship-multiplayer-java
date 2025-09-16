package se.boetsch.Battleship.exception;

public class PlayerSetRetrievalException extends WrongInputException {
    public PlayerSetRetrievalException(long id) {
        super("Failed to retrieve player set with id: " + id);
    }
}
