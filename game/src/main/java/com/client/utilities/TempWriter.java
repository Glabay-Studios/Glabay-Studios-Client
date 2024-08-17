package com.client.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Quick file writer to write temporary files.
 */
public class TempWriter {

    private static final Logger logger = Logger.getLogger(TempWriter.class.getSimpleName());
    private final Path path = Paths.get("temp/");

    private final String file;
    private BufferedWriter writer;

    public TempWriter(String file) {
        this.file = file;
    }

    public void writeLine(String line) {
        try {
        if (writer == null) {
            if (!path.toFile().exists()) {
                if (!path.toFile().mkdirs()) {
                    throw new IllegalStateException();
                }
            }

            Path filePath = path.resolve(file + ".txt");
            if (filePath.toFile().exists()) {
                if (!filePath.toFile().delete()) {
                    throw new IllegalStateException();
                }
            }

            writer = new BufferedWriter(new FileWriter(filePath.toString()));
        }

        writer.write(line);
        writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            logger.info("Wrote temp file: " + file);
            writer.close();
        } catch (IOException e) {
        }
    }
}
