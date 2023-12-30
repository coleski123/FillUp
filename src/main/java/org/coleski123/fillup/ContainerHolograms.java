package org.coleski123.fillup;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ContainerHolograms {

    private final FillUp plugin;

    public ContainerHolograms(FillUp plugin) {
        this.plugin = plugin;
    }

    public Boolean DisplayContainerMessages(){
        return plugin.getConfig().getBoolean("Options.ContainerMessages", true);
    }

    public void displayContainerMessage1(Player player, String prefix, String originalAmount, String ContainerName, Material material, Block targetBlock) {
        if (DisplayContainerMessages()) {
            String containerMSG1 = plugin.getConfig().getString("ContainerMessages.Message1");

            // Display the message above the container "targetBlock"
            String message = prefix + containerMSG1;
            message = message.replace("{AMOUNT1}", originalAmount)
                    .replace("{ITEM}", material.name().toLowerCase().replace("_", " "))
                    .replace("{CONTAINERNAME}", ContainerName)
                    .replace("&", "§");
            hologramPlacement(player, message, targetBlock);
        }
    }

    public void displayContainerMessage2(Player player, String prefix, String originalAmount, String originalAmount2, String ContainerName, Material material, Block targetBlock) {
            if (DisplayContainerMessages()) {
                String containerMSG1 = plugin.getConfig().getString("ContainerMessages.Message2");

                // Display the message above the container "targetBlock"
                String message2 = prefix + containerMSG1;
                message2 = message2.replace("{AMOUNT1}", originalAmount)
                        .replace("{AMOUNT2}", originalAmount2)
                        .replace("{ITEM}", material.name().toLowerCase().replace("_", " "))
                        .replace("{CONTAINERNAME}", ContainerName)
                        .replace("&", "§");
                hologramPlacement(player, message2, targetBlock);
            }
    }

    public void displayContainerMessage3(Player player, String prefix, String ContainerName, Block targetBlock) {
        if (DisplayContainerMessages()) {
            String containerMSG1 = plugin.getConfig().getString("ContainerMessages.Message3");

            // Display the message above the container "targetBlock"
            String message2 = prefix + containerMSG1;
            message2 = message2.replace("&", "§")
                    .replace("{CONTAINERNAME}", ContainerName);
            hologramPlacement(player, message2, targetBlock);
        }
    }

    public void displayContainerScreenMessage4(Player player, String prefix) {
        if (DisplayContainerMessages()) {
            String containerMSG1 = plugin.getConfig().getString("ContainerMessages.Message4");

            // Display the message above the container "targetBlock"
            String message2 = prefix + containerMSG1;
            message2 = message2.replace("&", "§");
            player.sendTitle("", message2, 10, 70, 20);
        }
    }

    public void hologramPlacement(Player player, String text, Block targetBlock) {
        Location chestLocation = targetBlock.getLocation().add(0.5, 0.5, 0.5); // Center of the chest
        Location hologramLocation = chestLocation.clone().add(0, 1.0, 0); // Offset it slightly above the chest
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(hologramLocation, EntityType.ARMOR_STAND);

        // Customize the Armor Stand
        armorStand.setVisible(false); // Hide the Armor Stand's body
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false); // Prevent it from falling
        armorStand.setSmall(true); // Make it smaller if desired
        armorStand.setMarker(true); // Make it not collide with players

        // Schedule the Armor Stand for removal after a few seconds (adjust the time as needed)
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            armorStand.remove();
        }, 100L); // 100 ticks ≈ 5 seconds
    }
}
