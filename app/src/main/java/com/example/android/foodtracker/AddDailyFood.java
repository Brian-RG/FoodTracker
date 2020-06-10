package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDailyFood extends AppCompatActivity {

    DailyFoodDB db;

    private EditText dailynameInp,dailypriceInp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_food);

        db= new DailyFoodDB(this);

        dailynameInp = findViewById(R.id.daily_nameInput);
        dailypriceInp = findViewById(R.id.daily_priceInput);
    }

    public void addFood(View v){

        if(dailynameInp.getText().toString().isEmpty() || dailypriceInp.getText().toString().isEmpty() ){
            dailynameInp.setError("Food name is required");
            dailypriceInp.setError("Food price is required");
        }
        else{
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String datetoday= format.format(today);
            db.Insert(dailynameInp.getText().toString(),Float.parseFloat(dailypriceInp.getText().toString()),datetoday);
            finish();
        }
    }
}
