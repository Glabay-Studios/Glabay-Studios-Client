package com.client.graphics;

import net.runelite.rs.api.RSDefaultsGroup;

public class DefaultsGroup implements RSDefaultsGroup {

    public static final DefaultsGroup groups = new DefaultsGroup(3);

    public final int group;

    DefaultsGroup(int group) {
        this.group = group;
    }


}
