package org.coleski123.fillup;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.List;

public class ParticalEffects {
    private final FillUp plugin;

    public ParticalEffects(FillUp plugin) {
        this.plugin = plugin;
    }

        public void fireworkFunc(Player player) {

            //Get the boolean for Effects
            boolean shoulduseEffects = plugin.config.getBoolean("Effects.DisplayEffects", true);
            List<String> effectColors = plugin.config.getStringList("Effects.EffectColors");

            //Check if enabled/disabled
            if (shoulduseEffects) {
                // Create red, white, and blue particle effects at the target block location
                Location targetLocation = player.getTargetBlock(null, 5).getLocation();

                int particleCount = 100;  // Number of particles
                float xOffset = 0.5f;    // Spread along the X-axis
                float yOffset = 0.5f;    // Spread along the Y-axis
                float zOffset = 0.5f;    // Spread along the Z-axis
                float speed = 0.1f;      // Speed of the animation

                for (String colorName : effectColors) {
                    Color color = getColorFromString(colorName);
                    // Create red particles
                    targetLocation.getWorld().spawnParticle(
                            Particle.REDSTONE,
                            targetLocation,
                            particleCount,
                            xOffset,
                            yOffset,
                            zOffset,
                            speed,
                            new Particle.DustOptions(color, 1)
                    );

                    // Create white particles
                    targetLocation.getWorld().spawnParticle(
                            Particle.REDSTONE,
                            targetLocation,
                            particleCount,
                            xOffset,
                            yOffset,
                            zOffset,
                            speed,
                            new Particle.DustOptions(color, 1)
                    );

                    // Create blue particles
                    targetLocation.getWorld().spawnParticle(
                            Particle.REDSTONE,
                            targetLocation,
                            particleCount,
                            xOffset,
                            yOffset,
                            zOffset,
                            speed,
                            new Particle.DustOptions(color, 1)
                    );
                }
            }
        }

    private Color getColorFromString(String colorName) {
        // Get all the fields (colors) defined in the Bukkit Color class
        for (java.lang.reflect.Field field : Color.class.getDeclaredFields()) {
            try {
                // Check if the field is a Color constant
                if (field.getType() == Color.class) {
                    // Get the Color constant's value
                    Color color = (Color) field.get(null);

                    // Check if the constant's name matches the specified colorName
                    if (field.getName().equalsIgnoreCase(colorName)) {
                        return color;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Default to RED if the color name is not found
        return Color.RED;
    }
}
