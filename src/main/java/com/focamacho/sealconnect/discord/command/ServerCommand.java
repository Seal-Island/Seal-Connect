package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.util.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class ServerCommand extends Command {

    public ServerCommand(String... aliases) {
        super(aliases);
    }

    @Override
    public boolean canUseCommand(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }

    @Override
    public void execute(Message message) {
        Guild guild = message.getGuild();

        message.reply(new EmbedBuilder()
                .setAuthor(guild.getName(), null, guild.getIconUrl())
                .setThumbnail(guild.getIconUrl())
                .setColor(SealConnect.config.color)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.server.players")), "" + DataHandler.savedNames.size(), true)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.server.connected")), "" + DataHandler.connectedAccounts.size(), true)
                .build()).queue();
    }

}