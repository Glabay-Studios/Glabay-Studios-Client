package com.client.draw.login.flames;

import com.client.Client;
import com.client.Rasterizer2D;
import com.client.Sprite;
import com.client.draw.ImageCache;

import java.util.Arrays;

public class FlameManager {

    private final int[] flameOffsets;

    private int flameFrameCounter;

    private int random1;

    private int random2;

    private int[] flameStrengths;

    private int[] tempFlameStrengths;

    private int[] titleFlames;

    private int[] titleFlamesTemp;

    private int animationFrame;

    private int animationOffset;

    private int currentCycle;

    private FlameColours flameColours;

    public FlameManager() {
        flameOffsets = new int[256];
        flameFrameCounter = 0;
        random1 = 0;
        random2 = 0;
        animationFrame = 0;
        animationOffset = 0;
        currentCycle = 0;

        initColors();
    }


    void initColors() {
        flameColours = new FlameColours();
        animationFrame = 0;
        titleFlames = new int[32768];
        titleFlamesTemp = new int[32768];
        updateFlameShape(null);
        flameStrengths = new int[32768];
        tempFlameStrengths = new int[32768];
    }


    public void reset() {
        flameColours.redFlameColours = null;
        flameColours.greenFlameColours = null;
        flameColours.blueFlameColours = null;
        flameColours.currentFlameColours = null;
        titleFlames = null;
        titleFlamesTemp = null;
        flameStrengths = null;
        tempFlameStrengths = null;
        animationFrame = 0;
        animationOffset = 0;
    }


    public void draw(int xPosition, int yPosition,int cycle, int alpha) {
        if (flameStrengths == null) {
            initColors();
        }

        if (currentCycle == 0) {
            currentCycle = cycle;
        }

        int lastCycle = cycle - currentCycle;
        if (lastCycle >= 256) {
            lastCycle = 0;
        }

        currentCycle = cycle;
        if (lastCycle > 0) {
            updateFlameAnimation(lastCycle);
        }

        initializeFlames(xPosition,yPosition,alpha);
    }

    final void updateFlameAnimation(int timeElapsed) {
        animationFrame += timeElapsed * 128;
        int index;
        if (animationFrame > titleFlames.length) {
            animationFrame -= titleFlames.length;
            updateFlameShape(ImageCache.get(FlameImages.getRandomImage()));
        }

        index = 0;
        int originalIndex = timeElapsed * 128;
        int currentTime = (256 - timeElapsed) * 128;

        int remainingIndex;
        for (int strengthIndex = 0; strengthIndex < currentTime; ++strengthIndex) {
            remainingIndex = flameStrengths[originalIndex + index] - titleFlames[index + animationFrame & titleFlames.length - 1] * timeElapsed / 6;
            if (remainingIndex < 0) {
                remainingIndex = 0;
            }

            flameStrengths[index++] = remainingIndex;
        }

        byte threshold = 10;
        int  variance = 128 - threshold;

        int rand;
        int offset;
        for (rand = 256 - timeElapsed; rand < 256; ++rand) {
            int baseOffset = rand * 128;

            for (int currentOffset = 0; currentOffset < 128; ++currentOffset) {
                offset = (int)(Math.random() * 100.0D);
                if (offset < 50 && currentOffset > threshold && currentOffset < variance) {
                    flameStrengths[currentOffset + baseOffset] = 255;
                } else {
                    flameStrengths[baseOffset + currentOffset] = 0;
                }
            }
        }

        if (random1 > 0) {
            random1 -= timeElapsed * 4;
        }

        if (random2 > 0) {
            random2 -= timeElapsed * 4;
        }

        if (random1 == 0 && random2 == 0) {
            rand = (int)(Math.random() * (double)(2000 / timeElapsed));
            if (rand == 0) {
                random1 = 1024;
            }

            if (rand == 1) {
                random2 = 1024;
            }
        }

        for (rand = 0; rand < 256 - timeElapsed; ++rand) {
            flameOffsets[rand] = flameOffsets[rand + timeElapsed];
        }

        for (rand = 256 - timeElapsed; rand < 256; ++rand) {
            flameOffsets[rand] = (int)(Math.sin((double) flameFrameCounter / 14.0D) * 16.0D + Math.sin((double) flameFrameCounter / 15.0D) * 14.0D + Math.sin((double) flameFrameCounter / 16.0D) * 12.0D);
            ++flameFrameCounter;
        }

        animationOffset += timeElapsed;
        rand = ((Client.loopCycle & 1) + timeElapsed) / 2;
        if (rand > 0) {
            short amplitude = 128;
            byte yOffset = 2;
            offset = 128 - yOffset - yOffset;

            int x;
            int y;
            int z;
            for (x = 0; x < animationOffset * 100; ++x) {
                y = (int)(Math.random() * (double)offset) + yOffset;
                z = (int)(Math.random() * (double)amplitude) + amplitude;
                flameStrengths[y + (z << 7)] = 192;
            }

            animationOffset = 0;

            int xIndex;
            for (x = 0; x < 256; ++x) {
                int accumulator = 0;
                int zIndex = x * 128;

                for (xIndex = -rand; xIndex < 128; ++xIndex) {
                    if (xIndex + rand < 128) {
                        accumulator += flameStrengths[rand + zIndex + xIndex];
                    }

                    if (xIndex - (rand + 1) >= 0) {
                        accumulator -= flameStrengths[zIndex + xIndex - (rand + 1)];
                    }

                    if (xIndex >= 0) {
                        tempFlameStrengths[zIndex + xIndex] = accumulator / (rand * 2 + 1);
                    }
                }
            }

            for (x = 0; x < 128; ++x) {
                y = 0;

                for (z = -rand; z < 256; ++z) {
                    xIndex = z * 128;
                    if (rand + z < 256) {
                        y += tempFlameStrengths[x + xIndex + rand * 128];
                    }

                    if (z - (rand + 1) >= 0) {
                        y -= tempFlameStrengths[xIndex + x - (rand + 1) * 128];
                    }

                    if (z >= 0) {
                        flameStrengths[xIndex + x] = y / (rand * 2 + 1);
                    }
                }
            }
        }

    }

