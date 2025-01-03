package com.fobov.fobov.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class DatabaseInitializer implements CommandLineRunner {
    @Autowired
    Environment env;
    @Autowired
    private DataSource dataSource; // Autowire the DataSource

    @Override
    public void run(String... args) throws Exception {
        String dbFilePath = "src/main/resources/database/" + env.getProperty("banco") + ".db";
        File dbFile = new File(dbFilePath);

        if (dbFile.exists()) dbFile.delete();

        try (Connection connection = dataSource.getConnection()) {
            executeSqlFile(connection, "/database/create-tables.sql");
            executeSqlFile(connection, "/database/insert-values.sql");
        }
    }

    private void executeSqlFile(Connection connection, String filePath) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(filePath), StandardCharsets.UTF_8))) {
            StringBuilder sql = new StringBuilder();
            String line;
            boolean inMultiLineComment = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("--")) {
                    continue; // Ignore single-line comments
                }
                if (line.startsWith("/*")) {
                    inMultiLineComment = true;
                }
                if (inMultiLineComment) {
                    if (line.endsWith("*/")) {
                        inMultiLineComment = false;
                    }
                    continue; // Ignore lines within multi-line comments
                }
                if (!line.isEmpty()) {
                    sql.append(line).append("\n");
                }
            }

            String[] statements = sql.toString().split(";");
            try (Statement statement = connection.createStatement()) {
                for (String stmt : statements) {
                    if (!stmt.trim().isEmpty()) {
                        statement.execute(stmt.trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
