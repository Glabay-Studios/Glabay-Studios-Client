package com.client.graphics.interfaces.eventcalendar;

import static com.client.graphics.interfaces.eventcalendar.EventChallenge.*;

public enum EventCalendarDay {
    DAY_1(KILL_X_NIGHTMARE),
    DAY_2(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_3(THIEVE_X_STALLS),
    DAY_4(PARTICIPATE_IN_X_OUTLAST_TOURNIES),
    DAY_5(COMPLETE_X_HARD_SLAYER_ASSIGNMENTS),
    DAY_6(KILL_X_GODWARS_BOSSES_OF_ANY_TYPE),
    DAY_7(KILL_X_BARROWS_BROTHERS),
    DAY_8(COMPLETE_X_RAIDS),
    DAY_9(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_10(COMPLETE_X_RUNS_AT_THE_ZMI_ALTAR),
    DAY_11(HAVE_126_COMBAT),
    DAY_12(OBTAIN_X_WILDY_EVENT_KEYS),
    DAY_13(KILL_X_REVS_IN_WILDY),
    DAY_14(WIELD_A_DRAGON_DEFENDER),
    DAY_15(KILL_HUNLLEF_X_TIMES),
    DAY_16(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_17(KILL_ZULRAH_X_TIMES),
    DAY_18(USE_X_CHEST_RATE_INCREASE_TABLETS),
    DAY_19(OBTAIN_X_LARRENS_KEYS),
    DAY_20(BURY_X_DRAGON_BONES),
    DAY_21(CUT_DOWN_X_MAGIC_LOGS),
    DAY_22(COMPLETE_TOB),
    DAY_23(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_24(KILL_X_DEMONIC_GORILLAS),
    DAY_25(GAIN_X_EXCHANGE_POINTS),
    DAY_26(OBTAIN_X_WILDY_EVENT_KEYS),
    DAY_27(KILL_X_WILDY_BOSSES),
   // DAY_28(OPEN_X_HESPORI_CHESTS),
    DAY_29(HAVE_2000_TOTAL_LEVEL),
    DAY_30(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_31(CONTRIBUTE_10M_TO_THE_WOGW),
    ;

    public static EventCalendarDay forDayOfTheMonth(int dayOfTheMonth) {
        int dayIndex = dayOfTheMonth - 1;
        if (dayIndex < 0 || dayIndex >= values().length) {
            throw new IllegalArgumentException();
        } else {
            return values()[dayIndex];
        }
    }

    private final EventChallenge challenge;

    EventCalendarDay(EventChallenge challenge) {
        this.challenge = challenge;
    }

    public int[] getReward() {
        return isLastDay() ? new int[] {200, 100} : new int[] {200, 100};
    }

    public boolean isLastDay() {
        return ordinal() == values().length - 1;
    }

    public EventChallenge getChallenge() {
        return challenge;
    }
}
