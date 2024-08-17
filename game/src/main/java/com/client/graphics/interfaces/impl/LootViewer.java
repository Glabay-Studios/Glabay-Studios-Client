package com.client.graphics.interfaces.impl;

import com.client.Sprite;
import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;
import com.client.model.Items;

public class LootViewer extends RSInterface {

    private static final int ID = 44_942;
    private static final int BUTTONS = 45_010;
    private static final int TABLE_ONE = 45_140;
    private static final int TABLE_TWO = 45_180;

    private static final boolean PRINT_IDS = false;

    private enum Button {
        VOTE_KEY("Vote key", 22093),
        PORAZDIRS_KEY("Porazdir's key", 4185),
        HUNNLEFS_KEY("Hunnlef's key", 23776),
        SERENS_KEY("Seren's key", 6792),
        HESPORI_KEY("Hespori key", 22374),
        MYSTERY_BOX("Mystery box", 6199),
        SUPER_MYSTERY_BOX("Super m. box", 6828),
        ULTRA_MYSTERY_BOX("Ultra m. box", 13346),
        FOE_MYSTERY_CHEST("FoE m. chest", 8167),
        SLAYER_MYSTERY_CHEST("Slayer m. chest", 13438),
        BRIMSTONE_KEY("Brimstone Key", Items.BRIMSTONE_KEY),
        XERIC_CHESTS("Xeric chests", 3464),
        TOB_CHEST("Theatre of Blood", 8151),
        VOTE_MYSTERY_BOX("Vote Mystery Box", 11739),
        CRYSTAL_CHEST("Crystal Chest", 989),
        PVM_CASKET("PvM Casket", 405),
        LARRANS_CHEST("Larran's Chest", Items.LARRANS_KEY)
        ;

        private final String name;

        Button(String name, int itemId) {
            this.name = name;
            this.itemId = itemId;
        }

        private final int itemId;

    }

    public void load(TextDrawingArea[] tda) {
        RSInterface inter = addInterface(ID);
        int childInterfaceId = ID + 1;

        // Background and title
        addSprite(childInterfaceId++, 0, "Interfaces/Generic/IMAGE");
        addText(childInterfaceId++, "Loot Table", tda, 2, 0xff9040, true, true);

        // Begin assembling children
        int childIndex = 0;
        int children = childInterfaceId - (ID + 1);
        children += 2; // close button
        children += 1; // buttons div
        children += 2; // item containers
        setChildren(children, inter);

        // Add these after children count is made

        // Add buttons
        getButtons(tda, BUTTONS);

        // Add tables
        getTable(tda, TABLE_ONE, "Common - Uncommon");
        getTable(tda, TABLE_TWO, "Rare");

        // Reset child interface id for children assembly
        childInterfaceId = ID + 1;

        int x = 0;
        int y = 4;

        // Background
        inter.child(childIndex++, childInterfaceId++, 16 + x, 20 + y);

        // Title
        inter.child(childIndex++, childInterfaceId++, 264 + x, 30 + y);

        // Close buttons
        inter.child(childIndex++, 47902, 473 + x, 27 + y);
        inter.child(childIndex++, 47903, 473 + x, 27 + y);

        // Left buttons
        inter.child(childIndex++, BUTTONS, 22 + x, 54 + y);

        // Containers
        inter.child(childIndex++, TABLE_ONE, 214 + x, 56 + y);
        inter.child(childIndex++, TABLE_TWO, 214 + x, 56 + 132 + y);
    }

    public int getTable(TextDrawingArea[] tda, int id, String header) {
        boolean bottom = !header.equals("Rare");

        // Header
        RSInterface inter = addInterface(id++);
        setChildren(bottom ? 4 : 3, inter);
        int childIndex = 0;

        // Header text
        inter.child(childIndex++, id, 141, 2);
        addText(id++, header, tda, 2, Integer.MAX_VALUE, true, true);

        // Border
        inter.child(childIndex++, id, 0, 18);
        if (bottom) {
            inter.child(childIndex++, id, 0, 130);
        }
        addSprite(id++, new Sprite("border"));

        // Item container
        inter.child(childIndex++, id, 0, 20);

        // Item container define
        int scrollInterface = id;
        RSInterface scrollable = addInterface(id++);
        setChildren(1, scrollable);
        scrollable.width = 265;
        scrollable.height = 110;
        scrollable.scrollMax = scrollable.height + 1;

        if (PRINT_IDS) {
            System.out.println(header + " scroll interface id " + scrollInterface);
            System.out.println(header + " inventory interface id " + id);
        }

        scrollable.child(0, id, 4, 4);
        addItemContainerAutoScrollable(id++, 5, 36, 22, 4, true, scrollInterface);

        return id;
    }

    public int getButtons(TextDrawingArea[] tda, int id) {
        final int parent = id;
        RSInterface inter = addInterface(id++);
        inter.height = 263;
        inter.width = 176;
        inter.scrollMax = inter.height + 215;


        setChildren(Button.values().length * 3, inter);
        int childIndex = 0;

        if (PRINT_IDS) {
            System.out.println("view table button start interface id " + id);
        }

        for (int index = 0; index < Button.values().length; index++) {
            Button button = Button.values()[index];
            int x = 0;
            int y = index * 28;

            // Button
            inter.child(childIndex++, id, x, y);
            addConfigButton(id, parent, 1, 0, "Interfaces/Generic/BUTTON", 176, 28, button.name, index,4, 1354);
            RSInterface.interfaceCache[id].ignoreConfigClicking = true;
            id++;

            // Text
            inter.child(childIndex++, id, x + 80, y + 5);
            addText(id++, button.name, tda, 2, 0xff9040, true, true);

            // Item sprite
            inter.child(childIndex++, id, x + 140, y + 5);
            addItemView(id++, button.itemId + 1, true, true);
        }

        return id;
    }
}
