package com.client.camera;

import com.client.Client;
import com.client.Configuration;
import com.client.camera.impl.StaticCamera;
import com.client.camera.impl.absract.AdvancedCameraAbstract;
import com.client.draw.Rasterizer3D;
import com.client.engine.impl.MouseHandler;

import static com.client.Client.*;

public class Camera {

    public static int mouseCamClickedY = 0;
    public static int mouseCamClickedX = 0;

    public static boolean shouldUpdateFocalPoint = false;

    public static int cameraAltitudeAdjustment = 0;

    public static StaticCamera cameraYaw;

    public static StaticCamera cameraPitch;


    public static int oculusOrbSpeed = 0;
    public static int orbCameraDirection = 0;
    public static int orbVerticalDirection = 0;
    public static boolean isCameraUpdating = false;
    public static int staticCameraX = 0;
    public static boolean staticCameraMode = false;
    public static int cameraPitchStep;
    public static int cameraYawSpeed;
    public static int cameraInterpolationSpeed;
    public static int cameraMinimumStep;
    public static int cameraTargetX;
    public static int cameraTargetY;
    public static int cameraAltitudeOffset;
    public static AdvancedCameraAbstract camera = null;

    public static boolean followCameraMode = false;
    public static int staticCameraAltitudeOffset;

    public static int staticCameraY;
    public static boolean fixedCamera = false;

    public static int oculusOrbAltitude;
    public static int targetCameraX;
    public static int targetCameraY;
    public static int targetCameraZ;
    public static int targetCameraPitch;
    public static int targetCameraYaw;
    
