package com.example.finalproject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private List<String> ordersList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String order);
    }

    public OrdersAdapter(List<String> ordersList, OnItemClickListener listener) {
        this.ordersList = ordersList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new OrdersViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        String order = ordersList.get(position);
        holder.textView.setText(order);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(order));
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    static class OrdersViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        OrdersViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
            Typeface typeface = ResourcesCompat.getFont(itemView.getContext(), R.font.aclonica);
            textView.setTypeface(typeface);
            textView.setTextColor(Color.WHITE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
            params.setMargins(0, 0, 0, 10);
            textView.setLayoutParams(params);
        }
    }
}
