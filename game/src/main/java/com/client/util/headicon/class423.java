package com.client.util.headicon;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class class423 {

    final Comparator field4619;

    final Map field4613;

    final class392 field4614;

    final class392 field4615;

    final long field4616;

    final class421 field4617;

    final int field4618;


    class423(long var1, int var3, class421 var4) {
        this.field4619 = new class422(this);
        this.field4616 = var1;
        this.field4618 = var3;
        this.field4617 = var4;
        if (this.field4618 == -1) {
            this.field4613 = new HashMap(64);
            this.field4614 = new class392(64, this.field4619);
            this.field4615 = null;
        } else {
            if (this.field4617 == null) {
                throw new IllegalArgumentException("");
            }

            this.field4613 = new HashMap(this.field4618);
            this.field4614 = new class392(this.field4618, this.field4619);
            this.field4615 = new class392(this.field4618);
        }

    }

    public class423(int var1, class421 var2) {
        this(-1L, var1, var2);
    }

    boolean method7780() {
        return this.field4618 != -1;
    }

    public Object method7781(Object var1) {
        synchronized(this) {
            if (this.field4616 != -1L) {
                this.method7783();
            }

            class424 var3 = (class424)this.field4613.get(var1);
            if (var3 == null) {
                return null;
            } else {
                this.method7784(var3, false);
                return var3.field4620;
            }
        }
    }

    public Object put(Object var1, Object var2) {
        synchronized(this) {
            if (this.field4616 != -1L) {
                this.method7783();
            }

            class424 var4 = (class424)this.field4613.get(var1);
            if (var4 != null) {
                Object var8 = var4.field4620;
                var4.field4620 = var2;
                this.method7784(var4, false);
                return var8;
            } else {
                class424 var5;
                if (this.method7780() && this.field4613.size() == this.field4618) {
                    var5 = (class424)this.field4615.remove();
                    this.field4613.remove(var5.field4621);
                    this.field4614.remove(var5);
                }

                var5 = new class424(var2, var1);
                this.field4613.put(var1, var5);
                this.method7784(var5, true);
                return null;
            }
        }
    }


    void method7784(class424 var1, boolean var2) {
        if (!var2) {
            this.field4614.remove(var1);
            if (this.method7780() && !this.field4615.remove(var1)) {
                throw new IllegalStateException("");
            }
        }

        var1.field4622 = System.currentTimeMillis();
        if (this.method7780()) {
            switch(this.field4617.field4607) {
                case 0:
                    var1.field4623 = var1.field4622;
                    break;
                case 1:
                    ++var1.field4623;
            }

            this.field4615.add(var1);
        }

        this.field4614.add(var1);
    }


    void method7783() {
        if (this.field4616 == -1L) {
            throw new IllegalStateException("");
        } else {
            long var1 = System.currentTimeMillis() - this.field4616;

            while (!this.field4614.isEmpty()) {
                class424 var3 = (class424)this.field4614.peek();
                if (var3.field4622 >= var1) {
                    return;
                }

                this.field4613.remove(var3.field4621);
                this.field4614.remove(var3);
                if (this.method7780()) {
                    this.field4615.remove(var3);
                }
            }

        }
    }

    public void method7779() {
        synchronized(this) {
            this.field4613.clear();
            this.field4614.clear();
            if (this.method7780()) {
                this.field4615.clear();
            }

        }
    }
}
