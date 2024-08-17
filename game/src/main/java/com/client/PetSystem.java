package com.client;

import com.client.definitions.NpcDefinition;
import com.client.definitions.anim.SequenceDefinition;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Near Reality
 * @author Nucleon
 * @version 1.2
 *
 */


public class PetSystem {


    /**
     * @param entity Use EntityDef.forID() to insert the ID of the Pet.
     */
    public PetSystem(NpcDefinition entity) {
        this.modelArray = entity.modelId;
        this.modelArrayLength = entity.modelId.length;
        this.primaryModel = entity.modelId[0];
        if (entity.modelId.length > 1) {
            this.secondaryModel = entity.modelId[1];
        } else {
            this.secondaryModel = entity.modelId[0];
        }
        this.name = entity.name;
        this.description = entity.description.getBytes();
        animation = entity.standingAnimation;
        if (animationFrame >= SequenceDefinition.get(animation).delays.length - 1) {
            animationFrame = 0;
        }
        animationDelay = SequenceDefinition.get(animation).delays[animationFrame];
    }

    public static void petAnimationStep() {
        if (updatePetAnimations) {
            return;
        }
        animationFrame++;
        if (animationFrame >= SequenceDefinition.get(animation).primaryFrameIds.length) {
            animationFrame = 0;
        }
    }


    public static void updateAnimations() {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                isPetAnimationRunning = true;
                petAnimationStep();
            }
        }, 0, (animationDelay == 0) ? 30 : animationDelay * 30 , TimeUnit.MILLISECONDS);


    }


    public int getAnimationDelay() {
        return animationDelay;
    }


    public int getPrimaryModel() {
        return primaryModel;
    }


    public int getAnimation() {
        return animation;
    }


    public String getName() {
        return name;
    }


    public int getAnimationFrame() {
        return animationFrame;
    }


    public byte[] getDescription() {
        return description;
    }


    public int getSecondaryModel() {
        return secondaryModel;
    }

    public int getModelArrayLength() {
        return modelArrayLength;
    }
    public int[] getModelArray() {
        return modelArray;
    }
    public int getPetSelected() {
        return petSelected;
    }
    public void setPetSelected(int petID) {
        petSelected = petID;
    }
    /**
     * The container where the models are loaded from.
     */
    private final int[] modelArray;
    /**
     * The length of the model container.
     */
    private final int modelArrayLength;
    /**
     * The first model in the model array.
     */
    private final int primaryModel;
    /**
     * The second model in the model array.
     */
    private final int secondaryModel;
    /**
     * The name of the pet.
     */
    private final String name;
    /**
     * The description of the pet.
     */
    private final byte[] description;
    /**
     * The default animation of the pet.
     */
    private static int animation;
    /**
     * The default animation delay of the animation frame's.
     */
    private static int animationDelay;
    /**
     * The current index in the animation array.
     */
    public static int animationFrame;
    /**
     * This boolean will prevent the pet animation from looping.
     */
    public static boolean updatePetAnimations = false;
    /**
     * Checks if the pet animation is currently lopping.
     */
    public static boolean isPetAnimationRunning = false;
    /**
     * The current pet your player has following you.
     */
    public static int petSelected = 0;


}