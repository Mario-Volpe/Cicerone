package com.example.cicerone.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME="Cicerone.db";
    public static final String UTENTE_TABLE="Utenti";
    public static final String U_COL1="Nome";
    public static final String U_COL2="Cognome";
    public static final String U_COL3="Email";
    public static final String U_COL4="Data_nascita";
    public static final String U_COL5="Password";

    public static final String ATTIVITA_TABLE="Attivita";
    public static final String A_COL1="ID_Attivita";
    public static final String A_COL2="ID_Cicerone";
    public static final String A_COL3="Descrizione_itenerario";
    public static final String A_COL4="Lingua";
    public static final String A_COL5="Max_partecipanti";
    public static final String A_COL6="Data";

    public static final String PRENOTAZIONE_TABLE="Prenotazione";
    public static final String P_COL1="ID_ACTIVITY";
    public static final String P_COL2="ID_Globetrotter";
    public static final String P_COL3="Numero_partecipanti";



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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ UTENTE_TABLE );
        db.execSQL("DROP TABLE IF EXISTS "+ ATTIVITA_TABLE );
        db.execSQL("DROP TABLE IF EXISTS "+ PRENOTAZIONE_TABLE );

        onCreate( db );
    }
}