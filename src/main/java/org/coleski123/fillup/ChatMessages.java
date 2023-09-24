package org.coleski123.fillup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChatMessages {
    private final FillUp plugin;

    public ChatMessages(FillUp plugin) {
        this.plugin = plugin;
    }

    public void ChatMessage1(String ContainerName, String originalAmount, Material material, Player player) {
        boolean shoulddisplayChatMessages = plugin.getConfig().getBoolean("Options.ChatMessages");
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        if (shoulddisplayChatMessages) {
            String message1 = plugin.config.getString("ChatMessages.Message1")
                    .replace("&", "§")
                    .replace("{ITEM}", material.name().toLowerCase().replace("_", " "))
                    .replace("{AMOUNT1}", originalAmount)
                    .replace("{CONTAINERNAME}", ContainerName);
            player.sendMessage(prefix + message1);
        }
    }

    public void ChatMessage2(Material material, String ContainerName, String originalAmount, String originalAmount2, Player player) {
        boolean shoulddisplayChatMessages = plugin.getConfig().getBoolean("Options.ChatMessages");
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        if (shoulddisplayChatMessages) {
            String message2 = plugin.config.getString("ChatMessages.Message2")
                    .replace("&", "§")
                    .replace("{AMOUNT1}", originalAmount)
                    .replace("{AMOUNT2}", originalAmount2)
                    .replace("{CONTAINERNAME}", ContainerName)
                    .replace("{ITEM}", material.name().toLowerCase().replace("_", " "));
            player.sendMessage(prefix + message2);
        }
    }

    public void ChatMessage3(String ContainerName, Player player) {
        boolean shoulddisplayChatMessages = plugin.getConfig().getBoolean("Options.ChatMessages");
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        if (shoulddisplayChatMessages) {
            String message3 = plugin.config.getString("ChatMessages.Message3")
                    .replace("&", "§")
                    .replace("{CONTAINERNAME}", ContainerName);
            player.sendMessage(prefix + message3);
        }
    }

    public void ChatMessage4(Player player) {
        boolean shoulddisplayChatMessages = plugin.getConfig().getBoolean("Options.ChatMessages");
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        if (shoulddisplayChatMessages) {
            String message4 = plugin.config.getString("ChatMessages.Message4").replace("&", "§");
            player.sendMessage(prefix + message4);
        }
    }

    public void ChatMessage5(Player player) {
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        String message5 = plugin.config.getString("ChatMessages.Message5").replace("&", "§");
        player.sendMessage(prefix + message5);
    }

    public void saveConfigMessage(Player player) {
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        player.sendMessage(prefix + ChatColor.GREEN + "Configuration saved.");
    }

    public void usageMessage(Player player){
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        player.sendMessage(prefix + ChatColor.RED + "Usage: /fillup <item_name> <amount>");
    }

    public void invalidItemName(Player player){
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        player.sendMessage(prefix + ChatColor.RED + "Invalid item name.");
    }

    public void invalidAmount(Player player){
        String prefix = plugin.config.getString("Prefix").replace('&', '§');
        player.sendMessage(prefix + ChatColor.RED + "Invalid amount.");
    }

}