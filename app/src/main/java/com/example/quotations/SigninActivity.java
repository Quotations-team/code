package com.example.quotations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;

public class SigninActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void signin(View view)
    {
        String userName = ((EditText)findViewById(R.id.etUserName)).getText().toString();
        String password = ((EditText)findViewById(R.id.etPassword)).getText().toString();

        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    Intent i = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG);
                }
            }
        });
    }
}