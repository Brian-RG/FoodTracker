package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateFood extends AppCompatActivity {


    DBHelper db;

    private int RESULT_LOAD_IMG;
//   private String name,description;
//    float price;
    byte[] imagedata;
    Bitmap bitmap;

    private EditText nameInp,descriptionInp,priceInp,imageInp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);
        db = new DBHelper(this);
        nameInp = findViewById(R.id.nameInput);
        descriptionInp = findViewById(R.id.descriptionInput);
        priceInp = findViewById(R.id.priceInput);

        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.defaultimg);
        bitmap = bd.getBitmap();
        imagedata = getBitmapAsByteArray(bitmap);
    }


    public void loadImage(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                bitmap =
                        MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //bitmap = Bitmap.createScaledBitmap(bitmap,60,80,false);
                //Code to convert bitmap into BLOB

                imagedata = getBitmapAsByteArray(bitmap); // this is a function

                TextView image_status= findViewById(R.id.imageStatus);
                image_status.setText("Image loaded succesfully");
                //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    public void addToDatabase(View v){

        if(nameInp.getText().toString().isEmpty() && priceInp.getText().toString().isEmpty() ){
            nameInp.setError("Food name is required");
            priceInp.setError("Food price is required");
        }
        else{
        db.Insert(nameInp.getText().toString(),descriptionInp.getText().toString(),Float.parseFloat(priceInp.getText().toString()), imagedata);
        finish();
        }
    }
}
