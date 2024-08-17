package com.client.scene;

import com.client.collection.node.NodeDeque;
import com.client.draw.Rasterizer3D;
import com.client.Client;
import com.client.Renderable;
import com.client.entity.model.Mesh;
import com.client.entity.model.Model;
import com.client.draw.rasterizer.Clips;
import com.client.engine.impl.MouseHandler;
import com.client.entity.model.ViewportMouse;
import com.client.scene.object.GroundDecoration;
import com.client.scene.object.tile.SimpleTile;
import com.client.scene.object.tile.Tile;
import com.client.scene.object.InteractiveObject;
import com.client.scene.object.Wall;
import com.client.scene.object.WallDecoration;
import com.client.scene.object.tile.ShapedTile;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.rs.api.*;

import java.util.List;
import java.util.*;

import static net.runelite.api.Constants.*;

public final class SceneGraph implements RSScene {
    public static int viewDistance = 9;

    public static boolean pitchRelaxEnabled;
    public static boolean hdMinimapEnabled = false;

    public static int skyboxColor;
    public static int[] tmpX = new int[6];
    public static int[] tmpY = new int[6];
    public static int roofRemovalMode = 0;
    public static final Set<RSTile> tilesToRemove = new HashSet<RSTile>();
    private int Scene_selectedPlane;

    public SceneGraph(int[][][] map) {
        int y = 104;// was parameter
        int x = 104;// was parameter
        int z = 4;// was parameter
        //aBoolean434 = true;
        gameObjectsCache = new InteractiveObject[5000];
        merge_a_normals = new int[10000];
        merge_b_normals = new int[10000];
        maxY = z;
        maxX = x;
        maxZ = y;
        tileArray = new Tile[z][x][y];
        field2020 = new int[z][x + 1][y + 1];
        tileHeights = map;
        this.clear();
    }

    public static void release() {
        interactive_obj = null;
        Scene_planeOccluderCounts = null;
        Scene_planeOccluders = null;
        tile_list = null;
        visibilityMap = null;
        renderArea = null;
    }

    public void clear() {
        int var1;
        int var2;
        for (var1 = 0; var1 < this.maxY; ++var1) {
            for (var2 = 0; var2 < maxX; ++var2) {
                for (int var3 = 0; var3 < maxZ; ++var3) {
                    this.tileArray[var1][var2][var3] = null;
                }
            }
        }

        for (var1 = 0; var1 < Scene_planesCount; ++var1) {
            for (var2 = 0; var2 < Scene_planeOccluderCounts[var1]; ++var2) {
                Scene_planeOccluders[var1][var2] = null;
            }

            Scene_planeOccluderCounts[var1] = 0;
        }

        for (var1 = 0; var1 < this.interactive_obj_cache_current_pos; ++var1) {
            this.gameObjectsCache[var1] = null;
        }

        this.interactive_obj_cache_current_pos = 0;

        for (var1 = 0; var1 < interactive_obj.length; ++var1) {
            interactive_obj[var1] = null;
        }

    }

    public void init(int var1) {
        this.minLevel = var1;

        for (int var2 = 0; var2 < maxX; ++var2) {
            for (int var3 = 0; var3 < maxY; ++var3) {
                if (this.tileArray[var1][var2][var3] == null) {
                    this.tileArray[var1][var2][var3] = new Tile(var1, var2, var3);
                }
            }
        }
        if (Client.instance.drawCallbacks != null) {
            Client.instance.drawCallbacks.loadScene(this);
            Client.instance.drawCallbacks.swapScene(this);
        }
    }

    public void setTileMinPlane(int var1, int var2, int var3, int var4) {
        Tile var5 = this.tileArray[var1][var2][var3];
        if (var5 != null) {
            this.tileArray[var1][var2][var3].logicHeight = var4;
        }
    }

    public void setLinkBelow(int var1, int var2) {
        Tile var3 = tileArray[0][var1][var2];

        for (int var4 = 0; var4 < 3; ++var4) {
            Tile var5 = tileArray[var4][var1][var2] = tileArray[var4 + 1][var1][var2];
            if (var5 != null) {
                --var5.z1AnInt1307;

                for (int var6 = 0; var6 < var5.gameObjectIndex; ++var6) {
                    InteractiveObject var7 = var5.gameObjects[var6];
                    long var9 = var7.uid;
                    boolean var8 = method1246(var9) == 2;
                    if (var8 && var7.startX == var1 && var2 == var7.endY) {
                        --var7.zLoc;
                    }
                }
            }
        }

        if (tileArray[0][var1][var2] == null) {
            tileArray[0][var1][var2] = new Tile(0, var1, var2);
        }

        tileArray[0][var1][var2].firstFloorTile = var3;
        tileArray[3][var1][var2] = null;
    }

    static int method1246(long var0) {
        return (int) (var0 >>> 14 & 3L);
    }

    public static void Scene_addOccluder(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        SceneCluster var8 = new SceneCluster();
        var8.tile_x = var2 / 128;
        var8.tile_width = var3 / 128;
        var8.tile_y = var4 / 128;
        var8.tile_height = var5 / 128;
        var8.search_mask = var1;
        var8.world_x = var2;
        var8.world_width = var3;
        var8.world_y = var4;
        var8.world_height = var5;
        var8.world_z = var6;
        var8.world_depth = var7;
        Scene_planeOccluders[var0][Scene_planeOccluderCounts[var0]++] = var8;
    }

    public void set_visible_planes(int plane, int x, int z, int height) {
        Tile tile = tileArray[plane][x][z];
        if (tile != null) {
            tileArray[plane][x][z].logicHeight = height;
        }
    }


    public void add_tile(int plane, int x, int y, int shape, int rotation, int texture_id, int v_sw, int v_se, int v_ne, int v_nw, int hsl_sw, int hsl_se,
                         int hsl_ne, int hsl_nw, int l_sw, int l_se, int l_ne, int l_nw, int color_id, int minimap_color) {
        if (shape == 0) {
            SimpleTile simple = new SimpleTile(hsl_sw, hsl_se, hsl_ne, hsl_nw, -1, color_id, false);
            for (int z = plane; z >= 0; z--)
                if (tileArray[z][x][y] == null)
                    tileArray[z][x][y] = new Tile(z, x, y);

            tileArray[plane][x][y].mySimpleTile = simple;
        } else if (shape != 1) {
            ShapedTile complex = new ShapedTile(y, l_sw, hsl_nw, v_ne, texture_id, l_ne, rotation, hsl_sw, color_id, hsl_ne, v_nw, v_se, v_sw, shape, l_nw, l_se, hsl_se, x, minimap_color);
            for (int z = plane; z >= 0; z--)
                if (tileArray[z][x][y] == null)
                    tileArray[z][x][y] = new Tile(z, x, y);
            tileArray[plane][x][y].myShapedTile = complex;
        } else {
            SimpleTile simple = new SimpleTile(l_sw, l_se, l_ne, l_nw, texture_id, minimap_color, v_sw == v_se && v_sw == v_ne && v_sw == v_nw);
            for (int z = plane; z >= 0; z--)
                if (tileArray[z][x][y] == null)
                    tileArray[z][x][y] = new Tile(z, x, y);

            tileArray[plane][x][y].mySimpleTile = simple;
        }
    }

    public void addTile(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20) {
        SimpleTile var21;
        int var22;
        if (var4 == 0) {
            var21 = new SimpleTile(var11, var12, var13, var14, -1, var19, false);

            for (var22 = var1; var22 >= 0; --var22) {
                if (this.tileArray[var22][var2][var3] == null) {
                    this.tileArray[var22][var2][var3] = new Tile(var22, var2, var3);
                }
            }

            this.tileArray[var1][var2][var3].mySimpleTile = var21;
        } else if (var4 != 1) {
            ShapedTile var23 = new ShapedTile(var4, var5, var6, var2, var3, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20);

            for (var22 = var1; var22 >= 0; --var22) {
                if (this.tileArray[var22][var2][var3] == null) {
                    this.tileArray[var22][var2][var3] = new Tile(var22, var2, var3);
                }
            }

            this.tileArray[var1][var2][var3].myShapedTile = var23;
        } else {
            var21 = new SimpleTile(var15, var16, var17, var18, var6, var20, var8 == var7 && var7 == var9 && var10 == var7);

            for (var22 = var1; var22 >= 0; --var22) {
                if (this.tileArray[var22][var2][var3] == null) {
                    this.tileArray[var22][var2][var3] = new Tile(var22, var2, var3);
                }
            }

            this.tileArray[var1][var2][var3].mySimpleTile = var21;
        }
    }

    public void add_ground_decor(int plane, int z, int y, Renderable ground, long uid, int x, int mask) {
        if (ground == null)
            return;

        GroundDecoration decor = new GroundDecoration();
        decor.node = ground;
        decor.world_x = x * 128 + 64;
        decor.world_y = y * 128 + 64;
        decor.world_z = z;
        decor.uid = uid;
        decor.mask = mask;
        if (tileArray[plane][x][y] == null)
            tileArray[plane][x][y] = new Tile(plane, x, y);

        tileArray[plane][x][y].groundDecoration = decor;
    }

    public void newFloorDecoration(int var1, int var2, int var3, int var4, Renderable var5, long var6, int var8) {
        if (var5 != null) {
            GroundDecoration var9 = new GroundDecoration();
            var9.node = var5;
            var9.world_x = var2 * 128 + 64;
            var9.world_y = var3 * 128 + 64;
            var9.world_z = var4;
            var9.uid = var6;
            var9.mask = var8;
            if (this.tileArray[var1][var2][var3] == null) {
                this.tileArray[var1][var2][var3] = new Tile(var1, var2, var3);
            }

            this.tileArray[var1][var2][var3].groundDecoration = var9;
        }
    }

    public void add_ground_item(int x, long uid, Renderable first, int plane, Renderable second, Renderable third, int z, int y) {
        ItemLayer itemLayer = new ItemLayer();
        itemLayer.first = first;
        itemLayer.x = x * 128 + 64;
        itemLayer.y = y * 128 + 64;
        itemLayer.z = plane;
        itemLayer.tag = uid;
        itemLayer.second = second;
        itemLayer.third = third;
        int var11 = 0;
        Tile var12 = this.tileArray[z][x][y];
        if (var12 != null) {
            for (int var13 = 0; var13 < var12.gameObjectIndex; ++var13) {
                if ((var12.gameObjects[var13].uid & 256) == 256 && var12.gameObjects[var13].renderable instanceof Model) {
                    Model var14 = (Model)var12.gameObjects[var13].renderable;
                    var14.calculateBoundsCylinder();
                    if (var14.model_height > var11) {
                        var11 = var14.model_height;
                    }
                }
            }
        }

        itemLayer.height = var11;
        if (this.tileArray[z][x][y] == null) {
            this.tileArray[z][x][y] = new Tile(z, x, y);
        }

        this.tileArray[z][x][y].itemLayer = itemLayer;
        this.tileArray[z][x][y].itemLayerChanged(0);
    }

    public void newBoundaryObject(int arg0, int arg1, int arg2, int arg3, Renderable arg4, Renderable arg5, int arg6, int arg7, long arg8, int arg9) {
        if (arg4 != null || arg5 != null) {
            Wall var12 = new Wall();
            var12.uid = arg8;
            var12.mask = arg9;
            var12.world_x = arg1 * 128 + 64;
            var12.world_y = arg2 * 128 + 64;
            var12.plane = arg3;
            var12.wall = arg4;
            var12.corner = arg5;
            var12.wall_orientation = arg6;
            var12.corner_orientation = arg7;

            for (int var13 = arg0; var13 >= 0; --var13) {
                if (this.tileArray[var13][arg1][arg2] == null) {
                    this.tileArray[var13][arg1][arg2] = new Tile(var13, arg1, arg2);
                }
            }

            this.tileArray[arg0][arg1][arg2].wallObject = var12;
        }
    }

    public void add_wall(int wall_orientation, Renderable side, long uid, int y, int x, Renderable corner, int plane, int corner_orientation, int z, int config) {
        if (side == null && corner == null)
            return;

        Wall wall = new Wall();
        wall.uid = uid;
        wall.world_x = x * 128 + 64;
        wall.world_y = y * 128 + 64;
        wall.plane = plane;
        wall.wall = side;
        wall.corner = corner;
        wall.mask = config;
        wall.wall_orientation = wall_orientation;
        wall.corner_orientation = corner_orientation;
        for (int tile_z = z; tile_z >= 0; tile_z--)
            if (tileArray[tile_z][x][y] == null)
                tileArray[tile_z][x][y] = new Tile(tile_z, x, y);

        tileArray[z][x][y].wallObject = wall;
    }

    public void newWallDecoration(int arg0, int arg1, int arg2, int arg3, Renderable arg4, Renderable arg5, int orientation1, int orientation2, int arg8, int arg9, long arg10, int arg11) {
        if (arg4 != null) {
            WallDecoration var14 = new WallDecoration();
            var14.uid = arg10;
            var14.config = arg11;
            var14.world_x = arg1 * 128 + 64;
            var14.world_y = arg2 * 128 + 64;
            var14.plane = arg3;
            var14.node = arg4;
            var14.renderable2 = arg5;
            var14.orientation = orientation1;
            var14.orientation2 = orientation2;
            var14.xOffset = arg8;
            var14.yOffset = arg9;

            for (int var15 = arg0; var15 >= 0; --var15) {
                if (this.tileArray[var15][arg1][arg2] == null) {
                    this.tileArray[var15][arg1][arg2] = new Tile(var15, arg1, arg2);
                }
            }

            this.tileArray[arg0][arg1][arg2].wallDecoration = var14;
        }
    }

    public long getGameObjectTag(int var1, int var2, int var3) {
        Tile var4 = this.tileArray[var1][var2][var3];
        if (var4 == null) {
            return 0L;
        } else {
            for (int var5 = 0; var5 < var4.gameObjectIndex; ++var5) {
                InteractiveObject var6 = var4.gameObjects[var5];
                if (ViewportMouse.method1417(var6.uid) && var2 == var6.startX && var3 == var6.endY) {
                    return var6.uid;
                }
            }

            return 0L;
        }
    }

    static boolean method715(long var0) {
        return method1306(var0) == 2;
    }

    public static int method1306(long var0) {
        return (int) (var0 >>> 14 & 3L);
    }

    public void addWallDecoration(long uid, int y, int orientation, int z, int x_offset, int plane, Renderable object, int x, int y_offset, int config, int mask) {
        if (object == null)
            return;

        WallDecoration decor = new WallDecoration();
        decor.uid = uid;
        decor.world_x = x * 128 + 64 + x_offset;
        decor.world_y = y * 128 + 64 + y_offset;
        decor.plane = plane;
        decor.node = object;
        decor.config = config;
        decor.mask = mask;
        decor.orientation = orientation;
        for (int tile_z = z; tile_z >= 0; tile_z--)
            if (tileArray[tile_z][x][y] == null)
                tileArray[tile_z][x][y] = new Tile(tile_z, x, y);

        tileArray[z][x][y].wallDecoration = decor;
    }

    public boolean method4166(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, Renderable arg6, int arg7, long arg8, int arg9) {
        if (arg6 == null) {
            return true;
        } else {
            int var12 = arg1 * 128 + 64 * arg4;
            int var13 = arg2 * 128 + 64 * arg5;
            return this.newGameObject(arg0, arg1, arg2, arg4, arg5, var12, var13, arg3, arg6, arg7, false, arg8, arg9);
        }
    }

    public boolean add_entity(long uid, int world_z, int height_offset, Renderable entity, int width_offset, int plane, int orientation, int y, int x, int config) {
        if (entity == null) {
            return true;
        } else {
            int world_x = x * 128 + 64 * width_offset;
            int world_y = y * 128 + 64 * height_offset;
            return add_entity(plane, x, y, width_offset, height_offset, world_x, world_y, world_z, entity, orientation, false, uid, config);
        }
    }

    public boolean method1288(int var1, int var2, int var3, int var4, int var5, int var6, Renderable var7, int var8, long var9, int var11) {
        if (var7 == null) {
            return true;
        } else {
            int var12 = var5 * 64 + var2 * 128;
            int var13 = var6 * 64 + var3 * 128;
            return this.newGameObject(var1, var2, var3, var5, var6, var12, var13, var4, var7, var8, false, var9, var11);
        }
    }

