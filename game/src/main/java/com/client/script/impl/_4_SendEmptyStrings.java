package com.client.script.impl;

import com.client.graphics.interfaces.RSInterface;

public class _4_SendEmptyStrings {

    public static void handle(int startInterfaceId, int lengthExclusive) {
        for (int i = 0; i < lengthExclusive; i++) {
            RSInterface.get(startInterfaceId + i).message = "";
        }
    }

}
