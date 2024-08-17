package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.StringUtils;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.impl.Interfaces;

import java.util.Arrays;
import java.util.stream.Collectors;

class TaskAchievementsSubInterface extends TaskInterfaceSubBuilder {

    public TaskAchievementsSubInterface(int baseInterfaceId, TaskInterfaceSub sub) {
        super(baseInterfaceId, sub);
    }

    @Override
    public void build() {
        addInterfaceContainer(nextInterface(), 155, 178, 500);
        getSub().setEntryContainer(RSInterface.get(lastInterface()));
        child(0, 24);

        AchievementFilter[] filters = AchievementFilter.values();
        String[] filterTexts = Arrays.stream(filters).map(it -> StringUtils.fixName(it.toString().toLowerCase())).collect(Collectors.toList()).toArray(new String[filters.length]);

        dropdownMenu(nextInterface(), 171, 0, filterTexts, (optionSelected, rsInterface) -> {
            Interfaces.taskInterface.actions.setFilter(filters[optionSelected]);
        }, defaultTextDrawingAreas, 0);
        child(0, 2);
    }
}
