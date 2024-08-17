package com.client;

import com.google.common.primitives.Bytes;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class PacketReader {

    private static String packet = "[47,64,98,108,97,64,91,64,98,108,117,64,72,101,108,112,64,98,108,97,64,93,32,60,105,109,103,61,57,62,32,115,111,104,97,110,115,58,32,64,100,114, 10]";


    private static Buffer stream;

    private static Buffer read() {
        ArrayList<Byte> buffer = new Gson().fromJson(packet, new ArrayList<>().getClass());
        byte[] array = Bytes.toArray(buffer);
        return new Buffer(array);
    }

    public static void main(String... args) throws IOException {
        stream = read();
        sendMessage();
    }

    private static void sendMessage() { // 253
        System.out.println(read().readString());
    }

    private static void sendGroundItem() { // 156
        int j1 = stream.method426();
        int i4 = (j1 >> 4 & 7);
        int l6 = (j1 & 7);
        int k9 = stream.readUShort();
        System.out.println("x: " + i4 + ", y: " + l6 + ", item: " + k9);
    }

    private static void sendCoords() { // 85
        System.out.println("x: " + stream.readNegUByte() + ", y: " + stream.readNegUByte());

    }

}
