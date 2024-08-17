package com.client.util.headicon;

import com.client.js5.disk.AbstractArchive;

import java.util.Comparator;

class class422 implements Comparator {

    static AbstractArchive field4609;
    // $FF: synthetic field

    final class423 this$0;


    class422(class423 var1) {
        this.this$0 = var1;
    }


    int method7770(class424 var1, class424 var2) {
        if (var1.field4622 > var2.field4622) {
            return 1;
        } else {
            return var1.field4622 < var2.field4622 ? -1 : 0;
        }
    }

    public boolean equals(Object var1) {
        return super.equals(var1);
    }

    public int compare(Object var1, Object var2) {
        return this.method7770((class424)var1, (class424)var2);
    }
}
