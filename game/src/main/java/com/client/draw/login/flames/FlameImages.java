package com.client.draw.login.flames;

import java.time.Instant;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public enum FlameImages {
    // Halloween Icons
    BAT(2169, FlameImages::isHalloween),
    PUMPKIN(2180, FlameImages::isHalloween),
    SKULL(2181, FlameImages::isHalloween),
    MOON(2182, FlameImages::isHalloween),


    // Christmas Icons
    SNOWFLAKE(2183, FlameImages::isChristmas),
    HOLLY(2184, FlameImages::isChristmas),
    SNOWMAN(2185, FlameImages::isChristmas),
    ANGEL(2186, FlameImages::isChristmas),

    // Normal Icons
    FIRE_RUNE(2187, FlameImages::isNotHoliday),
    WATER_RUNE(2188, FlameImages::isNotHoliday),
    EARTH_RUNE(2179, FlameImages::isNotHoliday),
    AIR_RUNE(2190, FlameImages::isNotHoliday),
    BODY_RUNE(2191, FlameImages::isNotHoliday),
    WRATH_RUNE(2192, FlameImages::isNotHoliday),
    CHAOS_RUNE(2193, FlameImages::isNotHoliday),
    COSMIC_RUNE(2194, FlameImages::isNotHoliday),
    NATURE_RUNE(2195, FlameImages::isNotHoliday),
    LAW_RUNE(2196, FlameImages::isNotHoliday),
    DEATH_RUNE(2197, FlameImages::isNotHoliday),
    SOUL_RUNE(2198, FlameImages::isNotHoliday),
    CUSTOM_LOGO_1(2199, FlameImages::isNotHoliday),
    CUSTOM_LOGO_2(2200, FlameImages::isNotHoliday);

    private final int id;
    private final BooleanSupplier active;

    public int getId() {
        return id;
    }

    FlameImages(int id, BooleanSupplier active) {
        this.id = id;
        this.active = active;
    }

    private static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public static boolean isNotHoliday() {
        return !isChristmas() && !isHalloween();
    }

    public static boolean isHalloween() {
        Date dateStart = new GregorianCalendar(currentYear, Calendar.OCTOBER, 21).getTime();
        Date dateEnd = new GregorianCalendar(currentYear, Calendar.NOVEMBER, 11).getTime();
        return isWithinRange(dateStart, dateEnd);
    }

    public static boolean isChristmas() {
        Date dateStart = new GregorianCalendar(currentYear, Calendar.DECEMBER, 16).getTime();
        Date dateEnd = new GregorianCalendar(currentYear, Calendar.JANUARY, 5).getTime();
        return isWithinRange(dateStart, dateEnd);
    }

    private static boolean isWithinRange(Date startDate, Date endDate) {
        Date currentDate = Date.from(Instant.now());
        return !(currentDate.before(startDate) || currentDate.after(endDate));
    }

    private static List<FlameImages> flameImages = Collections.emptyList();

    public static int getRandomImage() {
        if (flameImages.isEmpty()) {
            flameImages = Arrays.stream(FlameImages.values())
                    .filter(image -> image.active != null && image.active.getAsBoolean())
                    .collect(Collectors.toList());
        }
        return flameImages.get(new Random().nextInt(flameImages.size())).id;
    }
}