package com.example.cicerone;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";


    private EditText nomeUtente  = findViewById(R.id.nome);
    private EditText cognomeUtente = findViewById(R.id.cognome);
    private EditText passwordUtente = findViewById(R.id.password);
    private EditText emailUtente = findViewById(R.id.email);
    private EditText dataNascitaUtente = findViewById(R.id.dataNascita);
    private Button inviaDatiUtente = (Button) findViewById(R.id.bottoneInvia);
    private TextView collegamentoLogin = findViewById(R.id.link_login);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //ButterKnife.bind(this);

       inviaDatiUtente.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   signup();
               } catch (ParseException e) {
                   e.printStackTrace();
               }
           }
       });

        collegamentoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() throws ParseException {
        Log.d(TAG, "Accedi");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        inviaDatiUtente.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this, R.style.AppTheme);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();



        String nome = nomeUtente.getText().toString();
        String cognome = cognomeUtente.getText().toString();
        String email = emailUtente.getText().toString();
        String password = passwordUtente.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = sdf.parse(dataNascitaUtente.getText().toString());
        Calendar dataNascita = Calendar.getInstance();
        dataNascita.setTime(date);

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        inviaDatiUtente.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login fallito", Toast.LENGTH_LONG).show();
        inviaDatiUtente.setEnabled(true);
    }

    public boolean validate() throws ParseException {
        boolean valid = true;

        String nome = nomeUtente.getText().toString();
        String cognome = cognomeUtente.getText().toString();
        String email = emailUtente.getText().toString();
        String password = passwordUtente.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = sdf.parse(dataNascitaUtente.getText().toString());
        Calendar dataNascita = Calendar.getInstance();
        dataNascita.setTime(date);

        if (nome.isEmpty() || nome.length() < 3) {
            nomeUtente.setError("almeno 3 caratteri");
            valid = false;
        } else {
            nomeUtente.setError(null);
        }

        if (cognome.isEmpty() || cognome.length() < 3) {
            cognomeUtente.setError("almeno 3 caratteri");
            valid = false;
        } else {
            cognomeUtente.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailUtente.setError("inserisci un indirizzo email valido");
            valid = false;
        } else {
            emailUtente.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordUtente.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordUtente.setError(null);
        }

        return valid;
    }
}
