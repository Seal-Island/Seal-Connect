package com.focamacho.sealconnect.config;

import com.focamacho.sealconnect.SealConnect;
import com.focamacho.seallibrary.config.ILangConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.focamacho.sealconnect.SealConnect.config;

public class SealConnectLang implements ILangConfig {

    @Override
    public String getDefaultLang() {
        return config.language;
    }

    @Override
    public Map<String, LinkedHashMap<String, String>> getDefaultTranslations() {
        Map<String, LinkedHashMap<String, String>> translations = new HashMap<>();

        LinkedHashMap<String, String> pt_br = new LinkedHashMap<>();

        pt_br.put("discord.connect.title", "Conectar");
        pt_br.put("discord.connect.help", "Para conectar a sua conta você precisa informar a sua chave de uso único.\n" +
                                          "Para obtê-la é só entrar em qualquer servidor de minecraft da **Seal Island** e utilizar o comando **/discord**.\n" +
                                          "Com a chave em mãos, é só digitar **%botprefix%conectar <chave>** no Discord.");
        pt_br.put("discord.connect.wrongkey", "A chave que você inseriu é inválida.\n" +
                                              "Por favor, tente novamente utilizando a chave obtida por meio do comando **/discord** dentro do jogo.");
        pt_br.put("discord.connected.description", "Sua conta do Discord foi conectada ao Minecraft." +
                                                    "\nUtilize o comando **%botprefix%minecraft** para verificar a conexão.");
        pt_br.put("discord.connect.notconnected", "Você precisa primeiro ter sua conta do Discord conectada ao Minecraft por meio do comando **%botprefix%conectar**.");
        pt_br.put("discord.connect.already.connected", "A sua conta já está conectada ao **Minecraft**.");

        pt_br.put("discord.description.title", "Descrição");
        pt_br.put("discord.description.limit", "A descrição inserida ultrapassa o limite de 180 caracteres.");
        pt_br.put("discord.description.default", "Eai, turu bom? É possível modificar essa descrição usando **%botprefix%descricao <descrição>**.");
        pt_br.put("discord.description.defaultrank", "Jogador");

        pt_br.put("discord.minecraft.minecraft", "Minecraft");
        pt_br.put("discord.minecraft.noaccount", "Não foi possível encontrar nenhuma conta com os dados informados.");
        pt_br.put("discord.minecraft.nickname", "Nickname:");
        pt_br.put("discord.minecraft.rank", "Rank:");
        pt_br.put("discord.minecraft.lastlogin", "Último login:");
        pt_br.put("discord.minecraft.discord", "Discord:");
        pt_br.put("discord.minecraft.discordid", "ID do Discord:");
        pt_br.put("discord.minecraft.uuid", "UUID:");
        pt_br.put("discord.minecraft.yes", "Sim:");
        pt_br.put("discord.minecraft.no", "Não:");

        pt_br.put("discord.server.players", "Jogadores únicos:");
        pt_br.put("discord.server.connected", "Contas conectadas:");

        pt_br.put("discord.disconnect.title", "Desconectar?");
        pt_br.put("discord.disconnect.description", "Você tem certeza que deseja desconectar as contas?\nIsso irá resultar na perca de cargos e permissões provenientes dessa conexão.\n\nReaja abaixo para confirmar.");
        pt_br.put("discord.disconnect.success.title", "Desconectado(a)");
        pt_br.put("discord.disconnect.success.description", "As contas foram desconectadas.");
        pt_br.put("discord.disconnect.failed.title", "Desconectar");
        pt_br.put("discord.disconnect.failed.description", "As contas não serão desconectadas.");

        pt_br.put("discord.suggestion.title", "Sugerir");
        pt_br.put("discord.suggestion.missing", "Você precisa informar qual a sua sugestão. Exemplo: **%botprefix%sugerir Eu acho que x mod deveria ser adicionado ao servidor pois ele é muito legal e não causa problemas**.");
        pt_br.put("discord.suggestion.success", "Sua sugestão foi enviada e já pode ser visualizada em <#%suggestionschannel%>.");
        pt_br.put("discord.suggestion.suggestedby", "Sugerido por:");
        pt_br.put("discord.suggestion.servernick", "Nick no servidor:");
        pt_br.put("discord.suggestion.suggestion", "Sugestão:");
        pt_br.put("discord.suggestion.footer", "%guildname% • Sugira também usando o comando %botprefix%sugerir");
        pt_br.put("discord.suggestion.channelmissing", "Não foi possível encontrar o canal de sugestões. Caso você seja um administrador, verifique as configurações do Seal Connect.");

        pt_br.put("discord.error.title", "Erro");
        pt_br.put("discord.error.description", "Você não tem permissão para executar esse comando.");

        pt_br.put("minecraft.prefix", "&7[&dSeal Connect&7] ");
        pt_br.put("minecraft.only.players", "&cEsse comando só pode ser utilizado por jogadores.");
        pt_br.put("minecraft.already.connected", "Você já está conectado ao Discord.\n" +
                                                 "&d&o%discord%&r&7.");
        pt_br.put("minecraft.discord.connect", "&bClique para se conectar ao Discord!");
        pt_br.put("minecraft.key", "A sua chave de conexão é: &b%key%\n" +
                                    "Para usá-la é só ir até qualquer chat do\n" +
                                    "discord da &dSeal Island &7e usar o\n" +
                                    "comando %botprefix%conectar &b%key%&7.\n" +
                                    "&d&o%discord%&r&7.");
        pt_br.put("minecraft.arguments", "&cEsse comando não espera tantos argumentos assim.");
        pt_br.put("minecraft.console.account", "&cEi! Você é o console! Você não tem uma conta conectada.");
        pt_br.put("minecraft.no.account", "&cVocê não possui uma conta conectada ao &cDiscord. Conecte a sua conta usando &b/discord&c.");
        pt_br.put("minecraft.no.account.found", "&cNenhuma conta conectada foi encontrada a &cpartir do Nickname, UUID ou ID do Discord informado.");
        pt_br.put("minecraft.disconnect.confirm", "&7Você tem certeza que deseja desconectar as &7contas?\n" +
                "&7Isso irá resultar na &cperca &7de cargos e &7permissões provenientes dessa conexão.\n" +
                "&7Digite &4%disconnectconfirm% &7para continuar.");
        pt_br.put("minecraft.disconnected", "&7A sua conta foi desconectada do &cDiscord&7.");
        pt_br.put("minecraft.disconnected.other", "&7A conta foi desconectada do &cDiscord&7.");
        pt_br.put("minecraft.no.permission", "&cVocê não tem permissão para executar esse &ccomando.");
        pt_br.put("minecraft.disconnected.failed", "&7As contas não serão desconectadas.");

        translations.put("pt_br", pt_br);
        return translations;
    }

    public static String getLang(String key) {
        return SealConnect.lang.get(key);
    }

}
