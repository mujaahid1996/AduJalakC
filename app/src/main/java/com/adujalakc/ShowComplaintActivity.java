package com.adujalakc;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adujalakc.adapter.ComplaintAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.adujalakc.app.AppController;
import com.adujalakc.model.Complaint;
import com.adujalakc.util.Constant;

public class ShowComplaintActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Session session;
    private String id_account;

    private static final String TAG = ShowComplaintActivity.class.getSimpleName();

    private List<Complaint> complaintList = new ArrayList<Complaint>();
    private ComplaintAdapter adapter;
    ListView listView;
    EditText search;
    ProgressDialog progressDialog;
    String[] data = new String[6];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complaint);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //session
        session = new Session(this);
        if (!session.loggedin()) {
            logout();
        }
        id_account = String.valueOf(session.getId());
        //set navigation drawe header text
        View view = navigationView.getHeaderView(0);
        TextView tv_fullname = (TextView) view.findViewById(R.id.tv_fullname);
        tv_fullname.setText(session.getUsername());


        listView = (ListView) findViewById(R.id.listComplaint);
        search = (EditText) findViewById(R.id.txtsearch);
        adapter = new ComplaintAdapter(this, complaintList);
        listView.setAdapter(adapter);

        showData();
        selectData();
    }

    private void showData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.GET_LIST_COM, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        try {
                            JSONArray list = response.getJSONArray("result");
                            for (int i = 0; i < list.length(); i++) {
                                Complaint complaint = new Complaint();
                                JSONObject jo = list.getJSONObject(i);
                                complaint.setName(jo.getString("name"));
                                complaint.setLocation(jo.getString("location"));
                                complaint.setComp_date(jo.getString("date"));
                                complaint.setImg(Constant.URL_IMG_COMPLAINT + jo.getString("photo_name"));
                                complaint.setDesc(jo.getString("descript"));
                                complaint.setComp_id(jo.getString("id"));
                                complaintList.add(complaint);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        }
        );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private void selectData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Complaint c = complaintList.get(position);
                data[0] = c.getImg();
                data[1] = c.getName();
                data[2] = c.getLocation();
                data[3] = c.getDesc();
                data[4] = c.getComp_date();
                data[5] = c.getComp_id();
                Intent intent = new Intent(ShowComplaintActivity.this, DetailComplaintActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pending_complaint) {
            startActivity(new Intent(ShowComplaintActivity.this, ShowComplaintActivity.class));
            finish();
        }
        if (id == R.id.nav_acc_complaint) {
            startActivity(new Intent(ShowComplaintActivity.this, AccComplaintActivity.class));
            finish();
        } else if (id == R.id.nav_complaint_over) {
            startActivity(new Intent(ShowComplaintActivity.this, ComplaintOverActivity.class));
            finish();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    private void logout() {
        session.setLoggedIn(false, "DEFAULT", "NULL");
        Intent intent = new Intent(ShowComplaintActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void searchPendingComplaint(View view) {
        complaintList.clear();
        final String lokasi = ((EditText) findViewById(R.id.txtsearch)).getText().toString();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.SEARCH_LIST_COM + lokasi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        try {
                            JSONArray list = response.getJSONArray("result");
                            for (int i = 0; i < list.length(); i++) {
                                Complaint complaint = new Complaint();
                                JSONObject jo = list.getJSONObject(i);
                                complaint.setName(jo.getString("name"));
                                complaint.setLocation(jo.getString("location"));
                                complaint.setComp_date(jo.getString("date"));
                                complaint.setImg(Constant.URL_IMG_COMPLAINT + jo.getString("photo_name"));
                                complaint.setDesc(jo.getString("descript"));
                                complaint.setComp_id(jo.getString("id"));
                                complaintList.add(complaint);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        }
        );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}