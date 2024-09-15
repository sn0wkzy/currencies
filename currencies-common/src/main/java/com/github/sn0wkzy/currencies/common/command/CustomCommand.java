package com.github.sn0wkzy.currencies.common.command;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class CustomCommand extends Command {

    private final Set<CustomCommand> subCommands = new HashSet<>();
    private final String permission;
    private final boolean playerOnly;

    public CustomCommand(String name, String permission, boolean playerOnly, String... aliases) {
        super(name);

        if (aliases.length > 0) {
            setAliases(Lists.newArrayList(aliases));
        }

        if (permission != null) {
            setPermission(permission);
            setPermissionMessage("§cVocê não tem permissão para usar este comando.");
        }

        setUsage("§cUtilize /" + name);

        this.permission = permission;
        this.playerOnly = playerOnly;
    }

    protected abstract void onCommand(CommandSender commandSender, String[] arguments);

    public void executeRaw(CommandSender commandSender, String s, String[] strings) {
        if (playerOnly && !(commandSender instanceof Player)) {
            commandSender.sendMessage("§cApenas jogadores podem executar este comando.");
            return;
        }

        if (permission != null && !testPermissionSilent(commandSender)) {
            commandSender.sendMessage("§cVocê não tem permissão para executar este comando.");
            return;
        }

        if (strings.length > 0) {
            CustomCommand subCommandFound = null;
            for (CustomCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(strings[0])
                        || subCommand.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                    subCommandFound = subCommand;
                    break;
                }
            }

            if (subCommandFound != null) {
                subCommandFound.executeRaw(commandSender, s, Arrays.copyOfRange(strings, 1, strings.length));
                return;
            }
        }

        onCommand(commandSender, strings);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        executeRaw(commandSender, s, strings);
        return true;
    }

    public void registerSubCommands(CustomCommand... customCommands) {
        subCommands.addAll(Arrays.asList(customCommands));
    }
}