package com.client.graphics.interfaces.impl;

import java.util.Arrays;

import com.client.*;
import com.client.definitions.ItemDefinition;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.RSInterface;

public class Bank extends RSInterface {

    public static final int EMPTY_CHILD = 41582;
    public static final int BANK_INTERFACE_ID = 5292;
    public static final int SEARCH_CONTAINER = 41583;
    public static final int SEARCH_BUTTON = 18937;
    public static final int TITLE_INTERFACE_ID = 58064;
    public static final int BANK_CONTAINER_HEIGHT = 130;

    /**
     * The main container for each tab and the top container in the main tab.
     */
    public static final int OLD_CONTAINER_INTERFACE_ID = 5382;

    /**
     * Displays the x/350 item count at the top of the bank.
     */
    public static final int ITEM_COUNT_INTERFACE_ID = 58061;

    /**
     * Item containers for the items. The first one is not display but used to contain the main tab items.
     */
    public static final int[] ITEM_CONTAINERS = {41573, 41574, 41575, 41576, 41577, 41578, 41579, 41580, 41581};

    /**
     * Item containers that display the first items in each tab.
     */
    public static final int[] BANK_TAB_ITEM_DISPLAYS = {58040, 58041, 58042, 58043, 58044, 58045, 58046, 58047, 58048};

    /**
     * The components that display the tab sprite.
     */
    public static final int[] TAB_INTERFACE_IDS = {58050, 58051, 58052, 58053, 58054, 58055, 58056, 58057, 58058};

    /**
     * Buttons to collapse tabs into the main tab.
     */
    public static final int[] COLLAPSE_BUTTONS = {-1, 58053, 58064, 58075, 58086, 58097, 58108, 58119, 58130};

    /**
     * Buttons to open the tabs.
     */
    public static final int[] OPEN_TAB_BUTTONS = {58042, 58054, 58065, 58076, 58087, 58098, 58109, 58120, 58131};

    /**
     * Components that display the sprite button
     */
    public static final int[] TAB_SPRITE_DISPLAYS = {58031, 58032, 58033, 58034, 58035, 58036, 58037, 58038, 58039, };

    public static final Sprite TAB_WITH_PLUS = new Sprite("BankTab/TAB 4");
    public static final Sprite TAB_WITHOUT_PLUS = new Sprite("BankTab/TAB 3");

    /**
     * The children inside the bank scrollable that are dynamically replaced with the contaienrs to
     * display the items inside other tabs. Does not include the default bank item container;
     * the main container is static so isn't tracked.
     */
    public static int[] mainTabChildren = new int[ITEM_CONTAINERS.length];

    /**
     * Scrollable that contains the item containers.
     */
    public static RSInterface bankScrollable = null;

    /**
     * String used to search the bank.
     */
    public static String searchingBankString = "";

    public static int getCurrentBankTab() {
        return Client.instance.variousSettings[Configs.BANK_TAB_CONFIG];
    }

    public static boolean isBankContainer(RSInterface rsInterface) {
        return rsInterface.id == SEARCH_CONTAINER || Arrays.stream(ITEM_CONTAINERS).anyMatch(id -> rsInterface.id == id);
    }

    private static boolean moreTabsBelow(int index) {
        return index + 1 < ITEM_CONTAINERS.length && interfaceCache[ITEM_CONTAINERS[index + 1]].getItemContainerRows() > 0;
    }

    public static boolean isSearchingBank() {
        return Client.inputDialogState == 9;
    }

    public static void openBankSearch() {
        bankScrollable.children[mainTabChildren[0]] = SEARCH_CONTAINER;
        Client.inputDialogState = 9;
        interfaceCache[SEARCH_CONTAINER].resetItems();
    }

    public static void closeBankSearch() {
        interfaceCache[SEARCH_CONTAINER].resetItems();
        Client.inputDialogState = 0;
        searchingBankString = "";
    }

    public static void handleButton(int buttonId) {
        for (int index = 1; index < COLLAPSE_BUTTONS.length; index++) {
            if (buttonId == COLLAPSE_BUTTONS[index]) {
                RSInterface tab = interfaceCache[ITEM_CONTAINERS[index]];
                RSInterface main = interfaceCache[ITEM_CONTAINERS[0]];
                if (main.getInventoryContainerFreeSlots() >= tab.inventoryItemId.length - tab.getInventoryContainerFreeSlots()) {
                    RSInterface.addAllItems(tab, main);
                    shiftTabs();
                    openBankTab(0);
                }
            }
        }

        for (int buttonIndex = 0; buttonIndex < OPEN_TAB_BUTTONS.length; buttonIndex++) {
            if (buttonId == OPEN_TAB_BUTTONS[buttonIndex]) {
                openBankTab(buttonIndex);
            }
        }

        if (buttonId == SEARCH_BUTTON) {
            if (bankScrollable.children[mainTabChildren[0]] == SEARCH_CONTAINER) {
                closeBankSearch();
                openBankTab(getCurrentBankTab());
            } else {
                openBankSearch();
            }
        }
    }

