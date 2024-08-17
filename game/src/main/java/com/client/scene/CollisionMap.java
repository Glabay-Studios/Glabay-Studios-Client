package com.client.scene;

import net.runelite.rs.api.RSCollisionMap;

public final class CollisionMap implements RSCollisionMap {
    private final int inset_x;
    private final int inset_y;
    private final int width;
    private final int height;
    public final int[][] adjacencies;

    public CollisionMap(int xSize, int ySize) {
        inset_x = 0;
        inset_y = 0;
        width = xSize;
        height = ySize;
        adjacencies = new int[width][height];
        init();
    }

    public void init() {
        for(int var1 = 0; var1 < this.width; ++var1) {
            for(int var2 = 0; var2 < this.height; ++var2) {
                if (var1 != 0 && var2 != 0 && var1 < this.width - 5 && var2 < this.height - 5) {
                    this.adjacencies[var1][var2] = 16777216;
                } else {
                    this.adjacencies[var1][var2] = 16777215;
                }
            }
        }
    }

    public void addWallObstruction(int x, int y, int type, int orientation, boolean flag) {
        int var6 = x - this.inset_x;
        int var7 = y - this.inset_y;
        if (type == 0) {
            if (orientation == 0) {
                this.setFlag(var6, var7, 128);
                this.setFlag(var6 - 1, var7, 8);
            }

            if (orientation == 1) {
                this.setFlag(var6, var7, 2);
                this.setFlag(var6, 1 + var7, 32);
            }

            if (2 == orientation) {
                this.setFlag(var6, var7, 8);
                this.setFlag(1 + var6, var7, 128);
            }

            if (3 == orientation) {
                this.setFlag(var6, var7, 32);
                this.setFlag(var6, var7 - 1, 2);
            }
        }

        if (type == 1 || type == 3) {
            if (0 == orientation) {
                this.setFlag(var6, var7, 1);
                this.setFlag(var6 - 1, 1 + var7, 16);
            }

            if (1 == orientation) {
                this.setFlag(var6, var7, 4);
                this.setFlag(var6 + 1, 1 + var7, 64);
            }

            if (2 == orientation) {
                this.setFlag(var6, var7, 16);
                this.setFlag(1 + var6, var7 - 1, 1);
            }

            if (3 == orientation) {
                this.setFlag(var6, var7, 64);
                this.setFlag(var6 - 1, var7 - 1, 4);
            }
        }

        if (2 == type) {
            if (orientation == 0) {
                this.setFlag(var6, var7, 130);
                this.setFlag(var6 - 1, var7, 8);
                this.setFlag(var6, 1 + var7, 32);
            }

            if (orientation == 1) {
                this.setFlag(var6, var7, 10);
                this.setFlag(var6, 1 + var7, 32);
                this.setFlag(1 + var6, var7, 128);
            }

            if (2 == orientation) {
                this.setFlag(var6, var7, 40);
                this.setFlag(var6 + 1, var7, 128);
                this.setFlag(var6, var7 - 1, 2);
            }

            if (3 == orientation) {
                this.setFlag(var6, var7, 160);
                this.setFlag(var6, var7 - 1, 2);
                this.setFlag(var6 - 1, var7, 8);
            }
        }

        if (flag) {
            if (type == 0) {
                if (orientation == 0) {
                    this.setFlag(var6, var7, 65536);
                    this.setFlag(var6 - 1, var7, 4096);
                }

                if (1 == orientation) {
                    this.setFlag(var6, var7, 1024);
                    this.setFlag(var6, var7 + 1, 16384);
                }

                if (2 == orientation) {
                    this.setFlag(var6, var7, 4096);
                    this.setFlag(1 + var6, var7, 65536);
                }

                if (3 == orientation) {
                    this.setFlag(var6, var7, 16384);
                    this.setFlag(var6, var7 - 1, 1024);
                }
            }

            if (1 == type || type == 3) {
                if (orientation == 0) {
                    this.setFlag(var6, var7, 512);
                    this.setFlag(var6 - 1, 1 + var7, 8192);
                }

                if (1 == orientation) {
                    this.setFlag(var6, var7, 2048);
                    this.setFlag(1 + var6, 1 + var7, 32768);
                }

                if (orientation == 2) {
                    this.setFlag(var6, var7, 8192);
                    this.setFlag(1 + var6, var7 - 1, 512);
                }

                if (orientation == 3) {
                    this.setFlag(var6, var7, 32768);
                    this.setFlag(var6 - 1, var7 - 1, 2048);
                }
            }

            if (2 == type) {
                if (orientation == 0) {
                    this.setFlag(var6, var7, 66560);
                    this.setFlag(var6 - 1, var7, 4096);
                    this.setFlag(var6, var7 + 1, 16384);
                }

                if (1 == orientation) {
                    this.setFlag(var6, var7, 5120);
                    this.setFlag(var6, 1 + var7, 16384);
                    this.setFlag(var6 + 1, var7, 65536);
                }

                if (orientation == 2) {
                    this.setFlag(var6, var7, 20480);
                    this.setFlag(var6 + 1, var7, 65536);
                    this.setFlag(var6, var7 - 1, 1024);
                }

                if (orientation == 3) {
                    this.setFlag(var6, var7, 81920);
                    this.setFlag(var6, var7 - 1, 1024);
                    this.setFlag(var6 - 1, var7, 4096);
                }
            }
        }
    }

