package com.focamacho.sealconnect.data;

import lombok.Data;

@Data
public class ProfileData {

    private String description;

    public ProfileData(String description) {
        this.description = description;
    }

}
