package com.client.sign;

import net.runelite.rs.api.RSFileSystem;

import java.io.File;
import java.util.Hashtable;

public class FileSystem implements RSFileSystem {

    public static boolean hasPermissions = false;

    public static File cacheDir;

    public static Hashtable cacheFiles = new Hashtable(16);

    public static long currentTimeMills;



}
