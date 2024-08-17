package com.client;

import com.client.utilities.JsonUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PacketLog {

    private static Queue<LoggedPacket> packets = new ArrayDeque<>();

    public static void add(int opcode, int length, byte[] inStreamBuffer) {
        byte[] buffer = new byte[length];
        System.arraycopy(inStreamBuffer, 0, buffer, 0, length);
        packets.add(new LoggedPacket(opcode, length, buffer));
        while (packets.size() > 10)
            packets.poll();
    }

    public static void log() {
        try {
            List<LoggedPacket> loggedPackets = new ArrayList<>(packets);
            System.err.println(JsonUtil.toJsonString(loggedPackets));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear() {
        packets.clear();
    }

    private static class LoggedPacket {
        private final int opcode;
        private final int length;
        private final byte[] buffer;

        public LoggedPacket(int opcode, int length, byte[] buffer) {
            this.opcode = opcode;
            this.length = length;
            this.buffer = buffer;
        }

        @Override
        public String toString() {
            return "LoggedPacket{" +
                    "opcode=" + opcode +
                    ", length=" + length +
                    ", buffer=" + Arrays.toString(buffer) +
                    '}';
        }
    }
}
