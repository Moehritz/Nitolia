package de.nitolia.event;

import de.nitolia.event.database.entities.Announcement;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Announcer {

    private Map<Integer, ScheduledTask> tasks = new HashMap<>();

    public Announcer() {
        for (Announcement a : TCore.getInst().getDatabase().find(Announcement.class).findList()) {
            announce(a);
        }
    }

    public void announce(Announcement a) {
        final BaseComponent[] component = ComponentSerializer.parse(a.getMsg());
        ScheduledTask task = ProxyServer.getInstance().getScheduler().schedule(TCore.getInst(), new Runnable() {
            @Override
            public void run() {
                ProxyServer.getInstance().broadcast(component);
            }
        }, 1, a.getTime(), TimeUnit.MINUTES);
        tasks.put(a.getId(), task);
    }

    public void restart(Announcement a) {
        stop(a);
        announce(a);
    }

    public void stop(Announcement a) {
        tasks.remove(a.getId()).cancel();
    }
}