package com.example.plus3.chatme;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final  ArrayList<String> itemname;
    private final ArrayList<Bitmap> imgid;

    public CustomListAdapter(Activity context, ArrayList<String> itemname, ArrayList<Bitmap> imgid) {
        super(context, R.layout.userlist,itemname);
        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.gc();
        CustomListAdapter.ViewHolder holder = null;
        if(convertView == null) {
            holder = new CustomListAdapter.ViewHolder();
            LayoutInflater inflater=context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.userlist, null ,true);

            holder.txtTitle = (TextView) convertView.findViewById(R.id.textView1);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);

            try {
                holder.txtTitle.setText(itemname.get(position));
                holder.imageView.setImageBitmap(imgid.get(position));

            } catch (Exception e) {
                e.printStackTrace();
            }

            convertView.setTag(holder);
        } else {
            holder = (CustomListAdapter.ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txtTitle;
        ImageView imageView;
    }
}
