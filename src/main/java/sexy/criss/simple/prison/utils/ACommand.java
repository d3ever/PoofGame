/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/28/2018 - 23:38 Tuesday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.manager.Reference;

import java.lang.reflect.Field;
import java.util.List;

public abstract class ACommand implements CommandExecutor {
    private String command;
    private String description;
    private List<String> alias;
    private String usage;
    private String permission;
    private String permMessage;
    private static CommandMap cmap;
    private boolean console_allow = true;

    public ACommand(String command) {
        this(command, null, null, null, null);
    }

    public ACommand(String command, String... alias) {
        this(command, null, null, null ,Lists.newArrayList(alias));
    }

    private ACommand(String command, String usage, String description, String permissionMessage, List<String> aliases) {
        this.command = command.toLowerCase();
        this.usage = usage;
        this.description = description;
        this.permMessage = permissionMessage;
        this.alias = aliases;
    }

    public void register() {
        ReflectCommand cmd = new ReflectCommand(this.command);

        if (this.alias != null) cmd.setAliases(this.alias);
        if (this.description != null) cmd.setDescription(this.description);
        if (this.usage != null) cmd.setUsage(this.usage);
        if (this.permMessage != null) cmd.setPermissionMessage(this.permMessage);

        this.getCommandMap().register("", cmd);
        cmd.setExecutor(this);
    }

    private CommandMap getCommandMap() {
        if (cmap == null) {
            try {
                Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return this.getCommandMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else return cmap;
        return this.getCommandMap();
    }

    protected void unavailableFromConsole() {
        this.console_allow = false;
    }

    protected void unavailableWithoutPermission(String permission) {
        this.permission = permission;
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);

    private class ReflectCommand extends Command {
        private ACommand exe;

        private ReflectCommand(String command) {
            super(command);
            this.exe = null;
        }

        private void setExecutor(ACommand exe) {
            this.exe = exe;
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (this.exe != null) {
                if(!(this.exe.console_allow) && !(sender instanceof Player)) {
                    sender.sendMessage("Players command only.");
                    return false;
                }

                if((sender instanceof Player) && (this.exe.permission != null)) {
                    Player p = (Player) sender;
                    if(!(p.hasPermission(this.exe.permission)) || !(p.isOp())) {
                        p.sendMessage(Reference.PERMISSIONS_ERROR.get());
                        return true;
                    }
                }
                return this.exe.onCommand(sender, this, commandLabel, args);
            }
            return false;
        }
    }
}
