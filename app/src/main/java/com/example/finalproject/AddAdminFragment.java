package com.example.finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAdminFragment newInstance(String param1, String param2) {
        AddAdminFragment fragment = new AddAdminFragment();
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
        View view=inflater.inflate(R.layout.fragment_add_admin, container, false);
        EditText emailET=view.findViewById(R.id.email);
        EditText phoneET=view.findViewById(R.id.phone);
        EditText firstnameET=view.findViewById(R.id.firstname);
        EditText lastnameET=view.findViewById(R.id.lastname);
        EditText passwordET=(EditText) view.findViewById(R.id.password);
        EditText confirmPasswordET=(EditText)view.findViewById(R.id.confirmpassword);
        Spinner genderS=view.findViewById(R.id.gender);
        Button signup=view.findViewById(R.id.signup);
        TextView emailError=view.findViewById(R.id.emailerror);
        TextView phoneError=view.findViewById(R.id.phoneerror);
        TextView firstnameError=view.findViewById(R.id.firstnameerror);
        TextView lastnameError=view.findViewById(R.id.lastnameerror);
        TextView passwordError=view.findViewById(R.id.passworderror);
        TextView confirmPasswordError=view.findViewById(R.id.confirmpassworderror);
        ImageView image =view.findViewById(R.id.pizza);
        LoadImageTask loadImageTask = new LoadImageTask(image);
        loadImageTask.execute(MainActivity.userImage);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);

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
                boolean usermode=true;
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
                        Toast.makeText(getContext(), "Admin Add Success🎉", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
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

}