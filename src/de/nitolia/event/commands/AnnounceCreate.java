package de.nitolia.event.commands;

import de.nitolia.event.TCore;
import de.nitolia.event.Utils;
import de.nitolia.event.database.entities.Announcement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;

public class AnnounceCreate extends Command {
    public AnnounceCreate() {
        super("anncreate", "event.ann", "annc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(TCore.BASIC_INFO().append("Benutzung: /annc <Intervall> <Text>").create());
        } else if (!Utils.isInt(args[0])) {
            sender.sendMessage(TCore.BASIC_INFO().append("Du musst eine Zahl als Intervall angeben (In Minuten).").create());
        } else {
            int interval = Integer.parseInt(args[0]);
            String text = StringUtils.join(args, ' ', 1, args.length);
            Announcement a = new Announcement();
            a.setTime(interval);
            a.setMsg(text);
            a.save();
            try {
                TCore.getInst().getAnnouncer().announce(a);
            } catch (Exception e) {
                sender.sendMessage(TCore.SUB_INFO().append("Der Braodcast wurde aufgrund fehlerhaften JSONs nicht gestartet.").create());
            }
            sender.sendMessage(TCore.BASIC_INFO().append("Die Nachricht wurde erstellt und hat die ID ").append(a.getId() + "")
                    .color(ChatColor.GOLD).append(". Nutze /annadd " + a.getId() + " um die Nachricht zu verlängern")
                    .color(ChatColor.WHITE).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/annadd " + a.getId())).create());
        }
    }
}