package wallet.Server.Exceptions;

public class InvalidInputDataException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Input data is not correct";
    }
}