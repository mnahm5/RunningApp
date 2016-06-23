package com.example.mnahm5.runningapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        final Button btRecordLink = (Button) findViewById(R.id.btRecordLink);
        final Button btHistoryLink = (Button) findViewById(R.id.btHistoryLink);
        final Button btProfileLink = (Button) findViewById(R.id.btProfileLink);

        final Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        final String fullName = intent.getStringExtra("fullName");
        final String email = intent.getStringExtra("email");
        final String weight = intent.getStringExtra("weight");
        String welcomeMessage = "Welcome " + fullName;
        tvWelcome.setText(welcomeMessage);

        btRecordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Record.class);
                Home.this.startActivity(intent);
            }
        });

        btProfileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Home.this,Profile.class);
                intent1.putExtra("username", username);
                intent1.putExtra("fullName", fullName);
                intent1.putExtra("email", email);
                intent1.putExtra("weight", weight);
                Home.this.startActivity(intent1);
            }
        });
    }
}
