package com.example.android.foodtracker;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.FoodListHolder>{
    @Override
    public FoodListHolder onCreateViewHolder(ViewGroup vw, int viewType) {
        View v = LayoutInflater.from(vw.getContext()).inflate(R.layout.daily_food_item, vw, false);
        FoodListHolder pvh = new FoodListHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FoodListHolder holder, int position) {
        holder.FoodName.setText(food.get(position).getName());
        holder.FoodPrice.setText("$" +food.get(position).getPrice()+"MXN");
        holder.FoodId.setText("#"+food.get(position).getId());
        holder.FoodDate.setText(food.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FoodListHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView FoodId;
        TextView FoodName;
        TextView FoodPrice;
        TextView FoodDate;
        public FoodListHolder(View viewItem){
            super(viewItem);
            cv = viewItem.findViewById(R.id.daily_food_cv);
            FoodName = viewItem.findViewById(R.id.daily_food_name);
            FoodPrice = viewItem.findViewById(R.id.daily_food_price);
            FoodId = viewItem.findViewById(R.id.daily_food_id);
            FoodDate=viewItem.findViewById(R.id.daily_food_date);
        }
    }
    List<foodItem> food;

    MyListAdapter(List<foodItem> food){
        this.food = food;
    }
}