package com.client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.client.collection.EvictingDualNodeHashTable;
import com.client.definitions.ItemDefinition;
import com.client.entity.model.Model;
import com.client.entity.model.Mesh;
import com.client.definitions.NpcDefinition;
import com.client.definitions.anim.SequenceDefinition;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.kit.KitType;
import net.runelite.rs.api.*;

import javax.annotation.Nullable;

public final class Player extends Entity implements RSPlayer {

	public int usedItemID;
	public String title;
	public String titleColor;
	public int tileHeight;


	public void updatePlayer(Buffer stream) {
		stream.currentPosition = 0;
		myGender = stream.readUnsignedByte();
		title = stream.readString();
		titleColor = stream.readString();

		healthState = stream.readUnsignedByte();
		headIcon = stream.readUnsignedByte();
		skullIcon = stream.readUnsignedByte();
		npcDefinition = null;
		team = 0;
		for (int j = 0; j < 12; j++) {
			int k = stream.readUnsignedByte();
			if (k == 0) {
				equipment[j] = 0;
				continue;
			}
			int i1 = stream.readUnsignedByte();
			equipment[j] = (k << 8) + i1;
			if (j == 0 && equipment[0] == 65535) {
				npcDefinition = NpcDefinition.get(stream.readUShort());
				break;
			}
			if (j == 8) {
				Client.myHeadAndJaw[0] = equipment[j] - 256;
			}
			if (j == 11) {
				Client.myHeadAndJaw[1] = equipment[j] - 256;
			}
			if (equipment[j] >= 512 && equipment[j] - 512 < Js5List.getConfigSize(Js5ConfigType.ITEM)) {
				int l1 = ItemDefinition.lookup(equipment[j] - 512).team;
				if (l1 != 0)
					team = l1;
			}
		}

		for (int l = 0; l < 5; l++) {
			int j1 = stream.readUnsignedByte();
			if (j1 < 0 || j1 >= Client.APPEARANCE_COLORS[l].length)
				j1 = 0;
			appearanceColors[l] = j1;
		}

		super.idleSequence = stream.readUShort();
		if (super.idleSequence == 65535)
			super.idleSequence = -1;
		super.standTurnAnimIndex = stream.readUShort();
		if (super.standTurnAnimIndex == 65535)
			super.standTurnAnimIndex = -1;
		super.walkSequence = stream.readUShort();
		if (super.walkSequence == 65535)
			super.walkSequence = -1;
		super.walkBackSequence = stream.readUShort();
		if (super.walkBackSequence == 65535)
			super.walkBackSequence = -1;
		super.walkLeftSequence = stream.readUShort();
		if (super.walkLeftSequence == 65535)
			super.walkLeftSequence = -1;
		super.walkRightSequence = stream.readUShort();
		if (super.walkRightSequence == 65535)
			super.walkRightSequence = -1;
		super.runAnimIndex = stream.readUShort();
		if (super.runAnimIndex == 65535)
			super.runAnimIndex = -1;
		displayName = stream.readString();
		visible = stream.readUnsignedByte() == 0;
		combatLevel = stream.readUnsignedByte();
		rights = PlayerRights.readRightsFromPacket(stream).getRight();
		displayedRights = PlayerRights.getDisplayedRights(rights);
		skill = stream.readUShort();
		aLong1718 = 0L;
		for (int k1 = 0; k1 < 12; k1++) {
			aLong1718 <<= 4;
			if (equipment[k1] >= 256)
				aLong1718 += equipment[k1] - 256;
		}

		if (equipment[0] >= 256)
			aLong1718 += equipment[0] - 256 >> 4;
		if (equipment[1] >= 256)
			aLong1718 += equipment[1] - 256 >> 8;
		for (int i2 = 0; i2 < 5; i2++) {
			aLong1718 <<= 3;
			aLong1718 += appearanceColors[i2];
		}

		aLong1718 <<= 1;
		aLong1718 += myGender;
	}