    void setFlagOff(int flag, int arg1, int arg2) {
        this.adjacencies[flag][arg1] &= ~arg2;
    }

    public void method3743(int arg0, int arg1) {
        int var3 = arg0 - this.inset_x;
        int var4 = arg1 - this.inset_y;
        this.adjacencies[var3][var4] &= -262145;
    }

    public void setBlockedByFloor(int var1, int var2) {
        var1 -= this.inset_x;
        var2 -= this.inset_y;
        this.adjacencies[var1][var2] |= 2097152;
    }

    public void setBlockedByFloorDec(int arg0, int arg1) {
        int var3 = arg0 - this.inset_x;
        int var4 = arg1 - this.inset_y;
        this.adjacencies[var3][var4] |= 262144;
    }

    public void setFlag(int var1, int var2, int var3) {
        this.adjacencies[var1][var2] |= var3;
    }

    public void removeWallObstruction(int arg0, int arg1, int arg2, int arg3, boolean arg4) {
        int var6 = arg0 - this.inset_x;
        int var7 = arg1 - this.inset_y;
        if (0 == arg2) {
            if (arg3 == 0) {
                this.setFlagOff(var6, var7, 128);
                this.setFlagOff(var6 - 1, var7, 8);
            }

            if (1 == arg3) {
                this.setFlagOff(var6, var7, 2);
                this.setFlagOff(var6, var7 + 1, 32);
            }

            if (2 == arg3) {
                this.setFlagOff(var6, var7, 8);
                this.setFlagOff(1 + var6, var7, 128);
            }

            if (3 == arg3) {
                this.setFlagOff(var6, var7, 32);
                this.setFlagOff(var6, var7 - 1, 2);
            }
        }

        if (arg2 == 1 || arg2 == 3) {
            if (0 == arg3) {
                this.setFlagOff(var6, var7, 1);
                this.setFlagOff(var6 - 1, var7 + 1, 16);
            }

            if (1 == arg3) {
                this.setFlagOff(var6, var7, 4);
                this.setFlagOff(var6 + 1, var7 + 1, 64);
            }

            if (arg3 == 2) {
                this.setFlagOff(var6, var7, 16);
                this.setFlagOff(1 + var6, var7 - 1, 1);
            }

            if (arg3 == 3) {
                this.setFlagOff(var6, var7, 64);
                this.setFlagOff(var6 - 1, var7 - 1, 4);
            }
        }

        if (arg2 == 2) {
            if (0 == arg3) {
                this.setFlagOff(var6, var7, 130);
                this.setFlagOff(var6 - 1, var7, 8);
                this.setFlagOff(var6, var7 + 1, 32);
            }

            if (arg3 == 1) {
                this.setFlagOff(var6, var7, 10);
                this.setFlagOff(var6, 1 + var7, 32);
                this.setFlagOff(1 + var6, var7, 128);
            }

            if (2 == arg3) {
                this.setFlagOff(var6, var7, 40);
                this.setFlagOff(var6 + 1, var7, 128);
                this.setFlagOff(var6, var7 - 1, 2);
            }

            if (3 == arg3) {
                this.setFlagOff(var6, var7, 160);
                this.setFlagOff(var6, var7 - 1, 2);
                this.setFlagOff(var6 - 1, var7, 8);
            }
        }

        if (arg4) {
            if (0 == arg2) {
                if (0 == arg3) {
                    this.setFlagOff(var6, var7, 65536);
                    this.setFlagOff(var6 - 1, var7, 4096);
                }

                if (1 == arg3) {
                    this.setFlagOff(var6, var7, 1024);
                    this.setFlagOff(var6, 1 + var7, 16384);
                }

                if (arg3 == 2) {
                    this.setFlagOff(var6, var7, 4096);
                    this.setFlagOff(var6 + 1, var7, 65536);
                }

                if (arg3 == 3) {
                    this.setFlagOff(var6, var7, 16384);
                    this.setFlagOff(var6, var7 - 1, 1024);
                }
            }

            if (1 == arg2 || arg2 == 3) {
                if (0 == arg3) {
                    this.setFlagOff(var6, var7, 512);
                    this.setFlagOff(var6 - 1, var7 + 1, 8192);
                }

                if (1 == arg3) {
                    this.setFlagOff(var6, var7, 2048);
                    this.setFlagOff(var6 + 1, 1 + var7, 32768);
                }

                if (2 == arg3) {
                    this.setFlagOff(var6, var7, 8192);
                    this.setFlagOff(1 + var6, var7 - 1, 512);
                }

                if (arg3 == 3) {
                    this.setFlagOff(var6, var7, 32768);
                    this.setFlagOff(var6 - 1, var7 - 1, 2048);
                }
            }

            if (arg2 == 2) {
                if (0 == arg3) {
                    this.setFlagOff(var6, var7, 66560);
                    this.setFlagOff(var6 - 1, var7, 4096);
                    this.setFlagOff(var6, 1 + var7, 16384);
                }

                if (arg3 == 1) {
                    this.setFlagOff(var6, var7, 5120);
                    this.setFlagOff(var6, var7 + 1, 16384);
                    this.setFlagOff(1 + var6, var7, 65536);
                }

                if (2 == arg3) {
                    this.setFlagOff(var6, var7, 20480);
                    this.setFlagOff(var6 + 1, var7, 65536);
                    this.setFlagOff(var6, var7 - 1, 1024);
                }

                if (arg3 == 3) {
                    this.setFlagOff(var6, var7, 81920);
                    this.setFlagOff(var6, var7 - 1, 1024);
                    this.setFlagOff(var6 - 1, var7, 4096);
                }
            }
        }
    }

