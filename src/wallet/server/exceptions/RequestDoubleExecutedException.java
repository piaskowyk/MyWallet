package wallet.server.exceptions;

public class RequestDoubleExecutedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "You try execute executed request.";
    }
}
