package com.example.cicerone.data.control;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.R;
import com.example.cicerone.data.model.Attivita;
import com.example.cicerone.data.model.DBhelper;

import java.util.Calendar;

public class Creazione extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener setData ;

    private int anno=0;
    private int mese=0;
    private int giorno=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione);

        Button crea = findViewById(R.id.crea);
        final TextView mostraData = findViewById(R.id.datains);
        final EditText partecipanti = findViewById(R.id.partecipantiins);
        final EditText lingua = findViewById(R.id.linguains);
        final EditText citta = findViewById(R.id.cittains);
        final EditText itinerario = findViewById(R.id.itinerarioins);

        crea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer partecipantiInt=0;
                String partecipantiStr = partecipanti.getText().toString().trim();
                String dataStr = mostraData.getText().toString().trim();
                String itinerarioStr = itinerario.getText().toString().trim();
                String linguaStr = lingua.getText().toString().trim();
                String cittaStr = citta.getText().toString().trim();

                if(partecipantiStr.equals("")||dataStr.equals("")||itinerarioStr.equals("")||linguaStr.equals("")||cittaStr.equals(""))
                    Toast.makeText(Creazione.this, "Tutti i campi sono obbligatori!", Toast.LENGTH_SHORT).show();
                else partecipantiInt = Integer.parseInt(partecipantiStr);

                if (checkInfo(partecipantiInt)) {
                    final Attivita a = new Attivita(getIntent().getExtras().getString("id"), dataStr,itinerarioStr, linguaStr, cittaStr, partecipantiInt);

                    DBhelper db = new DBhelper(Creazione.super.getBaseContext());
                    if(db.inserisciAttivita(a)!=-1)
                        Toast.makeText(Creazione.this, "Creazione riuscita.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Creazione.this, "Errore nella creazione.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        mostraData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Creazione.this, setData,
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
                String date = dayOfMonth+"/"+month+"/"+year;
                mostraData.setText(date);
            }
        };
    }

    private boolean checkInfo(Integer partecipantiInt){

        if (!Functions.checkData(anno,mese,giorno)) {
            Toast.makeText(Creazione.this, "La data inserita non è corretta.", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            if(partecipantiInt<=0) {
                Toast.makeText(Creazione.this, "Il numero dei partecipanti non è corretto.", Toast.LENGTH_SHORT).show();
                return false;
            }

            else return true;
        }
    }
}
