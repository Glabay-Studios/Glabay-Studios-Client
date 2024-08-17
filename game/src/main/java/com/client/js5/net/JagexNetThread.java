package com.client.js5.net;

import com.client.Buffer;
import com.client.collection.node.DualNodeDeque;
import com.client.collection.table.IterableNodeHashTable;
import com.client.js5.disk.Js5Archive;
import com.client.net.AbstractSocket;
import com.client.util.MonotonicClock;
import net.runelite.rs.api.RSNetCache;

import java.io.IOException;
import java.util.zip.CRC32;

public class JagexNetThread implements RSNetCache {

    AbstractSocket socket;

    int field3586;

    long field3594;

    IterableNodeHashTable field3602;

    int field3588;

    IterableNodeHashTable field3600;

    int field3590;

    public DualNodeDeque field3597;

    public IterableNodeHashTable field3601;

    int field3591;

    IterableNodeHashTable field3599;

    int field3585;

    boolean field3583;

    public NetFileRequest field3596;
    Buffer field3605;
    public Buffer responsiveArchiveBuffer;

    int field3587;

    public CRC32 field3593;
    Buffer field3603;
    Js5Archive[] field3595;
    int field3589;
    int field3592;
    byte field3584;
    public int crcMissmatches;
    public int ioExceptions;

    public JagexNetThread() {
        this.field3586 = 0;
        this.field3602 = new IterableNodeHashTable(4096);
        this.field3588 = 0;
        this.field3600 = new IterableNodeHashTable(32);
        this.field3590 = 0;
        this.field3597 = new DualNodeDeque();
        this.field3601 = new IterableNodeHashTable(4096);
        this.field3591 = 0;
        this.field3599 = new IterableNodeHashTable(4096);
        this.field3585 = 0;
        this.field3605 = new Buffer(8);
        this.field3587 = 0;
        this.field3593 = new CRC32();
        this.field3595 = new Js5Archive[256];
        this.field3589 = -1;
        this.field3592 = 255;
        this.field3584 = 0;
        this.crcMissmatches = 0;
        this.ioExceptions = 0;
    }

