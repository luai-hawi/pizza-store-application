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


public class FavouriteFragment extends Fragment {
    private static RecyclerView recyclerView;
    private static FavouriteAdapter adapter;
    static ArrayList<String> favoritePizzas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadFavoritePizzas();

        return view;
    }

    public static void loadFavoritePizzas() {
        Cursor cursor = DataBaseHelper.database.getFavouritePizzas(CurrentUser.user);
        favoritePizzas=parseCursor(cursor);
        adapter = new FavouriteAdapter(favoritePizzas, recyclerView.getContext());
        recyclerView.setAdapter(adapter);
    }

    private static ArrayList<String> parseCursor(Cursor cursor){
        ArrayList<String> favouritePizzas=new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String pizzaName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    favouritePizzas.add(pizzaName);

                } while (cursor.moveToNext()); // Move the cursor to the next row
            }
            cursor.close();
        }
        return favouritePizzas;
    }
}

