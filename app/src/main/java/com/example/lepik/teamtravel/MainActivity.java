package com.example.lepik.teamtravel;

import android.app.Activity;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity{

    static int lastID;
    static int screenSizeX;
    LinearLayout placesList;

    EditText inputName;
    EditText inputSurname;
    EditText inputLatitude;
    EditText inputLongitude;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesList = findViewById(R.id.placesList);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenSizeX = size.x;

        lastID = 1000000;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("teams");


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ff", "*****ADDED****");
                Person person = dataSnapshot.getValue(Person.class);
                addNewLocation(person);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("ff", "*****CHANGED****");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("ff", "*****REMOVED****");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("ff", "****MOVED****");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ff", "*****CANCELED*****");
            }
        });

    }

    public void addNewPlace(View view) {
        //Добавление в разметку нового места
        inputName = findViewById(R.id.inputName);
        inputSurname = findViewById(R.id.inputSurname);
        inputLatitude = findViewById(R.id.inputLatitude);
        inputLongitude = findViewById(R.id.inputLongitude);

        String inputedName = inputName.getText().toString();
        String inputedSurname = inputSurname.getText().toString();
        double inputedLatitude = Double.parseDouble(inputLatitude.getText().toString());
        double inputedLongitude = Double.parseDouble(inputLongitude.getText().toString());

        Person person = new Person();

        String newID = myRef.push().getKey();

        person.setId(newID);
        person.setFirstName(inputedName);
        person.setSecondName(inputedSurname);
        person.setLatitudeCoord(inputedLatitude);
        person.setLongitudeCoord(inputedLongitude);

        myRef.child(newID).setValue(person);

        addNewLocation(person);
    }

    public void addNewLocation(Person person){
        //добавление элемента в список мест
        LinearLayout newPlace = new LinearLayout(this);
        newPlace.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams newPlaceLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        newPlace.setLayoutParams(newPlaceLayoutParams);

        //добавление имени места
        TextView newPlaceName = new TextView(this);

        LinearLayout.LayoutParams namelayoutParams = new LinearLayout.LayoutParams(
                screenSizeX/4,
                0,
                1
        );

        newPlaceName.setId(lastID);
        newPlaceName.setGravity(Gravity.CENTER);
        newPlaceName.setText(person.getFirstName());
        newPlace.setLayoutParams(namelayoutParams);

        newPlace.addView(newPlaceName);

        //добавление второго имени места
        TextView newPlaceSurname = new TextView(this);

        LinearLayout.LayoutParams surnamelayoutParams = new LinearLayout.LayoutParams(
                screenSizeX/4,
                0,
                5
        );

        newPlaceSurname.setId(lastID + 1000000);
        newPlaceSurname.setGravity(Gravity.CENTER);
        newPlaceSurname.setText(person.getSecondName());
        newPlaceSurname.setLayoutParams(surnamelayoutParams);

        newPlace.addView(newPlaceSurname);

        TextView newPlaceDist = new TextView(this);

        LinearLayout.LayoutParams distLayoutParams = new LinearLayout.LayoutParams(
                screenSizeX/4,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );

        newPlaceDist.setId(lastID + 2000000);
        newPlaceDist.setGravity(Gravity.CENTER);
        newPlace.setLayoutParams(distLayoutParams);
        newPlaceDist.setText("55");

        newPlace.addView(newPlaceDist);

        placesList.addView(newPlace);
        lastID++;

    }
}
