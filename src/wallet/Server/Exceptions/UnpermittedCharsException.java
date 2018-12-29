package wallet.Server.Exceptions;

public class UnpermittedCharsException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Unpermitted chars in url request";
    }
}
