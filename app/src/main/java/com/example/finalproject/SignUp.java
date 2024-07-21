package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;


public class SignUp extends AppCompatActivity {
    ImageView pizza;
    Animation leaving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText emailET=findViewById(R.id.email);
        EditText phoneET=findViewById(R.id.phone);
        EditText firstnameET=findViewById(R.id.firstname);
        EditText lastnameET=findViewById(R.id.lastname);
        EditText passwordET=(EditText) findViewById(R.id.password);
        EditText confirmPasswordET=(EditText)findViewById(R.id.confirmpassword);
        Spinner genderS=findViewById(R.id.gender);
        Button signup=findViewById(R.id.signup);
        TextView emailError=findViewById(R.id.emailerror);
        TextView phoneError=findViewById(R.id.phoneerror);
        TextView firstnameError=findViewById(R.id.firstnameerror);
        TextView lastnameError=findViewById(R.id.lastnameerror);
        TextView passwordError=findViewById(R.id.passworderror);
        TextView confirmPasswordError=findViewById(R.id.confirmpassworderror);
        pizza=findViewById(R.id.pizza);
        LoadImageTask loadImageTask = new LoadImageTask(pizza);
        loadImageTask.execute(MainActivity.userImage);
        leaving = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pizzaleaving);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.add("Male");
        adapter.add("Female");

        genderS.setAdapter(adapter);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailError.setText("");
                phoneError.setText("");
                firstnameError.setText("");
                lastnameError.setText("");
                passwordError.setText("");
                confirmPasswordError.setText("");

                String email=emailET.getText().toString();
                String phone=phoneET.getText().toString();
                String firstName=firstnameET.getText().toString();
                String lastName=lastnameET.getText().toString();
                boolean gender=genderS.getSelectedItem().toString().equals("Male");
                String password=passwordET.getText().toString();
                String confirmPassword=confirmPasswordET.getText().toString();
                boolean usermode=false;
                boolean validSignUp=true;
                if(!isValidEmail(email)){
                    validSignUp=false;
                    emailError.setText("E-mail is not valid. recheck it.");
                }
                if(!isValidPhoneNumber(phone)){
                    validSignUp=false;
                    phoneError.setText("phone number must be 10 digits starting with 05.");
                }
                if(firstName.length()<3){
                    validSignUp=false;
                    firstnameError.setText("first name must have 3 letters at least.");
                }
                if(lastName.length()<3){
                    validSignUp=false;
                    lastnameError.setText("last name must have 3 letters at least.");
                }
                if(!isValidPassword(password)){
                    validSignUp=false;
                    passwordError.setText("8 characters contains at least 1 number and 1 letter");
                }
                if(!confirmPassword.equals(password)){
                    validSignUp=false;
                    confirmPasswordError.setText("passwords do not match!");
                }
                User newUser=new User(email,phone,firstName,lastName,gender,Hash.hashPassword(password), usermode);
                newUser.setPicture(MainActivity.userImage);
                if(validSignUp) {
                    if (DataBaseHelper.database.insertUser(newUser) == -1) {
                        emailError.setText("This E-mail is used!");
                    }
                    else{
                        Toast.makeText(SignUp.this, "Sign up Success🎉", Toast.LENGTH_SHORT).show();
                        pizza.startAnimation(leaving);
                    }
                }
            }
        });
        leaving.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(SignUp.this, SignActivity.class);
                SignUp.this.startActivity(intent);
                SignUp.this.finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^05\\d{8}$";
        return phoneNumber.matches(regex);
    }
    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";
        return password.matches(regex);
    }
    @Override
    public void onBackPressed() {
        pizza.startAnimation(leaving);
    }


}