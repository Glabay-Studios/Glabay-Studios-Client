package com.client.graphics.textures;

import com.client.*;
import com.client.collection.node.NodeDeque;
import com.client.draw.Rasterizer3D;
import com.client.js5.disk.AbstractArchive;
import net.runelite.rs.api.RSTexture;
import net.runelite.rs.api.RSTextureProvider;

public class TextureProvider implements RSTextureProvider, TextureLoader {

    private final Texture[] textures;

    private NodeDeque deque = new NodeDeque();

    private int capacity;

    private int remaining;

    private double brightness;

    private int textureSize;

    private final AbstractArchive archive;

    public TextureProvider(AbstractArchive textures, AbstractArchive var2, int capacity, double brightness, int size) {
        this.archive = var2;
        this.capacity = capacity;
        this.remaining = this.capacity;
        this.brightness = brightness;
        this.textureSize = size;
        int[] fileIds = textures.getGroupFileIds(0);
        int texturesSize = fileIds.length;
        this.textures = new Texture[textures.getGroupFileCount(0)];

        for(int index = 0; index < texturesSize; ++index) {
            Buffer data = new Buffer(textures.takeFile(0, fileIds[index]));
            this.textures[fileIds[index]] = new Texture(data);
        }

        setMaxSize(128);
        setSize(128);
    }

    public int getLoadedPercentage() {
        int totalFiles = 0;
        int loadedFiles = 0;
        for (Texture texture : textures) {
            if (texture != null && texture.fileIds != null) {
                totalFiles += texture.fileIds.length;
                for (int fileId : texture.fileIds) {
                    if (archive.method6603(fileId)) {
                        loadedFiles++;
                    }
                }
            }
        }
        return totalFiles == 0 ? 0 : (loadedFiles * 100 / totalFiles);
    }


    @Override
    public double getBrightness() {
        return brightness;
    }

    public static final void method2482(int var0) {
        var0 = Math.max(Math.min(var0, 100), 0);
        var0 = 100 - var0;
        float var1 = 0.5F + (float)var0 / 200.0F;
        Rasterizer3D.setBrightness((double) var1);
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
        clear();
    }

    public void setTextureSize(int textureSize) {
        this.textureSize = textureSize;
        clear();
    }

    @Override
    public void setMaxSize(int maxSize) {
        capacity = maxSize;
    }

    @Override
    public void setSize(int size) {
        remaining = size;
    }

    @Override
    public RSTexture[] getTextures() {
        return textures;
    }

    @Override
    public int[] load(int textureId) {
        return getTexturePixels(textureId);
    }

    public int[] getTexturePixels(int textureID) {
        Texture texture = textures[textureID];
        if (texture != null) {
            if (texture.pixels != null) {
                deque.insertTail(texture);
                texture.isLoaded = true;
                return texture.pixels;
            }

            boolean hasLoaded = texture.load(brightness, textureSize, archive);

            if (hasLoaded) {
                if (remaining == 0) {
                    Texture currentTexture = (Texture)deque.popHead();
                    currentTexture.reset();
                } else {
                    --remaining;
                }

                deque.insertTail(texture);
                texture.isLoaded = true;
                return texture.pixels;
            }
        }

        return null;
    }

    public int getAverageTextureRGB(int textureID) {
        return textures[textureID] != null ? textures[textureID].averageRGB : 0;
    }

    public boolean isTransparent(int textureID) {
        return textures[textureID].isTransparent;
    }

    @Override
    public boolean isLowDetail(int textureID) {
        return textureSize == 64;
    }

    public void clear() {
        for (Texture texture : textures) {
            if (texture != null) {
                texture.reset();
            }
        }

        deque = new NodeDeque();
        remaining = capacity;
    }

    public void animate(int textureID) {
        for (Texture texture : textures) {
            if (texture != null && texture.animationDirection != 0 && texture.isLoaded) {
                texture.animate(textureID);
                texture.isLoaded = false;
            }
        }
    }



}
