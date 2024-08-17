package com.client.script.impl;

import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntryType;
import com.client.graphics.interfaces.builder.impl.tasks.TaskInterfaceActions;
import com.client.graphics.interfaces.impl.Interfaces;
import com.client.utilities.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class _2_UpdateTasksProgress {

    public static void handle(String typeString, String title, String progress, int claimed) throws IOException {
        TaskInterfaceActions actions = Interfaces.taskInterface.actions;
        TaskEntryType type = TaskEntryType.valueOf(typeString.toUpperCase());
        List<String> progressList = JsonUtil.fromJsonString(progress, new TypeToken<List<String>>() {});
        actions.setProgress(type, title, progressList, claimed == 1);
    }

}
