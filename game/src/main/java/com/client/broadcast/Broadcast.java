package com.client.broadcast;

import java.util.concurrent.TimeUnit;

public class Broadcast {

    public int type;
    public String message;
    public String url;
    public int x, y;
    public int z;
    public int index;
    public long time;

    public Broadcast() {

    }

    public void setExpireDelay() {
        time = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
    }

    public String getDecoratedMessage() {
        return "[Broadcast]: " + message;
    }
}
