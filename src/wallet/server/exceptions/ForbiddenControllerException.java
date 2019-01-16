package wallet.server.exceptions;

public class ForbiddenControllerException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Called forbidden controller class";
    }
}