package com.client.graphics.interfaces.dropdown;

import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.impl.Keybinding;
import com.client.graphics.interfaces.settings.Setting;

public class KeybindingMenu implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        Keybinding.bind((rsInterface.id - Keybinding.MIN_FRAME) / 3, optionSelected);
    }
}
