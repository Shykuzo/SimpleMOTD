package fr.shykuzo.simplemotd;

import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main extends Plugin implements Listener {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    // ------------------------------ \\

    @Override
    public void onLoad() {
        getProxy().getConsole().sendMessage("§2Starting " + name + " §2...");
    }

    @Override
    public void onEnable() {
        getProxy().getConsole().sendMessage(name + " §ahas been started successfully !");

        instance = this;
        getProxy().getPluginManager().registerListener(this, this);

        createFile("Config");
        saveConfig(config, "Config");
    }

    @Override
    public void onDisable() {
        getProxy().getConsole().sendMessage(name + " §chas been disabled !");
    }

    // ------------------------------ \\

    @EventHandler
    public void onPing(ProxyPingEvent e) {
        ServerPing p = e.getResponse();

        String firstLine = getConfig("Config").getString("FIRST_MESSAGE").replace("&", "§");
        String secondLine = getConfig("Config").getString("SECOND_MESSAGE").replace("&", "§");
        String version = getConfig("Config").getString("VERSION").replace("&", "§");
        String player = getConfig("Config").getString("PLAYERS").replace("&", "§");
        String favicon = getConfig("Config").getString("FAVICON");

        p.setDescription(firstLine + "\n" + secondLine);
        p.setVersion(new ServerPing.Protocol(version + " " + ProxyServer.getInstance().getOnlineCount() + player, 1));
        try {
            p.setFavicon(Favicon.create(ImageIO.read(new File(favicon + ".png"))));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        e.setResponse(p);

    }

    // ------------------------------ \\

    private void createFile(String name) {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), name + ".yml");

        if(!file.exists()) {
            try {
                file.createNewFile();

                if(name.equals("Config")) {
                    config.set("FIRST_MESSAGE", "&2You use &b&lSimpleMOTD &2by &c&lShykuzo &2! &aThank :D");
                    config.set("SECOND_MESSAGE", "&7Change this message in 'Config.yml' of SimpleMOTD.");
                    config.set("VERSION", "&7Welcome to SERVERNAME !");
                    config.set("PLAYERS", "&f/&c100");
                    config.set("FAVICON", "server-icon");
                    saveConfig(config, name);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Configuration getConfig(String name) {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), name + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveConfig(Configuration config, String name) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), name + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String name = "§b§lSimpleMOTD";
    Configuration config = getConfig("Config");

}
