package com.example.biblioteca.modules;

public class BookSearchModel {

    int id;
    String titolo;
    String autore;
    String tipologia;

    public BookSearchModel(int id, String titolo, String autore, String tipologia) {
        this.id = id;
        this.titolo = titolo;
        this.autore = autore;
        this.tipologia = tipologia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
}
