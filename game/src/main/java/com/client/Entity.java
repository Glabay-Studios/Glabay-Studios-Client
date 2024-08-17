package com.client;

import com.client.collection.iterable.IterableNodeDeque;
import com.client.collection.iterable.IterableNodeHashTableIterator;
import com.client.collection.table.IterableNodeHashTable;
import com.client.definitions.HitSplatDefinition;
import com.client.definitions.anim.SequenceDefinition;
import com.client.definitions.anim.SpotAnimation;
import com.client.definitions.healthbar.HealthBarDefinition;
import com.client.definitions.healthbar.HealthBar;
import com.client.entity.ActorSpotAnim;
import com.client.entity.model.Model;
import com.client.sound.Sound;
import com.client.sound.SoundType;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.HealthBarUpdated;
import net.runelite.rs.api.*;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import static com.client.Client.instance;
import static com.client.audio.StaticSound.playAnimationSound;
import static com.client.audio.StaticSound.playSkeletalSounds;

public class Entity extends Renderable implements RSActor {

	public IterableNodeDeque healthBars;
	byte hitSplatCount;
	public int[] hitSplatTypes;
	public int[] hitSplatValues;
	public int[] hitSplatCycles;
	public int[] hitSplatTypes2;
	public int[] hitSplatValues2;
	public int[] hitSplatDamageType;
	public int[] hitSplatDamageType2;

	public int index;
	public int movementFrameCycle;
	public int secondaryanimReplaycount;

	public boolean isLocalPlayer() {
		return this == Client.localPlayer;
	}

	public int getAbsoluteX() {
		int x = Client.baseX + (this.x - 6 >> 7);
		if (this instanceof Npc) {
			return x - ((Npc) this).desc.size / 2;
		}
		return x;
	}

	public int getAbsoluteY() {
		int y = Client.baseY + (this.y - 6 >> 7);
		if (this instanceof Npc) {
			return y - ((Npc) this).desc.size / 2;
		}
		return y;
	}

	public void readSpotAnimation(Buffer buffer) {
		int size = buffer.readUnsignedByte();

		for (int index = 0; index < size; ++index) {
			int pos = buffer.readUnsignedByte();
			int id = buffer.readUShort();
			int info = buffer.readInt();

			updateSpotAnimation(pos, id, info >> 16, info & 65535);
		}
	}

	public void updateSpotAnimation(int id, int height, int cycle, int frame) {
		int updatedFrame = frame + Client.loopCycle;

		// Remove existing spot animation
		ActorSpotAnim existingSpotAnim = (ActorSpotAnim) this.spotAnimations.get((long) id);
		if (existingSpotAnim != null) {
			existingSpotAnim.remove();
			--this.graphicsCount;
		}

		// Check if height is valid and add new spot animation
		if (height != 65535 && height != -1) {
			byte flag = frame > 0 ? (byte) -1 : 0;
			this.spotAnimations.put(new ActorSpotAnim(height, cycle, updatedFrame, flag), (long) id);
			++this.graphicsCount;
		}
	}

