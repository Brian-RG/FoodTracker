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

    public void Insert(String name, String description, float price, byte[] imgurl){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contenido = new ContentValues();
        contenido.put(FOOD_NAME,name);
        contenido.put(DESCRIPTION,description);
        contenido.put(PRICE,price);
        contenido.put(IMAGE,imgurl);

        db.insert(TABLE_NAME,null,contenido);
    }

    public int Delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        String clause = ID + " = ?";
        String[] args = {Integer.toString(id)};
        return db.delete(TABLE_NAME, clause, args);
    }

    public void update(int id,String newName, String newDescription, float newPrice,byte[] newImage){
        //To develop
        //To coordinate
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv= new ContentValues();
        cv.put(FOOD_NAME,newName);
        cv.put(DESCRIPTION,newDescription);
        cv.put(PRICE,newPrice);
        cv.put(IMAGE,newImage);
        String clause = ID + " = ?";
        String[] args = {Integer.toString(id)};
        db.update(TABLE_NAME,cv,clause,args);
    }

    public List<FoodRow> retrieveAll(){
        SQLiteDatabase db = getReadableDatabase();

        // Mejorar
        List<FoodRow> rows = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        if(c.moveToFirst()){
            do{
                int id = c.getInt(0);
                String foodname = c.getString(1);
                String foodDescription = c.getString(2);
                float foodPrice = c.getFloat(3);
                //String image = c.getString(4);
                FoodRow fr = new FoodRow(String.valueOf(id),foodname,foodDescription,foodPrice,c.getBlob(4));
                rows.add(fr);
            }
            while(c.moveToNext());
        }
        c.close();
        db.close();
        return rows;
    }

    public FoodRow Find(int id){
        SQLiteDatabase db = getReadableDatabase();
        FoodRow tmp = null;
        String Filter = ID + " = ?";
        String Args[]={Integer.toString(id)};
        Cursor c = db.query(TABLE_NAME,null,Filter,Args,null,null,null);
        if(c.moveToFirst()){
            String n = c.getString(1);
            String d = c.getString(2);
            float p = c.getFloat(3);
            tmp = new FoodRow(String.valueOf(id),n,d,p,c.getBlob(4));
        }
        return tmp;
    }


}
