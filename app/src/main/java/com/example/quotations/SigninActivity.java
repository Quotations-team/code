package com.example.quotations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

public class SigninActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signin);
    }

    public void signin(View view)
    {
        // code to hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(
                view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

        String userName = ((EditText)findViewById(R.id.etUserName)).getText().toString();
        String password = ((EditText)findViewById(R.id.etPassword)).getText().toString();

        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null && user != null) {
                    Intent i = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    int code = e.getCode();
                    if(code == 100)
                        Toast.makeText(getBaseContext(), getString(R.string.signin_connection_error), Toast.LENGTH_LONG).show();
                    else if(code == 101)
                        Toast.makeText(getBaseContext(), getString(R.string.signin_incorrect_user_pass), Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getBaseContext(), getString(R.string.signin_unknown_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}