package com.example.biblioteca;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    //    private static final String[] SAMPLE_NAME_DATA = { "John", "Jill", "Jack", "Jerry" };
    public Connection dbLink;

//        public Connection getDbConnection() throws ClassNotFoundException {
//        String dataName = "biblioteca";
//        String dataUser = "root";
//        String dataPass = "";
//        String url = "jdbc:mysql://localhost/biblioteca";
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            dbLink = DriverManager.getConnection(url, dataUser, dataPass);
//            System.out.println("Connesso al Database");
//        } catch (SQLException e) {
//            System.out.println("Problemi col db");
//            JOptionPane.showMessageDialog(null,"Database: " + dataName + " non trovato");
//            throw new RuntimeException(e);
//        }
//        return dbLink;
//    }
    public Connection getDbConnection() throws ClassNotFoundException {
        String dataUser = "sa";
        String dataPass = "";
        String url = "jdbc:h2:tcp://localhost/~/libreria";
        try {
            Class.forName("org.h2.Driver");
            dbLink = DriverManager.getConnection(url, dataUser, dataPass);
            logger.info("**** Connesso al Database");
            if (!schemaExists(dbLink)) {
                createSchema(dbLink);
            }
            return dbLink;
        } catch (SQLException e) {
            logger.warning("**** Problemi col db");
            JOptionPane.showMessageDialog(null, "Impossibile connettersi al database");
            throw new RuntimeException(e);
        }

    }

    public void closeDbConnection() throws SQLException {
        if (dbLink != null && !dbLink.isClosed()) {
            dbLink.close();
            logger.info("**** Connessione chiusa");
        }
    }

    private boolean schemaExists(Connection con) {
        logger.info("**** Verifico che la tabella libri esista");
        try {
            Statement st = con.createStatement();
            st.executeQuery("select count(*) from libri");
            logger.info("**** la tabella esiste");
            return true;
        } catch (SQLException ex) {
            logger.info("**** La tabella libri non esiste, procedo a crearla");
            return false;
        }
    }

    private void createSchema(Connection con) throws SQLException {
        logger.info("**** Creo tabella libri");
        Statement st = con.createStatement();
        String table = "CREATE TABLE libri (ID INT PRIMARY KEY AUTO_INCREMENT," +
                "   titolo VARCHAR(255)," +
                "   autore VARCHAR(255)," +
                "   genere VARCHAR(255))";
        st.executeUpdate(table);
        logger.info("**** Tabella creata");
    }

//    private void populateDatabase(Connection con) throws SQLException {
//        logger.info("Populating database");
//        Statement st = con.createStatement();
//        int i = 1;
//        for (String name: SAMPLE_NAME_DATA) {
//            st.executeUpdate("insert into libri values(i,'" + name + "')");
//            i++;
//        }
//        logger.info("Populated database");
//    }

//    private void fetchNamesFromDatabaseToListView(ListView listView) {
//        try (Connection con = getDbConnection()) {
//            if (!schemaExists(con)) {
//                createSchema(con);
//                populateDatabase(con);
//            }
//            listView.setItems(fetchNames(con));
//        } catch (SQLException | ClassNotFoundException ex) {
//            logger.log(Level.SEVERE, null, ex);
//        }
//    }

}
