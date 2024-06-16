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
     */
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    

}
