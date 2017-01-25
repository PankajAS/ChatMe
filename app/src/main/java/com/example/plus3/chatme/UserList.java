package com.example.plus3.chatme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase listData;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> list= new ArrayList<String>();
    List<Integer> piclist = new ArrayList<Integer>();
    ArrayAdapter<String> adapter;
    String CURRENT_USER;
    String USER_NAME;


    public  void getCurrentUserName(){
        databaseReference.child(CURRENT_USER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    for (DataSnapshot data2:data.getChildren()){
                        if(data2.getKey().equals("Name")){
                            //System.out.println(data2.getValue());
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
        setTitle("User List");
        listView = (ListView)findViewById(R.id.userList);
        listData = listData.getInstance();
        databaseReference = listData.getReference("Users");
        adapter = new ArrayAdapter<String>(this,R.layout.userlist,R.id.textView1,list);
        listView.setAdapter(adapter);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        final Intent intent = getIntent();
        CURRENT_USER = intent.getStringExtra("UID");
        getCurrentUserName();
        CustomListAdapter adapterr= new CustomListAdapter(this,list,piclist);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data1:dataSnapshot.getChildren()){

                    for(DataSnapshot dataSnapshot1:data1.getChildren()){
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()) {

                        if(dataSnapshot2.getKey().equals("Name") && !dataSnapshot2.getValue().equals(USER_NAME)){
                            list.add(dataSnapshot2.getValue().toString());
                        }
                       adapter.notifyDataSetChanged();
                   }
                 }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){

                            for (DataSnapshot data2:data.getChildren()){


                                for(DataSnapshot data3:data2.getChildren()){

                                    if(data3.getValue().equals(list.get(i))){
                                        Intent intent = new Intent(getApplicationContext(),UserChat.class);
                                        intent.putExtra("ChatUser",data.getKey().toString());
                                        intent.putExtra("CurrentUser",CURRENT_USER);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    //Class for image download
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = null;

                if(urlConnection instanceof HttpURLConnection){
                    httpURLConnection = (HttpURLConnection) urlConnection;
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();

                    Bitmap myImage = BitmapFactory.decodeStream(inputStream);
                    return myImage;
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.signout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }else if(item.getItemId()==R.id.settings){

            Intent intent = new Intent(this,ProfileActivity.class);
            intent.putExtra("UID",CURRENT_USER);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}
