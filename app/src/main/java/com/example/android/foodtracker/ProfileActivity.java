package com.example.android.foodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.foodtracker.ui.main.RecommendationsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, age, email;
    private Bitmap profilePic;
    private ImageView profile;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference ref;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        name = findViewById(R.id.nameText);
        age = findViewById(R.id.ageText);
        email = findViewById(R.id.mailText);
        profile = findViewById(R.id.profilePic);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        ref = storage.getReference();
        db = FirebaseFirestore.getInstance();
        
        fillSpaces();
    }

    private void fillSpaces() {
        db.collection("userData").whereEqualTo("uid", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot userData = task.getResult().getDocuments().get(0);
                            String userName = userData.getData().get("name").toString();
                            String userAge = userData.getData().get("age").toString();
                            String userMail = mAuth.getCurrentUser().getEmail();
                            String img = userData.getData().get("imageName").toString();
                            name.setText(userName);
                            age.setText(userAge);
                            email.setText(userMail);

                            getProfilePic(img);
                        } else {
                            Log.w("Error:", task.getException());
                        }
                    }
                });
    }

    private void getProfilePic(String img) {
        StorageReference storeRef = ref.child("images/"+img);
        Log.wtf("ByteArray", "images/"+img);

        final long TEN_MEGABYTE = 1024 * 1024 * 10;
        storeRef.getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                profilePic = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profile.setImageBitmap(profilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Could not fetch Profile Picture", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToEditProfile(View v) {
        Intent EditProfileIntent = new Intent(this, EditProfileActivity.class);
        startActivity(EditProfileIntent);
    }

    public void deleteProfile(View v) {
        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intentito = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intentito);
                } else {
                    Log.wtf("Hola", "Mundo");
                }

            }
        });
    }
}
