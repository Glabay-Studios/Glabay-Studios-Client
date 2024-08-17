package com.client.utilities.settings;

import java.awt.*;
import java.io.Serializable;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 12/10/19
 *
 */
public class Settings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4394926495169279946L;

	public static Settings getDefault() {
		Settings settings = new Settings();
		settings.oldGameframe = false;
		settings.gameTimers = true;
		settings.antiAliasing = false;
		settings.groundItemOverlay = true;
		settings.fog = false;
		settings.smoothShading = false;
		settings.tileBlending = false;
		settings.inventoryContextMenu = false;
		settings.startMenuColor = SettingsManager.DEFAULT_START_MENU_COLOR;
		settings.chatColor = SettingsManager.DEFAULT_CHAT_COLOR_OPTION;
		settings.bountyHunter = true;
		settings.showEntityTarget = true;
		settings.drawDistance = 30;
		return settings;
	}

	private boolean oldGameframe;
	private boolean gameTimers;
	private boolean antiAliasing;
	private boolean groundItemOverlay;
	private boolean fog;
	private boolean smoothShading;
	private boolean tileBlending;
	private boolean inventoryContextMenu;
	private int startMenuColor;
	private int chatColor;
	private boolean bountyHunter;
	private boolean showEntityTarget;
	private int drawDistance;
	private boolean stretchedMode;
	private Dimension stretchedModeDimensions;

	public Settings() {}

	public boolean isOldGameframe() {
		return oldGameframe;
	}

	public void setOldGameframe(boolean oldGameframe) {
		this.oldGameframe = oldGameframe;
	}

	public boolean isAntiAliasing() {
		return antiAliasing;
	}

	public void setAntiAliasing(boolean antiAliasing) {
		this.antiAliasing = antiAliasing;
	}

	public boolean isFog() {
		return fog;
	}

	public void setFog(boolean fog) {
		this.fog = fog;
	}

	public boolean isTileBlending() {
		return tileBlending;
	}

	public void setTileBlending(boolean tileBlending) {
		this.tileBlending = tileBlending;
	}

	public boolean isSmoothShading() {
		return smoothShading;
	}

	public void setSmoothShading(boolean smoothShading) {
		this.smoothShading = smoothShading;
	}

	public boolean isInventoryContextMenu() {
		return inventoryContextMenu;
	}

	public void setInventoryContextMenu(boolean inventoryContextMenu) {
		this.inventoryContextMenu = inventoryContextMenu;
	}

	public int getChatColor() {
		return chatColor;
	}

	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}

	public boolean isBountyHunter() {
		return bountyHunter;
	}

	public void setBountyHunter(boolean bountyHunter) {
		this.bountyHunter = bountyHunter;
	}

	public boolean isShowEntityTarget() {
		return showEntityTarget;
	}

	public void setShowEntityTarget(boolean showEntityTarget) {
		this.showEntityTarget = showEntityTarget;
	}

	public boolean isGameTimers() {
		return gameTimers;
	}

	public void setGameTimers(boolean gameTimers) {
		this.gameTimers = gameTimers;
	}

	public boolean isGroundItemOverlay() {
		return groundItemOverlay;
	}

	public void setGroundItemOverlay(boolean groundItemOverlay) {
		this.groundItemOverlay = groundItemOverlay;
	}

	public int getStartMenuColor() {
		return startMenuColor;
	}

	public void setStartMenuColor(int startMenuColor) {
		this.startMenuColor = startMenuColor;
	}

	public int getDrawDistance() {
		return drawDistance;
	}

	public void setDrawDistance(int drawDistance) {
		this.drawDistance = drawDistance;
	}

	public boolean isStretchedMode() {
		return stretchedMode;
	}

	public void setStretchedMode(boolean stretchedMode) {
		this.stretchedMode = stretchedMode;
	}

	public Dimension getStretchedModeDimensions() {
		return stretchedModeDimensions;
	}

	public void setStretchedModeDimensions(Dimension stretchedModeDimensions) {
		this.stretchedModeDimensions = stretchedModeDimensions;
	}
}
