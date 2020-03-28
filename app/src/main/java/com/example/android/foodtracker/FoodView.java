package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FoodView extends AppCompatActivity {

    DBHelper db;
    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        db = new DBHelper(this);
        //String[] datos = {"Prueba 1", "Prueba 2"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datos);

        List<FoodRow> elements = db.retrieveAll();
        RVAdapter adapter = new RVAdapter(elements);
        rv = findViewById(R.id.foodView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

    }

    public void goToCreate(View v){
        Intent toCreate = new Intent(this,CreateFood.class);
        startActivity(toCreate);
    }

    public void editFoodRegister(View v){
        Intent intentEdit = new Intent(this,EditFoodActivity.class);
        TextView id = v.findViewById(R.id.food_id);
        intentEdit.putExtra("foodId",id.getText().toString().substring(1));//Getting string without the # character
        startActivity(intentEdit);
    }

}

