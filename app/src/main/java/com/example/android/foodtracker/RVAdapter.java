package com.example.android.foodtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.FoodViewHolder>{
    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup vw, int viewType) {
        View v = LayoutInflater.from(vw.getContext()).inflate(R.layout.item, vw, false);
        FoodViewHolder pvh = new FoodViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        holder.FoodName.setText(food.get(position).getName());
        holder.FoodPrice.setText("$" +food.get(position).getPrice()+"MXN");
        holder.FoodDescription.setText(food.get(position).getDescription());
        holder.foodImage.setImageBitmap(food.get(position).getImgBitMap());
        holder.FoodId.setText("#"+food.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView FoodId;
        TextView FoodName;
        TextView FoodDescription;
        TextView FoodPrice;
        ImageView foodImage;
        public FoodViewHolder(View viewItem){
            super(viewItem);
            cv = viewItem.findViewById(R.id.cv);
            FoodName = viewItem.findViewById(R.id.food_name);
            FoodPrice = viewItem.findViewById(R.id.food_price);
            foodImage = viewItem.findViewById(R.id.food_image);
            FoodDescription = viewItem.findViewById(R.id.food_description);
            FoodId = viewItem.findViewById(R.id.food_id);
        }
    }
    List<FoodRow> food;

    RVAdapter(List<FoodRow> food){
        this.food = food;
    }
}
