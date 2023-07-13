package com.example.indangernew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private TextView email;
    private EditText name;
    private EditText phone;
    private EditText password;
    private EditText blood;
    private EditText gender;
    private ImageView edit;
    private TextView resetPassword;
    private TextView updateEmail;
    private Button save;
    private FirebaseAuth auth;
    private String userID;
    private String resetPassEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        blood = findViewById(R.id.blood);
        gender = findViewById(R.id.gender);
        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);
        email = findViewById(R.id.email);
        resetPassword = findViewById(R.id.resetPassword);
        updateEmail = findViewById(R.id.emailUpdate);
        auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        save.setVisibility(View.GONE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfoClass userProfile = snapshot.getValue(UserInfoClass.class);
                if (userProfile != null) {
                    name.setText(userProfile.name);
                    email.setText(userProfile.email);
                    phone.setText(userProfile.phoneNo);
                    blood.setText(userProfile.blood);
                    gender.setText(userProfile.gender);
                    resetPassEmail = userProfile.email;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        resetPassword.setOnClickListener(view -> {
            try {
                startActivity(new Intent(ProfileActivity.this, ResetPasswordProfileActivity.class));
                finish();
            } catch (Exception e) {
                Toast.makeText(ProfileActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        updateEmail.setOnClickListener(view -> {
            startActivity(new Intent(ProfileActivity.this, UpdateEmailActivity.class));
            finish();
        });

        edit.setOnClickListener(view -> {
            name.setEnabled(true);
            phone.setEnabled(true);
            blood.setEnabled(true);
            gender.setEnabled(true);
            save.setVisibility(View.VISIBLE);
            name.requestFocus();
        });

        save.setOnClickListener(view -> {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String txtName = name.getText().toString().trim();
            String txtPhoneNo = phone.getText().toString().trim();
            String txtBlood = blood.getText().toString().trim();
            String txtGender = gender.getText().toString().trim();
            String txtEmail = email.getText().toString().trim();
            UserInfoClass user = new UserInfoClass(txtName, txtEmail, txtPhoneNo, txtBlood, txtGender);
            FirebaseDatabase.getInstance().getReference("Users").child(userID).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Your information updated successfully!", Toast.LENGTH_SHORT).show();
                    name.setEnabled(false);
                    phone.setEnabled(false);
                    blood.setEnabled(false);
                    gender.setEnabled(false);
                    save.setVisibility(View.GONE);
                }
            }).addOnFailureListener(e -> {
                name.setEnabled(false);
                phone.setEnabled(false);
                blood.setEnabled(false);
                gender.setEnabled(false);
                save.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            });

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }
}