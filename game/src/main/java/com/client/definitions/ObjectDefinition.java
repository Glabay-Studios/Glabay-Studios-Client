package com.client.definitions;

import java.io.FileWriter;
import java.util.Arrays;

import com.client.Renderable;
import com.client.collection.EvictingDualNodeHashTable;
import com.client.collection.node.DualNode;
import com.client.collection.table.IterableNodeHashTable;
import com.client.definitions.anim.SequenceDefinition;
import com.client.entity.model.Mesh;
import com.client.entity.model.Model;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSBuffer;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSObjectComposition;
import org.apache.commons.lang3.StringUtils;

import com.client.Client;
import com.client.Buffer;

public final class ObjectDefinition extends DualNode implements RSObjectComposition {

	public boolean usePre220Sounds = false;


	public static EvictingDualNodeHashTable objectsCached = new EvictingDualNodeHashTable(4096);
	public static EvictingDualNodeHashTable cachedModelData = new EvictingDualNodeHashTable(500);
	public static EvictingDualNodeHashTable modelsCached = new EvictingDualNodeHashTable(30);

	public static Mesh[] meshData;

	static {
		meshData = new Mesh[4];
	}
	
	public static ObjectDefinition lookup(int objectID) {
		ObjectDefinition definition = (ObjectDefinition) objectsCached.get((long)objectID);
		if (definition == null) {
			byte[] data = Js5List.configs.takeFile(Js5ConfigType.OBJECT, objectID);
			definition = new ObjectDefinition();
			definition.setDefaults();
			definition.id = objectID;
			if (data != null) {
				definition.decode(new Buffer(data));
			}
			definition.postDecode();

			if (objectID >= 26281 && objectID <= 26290) {
				definition.actions = new String[] { "Choose", null, null, null, null };
			}

			switch (objectID) {
				case 36201: // Raids 1 lobby entrance
					definition.actions = new String[]{ "Enter", null, null, null, null};
					break;
				case 36062:
					definition.description = "Teleports you anywhere around Xeros.";
					definition.actions = new String[] { "Activate", "Previous Location", null, null, null };
					break;
				case 4152:
					definition.name = "Skilling Portal";
					definition.description = "Teleports you to various skilling areas.";
					definition.actions = new String[] { "Teleport", null, null, null, null };
					break;
				case 1206:
					definition.name = "Hespori Vines";
					definition.description = "The vines of Hespori.";
					definition.actions = new String[] { "Pick", null, null, null, null };
					break;
				case 33222:
					definition.name = "Burning Ore";
					definition.description = "I should try heating this up.";
					definition.actions = new String[] { "Mine", null, null, null, null };
					break;
				case 8880:
					definition.name = "Tool Box";
					definition.description = "Useful tools for resources in the area.";
					definition.actions = new String[] { "Take-tools", null, null, null, null };
					break;
				case 29771:
					definition.name = "Tools";
					definition.description = "Useful tools for resources in the area.";
					definition.actions = new String[] { null , null, null, null, null };
					break;
				case 33223:
					definition.name = "Enchanted stone";
					definition.description = "A fragile ancient stone.";
					definition.actions = new String[] { "Mine", null, null, null, null };
					break;

				case 33311:
					definition.name = "Fire";
					definition.description = "Looks very hot.";
					definition.actions = new String[] { "Burn-essence", "Burn-runes", null, null, null };
					break;
				case 12768:
					definition.name = "@gre@Nature Chest";
					definition.description = "Requires a Hespori key to open.";
					break;
				case 37743: // nightmare good flower
					definition.animation = 8617;
					break;
				case 37740: // nightmare bad flower
					definition.animation = 8623;
					break;
				case 37738: // nightmare spore spawn
					definition.animation = 8630;
					break;
				case 35914:
					definition.name = "Ahrim The Blighted";
					definition.actions = new String[] { "Awaken", null, null, null, null };
					break;
				case 9362:
					definition.name = "Dharok The Wretched";
					definition.actions = new String[] { "Awaken", null, null, null, null };
					break;
				case 14766:
					definition.name = "Verac The Defiled";
					definition.actions = new String[] { "Awaken", null, null, null, null };
					break;
				case 9360:
					definition.name = "Torag The Corrupted";
					definition.actions = new String[] { "Awaken", null, null, null, null };
					break;
				case 28723:
					definition.name = "Karil The Tainted";
					definition.actions = new String[] { "Awaken", null, null, null, null };
					break;
				case 31716:
					definition.name = "Guthan The Infested";
					definition.actions = new String[] { "Awaken", null, null, null, null };
					break;
				case 31622:
					definition.name = "Outlast Entrance";
					definition.actions = new String[] { "Enter", "Check Players", "Check Active", null, null };
					break;
				case 31624:
					definition.name = "@pur@Platinum Altar";
					break;
				case 29064:
					definition.name = "Xeros Leaderboards";
					definition.actions = new String[] { "View", null, null, null, null };
					break;
				case 33320:
					definition.name = "Fire of Exchange";
					definition.actions = new String[] { "Burn", "Burn Rates", null, null, null };
					break;
				case 33318:
					definition.name = "Fire of Destruction";
					definition.actions = new String[] { "Sacrifice", null, null, null, null };
					break;
				case 32508:
					definition.name = "Hunllef's Chest";
					definition.actions = new String[] { "Unlock", null, null, null, null };
					break;
				case 6097:
					definition.actions = new String[] { "Donate", null, null, null, null };
					break;
				case 14888:
					definition.name = "Jewelry Oven";
					break;
				case 29165:
					definition.name = "Coin Stack";
					definition.actions = new String[] { null, "Steal From", null, null, null };
					break;
				case 13681:
					definition.name = "Animal Cage";
					definition.actions = new String[] { null, null, null, null, null };
					break;
				case 30720:
					definition.name = "@red@Corrupt Chest";
					definition.actions = new String[] { "Open", null, null, null, null };
					break;
				case 34662:
					definition.actions = new String[] { "Open", "Teleport", null, null, null };
					break;
				case 12202:
					definition.actions = new String[] { "Dig", null, null, null, null };
					break;
				case 30107:
					definition.name = "Raids Reward Chest";
					definition.actions = new String[] { "Open", null, null, null, null };
					break;
				case 36197:
					definition.name = "Home Teleport";

					break;
				case 10562:
					definition.actions = new String[] { "Open", null, null, null, null };
					break;
				case 8207:
					definition.actions = new String[] { "Care-To", null, null, null, null };
					definition.name = "Herb Patch";
					break;
				case 8720:
					definition.name = "Vote shop";
					break;
				case 8210:
					definition.actions = new String[] { "Rake", null, null, null, null };
					definition.name = "Herb Patch";
					break;
				case 29150:
					definition.actions = new String[] { "Venerate", null, null, null, null };
					break;
				case 6764:
					definition.name = null;
					definition.actions = new String[] { null, null, null, null, null };
					break;
				case 8139:
				case 8140:
				case 8141:
				case 8142:
					definition.actions = new String[] { "Inspect", null, null, null, null };
					definition.name = "Herbs";
					break;
				case 2341:
					definition.actions = new String[] { null, null, null, null, null };
					break;
				case 14217:
					definition.actions = new String[5];
					break;
				case 3840:
					definition.actions = new String[5];
					definition.actions[0] = "Fill";
					definition.actions[1] = "Empty-From";
					definition.name = "Compost Bin";
					break;
				case 172:
					definition.name = "Ckey chest";
					break;
				case 31925:
					definition.name = "Max Island";
					definition.actions = new String[] { "Tele to", null, null, null, null };
					break;
				case 2996:
					definition.name = "Vote Chest";
					definition.actions = new String[] { "Unlock", null, null, null, null };
					break;

				case 12309:
					definition.actions = new String[5];
					definition.actions[0] = "Bank";
					definition.actions[1] = "Buy gloves";
					definition.actions[2] = null;
					definition.name = "Chest";
					break;
				case 32572:
					definition.actions = new String[5];
					definition.actions[0] = "Bank";
					definition.name = "Group chest";
					break;
				case 1750:
					definition.modelIds = new int[] { 8131, };
					definition.name = "Willow";
					definition.sizeX = 2;
					definition.sizeY = 2;
					definition.ambient = 25;
					definition.actions = new String[] { "Chop down", null, null, null, null };
					definition.mapscene = 3;
					break;

				case 26782:
					definition.actions = new String[] { "Recharge", null, null, null, null };
					break;

				case 1751:
					definition.modelIds = new int[] { 8037, 8040, };
					definition.name = "Oak";
					definition.sizeX = 3;
					definition.sizeY = 3;
					definition.ambient = 25;
					definition.actions = new String[] { "Chop down", null, null, null, null };
					definition.mapscene = 1;
					break;

				case 7814:
					definition.actions = new String[] { "Teleport", null, null, null, null };
					break;

				case 8356:
					definition.actions = new String[] { "Teleport", "Mt. Quidamortem", null, null, null };
					break;

				case 28900:
					definition.actions = new String[] { "Teleport", "Recharge Crystals", null, null, null };
					break;
				case 26740:
					definition.name = "Player Outlast";
					definition.actions = new String[] { "Join", "Setup", null, null, null };
					break;

				case 28837:
					definition.actions = new String[] { "Set Destination", null, null, null, null };
					break;

				case 7811:
					definition.name = "District Supplies";
					definition.actions = new String[] { "Blood Money", "Free", "Quick-Sets", null, null };
					break;
				case 10061:
				case 10060:
					definition.name = "Trading Post";
					definition.actions = new String[] { "Bank", "Open", "Collect", null, null };
					break;
				case 13287:
					definition.name = "Storage chest (UIM)";
					definition.description = "A chest to store items for UIM.";
					break;
				case 1752:
					definition.modelIds = new int[] { 4146, };
					definition.name = "Hollow tree";
					definition.ambient = 25;
					definition.actions = new String[] { "Chop down", null, null, null, null };
					definition.recolorTo = new int[] { 13592, 10512, };
					definition.recolorFrom = new int[] { 7056, 6674, };
					definition.mapscene = 0;
					break;
				case 4873:
					definition.name = "Wilderness Lever";
					definition.sizeX = 3;
					definition.sizeY = 3;
					definition.ambient = 25;
					definition.actions = new String[] { "Enter Deep Wildy", null, null, null, null };
					definition.mapscene = 3;
					break;
				case 29735:
					definition.name = "Basic Slayer Dungeon";
					break;
				case 2544:
					definition.name = "Dagannoth Manhole";
					break;
				case 29345:
					definition.name = "Training Teleports Portal";
					definition.actions = new String[] { "Teleport", null, null, null, null };
					break;
				case 29346:
					definition.name = "Wilderness Teleports Portal";
					definition.actions = new String[] { "Teleport", null, null, null, null };
					break;
				case 29347:
					definition.name = "Boss Teleports Portal";
					definition.actions = new String[] { "Teleport", null, null, null, null };
					break;
				case 29349:
					definition.name = "Mini-Game Teleports Portal";
					definition.actions = new String[] { "Teleport", null, null, null, null };
					break;
				case 7127:
					definition.name = "Leaderboards";
					definition.actions = new String[] { "Open", "Wins", "Kills", "KDR", null };
					break;
				case 4155:
					definition.name = "Zul Andra Portal";
					break;
				case 2123:
					definition.name = "Mt. Quidamortem Slayer Dungeon";
					break;
				case 4150:
					definition.name = "Warriors Guild Mini-game Portal";
					break;
				case 11803:
					definition.name = "Donator Slayer Dungeon";
					break;
				case 4151:
					definition.name = "Barrows Mini-game Portal";
					break;
				case 1753:
					definition.modelIds = new int[] { 8157, };
					definition.name = "Yew";
					definition.sizeX = 3;
					definition.sizeY = 3;
					definition.ambient = 25;
					definition.actions = new String[] { "Chop down", null, null, null, null };
					definition.mapscene = 3;
					break;

				case 6943:
					definition.modelIds = new int[] { 1270, };
					definition.name = "Bank booth";
					definition.boolean1 = false;
					definition.ambient = 25;
					definition.contrast = 25;
					definition.actions = new String[] { null, "Bank", "Collect", null, null };
					break;

				case 25016:
				case 25017:
				case 25018:
				case 25029:
					definition.actions = new String[] { "Push-Through", null, null, null, null };
					break;

				case 19038:
					definition.actions = new String[] { null, null, null, null, null };
					definition.sizeX = 3;
					definition.sizeY = 3;
					definition.modelSizeY = 340; // Width
					definition.modelSizeX = 500; // Thickness
					definition.modelHeight = 400; // Height
					break;

				case 18826:
				case 18819:
				case 18818:
					definition.sizeX = 3;
					definition.sizeY = 3;
					definition.modelSizeY = 200; // Width
					definition.modelSizeX = 200; // Thickness
					definition.modelHeight = 100; // Height
					break;

				case 27777:
					definition.name = "Gangplank";
					definition.actions = new String[] { "Travel to CrabClaw Isle", null, null, null, null };
					definition.sizeX = 1;
					definition.sizeY = 1;
					definition.modelSizeY = 80; // Width
					definition.modelSizeX = 80; // Thickness
					definition.modelHeight = 250; // Height
					break;
				case 13641:
					definition.name = "Teleportation Device";
					definition.actions = new String[] { "Quick-Teleport", null, null, null, null };
					definition.sizeX = 1;
					definition.sizeY = 1;
					definition.modelSizeY = 80; // Width
					definition.modelSizeX = 80; // Thickness
					definition.modelHeight = 250; // Height
					break;

				case 29333:
					definition.name = "Trading post";
					definition.actions = new String[] { "Open", null, "Collect", null, null };
					definition.modelIds = new int[] { 60884 };
					definition.ambient = 25;
					definition.nonFlatShading = false;
					definition.description = "Buy and sell items with players here!";
					break;

				case 11700:
					definition.modelIds = new int[] { 4086 };
					definition.name = "Venom";
					definition.sizeX = 3;
					definition.sizeY = 3;
					definition.interactType = 0;
					definition.clipType = 1;
					definition.animation = 1261;
					definition.recolorFrom = new int[] { 31636 };
					definition.recolorTo = new int[] { 10543 };
					definition.modelSizeX = 160;
					definition.modelHeight = 160;
					definition.modelSizeY = 160;
					definition.actions = new String[5];
					// definition.description = new String(
					// "It's a cloud of venomous smoke that is extremely toxic.");
					break;

				case 11601: // 11601
					definition.retextureFrom = new short[] { 2 };
					definition.retextureTo = new short[] { 46 };
					break;
			}

			objectsCached.put(definition, objectID);
		}
		return definition;
	}

