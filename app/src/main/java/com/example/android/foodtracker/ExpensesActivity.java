package com.example.android.foodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpensesActivity extends AppCompatActivity {

    TextView expensesLabel;
    String date;
    DailyFoodDB db;
    Calendar c;
    DatePickerDialog dpd;
    ListView expenses;
    FirebaseFirestore db2;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        expensesLabel = findViewById(R.id.total_expenses);
        db = new DailyFoodDB(this);
        c=Calendar.getInstance();
        this.expenses = findViewById(R.id.expensesList);
        db2 = FirebaseFirestore.getInstance();
        context = getApplicationContext();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        date = formatter.format(new Date());
        updateExpensesLabel(date);
    }

    private void updateExpensesLabel(String date){
        final float expenses = getDailyExpenses(date);
        final TextView exp_label = findViewById(R.id.exp_label);
        exp_label.setText(String.valueOf(expenses));
        db2.collection("presupuesto")
                .whereEqualTo("date", date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    float budget = 0.0f;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        budget = Float.parseFloat(document.getData().get("budget").toString());
                    }
                    float remaining = budget - expenses;
                    /*if(remaining > 0){
                        expensesLabel.setTextColor(R.color.green);
                    }
                    else{
                        expensesLabel.setTextColor(R.color.red);
                    }*/
                    expensesLabel.setText("$"+remaining+"MXN");

                }
                else{
                    Log.wtf("Error",task.getException());
                }
            }
        });
    }

    private float getDailyExpenses(String date){
        List<foodItem> registers = db.retrieveByDate(date);
        String[] expenses = new String[registers.size()];
        float total = 0;
        int i = 0;
        for(foodItem f : registers){
            expenses[i] = f.getName() + " $" + Float.toString(f.getPrice()) + " MXN";
            total += f.getPrice();
            i++;
        }
        ArrayAdapter<String> expensesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenses);
        this.expenses.setAdapter(expensesAdapter);
        return total;
    }

    public void changeDate(View v){
        int day=c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);



        dpd = new DatePickerDialog(ExpensesActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //dateSelection=year+"/"+(month+1)+"/"+dayOfMonth;
                c.set(year,month,dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                date= format.format(c.getTime());
                updateExpensesLabel(date);
            }
        },year,month,day);

        dpd.show();
    }
}
