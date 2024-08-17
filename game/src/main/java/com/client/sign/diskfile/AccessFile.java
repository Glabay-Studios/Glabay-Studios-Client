package com.client.sign.diskfile;

import net.runelite.rs.api.RSAccessFile;

import java.io.*;

public final class AccessFile implements RSAccessFile {

    private RandomAccessFile file;

    private final long maxSize;

    private long offset;

    public AccessFile(File file, String mode, long maxSize) throws IOException {
        if (-1L == maxSize) {
            maxSize = Long.MAX_VALUE;
        }

        if (file.length() > maxSize) {
            file.delete();
        }

        this.file = new RandomAccessFile(file, mode);
        this.maxSize = maxSize;
        this.offset = 0L;
        int var5 = this.file.read();
        if (var5 != -1 && !mode.equals("r")) {
            this.file.seek(0L);
            this.file.write(var5);
        }

        this.file.seek(0L);
    }

    final void seek(long pos) throws IOException {
        this.file.seek(pos);
        this.offset = pos;
    }


    public final void write(byte[] data, int offset, int len) throws IOException {
        if ((long) len + this.offset > this.maxSize) {
            this.file.seek(this.maxSize);
            this.file.write(1);
            throw new EOFException("End of the file was reached!");
        } else {
            this.file.write(data, offset, len);
            this.offset += (long) len;
        }
    }


    public final void close() throws IOException {
        this.closeSync(false);
    }


    public final void closeSync(boolean close) throws IOException {
        if (this.file != null) {
            if (close) {
                try {
                    this.file.getFD().sync();
                } catch (SyncFailedException ignored) {
                }
            }

            this.file.close();
            this.file = null;
        }

    }


    public final long length() throws IOException {
        return this.file.length();
    }

    public final int read(byte[] bytes, int off, int len) throws IOException {
        int var4 = this.file.read(bytes, off, len);
        if (var4 > 0) {
            this.offset += (long) var4;
        }

        return var4;
    }

    protected void finalize() throws Throwable {
        if (this.file != null) {
            System.out.println("Warning! fileondisk " + file + " not closed correctly using close(). Auto-closing instead. ");
            this.close();
        }

    }

    @Override
    public RandomAccessFile getFile() {
        return file;
    }

    @Override
    public long getPosition() {
        return offset;
    }

    @Override
    public long getLength() {
        return maxSize;
    }
}
