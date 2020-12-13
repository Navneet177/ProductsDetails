package com.example.productsdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {
    TextView txtComputerSys, txtComponents, txtGaming, txtHeadphones;
    Button viewProducts, logout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtComputerSys = (TextView)findViewById(R.id.txtComputer);
        txtComponents = (TextView)findViewById(R.id.txtComponents);
        txtGaming = (TextView)findViewById(R.id.txtGaming);
        txtHeadphones = (TextView)findViewById(R.id.txtHeadphones);
        viewProducts = (Button) findViewById(R.id.btn_admin_view_products) ;
        logout = (Button) findViewById(R.id.btn_admin_home_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(home.this,MainActivity.class));
                finish();
            }
        });

        viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, viewProducts.class));
                finish();
            }
        });

        txtComputerSys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, addProducts.class);
                intent.putExtra("category", "ComputerSystems");
                startActivity(intent);

            }
        });

        txtComponents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, addProducts.class);
                intent.putExtra("category", "Components");
                startActivity(intent);
            }
        });

        txtGaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, addProducts.class);
                intent.putExtra("category", "Gaming");
                startActivity(intent);
            }
        });

        txtHeadphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, addProducts.class);
                intent.putExtra("category", "Headphones");
                startActivity(intent);
            }
        });

    }
}