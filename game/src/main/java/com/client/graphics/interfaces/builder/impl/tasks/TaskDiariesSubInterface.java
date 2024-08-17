package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;

class TaskDiariesSubInterface extends TaskInterfaceSubBuilder {

    public TaskDiariesSubInterface(int baseInterfaceId, TaskInterfaceSub sub) {
        super(baseInterfaceId, sub);
    }

    @Override
    public void build() {
        addInterfaceContainer(nextInterface(), 155, 202, 500);
        getSub().setEntryContainer(RSInterface.get(lastInterface()));
        child(0, 0);
    }
}
