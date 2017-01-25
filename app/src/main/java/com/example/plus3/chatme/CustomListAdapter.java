package com.example.plus3.chatme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;
    public CustomListAdapter(Context context, int resource, Activity context1, String[] itemname, Integer[] imgid) {
        super(context, resource);
        this.context = context1;
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
