package de.nitolia.event.database;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import de.nitolia.event.database.entities.Announcement;
import de.nitolia.event.database.entities.BanTable;

public class DatabaseHandler {

    public static EbeanServer connect(String host, int port, String database, String user, String pass) {
        DataSourceConfig mysqlDb = new DataSourceConfig();
        mysqlDb.setDriver("com.mysql.jdbc.Driver");
        mysqlDb.setUsername(user);
        mysqlDb.setPassword(pass);
        mysqlDb.setUrl(String.format("jdbc:mysql://%s:%s/%s", host, port, database));
        mysqlDb.setHeartbeatSql("SELECT 1 FROM dual");

        ServerConfig config = new ServerConfig();
        config.setName(host + database);
        config.setDataSourceConfig(mysqlDb);
        config.setDdlGenerate(true);
        config.setDdlRun(false);
        config.setRegister(false);
        config.setDefaultServer(false);

        config.addClass(Announcement.class);
        config.addClass(BanTable.class);

        return EbeanServerFactory.create(config);
    }
}