package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * function to add a new message based on specifications:
     * message should not be empty
     * message should be less than 255 in length
     * user must exist
     * 
     * @param message
     * @return object of type Message or null depending if conditions are met
     */
    public Message addMessage(Message message) {
        String messageText = message.getMessage_text();

        // checking specified conditions
        if (!messageText.isBlank() && messageText.length() <= 255
                && messageDAO.userExists(message.getPosted_by()) != null) {
            Message newMessage = this.messageDAO.insertMessage(message);
            return newMessage;
        } else {
            return null;
        }

    }

    /**
     * function to retrieve all messages
     * 
     * @return list of Message objects
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * function that calls DAO to retrieve message based on message_id
     * 
     * @param messageId
     * @return Message object or null
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * function to delete a message by id passed
     * first checks if message exist using the getMessageById call
     * if exists then deletes message and returns object otherwise returns null
     * 
     * @param messageId
     * @return null or object of type Message
     */
    public Message deleteMessageById(int messageId) {
        Message message = messageDAO.getMessageById(messageId);

        if (message != null) {
            messageDAO.deleteMessageById(messageId);
        }

        return message;
    }

    /**
     * function calls update function from DAO with given params
     * checks for conditions:
     * message exists
     * message text is below 255 chars
     * message text is not empty
     * 
     * @param messageId
     * @param newMessageText
     * @return
     */
    public Message updateMessageById(int messageId, String newMessageText) {

        // getting back message if it exists
        Message returningMessage = messageDAO.getMessageById(messageId);

        // checking conditions for updating
        if (returningMessage != null && newMessageText.length() <= 255 && !newMessageText.isBlank()) {
            // lazy updating
            returningMessage.setMessage_text(newMessageText);
            messageDAO.updateMessageById(messageId, newMessageText);
            return returningMessage;
        }else{
            return null;
        }

    }

    /**
     * function that calls the getMessagesFromUser from the DAO
     * 
     * @param accountId
     * @return
     */
    public List<Message> getMessagesFromUser(int accountId){
        return messageDAO.getAllMessagesFromUser(accountId);
    }

}
