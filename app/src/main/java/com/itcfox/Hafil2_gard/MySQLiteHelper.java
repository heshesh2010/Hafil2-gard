package com.itcfox.Hafil2_gard;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MySQLiteHelper extends SQLiteOpenHelper {

// Database Version
private static final int DATABASE_VERSION = 1;


private static final String DATABASE_NAME = "HafilSTC";


private static final String TABLE_NAME1 = "gard";
private static final String TABLE_NAME2 = "FleetMaster";
private static final String TABLE_NAME3 = "Gate";
private static final String TABLE_NAME4 = "Users";

private static SQLiteDatabase sqliteDB;

public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
}


@Override
public void onCreate(SQLiteDatabase db) {

    // SQL statement to create gard table
    String CREATE_gard_TABLE = "CREATE TABLE gard ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ActionDate TEXT, " +
            "gateID INTEGER, " +
            "FleetNumber TEXT, " +
            "synced INTEGER, " +
            "type INTEGER" +
            "note TEXT)";



    // SQL statement to create FleetMaster table
    String CREATE_FleetMaster_TABLE = "CREATE TABLE FleetMaster ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "busID INTEGER, " +
            "FleetNumber INTEGER, " +
            "plateNumber TEXT, " +
            "model TEXT, " +
            "modelYear INTEGER,"+
            "seat INTEGER,"+
            "seat2 INTEGER)";

    // SQL statement to create Gate table
    String CREATE_Gate_TABLE = "CREATE TABLE Gate ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "GateID INTEGER," +
            "GateName TEXT)";


    // SQL statement to create Users table
    String CREATE_Users_TABLE = "CREATE TABLE Users ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Name TEXT, " +
            "LoginName TEXT, " +
            "Password TEXT) ";

    // create gard table
    db.execSQL(CREATE_gard_TABLE);

    // create FleetMaster table
    db.execSQL(CREATE_FleetMaster_TABLE);

    // create Gate table
    db.execSQL(CREATE_Gate_TABLE);

    // create Users table
    db.execSQL(CREATE_Users_TABLE);
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }



public void open() {

    sqliteDB = this.getWritableDatabase();


}

public void begin() {

    sqliteDB.beginTransaction();

}


// Close Database
public void close() {
    if(sqliteDB != null)
        sqliteDB.close();
}

// Close Database
public void setsucss() {


    sqliteDB.setTransactionSuccessful();

}


// Close Database
public void end() {


    sqliteDB.endTransaction();

}

////===MASTERS ECXEL INSERT===/////

public void InsertIntoGard(int gateID, String FleetNumber, int type , int synced , String note) {
    ContentValues contentValues = new ContentValues();
    sqliteDB = this.getWritableDatabase();
    String ActionDate = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault()).format(new Date());

    String qq = "select fleetNumber from FleetMaster where fleetNumber='"+FleetNumber+"'";
    Cursor cursor = sqliteDB.rawQuery(qq, null);
   String  FleetNumberTemp = "0";
    if(cursor != null && cursor.moveToFirst()) {
        FleetNumberTemp = cursor.getString(0);
    }

    contentValues.put("gateID", gateID);
    contentValues.put("FleetNumber", FleetNumberTemp);
    contentValues.put("type", type);
    contentValues.put("ActionDate", ActionDate);
    contentValues.put("synced", synced);
    contentValues.put("note", note);
    sqliteDB.insert(TABLE_NAME1, null, contentValues);
    sqliteDB.close();
}







public void insertFleetNumberForMasterFile(String FleetNumber) {
    ContentValues contentValues = new ContentValues();
    sqliteDB = this.getWritableDatabase();

    contentValues.put("FleetNumber", FleetNumber);


    sqliteDB.insert(TABLE_NAME2, null, contentValues);
}


public void insertFleetMasterFile(String busID, String FleetNumber,String plateNumber, String model , String modelYear, String seat, String seat2 ) {
    ContentValues contentValues = new ContentValues();
    sqliteDB = this.getWritableDatabase();

    contentValues.put("busID", busID);
    contentValues.put("FleetNumber", FleetNumber);
    contentValues.put("plateNumber", plateNumber);
    contentValues.put("model", model);
    contentValues.put("modelYear", modelYear);
    contentValues.put("seat", seat);
    contentValues.put("seat2", seat2);

    sqliteDB.insert(TABLE_NAME2, null, contentValues);
}




