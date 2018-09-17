package com.example.lepik.teamtravel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.FirebaseAppHelper;

public class RegistrationActivity extends AppCompatActivity {

    Person newPerson;

    EditText email;
    EditText firstName;
    EditText secondName;
    EditText password;
    EditText confirmPassword;

    Button registrationButton;

    FirebaseDatabase database;
    DatabaseReference usersRef;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.inputEmailReg);
        firstName = findViewById(R.id.inputFirstNameReg);
        secondName = findViewById(R.id.inputSecondNameReg);
        password = findViewById(R.id.inputPasswordReg);
        confirmPassword = findViewById(R.id.confirmPasswordReg);

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        auth = FirebaseAuth.getInstance();

        registrationButton = findViewById(R.id.finishRegistration);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
    }

    private void attemptRegistration(){
        newPerson = new Person();

        String inputedEmail = email.getText().toString().trim();
        String inputedFirstName = firstName.getText().toString().trim();
        String inputedSecondName = secondName.getText().toString().trim();
        String inputedPassword = password.getText().toString().trim();
        String inputedConfirmPassword = confirmPassword.getText().toString().trim();

        if (inputedEmail.equals("")) {
            Toast.makeText(RegistrationActivity.this, "E-mail is empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputedFirstName.equals("")) {
            Toast.makeText(RegistrationActivity.this, "First name is empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputedSecondName.equals("")) {
            Toast.makeText(RegistrationActivity.this, "Second name is empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputedPassword.equals("")) {
            Toast.makeText(RegistrationActivity.this, "Password is empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputedConfirmPassword.equals("")) {
            Toast.makeText(RegistrationActivity.this, "Passwords not coincide",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!inputedPassword.equals(inputedConfirmPassword)) {
            Toast.makeText(RegistrationActivity.this, "Passwords not coincide",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        newPerson.setFirstName(inputedFirstName);
        newPerson.setSecondName(inputedSecondName);
        newPerson.setEmail(inputedEmail);
        newPerson.setPassword(inputedPassword);
        newPerson.setLongitudeCoord(0.0);
        newPerson.setLatitudeCoord(0.0);


        auth.createUserWithEmailAndPassword(inputedEmail, inputedPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ff", "createUserWithEmail:success");
                            newPerson.setId(task.getResult().getUser().getUid());
                            usersRef.child(newPerson.getID()).setValue(newPerson);
                            Log.d("ff", newPerson.getID());
                            Intent intent  = new Intent(RegistrationActivity.this, SignInActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ff", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
