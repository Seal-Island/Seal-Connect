package com.focamacho.sealconnect.command;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.data.KeySealConnect;
import com.focamacho.sealconnect.util.TextUtils;
import com.focamacho.seallibrary.permission.PermissionHandler;
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
    public String[] getAliases() {
        return SealConnect.config.discordAliases;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.only.players"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(!PermissionHandler.hasPermission(player.getUniqueId(), "sealconnect.discord")) {
            TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.no.permission"));
            return;
        }

        if(DataHandler.getConnectedAccountFromUUID(player.getUniqueId()) != null) {
            TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.already.connected"));
            return;
        }

        KeySealConnect keySeal = DataHandler.getKey(player.getUniqueId());
        if(keySeal == null) keySeal = genNewKey(player.getUniqueId(), player.getName());

        TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.key").replace("%key%", keySeal.getKey()), player);
    }

    private KeySealConnect genNewKey(UUID uuid, String name) {
        Random rand = new Random();
        StringBuilder key = new StringBuilder();
        while(key.length() <= 6) {
            String chars = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
            key.append(chars.charAt(rand.nextInt(chars.length())));
            if(DataHandler.getKey(key.toString().toLowerCase()) != null) key = new StringBuilder();
        }
        KeySealConnect newKey = new KeySealConnect(uuid, name, key.toString());
        DataHandler.keys.add(newKey);
        return newKey;
    }
}
