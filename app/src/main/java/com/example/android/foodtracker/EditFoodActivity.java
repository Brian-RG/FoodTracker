package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditFoodActivity extends AppCompatActivity {

    DBHelper db;

    private int RESULT_NEW_IMG;
    int foodId;
    EditText nameEdit,descriptionEdit,priceEdit;
    ImageView foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        Intent opener = getIntent();
        foodId = Integer.parseInt(opener.getStringExtra("foodId"));
        Log.wtf("id",""+foodId);
        //Initializing database
        db = new DBHelper(this);

        nameEdit = findViewById(R.id.Name_Edit_Field);
        descriptionEdit = findViewById(R.id.Description_Edit_Field);
        priceEdit = findViewById(R.id.Price_Edit_Field);
        foodImage = findViewById(R.id.Image_Edit);

        FoodRow cur = db.Find(foodId);

        nameEdit.setText(cur.getName());
        descriptionEdit.setText(cur.getDescription());
        priceEdit.setText(cur.getPrice()+"");
        foodImage.setImageBitmap(cur.getImgBitMap());
    }

    public void deleteFoodRegister(View view){
        db.Delete(foodId);
        Toast.makeText(this,"Food deleted",Toast.LENGTH_SHORT).show();
        finish();
    }



    public void updateFoodRegister(View view){
        String newName=nameEdit.getText().toString();
        String newDesc=descriptionEdit.getText().toString();
        float newPrice=Float.parseFloat(priceEdit.getText().toString());
        Bitmap new_img=( (BitmapDrawable)(foodImage.getDrawable()) ).getBitmap();
        db.update(foodId,newName,newDesc,newPrice,CreateFood.getBitmapAsByteArray(new_img)); // This method is implemented in CreateFood.java
        Toast.makeText(this,"Food edited",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void changeImage(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_NEW_IMG);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                Bitmap bitmap =
                        MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                foodImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

}
