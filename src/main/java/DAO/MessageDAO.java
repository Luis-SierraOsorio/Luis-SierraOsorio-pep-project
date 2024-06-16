package DAO;

import Model.Message;

import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /**
     * function to check if user exist
     * 
     * @param user_id
     * @return true/false
     */
    public Boolean userExists(int user_id){
        try{
            // connection
            Connection connection = ConnectionUtil.getConnection();

            // sql string
            String sql = "SELECT * FROM user WHERE user_id = ?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // injecting values in prepared statement
            preparedStatement.setInt(1, user_id);

            // executing statement and getting result
            ResultSet result = preparedStatement.executeQuery();

            // if we get result then user exist, return true
            if (result.next()){
                return true;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        // user does not exist or connection failure
        return false;
    }

    /**
     * function to insert message into database
     * 
     * @param message 
     * @return object of type Message if successful or null if fails
     */
    public Message insertMessage(Message message) {

        try {
            // connection
            Connection connection = ConnectionUtil.getConnection();

            // making string
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?,?,?);";

            // creating preparedStatement, auto injection id
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // preparing statement
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            // execute update
            preparedStatement.executeUpdate();

            // getting the auto generated id key
            ResultSet results = preparedStatement.getGeneratedKeys();

            // checking that we have results and returning new object of type Message
            if (results.next()){
                int message_id = (int)results.getLong(1);
                return new Message(message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // something went wrong, return null
        return null;
    }

    /**
     * function to return ALL messages from database
     * 
     * @return list of type Message
     */
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        try {
            // connection
            Connection connection = ConnectionUtil.getConnection();
            
            // sql string
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // executing preparedStatement
            ResultSet results = preparedStatement.executeQuery();

            // iterating through all results, make new Message object, add new objetc to list
            while(results.next()){
                Message newMessage = new Message(results.getInt("message_id"), 
                results.getInt("posted_by"),
                results.getString("message_text"),
                results.getLong("time_posted_epoch"));

                messages.add(newMessage);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
        return messages;
    }

}
