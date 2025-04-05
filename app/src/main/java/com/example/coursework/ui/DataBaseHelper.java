package com.example.coursework.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coursework.YogaClassData;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "yogaClassDB";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(YogaClassData.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + YogaClassData.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public YogaClassData getYogaClass(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(YogaClassData.TABLE_NAME,
                new String[]{
                        YogaClassData.COLUMN_ID,
                        YogaClassData.COLUMN_DAY,
                        YogaClassData.COLUMN_DURATION,
                        YogaClassData.COLUMN_NUMBER_OF_PEOPLE,
                        YogaClassData.COLUMN_PRICE,
                        YogaClassData.COLUMN_CLASS_TYPE,
                        YogaClassData.COLUMN_DESCRIPTION},

                YogaClassData.COLUMN_ID + "=?",
                new String[]{
                        String.valueOf(id)
                },
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") YogaClassData yogaClassData = new YogaClassData(
                    cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_DAY)),
                    cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_DURATION)),
                    cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_NUMBER_OF_PEOPLE)),
                    cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndex(YogaClassData.COLUMN_CLASS_TYPE)),
                    cursor.getString(cursor.getColumnIndex(YogaClassData.COLUMN_DESCRIPTION))
            );
            cursor.close();
            return yogaClassData;
        } else {
            return null;
        }
    }


    //geting data
    @SuppressLint("Range")
    public ArrayList<YogaClassData> getAllYogaClasses() {
        ArrayList<YogaClassData> yogaClassDatas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + YogaClassData.TABLE_NAME + " ORDER BY " +
                YogaClassData.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                YogaClassData yogaClassData = new YogaClassData();

                yogaClassData.setId(cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_ID)));
                yogaClassData.setDay(cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_DAY)));
                yogaClassData.setDuration(cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_DURATION)));
                yogaClassData.setNumberOfPeople(cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_NUMBER_OF_PEOPLE)));
                yogaClassData.setPrice(cursor.getInt(cursor.getColumnIndex(YogaClassData.COLUMN_PRICE)));
                yogaClassData.setClassType(cursor.getString(cursor.getColumnIndex(YogaClassData.COLUMN_CLASS_TYPE)));
                yogaClassData.setDescription(cursor.getString(cursor.getColumnIndex(YogaClassData.COLUMN_DESCRIPTION)));
                yogaClassDatas.add(yogaClassData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return yogaClassDatas;
    }

    //insert data
    public long insertYogaClass(int day, int duration, int numberOfPeople, int price, String classType, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(YogaClassData.COLUMN_DAY, day);
        values.put(YogaClassData.COLUMN_DURATION, duration);
        values.put(YogaClassData.COLUMN_NUMBER_OF_PEOPLE, numberOfPeople);
        values.put(YogaClassData.COLUMN_PRICE, price);
        values.put(YogaClassData.COLUMN_CLASS_TYPE, classType);
        values.put(YogaClassData.COLUMN_DESCRIPTION, description);

        long id = db.insert(YogaClassData.TABLE_NAME, null, values);
        db.close();
        return id;
    }
    public void updateYogaClass(YogaClassData yogaClassData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(YogaClassData.COLUMN_DAY, yogaClassData.getDay());
        values.put(YogaClassData.COLUMN_DURATION, yogaClassData.getDuration());
        values.put(YogaClassData.COLUMN_NUMBER_OF_PEOPLE, yogaClassData.getNumberOfPeople());
        values.put(YogaClassData.COLUMN_PRICE, yogaClassData.getPrice());
        values.put(YogaClassData.COLUMN_CLASS_TYPE, yogaClassData.getClassType());
        values.put(YogaClassData.COLUMN_DESCRIPTION, yogaClassData.getDescription());
        int rowsUpdated = db.update(YogaClassData.TABLE_NAME, values, YogaClassData.COLUMN_ID + "=?",
                new String[]{String.valueOf(yogaClassData.getId())});
        db.close();
    }
    public void deleteYogaClass(YogaClassData yogaClassData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(YogaClassData.TABLE_NAME, YogaClassData.COLUMN_ID + "=?",
                new String[]{String.valueOf(yogaClassData.getId())});
        db.close();
    }
}