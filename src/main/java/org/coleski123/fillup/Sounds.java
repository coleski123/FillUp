package org.coleski123.fillup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {
    private final FillUp plugin;
    public Sounds(FillUp plugin) {
        this.plugin = plugin;
    }

    public void SuccessSound(Player player) {
        boolean pluginSounds = plugin.getConfig().getBoolean("Options.PluginSounds");
        if (pluginSounds) {
            Sound sound = Sound.BLOCK_NOTE_BLOCK_BIT;
            float volume = 0.10f;
            float pitch = 2.0f;
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void FailureSound(Player player) {
        boolean pluginSounds = plugin.getConfig().getBoolean("Options.PluginSounds");
        if (pluginSounds) {
            Sound sound = Sound.BLOCK_NOTE_BLOCK_BIT;
            float volume = 0.10f;
            float pitch = 1.0f;
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void SaveConfigSound(Player player) {
        boolean pluginSounds = plugin.getConfig().getBoolean("Options.PluginSounds");
        if (pluginSounds) {
            Sound sound = Sound.ENTITY_PLAYER_LEVELUP;
            float volume = 0.10f;
            float pitch = 1.0f;
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
