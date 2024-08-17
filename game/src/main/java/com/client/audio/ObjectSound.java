package com.client.audio;

import com.client.Client;
import com.client.collection.node.Node;
import com.client.collection.node.NodeDeque;
import com.client.definitions.ObjectDefinition;
import net.runelite.api.coords.LocalPoint;
import net.runelite.rs.api.RSObjectSound;

public final class ObjectSound extends Node implements RSObjectSound {

	public static NodeDeque objectSounds = new NodeDeque();

	int plane;

	int x;

	int y;

	int maxX;

	int maxY;

	int field812;

	int soundEffectId;

	public RawPcmStream stream1;

	int field819;

	int field811;
	int soundRetain;
	int[] soundEffectIds;

	int field809;

	ObjectDefinition obj;

	public RawPcmStream stream2;

	ObjectSound() {
	} // L: 27

	public static void updatePendingSound(int z, int var42, int x, int y, int var40) {
		ObjectDefinition var14 = ObjectDefinition.lookup(var40);
		if (var14 != null && var14.hasSound()) {
			int var17 = var14.sizeX;
			int var18 = var14.sizeY;
			if (var42 == 1 || var42 == 3) {
				var17 = var14.sizeY;
				var18 = var14.sizeX;
			}

			int var19 = (x + var17) * 128;
			int var20 = (y + var18) * 128;
			int var15 = x * 128;
			int var16 = y * 128;
			int var21 = var14.soundId;
			int var22 = var14.soundRange * 128;
			int var23 = var14.soundRetain * 128;
			if (var14.configs != null) {
				ObjectDefinition var24 = var14.method580();
				if (var24 != null) {
					var21 = var24.soundId;
					var22 = var24.soundRange * 128;
					var23 = var24.soundRetain * 128;
				}
			}

			Object var10000 = null;

			for (ObjectSound var28 = (ObjectSound)ObjectSound.objectSounds.last(); var28 != null; var28 = (ObjectSound)ObjectSound.objectSounds.previous()) {
				if (z == var28.plane && var15 == var28.x && var16 == var28.y && var19 == var28.maxX && var20 == var28.maxY && var21 == var28.soundEffectId && var22 == var28.field812 && var23 == var28.soundRetain) {
					if (var28.stream1 != null) {
						StaticSound.pcmStreamMixer.removeSubStream(var28.stream1);
						var28.stream1 = null;
					}

					if (var28.stream2 != null) {
						StaticSound.pcmStreamMixer.removeSubStream(var28.stream2);
						var28.stream2 = null;
					}

					var28.remove();
					break;
				}

				var10000 = null;
			}
		}

	}



	void set() {
		int var1 = this.soundEffectId; // L: 44
		ObjectDefinition var2 = this.obj.method580(); // L: 45
		if (var2 != null) { // L: 46
			this.soundEffectId = var2.soundId; // L: 47
			this.field812 = var2.soundRange * 128; // L: 48
			this.soundRetain = var2.soundRetain * 128;
			this.field819 = var2.soundMin; // L: 49
			this.field811 = var2.soundMax; // L: 50
			this.soundEffectIds = var2.soundEffectIds; // L: 51
		} else {
			this.soundEffectId = -1; // L: 54
			this.field812 = 0; // L: 55
			this.field819 = 0; // L: 56
			this.field811 = 0; // L: 57
			this.soundRetain = 0;
			this.soundEffectIds = null; // L: 58
		}
		if (var1 != this.soundEffectId && this.stream1 != null) { // L: 60
			StaticSound.pcmStreamMixer.removeSubStream(this.stream1); // L: 61
			this.stream1 = null; // L: 62
		}
	} // L: 64

	public static void addObjectSounds(int var0, int var1, int var2, ObjectDefinition var3, int var4) {
		ObjectSound object = new ObjectSound(); // L: 67
		object.plane = var0; // L: 68
		object.x = var1 * 128; // L: 69
		object.y = var2 * 128; // L: 70
		int var6 = var3.sizeX; // L: 71
		int var7 = var3.sizeY; // L: 72
		if (var4 == 1 || var4 == 3) { // L: 73
			var6 = var3.sizeY; // L: 74
			var7 = var3.sizeX; // L: 75
		}
		object.maxX = (var6 + var1) * 128; // L: 77
		object.maxY = (var7 + var2) * 128; // L: 78
		object.soundEffectId = var3.soundId; // L: 79
		object.soundRetain = Math.max(var3.soundRetain * 128 - 128, 0);
		object.field812 = var3.soundRange * 128; // L: 80
		object.field819 = var3.soundMin; // L: 81
		object.field811 = var3.soundMax; // L: 82
		object.soundEffectIds = var3.soundEffectIds; // L: 83
		if (var3.getTransforms() != null) { // L: 84
			object.obj = var3; // L: 85
			object.set(); // L: 86
		}
		objectSounds.addFirst(object); // L: 88
		if (object.soundEffectIds != null) { // L: 89
			object.field809 = object.field819 + (int) (Math.random() * (double) (object.field811 - object.field819));
		}
	} // L: 90

