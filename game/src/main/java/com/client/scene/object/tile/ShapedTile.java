package com.client.scene.object.tile;

import net.runelite.rs.api.RSSceneTileModel;

public final class ShapedTile implements RSSceneTileModel
{


    public int color61;
    public int color71;
    public int color81;
    public int color91;
    public int color62;
    public int color72;
    public int color82;
    public int color92;

    public ShapedTile(int tile_z, int j, int k, int v_ne, int material, int j1, int rotation,
                      int l1, int underlay_color, int j2, int k2, int l2, int v_sw, int shape,
                      int k3, int l3, int i4, int tile_x, int minimap_color)
    {
        if (v_sw != l2 || v_ne != l2 || k2 != l2) {
            this.hasDepth = false;
        }
        color61 = l1;
        color71 = i4;
        color81 = j2;
        color91 = k;
        color62 = j;
        color72 = l3;
        color82 = j1;
        color92 = k3;

        flat = !(v_sw != l2 || v_sw != v_ne || v_sw != k2);
        this.shape = shape;
        this.rotation = rotation;
        this.colourRGB = underlay_color;
        this.colourRGBA = minimap_color;
        char c_512 = '\200';
        int c_256 = c_512 / 2;
        int c_128 = c_512 / 4;
        int c_384 = (c_512 * 3) / 4;
        int[] mesh = shaped_tile_vertex_data[shape];
        int length = mesh.length;
        origVertexX = new int[length];
        origVertexY = new int[length];
        origVertexZ = new int[length];
        int vertex_color_overlay[] = new int[length];
        int vertex_color_underlay[] = new int[length];
        int x512 = tile_x * c_512;
        int z512 = tile_z * c_512;
        for(int vertex = 0; vertex < length; vertex++)
        {
            int opcode = mesh[vertex];
            if((opcode & 1) == 0 && opcode <= 8)
                opcode = (opcode - rotation - rotation - 1 & 7) + 1;

            if(opcode > 8 && opcode <= 12)
                opcode = (opcode - 9 - rotation & 3) + 9;

            if(opcode > 12 && opcode <= 16)
                opcode = (opcode - 13 - rotation & 3) + 13;

            int v_x;
            int v_y;
            int v_z;
            int v_color_overlay;
            int v_color_underlay;
            if(opcode == 1) {
                v_x = x512;
                v_y = z512;
                v_z = v_sw;
                v_color_overlay = l1;
                v_color_underlay = j;
            } else
            if(opcode == 2) {
                v_x = x512 + c_256;
                v_y = z512;
                v_z = v_sw + l2 >> 1;
                v_color_overlay = l1 + i4 >> 1;
                v_color_underlay = j + l3 >> 1;
            } else
            if(opcode == 3) {
                v_x = x512 + c_512;
                v_y = z512;
                v_z = l2;
                v_color_overlay = i4;
                v_color_underlay = l3;
            } else
            if(opcode == 4) {
                v_x = x512 + c_512;
                v_y = z512 + c_256;
                v_z = l2 + v_ne >> 1;
                v_color_overlay = i4 + j2 >> 1;
                v_color_underlay = l3 + j1 >> 1;
            } else
            if(opcode == 5) {
                v_x = x512 + c_512;
                v_y = z512 + c_512;
                v_z = v_ne;
                v_color_overlay = j2;
                v_color_underlay = j1;
            } else
            if(opcode == 6) {
                v_x = x512 + c_256;
                v_y = z512 + c_512;
                v_z = v_ne + k2 >> 1;
                v_color_overlay = j2 + k >> 1;
                v_color_underlay = j1 + k3 >> 1;
            } else
            if(opcode == 7) {
                v_x = x512;
                v_y = z512 + c_512;
                v_z = k2;
                v_color_overlay = k;
                v_color_underlay = k3;
            } else
            if(opcode == 8) {
                v_x = x512;
                v_y = z512 + c_256;
                v_z = k2 + v_sw >> 1;
                v_color_overlay = k + l1 >> 1;
                v_color_underlay = k3 + j >> 1;
            } else
            if(opcode == 9) {
                v_x = x512 + c_256;
                v_y = z512 + c_128;
                v_z = v_sw + l2 >> 1;
                v_color_overlay = l1 + i4 >> 1;
                v_color_underlay = j + l3 >> 1;
            } else
            if(opcode == 10) {
                v_x = x512 + c_384;
                v_y = z512 + c_256;
                v_z = l2 + v_ne >> 1;
                v_color_overlay = i4 + j2 >> 1;
                v_color_underlay = l3 + j1 >> 1;
            } else
            if(opcode == 11) {
                v_x = x512 + c_256;
                v_y = z512 + c_384;
                v_z = v_ne + k2 >> 1;
                v_color_overlay = j2 + k >> 1;
                v_color_underlay = j1 + k3 >> 1;
            } else
            if(opcode == 12) {
                v_x = x512 + c_128;
                v_y = z512 + c_256;
                v_z = k2 + v_sw >> 1;
                v_color_overlay = k + l1 >> 1;
                v_color_underlay = k3 + j >> 1;
            } else
            if(opcode == 13) {
                v_x = x512 + c_128;
                v_y = z512 + c_128;
                v_z = v_sw;
                v_color_overlay = l1;
                v_color_underlay = j;
            } else
            if(opcode == 14) {
                v_x = x512 + c_384;
                v_y = z512 + c_128;
                v_z = l2;
                v_color_overlay = i4;
                v_color_underlay = l3;
            } else
            if(opcode == 15) {
                v_x = x512 + c_384;
                v_y = z512 + c_384;
                v_z = v_ne;
                v_color_overlay = j2;
                v_color_underlay = j1;
            } else {
                v_x = x512 + c_128;
                v_y = z512 + c_384;
                v_z = k2;
                v_color_overlay = k;
                v_color_underlay = k3;
            }
            origVertexX[vertex] = v_x;
            origVertexY[vertex] = v_z;
            origVertexZ[vertex] = v_y;
            vertex_color_overlay[vertex] = v_color_overlay;
            vertex_color_underlay[vertex] = v_color_underlay;
        }

        int elements[] = shaped_tile_element_data[shape];
        int vertices = elements.length / 4;
        triangleA = new int[vertices];
        triangleB = new int[vertices];
        triangleC = new int[vertices];
        triangleHslA = new int[vertices];
        triangleHslB = new int[vertices];
        triangleHslC = new int[vertices];
        if(material != -1) {
            this.triangleTexture = new int[vertices];
        }
        int offset = 0;
        for(int vertex = 0; vertex < vertices; vertex++) {
            int type = elements[offset];
            int a = elements[offset + 1];
            int b = elements[offset + 2];
            int c = elements[offset + 3];
            offset += 4;
            if(a < 4)
                a = a - rotation & 3;

            if(b < 4)
                b = b - rotation & 3;

            if(c < 4)
                c = c - rotation & 3;

            triangleA[vertex] = a;
            triangleB[vertex] = b;
            triangleC[vertex] = c;
            if(type == 0) {
                triangleHslA[vertex] = vertex_color_overlay[a];
                triangleHslB[vertex] = vertex_color_overlay[b];
                triangleHslC[vertex] = vertex_color_overlay[c];
                if(this.triangleTexture != null)
                    this.triangleTexture[vertex] = -1;
            } else {
                triangleHslA[vertex] = vertex_color_underlay[a];
                triangleHslB[vertex] = vertex_color_underlay[b];
                triangleHslC[vertex] = vertex_color_underlay[c];
                if(this.triangleTexture != null)
                    this.triangleTexture[vertex] = material;
            }
        }
        int y_a_offset = v_sw;
        int y_b_offset = l2;
        if(l2 < y_a_offset)
            y_a_offset = l2;

        if(l2 > y_b_offset)
            y_b_offset = l2;

        if(v_ne < y_a_offset)
            y_a_offset = v_ne;

        if(v_ne > y_b_offset)
            y_b_offset = v_ne;

        if(k2 < y_a_offset)
            y_a_offset = k2;

        if(k2 > y_b_offset)
            y_b_offset = k2;

        y_a_offset /= 14;
        y_b_offset /= 14;
    }

