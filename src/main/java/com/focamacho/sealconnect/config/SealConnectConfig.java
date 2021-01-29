package com.focamacho.sealconnect.config;

import blue.endless.jankson.Comment;

import java.util.HashMap;
import java.util.Map;

public class SealConnectConfig {

    @Comment("Idioma usado. O nome deve ser o mesmo do arquivo dentro da pasta lang.")
    public String language = "pt_br";

    @Comment("Link de convite para o Discord.")
    public String discordUrl = "https://discord.sealisland.com.br";

    @Comment("O token para se conectar ao bot do Discord.")
    public String botToken = "";

    @Comment("O prefix usado para os comandos do bot no Discord.")
    public String botPrefix = "!";

    @Comment("Grupo para Nitros. Se você deseja que os Nitro Boosters do seu discord recebam um cargo especial no servidor, " +
            "defina aqui o nome do grupo no minecraft que você deseja que eles recebam.")
    public String nitroRoleName = "";

    @Comment("A mensagem exibida no Activity Presence do Bot.")
    public String activityPresence = "cidade pelas pedras dessas ruas!";

    @Comment("A cor usada para os embeds do bot no Discord.")
    public int color = 9438204;

    @Comment("O id da guilda em que o bot deve executar os comandos. Só precisa ser definida se o bot estiver em mais de uma guild.")
    public String guildId = "";

    @Comment("Caso queira ativar o módulo de sugestões, defina aqui o ID do canal que será usado para as sugestões.\n" +
            "Atenção: O canal será definido como somente para votação de sugestões.\n" +
            "Defina um canal vazio, com chat trancado para isso.")
    public String suggestionsChannel = "";

    @Comment("Faz com que o usuário tenha que ter a sua conta do Discord conectada ao minecraft para poder votar nas sugestões.")
    public boolean suggestionsRequireConnect = true;

    @Comment("Nome do emoji customizado para indicar 'sim' na mensagem de votação.")
    public String customYesEmoji = "";

    @Comment("Nome do emoji customizado para indicar 'não' na mensagem de votação.")
    public String customNoEmoji = "";

    @Comment("As roles que você quer que sejam linkadas do Minecraft para o Discord.\n" +
            "O formato de configuração é: 'permissao':'idDaRole'\n" +
            "Os usuários que tiverem a permissão definida, receberam a role com o ID definido")
    public Map<String, String> linkedRoles = new HashMap<>();

    @Comment("Ao usar o comando !minecraft as informações do jogador aparecem em um embed.\n" +
            "A cor desse embed, por padrão, é baseada na cor da role do jogador em questão.\n" +
            "Caso você queira que só jogadores com um cargo especifíco tenham a cor desse embed\n" +
            "alterada, coloque aqui o ID do cargo que eles precisarão possuir.")
    public String minecraftColorId = "";

    @Comment("Imagem usada de thumbnail em embeds onde o comando foi executado erroneamente.")
    public String erroredImage = "https://cdn.discordapp.com/attachments/761766827176886335/796281135415820318/grayminecraft.gif";

    @Comment("Imagem usada de thumbnail em embeds onde o comando foi executado corretamente.")
    public String successfulImage = "https://cdn.discordapp.com/attachments/761766827176886335/796281132429737994/minecraft.gif";

    @Comment("Os aliases usados para o comando !conectar do bot.")
    public String[] connectAliases = {"conectar", "usarkey", "key", "connect"};

    @Comment("Os aliases usados para o comando !minecraft do bot.")
    public String[] minecraftAliases = {"minecraft", "perfil", "profile"};

    @Comment("Os aliases usados para o comando !descricao do bot.")
    public String[] descriptionAliases = {"description", "descrição", "descricao", "descricão", "descriçao"};

    @Comment("Os aliases usados para o comando !servidoro bot.")
    public String[] serverAliases = {"servidor", "server"};

    @Comment("Os aliases usados para o comando !sugerir do bot.")
    public String[] suggestAliases = {"sugerir", "sugestao", "sugestão", "suggest"};
}
