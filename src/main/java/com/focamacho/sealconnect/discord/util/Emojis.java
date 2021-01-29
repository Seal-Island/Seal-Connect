package com.focamacho.sealconnect.discord.util;

import com.focamacho.sealconnect.discord.DiscordSealConnect;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

import static com.focamacho.sealconnect.SealConnect.config;

public class Emojis {

    public static Emote animatedCheckmark = null;
    public static Emote animatedCross = null;

    static {
        if(!config.guildId.isEmpty()) {
            Guild guild = DiscordSealConnect.jda.getGuildById(config.guildId);

            if(!config.customYesEmoji.isEmpty()) animatedCheckmark = guild.getEmotesByName(config.customYesEmoji, true).get(0);
            if(!config.customNoEmoji.isEmpty()) animatedCross = guild.getEmotesByName(config.customNoEmoji, true).get(0);
        } else {
            if(!config.customYesEmoji.isEmpty()) animatedCheckmark = DiscordSealConnect.jda.getEmotesByName(config.customYesEmoji, true).get(0);
            if(!config.customNoEmoji.isEmpty()) animatedCross = DiscordSealConnect.jda.getEmotesByName(config.customNoEmoji, true).get(0);
        }
    }

}
