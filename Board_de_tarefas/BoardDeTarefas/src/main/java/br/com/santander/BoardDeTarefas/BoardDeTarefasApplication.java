package br.com.santander.BoardDeTarefas;

import br.com.santander.BoardDeTarefas.persistence.migration.MigrationStrategy;
import br.com.santander.BoardDeTarefas.ui.MainMenu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

import static br.com.santander.BoardDeTarefas.persistence.config.ConnectionConfig.getConnection;

@SpringBootApplication
public class BoardDeTarefasApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(BoardDeTarefasApplication.class, args);

        try (var connection = getConnection()) {
            new MigrationStrategy(connection).executeMigration();
        }
        new MainMenu().execute();
    }

}
