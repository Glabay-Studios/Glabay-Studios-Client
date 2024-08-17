package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.features.settings.Preferences;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

public class PmNotificationMenu implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        Preferences.getPreferences().pmNotifications = optionSelected == 0;
    }
}
