package wallet.server.Exceptions;

public class ForbiddenControllerException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Called forbidden controller class";
    }
}