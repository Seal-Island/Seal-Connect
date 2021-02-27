package com.focamacho.sealconnect.command;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.AccountSealConnect;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.util.TextUtils;
import com.focamacho.seallibrary.chat.ChatHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class DisconnectCommand extends Command {

    public DisconnectCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return SealConnect.config.disconnectAliasesMinecraft;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!(sender instanceof ProxiedPlayer)){
                TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.console.account"));
                return;
            }

            if(!sender.hasPermission("sealconnect.disconnect.self")) {
                TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.no.permission"));
                return;
            }

            ProxiedPlayer player = (ProxiedPlayer) sender;
            AccountSealConnect account = DataHandler.getConnectedAccountFromUUID(player.getUniqueId());
            if(account != null) {
                sendConfirmation((ProxiedPlayer) sender, account);
            }
            else TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.no.account"));
        } else if(args.length == 1) {
            if(!sender.hasPermission("sealconnect.disconnect.other")) {
                TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.no.permission"));
                return;
            }

            String user = args[0];
            AccountSealConnect account = DataHandler.getConnectedAccountFromAny(user);
            if(account != null) {
                if(!(sender instanceof ProxiedPlayer)) {
                    DataHandler.removeUser(account);
                    TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.disconnected.other"));
                } else {
                    sendConfirmation((ProxiedPlayer) sender, account);
                }
            }
            else TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.no.account.found"));
        } else {
            TextUtils.sendMessage(sender, SealConnectLang.getLang("minecraft.arguments"));
        }
    }

    private void sendConfirmation(ProxiedPlayer player, AccountSealConnect account) {
        TextUtils.sendMessage(player, SealConnectLang.getLang("minecraft.disconnect.confirm"));
        ChatHandler.waitForMessage(player.getUniqueId(), (message) -> {
            if(message.getMessage().equalsIgnoreCase(SealConnect.config.disconnectConfirm)) {
                DataHandler.removeUser(account);
                if(account.getUuid().equals(player.getUniqueId())) TextUtils.sendMessage(player, SealConnectLang.getLang("minecraft.disconnected"));
                else TextUtils.sendMessage(player, SealConnectLang.getLang("minecraft.disconnected.other"));
            } else {
                TextUtils.sendMessage(player, SealConnectLang.getLang("minecraft.disconnected.failed"));
            }
        }, 120);
    }

}
