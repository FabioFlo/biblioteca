package com.example.biblioteca.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public Connection dbLink;

    public Connection getDbConnection() throws ClassNotFoundException {
        String dataName = "biblioteca";
        String dataUser = "root";
        String dataPass = "";
        String url = "jdbc:mysql://localhost:3306/biblioteca";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(url, dataUser, dataPass);
            System.out.println("Connesso al Database");
        } catch (SQLException e) {
            System.out.println("Problemi col db");
            throw new RuntimeException(e);
        }
        return dbLink;
    }


}
