package com.example.jithin.police_contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Delete extends AppCompatActivity {


    private ListView mMessageListView;
    private MessageAdapter1 mMessageAdapter;
    private MessageAdapter1 mMessageAdapter1;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    List<FriendlyMessage1> friendlyMessages = new ArrayList<>();
    List<FriendlyMessage1> searchResults  = new ArrayList<>(friendlyMessages);
    FriendlyMessage1 friendlyMessage;
    LinearLayout pass;
    private String mUsername;
    private EditText searchBox;
    private EditText password;
    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mMessagesDatabaseReference1;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    Menu m;
    String email;
    static boolean c = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mFirebaseDatabase.setPersistenceEnabled(true);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference();



        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.ListView);
        searchBox=(EditText) findViewById(R.id.searchBox);
        searchBox.setVisibility(View.GONE);

        mMessageAdapter = new MessageAdapter1(this, R.layout.item1, friendlyMessages);
        mMessageAdapter1 = new MessageAdapter1(this, R.layout.item1, searchResults);
        mMessageListView.setAdapter(mMessageAdapter);


        searchBox.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //get the text in the EditText

                // mMessageAdapter.clear();

                String searchString=searchBox.getText().toString();
                int textLength=searchString.length();
                searchResults.clear();

                for(int i=0;i<friendlyMessages.size();i++)
                {
                    String playerName=friendlyMessages.get(i).getName().toString();

                    if(textLength<=playerName.length()){
                        //compare the String in EditText with Names in the ArrayList
                        if(searchString.equalsIgnoreCase(playerName.substring(0,textLength)))
                        {  searchResults.add(friendlyMessages.get(i));
                            //mMessageAdapter.add(friendlyMessage);
                            //toast(friendlyMessages.get(i).getName().toString());
                        }}
                }
                mMessageListView.setAdapter(mMessageAdapter1);
                mMessageAdapter.notifyDataSetChanged();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {


            }
        });





        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        attachDatabaseReadListener();


    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        // mMessageAdapter.clear();
        // detachDatabaseReadListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu1, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view;
        switch (item.getItemId()) {

            case R.id.search:
                if(searchBox.getVisibility()==View.GONE) {
                    searchBox.setVisibility(View.VISIBLE);
                    searchBox.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
                }
                else {
                    String searchString="";
                    int textLength=searchString.length();
                    searchResults.clear();

                    for(int i=0;i<friendlyMessages.size();i++)
                    {
                        String playerName=friendlyMessages.get(i).getName().toString();

                        if(textLength<=playerName.length()){
                            //compare the String in EditText with Names in the ArrayList
                            if(searchString.equalsIgnoreCase(playerName.substring(0,textLength)))
                            {  searchResults.add(friendlyMessages.get(i));
                                //mMessageAdapter.add(friendlyMessage);
                                //toast(friendlyMessages.get(i).getName().toString());
                            }}
                    }
                    mMessageListView.setAdapter(mMessageAdapter1);
                    mMessageAdapter.notifyDataSetChanged();
                    searchBox.setText("");



                    searchBox.setVisibility(View.GONE);
                    view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            Query myTopPostsQuery = mMessagesDatabaseReference.child("messages").orderByChild("name");
            myTopPostsQuery.addChildEventListener(new ChildEventListener(){
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    friendlyMessage = dataSnapshot.getValue(FriendlyMessage1.class);
                    mMessageAdapter.add(friendlyMessage);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            });
            // mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

}