    public void setFlagOffNonSquare(int orientation, int width, int x, int y, int height, boolean arg5) {
        int var8 = 256;
        if (arg5) {
            var8 += 131072;
        }

        int var11 = x - this.inset_x;
        int var12 = y - this.inset_y;
        int var9;
        if (orientation == 1 || orientation == 3) {
            var9 = width;
            width = height;
            height = var9;
        }

        for(var9 = var11; var9 < var11 + width; ++var9) {
            if (var9 >= 0 && var9 < this.width) {
                for(int var10 = var12; var10 < var12 + height; ++var10) {
                    if (var10 >= 0 && var10 < this.height) {
                        this.setFlagOff(var9, var10, var8);
                    }
                }
            }
        }
    }


    public void addGameObject(int var1, int var2, int var3, int var4, boolean var5) {
        int var6 = 256;
        if (var5) {
            var6 += 131072;
        }

        var1 -= this.inset_x;
        var2 -= this.inset_y;

        for(int var7 = var1; var7 < var3 + var1; ++var7) {
            if (var7 >= 0 && var7 < this.width) {
                for(int var8 = var2; var8 < var2 + var4; ++var8) {
                    if (var8 >= 0 && var8 < this.height) {
                        this.setFlag(var7, var8, var6);
                    }
                }
            }
        }
    }

