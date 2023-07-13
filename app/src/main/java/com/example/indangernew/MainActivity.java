package com.example.indangernew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView profile;
    private ImageView profileIcon;
    private TextView logout;
    private ImageView logoutIcon;
    private Button contacts;
    private ImageView emergency;
    private Button blood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        profile = findViewById(R.id.profile);
        profileIcon = findViewById(R.id.profileIcon);
        logout = findViewById(R.id.logout);
        logoutIcon = findViewById(R.id.logoutIcon);
        contacts = findViewById(R.id.contacts);
        emergency = findViewById(R.id.emergency);
        blood = findViewById(R.id.blood);

        emergency.setOnClickListener(view -> {
            try {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                finish();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        profile.setOnClickListener(view -> {
            try {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        profileIcon.setOnClickListener(view -> {
            try {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        contacts.setOnClickListener(view -> {
            try {
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                finish();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(view -> {
            try {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        logoutIcon.setOnClickListener(view -> {
            try {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        blood.setOnClickListener(view -> {
            try {
                startActivity(new Intent(MainActivity.this, BloodActivity.class));
                finish();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}