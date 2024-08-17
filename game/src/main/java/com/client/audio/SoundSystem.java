package com.client.audio;

import net.runelite.rs.api.RSSoundSystem;

public class SoundSystem implements Runnable, RSSoundSystem {
	public volatile PcmPlayer[] players;
	public SoundSystem() {
		this.players = new PcmPlayer[2]; // L: 6
	} // L: 8
	public void run() {
		try {
			for (int var1 = 0; var1 < 2; ++var1) { // L: 12
				PcmPlayer var2 = this.players[var1]; // L: 13
				if (var2 != null) { // L: 14
					var2.run();
				}
			}
		} catch (Exception var4) { // L: 17
			var4.printStackTrace(); // L: 18
		}
	} // L: 20
}
