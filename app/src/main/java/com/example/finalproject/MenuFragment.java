package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private PizzaAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button filter=view.findViewById(R.id.filter);
        EditText price=view.findViewById(R.id.price_below);
        RadioButton chicken=view.findViewById(R.id.chicken);
        RadioButton beef=view.findViewById(R.id.beef);
        RadioButton veggies=view.findViewById(R.id.veggies);
        RadioButton sea=view.findViewById(R.id.sea);


        recyclerView = view.findViewById(R.id.orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Cursor c=DataBaseHelper.database.getAllPizzas();
        ArrayList<Pizza> pizzas=parseCursor(c);
        ArrayList<String> menu = getMenu(pizzas,-1);

        adapter = new PizzaAdapter(menu,pizzas, new PizzaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String pizzaName) {
                openPizzaDetailsFragment(pizzaName);
            }
        });
        recyclerView.setAdapter(adapter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double priceB= isDouble(price.getText().toString()) ? Double.parseDouble(price.getText().toString()) : -1;
                String category;
                if(chicken.isChecked())
                    category="chicken";
                else if(beef.isChecked())
                    category="beef";
                else if(veggies.isChecked())
                    category="veggies";
                else if(sea.isChecked())
                    category="sea";
                else
                    category="";
                Cursor cursor=DataBaseHelper.database.getAllPizzasFiltered(category);
                ArrayList<Pizza> newPizzas=parseCursor(cursor);
                adapter = new PizzaAdapter(getMenu(newPizzas,priceB),newPizzas, new PizzaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String pizzaName) {
                        openPizzaDetailsFragment(pizzaName);
                    }
                });
                recyclerView.setAdapter(adapter);

            }
        });
        return view;
    }
    public boolean isDouble(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private ArrayList<String> getMenu(ArrayList<Pizza> pizzas, double price){
        ArrayList<String> menu=new ArrayList<>();
        double basePrice=0;
        double topPrice=0;
        for(int counter=0;counter<pizzas.size();counter++){
            if(pizzas.get(counter).getSize().equals("s")){
                basePrice=pizzas.get(counter).getPrice();
            }
            else if(pizzas.get(counter).getSize().equals("l") && (price == -1 || price>basePrice)){
                topPrice=pizzas.get(counter).getPrice();
                menu.add(pizzas.get(counter).getName()+"\nPrice: "+basePrice+"$ - "+topPrice+"$");
            }
        }
        return menu;
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
                    byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"));
                    newPizzas.add(new Pizza(pizzaName,pizzaSize, pizzaPrice,pizzaCategory, image));


                } while (cursor.moveToNext()); // Move the cursor to the next row
            }
            cursor.close();
        }
        return newPizzas;
    }
    private void openPizzaDetailsFragment(String pizzaName) {
        PizzaDetailsFragment fragment = PizzaDetailsFragment.newInstance(pizzaName);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}