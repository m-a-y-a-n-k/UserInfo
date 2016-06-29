package com.mayank.user.userinfo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class SignUp extends AppCompatActivity {

    int GALLERY_REQUEST = 2;
    int CAMERA_REQUEST = 3;

    private ImageView MyImage;
    private EditText name;
    private EditText email;
    private EditText address;
    private EditText contact;
    private TextView gender;
    private Button save;
    private RadioGroup genderGroup;
    private RadioButton maleButton,femaleButton;
    private SQLiteDatabase db;
    private int count = 0;
    private final String TABLE_NAME = "USERS";
    private final String COLUMN_ID = "INDEX";
    private final String COLUMN_PIC = "PROFILE_PICTURE";
    private final String COLUMN_NAME = "USER_NAME";
    private final String COLUMN_EMAIL_ID = "USER_MAIL";
    private final String COLUMN_GENDER = "Gender";
    private final String COLUMN_ADDRESS = "ADDRESS";
    private final String COLUMN_CONTACT = "CONTACT";

    private String query =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + "INTEGER_PRIMARY_KEY_AUTOINCREMENT,"
                    + COLUMN_EMAIL_ID + "TEXT,"
                    + COLUMN_NAME + "TEXT,"
                    + COLUMN_GENDER + "TEXT,"
                    + COLUMN_ADDRESS + "TEXT,"
                    + COLUMN_PIC + "BLOB,"
                    + COLUMN_CONTACT + "TEXT);";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.UserName);
        email = (EditText) findViewById(R.id.UserEmail);
        address = (EditText) findViewById(R.id.address);
        contact = (EditText) findViewById( R.id.contact);
        gender = (TextView) findViewById(R.id.gender);
        save = (Button) findViewById( R.id.save);
        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        maleButton = (RadioButton) findViewById( R.id.maleButton);
        femaleButton = (RadioButton) findViewById(R.id.femaleButton);
        MyImage = (ImageView) findViewById( R.id.MyImage);

        db = openOrCreateDatabase("UserInformation.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.execSQL(query);
        query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            count++;
            c.moveToNext();
        }
    }

    public void saveInDB(View v) {

        String iGen = "";

        String iName = name.getText().toString();
        String iEmail = email.getText().toString();
        String iAddress = address.getText().toString();
        String iContact = contact.getText().toString();

        Bitmap bitmap = ((BitmapDrawable)MyImage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] userPic = stream.toByteArray();

        if( maleButton.isChecked()) {
            iGen = "Male";
        } else if ( femaleButton.isChecked() ) {
            iGen = "Female";
        }

        if(
                iName.equalsIgnoreCase("") ||
                        iAddress.equalsIgnoreCase("") ||
                        iGen.equalsIgnoreCase("") ||
                        iEmail.equalsIgnoreCase("") ||
                        iContact.equalsIgnoreCase("")
                ) {
            Toast.makeText(getApplicationContext(), "Please fill in all the fields !!", Toast.LENGTH_LONG).show();
            return;
        }

        String query = "INSERT INTO " + TABLE_NAME + " VALUES("
                + count++  + ", '"
                + iEmail + "' , '"
                + iName + "' , '"
                + iGen + "' , '"
                + iAddress + "' , '"
                + userPic + "' , '"
                + iContact + "')";

        db.execSQL(query);

        Toast.makeText(getApplicationContext(),"Saved in DB !!",Toast.LENGTH_LONG).show();
    }

    public void OpenCamera(View v)
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void EditProfile(View v)
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Choose Image from Gallery
        if( requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null ) {
            Uri ImageURI = data.getData();
            MyImage.setImageURI(ImageURI);
        }

        // Choose Image from Camera
        if( requestCode == CAMERA_REQUEST && resultCode == RESULT_OK ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap cmpic= Bitmap.createScaledBitmap(photo, photo.getWidth(), photo.getHeight(), true);
            MyImage.setImageBitmap(cmpic);
        }

    }
}

