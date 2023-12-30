package org.coleski123.fillup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChatMessages {
    private final FillUp plugin;

    public ChatMessages(FillUp plugin) {
        this.plugin = plugin;
    }

    public String Prefix(){
        return plugin.config.getString("Prefix").replace('&', '§');
    }

    public Boolean DisplayChatMessages(){
        return plugin.getConfig().getBoolean("Options.ChatMessages");
    }

    public void ChatMessage1(String ContainerName, String originalAmount, Material material, Player player) {
        if (DisplayChatMessages()) {
            String message1 = plugin.config.getString("ChatMessages.Message1")
                    .replace("&", "§")
                    .replace("{ITEM}", material.name().toLowerCase().replace("_", " "))
                    .replace("{AMOUNT1}", originalAmount)
                    .replace("{CONTAINERNAME}", ContainerName);
            player.sendMessage(Prefix() + message1);
        }
    }

    public void ChatMessage2(Material material, String ContainerName, String originalAmount, String originalAmount2, Player player) {
        if (DisplayChatMessages()) {
            String message2 = plugin.config.getString("ChatMessages.Message2")
                    .replace("&", "§")
                    .replace("{AMOUNT1}", originalAmount)
                    .replace("{AMOUNT2}", originalAmount2)
                    .replace("{CONTAINERNAME}", ContainerName)
                    .replace("{ITEM}", material.name().toLowerCase().replace("_", " "));
            player.sendMessage(Prefix() + message2);
        }
    }

    public void ChatMessage3(String ContainerName, Player player) {
        if (DisplayChatMessages()) {
            String message3 = plugin.config.getString("ChatMessages.Message3")
                    .replace("&", "§")
                    .replace("{CONTAINERNAME}", ContainerName);
            player.sendMessage(Prefix() + message3);
        }
    }

    public void ChatMessage4(Player player) {
        if (DisplayChatMessages()) {
            String message4 = plugin.config.getString("ChatMessages.Message4").replace("&", "§");
            player.sendMessage(Prefix() + message4);
        }
    }

    public void ChatMessage5(Player player) {
        String message5 = plugin.config.getString("ChatMessages.Message5").replace("&", "§");
        player.sendMessage(Prefix() + message5);
    }

    public void saveConfigMessage(Player player) {
        player.sendMessage(Prefix() + ChatColor.GREEN + "Configuration saved.");
    }

    public void usageMessage(Player player){
        player.sendMessage(Prefix() + ChatColor.RED + "Usage: /fillup <item_name> <amount>");
    }

    public void invalidItemName(Player player){
        player.sendMessage(Prefix() + ChatColor.RED + "Invalid item name.");
    }

    public void invalidAmount(Player player){
        player.sendMessage(Prefix() + ChatColor.RED + "Invalid amount.");
    }

}