package com.client.graphics.interfaces.builder.impl.tob;

import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;
import com.client.graphics.interfaces.impl.Interfaces;
import com.client.model.Items;

public class TobFoodChest extends InterfaceBuilder {

    private enum Supply {
        STAM(Items.STAMINA_POTION4, 1),
        PRAYER(Items.PRAYER_POTION4, 2),
        BREW(Items.SARADOMIN_BREW4, 3),
        RESTORE(Items.SUPER_RESTORE4, 3),
        POTATO(Items.TUNA_POTATO, 1),
        SHARK(Items.SHARK, 1),
        TURTLE(Items.SEA_TURTLE, 2),
        MANTA(Items.MANTA_RAY, 2),
        ;

        private final int itemId;
        private final int cost;

        Supply(int itemId, int cost) {
            this.itemId = itemId;
            this.cost = cost;
        }
    }

    private static final Sprite BG = new Sprite("tob/supplies_bg2");

    public TobFoodChest() {
        super(21_490);
    }

    @Override
    public void build() {
        int x = 142;
        int y = 76;

        addSprite(nextInterface(), BG);
        child(x, y);

        addText(nextInterface(), 2, RSInterface.DEFAULT_TEXT_COLOR, true, "Theatre of Blood Supplies");
        child(x + 124, y + 9);

        child(Interfaces.CLOSE_BUTTON_SMALL, x + 250, y + 9);
        child(Interfaces.CLOSE_BUTTON_SMALL_HOVER, x + 250, y + 9);

        int containerX = x + 14;
        int containerY = y + 52;

        addItemContainer(nextInterface(), 4, 2, 2, 28, true,"Buy");
        child(containerX, containerY);
        RSInterface container = get(nextInterface() - 1);

        for (int index = 0; index < 8; index++) {
            int foodCostX = containerX + (index % 4 * 34) + 15;
            int foostCostY = containerY + (index / 4 * 60) + 36;
            Supply supply = Supply.values()[index];
            addText(nextInterface(), 0, RSInterface.DEFAULT_TEXT_COLOR, true, "" + supply.cost);
            child(foodCostX, foostCostY);
            container.inventoryItemId[index] = supply.itemId + 1;
            container.inventoryAmounts[index] = 1;
        }

        addText(nextInterface(), 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Points Available: 0");
        child(x + 124, y + 165);
    }
}
