package com.client.audio;

import net.runelite.rs.api.RSDevicePcmPlayerProvider;

public class DevicePcmPlayerProvider implements PcmPlayerProvider, RSDevicePcmPlayerProvider {
	public DevicePcmPlayerProvider() {
	} // L: 7
	
	public PcmPlayer player() {
		return new DevicePcmPlayer(); // L: 11
	}
}
