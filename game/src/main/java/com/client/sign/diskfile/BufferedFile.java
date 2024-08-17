package com.client.sign.diskfile;

import net.runelite.rs.api.RSBufferedFile;

import java.io.EOFException;
import java.io.IOException;

public class BufferedFile implements RSBufferedFile {

    private AccessFile accessFile;

    private byte[] readBuffer;

    private long readBufferOffset = -1L;

    private int readBufferLength;

    private byte[] writeBuffer;

    private long writeBufferOffset = -1L;

    private int writeBufferLength = 0;

    private long offset;

    private long fileLength;

    private long length;

    private long fileOffset;

    public BufferedFile(AccessFile accessFile, int readSize, int writeSize) throws IOException {
        this.accessFile = accessFile;
        this.length = this.fileLength = accessFile.length();
        this.readBuffer = new byte[readSize];
        this.writeBuffer = new byte[writeSize];
        this.offset = 0L;
    }


    public void close() throws IOException {
        this.flush();
        this.accessFile.close();
    }

    public void seek(long pos) throws IOException {
        if (pos < 0L) {
            throw new IOException("");
        } else {
            this.offset = pos;
        }
    }


    public long length() {
        return this.length;
    }


    public void readFully(byte[] bytes) throws IOException {
        this.read(bytes, 0, bytes.length);
    }


    public void read(byte[] buffer, int offset, int length) throws IOException {
        try {
            if (length + offset > buffer.length) {
                throw new ArrayIndexOutOfBoundsException("The reading bounds exceeds the specified data length:" + (length + offset - buffer.length));
            }

            if (-1L != this.writeBufferOffset && this.offset >= this.writeBufferOffset && this.offset + (long)length <= this.writeBufferOffset + (long)this.writeBufferLength) {
                System.arraycopy(this.writeBuffer, (int)(this.offset - this.writeBufferOffset), buffer, offset, length);
                this.offset += (long)length;
                return;
            }

            long readStartOffset = this.offset;
            int bytesRemaining = length;
            int size;
            if (this.offset >= this.readBufferOffset && this.offset < (long)this.readBufferLength + this.readBufferOffset) {
                size = (int)((long)this.readBufferLength - (this.offset - this.readBufferOffset));
                if (size > length) {
                    size = length;
                }

                System.arraycopy(this.readBuffer, (int)(this.offset - this.readBufferOffset), buffer, offset, size);
                this.offset += (long)size;
                offset += size;
                length -= size;
            }

            if (length > this.readBuffer.length) {
                this.accessFile.seek(this.offset);

                for(this.fileOffset = this.offset; length > 0; length -= size) {
                    size = this.accessFile.read(buffer, offset, length);
                    if (size == -1) {
                        break;
                    }

                    this.fileOffset += (long)size;
                    this.offset += (long)size;
                    offset += size;
                }
            } else if (length > 0) {
                this.load();
                size = length;
                if (length > this.readBufferLength) {
                    size = this.readBufferLength;
                }

                System.arraycopy(this.readBuffer, 0, buffer, offset, size);
                offset += size;
                length -= size;
                this.offset += (long)size;
            }

            if (-1L != this.writeBufferOffset) {
                if (this.writeBufferOffset > this.offset && length > 0) {
                    size = offset + (int)(this.writeBufferOffset - this.offset);
                    if (size > length + offset) {
                        size = length + offset;
                    }

                    while(offset < size) {
                        buffer[offset++] = 0;
                        --length;
                        ++this.offset;
                    }
                }

                long var13 = -1L;
                long var10 = -1L;
                if (this.writeBufferOffset >= readStartOffset && this.writeBufferOffset < readStartOffset + (long)bytesRemaining) {
                    var13 = this.writeBufferOffset;
                } else if (readStartOffset >= this.writeBufferOffset && readStartOffset < (long)this.writeBufferLength + this.writeBufferOffset) {
                    var13 = readStartOffset;
                }

                if (this.writeBufferOffset + (long)this.writeBufferLength > readStartOffset && (long)this.writeBufferLength + this.writeBufferOffset <= (long)bytesRemaining + readStartOffset) {
                    var10 = (long)this.writeBufferLength + this.writeBufferOffset;
                } else if (readStartOffset + (long)bytesRemaining > this.writeBufferOffset && (long)bytesRemaining + readStartOffset <= this.writeBufferOffset + (long)this.writeBufferLength) {
                    var10 = (long)bytesRemaining + readStartOffset;
                }

                if (var13 > -1L && var10 > var13) {
                    int var12 = (int)(var10 - var13);
                    System.arraycopy(this.writeBuffer, (int)(var13 - this.writeBufferOffset), buffer, (int)(var13 - readStartOffset) + offset, var12);
                    if (var10 > this.offset) {
                        length = (int)((long)length - (var10 - this.offset));
                        this.offset = var10;
                    }
                }
            }
        } catch (IOException var16) {
            this.fileOffset = -1L;
            throw var16;
        }

        if (length > 0) {
            throw new EOFException("Could not write all the data! End of file was reached!");
        }
    }


