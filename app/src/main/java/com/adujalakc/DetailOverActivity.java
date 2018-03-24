package com.adujalakc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adujalakc.app.AppController;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class DetailOverActivity extends AppCompatActivity {

    TextView name, loc, desc, process_date, complaint_date, finish_date;
    NetworkImageView photo_confirm, photo_complaint;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_over);

        //toolbar back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar back arrow
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        photo_complaint = (NetworkImageView) findViewById(R.id.img_before);
        photo_confirm = (NetworkImageView) findViewById(R.id.img_after);
        name = (TextView) findViewById(R.id.name);
        loc = (TextView) findViewById(R.id.location);
        desc = (TextView) findViewById(R.id.desc);
        complaint_date = (TextView) findViewById(R.id.comp_date);
        process_date = (TextView) findViewById(R.id.process_date);
        finish_date = (TextView) findViewById(R.id.finish_date);

        Bundle bundle = getIntent().getExtras();
        String[] data = bundle.getStringArray("data");

        photo_complaint.setImageUrl(data[0], imageLoader);
        name.setText(data[1]);
        loc.setText(data[2]);
        desc.setText(data[3]);
        complaint_date.setText(data[4]);
        process_date.setText(data[5]);
        photo_confirm.setImageUrl(data[6], imageLoader);
        finish_date.setText(data[7]);

    }



}
