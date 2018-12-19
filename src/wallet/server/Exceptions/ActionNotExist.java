package wallet.server.Exceptions;

public class ActionNotExist extends RuntimeException {
    @Override
    public String getMessage() {
        return "This action not exist.";
    }
}