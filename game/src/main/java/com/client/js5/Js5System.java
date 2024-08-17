package com.client.js5;

import com.client.Buffer;
import com.client.Client;
import com.client.Configuration;
import com.client.engine.GameEngine;
import com.client.engine.task.Task;
import com.client.engine.task.TaskHandler;
import com.client.js5.disk.ArchiveDisk;
import com.client.js5.disk.Js5Archive;
import com.client.js5.util.ArchiveLoader;
import com.client.net.AbstractSocket;
import com.client.net.tcp.buffer.BufferedNetSocket;
import com.client.sign.FileSystem;
import com.client.sign.Signlink;
import com.client.util.MonotonicClock;

import java.io.IOException;
import java.net.Socket;

import static com.client.Configuration.DEDICATED_JS5_SERVER_ADDRESS;
import static com.client.Configuration.SERVICES_PORT;

public class Js5System {
    public static int groupSize;
    private static int js5Errors;
    private static int js5ConnectState;
    private static int js5Cycles;
    static Task js5SocketTask;
    private static AbstractSocket js5Socket;
    TaskHandler js5Task = new TaskHandler();

    static {
        groupSize = 0;
        js5Errors = 0;
        js5ConnectState = 0;
        js5Cycles = 0;
    }

    public static void doCycleJs5() {
        if (Client.instance.gameState != 1000) {
            boolean idle = Client.jagexNetThread.processNetwork();
            if (!idle) {
                connect();
            }
        }
    }

    public static Js5Archive createJs5(Js5ArchiveIndex archive, boolean useWhirlpool, boolean useGzip, boolean verifyChecksums, boolean prioritizeRequests) {
        ArchiveDisk cache = null;
        if (Signlink.cacheData != null) {
            cache = new ArchiveDisk(archive.getId(), Signlink.cacheData, Signlink.cacheIndexes[archive.getId()], 1000000);
        }

        return new Js5Archive(cache, Signlink.masterDisk, archive, useWhirlpool, useGzip, verifyChecksums, prioritizeRequests);
    }

    public static void init(Js5Archive js5Archive, String name) {
        ArchiveLoader archiveLoader = new ArchiveLoader(js5Archive, name);
        ArchiveLoader.archiveLoaders.add(archiveLoader);
        groupSize += archiveLoader.groupCount;
    }

    public static void connect() {
        if (Client.jagexNetThread.crcMissmatches >= 4) {
            Client.instance.error("js5crc");
            Client.instance.gameState = 1000;
            System.out.println("CRC mismatches exceeded 4, setting game state to 1000");
        } else {
            if (Client.jagexNetThread.ioExceptions >= 4) {
                if (Client.instance.gameState <= 5) {
                    Client.instance.error("js5io");
                    Client.instance.gameState = 1000;
                    System.out.println("IO exceptions exceeded 4 and game state <= 5, setting game state to 1000");
                    return;
                }

                js5Cycles = 3000;
                Client.jagexNetThread.ioExceptions = 3;
            }

            if (--js5Cycles + 1 <= 0) {
                try {
                    if (js5ConnectState == 0) {
                        js5SocketTask = GameEngine.taskHandler.newSocketTask(DEDICATED_JS5_SERVER_ADDRESS, SERVICES_PORT);
                        ++js5ConnectState;
                        System.out.println("js5ConnectState set to 1, creating socket task");
                    }

                    if (js5ConnectState == 1) {
                        if (js5SocketTask.status == 2) {
                            js5Error(-1);
                            System.out.println("Socket task status is 2, reporting an error");
                            return;
                        }

                        if (js5SocketTask.status == 1) {
                            ++js5ConnectState;
                            System.out.println("Socket task status is 1, advancing to js5ConnectState 2");
                        }
                    }

                    int VERSION = 217;
                    if (js5ConnectState == 2) {
                        js5Socket = new BufferedNetSocket((Socket) js5SocketTask.result, 40000, 5000);
                        Buffer buffer = new Buffer(5);
                        buffer.writeByte(15);
                        buffer.writeInt(VERSION);
                        js5Socket.write(buffer.payload, 0, 5);
                        ++js5ConnectState;
                        FileSystem.currentTimeMills = MonotonicClock.currentTimeMillis();
                        System.out.println("js5ConnectState set to 2, creating BufferedNetSocket and sending data");
                    }

                    if (js5ConnectState == 3) {
                        if (js5Socket.available() > 0) {
                            int socket = js5Socket.readUnsignedByte();
                            if (socket != 0) {
                                js5Error(socket);
                                System.out.println("Received non-zero socket value, reporting an error");
                                return;
                            }

                            ++js5ConnectState;
                            System.out.println("js5ConnectState advanced to 3");
                        } else if (MonotonicClock.currentTimeMillis() - FileSystem.currentTimeMills > 30000L) {
                            js5Error(-2);
                            System.out.println("Socket not available, and time exceeded 30000ms, reporting an error");
                            return;
                        }
                    }

                    if (js5ConnectState == 4) {
                        Client.jagexNetThread.initializeAndWriteDataToSocket(js5Socket, Client.instance.gameState > 20);
                        js5SocketTask = null;
                        js5Socket = null;
                        js5ConnectState = 0;
                        js5Errors = 0;
                        System.out.println("js5ConnectState set to 4, performing final initialization");
                    }
                } catch (IOException exception) {
                    js5Error(-3);
                    System.out.println("IOException caught, reporting an error");
                }
            }
        }
    }


    public static void js5Error(int response) {
        js5SocketTask = null;
        js5Socket = null;
        js5ConnectState = 0;

        ++js5Errors;
        if (js5Errors >= 2 && (response == 7 || response == 9)) {
            if (Client.instance.gameState <= 5) {
                Client.instance.error("js5connect_full");
                Client.instance.setRSGameState(1000);
            } else {
                js5Cycles = 3000;
            }
        } else if (js5Errors >= 2 && response == 6) {
            Client.instance.error("js5connect_outofdate");
            Client.instance.setRSGameState(1000);
        } else if (js5Errors >= 4) {
            if (Client.instance.gameState <= 5) {
                Client.instance.error("js5connect");
                Client.instance.setRSGameState(1000);
            } else {
                js5Cycles = 3000;
            }
        }
    }

}
