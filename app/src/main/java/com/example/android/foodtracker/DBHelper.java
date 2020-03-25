package com.example.android.foodtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "Food_Tracker.db";
    private static final String TABLE_NAME= "Food";
    private static final String ID= "ID";
    private static final String FOOD_NAME= "NAME";
    private static final String DESCRIPTION= "DESCRIPTION";
    private static final String PRICE= "PRICE";
    private static final String IMAGE= "IMAGE";


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                FOOD_NAME + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                PRICE + " FLOAT, " +
                IMAGE + " BLOB );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = "DROP TABLE IF EXISTS ?";
        String[] params = {TABLE_NAME};
        db.execSQL(query, params);
    }

    public void Insert(String name, String description, float price, byte[] img){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contenido = new ContentValues();
        contenido.put(FOOD_NAME,name);
        contenido.put(DESCRIPTION,description);
        contenido.put(PRICE,price);
        contenido.put(IMAGE,img);

        db.insert(TABLE_NAME,null,contenido);
    }

    public int Delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        String clause = ID + " = ?";
        String[] args = {Integer.toString(id)};
        return db.delete(TABLE_NAME, clause, args);
    }

    public void update(int id){
        //To develop
        //To coordinate
        /*
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv= new ContentValues();
        cv.put(,hob);
        String clause = NAME + " = ?";
        String[] args = {"Brian"};
        db.update(TABLE_NAME,cv,clause,args);*/
    }

    public List<FoodRow> retrieveAll(){
        SQLiteDatabase db = getReadableDatabase();

        // Mejorar
        List<FoodRow> rows = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        c.moveToFirst();
        while(c!=null){
            int id = c.getInt(0);
            String foodname = c.getString(1);
            String foodDescription = c.getString(2);
            float foodPrice = c.getFloat(3);
            byte[] image = c.getBlob(4);
            FoodRow fr = new FoodRow(id,foodname,foodDescription,foodPrice,image);
            rows.add(fr);
        }
        c.close();
        db.close();
        return rows;
    }


}
