package com.example.android.foodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTV, passwordTV;
    private Button regBtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        initializeUI();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);

        String email, password;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*Map<String,String> initialData = new HashMap<>();
                            initialData.put("userId", FirebaseAuth.getInstance().getUid());
                            FirebaseFirestore.getInstance().collection("favoritos")
                                    .add(initialData);*/
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void initializeUI() {
        emailTV = findViewById(R.id.registerEmail);
        passwordTV = findViewById(R.id.registerPassword);
        regBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.registerProgressBar);
    }
}
