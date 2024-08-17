package com.client.scene.object;

import com.client.Client;
import com.client.Renderable;
import com.client.definitions.ObjectDefinition;
import com.client.utilities.ObjectKeyUtil;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.geometry.Shapes;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSRenderable;
import net.runelite.rs.api.RSWallDecoration;

import java.awt.*;


public final class WallDecoration implements RSWallDecoration {

    public int plane;
    public int world_x;
    public int world_y;
    public int config;
    public int orientation;
    public int orientation2;
    public Renderable node;
    public long uid;
    public int mask;
    public int xOffset;
    public int yOffset;
    public Renderable renderable2;

    @Override
    public Shape getConvexHull() {
        RSModel model = node.getModel();

        if (model == null)
        {
            return null;
        }

        int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), Client.instance.getPlane());
        return model.getConvexHull(getX(), getY(), orientation, tileHeight);
    }

    @Override
    public Shape getConvexHull2() {
        RSModel model = node.getModel();

        if (model == null)
        {
            return null;
        }

        int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), Client.instance.getPlane());

        return model.getConvexHull(getX(), getY(), orientation, tileHeight);
    }
    @Override
    public int getPlane() {
        return plane;
    }
    @Override
    public int getId() {
        return ObjectKeyUtil.getObjectId(uid);
    }
    @Override
    public Point getCanvasLocation() {
        return Perspective.localToCanvas(Client.instance, getLocalLocation(), getPlane(), 0);
    }

    @Override
    public Point getCanvasLocation(int zOffset) {
        return Perspective.localToCanvas(Client.instance, this.getLocalLocation(), this.getPlane(), zOffset);
    }

    @Override
    public Polygon getCanvasTilePoly() {
        return Perspective.getCanvasTilePoly(Client.instance, this.getLocalLocation());
    }

    @Override
    public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
        return Perspective.getCanvasTextLocation(Client.instance, graphics, getLocalLocation(), text, zOffset);
    }

    @Override
    public Point getMinimapLocation() {
        return Perspective.localToMinimap(Client.instance, getLocalLocation());
    }

    public RSModel getModel1()
    {
        RSRenderable renderable = getRenderable();
        if (renderable == null)
        {
            return null;
        }

        RSModel model;

        if (renderable instanceof Model)
        {
            model = (RSModel) renderable;
        }
        else
        {
            model = renderable.getModel();
        }

        return model;
    }

    public RSModel getModel2()
    {
        RSRenderable renderable = getRenderable2();
        if (renderable == null)
        {
            return null;
        }

        RSModel model;

        if (renderable instanceof Model)
        {
            model = (RSModel) renderable;
        }
        else
        {
            model = renderable.getModel();
        }

        return model;
    }

    @Override
    public Shape getClickbox() {
        LocalPoint lp = getLocalLocation();
        Shape clickboxA = Perspective.getClickbox(Client.instance, getModel1(), 0, new LocalPoint(lp.getX() + getXOffset(), lp.getY() + getYOffset()));
        Shape clickboxB = Perspective.getClickbox(Client.instance, getModel2(), 0, lp);

        if (clickboxA == null && clickboxB == null)
        {
            return null;
        }

        if (clickboxA != null && clickboxB != null)
        {
            return new Shapes(new Shape[]{clickboxA, clickboxB});
        }

        if (clickboxA != null)
        {
            return clickboxA;
        }

        return clickboxB;
    }
    @Override
    public String getName() {
        return ObjectDefinition.lookup(getId()).name;
    }

    @Override
    public String[] getActions() {
        return ObjectDefinition.lookup(getId()).actions;
    }

    @Override
    public int getConfig() {
        return mask;
    }

    @Override
    public WorldPoint getWorldLocation() {
        return WorldPoint.fromLocal(Client.instance, this.getX(), this.getY(), this.getPlane());
    }

    @Override
    public LocalPoint getLocalLocation() {
        return new LocalPoint(this.getX(), this.getY());
    }

    @Override
    public long getHash() {
        return uid;
    }

    @Override
    public int getX() {
        return world_x;
    }

    @Override
    public int getY() {
        return world_y;
    }
    @Override
    public int getXOffset() {
        return 0;
    }
    @Override
    public int getYOffset() {
        return 0;
    }
    @Override
    public int getOrientation() {
        return orientation;
    }
    @Override
    public RSRenderable getRenderable() {
        return node;
    }
    @Override
    public RSRenderable getRenderable2() {
        return node;
    }
    @Override
    public void setPlane(int plane) {
        this.plane = plane;
    }

}
