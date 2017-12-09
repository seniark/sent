package com.example.proj.sent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends BaseNavDrawerActivity {

    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_user_profile, contentFrameLayout);

        name = findViewById(R.id.name_text);

        name.setText("Hello, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());



    }

    protected void signOutClicked(View v)
    {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        goToSplash();
                    }
                });

    }

    protected void goToSplash()
    {
        Intent i = new Intent();
        i.setClass(UserProfileActivity.this, SplashScreenActivity.class);
        startActivity(i);
    }
}
