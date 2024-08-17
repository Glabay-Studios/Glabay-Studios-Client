package com.client.graphics.interfaces.builder.impl;

import java.util.HashMap;
import java.util.Map;

import com.client.Sprite;
import com.client.features.settings.Preferences;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;
import com.client.graphics.interfaces.impl.Interfaces;

public class NotificationTab extends InterfaceBuilder {

    public static NotificationTab instance = new NotificationTab();
    private static final Sprite BG = new Sprite("/notifications_tab/bg");
    private static final Sprite BUTTON_UNDERLAY = new Sprite("/notifications_tab/button");

    public static final int ALWAYS_SHOW_UNTRADABLES_BUTTON_ID = 42_669;

    public Scrollable scrollable;

    private NotificationTab() {
        super(42_658);
    }

    @Override
    public void build() {
        addText(nextInterface(), 2, RSInterface.DEFAULT_TEXT_COLOR, true, "Notifications");
        child(90, 4);

        child(Interfaces.CLOSE_BUTTON_SMALL, 172, 4);
        child(Interfaces.CLOSE_BUTTON_SMALL_HOVER, 172, 4);

        scrollable = new Scrollable(nextInterface());
        scrollable.build();
        child(3, 24);
    }

    public static class Scrollable extends InterfaceBuilder {

        private Map<String, RSInterface> inputFields = new HashMap<>();

        public Scrollable(int baseInterfaceId) {
            super(baseInterfaceId);
        }

        @Override
        public void build() {
            getRoot().width = 169;
            getRoot().height = 226;
            getRoot().scrollMax = 227;

            addSprite(nextInterface(), BG);
            child(0, 0);

            int y = 12;
            y = inputField(y, "Show Items > Value", "[0-9]", "0", 10,">value");
            y = inputField(y, "Show Items", "[A-Za-z0-9 ,']", "", 500, "show");
            y = inputField(y, "Hide items", "[A-Za-z0-9 ,']", "", 500, "hide");
            y = toggleButton(y, "Show Untradeable Loot");
        }

        private int inputField(int y, String headerText, String regex, String defaultInputFieldMessage, int characterLimit, String type) {
            addText(nextInterface(), 0, RSInterface.DEFAULT_TEXT_COLOR, true, headerText);
            child(84, y);

            y += 12;
            addInputField(nextInterface(), characterLimit, 0xFF981F, defaultInputFieldMessage, 144, 20, false, true, regex, message -> handleInputFieldChanged(type, message), false);
            inputFields.put(type, get(getCurrentInterfaceId() - 1));
            child(11, y);

            y += 26;
            return y;
        }

        private int toggleButton(int y, String headerText) {
            addText(nextInterface(), 0, RSInterface.DEFAULT_TEXT_COLOR, true, headerText);
            child(84, y);

            y += 16;
            addButton(nextInterface(), BUTTON_UNDERLAY, "Toggle", 4);
            get(getCurrentInterfaceId() - 1).buttonListener = this::toggleButton;
            child(24, y);

            y += 4;
            addText(nextInterface(), 0, RSInterface.DEFAULT_TEXT_COLOR, true, "On");
            child(84, y);

            y += 24;
            return y;
        }

        public void update(String type, String message) {
            inputFields.get(type).message = message;
        }

        private void handleInputFieldChanged(String type, String message) {
            Preferences preferences = Preferences.getPreferences();
            switch (type) {
                case ">value":
                    preferences.groundItemTextShowMoreThan = message;
                    break;
                case "show":
                    preferences.groundItemTextShow = message;
                    break;
                case "hide":
                    preferences.groundItemTextHide = message;
                    break;
            }
        }

        private void toggleButton(int id) {
            switch (id) {
                case ALWAYS_SHOW_UNTRADABLES_BUTTON_ID:
                    Preferences.getPreferences().groundItemAlwaysShowUntradables = !Preferences.getPreferences().groundItemAlwaysShowUntradables;
                    break;
            }

            updateButtonText(id);
        }

        public void updateButtonText(int id) {
            switch (id) {
                case ALWAYS_SHOW_UNTRADABLES_BUTTON_ID:
                    get(ALWAYS_SHOW_UNTRADABLES_BUTTON_ID + 1).message = Preferences.getPreferences().groundItemAlwaysShowUntradables ? "On" : "Off";
                    break;
            }
        }
    };
}
