package com.focamacho.sealconnect.discord;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.discord.command.*;
import com.focamacho.sealconnect.discord.listener.CommandListener;
import com.focamacho.sealconnect.discord.listener.SuggestionListener;
import com.focamacho.seallibrary.permission.PermissionHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static com.focamacho.sealconnect.SealConnect.config;

public class DiscordSealConnect {

    public static JDA jda;

    public static void init() {
        SealConnect.logger.info("Tentando conexão com o bot...");
        try {
            jda = JDABuilder.createDefault(config.botToken).addEventListeners(new CommandListener(), new SuggestionListener()).build();
            SealConnect.logger.info("Conexão com o bot concluída com sucesso!");
        } catch(Exception e) {
            SealConnect.logger.severe("Um erro ocorreu ao tentar se conectar ao bot.");
            e.printStackTrace();
            return;
        }

        jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.of(Activity.ActivityType.DEFAULT, config.activityPresence));

        new ConnectCommand(config.connectAliases);
        new MinecraftCommand(config.minecraftAliases);
        new DescriptionCommand(config.descriptionAliases);
        new ServerCommand(config.serverAliases);
        new SuggestCommand(config.suggestAliases);
    }

    public static void updateRoles(ProxiedPlayer player) {
        if(PermissionHandler.hasPermission(player.getUniqueId(), "*")) return;

        Guild guild = config.guildId.isEmpty() ? jda.getGuilds().get(0) : jda.getGuildById(config.guildId);

        if(guild == null) return;

        if(DataHandler.getConnectedAccountFromUUID(player.getUniqueId()) != null) {
            if(!config.nitroRoleName.isEmpty() || config.linkedRoles.size() > 0) {
                guild.retrieveMemberById(DataHandler.connectedAccounts.get(player.getUniqueId())).queue(member -> {
                    config.linkedRoles.forEach((perm, role) -> {
                        Role rl = guild.getRoleById(role);
                        if (rl == null) return;

                        if(PermissionHandler.hasPermission(player.getUniqueId(), perm)) {
                            if(!member.getRoles().contains(rl)) guild.addRoleToMember(member, rl).queue();
                        } else {
                            if(member.getRoles().contains(rl)) guild.removeRoleFromMember(member, rl).queue();
                        }
                    });

                    if(!config.nitroRoleName.isEmpty()) {
                        if(member.getTimeBoosted() != null) {
                            PermissionHandler.addGroup(player.getUniqueId(), config.nitroRoleName);
                        } else {
                            PermissionHandler.removeGroup(player.getUniqueId(), config.nitroRoleName);
                        }
                    }
                }, ignored -> {});
            }
        }
    }

}