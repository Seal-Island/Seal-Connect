package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.discord.utils.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;
import java.util.UUID;

public class DescriptionCommand extends Command {

    public DescriptionCommand(String... aliases) {
        super(aliases);
    }

    @Override
    public void execute(Message message) {
        String[] args = getArgs(message);
        Map.Entry<UUID, String> connectedAccount = DataHandler.getConnectedAccountFromDiscordID(message.getAuthor().getId());

        if(connectedAccount == null) {
            message.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.connect.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.connect.notconnected")))
                    .setColor(SealConnect.config.color)
                    .setThumbnail(TextUtils.getString(SealConnect.config.erroredImage))
                    .build()).queue();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(args[i]);
            if(i < args.length - 1) stringBuilder.append(" ");
        }
        String description = stringBuilder.toString();

        if(description.length() > 180) {
            message.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.description.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.description.description")))
                    .setColor(SealConnect.config.color)
                    .setThumbnail(TextUtils.getString(SealConnect.config.erroredImage))
                    .build()).queue();
            return;
        }

        DataHandler.getProfileData(message.getAuthor().getId()).setDescription(description);
        message.getChannel().sendMessage(MinecraftCommand.getProfileMessage(message.getGuild(), connectedAccount)).queue();
    }

}
