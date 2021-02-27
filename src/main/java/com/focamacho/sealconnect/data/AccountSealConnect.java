package com.focamacho.sealconnect.data;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountSealConnect {

    private UUID uuid;
    private String name;
    private String discord;
    private String description;
    private long lastLogin;

    public AccountSealConnect(UUID uuid, String name, String discord, String description, long lastLogin) {
        this.uuid = uuid;
        this.name = name;
        this.discord = discord;
        this.description = description;
        this.lastLogin = lastLogin;
    }

    public void setName(String name) {
        this.name = name;
        DataHandler.handler.updateName(uuid, name);
    }

    public void setDescription(String description) {
        this.description = description;
        DataHandler.handler.updateDescription(uuid, description);
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
        DataHandler.handler.updateLastLogin(uuid, lastLogin);
    }
}
