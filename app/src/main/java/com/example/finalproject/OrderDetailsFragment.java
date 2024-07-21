package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailsFragment extends Fragment {

    private static final String ARG_ORDER="order";
    String order;
    int id;
    public static OrderDetailsFragment newInstance(String order) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = getArguments().getString(ARG_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order_details, container, false);
        id=Integer.parseInt(order.substring(14,order.indexOf("\n")));
        TextView number=view.findViewById(R.id.orderid);
        TextView detailsT=view.findViewById(R.id.details);
        TextView price=view.findViewById(R.id.price);
        TextView time=view.findViewById(R.id.date);
        TextView user=view.findViewById(R.id.user);
        Orders currentOrder=parseCursor(DataBaseHelper.database.getOrderById(id));
        number.setText(String.valueOf(currentOrder.getId()));
        String details = getDetails(currentOrder);
        detailsT.setText(details);
        price.setText(String.valueOf(currentOrder.getPrice()));
        time.setText(currentOrder.getTime());
        if(CurrentUser.admin)
            user.setText("Ordered By:\n"+CurrentUser.user);
        return view;

    }

    @NonNull
    private static String getDetails(Orders currentOrder) {
        String details="Order Details:\n";
        for(int counter = 0; counter< currentOrder.ordersExtend.size()-1; counter++){
            details=details+"* "+ currentOrder.ordersExtend.get(counter).quantity+" "+
                    currentOrder.ordersExtend.get(counter).name+" ("+
                    currentOrder.ordersExtend.get(counter).size+"),\n";
        }
        details=details+"* "+ currentOrder.ordersExtend.get(currentOrder.ordersExtend.size()-1).quantity+" "+
                currentOrder.ordersExtend.get(currentOrder.ordersExtend.size()-1).name+" ("+
                currentOrder.ordersExtend.get(currentOrder.ordersExtend.size()-1).size+").";
        return details;
    }

    private Orders parseCursor(Cursor cursor){
        Orders order1=new Orders();
        cursor.moveToFirst();
        order1.setId(id);
        order1.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("PRICE"))));
        order1.setTime(cursor.getString(cursor.getColumnIndexOrThrow("ORDER_TIME")));
        cursor.close();
        Cursor cursor1=DataBaseHelper.database.getOrdersExtend(id);

        ArrayList<PizzaNS> ordersExtends=new ArrayList<>();
        if (cursor1 != null) {
            if (cursor1.moveToFirst()) {
                do {
                    String pizzaName = cursor1.getString(cursor1.getColumnIndexOrThrow("NAME"));
                    String pizzaSize = cursor1.getString(cursor1.getColumnIndexOrThrow("SIZE"));
                    int quantity = cursor1.getInt(cursor1.getColumnIndexOrThrow("QUANTITY"));
                    ordersExtends.add(new PizzaNS(pizzaName,pizzaSize, quantity));

                } while (cursor1.moveToNext()); // Move the cursor to the next row
            }
            cursor1.close();
        }
        order1.setOrdersExtend(ordersExtends);
        return order1;
    }
}