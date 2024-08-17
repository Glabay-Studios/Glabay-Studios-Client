package com.client.definitions;

import com.client.*;
import com.client.collection.EvictingDualNodeHashTable;
import com.client.collection.node.DualNode;
import com.client.collection.table.IterableNodeHashTable;
import com.client.draw.Rasterizer3D;
import com.client.entity.model.Mesh;
import com.client.entity.model.Model;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import com.client.utilities.FileOperations;
import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSItemComposition;
import net.runelite.rs.api.RSIterableNodeHashTable;

import static net.runelite.api.Constants.CLIENT_DEFAULT_ZOOM;

public final class ItemDefinition extends DualNode implements RSItemComposition {

	public static EvictingDualNodeHashTable sprites = new EvictingDualNodeHashTable(100);
	public static EvictingDualNodeHashTable models = new EvictingDualNodeHashTable(50);
	public static boolean isMembers = true;

	public int price;
	public int[] recolorFrom;
	public int id;
	public int[] recolorTo;
	public boolean members;
	public int notedTemplate;
	public int femaleModel1;
	public int maleModel;
	public String[] groundActions;
	public int xOffset2d;
	public String name;
	public String description;
	public int inventoryModel;
	public int maleHeadModel;
	public boolean stackable;
	public int notedID;
	public int zoom2d;
	public int maleModel1;
	public String[] interfaceOptions;
	public int xan2d;
	public int[] countObj;
	public int yOffset2d;//
	public int femaleHeadModel;
	public int yan2d;
	public int femaleModel;
	public int[] countCo;
	public int team;
	public int zan2d;
	public String[] equipActions;
	public boolean isTradable;
	IterableNodeHashTable params;
	public int glowColor = -1;
	private short[] retextureTo;
	private short[] retextureFrom;
	private int femaleOffset;
	private int femaleModel2;
	private int maleHeadModel2;
	private int resizeX;
	private int femaleHeadModel2;
	public int contrast;
	private int maleModel2;
	private int resizeZ;
	private int resizeY;
	public int ambient;
	private int maleOffset;
	private int shiftClickIndex = -2;
	private int category;
	private int unnotedId;

	public int placeholder;

	public int placeholderTemplate;

	private ItemDefinition() {
		id = -1;
	}

	public void createCustomSprite(String img) {
		//customSpriteLocation = getCustomSprite(img);
	}

	public void createSmallCustomSprite(String img) {
		//customSmallSpriteLocation = getCustomSprite(img);
	}

	public byte[] customSpriteLocation;
	public byte[] customSmallSpriteLocation;


	public static void clear() {
		models.clear();
		sprites.clear();
	}


	public int weight;
	public int wearPos1;
	public int wearPos2;
	public int wearPos3;

	public static ItemDefinition copy(ItemDefinition itemDef, int newId, int copyingItemId, String newName, String...actions) {
		ItemDefinition copyItemDef = lookup(copyingItemId);
		itemDef.id = newId;
		itemDef.name = newName;
		itemDef.recolorTo = copyItemDef.recolorTo;
		itemDef.recolorFrom = copyItemDef.recolorFrom;
		itemDef.inventoryModel = copyItemDef.inventoryModel;
		itemDef.maleModel = copyItemDef.maleModel;
		itemDef.femaleModel = copyItemDef.femaleModel;
		itemDef.zoom2d = copyItemDef.zoom2d;
		itemDef.xan2d = copyItemDef.xan2d;
		itemDef.yan2d = copyItemDef.yan2d;
		itemDef.xOffset2d = copyItemDef.xOffset2d;
		itemDef.yOffset2d = copyItemDef.yOffset2d;
		itemDef.interfaceOptions = copyItemDef.interfaceOptions;
		itemDef.interfaceOptions = new String[5];
		if (actions != null) {
			for (int index = 0; index < actions.length; index++) {
				itemDef.interfaceOptions[index] = actions[index];
			}
		}
		return itemDef;
	}


