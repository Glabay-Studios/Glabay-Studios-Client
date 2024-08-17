package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntryType;

import java.util.List;

class TaskInterfaceSub {

    private final TaskEntryType type;
    private final String name;
    private final Sprite unselected;
    private final Sprite selected;
    private final int entryBasePointer;

    private int buttonInterfaceId;
    private int interfaceId;
    private RSInterface entryContainer;

    public TaskInterfaceSub(TaskEntryType type, String name, Sprite unselected, Sprite selected, int entryBasePointer) {
        this.type = type;
        this.name = name;
        this.unselected = unselected;
        this.selected = selected;
        this.entryBasePointer = entryBasePointer;
    }

    public void setEntries(List<RSInterface> entries) {
        entryContainer.children = new int[entries.size()];
        entryContainer.childX = new int[entries.size()];
        entryContainer.childY = new int[entries.size()];
        for (int index = 0; index < entries.size(); index++) {
            RSInterface inter = entries.get(index);
            entryContainer.children[index] = inter.id;
            entryContainer.childX[index] = 0;
            entryContainer.childY[index] = index * TaskEntryInterfaceBuilder.ENTRY_HEIGHT_SEPARATION;
        }

        // Update scrollbar
        entryContainer.scrollMax = entries.size() * TaskEntryInterfaceBuilder.ENTRY_HEIGHT_SEPARATION;
        if (entryContainer.scrollMax < entryContainer.height + 1) {
            entryContainer.scrollMax = entryContainer.height + 1;
        }
    }

    public void setButtonState(boolean on) {
        RSInterface rsInterface = RSInterface.get(buttonInterfaceId);
        rsInterface.sprite1 = on ? selected : unselected;
        rsInterface.sprite2 = rsInterface.sprite1;
    }

    public RSInterface getInterface() {
        return RSInterface.get(interfaceId);
    }

    public TaskEntryType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Sprite getUnselected() {
        return unselected;
    }

    public Sprite getSelected() {
        return selected;
    }

    public int getEntryBasePointer() {
        return entryBasePointer;
    }

    public int getButtonInterfaceId() {
        return buttonInterfaceId;
    }

    public void setButtonInterfaceId(int buttonInterfaceId) {
        this.buttonInterfaceId = buttonInterfaceId;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public RSInterface getEntryContainer() {
        return entryContainer;
    }

    public void setEntryContainer(RSInterface entryContainer) {
        this.entryContainer = entryContainer;
    }
}
