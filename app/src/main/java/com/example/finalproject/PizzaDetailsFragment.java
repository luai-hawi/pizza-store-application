package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PizzaDetailsFragment extends Fragment {

    private static final String ARG_PIZZA_NAME = "pizza_name";
    private String pizzaName;
    private String pizzaN;
    ArrayList<Pizza> pizzas;

    public static PizzaDetailsFragment newInstance(String pizzaName) {
        PizzaDetailsFragment fragment = new PizzaDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PIZZA_NAME, pizzaName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_details, container, false);

        TextView PizzaName = view.findViewById(R.id.pizza_name);
        TextView category = view.findViewById(R.id.cat);
        pizzaN=pizzaName.substring(0,pizzaName.indexOf('\n'));
        pizzas=parseCursor(DataBaseHelper.database.getPizzaByName(pizzaN));
        PizzaName.setText(pizzaName);
        category.setText(pizzas.get(0).getCategory());
        Button addToFav=view.findViewById(R.id.add_to_favourite);
        Button order=view.findViewById(R.id.order);
        LinearLayout pizzaPic=view.findViewById(R.id.pizza_pic);

        Drawable drawable = getDrawableFromBitmap(getActivity(), getBitmapFromBytes(pizzas.get(0).getPicture()));
        pizzaPic.setBackground(drawable);
        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataBaseHelper.database.insertFavourite(CurrentUser.user, pizzaN)) {
                    Toast.makeText(getActivity(), "pizza has been added to favourite❤️", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "error has occured or pizza is already in favourite❤️", Toast.LENGTH_SHORT).show();
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDialog();
            }
        });

        return view;
    }
    private ArrayList<Pizza> parseCursor(Cursor cursor){
        ArrayList<Pizza> newPizzas=new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String pizzaName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    String pizzaSize = cursor.getString(cursor.getColumnIndexOrThrow("SIZE"));
                    double pizzaPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
                    String pizzaCategory = cursor.getString(cursor.getColumnIndexOrThrow("CATEGORY"));
                    byte[] picture = cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"));
                    newPizzas.add(new Pizza(pizzaName,pizzaSize, pizzaPrice,pizzaCategory));
                    newPizzas.get(newPizzas.size()-1).setPicture(picture);


                } while (cursor.moveToNext()); // Move the cursor to the next row
            }
            cursor.close();
        }
        return newPizzas;
    }
    private void showOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.order_pizza, null);
        builder.setView(dialogView);

        Spinner spinnerPizzaSize = dialogView.findViewById(R.id.spinner_pizza_size);
        NumberPicker quantity = dialogView.findViewById(R.id.quantity);
        TextView totalPrice = dialogView.findViewById(R.id.totalprice);
        Button confirmButton = dialogView.findViewById(R.id.confirm);
        quantity.setMinValue(1);
        quantity.setMaxValue(10);
        quantity.setWrapSelectorWheel(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.add("s");
        adapter.add("m");
        adapter.add("l");
        spinnerPizzaSize.setAdapter(adapter);

        AlertDialog dialog = builder.create();
        String[] selectedS={"s"};
        double[] sizePrice={pizzas.get(2).getPrice()};
        int[] selectedQ={1};
        spinnerPizzaSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedS[0] = parent.getItemAtPosition(position).toString();
                if(selectedS[0].equals("s"))
                    sizePrice[0]=pizzas.get(2).getPrice();
                else if(selectedS[0].equals("m"))
                    sizePrice[0]=pizzas.get(1).getPrice();
                else if(selectedS[0].equals("l"))
                    sizePrice[0]=pizzas.get(0).getPrice();
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

                if(DataBaseHelper.database.insertOrder(CurrentUser.user, pizzaN, selectedS[0], selectedQ[0], selectedQ[0]*sizePrice[0]))
                    Toast.makeText(requireContext(), "Order confirmed👍 ", Toast.LENGTH_SHORT).show();
                else Toast.makeText(requireContext(), "Error has occured👎: ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public Bitmap getBitmapFromBytes(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    public Drawable getDrawableFromBitmap(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
