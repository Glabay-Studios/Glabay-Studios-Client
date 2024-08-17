package com.client.scene;

import com.client.Client;
import com.client.Renderable;
import com.client.definitions.anim.SequenceDefinition;
import com.client.definitions.anim.SpotAnimation;
import com.client.entity.model.Model;
import net.runelite.api.Actor;
import net.runelite.api.Animation;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.ProjectileSpawned;
import net.runelite.rs.api.RSNPC;
import net.runelite.rs.api.RSNode;
import net.runelite.rs.api.RSPlayer;
import net.runelite.rs.api.RSProjectile;
import org.jetbrains.annotations.Nullable;

public final class Projectile extends Renderable implements RSProjectile {

    boolean isMoving = false;
    public double y;
    double speedX;
    public double x;
    public double z;
    double speedY;
    double speed;
    double speedZ;
    double accelerationZ;
    int sourceZ;
    int sourceX;
    int pitch;
    int frameCycle = 0;
    public int yaw;
    public int cycleEnd;
    public int endHeight;
    int startHeight;
    public int targetIndex;
    int slope;
    public int cycleStart;
    int sourceY;
    int id;

    public int plane;

    private int flow;

    public SequenceDefinition sequenceDefinition;

    private int targetX;

    private int targetY;


    public Model getRotatedModel() {
        SpotAnimation var1 = SpotAnimation.lookup(this.id);
        Model model = var1.getModel(this.flow);
        if (model == null) {
            return null;
        } else {
            model.rotateZ(this.pitch);
            return model;
        }
    }

    public Projectile(int slope_start, int endHeight, int cycleStart, int cycleEnd, int startHeight, int plane,  int start_z, int start_y, int start_x, int target_id, int id) {
        this.id = id;
        int sequence = SpotAnimation.lookup(this.id).sequence;
        if (sequence != -1) {
            this.sequenceDefinition = SequenceDefinition.get(sequence);
        } else {
            this.sequenceDefinition = null;
        }
        this.plane = plane;
        this.sourceX = start_x;
        this.sourceY = start_y;
        this.sourceZ = start_z;
        this.cycleStart = cycleStart;
        this.cycleEnd = cycleEnd;
        this.slope = slope_start;
        this.startHeight = startHeight;
        this.targetIndex = target_id;
        this.endHeight = endHeight;
        isMoving = false;
        ProjectileSpawned spawned = new ProjectileSpawned();
        spawned.setProjectile(this);
        Client.instance.getCallbacks().post(spawned);
    }

    public final void setDestination(int arg0, int arg1, int arg2, int arg3) {
        double var6;
        if (!this.isMoving) {
            var6 = (double)(arg0 - this.sourceX);
            double var8 = (double)(arg1 - this.sourceY);
            double var10 = Math.sqrt(var8 * var8 + var6 * var6);
            this.x = (double)this.sourceX + var6 * (double)this.startHeight / var10;
            this.y = var8 * (double)this.startHeight / var10 + (double)this.sourceY;
            this.z = (double)this.sourceZ;
        }

        var6 = (double)(1 + this.cycleEnd - arg3);
        this.speedX = ((double)arg0 - this.x) / var6;
        this.speedY = ((double)arg1 - this.y) / var6;
        this.speed = Math.sqrt(this.speedX * this.speedX + this.speedY * this.speedY);
        if (!this.isMoving) {
            this.speedZ = -this.speed * Math.tan((double)this.slope * 0.02454369D);
        }

        this.accelerationZ = ((double)arg2 - this.z - this.speedZ * var6) * 2.0D / (var6 * var6);
        projectileMoved(arg0,arg1,arg2,arg2);
    }

    public void projectileMoved(int targetX, int targetY, int targetZ, int cycle)
    {

        this.targetX = targetX;
        this.targetY = targetY;
        final LocalPoint position = new LocalPoint(targetX, targetY);
        ProjectileMoved projectileMoved = new ProjectileMoved();
        projectileMoved.setProjectile(this);
        projectileMoved.setPosition(position);
        projectileMoved.setZ(targetZ);
        Client.instance.getCallbacks().post(projectileMoved);
    }

    public void travel(int step) {
        isMoving = true;
        x += speedX * (double)step;
        y += speedY * (double)step;
        z += speedZ * (double)step + 0.5D * accelerationZ * (double)step * (double)step;
        speedZ += accelerationZ * (double)step;
        yaw = (int)(Math.atan2(speedX, speedY) * 325.949D) + 1024 & 2047;
        pitch = (int)(Math.atan2(speedZ, speed) *  325.949D) & 2047;
        if (this.sequenceDefinition != null) {
            if (!this.sequenceDefinition.isCachedModelIdSet()) {
                this.frameCycle += step;

                while (true) {
                    do {
                        do {
                            if (this.frameCycle <= this.sequenceDefinition.delays[this.flow]) {
                                return;
                            }

                            this.frameCycle -= this.sequenceDefinition.delays[this.flow];
                            ++this.flow;
                        } while(this.flow < this.sequenceDefinition.primaryFrameIds.length);

                        this.flow -= this.sequenceDefinition.frameCount;
                    } while(this.flow >= 0 && this.flow < this.sequenceDefinition.primaryFrameIds.length);

                    this.flow = 0;
                }
            } else {
                this.flow += step;
                int var2 = this.sequenceDefinition.getSkeletalLength();
                if (this.flow >= var2) {
                    this.flow = var2 - this.sequenceDefinition.frameCount;
                }
            }
        }
    }

    @Override
    public Actor getInteracting() {
        int interactingIndex = getRsInteracting();
        if (interactingIndex == 0)
        {
            return null;
        }

        if (interactingIndex > 0)
        {
            int idx = interactingIndex - 1;
            RSNPC[] npcs = Client.instance.getCachedNPCs();
            return npcs[idx];
        }
        else
        {
            int idx = -interactingIndex - 1;

            if (idx == Client.instance.getLocalPlayerIndex())
            {
                return Client.instance.getLocalPlayer();
            }

            RSPlayer[] players = Client.instance.getCachedPlayers();
            return players[idx];
        }
    }

    @Override
    public LocalPoint getTarget()
    {
        return new LocalPoint(this.targetX, this.targetY);
    }

    @Override
    public int getRemainingCycles() {
        int currentGameCycle = Client.instance.getGameCycle();

        return getEndCycle() - currentGameCycle;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getRsInteracting() {
        return targetIndex;
    }

    @Override
    public int getHeight() {
        return sourceZ;
    }

    @Override
    public int getEndHeight() {
        return endHeight;
    }

    @Override
    public int getX1() {
        return sourceX;
    }

    @Override
    public int getY1() {
        return sourceY;
    }

    @Override
    public int getFloor() {
        return plane;
    }

    @Override
    public int getStartCycle() {
        return cycleStart;
    }

    @Override
    public int getEndCycle() {
        return cycleEnd;
    }

    @Override
    public void setEndCycle(int cycle) {
        cycleEnd = cycle;
    }

    @Override
    public int getSlope() {
        return slope;
    }

    @Override
    public int getStartHeight() {
        return startHeight;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public double getScalar() {
        return speed;
    }

    @Override
    public double getVelocityX() {
        return speedX;
    }

    @Override
    public double getVelocityY() {
        return speedY;
    }

    @Override
    public double getVelocityZ() {
        return speedZ;
    }

    @Nullable
    @Override
    public Animation getAnimation() {
        return sequenceDefinition;
    }

    @Override
    public int getAnimationFrame() {
        return frameCycle;
    }

}