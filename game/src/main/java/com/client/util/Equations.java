package com.client.util;

public class Equations {

	public static int numberOfBits(int var0) {
		var0 = (var0 & 1431655765) + (var0 >>> 1 & 1431655765); // L: 28
		var0 = (var0 >>> 2 & 858993459) + (var0 & 858993459); // L: 29
		var0 = var0 + (var0 >>> 4) & 252645135; // L: 30
		var0 += var0 >>> 8; // L: 31
		var0 += var0 >>> 16; // L: 32
		return var0 & 255; // L: 33
	}

	public static int farthestBitValue(int var0) {//or nextPowerOfTwo
		--var0; // L: 50
		var0 |= var0 >>> 1; // L: 51
		var0 |= var0 >>> 2; // L: 52
		var0 |= var0 >>> 4; // L: 53
		var0 |= var0 >>> 8; // L: 54
		var0 |= var0 >>> 16; // L: 55
		return var0 + 1; // L: 56
	}

	public static final synchronized long getCurrentTime() {
		long current = System.currentTimeMillis(); // L: 14
		if (current < Equations.lastPoll) { // L: 15
			Equations.error += Equations.lastPoll - current; // L: 16
		}
		Equations.lastPoll = current; // L: 18
		return Equations.error + current; // L: 19
	}

	private static long error;
	private static long lastPoll;

}
