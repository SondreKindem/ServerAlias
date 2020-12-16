package no.sonkin.serverAlias.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AliasCommand extends Command {

    private final String server;

    /**
     * Sends the player who executes the command to the specified server.
     *
     * @param name   The name of the command
     * @param server The name of the destination server
     */
    public AliasCommand(String name, String server) {
        super(name);
        this.server = server;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if ((sender instanceof ProxiedPlayer)) {
            ProxiedPlayer p = (ProxiedPlayer) sender;

            p.sendMessage(new ComponentBuilder("Sending you to " + this.server).color(ChatColor.GREEN).create());

            p.connect(ProxyServer.getInstance().getServerInfo(this.server));
        }
    }
}
