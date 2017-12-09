package com.example.proj.sent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private Button signinButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        signinButton = (Button) findViewById(R.id.signin_button);
        registerButton = (Button) findViewById(R.id.registerbutton);



        // Choose authentication providers
        /*
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
         */
    }

    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
                Log.d("Signin", "Success");

            } else {
                // Sign in failed, check response for error code
                // ...
                Log.d("Signin", "Failed");
            }
        }
    }
    */

    protected void signInClicked(View v)
    {
        goToHome();
    }

    protected void goToHome()
    {
        Intent i = new Intent();
        i.setClass(Login.this, HomeActivity.class);
        startActivity(i);

    }
}
