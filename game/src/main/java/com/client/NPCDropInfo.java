package com.client;

public class NPCDropInfo {

    public static NPCDropInfo[] list = new NPCDropInfo[1000];

    public static void addEntry(NPCDropInfo info) {
        if (exists(info.message)) {
            return;
        }
        int freeIndex = getFreeIndex();
        if (freeIndex == -1)
            return;
        list[freeIndex] = info;
    }

    public static boolean exists(String message) {
        for (NPCDropInfo info :  list) {
            if (info != null) {
                System.err.println(info.message);
                if (info.message != null) {
                    if (info.message.equalsIgnoreCase(message))
                        return true;
                }
            }
        }
        return false;
    }

    public static int getFreeIndex() {
        int index = -1;
        for (int i = 0 ; i < list.length; i++) {
            NPCDropInfo info = list[i];
            if (info == null)
                return i;
        }
        return index;
    }

    public static NPCDropInfo getEntry(String messageToSearch) {
        for (NPCDropInfo info :  list) {
            if (info != null) {
                if (info.message != null) {
                    if (info.message.equalsIgnoreCase(messageToSearch))
                        return info;
                }
            }
        }
        return null;
    }

    public NPCDropInfo() {

    }

    public int npcIndex;

    public String message;

    public int index;
}
