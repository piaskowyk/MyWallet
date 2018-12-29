package wallet.Server.Exceptions;

public class ControllerNotExistException extends RuntimeException {
    @Override
    public String getMessage() {
        return "This controller not exist.";
    }
}