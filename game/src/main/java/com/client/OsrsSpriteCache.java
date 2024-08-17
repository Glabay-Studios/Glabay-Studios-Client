package com.client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import static java.nio.file.StandardOpenOption.READ;

public final class OsrsSpriteCache implements Closeable {

	private Sprite[] cache;

	public Sprite[] getSprites() {
		return cache;
	}

	private byte[] dataBytes;
	private byte[] metaBytes;

	public void init(byte[] dataBytes, byte[] metaBytes) throws IOException {
		if (dataBytes == null || metaBytes == null) {
			throw new IllegalArgumentException("Data and meta bytes must not be null.");
		}

		this.dataBytes = dataBytes;
		this.metaBytes = metaBytes;

		final int spriteCount = metaBytes.length / 10;
		cache = new Sprite[spriteCount];
	}

	public Sprite lookup(int id) {
		try {
			if (contains(id)) {
				return cache[id];
			}

			if (dataBytes == null || metaBytes == null) {
				System.err.println("Sprite data or meta bytes are null!");
				return null;
			}

			final int entries = metaBytes.length / 10;

			if (id > entries) {
				//System.err.printf("id=%d > size=%d%n", id, entries);
				return null;
			}

			int metaPos = id * 10;
			int pos = ((metaBytes[metaPos++] & 0xFF) << 16) | ((metaBytes[metaPos++] & 0xFF) << 8) | (metaBytes[metaPos++] & 0xFF);
			int len = ((metaBytes[metaPos++] & 0xFF) << 16) | ((metaBytes[metaPos++] & 0xFF) << 8) | (metaBytes[metaPos++] & 0xFF);
			int offsetX = metaBytes[metaPos++] & 0xFF;
			int offsetY = metaBytes[metaPos] & 0xFF;

			byte[] dataBuf = Arrays.copyOfRange(dataBytes, pos, pos + len);

			try (InputStream is = new ByteArrayInputStream(dataBuf)) {
				BufferedImage bimage = ImageIO.read(is);

				if (bimage == null) {
					System.err.printf("Could not read image at %d%n", id);
					return null;
				}

				if (bimage.getType() != BufferedImage.TYPE_INT_ARGB) {
					bimage = convert(bimage);
				}

				final int[] pixels = ((DataBufferInt) bimage.getRaster().getDataBuffer()).getData();

				final Sprite sprite = new Sprite(bimage.getWidth(), bimage.getHeight(), offsetX, offsetY, pixels);

				// cache so we don't have to perform I/O calls again
				cache[id] = sprite;

				return sprite;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.err.printf("No sprite found for id=%d%n", id);
		return null;
	}

	public boolean contains(int id) {
		return id < cache.length && cache[id] != null;
	}

	public void set(int id, Sprite sprite) {
		if (!contains(id)) {
			return;
		}

		cache[id] = sprite;
	}

	private static BufferedImage convert(BufferedImage bimage) {
		BufferedImage converted = new BufferedImage(bimage.getWidth(), bimage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		converted.getGraphics().drawImage(bimage, 0, 0, null);
		return converted;
	}

	public void close() throws IOException {

	}

}
