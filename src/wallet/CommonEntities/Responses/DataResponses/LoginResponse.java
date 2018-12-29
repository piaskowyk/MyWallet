package wallet.CommonEntities.Responses.DataResponses;

import wallet.CommonEntities.Responses.BaseResponse;

public class LoginResponse extends BaseResponse {

    private boolean status;
    private String token = "";

    public LoginResponse(){
        super(200, "success");
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