    public final int[] origVertexX;
    public final int[] origVertexY;
    public final int[] origVertexZ;
    public final int[] triangleHslA;
    public final int[] triangleHslB;
    public final int[] triangleHslC;
    public final int[] triangleA;
    public final int[] triangleB;
    public final int[] triangleC;
    public int triangleTexture[];
    public final boolean flat;
    public final int shape;
    public final int rotation;
    public final int colourRGB;
    public final int colourRGBA;
    public static final int[] anIntArray688 = new int[6];
    public static final int[] anIntArray689 = new int[6];
    public static final int[] anIntArray690 = new int[6];
    public static final int[] anIntArray691 = new int[6];
    public static final int[] anIntArray692 = new int[6];
    public static final int[] vertex_viewpoint_z = new int[6];
    public static float[] depth = new float[6];
    public boolean hasDepth = true;
    private static final int[][] shaped_tile_vertex_data = {
        {
            1, 3, 5, 7
        }, {
        1, 3, 5, 7
    }, {
        1, 3, 5, 7
    }, {
        1, 3, 5, 7, 6
    }, {
        1, 3, 5, 7, 6
    }, {
        1, 3, 5, 7, 6
    }, {
        1, 3, 5, 7, 6
    }, {
        1, 3, 5, 7, 2, 6
    }, {
        1, 3, 5, 7, 2, 8
    }, {
        1, 3, 5, 7, 2, 8
    }, {
        1, 3, 5, 7, 11, 12
    }, {
        1, 3, 5, 7, 11, 12
    }, {
        1, 3, 5, 7, 13, 14
    }
    };
    private static final int[][] shaped_tile_element_data = {
        {
            0, 1, 2, 3, 0, 0, 1, 3
        }, {
        1, 1, 2, 3, 1, 0, 1, 3
    }, {
        0, 1, 2, 3, 1, 0, 1, 3
    }, {
        0, 0, 1, 2, 0, 0, 2, 4, 1, 0,
        4, 3
    }, {
        0, 0, 1, 4, 0, 0, 4, 3, 1, 1,
        2, 4
    }, {
        0, 0, 4, 3, 1, 0, 1, 2, 1, 0,
        2, 4
    }, {
        0, 1, 2, 4, 1, 0, 1, 4, 1, 0,
        4, 3
    }, {
        0, 4, 1, 2, 0, 4, 2, 5, 1, 0,
        4, 5, 1, 0, 5, 3
    }, {
        0, 4, 1, 2, 0, 4, 2, 3, 0, 4,
        3, 5, 1, 0, 4, 5
    }, {
        0, 0, 4, 5, 1, 4, 1, 2, 1, 4,
        2, 3, 1, 4, 3, 5
    }, {
        0, 0, 1, 5, 0, 1, 4, 5, 0, 1,
        2, 4, 1, 0, 5, 3, 1, 5, 4, 3,
        1, 4, 2, 3
    }, {
        1, 0, 1, 5, 1, 1, 4, 5, 1, 1,
        2, 4, 0, 0, 5, 3, 0, 5, 4, 3,
        0, 4, 2, 3
    }, {
        1, 0, 5, 4, 1, 0, 1, 5, 0, 0,
        4, 3, 0, 4, 5, 3, 0, 5, 2, 3,
        0, 1, 2, 5
    }
    };

