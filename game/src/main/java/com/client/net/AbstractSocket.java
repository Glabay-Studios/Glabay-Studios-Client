package com.client.net;

import net.runelite.rs.api.RSAbstractSocket;

import java.io.IOException;

public abstract class AbstractSocket implements RSAbstractSocket {


    public abstract boolean isAvailable(int var1) throws IOException;

    public abstract int available() throws IOException;

    public abstract int readUnsignedByte() throws IOException;

    public abstract int read(byte[] var1, int var2, int var3) throws IOException;

    public abstract void write(byte[] var1, int var2, int var3) throws IOException;

    public abstract void close();

}
