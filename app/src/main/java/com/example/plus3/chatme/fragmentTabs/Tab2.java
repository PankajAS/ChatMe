package com.example.plus3.chatme.fragmentTabs;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.plus3.chatme.R;
import com.example.plus3.chatme.UserChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    ArrayAdapter adapter;
    HashMap<String,String> map;
    List<String> listArray;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_tab2,container,false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dRef = database.getReference("Users");
        Bundle b= getActivity().getIntent().getExtras();
        listView = (ListView) v.findViewById(R.id.list);
        listView.setDivider(null);
        listArray = new ArrayList<>();
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
//                      map = new HashMap<String, String>();
//                      map.put("name", name);
//                      map.put("number", phoneNumber);
                      contactData.add(map);
                      listArray.add(name);
                  }
                  phones.close();
              }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        adapter = new ArrayAdapter(getContext(),R.layout.userlist, R.id.textView1,listArray);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Intent intent = new Intent(getContext(),UserChat.class);
                //intent.putExtra("ChatUser", userKeys.get(i));
                intent.putExtra("ChatUser", "");
                intent.putExtra("UserName", listArray.get(i));
                intent.putExtra("CurrentUser", listArray.get(i));
                startActivity(intent);
            }
        });
        return v;
    }


}