    public boolean processNetwork() {
        long field3596Time = MonotonicClock.currentTimeMillis();
        int timeElapsedSinceLastLoop = (int) (field3596Time - field3594);
        field3594 = field3596Time;

        if (timeElapsedSinceLastLoop > 200) {
            timeElapsedSinceLastLoop = 200;
        }

        field3586 += timeElapsedSinceLastLoop;

        if (field3585 == 0 && field3590 == 0 && field3591 == 0 && field3588 == 0) {
            return true;
        } else if (socket == null) {
            return false;
        } else {
            try {
                if (field3586 > 30000) {
                    throw new IOException();
                } else {
                    NetFileRequest request;
                    Buffer buffer;
                    while (field3590 < 200 && field3588 > 0) {
                        request = (NetFileRequest) field3602.first();
                        buffer = new Buffer(4);
                        buffer.writeByte(1);
                        buffer.writeMedium((int) request.key);
                        socket.write(buffer.payload, 0, 4);
                        field3600.put(request, request.key);
                        --field3588;
                        ++field3590;
                    }
                    while (field3585 < 200 && field3591 > 0) {
                        request = (NetFileRequest) field3597.removeLast();
                        buffer = new Buffer(4);
                        buffer.writeByte(0);
                        buffer.writeMedium((int) request.key);
                        socket.write(buffer.payload, 0, 4);
                        request.removeDual();
                        field3599.put(request, request.key);
                        --field3591;
                        ++field3585;
                    }

                    for (int loopCount = 0; loopCount < 100; ++loopCount) {
                        int available = socket.available();
                        if (available < 0) {
                            throw new IOException();
                        }

                        if (available == 0) {
                            break;
                        }

                        field3586 = 0;
                        byte controlPacketType = 0;
                        if (field3596 == null) {
                            controlPacketType = 8;
                        } else if (field3587 == 0) {
                            controlPacketType = 1;
                        }

                        int bytesRead;
                        int maxBytesToRead;
                        int data;
                        byte[] payload;
                        int payloadIndex;
                        Buffer responseBuffer;
                        if (controlPacketType > 0) {
                            bytesRead = controlPacketType - field3605.currentPosition;
                            if (bytesRead > available) {
                                bytesRead = available;
                            }

                            socket.read(field3605.payload, field3605.currentPosition, bytesRead);
                            if (field3584 != 0) {
                                for (maxBytesToRead = 0; maxBytesToRead < bytesRead; ++maxBytesToRead) {
                                    payload = field3605.payload;
                                    payloadIndex = maxBytesToRead + field3605.currentPosition;
                                    payload[payloadIndex] ^= field3584;
                                }
                            }

                            responseBuffer = field3605;
                            responseBuffer.currentPosition += bytesRead;

                            if (field3605.currentPosition < controlPacketType) {
                                break;
                            }

                            if (field3596 == null) {
                                field3605.currentPosition = 0;
                                maxBytesToRead = field3605.readUnsignedByte();
                                data = field3605.readUShort();
                                int controlPacketSubType = field3605.readUnsignedByte();
                                int fileLength = field3605.readInt();
                                long key = (long) (data + (maxBytesToRead << 16));
                                NetFileRequest netFileRequest = (NetFileRequest) field3600.get(key);
                                field3583 = true;

                                if (netFileRequest == null) {
                                    netFileRequest = (NetFileRequest) field3599.get(key);
                                    field3583 = false;
                                }

                                if (netFileRequest == null) {
                                    throw new IOException();
                                }

                                int additionalHeaderSize = (controlPacketSubType == 0) ? 5 : 9;
                                field3596 = netFileRequest;
                                responsiveArchiveBuffer = new Buffer(field3596.padding + additionalHeaderSize + fileLength);
                                responsiveArchiveBuffer.writeByte(controlPacketSubType);
                                responsiveArchiveBuffer.writeInt(fileLength);
                                field3587 = 8;
                                field3605.currentPosition = 0;
                            } else if (field3587 == 0) {
                                if (field3605.payload[0] == -1) {
                                    field3587 = 1;
                                    field3605.currentPosition = 0;
                                } else {
                                    field3596 = null;
                                }
                            }
                        } else {
                            bytesRead = responsiveArchiveBuffer.payload.length - field3596.padding;
                            maxBytesToRead = 512 - field3587;
                            if (maxBytesToRead > bytesRead - responsiveArchiveBuffer.currentPosition) {
                                maxBytesToRead = bytesRead - responsiveArchiveBuffer.currentPosition;
                            }

                            if (maxBytesToRead > available) {
                                maxBytesToRead = available;
                            }

                            socket.read(responsiveArchiveBuffer.payload, responsiveArchiveBuffer.currentPosition, maxBytesToRead);

                            if (field3584 != 0) {
                                for (data = 0; data < maxBytesToRead; ++data) {
                                    payload = responsiveArchiveBuffer.payload;
                                    payloadIndex = data + responsiveArchiveBuffer.currentPosition;
                                    payload[payloadIndex] ^= field3584;
                                }
                            }

                            responseBuffer = responsiveArchiveBuffer;
                            responseBuffer.currentPosition += maxBytesToRead;
                            field3587 += maxBytesToRead;

                            if (responsiveArchiveBuffer.currentPosition == bytesRead) {
                                if (16711935L == field3596.key) {
                                    field3603 = responsiveArchiveBuffer;

                                    for (data = 0; data < 256; ++data) {
                                        Js5Archive archive = field3595[data];
                                        if (archive != null) {
                                            loadArchiveIndex(archive, data);
                                        }
                                    }
                                } else {
                                    field3593.reset();
                                    field3593.update(responsiveArchiveBuffer.payload, 0, bytesRead);
                                    data = (int) field3593.getValue();

                                    if (field3596.crc != data) {
                                        try {
                                            socket.close();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        ++crcMissmatches;
                                        socket = null;
                                        field3584 = (byte) ((int) (Math.random() * 255.0D + 1.0D));
                                        return false;
                                    }

                                    crcMissmatches = 0;
                                    ioExceptions = 0;
                                    field3596.js5Archive.write((int) (field3596.key & 65535L), responsiveArchiveBuffer.payload, (field3596.key & 16711680L) == 16711680L, field3583);
                                }

                                field3596.unlink();

                                if (field3583) {
                                    --field3590;
                                } else {
                                    --field3585;
                                }

                                field3587 = 0;
                                field3596 = null;
                                responsiveArchiveBuffer = null;
                            } else {
                                if (field3587 != 512) {
                                    break;
                                }

                                field3587 = 0;
                            }
                        }
                    }
                    return true;
                }
            } catch (IOException ex) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ++ioExceptions;
                socket = null;
                return false;
            }
        }
    }


