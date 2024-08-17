package com.client.js5.net;

import com.client.collection.node.DualNode;
import com.client.js5.disk.Js5Archive;
import net.runelite.rs.api.RSNetFileRequest;

public class NetFileRequest extends DualNode implements RSNetFileRequest {
    public Js5Archive js5Archive;
    public int crc;
    public byte padding;
}