    public boolean obstruction_wall(int travel_x, int x, int y, int obstruction_orientation, int obstruction_type, int travel_y) {
        if (x == travel_x && y == travel_y)
            return true;

        x -= inset_x;
        y -= inset_y;
        travel_x -= inset_x;
        travel_y -= inset_y;
        if (obstruction_type == 0)
            if (obstruction_orientation == 0) {
                if (x == travel_x - 1 && y == travel_y)
                    return true;
                if (x == travel_x && y == travel_y + 1
                    && (adjacencies[x][y] & 19398944) == 0)
                    return true;
                if (x == travel_x && y == travel_y - 1
                    && (adjacencies[x][y] & 0x1280102) == 0)
                    return true;
            } else if (obstruction_orientation == 1) {
                if (x == travel_x && y == travel_y + 1)
                    return true;
                if (x == travel_x - 1 && y == travel_y
                    && (adjacencies[x][y] & 0x1280108) == 0)
                    return true;
                if (x == travel_x + 1 && y == travel_y
                    && (adjacencies[x][y] & 0x1280180) == 0)
                    return true;
            } else if (obstruction_orientation == 2) {
                if (x == travel_x + 1 && y == travel_y)
                    return true;
                if (x == travel_x && y == travel_y + 1
                    && (adjacencies[x][y] & 0x1280120) == 0)
                    return true;
                if (x == travel_x && y == travel_y - 1
                    && (adjacencies[x][y] & 0x1280102) == 0)
                    return true;
            } else if (obstruction_orientation == 3) {
                if (x == travel_x && y == travel_y - 1)
                    return true;
                if (x == travel_x - 1 && y == travel_y
                    && (adjacencies[x][y] & 0x1280108) == 0)
                    return true;
                if (x == travel_x + 1 && y == travel_y
                    && (adjacencies[x][y] & 0x1280180) == 0)
                    return true;
            }

        if (obstruction_type == 2)
            if (obstruction_orientation == 0) {
                if (x == travel_x - 1 && y == travel_y)
                    return true;

                if (x == travel_x && y == travel_y + 1)
                    return true;

                if (x == travel_x + 1 && y == travel_y
                    && (adjacencies[x][y] & 0x1280180) == 0)
                    return true;

                if (x == travel_x && y == travel_y - 1
                    && (adjacencies[x][y] & 0x1280102) == 0)
                    return true;

            } else if (obstruction_orientation == 1) {
                if (x == travel_x - 1 && y == travel_y
                    && (adjacencies[x][y] & 0x1280108) == 0)
                    return true;

                if (x == travel_x && y == travel_y + 1)
                    return true;

                if (x == travel_x + 1 && y == travel_y)
                    return true;

                if (x == travel_x && y == travel_y - 1
                    && (adjacencies[x][y] & 0x1280102) == 0)
                    return true;

            } else if (obstruction_orientation == 2) {
                if (x == travel_x - 1 && y == travel_y
                    && (adjacencies[x][y] & 0x1280108) == 0)
                    return true;

                if (x == travel_x && y == travel_y + 1
                    && (adjacencies[x][y] & 0x1280120) == 0)
                    return true;

                if (x == travel_x + 1 && y == travel_y)
                    return true;

                if (x == travel_x && y == travel_y - 1)
                    return true;

            } else if (obstruction_orientation == 3) {
                if (x == travel_x - 1 && y == travel_y)
                    return true;

                if (x == travel_x && y == travel_y + 1
                    && (adjacencies[x][y] & 0x1280120) == 0)
                    return true;

                if (x == travel_x + 1 && y == travel_y
                    && (adjacencies[x][y] & 0x1280180) == 0)
                    return true;

                if (x == travel_x && y == travel_y - 1)
                    return true;

            }

        if (obstruction_type == 9) {
            if (x == travel_x && y == travel_y + 1 && (adjacencies[x][y] & 0x20) == 0)
                return true;

            if (x == travel_x && y == travel_y - 1 && (adjacencies[x][y] & 2) == 0)
                return true;

            if (x == travel_x - 1 && y == travel_y && (adjacencies[x][y] & 8) == 0)
                return true;

            if (x == travel_x + 1 && y == travel_y && (adjacencies[x][y] & 0x80) == 0)
                return true;
        }
        return false;
    }

