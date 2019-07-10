package com.example.cicerone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.data.model.DBhelper;

public class DettagliAttivita extends AppCompatActivity {
    private String chiamante;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_attivita);

        description = findViewById(R.id.description);
        TextView city = findViewById(R.id.city);
        TextView date = findViewById(R.id.date);
        TextView tongue = findViewById(R.id.tongue);
        final TextView npartecipanti = findViewById(R.id.npartecipanti);
        TextView npartecipantitxt = findViewById(R.id.npartecipantitxt);
        TextView cicerone = findViewById(R.id.cicerone);
        TextView ciceronetxt = findViewById(R.id.ciceronetxt);
        Button rimuovi = findViewById(R.id.rimozione);
        TextView alert = findViewById(R.id.alert);

        final Integer id = getIntent().getExtras().getInt("id");

        final Attivita a = new DBhelper(DettagliAttivita.this).getAttivita(id);

        chiamante = getIntent().getExtras().getString("chiamante");

        inizializza(rimuovi,alert,npartecipantitxt,npartecipanti,cicerone,ciceronetxt,a);

        city.setText(a.getCitta());
        date.setText(a.getData());
        tongue.setText(a.getLingua());

        rimuovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottone(chiamante,a,id);
            }
        });

    }

    private void inizializza(Button rimuovi, TextView alert, TextView npartecipantitxt, TextView npartecipanti,TextView cicerone, TextView ciceronetxt, Attivita a){

        if(chiamante.equals("cerca")) {
            rimuovi.setText("Partecipa");
            alert.setText("Inoltrando la richiesta di partecipazione bisogna aspettare di essere accettati dal Cicerone.");
            npartecipantitxt.setText("Posti disponibili:");
            npartecipanti.setText(""+getIntent().getExtras().getInt("postidisponibili"));
            cicerone.setText(a.getCicerone());
            description.setText(a.getDescrizione());
        }
        if(chiamante.equals("modifica")) {
            npartecipanti.setText(""+a.getMaxPartecipanti());
            cicerone.setVisibility(View.INVISIBLE);
            ciceronetxt.setVisibility(View.INVISIBLE);
            description.setText(a.getDescrizione());
        }
        if(chiamante.equals("inoltrate")) {
            rimuovi.setText("Annulla prenotazione");
            alert.setVisibility(View.INVISIBLE);
            npartecipantitxt.setText("Posti prenotati:");
            npartecipanti.setText(""+getIntent().getExtras().getInt("prenotati"));
            cicerone.setText(a.getCicerone());
            if(getIntent().getExtras().getInt("flag")==0)
                description.setText(a.getDescrizione());
            else description.setText(getIntent().getExtras().getString("descrizione"));
        }
    }

    private void bottone(String chiamante,Attivita a,Integer id){
        if(chiamante.equals("modifica")){
            if(new DBhelper(DettagliAttivita.this).rimuoviAttivita(a.getIdAttivita())==0)
                Toast.makeText(DettagliAttivita.this, "Errore nella rimozione.", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(DettagliAttivita.this, "Rimozione completata.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        if(chiamante.equals("cerca")) {
            //richiesta di partecipazione
            int partecipanti = getIntent().getExtras().getInt("npartecipanti");
            String email = getIntent().getExtras().getString("email");

            if (new DBhelper(DettagliAttivita.this).richiestaPartecipazione(partecipanti,id,email)==-1)
                Toast.makeText(DettagliAttivita.this, "Errore nell'inoltro della richiesta.", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(DettagliAttivita.this, "Richiesta inoltrata.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if(chiamante.equals("inoltrate")){
            if(new DBhelper(DettagliAttivita.this).rimuoviPrenotazione(a.getIdAttivita())==0)
                Toast.makeText(DettagliAttivita.this, "Errore nell'annullamento.", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(DettagliAttivita.this, "Annullamento completato.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
