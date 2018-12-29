package wallet.Server.Exceptions;

public class RequestDoubleExecutedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "You try execute executed request.";
    }
}
