package com.example.sandraomari.registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText AdmissionNumber, Password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SecondActivity.class));//replace secondactivity with whatever comes after logging in
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        AdmissionNumber = (EditText) findViewById(R.id.AdmissionNumber);
        Password = (EditText) findViewById(R.id.Password);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        findViewById(R.id.Register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void userLogin() {
        final String admissionNo = AdmissionNumber.getText().toString();
        final String password = Password.getText().toString();

        if (TextUtils.isEmpty(admissionNo)) {
            AdmissionNumber.setError("Please enter your admission number");
            AdmissionNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Password.setError("Please enter your password");
            Password.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("user");

                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getInt("admissionNo"),
                                userJson.getString("email")
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        finish();
                        startActivity(new Intent(getApplicationContext(), SecondActivity.class));//replace secondactivity
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("admissionNo", admissionNo);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
