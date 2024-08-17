package net.runelite.api;

import net.runelite.api.model.Triangle;
import net.runelite.api.model.Vertex;

import java.util.List;

public interface Mesh<T extends Mesh<T>>
{
    /**
     * Gets a list of all vertices of the model.
     *
     * @return the vertices
     */
    List<Vertex> getVertices();

    /**
     * Gets a list of all triangles of the model.
     *
     * @return the triangle
     */
    List<Triangle> getTriangles();

    int getVerticesCount();
    int[] getVerticesX();
    int[] getVerticesY();
    int[] getVerticesZ();

    int getFaceCount();
    int[] getFaceIndices1();
    int[] getFaceIndices2();
    int[] getFaceIndices3();
    byte[] getFaceTransparencies();
    short[] getFaceTextures();

    /**
     * Rotates this model 90 degrees around the vertical axis.
     * {@link ModelData#cloneVertices()} should be called before calling this method
     */
    T rotateY90Ccw();

    /**
     * Rotates this model 180 degrees around the vertical axis.
     * {@link ModelData#cloneVertices()} should be called before calling this method
     */
    T rotateY180Ccw();

    /**
     * Rotates this model 270 degrees around the vertical axis.
     * {@link ModelData#cloneVertices()} should be called before calling this method
     */
    T rotateY270Ccw();

    /**
     * Offsets this model by the passed amount (1/128ths of a tile).
     * {@link ModelData#cloneVertices()} should be called before calling this method
     */
    T translate(int x, int y, int z);

    /**
     * Resizes this model by the passed amount (1/128ths).
     * {@link ModelData#cloneVertices()} should be called before calling this method
     */
    T scale(int x, int y, int z);
}
