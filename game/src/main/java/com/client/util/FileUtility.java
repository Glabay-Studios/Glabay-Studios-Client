package com.client.util;

import com.client.sign.Signlink;

import java.io.File;

/**
 * @author Arthur Behesnilian 8:14 PM
 */
public class FileUtility {

    public static int getFileCount(String dir) {
        File file = new File(dir);
        return file.list().length;
    }

}
