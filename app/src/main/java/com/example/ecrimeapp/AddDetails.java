package com.example.ecrimeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;

import com.karumi.dexter.listener.PermissionRequest;


import java.io.InputStream;
import java.util.Random;

import in.dd4you.appsconfig.DD4YouConfig;

public class AddDetails extends AppCompatActivity {
    EditText name,address,criminaltype,contact,previousrecord,habit,remanddate,id;
    Uri filepath;
    ImageView img;
    Button browse,signup;
    Bitmap bitmap;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        id = findViewById(R.id.t8);
        name = findViewById(R.id.t4);
        address = findViewById(R.id.t1);
        criminaltype = findViewById(R.id.t2);
        contact = findViewById(R.id.t3);
        previousrecord = findViewById(R.id.t5);
        habit = findViewById(R.id.t6);
        remanddate = findViewById(R.id.t7);
        img=(ImageView)findViewById(R.id.img);
        signup=(Button)findViewById(R.id.upload);
        browse=(Button)findViewById(R.id.browse);
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.t8, RegexTemplate.NOT_EMPTY,R.string.invalid_id);
        awesomeValidation.addValidation(this,R.id.t4, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.t1, RegexTemplate.NOT_EMPTY,R.string.invalid_add);
        awesomeValidation.addValidation(this,R.id.t2, RegexTemplate.NOT_EMPTY,R.string.invalid_ct);
        awesomeValidation.addValidation(this,R.id.t3, RegexTemplate.NOT_EMPTY,R.string.invalid_phone);
        awesomeValidation.addValidation(this,R.id.t5, RegexTemplate.NOT_EMPTY,R.string.invalid_pr);
        awesomeValidation.addValidation(this,R.id.t6, RegexTemplate.NOT_EMPTY,R.string.invalid_habit);
        awesomeValidation.addValidation(this,R.id.t7, RegexTemplate.NOT_EMPTY,R.string.invalid_rd);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(AddDetails.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Id = id.getText().toString().trim();
                String Name = name.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String CriminalType = criminaltype.getText().toString().trim();
                String Contact = contact.getText().toString().trim();
                String Previousrecord = previousrecord.getText().toString().trim();
                String Habit = habit.getText().toString().trim();
                String Remand = remanddate.getText().toString().trim();

                Log.d("TAG", "onClick: " + id.getText().toString());
                if(awesomeValidation.validate()){

                    Toast.makeText(getApplicationContext(),"Criminal Data Successfully Added",Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(Id)) {
                    id.setError("id is Required.");
                    return;
                }

                else if (TextUtils.isEmpty(Name)) {
                    name.setError("name is Required.");
                    return;
                }
                else if (TextUtils.isEmpty(Address)) {
                    address.setError("address is Required.");
                    return;
                }
                else if (TextUtils.isEmpty(CriminalType)) {
                    criminaltype.setError("criminal type is Required.");
                    return;
                }
                else if (TextUtils.isEmpty(Contact)) {
                    contact.setError("contact is Required.");
                    return;
                }
                else if (!Contact.matches("[+][0-9]{10,13}$")) {
                    contact.requestFocus();
                    contact.setError("Correct Format:+91**********");
                    return;
                }
                else if (TextUtils.isEmpty(Previousrecord)) {
                    previousrecord.setError("Previous record is Required.");
                    return;
                }
                else if (TextUtils.isEmpty(Habit)) {
                    habit.setError("habit is Required.");
                    return;
                }
                else if (TextUtils.isEmpty(Remand)) {
                    remanddate.setError("remand is Required.");
                    return;
                }

                uploadtofirebase();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1  && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadtofirebase()
    {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        name=(EditText)findViewById(R.id.t4);
        address=(EditText)findViewById(R.id.t1);
        contact=(EditText)findViewById(R.id.t3);
        criminaltype=(EditText)findViewById(R.id.t2);
        previousrecord=(EditText)findViewById(R.id.t5);
        habit=(EditText)findViewById(R.id.t6);
        remanddate=(EditText)findViewById(R.id.t7);
        id=(EditText)findViewById(R.id.t8);

        FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(50));

        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri){

                                dialog.dismiss();
                                FirebaseDatabase db=FirebaseDatabase.getInstance();
                                DatabaseReference root=db.getReference("users");

                                dataholder obj=new dataholder(name.getText().toString(),contact.getText().toString(),address.getText().toString(),criminaltype.getText().toString(),previousrecord.getText().toString(),habit.getText().toString(),remanddate.getText().toString(),uri.toString());
                                root.child(id.getText().toString()).setValue(obj);

                                name.setText("");
                                address.setText("");
                                contact.setText("");
                                criminaltype.setText("");
                                previousrecord.setText("");
                                habit.setText("");
                                remanddate.setText("");
                                img.setImageResource(R.drawable.ic_launcher_background);
                                Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :"+(int)percent+" %");
                    }
                });
    }
}