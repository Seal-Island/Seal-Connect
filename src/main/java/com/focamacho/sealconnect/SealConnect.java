package com.focamacho.sealconnect;

import com.focamacho.sealconnect.command.DiscordCommand;
import com.focamacho.sealconnect.config.SealConnectConfig;
import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.data.DataHandler;
import com.focamacho.sealconnect.discord.DiscordSealConnect;
import com.focamacho.sealconnect.event.ServerConnectedListener;
import com.focamacho.seallibrary.common.config.ILangConfig;
import com.focamacho.seallibrary.common.config.SealConfig;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public final class SealConnect extends Plugin {

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static SealConnectConfig config;
    public static ILangConfig lang;

    public static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();

        SealConfig sealConfig = new SealConfig(new File("./plugins/SealConnect/lang/"), new SealConnectLang());
        config = sealConfig.getConfig(new File("./plugins/SealConnect/SealConnect.json"), SealConnectConfig.class);
        lang = sealConfig.getLangConfig();

        if(config.botToken.isEmpty()) {
            logger.severe("Você precisa definir o token do seu bot no arquivo de configuração.");
            return;
        }

        DataHandler.init();
        DiscordSealConnect.init();

        getProxy().getPluginManager().registerListener(this, new ServerConnectedListener());
        getProxy().getPluginManager().registerCommand(this, new DiscordCommand("Discord"));
    }

    @Override
    public void onDisable() {
        if(!config.botToken.isEmpty()) DataHandler.save();
    }

}