    /**
     * Shift items in each tab and shift the tabs left if needed.
     */
    public static void shiftTabs() {
        for (int tabIndex = 1; tabIndex < ITEM_CONTAINERS.length; tabIndex++) {
            RSInterface container = interfaceCache[ITEM_CONTAINERS[tabIndex]];
            // Shift items left
            container.shiftItems();

            // Shift tabs left
            if (container.getItemContainerRows() == 0) {
                for (int shiftTabIndex = tabIndex; shiftTabIndex < ITEM_CONTAINERS.length; shiftTabIndex++) {
                    // shift from shiftTabIndex + 1 to shiftTabIndex
                    if (shiftTabIndex + 1 < ITEM_CONTAINERS.length) {
                        container = interfaceCache[ITEM_CONTAINERS[shiftTabIndex]];
                        RSInterface shiftLeft = interfaceCache[ITEM_CONTAINERS[shiftTabIndex + 1]];
                        container.inventoryItemId = shiftLeft.inventoryItemId.clone();
                        container.inventoryAmounts = shiftLeft.inventoryAmounts.clone();
                    }
                }
            }
        }
    }

    public static void bankUpdates() {
        // Shift tabs and items left
        shiftTabs();

        // Show/hide tabs based on whether they have items in them
        boolean newTabDisplayed = false;
        for (int tabIndex = 1; tabIndex < TAB_INTERFACE_IDS.length; tabIndex++) {
            if (interfaceCache[ITEM_CONTAINERS[tabIndex]].getInventoryContainerFreeSlots() != interfaceCache[ITEM_CONTAINERS[tabIndex]].inventoryItemId.length) {
                RSInterface.interfaceCache[TAB_INTERFACE_IDS[tabIndex]].isMouseoverTriggered = false;
                RSInterface.interfaceCache[TAB_SPRITE_DISPLAYS[tabIndex]].sprite1 = TAB_WITHOUT_PLUS;
            } else if (!newTabDisplayed) {
                RSInterface.interfaceCache[TAB_INTERFACE_IDS[tabIndex]].isMouseoverTriggered = false;
                RSInterface.interfaceCache[TAB_SPRITE_DISPLAYS[tabIndex]].sprite1 = TAB_WITH_PLUS;
                newTabDisplayed = true;
            } else {
                RSInterface.interfaceCache[TAB_INTERFACE_IDS[tabIndex]].isMouseoverTriggered = true;
            }
        }

        // Update the items displayed at the top of the bank
        for (int index = 1; index < BANK_TAB_ITEM_DISPLAYS.length; index++) {
            interfaceCache[BANK_TAB_ITEM_DISPLAYS[index]].inventoryItemId[0] = interfaceCache[ITEM_CONTAINERS[index]].inventoryItemId[0];
            interfaceCache[BANK_TAB_ITEM_DISPLAYS[index]].inventoryAmounts[0] = interfaceCache[ITEM_CONTAINERS[index]].inventoryAmounts[0];
        }
    }

    public static void drawOnBank(RSInterface rsInterface, int x, int y) {
        if (rsInterface.id == BANK_INTERFACE_ID) {
            Rasterizer2D.drawPixels(1, 20, 29, 0xE68A00, 16);
        }
    }

