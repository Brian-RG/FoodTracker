package com.example.android.foodtracker;

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

import java.util.ArrayList;
import java.util.List;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.FoodViewHolder>{
    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup vw, int viewType) {
        View v = LayoutInflater.from(vw.getContext()).inflate(R.layout.item, vw, false);
        FoodViewHolder pvh = new FoodViewHolder(v, this.listener, flListener);
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
        holder.like.setChecked(this.favoritos.contains(food.get(position).getId()));

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
        onFoodListener listener;
        FoodLikeListener foodListener;
        Switch like;

        public FoodViewHolder(View viewItem, onFoodListener listener, FoodLikeListener likeListener){
            super(viewItem);
            cv = viewItem.findViewById(R.id.cv);
            FoodName = viewItem.findViewById(R.id.food_name);
            FoodPrice = viewItem.findViewById(R.id.food_price);
            foodImage = viewItem.findViewById(R.id.food_image);
            FoodDescription = viewItem.findViewById(R.id.food_description);
            FoodId = viewItem.findViewById(R.id.food_id);
            like = viewItem.findViewById(R.id.like);
            this.foodListener = likeListener;
            this.listener=listener;
            viewItem.setOnClickListener(this);
            like.setOnCheckedChangeListener(this);
        }


        @Override
        public void onClick(View v) {
            this.listener.onFoodClick(getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            this.foodListener.onFoodLike(getAdapterPosition(), isChecked);
        }
    }
    List<FoodRow> food;
    onFoodListener listener;
    FoodLikeListener flListener;
    ArrayList<String> favoritos;

    public RecommendationsAdapter(List<FoodRow> food, onFoodListener mlistener, ArrayList<String> favos, FoodLikeListener fll){
        this.food = food;
        this.listener = mlistener;
        this.flListener = fll;
        this.favoritos = favos;
    }


    public interface onFoodListener{
        void onFoodClick(int position);
    }
    public interface FoodLikeListener{
        void onFoodLike(int position, boolean value);
    }
}
