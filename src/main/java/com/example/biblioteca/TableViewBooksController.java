package com.example.biblioteca;

import com.example.biblioteca.helpers.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TableViewBooksController implements Initializable {

//    @FXML
//    private TableView<String> booksList;

    @FXML
    ListView<String> bookListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Database database = new Database();
        Connection connection = database.getDbConnection();

        String connectQuery = "SELECT titolo,autore FROM LIBRI";

        try {
            Statement statement = connection.createStatement();
            ResultSet queryOut = statement.executeQuery(connectQuery);

            while (queryOut.next()) {
                Long id = queryOut.getLong("id");
                String titolo = queryOut.getString("titolo");
                String autore = queryOut.getString("autore");
                String tipologia = queryOut.getString("tipologia");
                String listOut = titolo + "\"" + autore + "\"";

                bookListView.getItems().add(listOut);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


//    @FXML
//    private Label welcomeText;

//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
}