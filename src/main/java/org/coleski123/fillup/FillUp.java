package org.coleski123.fillup;

import org.bukkit.*;
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
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public final class FillUp extends JavaPlugin implements Listener {
    private ContainerHolograms containerHolograms;
    private ParticalEffects particalEffects;
    private ChatMessages chatMessages;
    private Sounds playerSounds;

    @Override
    public void onEnable() {
        String pluginConsolePrefix = ChatColor.DARK_PURPLE + "[FillUp]";
        sendConsoleMessage(pluginConsolePrefix + ChatColor.GREEN + " FillUp has been enabled!");

        int pluginId = 19876; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        // Ensure the config.yml file exists and load it
        this.saveDefaultConfig();
        config = getConfig(); // Load the configuration

        getServer().getPluginManager().registerEvents(this, this);

        // Initialize the ContainerHolograms instance with the plugin
        containerHolograms = new ContainerHolograms(this);

        particalEffects = new ParticalEffects(this);

        chatMessages = new ChatMessages(this);

        playerSounds = new Sounds(this);

        new UpdateChecker(this, 112726).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                sendConsoleMessage(pluginConsolePrefix + ChatColor.GREEN + " No new versions available.");
            } else {
                sendConsoleMessage(pluginConsolePrefix + ChatColor.RED + " A new version is now available! Download: https://www.spigotmc.org/resources/instasmelt.112726//");
            }
        });
    }

    @Override
    public void onDisable() {
        String pluginPrefix = ChatColor.DARK_PURPLE + "[FillUp]";
        sendConsoleMessage(pluginPrefix + ChatColor.RED + " FillUp has been disabled!");
    }

    // Load the plugins configuration
    FileConfiguration config = getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = config.getString("Prefix").replace('&', '§');

        if (cmd.getName().equalsIgnoreCase("fillup") && sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the player has the "fillup.use" permission
            if (!player.hasPermission("fillup.use")) {
                chatMessages.ChatMessage5(player);
                playerSounds.FailureSound(player);
                    return true;
                }

                if (args.length == 1 && args[0].equalsIgnoreCase("saveconfig")) {
                    // Handle the "/fillup saveconfig" command here
                    reloadConfig();
                    config = getConfig(); // Get the updated configuration
                    chatMessages.saveConfigMessage(player);
                    playerSounds.SaveConfigSound(player);
                    return true;
                }

                // Check if the player specified a valid material and amount
                if (args.length < 2) {
                    chatMessages.usageMessage(player);
                    return true;
                }

                Material material;
                int amount;

                try {
                    material = Material.valueOf(args[0].toUpperCase()); // Convert the item name to uppercase
                } catch (IllegalArgumentException e) {
                    chatMessages.invalidItemName(player);
                    return true;
                }

                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    chatMessages.invalidAmount(player);
                    return true;
                }

                Block targetBlock = player.getTargetBlockExact(5);
                if (targetBlock != null && targetBlock.getState() instanceof org.bukkit.block.Container) {
                // Get the containers inventory
                Inventory chestInventory = ((org.bukkit.block.Container) targetBlock.getState()).getInventory();

                    Material blockMaterial = targetBlock.getType();
                    String ContainerName = blockMaterial.name().toLowerCase().replace("_", " ");
                    ContainerName = ContainerName.substring(0, 1).toUpperCase() + ContainerName.substring(1);

                    // Calculate how many items can fit in the chest
                    int availableSpace = calculateAvailableSpace(chestInventory, material);

                    if (availableSpace > 0) {
                        int actualAmount = Math.min(amount, availableSpace);
                        String originalAmount = String.valueOf(actualAmount);
                        // Create the item stack
                        ItemStack itemStack = new ItemStack(material, actualAmount);
                        chestInventory.addItem(itemStack);
                            if (actualAmount < amount) {
                                String originalAmount2 = String.valueOf(amount);
                                chatMessages.ChatMessage2(material, ContainerName, originalAmount, originalAmount2, player);
                                particalEffects.fireworkFunc(player);
                                playerSounds.SuccessSound(player);
                            } else {
                                chatMessages.ChatMessage1(ContainerName, originalAmount, material, player);
                                particalEffects.fireworkFunc(player);
                                playerSounds.SuccessSound(player);
                            }
                        playerSounds.SuccessSound(player);
                        if (actualAmount < amount) {
                            String originalAmount2 = String.valueOf(amount);
                            containerHolograms.displayContainerMessage2(player, prefix, originalAmount, originalAmount2, ContainerName, material, targetBlock);
                            particalEffects.fireworkFunc(player);
                        } else {
                            containerHolograms.displayContainerMessage1(player, prefix, originalAmount, ContainerName, material, targetBlock);
                            particalEffects.fireworkFunc(player);
                        }
                    } else {
                        chatMessages.ChatMessage3(ContainerName, player);
                        playerSounds.FailureSound(player);
                        containerHolograms.displayContainerMessage3(player, prefix, ContainerName, targetBlock);
                        playerSounds.FailureSound(player);
                    }
                } else {
                        chatMessages.ChatMessage4(player);
                        playerSounds.FailureSound(player);
                        containerHolograms.displayContainerScreenMessage4(player, prefix);
                        playerSounds.FailureSound(player);
                }
                return true;
            }
            return false;
        }

        //Handler to initialize player looking at container
        @EventHandler
        public void onPlayerInteract (PlayerInteractEvent event){
        }

    //Prevent the ArmorStand from being broken
        @EventHandler
        public void onEntityDamage (EntityDamageEvent event){
            if (event.getCause() == DamageCause.ENTITY_ATTACK && event.getEntity() instanceof ArmorStand) {
                event.setCancelled(true); // Prevent the Armor Stand from taking damage
                event.getEntity().remove(); // Remove the Armor Stand
            }
        }

        //calculate available space in an inventory for a specific material
        private int calculateAvailableSpace (Inventory inventory, Material material){
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

        private void sendConsoleMessage(String message) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }