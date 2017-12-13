package com.example.proj.sent;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TickDetailActivity extends BaseNavDrawerActivity {

    public static final String ARG_NAME = "name";
    public static final String ARG_GRADE = "grade";
    public static final String ARG_URI = "uri";

    TextView routeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_tick_detail, contentFrameLayout);

        Bundle args = getIntent().getExtras();
        routeName =  findViewById(R.id.detailRouteNameTextView);
        routeName.setText(args.getString(ARG_NAME));

        ((TextView) findViewById(R.id.detailRouteGradeTextView)).setText(args.getString(ARG_GRADE));
        ImageView route_img = findViewById(R.id.tickDetailBanner);

        GlideApp.with(TickDetailActivity.this)
                .load(args.getString(ARG_URI))
                .into(route_img);
    }

    protected void changeRouteName()
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Log.d("edit", value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    public void editNameButtonPressed (View v)
    {
        changeRouteName();
    }
}
