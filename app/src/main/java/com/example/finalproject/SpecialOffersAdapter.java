package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.SpecialOfferViewHolder> {
    private List<Offers> offers;
    private Context context;


    public SpecialOffersAdapter(List<Offers> offers, Context context) {
        this.offers = offers;
        this.context = context;
    }

    @NonNull
    @Override
    public SpecialOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_offer, parent, false);
        return new SpecialOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialOfferViewHolder holder, int position) {
        String offerId = String.valueOf(offers.get(position).getId());
        holder.offerNumber.setText(offerId);
        String details = getString(position);
        holder.details.setText(details);
        Drawable drawable = getDrawableFromBitmap(holder.itemView.getContext(),getBitmapFromBytes(offers.get(position).getPicture()));
        holder.offerImage.setBackground(drawable);

        holder.orderNow.setOnClickListener(v -> {
            if(DataBaseHelper.database.insertSpecialOrderToOrders(new Orders(CurrentUser.user, offers.get(position).getPrice(), offers.get(position).pizzas)))
                Toast.makeText(context, "Order confirmed👍 ", Toast.LENGTH_SHORT).show();
            else Toast.makeText(context, "Error has occurred👎 ", Toast.LENGTH_SHORT).show();
        });

    }

    @NonNull
    private String getString(int position) {
        String details="";
        for(int counter = 0; counter< offers.get(position).pizzas.size()-1; counter++){
            details=details + offers.get(position).getPizzas().get(counter).quantity+" "+
                    offers.get(position).getPizzas().get(counter).name +" ("+
                    offers.get(position).getPizzas().get(counter).size+"), and ";
        }
        details=details+offers.get(position).getPizzas().get(offers.get(position).pizzas.size()-1).quantity+" "+
                offers.get(position).getPizzas().get(offers.get(position).pizzas.size()-1).name +" ("+
                offers.get(position).getPizzas().get(offers.get(position).pizzas.size()-1).size+") with just: "+
                offers.get(position).getPrice()+"$ instead of: "+offers.get(position).getOldPrice()+"$.";
        return details;
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public static class SpecialOfferViewHolder extends RecyclerView.ViewHolder {
        TextView offerNumber;
        Button orderNow;
        TextView details;
        LinearLayout offerImage;

        public SpecialOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            offerNumber = itemView.findViewById(R.id.offer_number);
            orderNow = itemView.findViewById(R.id.oreder_now);
            details = itemView.findViewById(R.id.details);
            offerImage = itemView.findViewById(R.id.offer_image);
        }
    }
    public Bitmap getBitmapFromBytes(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    public Drawable getDrawableFromBitmap(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
