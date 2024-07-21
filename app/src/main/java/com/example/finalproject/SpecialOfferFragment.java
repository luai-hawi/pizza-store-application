package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SpecialOfferFragment extends Fragment {
    private static RecyclerView recyclerView;
    private static SpecialOffersAdapter adapter;
    static ArrayList<Offers> offers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offer, container, false);
        recyclerView = view.findViewById(R.id.special);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadSpecialOffers();

        return view;
    }

    public static void loadSpecialOffers() {
        Cursor cursor = DataBaseHelper.database.getOffers();
        offers=parseCursor(cursor);
        for(int counter=0;counter<offers.size();counter++){
            cursor=DataBaseHelper.database.getOffersExtend(offers.get(counter).id);
            offers.get(counter).pizzas=parseCursor2(cursor);
        }
        adapter = new SpecialOffersAdapter(offers, recyclerView.getContext());
        recyclerView.setAdapter(adapter);
    }

    private static ArrayList<Offers> parseCursor(Cursor cursor){
        ArrayList<Offers> offers2=new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("ID")));
                    String period =cursor.getString(cursor.getColumnIndexOrThrow("TIME_PERIOD"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    double price = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("PRICE")));
                    double oldPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("OLDPRICE")));
                    byte[] picture =  cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"));
                    offers2.add(new Offers(id, period, date, price, oldPrice, picture));

                } while (cursor.moveToNext()); // Move the cursor to the next row
            }
            cursor.close();
        }
        return offers2;
    }
    private static ArrayList<PizzaNS> parseCursor2(Cursor cursor){
        ArrayList<PizzaNS> extend=new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("QUANTITY")));
                    String name =cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    String size = cursor.getString(cursor.getColumnIndexOrThrow("SIZE"));
                    extend.add(new PizzaNS(name, size, quantity));

                } while (cursor.moveToNext()); // Move the cursor to the next row
            }
            cursor.close();
        }
        return extend;
    }
}