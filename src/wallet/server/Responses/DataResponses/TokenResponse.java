package wallet.server.Responses.DataResponses;

import wallet.server.Responses.BaseResponse;

public class TokenResponse extends BaseResponse {

    private boolean status;
    private String token = "";

    public TokenResponse(){
        super(200, "succes");
    }

    public boolean isStatus() {
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
