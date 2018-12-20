package wallet.server.Exceptions;

public class InvalidInputDataException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Input data is not correct";
    }
}