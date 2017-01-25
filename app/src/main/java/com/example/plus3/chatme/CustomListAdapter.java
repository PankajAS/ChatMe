package com.example.plus3.chatme;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final  ArrayList<String> itemname;
    private final  List<Integer> imgid;
    public CustomListAdapter(Activity context, ArrayList<String> itemname, List<Integer> imgid) {
        super(context, R.layout.userlist,itemname);
        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.userlist, null,true);

       // TextView txtTitle = (TextView) rowView.findViewById();
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        //txtTitle.setText(itemname[position]);
        //imageView.setImageResource(imgid[position]);
       // extratxt.setText("Description "+itemname[position]);
        return rowView;

    };
}