	public void updateAnimation() {
		isWalking = false;
		if (movementSequence != -1) {
			SequenceDefinition var2 = SequenceDefinition.get(movementSequence);
			if (var2 != null) {
				if (!var2.isCachedModelIdSet() && var2.primaryFrameIds != null) {
					++movementFrameCycle;
					if (movementFrame < var2.primaryFrameIds.length && movementFrameCycle > var2.delays[movementFrame]) {
						movementFrameCycle = 1;
						++movementFrame;
						playAnimationSound(var2, movementFrame, x, y,this);
					}

					if (movementFrame >= var2.primaryFrameIds.length) {
						if (var2.frameCount > 0) {
							movementFrame -= var2.frameCount;
							if (var2.replay) {
								++secondaryanimReplaycount;
							}

							if (movementFrame < 0 || movementFrame >= var2.primaryFrameIds.length || var2.replay && secondaryanimReplaycount >= var2.loopCount) {
								movementFrameCycle = 0;
								movementFrame = 0;
								secondaryanimReplaycount = 0;
							}
						} else {
							movementFrameCycle = 0;
							movementFrame = 0;
						}

						playAnimationSound(var2, movementFrame, x, y,this);
					}
				} else if (var2.isCachedModelIdSet()) {
					++movementFrame;
					int var3 = var2.getSkeletalLength();
					if (movementFrame < var3) {
						playSkeletalSounds(var2, movementFrame, x, y,this);
					} else {
						if (var2.frameCount > 0) {
							movementFrame -= var2.frameCount;
							if (var2.replay) {
								++secondaryanimReplaycount;
							}

							if (movementFrame < 0 || movementFrame >= var3 || var2.replay && secondaryanimReplaycount >= var2.loopCount) {
								movementFrame = 0;
								movementFrameCycle = 0;
								secondaryanimReplaycount = 0;
							}
						} else {
							movementFrameCycle = 0;
							movementFrame = 0;
						}

						playSkeletalSounds(var2, movementFrame, x, y,this);
					}
				} else {
					movementSequence = -1;
				}
			} else {
				movementSequence = -1;
			}
		}

		IterableNodeHashTableIterator var17 = new IterableNodeHashTableIterator(spotAnimations);

		for (ActorSpotAnim var14 = (ActorSpotAnim)var17.method2390(); var14 != null; var14 = (ActorSpotAnim)var17.next()) {
			if (var14.spotAnimation != -1 && Client.loopCycle >= var14.cycle) {
				int var4 = SpotAnimation.lookup(var14.spotAnimation).sequence;
				if (var4 == -1) {
					var14.remove();
					--graphicsCount;
				} else {
					var14.spotAnimationFrame = Math.max(var14.spotAnimationFrame, 0);
					SequenceDefinition var15 = SequenceDefinition.get(var4);
					if (var15.primaryFrameIds != null && !var15.isCachedModelIdSet()) {
						++var14.spotAnimationFrameCycle;
						if (var14.spotAnimationFrame < var15.primaryFrameIds.length && var14.spotAnimationFrameCycle > var15.delays[var14.spotAnimationFrame]) {
							var14.spotAnimationFrameCycle = 1;
							++var14.spotAnimationFrame;
							playAnimationSound(var15, var14.spotAnimationFrame, x, y,this);
						}

						if (var14.spotAnimationFrame >= var15.primaryFrameIds.length) {
							var14.remove();
							--graphicsCount;
						}
					} else if (var15.isCachedModelIdSet()) {
						++var14.spotAnimationFrame;
						int var12 = var15.getSkeletalLength();
						if (var14.spotAnimationFrame < var12) {
							playSkeletalSounds(var15, var14.spotAnimationFrame, x, y,this);
						} else {
							var14.remove();
							--graphicsCount;
						}
					} else {
						var14.remove();
						--graphicsCount;
					}
				}
			}
		}

		if (sequence != -1 && sequenceDelay <= 1) {
			SequenceDefinition sequenceDefinition_2 = SequenceDefinition.get(sequence);
			if (sequenceDefinition_2.moveStyle == 1 && stationaryPathPosition > 0 && initiate_movement  <= Client.loopCycle && cease_movement < Client.loopCycle) {
				sequenceDelay = 1;
				return;
			}
		}

		if (sequence != -1 && sequenceDelay == 0) {
			SequenceDefinition var2 = SequenceDefinition.get(sequence);
			if (var2 == null) {
				sequence = -1;
			} else if (!var2.isCachedModelIdSet() && var2.primaryFrameIds != null) {
				++sequenceFrameCycle;
				if (sequenceFrame < var2.primaryFrameIds.length && sequenceFrameCycle > var2.delays[sequenceFrame]) {
					sequenceFrameCycle = 1;
					++sequenceFrame;
					playAnimationSound(var2, sequenceFrame, x, y,this);
				}

				if (sequenceFrame >= var2.primaryFrameIds.length) {
					sequenceFrame -= var2.frameCount;
					++currentAnimationLoops;
					if (currentAnimationLoops >= var2.loopCount) {
						sequence = -1;
					} else if (sequenceFrame >= 0 && sequenceFrame < var2.primaryFrameIds.length) {
						playAnimationSound(var2, sequenceFrame, x, y,this);
					} else {
						sequence = -1;
					}
				}

				isWalking = var2.stretches;
			} else if (var2.isCachedModelIdSet()) {
				++sequenceFrame;
				int var3 = var2.getSkeletalLength();
				if (sequenceFrame < var3) {
					playSkeletalSounds(var2, sequenceFrame, x, y,this);
				} else {
					sequenceFrame -= var2.frameCount;
					++sequenceFrame;
					if (sequenceFrame >= var2.loopCount) {
						sequence = -1;
					} else if (sequenceFrame >= 0 && sequenceFrame < var3) {
						playSkeletalSounds(var2, sequenceFrame, x, y,this);
					} else {
						sequence = -1;
					}
				}
			} else {
				sequence = -1;
			}
		}

		if (sequenceDelay > 0)
			sequenceDelay--;
	}



