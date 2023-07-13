package com.example.indangernew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordProfileActivity extends AppCompatActivity {

    private EditText email;
    private Button resetPassword;
    private String txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_profile);

        email = findViewById(R.id.email);
        resetPassword = findViewById(R.id.resetPassword);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail = email.getText().toString().trim();
                if (txtEmail.isEmpty()) {
                    Toast.makeText(ResetPasswordProfileActivity.this, "Enter your email to reset your password!", Toast.LENGTH_SHORT).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    Toast.makeText(ResetPasswordProfileActivity.this, "Please provide a valid Email Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(txtEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                resetPassword.setText("RESEND RESET PASSWORD EMAIL");
                                Toast.makeText(ResetPasswordProfileActivity.this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ResetPasswordProfileActivity.this, "Please provide your valid account Email Address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ResetPasswordProfileActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ResetPasswordProfileActivity.this, ProfileActivity.class));
        finish();
    }
}
