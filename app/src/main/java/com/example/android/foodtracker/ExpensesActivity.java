package com.example.android.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        expensesLabel = findViewById(R.id.total_expenses);
        db = new DailyFoodDB(this);
        c=Calendar.getInstance();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        date= formatter.format(new Date());
        updateExpensesLabel(date);
    }

    private void updateExpensesLabel(String date){
        float expenses = getDailyExpenses(date);
        expensesLabel.setText("$"+expenses+"MXN");
    }

    private float getDailyExpenses(String date){
        List<foodItem> registers = db.retrieveByDate(date);
        float total=0;
        for(foodItem f : registers){
            total+=f.getPrice();
        }
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
