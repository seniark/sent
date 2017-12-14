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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TickDetailActivity extends BaseNavDrawerActivity {

    public static final String ARG_TICKID = "tickid";

    private TextView routeName;
    private TextView routeGrade;
    private ImageView route_img;
    private String mTickID;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mTickListRef;
    private Tick mTick;
    private Tick.Field mEditing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_tick_detail, contentFrameLayout);

        Bundle args = getIntent().getExtras();

        routeGrade = findViewById(R.id.detailRouteGradeTextView);
        route_img = findViewById(R.id.tickDetailBanner);
        routeName =  findViewById(R.id.detailRouteNameTextView);

        mEditing = Tick.Field.NAME;

        mTickID = args.getString(ARG_TICKID);
        Log.d("dbg", "Tick ID: " + mTickID);

        mDatabase = FirebaseDatabase.getInstance();
        mTickListRef = mDatabase.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ValueEventListener tickListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot tickSnapshot : dataSnapshot.getChildren())
                {
                    //deal with tick object
                    Log.d("dbg", "tickSnapshot.getKey() = " + tickSnapshot.getKey());
                    if(tickSnapshot.getKey().equals(mTickID))
                    {
                        mTick = tickSnapshot.getValue(Tick.class);
                        Log.d("dbg", tickSnapshot.getValue(Tick.class).getName());
                        break;
                    }
                    i++;
                }

                //load it into the view
                routeName.setText(mTick.getName());
                routeGrade.setText(mTick.getGrade());
                if(!TickDetailActivity.this.isFinishing()) {
                    GlideApp.with(TickDetailActivity.this)
                            .load(mTick.getImage_uri())
                            .into(route_img);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting tick failed, log a message
                Log.w("dbg", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mTickListRef.addValueEventListener(tickListener);
    }

    public void updateExistingTickDb(Tick t)
    {
        DatabaseReference dr= mTickListRef.child(t.getDbkey());
        dr.setValue(t);
    }

    public void postTickToEnd(Tick t)
    {
        mTickListRef.push().setValue(t);
    }

    protected void setNewValue(Tick.Field tf)
    {
        mEditing = tf;

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Enter New Value:");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String value = input.getText().toString();

                switch(mEditing)
                {
                    case NAME:
                        mTick.setName(value);
                        break;
                    case GRADE:
                        mTick.setGrade(value);
                        break;
                    case IMAGEURI:
                        Log.d("dbg", "NOT IMPLEMENTED");
                        break;
                    default:
                        Log.d("dbg", "field not found");
                        return;
                }
                updateExistingTickDb(mTick);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void editPressed (View v)
    {
        switch(v.getId())
        {
            case R.id.editNameButton:
                setNewValue(Tick.Field.NAME);
                break;
            case R.id.editGradeButton:
                setNewValue(Tick.Field.GRADE);
                break;
            default:
                break;
        }


    }
}
