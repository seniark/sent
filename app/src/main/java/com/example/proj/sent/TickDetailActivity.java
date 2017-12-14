package com.example.proj.sent;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class TickDetailActivity extends BaseNavDrawerActivity {

    public static final String ARG_TICKID = "tickid";

    private TextView routeName;
    private TextView routeGrade;
    private ImageView route_img;
    private TextView route_location;
    private TextView route_date;
    private TextView route_notes;
    private String mTickID;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mTickListRef;
    private Tick mTick;
    private Tick.Field mEditing;
    private int PLACE_PICKER_REQUEST = 1;
    private Calendar mCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_tick_detail, contentFrameLayout);


        Bundle args = getIntent().getExtras();

        routeGrade = findViewById(R.id.detailRouteGradeTextView);
        route_img = findViewById(R.id.tickDetailBanner);
        routeName =  findViewById(R.id.detailRouteNameTextView);
        route_location = findViewById(R.id.detailLocationTextView);
        route_date = findViewById(R.id.detailDateTextView);
        route_notes = findViewById(R.id.notesTextEdit);


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
                if(mTick != null) {
                    routeName.setText(mTick.getName());
                    routeGrade.setText(mTick.getGrade());
                    route_location.setText(mTick.getLocation());
                    route_date.setText(mTick.getDate());
                    route_notes.setText(mTick.getDescription());
                    if (!TickDetailActivity.this.isFinishing()) {
                        GlideApp.with(TickDetailActivity.this)
                                .load(mTick.getImage_uri())
                                .into(route_img);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting tick failed, log a message
                Log.w("dbg", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mTickListRef.addValueEventListener(tickListener);

        mCalendar = Calendar.getInstance();
    }

    public void updateExistingTickDb(Tick t)
    {
        DatabaseReference dr= mTickListRef.child(t.getDbkey());
        dr.setValue(t);
    }

    public void deleteExistingTickDb(Tick t)
    {
        DatabaseReference dr = mTickListRef.child(t.getDbkey());
        dr.removeValue();
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
                    case DESC:
                        mTick.setDescription(value);
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
            case R.id.editNotesButton:
                setNewValue(Tick.Field.DESC);
            default:
                break;
        }


    }

    public void mapPickClicked(View v)
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }
        catch (Exception e)
        {
            Log.d("","GOOGLE PLAY NOT AVAILABLE");
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                //String toastMsg = String.format("Place: %s", place.getName());
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                mTick.setLocation(place.getName().toString());
                updateExistingTickDb(mTick);
            }
        }
    }

    public void datePickClicked(View v)
    {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                //set text field
                ///TextView date = findViewById(R.id.detailDateTextView);
                //date.setText(sdf.format(mCalendar.getTime()));
                mTick.setDate(sdf.format(mCalendar.getTime()));
                updateExistingTickDb(mTick);
            }

        };

        new DatePickerDialog(TickDetailActivity.this, date, mCalendar
                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    public void deleteClicked(View v)
    {
        deleteExistingTickDb(mTick);
        Intent i = new Intent(getApplicationContext(), TickListActivity.class);
        startActivity(i);
        //Toast.makeText(this, "delete", Toast.LENGTH_LONG).show();
    }
}
