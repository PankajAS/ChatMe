package com.example.plus3.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private  EditText email, password;
    private DatabaseReference myRef,myRef1;
    private String userId;
    private ProgressBar progressBar;
    private Button button;



    public void login(View view){

        String email = this.email.getText().toString();
        String passwords = password.getText().toString();

        if(!email.isEmpty() && !passwords.isEmpty()) {

            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email, passwords).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = task.getResult().getUser().getUid().toString();
                            Intent intent = new Intent(getApplicationContext(), TabViewActivity.class);
                            if (id != null) {
                                userId = id;
                                intent.putExtra("UID", userId);
                                startActivity(intent);
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), task.getException().toString().substring(task.getException().toString().indexOf(" ")), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
        else
        {
            Toast.makeText(getApplicationContext(), R.string.loginError,Toast.LENGTH_LONG).show();
        }
    }

    public void signUp(View view){
        startActivity(new Intent(getApplicationContext(),SingUpActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = (Button)findViewById(R.id.login);

        setContentView(R.layout.activity_main);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        password.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER){
            login(button);
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
