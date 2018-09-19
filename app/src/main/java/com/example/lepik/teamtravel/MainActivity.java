package com.example.lepik.teamtravel;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    TextView latitude;
    TextView longitude;

    Person user;
    ArrayList<LinearLayout> personsLayout;
    static int screenSizeX;
    LinearLayout placesList;

    ImageButton toAddActivityBtn;

    FirebaseDatabase database;
    DatabaseReference teamsRef;
    DatabaseReference usersRef;

    TextView selectedPersFullName;
    TextView selectedPersLatitude;
    TextView selectedPersLongitude;
    TextView userFirstName;
    TextView userSecondName;

    private FirebaseAuth auth;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new Person();
        userFirstName = findViewById(R.id.userFirstName);
        userSecondName = findViewById(R.id.userSecondName);

        personsLayout = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        selectedPersFullName = findViewById(R.id.selectedFullName);
        selectedPersLatitude = findViewById(R.id.selectedLatitudeValue);
        selectedPersLongitude = findViewById(R.id.selectedLongitudeValue);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000 * 5, 0, locationListener);

        toAddActivityBtn = findViewById(R.id.addNewPlace);

        toAddActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((MainActivity.this), AddNewPlaceActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        placesList = findViewById(R.id.placesList);

        placesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ff", "CLICKED");
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenSizeX = size.x;

        database = FirebaseDatabase.getInstance();
        teamsRef = database.getReference("teams");
        usersRef = database.getReference("users");
        usersRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person pers = dataSnapshot.getValue(Person.class);
                if (pers != null) {
                    user = pers;
                    userFirstName.setText(user.getFirstName());
                    userSecondName.setText(user.getSecondName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        teamsRef.addChildEventListener(new ChildEventListener() {
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

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude.setText(Double.toString(location.getLatitude()));
            longitude.setText(Double.toString(location.getLongitude()));
            user.setLatitudeCoord(location.getLatitude());
            user.setLongitudeCoord(location.getLongitude());

            for (LinearLayout layout: personsLayout) {
                int childCount = layout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    String tag = (String)layout.getChildAt(i).getTag();
                    if (tag != null && tag.equals("Distance")) {
                        Person person = (Person)layout.getTag();
                        if (person != null) {
                        double distance = person.calcDistanceFrom(user.getLatitudeCoord(), user.getLongitudeCoord());
                        TextView distView = (TextView)layout.getChildAt(i);
                        distView.setText(Double.toString(distance));
                        }
                    }
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("ff", "RESULT");
        if (data == null) {
            Log.d("ex", "Failed to retrieve data from other Activity");
            return;
        }

        Person person = (Person)data.getSerializableExtra("newPerson");

        String newID = teamsRef.push().getKey();
        person.setId(newID);

        Log.d("ff",person.getFirstName());
        Log.d("ff",Double.toString(person.getLatitudeCoord()));
        Log.d("ff",Double.toString(person.getLongitudeCoord()));
        teamsRef.child(newID).setValue(person);
    }


    public void addNewLocation(Person person){
        //добавление элемента в список мест

        toAddActivityBtn.setVisibility(View.GONE);

        LinearLayout newPlace = new LinearLayout(this);
        newPlace.setOrientation(LinearLayout.VERTICAL);

        newPlace.setTag(person);

        LinearLayout.LayoutParams newPlaceLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        newPlace.setLayoutParams(newPlaceLayoutParams);

        //добавление имени места
        TextView newPlaceName = new TextView(this);
        newPlaceName.setTag("Name");

        LinearLayout.LayoutParams namelayoutParams = new LinearLayout.LayoutParams(
                screenSizeX/4,
                0,
                1
        );

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

        newPlaceSurname.setGravity(Gravity.CENTER);
        newPlaceSurname.setText(person.getSecondName());
        newPlaceSurname.setLayoutParams(surnamelayoutParams);

        newPlace.addView(newPlaceSurname);

        TextView newPlaceDist = new TextView(this);
        newPlaceDist.setTag("Distance");

        LinearLayout.LayoutParams distLayoutParams = new LinearLayout.LayoutParams(
                screenSizeX/4,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );

        newPlaceDist.setGravity(Gravity.CENTER);
        newPlace.setLayoutParams(distLayoutParams);
        newPlaceDist.setText("55");

        newPlace.addView(newPlaceDist);

        placesList.addView(newPlace);

        ImageButton newAddNewPlaceBtn = new ImageButton(this);

        newAddNewPlaceBtn.setLayoutParams(distLayoutParams);
        newAddNewPlaceBtn.setId(R.id.addNewPlace);
        newAddNewPlaceBtn.setImageResource(R.drawable.ic_launcher_background);

        toAddActivityBtn = newAddNewPlaceBtn;

        toAddActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((MainActivity.this), AddNewPlaceActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        personsLayout.add(newPlace);

        newPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person pers = (Person)v.getTag();
                String fullname = pers.getFirstName() + " " + pers.getSecondName();
                selectedPersFullName.setText(fullname);
                selectedPersLatitude.setText(Double.toString(pers.getLatitudeCoord()));
                selectedPersLongitude.setText(Double.toString(pers.getLongitudeCoord()));
            }
        });

        placesList.addView(newAddNewPlaceBtn);
    }
}
