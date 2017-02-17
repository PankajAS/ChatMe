package com.example.plus3.chatme.fragmentTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.plus3.chatme.R;

import java.util.List;

public class Tab2 extends Fragment {
    private ListView listView;
    private List<String> list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_tab2,container,false);
        Bundle b= getActivity().getIntent().getExtras();
        //System.out.println(b.getString("UID"));
        return v;
    }
}
