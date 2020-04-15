package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToFoodActivity(View v){
        Intent FoodViewIntent = new Intent(this,DailyFoodView.class);
        startActivity(FoodViewIntent);
    }

    public void goToExpensesActivity(View v){
        Intent ExpensesIntent = new Intent(this,ExpensesActivity.class);
        startActivity(ExpensesIntent);
    }

}
