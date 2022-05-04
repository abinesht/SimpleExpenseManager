package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private final DataBaseHelper db;
    public PersistentAccountDAO(DataBaseHelper db) {

        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor cursor = db.getAccountNum();
        List<String> accNumList = new ArrayList<String>();
        if(cursor.getCount()==0){
            return accNumList;
        }
        while(cursor.moveToNext()){
            String accNO = cursor.getString(0);
            accNumList.add(accNO);

        }
        return accNumList;
    }

    @Override
    public List<Account> getAccountsList() {

        Cursor cursor = db.getAccounts();
        List<Account> accountList = new ArrayList<Account>();
        if(cursor.getCount()==0){
            return accountList;
        }
        while(cursor.moveToNext()){
            String accNO = cursor.getString(0);
            String bankName=cursor.getString(1);;
            String accountHolderName= cursor.getString(2);;
            Double balance = cursor.getDouble(3);
            Account acc = new Account(accNO,bankName,accountHolderName,balance);
            accountList.add(acc);

        }
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = db.getAccount(accountNo);
        if (cursor.getCount()>0) {
            String accNO = cursor.getString(0);
            String bankName=cursor.getString(1);;
            String accountHolderName= cursor.getString(2);;
            Double balance = cursor.getDouble(3);
            Account acc = new Account(accNO,bankName,accountHolderName,balance);
            return acc;
        }
        String alert = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(alert);
    }

    @Override
    public void addAccount(Account account) {
        String bank = account.getBankName();
        String account_no =   account.getAccountNo();
        String holder = account.getAccountHolderName();
        Double balance = account.getBalance();
        db.insertAccount(account_no,bank,holder,balance);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db.deleteAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        if(!(account instanceof Account)){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);

        }


        double balance1 = account.getBalance();
        switch (expenseType) {
            case EXPENSE:
                balance1 = account.getBalance() - amount;
                account.setBalance(balance1);
                break;
            case INCOME:
                balance1 = account.getBalance() + amount;
                account.setBalance(balance1);
                break;
        }

        db.updateBalance(accountNo,balance1);
    }
}