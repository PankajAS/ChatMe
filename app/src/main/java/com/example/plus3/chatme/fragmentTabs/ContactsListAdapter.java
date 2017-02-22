package com.example.plus3.chatme.fragmentTabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plus3.chatme.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Plus 3 on 21-02-2017.
 */

public class ContactsListAdapter extends ArrayAdapter<String> {
    HashMap<String, String> map;
    private final Context context;


    public ContactsListAdapter(Context context, int resource, HashMap<String, String> map) {
        super(context, R.layout.userlist, new ArrayList<String>(map.keySet()));
        this.context =  context;
        this.map = map;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactsListAdapter.ViewHolder holder = null;

        if(convertView == null){
            holder = new ContactsListAdapter.ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.userlist, null ,true);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.textView1);
            holder.numbers = (TextView) convertView.findViewById(R.id.lastmsg);
            holder.from = (TextView) convertView.findViewById(R.id.time);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);

            try{
                holder.txtTitle.setText(new ArrayList<String>(map.values()).get(position));
                holder.numbers.setText(new ArrayList<String>(map.keySet()).get(position));
                holder.from.setText("Mobile");
            }catch (Exception e){
                e.printStackTrace();
            }
            convertView.setTag(holder);
        }else{
            holder = (ContactsListAdapter.ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    private class ViewHolder {
        TextView txtTitle, numbers, from;
        ImageView imageView;
    }
}
