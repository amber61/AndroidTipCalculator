package com.example.tgk.integrationwithfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Database of the first mini app--Tip Record
 * 
 * @author Zhe Huang created on 4/18/2016.
 * @version 1.0.0
 * @see android.content.ContentValues
 * @see android.content.Context
 * @see android.database.Cursor
 * @see android.database.SQLException
 * @see android.database.sqlite.SQLiteDatabase
 * @see android.database.sqlite.SQLiteOpenHelper
 * @see android.util.Log
 * @since 1.8.0_73 
 */
public class HMainDbAdapter {

    public static final String RECORD_ROWID = "_id";
    public static final String RESTAURANT_NAME = "restaurantName";
    public static final String EXPENSE_AMOUTN = "expenseAmount";
    public static final String TIP_PERCENTAGE = "tipPercentage";
    public static final String TIP_AMOUNT = "tipAmount";
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String TIP_NOTE = "tipNote";
    public static final String TIP_DATE = "tipDate";

    private static final String TAG = "HMainDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Restaurant";
    private static final String SQLITE_TABLE = "Tip";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    RECORD_ROWID + " integer PRIMARY KEY autoincrement," +
                    RESTAURANT_NAME + "," +
                    EXPENSE_AMOUTN + "," +
                    TIP_PERCENTAGE + "," +
                    TIP_AMOUNT + "," +
                    TOTAL_AMOUNT + "," +
                    TIP_NOTE + "," +
                    TIP_DATE + ");";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public HMainDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public HMainDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public boolean deleteAllRecords() {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchAllRecords() {
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{RECORD_ROWID, RESTAURANT_NAME, EXPENSE_AMOUTN,
                        TIP_PERCENTAGE, TIP_AMOUNT, TOTAL_AMOUNT, TIP_NOTE, TIP_DATE},
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean deleteTipRecordById(int id) {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, RECORD_ROWID + " = ?", new String[]{Integer.toString(id)});
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchTipRecordById(int id) {

        Log.w(TAG, Integer.toString(id));
        Cursor mCursor = null;
        if (id  == 0) {
            mCursor = mDb.query(SQLITE_TABLE, new String[]{RECORD_ROWID, RESTAURANT_NAME, EXPENSE_AMOUTN,
                            TIP_PERCENTAGE, TIP_AMOUNT, TOTAL_AMOUNT, TIP_NOTE, TIP_DATE},
                    null, null, null, null, null);

        } else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[]{RECORD_ROWID, RESTAURANT_NAME, EXPENSE_AMOUTN,
                            TIP_PERCENTAGE, TIP_AMOUNT, TOTAL_AMOUNT, TIP_NOTE, TIP_DATE},
                    RECORD_ROWID + " = " + id, null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int fetchTipRecordByName(String name) {

    Cursor c = mDb.query(SQLITE_TABLE, null, RESTAURANT_NAME + " = ?", new String[]{name}, null, null, null);
    int result = c.getCount();
    c.close();
    return result;
    }

    public long createTipRecords(String restaurantName, double expenseAmount, double tipPercentage, double tipAmount, double totalAmount, String tipNote, String tipDate) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(RESTAURANT_NAME, restaurantName);
        initialValues.put(EXPENSE_AMOUTN, expenseAmount);
        initialValues.put(TIP_PERCENTAGE, tipPercentage);
        initialValues.put(TIP_AMOUNT, tipAmount);
        initialValues.put(TOTAL_AMOUNT, totalAmount);
        initialValues.put(TIP_NOTE, tipNote);
        initialValues.put(TIP_DATE, tipDate);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public void insertSomeRecords() {

        createTipRecords("KFC", 30.00, 0.15, 4.50, 34.50, "good", "2015-12-12");
        createTipRecords("TimHortons", 25, 0.1, 2.5, 27.5, "excellent", "2016-04-05");
    }
}
