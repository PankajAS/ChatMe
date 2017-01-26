package com.example.plus3.chatme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

//imports to open media

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    DatabaseReference savedata;

    ImageView viewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Profile");
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        myRef = database.getReference("Users");
        final Intent intent = getIntent();
        savedata = myRef.child(intent.getStringExtra("UID")).child("Details");
        final TextView textView = (TextView)findViewById(R.id.profileName);

        viewImage = (ImageView)findViewById(R.id.imageView);
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        myRef.child(intent.getStringExtra("UID")).child("Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.getKey().equals("Name")) {
                        textView.setText(data.getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    viewImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
                    Bitmap bit = BitmapFactory.decodeFile(file.getPath(), options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    savedata.child("Photo").setValue(base64Image);


                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2 && data != null) {

                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    viewImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String[] filePath = { MediaStore.Images.Media.DATA };
                //Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                //Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
               // cursor.moveToFirst();
                //int columnIndex = cursor.getColumnIndex(filePath[0]);
                //String picturePath = cursor.getString(columnIndex);
               // cursor.close();
               // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath+"");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8; //shrink it down otherwise we will use stupid amounts of memory
                Bitmap bit = BitmapFactory.decodeFile(selectedImage.getPath(), options);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytes = baos.toByteArray();
                String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                savedata.child("Photo").setValue(base64Image);
              //  viewImage.setImageBitmap(thumbnail);

            }
        }
    }
}
