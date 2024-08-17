package com.client.hover;

import com.client.Client;
import com.client.RSFont;
import com.client.Rasterizer2D;
import com.client.Sprite;
import com.client.definitions.ItemDefinition;
import com.client.itembonus.ItemBonusDefinitionLoader;
import com.client.model.Items;

import java.util.Arrays;
import java.util.HashMap;

/**
 *
 *
 * @author C.T for koranes
 *
 */

public class HoverMenuManager {

    public static final int BACKGROUND_COLOUR = 0xFFFFFFF;

    public static HashMap<Integer, HoverMenu> menus = new HashMap<>();

    public static void init() {

        //seeds
        menus.put(22885, new HoverMenu("Donate to the well for 1 hour of x2 raid keys."));//krono seed
        menus.put(22883, new HoverMenu("Donate to the well for 1 hour of x10 droprate boost."));//iasor seed
        menus.put(22881, new HoverMenu("Donate to the well for 1 hour of 50% bonus experience."));//attas seed
        menus.put(6112, new HoverMenu("Donate to the well for 1 hour of x2 larren's keys."));//kelda seed
        menus.put(20903, new HoverMenu("Donate to the well for 1 hour of x2 slayer points."));//noxifier seed
        menus.put(20909, new HoverMenu("Donate to the well for 1 hour of x2 boss points."));//buchu seed
        menus.put(22869, new HoverMenu("Donate to the well for 1 hour of x2 brimstone keys."));//celastrus seed
        menus.put(20906, new HoverMenu("Donate to the well for 1 hour of x2 bonus loot."));//golpar seed
        menus.put(4205, new HoverMenu("Donate to the well for 1 hour of +5 bonus pc points."));//consercreation seed

        // PETS:
        menus.put(16015, new HoverMenu("10% increased droprate boost."));//dharok pet

        menus.put(6806, new HoverMenu("When used with the ring of Xeros +12% droprate bonus."));//runerogue imbue scroll

        menus.put(30023, new HoverMenu("100% chance to double votes when summoned."));//vote pet
        menus.put(30010, new HoverMenu("80% chance to pickup crystal keys that drop."));//postie pete
        menus.put(30110, new HoverMenu("Picks up all crystal keys & 25% chance to double."));//dark postie pete
        menus.put(30011, new HoverMenu("80% chance to pickup clue scrolls that drop."));//imp
        menus.put(30111, new HoverMenu("Picks up all crystal keys & 25% chance to double."));//dark imp
        menus.put(30012, new HoverMenu("80% chance to pickup resource packs."));//toucan
        menus.put(30112, new HoverMenu("Picks up all resource packs & 25% chance to double."));//dark toucan
        menus.put(30013, new HoverMenu("80% chance to auto-pickup coin bags."));//penguin king
        menus.put(30113, new HoverMenu("Picks up all coins bags & 25% chance to double."));//dark penguin king
        menus.put(30014, new HoverMenu("5% increased drop rate boost."));//klik
        menus.put(30114, new HoverMenu("10% increased drop rate boost."));//dark klik
        menus.put(30015, new HoverMenu("50% chance for an additional +10% strength bonus in pvm."));//shadow warrior
        menus.put(30115, new HoverMenu("Gives constant +10% strength bonus in pvm."));//dark shadow warrior

        menus.put(30016, new HoverMenu("50% chance for an additional +10% range strength bonus in pvm."));//shadow archer
        menus.put(30116, new HoverMenu("Gives constant +10% range strength bonus in pvm."));//dark shadow archer
        menus.put(30017, new HoverMenu("50% chance for an additional +10% mage strength bonus in pvm."));//shadown wizard
        menus.put(30117, new HoverMenu("Gives constant +10% mage strength bonus in pvm."));//dark shadown wizard
        menus.put(30018, new HoverMenu("5% chance npc hit restores your hitpoints."));//healer death spawn
        menus.put(30118, new HoverMenu("10% chance npc hit restores your hitpoints."));//dark healer death spawn
        menus.put(30019, new HoverMenu("5% chance half your hit is restored into prayer."));//holy death spawn
        menus.put(30119, new HoverMenu("10% chance half your hit is restored into prayer."));//dark holy death spawn
        menus.put(23939, new HoverMenu("50% chance for seren to hit 0s."));//seren
        menus.put(30123, new HoverMenu("Dont worry be happy."));//dark seren
        menus.put(30020, new HoverMenu("Extra 5% in droprate boost & 50% chance for +10 strength bonus for all styles in pvm."));//corrupt beast
        menus.put(30120, new HoverMenu("Extra 10% in droprate boost & constant +10 strength bonus for all styles in pvm."));//dark corrupt beast
        menus.put(30021, new HoverMenu("10% increased drop rate boost."));//roc
        menus.put(30121, new HoverMenu("20% increased drop rate boost."));//Dark roc
        menus.put(30022, new HoverMenu("The most powerful pet on Xeros."));//Kratos
        menus.put(30122, new HoverMenu("The most powerful pet on Xeros."));//Dark Kratos

        //MISC
        menus.put(21307, new HoverMenu("Contains high-tier pvp related gear."));//rouge crate
        menus.put(2528, new HoverMenu("Gives you 150k XP (regular) or 30k XP (hard) in any skill of your choice. The item is tradeable."));
        menus.put(12863, new HoverMenu("Purchase this to receive a dwarf cannon set!"));
        menus.put(24466, new HoverMenu("Apply this to a slayer helmet to create a twisted slayer helmet."));
        menus.put(8152, new HoverMenu("This chest will take you into the vote boss instance."));//vote boss chest

        //WEAPONS:
        menus.put(34037, new HoverMenu("10% increased strength bonus & 7% droprate boost."));//death cape


        // EXPERIENCE BOOSTED ITEMS:

        menus.put(21752, new HoverMenu("Collects & doubles drops to inventory or bank when equipped."));
        menus.put(21126, new HoverMenu("Auto collects all foe items to bank from skilling & 1/100 chance to double all drops."));
        menus.put(7409, new HoverMenu("Provides bonus herb yield while wearing the item."));
        menus.put(10071, new HoverMenu("Provides bonus hunter experince while wearing the item."));
        menus.put(12639, new HoverMenu("Provides bonus prayer experience when sacrificing to an altar while wearing the item."));
        menus.put(12638, new HoverMenu("Provides bonus prayer experience when sacrificing to an altar while wearing the item."));
        menus.put(12637, new HoverMenu("Provides bonus prayer experience when sacrificing to an altar while wearing the item."));
        menus.put(11850, new HoverMenu("Makes you use less run energy while wearing this item."));
        menus.put(11852, new HoverMenu("Makes you use less run energy while wearing this item."));
        menus.put(11854, new HoverMenu("Makes you use less run energy while wearing this item."));
        menus.put(11856, new HoverMenu("Makes you use less run energy while wearing this item."));
        menus.put(11858, new HoverMenu("Makes you use less run energy while wearing this item."));
        menus.put(11860, new HoverMenu("Makes you use less run energy while wearing this item."));
        menus.put(10941, new HoverMenu("Provides bonus woodcutting experience while wearing the item."));
        menus.put(10939, new HoverMenu("Provides bonus woodcutting experience while wearing the item."));
        menus.put(10940, new HoverMenu("Provides bonus woodcutting experience while wearing the item."));
        menus.put(10933, new HoverMenu("Provides bonus woodcutting experience while wearing the item."));
        menus.put(12013, new HoverMenu("Provides bonus mining experience while wearing the item."));
        menus.put(12014, new HoverMenu("Provides bonus mining experience while wearing the item."));
        menus.put(12015, new HoverMenu("Provides bonus mining experience while wearing the item."));
        menus.put(12016, new HoverMenu("Provides bonus mining experience while wearing the item."));
        menus.put(13258, new HoverMenu("Provides bonus fishing experience while wearing the item."));
        menus.put(13259, new HoverMenu("Provides bonus fishing experience while wearing the item."));
        menus.put(13260, new HoverMenu("Provides bonus fishing experience while wearing the item."));
        menus.put(13261, new HoverMenu("Provides bonus fishing experience while wearing the item."));
        menus.put(13646, new HoverMenu("Provides bonus farming experience while wearing the item."));
        menus.put(13642, new HoverMenu("Provides bonus farming experience while wearing the item."));
        menus.put(13643, new HoverMenu("Provides bonus farming experience while wearing the item."));
        menus.put(13640, new HoverMenu("Provides bonus farming experience while wearing the item."));
        menus.put(13644, new HoverMenu("Provides bonus farming experience while wearing the item."));
        menus.put(20708, new HoverMenu("Provides bonus firemaking experience while wearing the item."));
        menus.put(20710, new HoverMenu("Provides bonus firemaking experience while wearing the item."));
        menus.put(20712, new HoverMenu("Provides bonus firemaking experience while wearing the item."));
        menus.put(20704, new HoverMenu("Provides bonus firemaking experience while wearing the item."));
        menus.put(20706, new HoverMenu("Provides bonus firemaking experience while wearing the item."));
        menus.put(20008, new HoverMenu("Provides bonus runecrafting experience while wearing the item."));
        menus.put(5554, new HoverMenu("Provides bonus thieving experience while wearing the item."));
        menus.put(5553, new HoverMenu("Provides bonus thieving experience while wearing the item."));
        menus.put(5555, new HoverMenu("Provides bonus thieving experience while wearing the item."));
        menus.put(5556, new HoverMenu("Provides bonus thieving experience while wearing the item."));
        menus.put(5557, new HoverMenu("Provides bonus thieving experience while wearing the item."));
        menus.put(24034, new HoverMenu("Provides bonus gem cutting  experience while wearing the item."));
        menus.put(24037, new HoverMenu("Provides bonus gem cutting  experience while wearing the item."));
        menus.put(24040, new HoverMenu("Provides bonus gem cutting  experience while wearing the item."));
        menus.put(24043, new HoverMenu("Provides bonus gem cutting  experience while wearing the item."));
        menus.put(24046, new HoverMenu("Provides bonus gem cutting experience while wearing the item."));
        menus.put(7447, new HoverMenu("Provides bonus log cutting experience while having this item."));


        // MYSTERY BOXES:
        menus.put(6199, new HoverMenu("Contains various rare rewards:",//regular m box
                Arrays.asList(12002, 11804, 11806, 11808, 11832, 11834, 11826, 11828, 11830, 10346, 10348, 10350, 10352, 10330, 10332, 10338, 10340,
                        10334, 10342, 10344)));


        menus.put(6828, new HoverMenu("Contains various rare rewards:",//Super m box
                Arrays.asList(11802,
                        11826,
                        11828,
                        11830,
                        11832,
                        11834,
                        4084,
                        13346,
                        11785,
                        12437,
                        12424,
                        12426,
                        12422,
                        20014,
                        2403,
                        13239,
                        13235,
                        12785,
                        13237)));

        menus.put(13346, new HoverMenu("Contains various rare items:",//ultra m box
                Arrays.asList(22325,21295,20997,1038,
                        1040,
                        1042,
                        1044,
                        1046,
                        1048,
                        24111,
                        13346,
                        26484,
                        34037,
                        26233,
                        21752,
                        11862)));
        //KEYS:
        menus.put(989, new HoverMenu("Use it on the crystal chest to get a reward:",
                Arrays.asList(11840, 990, 24034, 24037, 24040, 24043, 24046, 21547, 21549, 21551, 21553)));

        menus.put(4589, new HoverMenu("Use it on the glod chest to get a reward:",
                Arrays.asList(20784, 21034, 22622, 22610, 22613, 22547, 22552, 22542, 22610)));//done

        menus.put(23083, new HoverMenu("Use it on the brimstone chest to get a reward:",
                Arrays.asList(Items.BURNT_PAGE, Items.DRAGON_HARPOON, Items.DRAGON_SWORD, Items.MYSTIC_BOOTS_DUSK, Items.MYSTIC_GLOVES_DUSK, Items.MYSTIC_HAT_DUSK, Items.MYSTIC_ROBE_BOTTOM_DUSK, Items.MYSTIC_ROBE_TOP_DUSK, Items.BOOTS_OF_BRIMSTONE, Items.GUARDIAN_BOOTS)));

        menus.put(22428, new HoverMenu("Use it on the solak chest to get a reward:",
                Arrays.asList(6524, 6528, 6522, 4587, 9185, 21905, 11230 , 4153, 11840, 6585, 2572)));//done

        menus.put(6792, new HoverMenu("Use it on the seren chest to get a reward:",
                Arrays.asList(22638, 22641, 22644, 22650, 22653, 22656, 24419, 24417, 24420, 24421)));

        menus.put(1464, new HoverMenu("Can be exchanged for vote points or sold to players."));
        menus.put(2996, new HoverMenu("Can be exchanged for PK points or sold to players."));

        System.out.println("Xeros has loaded " + menus.size() + "x menu hovers.");
    }

