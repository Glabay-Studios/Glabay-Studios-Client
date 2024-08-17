package com.client.definitions.anim.skeleton;

import com.client.Buffer;

public class AnimKey {
   AnimKey next;
   float value;
   float start2 = Float.MAX_VALUE;
   float start1 = Float.MAX_VALUE;
   float end1 = Float.MAX_VALUE;
   float end2 = Float.MAX_VALUE;
   int tick;

   void deserialise(Buffer buffer, int version) {
      this.tick = buffer.readShortOSRS();
      this.value = buffer.readFloat();
      this.start1 = buffer.readFloat();
      this.start2 = buffer.readFloat();
      this.end1 = buffer.readFloat();
      this.end2 = buffer.readFloat();
   }
}
