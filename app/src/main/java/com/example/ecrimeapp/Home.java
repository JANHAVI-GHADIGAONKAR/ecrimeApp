package com.example.ecrimeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Home extends AppCompatActivity implements View.OnClickListener {
    public CardView card1,card2,card4,card3;
    DrawerLayout drawerLayout;
    FirebaseFirestore fstore;
    String userId;
    FirebaseAuth fAuth;
    TextView FullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        card1=(CardView) findViewById(R.id.c1);
        card2=(CardView) findViewById(R.id.c2);
        card3=(CardView) findViewById(R.id.c3);
        card4=(CardView) findViewById(R.id.c4);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        FullName=findViewById(R.id.profileFullName);
        fAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();
        DocumentReference documentReference=fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                FullName.setText(documentSnapshot.getString("fName"));
            }
        });
        drawerLayout=findViewById(R.id.drawer);
    }
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.c1:
                i=new Intent(this, SearchName.class);
                startActivity(i);
                break;
            case R.id.c2:
                i=new Intent(this, CaseRegister.class);
                startActivity(i);
                break;
            case R.id.c3:
                i=new Intent(this, CreateFIR.class);
                startActivity(i);
                break;
            case R.id.c4:
                i=new Intent(this, AdminCase.class);
                startActivity(i);
                break;
        }
    }
    public  void ClickMenu(View view)
    {
        openDrawer (drawerLayout);
    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public  void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        recreate();
    }
    public void ClickLogout(View view){
        logout(this);
    }
    public static void logout(Activity activity) {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public static void redirectActivity(Activity activity,Class aclass) {
        Intent intent=new Intent(activity,aclass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
    int counter=0;
    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 100)
            super.onBackPressed();
    }
}