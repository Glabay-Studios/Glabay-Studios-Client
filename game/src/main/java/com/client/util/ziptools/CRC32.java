package com.client.util.ziptools;

public final class CRC32 {

    private static final int[] CRC_TABLE = new int[256];

    static {
        for (int byteIndex = 0; byteIndex < 256; byteIndex++) {
            int crc = byteIndex;
            for (int offset = 0; offset < 8; offset++) {
                if ((crc & 0x1) == 1) {
                    crc = crc >>> 1 ^ ~0x12477cdf;
                } else {
                    crc >>>= 1;
                }
            }
            CRC_TABLE[byteIndex] = crc;
        }
    }

    public static int getCRC(byte[] data, int length) {
        return getCRC(data, 0, length);
    }

    public static int getCRC(byte[] data, int offset, int length) {
        int crc = -1;
        for (int index = offset; index < length; index++) {
            crc = crc >>> 8 ^ CRC_TABLE[(crc ^ data[index]) & 0xff];
        }
        crc ^= 0xffffffff;
        return crc;
    }
}