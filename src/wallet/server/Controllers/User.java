package wallet.server.Controllers;

import wallet.server.Controller;
import wallet.server.Tmp;

public class User implements Controller {

    public Tmp indexAction(){
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        return new Tmp(4, "xd");
    }

}
