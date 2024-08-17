package com.client.audio;

import com.client.collection.node.Node;
import net.runelite.rs.api.RSMusicPatch;

public class MusicPatchNode extends Node implements RSMusicPatch {
	
	int field3284;
	
	MusicPatch patch;
	
	RawSound rawSound;
	
	MusicPatchNode2 field3264;
	
	int field3267;
	
	int field3269;
	
	int field3282;
	
	int field3271;
	
	int field3272;
	
	int field3273;
	
	int field3277;
	
	int field3275;
	
	int field3276;
	
	int field3274;
	
	int field3278;
	
	int field3279;
	
	int field3280;
	
	int field3281;
	
	RawPcmStream stream;
	
	int field3283;
	
	int field3270;
	MusicPatchNode() {
	} // L: 31
	
	void method5471() {
		this.patch = null; // L: 34
		this.rawSound = null; // L: 35
		this.field3264 = null; // L: 36
		this.stream = null; // L: 37
	} // L: 38
}
