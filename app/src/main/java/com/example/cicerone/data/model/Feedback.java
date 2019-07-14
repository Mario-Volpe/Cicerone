package com.example.cicerone.data.model;

public class Feedback {
    private String globetrotter;
    private Integer idAttivita;
    private String commento;
    private Integer voto;

    public Feedback(String globetrotter,Integer idAttivita,Integer voto,String commento){
        setGlobetrotter(globetrotter);
        setIdAttivita(idAttivita);
        setVoto(voto);
        setCommento(commento);

    }

    public Feedback(String globetrotter,Integer idAttivita){
        setGlobetrotter(globetrotter);
        setIdAttivita(idAttivita);
    }

    public String getGlobetrotter() {
        return globetrotter;
    }

    public void setGlobetrotter(String globetrotter) {
        this.globetrotter = globetrotter;
    }

    public Integer getIdAttivita() {
        return idAttivita;
    }

    public void setIdAttivita(Integer idAttivita) {
        this.idAttivita = idAttivita;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public Integer getVoto() {
        return voto;
    }

    public void setVoto(Integer voto) {
        this.voto = voto;
    }
}
