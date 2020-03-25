package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FoodView extends AppCompatActivity {

    DBHelper db;
    ListView Lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        db = new DBHelper(this);
        String[] datos = {"Prueba 1", "Prueba 2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datos);


        Lista=findViewById(R.id.Food_list);
        Lista.setAdapter(adapter);
    }

}

