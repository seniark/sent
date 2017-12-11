package com.example.proj.sent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class BaseNavDrawerActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    private TextView userName;
    private TextView userEmail;
    private RelativeLayout headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_nav_drawer);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.ndopen, R.string.ndclose);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        View hView = navigationView.getHeaderView(0);

        userName = hView.findViewById(R.id.header_name);
        userEmail = hView.findViewById(R.id.header_email);
        headerLayout = hView.findViewById(R.id.header_layout);

        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.item1:
                        Intent anIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.item2:
                        anIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.item3:
                        anIntent = new Intent(getApplicationContext(), TickListActivity.class);
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.item4:
                        anIntent = new Intent(getApplicationContext(), WODActivity.class);
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;

                }
                return false;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

}