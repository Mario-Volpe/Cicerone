package com.example.cicerone.data;

public class Feedback {
    private Integer globetrotter;
    private Integer idAttivita;
    private String commento;
    private Integer voto;

    public Feedback(Integer globetrotter,Integer idAttivita,Integer voto,String commento){
        setGlobetrotter(globetrotter);
        setIdAttivita(idAttivita);
        setVoto(voto);
        setCommento(commento);

    }

    public Feedback(Integer globetrotter,Integer idAttivita){
        setGlobetrotter(globetrotter);
        setIdAttivita(idAttivita);
    }

    public Integer getGlobetrotter() {
        return globetrotter;
    }

    public void setGlobetrotter(Integer globetrotter) {
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
