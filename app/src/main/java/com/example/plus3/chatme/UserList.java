package com.example.plus3.chatme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserList extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase listData;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    Utils utils;
    ListView listView;
    ArrayList<String> list= new ArrayList<String>();
    ArrayList<Bitmap> piclist = new ArrayList<>();
    ArrayList<String> userKeys;
    String CURRENT_USER;
    String USER_NAME;
    Map<String, String> map;
    Map<String, Bitmap> pics;


    public  void getCurrentUserName(){
        databaseReference.child(CURRENT_USER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    for (DataSnapshot data2:data.getChildren()){
                        if(data2.getKey().equals("Name")){
                            USER_NAME = data2.getValue().toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle(R.string.app_name);
        listView = (ListView)findViewById(R.id.userList);
        listData = listData.getInstance();
        databaseReference = listData.getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        final Intent intent = getIntent();
        CURRENT_USER = intent.getStringExtra("UID");
        getCurrentUserName();
        map = new HashMap<String,String>();
        pics = new HashMap<String,Bitmap>();
        final Map<String,String> lastmessages = new HashMap<>();
        final Map<String,String> time = new HashMap<>();
        final CustomListAdapter adapter = new CustomListAdapter(this,CURRENT_USER, lastmessages,map,pics,piclist,list);
        listView.setAdapter(adapter);
        userKeys  = new ArrayList<>();

        databaseReference.child(CURRENT_USER).child("Messages").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Chat chat = null;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        for(DataSnapshot data2:data.getChildren()) {
                            for(DataSnapshot data3:data2.getChildren()) {
                                chat = data3.getValue(Chat.class);
                            }
                            lastmessages.put(data.getKey(), chat.getBody());
                            time.put(data.getKey(), chat.getTime());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = null;
                String val = null;
                Bitmap pic = null;

                for(DataSnapshot data1:dataSnapshot.getChildren()){

                    if(!data1.getKey().equals(CURRENT_USER)){
                    userKeys.add(data1.getKey().toString());
                    key = data1.getKey().toString();
                    }

                    if(!data1.child("Details").child("Name").getValue().equals(USER_NAME)) {
                        if (data1.child("Details").child("Name").getKey().equals("Name")) {
                            list.add(data1.child("Details").child("Name").getValue().toString());
                            val = data1.child("Details").child("Name").getValue().toString();
                        }

                        if (data1.child("Details").child("pic").getKey().equals("pic")) {
                            String base64Image = (String) data1.child("Details").child("pic").getValue();
                            byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                            Bitmap image = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                            piclist.add(image);
                            pic=image;
                        }
                    }
                    if(val!=null && pic !=null && key!=null){
                    map.put(key,val);
                    pics.put(key,pic);
                    }


               }
                //String value = (new ArrayList<String>(map.values().hashCode()).get(1));
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Intent intent = new Intent(getApplicationContext(),UserChat.class);
                //intent.putExtra("ChatUser", userKeys.get(i));
                intent.putExtra("ChatUser", new ArrayList<String>(map.keySet()).get(i));
                intent.putExtra("UserName", new ArrayList<String>(map.values()).get(i));
                intent.putExtra("CurrentUser", CURRENT_USER);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.signout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }else if(id==R.id.settings){

            Intent intent = new Intent(this,ProfileActivity.class);
            intent.putExtra("UID",CURRENT_USER);
            startActivity(intent);

        } else  if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