	public void postDecode() {
		if (int1 == -1) {
			int1 = 0;
			if (modelIds != null && (models == null || models[0] == 10)) {
				int1 = 1;
			}

			for (int index = 0; index < 5; index++) {
				if (this.actions[index] != null) {
					this.int1 = 1;
				}
			}
		}

		if (int3 == -1) {
			int3 = interactType != 0 ? 1 : 0;
		}
	}




	public static void dumpList() {
		try {
			FileWriter fw = new FileWriter("./temp/" + "object_data.json");
			fw.write("[\n");
			for (int i = 0; i < totalObjects; i++) {
				ObjectDefinition def = ObjectDefinition.lookup(i);
				String output = "[\"" + StringUtils.join(def.actions, "\", \"") + "\"],";

				String finalOutput = "	{\n" + "		\"id\": " + def.id + ",\n		" + "\"name\": \"" + def.name
						+ "\",\n		\"models\": " + Arrays.toString(def.modelIds) + ",\n		\"actions\": "
						+ output.replaceAll(", \"\"]", ", \"Examine\"]").replaceAll("\"\"", "null")
								.replace("[\"null\"]", "[null, null, null, null, \"Examine\"]")
								.replaceAll(", \"Remove\"", ", \"Remove\", \"Examine\"")
						+ "	\n		\"width\": " + def.modelSizeY + "\n	},";
				fw.write(finalOutput.replaceAll("\"name\": \"null\",", "\"name\": null,"));
				fw.write(System.getProperty("line.separator"));
			}
			fw.write("]");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setDefaults() {
		modelIds = null;
		models = null;
		name = null;
		description = null;
		recolorFrom = null;
		recolorTo = null;
		retextureTo = null;
		retextureFrom = null;
		sizeX = 1;
		sizeY = 1;
		boolean1 = true;
		int1 = -1;
		clipType = -1;
		nonFlatShading = false;
		modelClipped = false;
		boolean3 = true;
		animation = -1;
		int2 = 16;
		interactType = 2;
		ambient = 0;
		contrast = 0;
		actions = new String[5];
		minimapFunction = -1;
		mapscene = -1;
		isRotated = false;
		clipped = true;
		modelSizeX = 128;
		modelHeight = 128;
		modelSizeY = 128;
		surroundings = 0;
		offsetX = 0;
		offsetHeight = 0;
		offsetY = 0;
		boolean2 = false;
		isSolid = false;
		int3 = -1;
		varpID = -1;
		varbitID = -1;
		configs = null;
	}

	public static void nullLoader() {
		baseModels.clear();
		cachedModelData = null;
		modelsCached = null;
	}

	public static int totalObjects;


	public boolean modelTypeCached(int i) {
		if (models == null) {
			if (modelIds == null)
				return true;
			if (i != 10)
				return true;
			boolean flag1 = true;
			for (int k = 0; k < modelIds.length; k++)
				flag1 &= Js5List.models.tryLoadFile(modelIds[k] & 0xffff);

			return flag1;
		}
		for (int j = 0; j < models.length; j++)
			if (models[j] == i)
				return Js5List.models.tryLoadFile(modelIds[j] & 0xffff);

		return true;
	}


	public final com.client.entity.model.Model getModelDynamic(int var1, int var2, int[][] var3, int var4, int var5, int var6, SequenceDefinition var7, int var8) {
		long var9;
		if (this.models == null) {
			var9 = (long)(var2 + (this.id << 10));
		} else {
			var9 = (long)(var2 + (var1 << 3) + (this.id << 10));
		}

		com.client.entity.model.Model var11 = (com.client.entity.model.Model) modelsCached.get(var9);
		if (var11 == null) {
			Mesh var12 = this.getModelData(var1, var2);
			if (var12 == null) {
				return null;
			}

			var11 = var12.toModel(this.ambient + 64, this.contrast + 768, -50, -10, -50);
			modelsCached.put(var11, var9);
		}

		if (var7 == null && this.clipType * 65536 == -1) {
			return var11;
		} else {
			if (var7 != null) {
				var11 = var7.transformObjectModel(var11, var8, var2);
			} else {
				var11 = var11.toSharedSequenceModel(true);
			}

			if (this.clipType * 65536 >= 0) {
				var11 = var11.contourGround(var3, var4, var5, var6, false, this.clipType * 65536);
			}

			return var11;
		}
	}

	public Renderable getEntity(int var1, int var2, int[][] var3, int var4, int var5, int var6) {
		long var7;
		if (this.models == null) {
			var7 = (long) (var2 + (this.id << 10));
		} else {
			var7 = (long) (var2 + (var1 << 3) + (this.id << 10));
		}

		Object var9 = (Renderable) cachedModelData.get(var7);
		if (var9 == null) {
			Mesh var10 = this.getModelData(var1, var2);
			if (var10 == null) {
				return null;
			}

			if (!this.nonFlatShading) {
				var9 = var10.toModel(this.ambient + 64, this.contrast + 768, -50, -10, -50);
			} else {
				var10.ambient = (short)(this.ambient + 64);
				var10.contrast = (short)(this.contrast + 768);
				var10.calculateVertexNormals();
				var9 = var10;
			}

			cachedModelData.put((DualNode)var9, var7);
		}

		if (this.nonFlatShading) {
			var9 = ((Mesh)var9).copyModelData();
		}

		if (this.clipType * 65536 >= 0) {
			if (var9 instanceof Model) {
				var9 = ((Model)var9).contourGround(var3, var4, var5, var6, true, this.clipType * 65536);
			} else if (var9 instanceof Mesh) {
				var9 = ((Mesh)var9).method4239(var3, var4, var5, var6, true, this.clipType * 65536);
			}
		}

		return (Renderable) var9;
	}

	public final Model getModel(int var1, int var2, int[][] var3, int var4, int var5, int var6) {
		long var7;
		if (this.models == null) {
			var7 = (long)(var2 + (this.id << 10));
		} else {
			var7 = (long)(var2 + (var1 << 3) + (this.id << 10));
		}

		Model var9 = (Model) cachedModelData.get(var7);
		if (var9 == null) {
			Mesh var10 = this.getModelData(var1, var2);
			if (var10 == null) {
				return null;
			}

			var9 = var10.toModel(this.ambient + 64, this.contrast + 768, -50, -10, -50);
			cachedModelData.put(var9, var7);
		}

		if (this.clipType * 65536 >= 0) {
			var9 = var9.contourGround(var3, var4, var5, var6, true, this.clipType * 65536);
		}

		return var9;
	}

	Mesh getModelData(int var1, int var2) {
		Mesh var3 = null;
		boolean var4;
		int var5;
		int var7;
		if (this.models == null) {
			if (var1 != 10) {
				return null;
			}

			if (this.modelIds == null) {
				return null;
			}

			var4 = this.isRotated;
			if (var1 == 2 && var2 > 3) {
				var4 = !var4;
			}

			var5 = this.modelIds.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				var7 = this.modelIds[var6];
				if (var4) {
					var7 += 65536;
				}

				var3 = (Mesh) cachedModelData.get((long)var7);
				if (var3 == null) {
					var3 = Mesh.getModel(var7 & 65535);
					if (var3 == null) {
						return null;
					}

					if (var4) {
						var3.method4306();
					}

					cachedModelData.put(var3, (long)var7);
				}

				if (var5 > 1) {
					meshData[var6] = var3;
				}
			}

			if (var5 > 1) {
				var3 = new Mesh(meshData, var5);
			}
		} else {
			int var9 = -1;

			for (var5 = 0; var5 < this.models.length; ++var5) {
				if (this.models[var5] == var1) {
					var9 = var5;
					break;
				}
			}

			if (var9 == -1) {
				return null;
			}

			var5 = this.modelIds[var9];
			boolean var10 = this.isRotated ^ var2 > 3;
			if (var10) {
				var5 += 65536;
			}

			var3 = (Mesh) cachedModelData.get((long)var5);
			if (var3 == null) {
				var3 = Mesh.getModel(var5 & 65535);
				if (var3 == null) {
					return null;
				}

				if (var10) {
					var3.method4306();
				}

				cachedModelData.put(var3, (long)var5);
			}
		}

		if (this.modelSizeX == 128 && this.modelHeight == 128 && this.modelSizeY == 128) {
			var4 = false;
		} else {
			var4 = true;
		}

		boolean var11;
		if (this.offsetX == 0 && this.offsetHeight == 0 && this.offsetY == 0) {
			var11 = false;
		} else {
			var11 = true;
		}

		Mesh var8 = new Mesh(var3, var2 == 0 && !var4 && !var11, this.recolorFrom == null, null == this.retextureFrom, true);
		if (var1 == 4 && var2 > 3) {
			var8.method4244(256);
			var8.changeOffset(45, 0, -45);
		}

		var2 &= 3;
		if (var2 == 1) {
			var8.method4281();
		} else if (var2 == 2) {
			var8.method4242();
		} else if (var2 == 3) {
			var8.method4243();
		}

		if (this.recolorFrom != null) {
			for (var7 = 0; var7 < this.recolorFrom.length; ++var7) {
				var8.recolor((short) this.recolorFrom[var7], (short) this.recolorTo[var7]);
			}
		}

		if (this.retextureFrom != null) {
			for (var7 = 0; var7 < this.retextureFrom.length; ++var7) {
				var8.retexture(this.retextureFrom[var7], this.retextureTo[var7]);
			}
		}

		if (var4) {
			var8.resize(this.modelSizeX, this.modelHeight, this.modelSizeY);
		}

		if (var11) {
			var8.changeOffset(this.offsetX, this.offsetHeight, this.offsetY);
		}

		return var8;
	}


	public final boolean needsModelFiles() {
		if (this.modelIds == null) {
			return true;
		} else {
			boolean var1 = true;

			for (int var2 = 0; var2 < this.modelIds.length; ++var2) {
				var1 &= Js5List.models.tryLoadFile(this.modelIds[var2] & 65535, 0);
			}

			return var1;
		}
	}


	public ObjectDefinition method580() {
		int i = -1;
		if (varpID != -1) {
			VariableBits varBit = VariableBits.lookup(varpID);
			int j = varBit.baseVar;
			int k = varBit.startBit;
			int l = varBit.endBit;
			int i1 = Client.anIntArray1232[l - k];
			i = clientInstance.variousSettings[j] >> k & i1;
		} else if (varbitID != -1)
			i = clientInstance.variousSettings[varbitID];
		int var3;
		if (i >= 0 && i < configs.length)
			var3 = configs[i];
		else
			var3 = configs[configs.length - 1];
		return var3 == -1 ? null : lookup(var3);
	}

	void decode(Buffer buffer) {
		while(true) {
			int var2 = buffer.readUnsignedByte();
			if (var2 == 0) {
				return;
			}

			this.decodeNext(buffer, var2);
		}
	}

	void processOp(Buffer buffer, int opcode) {
		int var3;
		int var4;
		if (opcode == 1) {
			var3 = buffer.readUnsignedByte();
			if (var3 > 0) {
				if (this.modelIds != null && !lowMem) {
					buffer.currentPosition += var3 * 3;
				} else {
					this.models = new int[var3];
					this.modelIds = new int[var3];

					for(var4 = 0; var4 < var3; ++var4) {
						this.modelIds[var4] = buffer.readUShort();
						this.models[var4] = buffer.readUnsignedByte();
					}
				}
			}
		} else if (opcode == 2) {
			this.name = buffer.readStringCp1252NullTerminated();
		} else if (opcode == 5) {
			var3 = buffer.readUnsignedByte();
			if (var3 > 0) {
				if (this.modelIds != null && !lowMem) {
					buffer.currentPosition += var3 * 2;
				} else {
					this.models = null;
					this.modelIds = new int[var3];

					for(var4 = 0; var4 < var3; ++var4) {
						this.modelIds[var4] = buffer.readUShort();
					}
				}
			}
		} else if (opcode == 14) {
			this.sizeX = buffer.readUnsignedByte();
		} else if (opcode == 15) {
			this.sizeY = buffer.readUnsignedByte();
		} else if (opcode == 17) {
			this.interactType = 0;
			this.boolean1 = false;
		} else if (opcode == 18) {
			this.boolean1 = false;
		} else if (opcode == 19) {
			int1 = buffer.readUnsignedByte();
		} else if (opcode == 21) {
			this.clipType = 0;
		} else if (opcode == 22) {
			this.nonFlatShading = true;
		} else if (opcode == 23) {
			this.modelClipped = true;
		} else if (opcode == 24) {
			this.animation = buffer.readUShort();
			if (this.animation == 65535) {
				this.animation = -1;
			}
		} else if (opcode == 27) {
			this.interactType = 1;
		} else if (opcode == 28) {
			this.int2 = buffer.readUnsignedByte();
		} else if (opcode == 29) {
			this.ambient = buffer.readSignedByte();
		} else if (opcode == 39) {
			this.contrast = buffer.readSignedByte() * 25;
		} else if (opcode >= 30 && opcode < 35) {
			this.actions[opcode - 30] = buffer.readStringCp1252NullTerminated();
			if (this.actions[opcode - 30].equalsIgnoreCase("Hidden")) {
				this.actions[opcode - 30] = null;
			}
		} else if (opcode == 40) {
			var3 = buffer.readUnsignedByte();
			this.recolorFrom = new int[var3];
			this.recolorTo = new int[var3];

			for(var4 = 0; var4 < var3; ++var4) {
				this.recolorFrom[var4] = (short)buffer.readUShort();
				this.recolorTo[var4] = (short)buffer.readUShort();
			}
		} else if (opcode == 41) {
			var3 = buffer.readUnsignedByte();
			this.retextureFrom = new short[var3];
			this.retextureTo = new short[var3];

			for(var4 = 0; var4 < var3; ++var4) {
				this.retextureFrom[var4] = (short)buffer.readUShort();
				this.retextureTo[var4] = (short)buffer.readUShort();
			}
		} else if (opcode == 61) {
			buffer.readUShort();
		} else if (opcode == 62) {
			this.isRotated = true;
		} else if (opcode == 64) {
			this.clipped = false;
		} else if (opcode == 65) {
			this.modelSizeX = buffer.readUShort();
		} else if (opcode == 66) {
			this.modelHeight = buffer.readUShort();
		} else if (opcode == 67) {
			this.modelSizeY = buffer.readUShort();
		} else if (opcode == 68) {
			this.mapscene = buffer.readUShort();
		} else if (opcode == 69) {
			surroundings = buffer.readUnsignedByte();
		} else if (opcode == 70) {
			this.offsetX = buffer.readShort();
		} else if (opcode == 71) {
			this.offsetHeight = buffer.readShort();
		} else if (opcode == 72) {
			this.offsetY = buffer.readShort();
		} else if (opcode == 73) {
			this.boolean2 = true;
		} else if (opcode == 74) {
			this.isSolid = true;
		} else if (opcode == 75) {
			this.int3 = buffer.readUnsignedByte();
		} else if (opcode != 77 && opcode != 92) {
			if (opcode == 78) {
				this.soundId = buffer.readUShort();
				this.soundRange = buffer.readUnsignedByte();
				this.soundRetain = usePre220Sounds ? 0: buffer.readUnsignedByte();

			} else if (opcode == 79) {
				this.soundMin = buffer.readUShort();
				this.soundMax = buffer.readUShort();
				this.soundRange = buffer.readUnsignedByte();
				this.soundRetain = usePre220Sounds ? 0: buffer.readUnsignedByte();

				var3 = buffer.readUnsignedByte();
				this.soundEffectIds = new int[var3];

				for(var4 = 0; var4 < var3; ++var4) {
					this.soundEffectIds[var4] = buffer.readUShort();
				}
			} else if (opcode == 81) {
				this.clipType = buffer.readUnsignedByte() * 256;
			} else if (opcode == 82) {
				this.minimapFunction = buffer.readUShort();
			} else if (opcode == 89) {
				this.boolean3 = false;
			} else if (opcode == 249) {
				this.params = Buffer.readStringIntParameters(buffer, this.params);
			}
		} else {
			this.varbitID = buffer.readUShort();
			if (this.varbitID == 65535) {
				this.varbitID = -1;
			}

			this.varpID = buffer.readUShort();
			if (this.varpID == 65535) {
				this.varpID = -1;
			}

			var3 = -1;
			if (opcode == 92) {
				var3 = buffer.readUShort();
				if (var3 == 65535) {
					var3 = -1;
				}
			}

			var4 = buffer.readUnsignedByte();
			this.configs = new int[var4 + 2];

			for(int var5 = 0; var5 <= var4; ++var5) {
				this.configs[var5] = buffer.readUShort();
				if (this.configs[var5] == 65535) {
					this.configs[var5] = -1;
				}
			}

			this.configs[var4 + 1] = var3;
		}

	}

	public int soundRange = 0;


	private ObjectDefinition() {
		id = -1;
	}

	private short[] retextureFrom;
	private short[] retextureTo;
	public boolean boolean2;
	@SuppressWarnings("unused")
	private int contrast;
	@SuppressWarnings("unused")
	private int ambient;
	private int offsetX;
	public String name;
	private int modelSizeY;

	public int sizeX;
	private int offsetHeight;
	public int minimapFunction;
	private int[] recolorTo;
	private int modelSizeX;
	public int varbitID;
	private boolean isRotated;
	public static boolean lowMem;

	public int id;

	public boolean boolean1;
	public int mapscene;
	public int configs[];
	public int int3;
	public int sizeY;
	public int clipType;
	public boolean modelClipped;
	public static Client clientInstance;
	private boolean isSolid;
	public int interactType;
	public int surroundings;
	private boolean nonFlatShading;

	private int modelHeight;
	public int[] modelIds;
	public int soundId = -1;
	public int soundDistance = 0;
	public int soundMin = 0;
	public int soundRetain = 0;
	public int soundMax = 0;
	public int[] soundEffectIds;


	public IterableNodeHashTable params = null;

	public boolean boolean3;
	public int varpID;
	public int int2;
	private int[] models;
	public String description;
	public int int1;
	public boolean clipped;

	public int animation;

	private int offsetY;
	private int[] recolorFrom;
	public static EvictingDualNodeHashTable baseModels = new EvictingDualNodeHashTable(500);
	public String actions[] = new String[5];

	@Override
	public int getAccessBitMask() {
		return 0;
	}

	@Override
	public int getVarbitId() {
		return varbitID;
	}

	@Override
	public int getVarPlayerId() {
		return varpID;
	}

	@Override
	public int getIntValue(int paramID) {
		return 0;
	}

	@Override
	public void setValue(int paramID, int value) {

	}

	@Override
	public String getStringValue(int paramID) {
		return null;
	}

	@Override
	public void setValue(int paramID, String value) {

	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public String[] getActions() {
		return new String[0];
	}

	@Override
	public int getMapSceneId() {
		return 0;
	}

	@Override
	public int getMapIconId() {
		return 0;
	}

	@Override
	public int[] getImpostorIds() {
		return configs;
	}

	@Override
	public RSObjectComposition getImpostor() {
		return method580();
	}

	@Override
	public RSIterableNodeHashTable getParams() {
		return null;
	}

	@Override
	public void setParams(IterableHashTable params) {

	}

	@Override
	public void setParams(RSIterableNodeHashTable params) {

	}

	@Override
	public void decodeNext(RSBuffer buffer, int opcode) {
		this.processOp((Buffer) buffer,opcode);
	}

	@Override
	public int[] getModelIds() {
		return new int[0];
	}

	@Override
	public void setModelIds(int[] modelIds) {

	}

	@Override
	public int[] getModels() {
		return new int[0];
	}

	@Override
	public void setModels(int[] models) {

	}

	@Override
	public boolean getObjectDefinitionIsLowDetail() {
		return false;
	}

	@Override
	public int getSizeX() {
		return 0;
	}

	@Override
	public void setSizeX(int sizeX) {

	}

	@Override
	public int getSizeY() {
		return 0;
	}

	@Override
	public void setSizeY(int sizeY) {

	}

	@Override
	public int getInteractType() {
		return 0;
	}

	@Override
	public void setInteractType(int interactType) {

	}

	@Override
	public boolean getBoolean1() {
		return false;
	}

	@Override
	public void setBoolean1(boolean boolean1) {

	}

	@Override
	public int getInt1() {
		return 0;
	}

	@Override
	public void setInt1(int int1) {

	}

	@Override
	public int getInt2() {
		return 0;
	}

	@Override
	public void setInt2(int int2) {

	}

	@Override
	public int getClipType() {
		return 0;
	}

	@Override
	public void setClipType(int clipType) {

	}

	@Override
	public boolean getNonFlatShading() {
		return false;
	}

	@Override
	public void setNonFlatShading(boolean nonFlatShading) {

	}

	@Override
	public void setModelClipped(boolean modelClipped) {

	}

	@Override
	public boolean getModelClipped() {
		return false;
	}

	@Override
	public int getAnimationId() {
		return 0;
	}

	@Override
	public void setAnimationId(int animationId) {

	}

	@Override
	public int getAmbient() {
		return 0;
	}

	@Override
	public void setAmbient(int ambient) {

	}

	@Override
	public int getContrast() {
		return 0;
	}

	@Override
	public void setContrast(int contrast) {

	}

	@Override
	public short[] getRecolorFrom() {
		return new short[0];
	}

	@Override
	public void setRecolorFrom(short[] recolorFrom) {

	}

	@Override
	public short[] getRecolorTo() {
		return new short[0];
	}

	@Override
	public void setRecolorTo(short[] recolorTo) {

	}

	@Override
	public short[] getRetextureFrom() {
		return new short[0];
	}

	@Override
	public void setRetextureFrom(short[] retextureFrom) {

	}

	@Override
	public short[] getRetextureTo() {
		return new short[0];
	}

	@Override
	public void setRetextureTo(short[] retextureTo) {

	}

	@Override
	public void setIsRotated(boolean rotated) {

	}

	@Override
	public boolean getIsRotated() {
		return false;
	}

	@Override
	public void setClipped(boolean clipped) {

	}

	@Override
	public boolean getClipped() {
		return false;
	}

	@Override
	public void setMapSceneId(int mapSceneId) {

	}

	@Override
	public void setModelSizeX(int modelSizeX) {

	}

	@Override
	public int getModelSizeX() {
		return 0;
	}

	@Override
	public void setModelHeight(int modelHeight) {

	}

	@Override
	public void setModelSizeY(int modelSizeY) {

	}

	@Override
	public void setOffsetX(int modelSizeY) {

	}

	@Override
	public void setOffsetHeight(int offsetHeight) {

	}

	@Override
	public void setOffsetY(int offsetY) {

	}

	@Override
	public void setInt3(int int3) {

	}

	@Override
	public void setInt5(int int5) {

	}

	@Override
	public void setInt6(int int6) {

	}

	@Override
	public void setInt7(int int7) {

	}

	@Override
	public void setBoolean2(boolean boolean2) {

	}

	@Override
	public void setIsSolid(boolean isSolid) {

	}

	@Override
	public void setAmbientSoundId(int ambientSoundId) {

	}

	@Override
	public void setSoundEffectIds(int[] soundEffectIds) {

	}

	@Override
	public int[] getSoundEffectIds() {
		return new int[0];
	}

	@Override
	public void setMapIconId(int mapIconId) {

	}

	@Override
	public void setBoolean3(boolean boolean3) {

	}

	@Override
	public void setTransformVarbit(int transformVarbit) {

	}

	@Override
	public int getTransformVarbit() {
		return 0;
	}

	@Override
	public void setTransformVarp(int transformVarp) {

	}

	@Override
	public int getTransformVarp() {
		return 0;
	}

	@Override
	public void setTransforms(int[] transforms) {

	}

	@Override
	public int[] getTransforms() {
		return configs;
	}

	public boolean hasSound() {
		if (this.configs == null) {
			return this.soundId != -1 || this.soundEffectIds != null;
		} else {
			for (int transform : this.configs) {
				if (transform != -1) {
					ObjectDefinition var2 = lookup(transform);
					if (var2.soundId != -1 || var2.soundEffectIds != null) {
						return true;
					}
				}
			}
			return false;
		}
	}

}