    public static void updateCamera() {
        if (Client.instance.oculusOrbState == 0) {
            int playerX = localPlayer.x;
            int playerY = localPlayer.y;
            if (Client.instance.oculusOrbFocalPointX - playerX < -500 || Client.instance.oculusOrbFocalPointX - playerX > 500 || Client.instance.oculusOrbFocalPointY - playerY < -500 || Client.instance.oculusOrbFocalPointY - playerY > 500) {
                Client.instance.oculusOrbFocalPointX = playerX;
                Client.instance.oculusOrbFocalPointY = playerY;
            }

            if (playerX != Client.instance.oculusOrbFocalPointX) {
                Client.instance.oculusOrbFocalPointX += (playerX - Client.instance.oculusOrbFocalPointX) / 16;
            }

            if (playerY != Client.instance.oculusOrbFocalPointY) {
                Client.instance.oculusOrbFocalPointY += (playerY - Client.instance.oculusOrbFocalPointY) / 16;
            }

            int focalPointTileX = Client.instance.oculusOrbFocalPointX >> 7;
            int focalPointTileY = Client.instance.oculusOrbFocalPointY >> 7;
            int highestHeight = Client.instance.getCenterHeight(Client.instance.plane, Client.instance.oculusOrbFocalPointY, Client.instance.oculusOrbFocalPointX);
            int maxTileHeightDiff = 0;
            if (focalPointTileX > 3 && focalPointTileY > 3 && focalPointTileX < 100 && focalPointTileY < 100) {
                for (int x = focalPointTileX - 4; x <= focalPointTileX + 4; ++x) {
                    for (int y = focalPointTileY - 4; y <= focalPointTileY + 4; ++y) {
                        int effectivePlane = Client.instance.plane;
                        if (effectivePlane < 3 && (instance.tileFlags[1][x][y] & 2) == 2) {
                            ++effectivePlane;
                        }

                        int heightDifference = highestHeight - instance.tileHeights[effectivePlane][x][y];
                        if (heightDifference > maxTileHeightDiff) {
                            maxTileHeightDiff = heightDifference;
                        }
                    }
                }
            }

            int adjustedHeight = maxTileHeightDiff * 192;
            if (adjustedHeight > 98048) {
                adjustedHeight = 98048;
            }

            if (adjustedHeight < 32768) {
                adjustedHeight = 32768;
            }

            if (adjustedHeight > cameraAltitudeAdjustment) {
                cameraAltitudeAdjustment += (adjustedHeight - cameraAltitudeAdjustment) / 24;
            } else if (adjustedHeight < cameraAltitudeAdjustment) {
                cameraAltitudeAdjustment += (adjustedHeight - cameraAltitudeAdjustment) / 80;
            }

            oculusOrbAltitude = Client.instance.getCenterHeight(Client.instance.plane,localPlayer.y, localPlayer.x) - camFollowHeight;
        } else if (Client.instance.oculusOrbState == 1) {
            if (shouldUpdateFocalPoint && localPlayer != null) {
                int playerFirstPathX = localPlayer.pathX[0];
                int playerFirstPathY = localPlayer.pathY[0];
                if (playerFirstPathX >= 0 && playerFirstPathY >= 0 && playerFirstPathX < 104 && playerFirstPathY < 104) {
                    Client.instance.oculusOrbFocalPointX = localPlayer.x;
                    int heightDifference = Client.instance.getCenterHeight(Client.instance.plane,localPlayer.y, localPlayer.x) - camFollowHeight;
                    if (heightDifference < oculusOrbAltitude) {
                        oculusOrbAltitude = heightDifference;
                    }

                    Client.instance.oculusOrbFocalPointY = localPlayer.y;
                    shouldUpdateFocalPoint = false;
                }
            }


            short cameraDirection = -1;
            if (keyManager.isKeyPressed(33)) {
                cameraDirection = 0;
            } else if (keyManager.isKeyPressed(49)) {
                cameraDirection = 1024;
            }

            if (keyManager.isKeyPressed(48)) {
                if (cameraDirection == 0) {
                    cameraDirection = 1792;
                } else if (cameraDirection == 1024) {
                    cameraDirection = 1280;
                } else {
                    cameraDirection = 1536;
                }
            } else if (keyManager.isKeyPressed(50)) {
                if (cameraDirection == 0) {
                    cameraDirection = 256;
                } else if (cameraDirection == 1024) {
                    cameraDirection = 768;
                } else {
                    cameraDirection = 512;
                }
            }

            byte verticalDirection = 0;
            if (keyManager.isKeyPressed(35)) {
                verticalDirection = -1;
            } else if (keyManager.isKeyPressed(51)) {
                verticalDirection = 1;
            }

            int movementSpeed = 0;
            if (cameraDirection >= 0 || verticalDirection != 0) {
                movementSpeed = keyManager.isKeyPressed(81) ? oculusOrbSlowedSpeed : oculusOrbNormalSpeed;
                movementSpeed *= 16;
                orbCameraDirection = cameraDirection;
                orbVerticalDirection = verticalDirection;
            }

            if (oculusOrbSpeed < movementSpeed) {
                oculusOrbSpeed += movementSpeed / 8;
                if (oculusOrbSpeed > movementSpeed) {
                    oculusOrbSpeed = movementSpeed;
                }
            } else if (oculusOrbSpeed > movementSpeed) {
                oculusOrbSpeed = oculusOrbSpeed * 9 / 10;
            }

            if (oculusOrbSpeed > 0) {
                int speedDivisor = oculusOrbSpeed / 16;
                if (orbCameraDirection >= 0) {
                    int angle = orbCameraDirection - Client.instance.cameraYaw & 2047;
                    int deltaX = Rasterizer3D.SINE[angle];
                    int deltaY = Rasterizer3D.COSINE[angle];
                    Client.instance.oculusOrbFocalPointX += speedDivisor * deltaX / 65536;
                    Client.instance.oculusOrbFocalPointY += deltaY * speedDivisor / 65536;
                }

                if (orbVerticalDirection != 0) {
                    oculusOrbAltitude += speedDivisor * orbVerticalDirection;
                    if (oculusOrbAltitude > 0) {
                        oculusOrbAltitude = 0;
                    }
                }
            } else {
                orbCameraDirection = -1;
                orbVerticalDirection = -1;
            }

            if (keyManager.isKeyPressed(13)) {
                Client.instance.oculusOrbState = 0;
            }
        }

        if (MouseHandler.currentButton == 4 && Configuration.MOUSE_CAM) {
            int mouseYDelta = MouseHandler.mouseY - mouseCamClickedY;
            Client.instance.camAngleDX = mouseYDelta * 2;
            mouseCamClickedY = mouseYDelta != -1 && mouseYDelta != 1 ? (MouseHandler.mouseY + mouseCamClickedY) / 2 : MouseHandler.mouseY;
            int mouseXDelta = mouseCamClickedX - MouseHandler.mouseX;
            Client.instance.camAngleDY = mouseXDelta * 2;
            mouseCamClickedX = mouseXDelta != -1 && mouseXDelta != 1 ? (mouseCamClickedX + MouseHandler.mouseX) / 2 : MouseHandler.mouseX;
        } else {

            if (keyManager.isKeyPressed(96)) {
                Client.instance.camAngleDY += (-24 - Client.instance.camAngleDY) / 2;
            } else if (keyManager.isKeyPressed(97)) {
                Client.instance.camAngleDY += (24 - Client.instance.camAngleDY) / 2;
            } else {
                Client.instance.camAngleDY /= 2;
            }
            if (keyManager.isKeyPressed(98)) {
                Client.instance.camAngleDX += (12 - Client.instance.camAngleDX) / 2;
            } else if (keyManager.isKeyPressed(99)) {
                Client.instance.camAngleDX += (-12 - Client.instance.camAngleDX) / 2;
            } else {
                Client.instance.camAngleDX /= 2;
            }
            mouseCamClickedY = MouseHandler.mouseY;
            mouseCamClickedX = MouseHandler.mouseX;
        }

        Client.instance.camAngleY = Client.instance.camAngleDY / 2 + Client.instance.camAngleY & 2047;
        Client.instance.camAngleX += Client.instance.camAngleDX / 2;

        if (Client.instance.camAngleX < 128) {
            Client.instance.camAngleX = 128;
        }

        if (Client.instance.camAngleX > 383) {
            Client.instance.camAngleX = 383;
        }

        if (isCameraUpdating) {
            updateCameraPosition(targetCameraX, targetCameraY, targetCameraZ);
            updateCameraOrientation(targetCameraPitch, targetCameraYaw);
            if (targetCameraX == Client.instance.cameraX && Client.instance.cameraY == targetCameraY && targetCameraZ == Client.instance.cameraZ && targetCameraPitch == Client.instance.cameraPitch && Client.instance.cameraYaw == targetCameraYaw) {
                isCameraUpdating = false;
                Client.instance.isCameraLocked = false;
                followCameraMode = false;
                staticCameraMode = false;
                staticCameraX = 0;
                staticCameraY = 0;
                staticCameraAltitudeOffset = 0;
                cameraPitchStep = 0;
                cameraYawSpeed = 0;
                cameraInterpolationSpeed = 0;
                cameraMinimumStep = 0;
                cameraTargetX = 0;
                cameraTargetY = 0;
                cameraAltitudeOffset = 0;
                camera = null;
                cameraPitch = null;
                cameraYaw = null;
            }
        } else if (Client.instance.isCameraLocked) {
            updateCameraAndView();
        }

        for (int counter = 0; counter < 5; ++counter) {
            Client.instance.cameraUpdateCounters[counter]++;
        }

        Client.instance.onCamAngleDXChange();
        Client.instance.onCamAngleDYChange();
        onCameraPitchTargetChanged(0);


    }

