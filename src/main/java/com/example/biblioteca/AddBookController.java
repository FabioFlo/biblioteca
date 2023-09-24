package com.example.biblioteca;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    // ########## DATABASE ATTRIBUTES ########## //
    String query = null;
    Database database = new Database();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    private boolean update = false;
    private int bookId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Database database = new Database();
//        try {
//            connection = database.getDbConnection();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

    }


    // ################################## GUI ADD BOOK ATTRIBUTES ############################ //
    @FXML
    private TextField textTitle;
    @FXML
    private TextField textAuthor;
    @FXML
    private TextField textUtente;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField textPrezzo;

    // ################################# MOUSE AND BUTTON EVENTS ################################ //
    @FXML
    private void saveBook(MouseEvent mouseEvent) throws ClassNotFoundException {
        this.save();
    }

    @FXML
    private void closeAddBook(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void closeAddBookKey(KeyEvent keyEvent) {
        Stage stage = (Stage) ((Node) keyEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void saveBookKey(KeyEvent keyEvent) throws ClassNotFoundException {
        this.save();
    }

    // funge con mysql non con h2
    private boolean bookExist(String titol) {
        String getByTitol = "SELECT cont(*) FROM libri where titolo = " + "\'" + titol + "\'";
        List<BookSearchModel> bookSearchModelList = new ArrayList<>();
        try {
            Statement statement = connection.prepareStatement(getByTitol);

            getBooksByQuery(getByTitol, bookSearchModelList, statement);
            return !bookSearchModelList.isEmpty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ########################## UTILS METHODS ################################ //

    static void getBooksByQuery(String query, List<BookSearchModel> bookSearchModelList, Statement statement) throws SQLException {
        TableViewBooksController.getAllBooks(query, statement, bookSearchModelList);
    }

    private void save() throws ClassNotFoundException {
        connection = database.getDbConnection();
        String titolo = textTitle.getText();
        String autore = textAuthor.getText();
        String prezzo = this.cleanDoubleValueToString(textPrezzo.getText());
        if ((titolo.isEmpty() || titolo.isBlank()) ||
                (autore.isEmpty() || autore.isBlank()) ||
                (prezzo.isEmpty() || prezzo.isBlank())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Inserisci tutti i dati");
            alert.showAndWait();
        }
        this.setQuery();
        this.insert();
        textTitle.setText("");
        textAuthor.setText("");
        textPrezzo.setText("");

    }

    private String cleanDoubleValueToString(String prezzo) {
        return prezzo.replaceAll("[^\\d.]", "");

    }

    private Double cleanDoubleValue(String prezzo) {
        return Double.parseDouble(prezzo);
    }

    private void savePrestito() throws ClassNotFoundException {
        connection = database.getDbConnection();
        // necessario metodo per aggiornare nella tabella tot ricavi il dato totricavi
        // è anche necessario creare un controllo per verificare l'anno in questione
//        Double prezzo = cleanDoubleValue(textPrezzo.getText());

        String utente = textUtente.getText();
       // String dataPrestito = LocalDate.now().toString();
        String dataFine = datePicker.toString();
       // String disponibile = "Non Disponibile";

        if ((utente.isEmpty() || utente.isBlank()) ||
                (dataFine.isEmpty() || dataFine.isBlank())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Inserisci tutti i dati");
            alert.showAndWait();
        }

        this.setQueryPrestito();
        this.insertPrestito();
        textUtente.setText("");
        datePicker.setChronology(null);

    }

    private void insertPrestito() throws ClassNotFoundException {

        connection = database.getDbConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, textUtente.getText());
            preparedStatement.setString(2, Date.valueOf(LocalDate.now()).toString());
            preparedStatement.setString(3, datePicker.getValue().toString());
            preparedStatement.setString(4, "Non disponibile");
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setQueryPrestito() {
//        if (!update) {
//            query = "INSERT INTO libri ( utente, dataprestito, datafine, disponibile ) VALUES (?, ?, ?, ?, ?)";
//        } else {
            query = "UPDATE libri SET "
                    + " utente = ?,"
                    + " dataprestito = ?,"
                    + " datafine = ?,"
                    + " disponibile = ?"
                    + " WHERE id = " + bookId;
//        }
    }

    private void insert() throws ClassNotFoundException {
        connection = database.getDbConnection();
                try {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, textTitle.getText());
                    preparedStatement.setString(2, "Disponibile");
                    preparedStatement.setString(3, textAuthor.getText());
                    preparedStatement.setString(4, textPrezzo.getText());
                    preparedStatement.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }



    void setUpdate(boolean b) {
        this.update = b;
    }

    private void setQuery() {
        if (!update) {
            query = "INSERT INTO libri (  titolo, disponibile, autore, prezzo ) VALUES (?, ?, ?, ?)";
        } else {
            query = "UPDATE libri SET "
                    + " titolo = ?,"
                    + " disponibile = ?,"
                    + " autore = ?,"
                    + " prezzo = ?"
                    + " WHERE id = " + bookId;
        }
    }

    void setBookTextField(int id, String titolo, String autore, String prezzo) {
        bookId = id;
        textTitle.setText(titolo);
        textAuthor.setText(autore);
        textPrezzo.setText(prezzo);
    }
    void setBookPrestitoTextField(int id, String utente, Date dataFine) {
        bookId = id;
        textUtente.setText(utente);
        // TODO: aggiungere nella scheda noleggiabook un field che mostri scritta la data fine
        datePicker.setChronology(dataFine.toLocalDate().getChronology());
    }

    public void restituitoBookKey(KeyEvent keyEvent) {
        // chiamo il metodo restituito book
    }

    public void restituitoBook(MouseEvent mouseEvent) {
        // Modifico il libro settando nome, cognome, data fine, data inizio a null e disponibile = disponibile;

    }

    public void savePrestitoKey(KeyEvent keyEvent) {
        // se chiamo questo metodo verifico utente e data fine siano stati passati altrimenti blocco
        // se passo procedo modificando il libro aggiungendo SOLO quei dati e settando disponibile = Non disponibile
    }

    public void savePrestitoBook(MouseEvent mouseEvent) throws ClassNotFoundException {
        this.savePrestito();
    }
}
