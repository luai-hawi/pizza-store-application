package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private List<String> pizzaList;
    private List<Pizza> pizzaCOmpleteList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String pizzaName);
    }

    public PizzaAdapter(List<String> pizzaList,List<Pizza> pizzaCOmpleteList, OnItemClickListener listener) {
        this.pizzaCOmpleteList=pizzaCOmpleteList;
        this.pizzaList = pizzaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new PizzaViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        String pizzaName = pizzaList.get(position);
        holder.textView.setText(pizzaName);
        Drawable drawable = getDrawableFromBitmap(holder.itemView.getContext(),getBitmapFromBytes(pizzaCOmpleteList.get(position*3).getPicture()));
        holder.image.setBackground(drawable);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(pizzaName));
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        LinearLayout image;

        PizzaViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text1);
            image= itemView.findViewById(R.id.pizza_background);
        }
    }
    public Bitmap getBitmapFromBytes(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    public Drawable getDrawableFromBitmap(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

}
