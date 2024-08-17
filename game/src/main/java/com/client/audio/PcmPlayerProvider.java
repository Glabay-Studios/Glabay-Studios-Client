package com.client.audio;

import net.runelite.rs.api.RSDevicePcmPlayerProvider;

public interface PcmPlayerProvider extends RSDevicePcmPlayerProvider {
	
	PcmPlayer player();
}
