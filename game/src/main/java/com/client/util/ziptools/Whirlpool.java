package com.client.util.ziptools;

public final class Whirlpool {

    /**
     * The substitution box.
     */
    private static final String sbox = "\u1823\uc6e8\u87b8\u014f\u36a6\ud2f5\u796f\u9152\u60bc\u9b8e\ua30c\u7b35\u1de0\ud7c2\u2e4b\ufe57\u1577\u37e5\u9ff0\u4ada\u58c9\u290a\ub1a0\u6b85\ubd5d\u10f4\ucb3e\u0567\ue427\u418b\ua77d\u95d8\ufbee\u7c66\udd17\u479e\uca2d\ubf07\uad5a\u8333\u6302\uaa71\uc819\u49d9\uf2e3\u5b88\u9a26\u32b0\ue90f\ud580\ubecd\u3448\uff7a\u905f\u2068\u1aae\ub454\u9322\u64f1\u7312\u4008\uc3ec\udba1\u8d3d\u9700\ucf2b\u7682\ud61b\ub5af\u6a50\u45f3\u30ef\u3f55\ua2ea\u65ba\u2fc0\ude1c\ufd4d\u9275\u068a\ub2e6\u0e1f\u62d4\ua896\uf9c5\u2559\u8472\u394c\u5e78\u388c\ud1a5\ue261\ub321\u9c1e\u43c7\ufc04\u5199\u6d0d\ufadf\u7e24\u3bab\uce11\u8f4e\ub7eb\u3c81\u94f7\ub913\u2cd3\ue76e\uc403\u5644\u7fa9\u2abb\uc153\udc0b\u9d6c\u3174\uf646\uac89\u14e1\u163a\u6909\u70b6\ud0ed\ucc42\u98a4\u285c\uf886";

    /**
     * The number of rounds of the internal dedicated block cipher.
     */
    public static final int NUM_ROUNDS = 10;

    /**
     * The message digest size (in bits)
     */
    public static final int DIGESTBITS = 512;

    /**
     * The message digest size (in bytes)
     */
    public static final int DIGESTBYTES = DIGESTBITS >>> 3;

    private static final long[][] C = new long[8][256];
    private static final long[] rc = new long[NUM_ROUNDS + 1];

    static {
        for (int x = 0; x < 256; x++) {
            int c = sbox.charAt(x / 2);
            long v1 = (x & 0x1) == 0 ? (long) (c >>> 8) : (long) (c & 0xff);
            long v2 = v1 << 1;
            if (v2 >= 256L) {
                v2 ^= 0x11dL;
            }
            long c4 = v2 << 1;
            if (c4 >= 256L) {
                c4 ^= 0x11dL;
            }
            long v5 = c4 ^ v1;
            long v8 = c4 << 1;
            if (v8 >= 256L) {
                v8 ^= 0x11dL;
            }
            long v9 = v8 ^ v1;
            C[0][x] = v1 << 56 | v1 << 48 | c4 << 40 | v1 << 32 | v8 << 24 | v5 << 16 | v2 << 8 | v9;
            for (int t = 1; t < 8; t++) {
                C[t][x] = C[t - 1][x] >>> 8 | C[t - 1][x] << 56;
            }
        }
        rc[0] = 0L;
        for (int i = 1; i <= NUM_ROUNDS; i++) {
            int i_22_ = (i - 1) * 8;
            rc[i] = C[0][i_22_] & ~0xffffffffffffffL ^ C[1][i_22_ + 1] & 0xff000000000000L
                    ^ C[2][2 + i_22_] & 0xff0000000000L ^ C[3][i_22_ + 3] & 0xff00000000L
                    ^ C[4][i_22_ + 4] & 0xff000000L ^ C[5][i_22_ + 5] & 0xff0000L ^ C[6][i_22_ + 6] & 0xff00L
                    ^ C[7][7 + i_22_] & 0xffL;
        }
    }

    public byte[] buffer;
    public int bufferPos;
    public int bufferBits;
    public byte[] bitSize;
    public long[] K;
    public long[] L;
    public long[] block;
    public long[] hash;
    public long[] state;

    public Whirlpool() {
        bufferPos = 0;
        bitSize = new byte[32];
        buffer = new byte[64];
        hash = new long[8];
        K = new long[8];
        L = new long[8];
        block = new long[8];
        state = new long[8];
    }

    protected void processBuffer() {
        int index = 0;
        int off = 0;
        while (index < 8) {
            block[index] = (long) buffer[off] << 56 ^ (buffer[1 + off] & 0xffL) << 48 ^ (buffer[off + 2] & 0xffL) << 40
                    ^ (buffer[3 + off] & 0xffL) << 32 ^ (buffer[off + 4] & 0xffL) << 24
                    ^ (buffer[off + 5] & 0xffL) << 16 ^ (buffer[off + 6] & 0xffL) << 8 ^ buffer[7 + off] & 0xffL;
            index++;
            off += 8;
        }
        for (index = 0; index < 8; index++) {
            state[index] = block[index] ^ (K[index] = hash[index]);
        }
        for (index = 1; index <= NUM_ROUNDS; index++) {
            for (off = 0; off < 8; off++) {
                L[off] = 0L;
                int incr = 0;
                int decr = 56;
                while (incr < 8) {
                    L[off] ^= C[incr][(int) (K[off - incr & 0x7] >>> decr) & 0xff];
                    incr++;
                    decr -= 8;
                }
            }
            for (off = 0; off < 8; off++) {
                K[off] = L[off];
            }
            K[0] ^= rc[index];
            for (off = 0; off < 8; off++) {
                L[off] = K[off];
                int incr = 0;
                int decr = 56;
                while (incr < 8) {
                    L[off] ^= C[incr][(int) (state[off - incr & 0x7] >>> decr) & 0xff];
                    incr++;
                    decr -= 8;
                }
            }
            for (off = 0; off < 8; off++) {
                state[off] = L[off];
            }
        }
        for (index = 0; index < 8; index++) {
            hash[index] ^= state[index] ^ block[index];
        }
    }

