package com.example.plus3.chatme;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.plus3.chatme.fragmentTabs.Pager;
import com.google.firebase.auth.FirebaseAuth;

public class TabViewActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private static final int REQUEST_WRITE_STORAGE = 1;
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
        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        viewPager = (ViewPager)findViewById(R.id.pager);
        Pager adapter = new Pager(getSupportFragmentManager(),tabLayout.getTabCount(),tabBarTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
        Intent in = getIntent();
        CURRENT_USER = in.getStringExtra("UID");

        requestContactReadPermission();

    }

    private void requestContactReadPermission() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(TabViewActivity.this,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(TabViewActivity.this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //selectImage();

                } else
                {
                    Toast.makeText(TabViewActivity.this, "The app was not allowed to read your contact info. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
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
