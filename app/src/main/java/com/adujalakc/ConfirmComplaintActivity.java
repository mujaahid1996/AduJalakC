package com.adujalakc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adujalakc.util.Constant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ConfirmComplaintActivity extends AppCompatActivity {

    private String complaint_id;


    //upload photo
    private File pictureFile;
    private static int code = 0;
    private ImageView pictIv;//imageView untuk set gambar hasil camera
    private TextView lokasiTv;//tampilkan lokasi
    private Uri uriPhoto;//sumber gambar
    private EditText deskripsiEt;//teks deskripsi
    private Button takePictBtn;//button ambil gambar
    private Button uploadBtn;//button upload

    private String photoTaken_val = null; // for cDate


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_complaint);

        MultiDex.install(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        complaint_id = bundle.getString("KEY_COMPLAINT_ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar back arrow
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pictIv = (ImageView) findViewById(R.id.pictIv);
        lokasiTv = (TextView) findViewById(R.id.lokasiTv);
        takePictBtn = (Button) findViewById(R.id.takePictBtn);
        deskripsiEt = (EditText) findViewById(R.id.deskripsiEt);
        uploadBtn = (Button) findViewById(R.id.uploadBtn);

        uploadBtn.setEnabled(false); //default

        klik_ambil_gambar(); //ambil photo

        klik_upload_gambar(); //upload photo

    }


    private void klik_ambil_gambar() {
        takePictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //1. ambil gambar
                    //format photo hasil camera
                    String formatFile = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    pictureFile = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ) , "pict_" + formatFile + ".jpg"  );
                    uriPhoto = Uri.fromFile(pictureFile);
                    //intent setelah ambil photo
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto );
                    startActivityForResult(intent, code);
                    //aktif button upload
                    uploadBtn.setEnabled(true);

            }
        });
    }

    //1. hasil ambil gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == code ) {

            switch (resultCode){

                case Activity.RESULT_OK:
                    //cek bahwa file tersimpan
                    if( pictureFile.exists() ){
                        Toast.makeText(getApplicationContext() , "berhasil tersimpan di: \n" + pictureFile.getAbsolutePath() , Toast.LENGTH_SHORT).show();
                        try {
                            //set photo taken to imageview
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhoto);
                            pictIv.setImageBitmap(bitmap);

                        }catch (Exception e){

                        }

                    }else{ Toast.makeText(getApplicationContext(), "gagal disimpan", Toast.LENGTH_SHORT).show(); }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }

    }


    private void klik_upload_gambar(){
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //3. upload gambar
                String deskripsi = deskripsiEt.getText().toString().trim(); //getting name for the image
                String path = uriPhoto.getPath(); //getting the actual path of the image
                //get photo taken date
                File file = new File(path);
                if (file.exists()) {
                    long date = file.lastModified();
                    Date fileData = new Date(date);
                    photoTaken_val = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fileData);
                    Toast.makeText(ConfirmComplaintActivity.this, "" + photoTaken_val, Toast.LENGTH_SHORT).show();
                }

                //Uploading code
                try {
                    String uploadId = UUID.randomUUID().toString();
                    // Creating a multi part request
                    new MultipartUploadRequest(ConfirmComplaintActivity.this, uploadId, Constant.UPLOAD_URL)
                            .addFileToUpload(path, Constant.PARAM_UPLOAD_FILE) //Adding file
                            .addParameter(Constant.PARAM_COMPLAINT_ID, complaint_id) //Adding from complaint ID
                            .addParameter(Constant.PARAM_COMPLAINT_DATE, photoTaken_val) //Adding formatted date
                            .addParameter(Constant.PARAM_UPLOAD_DESCRIPTION, "confirm photo") //Adding text parameter to the request
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                    Toast.makeText(ConfirmComplaintActivity.this, "image was uploaded", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ConfirmComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