    public boolean obstruction_decor(int travel_x, int travel_y, int y, int obstruction_type, int obstruction_orientation, int x) {
        if (x == travel_x && y == travel_y)
            return true;

        x -= inset_x;
        y -= inset_y;
        travel_x -= inset_x;
        travel_y -= inset_y;
        if (obstruction_type == 6 || obstruction_type == 7) {
            if (obstruction_type == 7)
                obstruction_orientation = obstruction_orientation + 2 & 3;
            if (obstruction_orientation == 0) {
                if (x == travel_x + 1 && y == travel_y && (adjacencies[x][y] & 0x80) == 0)
                    return true;

                if (x == travel_x && y == travel_y - 1 && (adjacencies[x][y] & 2) == 0)
                    return true;

            } else if (obstruction_orientation == 1) {
                if (x == travel_x - 1 && y == travel_y && (adjacencies[x][y] & 8) == 0)
                    return true;

                if (x == travel_x && y == travel_y - 1 && (adjacencies[x][y] & 2) == 0)
                    return true;

            } else if (obstruction_orientation == 2) {
                if (x == travel_x - 1 && y == travel_y && (adjacencies[x][y] & 8) == 0)
                    return true;

                if (x == travel_x && y == travel_y + 1 && (adjacencies[x][y] & 0x20) == 0)
                    return true;

            } else if (obstruction_orientation == 3) {
                if (x == travel_x + 1 && y == travel_y && (adjacencies[x][y] & 0x80) == 0)
                    return true;

                if (x == travel_x && y == travel_y + 1 && (adjacencies[x][y] & 0x20) == 0)
                    return true;

            }
        }
        if (obstruction_type == 8) {
            if (x == travel_x && y == travel_y + 1 && (adjacencies[x][y] & 0x20) == 0)
                return true;

            if (x == travel_x && y == travel_y - 1 && (adjacencies[x][y] & 2) == 0)
                return true;

            if (x == travel_x - 1 && y == travel_y && (adjacencies[x][y] & 8) == 0)
                return true;

            if (x == travel_x + 1 && y == travel_y && (adjacencies[x][y] & 0x80) == 0)
                return true;

        }
        return false;
    }

    public boolean obstruction(int travel_y, int travel_x, int x, int obstruction_height, int orientation, int width, int y) {
        int distance_x = (travel_x + width) - 1;
        int distance_y = (travel_y + obstruction_height) - 1;
        if (x >= travel_x && x <= distance_x && y >= travel_y && y <= distance_y)
            return true;

        if (x == travel_x - 1 && y >= travel_y && y <= distance_y
            && (adjacencies[x - inset_x][y - inset_y] & 8) == 0
            && (orientation & 8) == 0)
            return true;

        if (x == distance_x + 1 && y >= travel_y && y <= distance_y
            && (adjacencies[x - inset_x][y - inset_y] & 0x80) == 0
            && (orientation & 2) == 0)
            return true;

        return y == travel_y - 1 && x >= travel_x && x <= distance_x
            && (adjacencies[x - inset_x][y - inset_y] & 2) == 0
            && (orientation & 4) == 0 || y == distance_y + 1 && x >= travel_x && x <= distance_x
            && (adjacencies[x - inset_x][y - inset_y] & 0x20) == 0
            && (orientation & 1) == 0;
    }

    @Override
    public int[][] getFlags() {
        return adjacencies;
    }
}
