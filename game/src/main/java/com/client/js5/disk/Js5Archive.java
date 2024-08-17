package com.client.js5.disk;

import com.client.Client;
import com.client.Buffer;
import com.client.js5.Js5ArchiveIndex;
import com.client.js5.net.NetFileRequest;
import com.client.net.DirectByteArrayCopier;
import net.runelite.rs.api.RSArchive;

import java.util.Arrays;

public class Js5Archive extends AbstractArchive implements RSArchive {
    ArchiveDisk archiveDisk;

    ArchiveDisk masterDisk;

    int index;

    volatile int loading = 0;

    boolean verifyChecksums = false;

    volatile boolean[] validGroups;

    int indexCrc;

    int indexVersion;

    int field4206 = -1;

    public boolean prioritizeRequests = false;
    public static Buffer archiveBuffer;
    boolean field3549; //TODO
    boolean field3550;

    public Js5Archive(ArchiveDisk archiveDisk, ArchiveDisk masterDisk, Js5ArchiveIndex js5ArchiveIndex, boolean useWhirlpool, boolean useGzip, boolean verifyChecksums, boolean prioritizeRequests) {
        super(useWhirlpool, useGzip);
        this.archiveDisk = archiveDisk;
        this.masterDisk = masterDisk;
        this.index = js5ArchiveIndex.getId();
        this.verifyChecksums = verifyChecksums;
        this.prioritizeRequests = prioritizeRequests;
        Client.jagexNetThread.updateArchiveIndex(this, this.index);
    }

    public static void loadArchiveIndex(Js5Archive js5Archive, int index) {
        archiveBuffer.currentPosition = index * 8 + 5;
        if (archiveBuffer.currentPosition >= archiveBuffer.payload.length) {
            if (js5Archive.prioritizeRequests) {
                js5Archive.reset();
            } else {
                throw new RuntimeException("");
            }
        } else {
            int var2 = archiveBuffer.readInt();
            int var3 = archiveBuffer.readInt();
            js5Archive.loadIndex(var2, var3);
        }
    }

    void processArchiveData(ArchiveDisk var1, int var2, byte[] var3, boolean var4) {
        int var5;
        if (var1 == this.masterDisk) {
            if (loading == 1) {
                throw new RuntimeException();
            } else if (var3 == null) {
                Client.jagexNetThread.requestNetFile(this, 255, this.index, this.indexCrc, (byte)0, true);
            } else {
                Client.jagexNetThread.field3593.reset();
                Client.jagexNetThread.field3593.update(var3, 0, var3.length);
                var5 = (int) Client.jagexNetThread.field3593.getValue();
                if (var5 != this.indexCrc) {
                    Client.jagexNetThread.requestNetFile(this, 255, this.index, this.indexCrc, (byte)0, true);
                } else {
                    Buffer var6 = new Buffer(decompressBytes(var3));
                    int var7 = var6.readUnsignedByte();
                    if (var7 != 5 && var7 != 6) {
                        throw new RuntimeException(var7 + "," + this.index + "," + var2);
                    } else {
                        int var8 = 0;
                        if (var7 >= 6) {
                            var8 = var6.readInt();
                        }

                        if (var8 != this.indexVersion) {
                            Client.jagexNetThread.requestNetFile(this, 255, this.index, this.indexCrc, (byte)0, true);
                        } else {
                            this.decodeIndex(var3);
                            this.loadAllLocal();
                        }
                    }
                }
            }
        } else {
            if (!var4 && var2 == field4206) {
                loading = 1;
            }

            if (var3 != null && var3.length > 2) {
                Client.jagexNetThread.field3593.reset();
                Client.jagexNetThread.field3593.update(var3, 0, var3.length - 2);
                var5 = (int) Client.jagexNetThread.field3593.getValue();
                int var11 = ((var3[var3.length - 2] & 255) << 8) + (var3[var3.length - 1] & 255);
                if (var5 == super.groupCrcs[var2] && var11 == super.groupVersions[var2]) {
                    this.validGroups[var2] = true;
                    if (var4) {
                        Object[] var12 = super.groups;
                        Object var9;
                        if (var3 == null) {
                            var9 = null;
                        } else if (var3.length > 136) {
                            DirectByteArrayCopier var10 = new DirectByteArrayCopier();
                            var10.set(var3);
                            var9 = var10;
                        } else {
                            var9 = var3;
                        }

                        var12[var2] = var9;
                    }

                } else {
                    this.validGroups[var2] = false;
                    if (verifyChecksums || var4) {
                        Client.jagexNetThread.requestNetFile(this, this.index, var2, super.groupCrcs[var2], (byte)2, var4);
                    }

                }
            } else {
                this.validGroups[var2] = false;
                if (verifyChecksums || var4) {
                    Client.jagexNetThread.requestNetFile(this, this.index, var2, super.groupCrcs[var2], (byte)2, var4);
                }

            }
        }
    }

