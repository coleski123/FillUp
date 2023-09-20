package org.coleski123.fillup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class FillUp extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("ChestFillPlugin has been enabled!");

        // Ensure the config.yml file exists and load it
        this.saveDefaultConfig();
        config = getConfig(); // Load the configuration

        getServer().getPluginManager().registerEvents(this, this);

//        new UpdateChecker(this, 109263).getVersion(version -> {
//            if (this.getDescription().getVersion().equals(version)) {
//                getLogger().info("No new versions available.");
//            } else {
//                getLogger().info("A new version is now available! Download: https://www.spigotmc.org/resources/instasmelt.109263//");
//            }
//        });
    }

    @Override
    public void onDisable() {
        getLogger().info("ChestFillPlugin has been disabled!");
    }

    // Load your plugin's configuration file (assuming your plugin is using Bukkit/Spigot)
    FileConfiguration config = getConfig(); // This assumes you are in your main plugin class.


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = config.getString("Messages.Prefix").replace('&', '§');

        if (cmd.getName().equalsIgnoreCase("fillup") && sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the player has the "fillup.use" permission
            if (!player.hasPermission("fillup.use")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("saveconfig")) {
                // Handle the "/fillup saveconfig" command here
                reloadConfig();
                config = getConfig(); // Get the updated configuration
                player.sendMessage(ChatColor.GREEN + "Configuration saved.");
                Sound sound = Sound.ENTITY_PLAYER_LEVELUP;
                float volume = 0.10f;
                float pitch = 1.0f;
                player.playSound(player.getLocation(), sound, volume, pitch);
                return true;
            }

            // Check if the player specified a valid material and amount
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /fillup <item_name> <amount>");
                return true;
            }

            Material material;
            int amount;

            try {
                material = Material.valueOf(args[0].toUpperCase()); // Convert the item name to uppercase
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Invalid item name.");
                return true;
            }

            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid amount.");
                return true;
            }

            // Check if the player is looking at a chest
            Block targetBlock = player.getTargetBlockExact(5); // Change the range as needed
            if (targetBlock != null && (targetBlock.getType() == Material.CHEST ||
                    targetBlock.getType() == Material.TRAPPED_CHEST ||
                    targetBlock.getType() == Material.CHEST_MINECART ||
                    targetBlock.getType() == Material.FURNACE ||
                    targetBlock.getType() == Material.FURNACE_MINECART ||
                    targetBlock.getType() == Material.BLAST_FURNACE ||
                    targetBlock.getType() == Material.SMOKER ||
                    targetBlock.getType() == Material.HOPPER ||
                    targetBlock.getType() == Material.HOPPER_MINECART ||
                    targetBlock.getType() == Material.DROPPER ||
                    targetBlock.getType() == Material.DISPENSER ||
                    targetBlock.getType() == Material.BARREL)) {
                // Get the chest's inventory
                Inventory chestInventory = ((org.bukkit.block.Container) targetBlock.getState()).getInventory();

                // Calculate how many items can fit in the chest
                int availableSpace = calculateAvailableSpace(chestInventory, material);

                if (availableSpace > 0) {
                    // Calculate the actual amount to fill based on available space
                    int actualAmount = Math.min(amount, availableSpace);

                    // Create the item stack
                    ItemStack itemStack = new ItemStack(material, actualAmount);

                    String originalAmount = String.valueOf(actualAmount);
                    String originalAmount2 = String.valueOf(amount);
                    String message1 = config.getString("Messages.Message1")
                            .replace("&", "§")
                            .replace("{ITEM}", material.name().toLowerCase().replace("_", " "))
                            .replace("{AMOUNT1}", originalAmount);
                    chestInventory.addItem(itemStack);
                    player.sendMessage(prefix + message1 );
                    Sound sound = Sound.BLOCK_NOTE_BLOCK_BIT;
                    float volume = 0.10f;
                    float pitch = 2.0f;
                    player.playSound(player.getLocation(), sound, volume, pitch);

                    if (actualAmount < amount) {
                        String message2 = config.getString("Messages.Message2")
                                .replace("&", "§")
                                .replace("{AMOUNT1}", originalAmount)
                                .replace("{AMOUNT2}", originalAmount2);
                        player.sendMessage(prefix + message2);
                    }
                } else {
                    String message3 = config.getString("Messages.Message3")
                            .replace("&", "§");
                    player.sendMessage(prefix + message3);
                    Sound sound = Sound.BLOCK_NOTE_BLOCK_BIT;
                    float volume = 0.10f;
                    float pitch = 1.0f;
                    player.playSound(player.getLocation(), sound, volume, pitch);
                }
            } else {
                String message4 = config.getString("Messages.Message4")
                        .replace("&", "§");
                player.sendMessage(prefix + message4);
                Sound sound = Sound.BLOCK_NOTE_BLOCK_BIT;
                float volume = 0.10f;
                float pitch = 1.0f;
                player.playSound(player.getLocation(), sound, volume, pitch);
            }

            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // You can add more functionality here if needed
    }

    // Helper method to calculate available space in an inventory for a specific material
    private int calculateAvailableSpace(Inventory inventory, Material material) {
        int availableSpace = 0;
        for (ItemStack stack : inventory.getContents()) {
            if (stack == null || stack.getType() == Material.AIR) {
                availableSpace += material.getMaxStackSize();
            } else if (stack.getType() == material) {
                availableSpace += material.getMaxStackSize() - stack.getAmount();
            }
        }
        return availableSpace;
    }
}
