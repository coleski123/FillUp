package org.coleski123.fillup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("ChestFillPlugin has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fillup") && sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the player has the "fillup.use" permission
            if (!player.hasPermission("fillup.use")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
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

                    // Fill the chest with the specified item
                    chestInventory.addItem(itemStack);
                    player.sendMessage(ChatColor.GREEN + "[FillUp] " + ChatColor.YELLOW + "Chest filled with " + actualAmount + " of " + ChatColor.WHITE + material.toString());

                    if (actualAmount < amount) {
                        player.sendMessage(ChatColor.GREEN + "[FillUp] " + ChatColor.YELLOW + "Note: Only " + actualAmount + " of " + amount + " items could fit in the chest.");
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "[FillUp] " + ChatColor.RED + "The chest does not have enough space for these items. This is my test push GOD DAMNIT");
                }
            } else {
                player.sendMessage(ChatColor.GREEN + "[FillUp] " + ChatColor.RED + "You must be looking at a chest to use this command.");
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
