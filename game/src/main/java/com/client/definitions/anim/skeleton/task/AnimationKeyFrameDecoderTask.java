package com.client.definitions.anim.skeleton.task;

import com.client.definitions.anim.skeleton.SkeletalFrameHandler;
import com.client.Buffer;

import java.util.concurrent.Callable;

public class AnimationKeyFrameDecoderTask implements Callable<Void> {

    private final SkeletalFrameHandler keyFrameSet;
    private final Buffer buffer;
    private final int version;

    public AnimationKeyFrameDecoderTask(SkeletalFrameHandler keyFrameSet, Buffer buffer, int version) {
        this.keyFrameSet = keyFrameSet;
        this.buffer = buffer;
        this.version = version;
    }

    @Override
    public Void call() {
        this.keyFrameSet.decode(buffer, version);
        return null;
    }
}