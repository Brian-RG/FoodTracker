package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditDailyFood extends AppCompatActivity {

    DailyFoodDB db;

    int foodId;
    EditText daily_nameEdit,daily_priceEdit;
    private String curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_food);
        Intent opener = getIntent();
        foodId = Integer.parseInt(opener.getStringExtra("foodId"));

        db= new DailyFoodDB(this);

        daily_nameEdit = findViewById(R.id.Daily_Name_Edit_Field);
        daily_priceEdit = findViewById(R.id.Daily_Price_Edit_Field);

        foodItem cur = db.Find(foodId);

        daily_nameEdit.setText(cur.getName());
        daily_priceEdit.setText(cur.getPrice()+"");
        curDate=cur.getDate();
    }

    public void deleteFood(View view){
        db.Delete(foodId);
        Toast.makeText(this,"Food deleted",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void updateFood(View view){

        if(daily_nameEdit.getText().toString().isEmpty() || daily_priceEdit.getText().toString().isEmpty()){
            daily_nameEdit.setError("Food name is required");
            daily_priceEdit.setError("Price is required");
        }
        else{
            String newName=daily_nameEdit.getText().toString();
            float newPrice=Float.parseFloat(daily_priceEdit.getText().toString());
            db.update(foodId,newName,newPrice,curDate);
            Toast.makeText(this,"Food edited",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
