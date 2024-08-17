package com.client.model;

import java.util.stream.Stream;

import com.client.StringUtils;

public enum Skill {
	ATTACK(0, 1),
	DEFENCE(1, 1),
	STRENGTH(2, 1),
	HITPOINTS(3, 1),
	RANGED(4, 1),
	PRAYER(5, 50),
	MAGIC(6, 1),
	COOKING(7, 40),
	WOODCUTTING(8, 55),
	FLETCHING(9, 40),
	FISHING(10, 55),
	FIREMAKING(11, 50),
	CRAFTING(12, 45),
	SMITHING(13, 70),
	MINING(14, 55),
	HERBLORE(15, 60),
	AGILITY(16, 70),
	THIEVING(17, 80),
	SLAYER(18, 70),
	FARMING(19, 35),
	RUNECRAFTING(20, 75),
	HUNTER(21, 60);

	public static Skill forId(int id) {
		return Stream.of(values()).filter(s -> s.id == id).findFirst().orElse(null);
	}

	public static Skill[] getCombatSkills() {
		return Stream.of(values()).filter(skill -> skill.getId() <= 6).toArray(Skill[]::new);
	}

	public static Skill[] getNonCombatSkills() {
		return Stream.of(values()).filter(skill -> skill.getId() > 6).toArray(Skill[]::new);
	}

	public static Skill[] getNormalizingSkills() {
		return Stream.of(values()).filter(skill -> skill != PRAYER && skill != HITPOINTS).toArray(Skill[]::new);
	}

	public static Skill[] getAllButHitpoints() {
		return Stream.of(values()).filter(skill -> skill != HITPOINTS).toArray(Skill[]::new);
	}

	public static final int MAXIMUM_SKILL_ID = 21;

	public static Stream<Skill> stream() {
		return Stream.of(values());
	}

	public static int length() {
		return values().length;
	}

	private final int id;
	private final int experienceRate;

	Skill(int id) {
		this(id, 1);
	}

	Skill(int id, int experienceRate) {
		this.id = id;
		this.experienceRate = experienceRate;
	}

	public int getId() {
		return id;
	}

	public double getExperienceRate() {
		return experienceRate;
	}

	@Override
	public String toString() {
		String name = name().toLowerCase();
		return StringUtils.fixName(name);
	}
}

