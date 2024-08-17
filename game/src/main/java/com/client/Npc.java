package com.client;

import com.client.definitions.NpcDefinition;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
import com.client.definitions.anim.SequenceDefinition;
import com.client.entity.model.Model;
import com.client.features.settings.Preferences;
import com.client.util.headicon.class515;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.*;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import static com.client.Client.*;

public final class Npc extends Entity implements RSNPC {

	class515 field1329;

	@Override
	public boolean isVisible() {
		return desc != null;
	}

	Npc() {
	}

	public Model getRotatedModel() {
		if (!instance.isInterpolateNpcAnimations()
				|| this.getAnimation() == AnimationID.HELLHOUND_DEFENCE
				|| this.getAnimation() == 8270
				|| this.getAnimation() == 8271
				|| this.getPoseAnimation() == 5583
				|| this.getId() == NpcID.WYRM && this.getAnimation() == AnimationID.IDLE
				|| this.getId() == NpcID.TREE_SPIRIT && this.getAnimation() == AnimationID.IDLE
				|| this.getId() == NpcID.TREE_SPIRIT_6380 && this.getAnimation() == AnimationID.IDLE
				|| this.getId() == NpcID.TREE_SPIRIT_HARD && this.getAnimation() == AnimationID.IDLE
		)
		{
			return getModelVanilla();
		}
		int actionFrame = getActionFrame();
		int poseFrame = getPoseFrame();
		int spotAnimFrame = getSpotAnimFrame();
		try
		{
			// combine the frames with the frame cycle so we can access this information in the sequence methods
			// without having to change method calls
			setActionFrame(Integer.MIN_VALUE | getActionFrameCycle() << 16 | actionFrame);
			setPoseFrame(Integer.MIN_VALUE | getPoseFrameCycle() << 16 | poseFrame);
			setSpotAnimFrame(Integer.MIN_VALUE | getSpotAnimationFrameCycle() << 16 | spotAnimFrame);
			Iterator iter = getSpotAnims().iterator();
			while (iter.hasNext())
			{
				net.runelite.api.ActorSpotAnim actorSpotAnim = (net.runelite.api.ActorSpotAnim) iter.next();
				int frame = actorSpotAnim.getFrame();
				if (frame != -1)
				{
					actorSpotAnim.setFrame(Integer.MIN_VALUE | actorSpotAnim.getCycle() << 16 | frame);
				}
			}
			return getModelVanilla();
		}
		finally
		{
			// reset frames
			setActionFrame(actionFrame);
			setPoseFrame(poseFrame);
			setSpotAnimFrame(spotAnimFrame);
			Iterator iter = getSpotAnims().iterator();
			while (iter.hasNext())
			{
				net.runelite.api.ActorSpotAnim actorSpotAnim = (net.runelite.api.ActorSpotAnim) iter.next();
				int frame = actorSpotAnim.getFrame();
				if (frame != -1)
				{
					actorSpotAnim.setFrame(frame & '\uFFFF');
				}
			}
		}
	}

	public boolean isHintArrowPointingAtNpc(int index) {
		return Client.instance.hintArrowType == 1 && Client.instance.hintArrowNpcIndex == Client.instance.npcIndices[index - Client.instance.playerCount] && loopCycle % 20 < 10;
	}

	public int[] getSpriteIndices() {
		return this.field1329 != null ? this.field1329.method9299() : this.desc.getHeadIconArchiveIds();
	}

	public short[] getSpriteIds() {
		return this.field1329 != null ? this.field1329.method9300() : this.desc.headIconIndex();
	}


	public void drawIcons() {
		int[] spriteIndices = getSpriteIndices();
		short[] spriteIds = getSpriteIds();
		if (spriteIds != null && spriteIndices != null) {
			for (int index = 0; index < spriteIds.length; ++index) {
				if (spriteIds[index] >= 0 && spriteIndices[index] >= 0) {
					long cacheKey = (long) spriteIndices[index] << 8 | (long) spriteIds[index];
					Sprite icon = (Sprite) Client.archive5.method7781(cacheKey);
					if (icon == null) {
						Sprite[] sprites = Sprite.getSprites(spriteIndices[index], 0);
						if (sprites != null && spriteIds[index] < sprites.length) {
							icon = sprites[spriteIds[index]];
							Client.archive5.put(cacheKey, icon);
						}
					}

					if (icon != null) {
						Client.instance.npcScreenPos(this, defaultHeight + 15);
						if (Client.instance.viewportTempX > -1) {
							icon.drawSprite(viewportOffsetX + Client.instance.viewportTempX - (icon.myWidth >> 1), Client.instance.viewportTempY + (viewportOffsetY - icon.myHeight) - 4);
						}
					}
				}
			}
		}
	}

