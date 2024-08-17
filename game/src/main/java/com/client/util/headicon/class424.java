package com.client.util.headicon;

public final class class424 implements Comparable {

    Object field4620;

    Object field4621;

    long field4622;

    long field4623;

    class424(Object var1, Object var2) {
        this.field4620 = var1;
        this.field4621 = var2;
    }

    int method7797(class424 var1) {
        if (this.field4623 < var1.field4623) {
            return -1;
        } else {
            return this.field4623 > var1.field4623 ? 1 : 0;
        }
    }

    public boolean equals(Object var1) {
        if (var1 instanceof class424) {
            return this.field4621.equals(((class424)var1).field4621);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int compareTo(Object var1) {
        return this.method7797((class424)var1);
    }

    public int hashCode() {
        return this.field4621.hashCode();
    }
}