    private static void updateCameraPosition(int targetX, int targetY, int targetZ) {
        if (Client.instance.cameraX < targetX) {
            Client.instance.cameraX = (targetX - Client.instance.cameraX) * cameraInterpolationSpeed / 1000 + Client.instance.cameraX + cameraMinimumStep;
            if (Client.instance.cameraX > targetX) {
                Client.instance.cameraX = targetX;
            }
        }

        if (Client.instance.cameraX > targetX) {
            Client.instance.cameraX -= (Client.instance.cameraX - targetX) * cameraInterpolationSpeed / 1000 + cameraMinimumStep;
            if (Client.instance.cameraX < targetX) {
                Client.instance.cameraX = targetX;
            }
        }

        if (Client.instance.cameraY < targetY) {
            Client.instance.cameraY = (targetY - Client.instance.cameraY) * cameraInterpolationSpeed / 1000 + Client.instance.cameraY + cameraMinimumStep;
            if (Client.instance.cameraY > targetY) {
                Client.instance.cameraY = targetY;
            }
        }

        if (Client.instance.cameraY > targetY) {
            Client.instance.cameraY -= (Client.instance.cameraY - targetY) * cameraInterpolationSpeed / 1000 + cameraMinimumStep;
            if (Client.instance.cameraY < targetY) {
                Client.instance.cameraY = targetY;
            }
        }

        if (Client.instance.cameraZ < targetZ) {
            Client.instance.cameraZ = (targetZ - Client.instance.cameraZ) * cameraInterpolationSpeed / 1000 + Client.instance.cameraZ + cameraMinimumStep;
            if (Client.instance.cameraZ > targetZ) {
                Client.instance.cameraZ = targetZ;
            }
        }

        if (Client.instance.cameraZ > targetZ) {
            Client.instance.cameraZ -= (Client.instance.cameraZ - targetZ) * cameraInterpolationSpeed / 1000 + cameraMinimumStep;
            if (Client.instance.cameraZ < targetZ) {
                Client.instance.cameraZ = targetZ;
            }
        }

    }


