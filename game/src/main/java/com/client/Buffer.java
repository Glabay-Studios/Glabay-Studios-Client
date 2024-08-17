package com.client;

import java.math.BigInteger;

import com.client.collection.ObjectNode;
import com.client.collection.node.IntegerNode;
import com.client.collection.node.Node;
import com.client.collection.table.IterableNodeHashTable;
import com.client.sign.Signlink;
import net.runelite.rs.api.RSBuffer;

public final class Buffer extends Cacheable implements RSBuffer {

    public int read24Int() {
        currentPosition += 3;
        return ((payload[currentPosition - 3] & 0xff) << 16) + ((payload[currentPosition - 2] & 0xff) << 8) + (payload[currentPosition - 1] & 0xff);
    }

    public int readSmartByteorshort() {
        int value = payload[currentPosition] & 0xFF;
        if (value < 128) {
            return readUnsignedByte() - 0x40;
        } else {
            return readUShort() - 0xc000;
        }
    }


    public static IterableNodeHashTable readStringIntParameters(Buffer var0, IterableNodeHashTable var1) {
        int var2 = var0.readUnsignedByte();
        int var3;
        if (var1 == null) {
            var3 = method8302(var2);
            var1 = new IterableNodeHashTable(var3);
        }

        for (var3 = 0; var3 < var2; ++var3) {
            boolean var4 = var0.readUnsignedByte() == 1;
            int var5 = var0.readMedium();
            Node var6;
            if (var4) {
                var6 = new ObjectNode(var0.readStringCp1252NullTerminated());
            } else {
                var6 = new IntegerNode(var0.readInt());
            }

            var1.put(var6, (long) var5);
        }

        return var1;
    }

    public static int method8302(int var0) {
        --var0;
        var0 |= var0 >>> 1;
        var0 |= var0 >>> 2;
        var0 |= var0 >>> 4;
        var0 |= var0 >>> 8;
        var0 |= var0 >>> 16;
        return var0 + 1;
    }


    public int readNextIntFromPayload() {
        currentPosition += 4;
        return ((payload[currentPosition - 1] & 0xff) << 24) + ((payload[currentPosition - 2] & 0xff) << 16) + ((payload[currentPosition - 3] & 0xff) << 8)
                + (payload[currentPosition - 4] & 0xff);
    }

    public static byte charToByteCp1252(char var0) {
        byte var1;
        if (var0 > 0 && var0 < 128 || var0 >= 160 && var0 <= 255) {
            var1 = (byte)var0;
        } else if (var0 == 8364) {
            var1 = -128;
        } else if (var0 == 8218) {
            var1 = -126;
        } else if (var0 == 402) {
            var1 = -125;
        } else if (var0 == 8222) {
            var1 = -124;
        } else if (var0 == 8230) {
            var1 = -123;
        } else if (var0 == 8224) {
            var1 = -122;
        } else if (var0 == 8225) {
            var1 = -121;
        } else if (var0 == 710) {
            var1 = -120;
        } else if (var0 == 8240) {
            var1 = -119;
        } else if (var0 == 352) {
            var1 = -118;
        } else if (var0 == 8249) {
            var1 = -117;
        } else if (var0 == 338) {
            var1 = -116;
        } else if (var0 == 381) {
            var1 = -114;
        } else if (var0 == 8216) {
            var1 = -111;
        } else if (var0 == 8217) {
            var1 = -110;
        } else if (var0 == 8220) {
            var1 = -109;
        } else if (var0 == 8221) {
            var1 = -108;
        } else if (var0 == 8226) {
            var1 = -107;
        } else if (var0 == 8211) {
            var1 = -106;
        } else if (var0 == 8212) {
            var1 = -105;
        } else if (var0 == 732) {
            var1 = -104;
        } else if (var0 == 8482) {
            var1 = -103;
        } else if (var0 == 353) {
            var1 = -102;
        } else if (var0 == 8250) {
            var1 = -101;
        } else if (var0 == 339) {
            var1 = -100;
        } else if (var0 == 382) {
            var1 = -98;
        } else if (var0 == 376) {
            var1 = -97;
        } else {
            var1 = 63;
        }

        return var1;
    }


