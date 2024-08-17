package com.client.draw;

import java.util.Arrays;
import java.util.HashMap;

import com.client.*;
import com.client.definitions.ItemDefinition;
import com.client.engine.impl.MouseHandler;


public class HoverManager {

    public static final int BACKGROUND_COLOUR = 0xFFFFFFF;

    public static HashMap<Integer, HoverMenu> menus = new HashMap<Integer, HoverMenu>();

    public static void init() {
        // PETS:
        menus.put(20670, new HoverMenu("Auto-loots every non-spawnable items in non wilderness."));
        menus.put(23603, new HoverMenu("Auto banks every item in non wilderness."));
        menus.put(22001, new HoverMenu("Protects one extra item upon death when being skulled."));
        menus.put(23500, new HoverMenu("Heals 1/5 of all damage dealt above 25."));

        //WEAPONS:
        menus.put(12006, new HoverMenu("A whip which has 1/15 chance to poison your enemy."));
        menus.put(24015, new HoverMenu("A stronger variant of the whip which has 1/7 chance to poison your enemy."));
        menus.put(12773, new HoverMenu("A whip which has 1/15 chance to burn your enemy."));
        menus.put(12774, new HoverMenu("A whip which has 1/15 chance to freeze your enemy."));
        menus.put(20368, new HoverMenu("Stronger and requires less special attack energy."));
        menus.put(20370, new HoverMenu("Stronger and requires less special attack energy."));
        menus.put(20372, new HoverMenu("Stronger and requires less special attack energy."));
        menus.put(20374, new HoverMenu("Stronger and requires less special attack energy."));
        menus.put(13652, new HoverMenu("Stronger and requires less special attack energy."));

        // EQUIPMENT:
        menus.put(23582, new HoverMenu("The best in-game pickaxe and axe."));
        menus.put(11864, new HoverMenu("Deal 10% more damage against current slayer task."));
        menus.put(11865, new HoverMenu("Deal 15% more damage against current slayer task."));
        menus.put(19639, new HoverMenu("Deal 20% more damage against current slayer task."));
        menus.put(19643, new HoverMenu("Deal 20% more damage against current slayer task."));
        menus.put(19647, new HoverMenu("Deal 20% more damage against current slayer task."));
        menus.put(21264, new HoverMenu("Deal 20% more damage against current slayer task."));
        menus.put(21888, new HoverMenu("Deal 20% more damage against current slayer task."));
        menus.put(23597, new HoverMenu("A rare reward for all beta players, I should save this for later."));

        // EXPERIENCE BOOSTED ITEMS:
        menus.put(7409, new HoverMenu("Provides 25% more herblore experience while worn."));
        menus.put(7451, new HoverMenu("Provides 25% more cooking experience while worn."));
        menus.put(11850, new HoverMenu("Provides 25% more agility experience while wearing full set."));
        menus.put(11852, new HoverMenu("Provides 25% more agility experience while wearing full set."));
        menus.put(11854, new HoverMenu("Provides 25% more agility experience while wearing full set."));
        menus.put(11856, new HoverMenu("Provides 25% more agility experience while wearing full set."));
        menus.put(11858, new HoverMenu("Provides 25% more agility experience while wearing full set."));
        menus.put(11860, new HoverMenu("Provides 25% more agility experience while wearing full set."));
        menus.put(10941, new HoverMenu("Provides 25% more woodcutting experience while wearing full set."));
        menus.put(10939, new HoverMenu("Provides 25% more woodcutting experience while wearing full set."));
        menus.put(10940, new HoverMenu("Provides 25% more woodcutting experience while wearing full set."));
        menus.put(10933, new HoverMenu("Provides 25% more woodcutting experience while wearing full set."));
        menus.put(12013, new HoverMenu("Provides 25% more mining experience while wearing full set."));
        menus.put(12014, new HoverMenu("Provides 25% more mining experience while wearing full set."));
        menus.put(12015, new HoverMenu("Provides 25% more mining experience while wearing full set."));
        menus.put(12016, new HoverMenu("Provides 25% more mining experience while wearing full set."));
        menus.put(13258, new HoverMenu("Provides 25% more fishing experience while wearing full set."));
        menus.put(13259, new HoverMenu("Provides 25% more fishing experience while wearing full set."));
        menus.put(13260, new HoverMenu("Provides 25% more fishing experience while wearing full set."));
        menus.put(13261, new HoverMenu("Provides 25% more fishing experience while wearing full set."));
        menus.put(13646, new HoverMenu("Provides 25% more farming experience while wearing full set."));
        menus.put(13642, new HoverMenu("Provides 25% more farming experience while wearing full set."));
        menus.put(13643, new HoverMenu("Provides 25% more farming experience while wearing full set."));
        menus.put(13640, new HoverMenu("Provides 25% more farming experience while wearing full set."));
        menus.put(13644, new HoverMenu("Provides 25% more farming experience while wearing full set."));
        menus.put(20708, new HoverMenu("Provides 25% more firemaking experience while wearing full set."));
        menus.put(20710, new HoverMenu("Provides 25% more firemaking experience while wearing full set."));
        menus.put(20712, new HoverMenu("Provides 25% more firemaking experience while wearing full set."));
        menus.put(20704, new HoverMenu("Provides 25% more firemaking experience while wearing full set."));
        menus.put(20706, new HoverMenu("Provides 25% more firemaking experience while wearing full set."));
        menus.put(5554, new HoverMenu("Provides 25% more thieving experience while wearing full set."));
        menus.put(5553, new HoverMenu("Provides 25% more thieving experience while wearing full set."));
        menus.put(5555, new HoverMenu("Provides 25% more thieving experience while wearing full set."));
        menus.put(5556, new HoverMenu("Provides 25% more thieving experience while wearing full set."));
        menus.put(5557, new HoverMenu("Provides 25% more thieving experience while wearing full set."));
        menus.put(6799, new HoverMenu("Does not stack and vanishes when you log out."));
        menus.put(6800, new HoverMenu("Does not stack and vanishes when you log out."));
        menus.put(6801, new HoverMenu("Does not stack and vanishes when you log out."));
        menus.put(6803, new HoverMenu("Does not stack and vanishes when you log out."));
        menus.put(23206, new HoverMenu("Hits guaranteed double."));

        // FOOD:
        menus.put(10541, new HoverMenu("Heals @lre@23@whi@ and makes you immume to poison for 5 minutes."));
        menus.put(385, new HoverMenu("Heals @lre@22"));
        menus.put(1971, new HoverMenu("Heals @lre@4"));
        menus.put(1985, new HoverMenu("Heals @lre@4"));
        menus.put(1891, new HoverMenu("Heals @lre@5"));
        menus.put(1893, new HoverMenu("Heals @lre@5"));
        menus.put(1895, new HoverMenu("Heals @lre@5"));
        menus.put(14640, new HoverMenu("Heals @lre@12"));
        menus.put(247, new HoverMenu("Heals @lre@2"));
        menus.put(2205, new HoverMenu("Heals @lre@7"));
        menus.put(403, new HoverMenu("Heals @lre@4"));
        menus.put(319, new HoverMenu("Heals @lre@1"));
        menus.put(315, new HoverMenu("Heals @lre@3"));
        menus.put(325, new HoverMenu("Heals @lre@4"));
        menus.put(339, new HoverMenu("Heals @lre@7"));
        menus.put(333, new HoverMenu("Heals @lre@7"));
        menus.put(351, new HoverMenu("Heals @lre@8"));
        menus.put(329, new HoverMenu("Heals @lre@9"));
        menus.put(361, new HoverMenu("Heals @lre@10"));
        menus.put(379, new HoverMenu("Heals @lre@12"));
        menus.put(365, new HoverMenu("Heals @lre@13"));
        menus.put(373, new HoverMenu("Heals @lre@14"));
        menus.put(2293, new HoverMenu("Heals @lre@14"));
        menus.put(7946, new HoverMenu("Heals @lre@16"));
        menus.put(385, new HoverMenu("Heals @lre@20"));
        menus.put(397, new HoverMenu("Heals @lre@21"));
        menus.put(391, new HoverMenu("Heals @lre@22"));
        menus.put(3144, new HoverMenu("Heals @lre@18@whi@ and can be consumed nearly instantly with no delay."));
        menus.put(13441, new HoverMenu("Heals @lre@22"));

        // MYSTERY BOXES:
        menus.put(6199, new HoverMenu("Contains various random rewards:",
                Arrays.asList(4151, 11235, 2577, 2581, 6585, 12849, 11838, 4716, 4718, 4722, 4720, 4753, 4755, 4757, 4759, 7462)));
        menus.put(989, new HoverMenu("Use it on the crystal chest to get a reward:",
                Arrays.asList(23604, 4087, 11840, 6735, 6731, 6733, 6737, 11838, 12796, 12000)));


        menus.put(19941, new HoverMenu("A wearable mystery casket which contains guaranteed one of these items:",
                Arrays.asList(22324, 20997, 21003, 21006, 23528, 23854, 23856, 22325, 21295, 23594, 23595, 23596, 23206, 2699)));


        menus.put(13346, new HoverMenu("Contains various rare items:",
                Arrays.asList(12924, 22647, 22653, 22656, 22650, 22625, 22628, 22631,22622, 20784, 20997, 21003, 22324, 23594, 23595, 23596)));
        menus.put(13347, new HoverMenu("Contains various rare pets:",
                Arrays.asList(12703, 12816, 12655, 13247, 12648, 22000, 22473, 22001, 23603, 23602, 23601, 23600, 22001, 23500)));

        System.out.println("Somnium has loaded " + menus.size() + "x menu hovers.");
    }