    boolean newGameObject(int level, int startx, int starty, int endx, int endy, int localX, int localY, int height, Renderable var9, int rotation, boolean var11, long uid, int config) {
        int y;
        for (int x = startx; x < startx + endx; ++x) {
            for (y = starty; y < starty + endy; ++y) {
                if (x < 0 || y < 0 || x >= this.maxX || y >= this.maxZ) {
                    return false;
                }

                Tile var17 = this.tileArray[level][x][y];
                if (var17 != null && var17.gameObjectIndex >= 5) {
                    return false;
                }
            }
        }

        InteractiveObject var21 = new InteractiveObject();
        var21.uid = uid;
        var21.mask = config;
        var21.zLoc = level;
        var21.xPos = localX;
        var21.yPos = localY;
        var21.tileHeight = height;
        var21.renderable = var9;
        var21.orientation = rotation;
        var21.startX = startx;
        var21.endY = starty;
        var21.endX = startx + endx - 1;
        var21.startY = starty + endy - 1;

        for (y = startx; y < startx + endx; ++y) {
            for (int var22 = starty; var22 < starty + endy; ++var22) {
                int var18 = 0;
                if (y > startx) {
                    ++var18;
                }

                if (y < startx + endx - 1) {
                    var18 += 4;
                }

                if (var22 > starty) {
                    var18 += 8;
                }

                if (var22 < starty + endy - 1) {
                    var18 += 2;
                }

                for (int var19 = level; var19 >= 0; --var19) {
                    if (this.tileArray[var19][y][var22] == null) {
                        this.tileArray[var19][y][var22] = new Tile(var19, y, var22);
                    }
                }

                Tile var23 = this.tileArray[level][y][var22];
                var23.gameObjects[var23.gameObjectIndex] = var21;
                var23.tiledObjectMasks[var23.gameObjectIndex] = var18;
                var23.totalTiledObjectMask |= var18;
                ++var23.gameObjectIndex;
            }
        }

        if (var11) {
            this.gameObjectsCache[this.interactive_obj_cache_current_pos++] = var21;
        }

        return true;
    }

    public boolean add_entity(int plane, int yaw, int z, long uid, int y, int size, int x, Renderable entity, boolean rotate) {
        if (entity == null)
            return true;
        if (!Client.instance.addEntityMarker(x, y, entity)) {
            return true;
        }
        int width = x - size;
        int height = y - size;
        int tile_width = x + size;
        int tile_height = y + size;
        if (rotate) {
            if (yaw > 640 && yaw < 1408)
                tile_height += 128;

            if (yaw > 1152 && yaw < 1920)
                tile_width += 128;

            if (yaw > 1664 || yaw < 384)
                height -= 128;

            if (yaw > 128 && yaw < 896)
                width -= 128;

        }
        width /= 128;
        height /= 128;
        tile_width /= 128;
        tile_height /= 128;
        return newGameObject(plane, width, height, (tile_width - width) + 1, (tile_height - height) + 1, x, y, z, entity, yaw, true, uid, (byte) 0);
    }

    public boolean add_transformed_entity(int plane, int y, Renderable entity, int orientation, int height_offset, int x, int z, int width, int width_offset,
                                          long uid, int height) {

        return entity == null || add_entity(plane, width, height, (width_offset - width) + 1, (height_offset - height) + 1, x, y, z,
                entity, orientation, true, uid, (byte) 0);
    }

    private boolean add_entity(int plane, int width, int height, int width_offset, int height_offset, int world_x, int world_y, int world_z, Renderable entity,
                               int orientation, boolean cached, long uid, int mask) {
        if (!Client.instance.addEntityMarker(world_x, world_y, entity)) {
            return true;
        }
        for (int x = width; x < width + width_offset; x++) {
            for (int y = height; y < height + height_offset; y++) {
                if (x < 0 || y < 0 || x >= maxX || y >= maxZ)
                    return false;

                Tile tile = tileArray[plane][x][y];
                if (tile != null && tile.gameObjectIndex >= 5)
                    return false;

            }
        }
        InteractiveObject object = new InteractiveObject();
        object.uid = uid;
        object.zLoc = plane;
        object.mask = mask;
        object.xPos = world_x;
        object.yPos = world_y;
        object.tileHeight = world_z;
        object.renderable = entity;
        object.orientation = orientation;
        object.startX = width;
        object.endY = height;
        object.endX = (width + width_offset) - 1;
        object.startY = (height + height_offset) - 1;
        for (int x = width; x < width + width_offset; x++) {
            for (int y = height; y < height + height_offset; y++) {
                int size = 0;
                if (x > width)
                    size++;

                if (x < (width + width_offset) - 1)
                    size += 4;

                if (y > height)
                    size += 8;

                if (y < (height + height_offset) - 1)
                    size += 2;

                for (int z = plane; z >= 0; z--)
                    if (tileArray[z][x][y] == null)
                        tileArray[z][x][y] = new Tile(z, x, y);

                Tile tile = tileArray[plane][x][y];
                tile.gameObjects[tile.gameObjectIndex] = object;
                tile.tiledObjectMasks[tile.gameObjectIndex] = size;
                tile.totalTiledObjectMask |= size;
                tile.gameObjectIndex++;
            }
        }
        if (cached)
            gameObjectsCache[interactive_obj_cache_current_pos++] = object;

        return true;
    }

    public void reset_interactive_obj() {
        for (int index = 0; index < interactive_obj_cache_current_pos; index++) {
            InteractiveObject obj = gameObjectsCache[index];
            remove_object(obj);
            gameObjectsCache[index] = null;
        }
        interactive_obj_cache_current_pos = 0;
    }

    private void remove_object(InteractiveObject obj) {
        for (int width = obj.startX; width <= obj.endX; width++) {
            for (int height = obj.endY; height <= obj.startY; height++) {
                Tile tile = tileArray[obj.zLoc][width][height];
                if (tile != null) {
                    for (int active = 0; active < tile.gameObjectIndex; active++) {
                        if (tile.gameObjects[active] != obj)
                            continue;

                        tile.gameObjectIndex--;
                        for (int index = active; index < tile.gameObjectIndex; index++) {
                            tile.gameObjects[index] = tile.gameObjects[index + 1];
                            tile.tiledObjectMasks[index] = tile.tiledObjectMasks[index + 1];
                        }
                        tile.gameObjects[tile.gameObjectIndex] = null;
                        break;
                    }
                    tile.totalTiledObjectMask = 0;
                    for (int index = 0; index < tile.gameObjectIndex; index++)
                        tile.totalTiledObjectMask |= tile.tiledObjectMasks[index];

                }
            }
        }
    }

