package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FoodView extends AppCompatActivity {

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        db = new DBHelper(this);
    }


}
