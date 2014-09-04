package de.nitolia.event;

import com.avaje.ebean.EbeanServer;
import de.nitolia.event.commands.*;
import de.nitolia.event.commands.Shutdown;
import de.nitolia.event.database.DatabaseHandler;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.module.reconnect.yaml.YamlReconnectHandler;

import java.io.File;

@Getter
public class TCore extends Plugin {

    @Getter
    private static TCore inst;

    private Configuration cfg;
    private EbeanServer database;
    private Announcer announcer;
    private ReconnectHandler reconnectHandler = new YamlReconnectHandler();

    @Override
    public void onLoad() {
        inst = this;

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                System.out.println("Configordner konnte nicht erstellt werden! Crash sehr gut möglich");
            }
        }
        File configFile = new File(dataFolder, "config.yml");
        cfg = new Config(configFile).reload();

        database = DatabaseHandler.connect(cfg.getString("db.host"), cfg.getInt("db.port"), cfg.getString("db.database"), cfg.getString("db.user"), cfg.getString("db.pass"));

        getProxy().setReconnectHandler(new ReconnectHandler() {
            @Override
            public ServerInfo getServer(ProxiedPlayer player) {
                return Utils.bestServer(player);
            }

            @Override
            public void setServer(ProxiedPlayer player) {
                reconnectHandler.setServer(player);
            }

            @Override
            public void save() {
                reconnectHandler.save();
            }

            @Override
            public void close() {
                reconnectHandler.close();
            }
        });
    }

    @Override
    public void onEnable() {
        PluginManager pm = getProxy().getPluginManager();

        pm.registerCommand(this, new AnnounceAdd());
        pm.registerCommand(this, new AnnounceCreate());
        pm.registerCommand(this, new AnnounceDel());
        pm.registerCommand(this, new Ban());
        pm.registerCommand(this, new BanInfo());
        pm.registerCommand(this, new ShowBans());
        pm.registerCommand(this, new Shutdown());
        pm.registerCommand(this, new TempBan());
        pm.registerCommand(this, new Unban());

        pm.registerListener(this, new PlayerListener());

        announcer = new Announcer();
    }

    public static ComponentBuilder BASIC_INFO() {
        return new ComponentBuilder("►").color(ChatColor.BLUE).append("").color(ChatColor.WHITE);
    }

    public static ComponentBuilder SUB_INFO() {
        return new ComponentBuilder("▌").color(ChatColor.BLUE).append(" ").color(ChatColor.WHITE);
    }

    public static ComponentBuilder BANNED_BASE() {
        return new ComponentBuilder("► ").color(ChatColor.BLUE).append("Nitrado | Timolia")
                .color(ChatColor.AQUA).append(" ◄").color(ChatColor.BLUE).append("\n").color(ChatColor.WHITE);
    }

    public static ComponentBuilder BANNED_FOREVER() {
        return BANNED_BASE().append("Du bist permanent vom Server gebannt. Grund:").append("\n\n").color(ChatColor.GRAY);
    }
}