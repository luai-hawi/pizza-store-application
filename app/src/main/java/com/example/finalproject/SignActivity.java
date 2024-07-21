package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button signin=(Button)findViewById(R.id.signin);
        Button signup=(Button)findViewById(R.id.signup);
        final ImageView pizza = (ImageView) findViewById(R.id.pizza);
        Animation entrance = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pizzaentering);
        pizza.startAnimation(entrance);
        Animation firstAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.signpizza);
        firstAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent signInPage=new Intent(SignActivity.this, SignIn.class);
                SignActivity.this.startActivity(signInPage);
                SignActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        Animation secondAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.signpizza2);
        secondAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent signInPage=new Intent(SignActivity.this, SignUp.class);
                SignActivity.this.startActivity(signInPage);
                SignActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pizza.startAnimation(firstAnimation);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pizza.startAnimation(secondAnimation);
            }
        });
    }
}