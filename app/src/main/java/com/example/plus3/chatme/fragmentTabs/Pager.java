package com.example.plus3.chatme.fragmentTabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.plus3.chatme.UserList;

/**
 * Created by Plus 3 on 16-02-2017.
 */

public class Pager extends FragmentStatePagerAdapter {

    int tabCount;
    String[] tabBarTitles;

    public Pager(FragmentManager fm, int tabCount,String[] tabBarTitles) {
        super(fm);
        this.tabCount = tabCount;
        this.tabBarTitles = tabBarTitles;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                UserList tab2 = new UserList();
                return tab2;
            case 2:
                Tab2 tab22 = new Tab2();
                return tab22;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabBarTitles[position];
    }
}
