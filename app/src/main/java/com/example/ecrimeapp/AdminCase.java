package com.example.ecrimeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminCase extends AppCompatActivity {
DatabaseReference ref;
private ListView listdata;
private AutoCompleteTextView txtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_case);
        ref= FirebaseDatabase.getInstance().getReference("CaseR");
        listdata=(ListView)findViewById(R.id.ListData);
        txtSearch=(AutoCompleteTextView)findViewById(R.id.txtSearch);
        ValueEventListener event=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(event);
    }

    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> names=new ArrayList<>();
        if(snapshot.exists())
        {
            for (DataSnapshot ds: snapshot.getChildren())
            {
                String name=ds.child("rid").getValue(String.class);
                names.add(name);
            }
            ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);
            txtSearch.setAdapter(adapter);
            txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name=txtSearch.getText().toString();
                    searchUser(name);
                }
            });

        }else{
            Log.d("CaseR", "No Data Found ");
        }
    }

    private void searchUser(String name) {
        Query query=ref.orderByChild("rid").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<String> listusers=new ArrayList<>();
                    for(DataSnapshot ds: snapshot.getChildren()){

                        Case aCase=new Case(ds.child("fdate").getValue(String.class),ds.child("rid").getValue(String.class),ds.child("fname").getValue(String.class),ds.child("ph").getValue(String.class),ds.child("cr").getValue(String.class));
                        listusers.add(aCase.getFdate()+'\n'+aCase.getRid()+'\n'+aCase.getFname()+'\n'+aCase.getPh()+'\n'+aCase.getCr());
                    }
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext() , android.R.layout.simple_list_item_1,listusers);
                    listdata.setAdapter(adapter);
                }else
                {
                    Log.d("CaseR", "No Data Found ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class Case {
        String fdate;
        String rid;
        String fname;
        String ph;
        String cr;

        public Case() {
        }

        public Case(String fdate, String rid, String fname, String ph, String cr) {
            this.fdate = fdate;
            this.rid = rid;
            this.fname = fname;
            this.ph = ph;
            this.cr = cr;
        }

        public String getFdate() {
            return fdate;
        }

        public String getRid() {
            return rid;
        }

        public String getFname() {
            return fname;
        }

        public String getPh() {
            return ph;
        }

        public String getCr() {
            return cr;
        }
    }

}