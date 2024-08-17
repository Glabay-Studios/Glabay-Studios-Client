package com.client.net.tcp.buffer;

import com.client.net.AbstractSocket;

import java.io.IOException;
import java.net.Socket;

public class BufferedNetSocket extends AbstractSocket {

    Socket socket;

    BufferedSource source;

    BufferedSink sink;

    public BufferedNetSocket(Socket var1, int var2, int var3) throws IOException {
        this.socket = var1;
        this.socket.setSoTimeout(30000);
        this.socket.setTcpNoDelay(true);
        this.socket.setReceiveBufferSize(65536);
        this.socket.setSendBufferSize(65536);
        this.source = new BufferedSource(this.socket.getInputStream(), var2);
        this.sink = new BufferedSink(this.socket.getOutputStream(), var3);
    }


    public boolean isAvailable(int var1) throws IOException {
        return this.source.isAvailable(var1);
    }

    public int available() throws IOException {
        return this.source.available();
    }

    public int readUnsignedByte() throws IOException {
        return this.source.readUnsignedByte();
    }

    public int read(byte[] var1, int var2, int var3) throws IOException {
        return this.source.read(var1, var2, var3);
    }

    public void write(byte[] var1, int var2, int var3) throws IOException {
        this.sink.write(var1, var2, var3);
    }

    public void close() {
        this.sink.close();

        try {
            this.socket.close();
        } catch (IOException var2) {
        }

        this.source.close();
    }

    protected void finalize() {
        this.close();
    }
}

