package wallet.server.Exceptions;

public class ActionNotExistException extends RuntimeException {
    @Override
    public String getMessage() {
        return "This action not exist.";
    }
}