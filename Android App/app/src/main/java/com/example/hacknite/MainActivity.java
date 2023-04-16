package com.example.hacknite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SharedPreferences status;
    public static final String MyPREFERENCES = "MyPrefs";

    EditText mail, pass;
    Button signup;
    ProgressBar loading;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mail = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        signup = (Button) findViewById(R.id.sign_up);
        loading = (ProgressBar) findViewById(R.id.loading);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        status = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (!status.contains("status")) {
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.createUserWithEmailAndPassword(mail.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        SharedPreferences.Editor editor = status.edit();

                                        editor.putString("status", "y");
                                        editor.putString("email", mail.getText().toString());
                                        editor.putString("pass", pass.getText().toString());
                                        editor.apply();

                                        Intent intent = new Intent(MainActivity.this, SelectionAndHistory.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }
        else {
            mail.setVisibility(View.GONE);
            pass.setVisibility(View.GONE);
            signup.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(status.getString("email", ""), status.getString("pass", ""))
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent intent = new Intent(MainActivity.this, SelectionAndHistory.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}