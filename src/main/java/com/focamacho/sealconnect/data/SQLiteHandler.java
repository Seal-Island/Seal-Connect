package com.focamacho.sealconnect.data;

import com.focamacho.sealconnect.SealConnect;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class SQLiteHandler {

    private final File dataFile = new File("./plugins/SealConnect/db_sealconnect.db");

    private Connection connection;

    {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception ignored) {}

        //Criar arquivo de DB caso não exista
        if(!dataFile.exists()) {
            try {
                boolean mkdirs = dataFile.getParentFile().mkdirs();
                boolean nf = dataFile.createNewFile();

                if(connect()) {
                    try {
                        //Tabela de usuários
                        String sql = "CREATE TABLE IF NOT EXISTS tbl_accounts (" +
                                "user_uuid STRING PRIMARY KEY NOT NULL," +
                                "user_discord_id STRING NOT NULL," +
                                "user_description STRING," +
                                "user_name STRING NOT NULL" +
                                ");";
                        connection.createStatement().execute(sql);
                    } catch (SQLException e) {
                        SealConnect.logger.severe("Ocorreu um erro ao tentar criar as tabelas no banco de dados.");
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                SealConnect.logger.severe("Não foi possível criar o arquivo para o banco de dados do Seal Connect.");
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }
    }

    @SuppressWarnings("unused")
    public boolean connect() {
        try {
            if(connection != null && !connection.isClosed()) return true;
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFile.getAbsolutePath());
            return true;
        } catch (SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar se conectar ao banco de dados.");
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar se desconectar do banco de dados.");
            e.printStackTrace();
        }
    }

    public boolean insertUser(UUID uuid, String discordId, String description, String name) {
        String sql = "INSERT INTO tbl_accounts(" +
                "user_uuid," +
                "user_discord_id," +
                "user_description," +
                "user_name" +
                ") VALUES(?,?,?,?);";

        try {
            if(connect()) {
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, uuid.toString());
                statement.setString(2, discordId);
                statement.setString(3, description);
                statement.setString(4, name);

                int result = statement.executeUpdate();
                return result == 1;
            }
            return false;
        } catch (SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar inserir o usuário no banco de dados.");
            e.printStackTrace();
            return false;
        } finally {
            disconnect();
        }
    }

    public boolean deleteUser(UUID uuid) {
        String sql = "DELETE FROM tbl_accounts " +
                "WHERE user_uuid = ?;";

        try {
            if(connect()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
                return true;
            }
            return false;
        } catch (SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar remover um usuário do banco de dados.");
            e.printStackTrace();
            return false;
        } finally {
            disconnect();
        }
    }

    public void updateName(UUID uuid, String name) {
        String sql = "UPDATE tbl_accounts " +
                     "SET user_name = ? " +
                     "WHERE user_uuid = ?;";

        try {
            if(connect()) {
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, name);
                statement.setString(2, uuid.toString());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar atualizar o nome de um usuário no banco de dados.");
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public void updateDescription(UUID uuid, String description) {
        String sql = "UPDATE tbl_accounts " +
                "SET user_description = ? " +
                "WHERE user_uuid = ?;";

        try {
            if(connect()) {
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, description);
                statement.setString(2, uuid.toString());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar atualizar a descrição de um usuário no banco de dados.");
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public ResultSet getAllUsers() {
        try {
            if(connect()) return connection.createStatement().executeQuery("SELECT * FROM tbl_accounts");
            return null;
        } catch(SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar obter os usuário no banco de dados.");
            e.printStackTrace();
            return null;
        }
    }

}
