package com.patillosoftware.example.bluetooth;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


/**
 * Created by James on 11/27/2015.
 */
public class BtDevice extends ContentProvider {
    private static final String TAG = "BtDevice";
    public static final String AUTHORITY = "com.patillosoftware.example.bluetooth.provider.BtDevice";
    private static final String DATABASE_NAME = "bluetooth.db";
    private static final int DATABASE_VERSION = 1;

    //Tables
    public static final String DEVICE_TABLE = "devices";

    //URIs
    public static final Uri DEVICE_URI = Uri.parse("content://" + AUTHORITY + "/device");

    //URI mappings
    public static final int DEVICE = 1;


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch(mUriMatcher.match(uri)) {
            case DEVICE:
                count = db.delete(DEVICE_TABLE, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(count>0)
            getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        return "not implemented";
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues){

        Uri returnValue = null;

        ContentValues values;
        if(initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            return null;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = -1;

        switch (mUriMatcher.match(uri)) {
            case DEVICE:
                rowId = db.insert(DEVICE_TABLE, null, values);
                if(rowId >=0)
                    returnValue = ContentUris.withAppendedId(DEVICE_URI, rowId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }

        if(returnValue !=null) {
            getContext().getContentResolver().notifyChange(returnValue,null);
            return returnValue;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        switch(mUriMatcher.match(uri)) {
            case DEVICE:
                qBuilder.setTables(DEVICE_TABLE);
                sortOrder = DEVICE_TABLE + ".name DESC";
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(TextUtils.isEmpty(sortOrder))
            sortOrder = "_id";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = qBuilder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }


    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (mUriMatcher.match(uri)) {
            case DEVICE:
                count = db.update(DEVICE_TABLE, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(count >0)
            getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }




    private static final UriMatcher mUriMatcher;

    public static final int MatchURI(Uri uri) {
        return mUriMatcher.match(uri);
    }

    static {

        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "device", DEVICE);

    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + DEVICE_TABLE + " ("
                    + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "mac" + " VARCHAR,"
                    + "class" + " VARCHAR,"
                    + "name" + " VARCHAR,"
                    + "UNIQUE(mac) ON CONFLICT REPLACE"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }private DatabaseHelper dbHelper;


}
