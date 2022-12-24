package com.example.ecrimeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.util.Calendar;

import in.dd4you.appsconfig.DD4YouConfig;

public class CreateFIR extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView FullName,Fdate,Frandomid;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    EditText editText,Fname, Fphone;
    EditText Faddress,Fincident;
    Button button;
    private DD4YouConfig dd4YouConfig;
    DatabaseReference CreateFIR;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fir);
        dd4YouConfig= new DD4YouConfig(this);
        Frandomid =findViewById(R.id.Frandomid);
        button=findViewById(R.id.Fbutton);
        Frandomid.setText(dd4YouConfig.generateUniqueID(7));
        Calendar calendar= Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate=findViewById(R.id.Fdate);
        textViewDate.setText(currentDate);
        Fdate=(TextView) findViewById(R.id.Fdate);
        Frandomid=(TextView) findViewById(R.id.Frandomid);
        Fname=(EditText) findViewById(R.id.Fname);
        Fphone=(EditText) findViewById(R.id.Fphone);
        Faddress=(EditText) findViewById(R.id.Faddress);
        Fincident=(EditText) findViewById(R.id.Fincident);
        button=(Button) findViewById(R.id.Fbutton);
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.Fname, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.Fphone,"^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$",R.string.invalid_phone);
        awesomeValidation.addValidation(this,R.id.Faddress, RegexTemplate.NOT_EMPTY, R.string.invalid_case);
        awesomeValidation.addValidation(this,R.id.Fincident, RegexTemplate.NOT_EMPTY, R.string.invalid_incident);
        CreateFIR= FirebaseDatabase.getInstance().getReference().child("FIR");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFIR();
                Frandomid.setText(dd4YouConfig.generateUniqueID(7));
                Fname.getText().clear();
                Fphone.getText().clear();
                Faddress.getText().clear();
                Fincident.getText().clear();
            }
        });

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
    public void ClickMenu(View view){
        Home.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        Home.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        Home.redirectActivity(this,Home.class);
    }

    public void ClickLogout(View view){
        Home.logout(this);
    }
    protected void onPause(){
        super.onPause();
        Home.closeDrawer(drawerLayout);
    }
    private  void addFIR(){

        String fdate=Fdate.getText().toString().trim();
        String rid=Frandomid.getText().toString().trim();
        String fname=Fname.getText().toString().trim();
        String ph=Fphone.getText().toString().trim();
        String address=Faddress.getText().toString().trim();
        String incident=Fincident.getText().toString().trim();
        if(awesomeValidation.validate()){
            Case2 case2=new Case2(fdate,rid,fname,ph,address,incident);
            CreateFIR.push().setValue(case2);
            Toast.makeText(getApplicationContext(),"FIR Registered Successfully",Toast.LENGTH_SHORT).show();
        }
        else{

            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

        }


    }
}