package com.client.js5.util;

import com.client.js5.disk.Js5Archive;
import net.runelite.rs.api.RSArchiveLoader;

import java.util.ArrayList;

public class ArchiveLoader implements RSArchiveLoader {

    public static ArrayList<ArchiveLoader> archiveLoaders = new ArrayList<>(10);
    public static int archiveLoadersDone;

    final Js5Archive js5Archive;

    public final int groupCount;

    int loadedCount;

    public ArchiveLoader(Js5Archive js5Archive, String unknown) {
        this.loadedCount = 0;
        this.js5Archive = js5Archive;
        this.groupCount = js5Archive.getGroupCount();
    }

    public boolean isLoaded() {
        this.loadedCount = 0;

        for(int groupCount = 0; groupCount < this.groupCount; ++groupCount) {
            if (!this.js5Archive.isGroupIdsValid(groupCount) || this.js5Archive.isGroupValid(groupCount)) {
                ++this.loadedCount;
            }
        }

        return this.loadedCount >= this.groupCount;
    }



}
