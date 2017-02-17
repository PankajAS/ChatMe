package com.example.plus3.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.plus3.chatme.fragmentTabs.Pager;
import com.google.firebase.auth.FirebaseAuth;

public class TabViewActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    String CURRENT_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);
        String[] tabBarTitles = new String[]{
                getString(R.string.Calls),
                getString(R.string.Chats),
                getString(R.string.Contacts)
        };
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Calls"));
        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.pager);
        Pager adapter = new Pager(getSupportFragmentManager(),tabLayout.getTabCount(),tabBarTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
        Intent in = getIntent();
        CURRENT_USER = in.getStringExtra("UID");


    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.signout){
            FirebaseAuth.getInstance().signOut();
            //finish();
            startActivity(new Intent(this,MainActivity.class));
        }else if(id==R.id.settings){

            Intent intent = new Intent(this,ProfileActivity.class);
            intent.putExtra("UID",CURRENT_USER);
            startActivity(intent);

        } else  if (id == android.R.id.home){
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
