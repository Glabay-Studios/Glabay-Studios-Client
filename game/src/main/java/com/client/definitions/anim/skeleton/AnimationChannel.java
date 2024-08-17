package com.client.definitions.anim.skeleton;

import com.client.util.EnumExtension;
import com.client.util.EnumUtils;

public class AnimationChannel implements EnumExtension {
   public static final AnimationChannel TRANSLATE_X = new AnimationChannel(4, 4, 3);
   public static final AnimationChannel SCALE_X = new AnimationChannel(7, 7, 6);
   public static final AnimationChannel ROTATE_Y = new AnimationChannel(2, 2, 1);
   public static final AnimationChannel field2343 = new AnimationChannel(12, 12, 2);
   public static final AnimationChannel TRANSLATE_Y = new AnimationChannel(5, 5, 4);
   public  static final AnimationChannel TRANSLATE_Z = new AnimationChannel(6, 6, 5);
   public static final AnimationChannel NULL = new AnimationChannel(0, 0, -1);
   public static final AnimationChannel SCALE_Y = new AnimationChannel(8, 8, 7);
   public static final AnimationChannel field2341 = new AnimationChannel(10, 10, 0);
   public static final AnimationChannel field2342 = new AnimationChannel(11, 11, 1);
   public static final AnimationChannel SCALE_Z = new AnimationChannel(9, 9, 8);
   public static final AnimationChannel ROTATE_Z = new AnimationChannel(3, 3, 2);
   public static final AnimationChannel field2344 = new AnimationChannel(14, 14, 4);
   public static final AnimationChannel TRANSPARENCY = new AnimationChannel(16, 16, 0);
   public static final AnimationChannel field2347 = new AnimationChannel(13, 13, 3);
   public static final AnimationChannel ROTATE_X = new AnimationChannel(1, 1, 0);
   public static final AnimationChannel field2345 = new AnimationChannel(15, 15, 5);

   private final int ordinal;
   private final int id;
   private final int component;

   private static AnimationChannel[] values() {
      return new AnimationChannel[]{NULL, ROTATE_X, ROTATE_Y, ROTATE_Z, TRANSLATE_X, TRANSLATE_Y, TRANSLATE_Z, SCALE_X, SCALE_Y, SCALE_Z, field2341, field2342, field2343, field2347, field2344, field2345, TRANSPARENCY};
   }

   AnimationChannel(int ordinal, int id, int component) {
      this.ordinal = ordinal;
      this.id = id;
      this.component = component;
   }

   public static AnimationChannel lookup(int id) {
      AnimationChannel animationChannel = (AnimationChannel) EnumUtils.findEnumerated(values(), id);
      if (animationChannel == null) {
         animationChannel = NULL;
      }

      return animationChannel;
   }

   public int rsOrdinal() {
      return this.id;
   }

   public int getComponent() {
      return this.component;
   }

}
