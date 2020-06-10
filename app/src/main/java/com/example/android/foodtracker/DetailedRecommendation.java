package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedRecommendation extends AppCompatActivity {

    TextView name,description, price;
    ImageView img;

    String foodName,foodDes,foodPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_recommendation);
        Intent opener = getIntent();
        foodName = opener.getStringExtra("name");
        foodDes = opener.getStringExtra("description");
        foodPrice = (opener.getFloatExtra("price",-1) + "");

        name = findViewById(R.id.name_info);
        description=findViewById(R.id.description_info);
        price = findViewById(R.id.price_info);
        img = findViewById(R.id.image_info);

        name.setText(foodName);
        description.setText(foodDes);
        price.setText(foodPrice);
        Bitmap bmp = BitmapFactory.decodeByteArray(opener.getByteArrayExtra("img"),0,opener.getByteArrayExtra("img").length);
        img.setImageBitmap(bmp);
    }
}
