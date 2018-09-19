package com.example.lepik.teamtravel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends Activity {

    EditText email;
    EditText password;

    Button signIn;
    Button toRegistration;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.inputEmailAuth);
        password = findViewById(R.id.inputPasswordAuth);

        email.setText("blet@mail.ru");
        password.setText("qwerty");

        signIn = findViewById(R.id.signInBtn);
        toRegistration = findViewById(R.id.registrBtnAuth);

        auth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignIn();
            }
        });

        toRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    void attemptSignIn() {
        try {
        String inputedEmail = email.getText().toString().trim();
        String inputedPassword = password.getText().toString().trim();

        if (inputedEmail.equals(""))
            return;

        if (inputedPassword.equals(""))
            return;
        auth.signInWithEmailAndPassword(inputedEmail, inputedPassword).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("ff", "createUserWithEmail:success");
                    Intent intent  = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ff", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });}
        catch (Exception ex) {
            Log.d ("ff", ex.toString());
        }
    }
}
