package com.client.script.impl;

import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntry;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntryType;
import com.client.graphics.interfaces.builder.impl.tasks.TaskInterfaceActions;
import com.client.graphics.interfaces.impl.Interfaces;
import com.client.utilities.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class _1_UpdateTasksInterface {

    public static void handle(String typeString, String data) throws IOException {
        TaskInterfaceActions actions = Interfaces.taskInterface.actions;
        TaskEntryType type = TaskEntryType.valueOf(typeString.toUpperCase());

        boolean reset = data.equals("reset");
        boolean draw = data.equals("draw");
//        if (type == TaskEntryType.ACHIEVEMENTS) {
//            if (reset) {
//                actions.getAchievements().clear();
//            } else if (draw) {
//                actions.setAchievements(actions.getAchievements());
//            } else {
//                actions.setAchievements(getEntry(data));
//                //actions.getAchievements().add(getEntry(data));
//            }
//        } else {
            // Diaries only now
            if (reset) {
                actions.getDiaries().clear();
            } else if (draw) {
                actions.setDiaries(actions.getDiaries());
            } else {
                actions.setDiaries(getEntry(data));
            }
//        }
    }

    private static List<TaskEntry> getEntry(String data) throws IOException {
        return JsonUtil.fromJsonString(data, new TypeToken<List<TaskEntry>>() {});
    }
}
