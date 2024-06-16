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
        Account newAccount = null;

        if (!user.getUsername().isBlank() && user.getPassword().length() >= 4 && this.accountDAO.getAccountById(user.getAccount_id()) != null){
            newAccount = this.accountDAO.insertAccount(user);
        }
        
        return newAccount;
    }
}
