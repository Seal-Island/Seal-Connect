package com.focamacho.sealconnect.data;

import com.focamacho.sealconnect.SealConnect;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

import static com.focamacho.sealconnect.SealConnect.config;

@SuppressWarnings("unused")
public class DatabaseConnector {

    private final File dataFile = new File("./plugins/SealConnect/db_sealconnect.db");

    private Connection connection;

    {
        try {
            if(config.mysql.enableMysql) Class.forName("com.mysql.jdbc.Driver");
            else Class.forName("org.sqlite.JDBC");
        } catch (Exception ignored) {}

        //Criar arquivo de DB caso não exista
        if(!config.mysql.enableMysql && !dataFile.exists()) {
            try {
                boolean mkdirs = dataFile.getParentFile().mkdirs();
                boolean nf = dataFile.createNewFile();
            } catch (IOException e) {
                SealConnect.logger.severe("Não foi possível criar o arquivo para o banco de dados do Seal Connect.");
                e.printStackTrace();
            }
        }

        if(connect()) {
            try {
                //Tabela de usuários
                String userTableSql = "CREATE TABLE IF NOT EXISTS sealconnect_accounts (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "user_uuid VARCHAR(36) NOT NULL," +
                        "user_discord_id LONG NOT NULL," +
                        "user_description VARCHAR(180)," +
                        "user_last_login LONG," +
                        "user_name VARCHAR(16) NOT NULL)";

                connection.createStatement().execute(userTableSql);
            } catch (SQLException e) {
                SealConnect.logger.severe("Ocorreu um erro ao tentar criar as tabelas no banco de dados.");
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
            if(config.mysql.enableMysql) {
                String databaseUrl = "jdbc:mysql://" + config.mysql.mysqlAddress + "/" + config.mysql.mysqlDatabase + "?characterEncoding=utf8";
                if(!config.mysql.advanced.mysqlConnectionUrl.isEmpty()) databaseUrl = config.mysql.advanced.mysqlConnectionUrl;
                connection = DriverManager.getConnection(databaseUrl, config.mysql.mysqlUser, config.mysql.mysqlPassword);
            } else {
                connection = DriverManager.getConnection("jdbc:sqlite:" + dataFile.getAbsolutePath());
            }
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

    public boolean insertUser(UUID uuid, String discordId, String description, String name, long lastLogin) {
        String sql = "INSERT INTO sealconnect_accounts(" +
                "user_uuid," +
                "user_discord_id," +
                "user_description," +
                "user_name," +
                "user_last_login" +
                ") VALUES(?,?,?,?,?);";

        try {
            if(connect()) {
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, uuid.toString());
                statement.setLong(2, Long.parseLong(discordId));
                statement.setString(3, description);
                statement.setString(4, name);
                statement.setLong(5, lastLogin);

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
        String sql = "DELETE FROM sealconnect_accounts " +
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
        String sql = "UPDATE sealconnect_accounts " +
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
        String sql = "UPDATE sealconnect_accounts " +
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

    public void updateLastLogin(UUID uuid, long lastLogin) {
        String sql = "UPDATE sealconnect_accounts " +
                "SET user_last_login = ? " +
                "WHERE user_uuid = ?;";

        try {
            if(connect()) {
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setLong(1, lastLogin);
                statement.setString(2, uuid.toString());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar atualizar a data de último login de um usuário no banco de dados.");
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public ResultSet getAllUsers() {
        try {
            if(connect()) return connection.createStatement().executeQuery("SELECT * FROM sealconnect_accounts");
            return null;
        } catch(SQLException e) {
            SealConnect.logger.severe("Ocorreu um erro ao tentar obter os usuário no banco de dados.");
            e.printStackTrace();
            return null;
        }
    }

}
