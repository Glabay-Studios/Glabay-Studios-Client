package com.client.graphics.interfaces.settings;

import com.client.graphics.interfaces.MenuItem;

public class Setting {
    private int interfaceId;
    private final String settingName;
    private final int defaultOption;
    private final MenuItem menuItem;
    private final String[] options;

    public Setting(String settingName, int defaultOption, MenuItem menuItem, String...options) {
        this.settingName = settingName;
        this.defaultOption = defaultOption;
        this.menuItem = menuItem;
        this.options = options;
    }

    void setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public String getSettingName() {
        return settingName;
    }

    public int getDefaultOption() {
        return defaultOption;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public String[] getOptions() {
        return options;
    }
}
