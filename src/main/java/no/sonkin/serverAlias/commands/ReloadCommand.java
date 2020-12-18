package no.sonkin.serverAlias.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import no.sonkin.serverAlias.ServerAlias;

public class ReloadCommand extends Command {
    ServerAlias serverAliasInstance;

    public ReloadCommand(ServerAlias serverAliasInstance) {
        super("reloadutils");
        this.serverAliasInstance = serverAliasInstance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender.hasPermission("bungeeutils.reload")) {
            commandSender.sendMessage(new ComponentBuilder("Reloading config...").color(ChatColor.RED).create());
            serverAliasInstance.reload();
            commandSender.sendMessage(new ComponentBuilder("Config reloaded.").color(ChatColor.RED).create());
        } else {
            commandSender.sendMessage(new ComponentBuilder("You do not have permission to reload the config.").color(ChatColor.RED).create());
        }
    }
}
