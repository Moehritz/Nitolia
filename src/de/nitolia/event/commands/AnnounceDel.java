package de.nitolia.event.commands;

import de.nitolia.event.TCore;
import de.nitolia.event.Utils;
import de.nitolia.event.database.entities.Announcement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class AnnounceDel extends Command {
    public AnnounceDel() {
        super("anndel", "event.ann", "annd");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(TCore.BASIC_INFO().append("Benutzung: /annd <ID>").create());
        } else if (!Utils.isInt(args[0])) {
            sender.sendMessage(TCore.BASIC_INFO().append("Du musst eine Zahl als ID angeben.").create());
        } else {
            int id = Integer.parseInt(args[0]);
            Announcement a = TCore.getInst().getDatabase().find(Announcement.class).where().eq("id", id).findUnique();
            if (a == null) {
                sender.sendMessage(TCore.BASIC_INFO().append("Die Nachricht mit der ID ").append(id + "")
                        .color(ChatColor.GOLD).append(" wurde nicht gefunden.").color(ChatColor.WHITE).create());
                return;
            }
            TCore.getInst().getAnnouncer().stop(a);
            a.delete();
            sender.sendMessage(TCore.BASIC_INFO().append("Die Nachricht mit der ID ").append(id + "")
                    .color(ChatColor.GOLD).append(" wurde entfernt.").color(ChatColor.WHITE).create());
        }
    }
}
