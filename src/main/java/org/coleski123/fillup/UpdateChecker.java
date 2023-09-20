package org.coleski123.fillup;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final int resourceId;

    // Constructor that initializes the plugin and resource ID.
    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    // This method checks for the latest version of the plugin asynchronously.
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "&time=" + System.currentTimeMillis()).openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                // Log an error message if there's an issue with the update check.
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }
}
