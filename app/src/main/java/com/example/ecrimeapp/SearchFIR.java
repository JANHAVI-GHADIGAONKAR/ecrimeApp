package com.example.ecrimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFIR extends AppCompatActivity {
    DatabaseReference ref;
    private ListView Flistdata;
    private AutoCompleteTextView FtxtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fir);
        ref= FirebaseDatabase.getInstance().getReference("FIR");
        Flistdata=(ListView)findViewById(R.id.FListData);
        FtxtSearch=(AutoCompleteTextView)findViewById(R.id.FtxtSearch);
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
            FtxtSearch.setAdapter(adapter);
            FtxtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name=FtxtSearch.getText().toString();
                    searchUser(name);
                }
            });

        }else{
            Log.d("FIR", "No Data Found ");
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

                        Case2 aCase=new Case2(ds.child("fdate").getValue(String.class),ds.child("rid").getValue(String.class),ds.child("fname").getValue(String.class),ds.child("ph").getValue(String.class),ds.child("address").getValue(String.class),ds.child("incident").getValue(String.class));
                        listusers.add(aCase.getFdate()+'\n'+aCase.getRid()+'\n'+aCase.getFname()+'\n'+aCase.getPh()+'\n'+aCase.getAddress()+'\n'+aCase.getIncident());
                    }
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext() , android.R.layout.simple_list_item_1,listusers);
                    Flistdata.setAdapter(adapter);
                }else
                {
                    Log.d("FIR", "No Data Found ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class Case2 {
        String fdate;
        String rid;
        String fname;
        String ph;
        String address;
        String incident;


        public Case2(String fdate, String rid, String fname, String ph, String address, String incident) {
            this.fdate = fdate;
            this.rid = rid;
            this.fname = fname;
            this.ph = ph;
            this.address = address;
            this.incident = incident;
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

        public String getAddress() {
            return address;
        }

        public String getIncident() {
            return incident;
        }
    }
}