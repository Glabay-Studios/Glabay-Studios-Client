package com.client.js5.disk;

import com.client.sign.diskfile.BufferedFile;
import net.runelite.rs.api.RSArchiveDisk;

import java.io.EOFException;
import java.io.IOException;

public class ArchiveDisk implements RSArchiveDisk {

    /**
     * The group header information block size.
     */
    public static final int INDEX_BLOCK_SIZE = 6;

    /**
     * The group data block size.
     */
    public static final int DATA_BLOCK_SIZE = 520;

    /**
     * The sector chunk temporary buffer.
     */
    private static final byte[] SECTOR_BUFFER = new byte[DATA_BLOCK_SIZE];

    /**
     * The small header size.
     */
    public static final int SMALL_BLOCK_HEADER_SIZE = 8;

    /**
     * The large header size.
     */
    public static final int LARGE_BLOCK_HEADER_SIZE = 10;

    /**
     * The small blocks size.
     */
    public static final int SMALL_BLOCK_DATA_SIZE = DATA_BLOCK_SIZE - SMALL_BLOCK_HEADER_SIZE;

    /**
     * The large blocks size.
     */
    public static final int LARGE_BLOCK_DATA_SIZE = DATA_BLOCK_SIZE - LARGE_BLOCK_HEADER_SIZE;

    public final int archiveid;
    public final BufferedFile dataFile;
    public final BufferedFile indexFile;
    public final int maxSize;

    public ArchiveDisk(int archiveid, BufferedFile dataFile, BufferedFile indexFile, int maxSize) {
        this.archiveid = archiveid;
        this.dataFile = dataFile;
        this.indexFile = indexFile;
        this.maxSize = maxSize;
    }

