package de.nitolia.event.commands;

import com.imaginarycode.minecraft.redisbungee.util.UUIDFetcher;
import de.nitolia.event.TCore;
import de.nitolia.event.Utils;
import de.nitolia.event.database.entities.BanTable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.UUID;

public class TempBan extends Command {
    public TempBan() {
        super("tempban", "event.ban", "tban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(TCore.BASIC_INFO().append("Benutzung: /tban <Spieler> <Zeit> [Grund]").create());
        } else {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
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
            String reason = args.length == 2 ? "-/-" : StringUtils.join(args, ' ', 2, args.length);
            BanTable ban = new BanTable();
            ban.setStart(Utils.currTimestamp());
            Timestamp fin;
            try {
                fin = Utils.getTimestampForVariable(args[1]);
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(TCore.BASIC_INFO().append("Die Zeitangabe konnte nicht geparst werden.").create());
                return;
            }
            ban.setEnd(fin);
            ban.setPlayer(uuid);
            ban.setReason(reason);
            if (sender instanceof ProxiedPlayer) ban.setBanner(((ProxiedPlayer) sender).getUniqueId());
            ban.save();

            if (player != null)
                player.disconnect(TCore.BANNED_BASE().append("Du wurdest für " + Utils.formatTimeGap(
                        new Timestamp(fin.getTime() - Utils.currTimestamp().getTime())) + " gebannt. Grund:\n\n")
                        .append(reason).color(ChatColor.GRAY).italic(true).create());
            sender.sendMessage(TCore.BASIC_INFO().append("Der User ").append(player == null ? args[0] : player.getName()).color(ChatColor.GOLD)
                    .append(" wurde bis " + Utils.formatDate(fin) + " gebannt.").color(ChatColor.WHITE).create());
        }
    }
}
