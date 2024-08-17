package com.client.definitions.anim.skeleton;

import com.client.util.EnumExtension;
import com.client.util.EnumUtils;

public class InterpolationType implements EnumExtension {
   public static final InterpolationType STEP_INTERPOLATION = new InterpolationType(0, 0);
   public static final InterpolationType LINEAR_INTERPOLATION = new InterpolationType(1, 1);
   public static final InterpolationType FORWARDS_INTERPOLATION = new InterpolationType(4, 4);
   public static final InterpolationType CUBICSPLINE_INTERPOLATION = new InterpolationType(3, 3);
   public static final InterpolationType BACKWARDS_INTERPOLATION = new InterpolationType(2, 2);
   final int ordinal;
   final int id;

   static InterpolationType[] values() {
      return new InterpolationType[]{STEP_INTERPOLATION, LINEAR_INTERPOLATION, BACKWARDS_INTERPOLATION, CUBICSPLINE_INTERPOLATION, FORWARDS_INTERPOLATION};
   }

   InterpolationType(int ordinal, int id) {
      this.ordinal = ordinal;
      this.id = id;
   }

   public static InterpolationType lookupById(int id) {
      InterpolationType interpolationType = (InterpolationType) EnumUtils.findEnumerated(values(), id);
      if (null == interpolationType) {
         interpolationType = STEP_INTERPOLATION;
      }

      return interpolationType;
   }

   public int rsOrdinal() {
      return this.id;
   }
}
