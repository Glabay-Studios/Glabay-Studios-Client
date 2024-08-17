package com.client.net;

import net.runelite.rs.api.RSDirectByteArrayCopier;

import java.nio.ByteBuffer;

public class DirectByteArrayCopier extends AbstractByteArrayCopier implements RSDirectByteArrayCopier {

    ByteBuffer directBuffer;


    public byte[] get() {
        byte[] data = new byte[this.directBuffer.capacity()];
        this.directBuffer.position(0);
        this.directBuffer.get(data);
        return data;
    }


    public void set(byte[] data) {
        this.directBuffer = ByteBuffer.allocateDirect(data.length);
        this.directBuffer.position(0);
        this.directBuffer.put(data);
    }
}
