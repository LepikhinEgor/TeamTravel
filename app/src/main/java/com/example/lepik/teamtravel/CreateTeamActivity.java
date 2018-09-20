package com.example.lepik.teamtravel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateTeamActivity extends Activity {

    EditText inputName;
    EditText inputPassword;

    Button createTeamBtn;

    DatabaseReference teamsRef;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        inputName = findViewById(R.id.inputTeamName);
        inputPassword = findViewById(R.id.inputTeamPassword);

        createTeamBtn = findViewById(R.id.createTeamButton);

        database = FirebaseDatabase.getInstance();
        teamsRef = database.getReference("teams");

        setCreateBtnListener();
    }

    private void setCreateBtnListener() {
        createTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputedName = inputName.getText().toString().trim();
                String inputedPassword = inputPassword.getText().toString().trim();

                if (inputedName.equals(""))
                {
                    Toast.makeText(CreateTeamActivity.this, "Team name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (inputedPassword.equals(""))
                {
                    Toast.makeText(CreateTeamActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String newID = teamsRef.push().getKey();

                teamsRef.child(newID).child("name").setValue(inputedName);
                teamsRef.child(newID).child("password").setValue(inputedPassword);

                Intent intent = new Intent(CreateTeamActivity.this, MainActivity.class);
                intent.putExtra("TeamId", newID);
                intent.putExtra("TeamName", inputedName);
                intent.putExtra("TeamPassword", inputedPassword);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
