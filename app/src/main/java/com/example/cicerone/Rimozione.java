package com.example.cicerone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.data.model.DBhelper;

public class Rimozione extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rimozione);

        TextView city = findViewById(R.id.city);
        TextView date = findViewById(R.id.date);
        TextView tongue = findViewById(R.id.tongue);
        final TextView npartecipanti = findViewById(R.id.npartecipanti);
        TextView description = findViewById(R.id.description);
        TextView cicerone = findViewById(R.id.cicerone);
        TextView ciceronetxt = findViewById(R.id.ciceronetxt);
        Button rimuovi = findViewById(R.id.rimozione);
        TextView alert = findViewById(R.id.alert);

        final Attivita a = new DBhelper(Rimozione.this).getAttivita(getIntent().getExtras().getInt("id"));

        final String chiamante = getIntent().getExtras().getString("chiamante");

        if(chiamante.equals("cerca")) {
            rimuovi.setText("Partecipa");
            alert.setText("Inoltrando la richiesta di partecipazione bisogna aspettare di essere accettati dal Cicerone.");
            cicerone.setText(a.getCicerone());
        }
        else {
            cicerone.setVisibility(View.INVISIBLE);
            ciceronetxt.setVisibility(View.INVISIBLE);
        }

        city.setText(a.getCitta());
        date.setText(a.getData());
        tongue.setText(a.getLingua());
        npartecipanti.setText(""+a.getMaxPartecipanti());
        description.setText(a.getDescrizioneItinerario());

        rimuovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chiamante.equals("modifica")){
                    if(new DBhelper(Rimozione.this).rimuoviAttivita(a.getIdAttivita())==0)
                        Toast.makeText(Rimozione.this, "Errore nella rimozione.", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(Rimozione.this, "Rimozione completata.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else {
                    //richiesta di partecipazione
                    int partecipanti = getIntent().getExtras().getInt("npartecipanti");
                    Toast.makeText(Rimozione.this, "Partecipanti: "+partecipanti, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
