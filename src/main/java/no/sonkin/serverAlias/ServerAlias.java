package no.sonkin.serverAlias;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import no.sonkin.serverAlias.commands.AliasCommand;
import no.sonkin.serverAlias.commands.ReloadCommand;

import java.io.*;
import java.util.Collection;

/*
TODO: add conditional spawning - if player is in a server portal, spawn at server spawn instead of inside the portal
TODO: add custom rules command - let admin specify general rules & also server specific rules. Let player click text to see server specific rules if server not specified in command
TODO: add custom info command module - Let admin define commands that return defined info
 */

@SuppressWarnings({"ResultOfMethodCallIgnored", "UnstableApiUsage"})
public class ServerAlias extends Plugin {

    private Configuration config;
    private Events eventsListener;

    @Override
    public void onEnable() {
        loadConfig();

        this.eventsListener = new Events(config);

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
        getProxy().getPluginManager().registerListener(this, eventsListener);
    }

    public void reload() {
        getProxy().getPluginManager().unregisterListener(eventsListener);
        this.loadConfig();
        this.eventsListener = new Events(config);
        getProxy().getPluginManager().registerListener(this, eventsListener);
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

            ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand(this));
        }
    }
}