    public byte[] read(int groupId) {
        synchronized (dataFile) {
            try {
                if (indexFile.length() < (long) groupId * INDEX_BLOCK_SIZE + INDEX_BLOCK_SIZE) {
                    return null;
                }
                indexFile.seek((long) groupId * INDEX_BLOCK_SIZE);
                indexFile.read(SECTOR_BUFFER, 0, INDEX_BLOCK_SIZE);
                int size = g3(0);
                int block = g3(3);
                if (size < 0 || size > maxSize) {
                    return null;
                }
                if (block <= 0 || block > dataFile.length() / DATA_BLOCK_SIZE) {
                    return null;
                }
                byte[] data = new byte[size];
                int offset = 0;
                int chunk = 0;
                while (offset < size) {
                    if (block == 0) {
                        return null;
                    }
                    dataFile.seek((long) block * DATA_BLOCK_SIZE);
                    int count = size - offset;
                    int myHeaderLength;
                    int myGroup;
                    int myChunk;
                    int myNextBlock;
                    int myIndex;
                    if (groupId > 65535) {
                        if (count > LARGE_BLOCK_DATA_SIZE) {
                            count = LARGE_BLOCK_DATA_SIZE;
                        }
                        myHeaderLength = LARGE_BLOCK_HEADER_SIZE;
                        dataFile.read(SECTOR_BUFFER, 0, count + myHeaderLength);
                        myGroup = g4(0);
                        myChunk = g2(4);
                        myNextBlock = g3(6);
                        myIndex = g1(9);
                    } else {
                        if (count > SMALL_BLOCK_DATA_SIZE) {
                            count = SMALL_BLOCK_DATA_SIZE;
                        }
                        myHeaderLength = SMALL_BLOCK_HEADER_SIZE;
                        dataFile.read(SECTOR_BUFFER, 0, count + myHeaderLength);
                        myGroup = g2(0);
                        myChunk = g2(2);
                        myNextBlock = g3(4);
                        myIndex = g1(7);
                    }
                    if (myGroup != groupId || myChunk != chunk || myIndex != archiveid) {
                        return null;
                    }
                    if (myNextBlock < 0 || myNextBlock > dataFile.length() / DATA_BLOCK_SIZE) {
                        return null;
                    }
                    int numBytes = count + myHeaderLength;
                    for (int index = myHeaderLength; index < numBytes; index++) {
                        data[offset++] = SECTOR_BUFFER[index];
                    }
                    block = myNextBlock;
                    chunk++;
                }
                return data;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public boolean write(int groupId, byte[] data, int size) {
        synchronized (dataFile) {
            boolean stored = write(groupId, data, size, true);
            if (!stored) {
                stored = write(groupId, data, size, false);
            }
            return stored;
        }
    }

    private boolean write(int groupId, byte[] data, int size, boolean exists) {
        synchronized (dataFile) {
            try {
                int block;
                if (exists) {
                    if (indexFile.length() < (long) groupId * INDEX_BLOCK_SIZE + INDEX_BLOCK_SIZE) {
                        return false;
                    }
                    indexFile.seek((long) groupId * INDEX_BLOCK_SIZE);
                    indexFile.read(SECTOR_BUFFER, 0, INDEX_BLOCK_SIZE);
                    block = g3(3);
                    if (block <= 0 || block > dataFile.length() / DATA_BLOCK_SIZE) {
                        return false;
                    }
                } else {
                    block = (int) ((dataFile.length() + (DATA_BLOCK_SIZE - 1)) / DATA_BLOCK_SIZE);
                    if (block == 0) {
                        block = 1;
                    }
                }
                p3(0, size);
                p3(3, block);
                indexFile.seek((long) groupId * INDEX_BLOCK_SIZE);
                indexFile.write(SECTOR_BUFFER, 0, INDEX_BLOCK_SIZE);
                int offset = 0;
                int chunk = 0;
                while (offset < size) {
                    int myNextBlock = 0;
                    if (exists) {
                        dataFile.seek((long) block * DATA_BLOCK_SIZE);
                        int myGroup;
                        int myChunk;
                        int myIndex;
                        if (groupId > 65535) {
                            try {
                                dataFile.read(SECTOR_BUFFER, 0, LARGE_BLOCK_HEADER_SIZE);
                            } catch (EOFException eof) {
                                break;
                            }
                            myGroup = g4(0);
                            myChunk = g2(4);
                            myNextBlock = g3(6);
                            myIndex = g1(9);
                        } else {
                            try {
                                dataFile.read(SECTOR_BUFFER, 0, SMALL_BLOCK_HEADER_SIZE);
                            } catch (EOFException eofexception) {
                                break;
                            }
                            myGroup = g2(0);
                            myChunk = g2(2);
                            myNextBlock = g3(4);
                            myIndex = g1(7);
                        }
                        if (myGroup != groupId || myChunk != chunk || myIndex != archiveid) {
                            return false;
                        }
                        if (myNextBlock < 0 || myNextBlock > dataFile.length() / DATA_BLOCK_SIZE) {
                            return false;
                        }
                    }
                    if (myNextBlock == 0) {
                        exists = false;
                        myNextBlock = (int) ((dataFile.length() + DATA_BLOCK_SIZE - 1) / DATA_BLOCK_SIZE);
                        if (myNextBlock == 0) {
                            myNextBlock++;
                        }
                        if (myNextBlock == block) {
                            myNextBlock++;
                        }
                    }
                    if (size - offset <= SMALL_BLOCK_DATA_SIZE) {
                        myNextBlock = 0;
                    }
                    if (groupId > 65535) {
                        p4(0, groupId);
                        p2(4, chunk);
                        p3(6, myNextBlock);
                        p1(9, archiveid);
                        dataFile.seek((long) block * DATA_BLOCK_SIZE);
                        dataFile.write(SECTOR_BUFFER, 0, LARGE_BLOCK_HEADER_SIZE);
                        int numBytes = size - offset;
                        if (numBytes > LARGE_BLOCK_DATA_SIZE) {
                            numBytes = LARGE_BLOCK_DATA_SIZE;
                        }
                        dataFile.write(data, offset, numBytes);
                        offset += numBytes;
                    } else {
                        p2(0, groupId);
                        p2(2, chunk);
                        p3(4, myNextBlock);
                        p1(7, archiveid);
                        dataFile.seek((long) block * DATA_BLOCK_SIZE);
                        dataFile.write(SECTOR_BUFFER, 0, SMALL_BLOCK_HEADER_SIZE);
                        int numBytes = size - offset;
                        if (numBytes > SMALL_BLOCK_DATA_SIZE) {
                            numBytes = SMALL_BLOCK_DATA_SIZE;
                        }
                        dataFile.write(data, offset, numBytes);
                        offset += numBytes;
                    }
                    block = myNextBlock;
                    chunk++;
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    private static void p4(int offset, int value) {
        p1(offset++, value >> 24);
        p1(offset++, value >> 16);
        p1(offset++, value >> 8);
        p1(offset, value);
    }

    private static void p3(int offset, int value) {
        p1(offset++, value >> 16);
        p1(offset++, value >> 8);
        p1(offset, value);
    }

    private static void p2(int offset, int value) {
        p1(offset++, value >> 8);
        p1(offset, value);
    }

    private static void p1(int offset, int value) {
        SECTOR_BUFFER[offset] = (byte) value;
    }

    private static int g4(int offset) {
        return g1(offset++) << 24 | (g1(offset++) << 16) + (g1(offset++) << 8) + g1(offset);
    }

    private static int g3(int offset) {
        return (g1(offset++) << 16) + (g1(offset++) << 8) + g1(offset);
    }

    private static int g2(int offset) {
        return (g1(offset++) << 8) + g1(offset);
    }

    private static int g1(int offset) {
        return SECTOR_BUFFER[offset] & 0xff;
    }
}
