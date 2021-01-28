package com.focamacho.sealconnect.data;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.seallibrary.common.util.JsonHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataHandler {

    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public static final File dataFile = new File("./plugins/SealConnect/data.json");

    public static Map<UUID, String> connectedAccounts = new HashMap<>();
    public static Map<UUID, String> keys = new HashMap<>();
    public static Map<UUID, String> savedNames = new HashMap<>();
    public static Map<String, ProfileData> profileData = new HashMap<>();

    public static void init() {
        SealConnect.logger.info("Iniciando carregamento dos dados...");
        try {
            connectedAccounts.clear();
            keys.clear();
            savedNames.clear();

            JsonHandler.getOrCreateJsonObject(dataFile, "connectedAccounts").toMap().forEach((uuid, discord) -> connectedAccounts.put(UUID.fromString(uuid), (String) discord));

            JsonHandler.getOrCreateJsonObject(dataFile, "savedNames").toMap().forEach((uuid, name) -> savedNames.put(UUID.fromString(uuid), (String) name));

            JSONArray array = JsonHandler.getOrCreateJsonArray(dataFile, "profileData");
            for(int i = 0; i < array.length(); i++) {
                try {
                    JSONObject profileObject = array.getJSONObject(i);
                    profileData.put(profileObject.getString("discordId"), new ProfileData(profileObject.optString("description", "")));
                } catch(Exception ignored) {}
            }

            SealConnect.logger.info("Carregamento de dados concluido com sucesso!");
            SealConnect.logger.info("Dados de " + connectedAccounts.size() + " contas foram carregados.");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        executor.submit(() -> {
            try {
                JSONObject json = new JSONObject();

                JSONObject accounts = new JSONObject();
                connectedAccounts.forEach((uuid, discord) -> accounts.put(uuid.toString(), discord));
                json.put("connectedAccounts", accounts);

                JSONObject names = new JSONObject();
                savedNames.forEach((uuid, name) -> names.put(uuid.toString(), name));
                json.put("savedNames", names);

                JSONArray profiles = new JSONArray();
                profileData.forEach((discord, data) -> {
                    JSONObject profile = new JSONObject();
                    profile.put("discordId", discord);
                    profile.put("description", data.getDescription().isEmpty() ? "" : data.getDescription());
                    profiles.put(profile);
                });
                json.put("profileData", profiles);

                JsonHandler.saveToJson(dataFile, json);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static Map.Entry<UUID, String> getKey(String key) {
        for (Map.Entry<UUID, String> entry : keys.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(key)) return entry;
        }
        return null;
    }

    public static Map.Entry<UUID, String> getConnectedAccountFromDiscordID(String id) {
        for(Map.Entry<UUID, String> entry : connectedAccounts.entrySet()) {
            if(entry.getValue().equalsIgnoreCase(id)) {
                return entry;
            }
        }
        return null;
    }

    public static Map.Entry<UUID, String> getConnectedAccountFromUUID(UUID id) {
        for(Map.Entry<UUID, String> entry : connectedAccounts.entrySet()) {
            if(entry.getKey().equals(id)) {
                return entry;
            }
        }
        return null;
    }

    public static Map.Entry<UUID, String> getConnectedAccountFromName(String name) {
        for(Map.Entry<UUID, String> entry : savedNames.entrySet()) {
            if(entry.getValue().equalsIgnoreCase(name)) {
                return getConnectedAccountFromUUID(entry.getKey());
            }
        }
        return null;
    }

    public static ProfileData getProfileData(String id) {
        if(!profileData.containsKey(id)) profileData.put(id, new ProfileData(""));
        return profileData.get(id);
    }

    public static Map.Entry<UUID, String> getConnectedAccountFromAny(String any) {
        Map.Entry<UUID, String> account;
        if((account = getConnectedAccountFromDiscordID(any)) != null) return account;
        else if ((account = getConnectedAccountFromName(any)) != null) return account;
        else {
            try {
                if ((account = getConnectedAccountFromUUID(UUID.fromString(any))) != null) return account;
            } catch(IllegalArgumentException ignored) {}
        }
        return null;
    }

}
