package com.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SkillLevel {

    public static String getLevelsAsString(SkillLevel...skillArray) {
        String string = "";
        for (int i = 0; i < skillArray.length; i++) {
            string += skillArray[i].getFormattedString();
            if (i != skillArray.length - 1) {
                string += ", ";
            }
        }
        return string;
    }

    private final Skill skill;
    private final int level;

    public SkillLevel(Skill skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    private SkillLevel() {
        skill = null;
        level = 0;
    }

    /**
     * Gets the SkillLevel as a string (i.e. 99 attack, 99 strength);
     * @return
     *      the SkillLevel as a string
     */
    @JsonIgnore
    public String getFormattedString() {
        return level + " " + skill.toString();
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }
}
