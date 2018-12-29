package wallet.Server.Controllers;

import java.util.HashMap;

public abstract class Controller {

    private HashMap<String, String> headers;

    public abstract void setHeaders(HashMap<String, String> headers);

}
