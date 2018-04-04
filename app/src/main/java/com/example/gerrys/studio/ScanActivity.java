package com.example.gerrys.studio;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    FirebaseDatabase database;
    DatabaseReference Class,User,upload;
    String ID,acton;
    Button nutto;
    public static final int PERMISSION_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        database = FirebaseDatabase.getInstance();
        Class = database.getReference("Class");
        User = database.getReference("User");
        ID = getIntent().getStringExtra("id");
        acton = getIntent().getStringExtra("action");
        nutto = (Button) findViewById(R.id.but);
        upload = database.getReference("uploads");
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        nutto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan(view);
            }
        });
    }

    public void scan(View view){
        zXingScannerView =new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }



    @Override
    public void handleResult(final Result result) {
        zXingScannerView.resumeCameraPreview(this);
        if(acton.equals("absen")){
            User.child(EncodeString(ID)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Class.child("2018-04-02").child(result.getText().toString()).child("student").child(user.getNim().toString()).child("status").setValue("Attend");
                    Toast.makeText(getApplicationContext(),user.getNim().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (acton.equals("download")){
            upload.child(result.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Upload up = dataSnapshot.getValue(Upload.class);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(up.getUrl()));
                    startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

       // if(result.getText().equals("liconganjing")){
         //   Intent i =new Intent(MainActivity.this,tesActivity.class);
           // startActivity(i);
            //Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        //}






    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}