    public boolean isLoading() {
        return this.loading == 1;
    }

    public int percentage() {
        if (this.loading != 1 && (!this.prioritizeRequests || this.loading != 2)) {
            if (super.groups != null) {
                return 99;
            } else {
                int indexValue = this.index;
                long key = (long)(indexValue + 16711680);
                int percentage;
                if (Client.jagexNetThread.field3596 != null && Client.jagexNetThread.field3596.key == key) {
                    percentage = Client.jagexNetThread.responsiveArchiveBuffer.currentPosition * 99 / (Client.jagexNetThread.responsiveArchiveBuffer.payload.length - Client.jagexNetThread.field3596.padding) + 1;
                } else {
                    percentage = 0;
                }

                int loadingPercentage = percentage;
                if (percentage >= 100) {
                    loadingPercentage = 99;
                }

                return loadingPercentage;
            }
        } else {
            return 100;
        }
    }

    static void moveRequestToEnd(int index, int group) {
        long hash = (long)((index << 16) + group);
        NetFileRequest request = (NetFileRequest) Client.jagexNetThread.field3601.get(hash);
        if (request != null) {
            Client.jagexNetThread.field3597.addLast(request);
        }
    }

    void loadRegionFromGroup(int group) {
        moveRequestToEnd(this.index, group);
    }

    void loadGroup(int group) {
        if (this.archiveDisk != null && this.validGroups != null && this.validGroups[group]) {
            ArchiveDisk disk = this.archiveDisk;
            byte[] data = null;
            synchronized(ArchiveDiskActionHandler.requestQueue) {
                for(ArchiveDiskAction action = (ArchiveDiskAction)ArchiveDiskActionHandler.requestQueue.last(); action != null; action = (ArchiveDiskAction)ArchiveDiskActionHandler.requestQueue.previous()) {
                    if (action.key == (long)group && disk == action.archiveDisk && action.type == 0) {
                        data = action.data;
                        break;
                    }
                }
            }

            if (data != null) {
                this.processArchiveData(disk, group, data, true);
            } else {
                byte[] var5 = disk.read(group);
                this.processArchiveData(disk, group, var5, true);
            }
        } else {
            Client.jagexNetThread.requestNetFile(this, this.index, group, super.groupCrcs[group], (byte)2, true);
        }

    }

    public void reset() {
        this.loading = 2;
        super.groupIds = new int[0];
        super.groupCrcs = new int[0];
        super.groupVersions = new int[0];
        super.fileCounts = new int[0];
        super.fileIds = new int[0][];
        super.groups = new Object[0];
        super.files = new Object[0][];
    }

    public void loadIndex(int indexCrc, int indexVersion) {
        this.indexCrc = indexCrc;
        this.indexVersion = indexVersion;

        if (this.masterDisk != null) {
            int index = this.index;
            ArchiveDisk archiveDisk = this.masterDisk;
            byte[] data = null;

            synchronized (ArchiveDiskActionHandler.requestQueue) {
                for(ArchiveDiskAction action = (ArchiveDiskAction)ArchiveDiskActionHandler.requestQueue.last(); action != null; action = (ArchiveDiskAction)ArchiveDiskActionHandler.requestQueue.previous()) {
                    if (action.key == (long) index && action.archiveDisk == archiveDisk && action.type == 0) {
                        data = action.data;
                        break;
                    }
                }
            }

            if (data != null) {
                this.processArchiveData(archiveDisk, index, data, true);
            } else {
                byte[] readData = archiveDisk.read(index);
                this.processArchiveData(archiveDisk, index, readData, true);
            }
        } else {
            Client.jagexNetThread.requestNetFile(this, 255, this.index, this.indexCrc, (byte) 0, true);
        }
    }

