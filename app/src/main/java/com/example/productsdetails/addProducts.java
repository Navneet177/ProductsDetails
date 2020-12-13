package com.example.productsdetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class addProducts extends AppCompatActivity {
    Spinner spinner;
    private String productRandomKey,downloadImageUrl;
    TextView prdname,prdprc,prddecs;
    ImageView imageView;
    String pcategory, pname, pdesc, pprice, saveCurrentDate, saveCurrentTime;
    private static final int gallerypic = 1;
    private Uri imageUri;
    Button btnproduct;
    private StorageReference ProductImagesRef;
    FirebaseUser user;
    String userID;
    FirebaseFirestore db1;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        db1 = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        btnproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

        String[] computersystems = getResources().getStringArray(R.array.computerSystem);
        String[] gamings = getResources().getStringArray(R.array.gaming);
        String[] components = getResources().getStringArray(R.array.components);
        String[] headphones = getResources().getStringArray(R.array.headphones);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        btnproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

        if(getIntent().getStringExtra("category").equals("ComputerSystems")){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,computersystems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pcategory = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if(getIntent().getStringExtra("category").equals("Components")){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,components);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pcategory = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if(getIntent().getStringExtra("category").equals("Gaming")){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gamings);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pcategory = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if(getIntent().getStringExtra("category").equals("Headphones")){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,headphones);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pcategory = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }



    }

    private void validateProductData() {
        pname  = prdname.getText().toString();
        pdesc = prddecs.getText().toString();
        pprice = prdprc.getText().toString();

        if(imageUri == null){
            Toast.makeText(getApplicationContext(), "Image is mandatory.", Toast.LENGTH_SHORT).show();
            imageView.requestFocus();
        }

        else if(pname.isEmpty()){
            prdname.setError("Product name is required");
            prdname.requestFocus();
            return;
        }
        else if(pdesc.isEmpty()){
            prddecs.setError("Product description is required");
            prddecs.requestFocus();
            return;
        }
        else if(pprice.isEmpty()){
            prdprc.setError("Product price is required");
            prdprc.requestFocus();
            return;
        }
        else{
            storeProductInfo();
        }

    }

    private void storeProductInfo() {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM,dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(addProducts.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(addProducts.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(addProducts.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", pdesc);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", pcategory);
        productMap.put("price", pprice);
        productMap.put("pname", pname);


        db1.collection("Products").document(userID).set(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(addProducts.this, "Added Successfully.", Toast.LENGTH_SHORT).show();
                    // redirects to login activity
                    startActivity(new Intent(getApplicationContext(), home.class));
                }
                else{
                    Toast.makeText(addProducts.this, "Failed to add! Try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void OpenGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,gallerypic);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallerypic && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

}