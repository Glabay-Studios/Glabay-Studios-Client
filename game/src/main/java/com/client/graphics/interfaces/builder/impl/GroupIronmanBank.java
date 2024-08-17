package com.client.graphics.interfaces.builder.impl;

import com.client.Configuration;
import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;

public class GroupIronmanBank extends InterfaceBuilder {

    public GroupIronmanBank() {
        super(48670);
    }

    @Override
    public void build() {
        addSprite(nextInterface(), new Sprite("banktab/07/bank 12"));
        child(14, 3);

        addText(nextInterface(), "Group Ironman Bank", 2, 0xE68A00, true, true);
        child(250, 13);

        addText(nextInterface(), "0", 0, 0xE68A00, true, true);
        child(36, 9);

        addText(nextInterface(), "350", 0, 0xE68A00, true, true);
        child(36, 21);

        // Inventory and container for inventory
        addInventoryContainer(nextInterface(), 10, 130,12, 4, true, "Withdraw 1", "Withdraw 5", "Withdraw 10", "Withdraw All", "Withdraw X", "Withdraw All but one");
        RSInterface container = addInterface(nextInterface());
        container.width = 455;
        container.height = 248;
        container.scrollMax = 6000;
        setChildren(1, container);

        RSInterface itemContainer = RSInterface.get(getCurrentInterfaceId() - 2);
        itemContainer.contentType = 206;
        container.child(0, itemContainer.id, 12, 0);
        child(24, 42);

        child(5384, 474, 10); // Close
        child(5380, 474, 10);

        child(18929, 70, 306); // Swap items
        child(18930, 70, 306);

        child(18933, 170, 306); // Noting
        child(18934, 170, 306);

        child(58002, 20, 306); // Rearrange mode
        child(58003, 20, 306);

        child(58010, 120, 306); // Noting
        child(58011, 120, 306);

        child(58018, 423, 292); // Deposit backpack
        child(58019, 423, 292);

        child(58026, 460, 292); // Deposit worn items

        child(18941, 22, 291); // Rearrange mode:
        child(18942, 170, 291); // Withdraw as:

        child(18943, 45, 309); // Swap
        child(18944, 95, 309); // Insert
        child(18945, 145, 309); // Item
        child(18946, 195, 309); // Note
    }
}
