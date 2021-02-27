package com.focamacho.sealconnect.util;

import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.discord.DiscordSealConnect;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static com.focamacho.sealconnect.SealConnect.config;

public class TextUtils {

    public static void sendMessage(CommandSender sender, String message) {
        for (String s : message.split("\n")) {
            TextComponent component = getText(SealConnectLang.getLang("minecraft.prefix") + s);
            if(s.contains("%discord%")) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, config.discordUrl));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{getText("minecraft.discord.connect")}));
            }
            sender.sendMessage(component);
        }
    }

    public static void sendMessage(CommandSender sender, String message, ProxiedPlayer player) {
        for (String s : message.split("\n")) {
            TextComponent component = getText(SealConnectLang.getLang("minecraft.prefix") + s, player);
            if(s.contains("%discord%")) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, config.discordUrl));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{getText(SealConnectLang.getLang("minecraft.discord.connect"), player)}));
            }
            sender.sendMessage(component);
        }
    }

    public static TextComponent getText(String text) {
        return new TextComponent(getString(text));
    }

    public static TextComponent getText(String text, ProxiedPlayer player) {
        return new TextComponent(getString(text, player));
    }

    public static String getString(String text) {
        return text.replace("&", "§")
                .replace("%discord%", config.discordUrl)
                .replace("%botprefix%", config.botPrefix)
                .replace("%suggestionschannel%", config.suggestionsChannel)
                .replace("%guildname%", DiscordSealConnect.jda.getGuilds().size() > 0 ? DiscordSealConnect.jda.getGuilds().get(0).getName() : "")
                .replace("%disconnectconfirm%", config.disconnectConfirm);
    }

    public static String getString(String text, ProxiedPlayer player) {
        return getString(text.replace("%player%", player.getName())
                .replace("%uuid%", player.getUniqueId().toString()));
    }

}
