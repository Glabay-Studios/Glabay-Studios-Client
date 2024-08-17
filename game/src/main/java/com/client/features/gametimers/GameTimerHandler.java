package com.client.features.gametimers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.client.Client;
import com.client.Rasterizer2D;
import com.client.Sprite;
import com.client.definitions.ItemDefinition;

public final class GameTimerHandler {
	
	/**
	 * A single instance of the {@link GameTimerHandler} class.
	 */
	private static final GameTimerHandler SINGLETON = new GameTimerHandler();
	
	/**
	 * A means of formatting the timer to minutes and seconds
	 */
	private static final SimpleDateFormat SDF = new SimpleDateFormat("mm:ss");
	
	/**
	 * Determines the amount of timers used.
	 */
	public static final byte AMOUNT_OF_TIMERS = 26;
	
	/**
	 * A {@link List} of {@link GameTimer} objects
	 */
	private final List<GameTimer> timers = new ArrayList<>();
	
	
	public static final Sprite[] TIMER_IMAGES = new Sprite[AMOUNT_OF_TIMERS];
	
	/**
	 * Removes the possibility of the class being instanced outside of this class.
	 */
	private GameTimerHandler() { }
	
	/**
	 * Creates a new game timer by initiating a new {@link GameTimer} object with the given
	 * parameters.
	 * 
	 * @param id							the id of the timer and or sprite.
	 * @param unitOfTime					the unit of time to represent the duration.
	 * @param duration						how long the timer exists before coming to a halt.
	 * @throws IllegalArgumentException		thrown if the id parameter exceeds the side of {@link #timers}.
	 */
	public void startGameTimer(int id, TimeUnit unitOfTime, int duration, int itemId) throws IllegalArgumentException {
		if (itemId == 0 && (id < 0 || id > AMOUNT_OF_TIMERS - 1)) {
			throw new IllegalArgumentException("The id cannot exceed that of the allowed amount.");
		}

		GameTimer toAdd;
		if (itemId > 0) {
			toAdd = new GameTimer(id, ItemDefinition.lookup(itemId), unitOfTime, duration);
		} else {
			toAdd = new GameTimer(id, unitOfTime, duration);
		}

		for (int index = 0; index < timers.size(); index++) {
			GameTimer timer = timers.get(index);
			
			if (timer == null) {
				continue;
			}
			if (timer.getTimerId() == id) {
				timers.set(index, toAdd);
				return;
			}
		}
		timers.add(toAdd);
	}
	
	public void stopAll() {
		timers.clear();
	}

	public boolean hasActiveGameTimers() {
		return Client.getUserSettings().isGameTimers() && !timers.isEmpty();
	}

	public void drawGameTimers(Client client, int x, int y) throws ParseException {
		int drawingWidth = 0;
		int drawingHeight = 44;
	
		for(GameTimer timer : timers) {
			if (timer == null || timer.isStopped()) {
				continue;
			}
			if (timer.isCompleted() || timer.getSprite() == null) {
				timer.stop();
				continue;
			}
			drawingWidth += 34;
		}
		
		if (drawingWidth == 0) {
			return;
		}
		
		x -= drawingWidth;
		Rasterizer2D.drawAlphaBox(x, y, drawingWidth, drawingHeight, 0x000000, Byte.MAX_VALUE / 2);
		Rasterizer2D.drawAlphaBox(x, y, drawingWidth, 1, 0xCBBB99, 80);
		Rasterizer2D.drawAlphaBox(x + drawingWidth - 1, y, 1, drawingHeight, 0xCBBB99, 80);
		Rasterizer2D.drawAlphaBox(x, y + 1, 1, drawingHeight - 1, 0xCBBB99, 80);
		x += 2;
		
		for(GameTimer timer : timers) {
			if (timer == null || timer.isStopped() || timer.getSprite() == null) {
				continue;
			}
			timer.getSprite().drawCenteredSprite(x + 15, y + 17);
			client.newSmallFont.drawCenteredString(SDF.format(timer.getSecondsRemaining() * 1000), x + 15, y + 42, 0xFFFFFF, 0x00000);
			//client.newSmallFont.drawBasicString(SDF.format(timer.getSecondsRemaining() * 1000), x, y + 42, 0xFFFFFF, 0x00000);
			x += 34;
		}
	}
	
	/**
	 * The single instance of the class
	 * @return 	the single instance of {@link GameTimerHandler}
	 */
	public final static GameTimerHandler getSingleton() {
		return SINGLETON;
	}
	
}
