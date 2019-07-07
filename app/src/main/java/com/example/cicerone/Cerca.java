package com.example.cicerone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Cerca extends AppCompatActivity {
    private ListView lista;
    private ArrayAdapter<String> adapter=null;
    private FoodAdapter fadapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca);
        lista = findViewById(R.id.risultati);

        ArrayList<String> array = new ArrayList<>();
        ArrayList<Attivita> s = getIntent().getExtras().getParcelableArrayList("risultati");
        final Integer[] ids;
        String avv="";
        int flag=0;
        int j=0;

        if(s.size()==0){
            avv = "Nessuna attività trovata";
            Toast.makeText(Cerca.this, "Nessuna attività trovata", Toast.LENGTH_SHORT).show();
            flag=1;
        }

        if (flag==0) {
            ids = new Integer[s.size()];
            for (Attivita b : s) {
                array.add(b.toStringSearch());
                ids[j] = b.getIdAttivita();
                j++;
            }
        }
        else{
            ids = new Integer[1];
            array.add(avv);
        }

        adapter = new ArrayAdapter<>(
                Cerca.this,android.R.layout.simple_list_item_1,array
        );

        fadapter = new FoodAdapter(this,s);

        if (flag==1)
            lista.setAdapter(adapter);

        else{
            lista.setAdapter(fadapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //final String titoloriga = (String) parent.getItemAtPosition(position);
                    Intent inte = new Intent(Cerca.this,Rimozione.class);
                    inte.putExtra("id",ids[position]);
                    inte.putExtra("chiamante","cerca");
                    inte.putExtra("npartecipanti",getIntent().getExtras().getInt("npartecipanti"));
                    startActivity(inte);
                }
            });
        }

    }
}