	private static void customItems(int itemId, ItemDefinition itemDef) {

		switch (itemId) {
			case 21726:
			case 21728:
				itemDef.stackable = true;
				break;
			case 12863:
				itemDef.interfaceOptions = new String[] { "Open", null, null, null, null};
				break;
			case 13092: //this makes crystal halberds wieldable, weird af.
			case 13093:
			case 13094:
			case 13095:
			case 13096:
			case 13097:
			case 13098:
			case 13099:
			case 13100:
			case 13101:
				itemDef.interfaceOptions = new String[] { null, "Wield", null, null, null};
				break;
			case 23933:
				itemDef.name = "Vote crystal";
				break;
			case 9698:
				itemDef.name = "Unfired burning rune";
				//itemDef.description= "I should burn this.";
				itemDef.createCustomSprite("Unfired_burning_rune.png");
				break;
			case 9699:
				itemDef.name = "Burning rune";
				//itemDef.description= "Hot to the touch.";
				itemDef.createCustomSprite("Burning_rune.png");
				break;
			case 23778:
				itemDef.name = "Uncut toxic gem";
				//itemDef.description= "I should use a chisel on this.";
				break;
			case 22374:
				itemDef.name = "Hespori key";
				//itemDef.description= "Can be used on the Hespori chest.";
				break;
			case 23783:
				itemDef.name = "Toxic gem";
				//itemDef.description= "I should be careful with this.";
				break;
			case 9017:
				itemDef.name = "Hespori essence";
				//itemDef.description= "Maybe I should burn this.";
				itemDef.interfaceOptions = new String[] {  null, null, null, null, "Drop" };
				break;
			case 19473:
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				break;
			case 10556:
			case 10557:
			case 10558:
			case 10559:
				itemDef.interfaceOptions = new String[] { null, "Wear", "Feature", null, "Drop" };
				break;
			case 21898:
				itemDef.interfaceOptions = new String[] { null, "Wear", "Teleports", "Features", null };
				break;
			case 12873:
			case 12875:
			case 12877:
			case 12879:
			case 12881:
			case 12883:
				itemDef.interfaceOptions = new String[] { "Open", null, null, null, "Drop" };
				break;
			case 23804:
				itemDef.name = "Imbue Dust";
				break;
			case 22517:
				itemDef.name = "Crystal Shard";
				break;
			case 23951:
				itemDef.name = "Crystalline Key";
				break;
			case 691:
				itemDef.name = "@gre@10,000 FoE Point Certificate";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				break;
			case 692:
				itemDef.name = "@red@25,000 FoE Point Certificate";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				break;
			case 693:
				itemDef.name = "@cya@50,000 FoE Point Certificate";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				break;
			case 696:
				itemDef.name = "@yel@250,000 FoE Point Certificate";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				break;
			case 23877:
				itemDef.name = "Crystal Shard";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = true;
				break;
			case 23943:
				itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
				break;
			case 2996:
				itemDef.name = "@red@PKP Ticket";
				break;
			case 23776:
				itemDef.name = "@red@Hunllef's Key";
				break;
			case 13148:
				itemDef.name = "@red@Reset Lamp";
				break;
			case 6792:
				itemDef.name = "@red@Seren's Key";
				break;
			case 4185:
				itemDef.name = "@red@Porazdir's Key";
				break;
			case 21880:
				itemDef.name = "Wrath Rune";
				itemDef.price = 1930;
				break;
			case 12885:
			case 13277:
			case 19701:
			case 13245:
			case 12007:
			case 22106:
			case 12936:
			case 24495:
				itemDef.interfaceOptions = new String[] { null, null, "Open", null, "Drop" };
				break;
			case 21262:
				itemDef.name = "Vote Genie Pet";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Release" };
				break;
			case 21817:
				itemDef.interfaceOptions = new String[] { null, "Wear", "Dismantle", null, null, };
				break;
			case 21347:
				itemDef.interfaceOptions = new String[] { null, null, null, "Chisel-Options", null, };
				break;
			case 21259:
				itemDef.name = "@red@Name Change Scroll";
				itemDef.interfaceOptions = new String[] { null, null, "Read", null, null, };
				break;
			case 22547:
			case 22552:
			case 22542:
				itemDef.interfaceOptions = new String[] { null, null, null, null, null, };
				break;
			case 22555:
			case 22550:
			case 22545:
				itemDef.interfaceOptions = new String[] { null, "Wield", "Check", "Uncharge", null, };
				break;
			case 732:
				itemDef.name = "@blu@Imbuedeifer";
				itemDef.price = 1930;
				break;
			case 21881:
				itemDef.name = "Wrath Rune";
				itemDef.price = 1930;
				break;
			case 13226:
				itemDef.name = "Herb Sack";
				//itemDef.description= "Thats a nice looking sack.";
				break;
			case 3456:
				itemDef.name = "@whi@Common Raids Key";
				//itemDef.description= "Can be used on the storage unit.";
				break;
			case 3464:
				itemDef.name = "@pur@Rare Raids Key";
				//itemDef.description= "Can be used on the storage unit.";
				break;
			case 6829:
				itemDef.name = "@red@YT Video Giveaway Box";
				//itemDef.description= "Spawns items to giveaway for your youtube video.";
				itemDef.interfaceOptions = new String[] { "Giveaway", null, null, null, "Drop" };
				break;
			case 6831:
				itemDef.name = "@red@YT Video Giveaway Box (t2)";
				//itemDef.description= "Spawns items to giveaway for your youtube video.";
				itemDef.interfaceOptions = new String[] { "Giveaway", null, null, null, "Drop" };

				break;
			case 6832:
				itemDef.name = "@red@YT Stream Giveaway Box";
				//itemDef.description= "Spawns items to giveaway for your youtube stream.";
				itemDef.interfaceOptions = new String[] { "Giveaway", null, null, null, "Drop" };

				break;
			case 6833:
				itemDef.name = "@red@YT Stream Giveaway Box (t2)";
				//itemDef.description= "Spawns items to giveaway for your youtube stream.";
				itemDef.interfaceOptions = new String[] { "Giveaway", null, null, null, "Drop" };

				break;
			case 13190:
				itemDef.name = "@yel@100m OSRS GP";
				itemDef.interfaceOptions = new String[] { "Redeem", null, null, null, "Drop" };
				//itemDef.description= "Redeem for 100m OSRS GP!";
				break;
			case 6121:
				itemDef.name = "Break Vials Instruction";
				//itemDef.description= "How does one break a vial, its impossible?";
				break;
			case 2528:
				itemDef.name = "@red@Experience Lamp";
				//itemDef.description= "Should I rub it......";
				break;
			case 5509:
				itemDef.name = "Small Pouch";
				itemDef.createCustomSprite("Small_pouch.png");
				itemDef.interfaceOptions = new String[] { "Fill", "Empty", "Check", null, null };
				break;
			case 5510:
				itemDef.name = "Medium Pouch";
				itemDef.createCustomSprite("Medium_pouch.png");
				itemDef.interfaceOptions = new String[] { "Fill", "Empty", "Check", null, null };
				break;
			case 5512:
				itemDef.name = "Large Pouch";
				itemDef.createCustomSprite("Large_pouch.png");
				itemDef.interfaceOptions = new String[] { "Fill", "Empty", "Check", null, null };
				break;
			case 10724: //full skeleton
			case 10725:
			case 10726:
			case 10727:
			case 10728:
				itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
				break;
			case 5514:
				itemDef.name = "Giant Pouch";
				itemDef.createCustomSprite("Giant_pouch.png");
				break;
			case 22610: //vesta spear
				itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
				break;
			case 22613: //vesta longsword
				itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
				break;
			case 22504: //stat warhammer
				itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
				break;
			case 4224:
			case 4225:
			case 4226:
			case 4227:
			case 4228:
			case 4229:
			case 4230:
			case 4231:
			case 4232:
			case 4233:
			case 4234:
			case 4235://crystal sheild
				itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
				break;
			case 4212:
			case 4214:
			case 4215:
			case 4216:
			case 4217:
			case 4218:
			case 4219:
			case 4220:
			case 4221:
			case 4222:
			case 4223:
				itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
				break;
			case 2841:
				itemDef.name = "@red@Bonus Exp Scroll";
				itemDef.interfaceOptions = new String[] { "@yel@Activate", null, null, null, "Drop" };
				//itemDef.description= "You will get double experience using this scroll.";
				break;
			case 21791:
			case 21793:
			case 21795:
				itemDef.interfaceOptions = new String[] { null, "Wear", null, null, "Drop" };
				break;
			case 19841:
				itemDef.name = "Master Casket";
				break;
			case 21034:
				itemDef.interfaceOptions = new String[] { "Read", null, null, null, "Drop" };
				break;
			case 6830:
				itemDef.name = "@yel@BETA @blu@BOX";
				itemDef.interfaceOptions = new String[] { "Open", null, null, null, "Drop" };
				break;
			case 21079:
				itemDef.interfaceOptions = new String[] { "Read", null, null, null, "Drop" };
				break;
			case 22093:
				itemDef.name = "@gre@Vote Streak Key";
				//itemDef.description= "Thanks for voting!";
				break;
			case 22885:
				itemDef.name = "@gre@Kronos seed";
				//itemDef.description= "Provides whole server with bonus xp for 1 skill for 5 hours!";
				break;
			case 23824:
				itemDef.name = "Slaughter charge";
				//itemDef.description= "Can be used on bracelet of slaughter to charge it.";
				break;
			case 22883:
				itemDef.name = "@gre@Iasor seed";
				//itemDef.description= "Increased drop rate (+10%) for whole server for 5 hours!";
				break;
			case 22881:
				itemDef.name = "@gre@Attas seed";
				//itemDef.description= "Provides the whole server with bonus xp for 5 hours!";
				break;
			case 20906:
				itemDef.name = "@gre@Golpar seed";
				//itemDef.description= "Provides whole server with double c keys, resource boxes, coin bags, and clues!";
				break;
			case 6112:
				itemDef.name = "@gre@Kelda seed";
				//itemDef.description= "Provides whole server with x2 Larren's keys for 1 hour!";
				break;
			case 20903:
				itemDef.name = "@gre@Noxifer seed";
				//itemDef.description= "Provides whole server with x2 Slayer points for 1 hour!";
				break;
			case 20909:
				itemDef.name = "@gre@Buchu seed";
				//itemDef.description= "Provides whole server with x2 Boss points for 1 hour!";
				break;
			case 22869:
				itemDef.name = "@gre@Celastrus seed";
				//itemDef.description= "Provides whole server with x2 Brimstone keys for 1 hour!";
				break;
			case 4205:
				itemDef.name = "@gre@Consecration seed";
				//itemDef.description= "Provides the whole server with +5 PC points for 1 hour.";
				itemDef.stackable = true;
				break;
			case 11864:
			case 11865:
			case 19639:
			case 19641:
			case 19643:
			case 19645:
			case 19647:
			case 19649:
			case 24444:
			case 24370:
			case 23075:
			case 23073:
			case 21888:
			case 21890:
			case 21264:
			case 21266:
				itemDef.equipActions[2] = "Log";
				itemDef.equipActions[1] = "Check";
				break;
			case 13136:
				itemDef.equipActions[2] = "Elidinis";
				itemDef.equipActions[1] = "Kalphite Hive";
				break;
			case 2550:
				itemDef.equipActions[2] = "Check";
				break;

			case 1712:
			case 1710:
			case 1708:
			case 1706:
			case 19707:
				itemDef.equipActions[1] = "Edgeville";
				itemDef.equipActions[2] = "Karamja";
				itemDef.equipActions[3] = "Draynor";
				itemDef.equipActions[4] = "Al-Kharid";
				break;
			case 21816:
				itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", null, "Drop" };
				itemDef.equipActions[1] = "Check";
				itemDef.equipActions[2] = "Toggle-absorption";
				break;
			case 2552:
			case 2554:
			case 2556:
			case 2558:
			case 2560:
			case 2562:
			case 2564:
			case 2566: // Ring of duelling
				itemDef.equipActions[2] = "Shantay Pass";
				itemDef.equipActions[1] = "Clan wars";
				break;
			case 11739:
				itemDef.name = "@gre@Vote Mystery Box";
				//itemDef.description= "Probably contains cosmetics, or maybe not...";
				itemDef.interfaceOptions = new String[] { "Open", null, null, null, "Drop" };
				break;
			case 6828:
				itemDef.name = "Super Mystery Box";
				//itemDef.description= "Mystery box that contains goodies.";
				itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
				itemDef.createCustomSprite("Mystery_Box.png");
				itemDef.createSmallCustomSprite("Mystery_Box_Small.png");
				itemDef.stackable = false;
				break;
			case 30010:
				itemDef.setDefaults();
				itemDef.name = "Postie Pete";
				//itemDef.description= "50% chance to pick up crystal keys that drop.";
				itemDef.createCustomSprite("Postie_Pete.png");
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				break;
			case 30011:
				itemDef.setDefaults();
				itemDef.name = "Imp";
				//itemDef.description= "50% chance to pick up clue scrolls that drop.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Imp.png");
				break;
			case 30012:
				itemDef.setDefaults();
				itemDef.name = "Toucan";
				//itemDef.description= "50% chance to pick up resource packs.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Toucan.png");
				break;
			case 30013:
				itemDef.setDefaults();
				itemDef.name = "Penguin King";
				//itemDef.description= "50% chance to auto-pick up coin bags.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Penguin_King.png");
				break;
			case 30014:
				itemDef.setDefaults();
				itemDef.name = "K'klik";
				//itemDef.description= "An extra 5% in drop rate boost.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("K'klik.png");
				break;
			case 30015:
				itemDef.setDefaults();
				itemDef.name = "Shadow warrior";
				//itemDef.description= "50% chance for an additional +10% strength bonus in pvm.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Shadow_warrior.png");
				break;
			case 30016:
				itemDef.setDefaults();
				itemDef.name = "Shadow archer";
				//itemDef.description= "50% chance for an additional +10% range str bonus in PvM.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Shadow_archer.png");
				break;
			case 30017:
				itemDef.setDefaults();
				itemDef.name = "Shadow wizard";
				//itemDef.description= "50% chance for an additional +10% mage str bonus in PvM.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Shadow_wizard.png");
				break;
			case 30018:
				itemDef.setDefaults();
				itemDef.name = "Healer Death Spawn";
				//itemDef.description= "5% chance hit restores HP.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Healer_Death_Spawn.png");
				break;
			case 30019:
				itemDef.setDefaults();
				itemDef.name = "Holy Death Spawn";
				//itemDef.description= "5% chance 1/2 of your hit is restored into prayer.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Holy_Death_Spawn.png");
				break;
			case 30020:
				itemDef.setDefaults();
				itemDef.name = "Corrupt beast";
				//itemDef.description= "50% chance for an additional +10% strength bonus for melee, mage, and range in pvm.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Corrupt_beast.png");
				break;
			case 30021:
				itemDef.setDefaults();
				itemDef.name = "Roc";
				//itemDef.description= "An extra 10% in drop rate boost.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Roc.png");
				break;
			case 30022:
				itemDef.setDefaults();
				itemDef.name = "@red@Kratos";
				//itemDef.description= "The most powerful pet, see ::foepets for full list of perks.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Yama.png");
				break;
			case 30023:
				itemDef.setDefaults();
				itemDef.name = "Rain cloud";
				//itemDef.description= "Don't worry be happy.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("Rain_cloud.png");
				break;
			case 8866:
				itemDef.name = "Storage chest key (UIM)";
				//itemDef.description= "Used to open the UIM storage chest 1 time.";
				itemDef.stackable = true;
				break;
			case 8868:
				itemDef.name = "Perm. storage chest key (UIM)";
				//itemDef.description= "Permanently unlocks UIM storage chest.";
				break;
			case 771:
				itemDef.name = "@cya@Ancient branch";
				//itemDef.description= "Burning items in the FoE with this branch provides a 1 time +10% FoE value increase.";
				break;
			case 6199:
				itemDef.name = "Mystery Box";
				//itemDef.description= "Mystery box that contains goodies.";
				itemDef.interfaceOptions = new String[] { "Open", null, null, "Quick-Open", "Drop" };
				break;
			case 12789:
				itemDef.name = "@red@Youtube Mystery Box";
				//itemDef.description= "Mystery box that contains goodies.";
				itemDef.interfaceOptions = new String[] { "Open", null, null, null, "Drop" };
				break;
			case 13346:
				itemDef.name = "Ultra Mystery Box";
				itemDef.interfaceOptions = new String[] { "Open", null, null, "Quick-Open", "Drop" };
				break;
			case 8167:
				itemDef.name = "@or2@FoE Mystery Chest @red@(locked)";
				itemDef.interfaceOptions = new String[] { "Unlock", null, null, "Quick-Open", "Drop" };
				break;
			case 13438:
				itemDef.name = "Slayer Mystery Chest";
				itemDef.interfaceOptions = new String[] { "Open", null, null, null, "Drop" };
				break;
			case 2399:
				itemDef.name = "@or2@FoE Mystery Key";
				//itemDef.description= "Used to unlock the FoE Mystery Chest.";
				break;
			case 10832:
				itemDef.name = "Small coin bag";
				itemDef.interfaceOptions = new String[] { "Open", null, "Open-All", null, "Drop" };
				//itemDef.description= "I can see some coins inside.";
				break;
			case 10833:
				itemDef.name = "Medium coin bag";
				itemDef.interfaceOptions = new String[] { "Open", null, "Open-All", null, "Drop" };
				//itemDef.description= "I can see some coins inside.";
				break;
			case 10834:
				itemDef.name = "Large coin bag";
				itemDef.interfaceOptions = new String[] { "Open", null, "Open-All", null, "Drop" };
				//itemDef.description= "I can see some coins inside.";
				break;
			case 22316:
				itemDef.name = "Sword of Xeros";
				//itemDef.description= "The Sword of Xeros.";
				break;
			case 19942:
				itemDef.name = "Lil Mimic";
				//itemDef.description= "It's a lil mimic.";
				break;
			case 30110:
				itemDef.setDefaults();
				itemDef.name = "Dark postie pete";
				//itemDef.description= "Picks up all crystal keys and 25% chance to double.";
				itemDef.createCustomSprite("dark_Postie_Pete.png");
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				break;
			case 30111:
				itemDef.setDefaults();
				itemDef.name = "Dark imp";
				//itemDef.description= "Picks up all clue scrolls and 25% chance to double.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Imp.png");
				break;
			case 30112:
				itemDef.setDefaults();
				itemDef.name = "Dark toucan";
				//itemDef.description= "Picks up all resource boxes and 25% chance to double.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Toucan.png");
				break;
			case 30113:
				itemDef.setDefaults();
				itemDef.name = "Dark penguin King";
				//itemDef.description= "Picks up all coin bags and 25% chance to double.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Penguin_King.png");
				break;
			case 30114:
				itemDef.setDefaults();
				itemDef.name = "Dark k'klik";
				//itemDef.description= "An extra 10% in drop rate boost.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_K'klik.png");
				break;
			case 30115:
				itemDef.setDefaults();
				itemDef.name = "Dark shadow warrior";
				//itemDef.description= "Gives constant +10% strength bonus in pvm.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Shadow_warrior.png");
				break;
			case 30116:
				itemDef.setDefaults();
				itemDef.name = "Dark shadow archer";
				//itemDef.description= "Gives constant +10% range str bonus in PvM.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Shadow_archer.png");
				break;
			case 30117:
				itemDef.setDefaults();
				itemDef.name = "Dark shadow wizard";
				//itemDef.description= "Gives constant +10% mage str bonus in PvM.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Shadow_wizard.png");
				break;
			case 30118:
				itemDef.setDefaults();
				itemDef.name = "Dark healer death spawn";
				//itemDef.description= "10% chance hit restores HP.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Healer_Death_Spawn.png");
				break;
			case 30119:
				itemDef.setDefaults();
				itemDef.name = "Dark holy death spawn";
				//itemDef.description= "10% chance 1/2 of your hit is restored into prayer.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Holy_Death_Spawn.png");
				break;
			case 30120:
				itemDef.setDefaults();
				itemDef.name = "Dark corrupt beast";
				//itemDef.description= "Extra 10% in drop rate and constant +10% strength bonus for all styles in pvm.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Corrupt_beast.png");
				break;
			case 30121:
				itemDef.setDefaults();
				itemDef.name = "Dark roc";
				//itemDef.description= "An extra 20% in drop rate boost.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_Roc.png");
				break;
			case 30122:
				itemDef.setDefaults();
				itemDef.name = "@red@Dark kratos";
				//itemDef.description= "The most powerful pet, see ::foepets for full list of perks.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_yama.png");
				break;
			case 30123:
				itemDef.setDefaults();
				itemDef.name = "Dark seren";
				//itemDef.description= "85% chance for Wildy Event Boss to hit a 0 and 25% chance to double key.";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = false;
				itemDef.createCustomSprite("dark_seren.png");

				break;
			case 23939:
				itemDef.name = "Seren";
				//itemDef.description= "50% chance for wildy event bosses to hit a 0 on you.";
				itemDef.createCustomSprite("seren.png");
				break;
			case 21046:
				itemDef.name = "@cya@Chest rate bonus (+15%)";
				//itemDef.description= "A single use +15% chance from chests, or to receive a rare raids key.";
				itemDef.stackable = true;
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				break;
			case 11666:
				itemDef.name = "Full Elite Void Token";
				//itemDef.description= "Use this token to receive a full elite void set with all combat pieces.";
				itemDef.interfaceOptions = new String[] { "Activate", null, null, null, "Drop" };
				break;
			case 1004:
				itemDef.name = "@gre@20m Coins";
				//itemDef.description= "Lovely coins.";
				itemDef.stackable = false;
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 7629:
				itemDef.name = "@or3@2x Slayer point scroll";
				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
				itemDef.stackable = true;
				break;
			case 24460:
				itemDef.name = "@or3@Faster clues (30 mins)";
				//itemDef.description= "Clue rates are halved for npcs and skilling.";
				itemDef.interfaceOptions = new String[] { "Boost", null, null, null, "Drop" };
				itemDef.stackable = true;
				break;
			case 7968:
				itemDef.name = "@or3@+25% Skilling pet rate (30 mins)";
				itemDef.interfaceOptions = new String[] { "Boost", null, null, null, "Drop" };
				itemDef.stackable = true;
				break;
			case 8899:
				itemDef.name = "@gre@50m Coins";
				//itemDef.description= "Lovely coins.";
				itemDef.stackable = false;
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 4035:
				itemDef.interfaceOptions = new String[] { "Teleport", null, null, null, null };
				break;
			case 10835:
				itemDef.name = "Buldging coin bag";
				itemDef.interfaceOptions = new String[] { "Open", null, "Open-All", null, "Drop" };
				//itemDef.description= "I can see some coins inside.";
				break;
			case 15098:
				itemDef.name = "Dice (up to 100)";
				//itemDef.description= "A 100-sided dice.";
				itemDef.inventoryModel = 31223;
				itemDef.zoom2d = 1104;
				itemDef.yan2d = 215;
				itemDef.xan2d = 94;
				itemDef.yOffset2d = -5;
				itemDef.xOffset2d = -18;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Public-roll";
				itemDef.interfaceOptions[2] = null;
				itemDef.name = "Dice (up to 100)";
				itemDef.ambient = 15;
				itemDef.contrast = 25;
				itemDef.createCustomSprite("Dice_Bag.png");
				break;
			case 11773:
			case 11771:
			case 11770:
			case 11772:
				itemDef.ambient += 45;
				break;
			case 12792:
				itemDef.name = "Graceful Recolor Box";
				itemDef.interfaceOptions = new String[] { null, "Use", null, null, "Drop" };
				break;
			case 6769:
				itemDef.name = "@yel@$5 Scroll";
				//itemDef.description= "Claim this scroll to be rewarded with 5 donator points.";
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 2403:
				itemDef.name = "@yel@$10 Scroll";
				//itemDef.description= "Claim this scroll to be rewarded with 10 donator points.";
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 2396:
				itemDef.name = "@yel@$25 Scroll";
				//itemDef.description= "Claim this scroll to be rewarded with 25 donator points.";
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 786:
				itemDef.name = "@yel@$50 Donator";
				//itemDef.description= "Claim this scroll to be rewarded with 50 donator points.";
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 761:
				itemDef.name = "@yel@$100 Donator";
				//itemDef.description= "Claim this scroll to be rewarded with 100 donator points.";
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 607:
				itemDef.name = "@red@$250 Scroll";
				//itemDef.description= "Claim this scroll to be rewarded with 250 donator points.";
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 608:
				itemDef.name = "@gre@$500 Scroll";
				//itemDef.description= "Claim this scroll to be rewarded with 500 donator points.";
				itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
				break;
			case 1464:
				itemDef.name = "Vote ticket";
				//itemDef.description= "Exchange this for a Vote Point.";
				break;

			case 33049:
				itemDef.setDefaults();
				itemDef.name = "Agility master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 677, 801, 43540, 43543, 43546, 43549, 43550, 43552, 43554, 43558,
						43560, 43575 };
				itemDef.inventoryModel = 50030;
				itemDef.maleModel = 50031;
				itemDef.femaleModel = 50031;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33033:
				itemDef.setDefaults();
				itemDef.name = "Attack master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 7104, 9151, 911, 914, 917, 920, 921, 923, 925, 929, 931, 946 };
				itemDef.inventoryModel = 50032;
				itemDef.maleModel = 50033;
				itemDef.femaleModel = 50033;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33055:
				itemDef.setDefaults();
				itemDef.name = "Construction master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 6061, 5945, 6327, 6330, 6333, 6336, 6337, 6339, 6341, 6345, 6347,
						6362 };
				itemDef.inventoryModel = 50034;
				itemDef.maleModel = 50035;
				itemDef.femaleModel = 50035;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33040:
				itemDef.setDefaults();
				itemDef.name = "Cooking master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 920, 920, 51856, 51859, 51862, 51865, 51866, 51868, 51870, 51874,
						51876, 51891 };
				itemDef.inventoryModel = 50036;
				itemDef.maleModel = 50037;
				itemDef.femaleModel = 50037;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33045:
				itemDef.setDefaults();
				itemDef.name = "Crafting master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 9142, 9152, 4511, 4514, 4517, 4520, 4521, 4523, 4525, 4529, 4531,
						4546 };
				itemDef.inventoryModel = 50038;
				itemDef.maleModel = 50039;
				itemDef.femaleModel = 50039;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33034:
				itemDef.setDefaults();
				itemDef.name = "Defence master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 10460, 10473, 41410, 41413, 41416, 41419, 41420, 41422, 41424,
						41428, 41430, 41445 };
				itemDef.inventoryModel = 50040;
				itemDef.maleModel = 50041;
				itemDef.femaleModel = 50041;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33052:
				itemDef.setDefaults();
				itemDef.name = "Farming master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 14775, 14792, 22026, 22029, 22032, 22035, 22036, 22038, 22040,
						22044, 22046, 22061 };
				itemDef.inventoryModel = 50042;
				itemDef.maleModel = 50043;
				itemDef.femaleModel = 50043;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33044:
				itemDef.setDefaults();
				itemDef.name = "Firemaking master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 8125, 9152, 4015, 4018, 4021, 4024, 4025, 4027, 4029, 4033, 4035,
						4050 };
				itemDef.inventoryModel = 50044;
				itemDef.maleModel = 50045;
				itemDef.femaleModel = 50045;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33043:
				itemDef.setDefaults();
				itemDef.name = "Fishing master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 9144, 9152, 38202, 38205, 38208, 38211, 38212, 38214, 38216,
						38220, 38222, 38237 };
				itemDef.inventoryModel = 50046;
				itemDef.maleModel = 50047;
				itemDef.femaleModel = 50047;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33042:
				itemDef.setDefaults();
				itemDef.name = "Fletching master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 6067, 9152, 33670, 33673, 33676, 33679, 33680, 33682, 33684,
						33688, 33690, 33705 };
				itemDef.inventoryModel = 50048;
				itemDef.maleModel = 50049;
				itemDef.femaleModel = 50049;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33048:
				itemDef.setDefaults();
				itemDef.name = "Herblore master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 9145, 9156, 22414, 22417, 22420, 22423, 22424, 22426, 22428,
						22432, 22434, 22449 };
				itemDef.inventoryModel = 50050;
				itemDef.maleModel = 50051;
				itemDef.femaleModel = 50051;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33036:
				itemDef.setDefaults();
				itemDef.name = "Hitpoints master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 818, 951, 8291, 8294, 8297, 8300, 8301, 8303, 8305, 8309, 8311,
						8319 };
				itemDef.inventoryModel = 50052;
				itemDef.maleModel = 50053;
				itemDef.femaleModel = 50053;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				//itemDef.femaleOffset = 4;
				break;
			case 33054:
				itemDef.setDefaults();
				itemDef.name = "Hunter master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 5262, 6020, 8472, 8475, 8478, 8481, 8482, 8484, 8486, 8490, 8492,
						8507 };
				itemDef.inventoryModel = 50054;
				itemDef.maleModel = 50055;
				itemDef.femaleModel = 50055;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33039:
				itemDef.setDefaults();
				itemDef.name = "Magic master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 43569, 43685, 6336, 6339, 6342, 6345, 6346, 6348, 6350, 6354,
						6356, 6371 };
				itemDef.inventoryModel = 50056;
				itemDef.maleModel = 50057;
				itemDef.femaleModel = 50057;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33047:
				itemDef.setDefaults();
				itemDef.name = "Mining master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 36296, 36279, 10386, 10389, 10392, 10395, 10396, 10398, 10400,
						10404, 10406, 10421 };
				itemDef.inventoryModel = 50058;
				itemDef.maleModel = 50059;
				itemDef.femaleModel = 50059;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33038:
				itemDef.setDefaults();
				itemDef.name = "Prayer master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 9163, 9168, 117, 120, 123, 126, 127, 127, 127, 127, 127, 127 };
				itemDef.inventoryModel = 50060;
				itemDef.maleModel = 50061;
				itemDef.femaleModel = 50061;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33037:
				itemDef.setDefaults();
				itemDef.name = "Range master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 3755, 3998, 15122, 15125, 15128, 15131, 15132, 15134, 15136,
						15140, 15142, 15157 };
				itemDef.inventoryModel = 50062;
				itemDef.maleModel = 50063;
				itemDef.femaleModel = 50063;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33053:
				itemDef.setDefaults();
				itemDef.name = "Runecrafting master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				// 4 //7 //10 //13 //14//16//18//22 //24//39
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 9152, 8128, 10318, 10321, 10324, 10327, 10328, 10330, 10332,
						10336, 10338, 10353 };
				itemDef.inventoryModel = 50064;
				itemDef.maleModel = 50065;
				itemDef.femaleModel = 50065;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33051:
				itemDef.setDefaults();
				itemDef.name = "Slayer master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				itemDef.recolorFrom = new int[] { 57022, 48811 };
				itemDef.recolorTo = new int[] { 912, 920 };
				itemDef.inventoryModel = 50066;
				itemDef.maleModel = 50067;
				itemDef.femaleModel = 50067;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33046:
				itemDef.setDefaults();
				itemDef.name = "Smithing master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 8115, 9148, 10386, 10389, 10392, 10395, 10396, 10398, 10400,
						10404, 10406, 10421 };
				itemDef.inventoryModel = 50068;
				itemDef.maleModel = 50069;
				itemDef.femaleModel = 50069;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33035:
				itemDef.setDefaults();
				itemDef.name = "Strength master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 935, 931, 27538, 27541, 27544, 27547, 27548, 27550, 27552, 27556,
						27558, 27573 };
				itemDef.inventoryModel = 50070;
				itemDef.maleModel = 50071;
				itemDef.femaleModel = 50071;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33050:
				itemDef.setDefaults();
				itemDef.name = "Thieving master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 11, 0, 58779, 58782, 58785, 58788, 58789, 57891, 58793, 58797,
						58799, 58814 };
				itemDef.inventoryModel = 50072;
				itemDef.maleModel = 50073;
				itemDef.femaleModel = 50073;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = 5;
				break;
			case 33041:
				itemDef.setDefaults();
				itemDef.name = "Woodcutting master cape";
				//itemDef.description= "	A cape worn by those who've overachieved.";
				itemDef.recolorFrom = new int[] { 57022, 48811, 2, 1029, 1032, 11, 12, 14, 16, 20, 22, 2 };
				itemDef.recolorTo = new int[] { 25109, 24088, 6693, 6696, 6699, 6702, 6703, 6705, 6707, 6711,
						6713, 6728 };
				itemDef.inventoryModel = 50074;
				itemDef.maleModel = 50075;
				itemDef.femaleModel = 50075;
				itemDef.zoom2d = 2300;
				itemDef.xan2d = 400;
				itemDef.yan2d = 1020;
				itemDef.xOffset2d = 3;
				itemDef.yOffset2d = 30;
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = null;
				//itemDef.maleOffset = -2;
				break;
		}
	}

	public static EvictingDualNodeHashTable cached = new EvictingDualNodeHashTable(64);


	public static ItemDefinition lookup(int id) {
		ItemDefinition itemDef = (ItemDefinition) ItemDefinition.cached.get(id);
		if (newCustomItems(id) != null) {
			return newCustomItems(id);
		}
		if (itemDef == null) {
			byte[] data = Js5List.configs.takeFile(Js5ConfigType.ITEM, id);
			itemDef = new ItemDefinition();
			itemDef.setDefaults();
			itemDef.id = id;
			if (data != null) {
				itemDef.decodeValues(new Buffer(data));
			}

			itemDef.post();

			if (itemDef.notedTemplate != -1) {
				itemDef.updateNote();
			}

			customItems(id,itemDef);

			cached.put(itemDef, id);
		}
		return itemDef;
	}

	private void post() {
		if (stackable) {
			weight = 0;
		}
	}

	private static ItemDefinition newCustomItems(int itemId) {
		ItemDefinition itemDef = new ItemDefinition();
		itemDef.setDefaults();
		switch (itemId) {
			case 30000:
				return copy(itemDef, 30_000, 11738, "Resource box(small)", "Open");
			case 30001:
				return copy(itemDef, 30_001, 11738, "Resource box(medium)", "Open");
			case 30002:
				return copy(itemDef, 30_002, 11738, "Resource box(large)", "Open");
			case 22375:
				return copy(itemDef, 22375, 22374, "Mossy key");

			case 33056:
				itemDef.setDefaults();
				itemDef.id = 33056;
				itemDef.inventoryModel = 65270;
				itemDef.name = "Completionist cape";
				//itemDef.description= "A cape worn by those who've overachieved.";

				itemDef.zoom2d = 1385;
				itemDef.xan2d = 279;
				itemDef.yan2d = 948;
				itemDef.zan2d = 0;
				itemDef.xOffset2d = 0;
				itemDef.yOffset2d = 24;

				itemDef.maleModel = 65297;
				itemDef.femaleModel = 65316;
				//itemDef.groundActions = new String[5];
				//itemDef.groundActions[2] = "Take";
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				itemDef.interfaceOptions[2] = "Teleports";
				itemDef.interfaceOptions[3] = "Features";
				itemDef.interfaceOptions[4] = "Drop";
				return itemDef;
			case 33057:
				itemDef.setDefaults();
				itemDef.id = 33057;
				itemDef.inventoryModel = 65273;
				itemDef.name = "Completionist hood";
				//itemDef.description= "A hood worn by those who've over achieved.";

				itemDef.zoom2d = 760;
				itemDef.xan2d = 11;
				itemDef.yan2d = 0;
				itemDef.zan2d = 0;
				itemDef.xOffset2d = 0;
				itemDef.yOffset2d = 0;

				itemDef.maleModel = 65292;
				itemDef.femaleModel = 65310;
				//itemDef.groundActions = new String[5];
				//itemDef.groundActions[2] = "Take";
				itemDef.interfaceOptions = new String[5];
				itemDef.interfaceOptions[1] = "Wear";
				return itemDef;
		}

		return null;
	}


	void method2790(ItemDefinition var1, ItemDefinition var2) {
		inventoryModel = var1.inventoryModel * 1;
		zoom2d = 1 * var1.zoom2d;
		xan2d = var1.xan2d * 1;
		yan2d = var1.yan2d * 1;
		zan2d = var1.zan2d * 1;
		xOffset2d = 1 * var1.xOffset2d;
		yOffset2d = var1.yOffset2d * 1;
		recolorFrom = var1.recolorFrom;
		recolorTo = var1.recolorTo;
		retextureFrom = var1.retextureFrom;
		retextureTo = var1.retextureTo;
		stackable = var1.stackable;
		name = var2.name;
		price = 0;
	}

	void method2789(ItemDefinition var1, ItemDefinition var2) {
		inventoryModel = var1.inventoryModel * 1;
		zoom2d = var1.zoom2d * 1;
		xan2d = 1 * var1.xan2d;
		yan2d = 1 * var1.yan2d;
		zan2d = 1 * var1.zan2d;
		xOffset2d = 1 * var1.xOffset2d;
		yOffset2d = var1.yOffset2d * 1;
		recolorFrom = var2.recolorFrom;
		recolorTo = var2.recolorTo;
		// originalTextureColors = var2.originalTextureColors;
		// modifiedTextureColors = var2.modifiedTextureColors;
		name = var2.name;
		members = var2.members;
		stackable = var2.stackable;
		maleModel = 1 * var2.maleModel;
		maleModel1 = 1 * var2.maleModel1;
		maleModel2 = 1 * var2.maleModel2;
		femaleModel = var2.femaleModel * 1;
		femaleModel1 = var2.femaleModel1 * 1;
		femaleModel2 = 1 * var2.femaleModel2;
		maleHeadModel = 1 * var2.maleHeadModel;
		maleHeadModel2 = var2.maleHeadModel2 * 1;
		femaleHeadModel = var2.femaleHeadModel * 1;
		femaleHeadModel2 = var2.femaleHeadModel2 * 1;
		team = var2.team * 1;
		groundActions = var2.groundActions;
		interfaceOptions = new String[5];
		equipActions = new String[5];
		if (null != var2.interfaceOptions) {
			for (int var4 = 0; var4 < 4; ++var4) {
				interfaceOptions[var4] = var2.interfaceOptions[var4];
			}
		}

		interfaceOptions[4] = "Discard";
		price = 0;
	}

	void toPlaceholder(ItemDefinition var1, ItemDefinition var2) {
		inventoryModel = var1.inventoryModel * 1;
		zoom2d = 1 * var1.zoom2d;
		xan2d = var1.xan2d * 1;
		yan2d = var1.yan2d * 1;
		zan2d = var1.zan2d * 1;
		xOffset2d = 1 * var1.xOffset2d;
		yOffset2d = var1.yOffset2d * 1;
		recolorFrom = var1.recolorFrom;
		recolorTo = var1.recolorTo;
		retextureFrom = var1.retextureFrom;
		retextureTo = var1.retextureTo;
		stackable = var1.stackable;
		name = var2.name;
		price = 0;
	}

	public static Sprite getSprite(int itemId, int stackSize, int outlineColor, boolean noted, int border,int shadow) {

		return Sprite.EMPTY_SPRITE;
	}

	public static Sprite getSmallSprite(int itemId) {
		return getSmallSprite(itemId, 1);
	}

	public static Sprite getSmallSprite(int itemId, int stackSize) {
		return Sprite.EMPTY_SPRITE;
	}


	public static Sprite getSprite(int itemId, int stackSize, int outlineColor) {
		int zoom = Client.instance.get3dZoom();
		Client.instance.set3dZoom(CLIENT_DEFAULT_ZOOM);
		try {
			if (outlineColor == 0) {
				Sprite sprite = (Sprite) sprites.get(itemId);
				if (sprite != null && sprite.maxHeight != stackSize) {

					sprite.unlink();
					sprite = null;
				}
				if (sprite != null)
					return sprite;
			}
			ItemDefinition itemDef = lookup(itemId);
			if (itemDef.countObj == null)
				stackSize = -1;
			if (stackSize > 1) {
				int stack_item_id = -1;
				for (int j1 = 0; j1 < 10; j1++)
					if (stackSize >= itemDef.countCo[j1] && itemDef.countCo[j1] != 0)
						stack_item_id = itemDef.countObj[j1];

				if (stack_item_id != -1)
					itemDef = lookup(stack_item_id);
			}
			Model model = itemDef.getModel(1);
			if (model == null)
				return null;
			Sprite sprite = null;
			if (itemDef.notedTemplate != -1) {
				sprite = getSprite(itemDef.notedID, 10, -1);
				if (sprite == null) {
					return null;
				}
			} else if (itemDef.notedID != -1) {
				sprite = getSprite(itemDef.unnotedId, 10, -1);
				if (sprite == null) {
					return null;
				}
			} else if (itemDef.placeholderTemplate != -1) {
				sprite = getSprite(itemDef.placeholder, 10, -1);
				if (sprite == null) {
					return null;
				}
			}

			int[] pixels = Rasterizer2D.pixels;
			int width = Rasterizer2D.width;
			int height = Rasterizer2D.height;
			float[] depth = Rasterizer2D.depth;
			int[] arrayClip = new int[4];
			Rasterizer2D.getClipArray(arrayClip);
			Sprite enabledSprite = new Sprite(32, 32);
			Rasterizer3D.initDrawingArea(enabledSprite.myPixels, 32, 32, (float[]) null);
			Rasterizer2D.clear();
			Rasterizer3D.setupRasterizerClip();
			Rasterizer3D.drawImage(16, 16);
			Rasterizer3D.clips.rasterGouraudLowRes = false;
			model.renderonGpu = false;
			if (itemDef.placeholderTemplate != -1) {
				int old_w = sprite.maxWidth;
				int old_h = sprite.maxHeight;
				sprite.maxWidth = 32;
				sprite.maxHeight = 32;
				sprite.drawSprite(0, 0);
				sprite.maxWidth = old_w;
				sprite.maxHeight = old_h;
			}

			int k3 = itemDef.zoom2d;
			if (outlineColor == -1)
				k3 = (int) ((double) k3 * 1.5D);
			if (outlineColor > 0)
				k3 = (int) ((double) k3 * 1.04D);

			int l3 = Rasterizer3D.SINE[itemDef.xan2d] * k3 >> 16;
			int i4 = Rasterizer3D.COSINE[itemDef.xan2d] * k3 >> 16;

			model.calculateBoundsCylinder();
			model.renderModel(itemDef.yan2d, itemDef.zan2d, itemDef.xan2d, itemDef.xOffset2d, l3 + model.model_height / 2 + itemDef.yOffset2d, i4 + itemDef.yOffset2d);

			if (itemDef.notedID != -1) {
				enabledSprite.drawAdvancedSprite(0, 0);
			}

			enabledSprite.outline(1);
			if (outlineColor > 0) {
				enabledSprite.outline(16777215);
			}
			if (outlineColor == 0) {
				enabledSprite.shadow(3153952);
			}

			Rasterizer3D.initDrawingArea(enabledSprite.myPixels, 32, 32, (float[]) null);

			if (itemDef.notedTemplate != -1) {
				int old_w = sprite.maxWidth;
				int old_h = sprite.maxHeight;
				sprite.maxWidth = 32;
				sprite.maxHeight = 32;
				sprite.drawSprite(0, 0);
				sprite.maxWidth = old_w;
				sprite.maxHeight = old_h;
			}

			if (outlineColor == 0) {
				sprites.put(enabledSprite, itemId);
			}


			Rasterizer3D.initDrawingArea(pixels, width, height, depth);
			Rasterizer2D.setClipArray(arrayClip);
			Rasterizer3D.setupRasterizerClip();
			Rasterizer3D.clips.rasterGouraudLowRes = true;
			model.renderonGpu = true;
			return enabledSprite;
		} finally {
			Client.instance.set3dZoom(zoom);
		}
	}

	public static Sprite getSprite(int var0, int var1, int var2, int var3, int var4, boolean var5) {
		if (var1 == -1) {
			var4 = 0;
		} else if (var4 == 2 && var1 != 1) {
			var4 = 1;
		}

		long var6 = ((long) var2 << 38) + (long) var0 + ((long) var1 << 16) + ((long) var4 << 40) + ((long) var3 << 42);
		Sprite var8;
		if (!var5) {
			var8 = (Sprite) cached.get(var6);
			if (var8 != null) {
				return var8;
			}
		}

		ItemDefinition var9 = ItemDefinition.lookup(var0);
		if (var1 > 1 && var9.countObj != null) {
			int var10 = -1;

			for (int var11 = 0; var11 < 10; ++var11) {
				if (var1 >= var9.countCo[var11] && var9.countCo[var11] != 0) {
					var10 = var9.countObj[var11];
				}
			}

			if (var10 != -1) {
				var9 = ItemDefinition.lookup(var10);
			}
		}

		Model var20 = var9.getModel(1);
		if (var20 == null) {
			return null;
		} else {
			Sprite var21 = null;
			if (var9.notedTemplate != -1) {
				var21 = getSprite(var9.notedID, 10, 1, 0, 0, true);
				if (var21 == null) {
					return null;
				}
			} else if (var9.notedID != -1) {
				var21 = getSprite(var9.unnotedId, var1, var2, var3, 0, false);
				if (var21 == null) {
					return null;
				}
			} else if (var9.placeholderTemplate != -1) {
				var21 = getSprite(var9.placeholder, var1, 0, 0, 0, false);
				if (var21 == null) {
					return null;
				}
			}

			int[] var12 = Rasterizer2D.pixels;
			int var13 = Rasterizer2D.width;
			int var14 = Rasterizer2D.height;
			float[] var15 = Rasterizer2D.depth;
			int[] var16 = new int[4];
			Rasterizer2D.getClipArray(var16);
			var8 = new Sprite(36, 32);
			Rasterizer3D.initDrawingArea(var8.myPixels, 36, 32, (float[]) null);
			Rasterizer2D.clear();
			Rasterizer3D.setupRasterizerClip();
			Rasterizer3D.drawImage(16, 16);
			Rasterizer3D.clips.rasterGouraudLowRes = false;
			var20.renderonGpu = false;
			if (var9.placeholderTemplate != -1) {
				var21.drawAdvancedSprite(0, 0);
			}

			int var17 = var9.zoom2d;
			if (var5) {
				var17 = (int) (1.5D * (double) var17);
			} else if (var2 == 2) {
				var17 = (int) ((double) var17 * 1.04D);
			}

			int var18 = var17 * Rasterizer3D.SINE[var9.xan2d] >> 16;
			int var19 = var17 * Rasterizer3D.COSINE[var9.xan2d] >> 16;
			var20.calculateBoundsCylinder();
			var20.renderModel(var9.yan2d, var9.zan2d, var9.xan2d, var9.xOffset2d, var20.model_height / 2 + var18 + var9.yOffset2d, var19 + var9.yOffset2d);
			if (var9.notedID != -1) {
				var21.drawAdvancedSprite(0, 0);
			}

			if (var2 >= 1) {
				var8.outline(1);
			}

			if (var2 >= 2) {
				var8.outline(16777215);
			}

			if (var3 != 0) {
				var8.shadow(var3);
			}

			Rasterizer3D.initDrawingArea(var8.myPixels, 36, 32, (float[]) null);
			if (var9.notedTemplate != -1) {
				var21.drawAdvancedSprite(0, 0);
			}

			if (var4 == 1 || var4 == 2 && var9.stackable) {
				Client.instance.newSmallFont.drawBasicString(method633(var1), 0, 9, 16776960, 1);
			}

			if (!var5) {
				sprites.put(var8, var6);
			}

			Rasterizer3D.initDrawingArea(var12, var13, var14, var15);
			Rasterizer2D.setClipArray(var16);
			Rasterizer3D.setupRasterizerClip();
			Rasterizer3D.clips.rasterGouraudLowRes = true;
			var20.renderonGpu = true;
			return var8;
		}
	}

	static String method633(int var0) {
		if (var0 < 100000) {
			return "<col=ffff00>" + var0 + "</col>";
		} else {
			return var0 < 10000000 ? "<col=ffffff>" + var0 / 1000 + "K" + "</col>" : "<col=00ff80>" + var0 / 1000000 + "M" + "</col>";
		}
	}

	public boolean isDialogueModelCached(int gender) {
		int model_1 = maleHeadModel;
		int model_2 = maleHeadModel2;
		if (gender == 1) {
			model_1 = femaleHeadModel;
			model_2 = femaleHeadModel2;
		}
		if (model_1 == -1)
			return true;
		boolean cached = Js5List.models.tryLoadFile(model_1);
		if (model_2 != -1 && !Js5List.models.tryLoadFile(model_2))
			cached = false;
		return cached;
	}

	public boolean isEquippedModelCached(int gender) {
		int primaryModel = maleModel;
		int secondaryModel = maleModel1;
		int emblem = maleModel2;
		if (gender == 1) {
			primaryModel = femaleModel;
			secondaryModel = femaleModel1;
			emblem = femaleModel2;
		}
		if (primaryModel == -1)
			return true;
		boolean cached = Js5List.models.tryLoadFile(primaryModel);
		if (secondaryModel != -1 && !Js5List.models.tryLoadFile(secondaryModel))
			cached = false;
		if (emblem != -1 && !Js5List.models.tryLoadFile(emblem))
			cached = false;
		return cached;
	}

	public Mesh getEquippedModel(int gender) {
		int primaryModel = maleModel;
		int secondaryModel = maleModel1;
		int emblem = maleModel2;

		if (gender == 1) {
			primaryModel = femaleModel;
			secondaryModel = femaleModel1;
			emblem = femaleModel2;
		}

		if (primaryModel == -1)
			return null;
		Mesh primaryModel_ = Mesh.getModel(primaryModel);
		if (secondaryModel != -1)
			if (emblem != -1) {
				Mesh secondaryModel_ = Mesh.getModel(secondaryModel);
				Mesh emblemModel = Mesh.getModel(emblem);
				Mesh[] models = {primaryModel_, secondaryModel_, emblemModel};
				primaryModel_ = new Mesh(models,3);
			} else {
				Mesh model_2 = Mesh.getModel(secondaryModel);
				Mesh[] models = {primaryModel_, model_2};
				primaryModel_ = new Mesh(models,2);
			}
		if (gender == 0 && maleOffset != 0)
			primaryModel_.translate(0, maleOffset, 0);
		if (gender == 1 && femaleOffset != 0)
			primaryModel_.translate(0, femaleOffset, 0);


		if (recolorFrom != null) {
			for (int index = 0; index < recolorFrom.length; index++) {
				primaryModel_.recolor((short) recolorFrom[index], (short) recolorTo[index]);
			}
		}
		if (retextureFrom != null) {
			for (int index = 0; index < retextureFrom.length; index++) {
				primaryModel_.retexture(retextureFrom[index], retextureTo[index]);
			}
		}

		return primaryModel_;
	}

	private void setDefaults() {
		customSpriteLocation = null;
		customSmallSpriteLocation = null;
		equipActions = new String[]{"Remove", null, "Operate", null, null};
		inventoryModel = 0;
		name = null;
		recolorFrom = null;
		recolorTo = null;
		retextureTo = null;
		retextureFrom = null;

		zoom2d = 2000;
		xan2d = 0;
		yan2d = 0;
		zan2d = 0;
		xOffset2d = 0;
		yOffset2d = 0;
		stackable = false;
		price = 1;
		members = false;
		groundActions = new String[]{null, null, "Take", null, null};
		interfaceOptions = new String[]{null, null, null, null, "Drop"};
		maleModel = -1;
		maleModel1 = -1;
		maleOffset = 0;
		femaleModel = -1;
		femaleModel1 = -1;
		femaleOffset = 0;
		maleModel2 = -1;
		femaleModel2 = -1;
		maleHeadModel = -1;
		maleHeadModel2 = -1;
		femaleHeadModel = -1;
		femaleHeadModel2 = -1;
		countObj = null;
		countCo = null;
		notedTemplate = -1;
		resizeX = 128;
		resizeY = 128;
		resizeZ = 128;
		ambient = 0;
		contrast = 0;
		team = 0;
		unnotedId = -1;
		notedID = -1;
		placeholder = -1;
		placeholderTemplate = -1;
		glowColor = -1;
	}

	private void copy(ItemDefinition copy) {
		yan2d = copy.yan2d;
		xan2d = copy.xan2d;
		zan2d = copy.zan2d;
		resizeX = copy.resizeX;
		resizeY = copy.resizeY;
		resizeZ = copy.resizeZ;
		zoom2d = copy.zoom2d;
		xOffset2d = copy.xOffset2d;
		yOffset2d = copy.yOffset2d;
		inventoryModel = copy.inventoryModel;
		stackable = copy.stackable;

	}

	private void updateNote() {
		ItemDefinition itemDef = lookup(notedTemplate);
		inventoryModel = itemDef.inventoryModel;
		zoom2d = itemDef.zoom2d;
		xan2d = itemDef.xan2d;
		yan2d = itemDef.yan2d;

		zan2d = itemDef.zan2d;
		xOffset2d = itemDef.xOffset2d;
		yOffset2d = itemDef.yOffset2d;

		ItemDefinition itemDef_1 = lookup(notedID);
		name = itemDef_1.name;
		members = itemDef_1.members;
		price = itemDef_1.price;
		stackable = true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public int getNote() {
		return 0;
	}

	@Override
	public int getLinkedNoteId() {
		return 0;
	}

	@Override
	public int getPlaceholderId() {
		return 0;
	}

	@Override
	public int getPlaceholderTemplateId() {
		return 0;
	}

	@Override
	public int getPrice() {
		return 0;
	}

	@Override
	public boolean isMembers() {
		return false;
	}

	@Override
	public boolean isTradeable() {
		return false;
	}

	@Override
	public void setTradeable(boolean yes) {

	}

	@Override
	public int getIsStackable() {
		return 0;
	}

	@Override
	public int getMaleModel() {
		return 0;
	}

	@Override
	public String[] getInventoryActions() {
		return new String[0];
	}

	@Override
	public String[] getGroundActions() {
		return new String[0];
	}

	@Override
	public int getShiftClickActionIndex() {
		return 0;
	}

	@Override
	public void setShiftClickActionIndex(int shiftClickActionIndex) {

	}

	public Mesh getModelWidget(int stack_size) {
		if (countObj != null && stack_size > 1) {
			int stack_item_id = -1;
			for (int index = 0; index < 10; index++) {
				if (stack_size >= countCo[index] && countCo[index] != 0)
					stack_item_id = countObj[index];
			}
			if (stack_item_id != -1)
				return lookup(stack_item_id).getModelWidget(1);

		}
		Mesh widget_model = Mesh.getModel(inventoryModel);
		if (widget_model == null)
			return null;

		int var4;
		if (this.recolorFrom != null) {
			for (var4 = 0; var4 < this.recolorFrom.length; ++var4) {
				widget_model.recolor((short) this.recolorFrom[var4], (short) this.recolorTo[var4]);
			}
		}

		if (this.retextureFrom != null) {
			for (var4 = 0; var4 < this.retextureFrom.length; ++var4) {
				widget_model.retexture(this.retextureFrom[var4], this.retextureTo[var4]);
			}
		}


		return widget_model;
	}

	public Model getModel(int stack_size) {
		if (this.countObj != null && stack_size > 1) {
			int var2 = -1;

			for (int var3 = 0; var3 < 10; ++var3) {
				if (stack_size >= this.countCo[var3] && this.countCo[var3] != 0) {
					var2 = this.countObj[var3];
				}
			}

			if (var2 != -1) {
				return lookup(var2).getModel(1);
			}
		}

		Model var5 = (Model) models.get((long) this.id);
		if (var5 != null) {
			return var5;
		} else {
			Mesh var6 = Mesh.getModel(this.inventoryModel);
			if (var6 == null) {
				return null;
			} else {
				if (this.resizeX != 128 || this.resizeY != 128 || this.resizeZ != 128) {
					var6.resize(this.resizeX, this.resizeY, this.resizeZ);
				}


				int var4;
				if (this.recolorFrom != null) {
					for (var4 = 0; var4 < this.recolorFrom.length; ++var4) {
						var6.recolor((short) this.recolorFrom[var4], (short) this.recolorTo[var4]);
					}
				}

				if (this.retextureFrom != null) {
					for (var4 = 0; var4 < this.retextureFrom.length; ++var4) {
						var6.retexture(this.retextureFrom[var4], this.retextureTo[var4]);
					}
				}


				var5 = var6.toModel(this.ambient + 64, this.contrast + 768, -50, -10, -50);
				var5.singleTile = true;

				models.put(var5, (long) this.id);
				return var5;
			}
		}
	}


	@Override
	public int getInventoryModel() {
		return 0;
	}

	@Override
	public short[] getColorToReplaceWith() {
		return new short[0];
	}

	@Override
	public short[] getTextureToReplaceWith() {
		return new short[0];
	}

	@Override
	public RSIterableNodeHashTable getParams() {
		return null;
	}

	@Override
	public void setParams(IterableHashTable params) {

	}

	@Override
	public void setParams(RSIterableNodeHashTable params) {

	}

	private void decodeValues(Buffer stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				return;
			if (opcode == 1) {
				inventoryModel = stream.readUShort();
			} else if (opcode == 2) {
				name = stream.readStringCp1252NullTerminated();
			} else if (opcode == 3) {
				description = stream.readStringCp1252NullTerminated();
			} else if (opcode == 4) {
				zoom2d = stream.readUShort();
			} else if (opcode == 5) {
				xan2d = stream.readUShort();
			} else if (opcode == 6) {
				yan2d = stream.readUShort();
			} else if (opcode == 7) {
				xOffset2d = stream.readUShort();
				if (xOffset2d > 32767) {
					xOffset2d -= 65536;
				}
			} else if (opcode == 8) {
				yOffset2d = stream.readUShort();
				if (yOffset2d > 32767) {
					yOffset2d -= 65536;
				}
			} else if (opcode == 11) {
				stackable = true;
			} else if (opcode == 12) {
				price = stream.readInt();
			} else if (opcode == 13) {
				wearPos1 = stream.readByte();
			} else if (opcode == 14) {
				wearPos2 = stream.readByte();
			} else if (opcode == 16) {
				members = true;
			} else if (opcode == 23) {
				maleModel = stream.readUShort();
				maleOffset = stream.readUnsignedByte();
			} else if (opcode == 24) {
				maleModel1 = stream.readUShort();
			} else if (opcode == 25) {
				femaleModel = stream.readUShort();
				femaleOffset = stream.readUnsignedByte();
			} else if (opcode == 26) {
				femaleModel1 = stream.readUShort();
			} else if (opcode == 27) {
				wearPos3 = stream.readByte();
			} else if (opcode >= 30 && opcode < 35) {
				groundActions[opcode - 30] = stream.readStringCp1252NullTerminated();
				if (groundActions[opcode - 30].equalsIgnoreCase("Hidden")) {
					groundActions[opcode - 30] = null;
				}
			} else if (opcode >= 35 && opcode < 40) {
				if (interfaceOptions == null) {
					interfaceOptions = new String[5];
				}
				interfaceOptions[opcode - 35] = stream.readStringCp1252NullTerminated();
			} else if (opcode == 40) {
				int var5 = stream.readUnsignedByte();
				recolorFrom = new int[var5];
				recolorTo = new int[var5];

				for (int var4 = 0; var4 < var5; ++var4) {
					recolorFrom[var4] = (short) stream.readUShort();
					recolorTo[var4] = (short) stream.readUShort();
				}

			} else if (opcode == 41) {
				int var5 = stream.readUnsignedByte();
				retextureFrom = new short[var5];
				retextureTo = new short[var5];

				for (int var4 = 0; var4 < var5; ++var4) {
					retextureFrom[var4] = (short) stream.readUShort();
					retextureTo[var4] = (short) stream.readUShort();
				}

			} else if (opcode == 42) {
				shiftClickIndex = stream.readByte();
			} else if (opcode == 65) {
				isTradable = true;
			} else if (opcode == 75) {
				weight = stream.readShort();
			} else if (opcode == 78) {
				maleModel2 = stream.readUShort();
			} else if (opcode == 79) {
				femaleModel2 = stream.readUShort();
			} else if (opcode == 90) {
				maleHeadModel = stream.readUShort();
			} else if (opcode == 91) {
				femaleHeadModel = stream.readUShort();
			} else if (opcode == 92) {
				maleHeadModel2 = stream.readUShort();
			} else if (opcode == 93) {
				femaleHeadModel2 = stream.readUShort();
			} else if (opcode == 94) {
				category = stream.readUShort();
			} else if (opcode == 95) {
				zan2d = stream.readUShort();
			} else if (opcode == 97) {
				notedID = stream.readUShort();
			} else if (opcode == 98) {
				notedTemplate = stream.readUShort();
			} else if (opcode >= 100 && opcode < 110) {
				if (countObj == null) {
					countObj = new int[10];
					countCo = new int[10];
				}

				countObj[opcode - 100] = stream.readUShort();
				countCo[opcode - 100] = stream.readUShort();
			} else if (opcode == 110) {
				resizeX = stream.readUShort();
			} else if (opcode == 111) {
				resizeY = stream.readUShort();
			} else if (opcode == 112) {
				resizeZ = stream.readUShort();
			} else if (opcode == 113) {
				ambient = stream.readByte();
			} else if (opcode == 114) {
				contrast = stream.readByte();
			} else if (opcode == 115) {
				team = stream.readUnsignedByte();
			} else if (opcode == 139) {
				unnotedId = stream.readUShort();
			} else if (opcode == 140) {
				notedID = stream.readUShort();
			} else if (opcode == 148) {
				placeholder = stream.readUShort();
			} else if (opcode == 149) {
				placeholderTemplate = stream.readUShort();
			} else if (opcode == 249) {
				this.params = Buffer.readStringIntParameters(stream, this.params);
			} else {
				System.err.printf("Error unrecognised {Items} opcode: %d%n%n", opcode);
			}
		}
	}

	@Override
	public int getHaPrice() {
		return 0;
	}

	@Override
	public boolean isStackable() {
		return false;
	}

	@Override
	public void resetShiftClickActionIndex() {

	}

	@Override
	public int getIntValue(int paramID) {
		return 0;
	}

	@Override
	public void setValue(int paramID, int value) {

	}

	@Override
	public String getStringValue(int paramID) {
		return null;
	}

	@Override
	public void setValue(int paramID, String value) {

	}
}