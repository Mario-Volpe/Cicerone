package com.example.cicerone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.data.model.DBhelper;

import java.util.ArrayList;

public class ElencoAttivita extends AppCompatActivity {
    private static final String INOLTRATE = "inoltrate";
    private static final String MODIFICA = "modifica";
    private static final String RICHIESTE = "richieste";
    private int j=0;
    private int flag=0; //0 se ci sono attività da mostrare, 1 altrimenti.
    private String avv=""; //non ci sono attività
    private Integer[] ids; //array degli id
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_attivita);

        DBhelper db = new DBhelper(this);
        final String chiamante = getIntent().getExtras().getString("chiamante");
        ArrayAdapter<String> adapter;
        FoodAdapter fadapter=null;
        TextView partecipantitxt = findViewById(R.id.partecipantitxt);

        lista = findViewById(R.id.listaAttivita);

        ArrayList<String> array = new ArrayList<>(); //visualizzati nella lista
        ArrayList<Attivita> s = new ArrayList<>();
        ArrayList<Prenotazione> p = new ArrayList<>();

        ArrayList<Integer> r= new ArrayList<>(); //n richieste per attività

        if(chiamante.equals(INOLTRATE)){
            String email = getIntent().getExtras().getString("id");
            p = db.getAllPrenotazioniUtente(email);
            for(Prenotazione p2:p)
                s.add(db.getAttivita(p2.getIdAttivita()));
        } else s = db.getAllAttivita(getIntent().getExtras().getString("id"));

        inizializza(chiamante,partecipantitxt,s);

        if (flag == 0) {
                ids = new Integer[s.size()];
                for (Attivita b : s) {
                    array.add(b.toStringSearch());
                    ids[j] = b.getIdAttivita();
                    ArrayList<Prenotazione> p2 = db.getAllPrenotazioni(ids[j], chiamante);
                    r.add(p2.size());
                    j++;
                }
        }
        else {
            ids = new Integer[1];
            array.add(avv);
        }


        adapter = new ArrayAdapter<>(
                ElencoAttivita.this, android.R.layout.simple_list_item_1, array
        );

        if(flag==0) {
            if (chiamante.equals(MODIFICA))
                fadapter = new FoodAdapter(this, s, chiamante);
            if (chiamante.equals(RICHIESTE))
                fadapter = new FoodAdapter(this, s, r, chiamante);
            if (chiamante.equals(INOLTRATE))
                fadapter = new FoodAdapter(this, s, chiamante, p);

            lista.setAdapter(fadapter);
            final ArrayList<Prenotazione> pf = p;
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bottone(chiamante,position,pf,ids);
                }
        });
        }
        else lista.setAdapter(adapter);
    }

    private void inizializza(String chiamante,TextView partecipantitxt,ArrayList<Attivita> s){
        if(chiamante.equals(RICHIESTE))
            partecipantitxt.setText("Richieste");
        if(chiamante.equals(INOLTRATE))
            partecipantitxt.setText("Stato");

        if(s==null||s.size()==0) {
            if(chiamante.equals(INOLTRATE)){
                avv = "Nessuna richiesta inoltrata";
                Toast.makeText(ElencoAttivita.this, "Nessuna richiesta inoltrata", Toast.LENGTH_SHORT).show();
            }
            else{
                avv = "Nessuna attività creata";
                Toast.makeText(ElencoAttivita.this, "Nessuna attività creata", Toast.LENGTH_SHORT).show();
            }
            lista.setClickable(false);
            flag=1;
        }
    }

    private void bottone(String chiamante,int position,ArrayList<Prenotazione> pf,Integer[] ids){
        Intent inte=new Intent(ElencoAttivita.this, DettagliAttivita.class);
        if(chiamante.equals(MODIFICA))
            inte= new Intent(ElencoAttivita.this, DettagliAttivita.class);
        if(chiamante.equals(RICHIESTE))
            inte= new Intent(ElencoAttivita.this,DettaglioRichieste.class);
        if(chiamante.equals(INOLTRATE)){
            inte = new Intent(ElencoAttivita.this, DettagliAttivita.class);
            inte.putExtra("prenotati",pf.get(position).getPartecipanti());
            inte.putExtra("flag",pf.get(position).getFlagConferma());
            inte.putExtra("descrizione",pf.get(position).getCommenti());
        }

        inte.putExtra("id",ids[position]);
        inte.putExtra("chiamante",chiamante);
        startActivity(inte);
        finish();
    }
}
