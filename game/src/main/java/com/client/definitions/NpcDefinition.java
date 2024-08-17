package com.client.definitions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//
//import org.apache.commons.io.FileUtils;

import com.client.*;
import com.client.collection.EvictingDualNodeHashTable;
import com.client.collection.node.DualNode;
import com.client.collection.table.IterableNodeHashTable;
import com.client.definitions.anim.SequenceDefinition;
import com.client.entity.model.Mesh;
import com.client.entity.model.Model;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import com.client.model.Npcs;
import net.runelite.api.HeadIcon;
import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSNPCComposition;


public final class NpcDefinition extends DualNode implements RSNPCComposition {

	public static EvictingDualNodeHashTable npcsCached = new EvictingDualNodeHashTable(64);

	private static int defaultHeadIconArchive = -1;

	public static void init(int headIconArchive) {
		defaultHeadIconArchive = headIconArchive;
	}


	public static NpcDefinition get(int id) {
		NpcDefinition cachedNpc = (NpcDefinition)NpcDefinition.npcsCached.get(id);
		if (cachedNpc == null) {
			byte[] data = Js5List.configs.takeFile(Js5ConfigType.NPC, id);
			cachedNpc = new NpcDefinition();
			cachedNpc.npcId = id;
			if (data != null) {
				cachedNpc.decode(new Buffer(data));
			}

			if (id == Npcs.BOB_BARTER_HERBS) {
				cachedNpc.actions = new String[]{"Talk-to", "Prices", "Decant", "Clean", null};
			}
			if (id == Npcs.ZAHUR)
				cachedNpc.actions[0] = "Trade";
			if (id == Npcs.JOSSIK) {
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Talk-to";
				cachedNpc.actions[2] = "Trade";
			}
			if (id == 9460 || id == 1150 || id == 2912 || id == 2911 || id == 2910 || id == 6481
					|| id == 3500 || id == 9459 || id == 9457 || id == 9458) {
				// Setting combat to zero to npcs that can't be attacked
				cachedNpc.combatLevel = 0;
			}
			if (id == Npcs.PERDU) {
				cachedNpc.actions = new String[]{"Talk-to", null, "Reclaim-lost", null, null};
			}
			if (id == 8184) {
				cachedNpc.name = "Theatre Of Blood Wizard";
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Teleport";
			}
			if (id == 7599) {
				cachedNpc.name = "Xeros Guide";
			}
			if (id == 4305) {
				cachedNpc.name = "Drunken cannoneer";
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Pickpocket";
			}
			if (id == 3247) {
				cachedNpc.name = "Wizard";
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Teleport";
			}
			if (id == 6517) {
				cachedNpc.name = "Daily-reward wizard";
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Talk-to";
				cachedNpc.actions[2] = "View rewards";
			}
			if (id == 3428 || id == 3429) {
				cachedNpc.name = "Elf warrior";
			}
			if (id == 5044) { // sanfew (decant)
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Decant-potions";
			}
			if (id == 8026) {
				cachedNpc.combatLevel = 392;
			}
			if (id == 7913) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Ironman shop keeper";
				cachedNpc.description = "A shop specifically for iron men.";
			}
			if (id == 8906) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Santa's little elf";
				cachedNpc.description = "A helper sent from santa himself.";
				cachedNpc.actions = new String[]{"Talk-To", null, "Christmas Shop", "Return-Items", null};
			}
			if (id == 954) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Crystal Seed Trader";
				cachedNpc.description = "Use a seed on me to get a Crystal Bow.";

			}
			if (id == 6970) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Theif";
				cachedNpc.actions = new String[]{null, null, "Pickpocket", null, null};
			}
			if (id == 8761) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Talk-to", null, "Assignment", "Trade", "Rewards"};

			}
			if (id == 9400) {
				cachedNpc.name = "Ted O'bombr";
			}
			if (id == 8026 || id == 8027 || id == 8028) {
				cachedNpc.size = 9;
			}
			if (id == 7954) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Achievement Master";
				cachedNpc.actions = new String[]{"Trade", null, "Open Achievements", null, null,};

			}
			if (id == 5870) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Talk-to", null, "Assignment", "Trade", "Rewards"};

			}
			if (id == 3400) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Giveaway Manager";
				cachedNpc.actions = new String[]{"Open-manager", null, null, null, null};

			}
			if (id == 1013) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Gambler Shop";
				cachedNpc.description = "A shop specifically for gamblers.";
			}
			if (id == 308) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "PKP Manager";
			}
			if (id == 13) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Referral Tutor";
				cachedNpc.description = "He manages referrals.";
			}
			if (id == 5293) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Elven Keeper";
			}
			if (id == 3218 || id == 3217) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 2897) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Trading Post Manager";
				cachedNpc.actions = new String[]{"Open", null, "Collect", null, null};
			}
			if (id == 1306) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Make-over", null, null, null, null};
			}
			if (id == 3257) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 1011) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Item Gambler";
				cachedNpc.actions = new String[]{"Info", null, "Gamble", null, null};
			}
			if (id == 3248) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = Configuration.CLIENT_TITLE + " Wizard";
				cachedNpc.actions = new String[]{"Teleport", null, "Previous Location", null, null};
			}
			if (id == 1520) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Small Net", null, "Harpoon", null, null};
			}
			if (id == 8920) {

				cachedNpc.actions = new String[]{null, "Attack", null, null, null};
			}
			if (id == 8921) {
				cachedNpc.name = "Crystal Whirlwind";
			}
			if (id == 9120) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Donator Shop";
				cachedNpc.actions = new String[]{"Trade", null, "Rewards", null, null};
			}
			if (id == 2662) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Tournament Manager";
				cachedNpc.actions = new String[]{"Open-Shop", null, null, null, null};
			}
			if (id == 603) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Captain Kraken";
				cachedNpc.actions = new String[]{"Talk-to", null, null, null, null};
			}
			if (id == 7041) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Ticket Exchange";
				cachedNpc.actions = new String[]{"Exchange", null, null, null, null};
			}
			if (id == 3894) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Sigmund The Merchant";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}

			if (id == 7413) {
				cachedNpc.name = "Max Dummy";
				cachedNpc.actions[0] = null;
			}
			if (id == 9011) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Vote Shop";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 1933) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Mills";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 8819) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Boss point shop";
				cachedNpc.actions = new String[]{null, null, "Trade", null, null};
			}
			if (id == 8688) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Fat Tony";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 7769) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Shop Keeper";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 6987) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Man";
				cachedNpc.actions = new String[]{"Talk", null, "Pickpocket", null, null};
			}
			if (id == 5730) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Master Farmer";
				cachedNpc.actions = new String[]{"Pickpocket", null, "Trade", null, null};
			}
			if (id == 1501) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Hunter Store";
				cachedNpc.actions = new String[]{null, null, null, null, "Trade"};
			}
			if (id == 2913) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Fishing Store";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 5809) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Crafting and Tanner";
				cachedNpc.actions = new String[]{"Tan", null, "Trade", null, null};
			}
			if (id == 555) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Sell Me Store";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 9168) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Flex";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 8208) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Pet Collector";
				cachedNpc.actions = new String[]{"Talk-to", null, null, null, null};
			}
			if (id == 8202) {
				cachedNpc.actions = new String[]{"Talk-to", "Pick-Up", null, null, null};
			}
			if (id == 4921) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Supplies";
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 5314) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Mystical Wizard";
				cachedNpc.actions = new String[]{"Teleport", "Previous Location", null, null, null};
				cachedNpc.description = "This wizard has the power to teleport you to many locations.";
			}
			if (id == 8781) {
				cachedNpc.name = "@red@Queen Latsyrc";
				cachedNpc.combatLevel = 982;
				cachedNpc.isMinimapVisible = true;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{null, "Attack", null, null, null};
			}
			if (id == 1577) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Melee Shop";
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 1576) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Range Shop";
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 1578) {
				cachedNpc.combatLevel = 0;
				cachedNpc.name = "Mage Shop";
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Trade", null, null, null, null};
			}
			if (id == 8026) {
				cachedNpc.name = "Vorkath";
				// cachedNpc.combatLevel = 732;
				cachedNpc.modelId = new int[]{35023};
				cachedNpc.standingAnimation = 7946;
				cachedNpc.isMinimapVisible = true;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Poke", null, null, null, null};
				cachedNpc.heightScale = 100;
				cachedNpc.widthScale = 100;
			}
			if (id == 7852 || id == 7853 || id == 7884) {//Dawn
				cachedNpc.standingAnimation = 7775;
				cachedNpc.walkingAnimation = 7775;
			}
			if (id == 5518) {
				cachedNpc.standingAnimation = 185;
			}
			if (id == 8019) {
				cachedNpc.standingAnimation = 185;
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Talk-to";
				cachedNpc.actions[2] = "Trade";
			}
			if (id == 308) {
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Talk-to";
				cachedNpc.actions[2] = "Trade";
				cachedNpc.actions[3] = "Disable Interface";
				cachedNpc.actions[4] = "Skull";
			}
			if (id == 6088) {
				cachedNpc.standingAnimation = 185;
				cachedNpc.actions = new String[5];
				cachedNpc.actions[0] = "Talk-to";
				cachedNpc.actions[2] = "Travel";
			}
			if (id == 1434 || id == 876 || id == 1612) {//gnome fix
				cachedNpc.standingAnimation = 185;
			}
			if (id == 7674 || id == 8009 || id == 388 || id == 8010) {

				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", "Metamorphosis", null};
			}
			if (id == 8492 || id == 8493 || id == 8494 || id == 8495) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", "Metamorphosis", null};
			}
			if (id == 8737 || id == 8738 || id == 8009 || id == 7674) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", "Metamorphosis", null};
			}
			if (id == 326 || id == 327) {
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 85;
				cachedNpc.widthScale = 85;
				cachedNpc.name = "Vote Pet";
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", "Metamorphosis", null};
			}
			if (id >= 7354 && id <= 7367) {
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", "Metamorphosis", null};
			}
			if (id == 5559 || id == 5560) {
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", "Metamorphosis", null};
			}
			if (id == 2149 || id == 2150 || id == 2151 || id == 2148) {
				cachedNpc.name = "Trading Clerk";
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Bank", null, "Trading Post", null, null};
			}
			if (id == 6473) { //terror dog
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 50; //WIDTH
				cachedNpc.widthScale = 50; // HEIGH
			}
			if (id == 3510) { //outlast shop
				cachedNpc.name = "Trader";
				cachedNpc.combatLevel = 0;
				cachedNpc.isMinimapVisible = true;
				cachedNpc.heightScale = 150; //WIDTH
				cachedNpc.widthScale = 150; // HEIGH
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Open-Shop", null, null, null, null};
			}
			if (id == 488) { //rain cloud
				cachedNpc.combatLevel = 0;
				cachedNpc.size = 1;
				cachedNpc.isMinimapVisible = true;
				cachedNpc.heightScale = 150; //WIDTH
				cachedNpc.widthScale = 150; // HEIGH
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
			}
			if (id == 7668) { //voice of yama
				cachedNpc.name = "Kratos";
				cachedNpc.size = 2;
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 90; //WIDTH
				cachedNpc.widthScale = 90; // HEIGH
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};

			}
			if (id == 1377) {
				cachedNpc.size = 3;
				cachedNpc.heightScale = 300; //WIDTH
				cachedNpc.widthScale = 300; // HEIGH
				cachedNpc.actions[0] = null;


			}
			if (id == 2105) {
				cachedNpc.size = 4;
				cachedNpc.heightScale = 600; //WIDTH
				cachedNpc.widthScale = 600; // HEIGH
			}
			if (id == 2107) {
				cachedNpc.size = 4;
				cachedNpc.heightScale = 600; //WIDTH
				cachedNpc.widthScale = 600; // HEIGH
			}
			if (id == 2850) {
				cachedNpc.name = "GIM Tracker";
				cachedNpc.actions = new String[]{"Open", null, null, null, null};

			}
			if (id == 6119) { //weird monster
				cachedNpc.size = 1;
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 30; //WIDTH
				cachedNpc.widthScale = 30; // HEIGH
			}
			if (id == 763) { //roc

				cachedNpc.size = 1;
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 30; //WIDTH
				cachedNpc.widthScale = 30; // HEIGH
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", "Metamorphosis", null};


			}
			if (id == 762) { //foe small bird
				cachedNpc.size = 1;
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", "Metamorphosis", null};
			}
			if (id == 4987 || id == 6292 || id == 6354) { //chronzon
				cachedNpc.size = 1;
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 45; //WIDTH
				cachedNpc.widthScale = 45; // HEIGH
			}
			if (id == 8709) {
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.name = "Corrupt Beast";
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 60; //WIDTH
				cachedNpc.widthScale = 60; // HEIGH
				cachedNpc.size = 1;
			}
			if (id == 7025) { //guard dog
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.heightScale = 85; //WIDTH
				cachedNpc.widthScale = 85; // HEIGH
			}

			if (id == 6716) {//prayer
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.heightScale = 65; //WIDTH
				cachedNpc.widthScale = 65; // HEIGH
				cachedNpc.combatLevel = 0;


			}
			if (id == 6723) {//healer
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.heightScale = 65; //WIDTH
				cachedNpc.widthScale = 65; // HEIGH
				cachedNpc.combatLevel = 0;

			}
			if (id == 1088) {
				cachedNpc.name = "Seren";
				cachedNpc.modelId = new int[]{38605};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.heightScale = 65; //WIDTH
				cachedNpc.widthScale = 65; // HEIGH
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8372;
				cachedNpc.walkingAnimation = 8372;
				cachedNpc.modelId = new int[]{38605};

			}
			if (id == 1089) {
				cachedNpc.name = "Lil mimic";
				cachedNpc.modelId = new int[]{37142};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.heightScale = 25; //WIDTH
				cachedNpc.widthScale = 25; // HEIGH
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8307;
				cachedNpc.walkingAnimation = 8306;
				cachedNpc.modelId = new int[]{37142};

			}
			if (id == 2120) {
				cachedNpc.name = "Shadow Ranger";
				cachedNpc.modelId = new int[]{29267};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.heightScale = 85; //WIDTH
				cachedNpc.widthScale = 85; // HEIGH
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8526;
				cachedNpc.walkingAnimation = 8527;
				cachedNpc.modelId = new int[]{29267};

			}
			if (id == 2121) {
				cachedNpc.name = "Shadow Wizard";
				cachedNpc.modelId = new int[]{29268};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.heightScale = 85; //WIDTH
				cachedNpc.widthScale = 85; // HEIGH
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8526;
				cachedNpc.walkingAnimation = 8527;
				cachedNpc.modelId = new int[]{29268};
			}
			if (id == 2122) {
				cachedNpc.name = "Shadow Warrior";
				cachedNpc.modelId = new int[]{29266};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.heightScale = 85; //WIDTH
				cachedNpc.widthScale = 85; // HEIGH
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8526;
				cachedNpc.walkingAnimation = 8527;
				cachedNpc.modelId = new int[]{29266};
			}

			if (id == 7216 || id == 6473) {//green monkey and green dog
				cachedNpc.actions = new String[5];
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
			}
			if (id == 6723 || id == 6716 || id == 8709) {
				cachedNpc.actions = new String[5];
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
			}
			if (id == 3291) {//postie pete
				cachedNpc.actions = new String[5];
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
			}
			if (id == 5738) {//imp
				cachedNpc.actions = new String[5];
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};

			}
			if (id == 5240) {//toucan
				cachedNpc.actions = new String[5];
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};

			}
			if (id == 834) {
				cachedNpc.name = "King penguin";
				cachedNpc.actions = new String[5];
				cachedNpc.combatLevel = 0;
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};

			}
			if (id == 1873) {//klik
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.standingAnimation = 3345;
				cachedNpc.walkingAnimation = 3346;

			}
			//dark pets
			if (id == 2300) {
				cachedNpc.modelId = new int[1];
				cachedNpc.name = "Dark postie pete";
				cachedNpc.modelId = new int[]{46600};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 3948;
				cachedNpc.walkingAnimation = 3947;
			}
			if (id == 2301) {
				cachedNpc.name = "Dark imp";
				cachedNpc.modelId = new int[]{46700};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 171;
				cachedNpc.walkingAnimation = 168;
			}
			if (id == 2302) {
				cachedNpc.name = "Dark toucan";
				cachedNpc.modelId = new int[]{46800, 46801};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 6772;
				cachedNpc.walkingAnimation = 6774;
			}
			if (id == 2303) {
				cachedNpc.name = "Dark king penguin";
				cachedNpc.modelId = new int[]{46200};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 5668;
				cachedNpc.walkingAnimation = 5666;
			}
			if (id == 2304) {
				cachedNpc.name = "Dark k'klik";
				cachedNpc.modelId = new int[]{46300, 46301, 46302};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 3346;
				cachedNpc.walkingAnimation = -1;
			}
			if (id == 2305) {
				cachedNpc.name = "Dark shadow warrior";
				cachedNpc.modelId = new int[]{46100};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8526;
				cachedNpc.walkingAnimation = 8527;
				cachedNpc.heightScale = 85; //WIDTH
				cachedNpc.widthScale = 85; // HEIGH
			}
			if (id == 2306) {
				cachedNpc.name = "Dark shadow archer";
				cachedNpc.modelId = new int[]{56800};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8526;
				cachedNpc.walkingAnimation = 8527;
				cachedNpc.heightScale = 85; //WIDTH
				cachedNpc.widthScale = 85; // HEIGH
			}
			if (id == 2307) {
				cachedNpc.name = "Dark shadow wizard";
				cachedNpc.modelId = new int[]{45900};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8526;
				cachedNpc.walkingAnimation = 8527;
				cachedNpc.heightScale = 85; //WIDTH
				cachedNpc.widthScale = 85; // HEIGH
			}
			if (id == 2308) {
				cachedNpc.name = "Dark healer death spawn";
				cachedNpc.modelId = new int[]{46500, 46501, 46502, 46503, 46504, 46505, 46506,};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.heightScale = 65; //WIDTH
				cachedNpc.widthScale = 65; // HEIGH
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 1539;
				cachedNpc.walkingAnimation = 1539;
			}
			if (id == 2309) {
				cachedNpc.name = "Dark holy death spawn";
				cachedNpc.modelId = new int[]{46406, 46405, 46404, 46403, 46402, 46401, 46400};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.heightScale = 65; //WIDTH
				cachedNpc.widthScale = 65; // HEIGH
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 1539;
				cachedNpc.walkingAnimation = 1539;
			}
			if (id == 2310) {
				cachedNpc.name = "Dark seren";
				cachedNpc.modelId = new int[]{46900};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.standingAnimation = 8372;
				cachedNpc.walkingAnimation = 8372;
				cachedNpc.heightScale = 65; //WIDTH
				cachedNpc.widthScale = 65; // HEIGH
			}
			if (id == 2311) {
				cachedNpc.name = "Dark corrupt beast";
				cachedNpc.modelId = new int[]{45710};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 60; //WIDTH
				cachedNpc.widthScale = 60; // HEIGH
				cachedNpc.size = 1;
				cachedNpc.standingAnimation = 5616;
				cachedNpc.walkingAnimation = 5615;
			}
			if (id == 2312) {
				cachedNpc.name = "Dark roc";
				cachedNpc.modelId = new int[]{45600, 45601};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.standingAnimation = 5021;
				cachedNpc.walkingAnimation = 5022;
				cachedNpc.size = 1;
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 30; //WIDTH
				cachedNpc.widthScale = 30; // HEIGH
			}
			if (id == 2313) {
				cachedNpc.name = "Dark kratos";
				cachedNpc.modelId = new int[]{45500, 45501, 45502};
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{"Talk-to", null, "Pick-Up", null, null};
				cachedNpc.recolorToFind = null;
				cachedNpc.recolorToReplace = null;
				cachedNpc.standingAnimation = 7017;
				cachedNpc.walkingAnimation = 7016;
				cachedNpc.size = 2;
				cachedNpc.combatLevel = 0;
				cachedNpc.heightScale = 90; //WIDTH
				cachedNpc.widthScale = 90; // HEIGH
			}
			if (id == 8027) {
				cachedNpc.name = "Vorkath";
				cachedNpc.combatLevel = 732;
				cachedNpc.modelId = new int[]{35023};
				cachedNpc.standingAnimation = 7950;
				cachedNpc.isMinimapVisible = true;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{null, null, null, null, null};
				cachedNpc.heightScale = 100;
				cachedNpc.widthScale = 100;
			}
			if (id == 8028) {
				cachedNpc.name = "Vorkath";
				cachedNpc.combatLevel = 732;
				cachedNpc.modelId = new int[]{35023};
				cachedNpc.standingAnimation = 7948;
				cachedNpc.isMinimapVisible = true;
				cachedNpc.actions = new String[5];
				cachedNpc.actions = new String[]{null, "Attack", null, null, null};
				cachedNpc.heightScale = 100;
				cachedNpc.widthScale = 100;
			}
			if (id == 7144) {
				cachedNpc.anInt75 = 0;
			}
			if (id == 963) {
				cachedNpc.anInt75 = 6;
			}
			if (id == 7145) {
				cachedNpc.anInt75 = 1;
			}
			if (id == 7146) {
				cachedNpc.anInt75 = 2;
			}
			if (cachedNpc.name != null && cachedNpc.name.toLowerCase().contains("chinchompa") && !cachedNpc.name.toLowerCase().contains("baby")) {
				cachedNpc.actions = new String[5];
			}
			npcsCached.put(cachedNpc, id);
		}
        return cachedNpc;
    }

	public int[] headIconArchiveIds;
	public short[] headIconSpriteIndex;
	public int runRotate180Animation = -1;
	public int runRotateLeftAnimation = -1;
	public int runRotateRightAnimation = -1;
	public int crawlAnimation = -1;
	public int crawlRotate180Animation = -1;
	public int runAnimation = -1;
	public int crawlRotateLeftAnimation = -1;
	public int crawlRotateRightAnimation = -1;

    public static int totalAmount;





    public int category;


	public int defaultHeadIconArchive1 = -1;
	public int rotateLeftAnimation = -1;
	public int rotateRightAnimation = -1;

	void decode(Buffer var1) {
		while(true) {
			int var2 = var1.readUnsignedByte();
			if (var2 == 0) {
				return;
			}

			this.decodeNext(var1, var2);
		}
	}

	void decodeNext(Buffer buffer, int var2) {
		int index;
		int var4;
		if (var2 == 1) {
			index = buffer.readUnsignedByte();
			modelId = new int[index];

			for(var4 = 0; var4 < index; ++var4) {
				modelId[var4] = buffer.readUShort();
			}
		} else if (var2 == 2) {
			name = buffer.readStringCp1252NullTerminated();
		} else if (var2 == 12) {
			size = buffer.readUnsignedByte();
		} else if (var2 == 13) {
			standingAnimation = buffer.readUShort();
		} else if (var2 == 14) {
			walkingAnimation = buffer.readUShort();
		} else if (var2 == 15) {
			idleRotateLeftAnimation = buffer.readUShort();
		} else if (var2 == 16) {
			idleRotateRightAnimation = buffer.readUShort();
		} else if (var2 == 17) {
			walkingAnimation = buffer.readUShort();
			rotate180Animation = buffer.readUShort();
			rotate90LeftAnimation = buffer.readUShort();
			rotate90RightAnimation = buffer.readUShort();
		} else if (var2 == 18) {
			category = buffer.readUShort();
		} else if (var2 >= 30 && var2 < 35) {
			actions[var2 - 30] = buffer.readStringCp1252NullTerminated();
			if (actions[var2 - 30].equalsIgnoreCase("Hidden")) {
				actions[var2 - 30] = null;
			}
		} else if (var2 == 40) {
			index = buffer.readUnsignedByte();
			recolorToFind = new int[index];
			recolorToReplace = new int[index];

			for(var4 = 0; var4 < index; ++var4) {
				recolorToFind[var4] = (short)buffer.readUShort();
				recolorToReplace[var4] = (short)buffer.readUShort();
			}
		} else if (var2 == 41) {
			index = buffer.readUnsignedByte();
			retextureToFind = new short[index];
			retextureToReplace = new short[index];

			for(var4 = 0; var4 < index; ++var4) {
				retextureToFind[var4] = (short)buffer.readUShort();
				retextureToReplace[var4] = (short)buffer.readUShort();
			}
		} else if (var2 == 60) {
			index = buffer.readUnsignedByte();
			chatheadModels = new int[index];

			for(var4 = 0; var4 < index; ++var4) {
				chatheadModels[var4] = buffer.readUShort();
			}

		} else if (var2 == 74) {
			stats[0] = buffer.readUnsignedShort();
		} else if (var2 == 75) {
			stats[1] = buffer.readUnsignedShort();
		} else if (var2 == 76) {
			stats[2] = buffer.readUnsignedShort();
		} else if (var2 == 77) {
			stats[3] = buffer.readUnsignedShort();
		} else if (var2 == 78) {
			stats[4] = buffer.readUnsignedShort();
		} else if (var2 == 79) {
			stats[5] = buffer.readUnsignedShort();
		} else if (var2 == 93) {
			isMinimapVisible = false;
		} else if (var2 == 95) {
			combatLevel = buffer.readUShort();
		} else if (var2 == 97) {
			widthScale = buffer.readUShort();
		} else if (var2 == 98) {
			heightScale = buffer.readUShort();
		} else if (var2 == 99) {
			hasRenderPriority = true;
		} else if (var2 == 100) {
			ambient = buffer.readSignedByte();
		} else if (var2 == 101) {
			contrast = buffer.readSignedByte();
		} else {
			int var5;
			if (var2 == 102) {
				index = buffer.readUnsignedByte();
				var4 = 0;

				for(var5 = index; var5 != 0; var5 >>= 1) {
					++var4;
				}

				headIconArchiveIds = new int[var4];
				headIconSpriteIndex = new short[var4];

				for(int var6 = 0; var6 < var4; ++var6) {
					if ((index & 1 << var6) == 0) {
						headIconArchiveIds[var6] = -1;
						headIconSpriteIndex[var6] = -1;
					} else {
						headIconArchiveIds[var6] = buffer.readNullableLargeSmart();
						headIconSpriteIndex[var6] = (short)buffer.readShortSmartSub();
					}
				}
			} else if (var2 == 103) {
				rotationSpeed = buffer.readUShort();
			} else if (var2 != 106 && var2 != 118) {
				if (var2 == 107) {
					isInteractable = false;
				} else if (var2 == 109) {
					smoothWalk = false;
				} else if (var2 == 114) {
					runAnimation = buffer.readUShort();
				} else if (var2 == 115) {
					runAnimation = buffer.readUShort();
					runRotate180Animation = buffer.readUShort();
					runRotateLeftAnimation = buffer.readUShort();
					runRotateRightAnimation = buffer.readUShort();
				} else if (var2 == 116) {
					crawlAnimation = buffer.readUShort();
				} else if (var2 == 117) {
					crawlAnimation = buffer.readUShort();
					crawlRotate180Animation = buffer.readUShort();
					crawlRotateLeftAnimation = buffer.readUShort();
					crawlRotateRightAnimation = buffer.readUShort();
				} else if (var2 == 122) {
					lowPriorityFollowerOps = true;
				} else if (var2 == 123) {
					isPet = true;
				} else if (var2 == 249) {
					params = Buffer.readStringIntParameters(buffer, params);
				}
			} else {
				transformVarbit = buffer.readUShort();
				if (transformVarbit == 65535) {
					transformVarbit = -1;
				}

				transformVarp = buffer.readUShort();
				if (transformVarp == 65535) {
					transformVarp = -1;
				}

				index = -1;
				if (var2 == 118) {
					index = buffer.readUShort();
					if (index == 65535) {
						index = -1;
					}
				}

				var4 = buffer.readUnsignedByte();
				transforms = new int[var4 + 2];

				for(var5 = 0; var5 <= var4; ++var5) {
					transforms[var5] = buffer.readUShort();
					if (transforms[var5] == 65535) {
						transforms[var5] = -1;
					}
				}

				transforms[var4 + 1] = index;
			}
		}

	}

	public boolean smoothWalk = true;
	public boolean lowPriorityFollowerOps;
	public boolean isPet;
	IterableNodeHashTable params;

	public Mesh getDialogueModel() {
		if (this.transforms != null) {
			NpcDefinition var2 = this.morph();
			return var2 == null ? null : var2.getDialogueModel();
		} else {
			return this.getModelData(this.chatheadModels);
		}
	}

	Mesh getModelData(int[] models) {

		if (models == null) {
			return null;
		} else {
			boolean cached = false;

			for (int i : models) {
				if (i != -1 && !Js5List.models.tryLoadFile(i, 0)) {
					cached = true;
				}
			}

			if (cached) {
				return null;
			} else {
				Mesh[] modelParts = new Mesh[models.length];

				for(int var6 = 0; var6 < models.length; ++var6) {
					modelParts[var6] = Mesh.getModel(models[var6]);
				}

				Mesh model;
				if (modelParts.length == 1) {
					model = modelParts[0];
					if (model == null) {
						model = new Mesh(modelParts, modelParts.length);
					}
				} else {
					model = new Mesh(modelParts, modelParts.length);
				}

				if (recolorToFind != null) {
					for (int k = 0; k < recolorToFind.length; k++)
						model.recolor((short) recolorToFind[k], (short) recolorToReplace[k]);
				}

				if (retextureToReplace != null) {
					for (int k1 = 0; k1 < retextureToReplace.length; k1++) {
						model.recolor(retextureToReplace[k1], retextureToFind[k1]);
					}
				}

				return model;
			}
		}
	}


	public NpcDefinition morph() {
        int j = -1;
        if (transformVarbit != -1 && transformVarbit <= 2113) {
            VariableBits varBit = VariableBits.lookup(transformVarbit);
            int k = varBit.baseVar;
            int l = varBit.startBit;
            int i1 = varBit.endBit;
            int j1 = Client.anIntArray1232[i1 - l];
            j = clientInstance.variousSettings[k] >> l & j1;
        } else if (transformVarp != -1)
            j = clientInstance.variousSettings[transformVarp];
        int var3;
        if (j >= 0 && j < transforms.length)
            var3 = transforms[j];
        else
            var3 = transforms[transforms.length - 1];
        return var3 == -1 ? null : get(var3);
    }

	public final Model getAnimatedModel(SequenceDefinition var1, int var2, SequenceDefinition var3, int var4) {
		if (this.transforms != null) {
			NpcDefinition var10 = this.morph();
			return var10 == null ? null : var10.getAnimatedModel(var1, var2, var3, var4);
		} else {
			long var6 = (long)this.npcId;


			Model var8 = (Model)modelCache.get(var6);
			if (var8 == null) {
				Mesh var9 = this.getModelData(this.modelId);
				if (var9 == null) {
					return null;
				}

				var8 = var9.toModel(this.ambient + 64, this.contrast * 5 + 850, -30, -50, -30);
				modelCache.put(var8, var6);
			}

			Model var11;
			if (var1 != null && var3 != null) {
				var11 = var1.applyTransformations(var8, var2, var3, var4);
			} else if (var1 != null) {
				var11 = var1.transformActorModel(var8, var2);
			} else if (var3 != null) {
				var11 = var3.transformActorModel(var8, var4);
			} else {
				var11 = var8.toSharedSequenceModel(true);
			}

			if (this.widthScale != 128 || this.heightScale != 128) {
				var11.rs$scale(this.widthScale, this.heightScale, this.widthScale);
			}

			return var11;
		}
	}

    private NpcDefinition() {
        rotate90RightAnimation = -1;
        transformVarbit = walkingAnimation;
        rotate180Animation = walkingAnimation;
        transformVarp = walkingAnimation;
        combatLevel = -1;
        anInt64 = 1834;
        walkingAnimation = -1;
        size = 1;
        anInt75 = -1;
        standingAnimation = -1;
        npcId = -1;
        rotationSpeed = 32;
        rotate90LeftAnimation = -1;
        isInteractable = true;
        heightScale = 128;
        isMinimapVisible = true;
        widthScale = 128;
        hasRenderPriority = false;
    }


    @Override
    public String toString() {
        return "NpcDefinition{" +
                "npcId=" + npcId +
                ", combatLevel=" + combatLevel +
                ", name='" + name + '\'' +
                ", actions=" + Arrays.toString(actions) +
                ", walkAnim=" + walkingAnimation +
                ", size=" + size +
                ", standAnim=" + standingAnimation +
                ", childrenIDs=" + Arrays.toString(transforms) +
                ", models=" + Arrays.toString(modelId) +
                '}';
    }

    public static void nullLoader() {
        modelCache.clear();
        npcsCached.clear();
    }

    public static void dumpList() {
        try {
            File file = new File("./temp/npc_list.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < totalAmount; i++) {
                    NpcDefinition definition = get(i);
                    if (definition != null) {
                        writer.write("npc = " + i + "\t" + definition.name + "\t" + definition.combatLevel + "\t"
                                + definition.standingAnimation + "\t" + definition.walkingAnimation + "\t");
                        writer.newLine();
                    }
                }
            }

            System.out.println("Finished dumping npc definitions.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public int[] getHeadIconArchiveIds() {
		return this.headIconArchiveIds;
	}

	public boolean headIconArchiveAvailable() {
		return this.headIconArchiveIds != null && this.headIconSpriteIndex != null;
	}

	public short[] headIconIndex() {
		return this.headIconSpriteIndex;
	}

    public static void dumpSizes() {
        try {
            File file = new File(System.getProperty("user.home") + "/Desktop/npcSizes 143.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < totalAmount; i++) {
                    NpcDefinition definition = get(i);
                    if (definition != null) {
                        writer.write(i + "	" + definition.size);
                        writer.newLine();
                    }
                }
            }

            System.out.println("Finished dumping npc definitions.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int rotate90RightAnimation;

    public int transformVarbit;
    public int rotate180Animation;
    public int transformVarp;

    public int combatLevel;
    public final int anInt64;
    public String name;
    public String actions[] = new String[5];
    public int walkingAnimation;
	public int idleRotateLeftAnimation = -1;
	public int idleRotateRightAnimation = -1;
    public int size;
    public int[] recolorToReplace;
    public static int[] streamIndices;
    public int[] chatheadModels;
    public int anInt75;
    public int[] recolorToFind;
    public short[] retextureToFind, retextureToReplace;
    public int standingAnimation;
    public int npcId;
    public int rotationSpeed;

    public static Client clientInstance;
    public int rotate90LeftAnimation;
    public boolean isInteractable;
    public int ambient;
    public int heightScale;
    public boolean isMinimapVisible;
    public int transforms[];
    public String description;
    public int widthScale;
    public int contrast;
    public boolean hasRenderPriority;
    public int[] modelId;
	public int[] stats = {1, 1, 1, 1, 1, 1};
    public static EvictingDualNodeHashTable modelCache = new EvictingDualNodeHashTable(70);


    @Override
    public HeadIcon getOverheadIcon() {
        return null;
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

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int[] getModels() {
        return new int[0];
    }

    @Override
    public String[] getActions() {
        return new String[0];
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public boolean isFollower() {
        return false;
    }

    @Override
    public boolean isInteractible() {
        return false;
    }

    @Override
    public boolean isMinimapVisible() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public int getId() {
        return npcId;
    }

    @Override
    public int getCombatLevel() {
        return 0;
    }

    @Override
    public int[] getConfigs() {
        return new int[0];
    }

    @Override
    public RSNPCComposition transform() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getRsOverheadIcon() {
        return 0;
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
}