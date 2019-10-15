package com.example.cicerone.data;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cicerone.R;

public class ToCicerone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_cicerone);

        final EditText CFBox = findViewById(R.id.CF);
        final EditText TelefonoBox = findViewById(R.id.phone);
        final DBhelper db = new DBhelper(this);
        final Integer id = getIntent().getExtras().getInt("idUtente");
        Button invia = findViewById(R.id.buttonInvia);

        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CFstr = CFBox.getText().toString().toUpperCase().trim();
                String Telefonostr = TelefonoBox.getText().toString().trim();

                if(!PhoneNumberUtils.isWellFormedSmsAddress(Telefonostr)||Telefonostr.length()<6)
                    Toast.makeText(ToCicerone.this,"Il numero di telefono è errato.",Toast.LENGTH_SHORT).show();
                else {
                    if(CFstr.length()!=16)
                        Toast.makeText(ToCicerone.this,"Il codice fiscale è errato.",Toast.LENGTH_SHORT).show();
                    else {
                        db.upgradeUtente(id,CFstr,Telefonostr);
                        Toast.makeText(ToCicerone.this,"Account aggiornato.",Toast.LENGTH_SHORT).show();
                        db.close();
                        Intent inte = new Intent(ToCicerone.this, Creazione.class);
                        inte.putExtra("id",id);
                        startActivity(inte);
                        finish();
                    }
                }
            }
        });
    }
}
