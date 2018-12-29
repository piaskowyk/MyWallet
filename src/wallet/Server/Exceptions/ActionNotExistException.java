package wallet.Server.Exceptions;

public class ActionNotExistException extends RuntimeException {
    @Override
    public String getMessage() {
        return "This action not exist.";
    }
}