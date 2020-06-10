package com.example.android.foodtracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.foodtracker.ui.main.My_RecommendationsFragment;

import java.util.ArrayList;
import java.util.List;

public class MyRecommendationsAdapter extends RecyclerView.Adapter<MyRecommendationsAdapter.FoodViewHolder>{
    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup vw, int viewType) {
        View v = LayoutInflater.from(vw.getContext()).inflate(R.layout.item, vw, false);
        FoodViewHolder pvh = new FoodViewHolder(v, this.elementListener, this.switchlistener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        holder.FoodName.setText(food.get(position).getName());
        holder.FoodPrice.setText("$" +food.get(position).getPrice()+"MXN");
        holder.FoodDescription.setText(food.get(position).getDescription());
        Bitmap img=food.get(position).getImgBitMap();
        if(img!=null){
            holder.foodImage.setImageBitmap(img);
        }
        else{
            holder.foodImage.setImageResource(R.drawable.defaultimg);
        }
        holder.FoodId.setText("#"+food.get(position).getId());
        holder.s.setChecked(this.myfavs.contains(food.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        CardView cv;
        TextView FoodId;
        TextView FoodName;
        TextView FoodDescription;
        TextView FoodPrice;
        ImageView foodImage;
        OnElementListener listener;
        ElementLikeListener switchListener;
        Switch s;

        public FoodViewHolder(View viewItem , OnElementListener listener, ElementLikeListener ell){
            super(viewItem);
            cv = viewItem.findViewById(R.id.cv);
            FoodName = viewItem.findViewById(R.id.food_name);
            FoodPrice = viewItem.findViewById(R.id.food_price);
            foodImage = viewItem.findViewById(R.id.food_image);
            FoodDescription = viewItem.findViewById(R.id.food_description);
            FoodId = viewItem.findViewById(R.id.food_id);
            s= viewItem.findViewById(R.id.like);
            this.switchListener = ell;
            this.listener=listener;
            viewItem.setOnClickListener(this);
            s.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            this.listener.onElementClick(getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            this.switchListener.onElementLike(getAdapterPosition(), isChecked);
        }
    }
    List<FoodRow> food;
    OnElementListener elementListener;
    ElementLikeListener switchlistener;
    ArrayList<String> myfavs;

    public MyRecommendationsAdapter(List<FoodRow> food, OnElementListener mElementListener, ArrayList<String> favs, ElementLikeListener ell){
        this.food = food;
        this.elementListener = mElementListener;
        this.switchlistener=ell;
        this.myfavs = favs;

    }

    public interface OnElementListener{
        void onElementClick(int position);
    }
    public interface ElementLikeListener{
        void onElementLike(int position, boolean value);
    }
}