    public static int drawType() {
        if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 500 && MouseHandler.mouseY > 0
                && MouseHandler.mouseY < 300) {
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
        if (Client.instance.menuOptionsCount < 2 && Client.instance.itemSelected == 0
                && Client.instance.spellSelected == 0) {
            return false;
        }
        if (Client.instance.menuTargets[Client.instance.menuOptionsCount] != null) {
            if (Client.instance.menuTargets[Client.instance.menuOptionsCount].contains("Walk")) {
                return false;
            }
        }
        if (Client.instance.toolTip.contains("Walk") || Client.instance.toolTip.contains("World")) {
            return false;
        }
        if (Client.instance.isMenuOpen) {
            return false;
        }
        if (hintId == -1) {
            return false;
        }
        if (!showMenu) {
            return false;
        }
        return true;
    }

	public static void drawHintMenu() {
		int mouseX = MouseHandler.mouseX;
		int mouseY = MouseHandler.mouseY;

		if (!canDraw()) {
			return;
		}

		if (Client.instance.isResized()) {
			if (MouseHandler.mouseY < Client.canvasHeight - 450
				&& MouseHandler.mouseX < Client.canvasWidth - 200) {
				return;
			}
			mouseX -= 100;
			mouseY -= 50;
		}
		if (!Client.instance.isResized()) {
			if (MouseHandler.mouseY < 210 || MouseHandler.mouseX < 561) {
			} else {
				mouseX -= 516;
				mouseY -= 158;
			}
			if (MouseHandler.mouseX > 600 && MouseHandler.mouseX < 685) {
				mouseX -= 60;

			}
			if (MouseHandler.mouseX > 685) {
				mouseX -= 80;
			}
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
			String text[] = split(menu.text, 20).split("\n");

			int height = (text.length * 12) + (menu.items != null ? 40 : 0);

			int width = (16 + text[0].length() * 5) + (menu.items != null ? 30 : 0);

			Rasterizer2D.drawBoxOutline(mouseX, mouseY + 5, width + 4, 26 + height, 0x696969);
			Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, width + 2, 24 + height, 0x000000, 150);

			Client.instance.newSmallFont.drawBasicString("@lre@" + hintName, (int) (mouseX + 4), mouseY + 19,
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

		/*DrawingArea.drawBoxOutline(mouseX, mouseY + 5, 150, 36, 0x696969);
		DrawingArea.drawTransparentBox(mouseX + 1, mouseY + 6, 150, 35, 0x000000, 90);

		Client.instance.newSmallFont.drawBasicString("@lre@" + hintName, mouseX + 4, mouseY + 18, BACKGROUND_COLOUR, 1);
		Client.instance.newSmallFont.drawBasicString("Press CTRL to view the stats", mouseX + 4, mouseY + 35,
				BACKGROUND_COLOUR, 1);*/
	}


	public static void drawStatMenu() {
        if (!canDraw()) {
            return;
        }

        if(ItemBonusDefinition.getItemBonusDefinition(hintId) == null) {
            HoverManager.reset();
            return;
        }

        int mouseX = MouseHandler.mouseX;
        int mouseY = MouseHandler.mouseY;
        if (Client.instance.isResized()) {
            mouseX -= 100;
            mouseY -= 50;
        }
        if (!Client.instance.isResized()) {
            if (MouseHandler.mouseY < 214 || MouseHandler.mouseX < 561) {
                return;
            }
            mouseX -= 516;
            mouseY -= 158;
            if (MouseHandler.mouseX > 600 && MouseHandler.mouseX < 685) {
                mouseX -= 60;

            }
            if (MouseHandler.mouseX > 685) {
                mouseX -= 120;
            }
            if (MouseHandler.mouseY > 392) {
                mouseY -= 130;
            }
        }

        short stabAtk = ItemBonusDefinition.getItemBonuses(hintId)[0];
        int slashAtk = ItemBonusDefinition.getItemBonuses(hintId)[1];
        int crushAtk = ItemBonusDefinition.getItemBonuses(hintId)[2];
        int magicAtk = ItemBonusDefinition.getItemBonuses(hintId)[3];
        int rangedAtk = ItemBonusDefinition.getItemBonuses(hintId)[4];

        int stabDef = ItemBonusDefinition.getItemBonuses(hintId)[5];
        int slashDef = ItemBonusDefinition.getItemBonuses(hintId)[6];
        int crushDef = ItemBonusDefinition.getItemBonuses(hintId)[7];
        int magicDef = ItemBonusDefinition.getItemBonuses(hintId)[8];
        int rangedDef = ItemBonusDefinition.getItemBonuses(hintId)[9];

        int prayerBonus = ItemBonusDefinition.getItemBonuses(hintId)[11];
        int strengthBonus = ItemBonusDefinition.getItemBonuses(hintId)[10];

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

    private static String split(String text, int length) {
        String string = "";

        int size = 0;

        for (String s : text.split(" ")) {
            string += s + " ";
            size += s.length();
            if (size > length) {
                string += "\n";
                size = 0;
            }
        }
        return string;
    }

    public static void drawHoverBox(RSFont font, int xPos, int yPos, String text, int colour, int backgroundColour) {
        String[] results = text.split("\n");
        int height = (results.length * 16) + 6;
        int width;
        width = font.getTextWidth(results[0]) + 6;
        for (int i = 1; i < results.length; i++)
            if (width <= font.getTextWidth(results[i]) + 6)
                width = font.getTextWidth(results[i]) + 6;
        Rasterizer2D.drawBox(xPos, yPos, width, height, backgroundColour);
        Rasterizer2D.drawBoxOutline(xPos, yPos, width, height, 0);
        yPos += 14;
        for (int i = 0; i < results.length; i++) {
            font.drawBasicString(results[i], xPos + 3, yPos, colour, 0);
            yPos += 16;
        }
    }

}
