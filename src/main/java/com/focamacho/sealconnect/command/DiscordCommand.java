package com.focamacho.sealconnect.command;

import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.util.TextUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Random;
import java.util.UUID;

public class DiscordCommand extends Command {

    public DiscordCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.only.players"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(DataHandler.getConnectedAccountFromUUID(player.getUniqueId()) != null) {
            TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.already.connected"));
            return;
        }

        String key = DataHandler.keys.get(player.getUniqueId());
        if(key == null) key = genNewKey(player.getUniqueId());

        TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.key").replace("%key%", key), player);
    }

    private String genNewKey(UUID uuid) {
        Random rand = new Random();
        StringBuilder key = new StringBuilder();
        while(key.length() <= 6) {
            String chars = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
            key.append(chars.charAt(rand.nextInt(chars.length())));
            if(DataHandler.keys.containsValue(key.toString().toLowerCase())) key = new StringBuilder();
        }
        DataHandler.keys.put(uuid, key.toString());
        return key.toString();
    }
}
