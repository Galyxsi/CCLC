package com.eclipticcosmos.chosenthreads;

import com.eclipticcosmos.chosenthreads.species.VarynBaseSpecies;

import java.util.HashMap;
import java.util.Map;

public class VarynRegistry {

    public static Map<String, VarynBaseSpecies> Varyn_Map = new HashMap<>();

    public static void register(VarynBaseSpecies species) {
        Varyn_Map.put(species.identifier, species);
    }

    public static VarynBaseSpecies get(String id) {
        return Varyn_Map.get(id);
    }

    public static Map<String, VarynBaseSpecies> getAll() {
        return Varyn_Map;
    }

    public static boolean validVaryn(String identifier) {
        return Varyn_Map.containsKey(identifier);
    }

}