	@Override
	public Model getRotatedModel() {
		if (!Client.instance.isInterpolatePlayerAnimations() || this.getPoseAnimation() == 244)
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
				ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
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
				ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
				int frame = actorSpotAnim.getFrame();
				if (frame != -1)
				{
					actorSpotAnim.setFrame(frame & '\uFFFF');
				}
			}
		}
	}

	public Model getModelVanilla() {

		if (!this.visible) {
			return null;
		} else {
			SequenceDefinition sequenceDefinition = super.sequence != -1 && super.sequenceDelay == 0 ? SequenceDefinition.get(super.sequence) : null;
			SequenceDefinition walkSequenceDefinition = super.movementSequence == -1 || this.isUnanimated || super.movementSequence == super.idleSequence && sequenceDefinition != null ? null : SequenceDefinition.get(super.movementSequence);
			Model animatedModel = getAnimatedModel(sequenceDefinition, super.sequenceFrame, walkSequenceDefinition, super.movementFrame);
			if (animatedModel == null) {
				return null;
			} else {
				animatedModel.calculateBoundsCylinder();
				super.defaultHeight = animatedModel.model_height;
				int indicesCount = animatedModel.indicesCount;
				Model model;
				Model[] models;
				if (!this.isUnanimated) {
					animatedModel = this.createSpotAnimModel(animatedModel);
				}

				if (!this.isUnanimated && this.playerModel != null) {
					if (Client.loopCycle >= this.animationCycleEnd) {
						this.playerModel = null;
					}

					if (Client.loopCycle >= this.animationCycleStart && Client.loopCycle < this.animationCycleEnd) {
						model = this.playerModel;
						model.offsetBy(this.field1117 * 4096 - super.x, this.tileHeight2 - this.tileHeight, this.field1123 * 4096 - super.y);
						if (super.turn_direction == 512) {
							model.rs$rotateY90Ccw();
							model.rs$rotateY90Ccw();
							model.rs$rotateY90Ccw();
						} else if (super.turn_direction == 1024) {
							model.rs$rotateY90Ccw();
							model.rs$rotateY90Ccw();
						} else if (super.turn_direction == 1536) {
							model.rs$rotateY90Ccw();
						}

						models = new Model[]{animatedModel, model};
						animatedModel = new Model(models, 2);
						if (super.turn_direction == 512) {
							model.rs$rotateY90Ccw();
						} else if (super.turn_direction == 1024) {
							model.rs$rotateY90Ccw();
							model.rs$rotateY90Ccw();
						} else if (super.turn_direction == 1536) {
							model.rs$rotateY90Ccw();
							model.rs$rotateY90Ccw();
							model.rs$rotateY90Ccw();
						}

						model.offsetBy(super.x - this.field1117 * 4096, this.tileHeight - this.tileHeight2, super.y - this.field1123 * 4096);
					}
				}

				animatedModel.singleTile = true;
				if (super.recolourAmount != 0 && Client.loopCycle >= super.recolourStartCycle && Client.loopCycle < super.recolourEndCycle) {
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

	public Mesh getDialogueModel() {
		if (!visible) {
			return null;
		}

		if (npcDefinition != null) {
			return npcDefinition.getDialogueModel();
		}

		boolean cached = false;

		for (int index = 0; index < 12; index++) {
			int appearanceId = equipment[index];

			if (appearanceId >= 256 && appearanceId < 512 && !IdentityKit.lookup(appearanceId - 256).headLoaded()) {
				cached = true;
			}

			if (appearanceId >= 512 && !ItemDefinition.lookup(appearanceId - 512).isDialogueModelCached(myGender)) {
				cached = true;
			}
		}

		if (cached) {
			return null;
		}

		Mesh headModels[] = new Mesh[12];

		int headModelsOffset  = 0;

		for (int modelIndex  = 0; modelIndex  < 12; modelIndex ++) {
			int appearanceId  = equipment[modelIndex ];

			if (appearanceId  >= 256 && appearanceId  < 512) {

				Mesh subModel  = IdentityKit.lookup(appearanceId  - 256).headModel();

				if (subModel  != null) {
					headModels[headModelsOffset ++] = subModel;
				}

			}
			if (appearanceId  >= 512) {
				Mesh subModel  = ItemDefinition.lookup(appearanceId  - 512).getEquippedModel(myGender);

				if (subModel  != null) {
					headModels[headModelsOffset ++] = subModel;
				}

			}
		}

		Mesh headModel = new Mesh(headModels,headModelsOffset);

		for (int index = 0; index < 5; index++)
			if (appearanceColors[index] != 0) {
				headModel.recolor((short) Client.APPEARANCE_COLORS[index][0], (short) Client.APPEARANCE_COLORS[index][appearanceColors[index]]);
				if (index == 1)
					headModel.recolor((short) Client.SHIRT_SECONDARY_COLORS[0], (short) Client.SHIRT_SECONDARY_COLORS[appearanceColors[index]]);
			}



		return headModel;
	}


	public Model getAnimatedModel() {
		SequenceDefinition sequenceDefinition = super.sequence != -1 && super.sequenceDelay == 0 ? SequenceDefinition.get(super.sequence) : null;
		SequenceDefinition walkSequenceDefinition = (super.movementSequence == -1 || (super.movementSequence == super.idleSequence && sequenceDefinition != null)) ? null : SequenceDefinition.get(super.movementSequence);
		return getAnimatedModel(sequenceDefinition, super.sequenceFrame ,walkSequenceDefinition,super.movementFrame);
	}

	public Model getAnimatedModel(SequenceDefinition var1, int var2, SequenceDefinition var3, int var4) {
		if (npcDefinition != null) {
			Model model = npcDefinition.getAnimatedModel(var1, var2, var3, var4);
			return model;
		}

		long l = aLong1718;
		int k = -1;
		int i1 = -1;
		int j1 = -1;
		int k1 = -1;
		if (super.sequence >= 0 && super.sequenceDelay == 0) {
			SequenceDefinition animation = SequenceDefinition.get(super.sequence);
			k = animation.primaryFrameIds[super.sequenceFrame];
			if (super.movementSequence >= 0 && super.movementSequence != super.idleSequence)
				i1 = SequenceDefinition.get(super.movementSequence).primaryFrameIds[super.movementFrame];
			if (animation.leftHandItem >= 0) {
				j1 = animation.leftHandItem;
				l += j1 - equipment[5] << 40;
			}
			if (animation.rightHandItem >= 0) {
				k1 = animation.rightHandItem;
				l += k1 - equipment[3] << 48;
			}
		} else if (super.movementSequence >= 0)
			k = SequenceDefinition.get(super.movementSequence).primaryFrameIds[super.movementFrame];
		Model model_1 = (Model) mruNodes.get(l);
		if (model_1 == null) {
			boolean flag = false;
			for (int i2 = 0; i2 < 12; i2++) {
				int k2 = equipment[i2];
				if (k1 >= 0 && i2 == 3)
					k2 = k1;
				if (j1 >= 0 && i2 == 5)
					k2 = j1;
				if (k2 >= 256 && k2 < 512 && !IdentityKit.lookup(k2 - 256).bodyCached())
					flag = true;
				if (k2 >= 512 && !ItemDefinition.lookup(k2 - 512).isEquippedModelCached(myGender))
					flag = true;
			}

			if (flag) {
				if (aLong1697 != -1L)
					model_1 = (Model) mruNodes.get(aLong1697);
				if (model_1 == null)
					return null;
			}
		}
		if (model_1 == null) {
			Mesh aclass30_sub2_sub4_sub6s[] = new Mesh[12];
			int j2 = 0;
			for (int l2 = 0; l2 < 12; l2++) {
				int i3 = equipment[l2];
				if (k1 >= 0 && l2 == 3)
					i3 = k1;
				if (j1 >= 0 && l2 == 5)
					i3 = j1;
				if (i3 >= 256 && i3 < 512) {
					Mesh model_3 = IdentityKit.lookup(i3 - 256).getBody();
					if (model_3 != null)
						aclass30_sub2_sub4_sub6s[j2++] = model_3;
				}
				if (i3 >= 512) {
					Mesh model_4 = ItemDefinition.lookup(i3 - 512)
							.getEquippedModel(myGender);
					if (model_4 != null)
						aclass30_sub2_sub4_sub6s[j2++] = model_4;
				}
			}

			Mesh var20 = new Mesh(aclass30_sub2_sub4_sub6s,j2);
			for (int index = 0; index < 5; index++) {
				if (appearanceColors[index] != 0) {
					var20.recolor((short) Client.APPEARANCE_COLORS[index][0], (short) Client.APPEARANCE_COLORS[index][appearanceColors[index]]);
					if (index == 1)
						var20.recolor((short) Client.SHIRT_SECONDARY_COLORS[0], (short) Client.SHIRT_SECONDARY_COLORS[appearanceColors[index]]);

				}
			}

			model_1 = var20.toModel(64, 850, -30, -50, -30);
			mruNodes.put(model_1, l);
			aLong1697 = l;
		}
		Model animatedModel;
		if (var1 == null && var3 == null) {
			animatedModel = model_1.toSharedSequenceModel(true);
		} else if (var1 != null && var3 != null) {
			animatedModel = var1.applyTransformations(model_1, var2, var3, var4);
		} else if (var1 != null) {
			animatedModel = var1.transformActorModel(model_1, var2);
		} else {
			animatedModel = var3.transformActorModel(model_1, var4);
		}
		return animatedModel;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public int privelage;

	public boolean isAdminRights() {
		return hasRights(PlayerRights.ADMINISTRATOR)
				|| hasRights(PlayerRights.OWNER)
				|| hasRights(PlayerRights.GAME_DEVELOPER);
	}

	public boolean hasRightsOtherThan(PlayerRights playerRight) {
		return PlayerRights.hasRightsOtherThan(rights, playerRight);
	}

	public boolean hasRights(PlayerRights playerRights) {
		return PlayerRights.hasRights(rights, playerRights);
	}

	public boolean hasRightsLevel(int rightsId) {
		return PlayerRights.hasRightsLevel(rights, rightsId);
	}

	public boolean hasRightsBetween(int low, int high) {
		return PlayerRights.hasRightsBetween(rights, low, high);
	}

	public Player() {
		aLong1697 = -1L;
		isUnanimated = false;
		appearanceColors = new int[5];
		visible = false;
		equipment = new int[12];
	}

	public boolean inFlowerPokerArea() {
		int x = getAbsoluteX();
		int y = getAbsoluteY();
		return x >= 3109 && y >= 3504 && x <= 3121 && y <= 3515;
	}

	public boolean inFlowerPokerChatProximity() {
		int x = getAbsoluteX();
		int y = getAbsoluteY();
		return x >= 3106 && y >= 3502 && x <= 3123 && y <= 3517;
	}

	public boolean isHintArrowPointingAtPlayer(int index) {
		return index >= 0 && Client.instance.hintArrowType == 10 && Client.instance.getPlayerIndices()[index] == Client.instance.hintArrowPlayerIndex;
	}

	public PlayerRights[] getRights() {
		return rights;
	}

	public List<PlayerRights> getDisplayedRights() {
		return displayedRights;
	}
	
	public int getHealthState() {
		return healthState;
	}

	public PlayerRights[] rights = new PlayerRights[] {PlayerRights.PLAYER};
	private List<PlayerRights> displayedRights = new ArrayList<>();
	private long aLong1697;
	public NpcDefinition npcDefinition;
	boolean isUnanimated;
	public int[] appearanceColors;
	public int team;
	public int myGender;
	public String displayName;
	static EvictingDualNodeHashTable mruNodes = new EvictingDualNodeHashTable(260);
	public int combatLevel;
	public int headIcon;
	public int skullIcon;
	public int hintIcon;
	public int animationCycleStart;
	int animationCycleEnd;
	int height;
	boolean visible;
	int field1117;
	int tileHeight2;
	int field1123;
	Model playerModel;
	public int[] equipment;
	private long aLong1718;
	int minX;
	int minY;
	int maxX;
	int maxY;
	int skill;
	private int healthState;


	@Override
	public String getOverheadText() {
		return "";
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
	public int[] getPathX() {
		return pathX;
	}

	@Override
	public int[] getPathY() {
		return pathY;
	}

	@Override
	public int getAnimation() {
		return sequence;
	}

	@Override
	public void setAnimation(int animation) {
		sequence = animation;
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
	public int getLogicalHeight() {
		return height;
	}

	@Override
	public int getOrientation() {
		return orientation;
	}

	@Override
	public int getCurrentOrientation() {
		return orientation;
	}

	@Override
	public RSModel getModel() {
		return getRotatedModel();
	}

	@Override
	public RSNode getNext() {
		return null;
	}

	@Override
	public RSNode getPrevious() {
		return null;
	}

	@Override
	public String getName() {
		return displayName;
	}

	@Override
	public Actor getInteracting() {
		int index = interactingEntity;
		if (index == -1 || index == 65535)
		{
			return null;
		}
		Client client = Client.instance;

		if (index < 32768)
		{
			NPC[] npcs = client.getCachedNPCs();
			return npcs[index];
		}

		index -= 32768;
		Player[] players = client.players;
		return players[index];
	}

	@Override
	public WorldPoint getWorldLocation() {
		return WorldPoint.fromLocal(Client.instance,
				this.getPathX()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
				this.getPathY()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
				Client.instance.getPlane());
	}

	@Override
	public LocalPoint getLocalLocation() {
		return new LocalPoint(this.x, this.y);
	}

	@Override
	public Polygon getCanvasTilePoly() {
		return Perspective.getCanvasTilePoly(Client.instance, this.getLocalLocation());
	}

	@Nullable
	@Override
	public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
		return Perspective.getCanvasTextLocation(Client.instance, (Graphics2D) Client.instance.getGraphics(), getLocalLocation(), text, zOffset);
	}


	@Override
	public Point getCanvasImageLocation(BufferedImage image, int zOffset) {
		return Perspective.getCanvasImageLocation(Client.instance, getLocalLocation(), image, zOffset);
	}

	@Override
	public Point getMinimapLocation() {
		return Perspective.localToMinimap(Client.instance, getLocalLocation());
	}

	@Override
	public Shape getConvexHull() {
		RSModel model = getModel();
		if (model == null)
		{
			return null;
		}

		int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), Client.instance.getPlane());

		return model.getConvexHull(getX(), getY(), getOrientation(), tileHeight);
	}

	@Override
	public WorldArea getWorldArea() {
		return new WorldArea(getWorldLocation(), 1, 1);
	}

	@Override
	public boolean isDead() {
		return currentHealth <= 0;
	}

	@Override
	public boolean isMoving() {
		return false;
	}


	@Override
	public Polygon[] getPolygons()
	{
		RSModel model = getModel();

		if (model == null)
		{
			return null;
		}

		int[] x2d = new int[model.getVerticesCount()];
		int[] y2d = new int[model.getVerticesCount()];

		int localX = getX();
		int localY = getY();

		final int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(localX, localY), Client.instance.getPlane());

		Perspective.modelToCanvas(Client.instance, model.getVerticesCount(), localX, localY, tileHeight, getOrientation(), model.getVerticesX(), model.getVerticesZ(), model.getVerticesY(), x2d, y2d);
		ArrayList polys = new ArrayList(model.getFaceCount());

		int[] trianglesX = model.getFaceIndices1();
		int[] trianglesY = model.getFaceIndices2();
		int[] trianglesZ = model.getFaceIndices3();

		for (int triangle = 0; triangle < model.getFaceCount(); ++triangle)
		{
			int[] xx =
					{
							x2d[trianglesX[triangle]], x2d[trianglesY[triangle]], x2d[trianglesZ[triangle]]
					};

			int[] yy =
					{
							y2d[trianglesX[triangle]], y2d[trianglesY[triangle]], y2d[trianglesZ[triangle]]
					};

			polys.add(new Polygon(xx, yy, 3));
		}

		return (Polygon[]) polys.toArray(new Polygon[0]);
	}


	@Override
	public HeadIcon getOverheadIcon() {
		return null;
	}

	@Override
	public SkullIcon getSkullIcon() {
		return null;
	}

	@Override
	public RSUsername getRsName() {
		return null;
	}

	@Override
	public int getPlayerId() {
		return 0;
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public RSPlayerComposition getPlayerComposition() {
		return new RSPlayerComposition() {
			@Override
			public boolean isFemale() {
				return myGender == 1;
			}

			@Override
			public int[] getColors() {
				return appearanceColors;
			}

			@Override
			public int[] getEquipmentIds() {
				return equipment;
			}

			@Override
			public int getEquipmentId(KitType type) {
				return 0;
			}

			@Override
			public void setTransformedNpcId(int id) {

			}

			@Override
			public int getKitId(KitType type) {
				return equipment[type.getIndex()];
			}

			@Override
			public long getHash() {
				return 0;
			}

			@Override
			public void setHash() {
			}
		};
	}

	@Override
	public int getCombatLevel() {
		return combatLevel;
	}

	@Override
	public int getTotalLevel() {
		return 0;
	}

	@Override
	public int getTeam() {
		return team;
	}

	@Override
	public boolean isFriendsChatMember() {
		return false;
	}

	@Override
	public boolean isClanMember() {
		return false;
	}

	@Override
	public boolean isFriend() {
		return Client.instance.isFriended(displayName, true);
	}

	@Override
	public boolean isFriended() {
		return false;
	}

	@Override
	public int getRsOverheadIcon() {
		return headIcon;
	}

	@Override
	public int getRsSkullIcon() {
		return skullIcon;
	}

	@Override
	public int getRSSkillLevel() {
		return 0;
	}

	@Override
	public String[] getActions() {
		return null;
	}


}
