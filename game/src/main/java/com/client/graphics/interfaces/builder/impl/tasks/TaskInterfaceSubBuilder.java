package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.graphics.interfaces.builder.InterfaceBuilder;

public abstract class TaskInterfaceSubBuilder extends InterfaceBuilder {

    private final TaskInterfaceSub sub;

    public TaskInterfaceSubBuilder(int baseInterfaceId, TaskInterfaceSub sub) {
        super(baseInterfaceId);
        this.sub = sub;
    }

    public TaskInterfaceSub getSub() {
        return sub;
    }
}
