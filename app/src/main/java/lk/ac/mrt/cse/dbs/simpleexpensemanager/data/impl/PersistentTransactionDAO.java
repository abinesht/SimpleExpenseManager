package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    // private final List<Transaction> transactions;
    private DataBaseHelper db;

    public PersistentTransactionDAO(DataBaseHelper db) {
        //transactions = new LinkedList<>();
        this.db = db;

    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String strDate = dateFormat.format(date);
        db.insertTransaction(strDate,accountNo,expenseType,amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {

        Cursor cursor = db.getAllTransactions();
        List<Transaction> output = new ArrayList<Transaction>();
        if(cursor.getCount()==0){
            return output;
        }
        while(cursor.moveToNext()){
            ExpenseType type;
            String acc_no=cursor.getString(1);;
            String date_temp = cursor.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
            Date date =formatter.parse(date_temp);
            Double amount= cursor.getDouble(3);
            String type_temp = cursor.getString(4);
            Log.d("myTag", type_temp);

            type = ExpenseType.valueOf(type_temp);
            Transaction trans = new Transaction(date,acc_no,type,amount);
            output.add(trans);

        }
        return output;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {

        Cursor res = db.getLimitedTransactions(limit);
        List<Transaction> output = new ArrayList<Transaction>();
        if(res.getCount()==0){
            return output;
        }
        while(res.moveToNext()){
            ExpenseType type;
            String acc_no=res.getString(1);;
            String date_temp = res.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
            Date date =formatter.parse(date_temp);
            Double amount= res.getDouble(3);
            String type_temp = res.getString(4);
            type = ExpenseType.valueOf(type_temp);
            Transaction trans = new Transaction(date,acc_no,type,amount);
            output.add(trans);

        }
        return output;

    }
}
