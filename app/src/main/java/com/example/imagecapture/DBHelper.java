package com.example.imagecapture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    public static final String MY_TABLE = "MY_TABLE";
    public static final String COL_ID = "ID";
    public static final String COL_IMAGE_DATA = "IMAGE_DATA";

    public DBHelper(@Nullable Context context) {
        super(context, "images.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_query = "CREATE TABLE " + MY_TABLE + " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_IMAGE_DATA + " BLOB )";
        db.execSQL(create_table_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addImageToDB (Bitmap img) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG,0,outputStream);
        byte[] imgByteData = outputStream.toByteArray();


        ContentValues cv = new ContentValues();
        cv.put(COL_IMAGE_DATA, imgByteData);

        long insert = db.insert(MY_TABLE, null, cv);
        outputStream.close();

        if(insert == -1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }

    public Bitmap getImage(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String get_image_query = "SELECT * FROM " + MY_TABLE + " WHERE " + COL_ID + " = " + id;

        Cursor curosr = db.rawQuery(get_image_query,null);

        if(curosr.moveToFirst()){
            byte[] imageByteData = curosr.getBlob(1);
            curosr.close();
            return BitmapFactory.decodeByteArray(imageByteData,0,imageByteData.length);
        }

        return null;
    }

    public ArrayList<Bitmap> getAllImages(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Bitmap> reuslt = new ArrayList<>();

        String get_image_query = "SELECT * FROM " + MY_TABLE ;

        Cursor cursor = db.rawQuery(get_image_query,null);

        if(cursor.moveToFirst()){
            do{
                byte[] imageByteData = cursor.getBlob(1);
                reuslt.add(BitmapFactory.decodeByteArray(imageByteData,0,imageByteData.length));
            }while (cursor.moveToNext());

        }

        cursor.close();
        return reuslt;
    }


}
