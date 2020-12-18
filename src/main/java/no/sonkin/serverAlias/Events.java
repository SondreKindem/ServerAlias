package no.sonkin.serverAlias;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;

public class Events implements Listener {

    Configuration config;

    String loginMessage;
    HashMap<String, String> serverMessages = new HashMap<>();

    public Events(Configuration config) {
        this.config = config;

        // Parse messages
        loginMessage = config.getString("join-msg");

        for (String server : config.getSection("server-join-msg").getKeys()) {
            serverMessages.put(server, config.getString("server-join-msg." + server));
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        player.sendMessage(new ComponentBuilder(loginMessage).create());
    }


    @EventHandler
    public void onServerJoin(ServerConnectedEvent event) {
        if (serverMessages != null && serverMessages.size() > 0) {
            ProxiedPlayer player = event.getPlayer();
            String serverName = event.getServer().getInfo().getName();

            if (serverMessages.containsKey(serverName)) {
                player.sendMessage(new ComponentBuilder(serverMessages.get(serverName)).create());
            }
        }
    }
}
