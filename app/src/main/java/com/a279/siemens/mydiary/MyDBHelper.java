package com.a279.siemens.mydiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Diary";

    private static final String TABLE_DIARY = "Diary";
    private static final String KEY_ID = "ids";
    private static final String KEY_TEMA = "tema";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "date";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_DIARY_TABLE = "CREATE TABLE " + TABLE_DIARY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TEMA + " TEXT,"
                + KEY_TEXT + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_DIARY_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY);
        //Log.d("MyLog", "Restart BD");
        onCreate(sqLiteDatabase);
    }
    public void addDiar(Diar diar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, diar.getId());
        values.put(KEY_TEMA, diar.getTema());
        values.put(KEY_TEXT, diar.getText());
        values.put(KEY_DATE, diar.getDate());
        db.insert(TABLE_DIARY, null, values);
        db.close();
    }
    public Diar getDiar(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DIARY, new String[] { KEY_ID, KEY_TEMA, KEY_TEXT, KEY_DATE }, KEY_ID + "=?", new String[] { id }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Diar drug = new Diar(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return drug;
    }
    public ArrayList<Diar> getAllDiar() {
        ArrayList<Diar> drugList = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+TABLE_DIARY+" ORDER BY ids DESC"; //DESC
        //Log.d("MyLog", ""+selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Diar diar = new Diar();
                diar.setId(cursor.getInt(0));
                diar.setTema(cursor.getString(1));
                diar.setText(cursor.getString(2));
                diar.setDate(cursor.getString(3));
                drugList.add(diar);
            } while (cursor.moveToNext());
        }
        db.close();
        return drugList;
    }
    public int getCountDiar() {
        String countQuery = "SELECT * FROM " + TABLE_DIARY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Integer count = cursor.getCount();
        cursor.close();
        return count;
    }
    public void deleteDiar(Diar diar) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIARY, KEY_ID + " = ?", new String[] { String.valueOf(diar.getId()) });
        db.close();
    }
    public void deleteAllDiar() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIARY, null, null);
        db.close();
    }
    public ArrayList<Diar> findDiarByText(String find) {
        ArrayList<Diar> diarList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " +TABLE_DIARY +" WHERE "+KEY_TEXT+" LIKE '%"+find+"%' ORDER BY 'name' ASC"; //+ clients WHERE client_data LIKE '%Mark%' ORDER BY `client_id` ASC
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Diar diar = new Diar();
                diar.setId(cursor.getInt(0));
                diar.setTema(cursor.getString(1));
                diar.setText(cursor.getString(2));
                diar.setDate(cursor.getString(3));
                diarList.add(diar);
            } while (cursor.moveToNext());
        }
        db.close();
        return diarList;
    }
    public Boolean findDiarById(Integer id) {
        String selectQuery = "SELECT * FROM " +TABLE_DIARY +" WHERE "+KEY_ID+" = "+id; //+" ORDER BY name ASC"; //+ clients WHERE client_data LIKE '%Mark%' ORDER BY `client_id` ASC
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }
    public Integer updateDiarById(Diar diar) {
        //String selectQuery = "SELECT * FROM " +TABLE_DIARY +" WHERE "+KEY_ID+" LIKE '%"+id+"%' ORDER BY 'name' ASC"; //+ clients WHERE client_data LIKE '%Mark%' ORDER BY `client_id` AS

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("ids", diar.getId());
        data.put("tema", diar.getTema());
        data.put("text", diar.getText());
        data.put("date", diar.getDate());
        Integer b = db.update(TABLE_DIARY, data, KEY_ID+"=" + diar.getId(), null);
        db.close();
        /*Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }*/
        return b;
    }

    public void deleteAllBd() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIARY, null, null);
        db.close();
    }

}