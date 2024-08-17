package com.client.net;

import net.runelite.rs.api.RSAbstractByteArrayCopier;

public abstract class AbstractByteArrayCopier implements RSAbstractByteArrayCopier {

    public abstract byte[] get();


    public abstract void set(byte[] var1);
}
