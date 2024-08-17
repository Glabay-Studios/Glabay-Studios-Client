package com.client.definitions.anim.skeleton;

import com.client.Buffer;
import com.client.util.math.Matrix4f;

public class SkeletalBone {
   private boolean needsPoseToSkinTransform = true;
   private boolean needsBoneToPoseTransform = true;
   private Matrix4f bonePoseTransform = new Matrix4f();
   private Matrix4f skinningMatrix = new Matrix4f();
   private Matrix4f boneTransforms = new Matrix4f();
   private Matrix4f[] worldMatrix;
   private Matrix4f[] inverseLocalMatrix;
   private float[][] translations;
   private float[][] rotations;
   private float[][] scale;
   private float[][] boneVertices;
   private final Matrix4f[] localMatrix;
   public SkeletalBone parent;
   final int parentId;

   public SkeletalBone(int frameCount, Buffer buffer, boolean compact) {
      this.parentId = buffer.readShortOSRS();
      this.localMatrix = new Matrix4f[frameCount];
      this.worldMatrix = new Matrix4f[this.localMatrix.length];
      this.inverseLocalMatrix = new Matrix4f[this.localMatrix.length];
      this.boneVertices = new float[this.localMatrix.length][3];

      for(int frameId = 0; frameId < this.localMatrix.length; ++frameId) {
         this.localMatrix[frameId] = new Matrix4f(buffer, compact);
         this.boneVertices[frameId][0] = buffer.readFloat();
         this.boneVertices[frameId][1] = buffer.readFloat();
         this.boneVertices[frameId][2] = buffer.readFloat();
      }

      this.loadLocalSpace();
   }

   private void loadLocalSpace() {
      this.rotations = new float[this.localMatrix.length][3];
      this.translations = new float[this.localMatrix.length][3];
      this.scale = new float[this.localMatrix.length][3];
      Matrix4f matrix4f = Matrix4f.take();

      for(int currentMatrix = 0; currentMatrix < this.localMatrix.length; ++currentMatrix) {
         Matrix4f local = this.getLocalMatrix(currentMatrix);
         matrix4f.set(local);
         matrix4f.invert();
         this.rotations[currentMatrix] = matrix4f.getInverseYXZRotationEulerAngles();
         this.translations[currentMatrix][0] = local.values[12];
         this.translations[currentMatrix][1] = local.values[13];
         this.translations[currentMatrix][2] = local.values[14];
         this.scale[currentMatrix] = local.getScale();
      }

      matrix4f.release();
   }

   private Matrix4f getLocalMatrix(int matrixID) {
      return this.localMatrix[matrixID];
   }

   private Matrix4f getWorldMatrix(int frameId) {
      if (null == this.worldMatrix[frameId]) {
         this.worldMatrix[frameId] = new Matrix4f(this.getLocalMatrix(frameId));
         if (null != this.parent) {
            this.worldMatrix[frameId].multiply(this.parent.getWorldMatrix(frameId));
         } else {
            this.worldMatrix[frameId].multiply(Matrix4f.IDENTITY);
         }
      }

      return this.worldMatrix[frameId];
   }

   private Matrix4f getInverseLocalMatrix(int index) {
      if (null == this.inverseLocalMatrix[index]) {
         this.inverseLocalMatrix[index] = new Matrix4f(this.getWorldMatrix(index));
         this.inverseLocalMatrix[index].invert();
      }

      return this.inverseLocalMatrix[index];
   }

   void setBoneTransform(Matrix4f matrix4f) {
      this.boneTransforms.set(matrix4f);
      this.needsBoneToPoseTransform = true;
      this.needsPoseToSkinTransform = true;
   }

   private Matrix4f boneTransforms() {
      return this.boneTransforms;
   }

   private Matrix4f getRelativeBonePose() {
      try {
         if (this.needsBoneToPoseTransform) {
            this.bonePoseTransform.set(this.boneTransforms());
            if (this.parent != null) {
               this.bonePoseTransform.multiply(this.parent.getRelativeBonePose());
            }

            this.needsBoneToPoseTransform = false;
         }
      } catch(StackOverflowError e) {
         e.printStackTrace();
      }


      return this.bonePoseTransform;
   }

   public Matrix4f getSkinning(int frame_id) {
      if (this.needsPoseToSkinTransform) {
         this.skinningMatrix.set(this.getInverseLocalMatrix(frame_id));
         this.skinningMatrix.multiply(this.getRelativeBonePose());
         this.needsPoseToSkinTransform = false;
      }

      return this.skinningMatrix;
   }

   float[] getRotation(int index) {
      return this.rotations[index];
   }

   float[] getTranslation(int index) {
      return this.translations[index];
   }

   float[] getScale(int index) {
      return this.scale[index];
   }


}
