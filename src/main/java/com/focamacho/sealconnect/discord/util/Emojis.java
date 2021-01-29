package com.focamacho.sealconnect.discord.util;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.discord.DiscordSealConnect;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

public class Emojis {

    public static Emote animatedCheckmark = null;
    public static Emote animatedCross = null;

    static {
        if(!SealConnect.config.guildId.isEmpty()) {
            Guild guild = DiscordSealConnect.jda.getGuildById(SealConnect.config.guildId);

            if(!SealConnect.config.customYesEmoji.isEmpty()) animatedCheckmark = guild.getEmotesByName(SealConnect.config.customYesEmoji, true).get(0);
            if(!SealConnect.config.customNoEmoji.isEmpty()) animatedCross = guild.getEmotesByName(SealConnect.config.customNoEmoji, true).get(0);
        } else {
            if(!SealConnect.config.customYesEmoji.isEmpty()) animatedCheckmark = DiscordSealConnect.jda.getEmotesByName(SealConnect.config.customYesEmoji, true).get(0);
            if(!SealConnect.config.customNoEmoji.isEmpty()) animatedCross = DiscordSealConnect.jda.getEmotesByName(SealConnect.config.customNoEmoji, true).get(0);
        }
    }

}
