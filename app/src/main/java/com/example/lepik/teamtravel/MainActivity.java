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

public class MainActivity extends Activity {

    static int lastID;
    static int screenSizeX;
    LinearLayout placesList;

    EditText inputName;
    EditText inputLatitude;
    EditText inputLongitude;

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
    }

    public void addNewPlace(View view) {
        //Добавление в разметку нового места
        inputName = findViewById(R.id.inputName);
        inputLatitude = findViewById(R.id.inputLatitude);
        inputLongitude = findViewById(R.id.inputLongitude);

        String inputedName = inputName.getText().toString();
        String inputedLatitude = inputLatitude.getText().toString();
        String inputedLongitude = inputLongitude.getText().toString();

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
        newPlaceName.setText(inputedName);
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
        newPlaceSurname.setText(inputedName);
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
