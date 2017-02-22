package com.example.plus3.chatme.fragmentTabs;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.plus3.chatme.R;
import com.example.plus3.chatme.UserChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tab2 extends Fragment {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference dRef;
    private ListView listView;
    private List<String> list;
    ArrayList<HashMap<String,String>> contactData=new ArrayList<HashMap<String,String>>();
    //ArrayAdapter adapter;
    HashMap<String,String> map;
    List<String> listArray, listNumber;
    ContactsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_tab2,container,false);

        Bundle b= getActivity().getIntent().getExtras();
        listView = (ListView) v.findViewById(R.id.list);
        listView.setDivider(null);
        listArray = new ArrayList<>();
        listNumber = new ArrayList<>();
        map = new HashMap<String, String>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = database.getInstance();
        dRef = database.getReference("Users");

        AsyncTask<Void,String,String> task = new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                Cursor cursor = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
                while(cursor.moveToNext()){
                    try {
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                map.put(phoneNumber, name);
                                //contactData.add(map);
                            }
                            phones.close();
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                adapter = new ContactsListAdapter(getContext(),R.layout.userlist,map);
                listView.setAdapter(adapter);
                super.onPostExecute(s);
            }
        };
        task.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                callData(i);
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void callData(final int i){
        final Intent intent = new Intent(getContext(),UserChat.class);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("Details").child("Phone").getValue().equals(new ArrayList<String>(map.keySet()).get(i))){
                        if(!data.child("Details").child("UID").getValue().equals(user.getUid())){
                            System.out.println(data.child("Details").child("UID").getValue().toString());

                            intent.putExtra("ChatUser", data.child("Details").child("UID").getValue().toString());
                            intent.putExtra("UserName", data.child("Details").child("Name").getValue().toString());
                            intent.putExtra("CurrentUser", user.getUid());
                            startActivity(intent);
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
    public void onDestroy() {

        super.onDestroy();
    }
}
