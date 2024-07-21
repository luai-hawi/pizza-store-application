package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button getStarted;
    public static byte[] userImage;
    public static byte[] pizzaImage;
    public static byte[] pizzaImage0;
    public static byte[] pizzaImage1;
    public static byte[] pizzaImage2;
    public static byte[] pizzaImage3;
    public static byte[] pizzaImage4;
    public static byte[] pizzaImage5;
    public static byte[] pizzaImage6;
    public static byte[] pizzaImage7;
    public static byte[] pizzaImage8;
    public static byte[] pizzaImage9;
    public static byte[] pizzaImage10;
    public static byte[] pizzaImage11;
    public static byte[] pizzaImage12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseHelper.database=new DataBaseHelper(MainActivity.this,"PizzaRestaurantApp",null,1);

        pizzaImage=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.pizza2);
        pizzaImage0=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.margarita);
        pizzaImage1=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.neapolitan);
        pizzaImage2=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.hawaiian);
        pizzaImage3=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.pepperoni);
        pizzaImage4=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.newyork);
        pizzaImage5=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.calzone);
        pizzaImage6=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.tandoori);
        pizzaImage7=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.bbq);
        pizzaImage8=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.sea);
        pizzaImage9=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.veg);
        pizzaImage10=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.buffalo);
        pizzaImage11=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.mashroom);
        pizzaImage12=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.pesto);

        userImage=ImageUtils.convertDrawableToByteArray(MainActivity.this, R.drawable.user);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CurrentUser.types=new ArrayList<>();
        final ImageView sun = (ImageView) findViewById(R.id.imageView);
        sun.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.sun));

        final ImageView cloud1 = (ImageView) findViewById(R.id.imageView4);
        cloud1.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.cloud1));
        final ImageView cloud2 = (ImageView) findViewById(R.id.imageView5);
        cloud2.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.cloud2));


        final ImageView car = (ImageView) findViewById(R.id.imageView6);
        car.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.car));

        getStarted = (Button) findViewById(R.id.button);
        getStarted.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.button));

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(MainActivity.this);
                    connectionAsyncTask.execute("https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/");
            }
        });
        /*
        Cursor c=DataBaseHelper.database.getAllUsers();
        c.moveToFirst();
        String s=c.getString(c.getColumnIndexOrThrow("EMAIL"));
        */
    }
    public void setButtonText(String text) {
        getStarted.setText(text);
    }
    public void setProgress(boolean progress) {
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    public void setTypes(ArrayList<String> pizzaTypes) {

        CurrentUser.types=pizzaTypes;
        for(int counter=0;counter<CurrentUser.types.size();counter++){
            String category="veggies";

            if(pizzaTypes.get(counter).equals("Hawaiian") ||
                    pizzaTypes.get(counter).equals("Pepperoni") ||
                    pizzaTypes.get(counter).equals("New York Style") ||
                    pizzaTypes.get(counter).equals("Calzone"))
                category="beef";
            else if(pizzaTypes.get(counter).equals("Seafood Pizza"))
                category="sea";

            else if(pizzaTypes.get(counter).contains("Chicken"))
                category="chicken";
            double basePrice=Math.floor((Math.random()*20)+10);
            Pizza newPizza=new Pizza(pizzaTypes.get(counter), "s", basePrice, category);
            if(counter == 0)
                newPizza.setPicture(pizzaImage0);
            else if(counter ==1)
                newPizza.setPicture(pizzaImage1);
            else if(counter ==2)
                newPizza.setPicture(pizzaImage2);
            else if(counter ==3)
                newPizza.setPicture(pizzaImage3);
            else if(counter ==4)
                newPizza.setPicture(pizzaImage4);
            else if(counter ==5)
                newPizza.setPicture(pizzaImage5);
            else if(counter ==6)
                newPizza.setPicture(pizzaImage6);
            else if(counter ==7)
                newPizza.setPicture(pizzaImage7);
            else if(counter ==8)
                newPizza.setPicture(pizzaImage8);
            else if(counter ==9)
                newPizza.setPicture(pizzaImage9);
            else if(counter ==10)
                newPizza.setPicture(pizzaImage10);
            else if(counter ==11)
                newPizza.setPicture(pizzaImage11);
            else if(counter ==12)
                newPizza.setPicture(pizzaImage12);
            else
                newPizza.setPicture(pizzaImage);
            DataBaseHelper.database.insertPizza(newPizza);
            newPizza.setSize("m");
            newPizza.setPrice(basePrice*1.5);
            DataBaseHelper.database.insertPizza(newPizza);
            newPizza.setSize("l");
            newPizza.setPrice(basePrice*2);
            DataBaseHelper.database.insertPizza(newPizza);

        }
    }
    public void ConnectionStatus(boolean s) {
        if(s==false){
        Toast.makeText(MainActivity.this, "Connection has failed ... ", Toast.LENGTH_SHORT).show();
        }
    }
    public void nextActivity(){
        Intent SignActivityIntent = new Intent(MainActivity.this, SignActivity.class);
        this.startActivity(SignActivityIntent);
        MainActivity.this.finish();
    }
}