package com.example.jithin.police_contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {
    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.call);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView number = (TextView) convertView.findViewById(R.id.number);

        FriendlyMessage message = getItem(position);

        number.setText(message.getText());
        name.setText(message.getName());

        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v.getParent();
                TextView tv = (TextView) rl.findViewById(R.id.number);
                String text = tv.getText().toString();
                //Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                String uri = "tel:" + text.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Call Permission Denied!", Toast.LENGTH_SHORT).show();
                    return;
                }
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
