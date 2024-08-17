package com.client.definitions.anim.skeleton.task;

import com.client.definitions.anim.skeleton.SkeletalFrameHandler;

import java.util.concurrent.Callable;

public class AnimationKeyFrameBatchLoader implements Callable {

    private final SkeletalFrameHandler keyFrameSet;
    private final int startIndex;
    private final int endIndex;
    private final AnimationKeyFrameTask[] tasks;

    public AnimationKeyFrameBatchLoader(SkeletalFrameHandler keyFrameSet, int startIndex, int endIndex, AnimationKeyFrameTask[] tasks) {
        this.keyFrameSet = keyFrameSet;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tasks = tasks;
    }

    @Override
    public Void call() {
        for (int i = startIndex; i < endIndex; ++i) {
            tasks[i].call();
        }
        return null;
    }
}
