package com.client.definitions.anim.skeleton.task;

import com.client.definitions.anim.skeleton.AnimTransform;
import com.client.definitions.anim.skeleton.AnimationChannel;
import com.client.definitions.anim.skeleton.AnimationKeyFrame;
import com.client.definitions.anim.skeleton.SkeletalFrameHandler;

import java.util.concurrent.Callable;

public class AnimationKeyFrameTask implements Callable<Void> {

    private final AnimationKeyFrame keyFrame;
    private final AnimTransform transformType;
    private final AnimationChannel channel;
    private final int index;
    private final SkeletalFrameHandler keyFrameSet;

    public AnimationKeyFrameTask(SkeletalFrameHandler animKeyFrameSet, AnimationKeyFrame animationKeyFrame, AnimTransform animTransform, AnimationChannel animationChannel, int index) {
        this.keyFrameSet = animKeyFrameSet;
        this.keyFrame = animationKeyFrame;
        this.transformType = animTransform;
        this.channel = animationChannel;
        this.index = index;
    }

    @Override
    public Void call() {
        this.keyFrame.deserialise();
        AnimationKeyFrame[][] keyFramesArray;
        if (this.transformType == AnimTransform.VERTEX) {
            keyFramesArray = this.keyFrameSet.skeletalTransforms;
        } else {
            keyFramesArray = this.keyFrameSet.transforms;
        }

        keyFramesArray[this.index][this.channel.getComponent()] = this.keyFrame;
        return null;
    }

}