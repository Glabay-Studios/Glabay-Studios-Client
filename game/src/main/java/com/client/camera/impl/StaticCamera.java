package com.client.camera.impl;

import com.client.camera.impl.absract.StaticCameraAbstract;

public class StaticCamera extends StaticCameraAbstract {

    private final int initialYaw;
    private final int finalYaw;

    /**
     * Constructs a new CameraData instance.
     *
     * @param initialYaw The initial camera yaw value.
     * @param finalYaw The final camera yaw value.
     * @param targetValue A parameter inherited from CameraDataAbstract.
     * @param interpolationParameter Another parameter inherited from CameraDataAbstract.
     */
    public StaticCamera(int initialYaw, int finalYaw, int targetValue, int interpolationParameter) {
        super(targetValue, interpolationParameter);
        this.initialYaw = initialYaw;
        this.finalYaw = finalYaw;
    }

    /**
     * Calculates and returns the current camera yaw.
     *
     * @return The current camera yaw as an integer.
     */
    public int getCameraYaw() {
        double interpolationFactor = this.getInterpolationFactor();
        return (int) Math.round(initialYaw + interpolationFactor * (finalYaw - initialYaw));
    }
}