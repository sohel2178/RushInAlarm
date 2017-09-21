package com.example.sohel.rushinalarm.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sohel.rushinalarm.Model.AlarmData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sohel on 15-09-17.
 */

public class AlarmDatabase {
    private static final String TAG="AlarmDatabase";

    // Declare DB Field

    private static final String KEY_ROW_ID="id";
    private static final String KEY_ROW_TIME="time";
    private static final String KEY_ROW_NOTE="note";
    private static final String KEY_ROW_ISSET="isset";
    private static final String KEY_ROW_REPEAT_DAYS="repeat_days";
    private static final String KEY_ROW_VOLUME="volume";
    private static final String KEY_ROW_SOUND_ID="sound_id";
    private static final String KEY_ROW_VIBRATION="vibration";
    private static final String KEY_ROW_FADE_IN="fade_in";
    private static final String KEY_ROW_SNOOZE_DURATION="snooze_duration";

    private static final String[] ALL_KEYS={
            KEY_ROW_ID,
            KEY_ROW_TIME,
            KEY_ROW_NOTE,
            KEY_ROW_ISSET,
            KEY_ROW_REPEAT_DAYS,
            KEY_ROW_VOLUME,
            KEY_ROW_SOUND_ID,
            KEY_ROW_VIBRATION,
            KEY_ROW_FADE_IN,
            KEY_ROW_SNOOZE_DURATION
    };

    // Db Name and Table Name
    private static final String DB_NAME="AlarmDb";
    private static final String TABLE_NAME="AlarmTable";

    // Db Version
    private static final int DATABASE_VERSION=1;

    private static final String DATABASE_CREATE_SQL="create table "+TABLE_NAME
            +"("+KEY_ROW_ID+" integer primary key autoincrement, "
            +KEY_ROW_TIME+" string not null, "
            +KEY_ROW_NOTE+" string not null, "
            +KEY_ROW_ISSET+" int not null, "
            +KEY_ROW_REPEAT_DAYS+" string not null, "
            +KEY_ROW_VOLUME+" int not null, "
            +KEY_ROW_SOUND_ID+" int not null, "
            +KEY_ROW_VIBRATION+" int not null, "
            +KEY_ROW_FADE_IN+" int not null, "
            +KEY_ROW_SNOOZE_DURATION+" int not null"
            +");";


    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public AlarmDatabase(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public AlarmDatabase open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long insertRow(String time, String note, int isSet, String repeateDays, int volume, int soundId, int vibration, int fadeIn, int snoozeDurationInMin){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ROW_TIME,time);
        contentValues.put(KEY_ROW_NOTE,note);
        contentValues.put(KEY_ROW_ISSET,isSet);
        contentValues.put(KEY_ROW_REPEAT_DAYS,repeateDays);
        contentValues.put(KEY_ROW_VOLUME,volume);
        contentValues.put(KEY_ROW_SOUND_ID,soundId);
        contentValues.put(KEY_ROW_VIBRATION,vibration);
        contentValues.put(KEY_ROW_FADE_IN,fadeIn);
        contentValues.put(KEY_ROW_SNOOZE_DURATION,snoozeDurationInMin);

        return db.insert(TABLE_NAME,null,contentValues);
    }

    // Insert Direct Object
    public long insertRow(AlarmData data){
        return insertRow(data.getTime(),data.getNote(),data.getIsSet(),data.getRepeateDays(),data.getVolume(),data.getSoundId(),data.getVibration(),data.getFadeIn(),data.getSnoozeDurationInMin());
    }

    public boolean deleteAlarmData(int id){
        return deleteRow(id);
    }

    private boolean deleteRow(long rowId){
        String where = KEY_ROW_ID+"="+rowId;
        return db.delete(TABLE_NAME,where,null) != 0;
    }

    // Get All Rows as Cursor
    private Cursor getAllRows(){
        String where = null;
        Cursor c=db.query(true,TABLE_NAME,ALL_KEYS,where,null,null,null,null,null);

        if(c!= null){
            c.moveToFirst();
        }
        return c;
    }

    public List<AlarmData> getAllAlarmData(){

        List<AlarmData> alarmDataList = new ArrayList<>();

        Cursor cursor = getAllRows();

        if(cursor.moveToFirst()){
            do{
                AlarmData data = new AlarmData(
                        cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                        cursor.getInt(3),cursor.getString(4),cursor.getInt(5),
                        cursor.getInt(6),cursor.getInt(7),cursor.getInt(8),cursor.getInt(9)
                );
                alarmDataList.add(data);

            }while (cursor.moveToNext());
        }

        return alarmDataList;

    }

    public void deleteAll(){

        Cursor c = getAllRows();

        int rowId = c.getColumnIndexOrThrow(KEY_ROW_ID);

        if(c.moveToFirst()){
            do{
                deleteRow(c.getLong(rowId));
            }while (c.moveToNext());
        }

    }

    public AlarmData getAlarmDataById(int id){
        List<AlarmData> alarmDataList = new ArrayList<>();

        Cursor cursor = getRow(id);

        if(cursor.moveToFirst()){
            do{
                AlarmData data = new AlarmData(
                        cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                        cursor.getInt(3),cursor.getString(4),cursor.getInt(5),
                        cursor.getInt(6),cursor.getInt(7),cursor.getInt(8),cursor.getInt(9)
                );
                alarmDataList.add(data);

            }while (cursor.moveToNext());
        }

        return alarmDataList.get(0);


    }

    public Cursor getRow(long rowId){
        String where = KEY_ROW_ID+"="+rowId;
        Cursor c = db.query(true,TABLE_NAME,ALL_KEYS,where,null,null,null,null,null);

        if(c!= null){
            c.moveToFirst();
        }
        return c;
    }

    private boolean updateRow(int id,String time, String note, int isSet, String repeateDays, int volume, int soundId, int vibration, int fadeIn, int snoozeDurationInMin){
        String where= KEY_ROW_ID+"="+id;

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ROW_TIME,time);
        contentValues.put(KEY_ROW_NOTE,note);
        contentValues.put(KEY_ROW_ISSET,isSet);
        contentValues.put(KEY_ROW_REPEAT_DAYS,repeateDays);
        contentValues.put(KEY_ROW_VOLUME,volume);
        contentValues.put(KEY_ROW_SOUND_ID,soundId);
        contentValues.put(KEY_ROW_VIBRATION,vibration);
        contentValues.put(KEY_ROW_FADE_IN,fadeIn);
        contentValues.put(KEY_ROW_SNOOZE_DURATION,snoozeDurationInMin);

        long bal = db.update(TABLE_NAME,contentValues,where,null);

        Log.d("DDDD","Update row ID "+bal);

        return bal !=0;
    }

    public boolean updateAlarmData(AlarmData data){
        return updateRow(data.getId(),data.getTime(),data.getNote(),data.getIsSet(),data.getRepeateDays(),
                data.getVolume(),data.getSoundId(),data.getVibration(),data.getFadeIn(),data.getSnoozeDurationInMin());
    }






    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // Destroy old database:
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            // Recreate new database:
            onCreate(db);

        }
    }



}