    private static void updateCameraOrientation(int targetPitch, int targetYaw) {
        if (targetPitch < 128) {
            targetPitch = 128;
        } else if (targetPitch > 383) {
            targetPitch = 383;
        }

        if (Client.instance.cameraPitch < targetPitch) {
            Client.instance.cameraPitch = (targetPitch - Client.instance.cameraPitch) * cameraYawSpeed / 1000 + Client.instance.cameraPitch + cameraPitchStep;
            if (Client.instance.cameraPitch > targetPitch) {
                Client.instance.cameraPitch = targetPitch;
            }
        } else if (Client.instance.cameraPitch > targetPitch) {
            Client.instance.cameraPitch -= (Client.instance.cameraPitch - targetPitch) * cameraYawSpeed / 1000 + cameraPitchStep;
            if (Client.instance.cameraPitch < targetPitch) {
                Client.instance.cameraPitch = targetPitch;
            }
        }

        targetYaw &= 2047;
        int yawDifference = targetYaw - Client.instance.cameraYaw;
        if (yawDifference > 1024) {
            yawDifference -= 2048;
        } else if (yawDifference < -1024) {
            yawDifference += 2048;
        }

        if (yawDifference > 0) {
            Client.instance.cameraYaw = Client.instance.cameraYaw + cameraPitchStep + yawDifference * cameraYawSpeed / 1000;
            Client.instance.cameraYaw &= 2047;
        } else if (yawDifference < 0) {
            Client.instance.cameraYaw -= -yawDifference * cameraYawSpeed / 1000 + cameraPitchStep;
            Client.instance.cameraYaw &= 2047;
        }

        int finalYawDifference = targetYaw - Client.instance.cameraYaw;
        if (finalYawDifference > 1024) {
            finalYawDifference -= 2048;
        } else if (finalYawDifference < -1024) {
            finalYawDifference += 2048;
        }

        if (finalYawDifference < 0 && yawDifference > 0 || finalYawDifference > 0 && yawDifference < 0) {
            Client.instance.cameraYaw = targetYaw;
        }
        onCameraPitchChanged(0);

    }

    private static void updateCameraAndView() {
        int targetX;
        int targetY;
        int targetZ;
        if (!followCameraMode) {
            targetX = cameraTargetX * 128 + 64;
            targetY = cameraTargetY * 128 + 64;
            targetZ = Client.instance.getCenterHeight(Client.instance.plane, targetY, targetX) - cameraAltitudeOffset;
            updateCameraPosition(targetX, targetZ, targetY);
        } else if (camera != null) {
            Client.instance.cameraX = camera.getCameraX();
            Client.instance.cameraZ = camera.getCameraZ();
            if (fixedCamera) {
                Client.instance.cameraY = camera.getCameraY();
            } else {
                Client.instance.cameraY = Client.instance.getCenterHeight(Client.instance.plane, Client.instance.cameraZ, Client.instance.cameraX) - camera.getCameraY();
            }

            camera.updateCamera();
        }

        if (!staticCameraMode) {
            targetX = staticCameraX * 16384 + 64;
            targetY = staticCameraY * 16384 + 64;
            targetZ = Client.instance.getCenterHeight(Client.instance.plane, targetY, targetX) - staticCameraAltitudeOffset;
            int deltaX = targetX - Client.instance.cameraX;
            int deltaZ = targetZ - Client.instance.cameraY;
            int deltaY = targetY - Client.instance.cameraZ;
            int distance = (int) Math.sqrt((double) (deltaX * deltaX + deltaY * deltaY));
            int pitchAngle = (int) (Math.atan2((double) deltaZ, (double) distance) * 325.9490051269531D) & 2047;
            int yawAngle = (int) (Math.atan2((double) deltaX, (double) deltaY) * -325.9490051269531D) & 2047;
            updateCameraOrientation(pitchAngle, yawAngle);
        } else {
            if (cameraPitch != null) {
                Client.instance.cameraPitch = cameraPitch.getCameraYaw();
                Client.instance.cameraPitch = Math.min(Math.max(Client.instance.cameraPitch, 128), 383);
                cameraPitch.updateCamera();
            }

            if (cameraYaw != null) {
                Client.instance.cameraYaw = cameraYaw.getCameraYaw() & 2047;
                cameraYaw.updateCamera();
            }
        }

    }

