package com.client.graphics.interfaces.eventcalendar;

import com.client.Client;
import com.client.Configuration;
import com.client.Sprite;
import com.client.TextDrawingArea;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.impl.Interfaces;
import com.google.common.base.Preconditions;

public class EventCalendar extends RSInterface {

    public static final int INTERFACE_ID = 23332;
    private static final int CHALLENGE_SPRITE_COUNT = 30;
    private static final String REWARD_STRING_COLOUR = "<col=27C60D>";
    private static final String DESCRIPTION_COLOUR = "<col=C18C28>";
    private static final String HEADER_COLOUR = "<col=F6AB1E>";
    private static final String SPRITES_LOCATION = "Interfaces/Giveaway/";
    private static final String CHALLENGE_SPRITE_NAME = "DAY";
    private static final EventCalendar EVENT_CALENDAR = new EventCalendar();

    public static EventCalendar getCalendar() {
        return EVENT_CALENDAR;
    }

    private Sprite[] challengeSprites;
    private final Sprite backgroundSprite = new Sprite(SPRITES_LOCATION + "BACKGROUND 1");
    private final Sprite buttonSprite = new Sprite(SPRITES_LOCATION + "BUTTON 1");
    private final Sprite rectangle = new Sprite(SPRITES_LOCATION + "RECTANGLE 1");

    private int headerStringInterfaceId;
    private int challengeDescriptionInterfaceId;
    private int challengeSpriteInterfaceId;
    private int challengeSpriteChildId;
    private int challengeRewardInterfaceId;

    private int winnerNumbersContainerInterfaceId;
    private int winnerNamesContainerInterfaceId;
    private int participantsNumbersContainerInterfaceId;
    private int participantsNamesContainerInterfaceId;

    private EventCalendar() {}

    public void onStringContainerUpdated(int containerId) {
        if (containerId == participantsNamesContainerInterfaceId) {
            get(participantsNumbersContainerInterfaceId).stringContainer.clear();
            for (int index = 0; index < get(containerId).stringContainer.size(); index++) {
                get(participantsNumbersContainerInterfaceId).stringContainer.add((index + 1) + ".");
            }
        }
    }

    public void onConfigReceived(int config, int value) {
        if (config == Configs.EVENT_CALENDAR_CONFIG) {
            int dayIndex = value - 1;
            if (dayIndex >= 0 && dayIndex < challengeSprites.length) {
                EventCalendarDay eventCalendarDay = EventCalendarDay.forDayOfTheMonth(value);
                int[] reward = eventCalendarDay.getReward();
                setChallengeSprite(challengeSpriteInterfaceId, challengeSpriteChildId, dayIndex);
                get(challengeDescriptionInterfaceId).message = eventCalendarDay.getChallenge().getWrappedString(Client.instance.newSmallFont, DESCRIPTION_COLOUR);
                get(challengeRewardInterfaceId).message = REWARD_STRING_COLOUR
                        + reward[0]
                        + "m OSRS GP or \\n"
                        + REWARD_STRING_COLOUR
                        + "$" + reward[1]
                        + " Donator Scroll ";
                get(headerStringInterfaceId).message = "September Giveaway (Day " + value + ")";

            }
        }
    }

    private void setChallengeSprite(int interfaceId, int childId, int challengeSpriteIndex) {
        RSInterface inter = get(INTERFACE_ID);
        Sprite sprite = challengeSprites[challengeSpriteIndex];
        addSprite(interfaceId, challengeSprites[challengeSpriteIndex]);
        inter.child(childId, interfaceId, 97 - sprite.myWidth / 2, 166 - sprite.myHeight / 2);
    }

