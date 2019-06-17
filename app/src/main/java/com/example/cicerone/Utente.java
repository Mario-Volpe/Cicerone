package com.example.cicerone;

import android.provider.ContactsContract;

public class Utente {
    private static final Utente ourInstance = new Utente();

    public static Utente getInstance() {
        return ourInstance;
    }

    private String password;
    private String cognome;
    private String nome;
    private ContactsContract.CommonDataKinds.Email email;

    public ContactsContract.CommonDataKinds.Email getEmail() {
        return email;
    }

    public void setEmail(ContactsContract.CommonDataKinds.Email email) {
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
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Utente(String nome,String cognome, ContactsContract.CommonDataKinds.Email email, String password) {
        setCognome(cognome);
        setEmail(email);
        setNome(nome);
        setPassword(password);
    }

    public Utente(){

    }
}