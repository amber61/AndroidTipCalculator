package com.example.tgk.integrationwithfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.sql.Date;


/**
 * Created by yingmei on 05-Apr-16.
 */
public class ZContactDbAdapter {

    //contracts
    public static final String KEY_ROWID = "_id";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NOTE = "note";
    public static final String KEY_CREATEDATE = "createdate";

    private static final String TAG = "ContactDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Contact";
    private static final String SQLITE_TABLE = "Contact";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_FIRSTNAME + "," +
                    KEY_LASTNAME + "," +
                    KEY_PHONE + "," +
                    KEY_EMAIL + "," +
                    KEY_NOTE + "," +
                    KEY_CREATEDATE + "," +
                    " UNIQUE (" + KEY_EMAIL +"));";


    //inner class
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

    public ZContactDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public ZContactDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    //insert a record into database
    public long createContact(String firstname, String lastname,String phone, String email,String note) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FIRSTNAME, firstname);
        initialValues.put(KEY_LASTNAME, lastname);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_NOTE, note);

        Date createdate = new Date(new java.util.Date().getTime());
        initialValues.put(KEY_CREATEDATE, createdate.toString());

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllContacts() {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete) + "deleted");
        return doneDelete > 0;
    }

    public boolean deleteContactById(long id) {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, KEY_ROWID + " = " + id , null);
        Log.w(TAG, Integer.toString(doneDelete)+"deleted");
        return doneDelete > 0;
    }

    public Cursor fetchContactById(long id) throws SQLException {
        Cursor mCursor = null;

        mCursor = mDb.query(true, SQLITE_TABLE, new String[]{KEY_ROWID,
                        KEY_FIRSTNAME, KEY_LASTNAME,KEY_PHONE,KEY_EMAIL,KEY_NOTE,KEY_CREATEDATE},
                KEY_ROWID + " = " + id, null,null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor fetchContactsByName(String inputText) throws SQLException {

        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_FIRSTNAME, KEY_LASTNAME,KEY_PHONE,KEY_EMAIL,KEY_NOTE,KEY_CREATEDATE},
                    null, null, null, null, null);
        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_FIRSTNAME, KEY_LASTNAME,KEY_PHONE,KEY_EMAIL,KEY_NOTE,KEY_CREATEDATE},
                    KEY_FIRSTNAME + " like '%" + inputText + "%'" + " OR " + KEY_LASTNAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int countContactsByName(String inputText) throws SQLException {

        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_FIRSTNAME, KEY_LASTNAME,KEY_PHONE,KEY_EMAIL,KEY_NOTE,KEY_CREATEDATE},
                    null, null, null, null, null);
        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_FIRSTNAME, KEY_LASTNAME,KEY_PHONE,KEY_EMAIL,KEY_NOTE,KEY_CREATEDATE},
                    KEY_FIRSTNAME + " like '%" + inputText + "%'" + " OR " + KEY_LASTNAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor.getCount();

    }

    public Cursor fetchAllContacts() {
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_FIRSTNAME, KEY_LASTNAME,KEY_PHONE,KEY_EMAIL,KEY_NOTE,KEY_CREATEDATE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    // insert some contacts for testing purpose
    public void insertSomeContacts() {
        createContact("Eric", "White","613-123-4567", "FelixWhite@gmail.com","Cell Phone");
        createContact("Jack", "Pitt", "613-456-7890", "JackLee@gmail.com", "Home Phone");

    }

}
