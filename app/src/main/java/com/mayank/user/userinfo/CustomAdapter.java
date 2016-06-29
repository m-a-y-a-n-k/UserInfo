package com.mayank.user.userinfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.ByteBuffer;


/**
 * Created by user on 27-06-2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private int[] icons;

    public CustomAdapter(Context context, String[] details,int[] pics) {
        super(context, R.layout.custom_row,details);
        icons = pics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        String userDetails = getItem(position);

        TextView detailsText = (TextView) customView.findViewById(R.id.UserDetails);
        ImageView UserImage = (ImageView) customView.findViewById(R.id.UserPic);

        detailsText.setText(userDetails);

        byte[] bytes = ByteBuffer.allocate(4).putInt(icons[position]).array();

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        UserImage.setImageBitmap(bitmap);

        return customView;
    }
}
