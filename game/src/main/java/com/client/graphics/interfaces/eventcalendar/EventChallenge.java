package com.client.graphics.interfaces.eventcalendar;

import com.client.RSFont;
import com.client.StringUtils;

public enum EventChallenge {

    THIEVE_X_STALLS(300),
    OBTAIN_X_HESPORI_EVENT_KEYS(2),
    OBTAIN_X_WILDY_EVENT_KEYS(2),
    GAIN_X_PEST_CONTROL_POINTS(400),
    COMPLETE_X_HARD_SLAYER_ASSIGNMENTS(15),
    WIN_AN_OUTLAST_TOURNAMENT,
    CUT_DOWN_X_MAGIC_LOGS(1500),
    KILL_X_BARROWS_BROTHERS(200),
    GET_X_KILLS_IN_WILDY(2),
    COMPLETE_X_RAIDS(10),
    WIELD_A_DRAGON_DEFENDER,
    KILL_X_NIGHTMARE(3),
    HAVE_126_COMBAT,
    OPEN_X_HESPORI_CHESTS(10),
    KILL_X_REVS_IN_WILDY(300),
    GAIN_X_EXCHANGE_POINTS(15000),
    KILL_X_WILDY_BOSSES(30),
    OPEN_X_WILDY_CHESTS(10),
    BURY_X_DRAGON_BONES(500),
    KILL_BASILISK_KNIGHTS_X_TIMES(100),
    GAIN_X_PVM_POINTS(1000),
    PARTICIPATE_IN_X_OUTLAST_TOURNIES(2),
    WIELD_FULL_ELITE_VOID,
    KILL_ZULRAH_X_TIMES(50),
    KILL_HUNLLEF_X_TIMES(3),
    KILL_X_GODWARS_BOSSES_OF_ANY_TYPE(30),
    HAVE_2000_TOTAL_LEVEL,
    KILL_X_DEMONIC_GORILLAS(50),
    USE_X_CHEST_RATE_INCREASE_TABLETS(5),
    COMPLETE_A_63_WAVE_FIGHT_CAVES,
    COMPLETE_TOB,
    OBTAIN_X_LARRENS_KEYS(2),
    COMPLETE_X_RUNS_AT_THE_ZMI_ALTAR(10),
    CONTRIBUTE_10M_TO_THE_WOGW
    ;
    private final int ticks;

    EventChallenge() {
        this(1);
    }

    EventChallenge(int ticks) {
        this.ticks = ticks;
    }

    public String getWrappedString(RSFont font, String colour) {
        String formatted = getFormattedString();
        String[] split = formatted.split(" ");
        StringBuilder builder = new StringBuilder();
        builder.append(colour);
        int width = 0;
        for (int index = 0; index < split.length; index++) {
            boolean newLine = false;
            if (width + font.getTextWidth(split[index]) >= 70) {
                builder.append("\\n");
                builder.append(colour);
                newLine = true;
                width = 0;
            }
            builder.append((index != 0 && !newLine ? " " : "") + split[index]);
            width += font.getTextWidth(split[index]);
        }
        return builder.toString();
    }

    public String getFormattedString() {
        return StringUtils.fixName(toString()
                .toLowerCase()
                .replace("_x_", "_" + ticks + "_")
                .replaceAll("_", " "));
    }


}
