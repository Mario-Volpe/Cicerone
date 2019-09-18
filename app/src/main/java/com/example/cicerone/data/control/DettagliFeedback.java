package com.example.cicerone.data.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.R;
import com.example.cicerone.data.model.DBhelper;
import com.example.cicerone.data.model.Feedback;

public class DettagliFeedback extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Integer voto;
    private String commentotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_feedback);

        final String email = getIntent().getExtras().getString("email");
        final Integer id = getIntent().getExtras().getInt("idAttivita");
        Boolean flag = getIntent().getExtras().getBoolean("flag");
        TextView titolo = findViewById(R.id.titolo);
        Spinner spinner = findViewById(R.id.spinner);
        final TextView commento = findViewById(R.id.commento);
        TextView votoins = findViewById(R.id.votoins);
        TextView commentoins = findViewById(R.id.commentoins);
        Button b = findViewById(R.id.inviaF);
        final DBhelper db = new DBhelper(this);

        if(flag==true){
            titolo.setText("Riepilogo feedback");
            spinner.setVisibility(View.INVISIBLE);
            commentoins.setVisibility(View.VISIBLE);
            Feedback f = db.getFeedback(id,email);

            votoins.setText(""+f.getVoto()+"/5");
            if(!f.getCommento().equals(""))
                commentoins.setText(f.getCommento());
            b.setVisibility(View.INVISIBLE);
            commento.setVisibility(View.INVISIBLE);
        }
        else {
            commentoins.setVisibility(View.INVISIBLE);
            votoins.setVisibility(View.INVISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.voto,android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentotxt = commento.getText().toString().trim();
                Feedback f = new Feedback(email,id,voto,commentotxt);
                if(db.inserisciFeedback(f)!=-1)
                    Toast.makeText(DettagliFeedback.this, "Feedback inserito.", Toast.LENGTH_SHORT).show();
                else Toast.makeText(DettagliFeedback.this, "Errore nell'inserimento del feedback.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        voto = position+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