    public void load(TextDrawingArea[] tda) {
        challengeSprites = new Sprite[CHALLENGE_SPRITE_COUNT];
        for (int sprite = 0; sprite < CHALLENGE_SPRITE_COUNT; sprite++) {
            challengeSprites[sprite] = new Sprite(SPRITES_LOCATION + CHALLENGE_SPRITE_NAME + " " + (sprite + 1));
            Preconditions.checkState(challengeSprites[sprite] != null && challengeSprites[sprite].myWidth != 0);
        }
        Preconditions.checkState(backgroundSprite.myWidth != 0);

        RSInterface inter = addInterface(INTERFACE_ID);
        inter.totalChildren(17);
        int interfaceId = INTERFACE_ID + 1;
        int childId = 0;
        int x = 14;
        int y = 26;

        addSprite(interfaceId, backgroundSprite);
        inter.child(childId++, interfaceId++, x, y);

        inter.child(childId++, Interfaces.CLOSE_BUTTON_SMALL, x + 462, y + 10);
        inter.child(childId++, Interfaces.CLOSE_BUTTON_SMALL_HOVER, x + 462, y + 10);

        headerStringInterfaceId = interfaceId;
        addText(headerStringInterfaceId, Configuration.CLIENT_TITLE + "' June Giveaway", tda, 2, 0xff981f, true);
        inter.child(childId++, interfaceId++, 247, 36);

        // active challenge, past winners, current participants

        String[] headers = {"Active Challenges", "Past Winners", "Current Participants"};
        for (int index = 0; index < headers.length; index++) {
            addText(interfaceId, HEADER_COLOUR + headers[index], tda, 1, 0xff981f, true);
            inter.child(childId++, interfaceId++, 94 + (index * 161), 62);
        }

        // Start challenge panel
        int textCenterX = 97;
        // Description
        challengeDescriptionInterfaceId = interfaceId;
        addText(interfaceId, "Obtain 5 Wildy\\nEvent Keys", tda, 0, 0xff981f, true);
        inter.child(childId++, interfaceId++, textCenterX, 90);

        // Sprite
        challengeSpriteInterfaceId = interfaceId;
        challengeSpriteChildId = childId;
        setChallengeSprite(interfaceId, childId, 0);
        childId++;
        interfaceId++;

        // Status
        int statusY = 212;
        addText(interfaceId, HEADER_COLOUR + "Status:", tda, 2, 0xff981f, true);
        inter.child(childId++, interfaceId++, textCenterX, statusY);
        addText(interfaceId, "@red@Incomplete", tda, 0, 0xff981f, true);
        inter.child(childId++, interfaceId++, textCenterX, statusY + 14);

        // Reward options
        int rewardOptionsY = 250;
        addText(interfaceId, HEADER_COLOUR + "Reward Options:", tda, 2, 0xff981f, true);
        inter.child(childId++, interfaceId++, textCenterX, rewardOptionsY);
        challengeRewardInterfaceId = interfaceId;
        addText(interfaceId, "", tda, 0, 0xff981f, true);
        inter.child(childId++, interfaceId++, textCenterX, rewardOptionsY + 18);

        addButton(interfaceId, buttonSprite, "Open Rules", 1);
        int button = interfaceId;
        inter.child(childId++, interfaceId++, 21, 296);
        addText(interfaceId, "Rules", tda, 2, 0xff981f, true);
        inter.child(childId++, interfaceId++, textCenterX, 302);

        inter.child(childId++, interfaceId, 177, 84);
        interfaceId = getStringContainer(interfaceId, 0);

        inter.child(childId++, interfaceId, 336, 84);
        interfaceId = getStringContainer(interfaceId, 1);

        onConfigReceived(Configs.EVENT_CALENDAR_CONFIG, 1);
        get(INTERFACE_ID).setNewButtonClicking();
        Preconditions.checkState(get(button).newButtonClicking);
    }

    public int getStringContainer(int interfaceId, int containerIndex) {
        final int containerInterfaceId = interfaceId;
        RSInterface container = sectionContainer(interfaceId);
        interfaceId += 2; // scrollable and rectangle

        RSInterface names = addStringContainer(interfaceId++, containerInterfaceId, Client.instance.newSmallFont,
                true, 0xFFFFFFFF, true, 17, "Testing");
        RSInterface numbers = addStringContainer(interfaceId++, containerInterfaceId, Client.instance.newSmallFont,
                true, 0xFFFFFFFF, true, 17, null);

        container.child(100, names.id, 76, 12);
        container.child(101, numbers.id, 10, 12);

        for (int index = 0; index < 100; index++) {
            numbers.stringContainer.add((index + 1) + ".");
        }

        if (containerIndex == 0) {
            winnerNumbersContainerInterfaceId = numbers.id;
            winnerNamesContainerInterfaceId = names.id;
        } else {
            participantsNumbersContainerInterfaceId = numbers.id;
            participantsNamesContainerInterfaceId = names.id;
        }
        return interfaceId;
    }

    private RSInterface sectionContainer(int interfaceId) {
        RSInterface rsInterface = addInterface(interfaceId);
        rsInterface.width = 139;
        rsInterface.height = 242;
        rsInterface.scrollMax = 243;
        rsInterface.totalChildren(102);
        addSprite(interfaceId + 1, rectangle);
        for (int index = 0; index < 100; index++) {
            rsInterface.child(index, interfaceId + 1, 0, index * 34);
        }
        return rsInterface;
    }
}
