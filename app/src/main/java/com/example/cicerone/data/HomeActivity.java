package com.example.cicerone.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cicerone.R;

public class HomeActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private TextView info;
    private TextView info2;
    private Button logoutButton;
    private Button cerca;
    private Button crea;
    private Button modifica;
    private Button richieste;
    private Button storico;
    private Button inoltrate;
    private String nome;
    private String cognome;
    private String datanascita;
    private String email;
    private String CF;
    private Integer id;
    private static final String CHIAMANTE = "chiamante";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    logoutButton.setVisibility(View.INVISIBLE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    cerca.setVisibility(View.INVISIBLE);
                    crea.setVisibility(View.INVISIBLE);
                    richieste.setVisibility(View.INVISIBLE);
                    modifica.setVisibility(View.INVISIBLE);
                    inoltrate.setVisibility(View.INVISIBLE);
                    storico.setVisibility(View.INVISIBLE);
                    info2.setVisibility(View.INVISIBLE);
                    mTextMessage.setText("Ciao "+nome+"!");
                    return true;
                case R.id.navigation_dashboard:
                    logoutButton.setVisibility(View.INVISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    cerca.setVisibility(View.VISIBLE);
                    crea.setVisibility(View.VISIBLE);
                    modifica.setVisibility(View.VISIBLE);
                    richieste.setVisibility(View.VISIBLE);
                    inoltrate.setVisibility(View.VISIBLE);
                    storico.setVisibility(View.VISIBLE);
                    info2.setVisibility(View.INVISIBLE);
                    mTextMessage.setText("Menu attività");
                    return true;
                case R.id.navigation_profilo:
                    info.setVisibility(View.VISIBLE);
                    info2.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.VISIBLE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    cerca.setVisibility(View.INVISIBLE);
                    crea.setVisibility(View.INVISIBLE);
                    modifica.setVisibility(View.INVISIBLE);
                    richieste.setVisibility(View.INVISIBLE);
                    inoltrate.setVisibility(View.INVISIBLE);
                    storico.setVisibility(View.INVISIBLE);
                    mTextMessage.setText("Riepilogo dati:");
                    info.setText(nome+"\n"+cognome+"\n"+datanascita+"\n"+email);
                    return true;
                default:
                    logoutButton.setVisibility(View.INVISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    info2.setVisibility(View.INVISIBLE);
                    cerca.setVisibility(View.VISIBLE);
                    crea.setVisibility(View.VISIBLE);
                    modifica.setVisibility(View.VISIBLE);
                    richieste.setVisibility(View.VISIBLE);
                    inoltrate.setVisibility(View.VISIBLE);
                    storico.setVisibility(View.VISIBLE);
                    mTextMessage.setText("Menu attività");
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nome = getIntent().getExtras().getString("nome");
        cognome = getIntent().getExtras().getString("cognome");
        datanascita = getIntent().getExtras().getString("datanascita");
        email = getIntent().getExtras().getString("email");
        id = getIntent().getExtras().getInt("id");
        CF = getIntent().getExtras().getString("CF");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        info = findViewById(R.id.info);
        info.setVisibility(View.INVISIBLE);
        info2 = findViewById(R.id.info2);
        info2.setVisibility(View.INVISIBLE);
        mTextMessage.setText("Ciao "+nome+"!");

        cerca = findViewById(R.id.cerca);
        modifica = findViewById(R.id.modifica);
        crea = findViewById(R.id.crea);
        richieste = findViewById(R.id.richieste);
        storico = findViewById(R.id.storicoattivita);
        inoltrate = findViewById(R.id.richiesteInoltrate);

        cerca.setVisibility(View.INVISIBLE);
        crea.setVisibility(View.INVISIBLE);
        modifica.setVisibility(View.INVISIBLE);
        richieste.setVisibility(View.INVISIBLE);
        inoltrate.setVisibility(View.INVISIBLE);
        storico.setVisibility(View.INVISIBLE);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        logoutButton = findViewById(R.id.logout);
        logoutButton.setVisibility(View.INVISIBLE);

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Utente(HomeActivity.this,"","","","","",0).logout();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
            }
        });

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(HomeActivity.this, FormRicerca.class);
                inte.putExtra("idUtente",id);
                inte.putExtra(CHIAMANTE,"cerca");
                startActivity(inte);
            }
        });

        crea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte;
                Utente u = new DBhelper(HomeActivity.this).getInfoUtentebyID(HomeActivity.this,id);

                if(CF==null)
                    inte = new Intent(HomeActivity.this, ToCicerone.class);
                else {
                    if(CF.equals("null")||CF.isEmpty())
                        inte = new Intent(HomeActivity.this, ToCicerone.class);
                    else
                        inte = new Intent(HomeActivity.this, Creazione.class);
                }
                inte.putExtra("idUtente",id);
                startActivity(inte);
            }
        });

        modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(HomeActivity.this, ElencoAttivita.class);
                inte.putExtra("idUtente",id);
                inte.putExtra(CHIAMANTE,"modifica");
                startActivity(inte);
            }
        });

        richieste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(HomeActivity.this, ElencoAttivita.class);
                inte.putExtra("idUtente",id);
                inte.putExtra(CHIAMANTE,"richieste");
                startActivity(inte);
            }
        });

        inoltrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(HomeActivity.this, ElencoAttivita.class);
                inte.putExtra("idUtente",id);
                inte.putExtra(CHIAMANTE,"inoltrate");
                startActivity(inte);
            }
        });

        storico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(HomeActivity.this, ElencoAttivita.class);
                inte.putExtra("idUtente",id);
                inte.putExtra(CHIAMANTE,"storico");
                startActivity(inte);
            }
        });

    }

    @Override
    public void onBackPressed() {
        logoutButton.setVisibility(View.INVISIBLE);
        mTextMessage.setVisibility(View.VISIBLE);
        info.setVisibility(View.INVISIBLE);
        cerca.setVisibility(View.INVISIBLE);
        crea.setVisibility(View.INVISIBLE);
        richieste.setVisibility(View.INVISIBLE);
        modifica.setVisibility(View.INVISIBLE);
        inoltrate.setVisibility(View.INVISIBLE);
        storico.setVisibility(View.INVISIBLE);
        mTextMessage.setText("Ciao "+nome+"!");
    }
}
