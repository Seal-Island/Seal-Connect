package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.AccountSealConnect;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.util.TextUtils;
import com.focamacho.seallibrary.chat.ChatHandler;
import com.focamacho.seallibrary.util.JsonHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import static com.focamacho.sealconnect.SealConnect.config;

public class MinecraftCommand extends Command {

    public MinecraftCommand(String... aliases) {
        super(aliases);
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

            message.reply(getProfileMessage(message.getGuild(), connectedAccount)).queue();
            return;
        }

        AccountSealConnect connectedAccount = DataHandler.getConnectedAccountFromDiscordID(message.getAuthor().getId());

        if(connectedAccount == null) {
            redirectCommand(message, ConnectCommand.class);
            return;
        }

        message.reply(getProfileMessage(message.getGuild(), connectedAccount)).queue();
    }

    public static MessageEmbed getProfileMessage(Guild guild, AccountSealConnect connectedAccount) {
        String nick = connectedAccount.getName();
        String description = connectedAccount.getDescription();
        if(description.isEmpty()) description = TextUtils.getString(SealConnectLang.getLang("discord.description.default"));

        String prefix = ChatHandler.getPrefix(connectedAccount.getUuid());
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

        boolean original = JsonHandler.readJsonFromURL("https://sessionserver.mojang.com/session/minecraft/profile/" + connectedAccount.getUuid().toString().replace("-", "")).has("id");
        int color = config.color;

        try {
            Member member = guild.retrieveMemberById(connectedAccount.getDiscord()).complete();
            if (member != null) {
                if (config.minecraftColorId.isEmpty() || member.getRoles().contains(guild.getRoleById(config.minecraftColorId)))
                    color = member.getColorRaw();
            }
        } catch(Exception ignored) {}

        return new EmbedBuilder()
                .setAuthor(nick, null, "https://minotar.net/helm/" + nick + "/100.png")
                .setDescription(description)
                .setThumbnail("https://minotar.net/helm/" + nick + "/100.png")
                .setFooter(guild.getName(), guild.getIconUrl())
                .setColor(color)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.nickname")), nick, true)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.rank")), formattedPrefix.toString(), true)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.original")), original ? TextUtils.getString(SealConnectLang.getLang("discord.minecraft.yes")) : TextUtils.getString(SealConnectLang.getLang("discord.minecraft.no")), true)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.discord")), "<@!" + connectedAccount.getDiscord() + ">", true)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.discordid")), connectedAccount.getDiscord(), true)
                .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.uuid")), connectedAccount.getUuid().toString(), true)
                .build();
    }
}
