package com.example.biblioteca;

public class BookSearchModel {

    int id;
    String titolo;
    String autore;
    String genere;

    public BookSearchModel(int id, String titolo, String autore, String genere) {
        this.id = id;
        this.titolo = titolo;
        this.autore = autore;
        this.genere = genere;
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

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }
}
