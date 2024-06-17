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
     * was thinking about implementing this in the AccountDAO but a few things:
     * I didnt want to have to construct a an AccountDAO object in the
     * MessageService and
     * I really only need it here for this project
     * 
     * @param userId
     * @return true/false
     */
    public Boolean userExists(int userId) {
        try {
            // connection
            Connection connection = ConnectionUtil.getConnection();

            // sql string
            String sql = "SELECT * FROM user WHERE user_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // injecting values in prepared statement
            preparedStatement.setInt(1, userId);

            // executing statement and getting result
            ResultSet result = preparedStatement.executeQuery();

            // if we get result then user exist, return true
            if (result.next()) {
                return true;
            }
        } catch (Exception e) {
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
            ResultSet result = preparedStatement.getGeneratedKeys();

            // checking that we have results and returning new object of type Message
            if (result.next()) {
                int message_id = (int) result.getLong(1);
                return new Message(message_id, message.getPosted_by(), message.getMessage_text(),
                        message.getTime_posted_epoch());
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
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            // connection
            Connection connection = ConnectionUtil.getConnection();

            // sql string
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // executing preparedStatement
            ResultSet results = preparedStatement.executeQuery();

            // iterating through all results, make new Message object, add new objetc to
            // list
            while (results.next()) {
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

    /**
     * function to get message by id
     * 
     * @param messageId
     * @return new Object of type message or null if not found
     */
    public Message getMessageById(int messageId) {
        try {
            // connection
            Connection connection = ConnectionUtil.getConnection();

            // sql string
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // inject needed values into preparedStatement
            preparedStatement.setInt(1, messageId);

            // execute query
            ResultSet result = preparedStatement.executeQuery();

            // check if message exist and return new Object
            if (result.next()) {
                return new Message(result.getInt("message_id"),
                        result.getInt("posted_by"),
                        result.getString("message_text"),
                        result.getLong("time_posted_epoch"));
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        // message not found
        return null;
    }

    /**
     * function to delete a message based on the id provided
     * no need to return anything, either the delete happens or it doesn't
     * based on the documentation of sql delete return a boolean but I think this is
     * fine for now
     * 
     * @param messageId
     * @return
     */
    public void deleteMessageById(int messageId) {
        try {
            // connection
            Connection connection = ConnectionUtil.getConnection();

            // sql string
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // injection values into preparedStatement
            preparedStatement.setInt(1, messageId);

            // executing query
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    /**
     * function to update a message, funciton assumes message exists - I handle
     * verification on the service layer.
     * 
     * @param messageId
     * @param messageText
     */
    public void updateMessageById(int messageId, String messageText) {

        try {
            // connection
            Connection connection = ConnectionUtil.getConnection();

            // sql string
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // injecting parameters
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageId);

            // executing query
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }


    /**
     * function to get all messages from a specific user based on id
     * 
     * @param accountId 
     * @return list of Objects type Message
     */
    public List<Message> getAllMessagesFromUser(int accountId) {
        List<Message> messages = new ArrayList<>();

        try {
            // connection
            Connection connection = ConnectionUtil.getConnection();

            // sql string
            String sql = "SELECT * FROM message INNER JOIN account ON message.posted_by = account.account_id WHERE account.account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // injecting params
            preparedStatement.setInt(1, accountId);

            // executing query
            ResultSet results = preparedStatement.executeQuery();

            // going throughing the results and adding them to the returning list
            while (results.next()) {
                Message message = new Message(results.getInt("message_id"),
                        results.getInt("posted_by"),
                        results.getString("message_text"),
                        results.getLong("time_posted_epoch"));

                messages.add(message);
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        return messages;
    }

}
