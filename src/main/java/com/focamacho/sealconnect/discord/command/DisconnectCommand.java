package com.focamacho.sealconnect.discord.command;

import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.AccountSealConnect;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.discord.util.Emojis;
import com.focamacho.sealconnect.util.TextUtils;
import com.focamacho.seallibrary.permission.PermissionHandler;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.concurrent.TimeUnit;

import static com.focamacho.sealconnect.SealConnect.config;

public class DisconnectCommand extends Command {

    public static EventWaiter waiter = new EventWaiter();

    public DisconnectCommand(String... aliases) {
        super(aliases);
    }

    @Override
    public void execute(Message message) {
        String[] args = getArgs(message);

        if(args.length == 1) {
            if(message.getMember() == null || !message.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                message.reply(new EmbedBuilder()
                        .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.error.title")))
                        .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.error.description")))
                        .setThumbnail(TextUtils.getString(config.erroredImage))
                        .build()).queue();
                return;
            }

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

            sendDisconnectMessage(message, connectedAccount);
            return;
        }

        AccountSealConnect connectedAccount = DataHandler.getConnectedAccountFromDiscordID(message.getAuthor().getId());

        if(connectedAccount == null) {
            redirectCommand(message, ConnectCommand.class);
            return;
        }

        if(!PermissionHandler.hasPermission(connectedAccount.getUuid(), "sealconnect.disconnect.self") && (message.getMember() == null || !message.getMember().hasPermission(Permission.ADMINISTRATOR))) {
            message.reply(new EmbedBuilder()
                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.error.title")))
                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.error.description")))
                    .setThumbnail(TextUtils.getString(config.erroredImage))
                    .build()).queue();
            return;
        }

        sendDisconnectMessage(message, connectedAccount);
    }

    private void sendDisconnectMessage(Message message, AccountSealConnect connectedAccount) {
        message.reply(new EmbedBuilder()
                .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.disconnect.title")))
                .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.disconnect.description")))
                .setColor(config.color)
                .setThumbnail("https://minotar.net/helm/" + connectedAccount.getName() + "/100.png")
                .build()).queue(msg -> {

            if(Emojis.animatedCheckmark == null) msg.addReaction("✅").queue();
            else msg.addReaction(Emojis.animatedCheckmark).queue();

            if(Emojis.animatedCross == null) msg.addReaction("❌").queue();
            else msg.addReaction(Emojis.animatedCross).queue();

            waiter.waitForEvent(GuildMessageReactionAddEvent.class, event -> {
                String emoji = event.getReactionEmote().getEmoji();
                String check = Emojis.animatedCheckmark == null ? "✅" : Emojis.animatedCheckmark.getName();
                String cross = Emojis.animatedCross == null ? "❌" : Emojis.animatedCross.getName();
                return (emoji.equalsIgnoreCase(check) || emoji.equalsIgnoreCase(cross)) && event.getUser().getId().equalsIgnoreCase(message.getAuthor().getId());
            }, event -> {
                String emoji = event.getReactionEmote().getEmoji();
                String check = Emojis.animatedCheckmark == null ? "✅" : Emojis.animatedCheckmark.getName();
                String cross = Emojis.animatedCross == null ? "❌" : Emojis.animatedCross.getName();
                if(emoji.equalsIgnoreCase(check)) {
                    DataHandler.removeUser(connectedAccount);
                    message.reply(new EmbedBuilder()
                            .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.disconnect.success.title")))
                            .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.disconnect.success.description")))
                            .setColor(config.color)
                            .build()).queue();
                } else if(emoji.equalsIgnoreCase(cross)) {
                    message.reply(new EmbedBuilder()
                            .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.disconnect.failed.title")))
                            .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.disconnect.failed.description")))
                            .setColor(config.color)
                            .build()).queue();
                }
            }, 2, TimeUnit.MINUTES, () -> {});
        });
    }

}
