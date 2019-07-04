package com.example.cicerone;

import android.content.Context;
import android.content.SharedPreferences;

public class Utente {
    private static final Utente ourInstance = new Utente();
    Context context;
    SharedPreferences sharedPreferences;

    public Utente() {

    }

    public Utente(Context context, String password, String cognome, String nome, String email, String datanascita) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo",context.MODE_PRIVATE);

        setPassword(password);
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
        setDatanascita(datanascita);
    }

    public void rimuoviUtente(){
        sharedPreferences.edit().clear().commit();
    }


    public static Utente getInstance() {
        return ourInstance;
    }

    private String password;
    private String cognome;
    private String nome;
    private String email;
    private String datanascita;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        nome=sharedPreferences.getString("nome","");
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        sharedPreferences.edit().putString("nome",nome).commit();
    }

    public String getDatanascita() {
        return datanascita;
    }

    public void setDatanascita(String datanascita) {
        this.datanascita = datanascita;
    }
}