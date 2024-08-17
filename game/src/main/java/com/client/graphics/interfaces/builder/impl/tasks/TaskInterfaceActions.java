package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntry;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntryType;
import com.client.sign.Signlink;
import com.client.utilities.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.util.AssetUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TaskInterfaceActions {

    private final TaskInterface inter;
    private List<TaskEntry> achievements = Lists.newArrayList();
    private List<TaskEntry> diaries = Lists.newArrayList();
    private AchievementFilter filter = AchievementFilter.ALL;

    public TaskInterfaceActions(TaskInterface inter) {
        this.inter = inter;
    }

    public void loadAchievements() {
        try {
            achievements = JsonUtil.fromJson(AssetUtils.INSTANCE.getResource("achievements.json").openStream(),
                    new TypeToken<List<TaskEntry>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOpen(TaskEntryType type) {
        TaskInterfaceSub sub = inter.byType(type);
        inter.state = sub.getType();
        RSInterface.get(inter.containerInterfaceId).children[0] = sub.getInterfaceId();
        Arrays.stream(inter.subs).forEach(it -> it.setButtonState(inter.state == it.getType()));
    }

    public void onButtonClick(int buttonId) {
        if (buttonId == 24_501) {
            setOpen(TaskEntryType.DIARIES);
        } else if (buttonId == 24_503) {
            setOpen(TaskEntryType.ACHIEVEMENTS);
        }
    }

    public void setFilter(AchievementFilter filter) {
        if (this.filter != filter) {
            this.filter = filter;
            addEntries(TaskEntryType.ACHIEVEMENTS, getAchievements());
            inter.byType(TaskEntryType.ACHIEVEMENTS).getEntryContainer().scrollPosition = 0;
        }
    }

    public void setAchievements(List<TaskEntry> entries) {
        achievements = entries;
        addEntries(TaskEntryType.ACHIEVEMENTS, getAchievements());
    }

    public List<TaskEntry> getAchievements() {
        return achievements;
    }

    public void setDiaries(List<TaskEntry> entries) {
        diaries = entries;
        addEntries(TaskEntryType.DIARIES, entries);
    }

    public List<TaskEntry> getDiaries() {
        return diaries;
    }

    public void setProgress(TaskEntryType type, String title, List<String> progress, boolean claimed) {
        boolean achievements = type == TaskEntryType.ACHIEVEMENTS;
        List<TaskEntry> tasks = achievements ? getAchievements() : getDiaries();
        tasks.stream().filter(it -> it.getTitle().equals(title)).forEach(it -> {
            it.setProgress(progress);
            it.setClaimed(claimed);
        });
        if (achievements) {
            setAchievements(getAchievements());
        } else {
            setDiaries(getDiaries());
        }
    }

    /**
     * Redraws the entries.
     */
    private void addEntries(TaskEntryType type, List<TaskEntry> entries) {
        TaskInterfaceSub sub = inter.byType(type);
        int interfacePointer = sub.getEntryBasePointer();
        List<RSInterface> entryInterfaces = Lists.newArrayList();

        for (TaskEntry entry : entries) {
            if (entry.isFiltered(filter)) {
                interfacePointer += TaskEntryInterfaceBuilder.achievementsInterfaceSize; // Skip so buttons stay the same
            } else {
                TaskEntryInterfaceBuilder builder = new TaskEntryInterfaceBuilder(type, interfacePointer, entry);
                builder.build();
                entryInterfaces.add(RSInterface.get(interfacePointer));
                int size = builder.getCurrentInterfaceId() - interfacePointer;
                if (type == TaskEntryType.ACHIEVEMENTS && size != TaskEntryInterfaceBuilder.achievementsInterfaceSize)
                    System.err.println("Incorrect interface size"
                            + " id=" + interfacePointer
                            + ", size=" + size
                            + ", expected=" + TaskEntryInterfaceBuilder.achievementsInterfaceSize);
                interfacePointer = builder.getCurrentInterfaceId();
            }
        }

        sub.setEntries(entryInterfaces);
    }
}