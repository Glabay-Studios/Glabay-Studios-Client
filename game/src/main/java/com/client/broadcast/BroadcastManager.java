package com.client.broadcast;

import com.client.Client;

public class BroadcastManager {

    public static Broadcast[] broadcasts = new Broadcast[1000];

    public static void removeIndex(int broadcastIndex) {
        if (broadcasts[broadcastIndex] != null) {
            broadcasts[broadcastIndex] = null;
        }
    }

    public static Broadcast getCurrentBroadcast() {
        Broadcast b = broadcasts[getHighestIndex()];
        if (b == null || b.message == null)
            return null;
        if (b.time < System.currentTimeMillis())
            return null;
        return b;
    }

    public static void addBoradcast(Broadcast broadcast) {
        broadcasts[broadcast.index] = broadcast;
    }

    public static Broadcast getBroadCast(String message) {
        for (Broadcast b : broadcasts) {
            if (b != null && b.message != null) {
                if (b.message.equalsIgnoreCase(message))
                    return b;
            }
        }
        return null;
    }

    private static int getBroadcastSize() {
        int count = 0;
        for (Broadcast b : broadcasts) {
            if (b != null && b.message != null) {
                count++;
            }
        }
        return count;
    }

    public static int getHighestIndex() {
        int highestIndex = 0;
        for (Broadcast b : broadcasts) {
            if (b != null && b.message != null) {
                if (highestIndex < b.index)
                    highestIndex = b.index;
            }
        }
        return highestIndex;
    }

    public static boolean isDisplayed() {
        Broadcast b = broadcasts[getHighestIndex()];
        if (b == null || b.message == null) {
            return false;
        }
        return b.time > System.currentTimeMillis();
    }

    public static void display(Client client) {

        Broadcast b = broadcasts[getHighestIndex()];

        if (b == null || b.message == null)
            return;

        if (b.time < System.currentTimeMillis())
            return;

        int yPosition = (!Client.instance.isResized() ? 330 : client.canvasHeight - 173);
        if (client.isServerUpdating())
            yPosition -= 13;
        client.newRegularFont.drawBasicString(b.getDecoratedMessage(), !Client.instance.isResized() ? 5 : 0, yPosition, 0xffff00, 0);
    }
}