    public static int drawType() {
        if (Client.instance.getMouseX() > 0 && Client.instance.getMouseX() < 500 && Client.instance.getMouseY() > 0
                && Client.instance.getMouseY() < 300) {
            return 1;
        }
        return 0;
    }

    public static boolean shouldDraw(int id) {
        return menus.get(id) != null;
    }

    public static boolean showMenu;

    public static String hintName;

    public static int hintId;

    public static int displayIndex;

    public static long displayDelay;

    public static int[] itemDisplay = new int[4];

    private static int lastDraw;

    public static void reset() {
        showMenu = false;
        hintId = -1;
        hintName = "";
    }

    public static boolean canDraw() {

        return false;
    }

    public static void drawHintMenu() {
        int mouseX = Client.instance.getMouseX();
        int mouseY = Client.instance.getMouseY();
        if (!canDraw()) {
            return;
        }

        if (Client.instance.isResized()) {
            if (Client.instance.getMouseY() < Client.canvasHeight - 450
                    && Client.instance.getMouseX() < Client.canvasWidth - 200) {
                return;
            }
            mouseX -= 100;
            mouseY -= 50;
        }




        if (Client.controlIsDown) {
            drawStatMenu();
            return;
        }


        if (lastDraw != hintId) {
            lastDraw = hintId;
            itemDisplay = new int[4];
        }

        HoverMenu menu = menus.get(hintId);
        if (menu != null) {
            String[] text = split(menu.text).split("<br>");

            int height = (text.length * 12) + (menu.items != null ? 40 : 0);

            int width = (16 + text[0].length() * 5) + (menu.items != null ? 30 : 0);

            Rasterizer2D.drawBoxOutline(mouseX, mouseY + 5, width + 4, 26 + height, 0x696969);
            Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, width + 2, 24 + height, 0x000000, 150);

            Client.instance.newSmallFont.drawBasicString("<col=ff9040>" + hintName, (int) (mouseX + 4), mouseY + 19,
                    BACKGROUND_COLOUR, 1);
            int y = 0;

            for (String string : text) {
                Client.instance.newSmallFont.drawBasicString(string, mouseX + 4, mouseY + 35 + y, BACKGROUND_COLOUR, 1);
                y += 12;
            }

            if (menu.items != null) {
                int spriteX = 10;

                if (System.currentTimeMillis() - displayDelay > 300) {
                    displayDelay = System.currentTimeMillis();
                    displayIndex++;
                    if (displayIndex == menu.items.size()) {
                        displayIndex = 0;
                    }

                    if (menu.items.size() <= 4) {
                        for (int i = 0; i < menu.items.size(); i++) {
                            itemDisplay[i] = menu.items.get(i);
                        }
                    } else {
                        if (displayIndex >= menu.items.size() - 1) {
                            displayIndex = menu.items.size() - 1;
                        }
                        int next = menu.items.get(displayIndex);
                        for (int i = 0; i < itemDisplay.length - 1; i++) {
                            itemDisplay[i] = itemDisplay[i + 1];
                        }
                        itemDisplay[3] = next;
                    }
                }

                for (int id : itemDisplay) {
                    if (id < 1) {
                        continue;
                    }
                    Sprite item = ItemDefinition.getSprite(id, 1, 0);
                    if (item != null) {
                        item.drawSprite(mouseX + spriteX, mouseY + 35 + y);
                        spriteX += 40;
                    }
                }
            }
            return;
        }