public void insertGateFile(String GateID, String gateName) {
    ContentValues contentValues = new ContentValues();
    sqliteDB = this.getWritableDatabase();

    contentValues.put("GateID", GateID);
    contentValues.put("GateName", gateName);
    sqliteDB.insert(TABLE_NAME3, null, contentValues);
}


public void insertUserFile(String NameNotused,String Name,  String LoginName, String Password ) {
    ContentValues contentValues = new ContentValues();
    sqliteDB = this.getWritableDatabase();

    contentValues.put("Name", Name);
    contentValues.put("LoginName", LoginName);
    contentValues.put("Password", Password);

    sqliteDB.insert(TABLE_NAME4, null, contentValues);

}




public String CheckLogin(String user, String pass) throws ParseException {
    SQLiteDatabase db = this.getWritableDatabase();

    String qq = "select  LoginName  from Users where LoginName = '" + user + "' and password = '" + pass + "'";
    Cursor cursor = db.rawQuery(qq, null);

    if(cursor != null && cursor.moveToFirst()) {
        return cursor.getString(0);
    }

    cursor.close();
    db.close();
    return "0";

}



public int CheckLoginForID(String user, String pass) throws ParseException {
    SQLiteDatabase db = this.getWritableDatabase();

    String qq = "select  id  from Users where LoginName = '" + user + "' and password = '" + pass + "'";
    Cursor cursor = db.rawQuery(qq, null);

    if(cursor != null && cursor.moveToFirst()) {
        return cursor.getInt(0);
    }

    cursor.close();
    db.close();
    return 0;

}


public Cursor getAllGatesNames() {
    SQLiteDatabase db = this.getWritableDatabase();
    String qq = "select gateName  from Gate ";
    Cursor cursor = db.rawQuery(qq, null);

    if(cursor != null && cursor.moveToFirst()) {
        return cursor;
    }

    db.close();
    return cursor;

}


public Cursor getAllNotSyncedRows(int gateid) {
    SQLiteDatabase db = this.getWritableDatabase();
    String ActionDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String qq = "select FleetNumber , gateID , type , ActionDate from gard where synced=0 and gateID = "+gateid+" order by ActionDate desc LIMIT 10;";
    Cursor cursor = db.rawQuery(qq, null);

    if(cursor != null && cursor.moveToFirst()) {
        return cursor;
    }

    db.close();
    return cursor;

}




public Cursor getAllNotSyncedRowsForSync() {
    SQLiteDatabase db = this.getWritableDatabase();
    String ActionDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String qq = "select FleetNumber , gateID , type , ActionDate , note from gard where synced=0 order by ActionDate desc ;";
    Cursor cursor = db.rawQuery(qq, null);

    if(cursor != null && cursor.moveToFirst()) {
        return cursor;
    }

    db.close();
    return cursor;

}


public int getNotSyncedRowCount(int type, int gateID) {
    SQLiteDatabase db = this.getWritableDatabase();
    String ActionDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String qq = "select *  from gard where synced=0 and gateID = "+gateID+" and type ="+type;
    Cursor cursor = db.rawQuery(qq, null);

    if(cursor != null && cursor.moveToFirst()) {
        return cursor.getCount();
    }

    db.close();
    return cursor.getCount();

}


public void DeleteThisRecord(String ActionDate) {

    SQLiteDatabase db = this.getWritableDatabase();

    db.delete("gard", "ActionDate" + "='" + ActionDate+"'", null);

}

public void updateAllNotSyncedRows(String ActionDate) {
    ContentValues values = new ContentValues();
    values.put("synced", 1);



    SQLiteDatabase db = this.getWritableDatabase();

  //  db.update("gard", "ActionDate" + "='" + ActionDate+"'", null);

    db.update("gard",
            values, "synced = 0 "+"ActionDate = '"+ActionDate+"'",null);

    db.close();
}

}