package com.example.cicerone;

public class Utente {
    private static final Utente ourInstance = new Utente();

    public Utente() {

    }

    public Utente(String password, String cognome, String nome, String email) {
        this.password = password;
        this.cognome = cognome;
        this.nome = nome;
        this.email = email;
    }


    public static Utente getInstance() {
        return ourInstance;
    }

    private String password;
    private String cognome;
    private String nome;
    private String email;

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
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}