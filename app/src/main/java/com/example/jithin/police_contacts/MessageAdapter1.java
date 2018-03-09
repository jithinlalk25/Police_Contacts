package com.example.jithin.police_contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;





public class MessageAdapter1 extends ArrayAdapter<FriendlyMessage1> {

    List<FriendlyMessage1> data;


    public MessageAdapter1(Context context, int resource, List<FriendlyMessage1> objects) {
        super(context, resource, objects);
        data=objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item1, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.call);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView number = (TextView) convertView.findViewById(R.id.number);

        FriendlyMessage1 message = getItem(position);

        number.setText(message.getText());
        name.setText(message.getName());


        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()==false)
                    Toast.makeText(getContext(),"Network Unavailable!", Toast.LENGTH_SHORT).show();
else{
                Integer index = (Integer) v.getTag();

                data.remove(position);
                notifyDataSetChanged();



                    Toast.makeText(getContext(),"Deleted", Toast.LENGTH_SHORT).show();



                RelativeLayout rl = (RelativeLayout) v.getParent();


                TextView tv = (TextView) rl.findViewById(R.id.number);

                String text = tv.getText().toString();

                //Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                FirebaseDatabase mFirebaseDatabase;
                //DatabaseReference mMessagesDatabaseReference;
                mFirebaseDatabase = FirebaseDatabase.getInstance();

                DatabaseReference ref = mFirebaseDatabase.getReference();
                Query applesQuery = ref.child("messages").orderByChild("text").equalTo(text);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show();
                        //Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });




            }}
        });

        return convertView;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
