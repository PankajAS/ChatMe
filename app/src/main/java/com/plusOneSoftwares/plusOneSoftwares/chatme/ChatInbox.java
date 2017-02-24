package com.plusOneSoftwares.plusOneSoftwares.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

public class ChatInbox extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase fdatabase;
    DatabaseReference ref;
    List<Chat> mChats;
    private RecyclerView mRecyclerView;
    Button mbtSent;
    EditText metText;
    ChatAdapter mAdapter;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatinbox);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fdatabase = fdatabase.getInstance();
        ref =fdatabase.getReference("Users").child("a7rsTgsHLGRvCJbEDXLPno7u8XJ3").child("Messages");
        metText = (EditText)findViewById(R.id.etText);
        mbtSent = (Button)findViewById(R.id.btSent);
        mRecyclerView = (RecyclerView)findViewById(R.id.rvChat);
        mChats = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        String idU = intent.getStringExtra("UID");
        mAdapter = new ChatAdapter(mChats,idU);
        mRecyclerView.setAdapter(mAdapter);

        ref.child("72K4mHCv4ieeNLQFOqx4TsqjJOJ3").child("Inbox").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        Chat model = dataSnapshot.getValue(Chat.class);
                        mChats.add(model);
                        mRecyclerView.scrollToPosition(mChats.size() -1);
                        mAdapter.notifyItemInserted(mChats.size() -1);

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
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
}
