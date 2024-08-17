package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.StringUtils;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskDifficulty;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntry;
import com.client.model.GameItem;

public class TaskEntryHoverInterfaceBuilder extends InterfaceBuilder {

    private final int width;
    private final TaskEntry taskEntry;

    private int x;
    private int y;
    private int centerX;

    public TaskEntryHoverInterfaceBuilder(int baseInterfaceId, int width, TaskEntry taskEntry) {
        super(baseInterfaceId);
        this.width = width;
        this.taskEntry = taskEntry;
    }

    @Override
    public void build() {

        getRoot().type = 0;
        getRoot().height = 150;
        getRoot().width = width;

        x = 12;
        centerX = x + 75;

        // Box
        RSInterface box = addBox(nextInterface(), 0, 0xFFFFA0, 200, getRoot().width, getRoot().height);
        child(x, 0);

        y = 3;

        // Difficulty string
        String difficulty = taskEntry.getTaskDifficulty() == TaskDifficulty.NONE ? "" : StringUtils.fixName(taskEntry.getTaskDifficulty().toString().toLowerCase());
        addNewText(nextInterface(), difficulty, 0, 0, false, false);
        child(x + 2, y);

        if (difficulty.length() != 0) {
            y += getLineHeight(difficulty);
        }

        // Title
        addNewText(nextInterface(), taskEntry.getTitle(), 0, 0, true, false);
        child(centerX, y);
        int lineHeight = getLineHeight(taskEntry.getTitle());
        y += lineHeight;
        if (lineHeight > 0)
            y += 4; // Separator

        // Description
        addNewText(nextInterface(), taskEntry.getDescription(), 0, 0, true, false);
        child(centerX, y);
        lineHeight = getLineHeight(taskEntry.getDescription());
        y += lineHeight;
        if (lineHeight > 0)
            y += 6; // Separator

        // Box
//        int rewardsY = y;
//        RSInterface rewardsBox = addBox(nextInterface(), 0, 0, 256, getRoot().width - 8, getRoot().height);
//        child(x + 4, y);
//        y += 4;

        // Rewards header

        String rewards = taskEntry.getExtraRewards().length() != 0 || taskEntry.getRewards() != null ? "Rewards" : "";
        addNewText(nextInterface(), rewards, 0, 0, true, false);
        child(centerX, y);
        y += getLineHeight(rewards);

        createRewardItemsContainer();

        // Extra rewards item container
        String extraRewards = taskEntry.getExtraRewards() == null ? "" : taskEntry.getExtraRewards();
        addNewText(nextInterface(), extraRewards, 0, 0, true, false);
        child(centerX, y);
        y += getLineHeight(taskEntry.getExtraRewards());

        // Set container and box height
        y += 4; // Separator
        box.height = y;
        getRoot().height = y;
        //rewardsBox.height = y - rewardsY - 4;
    }

    private void createRewardItemsContainer() {
        int itemCount = taskEntry.getRewards() == null ? 0 : taskEntry.getRewards().size();
        int itemsWidth = Math.min(itemCount, 4);
        int itemsHeight = Math.max((int) Math.ceil(itemCount / 4.0), 1);
        RSInterface items = addItemContainer(nextInterface(), itemsWidth, itemsHeight, 0, 0, false);

        int itemsDrawWidth = Math.min(4, itemCount) * 32;
        int itemsX = centerX - (itemsDrawWidth / 2);
        child(itemsX, y);

        if (itemCount > 0) {
            for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                GameItem item = taskEntry.getRewards().get(itemIndex);
                items.inventoryItemId[itemIndex] = item.getId() + 1;
                items.inventoryAmounts[itemIndex] = item.getAmount();
            }

            y += 32 * items.height;
            y += 4; // Separator
        }
    }

    private int getLineHeight(String text) {
        if (text == null || text.length() == 0) return 0;
        int lines = 1;
        while (true) {
            int nlIndex = text.indexOf("\\n");
            if (nlIndex == -1)
                break;
            lines++;
            text = text.substring(nlIndex + 3);
        }
        return lines * 10;
    }
}
