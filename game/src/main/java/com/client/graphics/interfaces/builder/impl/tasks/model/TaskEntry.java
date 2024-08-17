package com.client.graphics.interfaces.builder.impl.tasks.model;

import com.client.StringUtils;
import com.client.graphics.interfaces.builder.impl.tasks.AchievementFilter;
import com.client.model.GameItem;
import com.client.utilities.Misc;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

public class TaskEntry {

    private static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");

    private final String title;
    private final String description;
    private boolean claimed;
    private final TaskDifficulty taskDifficulty;
    private final List<GameItem> rewards;
    private final String extraRewards;
    private List<String> progress;

    /**
     * Constructor for diaries.
     */
    public TaskEntry(String title, String description, List<GameItem> rewards, String extraRewards, List<String> progress) {
        this(title, description, false, TaskDifficulty.NONE, rewards, extraRewards, progress);
    }

    /**
     * Constructor for achievements.
     */
    public TaskEntry(String title, String description, boolean claimed, TaskDifficulty taskDifficulty,
                     List<GameItem> rewards, String extraRewards, List<String> progress) {
        this.title = title;
        this.description = description;
        this.claimed = claimed;
        this.taskDifficulty = taskDifficulty;
        this.rewards = rewards;
        this.extraRewards = extraRewards;
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "TaskEntry{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", claimed=" + claimed +
                ", taskDifficulty=" + taskDifficulty +
                ", rewards=" + rewards +
                ", extraRewards='" + extraRewards + '\'' +
                ", progress=" + progress +
                '}';
    }

    public boolean isFiltered(AchievementFilter filter) {
        switch (filter) {
            case STARTER:
                return taskDifficulty != TaskDifficulty.STARTER;
            case BEGINNER:
                return taskDifficulty != TaskDifficulty.BEGINNER;
            case INTERMEDIATE:
                return taskDifficulty != TaskDifficulty.INTERMEDIATE;
            case EXPERT:
                return taskDifficulty != TaskDifficulty.EXPERT;
            case LEGENDARY:
                return taskDifficulty != TaskDifficulty.LEGENDARY;
            case ALL:
                return false;
            case COMPLETE:
                return getCurrentTicks() != getTicksForCompletion();
            case INCOMPLETE:
                return getCurrentTicks() == getTicksForCompletion();
            case CLAIMABLE:
                return !isClaimable();
            default:
                throw new IllegalStateException("No option for filter: " + filter);
        }
    }

    private List<String[]> getCompletionPercentages() {
        return getProgress().stream().map(it -> it.split("/")).collect(Collectors.toList());
    }

    public int getCurrentTicks() {
        return getCompletionPercentages().stream().mapToInt(it -> Integer.parseInt(it[0])).sum();
    }

    public int getTicksForCompletion() {
        return getCompletionPercentages().stream().mapToInt(it -> Integer.parseInt(it[1])).sum();
    }

    public String getProgressSlash(boolean abbrev) {
        if (abbrev) {
            return Misc.format(getCurrentTicks()) + "/" + Misc.format(getTicksForCompletion());
        } else {
            return (StringUtils.insertCommas(getCurrentTicks()) + "/" + StringUtils.insertCommas(getTicksForCompletion()));
        }
    }

    public boolean isClaimable() {
        return !isClaimed()
                && getProgress().size() == 1 // Only achievements are claimable, cheap hax
                && getCompletionPercentage() == 1.0;
    }

    /**
     * Gets the completion percentage, 0.0 to 1.0 inclusive.
     */
    public double getCompletionPercentage() {
        return (double) getCurrentTicks() / (double) getTicksForCompletion();
    }

    public double getCompletionPercentage(int index) {
        String[] data = getProgress().get(index).split("/");
        return (double) Integer.parseInt(data[0]) / (double) Integer.parseInt(data[1]) ;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public TaskDifficulty getTaskDifficulty() {
        return taskDifficulty;
    }

    public List<GameItem> getRewards() {
        return rewards;
    }

    public String getExtraRewards() {
        return extraRewards;
    }

    public List<String> getProgress() {
        return progress;
    }

    public void setProgress(List<String> progress) {
        this.progress = progress;
    }
}
