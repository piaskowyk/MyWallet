package wallet.server.untils;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    /*
    * Klasa odpowiedzialna jest za połączenie się z bazą danych oraz udostępnia metody dzięki którymi można wyonywać
    * operacje na bazie danych
    * */

    private Connection connection;

    public DataBase(){
        try{
            //konfiguracja połączenia do bazy danych
            String dbName = "wallet";
            String dbUser = "root";
            String dbPassword = "";
            String dbHost = "localhost";

            //łączy się z bazą MySQL
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":3306/" + dbName + "?useUnicode=true&characterEncoding=utf-8", dbUser, dbPassword);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
        System.out.println(statement.toString());
        return statement.executeQuery();
    }

    public void queryUpdate(String query, ArrayList<Object> parameters) throws SQLException {
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

        statement.executeUpdate();
    }

    public boolean exist(ResultSet set){
        boolean exist = false;
        try {
            if(set.next()){
                exist = true;
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