    final int rotateFlameColour(int r, int g, int b) {
        int alpha = 256 - b;
        return ((r & 0xFF00FF) * alpha + (g & 0xFF00FF) * b & 0xFF00FF00) + ((r & 0x00FF00) * alpha + (g & 0x00FF00) * b & 0xFF0000) >> 8;
    }


    final void initializeFlames(int xPosition, int yPosition, int alpha) {
        int length = flameColours.currentFlameColours.length;
        if (random1 > 0) {
            changeColours(random1, flameColours.greenFlameColours);
        } else if (random2 > 0) {
            changeColours(random2, flameColours.blueFlameColours);
        } else {
            System.arraycopy(flameColours.redFlameColours, 0, flameColours.currentFlameColours, 0, length);
        }

        drawFlames(xPosition,yPosition,alpha);
    }

    final void changeColours(int random1, int[] colors) {
        int currentColorSize = flameColours.currentFlameColours.length;

        for (int strength = 0; strength < currentColorSize; ++strength) {
            if (random1 > 768) {
                flameColours.currentFlameColours[strength] = rotateFlameColour(flameColours.redFlameColours[strength], colors[strength], 1024 - random1);
            } else if (random1 > 256) {
                flameColours.currentFlameColours[strength] = colors[strength];
            } else {
                flameColours.currentFlameColours[strength] = rotateFlameColour(colors[strength], flameColours.redFlameColours[strength], 256 - random1);
            }
        }

    }


    final void drawFlames(int xPosition, int yPosition, int alpha) {
        int pixelIndex = 0;

        for (int offset = 1; offset < 255; ++offset) {
            int strength = (256 - offset) * flameOffsets[offset] / 256;
            int xPos = strength + xPosition;
            int clippingStart = Math.max(0, -xPos);
            int clippingEnd = Math.min(128, Client.rasterProvider.width - xPos);

            if (xPos + clippingEnd >= Client.rasterProvider.width) {
                clippingEnd = Client.rasterProvider.width - xPos;
            }

            int rowStart = xPos + (offset + 8 + yPosition) * Client.rasterProvider.width;
            pixelIndex += clippingStart;

            for (int xPixel = clippingStart; xPixel < clippingEnd; ++xPixel) {
                int currentStrength = flameStrengths[pixelIndex++];
                int currentColumn = rowStart % Rasterizer2D.width;

                if (currentStrength != 0 && currentColumn >= Rasterizer2D.xClipStart && currentColumn < Rasterizer2D.xClipEnd) {
                    int inverseStrength = 256 - currentStrength;
                    int currentColor = flameColours.getCurrentColour(currentStrength);
                    int background = Client.rasterProvider.pixels[rowStart];

                    int blendedColor = -16777216 | (currentStrength * (currentColor & 65280) + inverseStrength * (background & 65280) & 16711680)
                            + ((currentColor & 16711935) * currentStrength + (background & 16711935) * inverseStrength & -16711936) >> 8;

                    Rasterizer2D.drawAlpha(Client.rasterProvider.pixels, rowStart++, blendedColor, alpha);
                } else {
                    ++rowStart;
                }
            }

            pixelIndex += 128 - clippingEnd;
        }
    }


    final void updateFlameShape(Sprite runeImage) {
        final int alpha = 256;

        Arrays.fill(titleFlames, 0);

        for (int i = 0; i < 5000; i++) {
            final int pos = (int) (Math.random() * 128D * alpha);
            titleFlames[pos] = (int) (Math.random() * 256D);
        }

        for (int i = 0; i < 20; i++) {
            for (int x = 1; x < 256 - 1; x++) {
                for (int y = 1; y < 127; y++) {
                    final int pos = y + (x << 7);
                    titleFlamesTemp[pos] = (
                            titleFlames[pos - 1]
                                    + titleFlames[pos + 1]
                                    + titleFlames[pos - 128]
                                    + titleFlames[pos + 128]
                    ) / 4;
                }
            }

            final int[] temp = titleFlames;
            titleFlames = titleFlamesTemp;
            titleFlamesTemp = temp;
        }

        if (runeImage != null) {
            int imagePos = 0;
            for (int y = 0; y < runeImage.myHeight; ++y) {
                for (int x = 0; x < runeImage.myWidth; ++x) {
                    if (runeImage.myPixels[imagePos++] != 0) {
                        int xOffset = x + runeImage.drawOffsetX + 16;
                        int yOffset = y + runeImage.drawOffsetY + 16;
                        int pixelIndex = xOffset + (yOffset << 7);
                        this.titleFlames[pixelIndex] = 0;
                    }
                }
            }
        }

    }

}