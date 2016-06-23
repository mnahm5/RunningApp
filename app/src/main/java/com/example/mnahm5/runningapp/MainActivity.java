package com.example.mnahm5.runningapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btRegisterPage = (Button) findViewById(R.id.btRegisterPage);
        final Button btLoginPage = (Button) findViewById(R.id.btLoginPage);
        final Button btTutorialPage = (Button) findViewById(R.id.btTutorialPage);

        btRegisterPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, Register.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

        btLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(loginIntent);
            }
        });

    }
}
