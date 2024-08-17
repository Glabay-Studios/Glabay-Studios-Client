package com.client.utilities;

import java.io.IOException;
import java.io.OutputStream;

public final class TeeOutputStream extends OutputStream {

    private final OutputStream first;
    private final OutputStream second;

    public TeeOutputStream(OutputStream first, OutputStream second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void write(int data) throws IOException {
        first.write(data);
        second.write(data);
    }

    @Override
    public void write(byte[] data) throws IOException {
        first.write(data);
        second.write(data);
    }

    @Override
    public void write(byte[] data, int offset, int length) throws IOException {
        first.write(data, offset, length);
        second.write(data, offset, length);
    }

    @Override
    public void flush() throws IOException {
        first.flush();
        second.flush();
    }

    @Override
    public void close() throws IOException {
        first.close();
        second.close();
    }

}