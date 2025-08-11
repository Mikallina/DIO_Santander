package br.com.santander.boarddetarefas.persistence.config;


import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ConnectionConfig {

    private static final String PROPERTIES_FILE = "/application.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        try (InputStream input = ConnectionConfig.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Arquivo de configuração não encontrado: " + PROPERTIES_FILE);
            }

            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar propriedades do banco de dados", e);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        return connection;
    }
}
