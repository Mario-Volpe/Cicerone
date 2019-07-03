package com.example.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Button logoutButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    logoutButton.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    logoutButton.setVisibility(View.INVISIBLE);
                    mTextMessage.setText(R.string.title_dashboard);
                    mTextMessage.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_profilo:
                    logoutButton.setVisibility(View.VISIBLE);
                    mTextMessage.setText("Profilo");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        logoutButton = (Button) findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Utente(HomeActivity.this,"","","","","").rimuoviUtente();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
            }
        });
    }

}
