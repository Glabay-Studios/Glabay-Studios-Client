package com.client.util.ziptools;

public final class BZip2State {
    boolean[] inUse16 = new boolean[16];
    boolean[] inUse = new boolean[256];
    byte out_char;
    byte[] inputArray;
    byte[] outputArray;
    byte[] ll8 = new byte[4096];
    byte[] selector = new byte[18002];
    byte[] seqToUnseq = new byte[256];
    byte[] selectorMtf = new byte[18002];
    byte[][] temp_charArray2d = new byte[6][258];
    int su_rNToGo;
    int su_ch2;
    int nextBit_unused;
    int nextByte = 0;
    int nInUse;
    int field4813;
    int bsBuff;
    int bsLive;
    int field4818;
    int field4819;
    int field4820;
    int nblocks_used;
    int next_out = 0;
    int outputLength;
    int field4838;
    int[] minLens = new int[6];
    int[] unzftab = new int[256];
    int[] cftab = new int[257];
    int[] getAndMoveToFrontDecode_yy = new int[16];
    int[][] base = new int[6][258];
    int[][] limit = new int[6][258];
    int[][] perm = new int[6][258];
    final int field4803 = 258;
    final int field4805 = 50;
    final int field4806 = 18002;
    final int field4826 = 4096;
    final int field4833 = 6;
    final int field4835 = 16;

    BZip2State() {
    }
}
