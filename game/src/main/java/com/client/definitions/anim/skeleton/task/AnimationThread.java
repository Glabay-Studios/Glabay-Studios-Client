package com.client.definitions.anim.skeleton.task;

import java.util.concurrent.ThreadFactory;

public final class AnimationThread implements ThreadFactory {

    public Thread newThread(Runnable var1) {
        return new Thread(var1, "OSRS Maya Anim Load");
    }

}