package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.data.KeySealConnect;
import com.focamacho.sealconnect.discord.DiscordSealConnect;
import com.focamacho.sealconnect.util.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static com.focamacho.sealconnect.SealConnect.config;

public class ConnectCommand extends Command {

    public ConnectCommand(String... aliases) {
        super(aliases);
    }

    @Override
    public void execute(Message message) {
        String[] args = getArgs(message);

        if(DataHandler.getConnectedAccountFromDiscordID(message.getAuthor().getId()) != null) {
            if(args.length == 0) redirectCommand(message, MinecraftCommand.class);
            else message.reply(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.connect.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.connect.already.connected")))
                    .setColor(config.color)
                    .setThumbnail(TextUtils.getString(config.erroredImage))
                    .build()).queue();
            return;
        }

        if(args.length != 1) {
            message.reply(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.connect.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.connect.help")))
                    .setColor(config.color)
                    .setThumbnail(TextUtils.getString(config.erroredImage))
                    .build()).queue();
            return;
        }

        KeySealConnect key = DataHandler.getKey(args[0].toLowerCase());
        if(key != null) {
            DataHandler.addUser(key.getUuid(), message.getAuthor().getId(), "", key.getName());

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(key.getUuid());
            if(player != null) DiscordSealConnect.updateRoles(player);

            DataHandler.keys.remove(key);

            String title = player != null ? TextUtils.getString(SealConnectLang.getLang("discord.connect.title"), player) : TextUtils.getString(SealConnectLang.getLang("discord.connect.title"));
            String description = player != null ? TextUtils.getString(SealConnectLang.getLang("discord.connected.description"), player) : TextUtils.getString(SealConnectLang.getLang("discord.connected.description"));
            String thumb = player != null ? TextUtils.getString(config.successfulImage, player) : TextUtils.getString(config.successfulImage);

            message.reply(new EmbedBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setColor(config.color)
                    .setThumbnail(thumb)
                    .build()).queue();
        } else {
            message.reply(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.connect.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.connect.wrongkey")))
                    .setColor(config.color)
                    .setThumbnail(TextUtils.getString(config.erroredImage))
                    .build()).queue();
        }
    }

}
