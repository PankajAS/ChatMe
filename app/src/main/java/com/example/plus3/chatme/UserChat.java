package com.example.plus3.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserChat extends AppCompatActivity {
    private EditText editText;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private ListView listView;
    private ArrayAdapter<String> adapter;
    ChatArrayAdapter chatArrayAdapter;
    private boolean side = false;
    Button button;
    String Chatid;
    String UserId;
    JSONArray jsonArray;




    public void sendMsg(View v) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        System.out.println();
        String time = year + "" + month + "" + day + "" + hours + "" + minute + "" + sec;
        String keySend = databaseReference.child("Sends").push().getKey();
        String keyReceive = databaseReference2.child("Receive").push().getKey();
        String mainKey = databaseReference2.child("Inbox").push().getKey();
        Intent inte = getIntent();

        databaseReference.child("Inbox").child(mainKey).child("body").setValue(editText.getText().toString());
        databaseReference.child("Inbox").child(mainKey).child("time").setValue(time);
        databaseReference.child("Inbox").child(mainKey).child("MessageBy").setValue(inte.getStringExtra("CurrentUser"));

        databaseReference2.child("Inbox").child(mainKey).child("body").setValue(editText.getText().toString());
        databaseReference2.child("Inbox").child(mainKey).child("time").setValue(time);
        databaseReference2.child("Inbox").child(mainKey).child("MessageBy").setValue(inte.getStringExtra("ChatUser"));

        databaseReference.child("Sends").child(keySend).child("body").setValue(editText.getText().toString());
        databaseReference.child("Sends").child(keySend).child("time").setValue(time);
        databaseReference2.child("Receive").child(keySend).child("body").setValue(editText.getText().toString());
        databaseReference2.child("Receive").child(keySend).child("time").setValue(time);
        editText.setText("");
    }
    public class ChatMessage {
        public boolean left;
        public String message;

        public ChatMessage(boolean left, String message) {
            super();
            this.left = left;
            this.message = message;
        }
    }

    private boolean sendChatMessage(String name, Boolean right){
        side = right;
        chatArrayAdapter.add(new ChatMessage(side, name));
        editText.setText("");
         return true;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText = (EditText) findViewById(R.id.textmsg);
        listView = (ListView) findViewById(R.id.msgs);
        button = (Button)findViewById(R.id.send);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = database.getInstance();
        UserId  =user.getUid().toString();
        final Intent intent = getIntent();
        Intent intent2 = getIntent();
        setTitle(intent2.getStringExtra("UserName"));
        databaseReference = database.getReference("Users").child(intent.getStringExtra("CurrentUser")).child("Messages").child(intent.getStringExtra("ChatUser"));
        databaseReference2 = database.getReference("Users").child(intent.getStringExtra("ChatUser")).child("Messages").child(intent.getStringExtra("CurrentUser"));

        jsonArray = new JSONArray();
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.left);
        listView.setAdapter(chatArrayAdapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMsg(button);
            }
        });

        //Chat Messages
        databaseReference.child("Inbox").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String msg = null;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //System.out.println(data.getKey());
                    //jsonArray.put(data.getValue());
                    String name = null;
                    String id = null;
                    String time = null;

                    for (DataSnapshot dataChild : data.getChildren()) {


                        if (dataChild.getKey().equals("body")) {
                            msg = dataChild.getValue().toString();
                            //System.out.println("Hello  "+msg);
                            name = dataChild.getValue().toString();
                        }
                        if (dataChild.getKey().equals("MessageBy")) {
                            Chatid = dataChild.getValue().toString();
                            id = dataChild.getValue().toString();
                        }
                        if (dataChild.getKey().equals("time")) {

                            time = dataChild.getValue().toString();
                        }


                        if (msg != null) {
                            arrayList.add(msg);
                        }

                    }
                    String uId = UserId;
                    if (uId != null) {
                        if (id != null && name != null && time != null && uId != null) {
                            if (id.equals(uId)) {
                                sendChatMessage(name, true);
                            } else if(!id.equals(uId)) {
                                sendChatMessage(name, false);
                            }
                        }
                    }
                }
                chatArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            //overridePendingTransition(R.transition.stay, R.transition.slide_down);
        }

        return super.onOptionsItemSelected(item);
    }
}


