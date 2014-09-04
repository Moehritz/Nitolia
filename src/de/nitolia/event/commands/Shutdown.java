package de.nitolia.event.commands;

import de.nitolia.event.TCore;
import de.nitolia.event.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Shutdown extends Command {
    public Shutdown() {
        super("shutdown", "event.shutdown", "serverIstWiederAktivUndKannBenutztWerden", "unshotdown", "launch");
    }

    public static Set<ServerInfo> downServer = new HashSet<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        ServerInfo server = player.getServer().getInfo();
        if (downServer.remove(server)) {
            player.sendMessage(TCore.BASIC_INFO().append("Der Server ist jetzt wieder gelauncht und wird wieder befüllt!").create());
        } else {
            downServer.add(server);
            movePlayers(player, server, new HashSet<ProxiedPlayer>());
            player.sendMessage(TCore.BASIC_INFO().append("Es werden nun ").append(player.getServer().getInfo().getPlayers().size() - 1 + "").color(ChatColor.GOLD).append(" User verschoben.").color(ChatColor.WHITE).create());
        }
    }

    public void movePlayers(final ProxiedPlayer player, final ServerInfo info, final Set<ProxiedPlayer> done) {
        int cnt = 0;
        for (ProxiedPlayer p : player.getServer().getInfo().getPlayers()) {
            if (p.equals(player)) continue;
            if (done.contains(p)) continue;
            if (cnt == 10) { // Alle 10 Spieler eine Sek warten (gegen Laggs & Langsam updatende Spielerzahlen)
                ProxyServer.getInstance().getScheduler().schedule(TCore.getInst(), new Runnable() {
                    @Override
                    public void run() {
                        movePlayers(player, info, done);
                    }
                }, 1, TimeUnit.SECONDS);
                return;
            }
            p.connect(Utils.bestServer(player));
            done.add(p);
            cnt++;
        }
    }
}