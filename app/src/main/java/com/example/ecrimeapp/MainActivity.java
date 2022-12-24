package com.example.ecrimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;

    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);


        fstore = FirebaseFirestore.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.Email, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//if(mEmail.getText().toString().equals("admin@gmail.com")&& mPassword.getText().toString().equals("admin")){
                //startActivity(new Intent(getApplicationContext(), Admin.class));
                // finish();
                //}else if(mEmail.getText().toString().equals("police@gmail.com")&& mPassword.getText().toString().equals("police")){
                //  startActivity(new Intent(getApplicationContext(), Home.class));
                // finish();
                //}
                // else{
                //   Toast.makeText(MainActivity.this, "Error ! " , Toast.LENGTH_SHORT).show();
                // }
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                Log.d("TAG", "onClick: " + mEmail.getText().toString());

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }


                if (awesomeValidation.validate()) {
                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user
                fAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                 public void onSuccess(AuthResult authResult) {
                  Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                  checkUserAccessLevel(authResult.getUser().getUid());
                 }
                }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                 Toast.makeText(MainActivity.this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                  progressBar.setVisibility(View.GONE);
                 }
                 });

                //fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   // @Override
                    //public void onComplete(@NonNull Task<AuthResult> task) {
                       // if (task.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                        //    startActivity(new Intent(getApplicationContext(), Home.class));
                       // } else {
                         //   Toast.makeText(MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        //    progressBar.setVisibility(View.GONE);
                      //  }

                   // }

               // });

            }
        });

    }
        private void checkUserAccessLevel (String uid){
            DocumentReference df = fstore.collection("users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                    if (documentSnapshot.getString("isAdmin") != null) {
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                    }
                    if (documentSnapshot.getString("isPolice") != null) {
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    }
                }
            });
        }

    int counter=0;
    @Override
    public void onBackPressed() {
        counter++;
        if(counter==4)
            super.onBackPressed();
    }
}