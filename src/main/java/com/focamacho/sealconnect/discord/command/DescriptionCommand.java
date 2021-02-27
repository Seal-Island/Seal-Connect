package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.AccountSealConnect;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.util.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import static com.focamacho.sealconnect.SealConnect.config;

public class DescriptionCommand extends Command {

    public DescriptionCommand(String... aliases) {
        super(aliases);
    }

    @Override
    public void execute(Message message) {
        String[] args = getArgs(message);
        AccountSealConnect connectedAccount = DataHandler.getConnectedAccountFromDiscordID(message.getAuthor().getId());

        if(connectedAccount == null) {
            message.reply(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.connect.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.connect.notconnected")))
                    .setColor(config.color)
                    .setThumbnail(TextUtils.getString(config.erroredImage))
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
            message.reply(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.description.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.description.description")))
                    .setColor(config.color)
                    .setThumbnail(TextUtils.getString(config.erroredImage))
                    .build()).queue();
            return;
        }

        connectedAccount.setDescription(description);
        MessageEmbed embed = MinecraftCommand.getProfileMessage(message.getGuild(), connectedAccount);
        message.reply(embed).queue();
    }

}
