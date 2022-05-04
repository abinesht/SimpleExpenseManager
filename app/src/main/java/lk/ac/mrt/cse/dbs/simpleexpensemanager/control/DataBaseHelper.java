package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String dbName = "190019B.db";

    public DataBaseHelper(@Nullable Context context) {
        super(context, dbName, null ,1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Accounts (accNO VARCHAR(20) PRIMARY KEY, bankName VARCHAR(20), accountHolderName VARCHAR(20), balance DECIMAL(15,2) )");
        sqLiteDatabase.execSQL("CREATE TABLE Transactions (transactionID INTEGER PRIMARY KEY AUTOINCREMENT, accNO VARCHAR(20), date VARCHAR(20), amount DECIMAL(15,2), type VARCHAR(20) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Accounts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Transactions");
        onCreate(sqLiteDatabase);
    }

    public boolean insertAccount(String accNO, String bankName, String accountHolderName, double balance){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accNo",accNO);
        contentValues.put("bankName",bankName);
        contentValues.put("accountHolderName",accountHolderName);
        contentValues.put("balance",balance);

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Accounts WHERE accNO = ?", new String[]{accNO});

        if (cursor.getCount() == 0) {
            long result = sqLiteDatabase.insert("Accounts", null, contentValues);
            if(result==-1){
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean deleteAccount(String accNO) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?", new String[]{accNO});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.delete("Accounts", "accNO=?", new String[]{accNO});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getAccount(String accNO){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Accounts WHERE accNO = ?", new String[]{accNO});
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAccounts(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Accounts", null);
        return cursor;
    }

    public Cursor getAllTransactions(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Transactions", null);
        return cursor;
    }
    public Cursor getLimitedTransactions(int limit){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Transactions ORDER BY transactionID DESC LIMIT " + Integer.toString(limit) , null);
        return cursor;
    }

    public Cursor getAccountNum(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT accNO FROM Accounts", null);
        return cursor;
    }

    public boolean updateBalance(String accNO,  double amount) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", amount);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Accounts WHERE accNO = ?", new String[]{accNO});

        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.update("Accounts", contentValues, "accNO=?", new String[]{accNO});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean insertTransaction(String date, String accNO, ExpenseType expenseType, double amount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accNO", accNO);
        contentValues.put("date", date);
        contentValues.put("amount", amount );
        contentValues.put("type", expenseType.toString());
        long result = sqLiteDatabase.insert("Transactions",null,contentValues);
        if(result==-1){
            return false;
        }
        return true;
    }
}