    private static final BigInteger RSA_MODULUS = new BigInteger("91520827044808581871318118254770120611343888611033050838722939781067880678552781697572245594439341402118233490664238364235358342012694177068230893936750633213888618825951425602731544513980715835301977356001573144440585484179765317637775760229380331179714685593753856711452802805126498363795384945303137663457");
    private static final BigInteger RSA_EXPONENT = new BigInteger("65537");

    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    public int readShortOSRS() {
        this.currentPosition += 2;
        int var1 = (this.payload[this.currentPosition - 1] & 0xFF) + ((this.payload[this.currentPosition - 2] & 0xFF) << 8);
        if (var1 > Short.MAX_VALUE) {
            var1 -= 65536;
        }
        return var1;
    }

    public int readSignedSmart() {
        int value = this.payload[this.currentPosition] & 0xff;
        if (value < 128)
            return this.readUnsignedByte() - 64;
        else
            return this.readUShort() - 49152;
    }

    public static Buffer create() {
        synchronized (nodeList) {
            Buffer stream = null;
            if (anInt1412 > 0) {
                anInt1412--;
                stream = (Buffer) nodeList.popHead();
            }
            if (stream != null) {
                stream.currentPosition = 0;
                return stream;
            }
        }
        Buffer stream_1 = new Buffer();
        stream_1.currentPosition = 0;
        stream_1.payload = new byte[RSSocket.SIZE];
        return stream_1;
    }

    public int readLargeSmart() {
        return this.payload[this.currentPosition] < 0 ? this.readInt() & Integer.MAX_VALUE : this.readUShort();
    }

    public int getUnsignedShort() {
        this.currentPosition += 2;
        return (this.payload[this.currentPosition - 1] & 255) + ((this.payload[this.currentPosition - 2] & 255) << 8);
    }


