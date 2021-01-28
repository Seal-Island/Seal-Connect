package com.focamacho.sealconnect.discord;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.discord.command.*;
import com.focamacho.sealconnect.discord.listener.CommandListener;
import com.focamacho.sealconnect.discord.listener.SuggestionListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class DiscordSealConnect {

    public static JDA jda;

    public static void init() {
        SealConnect.logger.info("Tentando conexão com o bot...");
        try {
            jda = JDABuilder.createDefault(SealConnect.config.botToken).addEventListeners(new CommandListener(), new SuggestionListener()).build();
            SealConnect.logger.info("Conexão com o bot concluída com sucesso!");
        } catch(Exception e) {
            SealConnect.logger.severe("Um erro ocorreu ao tentar se conectar ao bot.");
            e.printStackTrace();
            return;
        }

        jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.of(Activity.ActivityType.DEFAULT, SealConnect.config.activityPresence));

        new ConnectCommand(SealConnect.config.connectAliases);
        new MinecraftCommand(SealConnect.config.minecraftAliases);
        new DescriptionCommand(SealConnect.config.descriptionAliases);
        new ServerCommand(SealConnect.config.serverAliases);
        new SuggestCommand(SealConnect.config.suggestAliases);
    }

    public static void updateRoles(ProxiedPlayer player) {
        Guild guild = SealConnect.config.guildId.isEmpty() ? jda.getGuilds().get(0) : jda.getGuildById(SealConnect.config.guildId);

        if(guild == null) return;

        if(DataHandler.getConnectedAccountFromUUID(player.getUniqueId()) != null) {
            SealConnect.config.linkedRoles.forEach((perm, role) -> {
                Role rl = guild.getRoleById(role);
                if(rl == null) return;

                guild.retrieveMemberById(DataHandler.connectedAccounts.get(player.getUniqueId())).queue(member -> {
                    if(player.hasPermission(perm)) {
                        if(!member.getRoles().contains(rl)) guild.addRoleToMember(member, rl).queue();
                    } else {
                        if(member.getRoles().contains(rl)) guild.removeRoleFromMember(member, rl).queue();
                    }
                });
            });
        }
    }

}