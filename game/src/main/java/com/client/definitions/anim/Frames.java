package com.client.definitions.anim;

import com.client.collection.node.DualNode;
import com.client.collection.node.NodeDeque;
import com.client.js5.Js5List;

public class Frames extends DualNode {

    public Animation[] frames;

    Frames(int var3, boolean var4) {
        NodeDeque var5 = new NodeDeque();
        int var6 = Js5List.animations.getGroupFileCount(var3);
        this.frames = new Animation[var6];
        int[] var7 = Js5List.animations.getGroupFileIds(var3);

        for (int var8 = 0; var8 < var7.length; ++var8) {
            byte[] var9 = Js5List.animations.takeFile(var3, var7[var8]);
            Skeleton var10 = null;
            int var11 = (var9[0] & 255) << 8 | var9[1] & 255;

            for (Skeleton var12 = (Skeleton) var5.last(); var12 != null; var12 = (Skeleton) var5.previous())
                if (var11 == var12.id) {
                    var10 = var12;
                    break;
                }

            if (var10 == null) {
                byte[] var13;
                if (var4) var13 = Js5List.skeletons.getFile(0, var11);
                else var13 = Js5List.skeletons.getFile(var11, 0);

                var10 = new Skeleton(var11, var13);
                var5.addFirst(var10);
            }

            this.frames[var7[var8]] = new Animation(var9, var10);
        }

    }

    public static Frames getFrames(int var0) {
        Frames var1 = (Frames) SequenceDefinition.cachedFrames.get((long) var0);
        if (var1 != null) return var1;
        else {
            var1 = getFrames(var0, false);
            if (var1 != null) SequenceDefinition.cachedFrames.put(var1, (long) var0);

            return var1;
        }
    }

    public static Frames getFrames(int var2, boolean var3) {
        boolean var4 = true;
        int[] var5 = Js5List.animations.getGroupFileIds(var2);

        for (int i : var5) {
            byte[] var7 = Js5List.animations.getFile(var2, i);
            if (var7 == null) var4 = false;
            else {
                int var8 = (var7[0] & 255) << 8 | var7[1] & 255;
                byte[] var9;
                if (var3) var9 = Js5List.skeletons.getFile(0, var8);
                else var9 = Js5List.skeletons.getFile(var8, 0);

                if (var9 == null) var4 = false;
            }
        }

        if (!var4) return null;
        else try {
            return new Frames(var2, var3);
        }
        catch (Exception var11) {
            return null;
        }
    }

    public boolean hasAlphaTransform(int frame_index) {
        return this.frames[frame_index].hasAlphaTransform;
    }

}
