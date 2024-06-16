package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    // default constructor with no args
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    // constructor for when accountDAO is given
    // I know this is meant for mocking of the DAO
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * function to add new account to the db
     * checks for specified conditions
     * 
     * @param user user account we are trying to insert
     * @return Account object, can be either actual object or null
     */
    public Account addAccount(Account user){
        // Account newAccount = null;
        Account existingAccount = this.accountDAO.getAccountByUsername(user.getUsername());
        if (!user.getUsername().isBlank() && user.getPassword().length() >= 4 && existingAccount == null){
            Account newAccount = this.accountDAO.insertAccount(user);
            return newAccount;
        }
        
        return null;
    }


    /**
     * function verifying that account exist based on username
     * 
     * @param user Object of type Account
     * @return Account object if account exist and passwords match else returns null
     */
    public Account verifyLogin(Account user){

        // checking is user exists
        Account account = this.accountDAO.getAccountByUsername(user.getUsername());
        
        // block to check user is valid
        // checks if account exists
        // checks if passwords match from the request body and the account retrieved
        if (account != null && user.getPassword().equals(account.getPassword())){
            return account;
        }else{
            return null;
        }

    }
}
