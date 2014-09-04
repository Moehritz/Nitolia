package de.nitolia.event;

import lombok.AllArgsConstructor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class Config {
    private File configFile;

    public Configuration reload() {
        try {
            return load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Configuration load(File file) throws IOException {
        this.configFile = file;

        if (!file.exists()) {
            saveDefaultValues(file);
        }

        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

    }

    public void saveDefaultValues(File file) throws IOException {
        file.createNewFile();

        InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            int r = in.read();
            while (r != -1) {
                fos.write(r);
                r = in.read();
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
