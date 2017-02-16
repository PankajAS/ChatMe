package com.example.plus3.chatme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

//imports to open media

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    DatabaseReference savedata;
    private static final int REQUEST_WRITE_STORAGE = 1;
    Intent intent;
    ImageView viewImage;
    Button btnProfile;
    TextView profileName;
    TextView txtName;
    TextView txtEmail;
    TextView txtMobile;

    Utils utils;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        utils = new Utils();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Update Profile");
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        myRef = database.getReference("Users");
        intent = getIntent();
        savedata = myRef.child(intent.getStringExtra("UID")).child("Details");

        profileName = (TextView)findViewById(R.id.profileName);
        txtName = (TextView)findViewById(R.id.txtName);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtMobile = (TextView)findViewById(R.id.txtMobile);
        viewImage = (ImageView)findViewById(R.id.imageView);
        btnProfile = (Button) findViewById(R.id.btnProfile);

        populateProfileInfo();
        //onclick handeler of profile image
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        //onclick handeler of UPDATE profile button
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileInfo();
            }
        });

    }

    private void populateProfileInfo() {
        myRef.child(intent.getStringExtra("UID")).child("Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.getKey().equals("Name")) {
                        profileName.setText(data.getValue().toString());
                        txtName.setText(data.getValue().toString());
                    }
                    else if(data.getKey().equals("Email")) {
                        txtEmail.setText(data.getValue().toString());
                    }
                    else if(data.getKey().equals("Phone")) {
                        txtMobile.setText(data.getValue().toString());
                    }else if(data.getKey().equals("pic")) {
                        String base64Image = (String) data.getValue();
                        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                        Bitmap image = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                        viewImage.setImageBitmap(utils.getCircularImage(image));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void openGallery() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        else
        {
            selectImage();
        }
    }

    private void selectImage() {
        //reload my activity with permission granted or use the features what required the permission
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    selectImage();

                } else
                {
                    Toast.makeText(ProfileActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                viewImage.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void updateProfileInfo() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
        Bitmap bitmap = ((BitmapDrawable)viewImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        // we finally have our base64 string version of the image, save it.
        savedata.child("pic").setValue(base64Image);

        savedata.child("Name").setValue(txtName.getText().toString());
        savedata.child("Phone").setValue(txtMobile.getText().toString());
        savedata.child("Email").setValue(txtEmail.getText().toString());

        Toast.makeText(this, R.string.profileUpdateMsg, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            //overridePendingTransition(R.transition.stay, R.transition.slide_down);
        }
        return super.onOptionsItemSelected(item);
    }
}
