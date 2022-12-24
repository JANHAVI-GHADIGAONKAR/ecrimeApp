package com.example.ecrimeapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.TextUtils;
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
import in.dd4you.appsconfig.DD4YouConfig;
import java.text.DateFormat;
import java.util.Calendar;

public class CaseRegister extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView FullName,date,randomid;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    EditText editText,name, phone,caseregister;
    Button button;
    private DD4YouConfig dd4YouConfig;
    DatabaseReference CaseRegister;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_register);
        dd4YouConfig= new DD4YouConfig(this);
        randomid =findViewById(R.id.randomid);
        button=findViewById(R.id.button);
        randomid.setText(dd4YouConfig.generateUniqueID(5));
        Calendar calendar= Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate=findViewById(R.id.date);
        textViewDate.setText(currentDate);
        date=(TextView) findViewById(R.id.date);
        randomid=(TextView) findViewById(R.id.randomid);
        name=(EditText) findViewById(R.id.name);
        phone=(EditText) findViewById(R.id.phone);
        caseregister=(EditText) findViewById(R.id.caseregister);
        button=(Button) findViewById(R.id.button);
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.name, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.phone,"^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$",R.string.invalid_phone);
        awesomeValidation.addValidation(this,R.id.caseregister, RegexTemplate.NOT_EMPTY, R.string.invalid_case);
        CaseRegister= FirebaseDatabase.getInstance().getReference().child("CaseR");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addcase();
                randomid.setText(dd4YouConfig.generateUniqueID(5));
                name.getText().clear();
                phone.getText().clear();
                caseregister.getText().clear();
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
    private  void addcase(){

        String fdate=date.getText().toString().trim();
        String rid=randomid.getText().toString().trim();
        String fname=name.getText().toString().trim();
        String ph=phone.getText().toString().trim();
        String cr=caseregister.getText().toString().trim();
        if(awesomeValidation.validate()){
            Case case1=new Case(fdate,rid,fname,ph,cr);
            CaseRegister.push().setValue(case1);
            Toast.makeText(getApplicationContext(),"Case Registered Successfully",Toast.LENGTH_SHORT).show();
        }
        else{

            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

        }


    }
}