    public  void writePacket(boolean var1) {
        if (socket != null) {
            try {
                Buffer var2 = new Buffer(4);
                var2.writeByte(var1 ? 2 : 3);
                var2.writeMedium(0);
                socket.write(var2.payload, 0, 4);
            } catch (IOException var5) {
                try {
                    socket.close();
                } catch (Exception var4) {
                    var4.printStackTrace();
                }

                ++ioExceptions;
                socket = null;
            }
        }
    }

    public  void initializeAndWriteDataToSocket(AbstractSocket var1, boolean var2) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            socket = null;
        }

        socket = var1;
        writePacket(var2);
        field3605.currentPosition = 0;
        field3596 = null;
        responsiveArchiveBuffer = null;
        field3587 = 0;

        while (true) {
            NetFileRequest var3 = (NetFileRequest) field3600.first();
            if (var3 == null) {
                while (true) {
                    var3 = (NetFileRequest) field3599.first();
                    if (var3 == null) {
                        if (field3584 != 0) {
                            try {
                                Buffer buffer = new Buffer(4);
                                buffer.writeByte(4);
                                buffer.writeByte(field3584);
                                buffer.writeShort(0);
                                socket.write(buffer.payload, 0, 4);
                            } catch (IOException var6) {
                                try {
                                    socket.close();
                                } catch (Exception var5) {

                                }
                                ++ioExceptions;
                                socket = null;
                            }
                        }

                        field3586 = 0;
                        field3594 = MonotonicClock.currentTimeMillis();
                        return;
                    }

                    field3597.addLast(var3);
                    field3601.put(var3, var3.key);
                    ++field3591;
                    --field3585;
                }
            }

            field3602.put(var3, var3.key);
            ++field3588;
            --field3590;
        }
    }

    public void updateArchiveIndex(Js5Archive var1, int var2) {
        if (var1.prioritizeRequests) {
            if (var2 <= this.field3589) {
                throw new RuntimeException("");
            }

            if (var2 < this.field3592) {
                this.field3592 = var2;
            }
        } else {
            if (var2 >= this.field3592) {
                throw new RuntimeException("");
            }

            if (var2 > this.field3589) {
                this.field3589 = var2;
            }
        }

        if (this.field3603 != null) {
            this.loadArchiveIndex(var1, var2);
        } else {
            this.requestNetFile(null, 255, 255, 0, (byte)0, true);
            this.field3595[var2] = var1;
        }
    }

    public  int method1968(boolean var1, boolean var2) {
        byte var3 = 0;
        int var4 = var3 + field3588 + field3590;
        return var4;
    }

    public void requestNetFile(Js5Archive js5Archive, int group, int file, int crc, byte padding, boolean prioritize) {
        long var6 = (long)(file + (group << 16));
        NetFileRequest request = (NetFileRequest)field3602.get(var6);
        if (request == null) {
            request = (NetFileRequest)field3600.get(var6);
            if (request == null) {
                request = (NetFileRequest) field3601.get(var6);
                if (request != null) {
                    if (prioritize) {
                        request.removeDual();
                        field3602.put(request, var6);
                        --field3591;
                        ++field3588;
                    }

                } else {
                    if (!prioritize) {
                        request = (NetFileRequest) field3599.get(var6);
                        if (request != null) {
                            return;
                        }
                    }

                    request = new NetFileRequest();
                    request.js5Archive = js5Archive;
                    request.crc = crc;
                    request.padding = padding;
                    if (prioritize) {
                       field3602.put(request, var6);
                        ++field3588;
                    } else {
                        field3597.addFirst(request);
                        field3601.put(request, var6);
                        ++field3591;
                    }
                }
            }
        }
    }

    public void loadArchiveIndex(Js5Archive archive, int archiveIndex) {
        field3603.currentPosition = archiveIndex * 8 + 5;

        if (field3603.currentPosition >= field3603.payload.length) {
            if (archive.prioritizeRequests) {
                archive.reset();
            } else {
                throw new RuntimeException("Index out of bounds");
            }
        } else {
            int containerId = field3603.readInt();
            int crc = field3603.readInt();
            archive.loadIndex(containerId, crc);
        }
    }

    public  void closeSocketStream() {
        if (socket != null) {
            socket.close();
        }
    }
}
