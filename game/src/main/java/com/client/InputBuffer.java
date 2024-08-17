package com.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import one.util.streamex.IntStreamEx;

public class InputBuffer {

    public ByteBuf buffer;
    public int pos;
    public int bitPosition;

    private static final int[] BIT_MASKS = {0, 1, 3, 7, 15, 31, 63, 127, 255,
            511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff,
            0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
            0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
            0x7fffffff, -1};

    public InputBuffer(byte[] data) {
        buffer = Unpooled.wrappedBuffer(data);
        this.pos = 0;
    }

    public InputBuffer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public byte readSignedByte() {
        return buffer.readByte();
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public int readUnsignedByte() {
        return buffer.readUnsignedByte();
    }

    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    public int readSignedShort() {
        return buffer.readShort();
    }

    public int readUnsignedShort() {
        return buffer.readUnsignedShort();
    }

    public int readInt() {
        return buffer.readInt();
    }

    public long readLong() {
        return buffer.readLong();
    }

    public int readSignedSmart() {
        int value = peek() & 0xFF;
        return value < 128 ? buffer.readUnsignedByte() - 64 : buffer.readUnsignedShort() - 49152;
    }

    public int readUnsignedSmart() {
        int value = peek() & 0xFF;
        return value < 128 ? buffer.readUnsignedByte() : buffer.readUnsignedShort() - 32768;
    }

    public int readBigSmart2() {
        if (peek() < 0) {
            return readInt() & Integer.MAX_VALUE; // and off sign bit
        }
        int value = readUnsignedShort();
        return value == 32767 ? -1 : value;
    }

    public int readUnsignedShortSmartMinusOne()
    {
        int peek = this.peek() & 0xFF;
        return peek < 128 ? this.readUnsignedByte() - 1 : this.readUnsignedShort() - 0x8001;
    }

    public int peek() {
        return buffer.getByte(buffer.readerIndex());
    }

    public int getPosition() {
        return buffer.readerIndex();
    }

    public void setPosition(int position) {
        buffer.readerIndex(position);
    }

    public void decrementPosition(int amount) {
        buffer.readerIndex(buffer.readerIndex() - amount);
    }

    public void incrementPosition(int amount) {
        buffer.readerIndex(buffer.readerIndex() + amount);
    }

    public byte[] getArray() {
        if (buffer.hasArray()) {
            return buffer.array();
        }
        byte[] array = new byte[buffer.readableBytes()];
        buffer.readBytes(array);
        return array;
    }

    public void disableBitAccess() {
        pos = (pos + 7) / 8;
    }

    public String readString() {
        return IntStreamEx.generate(this::readSignedByte).takeWhile(b -> b != 10).charsToString();
    }

    public String readStringOSRS2() {
        return IntStreamEx.generate(this::readUnsignedByte)
                .takeWhile(b -> b != 0)
                .map(b -> {
                    if (b >= 128 && b < 160) {
                        char c = CHARACTERS[b - 128];
                        return c == 0 ? '?' : c;
                    }
                    return b;
                }).charsToString();
    }

    public String readStringOSRS()
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
                char var7 = CHARACTERS[ch - 128];
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

    public int readLEUShort() {
        pos += 2;
        return ((buffer.array()[pos - 1] & 0xff) << 8) + (buffer.array()[pos - 2] & 0xff);
    }

    public int readUShortA() {
        pos += 2;
        return ((buffer.array()[pos - 2] & 0xff) << 8)
                + (buffer.array()[pos - 1] - 128 & 0xff);
    }

    public void initBitAccess() {
        bitPosition = pos * 8;
    }


    public int readBits(int amount) {
        int byteOffset = pos >> 3;
        int bitOffset = 8 - (pos & 7);
        int value = 0;
        pos += amount;
        for (; amount > bitOffset; bitOffset = 8) {
            value += (buffer.array()[byteOffset++] & BIT_MASKS[bitOffset]) << amount
                    - bitOffset;
            amount -= bitOffset;
        }
        if (amount == bitOffset)
            value += buffer.array()[byteOffset] & BIT_MASKS[bitOffset];
        else
            value += buffer.array()[byteOffset] >> bitOffset - amount
                    & BIT_MASKS[amount];
        return value;
    }

    public int readLEUShortA() {
        pos += 2;
        return ((buffer.array()[pos - 1] & 0xff) << 8)
                + (buffer.array()[pos - 2] - 128 & 0xff);
    }

    public void writeByteS(int value) {
        buffer.array()[pos++] = (byte) (128 - value);
    }

    public int readNegUByte() {
        return -buffer.array()[pos++] & 0xff;
    }

    public byte readByteS() {
        return (byte) (128 - buffer.array()[pos++]);
    }

    public int readMEInt() { // V1
        pos += 4;
        return ((buffer.array()[pos - 2] & 0xff) << 24)
                + ((buffer.array()[pos - 1] & 0xff) << 16)
                + ((buffer.array()[pos - 4] & 0xff) << 8)
                + (buffer.array()[pos - 3] & 0xff);
    }

    public int readIMEInt() {
        pos += 4;
        return ((buffer.array()[pos - 3] & 0xff) << 24)
                + ((buffer.array()[pos - 4] & 0xff) << 16)
                + ((buffer.array()[pos - 1] & 0xff) << 8)
                + (buffer.array()[pos - 2] & 0xff);
    }

    public int readUSmart() {
        int peek = buffer.array()[pos] & 0xFF;
        return peek < 128 ? this.readUnsignedByte() : this.readUnsignedShort() - 0x8000;
    }

    public int readUByteA() {
        return buffer.array()[pos++] - 128 & 0xff;
    }

    public int readLEShortA() {
        pos += 2;
        int value = ((buffer.array()[pos - 1] & 0xff) << 8) + (buffer.array()[pos - 2] - 128 & 0xff);
        if (value > 32767)
            value -= 0x10000;
        return value;
    }

    public byte[] getStringBytes() {
        return IntStreamEx.generate(this::readUnsignedByte).takeWhile(b -> b != 10).toByteArray();
    }

    public int read24BitInt() {
        return buffer.readUnsignedMedium();
    }

    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    // TODO write method & test
    public int readIncrSmallSmart() {
        int current = 0;
        int read;
        for (read = this.readUnsignedSmart(); read == 32767; read = this.readUnsignedSmart()) {
            current += 32767;
        }

        current += read;
        return current;
    }

    public int getLength() {
        return getArray().length;
    }

    public int getCapacity() {
        return buffer.capacity();
    }

    private static final char[] CHARACTERS = new char[]{
            '\u20ac', '\u0000', '\u201a', '\u0192', '\u201e', '\u2026',
            '\u2020', '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039',
            '\u0152', '\u0000', '\u017d', '\u0000', '\u0000', '\u2018',
            '\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014',
            '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\u0000',
            '\u017e', '\u0178'
    };

}
