package net.runelite.rs.api;

import net.runelite.mapping.Import;

public interface RSClips
{
    @Import("clipNegativeMidX")
    int getClipNegativeMidX();

    @Import("clipNegativeMidX")
    int getClipNegativeMidY();

    @Import("viewportZoom")
    int getViewportZoom();

    @Import("viewportZoom")
    void setViewportZoom(int zoom);
}
