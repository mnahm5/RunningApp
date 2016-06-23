package com.example.mnahm5.runningapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        final EditText etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        final EditText etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        final EditText etNewPassword2 = (EditText) findViewById(R.id.etNewPassword2);
        final Button btReset = (Button) findViewById(R.id.btReset);
        final Button btCancel = (Button) findViewById(R.id.btCancel);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPassword.this, Home.class);
                ResetPassword.this.startActivity(intent);
                finish();
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldPassword = etOldPassword.getText().toString();
                final String newPassword = etNewPassword.getText().toString();
                final String newPassword2 = etNewPassword2.getText().toString();

                if (newPassword.equals(newPassword2)) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Intent intent = new Intent(ResetPassword.this, Home.class);
                                    ResetPassword.this.startActivity(intent);
                                    finish();
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
                                    builder.setMessage("Password Reset Failed")
                                            .setNegativeButton("Retry",null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(username, oldPassword, newPassword, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ResetPassword.this);
                    queue.add(resetPasswordRequest);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
                    builder.setMessage("Passwords do not match")
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        });
    }
}
