package com.plusOneSoftwares.plusOneSoftwares.chatme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class UserList extends Fragment {

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
    CustomListAdapter adapter;
    int count=0;
    long time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_list);
        //setTitle(R.string.app_name);
        View v = inflater.inflate(R.layout.activity_user_list,container,false);
        listView = (ListView) v.findViewById(R.id.userList);
        listData = listData.getInstance();
        databaseReference = listData.getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        //final Intent intent = getIntent();
        Bundle b= getActivity().getIntent().getExtras();
        CURRENT_USER = b.getString("UID");
        getCurrentUserName();
        map = new HashMap<String,String>();
        pics = new HashMap<String,Bitmap>();
        final Map<String,String> lastmessages = new HashMap<>();
        final Map<String,String> time = new HashMap<>();
        adapter  = new CustomListAdapter(getContext(),CURRENT_USER,lastmessages,map,pics,piclist,list);
        listView.setAdapter(adapter);
        userKeys  = new ArrayList<>();
        final SharedPreferences pref1 = getContext().getSharedPreferences("com.plusOneSoftwares.plusOneSoftwares.chatme", Context.MODE_PRIVATE);
        //System.out.println(pref1.getAll().keySet());
       //last  [201711415568, 2017122175942, 2017122184818]

       databaseReference.child(CURRENT_USER).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Chat chat = null;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (DataSnapshot data2 : data.getChildren()) {
                        for (DataSnapshot data3 : data2.getChildren()) {
                            chat = data3.getValue(Chat.class);

                        }
                        lastmessages.put(data.getKey(), chat.getBody());
                        time.put(data.getKey(), chat.getTime());
                    }
                }
                /*Iterator it = time.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    //System.out.println(pair.getKey() + " = " + pair.getValue());
                    Iterator it1 = pref1.getAll().entrySet().iterator();
                    while (it1.hasNext()) {
                        Map.Entry pair1 = (Map.Entry) it1.next();
                        String value =  pair.getValue().toString();
                        String value1 = pair1.getValue().toString();
                            if (Long.parseLong(value) > Long.parseLong(value1)) {
                                System.out.println(pair1.getKey());
                            }

                        it1.remove();
                    }
                    it.remove();

                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Intent intent = new Intent(getContext(),UserChat.class);
                //intent.putExtra("ChatUser", userKeys.get(i));
                intent.putExtra("ChatUser", new ArrayList<String>(map.keySet()).get(i));
                intent.putExtra("UserName", new ArrayList<String>(map.values()).get(i));
                intent.putExtra("CurrentUser", CURRENT_USER);
                startActivity(intent);

            }
        });


        AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = null;
                        String val = null;
                        Bitmap pic = null;
                        try {
                            for (DataSnapshot data1 : dataSnapshot.getChildren()) {

                                if (!data1.getKey().equals(CURRENT_USER)) {
                                    userKeys.add(data1.getKey().toString());
                                    key = data1.getKey().toString();
                                }

                                if (!data1.child("Details").child("Name").getValue().equals(USER_NAME)) {

                                    if (data1.child("Details").child("Name").getKey().equals("Name")) {
                                        list.add(data1.child("Details").child("Name").getValue().toString());
                                        val = data1.child("Details").child("Name").getValue().toString();
                                    }

                                    if (data1.child("Details").child("pic").getKey().equals("pic")) {
                                        String base64Image = (String) data1.child("Details").child("pic").getValue();
                                        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                                        Bitmap image = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                                        piclist.add(image);
                                        pic = image;
                                    }
                                }

                                if (val != null && pic != null && key != null) {
                                    map.put(key, val);
                                    pics.put(key, pic);

                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex.getMessage());
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //String value = (new ArrayList<String>(map.values().hashCode()).get(1));
                        adapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return null;
            }
        };
        task.execute();

        return v;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }

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
}
