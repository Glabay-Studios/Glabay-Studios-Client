package com.client.util.ziptools;

public final class BZip2Decompressor {
    static BZip2State BZip2Decompressor_state = new BZip2State();
    static int[] BZip2Decompressor_block;

    BZip2Decompressor() throws Throwable {
        throw new Error();
    }

    public static int decompress(byte[] arg0, int arg1, byte[] arg2, int arg3, int arg4) {
        synchronized(BZip2Decompressor_state) {
            BZip2Decompressor_state.inputArray = arg2;
            BZip2Decompressor_state.nextByte = arg4;
            BZip2Decompressor_state.outputArray = arg0;
            BZip2Decompressor_state.next_out = 0;
            BZip2Decompressor_state.outputLength = arg1;
            BZip2Decompressor_state.bsLive = 0;
            BZip2Decompressor_state.bsBuff = 0;
            BZip2Decompressor_state.nextBit_unused = 0;
            BZip2Decompressor_state.field4813 = 0;
            method8051(BZip2Decompressor_state);
            int var8 = arg1 - BZip2Decompressor_state.outputLength;
            BZip2Decompressor_state.inputArray = null;
            BZip2Decompressor_state.outputArray = null;
            return var8;
        }
    }

    static void method8070(BZip2State arg0) {
        byte var2 = arg0.out_char;
        int var3 = arg0.su_rNToGo;
        int var4 = arg0.nblocks_used;
        int var5 = arg0.su_ch2;
        int[] var6 = BZip2Decompressor_block;
        int var7 = arg0.field4820;
        byte[] var8 = arg0.outputArray;
        int var9 = arg0.next_out;
        int var10 = arg0.outputLength;
        int var11 = var10;
        int var12 = arg0.field4838 + 1;

        label61:
        while(true) {
            if (var3 > 0) {
                while(true) {
                    if (var10 == 0) {
                        break label61;
                    }

                    if (var3 == 1) {
                        if (var10 == 0) {
                            var3 = 1;
                            break label61;
                        }

                        var8[var9] = var2;
                        ++var9;
                        --var10;
                        break;
                    }

                    var8[var9] = var2;
                    --var3;
                    ++var9;
                    --var10;
                }
            }

            while(var4 != var12) {
                var2 = (byte)var5;
                var7 = var6[var7];
                byte var1 = (byte)var7;
                var7 >>= 8;
                ++var4;
                if (var1 != var5) {
                    var5 = var1;
                    if (var10 == 0) {
                        var3 = 1;
                        break label61;
                    }

                    var8[var9] = var2;
                    ++var9;
                    --var10;
                } else {
                    if (var4 != var12) {
                        var3 = 2;
                        var7 = var6[var7];
                        var1 = (byte)var7;
                        var7 >>= 8;
                        ++var4;
                        if (var4 != var12) {
                            if (var1 != var5) {
                                var5 = var1;
                            } else {
                                var3 = 3;
                                var7 = var6[var7];
                                var1 = (byte)var7;
                                var7 >>= 8;
                                ++var4;
                                if (var4 != var12) {
                                    if (var1 != var5) {
                                        var5 = var1;
                                    } else {
                                        var7 = var6[var7];
                                        var1 = (byte)var7;
                                        var7 >>= 8;
                                        ++var4;
                                        var3 = (var1 & 255) + 4;
                                        var7 = var6[var7];
                                        var5 = (byte)var7;
                                        var7 >>= 8;
                                        ++var4;
                                    }
                                }
                            }
                        }
                        continue label61;
                    }

                    if (var10 == 0) {
                        var3 = 1;
                        break label61;
                    }

                    var8[var9] = var2;
                    ++var9;
                    --var10;
                }
            }

            var3 = 0;
            break;
        }

        int var13 = arg0.field4813;
        arg0.field4813 += var11 - var10;
        if (arg0.field4813 < var13) {
        }

        arg0.out_char = var2;
        arg0.su_rNToGo = var3;
        arg0.nblocks_used = var4;
        arg0.su_ch2 = var5;
        BZip2Decompressor_block = var6;
        arg0.field4820 = var7;
        arg0.outputArray = var8;
        arg0.next_out = var9;
        arg0.outputLength = var10;
    }

