package com.client;

import com.client.collection.node.DualNode;
import com.client.entity.model.Model;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSNode;
import net.runelite.rs.api.RSRenderable;

public class Renderable extends DualNode implements RSRenderable {



    public boolean hidden = false;

    public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1, int l1, long uid) {
        Model model = getRotatedModel();
        if (model != null) {
            model_height = model.model_height;
            model.renderAtPoint(i, j, k, l, i1, j1, k1, l1, uid);
        }
    }

    protected Model getRotatedModel() {
        return null;
    }

    public Renderable() {
        model_height = 1000;
    }


    public int model_height; // modelHeight


    @Override
    public int getModelHeight() {
        return model_height;
    }

    @Override
    public void setModelHeight(int modelHeight) {
        model_height = modelHeight;
    }

    @Override
    public void draw(int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash) {
        renderAtPoint(orientation,pitchSin,pitchCos,yawSin,yawCos,x,y,z,hash);
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public RSModel getModel() {
        return getRotatedModel();
    }

}