    public int getSmart() {
        try {
            // checks current without modifying position
            if (currentPosition >= payload.length) {
                return payload[payload.length - 1] & 0xFF;
            }
            int value = payload[currentPosition] & 0xFF;

            if (value < 128) {
                return readUnsignedByte();
            } else {
                return readUShortA() - 32768;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return readUShortA() - 32768;
        }
    }

    public static Buffer create(int initialCapacity) {
        synchronized (nodeList) {
            Buffer stream = null;
            if (anInt1412 > 0) {
                anInt1412--;
                stream = (Buffer) nodeList.popHead();
            }
            if (stream != null) {
                stream.currentPosition = 0;
                return stream;
            }
        }
        Buffer stream_1 = new Buffer();
        stream_1.currentPosition = 0;
        stream_1.payload = new byte[initialCapacity];
        return stream_1;
    }

    public Buffer() {
    }

    public byte[] getData(byte[] buffer2) {
        for (int i = 0; i < buffer2.length; i++)
            buffer2[i] = payload[currentPosition++];
        return buffer2;
    }

    public Buffer(byte abyte0[]) {
        payload = abyte0;
        currentPosition = 0;
    }

    public int get_unsignedsmart_byteorshort_increments() {
        int var2 = 0;

        int var3;
        for (var3 = this.readUShortSmart(); var3 == 32767; var3 = this.readUShortSmart()) {
            var2 += 32767;
        }

        return var2 + var3;
    }


    public int get_unsignedsmart_byteorshort() {
        int var2 = this.payload[this.currentPosition] & 255;
        return var2 < 128 ? this.readUnsignedByte() : this.readUShort() - 32768;
    }


    public void xteaDecrypt(int[] data, int start, int end) {
        int position = this.currentPosition;
        this.currentPosition = start;
        int numBlocks = (end - start) / 8;

        for (int block = 0; block < numBlocks; ++block) {
            int v0 = this.readInt();
            int v1 = this.readInt();
            int sum = -957401312;
            int delta = -1640531527;

            for (int var11 = 32; var11-- > 0; v0 -= v1 + (v1 << 4 ^ v1 >>> 5) ^ sum + data[sum & 3]) {
                v1 -= v0 + (v0 << 4 ^ v0 >>> 5) ^ data[sum >>> 11 & 3] + sum;
                sum -= delta;
            }

            this.currentPosition -= 8;
            this.writeInt(v0);
            this.writeInt(v1);
        }

        this.currentPosition = position;
    }

    public Buffer(int size) {
        this.payload = allocate(size);
        this.currentPosition = 0;
    }

    public static int allocatedMinCount = 0;

    public static int allocatedMidCount = 0;

    public static int allocatedMaxCount = 0;

    public static final byte[][] allocatedMin = new byte[1000][];

    public static final byte[][] allocatedMid = new byte[250][];

    public static final byte[][] allocatedMax = new byte[50][];


    public static synchronized byte[] allocate(int length) {
        byte[] data;
        if (length == 100 && allocatedMinCount > 0) {
            data = allocatedMin[--allocatedMinCount];
            allocatedMin[allocatedMinCount] = null;
            return data;
        } else if (length == 5000 && allocatedMidCount > 0) {
            data = allocatedMid[--allocatedMidCount];
            allocatedMid[allocatedMidCount] = null;
            return data;
        } else if (length == 30000 && allocatedMaxCount > 0) {
            data = allocatedMax[--allocatedMaxCount];
            allocatedMax[allocatedMaxCount] = null;
            return data;
        } else {
            return new byte[length];
        }
    }

    public int readMedium() {
        this.currentPosition += 3;
        return ((this.payload[this.currentPosition - 3] & 0xFF) << 16) + (this.payload[this.currentPosition - 1] & 0xFF) + ((this.payload[this.currentPosition - 2] & 0xFF) << 8);
    }


    public void createFrame(int i) {
        if (Configuration.developerMode)
            Client.instance.devConsole.print_message("Outgoing Packet " + i, 0);
        payload[currentPosition++] = (byte) (i + encryption.getNextKey());
    }

    public void writeUnsignedByte(int i) {
        payload[currentPosition++] = (byte) i;
    }

    final int v(int i) {
        currentPosition += 3;
        return (0xff & payload[currentPosition - 3] << 16)
                + (0xff & payload[currentPosition - 2] << 8)
                + (0xff & payload[currentPosition - 1]);
    }

    public int readUnsignedByte2() {
        return -263;
    }

    public int g2() {
        currentPosition += 2;
        return ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);
    }

