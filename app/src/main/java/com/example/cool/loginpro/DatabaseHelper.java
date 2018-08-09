package com.example.cool.loginpro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Student.db";
    // User table name
    private static final String TABLE_CAN = "durga";
    private static final String COLUMN_CANDIDATE_IMAGE= "candidate_image";
    private static final String COLUMN_CANDIDATE_ID = "candidate_id";
    private static final String COLUMN_CANDIDATE_FIRST_NAME = "candidate_firstname";
    private static final String COLUMN_CANDIDATE_LAST_NAME = "candidate_lastname";
    private static final String COLUMN_CANDIDATE_DATE_OF_BIRTH = "candidate_dateofbirth";
    private static final String COLUMN_CANDIDATE_EMAIL = "candidate_email";
    private static final String COLUMN_CANDIDATE_PASSWORD = "candidate_password";

    private static final String COLUMN_CANDIDATE_PHONE="candidate_phone";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CAN_TABLE = "CREATE TABLE " + TABLE_CAN + "("
                + COLUMN_CANDIDATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CANDIDATE_FIRST_NAME + " TEXT," +
                COLUMN_CANDIDATE_LAST_NAME + " TEXT," +
                COLUMN_CANDIDATE_DATE_OF_BIRTH + " TEXT,"
                + COLUMN_CANDIDATE_EMAIL + " TEXT," +
                COLUMN_CANDIDATE_PHONE + " TEXT," +
                COLUMN_CANDIDATE_PASSWORD + " TEXT," +

                COLUMN_CANDIDATE_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_CAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_CAN_TABLE = "DROP TABLE IF EXISTS " + TABLE_CAN;
        db.execSQL(DROP_CAN_TABLE);
        onCreate(db);
    }

    public void addUser(User user, byte [] data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANDIDATE_FIRST_NAME, user.getmFirstName());
        values.put(COLUMN_CANDIDATE_LAST_NAME, user.getmLastName());
        values.put(COLUMN_CANDIDATE_DATE_OF_BIRTH, user.getmDob());
        values.put(COLUMN_CANDIDATE_EMAIL, user.getmEmailId());
        values.put(COLUMN_CANDIDATE_PHONE,user.getmMobileNo());
        values.put(COLUMN_CANDIDATE_PASSWORD, user.getmPassword());

        values.put(COLUMN_CANDIDATE_IMAGE, data);
        // Inserting Row
        db.insert(TABLE_CAN, null, values);
        db.close();

    }
    public boolean checkUser( String email)
    {
        // array of columns to fetch
        String[] columns = {COLUMN_CANDIDATE_ID};
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_CANDIDATE_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_CAN,columns,selection,selectionArgs,null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    public boolean checkCandidate(String email, String password) {

        // array of columns to fetch
        String[] columns = {COLUMN_CANDIDATE_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_CANDIDATE_EMAIL + " = ?" + " AND " + COLUMN_CANDIDATE_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_CAN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean updateCandidate(User candidate, byte[] data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_CANDIDATE_ID,candidate.getId());
        values.put(COLUMN_CANDIDATE_FIRST_NAME, candidate.getmFirstName());
        values.put(COLUMN_CANDIDATE_LAST_NAME, candidate.getmLastName());
        values.put(COLUMN_CANDIDATE_DATE_OF_BIRTH, candidate.getmDob());
        values.put(COLUMN_CANDIDATE_EMAIL, candidate.getmEmailId());
        values.put(COLUMN_CANDIDATE_PHONE,candidate.getmMobileNo());
        values.put(COLUMN_CANDIDATE_PASSWORD, candidate.getmPassword());

        values.put(COLUMN_CANDIDATE_IMAGE,data);
        // updating row
        /*db.update(TABLE_CAN, values,null,null);
        db.close();*/

        return db.update(TABLE_CAN, values, COLUMN_CANDIDATE_EMAIL + "=?", new String[]{candidate.getmEmailId()}) == 1;

    }

    public boolean delete(String id)
    {
        String[] columns = {COLUMN_CANDIDATE_ID};
        SQLiteDatabase db= this.getReadableDatabase();
        String selection = COLUMN_CANDIDATE_ID + " = ?";
        String[] selectionArgs = {id};

        Cursor  cursor=db.query(TABLE_CAN,columns,selection,selectionArgs,null,null,null);
        int cursorCount =cursor.getCount();
        cursor.close();
        if (cursorCount>0)
        {
            return true;
        }
        return false;
    }
    public List<User> getAllUser() {
        String[] columns = {
                COLUMN_CANDIDATE_ID,
                COLUMN_CANDIDATE_FIRST_NAME,
                COLUMN_CANDIDATE_LAST_NAME,
                COLUMN_CANDIDATE_DATE_OF_BIRTH,
                COLUMN_CANDIDATE_EMAIL,
                COLUMN_CANDIDATE_PHONE,
                COLUMN_CANDIDATE_PASSWORD,

                COLUMN_CANDIDATE_IMAGE
        };
        String sortOrder = COLUMN_CANDIDATE_ID + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CAN, columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_ID))));
                user.setmFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_FIRST_NAME)));
                user.setmLastName(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_LAST_NAME)));
                user.setmEmailId(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_EMAIL)));
                user.setmMobileNo(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_PHONE)));
                user.setmDob(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_DATE_OF_BIRTH)));
                user.setmPassword(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_PASSWORD)));

                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    public List<User> getAllUserMobileNumber() {
        String[] columns = {
                COLUMN_CANDIDATE_PHONE
        };
//        String sortOrder = COLUMN_CANDIDATE_ID + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CAN, columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setmMobileNo(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_PHONE)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }
}