	public static void updateObjectSounds() {
		int var1 = Client.instance.plane;
		int var2 = Client.localPlayer.x;
		int var3 = Client.localPlayer.y;
		int var4 = Client.instance.tickDelta;

		for (ObjectSound var5 = (ObjectSound)ObjectSound.objectSounds.last(); var5 != null; var5 = (ObjectSound)ObjectSound.objectSounds.previous()) {
			if (var5.soundEffectId != -1 || var5.soundEffectIds != null) {
				int var6 = 0;
				if (var2 > var5.maxX) {
					var6 += var2 - var5.maxX;
				} else if (var2 < var5.x) {
					var6 += var5.x - var2;
				}

				if (var3 > var5.maxY) {
					var6 += var3 - var5.maxY;
				} else if (var3 < var5.y) {
					var6 += var5.y - var3;
				}

				var6 = Math.max(var6 - 64, 0);
				if (var6 < var5.field812 && Client.instance.settingManager.getAreaSoundEffectVolume() != 0 && var1 == var5.plane) {
					float var7 = var5.soundRetain < var5.field812 ? Math.min(Math.max((float)(var5.field812 - var6) / (float)(var5.field812 - var5.soundRetain), 0.0F), 1.0F) : 1.0F;
					int var8 = (int)(var7 * (float)Client.instance.settingManager.getAreaSoundEffectVolume());
					Object var10000;
					if (var5.stream1 == null) {
						if (var5.soundEffectId >= 0) {
							var10000 = null;
							SoundEffect var9 = SoundEffect.readSoundEffect(StaticSound.soundEffectsArchive, var5.soundEffectId, 0);
							if (var9 != null) {
								RawSound var10 = var9.toRawSound().resample(StaticSound.decimator);
								RawPcmStream var11 = RawPcmStream.createRawPcmStream(var10, 100, var8);
								var11.setNumLoops(-1);
								StaticSound.pcmStreamMixer.addSubStream(var11);
								var5.stream1 = var11;
							}
						}
					} else {
						var5.stream1.method790(var8);
					}

					if (var5.stream2 == null) {
						if (var5.soundEffectIds != null && (var5.field809 -= var4) <= 0) {
							int var13 = (int)(Math.random() * (double)var5.soundEffectIds.length);
							var10000 = null;
							SoundEffect var14 = SoundEffect.readSoundEffect(StaticSound.soundEffectsArchive, var5.soundEffectIds[var13], 0);
							if (var14 != null) {
								RawSound var15 = var14.toRawSound().resample(StaticSound.decimator);
								RawPcmStream var12 = RawPcmStream.createRawPcmStream(var15, 100, var8);
								var12.setNumLoops(0);
								StaticSound.pcmStreamMixer.addSubStream(var12);
								var5.stream2 = var12;
								var5.field809 = var5.field819 + (int)(Math.random() * (double)(var5.field811 - var5.field819));
							}
						}
					} else {
						var5.stream2.method790(var8);
						if (!var5.stream2.hasNext()) {
							var5.stream2 = null;
						}
					}
				} else {
					if (var5.stream1 != null) {
						StaticSound.pcmStreamMixer.removeSubStream(var5.stream1);
						var5.stream1 = null;
					}

					if (var5.stream2 != null) {
						StaticSound.pcmStreamMixer.removeSubStream(var5.stream2);
						var5.stream2 = null;
					}
				}
			}
		}
	}

	public static void clearObjectSounds() {
		for (ObjectSound var0 = (ObjectSound) objectSounds.last(); var0 != null; var0 = (ObjectSound) objectSounds.previous()) { // L:
			// 30
			if (var0.stream1 != null) { // L: 31
				StaticSound.pcmStreamMixer.removeSubStream(var0.stream1); // L: 32
				var0.stream1 = null; // L: 33
			}
			if (var0.stream2 != null) { // L: 35
				StaticSound.pcmStreamMixer.removeSubStream(var0.stream2);
				var0.stream2 = null;
			}
		}
		objectSounds.clear(); // L: 40
	} // L: 41

	public static void update() {
		for (ObjectSound var1 = (ObjectSound) ObjectSound.objectSounds.last(); var1 != null; var1 = (ObjectSound) ObjectSound.objectSounds.previous()) {
			if (var1.obj != null) {
				var1.set();
			}
		}
	}

	@Override
	public LocalPoint getMinPosition()
	{
		return new LocalPoint(getX(), getY());
	}

	@Override
	public LocalPoint getMaxPosition()
	{
		return new LocalPoint(getMaxX(), getMaxY());
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getMaxX() {
		return maxX;
	}

	@Override
	public int getMaxY() {
		return maxY;
	}

	@Override
	public int getSoundEffectId() {
		return soundEffectId;
	}

	@Override
	public int getPlane() {
		return plane;
	}
}
