package com.plusOneSoftwares.plusOneSoftwares.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    EditText name,phone,email,password;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ProgressBar progressBar;
    private Utils utils;

    public void signUp(View v){

       if(!name.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()
                && !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {

           utils.hideSoftKeyboard(this);//hide keyboard

           progressBar.setVisibility(View.VISIBLE);
           String uemail = email.getText().toString();
           String upassword = password.getText().toString();

            auth.createUserWithEmailAndPassword(uemail, upassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String id = task.getResult().getUser().getUid().toString();

                        if (id != null) {

                            myRef = database.getReference("Users").child(id).child("Details");
                            myRef.child("Name").setValue(name.getText().toString());
                            myRef.child("Phone").setValue(phone.getText().toString());
                            myRef.child("Email").setValue(email.getText().toString());
                            myRef.child("Passwords").setValue(password.getText().toString());
                            myRef.child("UID").setValue(id);
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("UID", id);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), R.string.signupSuccess, Toast.LENGTH_SHORT).show();

                    } else {
                        String msg;
                        msg = task.getException().toString().substring(task.getException().toString().indexOf(" "));
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), R.string.signupError,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        name =  (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        email = (EditText)findViewById(R.id.emailS);
        password = (EditText)findViewById(R.id.passwords);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        myRef = database.getReference("Users");
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        utils = new Utils();


    }
}