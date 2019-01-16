package wallet.server.exceptions;

public class UnpermittedCharsException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Unpermitted chars in url request";
    }
}
