package com.eclipticcosmos.chosenthreads;

import net.minecraft.world.entity.player.Player;


public class VarynHandler {
    public static final String SPECIES_KEY = "chosen_threads_varyn";

    public static void setSpecies(Player player, String speciesId)
    {
        if (speciesId.equals("remove"))
        {
            player.getPersistentData().putString(SPECIES_KEY, "");
        } else {
            player.getPersistentData().putString(SPECIES_KEY, speciesId);
        }

    }

    public static String getSpecies(Player player) {
        return player.getPersistentData().getString(SPECIES_KEY);
    }
}

