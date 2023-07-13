package com.example.indangernew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class BloodActivity extends AppCompatActivity {

    private Button blood;
    private String bloodType;
    private  FirebaseAuth auth;

    private String emergencyContact1;
    private String emergencyContact2;
    private String emergencyContact3;
    private String emergencyContact4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);

        try {
            blood = findViewById(R.id.blood);
            String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserInfoClass userProfile = snapshot.getValue(UserInfoClass.class);
                    if (userProfile != null) {
                        bloodType = userProfile.blood.trim();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(BloodActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });

            auth = FirebaseAuth.getInstance();
            userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID);
            reference2.child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Contacts contacts = snapshot.getValue(Contacts.class);

                    emergencyContact1 = contacts.contact1.trim();
                    emergencyContact2 = contacts.contact2.trim();
                    emergencyContact3 = contacts.contact3.trim();
                    emergencyContact4 = contacts.contact4.trim();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("error", "Something went wrong!");
                }
            });

            blood.setOnClickListener(view -> {
                if (!emergencyContact1.isEmpty()) {
                    String phoneNumber = emergencyContact1;

                    String message = "Hey, I need " + bloodType + " blood right now! I'm in Danger.";

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                }
                if (!emergencyContact2.isEmpty()) {
                    String phoneNumber = emergencyContact2;

                    String message = "Hey, I need " + bloodType + " blood right now! I'm in Danger.";

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                }
                if (!emergencyContact3.isEmpty()) {
                    String phoneNumber = emergencyContact3;

                    String message = "Hey, I need " + bloodType + " blood right now! I'm in Danger.";

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                }
                if (!emergencyContact4.isEmpty()) {
                    String phoneNumber = emergencyContact4;

                    String message = "Hey, I need " + bloodType + " blood right now! I'm in Danger.";

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Sorry! Emergency Blood Request message was not sent!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BloodActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BloodActivity.this, MainActivity.class));
        finish();
    }
}