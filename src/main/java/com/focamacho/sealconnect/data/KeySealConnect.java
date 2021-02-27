package com.focamacho.sealconnect.data;

import lombok.Data;

import java.util.UUID;

@Data
public class KeySealConnect {

    private UUID uuid;
    private String name;
    private String key;

    public KeySealConnect(UUID uuid, String name, String key) {
        this.uuid = uuid;
        this.name = name;
        this.key = key;
    }

}
