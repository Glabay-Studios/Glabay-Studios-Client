package com.client.camera.impl;

import com.client.camera.impl.absract.AdvancedCameraAbstract;

public class FollowCameraAdvanced extends AdvancedCameraAbstract {

    private int initialY;
    private int targetY;
    private double centerX;
    private double centerY;
    private double radius;
    private double startAngle;
    private double endAngle;

    /**
     * Constructs a FollowCameraAdvanced instance.
     * This camera model provides advanced camera following capabilities with various parameters to control its behavior and position.
     *
     * @param cameraX The initial X coordinate of the camera.
     * @param cameraZ The initial Z coordinate of the camera.
     * @param initialY The initial Y coordinate (elevation) of the camera.
     * @param centerX The X coordinate of the central point around which the camera might rotate or focus.
     * @param centerY The Y coordinate of the central point around which the camera might rotate or focus.
     * @param targetY The target Y coordinate (elevation) that the camera should reach or maintain.
     * @param verticalDisplacement The vertical displacement value for the camera, potentially affecting how the camera moves vertically.
     * @param offsetY The offset value in the Y direction, which can adjust the camera's position relative to the target.
     * @param targetValue A target value used in camera calculations, possibly related to the target position or state of the camera.
     * @param interpolationParameter A parameter used for interpolating camera movement or position, ensuring smooth transitions.
     */
    public FollowCameraAdvanced(int cameraX, int cameraZ, int initialY, int centerX, int centerY, int targetY, int verticalDisplacement, int offsetY, int targetValue, int interpolationParameter) {
        super(targetValue, interpolationParameter);
        this.initialY = initialY;
        this.targetY = targetY;

        // Calculate center and radius based on input parameters
        if ((centerY - offsetY) * (verticalDisplacement - cameraX) == (centerX - verticalDisplacement) * (offsetY - cameraZ)) {
            this.centerX = centerX;
            this.centerY = centerY;
        } else {
            double midX1 = (verticalDisplacement + cameraX) / 2.0;
            double midY1 = (offsetY + cameraZ) / 2.0;
            double midX2 = (centerX + verticalDisplacement) / 2.0;
            double midY2 = (centerY + offsetY) / 2.0;
            double slope1 = -1.0 * (verticalDisplacement - cameraX) / (offsetY - cameraZ);
            double slope2 = -1.0 * (centerX - verticalDisplacement) / (centerY - offsetY);

            this.centerX = (slope1 * midX1 - slope2 * midX2 + midY2 - midY1) / (slope1 - slope2);
            this.centerY = midY1 + (this.centerX - midX1) * slope1;
            this.radius = Math.sqrt(Math.pow(this.centerX - cameraX, 2.0) + Math.pow(this.centerY - cameraZ, 2.0));
            this.startAngle = Math.atan2(cameraZ - this.centerY, cameraX - this.centerX);
            this.endAngle = Math.atan2(centerY - this.centerY, centerX - this.centerX);

            // Adjust angles for consistent transition
            adjustAnglesForConsistency();
        }
    }

    private void adjustAnglesForConsistency() {
        boolean isAngleConsistent = this.startAngle <= this.endAngle && this.endAngle <= this.startAngle;
        if (!isAngleConsistent) {
            this.endAngle += Math.PI * (this.startAngle - this.endAngle > 0.0 ? 2 : -2);
        }
    }

    public int getCameraX() {
        double interpolationFactor = this.getInterpolationFactor();
        double angle = this.startAngle + interpolationFactor * (this.endAngle - this.startAngle);
        return (int) Math.round(this.centerX + this.radius * Math.cos(angle));
    }

    public int getCameraZ() {
        double interpolationFactor = this.getInterpolationFactor();
        double angle = interpolationFactor * (this.endAngle - this.startAngle) + this.startAngle;
        return (int) Math.round(this.centerY + this.radius * Math.sin(angle));
    }

    public int getCameraY() {
        double interpolationFactor = this.getInterpolationFactor();
        return (int) Math.round(interpolationFactor * (this.targetY - this.initialY) + this.initialY);
    }
}