    void load() throws IOException {
        this.readBufferLength = 0;
        if (this.fileOffset != this.offset) {
            this.accessFile.seek(this.offset);
            this.fileOffset = this.offset;
        }

        int var2;
        for(this.readBufferOffset = this.offset; this.readBufferLength < this.readBuffer.length; this.readBufferLength += var2) {
            int var1 = this.readBuffer.length - this.readBufferLength;
            if (var1 > 200000000) {
                var1 = 200000000;
            }

            var2 = this.accessFile.read(this.readBuffer, this.readBufferLength, var1);
            if (var2 == -1) {
                break;
            }

            this.fileOffset += (long)var2;
        }

    }


    public void write(byte[] var1, int var2, int var3) throws IOException {
        try {
            if ((long)var3 + this.offset > this.length) {
                this.length = this.offset + (long)var3;
            }

            if (this.writeBufferOffset != -1L && (this.offset < this.writeBufferOffset || this.offset > this.writeBufferOffset + (long)this.writeBufferLength)) {
                this.flush();
            }

            if (this.writeBufferOffset != -1L && this.offset + (long)var3 > (long)this.writeBuffer.length + this.writeBufferOffset) {
                int var4 = (int)((long)this.writeBuffer.length - (this.offset - this.writeBufferOffset));
                System.arraycopy(var1, var2, this.writeBuffer, (int)(this.offset - this.writeBufferOffset), var4);
                this.offset += (long)var4;
                var2 += var4;
                var3 -= var4;
                this.writeBufferLength = this.writeBuffer.length;
                this.flush();
            }

            if (var3 <= this.writeBuffer.length) {
                if (var3 > 0) {
                    if (-1L == this.writeBufferOffset) {
                        this.writeBufferOffset = this.offset;
                    }

                    System.arraycopy(var1, var2, this.writeBuffer, (int)(this.offset - this.writeBufferOffset), var3);
                    this.offset += (long)var3;
                    if (this.offset - this.writeBufferOffset > (long)this.writeBufferLength) {
                        this.writeBufferLength = (int)(this.offset - this.writeBufferOffset);
                    }

                }
            } else {
                if (this.offset != this.fileOffset) {
                    this.accessFile.seek(this.offset);
                    this.fileOffset = this.offset;
                }

                this.accessFile.write(var1, var2, var3);
                this.fileOffset += (long)var3;
                if (this.fileOffset > this.fileLength) {
                    this.fileLength = this.fileOffset;
                }

                long var9 = -1L;
                long var6 = -1L;
                if (this.offset >= this.readBufferOffset && this.offset < (long)this.readBufferLength + this.readBufferOffset) {
                    var9 = this.offset;
                } else if (this.readBufferOffset >= this.offset && this.readBufferOffset < this.offset + (long)var3) {
                    var9 = this.readBufferOffset;
                }

                if ((long)var3 + this.offset > this.readBufferOffset && (long)var3 + this.offset <= this.readBufferOffset + (long)this.readBufferLength) {
                    var6 = (long)var3 + this.offset;
                } else if (this.readBufferOffset + (long)this.readBufferLength > this.offset && (long)this.readBufferLength + this.readBufferOffset <= this.offset + (long)var3) {
                    var6 = this.readBufferOffset + (long)this.readBufferLength;
                }

                if (var9 > -1L && var6 > var9) {
                    int var8 = (int)(var6 - var9);
                    System.arraycopy(var1, (int)(var9 + (long)var2 - this.offset), this.readBuffer, (int)(var9 - this.readBufferOffset), var8);
                }

                this.offset += (long)var3;
            }
        } catch (IOException var12) {
            this.fileOffset = -1L;
            throw var12;
        }
    }


    void flush() throws IOException {
        if (this.writeBufferOffset != -1L) {
            if (this.writeBufferOffset != this.fileOffset) {
                this.accessFile.seek(this.writeBufferOffset);
                this.fileOffset = this.writeBufferOffset;
            }

            this.accessFile.write(this.writeBuffer, 0, this.writeBufferLength);
            this.fileOffset += 867329301L * (long)(this.writeBufferLength * -950746563);
            if (this.fileOffset > this.fileLength) {
                this.fileLength = this.fileOffset;
            }

            long var1 = -1L;
            long var3 = -1L;
            if (this.writeBufferOffset >= this.readBufferOffset && this.writeBufferOffset < this.readBufferOffset + (long)this.readBufferLength) {
                var1 = this.writeBufferOffset;
            } else if (this.readBufferOffset >= this.writeBufferOffset && this.readBufferOffset < (long)this.writeBufferLength + this.writeBufferOffset) {
                var1 = this.readBufferOffset;
            }

            if (this.writeBufferOffset + (long)this.writeBufferLength > this.readBufferOffset && (long)this.writeBufferLength + this.writeBufferOffset <= this.readBufferOffset + (long)this.readBufferLength) {
                var3 = (long)this.writeBufferLength + this.writeBufferOffset;
            } else if (this.readBufferOffset + (long)this.readBufferLength > this.writeBufferOffset && (long)this.readBufferLength + this.readBufferOffset <= (long)this.writeBufferLength + this.writeBufferOffset) {
                var3 = (long)this.readBufferLength + this.readBufferOffset;
            }

            if (var1 > -1L && var3 > var1) {
                int var5 = (int)(var3 - var1);
                System.arraycopy(this.writeBuffer, (int)(var1 - this.writeBufferOffset), this.readBuffer, (int)(var1 - this.readBufferOffset), var5);
            }

            this.writeBufferOffset = -1L;
            this.writeBufferLength = 0;
        }

    }
}