    public static void setupMainTab(RSInterface rsInterface, int x, int y) {
        if (rsInterface.id == SEARCH_CONTAINER) {
            bankUpdates();

            // Hide other bank container
            for (int child : mainTabChildren) {
                bankScrollable.children[child] = EMPTY_CHILD;
            }

            // Set the search container inside the bank
            bankScrollable.children[mainTabChildren[0]] = SEARCH_CONTAINER;

            // Update title
            interfaceCache[TITLE_INTERFACE_ID].message = "Results for '" + searchingBankString + "'";

            // Update search container items
            RSInterface searchContainer = interfaceCache[SEARCH_CONTAINER];
            searchContainer.resetItems();
            if (searchingBankString.length() > 0) {
                for (int index = 0; index < ITEM_CONTAINERS.length; index++) {
                    RSInterface container = interfaceCache[ITEM_CONTAINERS[index]];
                    for (int itemIndex = 0; itemIndex < container.inventoryItemId.length; itemIndex++) {
                        if (container.inventoryItemId[itemIndex] > 0) {
                            ItemDefinition definition = ItemDefinition.lookup(container.inventoryItemId[itemIndex] - 1);
                            if (definition != null && definition.name != null && definition.name.toLowerCase().contains(searchingBankString.toLowerCase())) {
                                searchContainer.addItem(container.inventoryItemId[itemIndex], container.inventoryAmounts[itemIndex]);
                            }
                        }
                    }
                }
            }
        } else if (rsInterface.id == ITEM_CONTAINERS[getCurrentBankTab()]) {
            bankUpdates();

            // Reset title
            interfaceCache[TITLE_INTERFACE_ID].message = "The Bank of " + Configuration.CLIENT_TITLE;

            if (getCurrentBankTab() == 0) {
                // Init the main tab view
                for (int index = 0; index < mainTabChildren.length; index++)
                    bankScrollable.children[mainTabChildren[index]] = EMPTY_CHILD;
                int itemContainerIndex = 0;
                for (int index = 0; index < mainTabChildren.length; index++) {
                    RSInterface container = interfaceCache[ITEM_CONTAINERS[index]];
                    if (index == 0 || container.getInventoryContainerFreeSlots() != container.inventoryItemId.length) {
                        bankScrollable.children[mainTabChildren[itemContainerIndex++]] = ITEM_CONTAINERS[index];
                    }
                }
            } else if (interfaceCache[ITEM_CONTAINERS[getCurrentBankTab()]].getInventoryContainerFreeSlots() ==
                    interfaceCache[ITEM_CONTAINERS[getCurrentBankTab()]].inventoryItemId.length) {
                openBankTab(0);
            }

            // Update the item count
            int size = 0;
            for (int container : ITEM_CONTAINERS) {
                size += interfaceCache[container].inventoryItemId.length - interfaceCache[container].getInventoryContainerFreeSlots();
            }
            interfaceCache[ITEM_COUNT_INTERFACE_ID].message = "<ul>" + size + "</ul>";

            // Hide/display the main tab containers
            if (getCurrentBankTab() == 0) {
                int height = 4;
                for (int index = 0; index < ITEM_CONTAINERS.length; index++) {
                    RSInterface container = interfaceCache[ITEM_CONTAINERS[index]];
                    if (index != 0 && moreTabsBelow(index - 1)) {
                        Client.instance.bankDivider.drawSprite(x, height + y);
                        height += 10; // buffer for tab separator
                    }
                    bankScrollable.childY[mainTabChildren[index]] = height;
                    height += container.getItemContainerHeight();
                }
                bankScrollable.scrollMax = height > bankScrollable.height + 1 ? height : bankScrollable.height + 1;
            } else {
                int height = rsInterface.getItemContainerHeight();
                bankScrollable.scrollMax = height > bankScrollable.height + 1 ? height : bankScrollable.height + 1;
            }
        }
    }

    public static void openBankTab(int tab) {
        // Don't open if tab is empty.
        if (interfaceCache[ITEM_CONTAINERS[tab]].getInventoryContainerFreeSlots() == interfaceCache[ITEM_CONTAINERS[tab]].inventoryItemId.length && tab != 0) {
            return;
        }

        closeBankSearch();

        Client.instance.variousSettings[Configs.BANK_TAB_CONFIG] = tab;

        // Reset the lines sprites that show the tab as closed
        for (int tabIndex = 0; tabIndex < ITEM_CONTAINERS.length; tabIndex++) {
            if (tabIndex == 0) {
                Client.instance.variousSettings[34 + tabIndex] = 1;
            } else {
                Client.instance.variousSettings[34 + tabIndex] = 0;
            }
        }

        // Set the sprite to show the current tab as open
        if (tab == 0) {
            Client.instance.variousSettings[34 + tab] = 0;
        } else {
            Client.instance.variousSettings[34 + tab] = 1;
        }

        // Reset scroll position
        bankScrollable.scrollPosition = 0;

        if (tab != 0) {
            // Hide the main tab view
            for (int child : mainTabChildren) {
                bankScrollable.children[child] = EMPTY_CHILD;
            }
        }

        // Set the tab container
        bankScrollable.children[mainTabChildren[0]] = ITEM_CONTAINERS[tab];
    }

    public static void onConfigChanged(int config, int value) {
        if (config == Configs.BANK_TAB_CONFIG) {
            closeBankSearch();
            openBankTab(value);
        }
    }

