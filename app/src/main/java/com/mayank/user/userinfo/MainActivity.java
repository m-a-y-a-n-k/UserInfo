package com.mayank.user.userinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private RadioGroup userType;
    private RadioButton newUser;
    private RadioButton existingUser;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userType = (RadioGroup) findViewById( R.id.UserType);

        newUser = (RadioButton)findViewById(R.id.newUser);
        existingUser = (RadioButton) findViewById(R.id.existingUser);

        submit = (Button) findViewById( R.id.submit);

    }

    public void submitClicked( View v) {

        if( !newUser.isChecked() && !existingUser.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please Select an Option!!", Toast.LENGTH_LONG).show();
        }

        if( newUser.isChecked() ) {

            Intent signUp = new Intent( MainActivity.this,SignUp.class);
            startActivity(signUp);
        }

        if( existingUser.isChecked() ) {
            Intent signIn = new Intent(MainActivity.this,LogIn.class);
            startActivity(signIn);
        }
    }
}

