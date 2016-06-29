package com.mayank.user.userinfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ShowData extends AppCompatActivity {

    String userDetails,encodedImageString;
    byte[] userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        userDetails = getIntent().getExtras().getString("Details");
        encodedImageString = getIntent().getExtras().getString("Image");

        userImage = Base64.decode(encodedImageString, Base64.DEFAULT);
        Bitmap myPic = BitmapFactory.decodeByteArray(userImage, 0,userImage.length);

        TextView myDetails = (TextView) findViewById(R.id.myDetails);
        ImageView myCover = (ImageView) findViewById( R.id.myCover);

        Toast.makeText(getApplicationContext(),userImage+"",Toast.LENGTH_LONG).show();

        if (myDetails != null) {
            myDetails.setText(userDetails);
        }

        if (myCover != null) {
            myCover.setImageBitmap(myPic);
        }

    }

}
