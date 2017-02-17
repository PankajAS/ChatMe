package com.example.plus3.chatme;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private Map<String, String> itemname;
    private Map<String, String> lastmsg;
    private Map<String, Bitmap> pics;
    private final ArrayList<Bitmap> imgid;
    private ArrayList<String> names;
    private String CurrentUser;

    public CustomListAdapter(Context context, String CurrentUser, Map<String,String> lastmsg, Map<String,String> map, Map<String,Bitmap> pics, ArrayList<Bitmap> imgid, ArrayList<String> list) {
        super(context, R.layout.userlist, list);
        this.context = context;
        this.itemname = map;
        this.imgid = imgid;
        this.names = list;
        this.pics = pics;
        this.lastmsg = lastmsg;
        this.CurrentUser = CurrentUser;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView= inflater.inflate(R.layout.userlist, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
        TextView lastmsgg = (TextView) rowView.findViewById(R.id.lastmsg);
        TextView time = (TextView) rowView.findViewById(R.id.time);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
        txtTitle.setText(new ArrayList<String>(itemname.values()).get(position));
        lastmsgg.setText(new ArrayList<String>(itemname.values()).get(position));
        time.setText(new ArrayList<String>(itemname.values()).get(position));
        imageView.setImageBitmap(new ArrayList<Bitmap>(pics.values()).get(position));

        try {
            String ks = new ArrayList<String>(itemname.keySet()).get(position);
            txtTitle.setText(new ArrayList<String>(itemname.values()).get(position));
            imageView.setImageBitmap(new ArrayList<Bitmap>(pics.values()).get(position));

            for(String msgkey:lastmsg.keySet()){
                if(msgkey.equals(ks)){
                        lastmsgg.setText(new ArrayList<String>(lastmsg.values()).get(position));
                }
            }
            //for(String timeKey:)
            time.setText("15/02/2017");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }


    private class ViewHolder {
        TextView txtTitle, lastmsg, time;
        ImageView imageView;
    }
}
