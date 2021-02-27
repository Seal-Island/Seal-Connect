package com.focamacho.sealconnect.data;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.discord.DiscordSealConnect;
import com.focamacho.seallibrary.permission.PermissionHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.focamacho.sealconnect.SealConnect.config;

public class DataHandler {

    public static final DatabaseConnector handler = new DatabaseConnector();

    public static List<AccountSealConnect> connectedAccounts = new ArrayList<>();
    public static List<KeySealConnect> keys = new ArrayList<>();

    public static boolean init() {
        SealConnect.logger.info("Iniciando carregamento dos dados...");

        connectedAccounts.clear();
        keys.clear();

        try {
            if (handler.connect()) {
                ResultSet users = handler.getAllUsers();
                while(users.next()) {
                    connectedAccounts.add(new AccountSealConnect(
                            UUID.fromString(users.getString("user_uuid")),
                            users.getString("user_name"),
                            String.valueOf(users.getLong("user_discord_id")),
                            users.getString("user_description")));
                }

                SealConnect.logger.info("Carregamento de dados concluido com sucesso!");
                SealConnect.logger.info("Dados de " + connectedAccounts.size() + " contas foram carregados.");
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            handler.disconnect();
        }

        SealConnect.logger.severe("O plugin será desligado devido aos erros com o banco de dados.");
        return false;
    }

    public static void addUser(UUID uuid, String discord, String description, String name) {
        if(handler.insertUser(uuid, discord, description, name)) {
            connectedAccounts.add(new AccountSealConnect(uuid, name, discord, description));
        }
    }

    public static void removeUser(AccountSealConnect account) {
        if(handler.deleteUser(account.getUuid())) {
            if(!config.nitroRoleName.isEmpty() || config.linkedRoles.size() > 0) {
                if(DiscordSealConnect.jda.getGuilds().size() > 0) {
                    Guild guild = DiscordSealConnect.jda.getGuilds().get(0);

                    guild.retrieveMemberById(account.getDiscord()).queue(member -> {
                        config.linkedRoles.forEach((perm, role) -> {
                            Role rl = guild.getRoleById(role);
                            if (rl == null) return;
                            if (member.getRoles().contains(rl)) guild.removeRoleFromMember(member, rl).queue();
                        });

                        if (!config.nitroRoleName.isEmpty()) {
                            PermissionHandler.removeGroup(account.getUuid(), config.nitroRoleName);
                        }
                    }, ignored -> {});
                }
            }

            connectedAccounts.remove(account);
        }
    }

    public static KeySealConnect getKey(String key) {
        for (KeySealConnect sealKey : keys) {
            if (sealKey.getKey().equalsIgnoreCase(key)) return sealKey;
        }
        return null;
    }

    public static KeySealConnect getKey(UUID uuid) {
        for (KeySealConnect sealKey : keys) {
            if (sealKey.getUuid().equals(uuid)) return sealKey;
        }
        return null;
    }

    public static AccountSealConnect getConnectedAccountFromDiscordID(String id) {
        for(AccountSealConnect account : connectedAccounts) {
            if(account.getDiscord().equalsIgnoreCase(id)) {
                return account;
            }
        }
        return null;
    }

    public static AccountSealConnect getConnectedAccountFromUUID(UUID id) {
        for(AccountSealConnect account : connectedAccounts) {
            if(account.getUuid().equals(id)) {
                return account;
            }
        }
        return null;
    }

    public static AccountSealConnect getConnectedAccountFromName(String name) {
        for(AccountSealConnect account : connectedAccounts) {
            if(account.getName().equalsIgnoreCase(name)) {
                return account;
            }
        }
        return null;
    }

    public static AccountSealConnect getConnectedAccountFromAny(String any) {
        AccountSealConnect account;
        if((account = getConnectedAccountFromDiscordID(any)) != null) return account;
        else if ((account = getConnectedAccountFromName(any)) != null) return account;
        else {
            try {
                if ((account = getConnectedAccountFromUUID(UUID.fromString(any))) != null) return account;
            } catch(IllegalArgumentException ignored) {}
        }
        return null;
    }

}
