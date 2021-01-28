package com.focamacho.sealconnect.discord.listener;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.discord.utils.Emojis;
import com.focamacho.sealconnect.discord.utils.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SuggestionListener extends ListenerAdapter {

    public static List<String> alreadyWarned = new ArrayList<>();

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if(SealConnect.config.suggestionsChannel.isEmpty()) return;
        if(event.getUser().isBot()) return;

        if(SealConnect.config.suggestionsRequireConnect && event.getChannel().getId().equalsIgnoreCase(SealConnect.config.suggestionsChannel)) {
            if(DataHandler.getConnectedAccountFromDiscordID(event.getUserId()) == null){
                event.getReaction().removeReaction(event.getUser()).queue();
                if(!alreadyWarned.contains(event.getUserId())) {
                    alreadyWarned.add(event.getUserId());
                    event.getChannel().sendMessage(new MessageBuilder().setContent(event.getUser().getAsMention()).setEmbed(new EmbedBuilder()
                            .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.connect.title")))
                            .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.connect.notconnected")))
                            .setColor(SealConnect.config.color)
                            .setThumbnail(TextUtils.getString(SealConnect.config.erroredImage))
                            .build())
                            .build()).queue(
                            msg -> SealConnect.scheduler.schedule(() ->
                                msg.delete().queue(dlt -> alreadyWarned.remove(event.getUserId())), 5, TimeUnit.SECONDS)
                    );
                }
            } else {
                if(Emojis.animatedCheckmark != null && event.getReactionEmote().getEmote().equals(Emojis.animatedCheckmark)) {
                    event.retrieveMessage().queue(msg -> msg.removeReaction(Emojis.animatedCross, event.getUser()).queue());
                } else if(Emojis.animatedCross != null && event.getReactionEmote().getEmote().equals(Emojis.animatedCross)) {
                    event.retrieveMessage().queue(msg -> msg.removeReaction(Emojis.animatedCheckmark, event.getUser()).queue());
                } else if(event.getReactionEmote().getEmoji().equalsIgnoreCase("✅")) {
                    event.retrieveMessage().queue(msg -> msg.removeReaction("✅", event.getUser()).queue());
                } else if(event.getReactionEmote().getEmoji().equalsIgnoreCase("❌")) {
                    event.retrieveMessage().queue(msg -> msg.removeReaction("❌", event.getUser()).queue());
                }
            }
        }
    }

}
