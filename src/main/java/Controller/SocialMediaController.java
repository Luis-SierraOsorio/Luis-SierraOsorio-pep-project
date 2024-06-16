package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Message;
import Model.Account;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    //creating an object mapper to transform types
    ObjectMapper  mapper = new ObjectMapper();

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::getAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * function to add a new user to the database
     * 
     * @param context 
     * @throws JsonProcessingException throws exception
     */
    private void postAccountHandler(Context context) throws JsonProcessingException{
        // putting the required fields in the object
        Account user = mapper.readValue(context.body(), Account.class);
        // getting back the user
        Account addedUser = accountService.addAccount(user);

        // checking to see if operation was successful
        if (addedUser == null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(addedUser));
        }
    }

    /**
     * function to handle checking if user is allowed to login
     * 
     * @param context used to get user request body
     * @throws JsonProcessingException
     */
    private void getAccountHandler(Context context) throws JsonProcessingException{        
         

        // getting request body and converting to Account object type with proper fields
        Account user = mapper.readValue(context.body(), Account.class);

        // checking for the user using service method
        Account returningAccount = accountService.verifyLogin(user);

        // block of code returns status code unathorized or the object
        if (returningAccount == null){
            context.status(401);
        }else{
            context.json(mapper.writeValueAsString(returningAccount));
        }

    }

    /**
     * function to handle adding a message request
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void postMessageHandler(Context context) throws JsonProcessingException{
        // getting body values
        Message message = mapper.readValue(context.body(), Message.class);

        // calling service method to add message
        Message returningMessage = messageService.addMessage(message);

        // checking to see what to return
        if (returningMessage == null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(returningMessage));
        }
    }

    /**
     * function to handle getting all messages request
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void getAllMessagesHandler(Context context) throws JsonProcessingException{
        List<Message> returningMessages = messageService.getAllMessages();
        
        context.json(mapper.writeValueAsString(returningMessages));
    }

}