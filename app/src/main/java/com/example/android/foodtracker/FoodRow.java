package com.example.android.foodtracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.BitSet;

public class FoodRow {
    private int id;
    private String name;
    private String description;
    private float price;
    private byte[] imgurl;

    public FoodRow(){
        this(-1,"","",-1,null);
    }

    public FoodRow(int id, String name, String description, float price, byte[] img){
        this.id=id;
        this.name=name;
        this.description=description;
        this.price=price;
        this.imgurl=img;
    }

    public void setName(String newName){
        this.name=newName;
    }
    public void setDescription(String newDescription){
        this.description=newDescription;
    }
    public void setPrice(float newPrice){
        this.price=newPrice;
    }
    public void setImg(byte[] newImg) {
        this.imgurl=newImg;
    }

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public float getPrice(){
        return this.price;
    }
    public byte[] getImg(){
        return this.imgurl;
    }

    public Bitmap getImgBitMap(){
        if (this.imgurl!=null){
            return BitmapFactory.decodeByteArray(this.imgurl, 0, this.imgurl.length);
        }
        return null;
    }
}
