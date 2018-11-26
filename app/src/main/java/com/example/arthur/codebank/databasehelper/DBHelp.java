package com.example.arthur.codebank.databasehelper;

import android.content.ContentValues;
import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelp extends SQLiteOpenHelper {

    private static DBHelp instance;
    private static final int DATABASE_VER = 1;
    private static final String DATABASE_NAME = "CODEBANK.db";

    private static final String TABLE_NAME = "STOREDTEXTS";
    private static final String COLUMN_HASHES = "STOREDHASHES";
    //password for the db
    private static final String DB_PHARSE = "#FBV2018!@";


    private static final String SQL_CREATE_TABLE_QUERY=
            "CREATE TABLE " +TABLE_NAME+ " (" + COLUMN_HASHES +" TEXT PRIMARY KEY)";
    private static final String SQL_DELETE_TABLE_QUERY=
            "DROP TABLE IF EXISTS "+TABLE_NAME;

    //Modifyed the default constructer and removed all inputs but the context.
    // The rest will be from super class -while recieving database_name, etc.
    public DBHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    static public synchronized DBHelp getInstance(Context context){
        if(instance == null){
            instance = new DBHelp(context);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_QUERY);
        onCreate(sqLiteDatabase);
    }

    public void insertNewCode(String code){
        SQLiteDatabase database = instance.getWritableDatabase(DB_PHARSE);
        ContentValues values = new ContentValues();
        values.put(COLUMN_HASHES,code);
        database.insert(TABLE_NAME,null,values);
        database.close();

    }
    public void updateCode(String oldCode, String newCode){
        SQLiteDatabase database = instance.getWritableDatabase(DB_PHARSE);
        ContentValues values = new ContentValues();
        values.put(COLUMN_HASHES,newCode);
        database.update(TABLE_NAME,values,COLUMN_HASHES+"='"+oldCode+"'",null);
        database.close();

    }
    public void deleteCode(String code){

        SQLiteDatabase database = instance.getWritableDatabase(DB_PHARSE);
        ContentValues values = new ContentValues();
        values.put(COLUMN_HASHES,code);
        database.delete(TABLE_NAME,COLUMN_HASHES+"='"+code+"'",null);
        database.close();
    }

    public List<String> getAllCodes(){

        SQLiteDatabase database = instance.getWritableDatabase(DB_PHARSE);

        Cursor cursor = database.rawQuery(String.format("SELECT * FROM '%s';",TABLE_NAME),null);
        List<String> codes = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String code = cursor.getString(cursor.getColumnIndex(COLUMN_HASHES));
                codes.add(code);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return codes;
    }



}
