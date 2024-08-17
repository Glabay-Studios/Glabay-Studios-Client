package com.client.camera.impl.absract;

import com.client.camera.CameraUtils;

public abstract class StaticCameraAbstract {

    private int targetValue;
    private int currentValue;
    private double interpolationFactor;
    private int interpolationParameter;

    /**
     * Constructs a new CameraDataAbstract instance.
     *
     * @param targetValue The target value for the camera.
     * @param interpolationParameter A parameter used in interpolation, must be between 0 and 27.
     */
    protected StaticCameraAbstract(int targetValue, int interpolationParameter) {
        this.currentValue = 0;
        this.interpolationFactor = 0.0D;
        this.targetValue = targetValue;
        this.interpolationParameter = validateInterpolationParameter(interpolationParameter);
        this.interpolationFactor = CameraUtils.calculateInterpolationFactor(this.currentValue, this.targetValue, this.interpolationParameter);
    }

    /**
     * Updates the camera state, incrementing the current value and recalculating the interpolation factor.
     */
    public void updateCamera() {
        if (this.currentValue < this.targetValue) {
            ++this.currentValue;
            this.interpolationFactor = CameraUtils.calculateInterpolationFactor(this.currentValue, this.targetValue, this.interpolationParameter);
        }
    }

    /**
     * Returns the current interpolation factor.
     *
     * @return The current interpolation factor.
     */
    protected double getInterpolationFactor() {
        return this.interpolationFactor;
    }

    /**
     * Validates and adjusts the interpolation parameter to ensure it's within the expected range.
     *
     * @param parameter The interpolation parameter to validate.
     * @return The validated interpolation parameter.
     */
    private int validateInterpolationParameter(int parameter) {
        return parameter >= 0 && parameter <= 27 ? parameter : 0;
    }
}