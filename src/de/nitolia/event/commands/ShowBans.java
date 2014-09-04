package de.nitolia.event.commands;

import com.imaginarycode.minecraft.redisbungee.util.UUIDFetcher;
import de.nitolia.event.TCore;
import de.nitolia.event.database.entities.BanTable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ShowBans extends Command {
    public ShowBans() {
        super("sban", "event.ban", "showban", "listbans");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(TCore.BASIC_INFO().append("Benutzung: /sban [Spieler]").create());
        } else if (args.length == 0 && !(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TCore.BASIC_INFO().append("Du musst einen Spieler angeben!").create());
        } else {
            ProxiedPlayer player = args.length == 0 ? (ProxiedPlayer) sender : ProxyServer.getInstance().getPlayer(args[0]);
            UUID uuid = null;
            if (player == null) {
                try {
                    uuid = UUIDFetcher.getUUIDOf(args[0]);
                } catch (Exception e) {
                    sender.sendMessage(TCore.BASIC_INFO().append("Die UUID von ").append(args[0]).color(ChatColor.GOLD)
                            .append(" konnte nicht abgerufen werden.").color(ChatColor.WHITE).create());
                }
                if (uuid == null) {
                    sender.sendMessage(TCore.BASIC_INFO().append("Die UUID von ").append(args[0]).color(ChatColor.GOLD)
                            .append(" wurde nicht gefunden.").color(ChatColor.WHITE).create());
                    return;
                }
            } else {
                uuid = player.getUniqueId();
            }
            List<BanTable> bans = TCore.getInst().getDatabase().find(BanTable.class).where().eq("player", uuid.toString()).findList();
            Collections.sort(bans);
            sender.sendMessage(TCore.BASIC_INFO().append("Der Spieler ").append(player == null ? args[0] : player.getName()).color(ChatColor.GOLD)
                    .append(" hat ").color(ChatColor.WHITE).append(bans.size() + "").color(ChatColor.YELLOW)
                    .append(" Bans.").color(ChatColor.WHITE).create());
            for (BanTable ban : bans) {
                ComponentBuilder b = new ComponentBuilder("#" + ban.getId() + " ").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/baninfo " + ban.getId()))
                        .color(ban.calcActive() ? ChatColor.GREEN : ChatColor.RED).append(ban.getEnd() == null ? "PERM" : "TEMP")
                        .color(ChatColor.WHITE).append(" | ").color(ChatColor.GRAY).append(ban.getReason());
                sender.sendMessage(b.create());
            }
        }
    }
}