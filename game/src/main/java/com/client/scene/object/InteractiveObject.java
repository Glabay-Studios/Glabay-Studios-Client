package com.client.scene.object;

import com.client.Client;
import com.client.Renderable;
import com.client.definitions.ObjectDefinition;
import com.client.utilities.ObjectKeyUtil;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.Angle;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.RSGameObject;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSRenderable;

import java.awt.*;

/**
 * ObjectGenre = 2
 */
public final class InteractiveObject implements RSGameObject {

    public int zLoc;
    public int tileHeight;
    public int xPos;
    public int yPos;
    public Renderable renderable;
    public int orientation;
    public int startX;
    public int endX;
    public int endY;
    public int startY;
    public int camera_distance;//TODO - zooming in and out scales the value, up/down do not
    public int rendered;
    public long uid;
    /**
     * mask = (byte)((objectRotation << 6) + objectType);
     */
    public int mask;

    public RSModel getModel() {
        RSRenderable renderable = getRenderable();
        if (renderable == null)
        {
            return null;
        }

        if (renderable instanceof RSModel)
        {
            return (RSModel) renderable;
        }
        else
        {
            return renderable.getModel();
        }
    }

    @Override
    public int sizeX() {
        return endX - startX;
    }

    @Override
    public int sizeY() {
        return endY - startY;
    }

    @Override
    public Point getSceneMinLocation() {
        return new Point(this.getStartX(), this.getStartY());
    }

    @Override
    public Point getSceneMaxLocation() {
        return new Point(this.getEndX(), this.getEndY());
    }

    @Override
    public Shape getConvexHull() {
        RSModel model = getModel();
        if (model == null)
        {
            return null;
        }
        int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), Client.instance.getPlane());
        return model.getConvexHull(getX(), getY(), getModelOrientation(), tileHeight);
    }

    @Override
    public Angle getOrientation() {
        int orientation = this.getModelOrientation();
        int face = this.getFlags() >> 6 & 3;
        return new Angle(orientation + face * 512);
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
        return Perspective.getCanvasTextLocation(Client.instance, graphics, this.getLocalLocation(), text, zOffset);
    }

    @Override
    public Point getMinimapLocation() {
        return Perspective.localToMinimap(Client.instance, getLocalLocation());
    }

    @Override
    public Shape getClickbox() {
        return Perspective.getClickbox(Client.instance, this.getModel(), this.getModelOrientation(), getLocalLocation());
    }

    @Override
    public String getName() { return ObjectDefinition.lookup(getId()).name; }

    @Override
    public String[] getActions() {
        return null;
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
    public RSRenderable getRenderable() {
        return renderable;
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public int getEndX() {
        return endX;
    }

    @Override
    public int getEndY() {
        return endY;
    }

    @Override
    public int getX() {
        return xPos;
    }

    @Override
    public int getY() {
        return yPos;
    }

    @Override
    public int getHeight() {
        return tileHeight;
    }

    @Override
    public int getModelOrientation() {
        return orientation;
    }

    @Override
    public long getHash() {
        return uid;
    }

    @Override
    public int getFlags() {
        return mask;
    }

    @Override
    public int getPlane() {
        return zLoc;
    }

    @Override
    public void setPlane(int plane) {
        zLoc = plane;
    }

}
