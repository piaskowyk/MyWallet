package wallet.server.exceptions;

public class InvalidInputDataException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Input data is not correct";
    }
}