package com.client.scene.object;

import com.client.Client;
import com.client.entity.model.Model;
import com.client.scene.SpotAnimEntity;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Animation;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSRuneLiteObject;
import net.runelite.rs.api.RSSequenceDefinition;

public class RuneLiteObject extends SpotAnimEntity implements RSRuneLiteObject {
    @Setter
    public Model model;

    public boolean loop;

    private static RSModel rlmodel;

    @Getter
    @Setter
    private int orientation;
    @Getter
    @Setter
    private int radius = 60;
    @Setter
    private boolean drawFrontTilesFirst;

    public RuneLiteObject() {
        super.expired = true;
    }

    public boolean isLooping() {
        return loop;
    }

    @Override
    public void advanceRL(int var1) {
        if (getSequenceDefinition() != null)
        {
            if (isLooping() && finished())
            {
                setFinished(false);
                setFrame(0);
                setFrameCycle(0);
            }
        }
    }

    @Override
    public RSModel getModelRl() {
        RSModel model = rlmodel;
        if (getSequenceDefinition() != null)
        {
            model = getSequenceDefinition().transformSpotAnimationModel(model, getFrame());
        }
        else
        {
            model = model.toSharedModel(true);
        }

        return model;
    }

    public boolean isActive() {
        return !super.expired;
    }

    public void setActive(boolean active) {
        if (super.expired == active) {
            super.expired = !active;
            if (active) {
                super.frame = 0;
                super.frameCycle = 0;
                Client.instance.incompleteAnimables.addFirst(this);
            } else {
                remove();
            }
        }
    }

    @Override
    public void setModel(net.runelite.api.Model model) {
        rlmodel = (RSModel) model;
    }

    @Override
    public void setAnimation(Animation animation) {
        setFrame(0);
        setFrameCycle(0);
        setSequenceDefinition((RSSequenceDefinition) animation);
    }

    public void setShouldLoop(boolean var1) {
        this.loop = var1;
    }

    @Override
    public void setLocation(LocalPoint point, int plane) {
        setX(point.getX());
        setY(point.getY());
        setLevel(plane);
        setZ(Perspective.getTileHeight(Client.instance, point, plane));
    }

    public boolean drawFrontTilesFirst() {
        return drawFrontTilesFirst;
    }
}