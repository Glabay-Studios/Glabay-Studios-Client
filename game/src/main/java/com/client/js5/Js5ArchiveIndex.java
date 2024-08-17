package com.client.js5;

public enum Js5ArchiveIndex
{
    ANIMATIONS(0),
    SKELETONS(1),
    CONFIGS(2),
    INTERFACES(3),
    SOUNDEFFECTS(4),
    MAPS(5),
    MUSIC_TRACKS(6),
    MODELS(7),
    SPRITES(8),
    TEXTURES(9),
    BINARY(10),
    MUSIC_JINGLES(11),
    CLIENTSCRIPT(12),
    FONTS(13),
    MUSIC_SAMPLES(14),
    MUSIC_PATCHES(15),
    WORLDMAP_OLD(16),
    ARCHIVE_17(17),
    WORLDMAP_GEOGRAPHY(18),
    WORLDMAP(19),
    WORLDMAP_GROUND(20),
    DBTABLEINDEX(21),
    DATS(22);

    /**
     * The id of the Js5 archive.
     */
    private final int id;

    /**
     * Constructs a new {@link Js5ArchiveIndex} enum constant.
     *
     * @param id
     *         the id of the archive.
     */
    Js5ArchiveIndex(int id) {
        this.id = id;
    }

    /**
     * Returns the id of the archive.
     *
     * @return the id of the archive.
     */
    public int getId() {
        return id;
    }

}