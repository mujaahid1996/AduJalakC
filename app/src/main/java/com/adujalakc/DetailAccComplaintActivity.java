package com.adujalakc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adujalakc.app.AppController;
import com.adujalakc.util.Constant;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DetailAccComplaintActivity extends AppCompatActivity {

    TextView name, loc, desc, process_date, complaint_date;
    NetworkImageView img;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private String complaint_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_acc_complaint);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar back arrow
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        img = (NetworkImageView) findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        loc = (TextView) findViewById(R.id.location);
        desc = (TextView) findViewById(R.id.desc);
        complaint_date = (TextView) findViewById(R.id.comp_date);
        process_date = (TextView) findViewById(R.id.process_date);

        Bundle bundle = getIntent().getExtras();
        String[] data = bundle.getStringArray("data");

        img.setImageUrl(data[0], imageLoader);
        name.setText(data[1]);
        loc.setText(data[2]);
        desc.setText(data[3]);
        complaint_date.setText(data[4]);
        process_date.setText(data[5]);
        complaint_id = data[6];

    }

    public void confirm(View view){
        Intent intent = new Intent(DetailAccComplaintActivity.this, ConfirmComplaintActivity.class);
        intent.putExtra("KEY_COMPLAINT_ID", complaint_id);
        startActivity(intent);
    }

}
