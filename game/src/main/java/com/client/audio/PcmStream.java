package com.client.audio;

import com.client.collection.node.Node;
import net.runelite.rs.api.RSPcmStream;

public abstract class PcmStream extends Node implements RSPcmStream {
	public volatile boolean active;
	
	PcmStream after;
	int field346;
	
	public AbstractSound sound;
	protected PcmStream() {
		this.active = true; // L: 11
	} // L: 13
	
	public abstract PcmStream firstSubStream();
	
	public abstract PcmStream nextSubStream();
	protected abstract int vmethod5437();
	protected abstract void fill(int[] var1, int var2, int var3);
	protected abstract void skip(int var1);
	int vmethod948() {
		return 255; // L: 16
	}
	final void update(int[] var1, int var2, int var3) {
		if (this.active) { // L: 24
			this.fill(var1, var2, var3);
		} else {
			this.skip(var3); // L: 25
		}
	} // L: 26
}
