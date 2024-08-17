package com.client.camera.impl;

import com.client.camera.impl.absract.AdvancedCameraAbstract;

public class FollowCameraSimple extends AdvancedCameraAbstract {

    private int initialX;
    private int initialY;
    private int initialZ;
    private int targetX;
    private int targetY;
    private int targetZ;

    /**
     * Constructs a new FollowCameraSimple instance.
     *
     * @param initialX Initial X position of the camera.
     * @param initialY Initial Y position of the camera.
     * @param initialZ Initial Z position of the camera.
     * @param targetX Target X position of the camera.
     * @param targetY Target Y position of the camera.
     * @param targetZ Target Z position of the camera.
     * @param targetValue A parameter inherited from AdvancedCameraAbstract.
     * @param interpolationParameter Another parameter inherited from AdvancedCameraAbstract.
     */
    public FollowCameraSimple(int initialX, int initialY, int initialZ, int targetX, int targetY, int targetZ, int targetValue, int interpolationParameter) {
        super(targetValue, interpolationParameter);
        this.initialX = initialX;
        this.initialY = initialY;
        this.initialZ = initialZ;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    /**
     * Calculates and returns the interpolated X position of the camera.
     *
     * @return The current interpolated X position of the camera.
     */
    public int getCameraX() {
        double interpolationFactor = this.getInterpolationFactor();
        return (int) Math.round(interpolationFactor * (targetX - initialX) + initialX);
    }

    /**
     * Calculates and returns the interpolated Y position of the camera.
     *
     * @return The current interpolated Y position of the camera.
     */
    public int getCameraY() {
        double interpolationFactor = this.getInterpolationFactor();
        return (int) Math.round(interpolationFactor * (targetY - initialY) + initialY);
    }

    /**
     * Calculates and returns the interpolated Z position of the camera.
     *
     * @return The current interpolated Z position of the camera.
     */
    public int getCameraZ() {
        double interpolationFactor = this.getInterpolationFactor();
        return (int) Math.round(interpolationFactor * (targetZ - initialZ) + initialZ);
    }

}