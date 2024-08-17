package com.client.util.headicon;

import java.util.concurrent.ThreadFactory;

public final class class130 implements ThreadFactory {

    public Thread newThread(Runnable var1) {
        return new Thread(var1, "OSRS Maya Anim Load");
    }

}