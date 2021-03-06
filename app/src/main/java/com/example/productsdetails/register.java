package com.example.productsdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText regtxtfullname,regtxtAge,regtxtEmail,regtxtPass;
    Button btnRegister;
    private ProgressBar progressBar;
    FirebaseFirestore db;
    String doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        regtxtAge = (EditText) findViewById(R.id.editTextUserAge);
        regtxtEmail = (EditText) findViewById(R.id.editTextTextPersonName);
        regtxtPass = (EditText) findViewById(R.id.editTextTextPassword);
        regtxtfullname = (EditText) findViewById(R.id.editTextFullName);
        progressBar = (ProgressBar) findViewById(R.id.proBarRegister);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        final String email = regtxtEmail.getText().toString().trim();
        final String password = regtxtPass.getText().toString().trim();
        final String name = regtxtfullname.getText().toString().trim();
        final String age = regtxtAge.getText().toString().trim();

        if (name.isEmpty()) {

            regtxtPass.setError("Full name is required");
            regtxtPass.requestFocus();
            return;
        }
        if (age.isEmpty()) {

            regtxtPass.setError("Age is required");
            regtxtPass.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            regtxtEmail.setError("Provide valid email");
            regtxtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {

            regtxtPass.setError("Password is required");
            regtxtPass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            regtxtPass.setError("Minimum length should be 6 characters");
            regtxtPass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = user1.getUid();
                    Admin admin = new Admin(name, age, email);

                    db.collection("Users").document(userId).set(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(register.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                // redirects to login activity
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else{
                                Toast.makeText(register.this, "Failed to register! Try again.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
                }else {
                    Toast.makeText(register.this, "Failed to register! Try again.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}