    private void editBank(TextDrawingArea[] tda) {
        // Store the bank scrollable
        bankScrollable = interfaceCache[5385];

        // Fixing container placement on interface
        bankScrollable.height -= 2;
        RSInterface bank = interfaceCache[BANK_INTERFACE_ID];
        for (int index = 0; index < bank.children.length; index++) {
            if (bank.children[index] == bankScrollable.id) {
                bank.childY[index] += 2;
            }
        }

        // Remove old container
        if (!RSInterface.deleteChild(OLD_CONTAINER_INTERFACE_ID, bankScrollable)) {
            System.out.println("Skipping bank reload.");
            return;
        }

        // Empty text to hide other containers while not in main tab
        addText(EMPTY_CHILD, "", tda, 1, 0xE68A00, true, true);

        // Adding new container for main tab
        int newContainersStartIndex = expandChildren(ITEM_CONTAINERS.length, bankScrollable);
        for (int index = 0; index < ITEM_CONTAINERS.length; index++) {
            RSInterface container = addInventoryContainer(ITEM_CONTAINERS[index], 10, BANK_CONTAINER_HEIGHT,12, 0, true);
            container.actions = new String[] {"Withdraw 1", "Withdraw 5", "Withdraw 10", "Withdraw All", "Withdraw X", "Withdraw All but one"};
            container.contentType = 206;
            mainTabChildren[index] = newContainersStartIndex;
            bankScrollable.child(newContainersStartIndex++, ITEM_CONTAINERS[index], 38, 0);
        }

        // Turn on dragging to other item containers
        for (int container : ITEM_CONTAINERS) {
            interfaceCache[container].allowInvDraggingToOtherContainers = true;
        }

        RSInterface container = addInventoryContainer(SEARCH_CONTAINER, 10, BANK_CONTAINER_HEIGHT,12, 0, true);
        container.actions = new String[] {"Withdraw 1", "Withdraw 5", "Withdraw 10", "Withdraw All", "Withdraw X", "Withdraw All but one"};
    }

