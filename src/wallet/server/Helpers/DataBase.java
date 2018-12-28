package wallet.server.Helpers;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    private Connection connection;

    public DataBase(){
        try{
            String dbName = "wallet";
            String dbUser = "root";
            String dbPassword = "";
            String dbHost = "localhost";

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":3306/" + dbName + "?useUnicode=true&characterEncoding=utf-8", dbUser, dbPassword);
        }
        catch(Exception e)
        { System.out.println(e);}
    }

    public ResultSet querySelect(String query, ArrayList<Object> parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        int i = 1;
        for(Object item : parameters){

            if(item instanceof String){
                statement.setString(i, (String) item);
            }
            else if (item instanceof Integer){
                statement.setInt(i, (Integer) item);
            }
            else if (item instanceof java.sql.Date){
                statement.setDate(i, (java.sql.Date) item);
            }
            else {
                String str = item.toString();
                statement.setString(i, str);
            }
            i++;
        }
        return statement.executeQuery();
    }

    public int queryUpdate(String query, ArrayList<Object> parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        int i = 1;
        for(Object item : parameters){

            if(item instanceof String){
                statement.setString(i, (String) item);
            }
            else if (item instanceof Integer){
                statement.setInt(i, (Integer) item);
            }
            else if (item instanceof java.sql.Date){
                statement.setDate(i, (java.sql.Date) item);
            }
            else {
                String str = item.toString();
                statement.setString(i, str);
            }
            i++;
        }
        System.out.println(statement.toString());
        return statement.executeUpdate();
    }

    public boolean exist(ResultSet set){
        boolean exist = false;
        try {
            if(set.next()){
                exist = true;
            } else {
                exist = false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return exist;
    }

    public void closeConnection() {
        try {
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
