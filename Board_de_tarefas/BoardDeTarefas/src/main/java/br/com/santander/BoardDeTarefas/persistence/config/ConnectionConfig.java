package br.com.santander.BoardDeTarefas.persistence.config;


import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ConnectionConfig {

    public static Connection getConnection() throws SQLException {
        var url = "jdbc:mysql://localhost/boards";
        var user = "root";
        var passaword = "1234";
        var connection = DriverManager.getConnection(url,user,passaword);
        connection.setAutoCommit(false);
        return connection;
    }
}
