package com.example.finalproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import android.content.ContentResolver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    ImageView profilePic;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
                    Glide.with(this).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(profilePic);

                    try {
                        Bitmap bitmap = getBitmapFromUri(selectedImageUri);
                        if (bitmap != null) {
                            Bitmap circularBitmap = getCircularBitmap(bitmap);
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(circularBitmap, 512, 512, false);
                            byte[] imageBytes = convertBitmapToByteArray(resizedBitmap);
                            ImageView profile;
                            if(CurrentUser.admin){
                                profile=AdminActivity.headerView.findViewById(R.id.profile_pic);
                            }else{
                                profile=UserActivity.headerView.findViewById(R.id.profile_pic);
                            }
                            LoadImageTask loadImageTask = new LoadImageTask(profile);
                            loadImageTask.execute(imageBytes);
                            DataBaseHelper.database.changeProfilePicture(CurrentUser.user, imageBytes);
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
    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int minEdge = Math.min(width, height);

        Bitmap output = Bitmap.createBitmap(minEdge, minEdge, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final RectF rect = new RectF(0, 0, minEdge, minEdge);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawOval(rect, paint);

        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);

        return output;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Cursor c=DataBaseHelper.database.getUserByName(CurrentUser.user);
        c.moveToFirst();
        byte[] imageData=c.getBlob(c.getColumnIndexOrThrow("PICTURE"));
        profilePic=view.findViewById(R.id.pic);
        LoadImageTask loadImageTask = new LoadImageTask(profilePic);
        loadImageTask.execute(imageData);
        profilePic.setOnClickListener((v)->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512).createIntent(new Function1<Intent, Unit>() {
                @Override
                public Unit invoke(Intent intent) {
                    imagePickLauncher.launch(intent);
                    return null;
                }
            });
        });
        EditText emailET=view.findViewById(R.id.email);
        EditText phoneET=view.findViewById(R.id.phone);
        EditText firstnameET=view.findViewById(R.id.firstname);
        EditText lastnameET=view.findViewById(R.id.lastname);
        EditText passwordET=(EditText) view.findViewById(R.id.password);
        EditText confirmPasswordET=(EditText)view.findViewById(R.id.confirmpassword);
        Spinner genderS=view.findViewById(R.id.gender);
        Button confirm=view.findViewById(R.id.signup);
        TextView emailError=view.findViewById(R.id.emailerror);
        TextView phoneError=view.findViewById(R.id.phoneerror);
        TextView firstnameError=view.findViewById(R.id.firstnameerror);
        TextView lastnameError=view.findViewById(R.id.lastnameerror);
        TextView passwordError=view.findViewById(R.id.passworderror);
        TextView confirmPasswordError=view.findViewById(R.id.confirmpassworderror);
        Cursor cursor1=DataBaseHelper.database.getUserByName(CurrentUser.user);
        cursor1.moveToFirst();
        emailET.setText(cursor1.getString(cursor1.getColumnIndexOrThrow("EMAIL")));
        phoneET.setText(cursor1.getString(cursor1.getColumnIndexOrThrow("PHONE")));
        firstnameET.setText(cursor1.getString(cursor1.getColumnIndexOrThrow("FIRSTNAME")));
        lastnameET.setText(cursor1.getString(cursor1.getColumnIndexOrThrow("LASTNAME")));

        boolean gender = cursor1.getInt(cursor1.getColumnIndexOrThrow("GENDER")) >0;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.add("Male");
        adapter.add("Female");

        genderS.setAdapter(adapter);
        genderS.setSelection(gender ? 0:1);
        confirm.setOnClickListener(new View.OnClickListener() {
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
                if(!password.equals("")){
                if(!isValidPassword(password)){
                    validSignUp=false;
                    passwordError.setText("8 characters contains at least 1 number and 1 letter");
                }
                if(!confirmPassword.equals(password)){
                    validSignUp=false;
                    confirmPasswordError.setText("passwords do not match!");
                }}
                else{
                    password="";
                }
                User newUser=new User(email,phone,firstName,lastName,gender,password.equals("") ? "":Hash.hashPassword(password), usermode);
                if(validSignUp) {
                    if (!DataBaseHelper.database.updateUser(newUser)) {
                        emailError.setText("This E-mail is used!");
                    }
                    else{
                        CurrentUser.user= newUser.getEmail();
                        Toast.makeText(getContext(), "profile update Success🎉", Toast.LENGTH_SHORT).show();
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