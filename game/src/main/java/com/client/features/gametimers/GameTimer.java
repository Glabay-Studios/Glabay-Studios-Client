package com.client.features.gametimers;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.client.definitions.ItemDefinition;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.time.StopWatch;

import com.client.Sprite;

public final class GameTimer extends StopWatch {

	/**
	 * The sprite of the timer displayed on screen.
	 */
	private final Sprite sprite;
	
	/**
	 * The duration, in milliseconds, that the stop watch will stop at.
	 */
	private final long duration;
	
	/**
	 * The id of the timer, making it unique to other timer objects
	 */
	private final int id;
	
	public GameTimer(int id, TimeUnit unitOfTime, int duration) {
		this.id = id;
		this.sprite = GameTimerHandler.TIMER_IMAGES[id];
		this.duration = TimeUnit.MILLISECONDS.convert(duration, unitOfTime);
		this.start();
	}

	public GameTimer(int id, ItemDefinition itemDefinition, TimeUnit unitOfTime, int duration) {
		Objects.requireNonNull(itemDefinition, "ItemDefinition is null");
		Preconditions.checkState(id > 100, "Must use an id over 1000 for item timers.");
		this.id = id;
		sprite = ItemDefinition.getSprite(itemDefinition.id, 1, 0);
		this.duration = TimeUnit.MILLISECONDS.convert(duration, unitOfTime);
		start();
	}
	
	/**
	 * Determines if the time of the counter has exceeded that of the duration
	 * @return	{@code true} if the time has exceeded the duration, otherwise {@code false}
	 */
	public final boolean isCompleted() {
		return getTime() >= duration && !isStopped();
	}
	
	/**
	 * Seconds remaining on before the timer terminates.
	 * @return time remaining in seconds
	 */
	public final int getSecondsRemaining() {
		long elapsed = getTime();
		if (elapsed > duration) {
			return 0;
		}
		return (int) TimeUnit.MILLISECONDS.toSeconds(duration - elapsed);
	}

	public Sprite getSprite() {
		return sprite;
	}
	
	public final int getTimerId() {
		return id;
	}

}
