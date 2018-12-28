package wallet.app.Exceptions;

public class UnauthorizationRequestException extends Exception{
    @Override
    public String getMessage() {
        return "Unauthorized request.";
    }
}
