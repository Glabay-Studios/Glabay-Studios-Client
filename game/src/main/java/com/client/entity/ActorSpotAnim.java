package com.client.entity;

import com.client.collection.node.Node;
import net.runelite.api.RSActorSpotAnim;

public class ActorSpotAnim extends Node implements RSActorSpotAnim {

    public int spotAnimation;

    public int spotAnimationFrame;

    public int spotAnimationFrameCycle;

    public int cycle;

    public int spotAnimationHeight;

    public ActorSpotAnim(int id, int height, int cycle, int frame) {
        this.spotAnimationFrameCycle = 0;
        this.spotAnimation = id;
        this.spotAnimationHeight = height;
        this.cycle = cycle;
        this.spotAnimationFrame = frame;
    }

    @Override
    public int getId() {
        return spotAnimation;
    }

    @Override
    public void setId(int id) {
        this.spotAnimation = id;
    }

    @Override
    public int getHeight() {
        return spotAnimationHeight;
    }

    @Override
    public void setHeight(int id) {
        spotAnimationHeight = id;
    }

    @Override
    public int getFrame() {
        return spotAnimationFrame;
    }

    @Override
    public void setFrame(int id) {
        spotAnimationFrame = id;
    }

    @Override
    public int getCycle() {
        return cycle;
    }

    @Override
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }
}