    public void offset_wall_decor(int y, int offset, int x, int plane) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null)
            return;

        WallDecoration decor = tile.wallDecoration;
        if (decor != null) {
            int abs_x = x * 128 + 64;
            int abs_y = y * 128 + 64;
            decor.world_x = abs_x + ((decor.world_x - abs_x) * offset) / 16;
            decor.world_y = abs_y + ((decor.world_y - abs_y) * offset) / 16;
        }
    }

    public void method4138(int arg0, int arg1, int arg2, int arg3) {
        Tile var5 = this.tileArray[arg0][arg1][arg2];
        if (var5 != null) {
            WallDecoration var6 = var5.wallDecoration;
            if (var6 != null) {
                var6.world_x = var6.world_x * arg3 / 16;
                var6.world_y = var6.world_y * arg3 / 16;
            }
        }
    }

    public void remove_wall(int x, int plane, int y) {
        Tile tile = tileArray[plane][x][y];
        if (tile != null) {
            tile.wallObject = null;
        }
    }

    public void remove_wall_decor(int y, int plane, int x) {
        Tile tile = tileArray[plane][x][y];
        if (tile != null) {
            tile.wallDecoration = null;
        }
    }

    public void remove_object(int plane, int x, int y) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null)
            return;

        for (int index = 0; index < tile.gameObjectIndex; index++) {
            InteractiveObject object = tile.gameObjects[index];
            if (ViewportMouse.method1417(object.uid) && object.startX == x && object.endY == y) {
                remove_object(object);
                return;
            }
        }
    }

    public void remove_ground_decor(int plane, int y, int x) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null)
            return;

        tile.groundDecoration = null;
    }

    public void removeGroundItemPile(int plane, int x, int y) {
        Tile tile = tileArray[plane][x][y];
        if (tile != null) {
            tile.itemLayer = null;
            tile.itemLayerChanged(0);
        }
    }

    public Wall get_wall(int plane, int x, int y) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null)
            return null;
        else
            return tile.wallObject;
    }

    public WallDecoration get_wall_decor(int x, int y, int plane) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null)
            return null;
        else
            return tile.wallDecoration;
    }

    public InteractiveObject get_interactive_object(int x, int y, int plane) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null)
            return null;

        for (int index = 0; index < tile.gameObjectIndex; index++) {
            InteractiveObject object = tile.gameObjects[index];
            if (ViewportMouse.method1417(object.uid) && object.startX == x && object.endY == y)
                return object;
        }
        return null;
    }

    public GroundDecoration get_ground_decor(int y, int x, int plane) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null || tile.groundDecoration == null)
            return null;
        else
            return tile.groundDecoration;
    }

    public long getWallDecorationTag(int arg0, int arg1, int arg2) {
        Tile var4 = this.tileArray[arg0][arg1][arg2];
        return var4 != null && var4.wallDecoration != null ? var4.wallDecoration.uid : 0L;
    }

    public long get_interactive_object_uid(int plane, int x, int y) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null) {
            return 0L;
        }
        for (int index = 0; index < tile.gameObjectIndex; index++) {
            InteractiveObject object = tile.gameObjects[index];
            if (ViewportMouse.method1417(object.uid) && object.startX == x && object.endY == y) {
                return object.uid;
            }
        }
        return 0L;
    }

    public long getFloorDecorationTag(int plane, int x, int y) {
        Tile tile = tileArray[plane][x][y];
        if (tile == null || tile.groundDecoration == null) {
            return 0;
        }
        return tile.groundDecoration.uid;
    }

    public void method4153(int arg0, int arg1, int arg2) {
        for (int var4 = 0; var4 < this.maxY; ++var4) {
            for (int var5 = 0; var5 < maxX; ++var5) {
                for (int var6 = 0; var6 < maxZ; ++var6) {
                    Tile var7 = this.tileArray[var4][var5][var6];
                    if (var7 != null) {
                        Wall var8 = var7.wallObject;
                        Mesh var10;
                        if (var8 != null && var8.wall instanceof Mesh) {
                            Mesh var9 = (Mesh) var8.wall;
                            this.method4285(var9, var4, var5, var6, 1, 1);
                            if (var8.corner instanceof Mesh) {
                                var10 = (Mesh) var8.corner;
                                this.method4285(var10, var4, var5, var6, 1, 1);
                                Mesh.method4253(var9, var10, 0, 0, 0, false);
                                var8.corner = var10.toModel(var10.ambient, var10.contrast, arg0, arg1, arg2);
                            }

                            var8.wall = var9.toModel(var9.ambient, var9.contrast, arg0, arg1, arg2);
                        }

                        try {
                            for (int var12 = 0; var12 < var7.gameObjectIndex; ++var12) {
                                InteractiveObject var14 = var7.gameObjects[var12];
                                if (var14 != null && var14.renderable instanceof Mesh) {
                                    Mesh var11 = (Mesh) var14.renderable;
                                    this.method4285(var11, var4, var5, var6, var14.endX - var14.startX + 1, var14.startY - var14.endY + 1);
                                    var14.renderable = var11.toModel(var11.ambient, var11.contrast, arg0, arg1, arg2);
                                }
                            }
                        } catch (ArithmeticException e) {
                            e.printStackTrace();
                        }

                        GroundDecoration var13 = var7.groundDecoration;
                        if (var13 != null && var13.node instanceof Mesh) {
                            var10 = (Mesh) var13.node;
                            this.method4249(var10, var4, var5, var6);
                            var13.node = var10.toModel(var10.ambient, var10.contrast, arg0, arg1, arg2);
                        }
                    }
                }
            }
        }
    }

    void method4249(Mesh arg0, int arg1, int arg2, int arg3) {
        Tile var5;
        Mesh var6;
        if (arg2 < this.maxX) {
            var5 = this.tileArray[arg1][arg2 + 1][arg3];
            if (var5 != null && var5.groundDecoration != null && var5.groundDecoration.node instanceof Mesh) {
                var6 = (Mesh) var5.groundDecoration.node;
                Mesh.method4253(arg0, var6, 128, 0, 0, true);
            }
        }

        if (arg3 < this.maxX) {
            var5 = this.tileArray[arg1][arg2][arg3 + 1];
            if (var5 != null && var5.groundDecoration != null && var5.groundDecoration.node instanceof Mesh) {
                var6 = (Mesh) var5.groundDecoration.node;
                Mesh.method4253(arg0, var6, 0, 0, 128, true);
            }
        }

        if (arg2 < this.maxX && arg3 < this.maxY) {
            var5 = this.tileArray[arg1][arg2 + 1][arg3 + 1];
            if (var5 != null && var5.groundDecoration != null && var5.groundDecoration.node instanceof Mesh) {
                var6 = (Mesh) var5.groundDecoration.node;
                Mesh.method4253(arg0, var6, 128, 0, 128, true);
            }
        }

        if (arg2 < this.maxX && arg3 > 0) {
            var5 = this.tileArray[arg1][arg2 + 1][arg3 - 1];
            if (var5 != null && var5.groundDecoration != null && var5.groundDecoration.node instanceof Mesh) {
                var6 = (Mesh) var5.groundDecoration.node;
                Mesh.method4253(arg0, var6, 128, 0, -128, true);
            }
        }
    }

    void method4285(Mesh arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
        boolean var7 = true;
        int var8 = arg2;
        int var9 = arg2 + arg4;
        int var10 = arg3 - 1;
        int var11 = arg3 + arg5;

        for (int var12 = arg1; var12 <= arg1 + 1; ++var12) {
            if (var12 != this.maxY) {
                for (int var13 = var8; var13 <= var9; ++var13) {
                    if (var13 >= 0 && var13 < this.maxX) {
                        for (int var14 = var10; var14 <= var11; ++var14) {
                            if (var14 >= 0 && var14 < this.maxZ && (!var7 || var13 >= var9 || var14 >= var11 || var14 < arg3 && var13 != arg2)) {
                                Tile var15 = this.tileArray[var12][var13][var14];
                                if (var15 != null) {
                                    int var16 = (this.tileHeights[var12][var13][var14] + this.tileHeights[var12][var13 + 1][var14] + this.tileHeights[var12][var13][var14 + 1] + this.tileHeights[var12][var13 + 1][var14 + 1]) / 4 - (this.tileHeights[arg1][arg2][arg3] + this.tileHeights[arg1][arg2 + 1][arg3] + this.tileHeights[arg1][arg2][arg3 + 1] + this.tileHeights[arg1][arg2 + 1][arg3 + 1]) / 4;
                                    Wall var17 = var15.wallObject;
                                    if (var17 != null) {
                                        Mesh var18;
                                        if (var17.wall instanceof Mesh) {
                                            var18 = (Mesh) var17.wall;
                                            Mesh.method4253(arg0, var18, (var13 - arg2) * 128 + (1 - arg4) * 64, var16, (var14 - arg3) * 128 + (1 - arg5) * 64, var7);
                                        }

                                        if (var17.corner instanceof Mesh) {
                                            var18 = (Mesh) var17.corner;
                                            Mesh.method4253(arg0, var18, (var13 - arg2) * 128 + (1 - arg4) * 64, var16, (var14 - arg3) * 128 + (1 - arg5) * 64, var7);
                                        }
                                    }

                                    for (int var23 = 0; var23 < var15.gameObjectIndex; ++var23) {
                                        InteractiveObject var19 = var15.gameObjects[var23];
                                        if (var19 != null && var19.renderable instanceof Mesh) {
                                            Mesh var20 = (Mesh) var19.renderable;
                                            int var21 = var19.endX - var19.startX + 1;
                                            int var22 = var19.startY - var19.endY + 1;
                                            Mesh.method4253(arg0, var20, (var19.startX - arg2) * 128 + (var21 - arg4) * 64, var16, (var19.endY - arg3) * 128 + (var22 - arg5) * 64, var7);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                --var8;
                var7 = false;
            }
        }

    }




    static int[] field1900 = new int[10000];
    static int[] field1888 = new int[10000];

    public void draw_minimap_tile(int[] pixels, int pixelOffset, int z, int x, int y) {
        if (!Client.instance.isHdMinimapEnabled()) {
            drawTileMinimapSD(pixels, pixelOffset, z, x, y);
            return;
        }
        RSTile tile = getTiles()[z][x][y];
        if (tile == null) {
            return;
        }
        SceneTilePaint sceneTilePaint = tile.getSceneTilePaint();
        if (sceneTilePaint != null) {
            int rgb = sceneTilePaint.getRBG();
            if (sceneTilePaint.getSwColor() != INVALID_HSL_COLOR) {
                // hue and saturation
                int hs = sceneTilePaint.getSwColor() & ~0x7F;
                // I know this looks dumb (and it probably is) but I don't feel like hunting down the problem
                int seLightness = sceneTilePaint.getNwColor() & 0x7F;
                int neLightness = sceneTilePaint.getNeColor() & 0x7F;
                int southDeltaLightness = (sceneTilePaint.getSwColor() & 0x7F) - seLightness;
                int northDeltaLightness = (sceneTilePaint.getSeColor() & 0x7F) - neLightness;
                seLightness <<= 2;
                neLightness <<= 2;
                for (int i = 0; i < 4; i++) {
                    if (sceneTilePaint.getTexture() == -1) {
                        pixels[pixelOffset] = Rasterizer3D.hslToRgb[hs | seLightness >> 2];
                        pixels[pixelOffset + 1] = Rasterizer3D.hslToRgb[hs | seLightness * 3 + neLightness >> 4];
                        pixels[pixelOffset + 2] = Rasterizer3D.hslToRgb[hs | seLightness + neLightness >> 3];
                        pixels[pixelOffset + 3] = Rasterizer3D.hslToRgb[hs | seLightness + neLightness * 3 >> 4];
                    } else {
                        int lig = 0xFF - ((seLightness >> 1) * (seLightness >> 1) >> 8);
                        pixels[pixelOffset] = ((rgb & 0xFF00FF) * lig & ~0xFF00FF) + ((rgb & 0xFF00) * lig & 0xFF0000) >> 8;
                        lig = 0xFF - ((seLightness * 3 + neLightness >> 3) * (seLightness * 3 + neLightness >> 3) >> 8);
                        pixels[pixelOffset + 1] = ((rgb & 0xFF00FF) * lig & ~0xFF00FF) + ((rgb & 0xFF00) * lig & 0xFF0000) >> 8;
                        lig = 0xFF - ((seLightness + neLightness >> 2) * (seLightness + neLightness >> 2) >> 8);
                        pixels[pixelOffset + 2] = ((rgb & 0xFF00FF) * lig & ~0xFF00FF) + ((rgb & 0xFF00) * lig & 0xFF0000) >> 8;
                        lig = 0xFF - ((seLightness + neLightness * 3 >> 3) * (seLightness + neLightness * 3 >> 3) >> 8);
                        pixels[pixelOffset + 3] = ((rgb & 0xFF00FF) * lig & ~0xFF00FF) + ((rgb & 0xFF00) * lig & 0xFF0000) >> 8;
                    }
                    seLightness += southDeltaLightness;
                    neLightness += northDeltaLightness;

                    pixelOffset += 512;
                }
            } else if (rgb != 0) {
                for (int i = 0; i < 4; i++) {
                    pixels[pixelOffset] = rgb;
                    pixels[pixelOffset + 1] = rgb;
                    pixels[pixelOffset + 2] = rgb;
                    pixels[pixelOffset + 3] = rgb;
                    pixelOffset += 512;
                }
            }
            return;
        }

        net.runelite.api.SceneTileModel sceneTileModel = tile.getSceneTileModel();
        if (sceneTileModel != null) {
            int shape = sceneTileModel.getShape();
            int rotation = sceneTileModel.getRotation();
            int overlayRgb = sceneTileModel.getModelOverlay();
            int underlayRgb = sceneTileModel.getModelUnderlay();
            int[] points = getTileShape2D()[shape];
            int[] indices = getTileRotation2D()[rotation];

            int shapeOffset = 0;

            if (sceneTileModel.getOverlaySwColor() != INVALID_HSL_COLOR) {
                // hue and saturation
                int hs = sceneTileModel.getOverlaySwColor() & ~0x7F;
                int seLightness = sceneTileModel.getOverlaySeColor() & 0x7F;
                int neLightness = sceneTileModel.getOverlayNeColor() & 0x7F;
                int southDeltaLightness = (sceneTileModel.getOverlaySwColor() & 0x7F) - seLightness;
                int northDeltaLightness = (sceneTileModel.getOverlayNwColor() & 0x7F) - neLightness;
                seLightness <<= 2;
                neLightness <<= 2;
                for (int i = 0; i < 4; i++) {
                    if (sceneTileModel.getTriangleTextureId() == null) {
                        if (points[indices[shapeOffset++]] != 0) {
                            pixels[pixelOffset] = Rasterizer3D.hslToRgb[hs | (seLightness >> 2)];
                        }
                        if (points[indices[shapeOffset++]] != 0) {
                            pixels[pixelOffset + 1] = Rasterizer3D.hslToRgb[hs | (seLightness * 3 + neLightness >> 4)];
                        }
                        if (points[indices[shapeOffset++]] != 0) {
                            pixels[pixelOffset + 2] = Rasterizer3D.hslToRgb[hs | (seLightness + neLightness >> 3)];
                        }
                        if (points[indices[shapeOffset++]] != 0) {
                            pixels[pixelOffset + 3] = Rasterizer3D.hslToRgb[hs | (seLightness + neLightness * 3 >> 4)];
                        }
                    } else {
                        if (points[indices[shapeOffset++]] != 0) {
                            int lig = 0xFF - ((seLightness >> 1) * (seLightness >> 1) >> 8);
                            pixels[pixelOffset] = ((overlayRgb & 0xFF00FF) * lig & ~0xFF00FF) +
                                    ((overlayRgb & 0xFF00) * lig & 0xFF0000) >> 8;
                        }
                        if (points[indices[shapeOffset++]] != 0) {
                            int lig = 0xFF - ((seLightness * 3 + neLightness >> 3) *
                                    (seLightness * 3 + neLightness >> 3) >> 8);
                            pixels[pixelOffset + 1] = ((overlayRgb & 0xFF00FF) * lig & ~0xFF00FF) +
                                    ((overlayRgb & 0xFF00) * lig & 0xFF0000) >> 8;
                        }
                        if (points[indices[shapeOffset++]] != 0) {
                            int lig = 0xFF - ((seLightness + neLightness >> 2) *
                                    (seLightness + neLightness >> 2) >> 8);
                            pixels[pixelOffset + 2] = ((overlayRgb & 0xFF00FF) * lig & ~0xFF00FF) +
                                    ((overlayRgb & 0xFF00) * lig & 0xFF0000) >> 8;
                        }
                        if (points[indices[shapeOffset++]] != 0) {
                            int lig = 0xFF - ((seLightness + neLightness * 3 >> 3) *
                                    (seLightness + neLightness * 3 >> 3) >> 8);
                            pixels[pixelOffset + 3] = ((overlayRgb & 0xFF00FF) * lig & ~0xFF00FF) +
                                    ((overlayRgb & 0xFF00) * lig & 0xFF0000) >> 8;
                        }
                    }
                    seLightness += southDeltaLightness;
                    neLightness += northDeltaLightness;

                    pixelOffset += 512;
                }
                if (underlayRgb != 0 && sceneTileModel.getUnderlaySwColor() != INVALID_HSL_COLOR) {
                    pixelOffset -= 512 << 2;
                    shapeOffset -= 16;
                    hs = sceneTileModel.getUnderlaySwColor() & ~0x7F;
                    seLightness = sceneTileModel.getUnderlaySeColor() & 0x7F;
                    neLightness = sceneTileModel.getUnderlayNeColor() & 0x7F;
                    southDeltaLightness = (sceneTileModel.getUnderlaySwColor() & 0x7F) - seLightness;
                    northDeltaLightness = (sceneTileModel.getUnderlayNwColor() & 0x7F) - neLightness;
                    seLightness <<= 2;
                    neLightness <<= 2;
                    for (int i = 0; i < 4; i++) {
                        if (points[indices[shapeOffset++]] == 0) {
                            pixels[pixelOffset] = Rasterizer3D.hslToRgb[hs | (seLightness >> 2)];
                        }
                        if (points[indices[shapeOffset++]] == 0) {
                            pixels[pixelOffset + 1] = Rasterizer3D.hslToRgb[hs | (seLightness * 3 + neLightness >> 4)];
                        }
                        if (points[indices[shapeOffset++]] == 0) {
                            pixels[pixelOffset + 2] = Rasterizer3D.hslToRgb[hs | (seLightness + neLightness >> 3)];
                        }
                        if (points[indices[shapeOffset++]] == 0) {
                            pixels[pixelOffset + 3] = Rasterizer3D.hslToRgb[hs | (seLightness + neLightness * 3 >> 4)];
                        }
                        seLightness += southDeltaLightness;
                        neLightness += northDeltaLightness;

                        pixelOffset += 512;
                    }
                }
            } else if (underlayRgb != 0) {
                for (int i = 0; i < 4; i++) {
                    pixels[pixelOffset] = points[indices[shapeOffset++]] != 0 ? overlayRgb : underlayRgb;
                    pixels[pixelOffset + 1] =
                            points[indices[shapeOffset++]] != 0 ? overlayRgb : underlayRgb;
                    pixels[pixelOffset + 2] =
                            points[indices[shapeOffset++]] != 0 ? overlayRgb : underlayRgb;
                    pixels[pixelOffset + 3] =
                            points[indices[shapeOffset++]] != 0 ? overlayRgb : underlayRgb;
                    pixelOffset += 512;
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (points[indices[shapeOffset++]] != 0) {
                        pixels[pixelOffset] = overlayRgb;
                    }
                    if (points[indices[shapeOffset++]] != 0) {
                        pixels[pixelOffset + 1] = overlayRgb;
                    }
                    if (points[indices[shapeOffset++]] != 0) {
                        pixels[pixelOffset + 2] = overlayRgb;
                    }
                    if (points[indices[shapeOffset++]] != 0) {
                        pixels[pixelOffset + 3] = overlayRgb;
                    }
                    pixelOffset += 512;
                }
            }
        }
    }

    public void drawTileMinimapSD(int pixels[], int drawIndex, int zLoc, int xLoc, int yLoc) {
        int leftOverWidth = 512;// was parameter
        Tile tile = tileArray[zLoc][xLoc][yLoc];
        if (tile == null)
            return;
        SimpleTile simpleTile = tile.mySimpleTile;
        if (simpleTile != null) {
            int tileRGB = simpleTile.getColourRGB();
            if (tileRGB == 0)
                return;
            for (int i = 0; i < 4; i++) {
                pixels[drawIndex] = tileRGB;
                pixels[drawIndex + 1] = tileRGB;
                pixels[drawIndex + 2] = tileRGB;
                pixels[drawIndex + 3] = tileRGB;
                drawIndex += leftOverWidth;
            }
            return;
        }
        ShapedTile shapedTile = tile.myShapedTile;

        if (shapedTile == null) {
            return;
        }

        int shape = shapedTile.shape;
        int rotation = shapedTile.rotation;
        int underlayRGB = shapedTile.colourRGB;
        int overlayRGB = shapedTile.colourRGBA;
        int shapePoints[] = tileVertices[shape];
        int shapePointIndices[] = tileVertexIndices[rotation];
        int shapePtr = 0;
        if (underlayRGB != 0) {
            for (int i = 0; i < 4; i++) {
                pixels[drawIndex] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
                pixels[drawIndex + 1] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
                pixels[drawIndex + 2] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
                pixels[drawIndex + 3] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
                drawIndex += leftOverWidth;
            }
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (shapePoints[shapePointIndices[shapePtr++]] != 0)
                pixels[drawIndex] = overlayRGB;
            if (shapePoints[shapePointIndices[shapePtr++]] != 0)
                pixels[drawIndex + 1] = overlayRGB;
            if (shapePoints[shapePointIndices[shapePtr++]] != 0)
                pixels[drawIndex + 2] = overlayRGB;
            if (shapePoints[shapePointIndices[shapePtr++]] != 0)
                pixels[drawIndex + 3] = overlayRGB;
            drawIndex += leftOverWidth;
        }
    }


    public static void buildVisiblityMap(int[] ai, int i, int var2, int viewportWidth, int viewportHeight) {
        xMin = 0;
        yMin = 0;
        SceneGraph.xMax = viewportWidth;
        SceneGraph.yMax = viewportHeight;
        viewportHalfWidth = viewportWidth / 2;
        viewportHalfHeight = viewportHeight / 2;
        boolean aflag[][][][] = new boolean[9][32][53][53];
        for (int zAngle = 128; zAngle <= 384; zAngle += 32) {
            for (int xyAngle = 0; xyAngle < 2048; xyAngle += 64) {
                pitchSineY = Model.SINE[zAngle];
                pitchCosineY = Model.COSINE[zAngle];
                yawSineX = Model.SINE[xyAngle];
                yawCosineX = Model.COSINE[xyAngle];
                int angularZSegment = (zAngle - 128) / 32;
                int angularXYSegment = xyAngle / 64;
                for (int xRelativeToCamera = -26; xRelativeToCamera <= 26; xRelativeToCamera++) {
                    for (int yRelativeToCamera = -26; yRelativeToCamera <= 26; yRelativeToCamera++) {
                        int xRelativeToCameraPos = xRelativeToCamera * 128;
                        int yRelativeToCameraPos = yRelativeToCamera * 128;
                        boolean flag2 = false;
                        for (int k4 = -i; k4 <= var2; k4 += 128) {
                            if (!method311(xRelativeToCameraPos, ai[angularZSegment] + k4, yRelativeToCameraPos))
                                continue;
                            flag2 = true;
                            break;
                        }
                        aflag[angularZSegment][angularXYSegment][xRelativeToCamera + 26][yRelativeToCamera + 26] = flag2;
                    }
                }
            }
        }

        for (int angularZSegment = 0; angularZSegment < 8; angularZSegment++) {
            for (int angularXYSegment = 0; angularXYSegment < 32; angularXYSegment++) {
                for (int xRelativeToCamera = -25; xRelativeToCamera < 25; xRelativeToCamera++) {
                    for (int yRelativeToCamera = -25; yRelativeToCamera < 25; yRelativeToCamera++) {
                        boolean flag1 = false;
                        label0:
                        for (int l3 = -1; l3 <= 1; l3++) {
                            for (int j4 = -1; j4 <= 1; j4++) {
                                if (aflag[angularZSegment][angularXYSegment][xRelativeToCamera + l3 + 26][yRelativeToCamera + j4 + 26])
                                    flag1 = true;
                                else if (aflag[angularZSegment][(angularXYSegment + 1) % 31][xRelativeToCamera + l3 + 26][yRelativeToCamera + j4 + 26])
                                    flag1 = true;
                                else if (aflag[angularZSegment + 1][angularXYSegment][xRelativeToCamera + l3 + 26][yRelativeToCamera + j4 + 26]) {
                                    flag1 = true;
                                } else {
                                    if (!aflag[angularZSegment + 1][(angularXYSegment + 1) % 31][xRelativeToCamera + l3 + 26][yRelativeToCamera + j4 + 26])
                                        continue;
                                    flag1 = true;
                                }
                                break label0;
                            }
                        }
                        visibilityMap[angularZSegment][angularXYSegment][xRelativeToCamera + 25][yRelativeToCamera + 25] = flag1;
                    }
                }
            }
        }
    }

    static boolean method311(int var0, int var1, int var2) {
        int var3 = var0 * yawCosineX + var2 * yawSineX >> 16;
        int var4 = var2 * yawCosineX - var0 * yawSineX >> 16;
        int j1 = var4 * pitchCosineY + pitchSineY * var1 >> 16;
        int var6 = pitchCosineY * var1 - var4 * pitchSineY >> 16;
        if (j1 >= 50 && j1 <= 3500) {
            int l1 = var3 * 128 / j1 + viewportHalfWidth;
            int i2 = var6 * 128 / j1 + viewportHalfHeight;
            return l1 >= xMin && l1 <= xMax && i2 >= yMin && i2 <= yMax;
        } else {
            return false;
        }
    }

    public static final int VIEW_DISTANCE = 3500;

    public void register_click(int y, int x, int z) {
        Scene_selectedPlane = z;
        clicked = true; //check click
        clickScreenX = x;
        clickScreenY = y;
        clickedTileX = -1;
        clickedTileY = -1;
    }

    private static final int INVALID_HSL_COLOR = 12345678;
    private static final int DEFAULT_DISTANCE = 25;
    private static final int PITCH_LOWER_LIMIT = 128;
    private static final int PITCH_UPPER_LIMIT = 383;

    public static final double JAG2RAD = 0.0030679615757712823D;

    /**
     * Renders the terrain.
     * The coordinates use the WorldCoordinate Axes but the modelWorld coordinates.
     *
     * @param cameraXPos The cameraViewpoint's X-coordinate.
     * @param cameraYPos The cameraViewpoint's Y-coordinate.
     * @param camAngleXY The cameraAngle in the XY-plain.
     * @param cameraZPos The cameraViewpoint's X-coordinate.
     * @param planeZ     The plain the camera's looking at.
     * @param camAngleZ  The cameraAngle on the Z-axis.
     */
    public void render(int cameraXPos, int cameraYPos, int camAngleXY, int cameraZPos, int planeZ, int camAngleZ) {

        final DrawCallbacks drawCallbacks = Client.instance.getDrawCallbacks();
        if (drawCallbacks != null) {
            Client.instance.getDrawCallbacks().drawScene(cameraXPos, cameraZPos, cameraYPos, camAngleZ * JAG2RAD, camAngleXY * JAG2RAD, planeZ);
        }

        final boolean isGpu = Client.instance.isGpu();
        final boolean checkClick = Client.instance.isCheckClick();
        final boolean menuOpen = Client.instance.isMenuOpen();

        if (!menuOpen && !checkClick) {
            Client.instance.getScene().menuOpen(Client.instance.getPlane(), Client.instance.getMouseX() - Client.instance.getViewportXOffset(), Client.instance.getMouseY() - Client.instance.getViewportYOffset(), false);
        }

        if (!isGpu && skyboxColor != 0) {
            Client.instance.rasterizerFillRectangle(
                    Client.instance.getViewportXOffset(),
                    Client.instance.getViewportYOffset(),
                    Client.instance.getViewportWidth(),
                    Client.instance.getViewportHeight(),
                    skyboxColor
            );
        }

        final int maxX = getMaxX();
        final int maxY = getMaxY();
        final int maxZ = getMaxZ();

        final int minLevel = getMinLevel();

        final RSTile[][][] tiles = getTiles();
        final int distance = isGpu ? drawDistance : DEFAULT_DISTANCE;

        if (cameraXPos < 0) {
            cameraXPos = 0;
        } else if (cameraXPos >= maxX * Perspective.LOCAL_TILE_SIZE) {
            cameraXPos = maxX * Perspective.LOCAL_TILE_SIZE - 1;
        }

        if (cameraYPos < 0) {
            cameraYPos = 0;
        } else if (cameraYPos >= maxZ * Perspective.LOCAL_TILE_SIZE) {
            cameraYPos = maxZ * Perspective.LOCAL_TILE_SIZE - 1;
        }


        // we store the uncapped pitch for setting camera angle for the pitch relaxer
        // we still have to cap the pitch in order to access the visibility map, though
        int realPitch = camAngleZ;
        if (camAngleZ < PITCH_LOWER_LIMIT) {
            camAngleZ = PITCH_LOWER_LIMIT;
        } else if (camAngleZ > PITCH_UPPER_LIMIT) {
            camAngleZ = PITCH_UPPER_LIMIT;
        }
        if (!pitchRelaxEnabled) {
            realPitch = camAngleZ;
        }


        Client.instance.setCycle(Client.instance.getCycle() + 1);
        Client.instance.setPitchSin(Perspective.SINE[realPitch]);
        Client.instance.setPitchCos(Perspective.COSINE[realPitch]);
        Client.instance.setYawSin(Perspective.SINE[camAngleXY]);
        Client.instance.setYawCos(Perspective.COSINE[camAngleXY]);


        final int[][][] tileHeights = Client.instance.getTileHeights();
        boolean[][] renderArea = Client.instance.getVisibilityMaps()[(camAngleZ - 128) / 32][camAngleXY / 64];
        Client.instance.setRenderArea(renderArea);

        Client.instance.setCameraX2(cameraXPos);
        Client.instance.setCameraY2(cameraZPos);
        Client.instance.setCameraZ2(cameraYPos);

        int screenCenterX = cameraXPos / Perspective.LOCAL_TILE_SIZE;
        int screenCenterZ = cameraYPos / Perspective.LOCAL_TILE_SIZE;

        Client.instance.setScreenCenterX(screenCenterX);
        Client.instance.setScreenCenterZ(screenCenterZ);
        Client.instance.setScenePlane(planeZ);

        int minTileX = screenCenterX - distance;
        if (minTileX < 0) {
            minTileX = 0;
        }

        int minTileZ = screenCenterZ - distance;
        if (minTileZ < 0) {
            minTileZ = 0;
        }

        int maxTileX = screenCenterX + distance;
        if (maxTileX > maxX) {
            maxTileX = maxX;
        }

        int maxTileZ = screenCenterZ + distance;
        if (maxTileZ > maxZ) {
            maxTileZ = maxZ;
        }

        Client.instance.setMinTileX(minTileX);
        Client.instance.setMinTileZ(minTileZ);
        Client.instance.setMaxTileX(maxTileX);
        Client.instance.setMaxTileZ(maxTileZ);

        updateOccluders();

        Client.instance.setTileUpdateCount(0);

        if (roofRemovalMode != 0) {
            tilesToRemove.clear();
            RSPlayer localPlayer = Client.instance.getLocalPlayer();
            if (localPlayer != null && (roofRemovalMode & ROOF_FLAG_POSITION) != 0) {
                LocalPoint localLocation = localPlayer.getLocalLocation();
                if (localLocation.isInScene()) {
                    tilesToRemove.add(tileArray[Client.instance.getPlane()][localLocation.getSceneX()][localLocation.getSceneY()]);
                }
            }

            if (hoverX >= 0 && hoverX < 104 && hoverY >= 0 && hoverY < 104 && (roofRemovalMode & ROOF_FLAG_HOVERED) != 0) {
                tilesToRemove.add(tileArray[Client.instance.getPlane()][hoverX][hoverY]);
            }

            LocalPoint localDestinationLocation = Client.instance.getLocalDestinationLocation();
            if (localDestinationLocation != null && localDestinationLocation.isInScene() && (roofRemovalMode & ROOF_FLAG_DESTINATION) != 0) {
                tilesToRemove.add(tileArray[Client.instance.getPlane()][localDestinationLocation.getSceneX()][localDestinationLocation.getSceneY()]);
            }

            if (Client.instance.getCameraPitch() < 310 && (roofRemovalMode & ROOF_FLAG_BETWEEN) != 0 && localPlayer != null) {
                int playerX = localPlayer.getX() >> 7;
                int playerY = localPlayer.getY() >> 7;
                int var29 = Client.instance.getCameraX() >> 7;
                int var30 = Client.instance.getCameraY() >> 7;
                if (playerX >= 0 && playerY >= 0 && var29 >= 0 && var30 >= 0 && playerX < 104 && playerY < 104 && var29 < 104 && var30 < 104) {
                    int var31 = Math.abs(playerX - var29);
                    int var32 = Integer.compare(playerX, var29);
                    int var33 = -Math.abs(playerY - var30);
                    int var34 = Integer.compare(playerY, var30);
                    int var35 = var31 + var33;

                    while (var29 != playerX || var30 != playerY) {
                        if (blocking(Client.instance.getPlane(), var29, var30)) {
                            tilesToRemove.add(tileArray[Client.instance.getPlane()][var29][var30]);
                        }

                        int var36 = 2 * var35;
                        if (var36 >= var33) {
                            var35 += var33;
                            var29 += var32;
                        } else {
                            var35 += var31;
                            var30 += var34;
                        }
                    }
                }
            }
        }

        if (!menuOpen) {
            hoverY = -1;
            hoverX = -1;
        }

        for (int z = minLevel; z < maxY; ++z) {
            RSTile[][] planeTiles = getTiles()[z];


            for (int x = minTileX; x < maxTileX; ++x) {
                for (int y = minTileZ; y < maxTileZ; ++y) {
                    RSTile tile = planeTiles[x][y];
                    if (tile != null) {

                        RSTile var30 = tileArray[Client.instance.getPlane()][x][y];
                        if (tile.getPhysicalLevel() > planeZ && roofRemovalMode == 0
                                || !isGpu && !renderArea[x - screenCenterX + DEFAULT_DISTANCE][y - screenCenterZ + DEFAULT_DISTANCE]
                                && tileHeights[z][x][y] - cameraYPos < 2000
                                || roofRemovalMode != 0 && Client.instance.getPlane() < tile.getPhysicalLevel()
                                && tilesToRemove.contains(var30)) {
                            tile.setDraw(false);
                            tile.setVisible(false);
                            tile.setWallCullDirection(0);
                        } else {
                            tile.setDraw(true);
                            tile.setVisible(true);
                            tile.setDrawEntities(true);
                            Client.instance.setTileUpdateCount(Client.instance.getTileUpdateCount() + 1);
                        }
                    }
                }
            }
        }

        for (int z = minLevel; z < maxY; ++z) {
            RSTile[][] planeTiles = tileArray[z];

            for (int x = -distance; x <= 0; ++x) {
                int var10 = x + screenCenterX;
                int var16 = screenCenterX - x;
                if (var10 >= minTileX || var16 < maxTileX) {
                    for (int y = -distance; y <= 0; ++y) {
                        int var13 = y + screenCenterZ;
                        int var14 = screenCenterZ - y;
                        if (var10 >= minTileX) {
                            if (var13 >= minTileZ) {
                                RSTile tile = planeTiles[var10][var13];
                                if (tile != null && tile.isDraw()) {
                                    draw(tile, true);
                                }
                            }

                            if (var14 < maxTileZ) {
                                RSTile tile = planeTiles[var10][var14];
                                if (tile != null && tile.isDraw()) {
                                    draw(tile, true);
                                }
                            }
                        }

                        if (var16 < maxTileX) {
                            if (var13 >= minTileZ) {
                                RSTile tile = planeTiles[var16][var13];
                                if (tile != null && tile.isDraw()) {
                                    draw(tile, true);
                                }
                            }

                            if (var14 < maxTileZ) {
                                RSTile tile = planeTiles[var16][var14];
                                if (tile != null && tile.isDraw()) {
                                    draw(tile, true);
                                }
                            }
                        }

                    }
                }
            }
        }

        if (!isGpu && (Client.instance.getOculusOrbState() != 0 && !Client.instance.getComplianceValue("orbInteraction"))) {
            Client.instance.setEntitiesAtMouseCount(0);
        }
        Client.instance.setCheckClick(false);
        Client.instance.getCallbacks().drawScene();
        if (Client.instance.getDrawCallbacks() != null) {
            Client.instance.getDrawCallbacks().postDrawScene();
        }
    }

    public static boolean blocking(int plane, int x, int y) {
        return (Client.instance.getTileSettings()[plane][x][y] & 4) != 0;
    }

    private void load(Tile loaded, boolean flag) {
        tile_list.insertBack(loaded);
        do {
            Tile ground;
            do {
                ground = (Tile) tile_list.pop();
                if (ground == null)
                    return;

            } while (!ground.aBoolean1323);

            int camera_x = ground.anInt1308;
            int camera_y = ground.anInt1309;
            int camera_z = ground.z1AnInt1307;
            int plane = ground.anInt1310;
            Tile tile_heights[][] = tileArray[camera_z];
            //seems to be object decoration related (sharing a tile with multiple objects)
            if (ground.aBoolean1322) {
                if (flag) {
                    if (camera_z > 0) {
                        Tile tile = tileArray[camera_z - 1][camera_x][camera_y];
                        if (tile != null && tile.aBoolean1323)
                            continue;

                    }
                    if (camera_x <= screenCenterX && camera_x > minTileX) {
                        Tile tile = tile_heights[camera_x - 1][camera_y];
                        if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (ground.totalTiledObjectMask & 1) == 0))
                            continue;
                    }
                    if (camera_x >= screenCenterX && camera_x < maxTileX - 1) {
                        Tile tile = tile_heights[camera_x + 1][camera_y];
                        if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (ground.totalTiledObjectMask & 4) == 0))
                            continue;
                    }
                    if (camera_y <= screenCenterZ && camera_y > minTileZ) {
                        Tile tile = tile_heights[camera_x][camera_y - 1];
                        if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (ground.totalTiledObjectMask & 8) == 0))
                            continue;
                    }
                    if (camera_y >= screenCenterZ && camera_y < maxTileZ - 1) {
                        Tile tile = tile_heights[camera_x][camera_y + 1];
                        if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (ground.totalTiledObjectMask & 2) == 0))
                            continue;
                    }
                } else {
                    flag = true;
                }
                ground.aBoolean1322 = false;
                if (ground.firstFloorTile != null) {
                    Tile sub = ground.firstFloorTile;
                    if (sub.mySimpleTile != null) {
                        if (!visibleTiles(0, camera_x, camera_y))
                            render_simple_tile(sub.mySimpleTile, 0, pitchSineY, pitchCosineY, yawSineX, yawCosineX, camera_x, camera_y);

                    } else if (sub.myShapedTile != null && !visibleTiles(0, camera_x, camera_y))
                        drawTileOverlay(camera_x, pitchSineY, yawSineX, sub.myShapedTile, pitchCosineY, camera_y, yawCosineX);

                    Wall wall = sub.wallObject;
                    if (wall != null)
                        wall.wall.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX,
                                wall.world_x - xCameraPos, wall.plane - zCameraPos, wall.world_y - yCameraPos,
                                wall.uid);

                    for (int index = 0; index < sub.gameObjectIndex; index++) {
                        InteractiveObject object = sub.gameObjects[index];
                        if (object != null)
                            object.renderable.renderAtPoint(object.orientation, pitchSineY, pitchCosineY, yawSineX,
                                    yawCosineX, object.xPos - xCameraPos, object.tileHeight - zCameraPos,
                                    object.yPos - yCameraPos, object.uid);

                    }
                }
                boolean enable_decor = false;
                if (ground.mySimpleTile != null) {
                    if (!visibleTiles(plane, camera_x, camera_y)) {
                        enable_decor = true;
                        if (ground.mySimpleTile.northEastColor != 12345678 || clicked && camera_z <= Scene_selectedPlane) {
                            render_simple_tile(ground.mySimpleTile, plane, pitchSineY, pitchCosineY, yawSineX, yawCosineX, camera_x, camera_y);
                        }
                    }
                } else if (ground.myShapedTile != null && !visibleTiles(plane, camera_x, camera_y)) {
                    enable_decor = true;
                    drawTileOverlay(camera_x, pitchSineY, yawSineX, ground.myShapedTile, pitchCosineY, camera_y, yawCosineX);
                }
                int cull_factor = 0;
                int camera_x_angle = 0;
                Wall wall = ground.wallObject;
                WallDecoration wall_decor = ground.wallDecoration;
                if (wall != null || wall_decor != null) {
                    if (screenCenterX == camera_x)
                        cull_factor++;
                    else if (screenCenterX < camera_x)
                        cull_factor += 2;
                    if (screenCenterZ == camera_y)
                        cull_factor += 3;
                    else if (screenCenterZ > camera_y)
                        cull_factor += 6;

                    camera_x_angle = wall_direction_x[cull_factor];
                    ground.anInt1328 = wall_direction_z[cull_factor];
                }
                if (wall != null) {
                    if ((wall.wall_orientation & wall_direction_y[cull_factor]) != 0) {
                        if (wall.wall_orientation == 16) {
                            ground.wallCullDirection = 3;
                            ground.anInt1326 = viewport_camera_left[cull_factor];
                            ground.anInt1327 = 3 - ground.anInt1326;
                        } else if (wall.wall_orientation == 32) {
                            ground.wallCullDirection = 6;
                            ground.anInt1326 = viewport_camera_right[cull_factor];
                            ground.anInt1327 = 6 - ground.anInt1326;
                        } else if (wall.wall_orientation == 64) {
                            ground.wallCullDirection = 12;
                            ground.anInt1326 = viewport_camera_top[cull_factor];
                            ground.anInt1327 = 12 - ground.anInt1326;
                        } else {
                            ground.wallCullDirection = 9;
                            ground.anInt1326 = viewport_camera_bottom[cull_factor];
                            ground.anInt1327 = 9 - ground.anInt1326;
                        }
                    } else {
                        ground.wallCullDirection = 0;
                    }
                    if ((wall.wall_orientation & camera_x_angle) != 0 && !wall_visible(plane, camera_x, camera_y, wall.wall_orientation))
                        wall.wall.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX,
                                wall.world_x - xCameraPos, wall.plane - zCameraPos,
                                wall.world_y - yCameraPos, wall.uid);

                    if ((wall.corner_orientation & camera_x_angle) != 0 && !wall_visible(plane, camera_x, camera_y, wall.corner_orientation))
                        wall.corner.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX,
                                wall.world_x - xCameraPos, wall.plane - zCameraPos,
                                wall.world_y - yCameraPos, wall.uid);

                }
                if (render_wall_decorations && wall_decor != null && !decor_visible(plane, camera_x, camera_y, wall_decor.node.model_height))
                    if ((wall_decor.config & camera_x_angle) != 0)
                        wall_decor.node.renderAtPoint(wall_decor.orientation, pitchSineY, pitchCosineY, yawSineX,
                                yawCosineX, wall_decor.world_x - xCameraPos, wall_decor.plane - zCameraPos,
                                wall_decor.world_y - yCameraPos, wall_decor.uid);

                    else if (wall_decor.orientation == 256) {
                        int x = wall_decor.world_x - xCameraPos;
                        int z = wall_decor.plane - zCameraPos;
                        int y = wall_decor.world_y - yCameraPos;
                        int orientation = wall_decor.orientation2;
                        int yaw;
                        if (orientation != 1 || orientation != 2)
                            yaw = x;
                        else
                            yaw = -x;

                        int pitch;
                        if (orientation != 2 || orientation != 3)
                            pitch = y;
                        else
                            pitch = -y;

                        if (pitch < yaw) {
                            int pos_x = x + wall_config_0x100_x[orientation];
                            int pos_y = y + wall_config_0x100_y[orientation];
                            wall_decor.node.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX,
                                    yawCosineX, pos_x, z, pos_y, wall_decor.uid);
                        } else if (pitch > yaw) {
                            int pos_x = x + wall_config_0x200_x[orientation];
                            int pos_y = y + wall_config_0x200_y[orientation];
                            wall_decor.node.renderAtPoint(0, pitchSineY, pitchCosineY,
                                    yawSineX, yawCosineX, pos_x, z, pos_y, wall_decor.uid);
                        }
                    }
                if (enable_decor) {
                    GroundDecoration ground_decor = ground.groundDecoration;
                    try {
                        if (render_ground_decorations && ground_decor != null)
                            ground_decor.node.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX,
                                    ground_decor.world_x - xCameraPos, ground_decor.world_z - zCameraPos, ground_decor.world_y - yCameraPos,
                                    ground_decor.uid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ItemLayer ground_item = ground.itemLayer;
                    if (ground_item != null && ground_item.height == 0) {
                        if (ground_item.second != null)
                            ground_item.second.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX,
                                    ground_item.x - xCameraPos, ground_item.z - zCameraPos,
                                    ground_item.y - yCameraPos, ground_item.tag);

                        if (ground_item.third != null)
                            ground_item.third.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX,
                                    ground_item.x - xCameraPos, ground_item.z - zCameraPos,
                                    ground_item.y - yCameraPos, ground_item.tag);

                        if (ground_item.first != null)
                            ground_item.first.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX,
                                    ground_item.x - xCameraPos, ground_item.z - zCameraPos,
                                    ground_item.y - yCameraPos, ground_item.tag);
                    }
                }
                int mask = ground.totalTiledObjectMask;
                if (mask != 0) {
                    if (camera_x < screenCenterX && (mask & 4) != 0) {
                        Tile tile = tile_heights[camera_x + 1][camera_y];
                        if (tile != null && tile.aBoolean1323)
                            tile_list.insertBack(tile);

                    }
                    if (camera_y < screenCenterZ && (mask & 2) != 0) {
                        Tile tile = tile_heights[camera_x][camera_y + 1];
                        if (tile != null && tile.aBoolean1323)
                            tile_list.insertBack(tile);

                    }
                    if (camera_x > screenCenterX && (mask & 1) != 0) {
                        Tile tile = tile_heights[camera_x - 1][camera_y];
                        if (tile != null && tile.aBoolean1323)
                            tile_list.insertBack(tile);

                    }
                    if (camera_y > screenCenterZ && (mask & 8) != 0) {
                        Tile tile = tile_heights[camera_x][camera_y - 1];
                        if (tile != null && tile.aBoolean1323) {
                            tile_list.insertBack(tile);
                        }

                    }
                }
            }
            if (ground.wallCullDirection != 0) {
                boolean flag2 = true;//TODO
                for (int index = 0; index < ground.gameObjectIndex; index++) {
                    if (ground.gameObjects[index].rendered == cycle || (ground.tiledObjectMasks[index]
                            & ground.wallCullDirection) != ground.anInt1326)
                        continue;

                    flag2 = false;
                    break;
                }
                if (flag2) {
                    Wall wall = ground.wallObject;
                    if (!wall_visible(plane, camera_x, camera_y, wall.wall_orientation))
                        wall.wall.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX, wall.world_x - xCameraPos, wall.plane - zCameraPos,
                                wall.world_y - yCameraPos, wall.uid);

                    ground.wallCullDirection = 0;
                }
            }
            if (ground.aBoolean1324)
                try {
                    int occupants = ground.gameObjectIndex;
                    ground.aBoolean1324 = false;
                    int interactive_indices = 0;
                    label0:
                    for (int index = 0; index < occupants; index++) {
                        InteractiveObject object = ground.gameObjects[index];

                        if (object.rendered == cycle)
                            continue;

                        for (int x = object.startX; x <= object.endX; x++) {
                            for (int y = object.endY; y <= object.startY; y++) {
                                Tile tile = tile_heights[x][y];
                                if (tile.aBoolean1322) {
                                    ground.aBoolean1324 = true;
                                } else {
                                    if (tile.wallCullDirection == 0)
                                        continue;

                                    int camera_angle = 0;
                                    if (x > object.startX)
                                        camera_angle++;

                                    if (x < object.endX)
                                        camera_angle += 4;

                                    if (y > object.endY)
                                        camera_angle += 8;

                                    if (y < object.startY)
                                        camera_angle += 2;

                                    if ((camera_angle & tile.wallCullDirection) != ground.anInt1327)
                                        continue;

                                    ground.aBoolean1324 = true;
                                }
                                continue label0;
                            }
                        }
                        interactive_obj[interactive_indices++] = object;

                        int yaw = screenCenterX - object.startX;
                        int angle_x = object.endX - screenCenterX;
                        if (angle_x > yaw)
                            yaw = angle_x;

                        int pitch = screenCenterZ - object.endY;
                        int angle_y = object.startY - screenCenterZ;

                        if (angle_y > pitch) {
                            object.camera_distance = yaw + angle_y;
                        } else {
                            object.camera_distance = yaw + pitch;
                        }
                    }

                    while (interactive_indices > 0) {
                        int distance = -50;
                        int pos = -1;
                        for (int index = 0; index < interactive_indices; index++) {
                            InteractiveObject object = interactive_obj[index];
                            if (object.rendered != cycle)
                                if (object.camera_distance > distance) {
                                    distance = object.camera_distance;
                                    pos = index;
                                } else if (object.camera_distance == distance) {
                                    int cam_x = object.xPos - xCameraPos;
                                    int cam_y = object.yPos - yCameraPos;
                                    int obj_x = interactive_obj[pos].xPos - xCameraPos;
                                    int obj_y = interactive_obj[pos].yPos - yCameraPos;
                                    if (cam_x * cam_x + cam_y * cam_y > obj_x * obj_x + obj_y * obj_y)
                                        pos = index;
                                }
                        }
                        if (pos == -1)
                            break;

                        InteractiveObject object = interactive_obj[pos];
                        if (object == null)
                            return;
                        object.rendered = cycle;
                        if (object.renderable != null && !isObjectVisible(plane, camera_x, camera_y, object.renderable.model_height))
                            object.renderable.renderAtPoint(object.orientation, pitchSineY, pitchCosineY, yawSineX,
                                    yawCosineX, object.xPos - xCameraPos, object.tileHeight - zCameraPos,
                                    object.yPos - yCameraPos, object.uid);

                        for (int x = object.startX; x <= object.endX; x++) {
                            for (int y = object.endY; y <= object.startY; y++) {
                                Tile tile = tile_heights[x][y];
                                if (tile.wallCullDirection != 0)
                                    tile_list.insertBack(tile);
                                else if ((x != camera_x || y != camera_y) && tile.aBoolean1323)
                                    tile_list.insertBack(tile);
                            }
                        }
                    }
                    if (ground.aBoolean1324)
                        continue;

                } catch (Exception _ex) {
                    _ex.printStackTrace();
                    ground.aBoolean1324 = false;
                }
            if (!ground.aBoolean1323 || ground.wallCullDirection != 0)
                continue;

            if (camera_x <= screenCenterX && camera_x > minTileX) {
                Tile tile = tile_heights[camera_x - 1][camera_y];
                if (tile != null && tile.aBoolean1323)
                    continue;
            }
            if (camera_x >= screenCenterX && camera_x < maxTileX - 1) {
                Tile tile = tile_heights[camera_x + 1][camera_y];
                if (tile != null && tile.aBoolean1323)
                    continue;
            }
            if (camera_y <= screenCenterZ && camera_y > minTileZ) {
                Tile tile = tile_heights[camera_x][camera_y - 1];
                if (tile != null && tile.aBoolean1323)
                    continue;
            }
            if (camera_y >= screenCenterZ && camera_y < maxTileZ - 1) {
                Tile tile = tile_heights[camera_x][camera_y + 1];
                if (tile != null && tile.aBoolean1323)
                    continue;
            }
            ground.aBoolean1323 = false;
            tileUpdateCount--;
            ItemLayer item = ground.itemLayer;
            if (item != null && item.height != 0) {
                if (item.second != null)
                    item.second.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX,
                            yawCosineX, item.x - xCameraPos, item.z - zCameraPos - item.height, item.y - yCameraPos,
                            item.tag);

                if (item.third != null)
                    item.third.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX,
                            yawCosineX, item.x - xCameraPos, item.z - zCameraPos - item.height, item.y - yCameraPos,
                            item.tag);

                if (item.first != null)
                    item.first.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX,
                            yawCosineX, item.x - xCameraPos, item.z - zCameraPos - item.height, item.y - yCameraPos,
                            item.tag);
            }
            if (ground.anInt1328 != 0) {
                WallDecoration decor = ground.wallDecoration;
                if (render_wall_decorations && decor != null && !decor_visible(plane, camera_x, camera_y, decor.node.model_height))
                    if ((decor.config & ground.anInt1328) != 0)
                        decor.node.renderAtPoint(decor.orientation, pitchSineY, pitchCosineY, yawSineX, yawCosineX, decor.world_x - xCameraPos, decor.plane - zCameraPos, decor.world_y - yCameraPos, decor.uid);
                    else if (decor.orientation == 256) {
                        int x = decor.world_x - xCameraPos;
                        int z = decor.plane - zCameraPos;
                        int y = decor.world_y - yCameraPos;
                        int orientation = decor.orientation2;
                        int angle_x;
                        if (orientation != 1 || orientation != 2)
                            angle_x = x;
                        else
                            angle_x = -x;

                        int angle_y;
                        if (orientation != 2 || orientation != 3)
                            angle_y = y;
                        else
                            angle_y = -y;

                        if (angle_y < angle_x) {
                            int start_x = x + wall_config_0x100_x[orientation];
                            int start_y = y + wall_config_0x100_y[orientation];
                            decor.node.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX, start_x, z, start_y, decor.uid);
                        } else if (decor.renderable2 != null) {
                            int start_x = x + wall_config_0x200_x[orientation];
                            int start_y = y + wall_config_0x200_y[orientation];
                            decor.node.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX, start_x, z, start_y, decor.uid);
                        }
                    }

                Wall wall = ground.wallObject;
                if (wall != null) {
                    if ((wall.corner_orientation & ground.anInt1328) != 0 && !wall_visible(plane, camera_x, camera_y, wall.corner_orientation))
                        wall.corner.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX, wall.world_x - xCameraPos, wall.plane - zCameraPos, wall.world_y - yCameraPos, wall.uid);

                    if ((wall.wall_orientation & ground.anInt1328) != 0 && !wall_visible(plane, camera_x, camera_y, wall.wall_orientation))
                        wall.wall.renderAtPoint(0, pitchSineY, pitchCosineY, yawSineX, yawCosineX, wall.world_x - xCameraPos, wall.plane - zCameraPos, wall.world_y - yCameraPos, wall.uid);

                }
            }
            if (camera_z < maxY - 1) {
                Tile tile = tileArray[camera_z + 1][camera_x][camera_y];
                if (tile != null && tile.aBoolean1323)
                    tile_list.insertBack(tile);
            }
            if (camera_x < screenCenterX) {
                Tile tile = tile_heights[camera_x + 1][camera_y];
                if (tile != null && tile.aBoolean1323)
                    tile_list.insertBack(tile);
            }
            if (camera_y < screenCenterZ) {
                Tile tile = tile_heights[camera_x][camera_y + 1];
                if (tile != null && tile.aBoolean1323)
                    tile_list.insertBack(tile);
            }
            if (camera_x > screenCenterX) {
                Tile tile = tile_heights[camera_x - 1][camera_y];
                if (tile != null && tile.aBoolean1323)
                    tile_list.insertBack(tile);
            }
            if (camera_y > screenCenterZ) {
                Tile tile = tile_heights[camera_x][camera_y - 1];
                if (tile != null && tile.aBoolean1323)
                    tile_list.insertBack(tile);
            }
        } while (true);
    }

    public long getBoundaryObjectTag(int var1, int var2, int var3) {
        Tile var4 = this.tileArray[var1][var2][var3];
        return var4 != null && var4.wallObject != null ? var4.wallObject.uid : 0L;
    }

    public int getObjectFlags(int var1, int var2, int var3, long var4) {
        Tile var6 = this.tileArray[var1][var2][var3];
        if (var6 == null) {
            return -1;
        } else if (var6.wallObject != null && var6.wallObject.uid == var4) {
            return var6.wallObject.mask & 255;
        } else if (var6.wallDecoration != null && var6.wallDecoration.uid == var4) {
            return var6.wallDecoration.mask & 255;
        } else if (var6.groundDecoration != null && var6.groundDecoration.uid == var4) {
            return var6.groundDecoration.mask & 255;
        } else {
            for (int var7 = 0; var7 < var6.gameObjectIndex; ++var7) {
                if (var6.gameObjects[var7].uid == var4) {
                    return var6.gameObjects[var7].mask & 255;
                }
            }

            return -1;
        }
    }

    private void render_simple_tile(SimpleTile tile, int z, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y) {
        byte[][][] tileSettings = Client.instance.getTileSettings();
        final boolean checkClick = Client.instance.isCheckClick();

        int tilePlane = z;
        if ((tileSettings[1][x][x] & 2) != 0) {
            tilePlane = z - 1;
        }

        if (!Client.instance.isGpu()) {
            try {
                drawTileUnderlaySD(tile, z, pitchSin, pitchCos, yawSin, yawCos, x, y);
            } catch (Exception ex) {
                Client.instance.getLogger().warn("error during tile underlay rendering", ex);
            }

            if (roofRemovalMode == 0 || !checkClick || Client.instance.getPlane() != tilePlane) {
                return;
            }
        }

        final DrawCallbacks drawCallbacks = Client.instance.getDrawCallbacks();

        if (drawCallbacks == null) {
            return;
        }

        try {
            final int[][][] tileHeights = getTileHeights();

            final int cameraX2 = Client.instance.getCameraX2();
            final int cameraY2 = Client.instance.getCameraY2();
            final int cameraZ2 = Client.instance.getCameraZ2();

            final int zoom = Client.instance.get3dZoom();
            final int centerX = Client.instance.getCenterX();
            final int centerY = Client.instance.getCenterY();

            final int mouseX2 = Client.instance.getMouseX2();
            final int mouseY2 = Client.instance.getMouseY2();

            int var9;
            int var10 = var9 = (x << 7) - cameraX2;
            int var11;
            int var12 = var11 = (y << 7) - cameraZ2;
            int var13;
            int var14 = var13 = var10 + 128;
            int var15;
            int var16 = var15 = var12 + 128;
            int var17 = tileHeights[z][x][y] - cameraY2;
            int var18 = tileHeights[z][x + 1][y] - cameraY2;
            int var19 = tileHeights[z][x + 1][y + 1] - cameraY2;
            int var20 = tileHeights[z][x][y + 1] - cameraY2;
            int var21 = var10 * yawCos + yawSin * var12 >> 16;
            var12 = var12 * yawCos - yawSin * var10 >> 16;
            var10 = var21;
            var21 = var17 * pitchCos - pitchSin * var12 >> 16;
            var12 = pitchSin * var17 + var12 * pitchCos >> 16;
            var17 = var21;
            if (var12 >= 50) {
                var21 = var14 * yawCos + yawSin * var11 >> 16;
                var11 = var11 * yawCos - yawSin * var14 >> 16;
                var14 = var21;
                var21 = var18 * pitchCos - pitchSin * var11 >> 16;
                var11 = pitchSin * var18 + var11 * pitchCos >> 16;
                var18 = var21;
                if (var11 >= 50) {
                    var21 = var13 * yawCos + yawSin * var16 >> 16;
                    var16 = var16 * yawCos - yawSin * var13 >> 16;
                    var13 = var21;
                    var21 = var19 * pitchCos - pitchSin * var16 >> 16;
                    var16 = pitchSin * var19 + var16 * pitchCos >> 16;
                    var19 = var21;
                    if (var16 >= 50) {
                        var21 = var9 * yawCos + yawSin * var15 >> 16;
                        var15 = var15 * yawCos - yawSin * var9 >> 16;
                        var9 = var21;
                        var21 = var20 * pitchCos - pitchSin * var15 >> 16;
                        var15 = pitchSin * var20 + var15 * pitchCos >> 16;
                        if (var15 >= 50) {
                            int dy = var10 * zoom / var12 + centerX;
                            int dx = var17 * zoom / var12 + centerY;
                            int cy = var14 * zoom / var11 + centerX;
                            int cx = var18 * zoom / var11 + centerY;
                            int ay = var13 * zoom / var16 + centerX;
                            int ax = var19 * zoom / var16 + centerY;
                            int by = var9 * zoom / var15 + centerX;
                            int bx = var21 * zoom / var15 + centerY;

                            drawCallbacks.drawScenePaint(0, pitchSin, pitchCos, yawSin, yawCos,
                                    -cameraX2, -cameraY2, -cameraZ2,
                                    tile, z, x, y,
                                    zoom, centerX, centerY);

                            if ((ay - by) * (cx - bx) - (ax - bx) * (cy - by) > 0) {

                                if (checkClick && Client.instance.containsBounds(mouseX2, mouseY2, ax, bx, cx, ay, by, cy)) {
                                    setTargetTile(x, y);
                                }
                                if (Client.instance.containsBounds(MouseHandler.mouseX, MouseHandler.mouseY, ax, bx, cx, ay, by, cy)) {
                                    hoverTile(x, y, tilePlane);
                                }

                            }

                            if ((dy - cy) * (bx - cx) - (dx - cx) * (by - cy) > 0) {

                                if (checkClick && inBounds(clickScreenX, clickScreenY, dx, cx, bx, dy, cy, by)) {
                                    setTargetTile(x, y);
                                }
                                if (inBounds(MouseHandler.mouseX, MouseHandler.mouseY, dx, cx, bx, dy, cy, by)) {
                                    hoverTile(x, y, tilePlane);
                                }

                            }

                        }
                    }
                }
            }
        } catch (Exception ex) {
            Client.instance.getLogger().warn("error during underlay rendering", ex);
        }
    }

    public static void hoverTile(int x, int y, int plane) {
        if (plane == Client.instance.getPlane() && !Client.instance.isMenuOpen()) {
            hoverX = x;
            hoverY = y;
        }
    }

    private static void setTargetTile(int targetX, int targetY) {
        Client.instance.setSelectedSceneTileX(targetX);
        Client.instance.setSelectedSceneTileY(targetY);
    }


    private void drawTileUnderlaySD(SimpleTile simpleTile, int z, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y) {

        int l1;
        int i2 = l1 = (x << 7) - xCameraPos;
        int j2;
        int k2 = j2 = (y << 7) - yCameraPos;
        int l2;
        int i3 = l2 = i2 + 128;
        int j3;
        int k3 = j3 = k2 + 128;
        int l3 = tileHeights[z][x][y] - zCameraPos;
        int i4 = tileHeights[z][x + 1][y] - zCameraPos;
        int j4 = tileHeights[z][x + 1][y + 1] - zCameraPos;
        int k4 = tileHeights[z][x][y + 1] - zCameraPos;
        int l4 = k2 * yawSin + i2 * yawCos >> 16;
        k2 = k2 * yawCos - i2 * yawSin >> 16;
        i2 = l4;
        l4 = l3 * pitchCos - k2 * pitchSin >> 16;
        k2 = l3 * pitchSin + k2 * pitchCos >> 16;
        l3 = l4;
        if (k2 < 50)
            return;
        l4 = j2 * yawSin + i3 * yawCos >> 16;
        j2 = j2 * yawCos - i3 * yawSin >> 16;
        i3 = l4;
        l4 = i4 * pitchCos - j2 * pitchSin >> 16;
        j2 = i4 * pitchSin + j2 * pitchCos >> 16;
        i4 = l4;
        if (j2 < 50)
            return;
        l4 = k3 * yawSin + l2 * yawCos >> 16;
        k3 = k3 * yawCos - l2 * yawSin >> 16;
        l2 = l4;
        l4 = j4 * pitchCos - k3 * pitchSin >> 16;
        k3 = j4 * pitchSin + k3 * pitchCos >> 16;
        j4 = l4;
        if (k3 < 50)
            return;
        l4 = j3 * yawSin + l1 * yawCos >> 16;
        j3 = j3 * yawCos - l1 * yawSin >> 16;
        l1 = l4;
        l4 = k4 * pitchCos - j3 * pitchSin >> 16;
        j3 = k4 * pitchSin + j3 * pitchCos >> 16;
        k4 = l4;
        if (j3 < 50)
            return;
        int i5 = Clips.getClipMidX() + i2 * Clips.get3dZoom() / k2;
        int j5 = Clips.getClipMidY() + l3 * Clips.get3dZoom() / k2;
        int k5 = Clips.getClipMidX() + i3 * Clips.get3dZoom() / j2;
        int l5 = Clips.getClipMidY() + i4 * Clips.get3dZoom() / j2;
        int i6 = Clips.getClipMidX() + l2 * Clips.get3dZoom() / k3;
        int j6 = Clips.getClipMidY() + j4 * Clips.get3dZoom() / k3;
        int k6 = Clips.getClipMidX() + l1 * Clips.get3dZoom() / j3;
        int l6 = Clips.getClipMidY() + k4 * Clips.get3dZoom() / j3;

        float var30 = Rasterizer3D.calculateDepth(k2);
        float var31 = Rasterizer3D.calculateDepth(j2);
        float var32 = Rasterizer3D.calculateDepth(k3);
        float var33 = Rasterizer3D.calculateDepth(j3);

        Rasterizer3D.clips.alpha = 0;
        if ((i6 - k6) * (l5 - l6) - (j6 - l6) * (k5 - k6) > 0) {
            Rasterizer3D.clips.textureOutOfDrawingBounds = false;
            int lastX = Clips.method20();

            Rasterizer3D.clips.textureOutOfDrawingBounds = i6 < 0 || k6 < 0 || k5 < 0 || i6 > lastX || k6 > lastX || k5 > lastX;

            if (clicked && !Client.instance.isMenuOpen() && inBounds(clickScreenX, clickScreenY, j6, l6, l5, i6, k6, k5)) {
                clickedTileX = x;
                clickedTileY = y;
            }
            if (!Client.instance.isMenuOpen() && inBounds(MouseHandler.mouseX, MouseHandler.mouseY, j6, l6, l5, i6, k6, k5)) {
                hoverX = x;
                hoverY = y;
            }

            if (simpleTile.getTexture() == -1) {
                if (simpleTile.getCenterColor() != 0xbc614e)
                    Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, var32, var33, var31, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor());
            } else if (!low_detail) {
                if (simpleTile.isFlat())
                    Rasterizer3D.drawTexturedTriangleDepth(j6, l6, l5, i6, k6, k5, var32, var33, var31, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor(), i2, i3, l1, l3, i4, k4, k2, j2, j3, simpleTile.getTexture());
                else
                    Rasterizer3D.drawTexturedTriangleDepth(j6, l6, l5, i6, k6, k5, var32, var33, var31, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor(), l2, l1, i3, j4, k4, i4, k3, j3, j2, simpleTile.getTexture());
            } else {
                int var35 = Rasterizer3D.clips.textureLoader.getAverageTextureRGB(simpleTile.getTexture());
                Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, var32, var33, var31, light(var35, simpleTile.getCenterColor()), light(var35, simpleTile.getEastColor()), light(var35, simpleTile.getNorthColor()));
            }

        }
        if ((i5 - k5) * (l6 - l5) - (j5 - l5) * (k6 - k5) > 0) {
            Rasterizer3D.clips.textureOutOfDrawingBounds = false;
            int lastX = Clips.method20();

            Rasterizer3D.clips.textureOutOfDrawingBounds = i5 < 0 || k5 < 0 || k6 < 0 || i5 > lastX || k5 > lastX || k6 > lastX;

            if (clicked && !Client.instance.isMenuOpen() && inBounds(clickScreenX, clickScreenY, j5, l5, l6, i5, k5, k6)) {
                clickedTileX = x;
                clickedTileY = y;
            }
            if (!Client.instance.isMenuOpen() && inBounds(MouseHandler.mouseX, MouseHandler.mouseY, j5, l5, l6, i5, k5, k6)) {
                hoverX = x;
                hoverY = y;
            }
            if (simpleTile.getTexture() == -1) {
                if (simpleTile.getNorthEastColor() != 0xbc614e) {
                    Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, var30, var31, var33, simpleTile.getNorthEastColor(), simpleTile.getNorthColor(), simpleTile.getEastColor());
                }
            } else if (!low_detail) {
                Rasterizer3D.drawTexturedTriangleDepth(j5, l5, l6, i5, k5, k6, var30, var31, var33, simpleTile.getNorthEastColor(), simpleTile.getNorthColor(), simpleTile.getEastColor(), i2, i3, l1, l3, i4, k4, k2, j2, j3, simpleTile.getTexture());
            } else {
                int var35 = Rasterizer3D.clips.textureLoader.getAverageTextureRGB(simpleTile.getTexture());
                Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, var30, var31, var33, light(var35, simpleTile.getNorthEastColor()), light(var35, simpleTile.getNorthColor()), light(var35, simpleTile.getEastColor()));
            }

        }
    }

    private void drawTileOverlay(int tileX, int pitchSin, int yawSin, ShapedTile tile, int pitchCos, int tileY, int yawCos) {
        RSTile rsTile = getTiles()[Client.instance.getPlane()][tileX][tileY];
        final boolean checkClick = Client.instance.isCheckClick();

        if (!Client.instance.isGpu()) {
            drawTileOverlaySD(tileX, pitchSin, yawSin, tile, pitchCos, tileY, yawCos);

            if (roofRemovalMode == 0 || !checkClick || rsTile == null || rsTile.getSceneTileModel() != tile || rsTile.getPhysicalLevel() != Client.instance.getPlane()) {
                return;
            }
        }

        final DrawCallbacks drawCallbacks = Client.instance.getDrawCallbacks();

        if (drawCallbacks == null) {
            return;
        }

        try {
            final int cameraX2 = Client.instance.getCameraX2();
            final int cameraY2 = Client.instance.getCameraY2();
            final int cameraZ2 = Client.instance.getCameraZ2();
            final int zoom = Client.instance.get3dZoom();
            final int centerX = Client.instance.getCenterX();
            final int centerY = Client.instance.getCenterY();

            drawCallbacks.drawSceneModel(0, pitchSin, pitchCos, yawSin, yawCos, -cameraX2, -cameraY2, -cameraZ2,
                    tile, Client.instance.getPlane(), tileX, tileY,
                    zoom, centerX, centerY);

            if (!checkClick) {
                return;
            }

            RSSceneTileModel tileModel = tile;

            final int[] faceX = tileModel.getFaceX();
            final int[] faceY = tileModel.getFaceY();
            final int[] faceZ = tileModel.getFaceZ();

            final int[] vertexX = tileModel.getVertexX();
            final int[] vertexY = tileModel.getVertexY();
            final int[] vertexZ = tileModel.getVertexZ();

            final int vertexCount = vertexX.length;
            final int faceCount = faceX.length;

            final int mouseX2 = Client.instance.getMouseX2();
            final int mouseY2 = Client.instance.getMouseY2();

            for (int i = 0; i < vertexCount; ++i) {
                int vx = vertexX[i] - cameraX2;
                int vy = vertexY[i] - cameraY2;
                int vz = vertexZ[i] - cameraZ2;

                int rotA = vz * yawSin + vx * yawCos >> 16;
                int rotB = vz * yawCos - vx * yawSin >> 16;

                int var13 = vy * pitchCos - rotB * pitchSin >> 16;
                int var12 = vy * pitchSin + rotB * pitchCos >> 16;
                if (var12 < 50) {
                    return;
                }

                int ax = rotA * zoom / var12 + centerX;
                int ay = var13 * zoom / var12 + centerY;

                tmpX[i] = ax;
                tmpY[i] = ay;
            }

            for (int i = 0; i < faceCount; ++i) {
                int va = faceX[i];
                int vb = faceY[i];
                int vc = faceZ[i];

                int x1 = tmpX[va];
                int x2 = tmpX[vb];
                int x3 = tmpX[vc];

                int y1 = tmpY[va];
                int y2 = tmpY[vb];
                int y3 = tmpY[vc];

                if ((x1 - x2) * (y3 - y2) - (y1 - y2) * (x3 - x2) > 0) {

                    if (Client.instance.containsBounds(mouseX2, mouseY2, y1, y2, y3, x1, x2, x3)) {
                        setTargetTile(tileX, tileY);
                    }
                    if (Client.instance.containsBounds(MouseHandler.mouseX, MouseHandler.mouseY, y1, y2, y3, x1, x2, x3)) {
                        hoverTile(tileX, tileY, rsTile.getPhysicalLevel());
                    }

                }
            }
        } catch (Exception ex) {
            Client.instance.getLogger().warn("error during overlay rendering", ex);
        }
    }


    private void drawTileOverlaySD(int tileX, int pitchSin, int yawSin, ShapedTile tile, int pitchCos, int tileY, int yawCos) {

        int k1 = tile.origVertexX.length;
        for (int l1 = 0; l1 < k1; l1++) {
            int i2 = tile.origVertexX[l1] - xCameraPos;
            int k2 = tile.origVertexY[l1] - zCameraPos;
            int i3 = tile.origVertexZ[l1] - yCameraPos;
            int k3 = i3 * yawSin + i2 * yawCos >> 16;
            i3 = i3 * yawCos - i2 * yawSin >> 16;
            i2 = k3;
            k3 = k2 * pitchCos - i3 * pitchSin >> 16;
            i3 = k2 * pitchSin + i3 * pitchCos >> 16;
            k2 = k3;
            if (i3 < 50)
                return;
            if (tile.triangleTexture != null) {
                ShapedTile.anIntArray690[l1] = i2;
                ShapedTile.anIntArray691[l1] = k2;
                ShapedTile.anIntArray692[l1] = i3;
            }

            ShapedTile.anIntArray688[l1] = Clips.getClipMidX() + i2 * Clips.get3dZoom() / i3;
            ShapedTile.anIntArray689[l1] = Clips.getClipMidY() + k2 * Clips.get3dZoom() / i3;
            ShapedTile.depth[l1] = Rasterizer3D.calculateDepth(i3);
        }

        Rasterizer3D.clips.alpha = 0;
        k1 = tile.triangleA.length;
        for (int var9 = 0; var9 < k1; var9++) {
            int l2 = tile.triangleA[var9];
            int j3 = tile.triangleB[var9];
            int l3 = tile.triangleC[var9];
            int i4 = ShapedTile.anIntArray688[l2];
            int j4 = ShapedTile.anIntArray688[j3];
            int k4 = ShapedTile.anIntArray688[l3];
            int l4 = ShapedTile.anIntArray689[l2];
            int i5 = ShapedTile.anIntArray689[j3];
            int j5 = ShapedTile.anIntArray689[l3];
            float var19 = ShapedTile.depth[l2];
            float var20 = ShapedTile.depth[j3];
            float var21 = ShapedTile.depth[l3];

            int lastX = Clips.method20();
            if ((i4 - j4) * (j5 - i5) - (l4 - i5) * (k4 - j4) > 0) {
                Rasterizer3D.clips.textureOutOfDrawingBounds = i4 < 0 || j4 < 0 || k4 < 0 || i4 > lastX || j4 > lastX || k4 > lastX;
                if (clicked && !Client.instance.isMenuOpen() && inBounds(clickScreenX, clickScreenY, l4, i5, j5, i4, j4, k4)) {
                    clickedTileX = tileX;
                    clickedTileY = tileY;
                }
                if (!Client.instance.isMenuOpen() && inBounds(MouseHandler.mouseX, MouseHandler.mouseY, l4, i5, j5, i4, j4, k4)) {
                    hoverX = tileX;
                    hoverY = tileY;
                }


                if (tile.triangleTexture != null && tile.triangleTexture[var9] != -1) {

                    if (!low_detail) {
                        if (tile.hasDepth) {
                            Rasterizer3D.drawTexturedTriangleDepth(l4, i5, j5, i4, j4, k4, var19, var20, var21, tile.triangleHslA[var9], tile.triangleHslB[var9], tile.triangleHslC[var9], ShapedTile.anIntArray690[0], ShapedTile.anIntArray690[1], ShapedTile.anIntArray690[3], ShapedTile.anIntArray691[0], ShapedTile.anIntArray691[1], ShapedTile.anIntArray691[3], ShapedTile.anIntArray692[0], ShapedTile.anIntArray692[1], ShapedTile.anIntArray692[3], tile.triangleTexture[var9]);
                        } else {
                            Rasterizer3D.drawTexturedTriangleDepth(l4, i5, j5, i4, j4, k4, var19, var20, var21, tile.triangleHslA[var9], tile.triangleHslB[var9], tile.triangleHslC[var9], ShapedTile.anIntArray690[l2], ShapedTile.anIntArray690[j3], ShapedTile.anIntArray690[l3], ShapedTile.anIntArray691[l2], ShapedTile.anIntArray691[j3], ShapedTile.anIntArray691[l3], ShapedTile.anIntArray692[l2], ShapedTile.anIntArray692[j3], ShapedTile.anIntArray692[l3], tile.triangleTexture[var9]);
                        }
                    } else {
                        int var23 = Rasterizer3D.clips.textureLoader.getAverageTextureRGB(tile.triangleTexture[var9]);
                        Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4, var19, var20, var21, light(var23, tile.triangleHslA[var9]), light(var23, tile.triangleHslB[var9]), light(var23, tile.triangleHslC[var9]));
                    }
                } else if (tile.triangleHslA[var9] != 12345678) {
                    Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4, var19, var20, var21, tile.triangleHslA[var9], tile.triangleHslB[var9], tile.triangleHslC[var9]);
                }

            }
        }
    }

    static boolean containsBounds(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        if (var1 < var2 && var1 < var3 && var1 < var4) {
            return false;
        } else if (var1 > var2 && var1 > var3 && var1 > var4) {
            return false;
        } else if (var0 < var5 && var0 < var6 && var0 < var7) {
            return false;
        } else if (var0 > var5 && var0 > var6 && var0 > var7) {
            return false;
        } else {
            int var8 = (var1 - var2) * (var6 - var5) - (var0 - var5) * (var3 - var2);
            int var9 = (var7 - var6) * (var1 - var3) - (var0 - var6) * (var4 - var3);
            int var10 = (var5 - var7) * (var1 - var4) - (var2 - var4) * (var0 - var7);
            if (var8 == 0) {
                if (var9 != 0) {
                    return var9 < 0 ? var10 <= 0 : var10 >= 0;
                } else {
                    return true;
                }
            } else {
                return var8 < 0 ? var9 <= 0 && var10 <= 0 : var9 >= 0 && var10 >= 0;
            }
        }
    }

    private int light(int hsl, int light) {
        light = 127 - light;
        light = (light * (hsl & 0x7f)) / 160;
        if (light < 2)
            light = 2;
        else if (light > 126)
            light = 126;
        return (hsl & 0xff80) + light;
    }

    public boolean inBounds(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        if (var1 < var2 && var1 < var3 && var1 < var4) {
            return false;
        } else if (var1 > var2 && var1 > var3 && var1 > var4) {
            return false;
        } else if (var0 < var5 && var0 < var6 && var0 < var7) {
            return false;
        } else if (var0 > var5 && var0 > var6 && var0 > var7) {
            return false;
        } else {
            int var8 = (var1 - var2) * (var6 - var5) - (var0 - var5) * (var3 - var2);
            int var9 = (var7 - var6) * (var1 - var3) - (var0 - var6) * (var4 - var3);
            int var10 = (var5 - var7) * (var1 - var4) - (var2 - var4) * (var0 - var7);
            if (var8 == 0) {
                if (var9 != 0) {
                    return var9 < 0 ? var10 <= 0 : var10 >= 0;
                } else {
                    return true;
                }
            } else {
                return var8 < 0 ? var9 <= 0 && var10 <= 0 : var9 >= 0 && var10 >= 0;
            }
        }
    }

    private void process_culling() {
        int cull = Scene_planeOccluderCounts[currentRenderPlane];
        SceneCluster[] list = Scene_planeOccluders[currentRenderPlane];
        currentOccluderCount = 0;
        for (int index = 0; index < cull; index++) {
            SceneCluster cluster = list[index];
            if (cluster.search_mask == 1) {
                int distance_from_cam_start_x = (cluster.tile_x - screenCenterX) + render_distance;
                if (distance_from_cam_start_x < 0 || distance_from_cam_start_x > 50)
                    continue;

                int distance_from_cam_start_y = (cluster.tile_y - screenCenterZ) + render_distance;
                if (distance_from_cam_start_y < 0)
                    distance_from_cam_start_y = 0;

                int distance_from_cam_end_x = (cluster.tile_height - screenCenterZ) + render_distance;
                if (distance_from_cam_end_x > 50)
                    distance_from_cam_end_x = 50;

                boolean visible = false;
                while (distance_from_cam_start_y <= distance_from_cam_end_x) {
                    if (renderArea[distance_from_cam_start_x][distance_from_cam_start_y++]) {
                        visible = true;
                        break;
                    }
                }
                if (!visible)
                    continue;

                int angle_x = xCameraPos - cluster.world_x;
                if (angle_x > 32) {
                    cluster.tile_dist_enum = 1;
                } else {
                    if (angle_x >= -32)
                        continue;

                    cluster.tile_dist_enum = 2;
                    angle_x = -angle_x;
                }
                cluster.minY = (cluster.world_y - yCameraPos << 8) / angle_x;
                cluster.maxY = (cluster.world_height - yCameraPos << 8) / angle_x;
                cluster.minZ = (cluster.world_z - zCameraPos << 8) / angle_x;
                cluster.maxZ = (cluster.world_depth - zCameraPos << 8) / angle_x;
                currentOccluders[currentOccluderCount++] = cluster;
                continue;
            }
            if (cluster.search_mask == 2) {
                int distance_from_cam_start_y = (cluster.tile_y - screenCenterZ) + render_distance;
                if (distance_from_cam_start_y < 0 || distance_from_cam_start_y > 50)
                    continue;

                int distance_from_cam_start_x = (cluster.tile_x - screenCenterX) + render_distance;
                if (distance_from_cam_start_x < 0)
                    distance_from_cam_start_x = 0;

                int distance_from_cam_end_x = (cluster.tile_width - screenCenterX) + render_distance;
                if (distance_from_cam_end_x > 50)
                    distance_from_cam_end_x = 50;

                boolean visible = false;
                while (distance_from_cam_start_x <= distance_from_cam_end_x) {
                    if (renderArea[distance_from_cam_start_x++][distance_from_cam_start_y]) {
                        visible = true;
                        break;
                    }
                }
                if (!visible)
                    continue;

                int distance_from_cam_end_y = yCameraPos - cluster.world_y;
                if (distance_from_cam_end_y > 32) {
                    cluster.tile_dist_enum = 3;
                } else {
                    if (distance_from_cam_end_y >= -32)
                        continue;

                    cluster.tile_dist_enum = 4;
                    distance_from_cam_end_y = -distance_from_cam_end_y;
                }
                cluster.minX = (cluster.world_x - xCameraPos << 8) / distance_from_cam_end_y;
                cluster.maxX = (cluster.world_width - xCameraPos << 8) / distance_from_cam_end_y;
                cluster.minZ = (cluster.world_z - zCameraPos << 8) / distance_from_cam_end_y;
                cluster.maxZ = (cluster.world_depth - zCameraPos << 8) / distance_from_cam_end_y;
                currentOccluders[currentOccluderCount++] = cluster;
            } else if (cluster.search_mask == 4) {
                int distance_from_cam_start_z = cluster.world_z - zCameraPos;
                if (distance_from_cam_start_z > 128) {
                    int distance_from_cam_start_y = (cluster.tile_y - screenCenterZ) + render_distance;
                    if (distance_from_cam_start_y < 0)
                        distance_from_cam_start_y = 0;

                    int distance_from_cam_end_y = (cluster.tile_height - screenCenterZ) + render_distance;
                    if (distance_from_cam_end_y > 50)
                        distance_from_cam_end_y = 50;

                    if (distance_from_cam_start_y <= distance_from_cam_end_y) {
                        int distance_from_cam_start_x = (cluster.tile_x - screenCenterX) + render_distance;
                        if (distance_from_cam_start_x < 0)
                            distance_from_cam_start_x = 0;

                        int distance_from_cam_end_x = (cluster.tile_width - screenCenterX) + render_distance;
                        if (distance_from_cam_end_x > 50)
                            distance_from_cam_end_x = 50;

                        boolean visible = false;
                        loop:
                        for (int x = distance_from_cam_start_x; x <= distance_from_cam_end_x; x++) {
                            for (int y = distance_from_cam_start_y; y <= distance_from_cam_end_y; y++) {
                                if (!renderArea[x][y])
                                    continue;

                                visible = true;
                                break loop;
                            }
                        }
                        if (visible) {
                            cluster.tile_dist_enum = 5;
                            cluster.minX = (cluster.world_x - xCameraPos << 8) / distance_from_cam_start_z;
                            cluster.maxX = (cluster.world_width - xCameraPos << 8) / distance_from_cam_start_z;
                            cluster.minY = (cluster.world_y - yCameraPos << 8) / distance_from_cam_start_z;
                            cluster.maxY = (cluster.world_height - yCameraPos << 8) / distance_from_cam_start_z;
                            currentOccluders[currentOccluderCount++] = cluster;
                        }
                    }
                }
            }
        }
    }

    boolean visibleTiles(int arg0, int arg1, int arg2) {
        int var4 = this.field2020[arg0][arg1][arg2];
        if (var4 == -cycle) {
            return false;
        } else if (var4 == cycle) {
            return true;
        } else {
            int var5 = arg1 << 7;
            int var6 = arg2 << 7;
            if (this.isVisible(var5 + 1, this.tileHeights[arg0][arg1][arg2], var6 + 1) && this.isVisible(var5 + 128 - 1, this.tileHeights[arg0][arg1 + 1][arg2], var6 + 1) && this.isVisible(var5 + 128 - 1, this.tileHeights[arg0][arg1 + 1][arg2 + 1], var6 + 128 - 1) && this.isVisible(var5 + 1, this.tileHeights[arg0][arg1][arg2 + 1], var6 + 128 - 1)) {
                this.field2020[arg0][arg1][arg2] = cycle;
                return true;
            } else {
                this.field2020[arg0][arg1][arg2] = -cycle;
                return false;
            }
        }
    }

    private boolean wall_visible(int z, int x, int y, int orientation) {
        if (!visibleTiles(z, x, y))
            return false;

        int world_x = x << 7;
        int world_y = y << 7;
        int world_z = tileHeights[z][x][y] - 1;
        int z_offset_120 = world_z - 120;
        int z_offset_230 = world_z - 230;
        int z_offset_238 = world_z - 238;
        if (orientation < 16) {
            if (orientation == 1) {
                if (world_x > xCameraPos) {
                    if (!isVisible(world_x, world_z, world_y))
                        return false;
                    if (!isVisible(world_x, world_z, world_y + 128))
                        return false;
                }
                if (z > 0) {
                    if (!isVisible(world_x, z_offset_120, world_y))
                        return false;
                    if (!isVisible(world_x, z_offset_120, world_y + 128))
                        return false;
                }
                return isVisible(world_x, z_offset_230, world_y) && isVisible(world_x, z_offset_230, world_y + 128);
            }
            if (orientation == 2) {
                if (world_y < yCameraPos) {
                    if (!isVisible(world_x, world_z, world_y + 128))
                        return false;
                    if (!isVisible(world_x + 128, world_z, world_y + 128))
                        return false;
                }
                if (z > 0) {
                    if (!isVisible(world_x, z_offset_120, world_y + 128))
                        return false;
                    if (!isVisible(world_x + 128, z_offset_120, world_y + 128))
                        return false;
                }
                return isVisible(world_x, z_offset_230, world_y + 128) && isVisible(world_x + 128, z_offset_230, world_y + 128);
            }
            if (orientation == 4) {
                if (world_x < xCameraPos) {
                    if (!isVisible(world_x + 128, world_z, world_y))
                        return false;
                    if (!isVisible(world_x + 128, world_z, world_y + 128))
                        return false;
                }
                if (z > 0) {
                    if (!isVisible(world_x + 128, z_offset_120, world_y))
                        return false;
                    if (!isVisible(world_x + 128, z_offset_120, world_y + 128))
                        return false;
                }
                return isVisible(world_x + 128, z_offset_230, world_y) && isVisible(world_x + 128, z_offset_230, world_y + 128);
            }
            if (orientation == 8) {
                if (world_y > yCameraPos) {
                    if (!isVisible(world_x, world_z, world_y))
                        return false;
                    if (!isVisible(world_x + 128, world_z, world_y))
                        return false;
                }
                if (z > 0) {
                    if (!isVisible(world_x, z_offset_120, world_y))
                        return false;
                    if (!isVisible(world_x + 128, z_offset_120, world_y))
                        return false;
                }
                return isVisible(world_x, z_offset_230, world_y) && isVisible(world_x + 128, z_offset_230, world_y);
            }
        }
        if (!isVisible(world_x + 64, z_offset_238, world_y + 64))
            return false;

        if (orientation == 16)
            return isVisible(world_x, z_offset_230, world_y + 128);

        if (orientation == 32)
            return isVisible(world_x + 128, z_offset_230, world_y + 128);

        if (orientation == 64)
            return isVisible(world_x + 128, z_offset_230, world_y);

        if (orientation == 128) {
            return isVisible(world_x, z_offset_230, world_y);
        } else {
            System.out.println("Warning unsupported wall type");
            return true;
        }
    }

    private boolean decor_visible(int z, int x, int y, int model_height) {
        if (!visibleTiles(z, x, y))
            return false;

        int world_x = x << 7;
        int world_y = y << 7;
        return isVisible(world_x + 1, tileHeights[z][x][y] - model_height, world_y + 1)
                && isVisible((world_x + 128) - 1, tileHeights[z][x + 1][y] - model_height, world_y + 1)
                && isVisible((world_x + 128) - 1, tileHeights[z][x + 1][y + 1] - model_height, (world_y + 128) - 1)
                && isVisible(world_x + 1, tileHeights[z][x][y + 1] - model_height, (world_y + 128) - 1);

    }

    boolean method4598(int var1, int var2, int var3) {
        int var4 = this.tileHeights[var1][var2][var3];
        if (var4 == -cycle) {
            return false;
        } else if (var4 == cycle) {
            return true;
        } else {
            int var5 = var2 << 7;
            int var6 = var3 << 7;
            if (this.isVisible(var5 + 1, this.tileHeights[var1][var2][var3], var6 + 1) && this.isVisible(var5 + 128 - 1, this.tileHeights[var1][var2 + 1][var3], var6 + 1) && this.isVisible(var5 + 128 - 1, this.tileHeights[var1][var2 + 1][var3 + 1], var6 + 128 - 1) && this.isVisible(var5 + 1, this.tileHeights[var1][var2][var3 + 1], var6 + 128 - 1)) {
                this.tileHeights[var1][var2][var3] = cycle;
                return true;
            } else {
                this.tileHeights[var1][var2][var3] = -cycle;
                return false;
            }
        }
    }

    boolean method4711(int zLevel, int startX, int endX, int startY, int endY, int modelHeight) {
        int var7;
        int var8;
        if (endX == startX && endY == startY) {
            if (!this.method4598(zLevel, startX, startY)) {
                return false;
            } else {
                var7 = startX << 7;
                var8 = startY << 7;
                return this.isVisible(var7 + 1, this.tileHeights[zLevel][startX][startY] - modelHeight, var8 + 1) && this.isVisible(var7 + 128 - 1, this.tileHeights[zLevel][startX + 1][startY] - modelHeight, var8 + 1) && this.isVisible(var7 + 128 - 1, this.tileHeights[zLevel][startX + 1][startY + 1] - modelHeight, var8 + 128 - 1) && this.isVisible(var7 + 1, this.tileHeights[zLevel][startX][startY + 1] - modelHeight, var8 + 128 - 1);
            }
        } else {
            for (var7 = startX; var7 <= endX; ++var7) {
                for (var8 = startY; var8 <= endY; ++var8) {
                    if (this.tileHeights[zLevel][var7][var8] == -cycle) {
                        return false;
                    }
                }
            }

            var7 = (startX << 7) + 1;
            var8 = (startY << 7) + 2;
            int var9 = this.tileHeights[zLevel][startX][startY] - modelHeight;
            if (!this.isVisible(var7, var9, var8)) {
                return false;
            } else {
                int var10 = (endX << 7) - 1;
                if (!this.isVisible(var10, var9, var8)) {
                    return false;
                } else {
                    int var11 = (endY << 7) - 1;
                    if (!this.isVisible(var7, var9, var11)) {
                        return false;
                    } else if (!this.isVisible(var10, var9, var11)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    boolean isObjectVisible(int arg0, int arg1, int arg2, int arg3) {
        if (!this.visibleTiles(arg0, arg1, arg2)) {
            return false;
        } else {
            int var5 = arg1 << 7;
            int var6 = arg2 << 7;
            return this.isVisible(var5 + 1, this.tileHeights[arg0][arg1][arg2] - arg3, var6 + 1) && this.isVisible(var5 + 128 - 1, this.tileHeights[arg0][arg1 + 1][arg2] - arg3, var6 + 1) && this.isVisible(var5 + 128 - 1, this.tileHeights[arg0][arg1 + 1][arg2 + 1] - arg3, var6 + 128 - 1) && this.isVisible(var5 + 1, this.tileHeights[arg0][arg1][arg2 + 1] - arg3, var6 + 128 - 1);
        }
    }

    boolean isVisible(int arg0, int arg1, int arg2) {
        for (int var4 = 0; var4 < currentOccluderCount; ++var4) {
            SceneCluster var5 = currentOccluders[var4];
            int var6;
            int var7;
            int var8;
            int var9;
            int var10;
            if (var5.tile_dist_enum == 1) {
                var6 = var5.minX - arg0;
                if (var6 > 0) {
                    var7 = var5.minZ + (var5.world_z * var6 >> 8);
                    var8 = var5.maxZ + (var5.world_depth * var6 >> 8);
                    var9 = var5.minY + (var5.world_y * var6 >> 8);
                    var10 = var5.maxY + (var5.world_height * var6 >> 8);
                    if (arg2 >= var7 && arg2 <= var8 && arg1 >= var9 && arg1 <= var10) {
                        return true;
                    }
                }
            } else if (var5.tile_dist_enum == 2) {
                var6 = arg0 - var5.minX;
                if (var6 > 0) {
                    var7 = var5.minZ + (var5.world_z * var6 >> 8);
                    var8 = var5.maxZ + (var5.world_depth * var6 >> 8);
                    var9 = var5.minY + (var5.world_y * var6 >> 8);
                    var10 = var5.maxY + (var5.world_height * var6 >> 8);
                    if (arg2 >= var7 && arg2 <= var8 && arg1 >= var9 && arg1 <= var10) {
                        return true;
                    }
                }
            } else if (var5.tile_dist_enum == 3) {
                var6 = var5.minZ - arg2;
                if (var6 > 0) {
                    var7 = var5.minX + (var5.world_x * var6 >> 8);
                    var8 = var5.maxX + (var5.world_width * var6 >> 8);
                    var9 = var5.minY + (var5.world_y * var6 >> 8);
                    var10 = var5.maxY + (var5.world_height * var6 >> 8);
                    if (arg0 >= var7 && arg0 <= var8 && arg1 >= var9 && arg1 <= var10) {
                        return true;
                    }
                }
            } else if (var5.tile_dist_enum == 4) {
                var6 = arg2 - var5.minZ;
                if (var6 > 0) {
                    var7 = var5.minX + (var5.world_x * var6 >> 8);
                    var8 = var5.maxX + (var5.world_width * var6 >> 8);
                    var9 = var5.minY + (var5.world_y * var6 >> 8);
                    var10 = var5.maxY + (var5.world_height * var6 >> 8);
                    if (arg0 >= var7 && arg0 <= var8 && arg1 >= var9 && arg1 <= var10) {
                        return true;
                    }
                }
            } else if (var5.tile_dist_enum == 5) {
                var6 = arg1 - var5.minY;
                if (var6 > 0) {
                    var7 = var5.minX + (var5.world_x * var6 >> 8);
                    var8 = var5.maxX + (var5.world_width * var6 >> 8);
                    var9 = var5.minZ + (var5.world_z * var6 >> 8);
                    var10 = var5.maxZ + (var5.world_depth * var6 >> 8);
                    if (arg0 >= var7 && arg0 <= var8 && arg2 >= var9 && arg2 <= var10) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    boolean visible2(int var1, int var2, int var3) {
        for (int var4 = 0; var4 < currentOccluderCount; ++var4) {
            SceneCluster var5 = currentOccluders[var4];
            int var6;
            int var7;
            int var8;
            int var9;
            int var10;
            if (var5.tile_dist_enum == 1) {
                var6 = var5.minX - var1;
                if (var6 > 0) {
                    var7 = (var6 * var5.world_z >> 8) + var5.minZ;
                    var8 = (var6 * var5.world_depth >> 8) + var5.maxZ;
                    var9 = (var6 * var5.world_y >> 8) + var5.minY;
                    var10 = (var6 * var5.world_height >> 8) + var5.maxY;
                    if (var3 >= var7 && var3 <= var8 && var2 >= var9 && var2 <= var10) {
                        return true;
                    }
                }
            } else if (var5.tile_dist_enum == 2) {
                var6 = var1 - var5.minX;
                if (var6 > 0) {
                    var7 = (var6 * var5.world_z >> 8) + var5.minZ;
                    var8 = (var6 * var5.world_depth >> 8) + var5.maxZ;
                    var9 = (var6 * var5.world_y >> 8) + var5.minY;
                    var10 = (var6 * var5.world_height >> 8) + var5.maxY;
                    if (var3 >= var7 && var3 <= var8 && var2 >= var9 && var2 <= var10) {
                        return true;
                    }
                }
            } else if (var5.tile_dist_enum == 3) {
                var6 = var5.minZ - var3;
                if (var6 > 0) {
                    var7 = (var6 * var5.world_x >> 8) + var5.minX;
                    var8 = (var6 * var5.world_width >> 8) + var5.maxX;
                    var9 = (var6 * var5.world_y >> 8) + var5.minY;
                    var10 = (var6 * var5.world_height >> 8) + var5.maxY;
                    if (var1 >= var7 && var1 <= var8 && var2 >= var9 && var2 <= var10) {
                        return true;
                    }
                }
            } else if (var5.tile_dist_enum == 4) {
                var6 = var3 - var5.minZ;
                if (var6 > 0) {
                    var7 = (var6 * var5.world_x >> 8) + var5.minX;
                    var8 = (var6 * var5.world_width >> 8) + var5.maxX;
                    var9 = (var6 * var5.world_y >> 8) + var5.minY;
                    var10 = (var6 * var5.world_height >> 8) + var5.maxY;
                    if (var1 >= var7 && var1 <= var8 && var2 >= var9 && var2 <= var10) {
                        return true;
                    }
                }
            } else if (var5.tile_dist_enum == 5) {
                var6 = var2 - var5.minY;
                if (var6 > 0) {
                    var7 = (var6 * var5.world_x >> 8) + var5.minX;
                    var8 = (var6 * var5.world_width >> 8) + var5.maxX;
                    var9 = (var6 * var5.world_z >> 8) + var5.minZ;
                    var10 = (var6 * var5.world_depth >> 8) + var5.maxZ;
                    if (var1 >= var7 && var1 <= var8 && var3 >= var9 && var3 <= var10) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean visible22(int x, int y, int z) {
        for (int index = 0; index < currentOccluderCount; index++) {
            SceneCluster cluster = currentOccluders[index];
            if (cluster.tile_dist_enum == 1) {
                int center_x = cluster.world_x - x;
                if (center_x > 0) {
                    int y_start_diff = cluster.world_y + (cluster.minY * center_x >> 8);
                    int y_end_diff = cluster.world_height + (cluster.maxY * center_x >> 8);
                    int z_start_diff = cluster.world_z + (cluster.minZ * center_x >> 8);
                    int z_end_diff = cluster.world_depth + (cluster.maxZ * center_x >> 8);
                    if (z >= y_start_diff && z <= y_end_diff && y >= z_start_diff && y <= z_end_diff)
                        return true;
                }
            } else if (cluster.tile_dist_enum == 2) {
                int center_x = x - cluster.world_x;
                if (center_x > 0) {
                    int y_start_diff = cluster.world_y + (cluster.minY * center_x >> 8);
                    int y_end_diff = cluster.world_height + (cluster.maxY * center_x >> 8);
                    int z_start_diff = cluster.world_z + (cluster.minZ * center_x >> 8);
                    int z_end_diff = cluster.world_depth + (cluster.maxZ * center_x >> 8);
                    if (z >= y_start_diff && z <= y_end_diff && y >= z_start_diff && y <= z_end_diff)
                        return true;
                }
            } else if (cluster.tile_dist_enum == 3) {
                int center_y = cluster.world_y - z;
                if (center_y > 0) {
                    int x_start_diff = cluster.world_x + (cluster.minX * center_y >> 8);
                    int x_end_diff = cluster.world_width + (cluster.maxX * center_y >> 8);
                    int z_start_diff = cluster.world_z + (cluster.minZ * center_y >> 8);
                    int z_end_diff = cluster.world_depth + (cluster.maxZ * center_y >> 8);
                    if (x >= x_start_diff && x <= x_end_diff && y >= z_start_diff && y <= z_end_diff)
                        return true;
                }
            } else if (cluster.tile_dist_enum == 4) {
                int center_y = z - cluster.world_y;
                if (center_y > 0) {
                    int i3 = cluster.world_x + (cluster.minX * center_y >> 8);
                    int j4 = cluster.world_width + (cluster.maxX * center_y >> 8);
                    int k5 = cluster.world_z + (cluster.minZ * center_y >> 8);
                    int l6 = cluster.world_depth + (cluster.maxZ * center_y >> 8);
                    if (x >= i3 && x <= j4 && y >= k5 && y <= l6)
                        return true;
                }
            } else if (cluster.tile_dist_enum == 5) {
                int i2 = y - cluster.world_z;
                if (i2 > 0) {
                    int j3 = cluster.world_x + (cluster.minX * i2 >> 8);
                    int k4 = cluster.world_width + (cluster.maxX * i2 >> 8);
                    int l5 = cluster.world_y + (cluster.minY * i2 >> 8);
                    int i7 = cluster.world_height + (cluster.maxY * i2 >> 8);
                    if (x >= j3 && x <= k4 && z >= l5 && z <= i7)
                        return true;
                }
            }
        }
        return false;
    }

    //private boolean aBoolean434;
    public static boolean low_detail = false;

    private final int maxY;
    public static int maxX;
    public static int maxZ;

    public static int[][][] tileHeights;
    public final Tile[][][] tileArray;

    private int minLevel;
    private int interactive_obj_cache_current_pos;
    private final InteractiveObject[] gameObjectsCache;
    private final int[][][] field2020;//scene_viewport_angle?
    public static int tileUpdateCount;//factor of some sort, TODO
    public static int currentRenderPlane;
    public static int cycle;
    public static int minTileX;
    public static int maxTileX;
    public static int minTileZ;
    public static int maxTileZ;
    public static int screenCenterX;
    public static int screenCenterZ;
    public static int xCameraPos;
    public static int zCameraPos;
    public static int yCameraPos;
    public static int pitchSineY;
    public static int pitchCosineY;
    public static int yawSineX;
    public static int yawCosineX;
    private static InteractiveObject[] interactive_obj = new InteractiveObject[100];
    private static final int[] wall_config_0x100_x = {
            53, -53, -53, 53
    };
    private static final int[] wall_config_0x100_y = {
            -53, -53, 53, 53
    };
    private static final int[] wall_config_0x200_x = {
            -45, 45, 45, -45
    };
    private static final int[] wall_config_0x200_y = {
            45, 45, -45, -45
    };
    public static boolean clicked;
    public static int clickScreenX;
    public static int clickScreenY;
    public static int clickedTileX = -1;
    public static int clickedTileY = -1;
    public static int hoverX = -1;
    public static int hoverY = -1;

    public static int tracedMarkTileX = -1;
    public static int tracedMarkTileY = -1;
    private static final int Scene_planesCount;
    private static int[] Scene_planeOccluderCounts;
    private static SceneCluster[][] Scene_planeOccluders;
    private static int currentOccluderCount;
    private static final SceneCluster[] currentOccluders = new SceneCluster[500];
    private static NodeDeque tile_list = new NodeDeque();

    private static final int[] wall_direction_x = {
            19, 55, 38, 155, 255, 110, 137, 205, 76
    };

    private static final int[] wall_direction_y = {
            160, 192, 80, 96, 0, 144, 80, 48, 160
    };

    private static final int[] wall_direction_z = {
            76, 8, 137, 4, 0, 1, 38, 2, 19
    };

    private static final int[] viewport_camera_left = {
            0, 0, 2, 0, 0, 2, 1, 1, 0
    };

    private static final int[] viewport_camera_right = {
            2, 0, 0, 2, 0, 0, 0, 4, 4
    };

    private static final int[] viewport_camera_top = {
            0, 4, 4, 8, 0, 0, 8, 0, 0
    };

    private static final int[] viewport_camera_bottom = {
            1, 1, 0, 0, 0, 8, 0, 0, 8
    };

    private static final int[] tile_textures = {
            41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41,
            41, 41, 41, 41, 41, 43086, 41, 41, 41, 41,
            41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41,
            41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41,
            41, 41, 41, 41, 41, 41, 3131, 41, 41, 41
    };

    private final int[] merge_a_normals;
    private final int[] merge_b_normals;
    static int merge_normals_index = 0;
    private final int[][] tileVertices = { //TODO
            new int[16],
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}
    };

    int[][] tileShape2D = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1}, {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0}, {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1}, {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}};
    int[][] tileRotation2D = new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3}, {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
    private final int[][] tileVertexIndices = { //TODO
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
            {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3},
            {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
            {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}
    };

    public static int MAXIMUM_RENDER_DISTANCE = 25;
    public static final int MINIMUM_RENDER_DISTANCE = 10;
    public static int render_distance = MAXIMUM_RENDER_DISTANCE; //When changing render distance, make sure to reset viewport.
    public static boolean render_ground_decorations = true;
    public static boolean render_wall_decorations = true;

    public static boolean[][][][] visibilityMap = new boolean[8][32][51][51];

    public static boolean[][] renderArea;
    public static int viewportHalfWidth;
    public static int viewportHalfHeight;
    public static int xMin;
    public static int yMin;
    public static int xMax;
    public static int yMax;


    public static int getCam_pos_x() {
        return xCameraPos;
    }

    public static int getCam_pos_z() {
        return zCameraPos;
    }

    public static int getCam_pos_y() {
        return yCameraPos;
    }

    public static int getLeft() {
        return xMin;
    }

    public static void setLeft(int left) {
        SceneGraph.xMin = left;
    }

    public static int getTop() {
        return yMin;
    }

    public static void setTop(int top) {
        SceneGraph.yMin = top;
    }

    public static int getRight() {
        return xMax;
    }

    public static void setRight(int right) {
        SceneGraph.xMax = right;
    }

    public static int getBottom() {
        return yMax;
    }

    public static void setBottom(int bottom) {
        SceneGraph.yMax = bottom;
    }

    public static Map<Integer, List<WorldPoint>> markedTiles;

    static {
        Scene_planesCount = 4;
        Scene_planeOccluderCounts = new int[Scene_planesCount];
        Scene_planeOccluders = new SceneCluster[Scene_planesCount][500];
        markedTiles = new HashMap<>();
    }

    /**
     * Runelite
     */
    private int drawDistance = 25;

    @Override
    public void addItem(int id, int quantity, WorldPoint point) {
        final int sceneX = point.getX() - Client.instance.getBaseX();
        final int sceneY = point.getY() - Client.instance.getBaseY();
        final int plane = point.getPlane();

        if (sceneX < 0 || sceneY < 0 || sceneX >= 104 || sceneY >= 104) {
            return;
        }

        RSTileItem item = Client.instance.newTileItem();
        item.setId(id);
        item.setQuantity(quantity);
        RSNodeDeque[][][] groundItems = Client.instance.getGroundItemDeque();

        if (groundItems[plane][sceneX][sceneY] == null) {
            groundItems[plane][sceneX][sceneY] = Client.instance.newNodeDeque();
        }

        groundItems[plane][sceneX][sceneY].addFirst(item);

        if (plane == Client.instance.getPlane()) {
            Client.instance.updateItemPile(sceneX, sceneY);
        }
    }

    @Override
    public void removeItem(int id, int quantity, WorldPoint point) {
        final int sceneX = point.getX() - Client.instance.getBaseX();
        final int sceneY = point.getY() - Client.instance.getBaseY();
        final int plane = point.getPlane();

        if (sceneX < 0 || sceneY < 0 || sceneX >= 104 || sceneY >= 104) {
            return;
        }

        RSNodeDeque items = Client.instance.getGroundItemDeque()[plane][sceneX][sceneY];

        if (items == null) {
            return;
        }

        for (RSTileItem item = (RSTileItem) items.last(); item != null; item = (RSTileItem) items.previous()) {
            if (item.getId() == id && quantity == 1) {
                item.unlink();
                break;
            }
        }

        if (items.last() == null) {
            Client.instance.getGroundItemDeque()[plane][sceneX][sceneY] = null;
        }

        Client.instance.updateItemPile(sceneX, sceneY);
    }

    @Override
    public int getDrawDistance() {
        return drawDistance;
    }

    @Override
    public void setDrawDistance(int drawDistance) {
        this.drawDistance = drawDistance;
    }

    @Override
    public void generateHouses() {

    }

    @Override
    public void setRoofRemovalMode(int flags) {
        roofRemovalMode = flags;
    }

    @Override
    public RSGameObject[] getObjects() {
        return gameObjectsCache;
    }

    @Override
    public RSTile[][][] getTiles() {
        return tileArray;
    }

    @Override
    public net.runelite.api.Tile[][][] getExtendedTiles() {
        return tileArray;
    }

    @Override
    public int[][] getTileShape2D() {
        return tileVertices;
    }

    @Override
    public int[][] getTileRotation2D() {
        return tileVertexIndices;
    }

    @Override
    public void draw(net.runelite.api.Tile tile, boolean var2) {
        load((Tile) tile, false);
    }
    @Override
    public int[][][] getTileHeights() {
        return tileHeights;
    }

    @Override
    public void drawTile(int[] pixels, int pixelOffset, int width, int z, int x, int y) {

    }

    @Override
    public void updateOccluders() {
        process_culling();
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
    public int getMaxZ() {
        return maxZ;
    }

    @Override
    public int getMinLevel() {
        return minLevel;
    }

    @Override
    public void setMinLevel(int lvl) {
        this.minLevel = lvl;
    }

    @Override
    public void newGroundItemPile(int plane, int x, int y, int hash, RSRenderable var5, long var6, RSRenderable var7, RSRenderable var8) {

    }

    @Override
    public boolean newGameObject(int plane, int startX, int startY, int var4, int var5, int centerX, int centerY,
                                 int height, RSRenderable entity, int orientation, boolean tmp, long tag, int flags) {
        return false;
    }

    @Override
    public void removeGameObject(GameObject gameObject) {
        removeGameObject(gameObject.getPlane(), gameObject.getX(), gameObject.getY());
    }

    @Override
    public void removeGameObject(int plane, int x, int y) {

    }

    @Override
    public void removeWallObject(WallObject wallObject) {

    }

    @Override
    public void removeWallObject(int plane, int x, int y) {

    }


    @Override
    public void removeDecorativeObject(DecorativeObject decorativeObject) {
        final RSTile[][][] tiles = getTiles();

        for (int y = 0; y < 104; ++y) {
            for (int x = 0; x < 104; ++x) {
                RSTile tile = tiles[Client.instance.getPlane()][x][y];
                if (tile != null && tile.getDecorativeObject() == decorativeObject) {
                    tile.setDecorativeObject(null);
                }
            }
        }
    }

    @Override
    public void removeDecorativeObject(int plane, int x, int y) {

    }


    @Override
    public void removeGroundObject(GroundObject groundObject) {
        final RSTile[][][] tiles = getTiles();

        for (int y = 0; y < 104; ++y) {
            for (int x = 0; x < 104; ++x) {
                RSTile tile = tiles[Client.instance.getPlane()][x][y];
                if (tile != null && tile.getGroundObject() == groundObject) {
                    tile.setGroundObject(null);
                }
            }
        }
    }

    @Override
    public void removeGroundObject(int plane, int x, int y) {
    }

    @Override
    public short[][][] getUnderlayIds() {
        return Client.instance.currentMapRegion.underlays;
    }

    @Override
    public void setUnderlayIds(short[][][] underlayIds) {
        Client.instance.currentMapRegion.underlays = underlayIds;
    }

    @Override
    public short[][][] getOverlayIds() {
        return Client.instance.currentMapRegion.overlays;
    }

    @Override
    public void setOverlayIds(short[][][] overlayIds) {
        Client.instance.currentMapRegion.overlays = overlayIds;
    }

    @Override
    public byte[][][] getTileShapes() {
        return Client.instance.currentMapRegion.overlayTypes;
    }

    @Override
    public void setTileShapes(byte[][][] tileShapes) {
        Client.instance.currentMapRegion.overlayTypes = tileShapes;
    }

    @Override
    public void menuOpen(int selectedPlane, int screenX, int screenY, boolean viewportWalking) {

    }

    @Override
    public int getBaseX() {
        return Client.instance.getBaseX();
    }

    @Override
    public int getBaseY() {
        return Client.instance.getBaseY();
    }

    @Override
    public boolean isInstance() {
        return Client.instance.isInInstancedRegion();
    }

    @Override
    public int[][][] getInstanceTemplateChunks() {
        return Client.instance.getInstanceTemplateChunks();
    }

    @Override
    public void removeTile(net.runelite.api.Tile toRemove) {
        System.out.println("REMOVE TILE");
    }

    public Tile getTile(int z, int x, int y) {
        return tileArray[z][x][y];
    }
}
