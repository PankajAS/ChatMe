package com.example.plus3.chatme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class UserChat extends AppCompatActivity{
    private EditText editText;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private Button button;
    private String UserId;
    private RecyclerView mRecyclerView;
    private List<Chat> mChats;
    private ChatAdapter mAdapter;
    Chat model;
    SharedPreferences pref, pref1;
    SharedPreferences.Editor editor;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatinbox);
        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText = (EditText) findViewById(R.id.etText);
        button = (Button) findViewById(R.id.btSent);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = database.getInstance();
        UserId = user.getUid().toString();
        //UserId = "a7rsTgsHLGRvCJbEDXLPno7u8XJ3";
        final Intent intent = getIntent();
        Intent intent2 = getIntent();
        setTitle(intent2.getStringExtra("UserName"));
        mRecyclerView = (RecyclerView)findViewById(R.id.rvChat);
        mChats = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = database.getReference("Users").child(intent.getStringExtra("CurrentUser")).child("Messages").child(intent.getStringExtra("ChatUser"));
        databaseReference2 = database.getReference("Users").child(intent.getStringExtra("ChatUser")).child("Messages").child(intent.getStringExtra("CurrentUser"));
        pref = getApplicationContext().getSharedPreferences("com.example.plus3.chatme", Context.MODE_PRIVATE);
        editor  = pref.edit();


        mAdapter = new ChatAdapter(mChats,UserId);
        mRecyclerView.setAdapter(mAdapter);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sendMessages();
            }
        });


        databaseReference.child("Inbox").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        model = dataSnapshot.getValue(Chat.class);
                        mChats.add(model);
                        mRecyclerView.scrollToPosition(mChats.size() -1);
                        mAdapter.notifyItemInserted(mChats.size() -1);
                        editor.putString(model.getMessageBy(),model.getTime());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                editor.commit();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendMessages() {
        String message = editText.getText().toString();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String time = year + "" + month + "" + day + "" + hours + "" + minute + "" + sec;


        if(message !=null && !message.isEmpty()){

            databaseReference.child("Inbox").push().setValue(new Chat(time, message + "    " + getCurrentTime(), UserId));
            databaseReference2.child("Inbox").push().setValue(new Chat(time, message, UserId));
           //System.out.println(new Chat(time, message + "    " + getCurrentTime(), UserId).getBody());
            //Chat chat2 = new Chat(time, message + "    " + getCurrentTime(), UserId);
            //editor.putString(chat2.getTime(), chat2.getMessageBy());
            //editor.commit();

        }

        editText.setText("");
    }

    private String getCurrentTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


