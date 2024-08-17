package com.client.graphics.interfaces.settings;

import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.dropdown.*;
import com.client.graphics.interfaces.impl.Interfaces;

import java.util.Arrays;

public class SettingsInterface extends RSInterface {

    public static final Setting OLD_GAMEFRAME = new Setting("Gameframe", 1, new OldGameframeMenu(), "2006", "OSRS");
    public static final Setting GAME_TIMERS = new Setting("Game Timers", 0, new GameTimersMenu(), "On", "Off");
    public static final Setting ANTI_ALIASING = new Setting("Anti-aliasing", 1, new AntiAliasingMenu(), "On", "Off");
    public static final Setting GROUND_ITEM_NAMES = new Setting("Ground Item Names", 1, new GroundItemOverlayMenu(), "On", "Off");
    public static final Setting FOG = new Setting("Fog", 0, new FogMenu(), "On", "Off");
    public static final Setting SMOOTH_SHADING = new Setting("Smooth Shading", 0, new SmoothShadingMenu(), "On", "Off");
    public static final Setting TILE_BLENDING = new Setting("Tile blending", 1, new TileBlendingMenu(), "On", "Off");
    public static final Setting STATUS_BARS = new Setting("Prayer & hp bar", 1, new StatusBarsMenu(), "On", "Off");
    public static final Setting DRAG = new Setting("Drag", 0, new DragMenu(), "5 (default)", "6", "8", "10", "12");
    public static final Setting PVP_TAB = new Setting("Pvp tab", 0, new PvpTab(), "On", "Off");
    public static final Setting ROOF = new Setting("Enable roofs", 0, new EnableRoofs(), "On", "Off");
    public static final Setting MENU_HOVERS = new Setting("Menu hovers", 0, new MenuHovers(), "Off", "On");
    public static final Setting STRETCHED_MODE = new Setting("Stretched Mode", 1, new StretchedModeMenu(), "On", "Off");
    public static final Setting PLAYER_PROFILE = new Setting("Player Profile Tab", 0, new PlayerProfile(), "On", "Off");

    public static final Setting INVENTORY_MENU = new Setting("Inventory Menu", 1, new InventoryHoverMenu(), "Off", "On (Magenta)", "On (Lime green)", "On (Cyan)", "On (Red)");
    public static final Setting BOUNTY_HUNTER = new Setting("Bounty Hunter", 0, new BountyHunterMenu(), "On", "Off");
    public static final Setting ENTITY_TARGET = new Setting("Entity Target", 0, new EntityTargetOverlayMenu(), "On", "Off");
    public static final Setting CHAT_EFFECT = new Setting("Chat Effect", 0, new ChatEffectMenu(), "Yellow (default)", "Red","Green","Cyan","Purple","White","Flash 1","Flash 2", "Flash 3","Glow 1", "Glow 2","Glow 3");
    public static final Setting DRAW_DISTANCE = new Setting("Draw distance", 0, new DrawDistanceMenu(), "30 (default)", "40", "50", "60", "70");
    public static final Setting PM_NOTIFICATION = new Setting("Private message notification", 1, new PmNotificationMenu(), "On", "Off");

    private int childInterfaceId;
    private int childIndex;
    private int yOffset = 0;
    private RSInterface scroll;

    public SettingsInterface() {
    }

    public void load(TextDrawingArea[] tda) {
        RSInterface tab = addInterface(39000);
        addSprite(39001, 0, "/Interfaces/settings/IMAGE");
        addText(39002, "Advanced Settings", tda, 2, 0xFFA500, true, true);
        String dir = "/Interfaces/Prestige/SPRITE";
        addHoverButton(39021, dir, 1, 16, 16, "Close", -1, 39022, 3);
        addHoveredButton(39022, dir, 2, 16, 16, 39023);

        setChildren(5,tab);
        int index = 0;
        setBounds(39001,16,16,index++,tab);
        setBounds(39002,253,24,index++,tab);
        setBounds(39003,30,60,index++,tab);
        setBounds(39021,478,24,index++,tab);
        setBounds(39022,478,24,index++,tab);

        scroll = addInterface(39003);
        scroll.width = 448;
        scroll.height = 246;
        childInterfaceId = 39023;
        childIndex = 0;

        SettingsWidgetSection[] sections = {
                new SettingsWidgetSection("Interface Options", OLD_GAMEFRAME, INVENTORY_MENU, BOUNTY_HUNTER, ENTITY_TARGET, GAME_TIMERS, CHAT_EFFECT, GROUND_ITEM_NAMES, PM_NOTIFICATION),
                new SettingsWidgetSection("Graphics Options", STRETCHED_MODE, DRAW_DISTANCE, ANTI_ALIASING, FOG, SMOOTH_SHADING, TILE_BLENDING , STATUS_BARS , DRAG , PVP_TAB, ROOF, MENU_HOVERS, PLAYER_PROFILE),
        };

        Arrays.stream(sections).forEach(section -> {
            expandChildren(1, scroll);
            header(section);
            Arrays.stream(section.settings).forEach(setting -> {
                expandChildren(2, scroll);
                build(setting);
            });
        });

        scroll.reverseChildren();
        scroll.scrollMax = yOffset + 80;//32
    }

    public void header(SettingsWidgetSection section) {
        addText(childInterfaceId, section.sectionName, Interfaces.defaultTextDrawingAreas, 2, 0xFFA500, false, true);
        setBounds(childInterfaceId,24, yOffset, childIndex, scroll);
        yOffset += 20;
        childInterfaceId++;
        childIndex++;
    }

    public void build(Setting setting) {
        setting.setInterfaceId(childInterfaceId + 1);
        addText(childInterfaceId, setting.getSettingName(), Interfaces.defaultTextDrawingAreas, 1, 0xFFA500, false, true);
        dropdownMenu(setting.getInterfaceId(), 166,setting.getDefaultOption(), setting.getOptions(), setting.getMenuItem(), Interfaces.defaultTextDrawingAreas, 1);
        setBounds(childInterfaceId,32, yOffset, childIndex, scroll);
        setBounds(childInterfaceId + 1,260, yOffset, childIndex + 1, scroll);
        childInterfaceId += 2;
        childIndex += 2;
        yOffset += 25;
    }

    private static class SettingsWidgetSection {

        private final String sectionName;
        private final Setting[] settings;

        public SettingsWidgetSection(String sectionName, Setting...settings) {
            this.sectionName = sectionName;
            this.settings = settings;
        }
    }
}
