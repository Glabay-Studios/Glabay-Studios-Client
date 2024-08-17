package com.client.definitions.anim.skeleton;

import com.client.definitions.anim.SequenceDefinition;
import com.client.definitions.anim.Skeleton;
import com.client.definitions.anim.skeleton.task.AnimationKeyFrameBatchLoader;
import com.client.definitions.anim.skeleton.task.AnimationKeyFrameDecoderTask;
import com.client.definitions.anim.skeleton.task.AnimationKeyFrameTask;
import com.client.collection.node.DualNode;
import com.client.Buffer;
import com.client.definitions.anim.skeleton.task.AnimationThread;
import com.client.js5.Js5List;
import com.client.util.EnumUtils;
import com.client.util.math.Matrix4f;
import com.client.util.math.Quaternion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class SkeletalFrameHandler extends DualNode {
   private static int totalCores;

   private boolean modifiesTrans;
   public AnimationKeyFrame[][] skeletalTransforms = null;

   private int keyID = 0;
   public AnimationKeyFrame[][] transforms = null;
   public Skeleton base;

   private Future<Void> decoderTask;

   private List<Future<Void>> activeThreads;

   private static ThreadPoolExecutor threadPoolExecutor;

   SkeletalFrameHandler(int frameID, boolean useAsFile) {
      byte[] data = Js5List.animations.takeFile(frameID >> 16 & '\uffff', frameID & '\uffff');
      Buffer var6 = new Buffer(data);
      int id = var6.readUnsignedByte();
      int baseID = var6.readUShort();
      byte[] payload;
      if (useAsFile) {
         payload = Js5List.skeletons.getFile(0, baseID);
      } else {
         payload = Js5List.skeletons.getFile(baseID, 0);
      }

      this.base = new Skeleton(baseID, payload);
      this.activeThreads = new ArrayList<>();
      this.decoderTask = threadPoolExecutor.submit(new AnimationKeyFrameDecoderTask(this, var6, id));
   }


   public static SkeletalFrameHandler getFrames(int frameId) {
      if (isFrameInvalid(frameId) != 0) {
         return null;
      } else {
         SkeletalFrameHandler cachedFrames = (SkeletalFrameHandler) SequenceDefinition.cachedModel.get((long) frameId);
         SkeletalFrameHandler frames;
         if (cachedFrames != null) {
            frames = cachedFrames;
         } else {
            cachedFrames = createFrames(frameId, false);
            if (cachedFrames != null) {
               SequenceDefinition.cachedModel.put(cachedFrames, (long) frameId);
            }

            frames = cachedFrames;
         }

         return frames;
      }
   }

   public static int isFrameInvalid(int id) {
      SkeletalFrameHandler cachedFrames = (SkeletalFrameHandler) SequenceDefinition.cachedModel.get((long) id);
      SkeletalFrameHandler frames;
      if (cachedFrames != null) {
         frames = cachedFrames;
      } else {
         cachedFrames = createFrames(id, false);
         if (cachedFrames != null) {
            SequenceDefinition.cachedModel.put(cachedFrames, (long) id);
         }

         frames = cachedFrames;
      }

      if (frames == null) {
         return 2;
      } else {
         return frames.hasError() ? 0 : 1;
      }
   }

   public boolean hasError() {
      if (this.decoderTask == null && this.activeThreads == null) {
         return true;
      } else {
         if (this.decoderTask != null) {
            if (!this.decoderTask.isDone()) {
               return false;
            }

            this.decoderTask = null;
         }

         boolean allThreadsDone = true;

         for (int var2 = 0; var2 < this.activeThreads.size(); ++var2) {
            if (!((Future<Void>) this.activeThreads.get(var2)).isDone()) {
               allThreadsDone = false;
            } else {
               this.activeThreads.remove(var2);
               --var2;
            }
         }

         if (!allThreadsDone) {
            return false;
         } else {
            this.activeThreads = null;
            return true;
         }
      }
   }

   public static SkeletalFrameHandler createFrames(int id, boolean useAsFile) {
      boolean success = true;
      byte[] data = Js5List.animations.getFile(id >> 16 & '\uffff', id & '\uffff');
      if (data == null) {
         return null;
      } else {
         int skeletonId = (data[1] & 255) << 8 | data[2] & 255;
         byte[] skeletonData;
         if (useAsFile) {
            skeletonData = Js5List.skeletons.getFile(0, skeletonId);
         } else {
            skeletonData = Js5List.skeletons.getFile(skeletonId, 0);
         }

         if (skeletonData == null) {
            success = false;
         }

         if (!success) {
            return null;
         } else {
            if (threadPoolExecutor == null) {
               totalCores = Runtime.getRuntime().availableProcessors();
               threadPoolExecutor = new ThreadPoolExecutor(0, totalCores, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(totalCores * 100 + 100), new AnimationThread());
            }

            try {
               return new SkeletalFrameHandler(id, useAsFile);
            } catch (Exception var9) {
               return null;
            }
         }
      }
   }

   public void decode(Buffer packet, int version) {
      packet.readShortOSRS();
      packet.readShortOSRS();
      this.keyID = packet.readUnsignedByte();
      int totalCount = packet.readUShort();
      this.skeletalTransforms = new AnimationKeyFrame[this.base.getSkeletalBase().boneCount()][];
      this.transforms = new AnimationKeyFrame[this.base.transformsCount()][];
      int id;
      AnimationKeyFrameTask[] var4 = new AnimationKeyFrameTask[totalCount];
      for(int index = 0; index < totalCount; ++index) {
         id = packet.readUnsignedByte();
         AnimTransform[] values = new AnimTransform[]{AnimTransform.NULL, AnimTransform.VERTEX, AnimTransform.field1210, AnimTransform.COLOUR, AnimTransform.TRANSPARENCY, AnimTransform.field1213};
         AnimTransform animTransform = (AnimTransform) EnumUtils.findEnumerated(values, id);
         if (animTransform == null) {
            animTransform = AnimTransform.NULL;
         }

         int transformLocation = packet.readSmartByteorshort();
         AnimationChannel animationChannel = AnimationChannel.lookup(packet.readUnsignedByte());
         AnimationKeyFrame animationKeyFrame = new AnimationKeyFrame();
         animationKeyFrame.deserialise(packet,version);
         var4[index] = new AnimationKeyFrameTask(this, animationKeyFrame, animTransform, animationChannel, transformLocation);
         int count = animTransform.getDimensions();
         AnimationKeyFrame[][] transforms;
         if (AnimTransform.VERTEX == animTransform) {
            transforms = this.skeletalTransforms;
         } else {
            transforms = this.transforms;
         }

         if (transforms[transformLocation] == null) {
            transforms[transformLocation] = new AnimationKeyFrame[count];
         }

         transforms[transformLocation][animationChannel.getComponent()] = animationKeyFrame;
         if (AnimTransform.TRANSPARENCY == animTransform) {
            this.modifiesTrans = true;
         }
      }

      int var5 = totalCount / totalCores;
      int var14 = totalCount % totalCores;
      int var15 = 0;

      for (int var9 = 0; var9 < totalCores; ++var9) {
         id = var15;
         var15 += var5;
         if (var14 > 0) {
            ++var15;
            --var14;
         }

         if (var15 == id) {
            break;
         }

         this.activeThreads.add(threadPoolExecutor.submit(new AnimationKeyFrameBatchLoader(this, id, var15, var4)));
      }

   }

   public int method3190() {
      return this.keyID;
   }

   public boolean hasAlphaTransforms() {
      return this.modifiesTrans;
   }

   public void method3167(int tick, SkeletalBone bone, int transform_index, int keyframe) {
      Matrix4f boneMatrix = Matrix4f.take();
      this.applyRotationTransforms(boneMatrix, transform_index, bone, tick);
      this.applyScaleTransforms(boneMatrix, transform_index, bone, tick);
      this.applyTranslationTransforms(boneMatrix, transform_index, bone, tick);
      bone.setBoneTransform(boneMatrix);
      boneMatrix.release();
   }

   void applyRotationTransforms(Matrix4f transMatrix, int transformIndex, SkeletalBone bone, int tick) {
      float[] rotations = bone.getRotation(this.keyID);
      float x = rotations[0];
      float y = rotations[1];
      float z = rotations[2];
      if (null != this.skeletalTransforms[transformIndex]) {
         AnimationKeyFrame xrotTransform = this.skeletalTransforms[transformIndex][0];
         AnimationKeyFrame yrotTransform = this.skeletalTransforms[transformIndex][1];
         AnimationKeyFrame zrotTransform = this.skeletalTransforms[transformIndex][2];
         if (xrotTransform != null) {
            x = xrotTransform.getValue(tick);
         }

         if (yrotTransform != null) {
            y = yrotTransform.getValue(tick);
         }

         if (zrotTransform != null) {
            z = zrotTransform.getValue(tick);
         }
      }

      Quaternion xrot = Quaternion.getOrCreateFromPool();
      xrot.rotate(1.0F, 0.0F, 0.0F, x);
      Quaternion yrot = Quaternion.getOrCreateFromPool();
      yrot.rotate(0.0F, 1.0F, 0.0F, y);
      Quaternion zrot = Quaternion.getOrCreateFromPool();
      zrot.rotate(0.0F, 0.0F, 1.0F, z);
      Quaternion other = Quaternion.getOrCreateFromPool();
      other.multiply(zrot);
      other.multiply(xrot);
      other.multiply(yrot);
      Matrix4f var14 = Matrix4f.take();
      var14.setRotation(other);
      transMatrix.multiply(var14);
      xrot.releaseToPool();
      yrot.releaseToPool();
      zrot.releaseToPool();
      other.releaseToPool();
      var14.release();
   }

   void applyTranslationTransforms(Matrix4f matrix4f, int transformIndex, SkeletalBone bone, int tick) {
      float[] goneTranslation = bone.getTranslation(this.keyID);
      float translateX = goneTranslation[0];
      float translateY = goneTranslation[1];
      float translateZ = goneTranslation[2];
      if (this.skeletalTransforms[transformIndex] != null) {
         AnimationKeyFrame animationKeyFrame3 = this.skeletalTransforms[transformIndex][3];
         AnimationKeyFrame animationKeyFrame4 = this.skeletalTransforms[transformIndex][4];
         AnimationKeyFrame animationKeyFrame5 = this.skeletalTransforms[transformIndex][5];
         if (animationKeyFrame3 != null) {
            translateX = animationKeyFrame3.getValue(tick);
         }

         if (null != animationKeyFrame4) {
            translateY = animationKeyFrame4.getValue(tick);
         }

         if (null != animationKeyFrame5) {
            translateZ = animationKeyFrame5.getValue(tick);
         }
      }

      matrix4f.values[12] = translateX;
      matrix4f.values[13] = translateY;
      matrix4f.values[14] = translateZ;
   }

   void applyScaleTransforms(Matrix4f transformMatrix, int transformIndex, SkeletalBone bone, int tick) {
      float[] boneScale = bone.getScale(this.keyID);
      float scaleX = boneScale[0];
      float scaleY = boneScale[1];
      float scaleZ = boneScale[2];
      if (this.skeletalTransforms[transformIndex] != null) {
         AnimationKeyFrame xscaleOp = this.skeletalTransforms[transformIndex][6];
         AnimationKeyFrame yscaleOp = this.skeletalTransforms[transformIndex][7];
         AnimationKeyFrame zscaleOp = this.skeletalTransforms[transformIndex][8];
         if (xscaleOp != null) {
            scaleX = xscaleOp.getValue(tick);
         }

         if (yscaleOp != null) {
            scaleY = yscaleOp.getValue(tick);
         }

         if (zscaleOp != null) {
            scaleZ = zscaleOp.getValue(tick);
         }
      }

      Matrix4f matrix4f = Matrix4f.take();
      matrix4f.setScale(scaleX, scaleY, scaleZ);
      transformMatrix.multiply(matrix4f);
      matrix4f.release();
   }

}
