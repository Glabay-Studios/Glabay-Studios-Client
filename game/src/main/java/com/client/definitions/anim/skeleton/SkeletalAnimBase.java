package com.client.definitions.anim.skeleton;

import com.client.Buffer;

public class SkeletalAnimBase {
   SkeletalBone[] bones;
   int maxConnections;

   public SkeletalAnimBase(Buffer packet, int count) {
      this.bones = new SkeletalBone[count];
      this.maxConnections = packet.readUnsignedByte();

      for(int boneIndex = 0; boneIndex < this.bones.length; ++boneIndex) {
         SkeletalBone animationBone = new SkeletalBone(this.maxConnections, packet, false);
         this.bones[boneIndex] = animationBone;
      }

      this.init();
   }

   void init() {
      for(int boneIndex = 0; boneIndex < this.bones.length; ++boneIndex) {
         SkeletalBone bone = this.bones[boneIndex];
         if (bone.parentId >= 0) {
            bone.parent = this.bones[bone.parentId];
         }
      }
   }

   public int boneCount() {
      return this.bones.length;
   }

   public SkeletalBone getBone(int id) {
      return id >= this.boneCount() ? null : this.bones[id];
   }

   SkeletalBone[] getBones() {
      return this.bones;
   }

   public void update(SkeletalFrameHandler arg0, int arg1) {
      this.update(arg0, arg1, null, false);
   }

   public void update(SkeletalFrameHandler animKeyFrameSet, int tick, boolean[] flowControl, boolean tweening) {
      int frame = animKeyFrameSet.method3190();
      int count = 0;
      SkeletalBone[] animationBones = this.getBones();

      for (SkeletalBone bone : animationBones) {
         if (flowControl == null || flowControl[count] == tweening) {
            animKeyFrameSet.method3167(tick, bone, count, frame);
         }
         ++count;
      }

   }

}
