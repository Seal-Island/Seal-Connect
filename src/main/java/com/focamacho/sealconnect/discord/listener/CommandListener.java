package com.focamacho.sealconnect.discord.listener;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.sealconnect.config.SealConnectLang;
import com.focamacho.sealconnect.discord.command.Command;
import com.focamacho.sealconnect.discord.utils.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();

        if(message.startsWith(SealConnect.config.botPrefix)) {
            String comando = message.split(" ")[0].substring(1);

            comando:
            for(Command cmd : Command.commands) {
                for(String alias : cmd.getAliases()) {
                    if(alias.equalsIgnoreCase(comando)) {
                        if(cmd.canUseCommand(event.getMember())) {
                            cmd.execute(event.getMessage());
                        } else {
                            event.getMessage().reply(new EmbedBuilder()
                                    .setTitle(TextUtils.getString(SealConnectLang.getLang("discord.error.title")))
                                    .setDescription(TextUtils.getString(SealConnectLang.getLang("discord.error.description")))
                                    .setThumbnail(TextUtils.getString(SealConnect.config.erroredImage))
                                    .build()).queue();
                        }
                        break comando;
                    }
                }
            }
        }
    }
}