	public Model getModelVanilla() {
		if (this.desc == null) {
			return null;
		} else {
			SequenceDefinition sequenceDefinition = super.sequence != -1 && super.sequenceDelay == 0 ? SequenceDefinition.get(super.sequence) : null;
			SequenceDefinition walkSequenceDefinition = super.movementSequence == -1 || super.idleSequence == super.movementSequence && sequenceDefinition != null ? null : SequenceDefinition.get(super.movementSequence);
			Model animatedModel = this.desc.getAnimatedModel(sequenceDefinition, super.sequenceFrame, walkSequenceDefinition, super.movementFrame);
			if (animatedModel == null) {
				return null;
			} else {
				animatedModel.calculateBoundsCylinder();
				super.defaultHeight = animatedModel.model_height;
				int indicesCount = animatedModel.indicesCount;
				animatedModel = createSpotAnimModel(animatedModel);

				if (this.desc.size == 1) {
					animatedModel.singleTile = true;
				}



				if (super.recolourAmount != 0 && loopCycle >= super.recolourStartCycle && loopCycle < super.recolourEndCycle) {
					animatedModel.overrideHue = super.recolorHue;
					animatedModel.overrideSaturation = super.recolourSaturation;
					animatedModel.overrideLuminance = super.recolourLuminance;
					animatedModel.overrideAmount = super.recolourAmount;
					animatedModel.field2196 = (short)indicesCount;
				} else {
					animatedModel.overrideAmount = 0;
				}

				return animatedModel;
			}
		}
	}

	public boolean isShowMenuOnHover() {
		return npcPetType == 0 || npcPetType == 2 && !Preferences.getPreferences().hidePetOptions;
	}

	public int npcPetType;
	public NpcDefinition desc;

	@Override
	public int getCombatLevel() {
		return 0;
	}

	@Nullable
	@Override
	public NPCComposition getTransformedComposition() {
		return null;
	}

	@Override
	public void onDefinitionChanged(NPCComposition composition) {

	}


	@Override
	public int getId() {
		return desc.getId();
	}


	@Nullable
	@Override
	public String getName() {
		return null;
	}

	@Override
	public Actor getInteracting() {
		int index = interactingEntity;
		if (index == -1 || index == 65535)
		{
			return null;
		}
		Client client = instance;

		if (index < 32768)
		{
			Npc[] npcs = (Npc[]) client.getCachedNPCs();
			return npcs[index];
		}

		index -= 32768;
		Player[] players = client.players;
		return players[index];
	}

	@Override
	public void setIdleRotateLeft(int animationID) {

	}

	@Override
	public void setIdleRotateRight(int animationID) {

	}

	@Override
	public void setWalkAnimation(int animationID) {

	}

	@Override
	public void setWalkRotateLeft(int animationID) {

	}

	@Override
	public void setWalkRotateRight(int animationID) {

	}

	@Override
	public void setWalkRotate180(int animationID) {

	}

	@Override
	public void setRunAnimation(int animationID) {

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
	public WorldArea getWorldArea() {
		return null;
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	@Override
	public boolean isHidden() {
		return false;
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
	public int getAnimation() {
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
	public int getGraphic() {
		return 0;
	}

	@Override
	public void setGraphic(int id) {

	}

	@Override
	public int getSpotAnimFrame() {
		return 0;
	}

	@Override
	public void setSpotAnimFrame(int id) {

	}

	@Override
	public int getSpotAnimationFrameCycle() {
		return 0;
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
		return null;
	}

	@Override
	public int[] getHitsplatValues() {
		return new int[0];
	}

	@Override
	public int[] getHitsplatTypes() {
		return new int[0];
	}

	@Override
	public int[] getHitsplatCycles() {
		return new int[0];
	}

	@Override
	public int getIdleRotateLeft() {
		return 0;
	}

	@Override
	public int getIdleRotateRight() {
		return 0;
	}

	@Override
	public int getWalkAnimation() {
		return 0;
	}

	@Override
	public int getWalkRotate180() {
		return 0;
	}

	@Override
	public int getWalkRotateLeft() {
		return 0;
	}

	@Override
	public int getWalkRotateRight() {
		return 0;
	}

	@Override
	public int getRunAnimation() {
		return 0;
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

	@Override
	public int getPoseAnimationFrame() {
		return 0;
	}

	@Override
	public void setPoseAnimationFrame(int frame) {

	}

	@Override
	public RSNPCComposition getComposition() {
		return null;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int id) {
		this.index = id;
	}



	@Override
	public int getModelHeight() {
		return model_height;
	}

	@Override
	public void setModelHeight(int modelHeight) {
		model_height = modelHeight;
	}

	@Override
	public RSModel getModel() {
		return getRotatedModel();
	}

	@Override
	public void draw(int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash) {

	}
}
