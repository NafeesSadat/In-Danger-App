package com.example.indangernew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdateEmailActivity extends AppCompatActivity {

    private EditText emailOld, emailNew, password;
    private Button updateEmail;
    private String txtEmailOld, txtEmailNew, txtPassword, email, name, phoneNo, blood, gender, userID;
    private String contact1, contact2, contact3, contact4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        emailOld = findViewById(R.id.emailOld);
        emailNew = findViewById(R.id.emailNew);
        password = findViewById(R.id.password);
        updateEmail = findViewById(R.id.updateEmail);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserInfoClass userProfile = snapshot.getValue(UserInfoClass.class);
                    if (userProfile != null) {
                        name = userProfile.name;
                        email = userProfile.email;
                        phoneNo = userProfile.phoneNo;
                        blood = userProfile.blood;
                        gender = userProfile.gender;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UpdateEmailActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DatabaseReference contactReference = FirebaseDatabase.getInstance().getReference("Users");
            contactReference.child(userID).child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Contacts contacts = snapshot.getValue(Contacts.class);
                    if (contacts != null) {
                        contact1 = contacts.contact1;
                        contact2 = contacts.contact2;
                        contact3 = contacts.contact3;
                        contact4 = contacts.contact4;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UpdateEmailActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateEmail.setOnClickListener(v -> {
            txtEmailOld = emailOld.getText().toString().trim();
            txtEmailNew = emailNew.getText().toString().trim();
            txtPassword = password.getText().toString().trim();
            if (txtEmailOld.isEmpty()) {
                emailOld.setError("Enter your old email!");
            }else if (txtEmailNew.isEmpty()) {
                emailNew.setError("Enter your new email!");
            }else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmailOld).matches() && !Patterns.EMAIL_ADDRESS.matcher(txtEmailNew).matches()) {
                Toast.makeText(UpdateEmailActivity.this, "Please provide a valid email address!", Toast.LENGTH_SHORT).show();
            }else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                changeEmail(txtEmailOld, txtPassword);
            }
        });
    }
    private void changeEmail(String txtEmailOld, String txtPassword) {
        txtEmailNew = emailNew.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(txtEmailOld, txtPassword);

        Objects.requireNonNull(user).reauthenticate(credential).addOnCompleteListener(task -> {
            Log.d("value", "User re-authenticated.");

            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
            user1.updateEmail(txtEmailNew).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    UserInfoClass user11 = new UserInfoClass(name, txtEmailNew, phoneNo, blood, gender);
                    DatabaseReference referenceUserInfo = FirebaseDatabase.getInstance().getReference("Users");
                    referenceUserInfo.child(userID).setValue(user11).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {
                                Toast.makeText(UpdateEmailActivity.this, "Email address Updated successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(e -> Log.d("error", "Something went wrong!"));

                    Contacts contacts = new Contacts(contact1, contact2, contact3, contact4);
                    DatabaseReference referenceContactsInfo = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    referenceContactsInfo.child("Contacts").setValue(contacts).addOnCompleteListener(task11 -> {
                        if (task11.isSuccessful()) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("error", "Something went wrong!");
                        }
                    });

                    startActivity(new Intent(UpdateEmailActivity.this, ProfileActivity.class));
                    finish();
                }
                else {
                    Log.d("error", "Something went wrong!");
                    Toast.makeText(UpdateEmailActivity.this, "Email Address already connected with an existing account! Select a different address!", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnFailureListener(e -> Log.d("error", "Something went wrong!"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateEmailActivity.this, ProfileActivity.class));
        finish();
    }
}