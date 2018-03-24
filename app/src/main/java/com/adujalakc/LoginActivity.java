package com.adujalakc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adujalakc.util.Constant;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String username, password;
    EditText et_username, et_password;
    Button btn_login, btn_register;

    private Session session;

    //id akun di set ke session
    private String id_user, fullname_account;

    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        //pass context dari LoginActivity
        session = new Session(this);

        /**
         * CEK SESSION
         *seleksi status key "loggedInmode"  saat running LoginActivity
         * jika true, maka intent ke class lain
         */
        if (session.loggedin()) {
            startActivity(new Intent(LoginActivity.this, ShowComplaintActivity.class));
            finish();
        }

    }

    //event setiap button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                get_user();
                //aktivitas login
                login();
                break;
        }
    }

    private void get_user(){
        username = et_username.getText().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constant.GET_USER + username,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hasil");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            id_user = jsonObject.getString("user_id");
                            fullname_account = jsonObject.getString("user_full_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void login() {

        progressDialog = ProgressDialog.show(LoginActivity.this, "Logging In...",
                "Please wait...", false, true);

        //ambil nilai textfield
        username = et_username.getText().toString();
        password = et_password.getText().toString();
        //string request volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //seleksi respon sesuai api login
                        if (response.trim().equals("success")) {
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                            /**
                             * SET SESSION
                             *  eksekusi method berparameter
                             * ubah status key "loggedInmode" menjadi true
                             */
                            session.setLoggedIn(true, fullname_account, id_user) ;
                            //intent ke class lain
                            startActivity(new Intent(LoginActivity.this, ShowComplaintActivity.class));
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                //parameter api login
                map.put("user_username", username);
                map.put("user_password", password);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
