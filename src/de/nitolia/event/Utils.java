package de.nitolia.event;

import de.nitolia.event.commands.Shutdown;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static Timestamp currTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static ServerInfo bestServer(ProxiedPlayer player) {
        ServerInfo best = TCore.getInst().getReconnectHandler().getServer(player);
        for (ServerInfo s : BungeeCord.getInstance().getServers().values()) {
            if (Shutdown.downServer.contains(s)) continue;
            if (s.getPlayers().size() < 50 && s.getPlayers().size() > best.getPlayers().size()) {
                best = s;
            } else if (best.getPlayers().size() > 50 && s.getPlayers().size() < best.getPlayers().size()) {
                best = s;
            }
        }
        return best;
    }

    public static String formatTimeGap(Timestamp timestamp) {
        long time = timestamp.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(time);
        time -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(time);
        time -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" Tag(e) ");
        }
        if (hours > 0) {
            sb.append(hours).append(" Stunde(n) ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" Minute(n) ");
        }
        if (seconds > 0) {
            sb.append(seconds).append(" Sekunde(n)");
        }
        return sb.toString();
    }

    public static String formatDate(Timestamp time) {
        SimpleDateFormat format = new SimpleDateFormat();
        return format.format(new Date(time.getTime()));
    }

    public static Timestamp getTimestampForVariable(String var) {
        Calendar result = Calendar.getInstance();
        result.setTimeInMillis(System.currentTimeMillis());
        if (isInt(var)) {
            result.add(Calendar.SECOND, Integer.parseInt(var));
            return new Timestamp(result.getTimeInMillis());
        }
        String time = var.substring(0, var.length() - 1);
        if (isInt(time)) {
            if (var.endsWith("s")) {
                result.add(Calendar.SECOND, Integer.parseInt(time));
                return new Timestamp(result.getTimeInMillis());
            } else if (var.endsWith("m")) {
                result.add(Calendar.MINUTE, Integer.parseInt(time));
                return new Timestamp(result.getTimeInMillis());
            } else if (var.endsWith("h")) {
                result.add(Calendar.HOUR, Integer.parseInt(time));
                return new Timestamp(result.getTimeInMillis());
            } else if (var.endsWith("d")) {
                result.add(Calendar.DATE, Integer.parseInt(time));
                return new Timestamp(result.getTimeInMillis());
            }
        }
        throw new IllegalArgumentException("Invalid parameter " + var);
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}