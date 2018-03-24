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

public class DetailComplaintActivity extends AppCompatActivity {
    TextView name, loc, desc, date;
    NetworkImageView img;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ProgressDialog progressDialog;

    private String complaint_id;

    //id_user user yang login
    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_complaint);

        //session
        final Session session = new Session(this);
        id_user = String.valueOf(session.getId());
        //toolbar back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar back arrow
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        img = (NetworkImageView) findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        loc = (TextView) findViewById(R.id.location);
        desc = (TextView) findViewById(R.id.desc);
        date = (TextView) findViewById(R.id.comp_date);

        Bundle bundle = getIntent().getExtras();
        String[] data = bundle.getStringArray("data");

        img.setImageUrl(data[0], imageLoader);
        name.setText(data[1]);
        loc.setText(data[2]);
        desc.setText(data[3]);
        date.setText(data[4]);
        complaint_id = data[5];

    }


    public void accept(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ACC_COMP,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Toast.makeText(DetailComplaintActivity.this, response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DetailComplaintActivity.this, ShowComplaintActivity.class);
                        startActivity(intent);
                        hideprogressDialog();
                        finish();
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailComplaintActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideprogressDialog();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("complaint_id", complaint_id);
                params.put("id_user", id_user);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void hideprogressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
