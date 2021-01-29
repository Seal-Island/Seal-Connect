package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.data.ProfileData;
import com.focamacho.sealconnect.util.TextUtils;
import com.focamacho.seallibrary.common.util.JsonHandler;
import com.focamacho.seallibrary.common.util.PermissionUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Map;
import java.util.UUID;

public class MinecraftCommand extends Command {

    public MinecraftCommand(String... aliases) {
        super(aliases);
    }

    @Override
    public void execute(Message message) {
        String[] args = getArgs(message);

        if(args.length == 1) {
            Map.Entry<UUID, String> connectedAccount = DataHandler.getConnectedAccountFromAny(args[0].replace("<@!", "").replace(">", ""));

            if(connectedAccount == null) {
                message.reply(new EmbedBuilder()
                        .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.minecraft")))
                        .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.noaccount")))
                        .setColor(SealConnect.config.color)
                        .setThumbnail(TextUtils.getString(SealConnect.config.erroredImage))
                        .build()).queue();
                return;
            }

            message.reply(getProfileMessage(message.getGuild(), connectedAccount)).queue();
            return;
        }

        Map.Entry<UUID, String> connectedAccount = DataHandler.getConnectedAccountFromDiscordID(message.getAuthor().getId());

        if(connectedAccount == null) {
            redirectCommand(message, ConnectCommand.class);
            return;
        }

        message.reply(getProfileMessage(message.getGuild(), connectedAccount)).queue();
    }

    public static MessageEmbed getProfileMessage(Guild guild, Map.Entry<UUID, String> connectedAccount) {
        try {
            ProfileData profileData = DataHandler.getProfileData(connectedAccount.getValue());
            String nick = DataHandler.savedNames.get(connectedAccount.getKey());
            String description = profileData.getDescription().isEmpty() ? TextUtils.getString(SealConnectLang.getLang("discord.description.default")) : profileData.getDescription();

            String prefix = PermissionUtils.getPrefix(connectedAccount.getKey());
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

            boolean original = JsonHandler.readJsonFromURL("https://sessionserver.mojang.com/session/minecraft/profile/" + connectedAccount.getKey().toString().replace("-", "")).has("id");
            int color = SealConnect.config.color;

            Member member = guild.retrieveMemberById(connectedAccount.getValue()).complete();
            if (member != null) {
                if (SealConnect.config.minecraftColorId.isEmpty() || member.getRoles().contains(guild.getRoleById(SealConnect.config.minecraftColorId))) color = member.getColorRaw();
            }

            return new EmbedBuilder()
                    .setAuthor(nick, null, "https://minotar.net/helm/" + nick + "/100.png")
                    .setDescription(description)
                    .setThumbnail("https://minotar.net/helm/" + nick + "/100.png")
                    .setFooter(guild.getName(), guild.getIconUrl())
                    .setColor(color)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.nickname")), nick, true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.rank")), formattedPrefix.toString(), true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.original")), original ? TextUtils.getString(SealConnectLang.getLang("discord.minecraft.yes")) : TextUtils.getString(SealConnectLang.getLang("discord.minecraft.no")), true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.discord")), "<@!" + connectedAccount.getValue() + ">", true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.discordid")), connectedAccount.getValue(), true)
                    .addField(TextUtils.getString(SealConnectLang.getLang("discord.minecraft.uuid")), connectedAccount.getKey().toString(), true)
                    .build();
        } catch(Exception e) { e.printStackTrace(); }
        return null;
    }
}
