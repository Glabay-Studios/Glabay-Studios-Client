package com.client.definitions.anim;

import com.client.Buffer;
import com.client.collection.node.DualNode;
import com.client.collection.table.EvictingDualNodeHashTable;
import com.client.entity.model.Mesh;
import com.client.entity.model.Model;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import net.runelite.rs.api.RSSpotAnimationDefinition;

public final class SpotAnimation extends DualNode implements RSSpotAnimationDefinition {

    public static EvictingDualNodeHashTable cached = new EvictingDualNodeHashTable(64);

    public static SpotAnimation lookup(int id) {
        SpotAnimation data = (SpotAnimation) SpotAnimation.cached.get(id);
        if (data == null) {
            byte[] var2 = Js5List.configs.takeFile(Js5ConfigType.SPOTANIM, id);
            data = new SpotAnimation();
            data.id = id;
            if (var2 != null) {
                data.decode(new Buffer(var2));
            }

            cached.put(data, id);
        }
        return data;
    }


    void decode(Buffer buffer) {
        while(true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }

            this.decodeNext(buffer, opcode);
        }
    }

    void decodeNext(Buffer buffer, int opcode) {
        if (opcode == 1) {
            this.modelId = buffer.readUShort();
        } else if (opcode == 2) {
            this.sequence = buffer.readUShort();
        } else if (opcode == 4) {
            this.widthScale = buffer.readUShort();
        } else if (opcode == 5) {
            this.heightScale = buffer.readUShort();
        } else if (opcode == 6) {
            this.orientation = buffer.readUShort();
        } else if (opcode == 7) {
            this.modelBrightness = buffer.readUnsignedByte();
        } else if (opcode == 8) {
            this.modelShadow = buffer.readUnsignedByte();
        } else {
            int size;
            int index;
            if (opcode == 40) {
                size = buffer.readUnsignedByte();
                this.recolorToFind = new short[size];
                this.recolorToReplace = new short[size];

                for(index = 0; index < size; ++index) {
                    this.recolorToFind[index] = (short)buffer.readUShort();
                    this.recolorToReplace[index] = (short)buffer.readUShort();
                }
            } else if (opcode == 41) {
                size = buffer.readUnsignedByte();
                this.textureFind = new short[size];
                this.textureReplace = new short[size];

                for(index = 0; index < size; ++index) {
                    this.textureFind[index] = (short)buffer.readUShort();
                    this.textureReplace[index] = (short)buffer.readUShort();
                }
            }
        }

    }

    public final Model getModel(int var1) {
        Model var2 = this.createModel();
        Model var3;
        if (this.sequence != -1 && var1 != -1) {
            var3 = SequenceDefinition.get(this.sequence).transformSpotAnimationModel(var2, var1);
        } else {
            var3 = var2.toSharedSpotAnimationModel(true);
        }

        if (this.widthScale != 128 || this.heightScale != 128) {
            var3.rs$scale(this.widthScale, this.heightScale, this.widthScale);
        }

        if (this.orientation != 0) {
            if (this.orientation == 90) {
                var3.rs$rotateY90Ccw();
            }

            if (this.orientation == 180) {
                var3.rs$rotateY90Ccw();
                var3.rs$rotateY90Ccw();
            }

            if (this.orientation == 270) {
                var3.rs$rotateY90Ccw();
                var3.rs$rotateY90Ccw();
                var3.rs$rotateY90Ccw();
            }
        }

        return var3;
    }

    public final Model createModel() {
        Model var1 = (Model) models.get((long)this.id);
        if (var1 == null) {
            Mesh var2 = Mesh.getModel(this.modelId);
            if (var2 == null) {
                return null;
            }

            if (recolorToFind != null) {
                for (int i1 = 0; i1 < recolorToFind.length; i1++)
                    var2.recolor(recolorToFind[i1], recolorToReplace[i1]);

            }
            if (textureFind != null) {
                for (int index = 0; index < textureFind.length; index++) {
                    var2.retexture(textureFind[index], textureReplace[index]);
                }
            }


            var1 = var2.toModel(this.modelBrightness + 64, this.modelShadow + 850, -30, -50, -30);
            models.put(var1, (long)this.id);
        }

        return var1;
    }

    private SpotAnimation() {
        sequence = -1;
        widthScale = 128;
        heightScale = 128;
    }

    private int modelId;
    public short[] textureReplace;
    public short[] textureFind;


    private short[] recolorToFind;
    private short[] recolorToReplace;
    public int widthScale;

    public int heightScale;
    public int orientation;
    public int modelBrightness;
    public int modelShadow;
    public int sequence;
    public int id;
    public static EvictingDualNodeHashTable models = new EvictingDualNodeHashTable(30);

    public static void clear() {
        cached.clear();
        models.clear();
    }

    @Override
    public void setRecolorFrom(short[] from) {
        recolorToFind = from;
    }

    @Override
    public void setRecolorTo(short[] to) {
        recolorToReplace = to;
    }

    @Override
    public int getSequence() {
        return sequence;
    }

}