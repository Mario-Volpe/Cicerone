package com.example.cicerone;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.data.model.DBhelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener setData ;
    private static final String TAG = "RegistrationActivity";

    private int anno=0;
    private int mese=0;
    private int giorno=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Button inviaDatiUtente;
        TextView collegamentoLogin;
        final TextView mostraData;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        final EditText nomeUtente =  findViewById(R.id.nome);
        final EditText cognomeUtente = findViewById(R.id.cognome);
        final EditText passwordUtente = findViewById(R.id.password);
        final EditText passwordUtente2 = findViewById(R.id.password2);
        final EditText emailUtente = findViewById(R.id.email);
        mostraData= (TextView) findViewById(R.id.date);
        inviaDatiUtente = findViewById(R.id.bottoneInvia);
        collegamentoLogin  = findViewById(R.id.link_login);
        ImageView img = findViewById(R.id.imageView3);

       inviaDatiUtente.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String nomeUtenteStr = nomeUtente.getText().toString().trim();
               String cognomeUtenteStr = cognomeUtente.getText().toString().trim();
               String emailUtenteStr = emailUtente.getText().toString().trim();
               String passwordUtenteStr = passwordUtente.getText().toString().trim();
               String passwordUtende2Str = passwordUtente2.getText().toString().trim();
               String datanascita = mostraData.getText().toString().trim();
               DBhelper db = new DBhelper(RegistrationActivity.super.getBaseContext());

               Utente nuovoUtente = new Utente(passwordUtenteStr,cognomeUtenteStr,nomeUtenteStr,emailUtenteStr,datanascita);

               if (!checkData()) {
                   Toast.makeText(RegistrationActivity.this, "La data inserita non è corretta.", Toast.LENGTH_SHORT).show();
               }
               else {
                   if (!passwordUtenteStr.equals(passwordUtende2Str)) {
                   //Password e conferma non coincidono!
                   Toast.makeText(RegistrationActivity.this, "Le password non coincidono!", Toast.LENGTH_SHORT).show();
                    } else {
                        //la password deve essere lunga almeno 5 caratteri
                        if (passwordUtenteStr.length() < 5)
                            Toast.makeText(RegistrationActivity.this, "La password deve contenere almeno 5 caratteri!", Toast.LENGTH_SHORT).show();
                        else {
                            if (passwordUtenteStr.length() > 15)
                           Toast.makeText(RegistrationActivity.this, "La password può contenere massimo 15 caratteri!", Toast.LENGTH_SHORT).show();
                            else {
                           //Se le password coincidono, bisogna controllare che l'utente (email) è gia usato e nel caso scrivere l'utente nel db
                            if (db.isSignedUp(nuovoUtente)) {
                               //utente già iscritto, stampo un messaggio d'errore
                               Toast.makeText(RegistrationActivity.this, "Questa mail risulta essere già usata!", Toast.LENGTH_LONG).show();
                           } else {
                               //inserimento nel db
                               if (db.inserisciUtente(nuovoUtente) != -1) {
                                   Toast.makeText(RegistrationActivity.this, "Registrato!", Toast.LENGTH_SHORT).show();
                                   db.close();
                                   finish();
                               }
                           }
                       }

                   }
               }
             }
            }
        }
        );

       img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openActivityMain();
           }
       });

        collegamentoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLogin();
            }
        });

        mostraData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this, setData,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        setData = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                anno=year;
                mese=month;
                giorno=dayOfMonth;

                Log.d(TAG,"onDateSet: dd/mm/yyyy: "+ dayOfMonth+"/"+month+"/"+year);

                String date = dayOfMonth+"/"+month+"/"+year;
                mostraData.setText(date);
            }
        };

    }

    private boolean checkData(){
        boolean res = true;
        Date date = new Date();
        int g=date.getDay();
        int m=date.getMonth();
        int a=date.getYear();

        if(anno>a-14||anno<a-100)
            res=false;
        else if(anno==a){
                if(mese>m)
                    res=false;
                else
                    if(mese==m&&giorno>g)
                        res=false;


        }
        return res;
    }

    public void openActivityLogin()
    {
        Intent Loginintent = new Intent(this, LoginActivity.class );
        startActivity(Loginintent);
    }

    public void openActivityMain()
    {
        Intent Mainintent = new Intent(this, MainActivity.class );
        startActivity(Mainintent);
    }

}
