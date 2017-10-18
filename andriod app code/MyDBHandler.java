package com.example.muhammad.homeautomationupdatedsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Home6.db";


    public static final String TABLE_DEVICES = "Devices";
    public static final String COLUMN_DEVICENAME = "devicename";
    public static final String COLUMN_DEVICEIP = "deviceip";
    public static final String COLUMN_DEVICEPIN = "devicepin";
    public static final String COLUMN_MAINDEVICEIP="maindeviceip";
    public static final String COLUMN_DEVICESTATUS="devicestatus";

    public static final String USER_TABLE = "Users";
    public static final String USER_COLUMN_FULLNAME = "fullname";
    public static final String USER_COLUMN_EMAIL= "email";
    public static final String USER_COLUMN_PASSWORD= "password";

    public static final String VOCATIONMODE_TABLE="Vocationmode";
    public static final String VOCATIONMODE_STARTTIME = "starttime";
    public static final String VOCATIONMODE_MINUTES="modeminutes";
    public static final String VOCATIONMODE_STATUS="status";
    public static final String VOCATIONMODE_DEVICENAME="devicename";

    public static final String AUTOMODE_TABLE = "AutoMode";
    public static final String AUTOMODE_DEVICENAME = "devicename";
    public static final String AUTOMODE_DEVICEPIN = "devicepin";
        public static final String AUTOMODE_DEVICESTATUS="devicestatus";
    //We need to pass database information along to superclass


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String devicequery = "CREATE TABLE " + TABLE_DEVICES + "(" +
                COLUMN_DEVICENAME + " TEXT PRIMARY KEY," +
                COLUMN_DEVICEIP + " TEXT," +
                COLUMN_DEVICEPIN + " TEXT," +
                COLUMN_MAINDEVICEIP + " TEXT," +
                COLUMN_DEVICESTATUS + " TEXT" +
                ");";
        db.execSQL(devicequery);

        String userquery="CREATE TABLE " + USER_TABLE + "(" +
                USER_COLUMN_FULLNAME + " TEXT ," +
                USER_COLUMN_EMAIL + " TEXT PRIMARY KEY," +
                USER_COLUMN_PASSWORD + " TEXT" +
                ");";
      db.execSQL(userquery);

      String vocationmodequery= "CREATE TABLE " + VOCATIONMODE_TABLE + "(" +
              VOCATIONMODE_DEVICENAME + " TEXT PRIMARY KEY ," +
                VOCATIONMODE_STARTTIME + " TEXT," +
                VOCATIONMODE_MINUTES + " TEXT," +
                VOCATIONMODE_STATUS + " TEXT" +

            // "FOREIGN KEY("+VOCATIONMODE_DEVICENAME+")REFERENCES"+TABLE_DEVICES+"("+COLUMN_DEVICENAME+")" +


        ");";
        db.execSQL(vocationmodequery);

        String autoModeQuery = "CREATE TABLE " + AUTOMODE_TABLE + "(" +
                AUTOMODE_DEVICENAME + " TEXT PRIMARY KEY," +
                COLUMN_DEVICESTATUS + " TEXT" +
                ");";
        db.execSQL(autoModeQuery);


    }
    //Lesson 51
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        onCreate(db);
    }

    //Add a new row to the database
    public void addDevice(Devices device)
    {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        try
        {
            values.put(COLUMN_DEVICENAME, device.getDeviceName());
            values.put(COLUMN_DEVICEIP, device.getDeviceIp());
            values.put(COLUMN_DEVICEPIN, device.getDevicePin());
            values.put(COLUMN_MAINDEVICEIP, device.getMaindeviceIp());
            values.put(COLUMN_DEVICESTATUS, device.getDeviceStatus());
            db.insert(TABLE_DEVICES, null, values);
        }
        catch(Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }


    }


    public void deleteDevice(String deviceName){
        SQLiteDatabase db = getWritableDatabase();
        try
        {
            db.execSQL("DELETE FROM " + TABLE_DEVICES + " WHERE " + COLUMN_DEVICENAME + "=\"" + deviceName + "\";");
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }
    }



    public ArrayList<String> getDevicesName() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_DEVICES + " WHERE 1";// why not leave out the WHERE  clause?
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                if (recordSet.getString(recordSet.getColumnIndex("devicename")) != null) {
                    String devicename = recordSet.getString(recordSet.getColumnIndex("devicename"));
                    list.add(devicename);
                }
                recordSet.moveToNext();
            }
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }
        return list;
    } //getDevicesnames

    public String getDataOfDevices(){
        String tempgetdataofdevice = "";
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_DEVICES + " WHERE 1";// why not leave out the WHERE  clause?
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                if (recordSet.getString(recordSet.getColumnIndex("devicename")) != null) {
                    tempgetdataofdevice += recordSet.getString(recordSet.getColumnIndex("devicename"));
                    tempgetdataofdevice += ",";
                    tempgetdataofdevice += recordSet.getString(recordSet.getColumnIndex("deviceip"));
                    tempgetdataofdevice += ",";
                    tempgetdataofdevice += recordSet.getString(recordSet.getColumnIndex("devicepin"));
                    tempgetdataofdevice += ",";
                    tempgetdataofdevice += recordSet.getString(recordSet.getColumnIndex("maindeviceip"));
                    tempgetdataofdevice += ",";
                    tempgetdataofdevice += recordSet.getString(recordSet.getColumnIndex("maindeviceip"));
                    tempgetdataofdevice += ",";
                    tempgetdataofdevice += recordSet.getString(recordSet.getColumnIndex("devicestatus"));
                    tempgetdataofdevice += "\n";
                }
                recordSet.moveToNext();
            }
        }//try
        catch(Exception e)
        {
            Log.v("error",e.toString());
        }
        finally
        {
            db.close();
        }
        return tempgetdataofdevice;
    }

      public boolean checkDeviceName(String devicename)
      {
          boolean found=false;
          SQLiteDatabase db = getWritableDatabase();
          try {
              String query = "SELECT * FROM " + TABLE_DEVICES + " WHERE 1";
              Cursor recordSet = db.rawQuery(query, null);
              recordSet.moveToFirst();
              while (!recordSet.isAfterLast()) {
                  if (recordSet.getString(recordSet.getColumnIndex("devicename")) != null) {
                      if (recordSet.getString(recordSet.getColumnIndex("devicename")).equals(devicename)) {
                          found = true;
                          break;
                      }
                  }
                  recordSet.moveToNext();
              }
          }//try
          catch(Exception e)
          {
              Log.e("error",e.toString());
          }
          finally
          {
              db.close();
          }
          return found;
      }

    public String getDeviceIp(String devicenam)
    {
        String tempdeviceip = "";
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_DEVICES + " WHERE 1";
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                if (recordSet.getString(recordSet.getColumnIndex("devicename")) != null) {

                    if (recordSet.getString(recordSet.getColumnIndex("devicename")).equals(devicenam)) {

                        tempdeviceip = recordSet.getString(recordSet.getColumnIndex("deviceip"));
                        break;
                    }
                }
                recordSet.moveToNext();
            }
        }//try
        catch(Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }

        return tempdeviceip;
    }


    public String getDevicePin(String devicenam)
    {
        String tempdevicepin = "";
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_DEVICES + " WHERE 1";
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                if (recordSet.getString(recordSet.getColumnIndex("devicename")) != null) {
                    if (recordSet.getString(recordSet.getColumnIndex("devicename")).equals(devicenam)) {
                        tempdevicepin = recordSet.getString(recordSet.getColumnIndex("devicepin"));
                        break;
                    }
                }
                recordSet.moveToNext();
            }
        }//try
        catch(Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }

        return tempdevicepin;
    }

    public String getMainDeviceIP(String devicenam)
    {
        String tempmaindeviceip = "";
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_DEVICES + " WHERE 1";
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                if (recordSet.getString(recordSet.getColumnIndex("devicename")) != null) {
                    if (recordSet.getString(recordSet.getColumnIndex("devicename")).equals(devicenam)) {
                        tempmaindeviceip = recordSet.getString(recordSet.getColumnIndex("maindeviceip"));
                        break;
                    }
                }
                recordSet.moveToNext();
            }
        }//try
        catch(Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }
        return tempmaindeviceip;
    }

    public String getDeviceStatus(String devicenam)
    {
        String tempdevicestatus = "";
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_DEVICES + " WHERE 1";
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                if (recordSet.getString(recordSet.getColumnIndex("devicename")) != null) {
                    if (recordSet.getString(recordSet.getColumnIndex("devicename")).equals(devicenam)) {
                        tempdevicestatus = recordSet.getString(recordSet.getColumnIndex("devicestatus"));
                        break;
                    }
                }
                recordSet.moveToNext();
            }
        }//try
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }

        return tempdevicestatus;

    }


    public  void updateDeviceStatus( String devicenam,String devicestatus)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try
        {
            values.put(COLUMN_DEVICESTATUS,devicestatus);
            db.update(TABLE_DEVICES, values, ""+COLUMN_DEVICENAME+"= '"+ devicenam+"'", null);
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
       finally
        {
            db.close();
        }


    }




    //UserDatabase------------------------------------------------------------------------------------------------------

    public void addUser(String fullname,String email,String password) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        try {
            values.put(USER_COLUMN_FULLNAME, fullname);
            values.put(USER_COLUMN_EMAIL, email);
            values.put(USER_COLUMN_PASSWORD, password);
            db.insert(USER_TABLE, null, values);
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }

    }
    public String getPassword(String email)
    {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + USER_TABLE + " WHERE 1";
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                // null could happen if we used our empty constructor
                if (recordSet.getString(recordSet.getColumnIndex("email")) != null) {
                    if (recordSet.getString(recordSet.getColumnIndex("email")).equals(email)) {
                        dbString = recordSet.getString(recordSet.getColumnIndex("password"));
                        break;
                    }
                }
                recordSet.moveToNext();
            }
        }//try
        catch(Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }

        return dbString;
    }

    public boolean checkEmail(String email)
    {
        boolean found=false;
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + USER_TABLE + " WHERE 1";
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                if (recordSet.getString(recordSet.getColumnIndex("email")) != null) {
                    if (recordSet.getString(recordSet.getColumnIndex("email")).equals(email)) {
                        found = true;
                        break;
                    }
                }
                recordSet.moveToNext();
            }
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
        finally {
            db.close();
        }

        return found;
    }

    //vocation mode database-----------------------------------------------------------------------




    public void addVocationModeData(String deviceName,String startTime,String ModeMinutes,String Status) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        try {
            values.put(VOCATIONMODE_DEVICENAME, deviceName);
            values.put(VOCATIONMODE_STARTTIME, startTime);
            values.put(VOCATIONMODE_MINUTES, ModeMinutes);
            values.put(VOCATIONMODE_STATUS, Status);
            db.insert(VOCATIONMODE_TABLE, null, values);
        } catch (Exception e) {
            Log.e("error", e.toString());
        } finally {
            db.close();
        }

    }

    public  void updateVocationModeData( String deviceName,String startTime,String modeMinutes,String status)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try
        {
            values.put(VOCATIONMODE_DEVICENAME,deviceName);
            values.put(VOCATIONMODE_STARTTIME,startTime);
            values.put(VOCATIONMODE_MINUTES,modeMinutes);
            values.put(VOCATIONMODE_STATUS,status);
            db.update(VOCATIONMODE_TABLE, values, null, null);
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }


    }//updateVocationModeStatus
    public String getVocationModeStatus()
    {
        String vModeStatus = "";
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT * FROM " + VOCATIONMODE_TABLE + " WHERE 1";
            Cursor recordSet = db.rawQuery(query, null);
            recordSet.moveToFirst();
            while (!recordSet.isAfterLast()) {
                        vModeStatus = recordSet.getString(recordSet.getColumnIndex("status"));
                        recordSet.moveToNext();
            }
        }//try
        catch(Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }

        return vModeStatus;
    }

    public String getVocationModeDeviceName()
    {    String vModeDeviceName = "";
    SQLiteDatabase db = getWritableDatabase();
    try {
        String query = "SELECT * FROM " + VOCATIONMODE_TABLE + " WHERE 1";
        Cursor recordSet = db.rawQuery(query, null);
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            vModeDeviceName = recordSet.getString(recordSet.getColumnIndex("devicename"));
            recordSet.moveToNext();
        }
    }//try
    catch(Exception e)
    {
        Log.e("error",e.toString());
    }
    finally
    {
        db.close();
    }

    return vModeDeviceName;
    }//class

    public  void updateVocationModeStatus( String devicenam,String devicestatus)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try
        {
            values.put(VOCATIONMODE_STATUS,devicestatus);
            db.update(VOCATIONMODE_TABLE, values, ""+COLUMN_DEVICENAME+"= '"+ devicenam+"'", null);
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }


    }
     //AutoMode Database


    public  void updateAutoModeData( String deviceName,String devicePin,String status)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try
        {
            values.put(AUTOMODE_DEVICENAME,deviceName);
            values.put(AUTOMODE_DEVICEPIN,devicePin);
            values.put(AUTOMODE_DEVICESTATUS,status);
            db.update(AUTOMODE_TABLE, values, null, null);
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            db.close();
        }


    }

}















