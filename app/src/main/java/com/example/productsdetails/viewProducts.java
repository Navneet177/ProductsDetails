package com.example.productsdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class viewProducts extends AppCompatActivity {
    ListView listView;
    private FirebaseAuth mAuth;
    listViewAdaptor listViewAdapter;
    ArrayList<Product> productArrayList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        listView = findViewById(R.id.listview);
        productArrayList = new ArrayList<>();
        productArrayList = getData();
        listViewAdapter = new listViewAdaptor(viewProducts.this,productArrayList);
        listView.setAdapter(listViewAdapter);


    }

    public  ArrayList<Product> getData(){
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document:task.getResult()){
                                Product product=document.toObject(Product.class);
                                String productName = product.getPname();
                                String productPrice = product.getPrice();
                                String productImage = product.getImage();
                                productArrayList.add(new Product(productName,productPrice,productImage));
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        return  productArrayList;
    }


}