    static void method8051(BZip2State arg0) {
        boolean var4 = false;
        boolean var5 = false;
        boolean var6 = false;
        boolean var7 = false;
        boolean var8 = false;
        boolean var9 = false;
        boolean var10 = false;
        boolean var11 = false;
        boolean var12 = false;
        boolean var13 = false;
        boolean var14 = false;
        boolean var15 = false;
        boolean var16 = false;
        boolean var17 = false;
        boolean var18 = false;
        boolean var19 = false;
        boolean var20 = false;
        boolean var21 = false;
        int var22 = 0;
        int[] var23 = null;
        int[] var24 = null;
        int[] var25 = null;
        arg0.field4818 = 100000;
        if (BZip2Decompressor_block == null) {
            BZip2Decompressor_block = new int[arg0.field4818];
        }

        boolean var26 = true;

        while(true) {
            while(var26) {
                byte var1 = method8052(arg0);
                if (var1 == 23) {
                    return;
                }

                var1 = method8052(arg0);
                var1 = method8052(arg0);
                var1 = method8052(arg0);
                var1 = method8052(arg0);
                var1 = method8052(arg0);
                var1 = method8052(arg0);
                var1 = method8052(arg0);
                var1 = method8052(arg0);
                var1 = method8052(arg0);
                var1 = method8053(arg0);
                if (var1 != 0) {
                }

                arg0.field4819 = 0;
                var1 = method8052(arg0);
                arg0.field4819 = arg0.field4819 << 8 | var1 & 255;
                var1 = method8052(arg0);
                arg0.field4819 = arg0.field4819 << 8 | var1 & 255;
                var1 = method8052(arg0);
                arg0.field4819 = arg0.field4819 << 8 | var1 & 255;

                int var36;
                for(var36 = 0; var36 < 16; ++var36) {
                    var1 = method8053(arg0);
                    if (var1 == 1) {
                        arg0.inUse16[var36] = true;
                    } else {
                        arg0.inUse16[var36] = false;
                    }
                }

                for(var36 = 0; var36 < 256; ++var36) {
                    arg0.inUse[var36] = false;
                }

                int var37;
                for(var36 = 0; var36 < 16; ++var36) {
                    if (arg0.inUse16[var36]) {
                        for(var37 = 0; var37 < 16; ++var37) {
                            var1 = method8053(arg0);
                            if (var1 == 1) {
                                arg0.inUse[var36 * 16 + var37] = true;
                            }
                        }
                    }
                }

                makeMaps(arg0);
                int var39 = arg0.nInUse + 2;
                int var40 = BZip2Decompressor_readBits(3, arg0);
                int var41 = BZip2Decompressor_readBits(15, arg0);

                for(var36 = 0; var36 < var41; ++var36) {
                    var37 = 0;

                    while(true) {
                        var1 = method8053(arg0);
                        if (var1 == 0) {
                            arg0.selectorMtf[var36] = (byte)var37;
                            break;
                        }

                        ++var37;
                    }
                }

                byte[] var27 = new byte[6];
                byte var29 = 0;

                while(var29 < var40) {
                    var27[var29] = var29++;
                }

                for(var36 = 0; var36 < var41; ++var36) {
                    var29 = arg0.selectorMtf[var36];
                    byte var28 = var27[var29];

                    while(var29 > 0) {
                        var27[var29] = var27[var29 - 1];
                        --var29;
                    }

                    var27[0] = var28;
                    arg0.selector[var36] = var28;
                }

                int var38;
                for(var38 = 0; var38 < var40; ++var38) {
                    int var50 = BZip2Decompressor_readBits(5, arg0);

                    for(var36 = 0; var36 < var39; ++var36) {
                        while(true) {
                            var1 = method8053(arg0);
                            if (var1 == 0) {
                                arg0.temp_charArray2d[var38][var36] = (byte)var50;
                                break;
                            }

                            var1 = method8053(arg0);
                            if (var1 == 0) {
                                ++var50;
                            } else {
                                --var50;
                            }
                        }
                    }
                }

                for(var38 = 0; var38 < var40; ++var38) {
                    byte var2 = 32;
                    byte var3 = 0;

                    for(var36 = 0; var36 < var39; ++var36) {
                        if (arg0.temp_charArray2d[var38][var36] > var3) {
                            var3 = arg0.temp_charArray2d[var38][var36];
                        }

                        if (arg0.temp_charArray2d[var38][var36] < var2) {
                            var2 = arg0.temp_charArray2d[var38][var36];
                        }
                    }

                    BZip2Decompressor_createHuffmanTables(arg0.limit[var38], arg0.base[var38], arg0.perm[var38], arg0.temp_charArray2d[var38], var2, var3, var39);
                    arg0.minLens[var38] = var2;
                }

                int var42 = arg0.nInUse + 1;
                int var43 = -1;
                byte var44 = 0;

                for(var36 = 0; var36 <= 255; ++var36) {
                    arg0.unzftab[var36] = 0;
                }

                int var56 = 4095;

                int var35;
                int var55;
                for(var35 = 15; var35 >= 0; --var35) {
                    for(var55 = 15; var55 >= 0; --var55) {
                        arg0.ll8[var56] = (byte)(var35 * 16 + var55);
                        --var56;
                    }

                    arg0.getAndMoveToFrontDecode_yy[var35] = var56 + 1;
                }

                int var47 = 0;
                byte var54;
                if (var44 == 0) {
                    ++var43;
                    var44 = 50;
                    var54 = arg0.selector[var43];
                    var22 = arg0.minLens[var54];
                    var23 = arg0.limit[var54];
                    var25 = arg0.perm[var54];
                    var24 = arg0.base[var54];
                }

                int var45 = var44 - 1;
                int var51 = var22;

                int var52;
                byte var53;
                for(var52 = BZip2Decompressor_readBits(var22, arg0); var52 > var23[var51]; var52 = var52 << 1 | var53) {
                    ++var51;
                    var53 = method8053(arg0);
                }

                int var46 = var25[var52 - var24[var51]];

                while(true) {
                    while(var46 != var42) {
                        if (var46 != 0 && var46 != 1) {
                            int var33 = var46 - 1;
                            int var30;
                            if (var33 < 16) {
                                var30 = arg0.getAndMoveToFrontDecode_yy[0];
                                var1 = arg0.ll8[var30 + var33];

                                while(var33 > 3) {
                                    int var34 = var30 + var33;
                                    arg0.ll8[var34] = arg0.ll8[var34 - 1];
                                    arg0.ll8[var34 - 1] = arg0.ll8[var34 - 2];
                                    arg0.ll8[var34 - 2] = arg0.ll8[var34 - 3];
                                    arg0.ll8[var34 - 3] = arg0.ll8[var34 - 4];
                                    var33 -= 4;
                                }

                                while(var33 > 0) {
                                    arg0.ll8[var30 + var33] = arg0.ll8[var30 + var33 - 1];
                                    --var33;
                                }

                                arg0.ll8[var30] = var1;
                            } else {
                                int var31 = var33 / 16;
                                int var32 = var33 % 16;
                                var30 = arg0.getAndMoveToFrontDecode_yy[var31] + var32;
                                var1 = arg0.ll8[var30];

                                while(var30 > arg0.getAndMoveToFrontDecode_yy[var31]) {
                                    arg0.ll8[var30] = arg0.ll8[var30 - 1];
                                    --var30;
                                }

                                int var10002 = arg0.getAndMoveToFrontDecode_yy[var31]++;

                                while(var31 > 0) {
                                    var10002 = arg0.getAndMoveToFrontDecode_yy[var31]--;
                                    arg0.ll8[arg0.getAndMoveToFrontDecode_yy[var31]] = arg0.ll8[arg0.getAndMoveToFrontDecode_yy[var31 - 1] + 16 - 1];
                                    --var31;
                                }

                                var10002 = arg0.getAndMoveToFrontDecode_yy[0]--;
                                arg0.ll8[arg0.getAndMoveToFrontDecode_yy[0]] = var1;
                                if (arg0.getAndMoveToFrontDecode_yy[0] == 0) {
                                    var56 = 4095;

                                    for(var35 = 15; var35 >= 0; --var35) {
                                        for(var55 = 15; var55 >= 0; --var55) {
                                            arg0.ll8[var56] = arg0.ll8[arg0.getAndMoveToFrontDecode_yy[var35] + var55];
                                            --var56;
                                        }

                                        arg0.getAndMoveToFrontDecode_yy[var35] = var56 + 1;
                                    }
                                }
                            }

                            ++arg0.unzftab[arg0.seqToUnseq[var1 & 255] & 255];
                            BZip2Decompressor_block[var47] = arg0.seqToUnseq[var1 & 255] & 255;
                            ++var47;
                            if (var45 == 0) {
                                ++var43;
                                var45 = 50;
                                var54 = arg0.selector[var43];
                                var22 = arg0.minLens[var54];
                                var23 = arg0.limit[var54];
                                var25 = arg0.perm[var54];
                                var24 = arg0.base[var54];
                            }

                            --var45;
                            var51 = var22;

                            for(var52 = BZip2Decompressor_readBits(var22, arg0); var52 > var23[var51]; var52 = var52 << 1 | var53) {
                                ++var51;
                                var53 = method8053(arg0);
                            }

                            var46 = var25[var52 - var24[var51]];
                        } else {
                            int var48 = -1;
                            int var49 = 1;

                            do {
                                if (var46 == 0) {
                                    var48 += var49;
                                } else if (var46 == 1) {
                                    var48 += 2 * var49;
                                }

                                var49 *= 2;
                                if (var45 == 0) {
                                    ++var43;
                                    var45 = 50;
                                    var54 = arg0.selector[var43];
                                    var22 = arg0.minLens[var54];
                                    var23 = arg0.limit[var54];
                                    var25 = arg0.perm[var54];
                                    var24 = arg0.base[var54];
                                }

                                --var45;
                                var51 = var22;

                                for(var52 = BZip2Decompressor_readBits(var22, arg0); var52 > var23[var51]; var52 = var52 << 1 | var53) {
                                    ++var51;
                                    var53 = method8053(arg0);
                                }

                                var46 = var25[var52 - var24[var51]];
                            } while(var46 == 0 || var46 == 1);

                            ++var48;
                            var1 = arg0.seqToUnseq[arg0.ll8[arg0.getAndMoveToFrontDecode_yy[0]] & 255];
                            arg0.unzftab[var1 & 255] += var48;

                            while(var48 > 0) {
                                BZip2Decompressor_block[var47] = var1 & 255;
                                ++var47;
                                --var48;
                            }
                        }
                    }

                    arg0.su_rNToGo = 0;
                    arg0.out_char = 0;
                    arg0.cftab[0] = 0;

                    for(var36 = 1; var36 <= 256; ++var36) {
                        arg0.cftab[var36] = arg0.unzftab[var36 - 1];
                    }

                    for(var36 = 1; var36 <= 256; ++var36) {
                        arg0.cftab[var36] += arg0.cftab[var36 - 1];
                    }

                    for(var36 = 0; var36 < var47; ++var36) {
                        var1 = (byte)(BZip2Decompressor_block[var36] & 255);
                        BZip2Decompressor_block[arg0.cftab[var1 & 255]] |= var36 << 8;
                        ++arg0.cftab[var1 & 255];
                    }

                    arg0.field4820 = BZip2Decompressor_block[arg0.field4819] >> 8;
                    arg0.nblocks_used = 0;
                    arg0.field4820 = BZip2Decompressor_block[arg0.field4820];
                    arg0.su_ch2 = (byte)(arg0.field4820 & 255);
                    arg0.field4820 >>= 8;
                    ++arg0.nblocks_used;
                    arg0.field4838 = var47;
                    method8070(arg0);
                    if (arg0.nblocks_used == arg0.field4838 + 1 && arg0.su_rNToGo == 0) {
                        var26 = true;
                        break;
                    }

                    var26 = false;
                    break;
                }
            }

            return;
        }
    }

