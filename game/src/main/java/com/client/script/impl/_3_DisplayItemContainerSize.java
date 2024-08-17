package com.client.script.impl;

import com.client.StringUtils;
import com.client.graphics.interfaces.RSInterface;

import java.util.Arrays;

public class _3_DisplayItemContainerSize {

    public static void handle(int itemContainerComponentId, int currentSizeComponentText, int maxSizeComponentText) {
        RSInterface inter = RSInterface.get(itemContainerComponentId);
        RSInterface currentText = RSInterface.get(currentSizeComponentText);
        RSInterface maxText = RSInterface.get(maxSizeComponentText);
        if (inter == null || inter.inventoryItemId == null || currentText == null || maxText == null)
            return;
        maxText.message = StringUtils.insertCommas(inter.inventoryItemId.length);
        currentText.message = StringUtils.insertCommas(Arrays.stream(inter.inventoryItemId).filter(it -> it > 0).count());
    }

}
