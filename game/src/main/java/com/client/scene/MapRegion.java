package com.client.scene;

import com.client.Buffer;
import com.client.Client;
import com.client.Renderable;
import com.client.audio.ObjectSound;
import com.client.audio.StaticSound;
import com.client.definitions.FloorOverlayDefinition;
import com.client.definitions.FloorUnderlayDefinition;
import com.client.definitions.ObjectDefinition;
import com.client.draw.Rasterizer3D;
import com.client.entity.model.Model;
import com.client.entity.model.ViewportMouse;
import com.client.scene.region.BitTools;
import com.client.utilities.ObjectKeyUtil;

import static com.client.Client.instance;

public final class MapRegion {
    private final int[] blended_hue;
    private final int[] blended_saturation;
    private final int[] blended_lightness;
    private final int[] blended_hue_factor;
    private final int[] blend_direction_tracker;
    public final int[][][] tileHeights;
    public short[][][] overlays;
    public static int plane;
    private final byte[][][] tile_shadow_intensity;
    private final int[][][] tile_culling_bitsets;
    public byte[][][] overlayTypes;
    private static final int[] decor_x_offsets = {
            1, 0, -1, 0
    };
    private final int[][] tile_light_intensity;
    private static final int[] wall_orientations = {
            16, 32, 64, 128
    };
    public short[][][] underlays;
    private static final int[] decor_y_offsets = {
            0, -1, 0, 1
    };

    public static final int[] field713 = new int[]{1, 0, -1, 0};
    public static final int[] field725 = new int[]{0, -1, 0, 1};
    public static final int[] field715 = new int[]{1, -1, -1, 1};
    public static final int[] field721 = new int[]{1, 2, 4, 8};
    public static final int[] field722 = new int[]{16, 32, 64, 128};
    public static final int[] field726 = new int[]{-1, -1, 1, 1};
    public static int min_plane = 99;
    private final int region_size_x;
    private final int region_size_y;
    public final byte[][][] overlayOrientations;
    private final byte[][][] tile_flags;
    public static boolean low_detail = false;

    public MapRegion(byte[][][] flags, int[][][] heights) {
        min_plane = 99;
        region_size_x = 104;
        region_size_y = 104;
        tileHeights = heights;
        tile_flags = flags;
        underlays = new short[4][region_size_x][region_size_y];
        overlays = new short[4][region_size_x][region_size_y];
        overlayTypes = new byte[4][region_size_x][region_size_y];
        overlayOrientations = new byte[4][region_size_x][region_size_y];
        tile_culling_bitsets = new int[4][region_size_x + 1][region_size_y + 1];
        tile_shadow_intensity = new byte[4][region_size_x + 1][region_size_y + 1];
        tile_light_intensity = new int[region_size_x + 1][region_size_y + 1];
        blended_hue = new int[region_size_y];
        blended_saturation = new int[region_size_y];
        blended_lightness = new int[region_size_y];
        blended_hue_factor = new int[region_size_y];
        blend_direction_tracker = new int[region_size_y];
    }

    public void method736(int var0, int var1, int var2, int var3) {
        for (int var4 = var1; var4 <= var3 + var1; ++var4) {
            for (int var5 = var0; var5 <= var0 + var2; ++var5) {
                if (var5 >= 0 && var5 < 104 && var4 >= 0 && var4 < 104) {
                    tile_shadow_intensity[0][var5][var4] = 127;
                    if (var0 == var5 && var5 > 0) {
                        tileHeights[0][var5][var4] = tileHeights[0][var5 - 1][var4];
                    }

                    if (var5 == var0 + var2 && var5 < 103) {
                        tileHeights[0][var5][var4] = tileHeights[0][var5 + 1][var4];
                    }

                    if (var4 == var1 && var4 > 0) {
                        tileHeights[0][var5][var4] = tileHeights[0][var5][var4 - 1];
                    }

                    if (var4 == var3 + var1 && var4 < 103) {
                        tileHeights[0][var5][var4] = tileHeights[0][var5][var4 + 1];
                    }
                }
            }
        }
    }

