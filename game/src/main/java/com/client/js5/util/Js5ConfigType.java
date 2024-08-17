package com.client.js5.util;

public enum Js5ConfigType {

    UNDERLAY(1,"Underlays"),
    IDENTKIT(3, "Identity Kits"),
    OVERLAY(4,"Overlays"),
    INV(5,"Inventory Sizes"),
    OBJECT(6,"Objects"),
    ENUM(8,"Enums"),
    NPC(9,"Npcs"),
    ITEM(10,"Items"),
    PARAMS(11,"Parameters"),
    SEQUENCE(12,"Sequences"),
    SPOTANIM(13,"Spot Animations"),
    VARBIT(14,"Varbits"),
    VARCLIENT(19,"Var Clients"),
    VARCLIENTSTRING(15,"Var Client Strings"),
    VARPLAYER(16,"Var Players"),
    HITSPLAT(32,"Hitsplats"),
    HEALTHBAR(33,"Healthbars"),
    STRUCT(34,"Structures"),
    AREA(35,"Areas"),
    DBROW(38,"Database Rows"),
    DBTABLE(39,"Database Tables"),
    CUSTOM_SPRITES(40,"Custom Sprites"),
    CUSTOM_DATA(41,"Custom Dats");

    private final int id;
    private final String formattedName;
    Js5ConfigType(int id, String formattedName) {
        this.id = id;
        this.formattedName = formattedName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return formattedName;
    }

}