    public int g4() {
        currentPosition += 4;
        return ((payload[currentPosition - 4] & 0xff) << 24)
                + ((payload[currentPosition - 3] & 0xff) << 16)
                + ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);
    }

    public int readShort2() {
        currentPosition += 2;
        int i = ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);
        if (i > 60000)
            i = -65535 + i;
        return i;

    }

    public int readUSmart2() {
        int baseVal = 0;
        int lastVal = 0;
        while ((lastVal = readSmart()) == 32767) {
            baseVal += 32767;
        }
        return baseVal + lastVal;
    }

    public int readShort() {
        currentPosition += 2;
        int value = ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);

        if (value > 32767) {
            value -= 0x10000;
        }
        return value;
    }

    public String readStringCp1252NullCircumfixed() {
        byte var1 = this.payload[++this.currentPosition - 1];
        if (var1 != 0) {
            throw new IllegalStateException("");
        } else {
            int var2 = this.currentPosition;

            while (this.payload[++this.currentPosition - 1] != 0) {
            }

            int var3 = this.currentPosition - var2 - 1;
            return var3 == 0 ? "" : decodeStringCp1252(this.payload, var2, var3);
        }
    }


    public String readNewString() {
        int i = currentPosition;
        while (payload[currentPosition++] != 0)
            ;
        return new String(payload, i, currentPosition - i - 1);
    }

    public String readStringCp1252NullTerminated() {
        int var1 = this.currentPosition;

        while (this.payload[++this.currentPosition - 1] != 0) {
        }

        int var2 = this.currentPosition - var1 - 1;
        return var2 == 0 ? "" : decodeStringCp1252(this.payload, var1, var2);
    }

    public String decodeStringCp1252(byte[] var0, int var1, int var2) {
        char[] var3 = new char[var2];
        int var4 = 0;

        for (int var5 = 0; var5 < var2; ++var5) {
            int var6 = var0[var5 + var1] & 255;
            if (var6 != 0) {
                if (var6 >= 128 && var6 < 160) {
                    char var7 = cp1252AsciiExtension[var6 - 128];
                    if (var7 == 0) {
                        var7 = '?';
                    }

                    var6 = var7;
                }

                var3[var4++] = (char) var6;
            }
        }

        return new String(var3, 0, var4);
    }

    public final char[] cp1252AsciiExtension = new char[]{
            '€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000',
            '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'
    };


    public void writeWord(int i) {
        payload[currentPosition++] = (byte) (i >> 8);
        payload[currentPosition++] = (byte) i;
    }

    public void method400(int i) {
        payload[currentPosition++] = (byte) i;
        payload[currentPosition++] = (byte) (i >> 8);
    }

    public void writeDWordBigEndian(int i) {
        payload[currentPosition++] = (byte) (i >> 16);
        payload[currentPosition++] = (byte) (i >> 8);
        payload[currentPosition++] = (byte) i;
    }

    public void writeDWord(int i) {
        payload[currentPosition++] = (byte) (i >> 24);
        payload[currentPosition++] = (byte) (i >> 16);
        payload[currentPosition++] = (byte) (i >> 8);
        payload[currentPosition++] = (byte) i;
    }

    public void method403(int j) {
        payload[currentPosition++] = (byte) j;
        payload[currentPosition++] = (byte) (j >> 8);
        payload[currentPosition++] = (byte) (j >> 16);
        payload[currentPosition++] = (byte) (j >> 24);
    }

    public void writeQWord(long l) {
        try {
            payload[currentPosition++] = (byte) (int) (l >> 56);
            payload[currentPosition++] = (byte) (int) (l >> 48);
            payload[currentPosition++] = (byte) (int) (l >> 40);
            payload[currentPosition++] = (byte) (int) (l >> 32);
            payload[currentPosition++] = (byte) (int) (l >> 24);
            payload[currentPosition++] = (byte) (int) (l >> 16);
            payload[currentPosition++] = (byte) (int) (l >> 8);
            payload[currentPosition++] = (byte) (int) l;
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("14395, " + 5 + ", " + l + ", "
                    + runtimeexception.toString());
            throw new RuntimeException();
        }
    }

    public void writeString(String s) {
        System.arraycopy(s.getBytes(), 0, payload, currentPosition, s.length());
        currentPosition += s.length();
        payload[currentPosition++] = 10;
    }


	public void writeWordBigEndian(int i) {
		payload[currentPosition++] = (byte) i;
	}

    public void writeBytes(byte abyte0[], int length, int startingPosition) {
        for (int k = startingPosition; k < startingPosition + length; k++)
            payload[currentPosition++] = abyte0[k];
    }

    public void writeHiddenString(String string) {
        writeByte(string.length());
        byte[] stringBytes = string.getBytes();
        for (int index = 0; index < stringBytes.length; index++)
            stringBytes[index] += 15;
        writeBytes(stringBytes, stringBytes.length, 0);
    }

    public String readHiddenString() {
        int length = readUnsignedByte();
        byte[] stringBytes = new byte[length];
        for (int index = 0; index < length; index++) {
            stringBytes[index] = (byte) (readUnsignedByte() - 15);
        }
        return new String(stringBytes);
    }

    public void writeLengthInt(int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        } else {
            this.payload[this.currentPosition - value - 4] = (byte) (value >> 24);
            this.payload[this.currentPosition - value - 3] = (byte) (value >> 16);
            this.payload[this.currentPosition - value - 2] = (byte) (value >> 8);
            this.payload[this.currentPosition - value - 1] = (byte) value;
        }
    }

    public void writeByte(int i) {
        payload[currentPosition++] = (byte) i;
    }

    public void writeMedium(int var1) {
        this.payload[++this.currentPosition - 1] = (byte) (var1 >> 16);
        this.payload[++this.currentPosition - 1] = (byte) (var1 >> 8);
        this.payload[++this.currentPosition - 1] = (byte) var1;
    }

    public int readVarInt() {
        byte var1 = this.payload[++this.currentPosition - 1]; // L: 410
        int var2;
        for (var2 = 0; var1 < 0; var1 = this.payload[++this.currentPosition - 1]) { // L:
            var2 = (var2 | var1 & 127) << 7;
        }
        return var2 | var1;
    }

    public String readCESU8() {
        byte var1 = this.payload[++this.currentPosition - 1];
        if (var1 != 0) {
            throw new IllegalStateException("");
        } else {
            int var2 = this.readVarInt();
            if (var2 + this.currentPosition > this.payload.length) {
                throw new IllegalStateException("");
            } else {
                byte[] var4 = this.payload;
                int var5 = this.currentPosition;
                char[] var6 = new char[var2];
                int var7 = 0;
                int var8 = var5;

                int var11;
                for (int var9 = var2 + var5; var8 < var9; var6[var7++] = (char) var11) {
                    int var10 = var4[var8++] & 255;
                    if (var10 < 128) {
                        if (var10 == 0) {
                            var11 = 65533;
                        } else {
                            var11 = var10;
                        }
                    } else if (var10 < 192) {
                        var11 = 65533;
                    } else if (var10 < 224) {
                        if (var8 < var9 && (var4[var8] & 192) == 128) {
                            var11 = (var10 & 31) << 6 | var4[var8++] & 63;
                            if (var11 < 128) {
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533;
                        }
                    } else if (var10 < 240) {
                        if (var8 + 1 < var9 && (var4[var8] & 192) == 128 && (var4[var8 + 1] & 192) == 128) {
                            var11 = (var10 & 15) << 12 | (var4[var8++] & 63) << 6 | var4[var8++] & 63;
                            if (var11 < 2048) {
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533;
                        }
                    } else if (var10 < 248) {
                        if (var8 + 2 < var9 && (var4[var8] & 192) == 128 && (var4[var8 + 1] & 192) == 128 && (var4[var8 + 2] & 192) == 128) {
                            var11 = (var10 & 7) << 18 | (var4[var8++] & 63) << 12 | (var4[var8++] & 63) << 6 | var4[var8++] & 63;
                            if (var11 >= 65536 && var11 <= 1114111) {
                                var11 = 65533;
                            } else {
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533;
                        }
                    } else {
                        var11 = 65533;
                    }
                }

                String var3 = new String(var6, 0, var7);
                this.currentPosition += var2;
                return var3;
            }
        }
    }

    public void writeCESU8(CharSequence charSequence) {
        int value = method868(charSequence);
        this.payload[++this.currentPosition - 1] = 0;
        this.writeVarInt(value);
        this.currentPosition += method3810(this.payload, this.currentPosition, charSequence);
    }

    public void writeVarInt(int var1) {
        if ((var1 & -128) != 0) {
            if ((var1 & -16384) != 0) {
                if ((var1 & -2097152) != 0) {
                    if ((var1 & -268435456) != 0) {
                        this.writeByte(var1 >>> 28 | 128);
                    }
                    this.writeByte(var1 >>> 21 | 128);
                }
                this.writeByte(var1 >>> 14 | 128);
            }
            this.writeByte(var1 >>> 7 | 128);
        }
        this.writeByte(var1 & 127);
    }

    public static int method868(CharSequence str) {
        int length = str.length();
        int byteLength = 0;

        for (int currentChar = 0; currentChar < length; ++currentChar) {
            char charAt = str.charAt(currentChar);
            if (charAt <= 127) {
                ++byteLength;
            } else if (charAt <= 2047) {
                byteLength += 2;
            } else {
                byteLength += 3;
            }
        }

        return byteLength;
    }

    public static int method3810(byte[] output, int offset, CharSequence input) {
        int length = input.length();
        int outIndex = offset;

        for (int currentChar = 0; currentChar < length; ++currentChar) {
            char charAt = input.charAt(currentChar);
            if (charAt <= 127) {
                output[outIndex++] = (byte) charAt;
            } else if (charAt <= 2047) {
                output[outIndex++] = (byte) (192 | charAt >> 6);
                output[outIndex++] = (byte) (128 | charAt & 63);
            } else {
                output[outIndex++] = (byte) (224 | charAt >> 12);
                output[outIndex++] = (byte) (128 | charAt >> 6 & 63);
                output[outIndex++] = (byte) (128 | charAt & 63);
            }
        }

        return outIndex - offset;
    }

    public void writeShort(int value) {
        payload[currentPosition++] = (byte) (value >> 8);
        payload[currentPosition++] = (byte) value;
    }

    public void writeInt(int value) {
        payload[currentPosition++] = (byte) (value >> 24);
        payload[currentPosition++] = (byte) (value >> 16);
        payload[currentPosition++] = (byte) (value >> 8);
        payload[currentPosition++] = (byte) value;
    }

    @Override
    public void writeLong(long var1) {

    }

    @Override
    public void writeStringCp1252NullTerminated(String string) {

    }

    public void writeBytes(int i) {
        payload[currentPosition - i - 1] = (byte) i;
    }

    public int read24BitInt()
    {
        return (this.readUnsignedByte() << 16) + (this.readUnsignedByte() << 8) + this.readUnsignedByte();
    }

    public int readShortSmart() {
        int var1 = this.payload[this.currentPosition] & 255;
        return var1 < 128?this.readUnsignedByte() - 64:this.readUShort() - '\uc000';
    }

    public int readNullableLargeSmart() {
        if (this.payload[this.currentPosition] < 0) {
            return this.readInt() & Integer.MAX_VALUE;
        } else {
            int var1 = this.readUShort();
            return var1 == 32767 ? -1 : var1;
        }
    }

    public int readShortSmartSub() {
        int var1 = this.payload[this.currentPosition] & 255;
        return var1 < 128 ? this.readUnsignedByte() - 1 : this.readUShort() - 0x8000;
    }

    public int readUnsignedByte() {
        return payload[currentPosition++] & 0xff;
    }

    public byte readSignedByte() {
        return payload[currentPosition++];
    }

    public int readShort3() {
        this.currentPosition += 2;
        int value = ((this.payload[this.currentPosition - 2] & 0xff) << 8) + (this.payload[this.currentPosition - 1] & 0xff);

        if (value > 60000)
            value = -65535 + value;
        return value;
    }

    public int readTriByte() {
        currentPosition += 3;
        return ((payload[currentPosition - 3] & 0xff) << 16)
                + ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);
    }


    public int readUShort() {
        currentPosition += 2;
        return ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);
    }

    public int method1606() {
        int var2 = 0;

        int var3;
        for (var3 = this.readUShortSmart(); var3 == 32767; var3 = this.readUShortSmart()) {
            var2 += 32767;
        }

        var2 += var3;
        return var2;
    }

    public int readUShortSmart() {
        int var2 = this.payload[this.currentPosition] & 255;
        return var2 < 128 ? this.readUnsignedByte() : this.readUShort() - 32768;
    }

    public int readSignedWord() {
        currentPosition += 2;
        int i = ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);
        if (i > 32767)
            i -= 0x10000;
        return i;
    }

    public int read3Bytes() {
        currentPosition += 3;
        return ((payload[currentPosition - 3] & 0xff) << 16)
                + ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);
    }

    public int readDWord() {
        currentPosition += 4;
        return ((payload[currentPosition - 4] & 0xff) << 24)
                + ((payload[currentPosition - 3] & 0xff) << 16)
                + ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] & 0xff);
    }

    public long readQWord() {
        long l = readDWord() & 0xffffffffL;
        long l1 = readDWord() & 0xffffffffL;
        return (l << 32) + l1;
    }

    public String readString() {
        int i = currentPosition;
        while (payload[currentPosition++] != 10)
            ;
        return new String(payload, i, currentPosition - i - 1);
    }

    public String readNullTerminatedString() {
        int i = currentPosition;
        while (payload[currentPosition++] != 0)
            ;
        return new String(payload, i, currentPosition - i - 1);
    }

    private static final char[] OSRS_CHARACTERS = new char[]
            {
                    '\u20ac', '\u0000', '\u201a', '\u0192', '\u201e', '\u2026',
                    '\u2020', '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039',
                    '\u0152', '\u0000', '\u017d', '\u0000', '\u0000', '\u2018',
                    '\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014',
                    '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\u0000',
                    '\u017e', '\u0178'
            };

    public String readOSRSString()
    {
        StringBuilder sb = new StringBuilder();

        for (; ; )
        {
            int ch = this.readUnsignedByte();

            if (ch == 0)
            {
                break;
            }

            if (ch >= 128 && ch < 160)
            {
                char var7 = OSRS_CHARACTERS[ch - 128];
                if (0 == var7)
                {
                    var7 = '?';
                }

                ch = var7;
            }

            sb.append((char) ch);
        }
        return sb.toString();
    }

    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(this.payload, this.currentPosition, bytes, 0, length);
        this.currentPosition += length;
        return bytes;
    }


    public byte[] readBytes() {
        int i = currentPosition;
        while (payload[currentPosition++] != 10)
            ;
        byte abyte0[] = new byte[currentPosition - i - 1];
        System.arraycopy(payload, i, abyte0, i - i, currentPosition - 1 - i);
        return abyte0;
    }

    public void readBytes(byte[] var1, int var2, int var3) {
        for (int var4 = var2; var4 < var3 + var2; ++var4) {
            var1[var4] = this.payload[++this.currentPosition - 1];
        }
    }


    public void readBytes(int length, int offset, byte abyte0[]) {
        for (int l = offset; l < offset + length; l++)
            abyte0[l] = payload[currentPosition++];
    }

    public void initBitAccess() {
        bitPosition = currentPosition * 8;
    }

    public int readBits(int i) {
        int k = bitPosition >> 3;
        int l = 8 - (bitPosition & 7);
        int i1 = 0;
        bitPosition += i;
        for (; i > l; l = 8) {
            i1 += (payload[k++] & anIntArray1409[l]) << i - l;
            i -= l;
        }
        if (i == l)
            i1 += payload[k] & anIntArray1409[l];
        else
            i1 += payload[k] >> l - i & anIntArray1409[i];
        return i1;
    }

    public void finishBitAccess() {
        currentPosition = (bitPosition + 7) / 8;
    }

    public int method421() {
        try {
            int i = payload[currentPosition] & 0xff;
            if (i < 128)
                return readUnsignedByte() - 64;
            else
                return readUShort() - 49152;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void doKeys() {
        int i = currentPosition;
        currentPosition = 0;
        byte abyte0[] = new byte[i];
        readBytes(i, 0, abyte0);
        BigInteger biginteger2 = new BigInteger(abyte0);
        BigInteger biginteger3 = biginteger2.modPow(RSA_EXPONENT, RSA_MODULUS);
        byte abyte1[] = biginteger3.toByteArray();
        currentPosition = 0;
        writeUnsignedByte(abyte1.length);
        writeBytes(abyte1, abyte1.length, 0);
    }

    public void method424(int i) {
        payload[currentPosition++] = (byte) (-i);
    }

    public void method425(int j) {
        payload[currentPosition++] = (byte) (128 - j);
    }

    public int method426() {
        return payload[currentPosition++] - 128 & 0xff;
    }

    public int readNegUByte() {
        return -payload[currentPosition++] & 0xff;
    }

    public int method428() {
        return 128 - payload[currentPosition++] & 0xff;
    }

    public byte method429() {
        return (byte) (-payload[currentPosition++]);
    }

    public byte method430() {
        return (byte) (128 - payload[currentPosition++]);
    }

    public void method431(int i) {
        payload[currentPosition++] = (byte) i;
        payload[currentPosition++] = (byte) (i >> 8);
    }

    public void method432(int j) {
        payload[currentPosition++] = (byte) (j >> 8);
        payload[currentPosition++] = (byte) (j + 128);
    }

    public void method433(int j) {
        payload[currentPosition++] = (byte) (j + 128);
        payload[currentPosition++] = (byte) (j >> 8);
    }

    public int method434() {
        currentPosition += 2;
        return ((payload[currentPosition - 1] & 0xff) << 8)
                + (payload[currentPosition - 2] & 0xff);
    }

    public int readUShortA() {
        currentPosition += 2;
        return ((payload[currentPosition - 2] & 0xff) << 8)
                + (payload[currentPosition - 1] - 128 & 0xff);
    }

    public int method436() {
        currentPosition += 2;
        return ((payload[currentPosition - 1] & 0xff) << 8)
                + (payload[currentPosition - 2] - 128 & 0xff);
    }

    public int method437() {
        currentPosition += 2;
        int j = ((payload[currentPosition - 1] & 0xff) << 8)
                + (payload[currentPosition - 2] & 0xff);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    public int method438() {
        currentPosition += 2;
        int j = ((payload[currentPosition - 1] & 0xff) << 8)
                + (payload[currentPosition - 2] - 128 & 0xff);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    public int method439() {
        currentPosition += 4;
        return ((payload[currentPosition - 2] & 0xff) << 24)
                + ((payload[currentPosition - 1] & 0xff) << 16)
                + ((payload[currentPosition - 4] & 0xff) << 8)
                + (payload[currentPosition - 3] & 0xff);
    }

    public int method440() {
        currentPosition += 4;
        return ((payload[currentPosition - 3] & 0xff) << 24)
                + ((payload[currentPosition - 4] & 0xff) << 16)
                + ((payload[currentPosition - 1] & 0xff) << 8)
                + (payload[currentPosition - 2] & 0xff);
    }
    public int readUnsignedIntSmartShortCompat() {
        int var1 = 0;

        int var2;
        for (var2 = this.readUSmart(); var2 == 32767; var2 = this.readUSmart()) {
            var1 += 32767;
        }

        var1 += var2;
        return var1;
    }

    public int readUSmart() {
        int peek = payload[currentPosition] & 0xFF;
        return peek < 128 ? this.readUnsignedByte() : this.readUShort() - 0x8000;
    }
    public int readSmart() {
        int value = payload[currentPosition] & 0xFF;
        if (value < 128) {
            return readUnsignedByte() - 64;
        } else {
            return readUShort() - 49152;
        }
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    @Override
    public int getOffset() {
        return currentPosition;
    }

    public void setOffset(int var11) {
        currentPosition = var11;
    }
    public void method441(int i, byte abyte0[], int j) {
        for (int k = (i + j) - 1; k >= i; k--)
            payload[currentPosition++] = (byte) (abyte0[k] + 128);

    }

    public void method442(int i, int j, byte abyte0[]) {
        for (int k = (j + i) - 1; k >= j; k--)
            abyte0[k] = payload[currentPosition++];

    }
    public int getUIncrementalSmart() {
        int value = 0, remainder;
        for (remainder = readUSmart(); remainder == 32767; remainder = readUSmart()) {
            value += 32767;
        }
        value += remainder;
        return value;
    }
    public int getLength() {
        return payload.length;
    }

    public byte payload[];
    public int currentPosition;
    public int bitPosition;
    public static final int[] anIntArray1409 = {0, 1, 3, 7, 15, 31, 63, 127,
            255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff,
            0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
            0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
            0x7fffffff, -1};
    public ISAACRandomGen encryption;
    public static int anInt1412;
    public static final Deque nodeList = new Deque();
    public byte readByte() {
        return this.payload[++this.currentPosition - 1];
    }

    @Override
    public int readUnsignedShort() {
        return readUShort();
    }

    public int readInt() {
        currentPosition += 4;
        return ((payload[currentPosition - 4] & 0xff) << 24) + ((payload[currentPosition - 3] & 0xff) << 16) + ((payload[currentPosition - 2] & 0xff) << 8) + (payload[currentPosition - 1] & 0xff);
    }
}
