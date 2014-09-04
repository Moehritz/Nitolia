package de.nitolia.event.commands;

import de.nitolia.event.TCore;
import de.nitolia.event.Utils;
import de.nitolia.event.database.entities.BanTable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanInfo extends Command {
    public BanInfo() {
        super("baninfo", "event.ban", "bani", "ban-info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(TCore.BASIC_INFO().append("Benutzung: /bani <BanID>").create());
        } else if (!Utils.isInt(args[0])) {
            sender.sendMessage(TCore.BASIC_INFO().append("Du musst eine Zahl angeben!").create());
        } else {
            int banID = Integer.parseInt(args[0]);
            BanTable ban = TCore.getInst().getDatabase().find(BanTable.class).where().eq("id", banID).findUnique();
            if (ban == null) {
                sender.sendMessage(TCore.BASIC_INFO().append("Der Ban mit der ID ").append(banID + "").color(ChatColor.GOLD)
                        .append(" ist nicht bekannt.").color(ChatColor.WHITE).create());
                return;
            }
            sender.sendMessage(TCore.BASIC_INFO().append("Zeige Info für Ban ").append(banID + "").color(ChatColor.GOLD)
                    .append(ban.calcActive() ? "" : " INAKTIV").color(ChatColor.RED).create());
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(ban.getPlayer());
            sender.sendMessage(TCore.SUB_INFO().append("User: ").append(player == null ? ban.getPlayer().toString() : player.getName()).create());
            sender.sendMessage(TCore.SUB_INFO().append("Grund: ").append(ban.getReason()).create());
            sender.sendMessage(TCore.SUB_INFO().append("Zeit: ").append(Utils.formatDate(ban.getStart())).create());
            sender.sendMessage(TCore.SUB_INFO().append("Bis: ").append(ban.getEnd() == null ? "Permanent" : "bis " + Utils.formatDate(ban.getEnd())).create());
        }
    }
}