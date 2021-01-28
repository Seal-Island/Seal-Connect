package com.focamacho.sealconnect.discord.command;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    public static List<Command> commands = new ArrayList<>();

    @Getter private final String[] aliases;

    public Command(String... aliases) {
        this.aliases = aliases;

        commands.add(this);
    }

    public boolean canUseCommand(Member member) {
      return true;
    }

    public abstract void execute(Message message);

    protected String[] getArgs(Message message) {
        String rawMessage = message.getContentRaw();
        if(!rawMessage.contains(" ")) return new String[]{};

        return rawMessage.substring(rawMessage.indexOf(" ") + 1).split(" ");
    }

    protected void redirectCommand(Message message, Class target) {
        for (Command command : commands) {
            if(command.getClass() == target) {
                command.execute(message);
                break;
            }
        }
    }

}
