package net.runelite.api;

public interface DynamicObject extends Renderable
{
	/**
	 * Get the animation applied to the object
	 * @return
	 */
	Animation getAnimation();

	/**
	 * Get the animation id
	 * @return
	 */
	int getAnimationID();

	/**
	 * Get the frame of the current animation
	 * @return
	 */
	int getAnimFrame();

	/**
	 * Get the frame cycle. The number of ticks the client has been on this frame.
	 * @return
	 */
	int getAnimCycle();
}