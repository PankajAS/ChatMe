package com.example.plus3.chatme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plus 3 on 28-01-2017.
 */

public class ChatArrayAdapter extends ArrayAdapter<UserChat.ChatMessage> {
    private TextView chatText;
    private List<UserChat.ChatMessage> chatMessageList = new ArrayList<UserChat.ChatMessage>();
    private Context context;



    public ChatArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;


    }


    @Override
    public void add(UserChat.ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }
    public int getCount() {
        return this.chatMessageList.size();
    }

    public UserChat.ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserChat.ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (chatMessageObj.left) {

                row = inflater.inflate(R.layout.right, parent, false);


            } else {

                row = inflater.inflate(R.layout.left, parent, false);


            }

            chatText = (TextView) row.findViewById(R.id.msgr);
            for (String item : chatMessageObj.message)
            {
                chatText.setText(item);
            }

        return row;
    }
}