    static byte method8052(BZip2State arg0) {
        return (byte)BZip2Decompressor_readBits(8, arg0);
    }

    static byte method8053(BZip2State arg0) {
        return (byte)BZip2Decompressor_readBits(1, arg0);
    }

    static int BZip2Decompressor_readBits(int arg0, BZip2State arg1) {
        while(arg1.bsLive < arg0) {
            arg1.bsBuff = arg1.bsBuff << 8 | arg1.inputArray[arg1.nextByte] & 255;
            arg1.bsLive += 8;
            ++arg1.nextByte;
            ++arg1.nextBit_unused;
            if (arg1.nextBit_unused == 0) {
            }
        }

        int var2 = arg1.bsBuff >> arg1.bsLive - arg0 & (1 << arg0) - 1;
        arg1.bsLive -= arg0;
        return var2;
    }

    static void makeMaps(BZip2State arg0) {
        arg0.nInUse = 0;

        for(int var1 = 0; var1 < 256; ++var1) {
            if (arg0.inUse[var1]) {
                arg0.seqToUnseq[arg0.nInUse] = (byte)var1;
                ++arg0.nInUse;
            }
        }

    }

    static void BZip2Decompressor_createHuffmanTables(int[] arg0, int[] arg1, int[] arg2, byte[] arg3, int arg4, int arg5, int arg6) {
        int var7 = 0;

        int var8;
        for(var8 = arg4; var8 <= arg5; ++var8) {
            for(int var9 = 0; var9 < arg6; ++var9) {
                if (arg3[var9] == var8) {
                    arg2[var7] = var9;
                    ++var7;
                }
            }
        }

        for(var8 = 0; var8 < 23; ++var8) {
            arg1[var8] = 0;
        }

        for(var8 = 0; var8 < arg6; ++var8) {
            ++arg1[arg3[var8] + 1];
        }

        for(var8 = 1; var8 < 23; ++var8) {
            arg1[var8] += arg1[var8 - 1];
        }

        for(var8 = 0; var8 < 23; ++var8) {
            arg0[var8] = 0;
        }

        int var10 = 0;

        for(var8 = arg4; var8 <= arg5; ++var8) {
            var10 += arg1[var8 + 1] - arg1[var8];
            arg0[var8] = var10 - 1;
            var10 <<= 1;
        }

        for(var8 = arg4 + 1; var8 <= arg5; ++var8) {
            arg1[var8] = (arg0[var8 - 1] + 1 << 1) - arg1[var8];
        }

    }
}
