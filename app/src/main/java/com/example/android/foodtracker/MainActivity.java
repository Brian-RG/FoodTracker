package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    public void goToFoodActivity(View v){
        Intent FoodViewIntent = new Intent(this,DailyFoodView.class);
        startActivity(FoodViewIntent);
    }

    public void goToExpensesActivity(View v){
        Intent ExpensesIntent = new Intent(this,ExpensesActivity.class);
        startActivity(ExpensesIntent);
    }

    public void goToRecommendations(View v){
        Intent RecommendationsIntent = new Intent(this,RecommendationsActivity.class);
        startActivity(RecommendationsIntent);
    }

    public void goToProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void signOut(View v){
        mAuth.signOut();
        Intent LoginAct = new Intent(this, LoginActivity.class);
        startActivity(LoginAct);
        finish();
    }

}