    private int bufferOffset = -1;

    public void setBufferOffset(int offset) {
        bufferOffset = offset;
    }

    private int uVBufferOffset = -1;

    public void setUvBufferOffset(int offset) {
        uVBufferOffset = offset;
    }

    private int bufferLength = -1;

    public void setBufferLen(int length) {
        bufferLength = length;
    }

    @Override
    public int getUnderlaySwColor() {
        return color61;
    }

    @Override
    public void setUnderlaySwColor(int color) {
        color61 = color;
    }

    @Override
    public int getUnderlaySeColor() {
        return color91;
    }

    @Override
    public void setUnderlaySeColor(int color) {
        this.color91 = color;
    }

    @Override
    public int getUnderlayNeColor() {
        return color81;
    }

    @Override
    public void setUnderlayNeColor(int color) {
        this.color81 = color;
    }

    @Override
    public int getUnderlayNwColor() {
        return color71;
    }

    @Override
    public void setUnderlayNwColor(int color) {
        color71 = color;
    }

    @Override
    public int getOverlaySwColor() {
        return color62;
    }

    @Override
    public void setOverlaySwColor(int color) {
        color62 = color;
    }

    @Override
    public int getOverlaySeColor() {
        return color92;
    }

    @Override
    public void setOverlaySeColor(int color) {
        color92 = color;
    }

    @Override
    public int getOverlayNeColor() {
        return color82;
    }

    @Override
    public void setOverlayNeColor(int color) {
        color82 = color;
    }

    @Override
    public int getOverlayNwColor() {
        return color72;
    }

    @Override
    public void setOverlayNwColor(int color) {
        color72 = color;
    }

    @Override
    public boolean isFlat() {
        return flat;
    }

    public int getBufferLen() {
        return bufferLength;
    }

    public int getBufferOffset() {
        return bufferOffset;
    }

    public int getUvBufferOffset() {
        return uVBufferOffset;
    }

    @Override
    public int getModelUnderlay() {
        return colourRGB;
    }

    @Override
    public int getModelOverlay() {
        return colourRGBA;
    }

    @Override
    public int getShape() {
        return shape;
    }

    @Override
    public int[] getFaceX() {
        return triangleA;
    }

    @Override
    public int[] getFaceY() {
        return triangleB;
    }

    @Override
    public int[] getFaceZ() {
        return triangleC;
    }

    @Override
    public int[] getVertexX() {
        return this.origVertexX;
    }

    @Override
    public int[] getVertexY() {
        return origVertexY;
    }

    @Override
    public int[] getVertexZ() {
        return origVertexZ;
    }

    @Override
    public int[] getTriangleColorA() {
        return triangleHslA;
    }

    @Override
    public int[] getTriangleColorB() {
        return triangleHslB;
    }

    @Override
    public int[] getTriangleColorC() {
        return triangleHslC;
    }

    @Override
    public int[] getTriangleTextureId() {
        return triangleTexture;
    }

    @Override
    public int getRotation() {
        return rotation;
    }


}
