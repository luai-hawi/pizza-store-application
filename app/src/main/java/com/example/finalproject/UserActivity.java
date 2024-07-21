package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    public static View headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#1B1616"));
        toolbar.setTitleTextColor(Color.WHITE);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(UserActivity.this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        headerView = navigationView.getHeaderView(0);

        // Find views in the header layout
        Cursor c=DataBaseHelper.database.getUserByName(CurrentUser.user);
        c.moveToFirst();
        byte[] imageData=c.getBlob(c.getColumnIndexOrThrow("PICTURE"));
        ImageView profile=headerView.findViewById(R.id.profile_pic);
        LoadImageTask loadImageTask = new LoadImageTask(profile);
        loadImageTask.execute(imageData);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_home) {
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_menu) {
            transaction.replace(R.id.fragment_container, new MenuFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_orders) {
            transaction.replace(R.id.fragment_container, new OrderFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_favourite) {
            transaction.replace(R.id.fragment_container, new FavouriteFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_offers) {
            transaction.replace(R.id.fragment_container, new SpecialOfferFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_profile) {
            transaction.replace(R.id.fragment_container, new ProfileFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_contact) {
            transaction.replace(R.id.fragment_container, new ContactFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(UserActivity.this, SignActivity.class);
            UserActivity.this.startActivity(intent);
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}