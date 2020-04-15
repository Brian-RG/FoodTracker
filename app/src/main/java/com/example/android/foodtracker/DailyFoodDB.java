package com.example.android.foodtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DailyFoodDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "Daily_Food.db";
    private static final String TABLE_NAME= "Food_per_day";
    private static final String ID= "ID";
    private static final String FOOD_NAME= "NAME";
    private static final String PRICE= "PRICE";
    private static final String DATE= "DATE";

    public DailyFoodDB(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                FOOD_NAME + " TEXT, " +
                PRICE + " FLOAT, "+
                DATE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = "DROP TABLE IF EXISTS ?";
        String[] params = {TABLE_NAME};
        db.execSQL(query, params);
    }

    public void Insert(String name, float price, String date){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contenido = new ContentValues();
        contenido.put(FOOD_NAME,name);
        contenido.put(PRICE,price);
        contenido.put(DATE,date);

        db.insert(TABLE_NAME,null,contenido);
    }

    public int Delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        String clause = ID + " = ?";
        String[] args = {Integer.toString(id)};
        return db.delete(TABLE_NAME, clause, args);
    }

    public void update(int id,String newName, float newPrice, String datenew){
        //To develop
        //To coordinate
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv= new ContentValues();
        cv.put(FOOD_NAME,newName);
        cv.put(PRICE,newPrice);
        cv.put(DATE,datenew);
        String clause = ID + " = ?";
        String[] args = {Integer.toString(id)};
        db.update(TABLE_NAME,cv,clause,args);
    }




    public List<foodItem> retrieveAll(){
        SQLiteDatabase db = getReadableDatabase();

        // Mejorar
        List<foodItem> rows = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                int id = c.getInt(0);
                String foodname = c.getString(1);
                float foodPrice = c.getFloat(2);
                String date = c.getString(3);
                foodItem fi= new foodItem(id,foodname,foodPrice,date);
                rows.add(fi);
            }
            while(c.moveToNext());
        }
        c.close();
        db.close();
        return rows;
    }

    public List<foodItem> retrieveByDate(String date){

        List<foodItem> rows = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String Filter = DATE + " = ?";
        String Args[]={date};
        Cursor c = db.query(TABLE_NAME,null,Filter,Args,null,null,null);
        if(c.moveToFirst()){
            do{
            int id = c.getInt(0);
            String n = c.getString(1);
            float p = c.getFloat(2);
            String d = c.getString(3);
            foodItem tmp= new foodItem(id,n,p,date);
            rows.add(tmp);}
            while(c.moveToNext());
        }

        return rows;
    }


    public foodItem Find(int id){
        SQLiteDatabase db = getReadableDatabase();
        foodItem tmp = null;
        String Filter = ID + " = ?";
        String Args[]={Integer.toString(id)};
        Cursor c = db.query(TABLE_NAME,null,Filter,Args,null,null,null);
        if(c.moveToFirst()){
            String n = c.getString(1);
            float p = c.getFloat(2);
            String date = c.getString(3);
            tmp= new foodItem(id,n,p,date);
        }
        return tmp;
    }


}
