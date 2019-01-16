package wallet.server.exceptions;

public class ControllerNotExistException extends RuntimeException {
    @Override
    public String getMessage() {
        return "This controller not exist.";
    }
}