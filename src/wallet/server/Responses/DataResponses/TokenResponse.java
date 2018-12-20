package wallet.server.Responses.DataResponses;

import wallet.server.Responses.BaseResponse;

public class TokenResponse extends BaseResponse {

    private boolean result;
    private String token = "";

    public TokenResponse(){
        super(200, "succes");
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
