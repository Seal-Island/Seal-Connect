package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.AccountSealConnect;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.discord.util.Emojis;
import com.focamacho.sealconnect.util.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;

import static com.focamacho.sealconnect.SealConnect.config;

public class SuggestCommand extends Command {

    public SuggestCommand(String... aliases) {
        super(aliases);
    }

    @Override
    public void execute(Message message) {
        if(config.suggestionsChannel.isEmpty()) return;

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

        if(args.length < 1) {
            message.reply(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.missing")))
                    .setColor(config.color)
                    .setThumbnail(TextUtils.getString(config.erroredImage))
                    .build()).queue();
            return;
        }

        StringBuilder suggestion = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            suggestion.append(args[i]);
            if(i < args.length - 1) suggestion.append(" ");
        }

        TextChannel channel = message.getGuild().getTextChannelById(config.suggestionsChannel);
        if(channel != null) {
            channel.sendMessage(getSuggestionMessage(message.getGuild(), connectedAccount, suggestion.toString())).queue(msg -> {
                message.reply(new EmbedBuilder()
                        .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.title")))
                        .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.success")))
                        .setColor(config.color)
                        .setThumbnail(TextUtils.getString(config.successfulImage))
                        .build()).queue();

                if(Emojis.animatedCheckmark == null) msg.addReaction("✅").queue();
                else msg.addReaction(Emojis.animatedCheckmark).queue();

                if(Emojis.animatedCross == null) msg.addReaction("❌").queue();
                else msg.addReaction(Emojis.animatedCross).queue();
            });
        } else {
            message.reply(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.channelmissing"))).queue();
        }
    }

    public static MessageEmbed getSuggestionMessage(Guild guild, AccountSealConnect connectedAccount, String suggestion) {
        String nick = connectedAccount.getName();

        return new EmbedBuilder()
                .setAuthor(nick, null, "https://minotar.net/helm/" + nick + "/100.png")
                .setThumbnail("https://minotar.net/helm/" + nick + "/100.png")
                .setFooter(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.footer")), guild.getIconUrl())
                .setTimestamp(OffsetDateTime.now())
                .setColor(config.color)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.suggestedby")), "<@!" + connectedAccount.getDiscord() + ">", true)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.servernick")), nick, true)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.suggestion.suggestion")), suggestion, false)
                .build();
    }
}
