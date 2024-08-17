package com.client.definitions.anim;

import com.client.Buffer;
import net.runelite.rs.api.RSAnimation;
import net.runelite.rs.api.RSSkeleton;

public final class Animation implements RSAnimation {

    public int delay;
    public Skeleton skeleton;
    public int transformCount;
    public int transformSkeletonLabels[];
    public int transformXs[];
    public int transformYs[];
    public int transformZs[];
    boolean hasAlphaTransform = false;

    Animation(byte[] data, Skeleton skeletonBase) {
        this.skeleton = skeletonBase;

        Buffer dataBuffer = new Buffer(data);
        Buffer indicesBuffer = new Buffer(data);
        dataBuffer.currentPosition = 2;
        int frameCount = dataBuffer.readUnsignedByte();

        indicesBuffer.currentPosition = frameCount + dataBuffer.currentPosition;

        int[] groups = new int[500];
        int[] xDeltas = new int[500];
        int[] yDeltas = new int[500];
        int[] zDeltas = new int[500];

        int lastI = -1;
        int groupCount = 0;
        for (int index = 0; index < frameCount; ++index) {
            int flag = dataBuffer.readUnsignedByte();
            if (flag > 0) {
                if (this.skeleton.transformTypes[index] != 0) {
                    for (int var10 = index - 1; var10 > lastI; --var10) {
                        if (this.skeleton.transformTypes[var10] == TransformType.ORIGIN.getValue()) {
                            groups[groupCount] = var10;
                            xDeltas[groupCount] = 0;
                            yDeltas[groupCount] = 0;
                            zDeltas[groupCount] = 0;
                            ++groupCount;
                            break;
                        }
                    }
                }

                groups[groupCount] = index;
                short defaultValue = 0;
                if (this.skeleton.transformTypes[index] == TransformType.SCALE.getValue()) {
                    defaultValue = 128;
                }

                if ((flag & 1) != 0) {
                    xDeltas[groupCount] = indicesBuffer.readShortSmart();
                } else {
                    xDeltas[groupCount] = defaultValue;
                }

                if ((flag & 2) != 0) {
                    yDeltas[groupCount] = indicesBuffer.readShortSmart();
                } else {
                    yDeltas[groupCount] = defaultValue;
                }

                if ((flag & 4) != 0) {
                    zDeltas[groupCount] = indicesBuffer.readShortSmart();
                } else {
                    zDeltas[groupCount] = defaultValue;
                }

                lastI = index;
                ++groupCount;
                if (this.skeleton.transformTypes[index] == 5) {
                    this.hasAlphaTransform = true;
                }
            }
        }

        if (data.length != indicesBuffer.currentPosition) {
            throw new RuntimeException("Animation Data does not match Indices Buffer");
        } else {
            this.transformCount = groupCount;
            this.transformSkeletonLabels = groups;
            this.transformXs = xDeltas;
            this.transformYs = yDeltas;
            this.transformZs = zDeltas;

        }
    }

    public static boolean noAnimationInProgress(int i) {
        return i == -1;
    }

    @Override
    public RSSkeleton getSkeleton() {
        return skeleton;
    }

    @Override
    public int getTransformCount() {
        return transformCount;
    }

    @Override
    public int[] getTransformTypes() {
        return transformSkeletonLabels;
    }

    @Override
    public int[] getTranslatorX() {
        return transformXs;
    }

    @Override
    public int[] getTranslatorY() {
        return transformYs;
    }

    @Override
    public int[] getTranslatorZ() {
        return transformZs;
    }

    @Override
    public boolean isShowing() {
        return hasAlphaTransform;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getNumFrames() {
        return 0;
    }

    @Override
    public int getRestartMode() {
        return 0;
    }

    @Override
    public void setRestartMode(int restartMode) {

    }


}
