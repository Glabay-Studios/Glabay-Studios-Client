package net.runelite.api;

public interface ActorSpotAnim extends Node
{
    /**
     * Get the spotanim id
     * @see GraphicID
     * @return
     */
    int getId();

    /**
     * Set the spotanim id
     * @see GraphicID
     * @param id
     */
    void setId(int id);

    /**
     * Get the spotanim height
     * @return
     */
    int getHeight();

    /**
     * Set the spotanim height
     * @param height
     */
    void setHeight(int height);

    /**
     * Get the spotanim frame
     * @return
     */
    int getFrame();

    /**
     * Set the spotanim frame
     * @param frame
     */
    void setFrame(int frame);

    /**
     * Get the frame cycle. The number of ticks the client has been on this frame.
     * @return
     */
    int getCycle();

    /**
     * Set the frame cycle.
     * @param cycle
     */
    void setCycle(int cycle);
}
