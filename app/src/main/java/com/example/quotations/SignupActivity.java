package com.example.quotations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);
    }

    public void signup(View view)
    {
        String userName = ((EditText)findViewById(R.id.etNewUserName)).getText().toString();
        String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.etNewPassword)).getText().toString();
        String confirmPassword = ((EditText)findViewById(R.id.etConfirmPassword)).getText().toString();

        if(!password.isEmpty() && password.equals(confirmPassword)) {

            ParseUser user = new ParseUser();
            user.setUsername(userName);
            user.setPassword(password);
            user.setEmail(email);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        Intent i = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        int code = e.getCode();
                        if (code == 100)
                            Toast.makeText(getBaseContext(), getString(R.string.signup_connection_error), Toast.LENGTH_LONG).show();
                        else if (code == 202)
                            Toast.makeText(getBaseContext(), getString(R.string.signup_user_exist), Toast.LENGTH_LONG).show();
                        else if (code == 125)
                            Toast.makeText(getBaseContext(), getString(R.string.signup_invalid_email), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getBaseContext(), getString(R.string.signup_unknown_error), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast toast = Toast.makeText(getBaseContext(), getString(R.string.signup_password_does_not_match), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM| Gravity.CENTER, 0, 10);
            toast.show();
            ((EditText) findViewById(R.id.etNewPassword)).setText("");
            ((EditText) findViewById(R.id.etNewUserName)).setText("");
        }
    }
}
