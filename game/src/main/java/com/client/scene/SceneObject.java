package com.client.scene;

import com.client.Client;
import com.client.Renderable;
import com.client.definitions.ObjectDefinition;
import com.client.definitions.anim.SequenceDefinition;
import com.client.entity.model.Model;
import net.runelite.api.Animation;
import net.runelite.api.DynamicObject;
import net.runelite.api.ObjectID;
import net.runelite.api.events.DynamicObjectAnimationChanged;

public final class SceneObject extends Renderable implements DynamicObject {

    private int frame;
    private final int[] configs;
    private final int varbit_id;
    private final int config_id;
    private final int sceneY;
    private final int level;
    private final int sceneX;

    private SequenceDefinition sequenceDefinition;
    private int cycle_delay;
    private final int object_id;
    private final int type;
    private final int orientation;



    public Model getRotatedModel() {
        try
        {
            // reset frame because it may have been set from the constructor
            // it should be set again inside the getModel method
            int animFrame = getAnimFrame();
            if (animFrame < 0)
            {
                setAnimFrame((animFrame ^ Integer.MIN_VALUE) & 0xFFFF);
            }
            int j = -1;

            if (this.sequenceDefinition != null) {
                int var1 = Client.loopCycle - this.cycle_delay;
                if (var1 > 100 && this.sequenceDefinition.frameCount > 0) {
                    var1 = 100;
                }

                int var2;
                if (this.sequenceDefinition.isCachedModelIdSet()) {
                    var2 = this.sequenceDefinition.getSkeletalLength();
                    this.frame += var1;
                    var1 = 0;
                    if (this.frame >= var2) {
                        this.frame = var2 - this.sequenceDefinition.frameCount;
                        if (this.frame < 0 || this.frame > var2) {
                            this.sequenceDefinition = null;
                        }
                    }
                } else {
                    label78: {
                        do {
                            do {
                                if (var1 <= this.sequenceDefinition.delays[this.frame]) {
                                    break label78;
                                }

                                var1 -= this.sequenceDefinition.delays[this.frame];
                                ++this.frame;
                            } while(this.frame < this.sequenceDefinition.primaryFrameIds.length);

                            this.frame -= this.sequenceDefinition.frameCount;
                        } while(this.frame >= 0 && this.frame < this.sequenceDefinition.primaryFrameIds.length);

                        this.sequenceDefinition = null;
                    }
                }

                this.cycle_delay = Client.loopCycle - var1;
            }

            ObjectDefinition objectDefinition = ObjectDefinition.lookup(object_id);
            if (objectDefinition.configs != null) {
                objectDefinition = objectDefinition.method580();
            }
            if (objectDefinition == null) {
                return null;
            } else {
                int sizeX;
                int sizeY;
                // Orientation
                if (this.orientation != 1 && this.orientation != 3) {
                    sizeX = objectDefinition.sizeX;
                    sizeY = objectDefinition.sizeY;
                } else {
                    sizeX = objectDefinition.sizeY;
                    sizeY = objectDefinition.sizeX;
                }

                int primaryX = (sizeX >> 1) + this.sceneX;
                int secondaryX = (sizeX + 1 >> 1) + this.sceneX;
                int primaryY = (sizeY >> 1) + this.sceneY;
                int secondaryY = (sizeY + 1 >> 1) + this.sceneY;
                int[][] var8 = Client.instance.getTileHeights()[this.level];
                int var9 = var8[secondaryX][secondaryY] + var8[primaryX][secondaryY] + var8[secondaryX][primaryY] + var8[primaryX][primaryY] >> 2;
                int var10 = (this.sceneX << 7) + (sizeX << 6);
                int var11 = (this.sceneY << 7) + (sizeY << 6);
                return objectDefinition.getModelDynamic(this.type, this.orientation, var8, var10, var9, var11, this.sequenceDefinition, this.frame);
            }
        }
        finally
        {
            int animFrame = getAnimFrame();
            if (animFrame < 0)
            {
                setAnimFrame((animFrame ^ Integer.MIN_VALUE) & 0xFFFF);
            }
        }

    }

    public void setAnimFrame(int frame) {
        this.frame = frame;
    }

    public SceneObject(final int objectId, final int orientation, final int type, int level, int sceneX, int sceneY, final int animationId, final boolean animating, Renderable var9) {
        object_id = objectId;
        this.type = type;
        this.orientation = orientation;
        this.sceneY = sceneY;
        this.level = level;
        this.sceneX = sceneX;
        ObjectDefinition def = ObjectDefinition.lookup(object_id);
        this.varbit_id = def.varbitID;
        this.config_id = def.varpID;
        this.configs = def.configs;

        if (animationId != -1) {

            DynamicObjectAnimationChanged dynamicObjectAnimationChanged = new DynamicObjectAnimationChanged();
            dynamicObjectAnimationChanged.setObject(objectId);
            dynamicObjectAnimationChanged.setAnimation(animationId);
            Client.instance.getCallbacks().post(dynamicObjectAnimationChanged);
            onAnimCycleCountChanged();

            this.sequenceDefinition = SequenceDefinition.get(animationId);
            this.frame = 0;
            this.cycle_delay = Client.loopCycle - 1;
            if (this.sequenceDefinition.delayType == 0 && var9 != null && var9 instanceof SceneObject) {
                SceneObject var10 = (SceneObject)var9;
                if (var10.sequenceDefinition == this.sequenceDefinition) {
                    this.frame = var10.frame;
                    this.cycle_delay = var10.cycle_delay;
                    return;
                }
            }

            if (animating && this.sequenceDefinition.frameCount != -1) {
                if (!this.sequenceDefinition.isCachedModelIdSet()) {
                    this.frame = (int)(Math.random() * (double)this.sequenceDefinition.primaryFrameIds.length);
                    this.cycle_delay -= (int)(Math.random() * (double)this.sequenceDefinition.delays[this.frame]);
                } else {
                    this.frame = (int)(Math.random() * (double)this.sequenceDefinition.getSkeletalLength());
                }
            }
        }
    }

    public void onAnimCycleCountChanged()
    {
        if (Client.instance.isInterpolateObjectAnimations() && this.getId() != ObjectID.WATER_WHEEL_26671)
        {
            // sets the packed anim frame with the frame cycle
            int objectFrameCycle = Client.instance.getGameCycle() - getAnimCycle();
            setAnimFrame(Integer.MIN_VALUE | objectFrameCycle << 16 | getAnimFrame());
        }
    }

    public int getId() {
        return object_id;
    }

    @Override
    public Animation getAnimation() {
        return sequenceDefinition;
    }

    @Override
    public int getAnimationID() {
        return sequenceDefinition.id;
    }

    @Override
    public int getAnimFrame() {
        return frame;
    }

    @Override
    public int getAnimCycle() {
        return cycle_delay;
    }

}
