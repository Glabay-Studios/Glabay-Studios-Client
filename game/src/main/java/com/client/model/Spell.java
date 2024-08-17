package com.client.model;

import java.util.Arrays;

import com.client.StringUtils;

public enum Spell {

    // Modern
    WIND_STRIKE(1152, SpellBook.MODERN, true),
    WATER_STRIKE(1154, SpellBook.MODERN, true),
    EARTH_STRIKE(1156, SpellBook.MODERN, true),
    FIRE_STRIKE(1158, SpellBook.MODERN, true),
    WIND_BOLT(1160, SpellBook.MODERN, true),
    WATER_BOLT(1163, SpellBook.MODERN, true),
    EARTH_BOLT(1166, SpellBook.MODERN, true),
    FIRE_BOLT(1169, SpellBook.MODERN, true),
    WIND_BLAST(1172, SpellBook.MODERN, true),
    WATER_BAST(1175, SpellBook.MODERN, true),
    EARTH_BLAST(1177, SpellBook.MODERN, true),
    FIRE_BLAST(1181, SpellBook.MODERN, true),
    WIND_WAVE(1183, SpellBook.MODERN, true),
    WATER_WAVE(1185, SpellBook.MODERN, true),
    EARTH_WAVE(1188, SpellBook.MODERN, true),
    FIRE_WAVE(1189, SpellBook.MODERN, true),
    WIND_SURGE(23619, SpellBook.MODERN2, true),
    WATER_SURGE(23633, SpellBook.MODERN2, true),
    EARTH_SURGE(23603, SpellBook.MODERN2, true),
    FIRE_SURGE(23583, SpellBook.MODERN2, true),

    SLAYER_DART(12037, SpellBook.MODERN, true),
    IBAN_BLAST(1539, SpellBook.MODERN, true),
    CRUMBLE_UNDEAD(1171, SpellBook.MODERN, true),
    FLAMES_OF_ZAMORAK(1192, SpellBook.MODERN, true),
    CLAWS_OF_GUTHIX(1191, SpellBook.MODERN, true),
    SARADOMIN_STRIKE(1190, SpellBook.MODERN, true),
    // Ancient
    SMOKE_RUSH(12939, SpellBook.ANCIENT, true),
    SHADOW_RUSH(12987, SpellBook.ANCIENT, true),
    BLOOD_RUSH(12901, SpellBook.ANCIENT, true),
    ICE_RUSH(12861, SpellBook.ANCIENT, true),
    SMOKE_BURST(12963, SpellBook.ANCIENT, true),
    SHADOW_BURST(13011, SpellBook.ANCIENT, true),
    BLOOD_BURST(12919, SpellBook.ANCIENT, true),
    ICE_BURST(12881, SpellBook.ANCIENT, true),
    SMOKE_BLITZ(12951, SpellBook.ANCIENT, true),
    SHADOW_BLITZ(12999, SpellBook.ANCIENT, true),
    BLOOD_BLITZ(12911, SpellBook.ANCIENT, true),
    ICE_BLITZ(12871, SpellBook.ANCIENT, true),
    SMOKE_BARRAGE(12975, SpellBook.ANCIENT, true),
    SHADOW_BARRAGE(13023, SpellBook.ANCIENT, true),
    BLOOD_BARRAGE(12929, SpellBook.ANCIENT, true),
    ICE_BARRAGE(12891, SpellBook.ANCIENT, true),
    ;

    private final int id;
    private final SpellBook spellBook;
    private final boolean autocastable;

    Spell(int id, SpellBook spellBook, boolean autocastable) {
        this.id = id;
        this.spellBook = spellBook;
        this.autocastable = autocastable;
    }

    Spell(int id, SpellBook spellBook) {
        this.id = id;
        this.spellBook = spellBook;
        autocastable = false;
    }

    @Override
    public String toString() {
        return StringUtils.fixName(name().toLowerCase().replaceAll("_", " "));
    }

    public int getId() {
        return id;
    }

    public SpellBook getSpellBook() {
        return spellBook;
    }

    public boolean isAutocastable() {
        return autocastable;
    }

    public static Spell forId(int id) {
        return Arrays.stream(Spell.values()).filter(spell -> spell.id == id).findAny().orElse(null);
    }
}
