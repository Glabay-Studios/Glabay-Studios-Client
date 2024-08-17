package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.Configuration;
import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskDifficulty;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntry;
import com.client.graphics.interfaces.builder.impl.tasks.model.TaskEntryType;
import com.client.model.GameItem;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

// TODO if hover has variable items we might need to move buttons to predictable ids (like 100, 101, etc)
// TODO server side: make claiming not automatic but clicking

/**
 * Achievements/Diaries interface.
 */
public class TaskInterface extends InterfaceBuilder {

    public static Sprite getSprite(String sprite) {
        return new Sprite("achievements_v2/" + sprite);
    }

    private final Sprite longTab = getSprite("long tab 0");
    private final Sprite longTabSelected = getSprite("long tab 1");
    private final Sprite shortTab = getSprite("short tab 0");
    private final Sprite shortTabSelected = getSprite("short tab 1");

    public TaskInterfaceActions actions = new TaskInterfaceActions(this);
    public TaskInterfaceSub[] subs;
    public int containerInterfaceId;
    public TaskEntryType state = TaskEntryType.ACHIEVEMENTS; // Default to achievements for now

    public static boolean TEST_MODE = false && Configuration.developerMode;

    public TaskInterface() {
        super(24_500); // 1400 free
    }

    @Override
    public void build() {
        init(); // reset interface/child id pointers

        subs = new TaskInterfaceSub[] {
                new TaskInterfaceSub(TaskEntryType.DIARIES, "Diaries", shortTab, shortTabSelected, getBaseInterfaceId() + 100), // 24_600
                new TaskInterfaceSub(TaskEntryType.ACHIEVEMENTS, "Achievements", longTab, longTabSelected, getBaseInterfaceId() + 400) // 24_900
        };

        int x = 5;
        int y = 3;
        for (TaskInterfaceSub state : subs) {
            boolean selected = this.state == state.getType();
            Sprite sprite = selected ? state.getSelected() : state.getUnselected();
            state.setButtonInterfaceId(getCurrentInterfaceId());
            addButton(nextInterface(), sprite, state.getName(), 4);
            child(x, y);
            addText(nextInterface(), 0, RSInterface.DEFAULT_TEXT_COLOR, true, state.getName());
            child(x + (sprite.myWidth / 2), y + 3);
            x += sprite.myWidth - 1;
        }

        // Contains the achievements or diaries interface, which in turn contains the dropdown menu and entries
        containerInterfaceId = getCurrentInterfaceId();
        RSInterface baseContainer = RSInterface.addInterfaceContainer(nextInterface(), 255, 255, 0);
        RSInterface.setChildren(1, baseContainer);
        child(10, 24);

        // Achievements
        TaskInterfaceSub achievements = byType(TaskEntryType.ACHIEVEMENTS);
        achievements.setInterfaceId(getCurrentInterfaceId());
        TaskAchievementsSubInterface achievementsInterface = new TaskAchievementsSubInterface(nextInterface(), achievements);
        achievementsInterface.build();
        setNextInterfaceId(achievementsInterface.getCurrentInterfaceId()); // This is important because it uses our nextInterface() as the base id call

        // Diaries
        TaskInterfaceSub diaries = byType(TaskEntryType.DIARIES);
        diaries.setInterfaceId(getCurrentInterfaceId());
        TaskDiariesSubInterface diariesInterface = new TaskDiariesSubInterface(nextInterface(), diaries);
        diariesInterface.build();
        setNextInterfaceId(diariesInterface.getCurrentInterfaceId());

        // Set open
        actions.setOpen(TaskEntryType.ACHIEVEMENTS);

        // Set new button clicking at the end
        getRoot().setNewButtonClicking();

        // TODO remove below after testing

        if (TEST_MODE) {
            List<TaskEntry> diariesList = Lists.newArrayList();
            List<TaskEntry> achievementsList = Lists.newArrayList();

            for (int index = 0; index <= 200; index += 20) {
                String title = "Test Task Entry " + index / 20;
                StringJoiner description = new StringJoiner("\\n");
                StringJoiner extraRewards = new StringJoiner("\\n");
                List<GameItem> items = Lists.newArrayList();

                description.add("Test description");
                extraRewards.add("Extra rewards");

                for (int i = 0; i < index / 20; i++) {
                    if (i >= 8)
                        break;
                    description.add("Description extension");
                    extraRewards.add("Extra rewards extension");
                    items.add(new GameItem(4151 + (i * 2), 1));
                }

                int progress = Math.min(index, 100);
                boolean skipItems = items.size() >= 4 && ((index / 20) % 2 == 0);

                achievementsList.add(
                        new TaskEntry(
                                title,
                                description.toString(),
                                false,
                                TaskDifficulty.values()[RandomUtils.nextInt(1, TaskDifficulty.values().length)],
                                skipItems ? null : items,
                                extraRewards.toString(),
                                Lists.newArrayList(progress + "/100")
                        )
                );

                diariesList.add(
                        new TaskEntry(
                                title,
                                description.toString(),
                                skipItems ? null : items,
                                extraRewards.toString(),
                                Lists.newArrayList(progress + "/100", progress + "/100", progress + "/100", progress + "/100")
                        )
                );
            }

            actions.setAchievements(achievementsList);
            actions.setDiaries(diariesList);
        }
    }

    public TaskInterfaceSub byType(TaskEntryType type) {
        Optional<TaskInterfaceSub> state = Arrays.stream(subs).filter(it -> it.getType() == type).findFirst();
        if (!state.isPresent()) {
            throw new IllegalStateException("No type found: " + type);
        }
        return state.get();
    }
}
