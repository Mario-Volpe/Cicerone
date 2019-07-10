package com.example.cicerone.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.cicerone.Attivita;
import com.example.cicerone.Prenotazione;
import com.example.cicerone.Utente;
import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE="CREATE TABLE ";
    private static final String DROP ="DROP TABLE IF EXISTS ";
    private static final String FROM=" FROM ";

    private static final String DBNAME="Cicerone.db";
    private static final String UTENTE_TABLE="Utenti";
    private static final String U_COL_NOME="Nome";
    private static final String U_COL_COGNOME="Cognome";
    private static final String U_COL_EMAIL="Email";
    private static final String U_COL_DATA_NASCITA="Data_nascita";
    private static final String U_COL_SEC="Password";

    private static final String ATTIVITA_TABLE="Attivita";
    private static final String A_COL_ATTIVITA="ID_Attivita";
    private static final String A_COL_CICERONE="ID_Cicerone";
    private static final String A_COL_CITTA="Citta";
    private static final String A_COL_ITINERARIO="Descrizione_itinerario";
    private static final String A_COL_LINGUA="Lingua";
    private static final String A_COL_PARTECIPANTI="Max_partecipanti";
    private static final String A_COL_DATA="Data";

    private static final String PRENOTAZIONE_TABLE="Prenotazione";
    private static final String P_COL_ATTIVITA="ID_ATTIVITA";
    private static final String P_COL_GLOBETROTTER="Globetrotter";
    private static final String P_COL_PARTECIPANTI="Numero_partecipanti";
    private static final String P_COL_COMMENTI="Commenti";
    private static final String P_COL_CONFERMA="Conferma";

    public DBhelper(Context context ) {
        super(context, DBNAME, null, 13);

        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableUtente = CREATE_TABLE +UTENTE_TABLE+"(" +
                "EMAIL TEXT PRIMARY KEY," +
                "NOME TEXT," +
                "COGNOME TEXT," +
                "PASSWORD TEXT," +
                "DATA_NASCITA TEXT)";
        db.execSQL(createTableUtente);

        String createTableAttivita = CREATE_TABLE +ATTIVITA_TABLE+"(" +
                "ID_ATTIVITA INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DATA TEXT," +
                "CITTA TEXT," +
                "LINGUA TEXT," +
                "DESCRIZIONE_ITINERARIO TEXT, " +
                "MAX_PARTECIPANTI INTEGER," +
                "ID_CICERONE TEXT," +
                "FOREIGN KEY (ID_CICERONE) REFERENCES Utenti(EMAIL)" +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE "+
                ")";
        db.execSQL(createTableAttivita);

        String createTablePrenotazione = CREATE_TABLE +PRENOTAZIONE_TABLE+"(" +
                "GLOBETROTTER TEXT REFERENCES Utenti( EMAIL)," +
                "ID_ATTIVITA INTEGER REFERENCES Attivita(ID_ATTIVITA)," +
                "NUMERO_PARTECIPANTI INTEGER," +
                "COMMENTI TEXT, " +
                "CONFERMA INTEGER," + //0: in attesa, 1:confermata, 2:rifiutata
                "PRIMARY KEY ( GLOBETROTTER, ID_ATTIVITA) " +
                ")";
        db.execSQL(createTablePrenotazione);

    }

    public long inserisciUtente( Utente u )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( U_COL_EMAIL, u.getEmail() );
        values.put( U_COL_NOME, u.getNome() );
        values.put( U_COL_COGNOME, u.getCognome());
        values.put(U_COL_SEC, u.getPassword());
        values.put( U_COL_DATA_NASCITA, u.getDatanascita());

        long res = db.insert( UTENTE_TABLE, null, values );
        db.close();

        return res;
    }

    public long inserisciAttivita( Attivita a )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( A_COL_DATA, a.getData() );
        values.put( A_COL_CITTA, a.getCitta() );
        values.put( A_COL_LINGUA, a.getLingua() );
        values.put( A_COL_ITINERARIO, a.getDescrizione() );
        values.put( A_COL_PARTECIPANTI, a.getMaxPartecipanti() );
        values.put( A_COL_CICERONE, a.getCicerone() );

        long res = db.insert( ATTIVITA_TABLE, null, values );
        db.close();

        return res;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP+ UTENTE_TABLE );
        db.execSQL(DROP+ ATTIVITA_TABLE );
        db.execSQL(DROP+ PRENOTAZIONE_TABLE );

        onCreate( db );
    }

    /**
     * Metodo che dato un utente cerca la corrispettiva password
     * @param utente dati del registrante
     * @return "" se la password non viene trovata, altrimenti password
     */
    public String searchPassword (Utente utente)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select EMAIL, PASSWORD"+FROM+UTENTE_TABLE;

        Cursor cursor = db.rawQuery(query, null);
        String c,b;
        b = ""; //stringa vuota indica password non trovata

        if (cursor.moveToFirst())
        {
            do {

                c = cursor.getString(0); //prende l'utente

                if (c.equals(utente.getEmail())) //email corrispondente trovata, assegno la password corrispondente
                {
                    b = cursor.getString(1);
                    break;
                }

            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return b;
    }

    /**
     * Metodo che dato un utente lo cerca nel db
     * @param utente da cercare
     * @return True se utente esiste, False altrimenti
     */
    public boolean isSignedUp (Utente utente)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select EMAIL"+FROM+UTENTE_TABLE;

        Cursor cursor = db.rawQuery(query, null);
        String email;
        boolean isIn = false;

        if (cursor.moveToFirst())
        {
            do {
                email = cursor.getString(0);

                if (email.equals(utente.getEmail()))
                {
                    isIn = true;
                    break;
                }
            }while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return isIn;
    }

    public Utente getInfoUtente( Utente utente )
    {
        String email=null,password=null,nome = null,cognome=null,data=null;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select EMAIL, NOME, COGNOME, PASSWORD, DATA_NASCITA" +
                    FROM +UTENTE_TABLE+" WHERE EMAIL = '"+utente.getEmail()+"'";

        Cursor cursor = db.rawQuery(query,null );

            if (cursor.moveToFirst()) {
                do {
                    email = cursor.getString(0);
                    nome = cursor.getString( 1 );
                    cognome = cursor.getString( 2 );
                    password = cursor.getString( 3 );
                    data = cursor.getString( 4 );

                    if ( email.equals(utente.getEmail()) &&
                            password.equals(utente.getPassword()) ) {
                        break;
                    }

                }while (cursor.moveToNext());

            db.close();
            cursor.close();
        }

        utente.setNome(nome);
        utente.setPassword(password);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setDatanascita(data);

        return utente;
    }

    public ArrayList<Attivita> getInfoAttivita(Attivita a,String email) {
        String query = "select a.ID_Attivita, a.Data, a.Citta, a.Lingua, a.Descrizione_itinerario, a.Max_partecipanti, a.Id_Cicerone" +
                 FROM +ATTIVITA_TABLE+" a WHERE a.Citta = '"+a.getCitta()+
                "' AND a.DATA = '"+a.getData()+"' AND a.Max_partecipanti >= "+a.getMaxPartecipanti()+
                " AND a.Id_Cicerone != '"+email+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Attivita> s = new ArrayList<>();

        String Cicerone;
        String data, descrizioneItinerario, lingua, citta;
        Integer maxPartecipanti, idAttivita;

        if (cursor.moveToFirst()) {
            do {
                idAttivita = cursor.getInt(0);
                data = cursor.getString(1);
                citta = cursor.getString(2);
                lingua = cursor.getString(3);
                descrizioneItinerario = cursor.getString(4);
                maxPartecipanti = cursor.getInt(5);
                Cicerone = cursor.getString(6);

                ArrayList <Prenotazione> p = getAllPrenotazioni(idAttivita,"cerca");

                if(p==null||p.size()==0){
                    Attivita c = new Attivita(idAttivita, Cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti);
                    s.add(c);
                }
                else
                    for(Prenotazione p2:p){
                        if(!p2.getEmail().equals(email)){
                            Attivita c = new Attivita(idAttivita, Cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti);
                            s.add(c);
                        }
                    }

            } while (cursor.moveToNext());

            db.close();
            cursor.close();
        }
        return  s;
    }

    public Attivita getAttivita(Integer id) {
        String query = "select ID_Attivita, Data, Citta, Lingua, Descrizione_itinerario, Max_partecipanti, Id_Cicerone" +
                  FROM  + ATTIVITA_TABLE + " WHERE ID_Attivita ="+id;

        ArrayList<Attivita> s = attivitaSearcher(query);

        return s.get(0);
    }

    public ArrayList<Attivita> getAllAttivita(String email) {
        String query = "select ID_Attivita, Data, Citta, Lingua, Descrizione_itinerario, Max_partecipanti, Id_Cicerone" +
                 FROM +ATTIVITA_TABLE+ " WHERE Id_Cicerone = '"+email+"'";
        return attivitaSearcher(query);
    }

    private ArrayList<Attivita> attivitaSearcher(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Attivita> s = new ArrayList<>();

        String Cicerone;
        String data, descrizioneItinerario, lingua, citta;
        Integer maxPartecipanti, idAttivita;

        if (cursor.moveToFirst()) {
            do {
                idAttivita = cursor.getInt(0);
                data = cursor.getString(1);
                citta = cursor.getString(2);
                lingua = cursor.getString(3);
                descrizioneItinerario = cursor.getString(4);
                maxPartecipanti = cursor.getInt(5);
                Cicerone = cursor.getString(6);

                Attivita c = new Attivita(idAttivita, Cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti);
                s.add(c);

            } while (cursor.moveToNext());

            db.close();
            cursor.close();
        }
        return  s;
    }

    public int rimuoviAttivita(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();

        int flag = db.delete(ATTIVITA_TABLE,"ID_Attivita=?",new String[]{""+id});

        db.close();
        return flag;
    }

    public long richiestaPartecipazione(int partecipanti,int id,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(P_COL_GLOBETROTTER, email );
        values.put(P_COL_ATTIVITA, id );
        values.put(P_COL_PARTECIPANTI, partecipanti );
        values.put(P_COL_COMMENTI,"");
        values.put(P_COL_CONFERMA,0);

        long res = db.insert( PRENOTAZIONE_TABLE, null, values );
        db.close();

        return res;
    }

    public ArrayList<Prenotazione> getAllPrenotazioni(Integer id,String chiamante){
        ArrayList<Prenotazione> p = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query;
        String sel="select GLOBETROTTER, ID_ATTIVITA, NUMERO_PARTECIPANTI, COMMENTI, CONFERMA";
        String where=" WHERE ID_ATTIVITA = ";
        if(chiamante.equals("modifica"))
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+id;
        else
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+id+" AND CONFERMA = 1";

        if(chiamante.equals("richieste"))
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+id+" AND CONFERMA = 0";

        Cursor cursor = db.rawQuery(query,null );

        String Globetrotter;
        Integer nPartecipanti,idAttivita;

        if (cursor.moveToFirst()) {
            do {
                Globetrotter = cursor.getString(0);
                idAttivita = cursor.getInt( 1 );
                nPartecipanti = cursor.getInt( 2 );

                Prenotazione c = new Prenotazione(Globetrotter,idAttivita,nPartecipanti);
                p.add(c);

            }while (cursor.moveToNext());

            db.close();
            cursor.close();
        }

        return p;
    }

    public ArrayList<Prenotazione> getAllPrenotazioniUtente(String id){
        ArrayList<Prenotazione> p = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query;

        query = "select GLOBETROTTER, ID_ATTIVITA, NUMERO_PARTECIPANTI, COMMENTI, CONFERMA" +
                 FROM +PRENOTAZIONE_TABLE+ " WHERE GLOBETROTTER = '"+id+"'";

        Cursor cursor = db.rawQuery(query,null );

        String Globetrotter,commenti;
        Integer nPartecipanti,idAttivita,conferma;

        if (cursor.moveToFirst()) {
            do {
                Globetrotter = cursor.getString(0);
                idAttivita = cursor.getInt( 1 );
                nPartecipanti = cursor.getInt( 2 );
                commenti = cursor.getString( 3 );
                conferma = cursor.getInt(4);

                Prenotazione c = new Prenotazione(Globetrotter,idAttivita,nPartecipanti,commenti,conferma);
                p.add(c);

            }while (cursor.moveToNext());

            db.close();
            cursor.close();
        }

        return p;
    }

    public int updatePrenotazione(Prenotazione p){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues args = new ContentValues();
        args.put(P_COL_COMMENTI, p.getCommenti());
        args.put(P_COL_CONFERMA, p.getFlagConferma());

        String[] str = new String[]{""+p.getIdAttivita(), p.getEmail()};

        int flag = db.update(PRENOTAZIONE_TABLE,args,"ID_ATTIVITA=? AND GLOBETROTTER=?",str);

        db.close();
        return flag;
    }

    public int rimuoviPrenotazione(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();

        int flag = db.delete(PRENOTAZIONE_TABLE,"ID_Attivita=?",new String[]{""+id});

        db.close();
        return flag;
    }
}