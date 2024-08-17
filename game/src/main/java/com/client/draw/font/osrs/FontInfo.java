package com.client.draw.font.osrs;

public enum FontInfo {
    SMALL_FONT(494),
    REGULAR_FONT(495),
    BOLD_FONT(496),
    FANCY_SMALL(497),
    FANCY_MEDIUM(645),
    FANCY_CAPS_LARGE(646),

    FAIRY_SMALL(647),
    FAIRY_LARGE(648),

    BARBARIAN(764),
    SUROK(819),

    VERDANA_11(1442),
    VERDANA_11_BOLD(1443),

    TAHOMA_11(1444),

    VERDANA_13(1445),
    VERDANA_13_BOLD(1446),

    VERDANA_15(1447);

    private int id;

    public int getId() {
        return id;
    }

    FontInfo(int id) {
        this.id = id;
    }

    public static FontInfo[] initialFonts() {
        return new FontInfo[]{
           SMALL_FONT, REGULAR_FONT, BOLD_FONT, FANCY_SMALL, FANCY_MEDIUM, FANCY_CAPS_LARGE, VERDANA_11, VERDANA_11_BOLD, VERDANA_15
        };
    }



}
