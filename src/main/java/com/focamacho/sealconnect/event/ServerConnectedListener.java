package com.focamacho.sealconnect.event;

import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.discord.DiscordSealConnect;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectedListener implements Listener {

    @EventHandler
    public void onConnect(ServerConnectedEvent event) {
        DiscordSealConnect.updateRoles(event.getPlayer());
        DataHandler.savedNames.put(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

}
