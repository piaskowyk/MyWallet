package wallet.app.Exceptions;

public class NoImplementsInterfaceException extends RuntimeException {
    @Override
    public String getMessage() {
        return "This controller is not instance of IViewController";
    }
}
