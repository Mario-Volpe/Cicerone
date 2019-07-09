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

public class Modifica extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica);

        ArrayAdapter<String> adapter=null;
        FoodAdapter fadapter=null;
        TextView partecipantitxt = findViewById(R.id.partecipantitxt);

        ListView lista = findViewById(R.id.listaAttivita);

        ArrayList<String> array = new ArrayList<>(); //visualizzati nella lista
        ArrayList<Attivita> s = new DBhelper(Modifica.this).getAllAttivita(getIntent().getExtras().getString("id"));
        ArrayList<Prenotazione> p;
        ArrayList<Integer> r= new ArrayList<>(); //n richieste per attività

        String avv=""; //non ci sono attività
        final String chiamante = getIntent().getExtras().getString("chiamante");
        if(chiamante.equals("richieste"))
            partecipantitxt.setText("Richieste");

        int j=0;
        int flag=0; //0 se ci sono attività da mostrare, 1 altrimenti.

        if(s.size()==0) {
            avv = "Nessuna attività creata";
            Toast.makeText(Modifica.this, "Nessuna attività creata", Toast.LENGTH_SHORT).show();
            lista.setClickable(false);
            flag=1;
        }

        //array degli id
        final Integer[] ids;
        if(flag==0) {
            ids = new Integer[s.size()];
            for(Attivita b:s) {
                array.add(b.toStringSearch());
                ids[j]=b.getIdAttivita();
                p = new DBhelper(Modifica.this).getAllPrenotazioni(ids[j],chiamante);
                r.add(p.size());
                j++;
            }
        }
        else {
            ids = new Integer[1];
            array.add(avv);
        }

        adapter = new ArrayAdapter<>(
                Modifica.this, android.R.layout.simple_list_item_1, array
        );

        if(chiamante.equals("modifica"))
            fadapter = new FoodAdapter(this,s,chiamante);
        else
            fadapter = new FoodAdapter(this,s,r,chiamante);


        if (flag==0){
            lista.setAdapter(fadapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //final String titoloriga = (String) parent.getItemAtPosition(position);
                    Intent inte;
                    if(chiamante.equals("modifica"))
                      inte= new Intent(Modifica.this, DettagliAttivita.class);
                    else inte= new Intent(Modifica.this,DettaglioRichieste.class);

                    inte.putExtra("id",ids[position]);
                    inte.putExtra("chiamante",chiamante);
                    startActivity(inte);
                    finish();
                }
        });
        }
        else lista.setAdapter(adapter);
    }
}
