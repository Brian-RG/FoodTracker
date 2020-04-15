package com.example.android.foodtracker;


public class foodItem {
    private int id;
    private String name;
    private float price;
    private String date;

    public foodItem(){
        this(-1,"",-1,"");
    }

    public foodItem(int id, String name, float price, String date){
        this.id=id;
        this.name=name;
        this.price=price;
        this.date=date;
    }

    public void setName(String newName){
        this.name=newName;
    }
    public void setPrice(float newPrice){
        this.price=newPrice;
    }
    public void setDate(String date){this.date=date;}

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public float getPrice(){
        return this.price;
    }
    public String getDate(){return this.date;}
}
