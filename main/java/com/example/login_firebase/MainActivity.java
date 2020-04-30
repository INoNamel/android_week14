package com.example.login_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button login, logout;
    private EditText password, email;

    private TextView secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        secret = findViewById(R.id.secret);

        password = findViewById(R.id.pwdText);
        email = findViewById(R.id.emailText);

        login = findViewById(R.id.login);
        logout = findViewById(R.id.logout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signIn(email.getText().toString(), password.getText().toString()); }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(@Nullable FirebaseUser currentUser) {
        if(currentUser != null) {
            secret.setText(" \\_ O ^ O _/ ");
        } else {
            secret.setText("secret");
        }
    }

    private void signIn(String email, String password) {
        if (validateForm()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void signOut() {
        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            updateUI(null);
            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Log in first", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        String emailInput = email.getEditableText().toString();
        String pwdInput = password.getEditableText().toString();
        if (emailInput.isEmpty()) {
            email.setError("empty");
        return false;
        } else if (pwdInput.isEmpty()) {
            password.setError("empty");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
}
