package com.example.cicerone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class DBhelper extends SQLiteOpenHelper {
    private static final String FROM=" FROM ";

    private static final String DBNAME="Cicerone.db";
    private static final String UTENTE_TABLE="Utenti";
    private static final String U_COL_ID="IdUtente";
    private static final String U_COL_NOME="NomeUtente";
    private static final String U_COL_COGNOME="CognomeUtente";
    private static final String U_COL_EMAIL="EmailUtente";
    private static final String U_COL_DATA_NASCITA="DataNascitaUtente";
    private static final String U_COL_SEC="PasswordUtente";
    private static final String U_COL_CF="CFUtente";
    private static final String U_COL_TELEFONO="TelefonoUtente";

    private static final String ATTIVITA_TABLE="Attivita";
    private static final String A_COL_CICERONE="FkUtentiAttivita";
    private static final String A_COL_ID="IdAttivita";
    private static final String A_COL_CITTA="CittaAttivita";
    private static final String A_COL_ITINERARIO="DescrizioneAttivita";
    private static final String A_COL_ORA="OraAttivita";
    private static final String A_COL_LINGUA="FkLingueAttivita";
    private static final String A_COL_PARTECIPANTI="MaxPartecipantiAttivita";
    private static final String A_COL_DATA="DataAttivita";

    private static final String PRENOTAZIONE_TABLE="Prenotazioni";
    private static final String P_COL_ATTIVITA="FkAttivitaPrenotazione";
    private static final String P_COL_GLOBETROTTER="FkUtentiPrenotazione";
    private static final String P_COL_PARTECIPANTI="PartecipantiPrenotazione";
    private static final String P_COL_COMMENTI="CommentiPrenotazione";
    private static final String P_COL_CONFERMA="ConfermaPrenotazione";

    private static final String FEEDBACK_TABLE="Feedback";
    private static final String F_COL_ATTIVITA="FkAttivitaFeedback";
    private static final String F_COL_GLOBETROTTER="FkUtentiFeedback";
    private static final String F_COL_VOTO="VotoFeedback";
    private static final String F_COL_COMMENTO="CommentoFeedback";

    private static final String LINGUA_TABLE="Lingue";
    private static final String L_COL_ID="IdLingua";
    private static final String L_COL_NOME="NomeLingua";

    public DBhelper(Context context ) {
        super(context, DBNAME, null, 15);
        new ConnectDB().conn();
    }


    public JSONArray doQuery(String query){

        String result="";
        JSONArray jArray = null;
        StringBuilder sb;
        InputStream is = null;
        ArrayList<NameValuePair> querySend = new ArrayList<NameValuePair>();
        querySend.add(new BasicNameValuePair("querySend",query));

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://18.232.247.191/query.php");
            httppost.setEntity(new UrlEncodedFormEntity(querySend));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, HTTP.UTF_8);
            is = entity.getContent();
            Log.e("log_tag", "Success in http connection ");
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());

        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);

            sb = new StringBuilder();

            sb.append(reader.readLine() + "\n");

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            is.close();
            Log.e("log_tag", "No converting errors");
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        if(result.isEmpty())
            Log.e("log_tag","è vuoto.");

        try{
            jArray = new JSONArray(result);
        } catch(JSONException e1){
            Log.e("Errore:", "Conversione fallita");
        }
        return jArray;
    }



    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    public long inserisciUtente( Utente u )
    {
        long res=0;
        String query="INSERT INTO "+UTENTE_TABLE+" ("+U_COL_EMAIL+","+U_COL_NOME+","+U_COL_COGNOME+","+U_COL_SEC+","+U_COL_DATA_NASCITA+","+U_COL_TELEFONO+","+U_COL_CF+")"+
                " VALUES ('"+u.getEmail()+"','"+u.getNome()+"','"+u.getCognome()+"','"+u.getPassword()+"','"+u.getDatanascita()+"',+'"+u.getTelefono()+"','"+u.getCF()+"')";

        doQuery(query);
        return res;
    }

    public void upgradeUtente(Integer id,String CF,String telefono){
        String query = "UPDATE "+UTENTE_TABLE+" SET "+U_COL_CF+" = '"+CF+"', "+ U_COL_TELEFONO+" = '"+telefono+"'"+
                " WHERE "+U_COL_ID+" = '"+id+"'";

        doQuery(query);
    }

    public long inserisciAttivita( Attivita a )
    {
        long res=0;
        String query="INSERT INTO "+ATTIVITA_TABLE+" ("+A_COL_CICERONE+","+A_COL_CITTA+","+A_COL_DATA+","+A_COL_ITINERARIO+","+A_COL_LINGUA+","+A_COL_ORA+","+A_COL_PARTECIPANTI+")"+
                " VALUES ('"+a.getCicerone()+"','"+a.getCitta()+"','"+a.getData()+"','"+a.getDescrizione()+"','"+a.getLingua()+"','"+a.getOra()+"','"+a.getMaxPartecipanti()+"')";

        doQuery(query);
        return res;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    /**
     * Metodo che dato un utente cerca la corrispettiva password
     * @param utente dati del registrante
     * @return "" se la password non viene trovata, altrimenti password
     */
    public String searchPassword (Utente utente)
    {
        String stringaFinale="";
        String query = "select "+U_COL_SEC+FROM+UTENTE_TABLE+" WHERE "+U_COL_EMAIL+"='"+utente.getEmail()+"'";

        JSONArray jArray = doQuery(query);
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                stringaFinale = stringaFinale + json_data.getString( U_COL_SEC);
            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca psw:","Non trovata");
        }

        return stringaFinale;
    }

    /**
     * Metodo che dato un utente lo cerca nel db
     * @param utente da cercare
     * @return True se utente esiste, False altrimenti
     */
    public boolean isSignedUp (Utente utente)
    {
        String stringaFinale="";
        String query = "select "+ U_COL_EMAIL+FROM+UTENTE_TABLE+" WHERE "+U_COL_EMAIL+"='"+utente.getEmail()+"'";
        boolean isIn = false;

        JSONArray jArray = doQuery(query);
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                stringaFinale = "" + stringaFinale + "" + json_data.getString( U_COL_EMAIL);
            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca utente:","Non trovato");
        }

        if (!stringaFinale.equals(""))
            isIn=true;

        return isIn;
    }

    public Utente getInfoUtentebyID( Integer id )
    {
        String query = "select "+U_COL_ID+","+ U_COL_NOME+","+ U_COL_COGNOME+","+ U_COL_SEC+","+ U_COL_DATA_NASCITA+","+U_COL_CF+","+U_COL_TELEFONO+
                FROM +UTENTE_TABLE+" WHERE "+U_COL_ID+" = '"+id+"'";

        JSONArray jArray = doQuery(query);
        Utente utente = new Utente();

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                utente.setId(json_data.getInt( U_COL_ID));
                utente.setNome(json_data.getString( U_COL_NOME));
                utente.setCognome(json_data.getString( U_COL_COGNOME));
                utente.setCF(json_data.getString( U_COL_CF));
                utente.setPassword(json_data.getString( U_COL_SEC));
                utente.setDatanascita(json_data.getString( U_COL_DATA_NASCITA));
                utente.setTelefono(json_data.getString( U_COL_TELEFONO));

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca utente:","Non trovato");
        }

        return utente;
    }

    public Utente getInfoUtente( Utente utente ) {

        String query = "select "+U_COL_ID+","+ U_COL_NOME+","+ U_COL_COGNOME+","+ U_COL_SEC+","+ U_COL_DATA_NASCITA+","+U_COL_CF+","+U_COL_TELEFONO+
                    FROM +UTENTE_TABLE+" WHERE "+U_COL_EMAIL+" = '"+utente.getEmail()+"'";

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                utente.setId(json_data.getInt( U_COL_ID));
                utente.setNome(json_data.getString( U_COL_NOME));
                utente.setCognome(json_data.getString( U_COL_COGNOME));
                utente.setCF(json_data.getString( U_COL_CF));
                utente.setPassword(json_data.getString( U_COL_SEC));
                utente.setDatanascita(json_data.getString( U_COL_DATA_NASCITA));
                utente.setTelefono(json_data.getString( U_COL_TELEFONO));

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca utente:","Non trovato");
        }

        return utente;
    }

    public ArrayList<Attivita> getInfoAttivita(Attivita a,String email) {

        String query = "select "+A_COL_ID+","+A_COL_DATA+","+ A_COL_CITTA+","+A_COL_LINGUA+","+ A_COL_ITINERARIO+","+A_COL_PARTECIPANTI+","+A_COL_CICERONE+","+A_COL_ORA+
                 FROM +ATTIVITA_TABLE+" a WHERE "+A_COL_CITTA+" = '"+a.getCitta()+
                "' AND "+A_COL_DATA+" != '"+a.getData()+"' AND "+A_COL_PARTECIPANTI+" >= '"+a.getMaxPartecipanti()+
                "' AND "+A_COL_CICERONE+" != '"+email+"' AND "+A_COL_LINGUA+" = '"+a.getLingua()+"'";

        ArrayList<Attivita> s = new ArrayList<>();

        String data, descrizioneItinerario,citta,ora;
        Integer maxPartecipanti, idAttivita,lingua,cicerone;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                cicerone = json_data.getInt(A_COL_CICERONE);
                idAttivita = json_data.getInt(A_COL_ID);
                data = json_data.getString(A_COL_DATA);
                descrizioneItinerario = json_data.getString(A_COL_ITINERARIO);
                lingua = json_data.getInt(A_COL_LINGUA);
                citta = json_data.getString(A_COL_CITTA);
                maxPartecipanti = json_data.getInt(A_COL_PARTECIPANTI);
                ora = json_data.getString(A_COL_ORA);

                ArrayList <Prenotazione> p = getAllPrenotazioni(idAttivita,"cerca");

                if(p==null||p.size()==0){
                    Integer[] gma = Functions.parseData(data);
                    if(Functions.checkData(gma[0],gma[1],gma[2])){
                        Attivita c = new Attivita(idAttivita, cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti, Time.valueOf(ora));
                        s.add(c);
                    }
                }
                else
                    for(Prenotazione p2:p){
                        if(!p2.getEmail().equals(email)){
                            Integer[] gma = Functions.parseData(data);
                            if(Functions.checkData(gma[0],gma[1],gma[2])){
                                Attivita c = new Attivita(idAttivita, cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti, Time.valueOf(ora));
                                s.add(c);
                            }
                        }
                    }

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }
        return  s;
    }

    public Attivita getAttivita(Integer id) {
        String query = "select "+A_COL_ID+","+A_COL_DATA+","+ A_COL_CITTA+","+A_COL_LINGUA+","+ A_COL_ITINERARIO+","+A_COL_PARTECIPANTI+","+A_COL_CICERONE+","+A_COL_ORA+
                  FROM  + ATTIVITA_TABLE + " WHERE "+A_COL_ID+" = '"+id+"'";

        ArrayList<Attivita> s = attivitaSearcher(query);

        return s.get(0);
    }

    public ArrayList<Attivita> getAllAttivita(Integer id) {
        String query = "select "+A_COL_ID+","+A_COL_DATA+","+ A_COL_CITTA+","+A_COL_LINGUA+","+ A_COL_ITINERARIO+","+A_COL_PARTECIPANTI+","+A_COL_CICERONE+","+A_COL_ORA+
                 FROM +ATTIVITA_TABLE+ " WHERE "+A_COL_CICERONE+" = '"+id+"'";
        return attivitaSearcher(query);
    }

    public ArrayList<Lingua> getAllLingue() {
        String query = "select "+L_COL_ID+","+L_COL_NOME+FROM +LINGUA_TABLE;

        ArrayList<Lingua> s = new ArrayList<>();

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                int idLingua = json_data.getInt(L_COL_ID);
                String nomeLingua = json_data.getString(L_COL_NOME);

                Lingua c = new Lingua(idLingua, nomeLingua);
                s.add(c);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }

        return s;
    }

    private ArrayList<Attivita> attivitaSearcher(String query) {
        ArrayList<Attivita> s = new ArrayList<>();

        String data, descrizioneItinerario, citta, ora;
        Integer maxPartecipanti, idAttivita, cicerone, lingua;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                cicerone = json_data.getInt(A_COL_CICERONE);
                idAttivita = json_data.getInt(A_COL_ID);
                data = json_data.getString(A_COL_DATA);
                descrizioneItinerario = json_data.getString(A_COL_ITINERARIO);
                lingua = json_data.getInt(A_COL_LINGUA);
                citta = json_data.getString(A_COL_CITTA);
                maxPartecipanti = json_data.getInt(A_COL_PARTECIPANTI);
                ora = json_data.getString(A_COL_ORA);

                Attivita c = new Attivita(idAttivita, cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti, Time.valueOf(ora));
                s.add(c);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }

        return  s;
    }

    public int rimuoviAttivita(Integer id) {

        int flag=0;

        String query = "DELETE"+FROM+ATTIVITA_TABLE+" WHERE "+A_COL_ID+" = '"+id+"'";

        JSONArray jArray = doQuery(query);

        try {
            if(jArray.getBoolean(0))
                flag=1;
        } catch (JSONException|NullPointerException e){
            e.printStackTrace();
        }

        return flag;
    }

    public long richiestaPartecipazione(int partecipanti,int id,String email){
        long res=0;
        String query="INSERT INTO "+PRENOTAZIONE_TABLE+" ("+P_COL_GLOBETROTTER+","+P_COL_ATTIVITA+","+P_COL_PARTECIPANTI+","+P_COL_COMMENTI+","+P_COL_CONFERMA+")"+
                " VALUES ('"+email+"','"+id+"','"+partecipanti+"','',0)";

        JSONArray jArray = doQuery(query);
        try {
            if(jArray.getBoolean(0))
                res=1;
        } catch (JSONException|NullPointerException e){
            e.printStackTrace();
        }

        return res;
    }

    public ArrayList<Prenotazione> getAllPrenotazioni(Integer id,String chiamante){
        ArrayList<Prenotazione> p = new ArrayList<>();

        String query;
        String sel="select "+P_COL_GLOBETROTTER+","+ P_COL_ATTIVITA+","+ P_COL_PARTECIPANTI+","+ P_COL_COMMENTI+","+ P_COL_CONFERMA;
        String where=" WHERE "+P_COL_ATTIVITA+" = ";
        if(chiamante.equals("modifica"))
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+id;
        else
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+id+" AND "+P_COL_CONFERMA+" = '1'";

        if(chiamante.equals("richieste"))
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+id+" AND "+P_COL_CONFERMA+" = '0'";


        String Globetrotter;
        Integer nPartecipanti,idAttivita;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                Globetrotter = json_data.getString(P_COL_GLOBETROTTER);
                idAttivita = json_data.getInt(P_COL_ATTIVITA);
                nPartecipanti = json_data.getInt(P_COL_PARTECIPANTI);

                Prenotazione c = new Prenotazione(Globetrotter,idAttivita,nPartecipanti);
                p.add(c);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }

        return p;
    }

    public ArrayList<Prenotazione> getAllPrenotazioniUtente(String id){
        ArrayList<Prenotazione> p = new ArrayList<>();

        String query = "select "+P_COL_GLOBETROTTER+","+ P_COL_ATTIVITA+","+ P_COL_PARTECIPANTI+","+ P_COL_COMMENTI+","+ P_COL_CONFERMA+
                 FROM +PRENOTAZIONE_TABLE+ " WHERE "+P_COL_GLOBETROTTER +" = '"+id+"'";

        String Globetrotter,commenti;
        Integer nPartecipanti,idAttivita,conferma;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                Globetrotter = json_data.getString(P_COL_GLOBETROTTER);
                idAttivita = json_data.getInt(P_COL_ATTIVITA);
                nPartecipanti = json_data.getInt(P_COL_PARTECIPANTI);
                commenti = json_data.getString(P_COL_COMMENTI);
                conferma =json_data.getInt(P_COL_CONFERMA);

                Prenotazione c = new Prenotazione(Globetrotter,idAttivita,nPartecipanti,commenti,conferma);
                p.add(c);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }

        return p;
    }

    public int updatePrenotazione(Prenotazione p){
        int flag=0;

        String query = "UPDATE"+PRENOTAZIONE_TABLE+" SET "+P_COL_COMMENTI+"='"+p.getCommenti()+"',"+P_COL_CONFERMA+"='"+p.getFlagConferma()+
                "' WHERE "+P_COL_ATTIVITA+" = '"+p.getIdAttivita()+"' AND "+P_COL_GLOBETROTTER+" = '"+p.getEmail()+"'";

        JSONArray jArray = doQuery(query);

        try {
            if(jArray.getBoolean(0))
                flag=1;
        } catch (JSONException|NullPointerException e){
            e.printStackTrace();
        }

        return flag;
    }

    public int rimuoviPrenotazione(Integer id) {
        int flag=0;

        String query = "DELETE"+FROM+PRENOTAZIONE_TABLE+" WHERE "+P_COL_ATTIVITA+" = '"+id+"'";

        JSONArray jArray = doQuery(query);

        try {
            if(jArray.getBoolean(0))
                flag=1;
        } catch (JSONException|NullPointerException e){
            e.printStackTrace();
        }
        return flag;
    }

    public long inserisciFeedback(Feedback f){
        long res=0;
        String query="INSERT INTO "+FEEDBACK_TABLE+" ("+F_COL_GLOBETROTTER+","+F_COL_ATTIVITA+","+F_COL_VOTO+","+F_COL_COMMENTO+")"+
                " VALUES ('"+f.getGlobetrotter()+"','"+f.getIdAttivita()+"','"+f.getVoto()+"','"+f.getCommento()+"'";

        JSONArray jArray = doQuery(query);
        try {
            if(jArray.getBoolean(0))
                res=1;
        } catch (JSONException|NullPointerException e){
            e.printStackTrace();
        }

        return res;
    }

    public Feedback getFeedback(Integer idAttivita, String email) {
        String query = "select "+ F_COL_GLOBETROTTER+","+ F_COL_ATTIVITA+","+ F_COL_VOTO+","+ F_COL_COMMENTO +
                FROM +FEEDBACK_TABLE+ " WHERE "+F_COL_GLOBETROTTER +"= '"+email+"' AND "+F_COL_ATTIVITA+" = '"+idAttivita+"'";

        ArrayList<Feedback> f = feedbackSearcher(query);

        if(f==null||f.size()==0)
            return null;
        else return f.get(0);
    }

    public ArrayList<Feedback> getAllFeedback(Integer idAttivita){
        String query = "select "+F_COL_GLOBETROTTER+","+ F_COL_ATTIVITA+", "+F_COL_VOTO+","+F_COL_COMMENTO +
                FROM +FEEDBACK_TABLE+ " WHERE "+F_COL_ATTIVITA+" = '"+idAttivita+"'";

        return feedbackSearcher(query);
    }

    private ArrayList<Feedback> feedbackSearcher(String query){
        Feedback f;
        ArrayList<Feedback> a = new ArrayList<>();
        String commento,email;
        Integer voto,idAttivita;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                email = json_data.getString(F_COL_GLOBETROTTER);
                idAttivita = json_data.getInt(F_COL_ATTIVITA);
                voto = json_data.getInt(F_COL_VOTO);
                commento = json_data.getString(F_COL_COMMENTO);

                f = new Feedback(email,idAttivita,voto,commento);
                a.add(f);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca Feedback:","Non trovato");
        }

        return a;
    }
}