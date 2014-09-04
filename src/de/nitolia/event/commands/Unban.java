package de.nitolia.event.commands;

import de.nitolia.event.TCore;
import de.nitolia.event.Utils;
import de.nitolia.event.database.entities.BanTable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Unban extends Command {
    public Unban() {
        super("unban", "event.ban", "uban", "removeban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(TCore.BASIC_INFO().append("Benutzung: /uban <BanID> [-delete]").create());
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
            if (args.length == 2 && args[1].equals("-delete")) {
                ban.delete();
                sender.sendMessage(TCore.BASIC_INFO().append("Der Ban mit der ID ").append(ban.getId() + "")
                        .color(ChatColor.GOLD).append(" wurde vollständig gelöscht.").color(ChatColor.WHITE).create());
            } else {
                ban.setInactive(true);
                ban.update();
                sender.sendMessage(TCore.BASIC_INFO().append("Der Ban mit der ID ").append(ban.getId() + "")
                        .color(ChatColor.GOLD).append(" wurde deaktiviert.").color(ChatColor.WHITE).create());
            }
        }
    }
}
