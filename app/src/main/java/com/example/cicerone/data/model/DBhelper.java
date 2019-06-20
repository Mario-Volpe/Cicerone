package com.example.cicerone.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.cicerone.Utente;

public class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME="Cicerone.db";
    public static final String UTENTE_TABLE="Utenti";
    public static final String U_COL_NOME="Nome";
    public static final String U_COL_COGNOME="Cognome";
    public static final String U_COL_EMAIL="Email";
    public static final String U_COL_DATA_NASCITA="Data_nascita";
    public static final String U_COL_PASSWORD="Password";

    public static final String ATTIVITA_TABLE="Attivita";
    public static final String A_COL_ATTIVITA="ID_Attivita";
    public static final String A_COL_CICERONE="ID_Cicerone";
    public static final String A_COL_ITINERARIO="Descrizione_itenerario";
    public static final String A_COL_LINGUA="Lingua";
    public static final String A_COL_PARTECIPANTI="Max_partecipanti";
    public static final String A_COL_DATA="Data";

    public static final String PRENOTAZIONE_TABLE="Prenotazione";
    public static final String P_COL_ATTIVITA="ID_ACTIVITY";
    public static final String P_COL_GLOBETROTTER="ID_Globetrotter";
    public static final String P_COL_PARTECIPANTI="Numero_partecipanti";



    public DBhelper(Context context ) {
        super(context, DBNAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableUtente = "CREATE TABLE " +UTENTE_TABLE+"(" +
                "ID_UTENTE INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NOME TEXT," +
                "COGNOME TEXT," +
                "EMAIL TEXT," +
                "PASSWORD TEXT)";
        db.execSQL(createTableUtente);

        String createTableAttivita = "CREATE TABLE " +ATTIVITA_TABLE+"(" +
                "ID_ATTIVITA INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DATA DATE," +
                "LINGUA TEXT," +
                "MAX_PARTECIPANTI INTEGER," +
                "ID_CICERONE INTEGER," +
                "FOREIGN KEY (ID_CICERONE) REFERENCES Utenti(ID_UTENTE)" +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE "+
                ")";
        db.execSQL(createTableAttivita);

        String createTablePrenotazione = "CREATE TABLE " +PRENOTAZIONE_TABLE+"(" +
                "GLOBETROTTER INTEGER REFERENCES Utenti( ID_UTENTE)," +
                "ID_ACTIVITY INTEGER REFERENCES Attivita(ID_ATTIVTA)," +
                "NUMERO_PARTECIPANTI INTEGER," +
                "PRIMARY KEY ( GLOBETROTTER, ID_ACTIVITY) " +
                ")";
        db.execSQL(createTablePrenotazione);

    }

    public long inserisciUtente( Utente u )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( U_COL_NOME, u.getNome() );
        values.put( U_COL_COGNOME, u.getCognome());
        values.put( U_COL_PASSWORD, u.getPassword());

        long res = db.insert( UTENTE_TABLE, null, values );
        db.close();

        return res;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ UTENTE_TABLE );
        db.execSQL("DROP TABLE IF EXISTS "+ ATTIVITA_TABLE );
        db.execSQL("DROP TABLE IF EXISTS "+ PRENOTAZIONE_TABLE );

        onCreate( db );
    }
}