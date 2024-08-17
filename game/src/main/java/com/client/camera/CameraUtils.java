package com.client.camera;

import com.client.Client;

public class CameraUtils {

    public static double calculateInterpolationFactor(int var0, int var1, int var2) {
        double var3 = var1 > 0 ? (double)Math.max(0.0F, Math.min(1.0F, (float)var0 / (float)var1)) : 1.0D;
        if (var3 > 0.0D && var3 < 1.0D) {
            double var5;
            double var7;
            switch(var2) {
                case 0:
                    return var3;
                case 1:
                    return 1.0D - Math.cos(3.141592653589793D * var3 / 2.0D);
                case 2:
                    return Math.sin(3.141592653589793D * var3 / 2.0D);
                case 3:
                    return -(Math.cos(3.141592653589793D * var3) - 1.0D) / 2.0D;
                case 4:
                    return var3 * var3;
                case 5:
                    return 1.0D - (1.0D - var3) * (1.0D - var3);
                case 6:
                    return var3 < 0.5D ? var3 * 2.0D * var3 : 1.0D - Math.pow(2.0D + var3 * -2.0D, 2.0D) / 2.0D;
                case 7:
                    return var3 * var3 * var3;
                case 8:
                    return 1.0D - Math.pow(1.0D - var3, 3.0D);
                case 9:
                    return var3 < 0.5D ? var3 * 4.0D * var3 * var3 : 1.0D - Math.pow(var3 * -2.0D + 2.0D, 3.0D) / 2.0D;
                case 10:
                    return var3 * var3 * var3 * var3;
                case 11:
                    return 1.0D - Math.pow(1.0D - var3, 4.0D);
                case 12:
                    return var3 < 0.5D ? var3 * var3 * var3 * 8.0D * var3 : 1.0D - Math.pow(var3 * -2.0D + 2.0D, 4.0D) / 2.0D;
                case 13:
                    return var3 * var3 * var3 * var3 * var3;
                case 14:
                    return 1.0D - Math.pow(1.0D - var3, 5.0D);
                case 15:
                    return var3 < 0.5D ? var3 * 8.0D * var3 * var3 * var3 * var3 : 1.0D - Math.pow(-2.0D * var3 + 2.0D, 5.0D) / 2.0D;
                case 16:
                    return Math.pow(2.0D, var3 * 10.0D - 10.0D);
                case 17:
                    return 1.0D - Math.pow(2.0D, var3 * -10.0D);
                case 18:
                    return var3 < 0.5D ? Math.pow(2.0D, 10.0D + var3 * 20.0D) / 2.0D : (2.0D - Math.pow(2.0D, 10.0D + var3 * -20.0D)) / 2.0D;
                case 19:
                    return 1.0D - Math.sqrt(1.0D - Math.pow(var3, 2.0D));
                case 20:
                    return Math.sqrt(1.0D - Math.pow(var3 - 1.0D, 2.0D));
                case 21:
                    return var3 < 0.5D ? (1.0D - Math.sqrt(1.0D - Math.pow(2.0D * var3, 2.0D))) / 2.0D : (Math.sqrt(1.0D - Math.pow(-2.0D * var3 + 2.0D, 2.0D)) + 1.0D) / 2.0D;
                case 22:
                    var5 = 1.70158D;
                    var7 = 2.70158D;
                    return var3 * var3 * 2.70158D * var3 - 1.70158D * var3 * var3;
                case 23:
                    var5 = 1.70158D;
                    var7 = 2.70158D;
                    return 1.0D + 2.70158D * Math.pow(var3 - 1.0D, 3.0D) + 1.70158D * Math.pow(var3 - 1.0D, 2.0D);
                case 24:
                    var5 = 1.70158D;
                    var7 = 2.5949095D;
                    return var3 < 0.5D ? Math.pow(var3 * 2.0D, 2.0D) * (var3 * 7.189819D - 2.5949095D) / 2.0D : (Math.pow(var3 * 2.0D - 2.0D, 2.0D) * (3.5949095D * (2.0D * var3 - 2.0D) + 2.5949095D) + 2.0D) / 2.0D;
                case 25:
                    var5 = 2.0943951023931953D;
                    return -Math.pow(2.0D, 10.0D * var3 - 10.0D) * Math.sin((var3 * 10.0D - 10.75D) * 2.0943951023931953D);
                case 26:
                    var5 = 2.0943951023931953D;
                    return Math.pow(2.0D, var3 * -10.0D) * Math.sin(2.0943951023931953D * (10.0D * var3 - 0.75D)) + 1.0D;
                case 27:
                    var5 = 1.3962634015954636D;
                    var7 = Math.sin(1.3962634015954636D * (var3 * 20.0D - 11.125D));
                    return var3 < 0.5D ? -(Math.pow(2.0D, 20.0D * var3 - 10.0D) * var7) / 2.0D : Math.pow(2.0D, 10.0D + var3 * -20.0D) * var7 / 2.0D + 1.0D;
                default:
                    return var3;
            }
        } else {
            return var3 <= 0.0D ? 0.0D : 1.0D;
        }
    }


    /**
     * Adjusts the given camera yaw angle relative to the current camera yaw of the client instance.
     * If the difference between the given yaw and the current camera yaw is greater than 1024,
     * it adjusts the yaw by 2048 in the appropriate direction.
     *
     * @param yaw The camera yaw angle to be adjusted.
     * @return The adjusted camera yaw angle.
     */
    public static final int adjustCameraYaw(int yaw) {
        int currentCameraYaw = Client.instance.cameraYaw;
        boolean isDifferenceLarge = Math.abs(yaw - currentCameraYaw) > 1024;
        if (isDifferenceLarge) {
            int directionMultiplier = yaw < currentCameraYaw ? 1 : -1;
            return 2048 * directionMultiplier + yaw;
        }
        return yaw;
    }

    /**
     * Clamps the given value within a specified range.
     *
     * @param value The value to be clamped.
     * @return The clamped value, constrained between 128 and 383.
     */
    public static final int clampValue(int value) {
        return Math.min(Math.max(value, 128), 383);
    }

}
