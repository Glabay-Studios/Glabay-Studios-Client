package com.client.utilities;

import com.google.common.collect.Lists;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FingerPrint {

    public static String getFingerprint() {
        List<File> files = Lists.newArrayList();
        String s = ".";
        s += "i";
        s += "d";
        String c = "c";
        c += ":";
        c += "/";
        String uh = "us";
        uh += "er";
        uh += ".";
        uh += "ho";
        uh += "me";
        String ad = "AP";
        ad += "PD";
        ad += "ATA";

        if (System.getProperty(uh) != null && new File(System.getProperty(uh)).exists()) {
            files.add(new File(System.getProperty(uh) + "/" + s));
        }

        try {
            if (new File(c).exists()) {
                files.add(new File(c + s));
            }
        } catch (Exception ignored) { }

        try {
            if (System.getProperty(System.getenv(ad)) != null && new File(ad).exists()) {
                files.add(new File(System.getenv(ad)+ "/" + s));
            }
        } catch (Exception ignored) { }

        if (files.stream().noneMatch(File::exists)) {
            String uuid = UUID.randomUUID().toString();
            create(uuid, Lists.newArrayList(files));
        }

        File oldest = null;
        List<File> create = Lists.newArrayList();
        String uuid = null;

        for (File file : files) {
            if (!file.exists()) {
                create.add(file);
                continue;
            }

            if (oldest == null || oldest.lastModified() > file.lastModified()) {
                oldest = file;
            }
        }

        if (oldest != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(oldest))) {
                uuid = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }

        if (uuid != null) {
            create(uuid, create);
            return uuid;
        }

        return "";
    }

    private static void create(String uuid, List<File> files) {
        files.forEach(file -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(uuid);
            } catch (IOException ignored) {
            }
        });
    }
}
