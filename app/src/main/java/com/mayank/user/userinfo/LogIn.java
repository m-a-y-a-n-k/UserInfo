package com.mayank.user.userinfo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class LogIn extends AppCompatActivity {

    private TextView idTextView;
    private EditText idInput;
    private Button showDetails;

    private SQLiteDatabase db;

    private final String TABLE_NAME = "USERS";
    private final String COLUMN_ID = "INDEX";
    private final String COLUMN_NAME = "USER_NAME";
    private final String COLUMN_EMAIL_ID = "USER_MAIL";
    private final String COLUMN_GENDER = "Gender";
    private final String COLUMN_ADDRESS = "ADDRESS";
    private final String COLUMN_CONTACT = "CONTACT";
    private final String COLUMN_PIC = "PROFILE_PICTURE";

    private final String query =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + "INTEGER_PRIMARY_KEY_AUTOINCREMENT,"
                    + COLUMN_EMAIL_ID + "TEXT,"
                    + COLUMN_NAME + "TEXT,"
                    + COLUMN_GENDER + "TEXT,"
                    + COLUMN_ADDRESS + "TEXT,"
                    + COLUMN_PIC + "BLOB,"
                    + COLUMN_CONTACT + "TEXT);";
    private User[] users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        idTextView = (TextView) findViewById( R.id.UserDetailsText);
        idInput = (EditText) findViewById( R.id.UserIDInput);
        showDetails = (Button) findViewById( R.id.showDB);

        db = openOrCreateDatabase("UserInformation.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.execSQL(query);
    }

    public void showInDB( View v) throws UnsupportedEncodingException {

        String UserId = idInput.getText().toString();
        User user = fetchUserFromMail(UserId);

        if(  user == null ) {
            Toast.makeText(getApplicationContext(),"No Data Found !!",Toast.LENGTH_LONG).show();
        } else {
            Intent showData = new Intent( LogIn.this, ShowData.class);

            Bundle b = new Bundle();

            String details = user.details;
            byte[] image = user.myImageId;

            String encodedImageString = Base64.encodeToString(image, Base64.DEFAULT);

            b.putString("Details", details);
            b.putString("Image",encodedImageString);
            showData.putExtras(b);
            startActivity(showData);
        }
    }

    private User fetchUserFromMail(String userEmail) {

        User dbUser = new User();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();

        while ( true ) {
            if( c.getString(1).equalsIgnoreCase(userEmail) ) {
                break;
            }

            c.moveToNext();
        }

        if( c.isAfterLast() ) {
            Toast.makeText(getApplicationContext(),"No Data Available !!",Toast.LENGTH_LONG).show();
            return null;
        }

        dbUser.details = "";

        dbUser.details += "NAME: ";
        dbUser.details += c.getString(2);
        dbUser.details += "\n";

        dbUser.details += "EMAIL: ";
        dbUser.details += c.getString(1);
        dbUser.details += "\n";

        dbUser.details += "GENDER: ";
        dbUser.details += c.getString(3);
        dbUser.details += "\n";

        dbUser.details += "ADDRESS: ";
        dbUser.details += c.getString(4);
        dbUser.details += "\n";

        dbUser.myImageId = c.getBlob(5);

        dbUser.details += "CONTACT: ";
        dbUser.details += c.getString(6);
        dbUser.details += "\n\n";

        return dbUser;
    }

    public User[] fetchAllUsers() {

        User[] dbUser;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();
        int i = 0,size=0;

        while( !c.isAfterLast() ) {
            c.moveToNext();
            size++;
        }
        dbUser = new User[size];

        c.moveToFirst();

        while( !c.isAfterLast()  ) {

            dbUser[i] = new User();

            dbUser[i].details = "";

            dbUser[i].details += "NAME: ";
            dbUser[i].details += c.getString(2);
            dbUser[i].details += "\n";

            dbUser[i].details += "EMAIL: ";
            dbUser[i].details += c.getString(1);
            dbUser[i].details += "\n";

            dbUser[i].details += "GENDER: ";
            dbUser[i].details += c.getString(3);
            dbUser[i].details += "\n";

            dbUser[i].details += "ADDRESS: ";
            dbUser[i].details += c.getString(4);
            dbUser[i].details += "\n";

            dbUser[i].myImageId = c.getBlob(5);

            dbUser[i].details += "CONTACT: ";
            dbUser[i].details += c.getString(6);
            dbUser[i].details += "\n\n";

            i++;
            c.moveToNext();
        }

        return dbUser;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == R.id.action_settings) {
            return true;
        }
        if( id == R.id.action_showDB) {

            User[] users = fetchAllUsers();

            String[] details = new String[users.length];
            int[] images = new int[users.length];

            for( int i = 0; i < users.length; i++ ) {
                details[i] = users[i].details;
                images[i] = ByteBuffer.wrap(users[i].myImageId).getInt();
            }

            Intent showDatabase = new Intent(this,ShowDB.class);

            Bundle myBundle = new Bundle();

            myBundle.putStringArray("All Details",details);
            myBundle.putIntArray("All Pics", images);

            showDatabase.putExtras(myBundle);

            startActivity(showDatabase);

            return true;
        }

        return false;
    }
}
