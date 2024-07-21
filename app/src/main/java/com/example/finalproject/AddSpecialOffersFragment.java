package com.example.finalproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSpecialOffersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSpecialOffersFragment extends Fragment {

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    byte[] picture;
    ArrayList<PizzaNS> pizzas;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddSpecialOffersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSpecialOffersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSpecialOffersFragment newInstance(String param1, String param2) {
        AddSpecialOffersFragment fragment = new AddSpecialOffersFragment();
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


        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            try {
                                Bitmap bitmap = getBitmapFromUri(selectedImageUri);
                                if (bitmap != null) {
                                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, false);
                                    picture = convertBitmapToByteArray(resizedBitmap);
                                } else {
                                    Toast.makeText(getContext(), "Failed to get Bitmap from Uri", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error getting Bitmap from Uri", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
        );



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_special_offers, container, false);
        Spinner type=view.findViewById(R.id.spinner_pizza_type);
        Spinner size=view.findViewById(R.id.spinner_pizza_size);
        NumberPicker quantity=view.findViewById(R.id.quantity);
        NumberPicker dicount=view.findViewById(R.id.dicount);
        TextView totalPrice=view.findViewById(R.id.totalprice);
        TextView details=view.findViewById(R.id.details2);
        Button add=view.findViewById(R.id.add);
        Button confirm=view.findViewById(R.id.confirm);
        Button choosePic=view.findViewById(R.id.choose_pic);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("s");
        adapter.add("m");
        adapter.add("l");
        size.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, CurrentUser.types);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter2);

        quantity.setMaxValue(10);
        quantity.setMinValue(1);
        quantity.setWrapSelectorWheel(true);
        dicount.setMaxValue(100);
        dicount.setMinValue(0);
        dicount.setWrapSelectorWheel(true);
        totalPrice.setText("0");
        pizzas=new ArrayList<>();
        int[] discountValue={0};
        double[] totalPriceValue={0};
        picture=new byte[0];
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pizzaName=type.getSelectedItem().toString();
                String pizzaSize=size.getSelectedItem().toString();
                int pizzaQuantity = quantity.getValue();
                Cursor cursor=DataBaseHelper.database.getPizzaPrice(pizzaName, pizzaSize);
                cursor.moveToFirst();
                double price =cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
                totalPriceValue[0]+=price * pizzaQuantity;
                totalPrice.setText(String.valueOf(totalPriceValue[0]-totalPriceValue[0]*(discountValue[0]/100.0))+"$");
                pizzas.add(new PizzaNS(pizzaName, pizzaSize, pizzaQuantity));
                String detailsValue=details.getText().toString();
                detailsValue=detailsValue+pizzaQuantity+" "+pizzaName+"("+pizzaSize+")\n";
                details.setText(detailsValue);
            }
        });
        dicount.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                discountValue[0]=newVal;
                totalPrice.setText(String.valueOf(totalPriceValue[0]-totalPriceValue[0]*(discountValue[0]/100.0))+"$");
            }
        });

        choosePic.setOnClickListener((v)->{
            ImagePicker.with(this).crop(100,50).compress(512).maxResultSize(1024,512).createIntent(new Function1<Intent, Unit>() {
                @Override
                public Unit invoke(Intent intent) {
                    imagePickLauncher.launch(intent);
                    return null;
                }
            });
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pizzas.size() == 0)
                    Toast.makeText(getContext(), "Add something first😊", Toast.LENGTH_SHORT).show();
                else if(picture.length == 0)
                    Toast.makeText(getContext(), "Choose picture first😊", Toast.LENGTH_SHORT).show();
                else{
                    DataBaseHelper.database.insertSpecialOrder(pizzas, totalPriceValue[0], (totalPriceValue[0]-totalPriceValue[0]*(discountValue[0]/100.0)), picture);
                    totalPriceValue[0]=0;
                    details.setText("");
                    totalPrice.setText("");
                    refresh();
                    Toast.makeText(getActivity(), "Offer has been added...🎉", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private void refresh(){
        picture=new byte[0];
        pizzas=new ArrayList<>();
    }

}