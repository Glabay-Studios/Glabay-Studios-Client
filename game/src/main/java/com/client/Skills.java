package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Skills {

	public static final int SKILLS_COUNT = 25;
	public static String[] SKILL_NAMES = {
			"-1", "Attack", "Strength", "Defence", "Ranged",
			"Prayer", "Magic", "Runecrafting", "Hunter", "Hitpoints", "Agility",
			"Herblore", "Thieving", "Crafting", "Fletching", "Slayer", "-1", "-1",
			"Mining", "Smithing", "Fishing", "Cooking", "Firemaking", "Woodcutting",
			"Farming"
	};

	public static final String[] SKILL_NAMES_ORDER = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged",
		"Prayer", "Magic", "Cooking","Woodcutting","Fletching",
		"Fishing","Firemaking","Crafting", "Smithing","Mining",
		"Herblore","Agility","Thieving","Slayer","Farming","Runecraft",
		"Hunter","Construction"};

	public static final boolean[] SKILLS_ENABLED = {
			true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true,
			true, true, false, false, false };




}
