package no.sonkin.serverAlias;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import no.sonkin.serverAlias.commands.AliasCommand;

import java.io.*;
import java.util.Collection;

public class ServerAlias extends Plugin {

    private Configuration config;

    @Override
    public void onEnable() {
        loadConfig();

        if (config.getBoolean("enable-messages")) {
            registerListeners();
        }

        registerCommands();
    }

    /**
     * Parse the config.yml file. Create it if it does not exist
     */
    private void loadConfig() {
        try {
            getLogger().info("Trying to load config");

            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            File config = new File(getDataFolder().getPath(), "config.yml");

            if (!config.exists()) {
                try {
                    config.createNewFile();
                    try (InputStream is = getResourceAsStream("config.yml");
                         OutputStream os = new FileOutputStream(config)) {
                        ByteStreams.copy(is, os);
                    }
                } catch (IOException e) {
                    getLogger().severe("Could not create the config!");
                    throw new RuntimeException("Could not create config!", e);
                }
            }

            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);

        } catch (IOException e) {
            getLogger().severe("Error while trying to load the config!");
            throw new RuntimeException("Could not load the config!", e);
        }
    }

    private void registerListeners() {
        getProxy().getPluginManager().registerListener(this, new Events(config));
    }

    private void registerCommands() {
        if (this.config != null) {
            Collection<String> servers = config.getSection("servers").getKeys();

            getLogger().info("Registering aliases");
            for (String server : servers) {
                for (Object alias : config.getList("servers." + server)) {
                    // Register an alias command for each alias for each server
                    ProxyServer.getInstance().getPluginManager().registerCommand(this, new AliasCommand((String) alias, server));
                }
            }
        }
    }
}