    public void write(int groupId, byte[] data, boolean decode, boolean saveToDisk) {
        if (decode) {
            if (loading == 1) {
                throw new RuntimeException("Already loading");
            }

            if (masterDisk != null) {
                writeDataToDisk(index, data, masterDisk);
            }

            decodeIndex(data);
            loadAllLocal();
        } else {
            data[data.length - 2] = (byte) (super.groupVersions[groupId] >> 8);
            data[data.length - 1] = (byte) super.groupVersions[groupId];
            if (archiveDisk != null && saveToDisk) {
                writeDataToDisk(groupId, data, archiveDisk);
                validGroups[groupId] = true;
            }

            if (saveToDisk) {
                Object[] var5 = super.groups;
                Object var7;
                if (data == null) {
                    var7 = null;
                } else if (data.length > 136) {
                    DirectByteArrayCopier var8 = new DirectByteArrayCopier();
                    var8.set(data);
                    var7 = var8;
                } else {
                    var7 = data;
                }

                var5[groupId] = var7;
            }

        }
    }

    static void writeDataToDisk(int key, byte[] data, ArchiveDisk archiveDisk) {
        ArchiveDiskAction action = new ArchiveDiskAction();
        action.type = 0;
        action.key = (long) key;
        action.data = data;
        action.archiveDisk = archiveDisk;

        synchronized(ArchiveDiskActionHandler.requestQueue) {
            ArchiveDiskActionHandler.requestQueue.addFirst(action);
        }

        ArchiveDiskActionHandler.processPendingActions();
    }

    public void loadAllLocal() {
        this.validGroups = new boolean[super.groups.length];

        Arrays.fill(this.validGroups, false);

        if (this.archiveDisk == null) {
            this.loading = 1;
        } else {
            this.field4206 = -1;

            for(int index = 0; index < this.validGroups.length; ++index) {
                if (super.fileCounts[index] > 0) {
                    enqueueArchiveDiskAction(index, this.archiveDisk, this);
                    this.field4206 = index;
                }
            }

            if (this.field4206 == -1) {
                this.loading = 1;
            }

        }
    }

    static void enqueueArchiveDiskAction(int key, ArchiveDisk archiveDisk, Js5Archive js5Archive) {
        ArchiveDiskAction var3 = new ArchiveDiskAction();
        var3.type = 1;
        var3.key = (long) key;
        var3.archiveDisk = archiveDisk;
        var3.js5Archive = js5Archive;
        synchronized(ArchiveDiskActionHandler.requestQueue) {
            ArchiveDiskActionHandler.requestQueue.addFirst(var3);
        }

        ArchiveDiskActionHandler.processPendingActions();
    }

    int groupLoadPercent(int groupIndex) {
        if (super.groups[groupIndex] != null || this.validGroups[groupIndex]) {
            return 100;
        }

        long groupKey = ((long) this.index << 16) + groupIndex;
        if (Client.jagexNetThread.field3596 != null && Client.jagexNetThread.field3596.key == groupKey) {
            int bufferLength = Client.jagexNetThread.responsiveArchiveBuffer.payload.length - Client.jagexNetThread.field3596.padding;
            int percentComplete = Client.jagexNetThread.responsiveArchiveBuffer.currentPosition * 99 / bufferLength + 1;
            return percentComplete;
        } else {
            return 0;
        }
    }

    public boolean isGroupValid(int group) {
        return this.validGroups[group];
    }

    public boolean isGroupIdsValid(int group) {
        return this.getGroupFileIds(group) != null;
    }

    public int loadPercent() {
        int totalPercentage = 0;
        int loadedPercentage = 0;

        for(int group = 0; group < super.groups.length; ++group) {
            if (super.fileCounts[group] > 0) {
                totalPercentage += 100;
                loadedPercentage += this.groupLoadPercent(group);
            }
        }

        if (totalPercentage == 0) {
            return 100;
        } else {
            return loadedPercentage * 100 / totalPercentage;
        }
    }

    @Override
    public boolean isOverlayOutdated() {
        return false;
    }

    @Override
    public byte[] getConfigData(int archiveId, int fileId) {
        return takeFile(archiveId,fileId);
    }

    @Override
    public int[] getFileIds(int groupId) {
        return fileIds[groupId];
    }

    @Override
    public int[][] getFileIds() {
        return fileIds;
    }

    @Override
    public int[] getFileCounts() {
        return fileCounts;
    }

    @Override
    public int getIndex() {
        return index;
    }

}
