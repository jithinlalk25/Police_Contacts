package com.example.jithin.police_contacts;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add extends AppCompatActivity {

    private EditText name;
    private EditText number;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");

        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);

    }

    public void add(View view){
        if(isNetworkAvailable()==false)
            Toast.makeText(this,"Network Unavailable!", Toast.LENGTH_SHORT).show();
        else if(name.getText().toString().trim().equals("") || number.getText().toString().trim().equals(""))
        {
            Toast.makeText(this,"Invalid Name or Number", Toast.LENGTH_SHORT).show();
        }
        else{

        FriendlyMessage friendlyMessage = new FriendlyMessage(number.getText().toString(), name.getText().toString());
        mMessagesDatabaseReference.push().setValue(friendlyMessage);

        // Clear input box
        name.setText("");
        number.setText("");


            Toast.makeText(this,"Added", Toast.LENGTH_SHORT).show();

    }}
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
