package com.client.sign;

import com.client.Buffer;
import com.client.js5.disk.ArchiveDisk;
import com.client.sign.diskfile.AccessFile;
import com.client.sign.diskfile.BufferedFile;
import net.runelite.client.RuneLite;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Signlink {

	public static BufferedFile uid = null;

	public static BufferedFile cacheData = null;

	public static BufferedFile cacheMasterIndex = null;

	public static BufferedFile[] cacheIndexes;

	public static String javaVersion;

	public static ArchiveDisk masterDisk;

	public static String javaVendor;
	public static String dns = null;
	public static String operatingSystemName;
	public static String formattedOperatingSystemName;
	public static String userHomeDirectory = "";
	public static String[] cacheParentPaths;
	public static String[] cacheSubPaths;
	public static File cacheDir;

	public int build;
	public static int archiveCount;

	public static void init(int size) throws Exception {
		archiveCount = size;
		try {
			operatingSystemName = System.getProperty("os.name");
		} catch (Exception var14) {
			operatingSystemName = "Unknown";
		}

		formattedOperatingSystemName = operatingSystemName.toLowerCase();

		try {
			userHomeDirectory = System.getProperty("user.home");
			if (userHomeDirectory != null) {
				userHomeDirectory = userHomeDirectory + "/";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		try {
			if (formattedOperatingSystemName.startsWith("win")) {
				if (userHomeDirectory == null) {
					userHomeDirectory = System.getenv("USERPROFILE");
				}
			} else if (userHomeDirectory == null) {
				userHomeDirectory = System.getenv("HOME");
			}

			if (userHomeDirectory != null) {
				userHomeDirectory = userHomeDirectory + "/";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		if (userHomeDirectory == null) {
			userHomeDirectory = "~/";
		}

		cacheParentPaths = new String[]{"c:/rscache/", "/rscache/", "c:/windows/", "c:/winnt/", "c:/", userHomeDirectory, "/tmp/", ""};
		cacheSubPaths = new String[]{"."  + "Xeros_cache_" + 209, ".file_store_" + 209};

		label135:
		for(int var8 = 0; var8 < 4; ++var8) {
			cacheDir = getCachedFile("LIVE", "", var8);
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}

			File[] cacheFiles = cacheDir.listFiles();
			if (cacheFiles == null) {
				break;
			}

			int fileIndex = 0;

			while(true) {
				if (fileIndex >= cacheFiles.length) {
					break label135;
				}

				File var7 = cacheFiles[fileIndex];
				if (!overwriteFileWithFirstByte(var7, false)) {
					break;
				}

				++fileIndex;
			}
		}

		hasPermissions(cacheDir);
		generateUID();

		cacheData = new BufferedFile(new AccessFile(getFile("main_file_cache.dat2"), "rw", 1048576000L), 5200, 0);
		cacheMasterIndex = new BufferedFile(new AccessFile(getFile("main_file_cache.idx255"), "rw", 1048576L), 6000, 0);
		cacheIndexes = new BufferedFile[archiveCount];

		for(int currentArchive = 0; currentArchive < archiveCount; ++currentArchive) {
			cacheIndexes[currentArchive] = new BufferedFile(new AccessFile(getFile("main_file_cache.idx" + currentArchive), "rw", 1048576L), 6000, 0);
		}

	}

	public static boolean overwriteFileWithFirstByte(File file, boolean deleteOriginal) {
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
			int firstByte = raf.read();
			raf.seek(0L);
			raf.write(firstByte);
		} catch (IOException e) {
			return false;
		}
		if (deleteOriginal) {
			return file.delete();
		}
		return true;
	}

	/**
	 * Returns the File object for the given file name, creating it if it doesn't exist.
	 *
	 * @param fileName the name of the file
	 * @return the File object for the given file name
	 * @throws RuntimeException if the file system permissions are not set or the file cannot be created
	 */
	public static File getFile(String fileName) {
		if (!FileSystem.hasPermissions) {
			throw new RuntimeException("File system permissions are not set");
		}

		File file = (File) FileSystem.cacheFiles.get(fileName);

		if (file != null) {
			return file;
		} else {
			File newFile = new File(FileSystem.cacheDir, fileName);

			try {
				File parentDir = newFile.getParentFile();

				if (!parentDir.exists()) {
					throw new RuntimeException("Parent directory does not exist");
				}

				RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
				int firstByte = raf.read();
				raf.seek(0L);
				raf.write(firstByte);
				raf.seek(0L);
				raf.close();

				FileSystem.cacheFiles.put(fileName, newFile);

				return newFile;
			} catch (IOException e) {
				throw new RuntimeException("Failed to create file: " + e.getMessage());
			}
		}
	}

	public static void generateUID() {
		try {
			File uidFile = null;
			for (String subPath : cacheSubPaths) {
				for (String parentPath : cacheParentPaths) {
					File uidFileTemp = new File(parentPath + subPath + File.separatorChar + "random.dat");
					if (uidFileTemp.exists()) {
						uidFile = uidFileTemp;
					}
				}
			}
			if (uidFile == null) {
				uidFile = createUIDFile();
			}
			uid = new BufferedFile(new AccessFile(uidFile, "rw", 25L), 24, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static File createUIDFile() throws IOException {
		File uidFile = new File(userHomeDirectory, "random.dat");
		RandomAccessFile raf = new RandomAccessFile(uidFile, "rw");
		int firstByte = raf.read();
		raf.seek(0L);
		raf.write(firstByte);
		raf.seek(0L);
		raf.close();
		return uidFile;
	}

	public static File getCachedFile(String cacheType, String cacheKey, int cacheVersion) {
		String versionSuffix = cacheVersion == 0 ? "" : String.valueOf(cacheVersion);
		File locationFile = new File(userHomeDirectory, "Xeros_cl_" + cacheType + "_" + cacheKey + versionSuffix + ".dat");
		String filePath = null;
		String backupFilePath = null;
		boolean isFileLocationFound = false;

		if (locationFile.exists()) {
			try {
				AccessFile file = new AccessFile(locationFile, "rw", 10000L);
				Buffer buffer = new Buffer((int) file.length());
				int read;
				while ((read = file.read(buffer.payload, buffer.currentPosition, buffer.payload.length - buffer.currentPosition)) != -1) {
					buffer.currentPosition += read;
					if (buffer.currentPosition == buffer.payload.length) {
						break;
					}
				}
				buffer.currentPosition = 0;
				int version = buffer.readUnsignedByte();

				if (version < 1 || version > 3) {
					throw new IOException("Unsupported version " + version);
				}

				int extraFile = 0;
				if (version > 1) {
					extraFile = buffer.readUnsignedByte();
				}

				if (version <= 2) {
					filePath = buffer.readString();
					if (extraFile == 1) {
						backupFilePath = buffer.readString();
					}
				} else {
					filePath = buffer.readCESU8();
					if (extraFile == 1) {
						backupFilePath = buffer.readCESU8();
					}
				}

				file.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					filePath = null;
				}
			}

			if (filePath != null) {
				File file = new File(filePath, "test.dat");
				if (!overwriteFileWithFirstByte(file, true)) {
					filePath = null;
				}
			}
		}

		if (filePath == null && cacheVersion == 0) {
			for (String subPath : cacheSubPaths) {
				for (String parentPath : cacheParentPaths) {
					File file = new File(parentPath + subPath + File.separatorChar + cacheType + File.separatorChar);
					if (file.exists() && overwriteFileWithFirstByte(new File(file, "test.dat"), true)) {
						filePath = file.toString();
						isFileLocationFound = true;
						break;
					}
				}
			}
		}

		if (filePath == null) {
			filePath = RuneLite.CACHE_DIR + versionSuffix + File.separatorChar + cacheType + File.separatorChar + cacheKey + File.separatorChar;
			isFileLocationFound = true;
		}

		if (backupFilePath != null) {
			File backupFile = new File(backupFilePath);
			File backupLocation = new File(filePath);

			try {
				File[] files = backupFile.listFiles();
				if (files != null) {
					for (File srcFile : files) {
						File destFile = new File(backupLocation, srcFile.getName());
						boolean isRenamed = srcFile.renameTo(destFile);
						if (!isRenamed) {
							throw new IOException("Failed to rename file from " + srcFile.getPath() + " to " + destFile.getPath());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			isFileLocationFound = true;
		}

		if (isFileLocationFound) {
			writeToFile(locationFile,new File(filePath), (File)null);
		}

		return new File(filePath);
	}

	public static void hasPermissions(File cacheDir) {
		FileSystem.cacheDir = cacheDir;
		if (!cacheDir.exists()) {
			throw new RuntimeException("Cache directory does not have Permission : " + cacheDir.getAbsolutePath());
		}
		FileSystem.hasPermissions = true;
	}


	public static void writeToFile(File locationFile,File file1, File file2) {
		try {
			AccessFile accessFile = new AccessFile(locationFile, "rw", 10000L);
			Buffer buffer = new Buffer(500);
			buffer.writeByte(3);
			buffer.writeByte(file2 != null ? 1 : 0);
			buffer.writeCESU8(file1.getPath());
			if (file2 != null) {
				buffer.writeCESU8("");
			}
			accessFile.write(buffer.payload, 0, buffer.currentPosition);
			accessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final String getCacheDirectory() {
		return RuneLite.CACHE_DIR.getAbsolutePath() + "/";
	}

	public static synchronized void dnslookup(String s) {
		dns = s;
	}

	public static void reporterror(String s) {
		System.out.println(s);
	}

	public static void setError(String error) {
		errorName = error;
	}


	public static boolean reporterror = true;
	public static String errorName = "";

	private Signlink() {
	}

}
