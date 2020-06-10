package com.example.android.foodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.Map;

public class EditFoodActivity extends AppCompatActivity {

    FirebaseFirestore db;

    private int RESULT_NEW_IMG;
    String foodId;
    EditText nameEdit,descriptionEdit,priceEdit;
    ImageView foodImage;
    String currentName, currentDescription;
    float currentPrice;
    Context context;

    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        Intent opener = getIntent();
        foodId = opener.getStringExtra("id");

        currentName = opener.getStringExtra("name");
        currentDescription = opener.getStringExtra("description");
        currentPrice = opener.getFloatExtra("price", -1);

        //Initializing database
        db = FirebaseFirestore.getInstance();
        docRef= db.collection("recomendaciones").document(foodId);
        context = this.getApplicationContext();

        nameEdit = findViewById(R.id.Name_Edit_Field);
        descriptionEdit = findViewById(R.id.Description_Edit_Field);
        priceEdit = findViewById(R.id.Price_Edit_Field);
        foodImage = findViewById(R.id.Image_Edit);


        nameEdit.setText(currentName);
        descriptionEdit.setText(currentDescription);
        priceEdit.setText(currentPrice+"");
        //Bitmap image = BitmapFactory.decodeByteArray(opener.getByteArrayExtra("img"),0 , opener.getByteArrayExtra("img").length);
        //foodImage.setImageBitmap(image);
    }

    public void deleteFoodRegister(View view){
        nameEdit.setEnabled(false);
        descriptionEdit.setEnabled(false);
        priceEdit.setEnabled(false);
        foodImage.setEnabled(false);

        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Successfully deleted",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateFoodRegister(View view){

        if(nameEdit.getText().toString().isEmpty()){
            nameEdit.setError("Can't be empty!");
            return ;
        }
        if(priceEdit.getText().toString().isEmpty()){
            priceEdit.setError("Can't be empty!");
            return ;
        }

        nameEdit.setEnabled(false);
        descriptionEdit.setEnabled(false);
        priceEdit.setEnabled(false);
        foodImage.setEnabled(false);
        findViewById(R.id.save_button).setEnabled(false);
        findViewById(R.id.delete_button).setEnabled(false);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) {
                transaction.update(docRef, "NAME", nameEdit.getText().toString());
                transaction.update(docRef, "DESCRIPTION", descriptionEdit.getText().toString());
                transaction.update(docRef, "PRICE", priceEdit.getText().toString());
                Bitmap img= ((BitmapDrawable) (foodImage.getDrawable())).getBitmap();
                byte[] data = CreateFood.getBitmapAsByteArray(img);
                String image_string = Base64.encodeToString(data, Base64.DEFAULT);
                transaction.update(docRef,"IMAGE", image_string);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Edited successfully", Toast.LENGTH_SHORT);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        });
    }

    /*
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
    */
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
