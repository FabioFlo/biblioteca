package com.example.biblioteca;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
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
    private TextField textGenere;

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
        String genere = textGenere.getText();
        if ((titolo.isEmpty() || titolo.isBlank()) ||
                (autore.isEmpty() || autore.isBlank()) ||
                (genere.isEmpty() || genere.isBlank())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Inserisci tutti i dati");
            alert.showAndWait();
        }
//        else if (bookExist(titolo)) {
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText("Titolo già presente in archivio");
//            alert.showAndWait();
//        } else {
        this.setQuery();
        this.insert();
        textTitle.setText("");
        textAuthor.setText("");
        textGenere.setText("");
//    }

}

    private void insert() throws ClassNotFoundException {
        connection = database.getDbConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, textTitle.getText());
            preparedStatement.setString(2, textAuthor.getText());
            preparedStatement.setString(3, textGenere.getText());
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
            query = "INSERT INTO libri ( titolo, autore, genere ) VALUES (?, ?, ?)";
        } else {
            query = "UPDATE libri SET "
                    + " titolo = ?,"
                    + " autore = ?,"
                    + " genere = ?"
                    + " WHERE id = " + bookId;
        }
    }

    void setTextField(int id, String titolo, String autore, String genere) {
        bookId = id;
        textTitle.setText(titolo);
        textAuthor.setText(autore);
        textGenere.setText(genere);
    }
}
