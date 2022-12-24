package com.example.ecrimeapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder> {
    private int position;

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder,final int position, @NonNull final model model) {

        holder.name.setText(model.getName());
        holder.criminaltype.setText(model.getCriminaltype());
        Glide.with(holder.img.getContext()).load(model.getPimage()).into(holder.img);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true,1750)
                        .create();

                View myview=dialogPlus.getHolderView();
                final EditText name=myview.findViewById(R.id.uname);
                final EditText contact=myview.findViewById(R.id.ucontact);
                final EditText address=myview.findViewById(R.id.uaddress);
                final EditText criminaltype=myview.findViewById(R.id.ucriminaltype);
                final EditText previousrecord=myview.findViewById(R.id.upr);
                final EditText habits=myview.findViewById(R.id.uhabits);
                final EditText remanddate=myview.findViewById(R.id.uremand);
                final EditText pimage=myview.findViewById(R.id.uimgurl);
                Button submit=myview.findViewById(R.id.usubmit);


                name.setText(model.getName());
                contact.setText(model.getContact());
                address.setText(model.getAddress());
                criminaltype.setText(model.getCriminaltype());
                previousrecord.setText(model.getPreviousrecord());
                habits.setText(model.getHabits());
                remanddate.setText(model.getRemaddate());
                pimage.setText(model.getPimage());
                dialogPlus.show();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();

                        map.put("name",name.getText().toString());
                        map.put("contact",contact.getText().toString());
                        map.put("address",address.getText().toString());
                        map.put("criminaltype",criminaltype.getText().toString());
                        map.put("previousrecord",previousrecord.getText().toString());
                        map.put("habits",habits.getText().toString());
                        map.put("remanddate",remanddate.getText().toString());
                        map.put("pimage",pimage.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(), "Error while updation", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.name.getContext(), "Data deleted successfully", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        Toast.makeText(holder.name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }



    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        ImageView edit,delete;
        TextView name,criminaltype;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(CircleImageView)itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.nametext);

            criminaltype=(TextView)itemView.findViewById(R.id.criminaltype);

            edit=(ImageView)itemView.findViewById(R.id.editicon);
            delete=(ImageView)itemView.findViewById(R.id.deleteicon);
        }
    }
}
