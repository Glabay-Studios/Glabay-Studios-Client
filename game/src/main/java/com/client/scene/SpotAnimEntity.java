package com.client.scene;

import com.client.Client;
import com.client.Renderable;
import com.client.definitions.anim.SequenceDefinition;
import com.client.definitions.anim.SpotAnimation;
import com.client.entity.model.Model;
import com.client.scene.object.RuneLiteObject;
import net.runelite.api.Animation;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.rs.api.RSGraphicsObject;
import net.runelite.rs.api.RSRuneLiteObject;
import net.runelite.rs.api.RSSequenceDefinition;

import javax.annotation.Nullable;


public class SpotAnimEntity extends Renderable implements RSGraphicsObject {

    public int z;
    public int x;
    public int y;
    public int height;
    public int cycleStart;
    public boolean expired;

    public int frame;

    private int id;
    public int frameCycle;

    SequenceDefinition graphics;

    public SpotAnimEntity() {
        final GraphicsObjectCreated event = new GraphicsObjectCreated(this);
        Client.instance.getCallbacks().post(event);
    }

    public SpotAnimEntity(int z, int cycleStart, int offset, int id, int height, int y, int x) {
        this.frame = 0;
        this.frameCycle = 0;
        this.z = z;
        this.x = x;
        this.y = y;
        this.id = id;
        this.height = height;
        this.cycleStart = cycleStart + offset;
        expired = false;
        int sequence = SpotAnimation.lookup(id).sequence;
        if (sequence != -1) {
            this.expired = false;
            this.graphics = SequenceDefinition.get(sequence);
        } else {
            this.expired = true;
        }
        final GraphicsObjectCreated event = new GraphicsObjectCreated(this);
        Client.instance.getCallbacks().post(event);
    }

    public Model getRotatedModel() {
        if (this instanceof RuneLiteObject)
        {
            return (Model) ((RSRuneLiteObject) this).getModelRl();
        }
        else
        {
            SpotAnimation var1 = SpotAnimation.lookup(this.id);
            Model var2;
            if (!this.expired) {
                var2 = var1.getModel(this.frame);
            } else {
                var2 = var1.getModel(-1);
            }

            return var2 == null ? null : var2;
        }
    }

    public void step(int cycle) {
        if (!this.expired) {
            this.frameCycle += cycle;
            if (!this.graphics.isCachedModelIdSet()) {
                while (this.frameCycle > this.graphics.delays[this.frame]) {
                    this.frameCycle -= this.graphics.delays[this.frame];
                    ++this.frame;
                    if (this.frame >= this.graphics.primaryFrameIds.length) {
                        this.expired = true;
                        break;
                    }
                }
            } else {
                this.frame += cycle;
                if (this.frame >= this.graphics.getSkeletalLength()) {
                    this.expired = true;
                }
            }

        }
        if (this instanceof RuneLiteObject)
        {
            ((RSRuneLiteObject) this).advanceRL(cycle);
        }

    }

    @Override
    public LocalPoint getLocation()
    {
        return new LocalPoint(this.getX(), this.getY());
    }

    @Nullable
    @Override
    public RSSequenceDefinition getAnimation() {
        return getSequenceDefinition();
    }

    @Override
    public int getAnimationFrame() {
        return 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getStartCycle() {
        return 0;
    }

    @Override
    public int getLevel() {
        return height;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int height) {
        z = height;
    }

    @Override
    public boolean finished() {
        return expired;
    }

    @Override
    public int getFrame() {
        return frame;
    }

    @Override
    public void setFrame(int frame) {
        this.frame = frame;
    }

    @Override
    public int getFrameCycle() {
        return frameCycle;
    }

    @Override
    public void setFrameCycle(int frameCycle) {
        this.frameCycle = frameCycle;
    }

    @Override
    public void setFinished(boolean finished) {
        expired = finished;
    }

    @Override
    public void setLevel(int level) {
        height = level;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public RSSequenceDefinition getSequenceDefinition() {
        return graphics;
    }

    @Override
    public void setSequenceDefinition(RSSequenceDefinition sequenceDefinition) {
        graphics = (SequenceDefinition) sequenceDefinition;
    }

    @Override
    public void advance(int var1) {
        step(var1);
    }
}
