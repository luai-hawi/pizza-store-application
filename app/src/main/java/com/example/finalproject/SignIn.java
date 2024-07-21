package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignIn extends AppCompatActivity {
    ImageView pizza;
    Animation leaving;
SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPrefManager =SharedPrefManager.getInstance(this);
        Button signin=findViewById(R.id.signin);
        EditText username=findViewById(R.id.username);
        EditText password=(EditText) findViewById(R.id.password);
        CheckBox rememberMe=findViewById(R.id.rememberMe);
        TextView error=findViewById(R.id.error);
        pizza=findViewById(R.id.pizza);
        leaving = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pizzaleaving);
        Animation leaving2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pizzaleaving);

        String prevUsername=sharedPrefManager.readString("username", "noValue");
        String prevPassword=sharedPrefManager.readString("password", "noValue");
        if(!prevUsername.equals("noValue")){
            username.setText(prevUsername);
            password.setText(prevPassword);
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int usermode=DataBaseHelper.database.LogIn(username.getText().toString(), Hash.hashPassword(password.getText().toString()));
                if(usermode!=-1){
                    CurrentUser.user=username.getText().toString();
                    CurrentUser.admin=usermode > 0 ? true:false;
                    if(rememberMe.isChecked()){
                        sharedPrefManager.writeString("username", username.getText().toString());
                        sharedPrefManager.writeString("password", password.getText().toString());
                    }
                    pizza.startAnimation(leaving2);
                    Intent intent= usermode==0 ? new Intent(SignIn.this, UserActivity.class):new Intent(SignIn.this, AdminActivity.class);
                    SignIn.this.startActivity(intent);
                    SignIn.this.finish();

                }
                else{
                    error.setText("Username or password is wrong.");
                }
            }
        });
        leaving.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(SignIn.this, SignActivity.class);
                SignIn.this.startActivity(intent);
                SignIn.this.finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
    @Override
    public void onBackPressed() {
        pizza.startAnimation(leaving);
    }
}