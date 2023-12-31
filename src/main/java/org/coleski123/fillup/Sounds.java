package org.coleski123.fillup;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {
    private final FillUp plugin;
    public Sounds(FillUp plugin) {
        this.plugin = plugin;
    }

    public boolean PluginSounds(){
        return plugin.getConfig().getBoolean("Options.PluginSounds");
    }

    public void SuccessSound(Player player) {
        if (PluginSounds()) {
            Sound sound = Sound.BLOCK_NOTE_BLOCK_BIT;
            float volume = 0.10f;
            float pitch = 2.0f;
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void FailureSound(Player player) {
        if (PluginSounds()) {
            Sound sound = Sound.BLOCK_NOTE_BLOCK_BIT;
            float volume = 0.10f;
            float pitch = 1.0f;
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void SaveConfigSound(Player player) {
        if (PluginSounds()) {
            Sound sound = Sound.ENTITY_PLAYER_LEVELUP;
            float volume = 0.10f;
            float pitch = 1.0f;
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
