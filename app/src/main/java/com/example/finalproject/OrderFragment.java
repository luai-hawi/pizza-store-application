package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if(CurrentUser.admin){
            view=inflater.inflate(R.layout.admin_orders, container, false);
            recyclerView = view.findViewById(R.id.orders);
            TextView caculations = view.findViewById(R.id.calcs);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            Cursor cursor = DataBaseHelper.database.getAllOrders();
            ArrayList<Orders> orders = parseCursor(cursor);
            ArrayList<String> titls = getOrdersTitles(orders);

            adapter = new OrdersAdapter(titls, new OrdersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String order) {
                    openOrderDetailsFragment(order);
                }
            });
            recyclerView.setAdapter(adapter);

            String calcs="";
            double totalIncome=0;
            for(int counter=0;counter<CurrentUser.types.size();counter++){
                double Sprice=0;
                double Mprice=0;
                double Lprice=0;
                double totalPrice=0;
                int totalOrders=0;

                String type=CurrentUser.types.get(counter);
                calcs=calcs+type+":\n";

                Cursor c2=DataBaseHelper.database.getPizzaByName(type);
                c2.moveToFirst();
                Lprice=c2.getDouble(c2.getColumnIndexOrThrow("PRICE"));
                c2.moveToNext();
                Mprice=c2.getDouble(c2.getColumnIndexOrThrow("PRICE"));
                c2.moveToNext();
                Sprice=c2.getDouble(c2.getColumnIndexOrThrow("PRICE"));
                c2.close();

                Cursor c1=DataBaseHelper.database.getAllOrdersForOneType(type);

                if (c1 != null) {
                    if (c1.moveToFirst()) {
                        do {
                            String size= c1.getString(c1.getColumnIndexOrThrow("SIZE"));
                            int quantity = c1.getInt(c1.getColumnIndexOrThrow("QUANTITY"));
                            totalOrders+=quantity;
                            switch(size){
                                case "s":
                                    totalPrice+=Sprice*quantity;
                                    break;
                                case "m":
                                    totalPrice+=Mprice*quantity;
                                    break;
                                case "l":
                                    totalPrice+=Lprice*quantity;
                                    break;
                            }
                        } while (c1.moveToNext());
                    }
                    c1.close();
                }
                totalIncome+=totalPrice;
                calcs=calcs+totalOrders+" orders\n"+totalPrice+"$ total income.\n\n";
            }
            calcs=calcs+"Total Income:\n"+totalIncome+"$.";
            caculations.setText(calcs);
        }else {

            view=inflater.inflate(R.layout.fragment_order, container, false);
            recyclerView = view.findViewById(R.id.orders);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            Cursor cursor = DataBaseHelper.database.getOrdersByEmail(CurrentUser.user);
            ArrayList<Orders> orders = parseCursor(cursor);
            ArrayList<String> titls = getOrdersTitles(orders);

            adapter = new OrdersAdapter(titls, new OrdersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String order) {
                    openOrderDetailsFragment(order);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
    private void openOrderDetailsFragment(String order) {
        OrderDetailsFragment fragment = OrderDetailsFragment.newInstance(order);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private ArrayList<Orders> parseCursor(Cursor cursor){
        ArrayList<Orders> orders=new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow("ORDER_TIME"));
                    orders.add(new Orders(id,email, price, time));


                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return orders;
    }
    private ArrayList<String> getOrdersTitles(ArrayList<Orders> orders){
        ArrayList<String> titls=new ArrayList<>();
        for(int counter=0;counter<orders.size();counter++){
            titls.add("Order number: "+orders.get(counter).id+"\n"+orders.get(counter).time);
        }
        return titls;
    }
}