package com.client.graphics.interfaces.impl;

import java.awt.Point;
import java.text.NumberFormat;

import com.client.Client;
import com.client.Sprite;
import com.client.TextDrawingArea;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.RSInterface;

public class Nightmare extends RSInterface {

    public static final Nightmare instance = new Nightmare();

    private static final String SPRITE_FOLDER = "/Interfaces/nightmare/";
    private static final Sprite NIGHTMARE_HEALTH_BACKGROUND = new Sprite(SPRITE_FOLDER + "0");
    private static final Sprite TOTEM_CHARGE_BACKGROUND = new Sprite(SPRITE_FOLDER + "1");

    private static final int HEALTH_REMAINING_COLOUR = 0x00C800;
    private static final int HEALTH_BACKGROUND_COLOUR = 0xC80000;
    private static final int SHIELD_REMAINING_COLOUR = 0x00D0A8;
    private static final int SHIELD_BACKGROUND_COLOUR = 0x002020;
    private static final int TOTEM_CHARGE_COLOUR = 0xDBD300;
    private static final int TOTEM_FULLY_CHARGED_COLOUR = 0xA1A08D;

    public static final int NIGHTMARE_HEALTH_INTERFACE_ID = 47302;
    public static final int TOTEM_CHARGE_INTERFACE_ID = 47352;

    private static final Point NIGHTMARE_HEALTH_SIZE = new Point(190, 17);
    private static final Point TOTEM_HEALTH_SIZE = new Point(61, 9);

    private int healthFillBackgroundId;
    private int healthFillId;
    private int nightmareHealthTextId;
    private int[] shieldProgressBarIds = new int[4];

    private int[][] totemHealth = new int[][] {{500, 1000}, {100, 1000}, {0, 1000}, {900, 1000}};
    private int nightmareHealth = 2000;
    private int nightmareMaxHealth = 2000;
    private boolean nightmareOnHealth = false;

    private Nightmare() {}

    public void load(TextDrawingArea[] tda) {
        nightmareHealth(tda);
        totemCharge(tda);
    }

    private void nightmareHealth(TextDrawingArea[] tda) {
        int id = NIGHTMARE_HEALTH_INTERFACE_ID;
        int child = 0;
        RSInterface inter = addInterface(id++);
        inter.totalChildren(4);

        int x = 162;
        int y = 28;

        addSprite(id, NIGHTMARE_HEALTH_BACKGROUND);
        inter.child(child++, id++, x, y);

        healthFillBackgroundId = id;
        inter.child(child++, id++, x + 4, y + 19);

        healthFillId = id;
        inter.child(child++, id++, x + 4, y + 19);

        nightmareHealthTextId = id;
        inter.child(child++, id++, x + 98, y + 22);

        buildNightmareHealthInter();
    }

    private void buildNightmareHealthInter() {
        double percentage = (double) nightmareHealth / (double) nightmareMaxHealth;
        addProgressBarReal(healthFillBackgroundId, NIGHTMARE_HEALTH_SIZE.x, NIGHTMARE_HEALTH_SIZE.y, !nightmareOnHealth ? SHIELD_BACKGROUND_COLOUR : HEALTH_BACKGROUND_COLOUR);
        addProgressBarReal(healthFillId, (int) (NIGHTMARE_HEALTH_SIZE.x * percentage), NIGHTMARE_HEALTH_SIZE.y, !nightmareOnHealth ? SHIELD_REMAINING_COLOUR : HEALTH_REMAINING_COLOUR);

        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        addText(nightmareHealthTextId, myFormat.format(nightmareHealth) + " / " + myFormat.format(nightmareMaxHealth), Interfaces.defaultTextDrawingAreas, 0, 0xFFFFFFFF, true);
    }

    private void totemCharge(TextDrawingArea[] tda) {
        int id = TOTEM_CHARGE_INTERFACE_ID;
        int child = 0;
        RSInterface inter = addInterface(id++);
        inter.totalChildren(5);

        int x = 8;
        int y = 28;

        addSprite(id, TOTEM_CHARGE_BACKGROUND);
        inter.child(child++, id++, x, y);

        for (int index = 0; index < shieldProgressBarIds.length; index++) {
            shieldProgressBarIds[index] = id;
            inter.child(child++, id++, x + 5 + (index % 2 * 66), y + 18 + (index / 2 * 30));
        }

        buildTotemChargeInter();
    }

    private void buildTotemChargeInter() {
        for (int index = 0; index < shieldProgressBarIds.length; index++) {
            double percentage = (double) totemHealth[index][0] / (double) totemHealth[index][1];
            addProgressBarReal(shieldProgressBarIds[index], (int) (TOTEM_HEALTH_SIZE.x * (1.0 - percentage)), TOTEM_HEALTH_SIZE.y, totemHealth[index][0] == 0 ? TOTEM_FULLY_CHARGED_COLOUR : TOTEM_CHARGE_COLOUR);
        }
    }

    public boolean drawNightmareInterfaces(int interfaceId) {
        if (interfaceId == NIGHTMARE_HEALTH_INTERFACE_ID) {
            int x = 0;
            int y = 0;
            if (Client.instance.isResized()) {
                x = Client.canvasWidth / 2 - 261;
            }

            // draw nightmare health
            Client.instance.drawInterface(0, x, RSInterface.get(NIGHTMARE_HEALTH_INTERFACE_ID), y);

            if (nightmareOnHealth) {
                x = 0;
                y = 0;
                Client.instance.drawInterface(0, x, RSInterface.get(TOTEM_CHARGE_INTERFACE_ID), y);
            }
            return true;
        } else {
            return false;
        }
    }

    public void handleConfig(int configId, int configValue) {
        switch (configId) {
            case Configs.NIGHTMARE_HEALTH_AMOUNT:
                nightmareHealth = configValue;
                break;
            case Configs.NIGHTMARE_MAX_HEALTH_AMOUNT:
                nightmareMaxHealth = configValue;
                break;
            case Configs.NIGHTMARE_HEALTH_STATUS:
                nightmareOnHealth = configValue == 1;
                buildNightmareHealthInter();
                break;
            case Configs.NIGHTMARE_TOTEM_MAX_HEALTH:
                for (int index = 0; index < totemHealth.length; index++) {
                    totemHealth[index][1] = configValue;
                }
                break;
            case Configs.NIGHTMARE_TOTEM1_HEALTH:
                totemHealth[0][0] = configValue;
                break;
            case Configs.NIGHTMARE_TOTEM2_HEALTH:
                totemHealth[1][0] = configValue;
                break;
            case Configs.NIGHTMARE_TOTEM3_HEALTH:
                totemHealth[2][0] = configValue;
                break;
            case Configs.NIGHTMARE_TOTEM4_HEALTH:
                totemHealth[3][0] = configValue;
                buildTotemChargeInter();
                break;
        }
    }

}
