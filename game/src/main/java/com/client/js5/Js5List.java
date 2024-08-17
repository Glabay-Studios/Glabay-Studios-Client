package com.client.js5;

import com.client.js5.disk.Js5Archive;
import com.client.js5.util.Js5ConfigType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class Js5List {

    public static Js5Archive animations;
    public static Js5Archive skeletons;
    public static Js5Archive configs;
    public static Js5Archive interfaces;
    public static Js5Archive soundEffects;
    public static Js5Archive maps;
    public static Js5Archive musicTracks;
    public static Js5Archive models;
    public static Js5Archive sprites;
    public static Js5Archive textures;
    public static Js5Archive binary;
    public static Js5Archive musicJingles;
    public static Js5Archive clientScript;
    public static Js5Archive fonts;
    public static Js5Archive musicSamples;
    public static Js5Archive musicPatches;
    public static Js5Archive archive17;
    public static Js5Archive worldmapGeography;
    public static Js5Archive worldmap;
    public static Js5Archive worldmapold;
    public static Js5Archive worldmapGround;
    public static Js5Archive dbtableindex;

    public static HashMap<Js5ConfigType,Integer> configSizes = new HashMap<>();

    public static void initConfigSizes() {
        for (Js5ConfigType configType : Js5ConfigType.values()) {
            int size = 0;
            try {
                size = Js5List.configs.getGroupFileCount(configType);
            }catch (Exception ignored) {}
            configSizes.put(configType,size);
            log.info("{} read -> {}",configType.getName(),size);
        }
    }

    public static int getConfigSize(Js5ConfigType type) {
        return configSizes.get(type);
    }

}
