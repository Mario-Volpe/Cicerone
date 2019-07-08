package com.example.cicerone;

public class Prenotazione {
    private String email,commenti;
    private Integer idAttivita,partecipanti,flagConferma;

    public Prenotazione(String email,Integer idAttivita,Integer partecipanti){
        setIdAttivita(idAttivita);
        setEmail(email);
        setPartecipanti(partecipanti);
        setCommenti("");
        setFlagConferma(0);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommenti() {
        return commenti;
    }

    public void setCommenti(String commenti) {
        this.commenti = commenti;
    }

    public Integer getIdAttivita() {
        return idAttivita;
    }

    public void setIdAttivita(Integer idAttivita) {
        this.idAttivita = idAttivita;
    }

    public Integer getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(Integer partecipanti) {
        this.partecipanti = partecipanti;
    }

    public Integer getFlagConferma() {
        return flagConferma;
    }

    public void setFlagConferma(Integer flagConferma) {
        this.flagConferma = flagConferma;
    }
}
