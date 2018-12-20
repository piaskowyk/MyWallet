package wallet.server.Responses;

public class BaseResponse {
    private final int code;
    private final String message;

    protected BaseResponse(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