    public void bank(TextDrawingArea[] tda) {
        RSInterface rs = addInterface(BANK_INTERFACE_ID);
        rs.message = "";
        setChildren(41, rs);
        addSprite(58001, 0, "BankTab/07/BANK");
        addHoverButton(5384, "BankTab/updated/CLOSE", 97, 21, 21, "Close Window", 250, 5380, 3);
        addHoveredButton(5380, "BankTab/updated/CLOSE", 98, 21, 21, 5379);
        addHoverButton(5294, "BankTab/07/BANK", 7, 37, 29, "Set/Edit Your Account-Pin", 250, 5295, 4);
        addHoveredButton(5295, "BankTab/BANK", 4, 100, 33, 5296);


        //Item Movement
        addBankHover(58002, 4, 58003, 0, 1, "BankTab/updated/FLOW", 50, 22, 304, 1, "Swap Item Movement Mode", 58004, 7, 6,
                "BankTab/BANK", 58005, "Switch to insert items \nmode", "Switch to swap items \nmode.", 12, 20); //7


        //Noted
        addBankHover(58010, 4, 58011, 0, 1, "BankTab/updated/FLOW", 50, 22, 116, 1, "Enable/Disable Noted Withdrawal", 58012,
                10, 12, "BankTab/BANK", 58013, "Switch to note withdrawal \nmode", "Switch to item withdrawal \nmode",
                12, 20); //9


        addBankHover1(58018, 5, 58019, 1, "BankTab/07/BANK", 37, 29, "Deposit carried items", 58020, 2,
                "BankTab/07/BANK", 58021, "Empty your backpack into\nyour bank", 0, 20); //12

        addBankHover1(58026, 5, 58027, 3, "BankTab/07/BANK", 35, 25, "Deposit worn items", 58028, 4, "BankTab/07/BANK",
                58029, "Empty the items your are\nwearing into your bank", 0, 20); //14

        for (int i = 0; i < 9; i++) {
            addInterface(58050 + i);
            if (i == 0) {
                addConfigButton(58031, BANK_INTERFACE_ID, 0, 1, "BankTab/TAB", 48, 38, new String[] {"Price Check", "View"}, 1,
                        34);
                RSInterface.interfaceCache[58031].ignoreConfigClicking = true;
            } else {
                addConfigButton(58031 + i, BANK_INTERFACE_ID, 4, 2, "BankTab/TAB", 48, 38,
                        new String[] {"Price Check", "Collapse", "View"}, 1, 34 + i);
                RSInterface.interfaceCache[58031 + i].ignoreConfigClicking = true;
            }
            addToItemGroup(58040 + i, 1, 1, 0, 0, false, "", "", "");
        }

        addText(58061, "0", tda, 0, 0xE68A00, true, true); //24
        addText(58062, "350", tda, 0, 0xE68A00, true, true);

        addInputField(58063, 50, 0xE68A00, "Search", 235, 23, false, true);
        addText(58064, "The Bank of " + Configuration.CLIENT_TITLE, tda, 2, 0xE68A00, true, true);

        addBankHover(18929, 4, 18930, 0, 1, "BankTab/updated/FLOW", 50, 22, 305, 1, "Swap Item Movement Mode", 18931, 7, 6,
                "BankTab/BANK", 18932, "Switch to insert items \nmode", "Switch to swap items \nmode.", 12, 20); //7

        addBankHover(18933, 4, 18934, 0, 1, "BankTab/updated/FLOW", 50, 22, 117, 1, "Enable/Disable Noted Withdrawal", 18935,
                10, 12, "BankTab/BANK", 18936, "Switch to note withdrawal \nmode", "Switch to item withdrawal \nmode",
                12, 20); //9

        addBankHover1(18937, 5, 18938, 0, "BankTab/updated/SEARCH", 36, 36, "Search items", 18939, 4, "BankTab/updated/SEARCH",
                18940, "Empty the items your are\nwearing into your bank", 0, 1); //14

        addClickableSprites(58014, "Enable/Disable Always Placeholders", "BankTab/07/BANK", 5, 6, 5);

        addText(18941, "Rearrange mode:", tda, 1, 0xE68A00, false, true);
        addText(18942, "Withdraw as:", tda, 1, 0xE68A00, true, true);

        addText(18943, "Swap", tda, 1, 0xE68A00, true, true);
        addText(18944, "Insert", tda, 1, 0xE68A00, true, true);
        addText(18945, "Item", tda, 1, 0xE68A00, true, true);
        addText(18946, "Note", tda, 1, 0xE68A00, true, true);

        addBankHover1(18947, 5, 18948, 0, "Presets/OPEN", 36, 36, "Open Preset Interface", 18949, 4, "Presets/OPEN",
                18950, "Open Preset Interface", 0, 1);

        //addInputField(58063, 50, 0xE68A00, "Search", 235, 23, false, true);
        RSInterface Interface = interfaceCache[5385];
        Interface.height = 202;
        Interface.width = 481;


        setBounds(58001, 14, 3, 0, rs);
        setBounds(5384, 474, 10, 1, rs);
        setBounds(5380, 474, 10, 2, rs);
        setBounds(5294, 312, 292, 3, rs); // Bank pin
        setBounds(5295, 312, 297, 4, rs);
        setBounds(58002, 20, 306, 5, rs); // Rearrange mode
        setBounds(58003, 20, 306, 6, rs);
        setBounds(58010, 120, 306, 7, rs); // Noting
        setBounds(58011, 120, 306, 8, rs);

        setBounds(58018, 423, 292, 9, rs); // Items
        setBounds(58019, 423, 292, 10, rs);

        setBounds(58026, 460, 292, 11, rs); // Invo

        setBounds(58027, 460, 292, 12, rs);
        setBounds(5385, -3, 76, 13, rs);
        RSInterface.interfaceCache[5385].height = 216;
        int x = 59;
        for (int i = 0; i < 9; i++) {
            setBounds(58050 + i, 0, 0, 15 + i, rs);
            RSInterface rsi = interfaceCache[58050 + i];
            setChildren(2, rsi);
            setBounds(58031 + i, x, 38, 0, rsi);
            setBounds(58040 + i, x + 5, 42, 1, rsi);
            x += 41;
        }
        // 0-350
        setBounds(58061, 36, 9, 24, rs);
        setBounds(58062, 36, 21, 25, rs);

//		setBounds(58063, 25, 298, 26, rs); // Search
        setBounds(58064, 250, 13, 26, rs);

        setBounds(18929, 70, 306, 27, rs); // Swap items
        setBounds(18930, 70, 306, 28, rs);

        setBounds(18933, 170, 306, 29, rs); // Noting
        setBounds(18934, 170, 306, 30, rs);

        setBounds(18937, 386, 292, 31, rs); // Search
        setBounds(18938, 386, 292, 32, rs);

//
        setBounds(58014, 349, 292, 33, rs); // Placeholder

        setBounds(18941, 22, 291, 34, rs); // Titles
        setBounds(18942, 170, 291, 35, rs); // Titles

        setBounds(18943, 45, 309, 36, rs); // Titles
        setBounds(18944, 95, 309, 37, rs); // Titles
        setBounds(18945, 145, 309, 38, rs); // Titles
        setBounds(18946, 195, 309, 39, rs); // Titles

        setBounds(18947, 275, 292, 40, rs); // Titles

        editBank(tda);
    }

}
