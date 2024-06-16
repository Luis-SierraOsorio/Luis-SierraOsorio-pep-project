package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {
    
    /**
     * method to insert user account
     * 
     * @param author object with all the required fields
     * @return Account object or null if insertion fails
     */
    public Account insertAccount(Account user){

        // getting connection
        Connection connection = ConnectionUtil.getConnection();

        // checking for conditions specified
        if (!user.getUsername().isBlank() && user.getPassword().length() >= 4 && getAccountById(user.getAccount_id()) != null ){

            // block of code attempts to insert object
            try{
                // sql query
                String sql = "INSERT INTO account (username, password) VALUES(?,?);";
                // creating prepared statement and auto inserting the primary key
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
                // injecting the required values
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
    
                // executing the sql query
                preparedStatement.executeUpdate();
    
                // getting the keys just generated for the object.
                ResultSet results = preparedStatement.getGeneratedKeys();
    
                // block gets newly generated key and returns new Account object with proper fields
                if (results.next()){
                   int generatedPrimaryKey = (int)results.getLong(1); 
                   return new Account(generatedPrimaryKey, user.getUsername(), user.getPassword());
                }
    
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        
        // null returned if operation fails
        return null;
    }

    /**
     * method to get a user by id 
     * 
     * @param account_id account id (primary key)
     * @return account or null if user is not found
     */
    public Account getAccountById(int account_id){
        Connection connection = ConnectionUtil.getConnection();

        // try/catch block to get user by id
        try {

            // sql logic to get user
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // setting the preparedStatement params
            preparedStatement.setInt(1, account_id);

            // getting the results from executing the query
            ResultSet results = preparedStatement.executeQuery();

            // iterating through results, creating accoutn object with fields and returing Account object
            while(results.next()){
                Account account = new Account(results.getInt("account_id"), results.getString("username"), results.getString("password"));
                return account;
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        //null gets returned if Account is not found from the query 
        return null;
    }

}
