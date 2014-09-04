package de.nitolia.event;

import de.nitolia.event.database.entities.BanTable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(LoginEvent event) {
        List<BanTable> bans = TCore.getInst().getDatabase().find(BanTable.class).where().eq("player", event.getConnection().getUniqueId()).findList();
        List<String> msgs = new ArrayList<>();
        Timestamp longest = new Timestamp(0);
        for (BanTable ban : bans) {
            if (ban.calcActive()) {
                msgs.add(ban.getReason());
                if (ban.getEnd() == null || longest == null) {
                    longest = null;
                    continue;
                }
                if (longest.before(ban.getEnd())) {
                    longest = ban.getEnd();
                }
            }
        }
        if (msgs.size() == 0) return;

        ComponentBuilder base;
        if (longest == null) {
            base = TCore.BANNED_FOREVER();
        } else {
            base = TCore.BANNED_BASE().append("Du bist gebannt bis: " + Utils.formatDate(longest));
        }
        base.append("\n").color(ChatColor.GRAY);
        for (String s : msgs) {
            base.append("\n").append(s).italic(true);
        }
        event.setCancelled(true);
        event.setCancelReason("Banned");
        event.getConnection().disconnect(base.create());
    }
}