    public void NESSIEinit() {
        for (int i_13_ = 0; i_13_ < 32; i_13_++) {
            bitSize[i_13_] = (byte) 0;
        }
        bufferPos = 0;
        bufferBits = 0;
        buffer[0] = (byte) 0;
        for (int h = 0; h < 8; h++) {
            hash[h] = 0L;
        }
    }

    public void NESSIEadd(byte[] source, long sourceBits) {
        int sourcePos = 0;
        int sourceGap = 8 - ((int) sourceBits & 0x7) & 0x7;
        int bufferRem = bufferBits * -1728608581 & 0x7;
        long value = sourceBits;
        int off = 31;
        int carry = 0;
        for (/**/; off >= 0; off--) {
            carry += ((int) value & 0xff) + (bitSize[off] & 0xff);
            bitSize[off] = (byte) carry;
            carry >>>= 8;
            value >>>= 8;
        }
        while (sourceBits > 8L) {
            int b = source[sourcePos] << sourceGap & 0xff | (source[1 + sourcePos] & 0xff) >>> 8 - sourceGap;
            if (b < 0 || b >= 256) {
                throw new RuntimeException("LOGIC ERROR");
            }
            buffer[935018147 * bufferPos] |= b >>> bufferRem;
            bufferPos += -2119539445;
            bufferBits += (8 - bufferRem) * 1391251059;
            if (bufferBits * -1728608581 == 512) {
                processBuffer();
                bufferPos = 0;
                bufferBits = 0;
            }
            buffer[935018147 * bufferPos] = (byte) (b << 8 - bufferRem & 0xff);
            bufferBits += 1391251059 * bufferRem;
            sourceBits -= 8L;
            sourcePos++;
        }
        int i_6_;
        if (sourceBits > 0L) {
            i_6_ = source[sourcePos] << sourceGap & 0xff;
            buffer[935018147 * bufferPos] |= i_6_ >>> bufferRem;
        } else {
            i_6_ = 0;
        }
        if (sourceBits + bufferRem < 8L) {
            bufferBits += 1391251059L * sourceBits;
        } else {
            bufferPos += -2119539445;
            bufferBits += (8 - bufferRem) * 1391251059;
            sourceBits -= 8 - bufferRem;
            if (512 == bufferBits * -1728608581) {
                processBuffer();
                bufferPos = 0;
                bufferBits = 0;
            }
            buffer[bufferPos * 935018147] = (byte) (i_6_ << 8 - bufferRem & 0xff);
            bufferBits += 1391251059 * (int) sourceBits;
        }
    }

    public void NESSIEfinalize(byte[] digset, int digsetOffset) {
        buffer[bufferPos * 935018147] |= 128 >>> (bufferBits * -1728608581 & 0x7);
        bufferPos += -2119539445;
        if (935018147 * bufferPos > 32) {
            while (bufferPos * 935018147 < 64) {
                buffer[(bufferPos += -2119539445) * 935018147 - 1] = (byte) 0;
            }
            processBuffer();
            bufferPos = 0;
        }
        while (935018147 * bufferPos < 32) {
            buffer[(bufferPos += -2119539445) * 935018147 - 1] = (byte) 0;
        }
        System.arraycopy(bitSize, 0, buffer, 32, 32);
        processBuffer();
        int index = 0;
        int off = digsetOffset;
        while (index < 8) {
            long l = hash[index];
            digset[off] = (byte) (int) (l >>> 56);
            digset[off + 1] = (byte) (int) (l >>> 48);
            digset[2 + off] = (byte) (int) (l >>> 40);
            digset[3 + off] = (byte) (int) (l >>> 32);
            digset[4 + off] = (byte) (int) (l >>> 24);
            digset[5 + off] = (byte) (int) (l >>> 16);
            digset[off + 6] = (byte) (int) (l >>> 8);
            digset[off + 7] = (byte) (int) l;
            index++;
            off += 8;
        }
    }

    public static byte[] getHash(byte[] data, int dataOffset, int dataLength) {
        byte[] hash;
        if (dataOffset > 0) {
            hash = new byte[dataLength];
            System.arraycopy(data, dataOffset, hash, 0, dataLength);
        } else {
            hash = data;
        }
        Whirlpool whirlpool = new Whirlpool();
        whirlpool.NESSIEinit();
        whirlpool.NESSIEadd(hash, dataLength * 8);
        byte[] result = new byte[64];
        whirlpool.NESSIEfinalize(result, 0);
        return result;
    }
}