package com.example.lepik.teamtravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewPlaceActivity extends AppCompatActivity {

    Button addButton;

    EditText inputNameField;
    EditText inputLatitudeField;
    EditText inputLongitudeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        inputNameField = findViewById(R.id.inputName);
        inputLatitudeField = findViewById(R.id.inputLatitude);
        inputLongitudeField = findViewById(R.id.inputLongitude);

        addButton = findViewById(R.id.addNewPlace);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = inputPerson();
                if (person == null)
                    return;
                Log.d("ff", person.getFirstName());
                Log.d("ff", Double.toString(person.getLatitudeCoord()));
                Log.d("ff", Double.toString(person.getLongitudeCoord()));

                Intent intent = new Intent(AddNewPlaceActivity.this, MainActivity.class);
                intent.putExtra("newPerson", person);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private Person inputPerson() {
        boolean allFieldsFilled = false;
        StringBuilder errToastText = new StringBuilder();

        Person person = new Person();

        if (!inputNameField.getText().toString().trim().equals(""))
            person.setFirstName(inputNameField.getText().toString().trim());
        else{
            Log.d("err","Input name is empty");
            errToastText.append("Place name");
            allFieldsFilled = true;
        }

        if (!inputLatitudeField.getText().toString().trim().equals(""))
            person.setLatitudeCoord(Double.parseDouble(inputLatitudeField.getText().toString().trim()));
        else{
            Log.d("err","Input latitude is empty");
            errToastText.append(allFieldsFilled ? ", latitude" : "Latitude");
            allFieldsFilled = true;
        }

        if (!inputLongitudeField.getText().toString().trim().equals(""))
            person.setLongitudeCoord(Double.parseDouble(inputLongitudeField.getText().toString().trim()));
        else{
            Log.d("err","Input longitude is empty");
            errToastText.append(allFieldsFilled ? ", longitude" : "Longitude");
            allFieldsFilled = true;
        }

        if (allFieldsFilled) {
            errToastText.append(" is empty");
            Toast errToast = Toast.makeText(this, errToastText.toString(), Toast.LENGTH_SHORT);
            errToast.show();
            return null;
        }

        person.setSecondName("");

        return  person;
    }
}
