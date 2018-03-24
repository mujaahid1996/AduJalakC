package com.adujalakc;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.adujalakc.app.AppController;
import com.adujalakc.model.Complaint;
import com.adujalakc.util.Constant;
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

public class AccComplaintActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Session session;

    private List<Complaint> complaintsList = new ArrayList<Complaint>();
    private ComplaintAdapter adapter;
    ListView listView;
    EditText search;
    String[] data = new String[7];
    private ProgressDialog progressDialog;
    private static final String TAG = AccComplaintActivity.class.getSimpleName();

    //id user yang login
    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_complaint);

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
        id_user = String.valueOf(session.getId());
        //set navigation drawe header text
        View view = navigationView.getHeaderView(0);
        TextView tv_fullname = (TextView) view.findViewById(R.id.tv_fullname);
        tv_fullname.setText(session.getUsername());


        listView = (ListView) findViewById(R.id.listview);
        search = (EditText) findViewById(R.id.search);
        adapter = new ComplaintAdapter(this, complaintsList);
        listView.setAdapter(adapter);
        showacc();
    }

    private void showacc() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.GET_ACC_COM + id_user, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideprogressDialog();

                        try {
                            JSONArray list = response.getJSONArray("result");
                            for (int i = 0; i < list.length(); i++) {
                                Complaint complaint = new Complaint();
                                JSONObject jo = list.getJSONObject(i);
                                complaint.setName(jo.getString("name"));
                                complaint.setLocation(jo.getString("location"));
                                complaint.setComp_date(jo.getString("complaint_date"));
                                complaint.setProcess_date(jo.getString("date_process"));
                                complaint.setImg(Constant.URL_IMG_COMPLAINT + jo.getString("photo_name"));
                                complaint.setDesc(jo.getString("descript"));
                                complaint.setComp_id(jo.getString("id"));
                                complaintsList.add(complaint);
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
                hideprogressDialog();
            }
        }
        );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Complaint c = complaintsList.get(position);
                data[0] = c.getImg();
                data[1] = c.getName();
                data[2] = c.getLocation();
                data[3] = c.getDesc();
                data[4] = c.getComp_date();
                data[5] = c.getProcess_date();
                data[6] = c.getComp_id();
                Intent intent = new Intent(AccComplaintActivity.this, DetailAccComplaintActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hideprogressDialog();
    }

    private void hideprogressDialog() {
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
            startActivity(new Intent(AccComplaintActivity.this, ShowComplaintActivity.class));
            finish();
        }
        if (id == R.id.nav_acc_complaint) {
            startActivity(new Intent(AccComplaintActivity.this, AccComplaintActivity.class));
            finish();
        } else if (id == R.id.nav_complaint_over) {
            startActivity(new Intent(AccComplaintActivity.this, ComplaintOverActivity.class));
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
        Intent intent = new Intent(AccComplaintActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void searchAccComplaint(View view) {

        complaintsList.clear();
        final String lokasi = search.getText().toString();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.SEARCH_ACC_COM + id_user + "&cLocation=" + lokasi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideprogressDialog();

                        try {
                            JSONArray list = response.getJSONArray("result");
                            for (int i = 0; i < list.length(); i++) {
                                Complaint complaint = new Complaint();
                                JSONObject jo = list.getJSONObject(i);
                                complaint.setName(jo.getString("name"));
                                complaint.setLocation(jo.getString("location"));
                                complaint.setComp_date(jo.getString("complaint_date"));
                                complaint.setProcess_date(jo.getString("date_process"));
                                complaint.setImg(Constant.URL_IMG_COMPLAINT + jo.getString("photo_name"));
                                complaint.setDesc(jo.getString("descript"));
                                complaint.setComp_id(jo.getString("id"));
                                complaintsList.add(complaint);
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
                hideprogressDialog();
            }
        }
        );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }


}
