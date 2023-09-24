package com.example.biblioteca;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableViewBooksController implements Initializable {

    // ########## DATABASE ATTRIBUTES ########## //
    String query = null;
    Connection connection;
    Database database = new Database();
    PreparedStatement preparedStatement;

    // ################################## TABLE ATTRIBUTES ############################ //
    @FXML
    private TableView<BookSearchModel> booksTable;
    @FXML
    private TableColumn<BookSearchModel, Integer> booksColumnID;
    @FXML
    private TableColumn<BookSearchModel, String> booksColumnTITLE;
    @FXML
    private TableColumn<BookSearchModel, String> booksColumnAUTHOR;
    @FXML
    private TableColumn<BookSearchModel, Double> booksColumnPREZZO;
    @FXML
    private TableColumn<BookSearchModel, String> booksColumnDISPONIBILE;
    @FXML
    private TableColumn<BookSearchModel, String> booksColumnUTENTE;
    @FXML
    private TableColumn<BookSearchModel, Date> booksColumnDATAPRES;
    @FXML
    private TableColumn<BookSearchModel, Date> booksColumnDATAFINE;

    // ################################## SEARCH TEXT BAR ############################ //
    @FXML
    private TextField textField;

    ObservableList<BookSearchModel> bookSearchModels = FXCollections.observableArrayList();
    BookSearchModel book = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            connection = database.getDbConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        listBooks();
    }

    public void listBooks() {
        String getAllQuery = "SELECT * FROM libri";

        try {
            Statement statement = connection.createStatement();

            getAllBooks(getAllQuery, statement, bookSearchModels);
            booksColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
            booksColumnTITLE.setCellValueFactory(new PropertyValueFactory<>("titolo"));
            booksColumnAUTHOR.setCellValueFactory(new PropertyValueFactory<>("autore"));
            booksColumnPREZZO.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
            booksColumnDISPONIBILE.setCellValueFactory(new PropertyValueFactory<>("disponibile"));
            booksColumnUTENTE.setCellValueFactory(new PropertyValueFactory<>("utente"));
            booksColumnDATAPRES.setCellValueFactory(new PropertyValueFactory<>("dataPrestito"));
            booksColumnDATAFINE.setCellValueFactory(new PropertyValueFactory<>("dataFine"));

            booksTable.setItems(bookSearchModels);

            FilteredList<BookSearchModel> filteredList = new FilteredList<>(bookSearchModels, bookSearchModel -> true);

            textField.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(bookSearchModel -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (bookSearchModel.getTitolo().toLowerCase().contains(searchKeyword)) {
                    return true; // La ricerca ha prodotto un risultrato
                } else return (bookSearchModel.getAutore().toLowerCase().contains(searchKeyword));


            }));

            SortedList<BookSearchModel> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(booksTable.comparatorProperty());

            booksTable.setItems(sortedList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void getAllBooks(String getAllQuery, Statement statement, List<BookSearchModel> bookSearchModels) throws SQLException {
        ResultSet queryOut = statement.executeQuery(getAllQuery);
        while (queryOut.next()) {
            int id = queryOut.getInt("id");
            String disponibile = queryOut.getString("disponibile");
            String titolo = queryOut.getString("titolo");
            String autore = queryOut.getString("autore");
            String prezzo = queryOut.getString("prezzo");
            String utente = queryOut.getString("utente");
            Date dataPrestito = queryOut.getDate("dataprestito");
            Date dataFine = queryOut.getDate("datafine");


            bookSearchModels.add(new BookSearchModel(id, disponibile,titolo, autore,prezzo,utente,dataPrestito,dataFine));
        }
    }

    @FXML
    public void getAddBookView(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addBook.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    @FXML
    public void close(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        database.closeDbConnection();
        stage.close();

    }

    public void getAllBooks(MouseEvent mouseEvent) {
        bookSearchModels.clear();
        listBooks();
    }

    public void getAllBooks() {
        bookSearchModels.clear();
        listBooks();
    }

    public void updateBook(MouseEvent mouseEvent) {

        book = booksTable.getSelectionModel().getSelectedItem();
        if (book != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("addBook.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(TableViewBooksController.class.getName()).log(Level.SEVERE, null, ex);
            }

            AddBookController addBookController = loader.getController();
            addBookController.setUpdate(true);
            addBookController.setBookTextField(book.getId(), book.getTitolo(),
                    book.getAutore(), book.getPrezzo());
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Nessun libro selezionato");
        }

    }

    public void deleteBook(MouseEvent mouseEvent) {
        book = booksTable.getSelectionModel().getSelectedItem();
        if (book == null) {
            JOptionPane.showMessageDialog(null, "Nessun libro selezionato");
        } else {
            int input = JOptionPane.showConfirmDialog(null, "Procedere con l'eliminazione?");
            switch (input) {
                case 0:
                    try {

                        database = new Database();
                        query = "DELETE FROM `libri` WHERE id  = " + book.getId();
                        connection = database.getDbConnection();
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.execute();
                        getAllBooks();


                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 1:
                case 2:
                    getAllBooks();
                    break;
                default:
            }
        }
    }


    public void getTotRaccoltoView(MouseEvent mouseEvent) {
    }

    public void noleggiaBook(MouseEvent mouseEvent) {
        book = booksTable.getSelectionModel().getSelectedItem();
        if (book != null ) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("noleggiaBook.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(TableViewBooksController.class.getName()).log(Level.SEVERE, null, ex);
            }

            AddBookController addBookController = loader.getController();
            addBookController.setUpdate(true);
            if (book.utente != null && book.dataPrestito != null && book.dataFine != null) {
                addBookController.setBookPrestitoTextField(book.getId(),book.getUtente(), book.getDataFine());
            }
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Nessun libro selezionato");
        }
    }
}

//    public void addDell() {
//        //add cell of button edit
//        Callback<TableColumn<BookSearchModel, String>, TableCell<BookSearchModel, String>> cellFoctory = (TableColumn<BookSearchModel, String> param) -> {
//            // make cell containing buttons
//            return new TableCell<BookSearchModel, String>() {
//                @Override
//                public void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    //that cell created only on non-empty rows
//                    if (empty) {
//                        setGraphic(null);
//                        setText(null);
//
//                    } else {
//
//                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
//                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
//
//                        deleteIcon.setStyle(
//                                " -fx-cursor: hand ;"
//                                        + "-glyph-size:28px;"
//                                        + "-fx-fill:#ff1744;"
//                        );
//                        editIcon.setStyle(
//                                " -fx-cursor: hand ;"
//                                        + "-glyph-size:28px;"
//                                        + "-fx-fill:#00E676;"
//                        );
//                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
//
//                            try {
//                                Database database = new Database();
//                                book = booksTable.getSelectionModel().getSelectedItem();
//                                query = "DELETE FROM `libri` WHERE id  = " + book.getId();
//                                connection = database.getDbConnection();
//                                preparedStatement = connection.prepareStatement(query);
//                                preparedStatement.execute();
//                                getAllBooks();
//
//                            } catch (ClassNotFoundException e) {
//                                throw new RuntimeException(e);
//                            } catch (SQLException e) {
//                                throw new RuntimeException(e);
//                            }
//
//
//                        });
//                        editIcon.setOnMouseClicked((MouseEvent event) -> {
//
//                            book = booksTable.getSelectionModel().getSelectedItem();
//                            FXMLLoader loader = new FXMLLoader();
//                            loader.setLocation(getClass().getResource("addBook.fxml"));
//                            try {
//                                loader.load();
//                            } catch (IOException ex) {
//                                Logger.getLogger(TableViewBooksController.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//
//                            AddBookController addBookBookController = loader.getController();
//                            addBookBookController.setUpdate(true);
//                            addBookBookController.setTextField(book.getId(), book.getTitolo(),
//                                    book.getAutore(), book.getGenere());
//                            Parent parent = loader.getRoot();
//                            Stage stage = new Stage();
//                            stage.setScene(new Scene(parent));
//                            stage.initStyle(StageStyle.UTILITY);
//                            stage.show();
//
//
//                        });
//
//                        HBox managebtn = new HBox(editIcon, deleteIcon);
//                        managebtn.setStyle("-fx-alignment:center");
//                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
//                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
//
//                        setGraphic(managebtn);
//
//                        setText(null);
//
//                    }
//                }
//
//            };
//        };
//    }


