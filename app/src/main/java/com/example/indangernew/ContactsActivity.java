package com.example.indangernew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private Button addContact;
    private EditText contact1;
    private EditText contact2;
    private EditText contact3;
    private EditText contact4;
    private ImageView edit;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contact1 = findViewById(R.id.contact1);
        contact2 = findViewById(R.id.contact2);
        contact3 = findViewById(R.id.contact3);
        contact4 = findViewById(R.id.contact4);
        addContact = findViewById(R.id.addContact);
        edit = findViewById(R.id.edit);
        auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();

        contact1.setEnabled(false);
        contact2.setEnabled(false);
        contact3.setEnabled(false);
        contact4.setEnabled(false);
        addContact.setVisibility(View.GONE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference.child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Contacts contact = snapshot.getValue(Contacts.class);
                if (contact != null) {
                    contact1.setText(contact.contact1);
                    contact2.setText(contact.contact2);
                    contact3.setText(contact.contact3);
                    contact4.setText(contact.contact4);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact1.setEnabled(true);
                contact2.setEnabled(true);
                contact3.setEnabled(true);
                contact4.setEnabled(true);
                addContact.setVisibility(View.VISIBLE);
            }
        });

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emergencyContact1 = contact1.getText().toString().trim();
                String emergencyContact2 = contact2.getText().toString().trim();
                String emergencyContact3 = contact3.getText().toString().trim();
                String emergencyContact4 = contact4.getText().toString().trim();

                Contacts contacts = new Contacts(emergencyContact1, emergencyContact2, emergencyContact3, emergencyContact4);
                FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Contacts").setValue(contacts).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ContactsActivity.this, "Contact added successfully!", Toast.LENGTH_SHORT).show();
                            contact1.setEnabled(false);
                            contact2.setEnabled(false);
                            contact3.setEnabled(false);
                            contact4.setEnabled(false);
                            addContact.setVisibility(View.GONE);
                        }
                        else {
                            Toast.makeText(ContactsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ContactsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ContactsActivity.this, MainActivity.class));
        finish();
    }
}