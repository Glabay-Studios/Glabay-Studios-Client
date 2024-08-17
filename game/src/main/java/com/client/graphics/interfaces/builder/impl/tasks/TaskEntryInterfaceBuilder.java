package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.Sprite;
import com.client.StringUtils;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntry;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntryType;

public class TaskEntryInterfaceBuilder extends InterfaceBuilder {

    /**
     * Height of an entry.
     */
    public static final int ENTRY_HEIGHT_SEPARATION = 27 /* size */ + 1 /* buffer */;
    public static int achievementsInterfaceSize;
    private static Sprite background;
    private static Sprite backgroundHover;
    private final TaskEntry taskEntry;
    private final TaskEntryType type;

    public TaskEntryInterfaceBuilder(TaskEntryType type, int baseInterfaceId, TaskEntry taskEntry) {
        super(baseInterfaceId);
        this.taskEntry = taskEntry;
        this.type = type;
        getRoot().width = 200;
        getRoot().height = ENTRY_HEIGHT_SEPARATION;
    }

    @Override
    public void build() {
        if (background == null) {
            background = TaskInterface.getSprite("orange_outline_box");
            backgroundHover = TaskInterface.getSprite("orange_outline_box_hover");
            background.setTransparency(255, 255, 255);
        }

        try {
            int startInterfaceId = getCurrentInterfaceId();

            // Progress calculation
            double completionPercentage = taskEntry.getCompletionPercentage();
            int color = RSInterface.getRgbProgressColor(completionPercentage);
            boolean claimable = taskEntry.isClaimable();

            // Button
            String buttonText = claimable ? "Claim: " : "Select: ";
            RSInterface button = addButton(nextInterface(), background, buttonText + taskEntry.getTitle(), 4);
            child(0, 0);

            // Hover
            button.setHover(getCurrentInterfaceId(), TaskInterface.TEST_MODE ? 0 : 700);
            TaskEntryHoverInterfaceBuilder hover = new TaskEntryHoverInterfaceBuilder(getCurrentInterfaceId(), 150, taskEntry);
            hover.build();
            setNextInterfaceId(hover.getCurrentInterfaceId());

            // Title
            RSInterface title = addText(nextInterface(), 0, color, false, taskEntry.getTitle());
            child(4, 3);

            // Progress text
            String progressText = claimable ? "<clan=6>Claim " : taskEntry.getProgressSlash(true);
            RSInterface progress = addLeftText(nextInterface(), 0, color, progressText);
            child(148, 3);

            // Progress bars
            int barWidth = 143 / taskEntry.getProgress().size();
            for (int index = 0; index < taskEntry.getProgress().size(); index++) {
                double difficultyCompletionPercentage = taskEntry.getCompletionPercentage(index);
                addProgressBar2021(nextInterface(), barWidth, 6, 0);
                get(lastInterface()).progressBar2021Percentage = difficultyCompletionPercentage;
                child(4 + (index * barWidth), 16);
            }

            getRoot().setNewButtonClicking();

            // Set change stuff on hover
            button.setHoverAction(() -> {
                if (button.isHovered) {
                    button.sprite1 = button.sprite2 = backgroundHover;
                    button.drawsTransparent = true;
                    button.opacity = 100;
                    title.textColor = RSInterface.WHITE_COLOR;
                    progress.textColor = RSInterface.WHITE_COLOR;
                } else {
                    button.sprite1 = button.sprite2 = background;
                    button.drawsTransparent = false;
                    title.textColor = color;
                    progress.textColor = color;
                }
            });

            if (type == TaskEntryType.ACHIEVEMENTS && achievementsInterfaceSize == 0) {
                achievementsInterfaceSize = getCurrentInterfaceId() - startInterfaceId + 1 /* container */;
            }
        } catch (Exception e) {
            System.err.println("Error in entry: " + taskEntry);
            e.printStackTrace();
        }
    }
}
