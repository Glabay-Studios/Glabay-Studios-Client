package com.client.camera.impl.absract;


public abstract class AdvancedCameraAbstract extends StaticCameraAbstract {
    protected AdvancedCameraAbstract(int targetValue, int interpolationParameter) {
        super(targetValue, interpolationParameter);
    }


    public abstract int getCameraX();


    public abstract int getCameraZ();


    public abstract int getCameraY();
}
