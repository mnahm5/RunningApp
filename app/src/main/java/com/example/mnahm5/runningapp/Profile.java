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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final EditText etFullName = (EditText) findViewById(R.id.etFullName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etWeight = (EditText) findViewById(R.id.etWeight);
        final Button btEdit = (Button) findViewById(R.id.btEdit);
        final Button btRePassword = (Button) findViewById(R.id.btRePassword);

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        String fullName = intent.getStringExtra("fullName");
        String email = intent.getStringExtra("email");
        String weight = intent.getStringExtra("weight");

        etFullName.setText(fullName);
        etFullName.setEnabled(false);
        etEmail.setText(email);
        etEmail.setEnabled(false);
        etWeight.setText(weight);
        etWeight.setEnabled(false);

        btRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Profile.this,ResetPassword.class);
                intent1.putExtra("username",username);
                Profile.this.startActivity(intent1);
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag) {
                    String buttonText = "Save Changes";
                    btEdit.setText(buttonText);
                    etFullName.setEnabled(true);
                    etEmail.setEnabled(true);
                    etWeight.setEnabled(true);
                    flag = true;
                }
                else {
                    final String newFullName = etFullName.getText().toString();
                    final String newEmail = etEmail.getText().toString();
                    final int newWeight = Integer.parseInt(etWeight.getText().toString());
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Intent intent = new Intent(Profile.this, Home.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("fullName", newFullName);
                                    intent.putExtra("email", newEmail);
                                    intent.putExtra("weight", newWeight+"");
                                    Profile.this.startActivity(intent);
                                    finish();
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                                    builder.setMessage("Update Failed")
                                            .setNegativeButton("Retry",null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(username, newFullName, newEmail, newWeight, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Profile.this);
                    queue.add(updateProfileRequest);
                }
            }
        });
    }
}
