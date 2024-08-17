package com.client.draw.font.custom;

import com.client.Client;
import com.util.AssetUtils;

import java.awt.*;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FontArchiver {

    private static CustomFont[] fontArchive;


    public static CustomFont getFont(int index) {
        if (fontArchive == null) {
            return null;
        }
        return fontArchive[index];
    }


    public static boolean isNull(int index) {
        if (fontArchive[index] == null)
            return false;
        return true;

    }

    public static void loadArchive() throws FileNotFoundException, IOException {
        try (DataInputStream in = new DataInputStream(new GZIPInputStream(AssetUtils.INSTANCE.getResource("main_file_fonts.dat").openStream()))) {
            byte fontCount = in.readByte();
            fontArchive = new CustomFont[fontCount + 1];
            for (int index = 0; index < fontCount; index++) {
                long length = in.readLong();
                byte[] fontData = new byte[(int) length];
                in.readFully(fontData);
                fontArchive[index] = new CustomFont(fontData);
            }
            fontArchive[0] = new CustomFont(Client.instance, "RuneScape UF", Font.PLAIN, 40, true);
            fontArchive[1] = new CustomFont(Client.instance, "RuneScape UF", Font.PLAIN, 16, true);
        }
    }




    public static void writeFontArchive() {
        try {
            final CustomFont[] FONTS = new CustomFont[] {
                    new CustomFont(Client.instance, "RuneScape UF", Font.PLAIN, 40, true),
                    new CustomFont(Client.instance, "RuneScape UF", Font.PLAIN, 16, true)
            };
            DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream("./main_file_fonts.dat")));
            try {
                out.writeByte(FONTS.length);
                for (int index = 0; index < FONTS.length; index++) {
                    out.writeLong(FONTS[index].toByteArray().length);
                    out.write(FONTS[index].toByteArray());
                }
            } finally {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
