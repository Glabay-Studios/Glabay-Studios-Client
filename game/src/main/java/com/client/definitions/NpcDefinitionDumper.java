package com.client.definitions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NpcDefinitionDumper {

    public static void dump() {
        Map<Integer, Npc> npcs = new HashMap<>();
        for (int i = 0; i < 100_000; i++) {
            try {
                NpcDefinition def = NpcDefinition.get(i);
                if (def != null) {
                    npcs.put(i, new Npc(def.name, def.combatLevel, def.size));
                }
            } catch (Exception e) {
            }
        }
        toJson(npcs, "./temp/npc_definitions.json");
    }

    private static <T> void toJson(T t, String filePath) {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = prettyGson.toJson(t);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filePath)));
            bw.write(prettyJson);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Npc {
        private final String name;
        private final int combatLevel;
        private final int size;

        public Npc(String name, int combatLevel, int size) {
            this.name = name;
            this.combatLevel = combatLevel;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public int getCombatLevel() {
            return combatLevel;
        }

        public int getSize() {
            return size;
        }
    }
}