    public void addPendingSpawnToScene(int z, int group, int x, int y, int id, int face, int type) {
        if (x >= 1 && y >= 1 && x <= 102 && y <= 102) {
            if (low_detail && z != instance.plane) {
                return;
            }

            long var8 = 0L;
            boolean var10 = true;
            boolean var11 = false;
            boolean var12 = false;
            if (group == 0) {
                var8 = instance.scene.getBoundaryObjectTag(z, x, y);
            }

            if (1 == group) {
                var8 = instance.scene.getWallDecorationTag(z, x, y);
            }

            if (2 == group) {
                var8 = instance.scene.getGameObjectTag(z, x, y);
            }

            if (group == 3) {
                var8 = instance.scene.getFloorDecorationTag(z, x, y);
            }

            int plane;
            if (0L != var8) {
                plane = instance.scene.getObjectFlags(z, x, y, var8);
                int var40 = ViewportMouse.Entity_unpackID(var8);
                int var41 = plane & 31;
                int var42 = plane >> 6 & 3;
                ObjectSound.updatePendingSound(z,var42,x,y,var42);
                ObjectDefinition def;
                if (0 == group) {
                    instance.scene.remove_wall(x, z, y);
                    def = ObjectDefinition.lookup(var40);
                    if (def.interactType != 0) {
                        instance.collisionMaps[z].removeWallObstruction(x, y, var41, var42, def.boolean1);
                    }
                }

                if (1 == group) {
                    instance.scene.remove_wall_decor(y, z, x);
                }

                if (2 == group) {
                    instance.scene.remove_object(z, x, y);
                    def = ObjectDefinition.lookup(var40);
                    if (def.sizeX + x > 103 || def.sizeX + y > 103 || def.sizeY + x > 103 || y + def.sizeY > 103) {
                        return;
                    }

                    if (def.interactType != 0) {
                        instance.collisionMaps[z].setFlagOffNonSquare(var42, def.sizeX, x, y, def.sizeY, def.boolean1);
                    }
                }

                if (3 == group) {
                    instance.scene.remove_ground_decor(z, y, x);
                    def = ObjectDefinition.lookup(var40);
                    if (def.interactType == 1) {
                        instance.collisionMaps[z].method3743(x, y);
                    }
                }
            }

            if (id >= 0) {
                plane = z;
                if (z < 3 && 2 == (tile_flags[1][x][y] & 2)) {
                    plane = z + 1;
                }

                SceneGraph var43 = instance.scene;
                CollisionMap var15 = instance.collisionMaps[z];
                ObjectDefinition var16 = ObjectDefinition.lookup(id);
                int var17;
                int var18;
                if (face != 1 && face != 3) {
                    var17 = var16.sizeX;
                    var18 = var16.sizeY;
                } else {
                    var17 = var16.sizeY;
                    var18 = var16.sizeX;
                }

                int var19;
                int var20;
                if (x + var17 <= 104) {
                    var19 = (var17 >> 1) + x;
                    var20 = x + (var17 + 1 >> 1);
                } else {
                    var19 = x;
                    var20 = 1 + x;
                }

                int var21;
                int var22;
                if (var18 + y <= 104) {
                    var21 = y + (var18 >> 1);
                    var22 = y + (1 + var18 >> 1);
                } else {
                    var21 = y;
                    var22 = y + 1;
                }

                int[][] var23 = tileHeights[plane];
                int var24 = var23[var20][var22] + var23[var19][var21] + var23[var20][var21] + var23[var19][var22] >> 2;
                int var25 = (var17 << 6) + (x << 7);
                int var26 = (var18 << 6) + (y << 7);
                long var27 = ObjectKeyUtil.calculateTag(x, y, 2, var16.int1 == 0, id);
                int var29 = (face << 6) + type;
                if (1 == var16.int3) {
                    var29 += 256;
                }

                if (var16.hasSound()) {
                    ObjectSound.addObjectSounds(z, x, y, var16, face);
                }

                Renderable var30;
                if (22 == type) {
                    if (-1 == var16.animation && var16.configs == null) {
                        var30 = var16.getModel(22, face, var23, var25, var24, var26);
                    } else {
                        var30 = new SceneObject(id, face, 22, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                    }

                    var43.newFloorDecoration(z, x, y, var24, var30, var27, var29);
                    if (1 == var16.interactType) {
                        var15.setBlockedByFloorDec(x, y);
                    }
                } else if (10 != type && type != 11) {
                    if (type >= 12) {
                        if (var16.animation == -1 && null == var16.configs) {
                            var30 = var16.getModel(type, face, var23, var25, var24, var26);
                        } else {
                            var30 = new SceneObject(id, face, type, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                        }

                        var43.method4166(z, x, y, var24, 1, 1, var30, 0, var27, var29);
                        if (var16.interactType != 0) {
                            var15.addGameObject(x, y, var17, var18, var16.boolean1);
                        }
                    } else if (0 == type) {
                        if (var16.animation == -1 && var16.configs == null) {
                            var30 = var16.getModel(0, face, var23, var25, var24, var26);
                        } else {
                            var30 = new SceneObject(id, face, 0, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                        }

                        var43.newBoundaryObject(z, x, y, var24, var30, (Renderable) null, field721[face], 0, var27, var29);
                        if (0 != var16.interactType) {
                            var15.removeWallObstruction(x, y, type, face, var16.boolean1);
                        }
                    } else if (1 == type) {
                        if (var16.animation == -1 && var16.configs == null) {
                            var30 = var16.getModel(1, face, var23, var25, var24, var26);
                        } else {
                            var30 = new SceneObject(id, face, 1, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                        }

                        var43.newBoundaryObject(z, x, y, var24, var30, (Renderable) null, field722[face], 0, var27, var29);
                        if (var16.interactType != 0) {
                            var15.removeWallObstruction(x, y, type, face, var16.boolean1);
                        }
                    } else {
                        int var36;
                        if (2 == type) {
                            var36 = face + 1 & 3;
                            Renderable var31;
                            Renderable var32;
                            if (-1 == var16.animation && null == var16.configs) {
                                var31 = var16.getModel(2, face + 4, var23, var25, var24, var26);
                                var32 = var16.getModel(2, var36, var23, var25, var24, var26);
                            } else {
                                var31 = new SceneObject(id, 4 + face, 2, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                                var32 = new SceneObject(id, var36, 2, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                            }

                            var43.newBoundaryObject(z, x, y, var24, var31, var32, field721[face], field721[var36], var27, var29);
                            if (var16.interactType != 0) {
                                var15.removeWallObstruction(x, y, type, face, var16.boolean1);
                            }
                        } else if (type == 3) {
                            if (-1 == var16.animation && null == var16.configs) {
                                var30 = var16.getModel(3, face, var23, var25, var24, var26);
                            } else {
                                var30 = new SceneObject(id, face, 3, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                            }

                            var43.newBoundaryObject(z, x, y, var24, var30, (Renderable) null, field722[face], 0, var27, var29);
                            if (0 != var16.interactType) {
                                var15.removeWallObstruction(x, y, type, face, var16.boolean1);
                            }
                        } else if (type == 9) {
                            if (-1 == var16.animation && null == var16.configs) {
                                var30 = var16.getModel(type, face, var23, var25, var24, var26);
                            } else {
                                var30 = new SceneObject(id, face, type, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                            }

                            var43.method4166(z, x, y, var24, 1, 1, var30, 0, var27, var29);
                            if (0 != var16.interactType) {
                                var15.addGameObject(x, y, var17, var18, var16.boolean1);
                            }
                        } else if (4 == type) {
                            if (-1 == var16.animation && var16.configs == null) {
                                var30 = var16.getModel(4, face, var23, var25, var24, var26);
                            } else {
                                var30 = new SceneObject(id, face, 4, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                            }

                            var43.newWallDecoration(z, x, y, var24, var30, (Renderable) null, field721[face], 0, 0, 0, var27, var29);
                        } else {
                            Renderable var33;
                            long var37;
                            if (type == 5) {
                                var36 = 16;
                                var37 = var43.getBoundaryObjectTag(z, x, y);
                                if (var37 != 0L) {
                                    var36 = ObjectDefinition.lookup(ViewportMouse.Entity_unpackID(var37)).int2;
                                }

                                if (var16.animation == -1 && var16.configs == null) {
                                    var33 = var16.getModel(4, face, var23, var25, var24, var26);
                                } else {
                                    var33 = new SceneObject(id, face, 4, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                                }

                                var43.newWallDecoration(z, x, y, var24, var33, (Renderable) null, field721[face], 0, field713[face] * var36, var36 * field725[face], var27, var29);
                            } else if (type == 6) {
                                var36 = 8;
                                var37 = var43.getBoundaryObjectTag(z, x, y);
                                if (0L != var37) {
                                    var36 = ObjectDefinition.lookup(ViewportMouse.Entity_unpackID(var37)).int2 / 2;
                                }

                                if (-1 == var16.animation && null == var16.configs) {
                                    var33 = var16.getModel(4, face + 4, var23, var25, var24, var26);
                                } else {
                                    var33 = new SceneObject(id, face + 4, 4, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                                }

                                var43.newWallDecoration(z, x, y, var24, var33, (Renderable) null, 256, face, var36 * field715[face], var36 * field726[face], var27, var29);
                            } else if (7 == type) {
                                int var39 = face + 2 & 3;
                                if (var16.animation == -1 && null == var16.configs) {
                                    var30 = var16.getModel(4, 4 + var39, var23, var25, var24, var26);
                                } else {
                                    var30 = new SceneObject(id, 4 + var39, 4, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                                }

                                var43.newWallDecoration(z, x, y, var24, var30, (Renderable) null, 256, var39, 0, 0, var27, var29);
                            } else if (8 == type) {
                                var36 = 8;
                                var37 = var43.getBoundaryObjectTag(z, x, y);
                                if (var37 != 0L) {
                                    var36 = ObjectDefinition.lookup(ViewportMouse.Entity_unpackID(var37)).int2 / 2;
                                }

                                int var35 = face + 2 & 3;
                                Renderable var34;
                                if (var16.animation == -1 && var16.configs == null) {
                                    var33 = var16.getModel(4, 4 + face, var23, var25, var24, var26);
                                    var34 = var16.getModel(4, 4 + var35, var23, var25, var24, var26);
                                } else {
                                    var33 = new SceneObject(id, face + 4, 4, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                                    var34 = new SceneObject(id, 4 + var35, 4, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                                }

                                var43.newWallDecoration(z, x, y, var24, var33, var34, 256, face, var36 * field715[face], var36 * field726[face], var27, var29);
                            }
                        }
                    }
                } else {
                    if (var16.animation == -1 && var16.configs == null) {
                        var30 = var16.getModel(10, face, var23, var25, var24, var26);
                    } else {
                        var30 = new SceneObject(id, face, 10, plane, x, y, var16.animation, var16.boolean3, (Renderable)null);
                    }

                    if (var30 != null) {
                        var43.method4166(z, x, y, var24, var17, var18, var30, 11 == type ? 256 : 0, var27, var29);
                    }

                    if (var16.interactType != 0) {
                        var15.addGameObject(x, y, var17, var18, var16.boolean1);
                    }
                }
            }
        }
    }

    public void addObjectsToScene(int z, int x, int y, int id, int face, int type, SceneGraph scene, CollisionMap collision) {
        if (!low_detail || 0 != (tile_flags[0][x][y] & 2) || (tile_flags[z][x][y] & 16) == 0) {
            if (z < min_plane) {
                min_plane = z;
            }

            ObjectDefinition var9 = ObjectDefinition.lookup(id);
            int var10;
            int var11;
            if (face != 1 && 3 != face) {
                var10 = var9.sizeX;
                var11 = var9.sizeY;
            } else {
                var10 = var9.sizeY;
                var11 = var9.sizeX;
            }

            int var12;
            int var13;
            if (x + var10 <= 104) {
                var12 = (var10 >> 1) + x;
                var13 = x + (var10 + 1 >> 1);
            } else {
                var12 = x;
                var13 = x + 1;
            }

            int var14;
            int var15;
            if (var11 + y <= 104) {
                var14 = y + (var11 >> 1);
                var15 = (var11 + 1 >> 1) + y;
            } else {
                var14 = y;
                var15 = y + 1;
            }

            int[][] heights = tileHeights[z];
            int mean = heights[var13][var15] + heights[var12][var15] + heights[var12][var14] + heights[var13][var14] >> 2;
            int centerX = (x << 7) + (var10 << 6);
            int centerY = (y << 7) + (var11 << 6);
            long var20 = ObjectKeyUtil.calculateTag(x, y, 2, 0 == var9.int1, id);
            int var22 = type + (face << 6);
            if (1 == var9.int3) {
                var22 += 256;
            }


            if (var9.hasSound()) {
                ObjectSound.addObjectSounds(z, x, y, var9, face);
            }


            int var24;
            int var25;

            Renderable renderable;
            if (22 == type) {
                if (!low_detail || 0 != var9.int1 || var9.interactType == 1 || var9.boolean2) {
                    if (var9.animation == -1 && null == var9.configs) {
                        renderable = var9.getEntity(22, face, heights, centerX, mean, centerY);
                    } else {
                        renderable = new SceneObject(id, face, 22, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                    }

                    scene.newFloorDecoration(z, x, y, mean, renderable, var20, var22);
                    if (1 == var9.interactType && null != collision) {
                        collision.setBlockedByFloorDec(x, y);
                    }
                }
            } else if (type != 10 && 11 != type) {
                if (type >= 12) {
                    if (var9.animation == -1 && null == var9.configs) {
                        renderable = var9.getEntity(type, face, heights, centerX, mean, centerY);
                    } else {
                        renderable = new SceneObject(id, face, type, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                    }

                    scene.method4166(z, x, y, mean, 1, 1, renderable, 0, var20, var22);
                    if (type >= 12 && type <= 17 && 13 != type && z > 0) {
                        tile_culling_bitsets[z][x][y] |= 2340;
                    }

                    if (0 != var9.interactType && null != collision) {
                        collision.addGameObject(x, y, var10, var11, var9.boolean1);
                    }

                } else if (type == 0) {
                    if (-1 == var9.animation && null == var9.configs) {
                        renderable = var9.getEntity(0, face, heights, centerX, mean, centerY);
                    } else {
                        renderable = new SceneObject(id, face, 0, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                    }

                    scene.newBoundaryObject(z, x, y, mean, renderable, (Renderable) null, field721[face], 0, var20, var22);
                    if (0 == face) {
                        if (var9.clipped) {
                            tile_shadow_intensity[z][x][y] = 50;
                            tile_shadow_intensity[z][x][y + 1] = 50;
                        }

                        if (var9.modelClipped) {
                            tile_culling_bitsets[z][x][y] |= 585;
                        }
                    } else if (1 == face) {
                        if (var9.clipped) {
                            tile_shadow_intensity[z][x][y + 1] = 50;
                            tile_shadow_intensity[z][x + 1][1 + y] = 50;
                        }

                        if (var9.modelClipped) {
                            tile_culling_bitsets[z][x][1 + y] |= 1170;
                        }
                    } else if (2 == face) {
                        if (var9.clipped) {
                            tile_shadow_intensity[z][1 + x][y] = 50;
                            tile_shadow_intensity[z][x + 1][1 + y] = 50;
                        }

                        if (var9.modelClipped) {
                            tile_culling_bitsets[z][x + 1][y] |= 585;
                        }
                    } else if (3 == face) {
                        if (var9.clipped) {
                            tile_shadow_intensity[z][x][y] = 50;
                            tile_shadow_intensity[z][1 + x][y] = 50;
                        }

                        if (var9.modelClipped) {
                            tile_culling_bitsets[z][x][y] |= 1170;
                        }
                    }

                    if (0 != var9.interactType && null != collision) {
                        collision.addWallObstruction(x, y, type, face, var9.boolean1);
                    }

                    if (16 != var9.int2) {
                        scene.method4138(z, x, y, var9.int2);
                    }

                } else if (1 == type) {
                    if (-1 == var9.animation && null == var9.configs) {
                        renderable = var9.getEntity(1, face, heights, centerX, mean, centerY);
                    } else {
                        renderable = new SceneObject(id, face, 1, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                    }

                    scene.newBoundaryObject(z, x, y, mean, renderable, (Renderable) null, field722[face], 0, var20, var22);
                    if (var9.clipped) {
                        if (0 == face) {
                            tile_shadow_intensity[z][x][1 + y] = 50;
                        } else if (1 == face) {
                            tile_shadow_intensity[z][x + 1][y + 1] = 50;
                        } else if (2 == face) {
                            tile_shadow_intensity[z][1 + x][y] = 50;
                        } else if (3 == face) {
                            tile_shadow_intensity[z][x][y] = 50;
                        }
                    }

                    if (var9.interactType != 0 && collision != null) {
                        collision.addWallObstruction(x, y, type, face, var9.boolean1);
                    }

                } else {
                    int var29;
                    if (2 == type) {
                        var29 = 1 + face & 3;
                        Renderable var30;
                        Renderable var31;
                        if (var9.animation == -1 && var9.configs == null) {
                            var30 = var9.getEntity(2, 4 + face, heights, centerX, mean, centerY);
                            var31 = var9.getEntity(2, var29, heights, centerX, mean, centerY);
                        } else {
                            var30 = new SceneObject(id, face + 4, 2, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                            var31 = new SceneObject(id, var29, 2, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                        }

                        scene.newBoundaryObject(z, x, y, mean, var30, var31, field721[face], field721[var29], var20, var22);
                        if (var9.modelClipped) {
                            if (face == 0) {
                                tile_culling_bitsets[z][x][y] |= 585;
                                tile_culling_bitsets[z][x][1 + y] |= 1170;
                            } else if (1 == face) {
                                tile_culling_bitsets[z][x][y + 1] |= 1170;
                                tile_culling_bitsets[z][x + 1][y] |= 585;
                            } else if (2 == face) {
                                tile_culling_bitsets[z][1 + x][y] |= 585;
                                tile_culling_bitsets[z][x][y] |= 1170;
                            } else if (face == 3) {
                                tile_culling_bitsets[z][x][y] |= 1170;
                                tile_culling_bitsets[z][x][y] |= 585;
                            }
                        }

                        if (var9.interactType != 0 && collision != null) {
                            collision.addWallObstruction(x, y, type, face, var9.boolean1);
                        }

                        if (var9.int2 != 16) {
                            scene.method4138(z, x, y, var9.int2);
                        }

                    } else if (type == 3) {
                        if (-1 == var9.animation && var9.configs == null) {
                            renderable = var9.getEntity(3, face, heights, centerX, mean, centerY);
                        } else {
                            renderable = new SceneObject(id, face, 3, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                        }

                        scene.newBoundaryObject(z, x, y, mean, renderable, (Renderable) null, field722[face], 0, var20, var22);
                        if (var9.clipped) {
                            if (face == 0) {
                                tile_shadow_intensity[z][x][y + 1] = 50;
                            } else if (1 == face) {
                                tile_shadow_intensity[z][1 + x][y + 1] = 50;
                            } else if (2 == face) {
                                tile_shadow_intensity[z][x + 1][y] = 50;
                            } else if (3 == face) {
                                tile_shadow_intensity[z][x][y] = 50;
                            }
                        }

                        if (var9.interactType != 0 && collision != null) {
                            collision.addWallObstruction(x, y, type, face, var9.boolean1);
                        }

                    } else if (9 == type) {
                        if (-1 == var9.animation && null == var9.configs) {
                            renderable = var9.getEntity(type, face, heights, centerX, mean, centerY);
                        } else {
                            renderable = new SceneObject(id, face, type, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                        }

                        scene.method4166(z, x, y, mean, 1, 1, renderable, 0, var20, var22);
                        if (0 != var9.interactType && collision != null) {
                            collision.addGameObject(x, y, var10, var11, var9.boolean1);
                        }

                        if (var9.int2 != 16) {
                            scene.method4138(z, x, y, var9.int2);
                        }

                    } else if (4 == type) {
                        if (-1 == var9.animation && var9.configs == null) {
                            renderable = var9.getEntity(4, face, heights, centerX, mean, centerY);
                        } else {
                            renderable = new SceneObject(id, face, 4, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                        }

                        scene.newWallDecoration(z, x, y, mean, renderable, (Renderable) null, field721[face], 0, 0, 0, var20, var22);
                    } else {
                        long var32;
                        Renderable var34;
                        if (type == 5) {
                            var29 = 16;
                            var32 = scene.getBoundaryObjectTag(z, x, y);
                            if (var32 != 0L) {
                                var29 = ObjectDefinition.lookup(ViewportMouse.Entity_unpackID(var32)).int2;
                            }

                            if (-1 == var9.animation && null == var9.configs) {
                                var34 = var9.getEntity(4, face, heights, centerX, mean, centerY);
                            } else {
                                var34 = new SceneObject(id, face, 4, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                            }

                            scene.newWallDecoration(z, x, y, mean, var34, (Renderable) null, field721[face], 0, field713[face] * var29, var29 * field725[face], var20, var22);
                        } else if (6 == type) {
                            var29 = 8;
                            var32 = scene.getBoundaryObjectTag(z, x, y);
                            if (var32 != 0L) {
                                var29 = ObjectDefinition.lookup(ViewportMouse.Entity_unpackID(var32)).int2 / 2;
                            }

                            if (var9.animation == -1 && null == var9.configs) {
                                var34 = var9.getEntity(4, face + 4, heights, centerX, mean, centerY);
                            } else {
                                var34 = new SceneObject(id, 4 + face, 4, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                            }

                            scene.newWallDecoration(z, x, y, mean, var34, (Renderable) null, 256, face, var29 * field715[face], var29 * field726[face], var20, var22);
                        } else if (type == 7) {
                            var24 = face + 2 & 3;
                            if (var9.animation == -1 && null == var9.configs) {
                                renderable = var9.getEntity(4, var24 + 4, heights, centerX, mean, centerY);
                            } else {
                                renderable = new SceneObject(id, 4 + var24, 4, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                            }

                            scene.newWallDecoration(z, x, y, mean, renderable, (Renderable) null, 256, var24, 0, 0, var20, var22);
                        } else if (8 == type) {
                            var29 = 8;
                            var32 = scene.getBoundaryObjectTag(z, x, y);
                            if (var32 != 0L) {
                                var29 = ObjectDefinition.lookup(ViewportMouse.Entity_unpackID(var32)).int2 / 2;
                            }

                            int var28 = face + 2 & 3;
                            Renderable var27;
                            if (-1 == var9.animation && null == var9.configs) {
                                var34 = var9.getEntity(4, face + 4, heights, centerX, mean, centerY);
                                var27 = var9.getEntity(4, 4 + var28, heights, centerX, mean, centerY);
                            } else {
                                var34 = new SceneObject(id, face + 4, 4, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                                var27 = new SceneObject(id, var28 + 4, 4, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                            }

                            scene.newWallDecoration(z, x, y, mean, var34, var27, 256, face, field715[face] * var29, field726[face] * var29, var20, var22);
                        }
                    }
                }
            } else {
                try {
                    if (-1 == var9.animation && var9.configs == null) {
                        renderable = var9.getEntity(10, face, heights, centerX, mean, centerY);
                    } else {
                        renderable = new SceneObject(id, face, 10, z, x, y, var9.animation, var9.boolean3, (Renderable)null);
                    }

                    if (renderable != null && scene.method4166(z, x, y, mean, var10, var11, renderable, 11 == type ? 256 : 0, var20, var22) && var9.clipped) {
                        var24 = 15;
                        if (renderable instanceof Model) {
                            var24 = ((Model) renderable).getShadowIntensity() / 4;
                            if (var24 > 30) {
                                var24 = 30;
                            }
                        }

                        for (var25 = 0; var25 <= var10; ++var25) {
                            for (int var26 = 0; var26 <= var11; ++var26) {
                                if (var24 > tile_shadow_intensity[z][var25 + x][var26 + y]) {
                                    tile_shadow_intensity[z][x + var25][var26 + y] = (byte) var24;
                                }
                            }
                        }
                    }

                    if (0 != var9.interactType && collision != null) {
                        collision.addGameObject(x, y, var10, var11, var9.boolean1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean cached(int object_id, int type) {
        ObjectDefinition def = ObjectDefinition.lookup(object_id);
        if (type == 11)
            type = 10;

        if (type >= 5 && type <= 8)
            type = 4;

        return def.modelTypeCached(type);
    }

    public int rndLightness = (int) (Math.random() * 33.0D) - 16;

    public int rndHue = (int) (Math.random() * 17.0D) - 8;

    public final void method3968(byte[] arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, CollisionMap[] arg8) {
        int var11;
        for(int var10 = 0; var10 < 8; ++var10) {
            for(var11 = 0; var11 < 8; ++var11) {
                if (arg2 + var10 > 0 && var10 + arg2 < 103 && var11 + arg3 > 0 && arg3 + var11 < 103) {
                    arg8[arg1].adjacencies[var10 + arg2][var11 + arg3] &= -16777217;
                }
            }
        }

        Buffer var21 = new Buffer(arg0);

        for(var11 = 0; var11 < 4; ++var11) {
            for(int var12 = 0; var12 < 64; ++var12) {
                for(int var13 = 0; var13 < 64; ++var13) {
                    if (var11 == arg4 && var12 >= arg5 && var12 < 8 + arg5 && var13 >= arg6 && var13 < 8 + arg6) {
                        int var18 = var12 & 7;
                        int var19 = var13 & 7;
                        int var20 = arg7 & 3;
                        int var17;
                        if (0 == var20) {
                            var17 = var18;
                        } else if (1 == var20) {
                            var17 = var19;
                        } else if (2 == var20) {
                            var17 = 7 - var18;
                        } else {
                            var17 = 7 - var19;
                        }

                        loadMapTerrain(var21, arg1, var17 + arg2, arg3 + method3498(var12 & 7, var13 & 7, arg7), 0, 0, arg7);
                    } else {
                        loadMapTerrain(var21, 0, -1, -1, 0, 0, 0);
                    }
                }
            }
        }
    }

    public void load_sub_terrain_block(byte[] var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, CollisionMap[] var10) {
        int var12;
        for (int var11 = 0; var11 < 8; ++var11) {
            for (var12 = 0; var12 < 8; ++var12) {
                if (var11 + var2 > 0 && var11 + var2 < 103 && var3 + var12 > 0 && var3 + var12 < 103) {
                    var10[var1].adjacencies[var11 + var2][var12 + var3] &= -16777217;
                }
            }
        }

        Buffer var24 = new Buffer(var0);

        for (var12 = 0; var12 < 4; ++var12) {
            for (int var13 = 0; var13 < 64; ++var13) {
                for (int var14 = 0; var14 < 64; ++var14) {
                    if (var12 == var4 && var13 >= var5 && var13 < var5 + 8 && var14 >= var6 && var14 < var6 + 8) {
                        int var15 = var2 + method1682(var13 & 7, var14 & 7, var7);
                        int var18 = var13 & 7;
                        int var19 = var14 & 7;
                        int var20 = var7 & 3;
                        int var17;
                        if (var20 == 0) {
                            var17 = var19;
                        } else if (var20 == 1) {
                            var17 = 7 - var18;
                        } else if (var20 == 2) {
                            var17 = 7 - var19;
                        } else {
                            var17 = var18;
                        }

                        int var21 = var3 + var17;
                        int var22 = (var13 & 7) + var8 + var2;
                        int var23 = var3 + (var14 & 7) + var9;
                        loadMapTerrain(var24, var1, var15, var21, var22, var23, var7);
                    } else {
                        loadMapTerrain(var24, 0, -1, -1, 0, 0, 0);
                    }
                }
            }
        }
    }

    public static int method3498(int arg0, int arg1, int arg2) {
        int var3 = arg2 & 3;
        if (var3 == 0) {
            return arg1;
        } else if (1 == var3) {
            return 7 - arg0;
        } else {
            return 2 == var3 ? 7 - arg1 : arg0;
        }
    }

    public static int method1682(int var0, int var1, int var2) {
        var2 &= 3;
        if (var2 == 0) {
            return var0;
        } else if (var2 == 1) {
            return var1;
        } else {
            return var2 == 2 ? 7 - var0 : 7 - var1;
        }
    }

    public final void decode_map_terrain(byte[] arg0, int arg1, int arg2, int arg3, int arg4, CollisionMap[] arg5) {
        int var8;
        int var9;
        for (int var7 = 0; var7 < 4; ++var7) {
            for (var8 = 0; var8 < 64; ++var8) {
                for (var9 = 0; var9 < 64; ++var9) {
                    if (var8 + arg1 > 0 && var8 + arg1 < 103 && arg2 + var9 > 0 && arg2 + var9 < 103) {
                        arg5[var7].adjacencies[var8 + arg1][var9 + arg2] &= -16777217;
                    }
                }
            }
        }

        Buffer var11 = new Buffer(arg0);

        for (var8 = 0; var8 < 4; ++var8) {
            for (var9 = 0; var9 < 64; ++var9) {
                for (int var10 = 0; var10 < 64; ++var10) {
                    loadMapTerrain(var11, var8, arg1 + var9, arg2 + var10, arg3, arg4, 0);
                }
            }
        }

    }

    static final int method431(int arg0, int arg1) {
        int var3 = method4727(arg0 - 1, arg1 - 1) + method4727(arg0 + 1, arg1 - 1) + method4727(arg0 - 1, arg1 + 1) + method4727(1 + arg0, arg1 + 1);
        int var4 = method4727(arg0 - 1, arg1) + method4727(1 + arg0, arg1) + method4727(arg0, arg1 - 1) + method4727(arg0, arg1 + 1);
        int var5 = method4727(arg0, arg1);
        return var5 / 4 + var4 / 8 + var3 / 16;
    }

    static final int method4727(int arg0, int arg1) {
        int var3 = arg0 + arg1 * 57;
        int var5 = var3 << 13 ^ var3;
        int var4 = 1376312589 + (var5 * var5 * 15731 + 789221) * var5 & 2147483647;
        return var4 >> 19 & 255;
    }

    public static final int method6259(int arg0, int arg1, int arg2) {
        int var4 = arg0 / arg2;
        int var5 = arg0 & arg2 - 1;
        int var6 = arg1 / arg2;
        int var7 = arg1 & arg2 - 1;
        int var8 = method431(var4, var6);
        int var9 = method431(1 + var4, var6);
        int var10 = method431(var4, 1 + var6);
        int var11 = method431(1 + var4, 1 + var6);
        int var13 = 65536 - Rasterizer3D.COSINE[1024 * var5 / arg2] >> 1;
        int var12 = (var8 * (65536 - var13) >> 16) + (var9 * var13 >> 16);
        int var15 = 65536 - Rasterizer3D.COSINE[var5 * 1024 / arg2] >> 1;
        int var14 = ((65536 - var15) * var10 >> 16) + (var11 * var15 >> 16);
        int var17 = 65536 - Rasterizer3D.COSINE[var7 * 1024 / arg2] >> 1;
        return (var14 * var17 >> 16) + (var12 * (65536 - var17) >> 16);
    }

    public static final int method53(int arg0, int arg1) {
        int var3 = method6259(arg0 + 45365, arg1 + 91923, 4) - 128 + (method6259(10294 + arg0, 37821 + arg1, 2) - 128 >> 1) + (method6259(arg0, arg1, 1) - 128 >> 2);
        var3 = 35 + (int)((double)var3 * 0.3D);
        if (var3 < 10) {
            var3 = 10;
        } else if (var3 > 60) {
            var3 = 60;
        }

        return var3;
    }

    public final void loadMapTerrain(Buffer arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        try {
            int var8;
            if (arg2 >= 0 && arg2 < 104 && arg3 >= 0 && arg3 < 104) {
                tile_flags[arg1][arg2][arg3] = 0;

                while (true) {
                    var8 = arg0.readUShort();
                    if (0 == var8) {
                        if (0 == arg1) {
                            tileHeights[0][arg2][arg3] = -method53(932731 + arg2 + arg4, arg5 + 556238 + arg3) * 8;
                        } else {
                            tileHeights[arg1][arg2][arg3] = tileHeights[arg1 - 1][arg2][arg3] - 240;
                        }
                        break;
                    }

                    if (1 == var8) {
                        int var9 = arg0.readUnsignedByte();
                        if (var9 == 1) {
                            var9 = 0;
                        }

                        if (arg1 == 0) {
                            tileHeights[0][arg2][arg3] = -var9 * 8;
                        } else {
                            tileHeights[arg1][arg2][arg3] = tileHeights[arg1 - 1][arg2][arg3] - 8 * var9;
                        }
                        break;
                    }

                    if (var8 <= 49) {
                        overlays[arg1][arg2][arg3] = (short) arg0.readShort();
                        overlayTypes[arg1][arg2][arg3] = (byte) ((var8 - 2) / 4);
                        overlayOrientations[arg1][arg2][arg3] = (byte) (arg6 + (var8 - 2) & 3);
                    } else if (var8 <= 81) {
                        tile_flags[arg1][arg2][arg3] = (byte) (var8 - 49);
                    } else {
                        underlays[arg1][arg2][arg3] = (short) (var8 - 81);
                    }
                }
            } else {
                while (true) {
                    var8 = arg0.readUShort();
                    if (var8 == 0) {
                        break;
                    }

                    if (1 == var8) {
                        arg0.readUnsignedByte();
                        break;
                    }

                    if (var8 <= 49) {
                        arg0.readShort();
                    }
                }
            }

        }catch (Exception e) {
            System.out.println("Isuee Loading map please report with cords");
            //e.printStackTrace();
        }

    }

    public void load_terrain_block(byte[] var0, int var1, int var2, int var3, int var4, CollisionMap[] var5) {
        int var7;
        int var8;
        for (int var6 = 0; var6 < 4; ++var6) {
            for (var7 = 0; var7 < 64; ++var7) {
                for (var8 = 0; var8 < 64; ++var8) {
                    if (var7 + var1 > 0 && var7 + var1 < 103 && var8 + var2 > 0 && var8 + var2 < 103) {
                        var5[var6].adjacencies[var7 + var1][var8 + var2] &= -16777217;
                    }
                }
            }
        }

        Buffer var12 = new Buffer(var0);

        for (var7 = 0; var7 < 4; ++var7) {
            for (var8 = 0; var8 < 64; ++var8) {
                for (int var9 = 0; var9 < 64; ++var9) {
                    int var10 = var8 + var1;
                    int var11 = var9 + var2;
                    loadTerrain(var12, var7, var10, var11, var3 + var10, var11 + var4, 0);
                }
            }
        }
    }

    public void loadTerrain(Buffer var0, int var1, int var2, int var3, int var4, int var5, int var6) {
        boolean var7 = var1 >= 0 && var1 < 4 && var2 >= 0 && var2 < 104 && var3 >= 0 && var3 < 104;
        int var8;
        if (var7) {
            tile_flags[var1][var2][var3] = 0;

            while (true) {
                var8 = var0.readUShort();
                if (var8 == 0) {
                    if (var1 == 0) {
                        int[] var9 = tileHeights[0][var2];
                        int var12 = var4 + 932731;
                        int var13 = var5 + 556238;
                        int var14 = CalcHeights.perlinNoise(var12 + '넵', var13 + 91923, 4) - 128 + (CalcHeights.method634(var12 + 10294, var13 + '鎽', 2) - 128 >> 1) + (CalcHeights.method634(var12, var13, 1) - 128 >> 2);
                        var14 = (int) ((double) var14 * 0.3D) + 35;
                        if (var14 < 10) {
                            var14 = 10;
                        } else if (var14 > 60) {
                            var14 = 60;
                        }

                        var9[var3] = -var14 * 8;
                    } else {
                        tileHeights[var1][var2][var3] = tileHeights[var1 - 1][var2][var3] - 240;
                    }
                    break;
                }

                if (var8 == 1) {
                    int var15 = var0.readUnsignedByte();
                    if (var15 == 1) {
                        var15 = 0;
                    }

                    if (var1 == 0) {
                        tileHeights[0][var2][var3] = -var15 * 8;
                    } else {
                        tileHeights[var1][var2][var3] = tileHeights[var1 - 1][var2][var3] - var15 * 8;
                    }
                    break;
                }

                if (var8 <= 49) {
                    overlays[var1][var2][var3] = (short) var0.readShort();
                    overlayTypes[var1][var2][var3] = (byte) ((var8 - 2) / 4);
                    overlayOrientations[var1][var2][var3] = (byte) (var8 - 2 + var6 & 3);
                } else if (var8 <= 81) {
                    tile_flags[var1][var2][var3] = (byte) (var8 - 49);
                } else {
                    underlays[var1][var2][var3] = (short) (var8 - 81);
                }
            }
        } else {
            while (true) {
                var8 = var0.readUShort();
                if (var8 == 0) {
                    break;
                }

                if (var8 == 1) {
                    var0.readUnsignedByte();
                    break;
                }

                if (var8 <= 49) {
                    var0.readShort();
                }
            }
        }
    }

    public int method425(int var0, int var1) {
        if (var0 == -2) {
            return 12345678;
        } else if (var0 == -1) {
            if (var1 < 2) {
                var1 = 2;
            } else if (var1 > 126) {
                var1 = 126;
            }

            return var1;
        } else {
            var1 = (var0 & 127) * var1 / 128;
            if (var1 < 2) {
                var1 = 2;
            } else if (var1 > 126) {
                var1 = 126;
            }

            return (var0 & 0xFF80) + var1;
        }
    }

    public int method1766(int var0, int var1) {
        if (var0 == -1) {
            return 12345678;
        } else {
            var1 = (var0 & 127) * var1 / 128;
            if (var1 < 2) {
                var1 = 2;
            } else if (var1 > 126) {
                var1 = 126;
            }

            return (var0 & 0xFF80) + var1;
        }
    }

    public void method6042(SceneGraph arg0, CollisionMap[] arg1) {
        int var3;
        int var4;
        int var5;
        int plane;
        for (var3 = 0; var3 < 4; ++var3) {
            for (var4 = 0; var4 < 104; ++var4) {
                for (var5 = 0; var5 < 104; ++var5) {
                    if (1 == (tile_flags[var3][var4][var5] & 1)) {
                        plane = var3;
                        if (2 == (tile_flags[1][var4][var5] & 2)) {
                            plane = var3 - 1;
                        }

                        if (plane >= 0) {
                            arg1[plane].setBlockedByFloor(var4, var5);
                        }
                    }
                }
            }
        }

        rndHue += (int) (Math.random() * 5.0D) - 2;
        if (rndHue < -8) {
            rndHue = -8;
        }

        if (rndHue > 8) {
            rndHue = 8;
        }

        rndLightness += (int) (Math.random() * 5.0D) - 2;
        if (rndLightness < -16) {
            rndLightness = -16;
        }

        if (rndLightness > 16) {
            rndLightness = 16;
        }

        int var10;
        int var11;
        int var12;
        int var13;
        int var14;
        int var15;
        int var16;
        int var17;
        int var18;
        int var19;
        for (var3 = 0; var3 < 4; ++var3) {
            byte[][] var46 = tile_shadow_intensity[var3];
            boolean var56 = true;
            boolean var57 = true;
            boolean var7 = true;
            boolean var8 = true;
            boolean var9 = true;
            var10 = (int) Math.sqrt(5100.0D);
            var11 = var10 * 768 >> 8;//213

            int var20;
            int var21;
            for (var12 = 1; var12 < 103; ++var12) {
                for (var13 = 1; var13 < 103; ++var13) {
                    var14 = tileHeights[var3][1 + var13][var12] - tileHeights[var3][var13 - 1][var12];
                    var15 = tileHeights[var3][var13][var12 + 1] - tileHeights[var3][var13][var12 - 1];
                    var16 = (int) Math.sqrt((double) (var15 * var15 + 65536 + var14 * var14));
                    var17 = (var14 << 8) / var16;
                    var18 = 65536 / var16;
                    var19 = (var15 << 8) / var16;
                    var20 = 96 + (-50 * var19 + var17 * -50 + -10 * var18) / var11;
                    var21 = (var46[1 + var13][var12] >> 3) + (var46[var13 - 1][var12] >> 2) + (var46[var13][var12 - 1] >> 2) + (var46[var13][var12 + 1] >> 3) + (var46[var13][var12] >> 1);
                    tile_light_intensity[var13][var12] = var20 - var21;
                }
            }

            for (var12 = 0; var12 < 104; ++var12) {
                blended_hue[var12] = 0;
                blended_saturation[var12] = 0;
                blended_lightness[var12] = 0;
                blended_hue_factor[var12] = 0;
                blend_direction_tracker[var12] = 0;
            }

            for (var12 = -5; var12 < 109; ++var12) {
                for (var13 = 0; var13 < 104; ++var13) {
                    var14 = 5 + var12;
                    int var10002;
                    if (var14 >= 0 && var14 < 104) {
                        var15 = (int) BitTools.nbit_max_unsigned(15);
                        var16 = underlays[var3][var14][var13] & var15;
                        if (var16 > 0) {
                            var18 = var16 - 1;
                            FloorUnderlayDefinition var47 = FloorUnderlayDefinition.lookup((int) var18);
                            FloorUnderlayDefinition var48 = null;
                            if (null != var47) {
                                var48 = var47;
                            }

                            blended_hue[var13] += var48.getHue();
                            blended_saturation[var13] += var48.getSaturation();
                            blended_lightness[var13] += var48.getLightness();
                            blended_hue_factor[var13] += var48.getHueMultiplier();
                            var10002 = blend_direction_tracker[var13]++;
                        }
                    }

                    var15 = var12 - 5;
                    if (var15 >= 0 && var15 < 104) {
                        var16 = (int) BitTools.nbit_max_unsigned(15);
                        var17 = underlays[var3][var15][var13] & var16;
                        if (var17 > 0) {
                            var19 = var17 - 1;
                            FloorUnderlayDefinition var65 = FloorUnderlayDefinition.lookup(var19);
                            FloorUnderlayDefinition var50 = null;
                            if (var65 != null) {
                                var50 = var65;
                            }

                            blended_hue[var13] -= var50.getHue();
                            blended_saturation[var13] -= var50.getSaturation();
                            blended_lightness[var13] -= var50.getLightness();
                            blended_hue_factor[var13] -= var50.getHueMultiplier();
                            var10002 = blend_direction_tracker[var13]--;
                        }
                    }
                }

                if (var12 >= 1 && var12 < 103) {
                    var13 = 0;
                    var14 = 0;
                    var15 = 0;
                    var16 = 0;
                    var17 = 0;

                    for (var18 = -5; var18 < 109; ++var18) {
                        var19 = 5 + var18;
                        if (var19 >= 0 && var19 < 104) {
                            var13 += blended_hue[var19];
                            var14 += blended_saturation[var19];
                            var15 += blended_lightness[var19];
                            var16 += blended_hue_factor[var19];
                            var17 += blend_direction_tracker[var19];
                        }

                        var20 = var18 - 5;
                        if (var20 >= 0 && var20 < 104) {
                            var13 -= blended_hue[var20];
                            var14 -= blended_saturation[var20];
                            var15 -= blended_lightness[var20];
                            var16 -= blended_hue_factor[var20];
                            var17 -= blend_direction_tracker[var20];
                        }

                        if (var18 >= 1 && var18 < 103 && (!low_detail || 0 != (tile_flags[0][var12][var18] & 2) || 0 == (tile_flags[var3][var12][var18] & 16))) {
                            if (var3 < min_plane) {
                                min_plane = var3;
                            }

                            var21 = (int) BitTools.nbit_max_unsigned(15);
                            int var22 = underlays[var3][var12][var18] & var21;
                            int var23 = overlays[var3][var12][var18] & var21;
                            if (var22 > 0 || var23 > 0) {
                                int var24 = tileHeights[var3][var12][var18];
                                int var25 = tileHeights[var3][var12 + 1][var18];
                                int var26 = tileHeights[var3][1 + var12][1 + var18];
                                int var27 = tileHeights[var3][var12][1 + var18];
                                int var28 = tile_light_intensity[var12][var18];
                                int var29 = tile_light_intensity[var12 + 1][var18];
                                int var30 = tile_light_intensity[var12 + 1][var18 + 1];
                                int var31 = tile_light_intensity[var12][var18 + 1];
                                int var32 = -1;
                                int var33 = -1;
                                int var34;
                                int var35;
                                int var36;
                                if (var22 > 0) {
                                    var34 = 256 * var13 / var16;
                                    var35 = var14 / var17;
                                    var36 = var15 / var17;
                                    var32 = set_hsl_bitset(var34, var35, var36);
                                    int var62 = rndHue + var34 & 255;
                                    var36 += rndLightness;
                                    if (var36 < 0) {
                                        var36 = 0;
                                    } else if (var36 > 255) {
                                        var36 = 255;
                                    }

                                    var33 = set_hsl_bitset(var62, var35, var36);
                                }

                                FloorOverlayDefinition var37 = null;
                                if (var3 > 0) {
                                    boolean var63 = true;
                                    if (var22 == 0 && overlayTypes[var3][var12][var18] != 0) {
                                        var63 = false;
                                    }

                                    if (var23 > 0) {
                                        var36 = var23 - 1;
                                        var37 = FloorOverlayDefinition.lookup(var36);
                                        FloorOverlayDefinition var52 = null;
                                        if (null != var37) {
                                            var52 = var37;
                                        }

                                        if (!var52.isHideUnderlay()) {
                                            var63 = false;
                                        }
                                    }

                                    if (var63 && var24 == var25 && var26 == var24 && var27 == var24) {
                                        tile_culling_bitsets[var3][var12][var18] |= 2340;
                                    }
                                }

                                var34 = 0;
                                if (-1 != var33) {
                                    var34 = Rasterizer3D.hslToRgb[method1766(var33, 96)];
                                }

                                if (var23 == 0) {
                                    arg0.add_tile(var3, var12, var18, 0, 0, -1, var24, var25, var26, var27, method1766(var32, var28), method1766(var32, var29), method1766(var32, var30), method1766(var32, var31), 0, 0, 0, 0, var34, 0);
                                } else {
                                    var35 = overlayTypes[var3][var12][var18] + 1;
                                    byte var64 = overlayOrientations[var3][var12][var18];
                                    int var38 = var23 - 1;
                                    FloorOverlayDefinition var39 = FloorOverlayDefinition.lookup(var38);
                                    if (null != var39) {
                                        var37 = var39;
                                    }

                                    int var40 = var37.getTexture();
                                    int var41;
                                    int var42;
                                    int var43;
                                    int var44;
                                    if (var40 >= 0) {
                                        var42 = Rasterizer3D.clips.textureLoader.getAverageTextureRGB(var40);
                                        var41 = -1;
                                    } else if (16711935 == var37.getPrimaryRgb()) {
                                        var41 = -2;
                                        var40 = -1;
                                        var42 = -2;
                                    } else {
                                        var41 = set_hsl_bitset(var37.getHue(), var37.getSaturation(), var37.getLightness());
                                        var43 = var37.getHue() + rndHue & 255;
                                        var44 = rndLightness + var37.getLightness();
                                        if (var44 < 0) {
                                            var44 = 0;
                                        } else if (var44 > 255) {
                                            var44 = 255;
                                        }

                                        var42 = set_hsl_bitset(var43, var37.getSaturation(), var44);
                                    }

                                    var43 = 0;
                                    if (var42 != -2) {
                                        var43 = Rasterizer3D.hslToRgb[method425(var42, 96)];
                                    }

                                    if (var37.secondaryRgb != -1) {
                                        var44 = rndHue + var37.secondaryHue & 255;
                                        int var45 = var37.secondaryLightness + rndLightness;
                                        if (var45 < 0) {
                                            var45 = 0;
                                        } else if (var45 > 255) {
                                            var45 = 255;
                                        }

                                        var42 = set_hsl_bitset(var44, var37.secondarySaturation, var45);
                                        var43 = Rasterizer3D.hslToRgb[method425(var42, 96)];
                                    }

                                    arg0.add_tile(var3, var12, var18, var35, var64, var40, var24, var25, var26, var27, method1766(var32, var28), method1766(var32, var29), method1766(var32, var30), method1766(var32, var31), method425(var41, var28), method425(var41, var29), method425(var41, var30), method425(var41, var31), var34, var43);
                                }
                            }
                        }
                    }
                }
            }

            for (var12 = 1; var12 < 103; ++var12) {
                for (var13 = 1; var13 < 103; ++var13) {
                    if ((tile_flags[var3][var13][var12] & 8) != 0) {
                        var18 = 0;
                    } else if (var3 > 0 && 0 != (tile_flags[1][var13][var12] & 2)) {
                        var18 = var3 - 1;
                    } else {
                        var18 = var3;
                    }

                    arg0.setTileMinPlane(var3, var13, var12, var18);
                }
            }

            //underlays[var3] = (short[][])null;
            //overlays[var3] = (short[][])null;
            //overlayTypes[var3] = (byte[][])null;
            //overlayOrientations[var3] = (byte[][])null;
            //tile_shadow_intensity[var3] = (byte[][])null;

        }

        arg0.method4153(-50, -10, -50);

        for (var4 = 0; var4 < 104; ++var4) {
            for (var5 = 0; var5 < 104; ++var5) {
                if ((tile_flags[1][var4][var5] & 2) == 2) {
                    arg0.setLinkBelow(var4, var5);
                }
            }
        }

        var3 = 1;
        var4 = 2;
        var5 = 4;

        for (plane = 0; plane < 4; ++plane) {
            if (plane > 0) {
                var3 <<= 3;
                var4 <<= 3;
                var5 <<= 3;
            }

            for (int var58 = 0; var58 <= plane; ++var58) {
                for (int var59 = 0; var59 <= 104; ++var59) {
                    for (int var60 = 0; var60 <= 104; ++var60) {
                        short var61;
                        if ((tile_culling_bitsets[var58][var60][var59] & var3) != 0) {
                            var10 = var59;
                            var11 = var59;
                            var12 = var58;
                            var13 = var58;

                            while (var10 > 0 && (tile_culling_bitsets[var58][var60][var10 - 1] & var3) != 0) {
                                --var10;
                            }

                            while (var11 < 104 && 0 != (tile_culling_bitsets[var58][var60][var11 + 1] & var3)) {
                                ++var11;
                            }

                            label487:
                            while (var12 > 0) {
                                for (var14 = var10; var14 <= var11; ++var14) {
                                    if ((tile_culling_bitsets[var12 - 1][var60][var14] & var3) == 0) {
                                        break label487;
                                    }
                                }

                                --var12;
                            }

                            label476:
                            while (var13 < plane) {
                                for (var14 = var10; var14 <= var11; ++var14) {
                                    if (0 == (tile_culling_bitsets[var13 + 1][var60][var14] & var3)) {
                                        break label476;
                                    }
                                }

                                ++var13;
                            }

                            var14 = (var11 - var10 + 1) * (var13 + 1 - var12);
                            if (var14 >= 8) {
                                var61 = 240;
                                var16 = tileHeights[var13][var60][var10] - var61;
                                var17 = tileHeights[var12][var60][var10];
                                SceneGraph.Scene_addOccluder(plane, 1, 128 * var60, var60 * 128, 128 * var10, 128 * var11 + 128, var16, var17);

                                for (var18 = var12; var18 <= var13; ++var18) {
                                    for (var19 = var10; var19 <= var11; ++var19) {
                                        tile_culling_bitsets[var18][var60][var19] &= ~var3;
                                    }
                                }
                            }
                        }

                        if ((tile_culling_bitsets[var58][var60][var59] & var4) != 0) {
                            var10 = var60;
                            var11 = var60;
                            var12 = var58;
                            var13 = var58;

                            while (var10 > 0 && (tile_culling_bitsets[var58][var10 - 1][var59] & var4) != 0) {
                                --var10;
                            }

                            while (var11 < 104 && (tile_culling_bitsets[var58][var11 + 1][var59] & var4) != 0) {
                                ++var11;
                            }

                            label540:
                            while (var12 > 0) {
                                for (var14 = var10; var14 <= var11; ++var14) {
                                    if ((tile_culling_bitsets[var12 - 1][var14][var59] & var4) == 0) {
                                        break label540;
                                    }
                                }

                                --var12;
                            }

                            label529:
                            while (var13 < plane) {
                                for (var14 = var10; var14 <= var11; ++var14) {
                                    if (0 == (tile_culling_bitsets[var13 + 1][var14][var59] & var4)) {
                                        break label529;
                                    }
                                }

                                ++var13;
                            }

                            var14 = (1 + var13 - var12) * (var11 - var10 + 1);
                            if (var14 >= 8) {
                                var61 = 240;
                                var16 = tileHeights[var13][var10][var59] - var61;
                                var17 = tileHeights[var12][var10][var59];
                                SceneGraph.Scene_addOccluder(plane, 2, 128 * var10, var11 * 128 + 128, var59 * 128, 128 * var59, var16, var17);

                                for (var18 = var12; var18 <= var13; ++var18) {
                                    for (var19 = var10; var19 <= var11; ++var19) {
                                        tile_culling_bitsets[var18][var19][var59] &= ~var4;
                                    }
                                }
                            }
                        }

                        if (0 != (tile_culling_bitsets[var58][var60][var59] & var5)) {
                            var10 = var60;
                            var11 = var60;
                            var12 = var59;
                            var13 = var59;

                            while (var12 > 0 && 0 != (tile_culling_bitsets[var58][var60][var12 - 1] & var5)) {
                                --var12;
                            }

                            while (var13 < 104 && 0 != (tile_culling_bitsets[var58][var60][1 + var13] & var5)) {
                                ++var13;
                            }

                            label593:
                            while (var10 > 0) {
                                for (var14 = var12; var14 <= var13; ++var14) {
                                    if (0 == (tile_culling_bitsets[var58][var10 - 1][var14] & var5)) {
                                        break label593;
                                    }
                                }

                                --var10;
                            }

                            label582:
                            while (var11 < 104) {
                                for (var14 = var12; var14 <= var13; ++var14) {
                                    if ((tile_culling_bitsets[var58][var11 + 1][var14] & var5) == 0) {
                                        break label582;
                                    }
                                }

                                ++var11;
                            }

                            if ((1 + (var11 - var10)) * (1 + (var13 - var12)) >= 4) {
                                var14 = tileHeights[var58][var10][var12];
                                SceneGraph.Scene_addOccluder(plane, 4, 128 * var10, var11 * 128 + 128, var12 * 128, 128 * var13 + 128, var14, var14);

                                for (var15 = var10; var15 <= var11; ++var15) {
                                    for (var16 = var12; var16 <= var13; ++var16) {
                                        tile_culling_bitsets[var58][var15][var16] &= ~var5;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public final void method1668(byte[] buffer, int z, int var2, int var3, int var4, int var5, int var6, int var7, SceneGraph scene, CollisionMap[] collision) {
        Buffer var10 = new Buffer(buffer);
        int id = -1;

        while (true) {
            int var12 = var10.get_unsignedsmart_byteorshort_increments();
            if (var12 == 0) {
                return;
            }

            id += var12;
            int position = 0;

            while (true) {
                int positionOffset = var10.get_unsignedsmart_byteorshort();
                if (positionOffset == 0) {
                    break;
                }

                position += positionOffset - 1;
                int yOffset = position & 63;
                int xOffset = position >> 6 & 63;
                int height = position >> 12;
                int attributes = var10.readUnsignedByte();
                int type = attributes >> 2;
                int orientation = attributes & 3;
                if (height == var4 && xOffset >= var5 && xOffset < var5 + 8 && yOffset >= var6 && yOffset < var6 + 8) {
                    ObjectDefinition var21 = ObjectDefinition.lookup(id);
                    int var24 = xOffset & 7;
                    int var25 = yOffset & 7;
                    int var27 = var21.sizeX;
                    int var28 = var21.sizeY;
                    int x;
                    if ((orientation & 1) == 1) {
                        x = var27;
                        var27 = var28;
                        var28 = x;
                    }

                    int var26 = var7 & 3;
                    int var23;
                    if (var26 == 0) {
                        var23 = var24;
                    } else if (var26 == 1) {
                        var23 = var25;
                    } else if (var26 == 2) {
                        var23 = 7 - var24 - (var27 - 1);
                    } else {
                        var23 = 7 - var25 - (var28 - 1);
                    }

                    x = var2 + var23;
                    int var32 = xOffset & 7;
                    int var33 = yOffset & 7;
                    int var35 = var21.sizeX;
                    int var36 = var21.sizeY;
                    int y;
                    if ((orientation & 1) == 1) {
                        y = var35;
                        var35 = var36;
                        var36 = y;
                    }

                    int var34 = var7 & 3;
                    int var31;
                    if (var34 == 0) {
                        var31 = var33;
                    } else if (var34 == 1) {
                        var31 = 7 - var32 - (var35 - 1);
                    } else if (var34 == 2) {
                        var31 = 7 - var33 - (var36 - 1);
                    } else {
                        var31 = var32;
                    }

                    y = var3 + var31;
                    if (x > 0 && y > 0 && x < 103 && y < 103) {
                        int plane = z;
                        if ((tile_flags[1][x][y] & 2) == 2) {
                            plane = z - 1;
                        }

                        CollisionMap collisionMap = null;
                        if (plane >= 0) {
                            collisionMap = collision[plane];
                        }
                        addObjectsToScene(z, x, y, id, orientation + var26 & 3, type, scene, collisionMap);
                    }
                }
            }
        }
    }

    static int set_hsl_bitset(int h, int s, int l) {
        if (l > 179) {
            s /= 2;
        }
        if (l > 192) {
            s /= 2;
        }
        if (l > 217) {
            s /= 2;
        }
        if (l > 243) {
            s /= 2;
        }
        int hsl = (s / 32 << 7) + (h / 4 << 10) + l / 2;
        return hsl;
    }

    public static boolean method787(byte[] var0, int var1, int var2) {
        boolean var3 = true;
        Buffer var4 = new Buffer(var0);
        int var5 = -1;

        label57:
        while (true) {
            int var6 = var4.get_unsignedsmart_byteorshort_increments();
            if (var6 == 0) {
                return var3;
            }

            var5 += var6;
            int var7 = 0;
            boolean var8 = false;

            while (true) {
                int var9;
                while (!var8) {
                    var9 = var4.get_unsignedsmart_byteorshort();
                    if (var9 == 0) {
                        continue label57;
                    }

                    var7 += var9 - 1;
                    int var10 = var7 & 63;
                    int var11 = var7 >> 6 & 63;
                    int var12 = var4.readUnsignedByte() >> 2;
                    int var13 = var11 + var1;
                    int var14 = var10 + var2;
                    if (var13 > 0 && var14 > 0 && var13 < 103 && var14 < 103) {
                        ObjectDefinition def = ObjectDefinition.lookup(var5);
                        if (var12 != 22 || !low_detail || def.int1 != 0 || def.interactType == 1 || def.boolean2) {
                            if (!def.needsModelFiles()) {
                                ++Client.objectsLoaded;
                                var3 = false;
                            }

                            var8 = true;
                        }
                    }
                }

                var9 = var4.get_unsignedsmart_byteorshort();
                if (var9 == 0) {
                    break;
                }

                var4.readUnsignedByte();
            }
        }
    }

    public void decode_map_locations(int chunkX, CollisionMap[] collision, int tileBits, SceneGraph sceneG, byte[] data) {
        Buffer buffer = new Buffer(data);
        int id = -1;

        while (true) {
            int idOffset = buffer.get_unsignedsmart_byteorshort_increments();
            if (idOffset == 0) {
                return;
            }

            id += idOffset;
            int position = 0;

            while (true) {
                int positionOffset = buffer.get_unsignedsmart_byteorshort();
                if (positionOffset == 0) {
                    break;
                }

                position += positionOffset - 1;
                int localY = position & 63;
                int localX = position >> 6 & 63;
                int height = position >> 12;
                int attributes = buffer.readUnsignedByte();
                int type = attributes >> 2;
                int orientation = attributes & 3;
                int regionX = localX + chunkX;
                int regionY = localY + tileBits;

                if (regionX > 0 && regionY > 0 && regionX < 103 && regionY < 103) {
                    int zLevel = height;
                    if ((tile_flags[1][regionX][regionY] & 0x2) == 2) {
                        zLevel = height - 1;
                    }

                    CollisionMap collisionFlags = null;
                    if (zLevel >= 0) {
                        collisionFlags = collision[zLevel];
                    }

                    addObjectsToScene(height, regionX, regionY, id, orientation, type, sceneG, collisionFlags);
                }
            }
        }
    }

    public static final void method1307(int var0, int var1, int var2) {
        int var3;
        for(var3 = 0; var3 < 8; ++var3) {
            for(int var4 = 0; var4 < 8; ++var4) {
                instance.tileHeights[var0][var3 + var1][var4 + var2] = 0;
            }
        }

        if (var1 > 0) {
            for(var3 = 1; var3 < 8; ++var3) {
                instance.tileHeights[var0][var1][var3 + var2] = instance.tileHeights[var0][var1 - 1][var3 + var2];
            }
        }

        if (var2 > 0) {
            for(var3 = 1; var3 < 8; ++var3) {
                instance.tileHeights[var0][var3 + var1][var2] = instance.tileHeights[var0][var3 + var1][var2 - 1];
            }
        }

        if (var1 > 0 && instance.tileHeights[var0][var1 - 1][var2] != 0) {
            instance.tileHeights[var0][var1][var2] = instance.tileHeights[var0][var1 - 1][var2];
        } else if (var2 > 0 && instance.tileHeights[var0][var1][var2 - 1] != 0) {
            instance.tileHeights[var0][var1][var2] = instance.tileHeights[var0][var1][var2 - 1];
        } else if (var1 > 0 && var2 > 0 && instance.tileHeights[var0][var1 - 1][var2 - 1] != 0) {
            instance.tileHeights[var0][var1][var2] = instance.tileHeights[var0][var1 - 1][var2 - 1];
        }
    }

}
