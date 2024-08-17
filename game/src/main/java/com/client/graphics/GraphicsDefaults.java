package com.client.graphics;

import com.client.Buffer;
import com.client.js5.disk.AbstractArchive;

public class GraphicsDefaults {

    public int compass = -1;

    public int field4588 = -1;

    public int mapScenes = -1;

    public int headIconsPk = -1;

    public int headIconArchive = -1;

    public int field4587 = -1;

    public int field4591 = -1;

    public int field4589 = -1;

    public int field4590 = -1;

    public int field4586 = -1;

    public int field4592 = -1;


    public void decode(AbstractArchive archive) {
        byte[] data = archive.takeFileFlat(DefaultsGroup.groups.group);
        Buffer buffer = new Buffer(data);

        while(true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }

            switch (opcode) {
                case 1:
                    buffer.readMedium();
                    break;
                case 2:
                    this.compass = buffer.readNullableLargeSmart();
                    this.field4588 = buffer.readNullableLargeSmart();
                    this.mapScenes = buffer.readNullableLargeSmart();
                    this.headIconsPk = buffer.readNullableLargeSmart();
                    this.headIconArchive = buffer.readNullableLargeSmart();
                    this.field4587 = buffer.readNullableLargeSmart();
                    this.field4591 = buffer.readNullableLargeSmart();
                    this.field4589 = buffer.readNullableLargeSmart();
                    this.field4590 = buffer.readNullableLargeSmart();
                    this.field4586 = buffer.readNullableLargeSmart();
                    this.field4592 = buffer.readNullableLargeSmart();
            }
        }
    }
}
