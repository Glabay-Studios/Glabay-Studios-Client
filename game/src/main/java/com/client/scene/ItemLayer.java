package com.client.scene;

import com.client.Client;
import com.client.Renderable;
import com.client.definitions.ItemDefinition;
import com.client.utilities.ObjectKeyUtil;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.RSGameObject;
import net.runelite.rs.api.RSItemLayer;
import net.runelite.rs.api.RSRenderable;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.geom.Area;

public final class ItemLayer implements RSItemLayer {

    public int z;
    public int tileHeights;
    public int x;
    public int y;
    public Renderable first;
    public Renderable second;
    public Renderable third;
    public long tag;
    public int height;

    private int itemLayerPlane;


    @Override
    public Model getModelBottom()
    {
        net.runelite.api.Renderable renderable = getBottom();
        if (renderable == null)
        {
            return null;
        }

        if (renderable instanceof Model)
        {
            return (Model) renderable;
        }
        else
        {
            return renderable.getModel();
        }
    }

    @Override
    public Model getModelMiddle()
    {
        net.runelite.api.Renderable renderable = getMiddle();
        if (renderable == null)
        {
            return null;
        }

        if (renderable instanceof Model)
        {
            return (Model) renderable;
        }
        else
        {
            return renderable.getModel();
        }
    }

    @Override
    public Model getModelTop()
    {
        net.runelite.api.Renderable renderable = getTop();
        if (renderable == null)
        {
            return null;
        }

        if (renderable instanceof Model)
        {
            return (Model) renderable;
        }
        else
        {
            return renderable.getModel();
        }
    }
    @Override
    public WorldPoint getWorldLocation()
    {
        return WorldPoint.fromScene(Client.instance, getX(), getY(), getPlane());
    }
    @Override
    public LocalPoint getLocalLocation()
    {
        return LocalPoint.fromScene(getX(), getY());
    }

    @Override
    public int getPlane()
    {
        return itemLayerPlane;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Point getCanvasLocation()
    {
        return getCanvasLocation(0);
    }

    @Override
    public Point getCanvasLocation(int zOffset)
    {
        return Perspective.localToCanvas(Client.instance, getLocalLocation(), getPlane(), zOffset);
    }

    @Override
    public Polygon getCanvasTilePoly()
    {
        int sizeX = 1;
        int sizeY = 1;

        return Perspective.getCanvasTileAreaPoly(Client.instance, getLocalLocation(), sizeX, sizeY, getPlane(), 0);
    }

    @Override
    public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset)
    {
        return Perspective.getCanvasTextLocation(Client.instance, graphics, getLocalLocation(), text, zOffset);
    }

    @Override
    public Point getMinimapLocation()
    {
        return Perspective.localToMinimap(Client.instance, getLocalLocation());
    }

    @Nullable
    @Override
    public Area getClickbox()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return ItemDefinition.lookup(ObjectKeyUtil.getObjectId(tag)).name;
    }

    @Override
    public String[] getActions() {
        return ItemDefinition.lookup(ObjectKeyUtil.getObjectId(tag)).equipActions;
    }

    @Override
    public int getConfig() {
        return 0;
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
    public int getZ() {
        return z;
    }

    @Override
    public long getHash() {
        return tag;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public RSRenderable getTop() {
        return first;
    }

    @Override
    public RSRenderable getMiddle() {
        return second;
    }

    @Override
    public RSRenderable getBottom() {
        return third;
    }

    @Override
    public void setPlane(int plane)
    {
        this.itemLayerPlane = plane;
    }
}