    public static int determineRoofHeight() {
        if (Client.removeRoofs) {
            return Client.instance.plane;
        } else {
            int calculatedPlane = 3;
            if (Client.instance.cameraPitch < 310) {
                int focalPointX;
                int focalPointY;
                if (Client.instance.oculusOrbState == 1) {
                    focalPointX = Client.instance.oculusOrbFocalPointX >> 7;
                    focalPointY = Client.instance.oculusOrbFocalPointY >> 7;
                } else {
                    focalPointX = localPlayer.x >> 7;
                    focalPointY = localPlayer.y >> 7;
                }

                int cameraTileX = Client.instance.cameraX >> 7;
                int cameraTileZ = Client.instance.cameraZ >> 7;
                if (cameraTileX < 0 || cameraTileZ < 0 || cameraTileX >= 104 || cameraTileZ >= 104) {
                    return Client.instance.plane;
                }

                if (focalPointX < 0 || focalPointY < 0 || focalPointX >= 104 || focalPointY >= 104) {
                    return Client.instance.plane;
                }

                if ((instance.tileFlags[Client.instance.plane][cameraTileX][cameraTileZ] & 4) != 0) {
                    calculatedPlane = Client.instance.plane;
                }

                int deltaX;
                if (focalPointX > cameraTileX) {
                    deltaX = focalPointX - cameraTileX;
                } else {
                    deltaX = cameraTileX - focalPointX;
                }

                int deltaZ;
                if (focalPointY > cameraTileZ) {
                    deltaZ = focalPointY - cameraTileZ;
                } else {
                    deltaZ = cameraTileZ - focalPointY;
                }

                int stepMultiplier;
                int stepCounter;
                if (deltaX > deltaZ) {
                    stepMultiplier = deltaZ * 65536 / deltaX;
                    stepCounter = 32768;

                    while (focalPointX != cameraTileX) {
                        if (cameraTileX < focalPointX) {
                            ++cameraTileX;
                        } else if (cameraTileX > focalPointX) {
                            --cameraTileX;
                        }

                        if ((instance.tileFlags[Client.instance.plane][cameraTileX][cameraTileZ] & 4) != 0) {
                            calculatedPlane = Client.instance.plane;
                        }

                        stepCounter += stepMultiplier;
                        if (stepCounter >= 65536) {
                            stepCounter -= 65536;
                            if (cameraTileZ < focalPointY) {
                                ++cameraTileZ;
                            } else if (cameraTileZ > focalPointY) {
                                --cameraTileZ;
                            }

                            if ((instance.tileFlags[Client.instance.plane][cameraTileX][cameraTileZ] & 4) != 0) {
                                calculatedPlane = Client.instance.plane;
                            }
                        }
                    }
                } else if (deltaZ > 0) {
                    stepMultiplier = deltaX * 65536 / deltaZ;
                    stepCounter = 32768;

                    while (cameraTileZ != focalPointY) {
                        if (cameraTileZ < focalPointY) {
                            ++cameraTileZ;
                        } else if (cameraTileZ > focalPointY) {
                            --cameraTileZ;
                        }

                        if ((instance.tileFlags[Client.instance.plane][cameraTileX][cameraTileZ] & 4) != 0) {
                            calculatedPlane = Client.instance.plane;
                        }

                        stepCounter += stepMultiplier;
                        if (stepCounter >= 65536) {
                            stepCounter -= 65536;
                            if (cameraTileX < focalPointX) {
                                ++cameraTileX;
                            } else if (cameraTileX > focalPointX) {
                                --cameraTileX;
                            }

                            if ((instance.tileFlags[Client.instance.plane][cameraTileX][cameraTileZ] & 4) != 0) {
                                calculatedPlane = Client.instance.plane;
                            }
                        }
                    }
                }
            }

            if (localPlayer.x >= 0 && localPlayer.y >= 0 && localPlayer.x < 13312 && localPlayer.y < 13312) {
                if ((instance.tileFlags[Client.instance.plane][localPlayer.x >> 7][localPlayer.y >> 7] & 4) != 0) {
                    calculatedPlane = Client.instance.plane;
                }

                return calculatedPlane;
            } else {
                return Client.instance.plane;
            }
        }
    }

    public static int calculateCameraPlane() {
        if (removeRoofs) {
            return Client.instance.plane;
        } else {
            int cameraHeight = Client.instance.getCenterHeight(Client.instance.plane, Client.instance.cameraZ, Client.instance.cameraX);
            return cameraHeight - Client.instance.cameraY < 800 && (instance.tileFlags[Client.instance.plane][Client.instance.cameraX >> 7][Client.instance.cameraZ >> 7] & 4) != 0 ? Client.instance.plane : 3;
        }
    }


}
