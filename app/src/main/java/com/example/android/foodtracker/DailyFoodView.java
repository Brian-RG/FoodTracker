package com.example.android.foodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DailyFoodView extends AppCompatActivity {

    DailyFoodDB mydb;
    RecyclerView foodList;
    TextView emptyV, available_budget;
    EditText totalBudget;
    Button confirmBudget;
    Context context;

    Calendar c;
    DatePickerDialog dpd;
    String dateSelection;
    String budget;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_food_view);
        mydb= new DailyFoodDB(this);
        c = Calendar.getInstance();
        emptyV = findViewById(R.id.emptyView);
        foodList = findViewById(R.id.daily_food_list);
        available_budget = findViewById(R.id.available_budget);
        totalBudget = findViewById(R.id.Budget_Edit_Field);
        confirmBudget = findViewById(R.id.confirm_budget);

        context = getApplicationContext();
        db = FirebaseFirestore.getInstance();


        emptyV.setVisibility(View.GONE);
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        dateSelection= format.format(today);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        foodList.setLayoutManager(lm);
        updateRecyclerView(dateSelection);

    }

    public void updateRecyclerView(String date){

        List<foodItem> elements = mydb.retrieveByDate(date);
        MyListAdapter adapter = new MyListAdapter(elements);
        foodList.setAdapter(adapter);
        if(elements.isEmpty()){
            foodList.setVisibility(View.GONE);
            emptyV.setVisibility(View.VISIBLE);
        }
        else{
            foodList.setVisibility(View.VISIBLE);
            emptyV.setVisibility(View.GONE);
        }
    }

    public void gotoAddDailyFood(View v){
        Intent toCreate = new Intent(this,AddDailyFood.class);
        startActivityForResult(toCreate,1);
    }


    protected  void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            this.updateRecyclerView(format.format(new Date()));
    }

    public void selectDate(View v){

        //c.setTime(new Date());
        int day=c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);



        dpd = new DatePickerDialog(DailyFoodView.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //dateSelection=year+"/"+(month+1)+"/"+dayOfMonth;
                c.set(year,month,dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                dateSelection= format.format(c.getTime());
                updateRecyclerView(dateSelection);
            }
        },year,month,day);

        dpd.show();

    }
    public void setBudget(View v){
        budget =  totalBudget.getText().toString();
        available_budget.setText(budget);

        // Update one field, creating the document if it does not already exist.
        Map<String, String> data = new HashMap<>();
        data.put("budget", totalBudget.getText().toString());

        Map<String,Object> Budget_Record = new HashMap<>();
        Budget_Record.put("date", dateSelection );
        Budget_Record.put("budget", budget);
        db.collection("presupuesto")
                .add(Budget_Record)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context,"Budget added successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context,"Something went wrong adding the recommendation", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editFood(View v){
        Intent intentEdit = new Intent(this,EditDailyFood.class);
        TextView id = v.findViewById(R.id.daily_food_id);
        intentEdit.putExtra("foodId",id.getText().toString().substring(1));//Getting string without the # character
        startActivityForResult(intentEdit,2);
    }

}