	public int getDistanceFrom(Entity entity) {
		return getDistanceFrom(entity.getAbsoluteX(), entity.getAbsoluteY());
	}

	public int getDistanceFrom(int x2, int y2) {
		int x = (int) Math.pow(getAbsoluteX() - x2, 2.0D);
		int y = (int) Math.pow(getAbsoluteY() - y2, 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}

	public void makeSound(int soundId) {
		double distance = getDistanceFrom(Client.localPlayer);
//		if (Configuration.developerMode) {
//			System.out.println("entity sound: id " + id + " x" + getAbsoluteX() + " y" + getAbsoluteY() + " d" + distance);
//		}
		Sound.getSound().playSound(soundId, isLocalPlayer() || this instanceof Npc ? SoundType.SOUND : SoundType.AREA_SOUND, distance);
	}

	public final void setPos(int i, int j, boolean flag) {
		if (sequence != -1 && SequenceDefinition.get(sequence).idleStyle == 1)
			sequence = -1;
		if (!flag) {
			int k = i - pathX[0];
			int l = j - pathY[0];
			if (k >= -8 && k <= 8 && l >= -8 && l <= 8) {
				if (smallXYIndex < 9)
					smallXYIndex++;
				for (int i1 = smallXYIndex; i1 > 0; i1--) {
					pathX[i1] = pathX[i1 - 1];
					pathY[i1] = pathY[i1 - 1];
					aBooleanArray1553[i1] = aBooleanArray1553[i1 - 1];
				}

				pathX[0] = i;
				pathY[0] = j;
				aBooleanArray1553[0] = false;
				return;
			}
		}
		smallXYIndex = 0;
		stationaryPathPosition = 0;
		anInt1503 = 0;
		pathX[0] = i;
		pathY[0] = j;
		x = pathX[0] * 128 + size * 64;
		y = pathY[0] * 128 + size * 64;
	}

	public final void method446() {
		smallXYIndex = 0;
		stationaryPathPosition = 0;
	}

	public final void moveInDir(boolean flag, int i) {
		int j = pathX[0];
		int k = pathY[0];
		if (i == 0) {
			j--;
			k++;
		}
		if (i == 1)
			k++;
		if (i == 2) {
			j++;
			k++;
		}
		if (i == 3)
			j--;
		if (i == 4)
			j++;
		if (i == 5) {
			j--;
			k--;
		}
		if (i == 6)
			k--;
		if (i == 7) {
			j++;
			k--;
		}
		if (sequence != -1 && SequenceDefinition.get(sequence).idleStyle == 1)
			sequence = -1;
		if (smallXYIndex < 9)
			smallXYIndex++;
		for (int l = smallXYIndex; l > 0; l--) {
			pathX[l] = pathX[l - 1];
			pathY[l] = pathY[l - 1];
			aBooleanArray1553[l] = aBooleanArray1553[l - 1];
		}
		pathX[0] = j;
		pathY[0] = k;
		aBooleanArray1553[0] = flag;
	}


	int graphicsCount;
	public IterableNodeHashTable spotAnimations;

	public void clearSpotAnimations() {
		IterableNodeHashTableIterator spotAnim = new IterableNodeHashTableIterator(this.spotAnimations);

		for (ActorSpotAnim currentSpotAnimation = (ActorSpotAnim)spotAnim.method2390(); currentSpotAnimation != null; currentSpotAnimation = (ActorSpotAnim)spotAnim.next()) {
			currentSpotAnimation.remove();
		}

		this.graphicsCount = 0;
	}


	Model createSpotAnimModel(Model model) {
		if (this.graphicsCount == 0) {
			return model;
		} else {
			IterableNodeHashTableIterator iterator = new IterableNodeHashTableIterator(this.spotAnimations);
			int totalVertices = model.verticesCount;
			int totalIndices = model.indicesCount;
			int someField = model.field2153;
			byte var6 = model.field2152;

			for (ActorSpotAnim spotAnim = (ActorSpotAnim)iterator.method2390(); spotAnim != null; spotAnim = (ActorSpotAnim)iterator.next()) {
				if (spotAnim.spotAnimationFrame != -1) {
					Model animModel = SpotAnimation.lookup(spotAnim.spotAnimation).createModel();
					if (animModel != null) {
						totalVertices += animModel.verticesCount;
						totalIndices += animModel.indicesCount;
						someField += animModel.field2153;
					}
				}
			}

			Model resultModel = new Model(totalVertices, totalIndices, someField, var6);
			resultModel.method1342(model);

			for (ActorSpotAnim spotAnim = (ActorSpotAnim)iterator.method2390(); spotAnim != null; spotAnim = (ActorSpotAnim)iterator.next()) {
				if (spotAnim.spotAnimationFrame != -1) {
					Model animModel = SpotAnimation.lookup(spotAnim.spotAnimation).getModel(spotAnim.spotAnimationFrame);
					if (animModel != null) {
						animModel.offsetBy(0, -spotAnim.spotAnimationHeight, 0);
						resultModel.method1342(animModel);
					}
				}
			}

			return resultModel;
		}
	}

	public boolean isVisible() {
		return false;
	}

	Entity() {
		pathX = new int[10];
		pathY = new int[10];
		interactingEntity = -1;
		healthBars = new IterableNodeDeque();
		hitSplatCount = 0;
		hitSplatTypes = new int[4];
		hitSplatValues = new int[4];
		hitSplatCycles = new int[4];
		hitSplatTypes2 = new int[4];
		hitSplatValues2 = new int[4];
		hitSplatDamageType = new int[4];
		hitSplatDamageType2 = new int[4];
		rotation = 32;
		runAnimIndex = -1;
		defaultHeight = 200;
		idleSequence = -1;
		standTurnAnimIndex = -1;

		movementSequence = -1;
		sequence = -1;
		overheadTextCyclesRemaining = 100;
		size = 1;
		isWalking = false;
		aBooleanArray1553 = new boolean[10];
		walkSequence = -1;
		walkBackSequence = -1;
		walkLeftSequence = -1;
		walkRightSequence = -1;
		spotAnimations = new IterableNodeHashTable(4);
	}

	public final int[] pathX;
	public final int[] pathY;
	public int interactingEntity;
	int anInt1503;
	int rotation;
	int runAnimIndex;
	public String spokenText;
	public String lastForceChat;
	public int defaultHeight;
	int turn_direction;
	public int idleSequence;
	int standTurnAnimIndex;
	int textColour;

	int movementSequence;
	int movementFrame;
	int queued_animation_duration;

	public int next_animation_frame;
	public int next_idle_frame;
	public int next_graphic_frame;
	int smallXYIndex;
	public int sequence;
	public byte recolorHue;
	public byte recolourSaturation;
	public int recolourStartCycle;
	public int recolourEndCycle;

	public byte recolourLuminance;
	public byte recolourAmount;
	public int sequenceFrame;
	public int sequenceFrameCycle;
	public int sequenceDelay;
	public int currentAnimationLoops;
	int textEffect;

	public int currentHealth;
	public int maxHealth;
	int overheadTextCyclesRemaining;
	int lastUpdateTick;
	int anInt1538;
	int anInt1539;
	int size;
	boolean isWalking;
	int stationaryPathPosition;
	int anInt1543;
	int anInt1544;
	int anInt1545;
	int anInt1546;
	int initiate_movement;
	int cease_movement;
	int forceMovementDirection;
	public int x;
	public int y;
	int orientation;
	final boolean[] aBooleanArray1553;
	int walkSequence;
	int walkBackSequence;
	int walkLeftSequence;
	int walkRightSequence;

	public int getTurnDirection() {
		return turn_direction;
	}

	public void setTurnDirection(int turnDirection) {
		this.turn_direction = turnDirection;
	}

	@Override
	public boolean isDead()
	{
		return false;
	}

	@Override
	public void setDead(boolean dead) {

	}

	@Override
	public int getPathLength() {
		return 0;
	}

	@Override
	public int getOverheadCycle() {
		return 0;
	}

	@Override
	public void setOverheadCycle(int cycle) {

	}

	public void setCombatInfo(int combatInfoId, int gameCycle, int var3, int var4, int healthRatio, int health)
	{
		final HealthBarUpdated event = new HealthBarUpdated(this, healthRatio);
		Client.instance.getCallbacks().post(event);

		if (healthRatio == 0)
		{
			if (isDead())
			{
				return;
			}


			setDead(true);

			final ActorDeath actorDeath = new ActorDeath(this);
			Client.instance.getCallbacks().post(actorDeath);
		}
		else if (healthRatio > 0)
		{
			if (this instanceof RSNPC && ((RSNPC) this).getId() == NpcID.CORPOREAL_BEAST && isDead())
			{
				return;
			}

			setDead(false);
		}
	}

	@Override
	public boolean isMoving()
	{
		return getPathLength() > 0;
	}

	@Override
	public boolean isAnimating() {
		return RSActor.super.isAnimating();
	}

	@Override
	public int getGraphic()
	{
		Iterator iter = this.getSpotAnims().iterator();
		if (iter.hasNext())
		{
			ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
			return actorSpotAnim.getId();
		}
		else
		{
			return -1;
		}
	}

	@Override
	public int getRSInteracting() {
		return 0;
	}

	@Override
	public String getOverheadText() {
		return null;
	}

	@Override
	public void setOverheadText(String overheadText) {

	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int[] getPathX() {
		return new int[0];
	}

	@Override
	public int[] getPathY() {
		return new int[0];
	}

	@Override
	public int getRSAnimation() {
		return 0;
	}

	@Override
	public void setAnimation(int animation) {

	}

	@Override
	public int getAnimationFrame() {
		return 0;
	}

	@Override
	public int getActionFrame() {
		return 0;
	}

	@Override
	public void setAnimationFrame(int frame) {

	}

	@Override
	public void setActionFrame(int frame) {

	}

	@Override
	public int getActionFrameCycle() {
		return 0;
	}

	@Override
	public IterableNodeHashTable getSpotAnims() {
		return spotAnimations;
	}

	@Override
	public RSActorSpotAnim newActorSpotAnim(int id, int height, int delay, int frame) {
		return null;
	}

	@Override
	public int getGraphicsCount() {
		return 0;
	}

	@Override
	public void setGraphicsCount(int count) {

	}

	@Override
	public int getIdlePoseAnimation() {
		return 0;
	}

	@Override
	public void setIdlePoseAnimation(int animation) {

	}

	@Override
	public int getPoseAnimation() {
		return 0;
	}

	@Override
	public void setPoseAnimation(int animation) {

	}

	@Override
	public int getPoseFrame() {
		return 0;
	}

	@Override
	public void setPoseFrame(int frame) {

	}

	@Override
	public int getPoseFrameCycle() {
		return 0;
	}

	@Override
	public int getLogicalHeight() {
		return 0;
	}

	@Override
	public int getOrientation() {
		return 0;
	}

	@Override
	public int getCurrentOrientation() {
		return 0;
	}

	@Override
	public RSIterableNodeDeque getHealthBars() {
		return healthBars;
	}

	@Override
	public int[] getHitsplatValues() {
		return hitSplatValues;
	}

	@Override
	public int[] getHitsplatTypes() {
		return hitSplatTypes;
	}

	@Override
	public int[] getHitsplatCycles() {
		return hitSplatCycles;
	}

	@Override
	public int getIdleRotateLeft() {
		return 0;
	}

	@Override
	public void setIdleRotateLeft(int id) {

	}

	@Override
	public int getIdleRotateRight() {
		return 0;
	}

	@Override
	public void setIdleRotateRight(int id) {

	}

	@Override
	public int getWalkAnimation() {
		return 0;
	}

	@Override
	public void setWalkAnimation(int id) {

	}

	@Override
	public int getWalkRotate180() {
		return 0;
	}

	@Override
	public void setWalkRotate180(int id) {

	}

	@Override
	public int getWalkRotateLeft() {
		return 0;
	}

	@Override
	public void setWalkRotateLeft(int id) {

	}

	@Override
	public int getWalkRotateRight() {
		return 0;
	}

	@Override
	public void setWalkRotateRight(int id) {

	}

	@Override
	public int getRunAnimation() {
		return 0;
	}

	@Override
	public void setRunAnimation(int id) {

	}

	@Override
	public boolean hasSpotAnim(int spotAnimId) {
		return false;
	}

	@Override
	public void createSpotAnim(int id, int spotAnimId, int height, int delay) {

	}

	@Override
	public void removeSpotAnim(int id) {

	}

	@Override
	public void clearSpotAnims() {

	}

	@Override
	public void setGraphic(int id)
	{
		if (id == -1)
		{
			this.getSpotAnims().clear();
			this.setGraphicsCount(0);
		}
		else
		{
			Iterator iter = this.getSpotAnims().iterator();
			if (iter.hasNext())
			{
				ActorSpotAnim var3 = (ActorSpotAnim) iter.next();
				var3.setId(id);
			}
			else
			{
				RSActorSpotAnim actorSpotAnim = this.newActorSpotAnim(id, 0, 0, 0);
				this.getSpotAnims().put(actorSpotAnim, 0L);
				this.setGraphicsCount(getGraphicsCount() + 1);
			}
		}
	}

	@Override
	public int getGraphicHeight()
	{
		Iterator iter = this.getSpotAnims().iterator();
		if (iter.hasNext())
		{
			ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
			return actorSpotAnim.getHeight();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void setGraphicHeight(int height)
	{
		Iterator iter = this.getSpotAnims().iterator();
		if (iter.hasNext())
		{
			ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
			actorSpotAnim.setHeight(height);
		}
	}

	@Override
	public int getSpotAnimFrame()
	{
		Iterator iter = this.getSpotAnims().iterator();
		if (iter.hasNext())
		{
			ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
			return actorSpotAnim.getFrame();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void setSpotAnimFrame(int id)
	{
		Iterator iter = this.getSpotAnims().iterator();
		if (iter.hasNext())
		{
			ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
			actorSpotAnim.setFrame(id);
		}
	}

	@Override
	public int getSpotAnimationFrameCycle()
	{
		Iterator iter = this.getSpotAnims().iterator();
		if (iter.hasNext())
		{
			ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
			return actorSpotAnim.getCycle();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public Polygon getCanvasTilePoly() {
		return null;
	}

	@Nullable
	@Override
	public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
		return null;
	}

	@Override
	public Point getCanvasImageLocation(BufferedImage image, int zOffset) {
		return null;
	}

	@Override
	public Point getCanvasSpriteLocation(SpritePixels sprite, int zOffset) {
		return null;
	}

	@Override
	public Point getMinimapLocation() {
		return null;
	}

	@Override
	public Shape getConvexHull() {
		return null;
	}

	@Override
	public int getCombatLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isInteracting() {
		return false;
	}

	@Override
	public Actor getInteracting() {
		return null;
	}

	@Override
	public int getHealthRatio()
	{
		RSIterableNodeDeque healthBars = getHealthBars();
		if (healthBars != null)
		{
			RSNode current = healthBars.getCurrent();
			RSNode next = current.getNext();
			if (next instanceof RSHealthBar)
			{
				RSHealthBar wrapper = (RSHealthBar) next;
				RSIterableNodeDeque updates = wrapper.getUpdates();

				RSNode currentUpdate = updates.getCurrent();
				RSNode nextUpdate = currentUpdate.getNext();
				if (nextUpdate instanceof RSHealthBarUpdate)
				{
					RSHealthBarUpdate update = (RSHealthBarUpdate) nextUpdate;
					return update.getHealthRatio();
				}
			}
		}
		return -1;
	}
	@Override
	public int getHealthScale()
	{
		RSIterableNodeDeque healthBars = getHealthBars();
		if (healthBars != null)
		{
			RSNode current = healthBars.getCurrent();
			RSNode next = current.getNext();
			if (next instanceof RSHealthBar)
			{
				RSHealthBar wrapper = (RSHealthBar) next;
				RSHealthBarDefinition definition = wrapper.getDefinition();
				return definition.getHealthScale();
			}
		}
		return -1;
	}

	@Override
	public WorldPoint getWorldLocation() {
		return WorldPoint.fromLocal(instance, getLocalLocation());
	}

	@Override
	public LocalPoint getLocalLocation() {
		return new LocalPoint(this.x, this.y);
	}


	@Override
	public int getAnimation()
	{
		int animation = getRSAnimation();
		switch (animation)
		{
			/*case 7592:
			case 7593:
			case 7949:
			case 7950:
			case 7951:
			case 7952:
			case 7957:
			case 7960:
			case 8059:
			case 8123:
			case 8124:
			case 8125:
			case 8126:
			case 8127:
			case 8234:
			case 8235:
			case 8236:
			case 8237:
			case 8238:
			case 8241:
			case 8242:
			case 8243:
			case 8244:
			case 8245:
			case 8248:
			case 8249:
			case 8250:
			case 8251:
			case 8252:
			case 8255:
			case 8256:
			case 8257:
			case 8258:
				return -1;*/
			default:
				return animation;
		}
	}

	public final void updateHitSplat(int primaryId, int primaryDamage, int secondaryId, int secondaryDamage, int currentTick, int delay, int primaryDamageType, int secondaryDamageType) {

		boolean allSlotsOlderThanCurrent = true;
		boolean allSlotsNewerThanCurrent = true;

		// Check the age of all hit splat slots
		for (int i = 0; i < 4; ++i) {
			if (this.hitSplatCycles[i] > currentTick) {
				allSlotsOlderThanCurrent = false;
			} else {
				allSlotsNewerThanCurrent = false;
			}
		}

		int availableSlotIndex = -1;
		int priorityType = -1;
		int expirationDuration = 0;

		// If the primary hit splat has a valid definition, obtain its priority and expiration values
		if (primaryId >= 0) {
			HitSplatDefinition hitDef = HitSplatDefinition.lookup(primaryId);
			priorityType = hitDef.usedDamage;
			expirationDuration = hitDef.animationDuration;
		}

		if (allSlotsNewerThanCurrent) {
			// All hit splats are newer than current tick, we need to potentially overwrite an existing hit splat
			if (priorityType == -1) {
				return; // No suitable slot found
			}

			int oldestSlotValue = (priorityType == 0) ? this.hitSplatCycles[0] : this.hitSplatValues[0];
			availableSlotIndex = 0;

			// Find the index of the oldest or lowest damage hit splat based on the priority type
			for (int i = 1; i < 4; ++i) {
				int currentValue = (priorityType == 0) ? this.hitSplatCycles[i] : this.hitSplatValues[i];
				if (currentValue < oldestSlotValue) {
					availableSlotIndex = i;
					oldestSlotValue = currentValue;
				}
			}

			if (priorityType == 1 && oldestSlotValue >= primaryDamage) {
				return; // If by damage priority and the oldest damage is greater than or equal to the current, then skip
			}
		} else {
			if (allSlotsOlderThanCurrent) {
				this.hitSplatCount = 0;  // Reset counter if all slots are older than current tick
			}

			// Find the first available slot older than the current tick or circle back to the oldest
			for (int i = 0; i < 4; ++i) {
				int currentIndex = (this.hitSplatCount + i) % 4;
				if (this.hitSplatCycles[currentIndex] <= currentTick) {
					availableSlotIndex = currentIndex;
					break;
				}
			}
		}

		// Update hit splat information in the chosen slot
		if (availableSlotIndex >= 0) {
			this.hitSplatTypes[availableSlotIndex] = primaryId;
			this.hitSplatValues[availableSlotIndex] = primaryDamage;
			this.hitSplatTypes2[availableSlotIndex] = secondaryId;
			this.hitSplatValues2[availableSlotIndex] = secondaryDamage;
			this.hitSplatCycles[availableSlotIndex] = currentTick + expirationDuration + delay;
			this.hitSplatDamageType[availableSlotIndex] = primaryDamageType;
			this.hitSplatDamageType2[availableSlotIndex] = secondaryDamageType;
		}
	}

	public void addHitSplat(Buffer buffer) {
		int hitSplatCount = buffer.readUnsignedByte();
		if (hitSplatCount > 0) {
			for (int i = 0; i < hitSplatCount; i++) {
				readAndAddHitSplat(buffer);
			}
		}
	}

	private void readAndAddHitSplat(Buffer buffer) {
		int primaryId = -1;
		int primaryDamage = -1;
		int secondaryId = -1;
		int secondaryDamage = -1;
		int primaryDamageType = -1;
		int secondaryDamageType = -1;
		int initialId = buffer.readUnsignedByte();
		if (initialId == 1) {
			primaryId = buffer.readUnsignedByte();
			primaryDamage = buffer.readUnsignedByte();
			primaryDamageType = buffer.readUnsignedByte();
			secondaryId = buffer.readUnsignedByte();
			secondaryDamage = buffer.readUnsignedByte();
			secondaryDamageType = buffer.readUnsignedByte();
		} else if (initialId == 0) {
			primaryId = buffer.readUnsignedByte();
			primaryDamage = buffer.readUnsignedByte();
			primaryDamageType = buffer.readUnsignedByte();
		}

		int delay = buffer.readUnsignedByte();
		updateHitSplat(primaryId, primaryDamage, secondaryId, secondaryDamage, Client.loopCycle, delay, primaryDamageType, secondaryDamageType);
	}


	public void addHealthBar(Buffer buffer) {
		int healthBarCount = buffer.readUShort();
		if (healthBarCount > 0) {
			for (int i = 0; i < healthBarCount; i++) {
				int barId = buffer.readUShort();
				int barValue = buffer.readUShort();

				if (barValue != 32767) {
					int cycleDuration = buffer.readUShort();
					int healthBarEndCycle = buffer.readUShort();
					int healthBarCycleOffset = barValue > 0 ? buffer.readUShort() : healthBarEndCycle;

					updateHealthBar(barId, Client.loopCycle, barValue, cycleDuration, healthBarEndCycle, healthBarCycleOffset);
				} else {
					removeHealthBar(barId);
				}
			}
		}
	}

	public final void updateHealthBar(int var1, int tickOffset, int healthValue, int cycleDuration, int endCycle, int cycleOffset) {
		HealthBarDefinition healthBarDef = HealthBarDefinition.lookup(var1);

		HealthBar previousMatchingBar = null;
		HealthBar lastBarWithLowerPriority = null;
		int healthBarCount = 0;
		int highestPrioritySeen = healthBarDef.int2;

		for (HealthBar currentBar = (HealthBar) this.healthBars.last();
			 currentBar != null;
			 currentBar = (HealthBar) this.healthBars.previous()) {

			healthBarCount++;

			if (currentBar.definition.field1994 == healthBarDef.field1994) {
				currentBar.put(tickOffset + cycleDuration, endCycle, cycleOffset, healthValue);
				return;
			}

			if (currentBar.definition.int1 <= healthBarDef.int1) {
				lastBarWithLowerPriority = currentBar;
			}

			if (currentBar.definition.int2 > highestPrioritySeen) {
				previousMatchingBar = currentBar;
				highestPrioritySeen = currentBar.definition.int2;
			}
		}

		if (previousMatchingBar != null || healthBarCount < 4) {
			HealthBar newBar = new HealthBar(healthBarDef);
			if (lastBarWithLowerPriority == null) {
				this.healthBars.addLast(newBar);
			} else {
				IterableNodeDeque.IterableNodeDeque_addBefore(newBar, lastBarWithLowerPriority);
			}

			newBar.put(tickOffset + cycleDuration, endCycle, cycleOffset, healthValue);

			if (healthBarCount >= 4) {
				previousMatchingBar.remove();
			}
		}
	}


	public final void removeHealthBar(int barId) {
		HealthBarDefinition healthBarDef = HealthBarDefinition.lookup(barId);

		for (HealthBar currentBar = (HealthBar) this.healthBars.last();
			 currentBar != null;
			 currentBar = (HealthBar) this.healthBars.previous()) {

			if (healthBarDef == currentBar.definition) {
				currentBar.remove();
				return;
			}
		}
	}

	@Override
	public int getPoseAnimationFrame() {
		return 0;
	}

	@Override
	public void setPoseAnimationFrame(int frame) {

	}

	public WorldArea getWorldArea()
	{
		int size = 1;
		if (this instanceof NPC)
		{
			NPCComposition composition = ((NPC) this).getComposition();
			if (composition != null && composition.getConfigs() != null)
			{
				composition = composition.transform();
			}
			if (composition != null)
			{
				size = composition.getSize();
			}
		}

		return new WorldArea(this.getWorldLocation(), size, size);
	}


}
