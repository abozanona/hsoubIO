package com.rond.hsoub.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rond.hsoub.API.APIModules.User;
import com.rond.hsoub.Models.comment;
import com.rond.hsoub.Models.postListItem;

import java.util.ArrayList;

/**
 * Created by Nullsky on 12/29/2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int MAXIMUM_NUMBER_OF_STORED_ITEMS = 20;
    private static final String DATABASE_NAME = "hsoub.db";

    private static final String TABLE_POSTS_LIST = "postslist";
    private static final String TABLE_POST = "post";
    private static final String TABLE_USER = "user";
    private static final String TABLE_DICTIONARY = "dictionary";//Key-value
    public static final int version = 3;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_POSTS_LIST +
                        " (link TEXT PRIMARY KEY, object BLOB, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );
        db.execSQL(
                "CREATE TABLE " + TABLE_POST +
                        " (postid TEXT PRIMARY KEY, object BLOB, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );
        db.execSQL(
                "CREATE TABLE " + TABLE_USER +
                        " (userid TEXT PRIMARY KEY, object BLOB, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );
        db.execSQL(
                "CREATE TABLE " + TABLE_DICTIONARY +
                        " (t_key TEXT PRIMARY KEY, value BLOB, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICTIONARY);
        onCreate(db);
    }

    public boolean setPostList(String link, ArrayList<postListItem> object) {
        byte[] obj = Serialization.serializeObject(object);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("link", link);
        contentValues.put("object", obj);
        db.insertWithOnConflict(TABLE_POSTS_LIST, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public ArrayList<postListItem> getPostList(String link) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_POSTS_LIST + " WHERE link='" + link + "'", null);

        Object obj = null;
        if (res.moveToFirst()) {
            obj = Serialization.deserializeObject(res.getBlob(1));
        }
        res.close();
        return (ArrayList<postListItem>) obj;
    }

    public boolean setPost(String postid, ArrayList<comment> object) {
        byte[] obj = Serialization.serializeObject(object);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("postid", postid);
        contentValues.put("object", obj);
        db.insertWithOnConflict(TABLE_POST, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public ArrayList<comment> getPost(String postid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_POST + " WHERE postid='" + postid + "'", null);

        Object obj = null;
        if (res.moveToFirst()) {
            obj = Serialization.deserializeObject(res.getBlob(1));
        }
        res.close();
        return (ArrayList<comment>) obj;
    }

    public boolean setUser(String userid, User object) {
        byte[] obj = Serialization.serializeObject(object);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("object", obj);
        db.insertWithOnConflict(TABLE_USER, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public User getUser(String userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE userid='" + userid + "'", null);

        Object obj = null;
        if (res.moveToFirst()) {
            obj = Serialization.deserializeObject(res.getBlob(1));
        }
        res.close();
        return (User) obj;
    }

    //basically for Communities & CommunitiesList
    public boolean setDictionary(String key, byte[] value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("t_key", key);
        contentValues.put("value", value);
        db.insertWithOnConflict(TABLE_DICTIONARY, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public byte[] getDictionary(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_DICTIONARY + " WHERE t_key='" + key + "'", null);

        byte[] obj = null;
        if (res.moveToFirst()) {
            obj = res.getBlob(1);
        }
        res.close();
        return obj;
    }
}