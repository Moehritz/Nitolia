package de.nitolia.event.commands;

import de.nitolia.event.TCore;
import de.nitolia.event.Utils;
import de.nitolia.event.database.entities.Announcement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;

public class AnnounceAdd extends Command {
    public AnnounceAdd() {
        super("annadd", "event.ann", "anna");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(TCore.BASIC_INFO().append("Benutzung: /anna <ID> <Text>").create());
        } else if (!Utils.isInt(args[0])) {
            sender.sendMessage(TCore.BASIC_INFO().append("Du musst eine Zahl als ID angeben.").create());
        } else {
            int id = Integer.parseInt(args[0]);
            String text = StringUtils.join(args, ' ', 1, args.length);
            Announcement a = TCore.getInst().getDatabase().find(Announcement.class).where().eq("id", id).findUnique();
            if (a == null) {
                sender.sendMessage(TCore.BASIC_INFO().append("Die Nachricht mit der ID ").append(id + "")
                        .color(ChatColor.GOLD).append(" wurde nicht gefunden.").color(ChatColor.WHITE).create());
                return;
            }
            a.setMsg(a.getMsg() + text);
            a.update();
            try {
                TCore.getInst().getAnnouncer().restart(a);
            } catch (Exception e) {
                sender.sendMessage(TCore.SUB_INFO().append("Der Braodcast wurde aufgrund fehlerhaften JSONs nicht gestartet.").create());
            }
            sender.sendMessage(TCore.BASIC_INFO().append("Der Text wurde hinzugefügt!").create());
        }
    }
}
