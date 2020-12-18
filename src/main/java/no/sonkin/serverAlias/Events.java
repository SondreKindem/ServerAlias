package no.sonkin.serverAlias;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

    Configuration config;

    public Events(Configuration config){
        this.config = config;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(new ComponentBuilder(config.getString("join-msg")).create());
        }
    }

    /*
    @EventHandler
    public void onServerJoin() {

    }
     */
}
