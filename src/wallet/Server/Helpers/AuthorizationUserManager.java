package wallet.Server.Helpers;

import wallet.CommonEntities.Entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorizationUserManager {

    public static User isLogged(DataBase db, String token){
        User user = new User();

        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(token);

        ResultSet dbResult;
        try {
            dbResult = db.querySelect("select * from users where token = ?", arguments);

            if(dbResult.next()){
                user.setId(dbResult.getInt("id"));
                user.setName(dbResult.getString("name"));
                user.setSurname(dbResult.getString("surname"));
                user.setEmail(dbResult.getString("email"));
                user.setToken(dbResult.getString("token"));
                user.setExpired_date(dbResult.getString("expired_date"));
            } else {
                System.out.println("user not found");
                user = null;
            }

        } catch (SQLException e) {
            return null;
        }

        return user;
    }

}
