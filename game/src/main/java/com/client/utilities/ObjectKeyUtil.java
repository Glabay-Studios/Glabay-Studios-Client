package com.client.utilities;

public class ObjectKeyUtil {



	public static int getObjectType1(long id) {
		return (int) id >> 14 & 0x1f;
	}

	public static int getObjectOrientation(long id) {
		return (int) id >> 20 & 0x3;
	}

	public static int getObjectType(long id) {
		return (int) id >> 14 & 0x1f;
	}

	public static int getObjectOrientation(int id) {
		return id >> 6 & 3;
	}

	public static int getObjectType(int id) {
		return id & 31;
	}

	public static int getObjectX(long var0) {
		return (int)(var0 >>> 0 & 127L);
	}

	public static int getObjectY(long var0) {
		return (int)(var0 >>> 7 & 127L);
	}

	public static int getObjectOpcode(long var0) {
		return (int)(var0 >>> 14 & 3L);
	}

	public static int getObjectId(long id) {
		return (int)(id >>> 17 & 4294967295L);
	}

	public static long calculateTag(int var0, int var1, int var2, boolean var3, int var4) {
		long var5 = (long)((var0 & 127) << 0 | (var1 & 127) << 7 | (var2 & 3) << 14) | ((long)var4 & 4294967295L) << 17;
		if (var3) {
			var5 |= 65536L;
		}

		return var5;
	}

	public static boolean isInteractable(long var0) {
		boolean var2 = 0L != var0;
		if (var2) {
			boolean var3 = (int)(var0 >>> 16 & 1L) == 1;
			var2 = !var3;
		}

		return var2;
	}

}