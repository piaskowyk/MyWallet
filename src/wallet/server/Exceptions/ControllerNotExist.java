package wallet.server.Exceptions;

public class ControllerNotExist extends RuntimeException {
    @Override
    public String getMessage() {
        return "This controller not exist.";
    }
}