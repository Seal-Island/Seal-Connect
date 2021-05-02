package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.AccountSealConnect;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.util.TextUtils;
import com.focamacho.seallibrary.chat.ChatHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

import static com.focamacho.sealconnect.SealConnect.config;

public class MinecraftCommand extends Command {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public MinecraftCommand(String... aliases) {
        super(aliases);
        dateFormat.setTimeZone(TimeZone.getTimeZone(config.timezone));
    }

    @Override
    public void execute(Message message) {
        String[] args = getArgs(message);

        if(args.length == 1) {
            AccountSealConnect connectedAccount = DataHandler.getConnectedAccountFromAny(args[0].replace("<", "").replace("@", "").replace("!", "").replace(">", ""));

            if(connectedAccount == null) {
                message.reply(new EmbedBuilder()
                        .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.minecraft")))
                        .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.noaccount")))
                        .setColor(config.color)
                        .setThumbnail(TextUtils.getString(config.erroredImage))
                        .build()).queue();
                return;
            }

            getProfileMessage(message.getGuild(), connectedAccount).whenComplete((msg, ignore) -> message.reply(msg).queue());
            return;
        }

        AccountSealConnect connectedAccount = DataHandler.getConnectedAccountFromDiscordID(message.getAuthor().getId());

        if(connectedAccount == null) {
            redirectCommand(message, ConnectCommand.class);
            return;
        }

        getProfileMessage(message.getGuild(), connectedAccount).whenComplete((msg, ignore) -> message.reply(msg).queue());
    }

    public static CompletableFuture<MessageEmbed> getProfileMessage(Guild guild, AccountSealConnect connectedAccount) {
        CompletableFuture<MessageEmbed> toReturn = new CompletableFuture<>();

        String nick = connectedAccount.getName();
        String description = connectedAccount.getDescription();
        if(description.isEmpty()) description = TextUtils.getString(SealConnectLang.getLang("discord.description.default"));

        String finalDescription = description;
        ChatHandler.getPrefix(connectedAccount.getUuid()).whenComplete((prefix, ignore) -> {
            if(prefix == null) prefix = TextUtils.getString(SealConnectLang.getLang("discord.description.defaultrank"));

            StringBuilder formattedPrefix = new StringBuilder();

            boolean skipNext = false;
            for(char c : prefix.toCharArray()) {
                if(skipNext) {
                    skipNext = false;
                } else {
                    if(c == '&' || c == '§') {
                        skipNext = true;
                    } else {
                        formattedPrefix.append(c);
                    }
                }
            }

            int color = config.color;

            try {
                Member member = guild.retrieveMemberById(connectedAccount.getDiscord()).complete();
                if (member != null) {
                    if (config.minecraftColorId.isEmpty() || member.getRoles().contains(guild.getRoleById(config.minecraftColorId)))
                        color = member.getColorRaw();
                }
            } catch(Exception ignored) {}

            toReturn.complete(new EmbedBuilder()
                    .setAuthor(nick, null, "https://minotar.net/helm/" + nick + "/100.png")
                    .setDescription(finalDescription)
                    .setThumbnail("https://minotar.net/helm/" + nick + "/100.png")
                    .setFooter(guild.getName(), guild.getIconUrl())
                    .setColor(color)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.nickname")), nick, true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.rank")), formattedPrefix.toString(), true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.lastlogin")), connectedAccount.getLastLogin() == 0 ? "???" : dateFormat.format(new Date(connectedAccount.getLastLogin())).replace("-", "/"), true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.discord")), "<@!" + connectedAccount.getDiscord() + ">", true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.discordid")), connectedAccount.getDiscord(), true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.uuid")), connectedAccount.getUuid().toString(), true)
                    .build());
        });

        return toReturn;
    }
}
