package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    private List<String> pizzaList;
    private Context context;


    public FavouriteAdapter(List<String> pizzaList, Context context) {
        this.pizzaList = pizzaList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        String pizza = pizzaList.get(position);
        holder.pizzaName.setText(pizza);

        holder.btnUndoFavourite.setOnClickListener(v -> {
            DataBaseHelper.database.undoFavourite(CurrentUser.user, holder.pizzaName.getText().toString());
            FavouriteFragment.loadFavoritePizzas();
            Toast.makeText(context, "Pizza removed from favourites", Toast.LENGTH_SHORT).show();
        });

        holder.btnOrderPizza.setOnClickListener(v -> {
            showOrderDialog(holder.pizzaName.getText().toString());
        });
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }
    private void showOrderDialog(String pizza) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.order_pizza, null);
        builder.setView(dialogView);

        Spinner spinnerPizzaSize = dialogView.findViewById(R.id.spinner_pizza_size);
        NumberPicker quantity = dialogView.findViewById(R.id.quantity);
        TextView totalPrice = dialogView.findViewById(R.id.totalprice);
        Button confirmButton = dialogView.findViewById(R.id.confirm);
        quantity.setMinValue(1);
        quantity.setMaxValue(10);
        quantity.setWrapSelectorWheel(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.add("s");
        adapter.add("m");
        adapter.add("l");
        spinnerPizzaSize.setAdapter(adapter);
        Cursor c=DataBaseHelper.database.getPizzaByName(pizza);
        c.moveToFirst();
        double LPrice=c.getDouble(c.getColumnIndexOrThrow("PRICE"));
        c.moveToNext();
        double MPrice=c.getDouble(c.getColumnIndexOrThrow("PRICE"));
        c.moveToNext();
        double SPrice=c.getDouble(c.getColumnIndexOrThrow("PRICE"));
        c.close();
        totalPrice.setText(String.valueOf(SPrice));
        AlertDialog dialog = builder.create();

        String[] selectedS={"s"};
        int[] selectedQ={1};
        double[] sizePrice={SPrice};
        spinnerPizzaSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedS[0] = parent.getItemAtPosition(position).toString();
                if(selectedS[0].equals("s"))
                    sizePrice[0]=SPrice;
                else if(selectedS[0].equals("m"))
                    sizePrice[0]=MPrice;
                else if(selectedS[0].equals("l"))
                    sizePrice[0]=LPrice;
                totalPrice.setText(String.valueOf(selectedQ[0]*sizePrice[0]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected if necessary
            }
        });
        quantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedQ[0]=newVal;
                totalPrice.setText(String.valueOf(selectedQ[0]*sizePrice[0]));
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(DataBaseHelper.database.insertOrder(CurrentUser.user, pizza, selectedS[0], selectedQ[0], selectedQ[0]*sizePrice[0]))
                    Toast.makeText(context, "Order confirmed👍 ", Toast.LENGTH_SHORT).show();
                else Toast.makeText(context, "Error has occured👎: ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaName;
        Button btnUndoFavourite;
        Button btnOrderPizza;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaName = itemView.findViewById(R.id.pizza_name);
            btnUndoFavourite = itemView.findViewById(R.id.btn_undo_favourite);
            btnOrderPizza = itemView.findViewById(R.id.btn_order_pizza);
        }
    }
}