        Rasterizer2D.drawBoxOutline(mouseX, mouseY + 5, 150, 36, 0x696969);
        Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, 150, 35, 0x000000, 90);

        Client.instance.newSmallFont.drawBasicString("@lre@" + hintName, mouseX + 4, mouseY + 18, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("Press CTRL to view the stats", mouseX + 4, mouseY + 35,
                BACKGROUND_COLOUR, 1);
    }


    private static String split(String text) {
        StringBuilder string = new StringBuilder();

        int size = 0;

        for (String s : text.split(" ")) {
            string.append(s).append(" ");
            size += s.length();
            if (size > 20) {
                string.append("<br>");
                size = 0;
            }
        }
        return string.toString();
    }

    public static void drawHoverBox(RSFont font, int xPos, int yPos, String text, int colour, int backgroundColour) {
        String[] results = text.split("\n");
        int height = (results.length * 16) + 6;
        int width = font.getTextWidth(results[0]) + 6;
        for (int i = 1; i < results.length; i++)
            if (width <= font.getTextWidth(results[i]) + 6)
                width = font.getTextWidth(results[i]) + 6;
        Rasterizer2D.drawBox(xPos, yPos, width, height, backgroundColour);
        Rasterizer2D.drawBoxOutline(xPos, yPos, width, height, 0);
        yPos += 14;
        for (String result : results) {
            font.drawBasicString(result, xPos + 3, yPos, colour, 0);
            yPos += 16;
        }
    }






    public static void drawStatMenu() {
        if (!canDraw()) {
            return;
        }

        if(ItemBonusDefinitionLoader.getItemBonusDefinition(hintId) == null) {
            HoverMenuManager.reset();
            return;
        }

        int mouseX = Client.instance.getMouseX();
        int mouseY = Client.instance.getMouseY();
        if (!Client.instance.isResized()) {
            //if (Client.isFixedScreen()) {
            mouseX -= 100;
            mouseY -= 50;
        }
        if (Client.instance.isResized()) {
            //if (Client.isFixedScreen()) {
            if (Client.instance.getMouseY() < 214 || Client.instance.getMouseX() < 561) {
                return;
            }
            mouseX -= 516;
            mouseY -= 158;
            if (Client.instance.getMouseX() > 600 && Client.instance.getMouseX() < 685) {
                mouseX -= 60;

            }
            if (Client.instance.getMouseX() > 685) {
                mouseX -= 120;
            }
            if (Client.instance.getMouseY() > 392) {
                mouseY -= 130;
            }
        }

        short stabAtk = ItemBonusDefinitionLoader.getItemBonuses(hintId)[0];
        int slashAtk = ItemBonusDefinitionLoader.getItemBonuses(hintId)[1];
        int crushAtk = ItemBonusDefinitionLoader.getItemBonuses(hintId)[2];
        int magicAtk = ItemBonusDefinitionLoader.getItemBonuses(hintId)[3];
        int rangedAtk = ItemBonusDefinitionLoader.getItemBonuses(hintId)[4];

        int stabDef = ItemBonusDefinitionLoader.getItemBonuses(hintId)[5];
        int slashDef = ItemBonusDefinitionLoader.getItemBonuses(hintId)[6];
        int crushDef = ItemBonusDefinitionLoader.getItemBonuses(hintId)[7];
        int magicDef = ItemBonusDefinitionLoader.getItemBonuses(hintId)[8];
        int rangedDef = ItemBonusDefinitionLoader.getItemBonuses(hintId)[9];

        int prayerBonus = ItemBonusDefinitionLoader.getItemBonuses(hintId)[11];
        int strengthBonus = ItemBonusDefinitionLoader.getItemBonuses(hintId)[10];

        Rasterizer2D.drawBoxOutline(mouseX, mouseY + 5, 150, 120, 0x696969);
        Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, 150, 121, 0x000000, 90);

        Client.instance.newSmallFont.drawBasicString("@lre@" + hintName, mouseX + 4, mouseY + 18, BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString("ATK:", mouseX + 62, mouseY + 30, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("DEF:", mouseX + 112, mouseY + 30, BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString("Stab", mouseX + 2, mouseY + 43, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(stabAtk), mouseX + 62, mouseY + 43,
                BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(stabDef), mouseX + 112, mouseY + 43,
                BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString("Slash", mouseX + 2, mouseY + 56, 0xFF00FF, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(slashAtk), mouseX + 62, mouseY + 56,
                BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(slashDef), mouseX + 112, mouseY + 56,
                BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString("Crush", mouseX + 2, mouseY + 69, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(crushAtk), mouseX + 62, mouseY + 69,
                BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(crushDef), mouseX + 112, mouseY + 69,
                BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString("Magic", mouseX + 2, mouseY + 80, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(magicAtk), mouseX + 62, mouseY + 80,
                BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(magicDef), mouseX + 112, mouseY + 80,
                BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString("Ranged", mouseX + 2, mouseY + 95, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(rangedAtk), mouseX + 62, mouseY + 95,
                BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(rangedDef), mouseX + 112, mouseY + 95,
                BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString("Strength", mouseX + 2, mouseY + 108, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("Prayer", mouseX + 2, mouseY + 121, BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString(Integer.toString(strengthBonus), mouseX + 112, mouseY + 108,
                BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString(Integer.toString(prayerBonus), mouseX + 112, mouseY + 121,
                BACKGROUND_COLOUR, 1);

        Client.instance.newSmallFont.drawBasicString("Stab", mouseX + 2, mouseY + 43, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("Slash", mouseX + 2, mouseY + 56, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("Crush", mouseX + 2, mouseY + 69, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("Magic", mouseX + 2, mouseY + 80, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("Ranged", mouseX + 2, mouseY + 95, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("Strength", mouseX + 2, mouseY + 108, BACKGROUND_COLOUR, 1);
        Client.instance.newSmallFont.drawBasicString("Prayer", mouseX + 2, mouseY + 121, BACKGROUND_COLOUR, 1);
    }






}
