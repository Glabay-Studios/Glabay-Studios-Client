package com.client;

import com.client.audio.ObjectSound;
import com.client.audio.StaticSound;
import com.client.camera.Camera;
import com.client.collection.EvictingDualNodeHashTable;
import com.client.collection.node.NodeDeque;
import com.client.definitions.anim.SequenceDefinition;
import com.client.definitions.healthbar.HealthBarDefinition;
import com.client.definitions.healthbar.HealthBarUpdate;
import com.client.definitions.healthbar.HealthBar;
import com.client.draw.Rasterizer3D;
import com.client.draw.ImageCache;
import com.client.draw.font.custom.FontArchiver;
import com.client.draw.font.osrs.FontInfo;
import com.client.draw.font.osrs.FontLoader;
import com.client.draw.font.osrs.RSFontOSRS;
import com.client.draw.login.LoginBackground;
import com.client.draw.login.LoginScreen;
import com.client.draw.login.LoginState;
import com.client.draw.rasterizer.Clips;
import com.client.draw.widget.components.Background;
import com.client.draw.widget.components.Divider;
import com.client.draw.widget.components.Rasterizer;
import com.client.engine.impl.MouseWheel;
import com.client.engine.keys.KeyEventProcessorImpl;
import com.client.entity.model.Model;
import com.client.entity.model.ViewportMouse;
import com.client.graphics.GraphicsDefaults;
import com.client.graphics.SpriteData;
import com.client.mixins.menu.RuneLiteMenuEntry;
import com.client.scene.*;
import com.client.graphics.loaders.SpriteLoader4;
import com.client.js5.Js5ArchiveIndex;
import com.client.js5.Js5List;
import com.client.js5.Js5System;
import com.client.js5.disk.ArchiveDisk;
import com.client.js5.disk.ArchiveDiskActionHandler;
import com.client.collection.node.Node;
import com.client.js5.net.JagexNetThread;
import com.client.js5.util.ArchiveLoader;
import com.client.js5.util.Js5ConfigType;
import com.client.scene.Projectile;
import com.client.scene.object.GroundDecoration;
import com.client.scene.object.InteractiveObject;
import com.client.scene.object.Wall;
import com.client.scene.object.WallDecoration;
import com.client.settings.AccountData;
import com.client.settings.SettingManager;
import com.client.util.ColorUtils;
import com.client.util.headicon.class421;
import com.client.util.headicon.class423;
import com.google.common.primitives.Ints;
import com.util.AssetUtils;
import com.client.hover.HoverMenuManager;
import java.awt.*;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import javax.swing.JFrame;

import ch.qos.logback.classic.Level;
import com.client.broadcast.Broadcast;
import com.client.broadcast.BroadcastManager;
import com.client.definitions.*;
import com.client.definitions.server.ItemDef;
import com.client.engine.impl.KeyHandler;
import com.client.engine.impl.MouseHandler;
import com.client.features.settings.Preferences;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.daily.DailyRewards;
import com.client.graphics.interfaces.eventcalendar.EventCalendar;
import com.client.graphics.interfaces.impl.Autocast;
import com.client.graphics.interfaces.impl.Bank;
import com.client.graphics.interfaces.impl.DonatorRewards;
import com.client.graphics.interfaces.impl.Interfaces;
import com.client.graphics.interfaces.impl.MonsterDropViewer;
import com.client.graphics.interfaces.impl.Nightmare;
import com.client.graphics.interfaces.impl.QuestTab;

import static com.client.scene.SceneGraph.low_detail;
import static com.client.scene.SceneGraph.pitchRelaxEnabled;
import static com.client.engine.impl.MouseHandler.clickMode3;
import static net.runelite.api.MenuAction.UNKNOWN;

import com.client.features.EntityTarget;
import com.client.features.ExperienceDrop;
import com.client.features.gametimers.GameTimer;
import com.client.features.gametimers.GameTimerHandler;
import com.client.features.settings.InformationFile;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.impl.DropdownMenu;
import com.client.graphics.interfaces.impl.GrandExchange;
import com.client.graphics.interfaces.impl.Keybinding;
import com.client.graphics.interfaces.impl.SpawnContainer;
import com.client.graphics.interfaces.settings.Setting;
import com.client.graphics.interfaces.settings.SettingsInterface;
import com.client.graphics.interfaces.impl.SettingsTabWidget;
import com.client.graphics.interfaces.impl.Slider;
import com.client.graphics.loaders.SpriteLoader1;
import com.client.graphics.textures.TextureProvider;
import com.client.graphics.loaders.SpriteLoader2;
import com.client.graphics.loaders.SpriteLoader3;
import com.client.model.Items;
import com.client.script.ClientScripts;
import com.client.sign.Signlink;
import com.client.sound.Sound;
import com.client.sound.SoundType;
import com.client.ui.DevConsole;
import com.client.utilities.*;
import com.client.utilities.settings.Settings;
import com.client.utilities.settings.SettingsManager;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import dorkbox.notify.Notify;
import dorkbox.notify.Pos;
import dorkbox.util.ActionHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.api.Tile;
import net.runelite.api.events.*;
import net.runelite.api.widgets.WidgetID;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.runelite.api.*;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.rs.api.*;
import com.client.engine.GameEngine;

@Slf4j
public class Client extends GameEngine implements RSClient {

	public LoginScreen loginScreen = new LoginScreen(this);

	public static KeyEventProcessorImpl keyManager;
	static MouseWheel mouseWheel;

	protected void setUp() {
		setUpKeyboard();
		mouseWheel = this.mouseWheel();
		keyHandler.assignProcessor(keyManager, 0);
		setUpClipboard();
		Signlink.masterDisk = new ArchiveDisk(255, Signlink.cacheData, Signlink.cacheMasterIndex, 500000);
	}

	public final void init() {
		nodeID = 1;
		portOff = 0;
		setHighMem();
		isMembers = true;

		try {
			Signlink.init(22);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
            throw new RuntimeException(e);
        }

        instance = this;
		Sprite.start();

		Sprite.loadManifest();
		settingManager = SettingManager.Companion.load();
		startThread(765, 503, 206, 1);
		setMaxCanvasSize(765, 503);
		//FontArchiver.writeFontArchive();
        try {
            FontArchiver.loadArchive();
        } catch (IOException e) {
			e.printStackTrace();
        }
		LoginBackground.Companion.load();
    }



	@Override
	protected void vmethod1099() {

	}

	private int[] quakeMagnitude = new int[5];

	public SettingManager settingManager;
	protected final void resizeGame() {

	}

	private static final Logger logger = LoggerFactory.getLogger("Client");
	public String lastViewedDropTable;

	public boolean isServerUpdating() {
		return anInt1104 > 0;
	}

	public DevConsole devConsole = new DevConsole();
	public int scrollbar_position;
	int scrollMax = 0;
	public void draw_scrollbar(int x, int y, int width, int height, int offset, int max_lines, int height_seperator, int y_pad)
	{
		scrollMax = (offset * max_lines + (height_seperator - 1));
		if(scrollMax < height - 3)
			scrollMax = height - 3;

		aClass9_1059.scrollPosition = scrollMax - scrollbar_position - (height - 4);
		if(scrollMax > height)
			method65(x, height, MouseHandler.mouseX - 0, MouseHandler.mouseY - y, aClass9_1059, y_pad, true, scrollMax);

		int pos = scrollMax - (height - 4) - aClass9_1059.scrollPosition;
		if(pos < 0)
			pos = 0;

		if(pos > scrollMax - (height - 4))
			pos = scrollMax - (height - 4);

		if(scrollbar_position != pos)
			scrollbar_position = pos;

		drawScrollbar(height, scrollMax - scrollbar_position - (height - 1), x, y, scrollMax);
	}

	private void drawGrid() {
		for (int index = 0; index < 516; index += 10) {
			if (index < 334) {
				Rasterizer2D.drawHorizontalLine2(0, index, 516, 0xff0000);
			}
			Rasterizer2D.drawVerticalLine2(index, 0, 334, 0xff0000);
		}

		int xPos = MouseHandler.mouseX - 4 - ((MouseHandler.mouseX - 4) % 10);
		int yPos = MouseHandler.mouseY - 4 - ((MouseHandler.mouseY - 4) % 10);

		Rasterizer2D.drawTransparentBox(xPos, yPos, 10, 10, 0xffffff, 255);
		newSmallFont.drawCenteredString("(" + (xPos + 4) + ", " + (yPos + 4) + ")", xPos + 4, yPos - 1, 0xffff00, 0);
	}

	private boolean screenFlashDrawing;
	private boolean screenFlashOpacityDownward;
	private double screenFlashOpacity = 0d;
	private int screenFlashColor = 0;
	private int screenFlashMaxIntensity = 30;
	private boolean screenFlashAutoFadeOut;

	public boolean fogEnabled;
	public int fogOpacity;

	public void drawScreenBox() {
		if (screenFlashDrawing) {
			double rate = 1_000d / fps / 20d;
			if (screenFlashOpacityDownward) {
				screenFlashOpacity -= rate;
				if (screenFlashOpacity <= 10) {
					screenFlashOpacityDownward = false;
					screenFlashDrawing = false;
				}
			} else {
				screenFlashOpacity += rate;
				if (screenFlashOpacity >= screenFlashMaxIntensity) {
					screenFlashOpacityDownward = true;
				}
			}
			Rasterizer2D.drawAlphaGradient(0, 0, canvasWidth, canvasHeight, screenFlashColor, screenFlashColor, (int) screenFlashOpacity);
		}

		if (fogEnabled) {
			Rasterizer2D.drawAlphaBox(0, 0, canvasWidth, canvasHeight, 0xE67D2D, fogOpacity);
		}
	}


	public int getDragSetting(int interfaceId) {
		return interfaceId == 3214 ? Preferences.getPreferences().dragTime : 5;
	}



	public static int IDLE_TIME = 30000; // 1 minute = 3000
	public boolean hintMenu;
	public String hintName;
	public int hintId;

	private boolean pollActive;

	private Pattern pattern;

	private Matcher matcher;

	private boolean placeHolders = true;

	public static boolean debugModels = false;

	private EntityTarget entityTarget;

	private InformationFile informationFile = new InformationFile();

	public static boolean snowVisible = Configuration.CHRISTMAS ? true : false;

	public static int[][] runePouch = new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 } };

	public static int itemX = 0, itemY = 0;

	private static boolean shiftDrop = true;

	public static boolean shiftDown;
	public static boolean isCtrlPressed = false;

	private static boolean removeShiftDropOnMenuOpen;

	public static Settings getUserSettings() {
		return userSettings;
	}

	public static void setUserSettings(Settings settings) {
		Client.userSettings = settings;
	}

	private static Settings userSettings;

	/*
	 * @Override public void keyPressed(KeyEvent event) { MouseHandler.keyPressed(event);
	 * if(loggedIn) { stream.createFrame(186); stream.writeWord(event.getKeyCode());
	 * } }
	 */
	public static void mouseMoved() {
		if (loggedIn)
			// if(idleTime >= (Client.IDLE_TIME - 500))
			stream.createFrame(187);
	}

	/**
	 * Handles rune pouch input
	 * @return
	 * @author Sky
	 */
	private void loadPlayerData() throws IOException {
		File file = new File(Signlink.getCacheDirectory() + "/fkeys.dat");
		if (!file.exists()) {
			return;
		}

		DataInputStream stream = new DataInputStream(new FileInputStream(file));

		try {


			for(int i = 0; i < Keybinding.KEYBINDINGS.length; i++) {
				Keybinding.KEYBINDINGS[i] = stream.readByte();
			}

		} catch (IOException e) {
			System.out.println("Unable to load player data.");
			file.delete();
		} finally {
			stream.close();
		}
	}
	public void savePlayerData() {
		try {
			File file = new File(Signlink.getCacheDirectory() + "/fkeys.dat");
			if (!file.exists()) {
				file.createNewFile();
			}
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));

			try {
				for(int i = 0; i < 14; i++) {
					stream.writeByte(Keybinding.KEYBINDINGS[i]);
				}
			} finally {
				stream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleScrollPosition(String text, int frame) {
		if (text.startsWith(":scp:")) {
			int scrollPosition = Integer.parseInt(text.split(" ")[1]);
			RSInterface widget = RSInterface.interfaceCache[frame];
			widget.scrollPosition = scrollPosition;
		} else if (text.startsWith(":scpfind:")) {
			RSInterface widget = RSInterface.interfaceCache[frame];
			sendString(7, widget.scrollPosition + "");
		}

		return;
	}
	public static boolean handleRunePouch(String text, int frame) {
		if (!(text.startsWith("#") && text.endsWith("$") && frame == 49999)) {
			return false;
		}
		try {
			// System.out.println("got here");
			text = text.replace("#", "");
			text = text.replace("$", "");
			String[] runes = text.split("-");
			for (int index = 0; index < runes.length; index++) {
				String[] args = runes[index].split(":");
				int id = Integer.parseInt(args[0]);
				int amt = Integer.parseInt(args[1]);
				runePouch[index][0] = id;
				runePouch[index][1] = amt;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	private boolean updateGameMode = false;

	public void frameMode(boolean resizable) {
		if(isResized() == resizable) {
			return;
		}

		setResized(resizable);

		Bounds bounds = getFrameContentBounds();
		canvasWidth = !isResized() ? 765 : bounds.highX;
		canvasHeight = !isResized() ? 503 : bounds.highY;

		changeTabArea = !isResized() ? false : true;
		changeChatArea = !isResized() ? false : true;
		setMaxCanvasSize(canvasWidth, canvasHeight);
		ResizeableChanged event = new ResizeableChanged();
		event.setResized(resizable);
		callbacks.post(event);

		//setBounds();

		showChatComponents = !isResized() || showChatComponents;
		showTabComponents = !isResized() || showTabComponents;
	}

	private void setBounds() {
		Preferences.getPreferences().fixed = !isResized();
		Preferences.getPreferences().screenWidth = canvasWidth;
		Preferences.getPreferences().screenHeight = canvasHeight;


	}


	private long experienceCounter;
	private Sprite mapBack;
	private Sprite[] smallXpSprites = new Sprite[22];
	private static final long serialVersionUID = 1L;
	private Sprite[] inputSprites = new Sprite[7];

	private int modifiableXValue = 1; // u dont care if it starts at 1? Can't see a real problem with it :P kk
	private int achievementCutoff = 100;
	private Sprite[] minimapIcons = new Sprite[2];
	private String macAddress;

	public static void dumpModels() {
		int id = 6610;
		NpcDefinition npc = NpcDefinition.get(id);
		System.out.println(npc.name);
		if (npc.modelId != null) {
			for (int modelid = 0; modelid < npc.modelId.length; modelid++) {
				System.out.println(npc.modelId[modelid]);
			}
		}
	}

	private static String intToKOrMilLongName(int i) {
		String s = String.valueOf(i);
		for (int k = s.length() - 3; k > 0; k -= 3)
			s = s.substring(0, k) + "," + s.substring(k);
		if (s.length() > 8)
			s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
		else if (s.length() > 4)
			s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
		return " " + s;
	}

	private static String intToKOrMil(int j) {
		if (j < 0x186a0)
			return String.valueOf(j);
		if (j < 0x989680)
			return j / 1000 + "K";
		else
			return j / 0xf4240 + "M";
	}


	public static int spellID = 0;

	public static void setTab(int id) {
		needDrawTabArea = true;
		tabID = id;
		tabAreaAltered = true;
	}

	public void setSidebarInterface(int sidebarID, int interfaceID) {
		tabInterfaceIDs[sidebarID] = interfaceID;
		tabID = sidebarID;
		needDrawTabArea = true;
		tabAreaAltered = true;
	}

	private boolean menuHasAddFriend(int j) {
		if (j < 0)
			return false;
		int k = menuOpcodes[j];
		if (k >= 2000)
			k -= 2000;
		return k == 337;
	}

	private Sprite channelButtons;

	private void drawChannelButtons() {

		String text[] = { "On", "Friends", "Off", "Hide" };
		int textColor[] = { 65280, 0xffff00, 0xff0000, 65535 };
		int yOffset = (!isResized() ? 338 : canvasHeight - 165);

		if (hideChatArea) {
			channelButtons.drawSprite(0, canvasHeight - 23);
		}

		switch (channelButtonClickPosition) {
			case 0:
				chatButtons[1].drawSprite(5, 142 + yOffset);
				break;
			case 1:
				chatButtons[1].drawSprite(71, 142 + yOffset);
				break;
			case 2:
				chatButtons[1].drawSprite(137, 142 + yOffset);
				break;
			case 3:
				chatButtons[1].drawSprite(203, 142 + yOffset);
				break;
			case 4:
				chatButtons[1].drawSprite(269, 142 + yOffset);
				break;
			case 5:
				chatButtons[1].drawSprite(335, 142 + yOffset);
				break;
		}
		if (channelButtonHoverPosition == channelButtonClickPosition) {
			switch (channelButtonHoverPosition) {
				case 0:
					chatButtons[2].drawSprite(5, 142 + yOffset);
					break;
				case 1:
					chatButtons[2].drawSprite(71, 142 + yOffset);
					break;
				case 2:
					chatButtons[2].drawSprite(137, 142 + yOffset);
					break;
				case 3:
					chatButtons[2].drawSprite(203, 142 + yOffset);
					break;
				case 4:
					chatButtons[2].drawSprite(269, 142 + yOffset);
					break;
				case 5:
					chatButtons[2].drawSprite(335, 142 + yOffset);
					break;
				case 6:
					chatButtons[3].drawSprite(404, 142 + yOffset);
					break;
			}
		} else {
			switch (channelButtonHoverPosition) {
				case 0:
					chatButtons[0].drawSprite(5, 142 + yOffset);
					break;
				case 1:
					chatButtons[0].drawSprite(71, 142 + yOffset);
					break;
				case 2:
					chatButtons[0].drawSprite(137, 142 + yOffset);
					break;
				case 3:
					chatButtons[0].drawSprite(203, 142 + yOffset);
					break;
				case 4:
					chatButtons[0].drawSprite(269, 142 + yOffset);
					break;
				case 5:
					chatButtons[0].drawSprite(335, 142 + yOffset);
					break;
				case 6:
					chatButtons[3].drawSprite(404, 142 + yOffset);
					break;
			}
		}
		/**
		 * Simple format.
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		/**
		 * Sets timezone to USA
		 */
		dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		/**
		 * Updates button per tick.
		 */
		newSmallFont.drawCenteredString(""+dateFormat.format(new Date().getTime()), 459, 157 + yOffset, 0xffffff, 0);


		smallText.method389(true, 26, 0xffffff, "All", 157 + yOffset);
		smallText.method389(true, 86, 0xffffff, "Game", 152 + yOffset);
		smallText.method389(true, 150, 0xffffff, "Public", 152 + yOffset);
		smallText.method389(true, 212, 0xffffff, "Private", 152 + yOffset);
		smallText.method389(true, 286, 0xffffff, "Clan", 152 + yOffset);
		smallText.method389(true, 349, 0xffffff, "Trade", 152 + yOffset);
		smallText.method382(textColor[gameMode], 98, text[gameMode], 163 + yOffset, true);
		smallText.method382(textColor[publicChatMode], 164, text[publicChatMode], 163 + yOffset, true);
		smallText.method382(textColor[privateChatMode], 230, text[privateChatMode], 163 + yOffset, true);
		smallText.method382(textColor[clanChatMode], 296, text[clanChatMode], 163 + yOffset, true);
		smallText.method382(textColor[tradeMode], 362, text[tradeMode], 163 + yOffset, true);
	}

	ItemSearch grandExchangeItemSearch = null;

	public static int grandExchangeSearchScrollPostion;

	private static void handleGEItemSearchClick(int itemId) {
		// TODO Send Item Packet
		System.out.println("Clicking Item: " + itemId);
	}

	private void drawChatArea() {
		if (!isResized())
			hideChatArea = false;

		final int yOffset = (!isResized() ? 338 : canvasHeight - 165);
		final int yOffset2 = !isResized() ? 338 : 0;

		if (hideChatArea)
			drawChannelButtons();

		if (!hideChatArea) {
			chatArea.drawSprite(0, yOffset);
			drawChannelButtons();

			TextDrawingArea textDrawingArea = aTextDrawingArea_1271;
			if (MouseHandler.saveClickX >= 0 && MouseHandler.saveClickX <= 518) {
				if (MouseHandler.saveClickY >= (!isResized() ? 335 : canvasHeight - 164)
						&& MouseHandler.saveClickY <= (!isResized() ? 484 : canvasHeight - 30)) {
					if (this.isFieldInFocus()) {
						Client.inputString = "";
						this.resetInputFieldFocus();
					}
				}
			}
			if (messagePromptRaised) {
				newBoldFont.drawCenteredString(aString1121, 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(promptInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 1) {
				newBoldFont.drawCenteredString(enterInputInChatString, 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 2 || inputDialogState == 10) {
				newBoldFont.drawCenteredString(enterInputInChatString, 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 7) {
				newBoldFont.drawCenteredString("Enter the price for the item:", 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 8) {
				newBoldFont.drawCenteredString("Amount you want to sell:", 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (Bank.isSearchingBank()) {
				newBoldFont.drawCenteredString("Enter an item to search for:", 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(Bank.searchingBankString + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 3) {
				Rasterizer2D.fillPixels(8, 505, 108, 0x463214, 28 + yOffset);
				Rasterizer2D.drawAlphaBox(8, 28 + yOffset, 505, 108, 0x746346, 75);

				newBoldFont.drawCenteredString("@bla@What would you like to buy? @blu@" + amountOrNameInput + "*", 259,
						20 + yOffset, 128, -1);
				if (amountOrNameInput != "") {
					grandExchangeItemSearch = new ItemSearch(amountOrNameInput, 100, true);

					final int xPosition = 15;
					final int yPosition = 32 + yOffset - grandExchangeSearchScrollPostion;
					int rowCountX = 0;
					int rowCountY = 0;

					int itemAmount = grandExchangeItemSearch.getItemSearchResultAmount();

					if (amountOrNameInput.length() == 0) {
						newRegularFont.drawCenteredString("Start typing the name of an item to search for it.", 259,
								70 + yOffset, 0, -1);
					} else if (itemAmount == 0) {
						newRegularFont.drawCenteredString("No matching items found!", 259, 70 + yOffset, 0, -1);
					} else {
						Rasterizer2D.setDrawingArea(134 + yOffset, 8, 497, 29 + yOffset);
						for (int itemId = 0; itemId < itemAmount; itemId++) {
							int itemResults[] = grandExchangeItemSearch.getItemSearchResults();

							if (itemResults[itemId] != -1) {
								final int startX = xPosition + rowCountX * 160;
								final int startY = yPosition + rowCountY * 35;
								Sprite itemSprite = ItemDefinition.getSprite(itemResults[itemId], 1, 0);
								if (itemSprite != null)
									itemSprite.drawSprite(startX, startY);

								ItemDefinition itemDef = ItemDefinition.lookup(itemResults[itemId]);
								newRegularFont.drawBasicString(itemDef.name, startX + 40, startY + 14, 0, -1);

								if (MouseHandler.mouseX >= startX && MouseHandler.mouseX <= startX + 160) {
									if (MouseHandler.mouseY >= (startY + yOffset2)
											&& MouseHandler.mouseY <= (startY + yOffset2) + 35) {
										Rasterizer2D.drawAlphaBox(startX, startY, 160, 35, 0xFFFFFF, 120);

										if (clickMode3 == 1)
											handleGEItemSearchClick(itemDef.id);
									}
								}
							}

							rowCountX++;

							if (rowCountX > 2) {
								rowCountY++;
								rowCountX = 0;
							}
						}
						//Rasterizer2D.defaultDrawingAreaSize();
					}

					int maxScrollPosition = itemAmount / 3 * 35;
					if (itemAmount > 9)
						drawScrollbar(106, grandExchangeSearchScrollPostion > maxScrollPosition ? 0
								: grandExchangeSearchScrollPostion, 29 + yOffset, 496, itemAmount / 3 * 35);

				}
			} else if (clickToContinueString != null) {
				newBoldFont.drawCenteredString(clickToContinueString, 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString("Click to continue", 259, 80 + yOffset, 128, -1);
			} else if (backDialogID != -1) {
				drawInterface(0, 20, RSInterface.interfaceCache[backDialogID], 20 + yOffset);
			} else if (dialogID != -1) {
				drawInterface(0, 20, RSInterface.interfaceCache[dialogID], 20 + yOffset);
			} else {
				int j77 = -3;
				int j = 0;
				Rasterizer2D.setDrawingArea(0, 7 + yOffset, 497, 122 + yOffset);
				for (int k = 0; k < 500; k++)
					if (chatMessages[k] != null) {
						// System.out.println(chatMessages[k]);
						int chatType = chatTypes[k];
						int yPos = (70 - j77 * 14) + anInt1089 + 5;
						String s1 = chatNames[k];
						//byte byte0 = 0;

						List<Integer> crowns = new ArrayList<>();

						while (s1.startsWith("@cr")) {
							String s2 = s1.substring(3, s1.length());
							int index = s2.indexOf("@");
							if (index != -1) {
								s2 = s2.substring(0, index);
								crowns.add(Integer.parseInt(s2));
								s1 = s1.substring(4 + s2.length());
							}
						}
						if (chatType == 0) {
							if (chatTypeView == 5 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									newRegularFont.drawBasicString(chatMessages[k], 10, yPos - 1 + yOffset, 0, -1);
								}
								j++;
								j77++;
							}
						}
						if ((chatType == 1 || chatType == 2) && (chatType == 1 || publicChatMode == 0
								|| publicChatMode == 1 && isFriendOrSelf(s1))) {
							if (chatTypeView == 1 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									int xPos = 11;
									if (!crowns.isEmpty()) {
										for (int crown : crowns) {
											for (int right = 0; right < modIconss.length; right++) {
												if (right == (crown - 1) && modIconss[right] != null) {
													modIconss[right].drawAdvancedSprite(xPos - 1,
															yPos + yOffset - modIconss[right].myHeight);
													xPos += modIconss[right].myWidth;
													xPos += 2;
													break;
												}
											}
										}
									}
									newRegularFont.drawBasicString(s1 + ":", xPos - 1, yPos - 1 + yOffset, 0, -1);
									xPos += newRegularFont.getTextWidth(s1) + 5;
									newRegularFont.drawBasicString(chatMessages[k], xPos - 1, yPos - 1 + yOffset, 255,-1, false);
								}
								j++;
								j77++;
							}
						}

						if (chatType == 99) {
							if (yPos > 0 && yPos < 210) {
								lastViewedDropTable = chatMessages[k];
								newRegularFont.drawBasicString(s1 + " " + chatMessages[k], 10, yPos - 1 + yOffset, 0x7e3200, -1, false);
							}
							j++;
							j77++;
						}


						if ((chatType == 3 || chatType == 7) && (!splitPrivateChat || chatTypeView == 2)
								&& (chatType == 7 || privateChatMode == 0
								|| privateChatMode == 1 && isFriendOrSelf(s1))) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									int k1 = 11;
									newRegularFont.drawBasicString("From", k1, yPos - 1 + yOffset, 0, -1);
									k1 += textDrawingArea.getTextWidth("From ");
									if (!crowns.isEmpty()) {
										for (int crown : crowns) {
											for (int right = 0; right < modIconss.length; right++) {
												if (right == (crown - 1) && modIconss[right] != null) {
													modIconss[right].drawAdvancedSprite(k1,
															yPos + yOffset - modIconss[right].myHeight);
													k1 += modIconss[right].myWidth;
													break;
												}
											}
										}
									}
									newRegularFont.drawBasicString(s1 + ":", k1, yPos - 1 + yOffset, 0, -1);
									k1 += newRegularFont.getTextWidth(s1) + 8;
									newRegularFont.drawBasicString(chatMessages[k], k1, yPos - 1 + yOffset, 0x800000,-1, false);
								}
								j++;
								j77++;
							}
						}
						if (chatType == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
							if (chatTypeView == 3 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210)
									newRegularFont.drawBasicString(s1 + " " + chatMessages[k], 11, yPos - 1 + yOffset,0x800080, -1, false);
								j++;
								j77++;
							}
						}
						if (chatType == 31 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
							int crownId = 29;
							if (chatTypeView == 3 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									int xPos = 11;
									modIconss[crownId].drawAdvancedSprite(xPos - 1, yPos + yOffset - modIconss[crownId].myHeight);
									xPos += modIconss[crownId].myWidth;
									newRegularFont.drawBasicString(s1 + " " + chatMessages[k], xPos - 1, yPos - 1 + yOffset, 0x1950ce, -1, false);
								}
								j++;
								j77++;
							}
						}
						if (chatType == 5 && !splitPrivateChat && privateChatMode < 2) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210)
									newRegularFont.drawBasicString(chatMessages[k], 10, yPos - 1 + yOffset, 0x800000,-1, false);
								j++;
								j77++;
							}
						}
						if (chatType == 6 && (!splitPrivateChat || chatTypeView == 2) && privateChatMode < 2) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									int k1 = 15;
									k1 += textDrawingArea.getTextWidth("To " + s1);
									newRegularFont.drawBasicString("To " + s1 + ":", 10, yPos - 1 + yOffset, 0, -1);
									// newRegularFont.drawBasicString(chatMessages[k], 15 +
									// newRegularFont.getTextWidth("To :" + s1), yPos, 0x800000, -1);
									newRegularFont.drawBasicString(chatMessages[k], k1, yPos - 1 + yOffset, 0x800000,-1, false);
								}
								j++;
								j77++;
							}
						}

						// Clan chat
						if (chatType == 11 && (clanChatMode == 0)) {
							if (chatTypeView == 11 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									try {
										String lastChatNameEnd = "@dre@";
										String clanChatMessage = chatMessages[k];
										String chatMessage = clanChatMessage.substring(clanChatMessage.indexOf(lastChatNameEnd) + lastChatNameEnd.length());
										String usernameAndFormatting = clanChatMessage.substring(0, clanChatMessage.indexOf(lastChatNameEnd) + lastChatNameEnd.length());
										newRegularFont.drawBasicString(usernameAndFormatting, 8, yPos - 1 + yOffset, 0x7e3200, -1, true);
										newRegularFont.drawBasicString(chatMessage, newRegularFont.getTextWidth(usernameAndFormatting) + 8, yPos - 1 + yOffset, 0x7e3200, -1, false);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								j++;
								j77++;
							}
						}
						if (chatType == 13 && chatTypeView == 12) {
							if (yPos > 0 && yPos < 210)
								newRegularFont.drawBasicString(s1 + " " + chatMessages[k], 8, yPos - 1 + yOffset,0x7e3200, -1, false);
							j++;
							j77++;
						}
						if (chatType == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
							if (chatTypeView == 3 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210)
									newRegularFont.drawBasicString(s1 + " " + chatMessages[k], 10, yPos - 1 + yOffset,0x7e3200, -1, false);
								j++;
								j77++;
							}
						}
						if (chatType == 16) {
							int j2 = 40 + 11;
							String clanname = clanList[k];
							int clanNameWidth = textDrawingArea.getTextWidth(clanname);
							if (chatTypeView == 11 || chatTypeView == 0) {
								newRegularFont.drawBasicString("[", 19, yPos - 1 + yOffset, 0, -1);
								newRegularFont.drawBasicString("]", clanNameWidth + 16 + 10, yPos - 1 + yOffset, 0, -1);
								newRegularFont.drawBasicString("" + capitalize(clanname) + "", 25, yPos - 1 + yOffset,
										255, -1);
								newRegularFont.drawBasicString(chatNames[k] + ":", j2 - 17, yPos);
								j2 += newRegularFont.getTextWidth(chatNames[k]) + 7;
								newRegularFont.drawBasicString(capitalize(chatMessages[k]), j2 - 16, yPos - 1 + yOffset,0x800000, -1);

								j++;
								j77++;
							}
						}
					}
				Rasterizer2D.set_default_size();
				chatAreaScrollLength = j * 14 + 7 + 5;
				if (chatAreaScrollLength < 111)
					chatAreaScrollLength = 111;
				drawScrollbar(114, chatAreaScrollLength - anInt1089 - 113, 6 + yOffset, 496, chatAreaScrollLength);
				String fixedString;
				if (localPlayer != null && localPlayer.displayName != null) {
					if (localPlayer.title.length() > 0) {
						fixedString = "<col=" + localPlayer.titleColor + ">" + localPlayer.title + "</col>" + " "
								+ localPlayer.displayName;
					} else {
						fixedString = localPlayer.displayName;
					}
				} else {
					fixedString = StringUtils.fixName(capitalize(myUsername));
				}
				String s;
				if (localPlayer != null && localPlayer.displayName != null && localPlayer.title.length() > 1)
					s = localPlayer.title + " " + localPlayer.displayName;
				else
					s = StringUtils.fixName(capitalize(myUsername));
				Rasterizer2D.setDrawingArea(0, yOffset, 519, 142 + yOffset);
				int xOffset = 0;
				if (localPlayer.hasRightsOtherThan(PlayerRights.PLAYER)) {
					for (PlayerRights right : localPlayer.getDisplayedRights()) {
						if (right.hasCrown()) {
							modIconss[right.spriteId()].drawSprite(9 + xOffset, 134 + yOffset - modIconss[right.spriteId()].myHeight);
							xOffset += modIconss[right.spriteId()].myWidth;
							xOffset += 2;
						}
					}

					newRegularFont.drawBasicString(fixedString, 10 + xOffset, 133 + yOffset, 0, -1);
				} else {
					newRegularFont.drawBasicString(fixedString, 10 + xOffset, 133 + yOffset, 0, -1);
				}

				xOffset += newRegularFont.getTextWidth(fixedString) + 10;
				newRegularFont.drawBasicString(": ", xOffset, 133 + yOffset, 0, -1);
				xOffset += textDrawingArea.getTextWidth(": ");

				if (!isFieldInFocus()) {
					newRegularFont.drawBasicString(inputString + "*",xOffset, 133 + yOffset, 255, -1, false);
				}
				Rasterizer2D.set_default_size();
				Rasterizer2D.method339(120 + yOffset, 0x807660, 506, 7);
			}

		}

	}

	private String clanUsername;
	private String clanMessage;
	private String clanTitle;
	private String[] clanTitles;
	private EnumSet channelRights;

	public Socket openSocket(int port) throws IOException {
		return new Socket(InetAddress.getByName(server), port);
	}

	final void menu()
	{
		boolean var1 = false;
		int var2;
		int var5;
		while (!var1)
		{
			var1 = true;
			for (var2 = 0; var2 < getMenuOptionCount() - 1; ++var2)
			{
				if (getMenuOpcodes()[var2] < 1000 && getMenuOpcodes()[var2 + 1] > 1000)
				{
					sortMenuEntries(var2, var2 + 1);
					var1 = false;
				}
			}
			if (var1 && !isMenuOpen())
			{
				getCallbacks().post(new PostMenuSort());
			}
		}

		if (activeInterfaceType == 0)
		{

			int var19 = clickMode3;
			if (spellSelected == 1 && MouseHandler.saveClickX >= 516 && MouseHandler.saveClickY >= 160 && MouseHandler.saveClickX <= 765 && MouseHandler.saveClickY <= 205) {
				var19 = 0;
			}
			int var4;
			int var7;
			int var8;
			int var20;
			if (isMenuOpen())
			{
				int var3;
				if (var19 != 1 && (isMouseCam() || var19 != 4))
				{
					var2 = getMouseX();
					var3 = getMouseY();
					//this checks if the mouse is contained in the menu area
					boolean regmenuContainsMouse = (var2 >= getMenuX() - 10 && var2 <= getMenuX() + getMenuWidth() + 10 && var3 >= getMenuY() - 10 && var3 <= getMenuY() + getMenuHeight() + 10);
					boolean submenuContainsMouse = (var2 >= getSubmenuX() - 10 && var2 <= getSubmenuX() + getSubmenuWidth() + 10 && var3 >= getSubmenuY() - 10 && var3 <= getSubmenuY() + getSubmenuHeight() + 10);
					if (!regmenuContainsMouse && !submenuContainsMouse)
					{
						isMenuOpen = false;
						setSubmenuIdx(-1);
						for (var8 = 0; var8 < getRootWidgetCount(); ++var8)
						{
							//RESET
						}
					}
					else if (!submenuContainsMouse)
					{
						setSubmenuIdx(-1);
					}

					if (isMenuOpen() && getMouseWheelRotation() != 0)
					{
						if (regmenuContainsMouse && menuScrollMax > 0)
						{

							setMenuScroll(menuScroll + getMouseWheelRotation());
							if (menuScroll < 0)
							{
								menuScroll = 0;
							}
							else if (menuScroll > menuScrollMax)
							{
								menuScroll = menuScrollMax;
							}
						}

						if (submenuContainsMouse && submenuScrollMax > 0)
						{
							setSubmenuScroll(submenuScroll + getMouseWheelRotation());
							if (submenuScroll < 0)
							{
								submenuScroll = 0;
							}
							else if (submenuScroll > submenuScrollMax)
							{
								submenuScroll = submenuScrollMax;
							}
						}
					}
				}

				if (var19 == 1 || !isMouseCam() && var19 == 4)
				{
					var7 = -1;

					MenuEntry[] entries = getMenuEntries();
					int rootMenuTotalCount = 0;
					int submenuTotalCount = 0;
					for (var8 = 0; var8 < entries.length; ++var8)
					{
						MenuEntry e = entries[var8];
						if (e.getParent() == null)
						{
							rootMenuTotalCount++;
						}
						else if (((RSRuneLiteMenuEntry)e.getParent()).getIdx() == getSubmenuIdx())
						{
							submenuTotalCount++;
						}
					}
					int rootMenuCount = 0;
					int submenuCount = 0;
					for (var8 = 0; var8 < entries.length; ++var8)
					{
						MenuEntry e = entries[var8];
						if (e.getParent() == null)
						{
							int rowY = (rootMenuTotalCount - 1 - rootMenuCount - getMenuScroll()) * 15 + getMenuY() + 31;
							if (getMouseLastPressedX() > getMenuX() && getMouseLastPressedX() < getMenuWidth() + getMenuX() && getMouseLastPressedY() > rowY - 13 && getMouseLastPressedY() < rowY + 3)
							{
								var7 = var8;
							}
							rootMenuCount++;
						}
						else if (((RSRuneLiteMenuEntry)e.getParent()).getIdx() == getSubmenuIdx())
						{
							int rowY = (submenuTotalCount - 1 - submenuCount - getSubmenuScroll()) * 15 + getSubmenuY() + 31;
							if (getMouseLastPressedX() > getSubmenuX() && getMouseLastPressedX() < getSubmenuWidth() + getSubmenuX() && getMouseLastPressedY() > rowY - 13 && getMouseLastPressedY() < rowY + 3)
							{
								var7 = var8;
							}
							submenuCount++;
						}
					}

					clickedIdx = var7;
					int var11;
					int var12;
					int var16;
					int var15;
					if (var7 != -1 && var7 >= 0)
					{
						processMenuActions(var7);
					}

					isMenuOpen = false;

					for (var12 = 0; var12 < getRootWidgetCount(); ++var12)
					{
						//RESRT WIDGETS
					}
					setSubmenuIdx(-1);
				}
			}
			else
			{
				var2 = getMenuOptionCount() - 1;
				if (var19 == 1 && menuOptionsCount > 0) {
					long i1 = menuOpcodes[menuOptionsCount -1];
					if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74
							|| i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447
							|| i1 == 1125) {
						int l1 = menuArguments1[menuOptionsCount - 1];
						int j2 = menuArguments2[menuOptionsCount - 1];
						RSInterface class9 = RSInterface.interfaceCache[j2];
						if (class9.aBoolean259 || class9.aBoolean235) {
							aBoolean1242 = false;
							anInt989 = 0;
							draggingItemInterfaceId = j2;
							itemDraggingSlot = l1;
							activeInterfaceType = 2;
							anInt1087 = MouseHandler.saveClickX;
							anInt1088 = MouseHandler.saveClickY;
							if (RSInterface.interfaceCache[j2].parentID == openInterfaceID)
								activeInterfaceType = 1;
							if (RSInterface.interfaceCache[j2].parentID == backDialogID)
								activeInterfaceType = 3;
							return;
						}
					}
				}

				if ((var19 == 1 || !isMouseCam() && var19 == 4) && shouldLeftClickOpenMenu())
				{
					var19 = 2;
				}

				if ((var19 == 1 || !isMouseCam() && var19 == 4) && getMenuOptionCount() > 0 && var2 >= 0)
				{
					processMenuActions(var2);
				}

				if (var19 == 2 && getMenuOptionCount() > 0)
				{
					this.openMenu(getMouseLastPressedX(), getMouseLastPressedY());
				}
				if(loggedIn) {
					processTabClick();
					processMainScreenClick();

					// processTabClick2();
					processChatModeClick();
					minimapHovers();
					processChatModeClick();
				}
			}
		}
	}

	private boolean shouldLeftClickOpenMenu() {
		return false;
	}

	private int getMouseLastPressedY() {
		return MouseHandler.saveClickY;
	}

	private int getMouseLastPressedX() {
		return MouseHandler.saveClickX;
	}


	private final void minimapHovers() {
		final boolean fixed = !isResized();
		prayHover = fixed
			? prayHover = MouseHandler.mouseX >= 517 && MouseHandler.mouseX <= 573
			&& MouseHandler.mouseY >= (Configuration.osbuddyGameframe ? 81 : 76)
			&& MouseHandler.mouseY < (Configuration.osbuddyGameframe ? 109 : 107)
			: MouseHandler.mouseX >= canvasWidth - 210 && MouseHandler.mouseX <= canvasWidth - 157
			&& MouseHandler.mouseY >= (Configuration.osbuddyGameframe ? 90 : 55)
			&& MouseHandler.mouseY < (Configuration.osbuddyGameframe ? 119 : 104);
		runHover = fixed
			? runHover = MouseHandler.mouseX >= (Configuration.osbuddyGameframe ? 530 : 522)
			&& MouseHandler.mouseX <= (Configuration.osbuddyGameframe ? 582 : 594)
			&& MouseHandler.mouseY >= (Configuration.osbuddyGameframe ? 114 : 109)
			&& MouseHandler.mouseY < (Configuration.osbuddyGameframe ? 142 : 142)
			: MouseHandler.mouseX >= canvasWidth - (Configuration.osbuddyGameframe ? 196 : 186)
			&& MouseHandler.mouseX <= canvasWidth - (Configuration.osbuddyGameframe ? 142 : 132)
			&& MouseHandler.mouseY >= (Configuration.osbuddyGameframe ? 123 : 132)
			&& MouseHandler.mouseY < (Configuration.osbuddyGameframe ? 150 : 159);
		counterHover = fixed ? MouseHandler.mouseX >= 522 && MouseHandler.mouseX <= 544 && MouseHandler.mouseY >= 20 && MouseHandler.mouseY <= 47
			: MouseHandler.mouseX >= canvasWidth - 205 && MouseHandler.mouseX <= canvasWidth - 184 && MouseHandler.mouseY >= 27
			&& MouseHandler.mouseY <= 44;
		worldHover = fixed ? MouseHandler.mouseX >= 715 && MouseHandler.mouseX <= 740 && MouseHandler.mouseY >= 122 && MouseHandler.mouseY <= 135 // 132 160
			: MouseHandler.mouseX >= canvasWidth - 34 && MouseHandler.mouseX <= canvasWidth - 5 && MouseHandler.mouseY >= 143
			&& MouseHandler.mouseY <= 172;

		wikiHover = fixed ? MouseHandler.mouseX >= 704 && MouseHandler.mouseX <= 744 && MouseHandler.mouseY >= 150 && MouseHandler.mouseY <= 161
			: MouseHandler.mouseX >= canvasWidth - 34 && MouseHandler.mouseX <= canvasWidth - 5 && MouseHandler.mouseY >= 160 && MouseHandler.mouseY <= 173;

		teleOrbHover = fixed ? MouseHandler.mouseX >= 709 && MouseHandler.mouseX <= 734 && MouseHandler.mouseY >= 32 && MouseHandler.mouseY <= 57
			: MouseHandler.mouseX >= canvasWidth - 94 && MouseHandler.mouseX <= canvasWidth - 80 && MouseHandler.mouseY >= 160 && MouseHandler.mouseY <= 173;

		pouchHover = fixed ? MouseHandler.mouseX >= 678 && MouseHandler.mouseX <= 739 && MouseHandler.mouseY >= 129 && MouseHandler.mouseY <= 157 :
			MouseHandler.mouseX >= canvasWidth - 65 && MouseHandler.mouseX <= canvasWidth && MouseHandler.mouseY >= 154 && MouseHandler.mouseY <= 185;
	}

	private boolean prayHover, prayClicked;

	public static boolean counterOn;

	private boolean counterHover;

	private boolean worldHover;

	private boolean donatorHover;

	private boolean newHover;

	private boolean showClanOptions;

	public static int totalRead = 0;

	public static String getFileNameWithoutExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}

	public String indexLocation(int cacheIndex, int index) {
		return Signlink.getCacheDirectory() + "index" + cacheIndex + "/" + (index != -1 ? index + ".gz" : "");
	}

	public byte[] getModel(int Index) {
		try {
			File Model = new File(Signlink.getCacheDirectory() + "./pModels/" + Index + ".gz");
			byte[] aByte = new byte[(int) Model.length()];
			FileInputStream fis = new FileInputStream(Model);
			fis.read(aByte);
			System.out.println("" + Index + " aByte = [" + aByte + "]!");
			fis.close();
			return aByte;
		} catch (Exception e) {
			return null;
		}
	}


	public MapRegion currentMapRegion;
	private StringBuilder objectMaps = new StringBuilder();
	private StringBuilder floorMaps = new StringBuilder();
	public final void loadRegion() {
		if (!floorMaps.equals("") || !objectMaps.equals("")) {
			floorMaps = new StringBuilder();
			objectMaps = new StringBuilder();
		}
		Client.mapsLoaded = 0;
		boolean var1 = true;

		int var2;

		for (var2 = 0; var2 < regionLandArchives.length; var2++) {
			floorMaps.append("  ").append(regionLandIds[var2]);
			objectMaps.append("  ").append(regionLocIds[var2]);
			if (regionLandArchives[var2] == null && regionLandIds[var2] != -1) {
				regionLandArchives[var2] = Js5List.maps.getFile(regionLandIds[var2], 0);
				if (regionLandArchives[var2] == null) {
					var1 = false;
					++Client.mapsLoaded;
				}
			}
			if (regionMapArchives[var2] == null && regionLocIds[var2] != -1) {
				try {
					regionMapArchives[var2] = Js5List.maps.getFile(regionLocIds[var2], 0);
					if (regionMapArchives[var2] == null) {
						var1 = false;
						++Client.mapsLoaded;
					}
				} catch (Throwable e) {
					log.info("Missing xteas for region: {}", regions[var2]);
				}
			}
		}

		if (!var1) {
			Client.loadingType = 1;
		} else {
			Client.objectsLoaded = 0;
			var1 = true;

			int xChunk;
			int yChunk;
			for (var2 = 0; var2 < regionLandArchives.length; ++var2) {
				byte[] var3 = regionMapArchives[var2];
				if (var3 != null) {
					xChunk = 64 * (regions[var2] >> 8) - baseX;
					yChunk = 64 * (regions[var2] & 255) - baseY;
					if (isInInstance) {
						xChunk = 10;
						yChunk = 10;
					}
					var1 &= MapRegion.method787(var3, xChunk, yChunk);
				}
			}

			if (!var1) {
				Client.loadingType = 2;
			} else {
				if (0 != Client.loadingType) {
					drawLoadingMessage("Loading - please wait." + "<br>" + " (" + 100 + "%" + ")");
				}

				setGameState(GameState.LOADING);
				StaticSound.playPcmPlayers();
				lastKnownPlane = -1;
				incompleteAnimables.clear();

				projectiles.clear();
				scene.clear();
				for (int l2 = 0; l2 < 4; l2++) {
					for (int i3 = 0; i3 < 104; i3++) {
						for (int k3 = 0; k3 < 104; k3++)
							groundItems[l2][i3][k3] = null;

					}

				}
				unlinkMRUNodes();
				System.gc();

				for (var2 = 0; var2 < 4; ++var2) {
					collisionMaps[var2].init();
				}

				int zChunk;
				for (var2 = 0; var2 < 4; ++var2) {
					for (zChunk = 0; zChunk < 104; ++zChunk) {
						for (xChunk = 0; xChunk < 104; ++xChunk) {
							tileFlags[var2][zChunk][xChunk] = 0;
						}
					}
				}
				StaticSound.playPcmPlayers();
				currentMapRegion = new MapRegion(tileFlags, tileHeights);

				var2 = regionLandArchives.length;
				ObjectSound.clearObjectSounds();
				stream.createFrame(0);
				int tileBits;
				if (!isInInstance) {
					byte[] var6;
					for (zChunk = 0; zChunk < var2; ++zChunk) {
						xChunk = 64 * (regions[zChunk] >> 8) - baseX;
						yChunk = 64 * (regions[zChunk] & 255) - baseY;
						var6 = regionLandArchives[zChunk];
						if (null != var6) {
							StaticSound.playPcmPlayers();
							currentMapRegion.decode_map_terrain(
									var6, xChunk,
									yChunk,
									currentRegionX * 8 - 48, currentRegionY * 8 - 48,
									collisionMaps);
						}
					}

					for (zChunk = 0; zChunk < var2; ++zChunk) {
						xChunk = (regions[zChunk] >> 8) * 64 - baseX;
						yChunk = (regions[zChunk] & 255) * 64 - baseY;
						var6 = regionLandArchives[zChunk];
						if (null == var6 && currentRegionY < 800) {
							StaticSound.playPcmPlayers();
							currentMapRegion.method736(xChunk, yChunk, 64, 64);
						}
					}

					stream.createFrame(0);

					for (zChunk = 0; zChunk < var2; ++zChunk) {
						byte[] var15 = regionMapArchives[zChunk];
						if (var15 != null) {
							yChunk = 64 * (regions[zChunk] >> 8) - baseX;
							tileBits = 64 * (regions[zChunk] & 255) - baseY;
							StaticSound.playPcmPlayers();
							currentMapRegion.decode_map_locations(yChunk, collisionMaps, tileBits, scene, var15);
						}
					}
				}

				int level;
				int rotation;
				int worldX;
				if (isInInstance) {
					int worldY;
					int region;
					int var12;
					for (zChunk = 0; zChunk < 4; ++zChunk) {
						StaticSound.playPcmPlayers();
						for (xChunk = 0; xChunk < 13; ++xChunk) {
							for (yChunk = 0; yChunk < 13; ++yChunk) {
								boolean var18 = false;
								level = constructRegionData[zChunk][xChunk][yChunk];
								if (level != -1) {
									rotation = level >> 24 & 3;
									worldX = level >> 1 & 3;
									worldY = level >> 14 & 1023;
									region = level >> 3 & 2047;
									var12 = (worldY / 8 << 8) + region / 8;

									for (int var13 = 0; var13 < regions.length; ++var13) {
										if (regions[var13] == var12 && null != regionLandArchives[var13]) {
											currentMapRegion.method3968(
													regionLandArchives[var13],
													zChunk,
													8 * xChunk,
													8 * yChunk,
													rotation, 8 * (worldY & 7), 8 * (region & 7),
													worldX,
													collisionMaps);
											var18 = true;
											break;
										}
									}
								}

								if (!var18) {
									MapRegion.method1307(zChunk, xChunk * 8, 8 * yChunk);
								}
							}
						}
					}

					for (zChunk = 0; zChunk < 13; ++zChunk) {
						for (xChunk = 0; xChunk < 13; ++xChunk) {
							yChunk = constructRegionData[0][zChunk][xChunk];
							if (yChunk == -1) {
								currentMapRegion.method736(zChunk * 8, 8 * xChunk, 8, 8);
							}
						}
					}

					stream.createFrame(0);

					for (zChunk = 0; zChunk < 4; ++zChunk) {
						StaticSound.playPcmPlayers();
						for (xChunk = 0; xChunk < 13; ++xChunk) {
							for (yChunk = 0; yChunk < 13; ++yChunk) {
								tileBits = constructRegionData[zChunk][xChunk][yChunk];
								if (tileBits != -1) {
									level = tileBits >> 24 & 3;
									rotation = tileBits >> 1 & 3;
									worldX = tileBits >> 14 & 1023;
									worldY = tileBits >> 3 & 2047;
									region = worldY / 8 + (worldX / 8 << 8);

									for (var12 = 0; var12 < regions.length; ++var12) {
										if (regions[var12] == region && null != regionMapArchives[var12]) {
											currentMapRegion.method1668(
													regionMapArchives[var12],
													zChunk,
													xChunk * 8, 8 * yChunk,
													level,
													(worldX & 7) * 8, (worldY & 7) * 8,
													rotation,
													scene, collisionMaps);
											break;
										}
									}
								}
							}
						}
					}
				}

				stream.createFrame(0);
				StaticSound.playPcmPlayers();
				currentMapRegion.method6042(scene, collisionMaps);
				rasterProvider.init();
				stream.createFrame(0);

				zChunk = MapRegion.min_plane;
				if (zChunk > plane) {
					zChunk = plane;
				}

				if (zChunk < plane - 1) {
					zChunk = plane - 1;
				}

				if (low_detail) {
					scene.init(MapRegion.min_plane);
				} else {
					scene.init(0);
				}

				for (xChunk = 0; xChunk < 104; ++xChunk) {
					for (yChunk = 0; yChunk < 104; ++yChunk) {
						this.updateItemPile(xChunk, yChunk);
					}
				}

				StaticSound.playPcmPlayers();
				anInt1051++;
				if (anInt1051 > 98) {
					anInt1051 = 0;
					stream.createFrame(150);
				}

				stream.createFrame(210);
				stream.writeDWord(0x3f008edd);
				System.gc();


				if (!isInInstance) {
					xChunk = (currentRegionX - 6) / 8;
					yChunk = (currentRegionX + 6) / 8;
					tileBits = (currentRegionY - 6) / 8;
					level = (currentRegionY + 6) / 8;

					for (rotation = xChunk - 1; rotation <= 1 + yChunk; ++rotation) {
						for (worldX = tileBits - 1; worldX <= level + 1; ++worldX) {
							if (rotation < xChunk || rotation > yChunk || worldX < tileBits || worldX > level) {
								Js5List.maps.loadRegionFromName("m" + rotation + "_" + worldX);
								Js5List.maps.loadRegionFromName("l" + rotation + "_" + worldX);
							}
						}
					}
				}

				if (plane != lastKnownPlane) {
					lastKnownPlane = plane;
					renderMapScene(plane);
				}
				setGameState(GameState.LOGGED_IN);
			}
		}
	}

	public void unlinkMRUNodes() {
		ObjectDefinition.baseModels.clear();
		NpcDefinition.modelCache.clear();
		ItemDefinition.models.clear();
		ItemDefinition.sprites.clear();
		Player.mruNodes.clear();
	}

	public void renderMapScene(int i) {
		int ai[] = minimapImage.myPixels;
		int j = ai.length;

		for (int k = 0; k < j; k++)
			ai[k] = 0;

		for (int l = 1; l < 103; l++) {
			int i1 = 24628 + (103 - l) * 512 * 4;
			for (int k1 = 1; k1 < 103; k1++) {
				if ((tileFlags[i][k1][l] & 0x18) == 0)
					scene.draw_minimap_tile(ai, i1, i, k1, l);
				if (i < 3 && (tileFlags[i + 1][k1][l] & 8) != 0)
					scene.draw_minimap_tile(ai, i1, i + 1, k1, l);
				i1 += 4;
			}

		}

		int wallColor = (238 + (int) (Math.random() * 20.0D) - 10 << 16) + (238 + (int) (Math.random() * 20.0D) - 10 << 8) + (238 + (int) (Math.random() * 20.0D) - 10);
		int interactable = 238 + (int) (Math.random() * 20.0D) - 10 << 16;
		minimapImage.init();

		for (int y = 1; y < 103; ++y) {
			for (int x = 1; x < 103; ++x) {
				if ((tileFlags[plane][x][y] & 24) == 0) {
					drawMapScenes(plane, x, y, wallColor, interactable);
				}

				if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0) {
					drawMapScenes(plane + 1, x, y, wallColor, interactable);
				}
			}
		}

		rasterProvider.init();
		mapIconAmount = 0;
		for (int k2 = 0; k2 < 104; k2++) {
			for (int l2 = 0; l2 < 104; l2++) {
				long id = scene.getFloorDecorationTag(plane, k2, l2);
				int function = ObjectDefinition.lookup(ObjectKeyUtil.getObjectId(id)).minimapFunction;
				if (function >= 0 && AreaDefinition.lookup(function).field1940) {
					int k3 = k2;
					int l3 = l2;


					minimapHint[mapIconAmount] = AreaDefinition.lookup(function).getIconSprite();
					minimapHintX[mapIconAmount] = k3;
					minimapHintY[mapIconAmount] = l3;
					mapIconAmount++;
				}
			}

		}

	}

	public void updateItemPile(int plane, int x, int y) {
		NodeDeque var3 = groundItems[plane][x][y];
		if (var3 == null) {
			scene.removeGroundItemPile(plane, x, y);
		} else {
			long var4 = -99999999L;
			TileItem first = null;

			TileItem var7;
			for (var7 = (TileItem)var3.last(); var7 != null; var7 = (TileItem)var3.previous()) {
				ItemDefinition var8 = ItemDefinition.lookup(var7.id);
				long var9 = (long)var8.price;
				if (var8.stackable) {
					var9 *= var7.quantity < Integer.MAX_VALUE ? (long)(var7.quantity + 1) : (long)var7.quantity;
				}

				if (var9 > var4) {
					var4 = var9;
					first = var7;
				}
			}

			if (first == null) {
				scene.removeGroundItemPile(plane, x, y);
			} else {
				var3.addLast(first);
				TileItem second = null;
				TileItem third = null;

				for (var7 = (TileItem)var3.last(); var7 != null; var7 = (TileItem)var3.previous()) {
					if (first.id != var7.id) {
						if (second == null) {
							second = var7;
						}

						if (second.id != var7.id && third == null) {
							third = var7;
						}
					}
				}

				long key = ObjectKeyUtil.calculateTag(x, y, 3, false, 0);
				scene.add_ground_item(x, key, second, getCenterHeight(plane, y * 128 + 64, x * 128 + 64), third, first, plane, y);
			}
		}
	}
	private void showPrioritizedNPCs() {
		addNpcsToScene(true);
	}


	private void showOtherNpcs() {
		addNpcsToScene(false);
	}


	public final void addNpcsToScene(boolean arg0) {
		for (int idx = 0; idx < npcCount; ++idx) {
			Npc var3 = npcs[npcIndices[idx]];
			var3.setIndex(idx);
			if (null != var3 && var3.isVisible() && var3.desc.isVisible() == arg0) {
				int var4 = var3.x >> 7;
				int var5 = var3.y >> 7;
				if (var4 >= 0 && var4 < 104 && var5 >= 0 && var5 < 104) {
					if (1 == var3.desc.size && 64 == (var3.x & 127) && 64 == (var3.y & 127)) {
						if (viewportDrawCount == tileCycleMap[var4][var5]) {
							continue;
						}

						tileCycleMap[var4][var5] = viewportDrawCount;
					}

					long var6 = ObjectKeyUtil.calculateTag(0, 0, 1, !var3.desc.isInteractable, npcIndices[idx]);
					var3.lastUpdateTick = loopCycle;
					scene.add_entity(plane, var3.orientation, getCenterHeight(plane, var3.size * 64 - 64 + var3.y, var3.x + (var3.size * 64 - 64)), var6, var3.y, 60 + (var3.size * 64 - 64), var3.x, var3, var3.isWalking);
				}
			}
		}
	}

	public void loadError() {
		String s = "ondemand";// was a constant parameter
		System.out.println(s);
		try {
			getAppletContext().showDocument(new URL(getCodeBase(), "loaderror_" + s + ".html"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		do
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		while (true);
	}

	public int drawTabInterfaceHoverParent;
	public int drawTabInterfaceHover;
	public int drawTabInterfaceHoverLast;
	public long drawTabInterfaceHoverTimer;

	public RSInterface lastHovered;

	public void resetTabInterfaceHover() {
		drawTabInterfaceHover = 0;
		if (lastHovered != null) {
			lastHovered.isHovered = false;
			lastHovered.hoverRunnable.run();
			lastHovered = null;
		}
	}

	// interfaceClick
	public void buildInterfaceMenu(int xPosition, RSInterface widget, int mouseX, int yPosition, int mouseY, int j1) {
		if (widget == null) {
			return;
		}
		if (widget.type != 0 || widget.children == null || widget.isMouseoverTriggered)
			return;
		if (mouseX < xPosition || mouseY < yPosition || mouseX > xPosition + widget.width || mouseY > yPosition + widget.height)
			return;
		int childCount = widget.children.length;
		for (int childIndex = 0; childIndex < childCount; childIndex++) {
			try {
				int drawX = widget.childX[childIndex] + xPosition;
				int drawY = (widget.childY[childIndex] + yPosition) - j1;
				RSInterface childInterface = RSInterface.interfaceCache[widget.children[childIndex]];
				HoverMenuManager.reset();
				if (childInterface == null) {
					break;
				}
				if (childInterface.clickingDisabled) {
					break;
				}

				drawX += childInterface.anInt263;
				drawY += childInterface.anInt265;

				if (mouseX >= drawX && mouseY >= drawY && mouseX <= drawX + childInterface.width && mouseY <= drawY + childInterface.height) {
					if (childInterface.hoverInterfaceId != 0) {
						drawTabInterfaceHoverParent = childInterface.id;
						drawTabInterfaceHover = childInterface.hoverInterfaceId;
					}

					if (childInterface.hoverRunnable != null && lastHovered == null) {
						childInterface.isHovered = true;
						childInterface.hoverRunnable.run();
						lastHovered = childInterface;
					}
				}

				if (mouseX >= drawX && mouseY >= drawY && mouseX <= drawX + childInterface.width && mouseY <= drawY + childInterface.height) {
					childInterface.toggled = true;
				} else {
					childInterface.toggled = false;
				}

				if ((childInterface.mOverInterToTrigger >= 0 || childInterface.defaultHoverColor != 0) && mouseX >= drawX && mouseY >= drawY
						&& mouseX < drawX + childInterface.width && mouseY < drawY + childInterface.height) {
					if (childInterface.mOverInterToTrigger >= 0) {
						anInt886 = childInterface.mOverInterToTrigger;
					} else {
						anInt886 = childInterface.id;
					}
				}
				if (childInterface.atActionType == RSInterface.AT_ACTION_TYPE_OPTION_DROPDOWN) {

					boolean flag = false;
					childInterface.hovered = false;
					childInterface.dropdownHover = -1;

					if (childInterface.dropdown.isOpen()) {

						// Inverted keybinds dropdown
						if (childInterface.type == RSInterface.TYPE_KEYBINDS_DROPDOWN && childInterface.inverted && mouseX >= drawX
								&& mouseX < drawX + (childInterface.dropdown.getWidth() - 16)
								&& mouseY >= drawY - childInterface.dropdown.getHeight() - 10 && mouseY < drawY) {
							resetTabInterfaceHover();
							int yy = mouseY - (drawY - childInterface.dropdown.getHeight());

							if (mouseX > drawX + (childInterface.dropdown.getWidth() / 2)) {
								childInterface.dropdownHover = ((yy / 15) * 2) + 1;
							} else {
								childInterface.dropdownHover = (yy / 15) * 2;
							}
							flag = true;
						} else if (!childInterface.inverted && mouseX >= drawX && mouseX < drawX + (childInterface.dropdown.getWidth() - 16)
								&& mouseY >= drawY + 19 && mouseY < drawY + 19 + childInterface.dropdown.getHeight()) {
							resetTabInterfaceHover();
							int yy = mouseY - (drawY + 19);

							if (childInterface.type == RSInterface.TYPE_KEYBINDS_DROPDOWN && childInterface.dropdown.doesSplit()) {
								if (mouseX > drawX + (childInterface.dropdown.getWidth() / 2)) {
									childInterface.dropdownHover = ((yy / 15) * 2) + 1;
								} else {
									childInterface.dropdownHover = (yy / 15) * 2;
								}
							} else {
								childInterface.dropdownHover = yy / 14; // Regular dropdown hover
							}
							flag = true;
						}
						if (flag) {
							if (menuOptionsCount != 1) {
								menuOptionsCount = 1;
							}
							insertMenuItemNoShift("Select", MenuAction.DROPDOWN_SELECT, widget.id, childInterface.dropdownHover, childInterface.id);
						}
					}
					if (mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.dropdown.getWidth() && mouseY < drawY + 24
							&& menuOptionsCount == 1) {
						childInterface.hovered = true;
						insertMenuItemNoShift(childInterface.dropdown.isOpen() ? "Hide" : "Show", MenuAction.DROPDOWN, widget.id, childInterface.dropdownHover, childInterface.id);
					}
				}



				if (mouseX >= drawX && mouseY >= drawY && mouseX < drawX + (childInterface.type == 4 ? 100 : childInterface.width)
						&& mouseY < drawY + childInterface.height) {
					if (childInterface.actions != null && !childInterface.invisible && !childInterface.drawingDisabled) {

						if (!(childInterface.contentType == 206 && interfaceIsSelected(childInterface))) {
							if (/*(childInterface.type == 4 && childInterface.message.length() > 0) || */childInterface.type == 5) {

								boolean drawOptions = true;

								// HARDCODE CLICKABLE TEXT HERE
								if (childInterface.parentID == 37128) { // Clan chat interface, dont show options for guests
									drawOptions = showClanOptions;
								}

								if (drawOptions) {
									for (int action = childInterface.actions.length - 1; action >= 0; action--) {
										if (childInterface.actions[action] != null) {
											String s = childInterface.actions[action]
													+ (childInterface.type == 4 ? " @or1@" + childInterface.message : "");

											if (s.contains("img")) {
												int prefix = s.indexOf("<img=");
												int suffix = s.indexOf(">");
												s = s.replaceAll(s.substring(prefix + 5, suffix), "");
												s = s.replaceAll("</img>", "");
												s = s.replaceAll("<img=>", "");
											}
											if (s.contains("ico")) {
												int prefix = s.indexOf("<ico=");
												int suffix = s.indexOf(">");
												s = s.replaceAll(s.substring(prefix + 5, suffix), "");
												s = s.replaceAll("</ico>", "");
												s = s.replaceAll("<ico=>", "");
											}
											insertMenuItemNoShift(s, MenuAction.UNKNOWN_WIDGET, 0, action, childInterface.id);
										}
									}
								}
							}
						}
					}
				}
				if (childInterface.type == 22) {
					RSInterface class9_2;
					int slot = childInterface.grandExchangeSlot;
					if (grandExchangeInformation[slot][0] == -1)
						class9_2 = RSInterface.interfaceCache[GrandExchange.grandExchangeBuyAndSellBoxIds[slot]];
					else
						class9_2 = RSInterface.interfaceCache[GrandExchange.grandExchangeItemBoxIds[slot]];
					buildInterfaceMenu(drawX, class9_2, mouseX, drawY, mouseY, j1);
				}
				if (childInterface.type == 9 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width && mouseY < drawY + childInterface.height) {
					anInt1315 = childInterface.id;
				}
				if (childInterface.type == 5 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width && mouseY < drawY + childInterface.height) {
					hoverId = childInterface.id;
				}

				if (childInterface.type == 0) {
					buildInterfaceMenu(drawX, childInterface, mouseX, drawY, mouseY, childInterface.scrollPosition);
					if (childInterface.scrollMax > childInterface.height) {
						if (scrollbarVisible(childInterface)) {
							method65(drawX + childInterface.width, childInterface.height, mouseX, mouseY, childInterface, drawY, true, childInterface.scrollMax);
						}
					}
				} else {
					if (childInterface.atActionType == RSInterface.OPTION_OK && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width
							&& mouseY < drawY + childInterface.height) {
						boolean flag = false;
						if (childInterface.contentType != 0)
							flag = buildFriendsListMenu(childInterface);
						if (!flag) {
							insertMenuItemNoShift(childInterface.tooltip, MenuAction.WIDGET_TYPE_1, -1, -1, childInterface.id);
						}
						if (childInterface.type == RSInterface.TYPE_HOVER || childInterface.type == RSInterface.TYPE_CONFIG_HOVER
								|| childInterface.type == RSInterface.TYPE_ADJUSTABLE_CONFIG
								|| childInterface.type == RSInterface.TYPE_BOX) {
							childInterface.toggled = true;
						}

					}
					if (childInterface.atActionType == 2 && spellSelected == 0 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width
							&& mouseY < drawY + childInterface.height) {
						String s = childInterface.selectedActionName;
						if (s.indexOf(" ") != -1)
							s = s.substring(0, s.indexOf(" "));
						insertMenuItemNoShift(s + " @gre@" + childInterface.spellName, MenuAction.SPELL, -1, -1, childInterface.id);
					}
					// close interface button
					if (childInterface.atActionType == 3 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width
							&& mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						insertMenuItemNoShift("Close", MenuAction.CLOSE, -1, -1, childInterface.id);
					}
					if (childInterface.atActionType == 4 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width
							&& mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						insertMenuItemNoShift(childInterface.tooltip, MenuAction.TOGGLE_SETTINGS, 0, 0, childInterface.id);
					}
					if (childInterface.atActionType == RSInterface.TYPE_TOOLTIP && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width
							&& mouseY < drawY + childInterface.height) {
						if (childInterface.tooltips != null) {
							for(int index = 0; index < childInterface.tooltips.length; index++) {
								if (childInterface.tooltips[index] != null) {
									if(childInterface.tooltips[index] != "") {
										insertMenuItemNoShift(childInterface.tooltips[index], MenuAction.WIDGET_TYPE_1, 315, index, childInterface.id);
									}
								}
							}
						} else {
							if(childInterface.tooltip != "") {
								insertMenuItemNoShift(childInterface.tooltip, MenuAction.WIDGET_TYPE_1, 0, 0, childInterface.id);
							}
						}
					}

					if (childInterface.atActionType == 5 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width
							&& mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						insertMenuItemNoShift(childInterface.tooltip, MenuAction.OPTION_RESET_SETTINGS, 0, 0, childInterface.id);
					}
					if (childInterface.atActionType == 6 && !continuedDialogue && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width
							&& mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						insertMenuItemNoShift(childInterface.tooltip, MenuAction.OPTION_CONTINUE, 0, 0, childInterface.id);
					}

					if (spellSelected == 0 && childInterface.atActionType == RSInterface.AT_ACTION_TYPE_AUTOCAST && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width && mouseY < drawY + childInterface.height) {
						insertMenuItemNoShift(childInterface.tooltip, MenuAction.AUTOCAST_MENU_ACTION_ID, 0, 0, childInterface.id);
					}

					if (childInterface.type == 8 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width && mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						anInt1315 = childInterface.id;
					}
					if (childInterface.atActionType == 8 && !continuedDialogue && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width && mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						for (int s1 = 0; s1 < childInterface.tooltips.length; s1++) {
							insertMenuItemNoShift(childInterface.tooltips[s1], 1700 + s1, 0, 0, childInterface.id);
						}
					}
					if (childInterface.atActionType == 9 && !continuedDialogue && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width && mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						insertMenuItemNoShift(childInterface.tooltip, MenuAction.WIDGET_DUNNO_2, 0, 0, childInterface.id);
					}
					if (childInterface.atActionType == 10 && !continuedDialogue && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width
							&& mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						insertMenuItemNoShift(childInterface.getMenuItem().getText(), MenuAction.WIDGET_DUNNO_3, 0, 0, childInterface.id);
					}
					if (childInterface.atActionType == 11 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + childInterface.width && mouseY < drawY + childInterface.height) {
						HoverMenuManager.reset();
						insertMenuItemNoShift(childInterface.tooltip, MenuAction.WIDGET_DUNNO_4, 0, 0, childInterface.id);
					}
					if (mouseX >= drawX && mouseY >= drawY && mouseX < drawX + (childInterface.type == 4 ? 100 : childInterface.width)
							&& mouseY < drawY + childInterface.height) {

						if (childInterface.actions != null) {
							if ((childInterface.type == 4 && childInterface.message.length() > 0) || childInterface.type == 5) {
								for (int action = childInterface.actions.length - 1; action >= 0; action--) {
									if (childInterface.actions[action] != null) {
										HoverMenuManager.reset();
										insertMenuItemNoShift(childInterface.actions[action] + (childInterface.type == 4 ? " " + childInterface.message : ""), MenuAction.UNKNOWN_WIDGET, 0, action, childInterface.id);
									}
								}
							}
						}
					}
					if (childInterface.type == 2) {
						int k2 = 0;
						for (int l2 = 0; l2 < childInterface.height; l2++) {
							for (int i3 = 0; i3 < childInterface.width; i3++) {
								boolean smallSprite = openInterfaceID == 26000
										&& GrandExchange.isSmallItemSprite(childInterface.id) || childInterface.smallInvSprites;
								int size = smallSprite ? 18 : 32;
								int j3 = drawX + i3 * (size + childInterface.invSpritePadX);
								int k3 = drawY + l2 * (size + childInterface.invSpritePadY);
								if (k2 < 20) {
									j3 += childInterface.spritesX[k2];
									k3 += childInterface.spritesY[k2];
								}
								if (mouseX >= j3 && mouseY >= k3 && mouseX < j3 + size && mouseY < k3 + size) {
									mouseInvInterfaceIndex = k2;
									lastActiveInvInterface = childInterface.id;
									int itemID = childInterface.inventoryItemId[k2] - 1;
									if (childInterface.id == 23231) {
										itemID = (childInterface.inventoryItemId[k2] & 0x7FFF) - 1;
									}
									if (childInterface.inventoryItemId[k2] > 0) {
										ItemDefinition itemDef = ItemDefinition.lookup(itemID);
										boolean hasDestroyOption = false;
										if (itemSelected == 1 && childInterface.isInventoryInterface) {
											if (childInterface.id != anInt1284 || k2 != anInt1283) {
												insertMenuItemNoShift("Use " + selectedItemName + " with @lre@" + itemDef.name, MenuAction.ITEM_USE_ON_WIDGET_ITEM, itemDef.id, k2, childInterface.id);
											}
										} else if (spellSelected == 1 && childInterface.isInventoryInterface) {
											if ((spellUsableOn & 0x10) == 16) {
												insertMenuItemNoShift(spellTooltip + " @lre@" + itemDef.name, MenuAction.SPELL_CAST_ON_WIDGET, itemDef.id, k2, childInterface.id);
											}
										} else {
											if (childInterface.isInventoryInterface && fullscreenInterfaceID != 44900) {

												for (int type = 4; type >= 3; type--)
													if (itemDef.interfaceOptions != null && itemDef.interfaceOptions[type] != null) {

														int type0 = 0;
														if (HoverMenuManager.shouldDraw(itemDef.id)) {
															HoverMenuManager.showMenu = true;
															HoverMenuManager.hintName = itemDef.name;
															HoverMenuManager.hintId = itemDef.id;
														} else {
															HoverMenuManager.reset();
														}
														if (itemDef.interfaceOptions[type].contains("Wield") || itemDef.interfaceOptions[type].contains("Wear")) {
															hintMenu = true;
															hintName = itemDef.name;
															hintId = itemDef.id;
														} else {
															hintMenu = false;
														}
														if (type == 3)
															type0 = 493;
														if (type == 4)
															type0 = 847;
														hasDestroyOption = itemDef.interfaceOptions[type].contains("Destroy");

														insertMenuItemNoShift(itemDef.interfaceOptions[type] + " @lre@" + itemDef.name, MenuAction.of(type0), itemDef.id, k2, childInterface.id);
													} else if (type == 4) {
														insertMenuItemNoShift("Drop" + " @lre@" + itemDef.name, MenuAction.ITEM_DROP, itemDef.id, k2, childInterface.id);
													}

											}
											if (childInterface.usableItemInterface) {
												if (HoverMenuManager.shouldDraw(itemDef.id)) {
													HoverMenuManager.showMenu = true;
													HoverMenuManager.hintName = itemDef.name;
													HoverMenuManager.hintId = itemDef.id;
												} else {
													HoverMenuManager.reset();
												}
												hintMenu = false;
												insertMenuItemNoShift("Use @lre@" + itemDef.name, MenuAction.ITEM_USE, itemDef.id, k2, childInterface.id);

												if (!hasDestroyOption && !isMenuOpen && shiftDrop && shiftDown) {
													insertMenuItemNoShift("Drop @lre@" + itemDef.name, MenuAction.ITEM_DROP, itemDef.id, k2, childInterface.id);
													removeShiftDropOnMenuOpen = true;
												}
											}
											if (childInterface.isInventoryInterface && itemDef.interfaceOptions != null) {
												for (int type = 2; type >= 0; type--)
													if (itemDef.interfaceOptions[type] != null) {

														if (itemDef.interfaceOptions[type].contains("Wield") || itemDef.interfaceOptions[type].contains("Wear")) {
															HoverMenuManager.showMenu = true;
															HoverMenuManager.hintName = itemDef.name;
															HoverMenuManager.hintId = itemDef.id;
														} else {
															HoverMenuManager.reset();
														}
														if (HoverMenuManager.showMenu) {
															HoverMenuManager.drawHintMenu();
														}

														int type0 = 0;

														if (type == 0)
															type0 = 74;
														if (type == 1)
															type0 = 454;
														if (type == 2)
															type0 = 539;


														insertMenuItemNoShift(itemDef.interfaceOptions[type]
																+ " @lre@" + itemDef.name, MenuAction.of(type0), itemDef.id, k2, childInterface.id);

														if (!hasDestroyOption && !isMenuOpen && shiftDrop && shiftDown) {
															insertMenuItemNoShift("Drop @lre@" + itemDef.name, MenuAction.ITEM_DROP, itemDef.id, k2, childInterface.id);
															removeShiftDropOnMenuOpen = true;
														}
													}

											}

											if (childInterface.actions != null) {
												if (openInterfaceID == 21553 && childInterface.parentID == 3213) {
													// Equipment
													String[] options = new String[]{"Place in inventory", "Use as equipment"};
													for (int type = 8; type >= 0; type--) {
														if (type > options.length - 1)
															continue;
														String option = childInterface.actions[type];
														if (options[type] != null) {
															int type0 = 0;
															if (type == 0)
																type0 = 632;
															if (type == 1)
																type0 = 78;
															if (type == 2)
																type0 = 867;
															if (type == 3)
																type0 = 431;
															if (type == 4)
																type0 = 53;
															if (type == 7)
																type0 = 1337;

															MenuAction action = MenuAction.of(type0);
															insertMenuItemNoShift(option + " @lre@" + itemDef.name, action, itemDef.id, k2, childInterface.id);
														}
													}

												} else {
													// Standard containers
													if (Bank.isBankContainer(childInterface)) {
														if (childInterface.id == 5382 && placeHolders && childInterface.inventoryAmounts[k2] <= 0) {
															childInterface.actions = new String[8];
															childInterface.actions[1] = "Release";
														} else {
															if (modifiableXValue > 0) {// so issue is when x = 0
																if (childInterface.actions.length < 9) {
																	childInterface.actions = new String[]{
																			"Withdraw-1",
																			"Withdraw-5",
																			"Withdraw-10",
																			"Withdraw-All",
																			"Withdraw-X",
																			"Withdraw-" + modifiableXValue,
																			"Withdraw-All-but-1"
																	};
																} else {
																	childInterface.actions[5] = "Withdraw-" + modifiableXValue;
																}
															}
														}
													}

													for (int j4 = 8; j4 >= 0; j4--) {
														if (j4 > childInterface.actions.length - 1)
															continue;
														if (childInterface.actions[j4] != null) {
															String[] menuTarget = new String[30];
															menuTarget[j4] = childInterface.actions[j4] + " @lre@" + itemDef.name;
															int type0 = 0;
															if (childInterface.id != 1688/*equipment*/) {
																if (j4 == 0)
																	type0 = 632;
																if (j4 == 1)
																	type0 = 78;
																if (j4 == 2)
																	type0 = 867;
																if (j4 == 3)
																	type0 = 431;
																if (j4 == 4)
																	type0 = 53;
																if (j4 == 5)
																	type0 = 300;
																if (j4 == 6)
																	type0 = 291;

															} else {
																if (itemDef.equipActions[j4] == null) {
																	menuTarget[3] = "Operate @lre@" + itemDef.name;
																	menuTarget[4] = "Remove @lre@" + itemDef.name;
																} else {
																	childInterface.actions = itemDef.equipActions;
																	menuTarget[menuOptionsCount] = itemDef.equipActions[j4] + " @lre@" + itemDef.name;
																}
																if (j4 == 0)
																	type0 = 632; // remove
																if (j4 == 1)
																	type0 = 661; // operate 1
																if (j4 == 2)
																	type0 = 662; // operate 2
																if (j4 == 3)
																	type0 = 663; // operate 3
																if (j4 == 4)
																	type0 = 664; // operate 4
															}


															MenuAction action = MenuAction.of(type0);
															insertMenuItemNoShift(menuTarget[j4], action, itemDef.id, k2, childInterface.id);
														}
													}
												}
											}
										}

										if (childInterface.parentID >= 58040 && childInterface.parentID <= 58048
												|| childInterface.parentID >= 32100 && childInterface.parentID <= 32156
												|| childInterface.parentID >= 32200 && childInterface.parentID <= 32222) {
											return;
										}

										if (childInterface.isItemSearchComponent) {
											insertMenuItemNoShift("Select @lre@" + itemDef.name, MenuAction.ITEM_SELECT, itemDef.id, k2, childInterface.id);
										} else {
											insertMenuItemNoShift("Examine @lre@" + itemDef.name, MenuAction.EXAMINE_ITEM, itemDef.id, k2, childInterface.id);
										}
									}
								}
								k2++;
							}

						}

					}
				}

			} catch (Exception | StackOverflowError e) {
				System.err.println(String.format("Error building interface menu: rootId=%d, childIndex=%d, childId=%d", widget.id, childIndex, widget.children[childIndex]));
				e.printStackTrace();
			}
		}
	}

	public void drawScrollbar(int height, int scrollPosition, int yPosition, int xPosition, int scrollMax) {
		scrollBar1.drawSprite(xPosition, yPosition);
		scrollBar2.drawSprite(xPosition, (yPosition + height) - 16);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x000001, 16);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x3d3426, 15);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x342d21, 13);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x2e281d, 11);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x29241b, 10);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x252019, 9);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x000001, 1);
		int k1 = ((height - 32) * height) / scrollMax;
		if (k1 < 8)
			k1 = 8;
		int l1 = ((height - 32 - k1) * scrollPosition) / (scrollMax - height);
		Rasterizer2D.drawPixels(k1, yPosition + 16 + l1, xPosition, barFillColor, 16);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x000001, k1, xPosition);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x817051, k1, xPosition + 1);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x73654a, k1, xPosition + 2);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x6a5c43, k1, xPosition + 3);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x6a5c43, k1, xPosition + 4);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x655841, k1, xPosition + 5);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x655841, k1, xPosition + 6);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x61553e, k1, xPosition + 7);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x61553e, k1, xPosition + 8);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x5d513c, k1, xPosition + 9);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x5d513c, k1, xPosition + 10);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x594e3a, k1, xPosition + 11);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x594e3a, k1, xPosition + 12);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x514635, k1, xPosition + 13);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x4b4131, k1, xPosition + 14);
		Rasterizer2D.method339(yPosition + 16 + l1, 0x000001, 15, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x000001, 15, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x655841, 14, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x6a5c43, 13, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x6d5f48, 11, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x73654a, 10, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x76684b, 7, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x7b6a4d, 5, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x7e6e50, 4, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x817051, 3, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x000001, 2, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x564b38, 15, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x5d513c, 14, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x625640, 11, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x655841, 10, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x6a5c43, 7, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x6e6046, 5, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x716247, 4, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x7b6a4d, 3, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x817051, 2, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x000001, 1, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x514635, 15, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x564b38, 14, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x5d513c, 11, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x61553e, 9, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x655841, 7, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x6a5c43, 5, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x6e6046, 4, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x73654a, 3, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x817051, 2, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x000001, 1, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x4b4131, 15, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x544936, 14, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x594e3a, 13, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x5d513c, 10, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x61553e, 8, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x655841, 6, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x6a5c43, 4, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x73654a, 3, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x817051, 2, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x000001, 1, xPosition);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x000001, k1, xPosition + 15);
		Rasterizer2D.method339(yPosition + 15 + l1 + k1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x000001, 15, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x3f372a, 14, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x443c2d, 10, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x483e2f, 9, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x4a402f, 7, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x4b4131, 4, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x564b38, 3, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x000001, 2, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x443c2d, 15, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x4b4131, 11, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x514635, 9, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x544936, 7, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x564b38, 6, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x594e3a, 4, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x625640, 3, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x6a5c43, 2, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x000001, 1, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x443c2d, 15, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x4b4131, 14, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x544936, 12, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x564b38, 11, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x594e3a, 10, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x5d513c, 7, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x61553e, 4, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x6e6046, 3, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x7b6a4d, 2, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x000001, 1, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x4b4131, 15, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x514635, 14, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x564b38, 13, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x594e3a, 11, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x5d513c, 9, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x61553e, 7, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x655841, 5, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x6a5c43, 4, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x73654a, 3, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x7b6a4d, 2, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x000001, 1, xPosition);
	}

	/**
	 * NPC Updating
	 * <p>
	 * There is a crash at random npcs so i did some sort of cheap fix to stop it
	 * from throwing runtime exception which will ultimately log a player out, even
	 * regardless of where and when they are in the game, causes to many issues.
	 *
	 * @param stream
	 * @param i
	 */
	public void updateNPCs(Buffer stream, int i) {
		anInt839 = 0;
		anInt893 = 0;
		method139(stream);
		method46(i, stream);
		updateNpcState(stream);
		for (int k = 0; k < anInt839; k++) {
			int l = anIntArray840[k];
			if (npcs[l].lastUpdateTick != loopCycle) {
				callbacks.post(new NpcDespawned(npcs[l]));
				npcs[l].desc = null;
				npcs[l] = null;
			}
		}

		// Cheaphax fix to it doesnt throw exception and boot player
		if (stream.currentPosition == -1 || stream.currentPosition != i) {
			System.out.println("[NPC] Size mismatch : returning");
			return;
		}

		if (stream.currentPosition != i) {
			Signlink.reporterror(
					myUsername + " size mismatch in getnpcpos - pos:" + stream.currentPosition + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for (int i1 = 0; i1 < npcCount; i1++) {
			if (npcs[npcIndices[i1]] == null) {
				Signlink.reporterror(myUsername + " null entry in npc list - pos:" + i1 + " size:" + npcCount);
				throw new RuntimeException("eek");
			}
		}
		callbacks.updateNpcs();
	}

	private int channelButtonHoverPosition;
	private int channelButtonClickPosition;

	public void processChatModeClick() {
		if (MouseHandler.mouseY >= canvasHeight - 22 && MouseHandler.mouseY <= canvasHeight) {
			if (MouseHandler.mouseX >= 5 && MouseHandler.mouseX <= 61) {
				channelButtonHoverPosition = 0;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 71 && MouseHandler.mouseX <= 127) {
				channelButtonHoverPosition = 1;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 137 && MouseHandler.mouseX <= 193) {
				channelButtonHoverPosition = 2;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 203 && MouseHandler.mouseX <= 259) {
				channelButtonHoverPosition = 3;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 269 && MouseHandler.mouseX <= 325) {
				channelButtonHoverPosition = 4;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 335 && MouseHandler.mouseX <= 391) {
				channelButtonHoverPosition = 5;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 404 && MouseHandler.mouseX <= 515) {
				channelButtonHoverPosition = 6;
				inputTaken = true;
			}
		} else {
			channelButtonHoverPosition = -1;
			inputTaken = true;
		}
		if (clickMode3 == 1) {
			if (MouseHandler.saveClickY >= canvasHeight - 22 && MouseHandler.saveClickY <= canvasHeight) {
				if (MouseHandler.saveClickX >= 5 && MouseHandler.saveClickX <= 61) {
					if (channelButtonClickPosition == 0)
						toggleHideChatArea();
					channelButtonClickPosition = 0;
					chatTypeView = 0;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 71 && MouseHandler.saveClickX <= 127) {
					if (channelButtonClickPosition == 1)
						toggleHideChatArea();
					channelButtonClickPosition = 1;
					chatTypeView = 5;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 137 && MouseHandler.saveClickX <= 193) {
					if (channelButtonClickPosition == 2)
						toggleHideChatArea();
					channelButtonClickPosition = 2;
					chatTypeView = 1;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 203 && MouseHandler.saveClickX <= 259) {
					if (channelButtonClickPosition == 3)
						toggleHideChatArea();
					channelButtonClickPosition = 3;
					chatTypeView = 2;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 269 && MouseHandler.saveClickX <= 325) {
					if (channelButtonClickPosition == 4)
						toggleHideChatArea();
					channelButtonClickPosition = 4;
					chatTypeView = 11;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 335 && MouseHandler.saveClickX <= 391) {
					if (channelButtonClickPosition == 5)
						toggleHideChatArea();
					channelButtonClickPosition = 5;
					chatTypeView = 3;
					inputTaken = true;
				}
			}
		}
	}

	private static boolean hideChatArea = false;

	private static void toggleHideChatArea() {
		if (!instance.isResized())
			return;
		hideChatArea = !hideChatArea;
	}

	public void updateVarp(int i) {
		ObjectSound.update();
		if (i > Js5List.getConfigSize(Js5ConfigType.VARPLAYER))
			return;

		int j = VariablePlayer.lookup(i).type;
		if (j == 0)
			return;
		int k = variousSettings[i];
		if (j == 1) {
//			if (k == 1)
//				Rasterizer.setBrightness(0.90000000000000002D);
//			if (k == 2)
//				Rasterizer.setBrightness(0.80000000000000004D);
//			if (k == 3)
//				Rasterizer.setBrightness(0.69999999999999996D);
//			if (k == 4)
//				Rasterizer.setBrightness(0.59999999999999998D);
			ItemDefinition.sprites.clear();
			welcomeScreenRaised = true;
		}
		if (j == 3) {
			int volume = 0;

			if (k == 0) {
				volume = 255;
			} else if (k == 1) {
				volume = 192;
			} else if (k == 2) {
				volume = 128;
			} else if (k == 3) {
				volume = 64;
			} else if (k == 4) {
				volume = 0;
			}

			if (volume != musicVolume) {
				if (musicVolume != 0 || currentSong == -1) {
					if (volume != 0) {
						// setVolume(volume);
					} else {
						// method55(false);
						prevSong = 0;
					}
				} else {
					// method56(volume, false, currentSong);
					prevSong = 0;
				}

				musicVolume = volume;
			}
		}
		if (j == 4) {
			if (k == 0) {
				soundEffectVolume = 127;
			} else if (k == 1) {
				soundEffectVolume = 96;
			} else if (k == 2) {
				soundEffectVolume = 64;
			} else if (k == 3) {
				soundEffectVolume = 32;
			} else if (k == 4) {
				soundEffectVolume = 0;
			}
		}
		if (j == 5)
			anInt1253 = k;
		if (j == 6)
			chatEffects = k;
		if (j == 9)
			anInt913 = k;
	}

	public RSFontOSRS smallFontOSRS,regularFontOSRS,boldFontOSRS,fancyFontOSRS,fancyFontMediumOSRS,fancyFontLargeOSRS;

	public boolean splatFade = true;

	private void updateEntities(int viewportOffsetX, int viewportOffsetY) {

		for (int index = -1; index < playerCount + npcCount; index++) {
			Entity actor;

			if (index == -1) {
				actor = localPlayer;
			} else if (index < playerCount) {
				actor = players[playerIndices[index]];
			} else {
				actor = npcs[npcIndices[index - playerCount]];
			}

			if (actor == null) {
				continue;
			}

			if (actor instanceof Npc) {
				NpcDefinition entityDef = ((Npc) actor).desc;

				if (entityDef.transforms != null) {
					entityDef = entityDef.morph();
				}

				if (entityDef == null) {
					continue;
				}
			}

			int offsetY = -2;

			if (actor.spokenText != null && (index >= playerCount || publicChatMode == 4 || publicChatMode == 0 || publicChatMode == 3 || publicChatMode == 1 && isFriendOrSelf(((Player) actor).displayName))) {
				npcScreenPos(actor, actor.defaultHeight);
				if (viewportTempX > -1 && overheadTextCount < overheadTextLimit) {
					overheadTextXOffsets[overheadTextCount] = boldFontOSRS.stringWidth(actor.spokenText) / 2;
					overheadTextAscents[overheadTextCount] = boldFontOSRS.ascent;
					overheadTextXs[overheadTextCount] = viewportTempX;
					overheadTextYs[overheadTextCount] = viewportTempY - offsetY;
					overheadTextColors[overheadTextCount] = actor.textColour;
					overheadTextEffects[overheadTextCount] = actor.textEffect;
					overheadTextCyclesRemaining[overheadTextCount] = actor.overheadTextCyclesRemaining;
					overheadText[overheadTextCount] = actor.spokenText;
					++Client.overheadTextCount;
					if (chatEffects == 0 && actor.textEffect >= 1 && actor.textEffect <= 3) {
						overheadTextAscents[overheadTextCount] += 10;
						overheadTextYs[overheadTextCount] += 5;
					}

					if (chatEffects == 0 && actor.textEffect == 4) {
						overheadTextXOffsets[overheadTextCount] = 60;
					}

					if (chatEffects == 0 && actor.textEffect == 5) {
						overheadTextAscents[overheadTextCount] += 5;
					}
					offsetY += 12;
				}
			}


			int hitsplatType2;
			int classicWidth;
			int middleGraphicWidth;


			int verticalOffset = offsetY;
			if (!actor.healthBars.hasActiveHealthBars()) {
				npcScreenPos(actor, actor.defaultHeight + 15);

				for (HealthBar healthBar = (HealthBar) actor.healthBars.last(); healthBar != null; healthBar = (HealthBar) actor.healthBars.previous()) {
					HealthBarUpdate healthUpdate = healthBar.get(loopCycle);

					if (healthUpdate == null && healthBar.isEmpty()) {
						healthBar.remove();
					} else if (healthUpdate != null) {
						HealthBarDefinition definition = healthBar.definition;
						Sprite backSprite = definition.getBackSprite();
						Sprite frontSprite = definition.getFrontSprite();
						int padding = (backSprite != null && frontSprite != null && definition.widthPadding * 2 < frontSprite.myWidth) ? definition.widthPadding : 0;
						int barWidth = (frontSprite != null) ? frontSprite.myWidth - padding * 2 : definition.width;

						int elapsedTicks = loopCycle - healthUpdate.cycle;  // Here's where we define elapsedTicks.
						int opacity = healthBar.calculateOpacity(healthUpdate, definition, elapsedTicks);
						int renderedWidth = healthBar.calculateRenderedWidth(healthUpdate, definition, elapsedTicks, barWidth);

						verticalOffset = healthBar.drawHealthBar(backSprite, frontSprite, barWidth, padding, renderedWidth, opacity, verticalOffset);
					}
				}
			}
			offsetY = verticalOffset;

			if (offsetY == -2) {
				offsetY += 7;
			}

			if (actor instanceof Player) {
				Player player = (Player) actor;

				if (player.skullIcon != -1 || player.headIcon != -1) {
					npcScreenPos(actor, actor.defaultHeight + 15);
					if (viewportTempX > -1) {
						if (player.skullIcon != -1 && skullIcons.length >= player.skullIcon) {
							offsetY += 25;
							skullIcons[player.skullIcon].drawSprite(viewportOffsetX + viewportTempX - 12, viewportOffsetY + viewportTempY - offsetY);
						}

						if (player.headIcon != -1 && headIcons.length >= player.headIcon) {
							offsetY += 25;
							headIcons[player.headIcon].drawSprite(viewportOffsetX + viewportTempX - 12, viewportOffsetY + viewportTempY - offsetY);
						}
					}
				}

				if (player.isHintArrowPointingAtPlayer(index)) {
					npcScreenPos(actor, actor.defaultHeight + 15);
					if (viewportTempX > -1) {
						offsetY += headIconsHint[1].myHeight;
						headIconsHint[1].drawSprite(viewportOffsetX + viewportTempX - 12, viewportOffsetY + viewportTempY - offsetY);
					}
				}

			} else if (actor instanceof Npc) {
				Npc npc = (Npc) actor;

				// Draw NPC icons
				npc.drawIcons();

				if (npc.isHintArrowPointingAtNpc(index)) {
					npcScreenPos(actor, actor.defaultHeight + 15);
					if (viewportTempX > -1) {
						headIconsHint[0].drawSprite(viewportOffsetX + viewportTempX - 12, viewportOffsetY + viewportTempY - 28);
					}
				}

			}

			for (int hitsplatIndex = 0; hitsplatIndex < 4; ++hitsplatIndex) {
				int cycles = actor.hitSplatCycles[hitsplatIndex];
				int types = actor.hitSplatTypes[hitsplatIndex];
				HitSplatDefinition hitSplatDefinition = null;
				int animationDuration = 0;
				if (types >= 0) {
					if (cycles <= Client.loopCycle) {
						continue;
					}


					hitSplatDefinition = HitSplatDefinition.lookup(actor.hitSplatTypes[hitsplatIndex]);
					animationDuration = hitSplatDefinition.animationDuration;
					if (hitSplatDefinition != null && hitSplatDefinition.multimark != null) {
						hitSplatDefinition = hitSplatDefinition.transform();
						if (hitSplatDefinition == null) {
							actor.hitSplatCycles[hitsplatIndex] = -1;
							continue;
						}
						if (splatFade) {
							hitSplatDefinition.fadeat = 40;
							hitSplatDefinition.animationEndY = 10;
							hitSplatDefinition.animationDuration = 50;
							if (actor.hitSplatDamageType[hitsplatIndex] != -1) {
								hitSplatDefinition.classicGraphic = 2224 + (actor.hitSplatDamageType[hitsplatIndex]);
							}
						}
					}
				} else if (cycles < 0) {
					continue;
				}


				hitsplatType2 = actor.hitSplatTypes2[hitsplatIndex];
				HitSplatDefinition hitSplatDefinition2 = null;
				if (hitsplatType2 >= 0) {
					hitSplatDefinition2 = HitSplatDefinition.lookup(hitsplatType2);
					if (hitSplatDefinition2 != null && hitSplatDefinition2.multimark != null) {
						hitSplatDefinition2 = hitSplatDefinition2.transform();
					}
					if (splatFade) {
						hitSplatDefinition2.fadeat = 40;
						hitSplatDefinition2.animationEndY = 10;
						hitSplatDefinition2.animationDuration = 50;
						if (actor.hitSplatDamageType2[hitsplatIndex] != -1) {
							hitSplatDefinition2.classicGraphic = 2224 + actor.hitSplatDamageType2[hitsplatIndex];
						}
					}
				}

				if (cycles - animationDuration <= Client.loopCycle) {
					if (hitSplatDefinition == null) {
						actor.hitSplatCycles[hitsplatIndex] = -1;
					} else {
						npcScreenPos(actor, actor.defaultHeight / 2);
						if (viewportTempX > -1) {
							if (hitsplatIndex == 1) {
								viewportTempY -= 20;
							}

							if (hitsplatIndex == 2) {
								viewportTempX -= 15;
								viewportTempY -= 10;
							}

							if (hitsplatIndex == 3) {
								viewportTempX += 15;
								viewportTempY -= 10;
							}


							classicWidth = 0;
							middleGraphicWidth = 0;
							int leftGraphicWidth = 0;
							int rightGraphicWidth = 0;
							int classicOffsetX = 0;
							int middleOffsetX = 0;
							int leftOffsetX = 0;
							int rightOffsetX = 0;
							Sprite classicGraphic2 = null;
							Sprite middleGraphic2 = null;
							Sprite leftGraphic2 = null;
							Sprite rightGraphic2 = null;
							int classicGraphic2Width = 0;
							int middleGraphic2Width = 0;
							int leftGraphic2Width = 0;
							int rightGraphic2Width = 0;
							int classic2OffsetX = 0;
							int middle2OffsetX = 0;
							int left2OffsetX = 0;
							int right2OffsetX = 0;
							int realHeight = 0;
							Sprite classicGraphic = hitSplatDefinition.getClassicGraphic();
							int graphicHeight;
							if (classicGraphic != null) {
								classicWidth = classicGraphic.myWidth;
								graphicHeight = classicGraphic.myHeight;
								if (graphicHeight > realHeight) {
									realHeight = graphicHeight;
								}

								classicOffsetX = classicGraphic.drawOffsetX;
							}

							Sprite middleGraphic = hitSplatDefinition.getMiddleGraphic();
							if (middleGraphic != null) {
								middleGraphicWidth = middleGraphic.myWidth;
								graphicHeight = middleGraphic.myHeight;
								if (graphicHeight > realHeight) {
									realHeight = graphicHeight;
								}

								middleOffsetX = middleGraphic.drawOffsetX;
							}

							Sprite leftGraphic = hitSplatDefinition.getLeftGraphic();
							if (leftGraphic != null) {
								leftGraphicWidth = leftGraphic.myWidth;
								graphicHeight = leftGraphic.myHeight;
								if (graphicHeight > realHeight) {
									realHeight = graphicHeight;
								}

								leftOffsetX = leftGraphic.drawOffsetX;
							}

							Sprite rightGraphic = hitSplatDefinition.getRightGraphic();
							if (rightGraphic != null) {
								rightGraphicWidth = rightGraphic.myWidth;
								graphicHeight = rightGraphic.myHeight;
								if (graphicHeight > realHeight) {
									realHeight = graphicHeight;
								}

								rightOffsetX = rightGraphic.drawOffsetX;
							}

							if (hitSplatDefinition2 != null) {
								classicGraphic2 = hitSplatDefinition2.getClassicGraphic();
								if (classicGraphic2 != null) {
									classicGraphic2Width = classicGraphic2.myWidth;
									graphicHeight = classicGraphic2.myHeight;
									if (graphicHeight > realHeight) {
										realHeight = graphicHeight;
									}

									classic2OffsetX = classicGraphic2.drawOffsetX;
								}

								middleGraphic2 = hitSplatDefinition2.getMiddleGraphic();
								if (middleGraphic2 != null) {
									middleGraphic2Width = middleGraphic2.myWidth;
									graphicHeight = middleGraphic2.myHeight;
									if (graphicHeight > realHeight) {
										realHeight = graphicHeight;
									}

									middle2OffsetX = middleGraphic2.drawOffsetX;
								}

								leftGraphic2 = hitSplatDefinition2.getLeftGraphic();
								if (leftGraphic2 != null) {
									leftGraphic2Width = leftGraphic2.myWidth;
									graphicHeight = leftGraphic2.myHeight;
									if (graphicHeight > realHeight) {
										realHeight = graphicHeight;
									}

									left2OffsetX = leftGraphic2.drawOffsetX;
								}

								rightGraphic2 = hitSplatDefinition2.getRightGraphic();
								if (rightGraphic2 != null) {
									rightGraphic2Width = rightGraphic2.myWidth;
									graphicHeight = rightGraphic2.myHeight;
									if (graphicHeight > realHeight) {
										realHeight = graphicHeight;
									}

									right2OffsetX = rightGraphic2.drawOffsetX;
								}
							}

							RSFontOSRS hitSplatDefinitionFont = hitSplatDefinition.getFont();
							if (hitSplatDefinitionFont == null) {
								hitSplatDefinitionFont = regularFontOSRS;
							}

							RSFontOSRS hitSplatDefinitionFont2;
							if (hitSplatDefinition2 != null) {
								hitSplatDefinitionFont2 = hitSplatDefinition2.getFont();
								if (hitSplatDefinitionFont2 == null) {
									hitSplatDefinitionFont2 = regularFontOSRS;
								}
							} else {
								hitSplatDefinitionFont2 = regularFontOSRS;
							}

							String damageText = null;
							String damageText2 = null;

							int stringWidth2 = 0;
							damageText = hitSplatDefinition.formatDamage(actor.hitSplatValues[hitsplatIndex]);
							int stringWidth = hitSplatDefinitionFont.stringWidth(damageText);
							if (hitSplatDefinition2 != null) {
								damageText2 = hitSplatDefinition2.formatDamage(actor.hitSplatValues2[hitsplatIndex]);
								stringWidth2 = hitSplatDefinitionFont2.stringWidth(damageText2);
							}

							int var49 = 0;
							int var50 = 0;
							if (middleGraphicWidth > 0) {
								if (leftGraphic == null && rightGraphic == null) {
									var49 = 1;
								} else {
									var49 = stringWidth / middleGraphicWidth + 1;
								}
							}

							if (hitSplatDefinition2 != null && middleGraphic2Width > 0) {
								if (leftGraphic2 == null && rightGraphic2 == null) {
									var50 = 1;
								} else {
									var50 = stringWidth2 / middleGraphic2Width + 1;
								}
							}

							int var51 = 0;
							int var52 = var51;
							if (classicWidth > 0) {
								var51 += classicWidth;
							}

							var51 += 2;
							int var53 = var51;
							if (leftGraphicWidth > 0) {
								var51 += leftGraphicWidth;
							}

							int var54 = var51;
							int var55 = var51;
							int var56;
							if (middleGraphicWidth > 0) {
								var56 = middleGraphicWidth * var49;
								var51 += var56;
								var55 += (var56 - stringWidth) / 2;
							} else {
								var51 += stringWidth;
							}

							var56 = var51;
							if (rightGraphicWidth > 0) {
								var51 += rightGraphicWidth;
							}

							int var57 = 0;
							int var58 = 0;
							int var59 = 0;
							int var60 = 0;
							int var61 = 0;
							int var62;
							if (hitSplatDefinition2 != null) {
								var51 += 2;
								var57 = var51;
								if (classicGraphic2Width > 0) {
									var51 += classicGraphic2Width;
								}

								var51 += 2;
								var58 = var51;
								if (leftGraphic2Width > 0) {
									var51 += leftGraphic2Width;
								}

								var59 = var51;
								var61 = var51;
								if (middleGraphic2Width > 0) {
									var62 = var50 * middleGraphic2Width;
									var51 += var62;
									var61 += (var62 - stringWidth2) / 2;
								} else {
									var51 += stringWidth2;
								}

								var60 = var51;
								if (rightGraphic2Width > 0) {
									var51 += rightGraphic2Width;
								}
							}

							var62 = actor.hitSplatCycles[hitsplatIndex] - Client.loopCycle;
							int var63 = hitSplatDefinition.animationEndX - var62 * hitSplatDefinition.animationEndX / hitSplatDefinition.animationDuration;
							int var64 = var62 * hitSplatDefinition.animationEndY / hitSplatDefinition.animationDuration + -hitSplatDefinition.animationEndY;
							int var65 = var63 + (viewportOffsetX + viewportTempX - (var51 >> 1));
							int var66 = viewportOffsetY + viewportTempY - 12 + var64;
							int var67 = var66;
							int var68 = realHeight + var66;
							int var69 = var66 + hitSplatDefinition.damageYOfset + 15;
							int var70 = var69 - hitSplatDefinitionFont.maxAscent;
							int var71 = var69 + hitSplatDefinitionFont.maxDescent;
							if (var70 < var66) {
								var67 = var70;
							}

							if (var71 > var68) {
								var68 = var71;
							}

							int var72 = 0;
							int var73;
							int var74;
							if (hitSplatDefinition2 != null) {
								var72 = var66 + hitSplatDefinition2.damageYOfset + 15;
								var73 = var72 - hitSplatDefinitionFont2.maxAscent;
								var74 = var72 + hitSplatDefinitionFont2.maxDescent;
								if (var73 < var67) {
									;
								}

								if (var74 > var68) {
									;
								}
							}

							var73 = 255;
							if (hitSplatDefinition.fadeat >= 0) {
								var73 = (var62 << 8) / (hitSplatDefinition.animationDuration - hitSplatDefinition.fadeat);
							}

							if (var73 >= 0 && var73 < 255) {
								if (classicGraphic != null) {
									classicGraphic.drawAdvancedSprite(var65 + var52 - classicOffsetX, var66, var73);
								}

								if (leftGraphic != null) {
									leftGraphic.drawAdvancedSprite(var65 + var53 - leftOffsetX, var66, var73);
								}

								if (middleGraphic != null) {
									for (var74 = 0; var74 < var49; ++var74) {
										middleGraphic.drawAdvancedSprite(var74 * middleGraphicWidth + (var54 + var65 - middleOffsetX), var66, var73);
									}
								}

								if (rightGraphic != null) {
									rightGraphic.drawAdvancedSprite(var65 + var56 - rightOffsetX, var66, var73);
								}

								hitSplatDefinitionFont.drawAlpha(damageText, var65 + var55, var69, hitSplatDefinition.textColor, 0, var73);
								if (hitSplatDefinition2 != null) {
									if (classicGraphic2 != null) {
										classicGraphic2.drawAdvancedSprite(var57 + var65 - classic2OffsetX, var66, var73);
									}

									if (leftGraphic2 != null) {
										leftGraphic2.drawAdvancedSprite(var58 + var65 - left2OffsetX, var66, var73);
									}

									if (middleGraphic2 != null) {
										for (var74 = 0; var74 < var50; ++var74) {
											middleGraphic2.drawAdvancedSprite(var74 * middleGraphic2Width + (var59 + var65 - middle2OffsetX), var66, var73);
										}
									}

									if (rightGraphic2 != null) {
										rightGraphic2.drawAdvancedSprite(var65 + var60 - right2OffsetX, var66, var73);
									}

									hitSplatDefinitionFont2.drawAlpha(damageText2, var61 + var65, var72, hitSplatDefinition2.textColor, 0, var73);
								}
							} else {
								if (classicGraphic != null) {
									classicGraphic.drawSprite(var65 + var52 - classicOffsetX, var66);
								}

								if (leftGraphic != null) {
									leftGraphic.drawSprite(var65 + var53 - leftOffsetX, var66);
								}

								if (middleGraphic != null) {
									for (var74 = 0; var74 < var49; ++var74) {
										middleGraphic.drawSprite(middleGraphicWidth * var74 + (var65 + var54 - middleOffsetX), var66);
									}
								}

								if (rightGraphic != null) {
									rightGraphic.drawSprite(var56 + var65 - rightOffsetX, var66);
								}

								hitSplatDefinitionFont.draw(damageText, var65 + var55, var69, hitSplatDefinition.textColor | -16777216, 0);
								if (hitSplatDefinition2 != null) {
									if (classicGraphic2 != null) {
										classicGraphic2.drawSprite(var57 + var65 - classic2OffsetX, var66);
									}

									if (leftGraphic2 != null) {
										leftGraphic2.drawSprite(var58 + var65 - left2OffsetX, var66);
									}

									if (middleGraphic2 != null) {
										for (var74 = 0; var74 < var50; ++var74) {
											middleGraphic2.drawSprite(var74 * middleGraphic2Width + (var65 + var59 - middle2OffsetX), var66);
										}
									}

									if (rightGraphic2 != null) {
										rightGraphic2.drawSprite(var65 + var60 - right2OffsetX, var66);
									}

									hitSplatDefinitionFont2.draw(damageText2, var65 + var61, var72, hitSplatDefinition2.textColor | -16777216, 0);
								}
							}
						}
					}
				}
			}

			for (int textIndex = 0; textIndex < overheadTextCount; ++textIndex) {
				int textX = overheadTextXs[textIndex];
				int textY = overheadTextYs[textIndex];
				int textOffsetX = overheadTextXOffsets[textIndex];
				int textOffsetY = overheadTextAscents[textIndex];
				boolean needsPositionAdjustment = true;

				while (needsPositionAdjustment) {
					needsPositionAdjustment = false;

					for (int textCheckCount = 0; textCheckCount < textIndex; ++textCheckCount) {
						if (textY + 2 > overheadTextYs[textCheckCount] - overheadTextAscents[textCheckCount] && textY - textOffsetY < overheadTextYs[textCheckCount] + 2 && textX - textOffsetX < overheadTextXOffsets[textCheckCount] + overheadTextXs[textCheckCount] && textX + textOffsetX > overheadTextXs[textCheckCount] - overheadTextXOffsets[textCheckCount] && overheadTextYs[textCheckCount] - overheadTextAscents[textCheckCount] < textY) {
							textY = overheadTextYs[textCheckCount] - overheadTextAscents[textCheckCount];
							needsPositionAdjustment = true;
						}
					}
				}

				viewportTempX = overheadTextXs[textIndex];
				viewportTempY = overheadTextYs[textIndex] = textY;
				String currentText = overheadText[textIndex];
				int textColor = 16776960;
				if (chatEffects == 0) {
					if (overheadTextColors[textIndex] < 6) {
						textColor = field758[overheadTextColors[textIndex]];
					}

					if (overheadTextColors[textIndex] == 6) {
						textColor = viewportDrawCount % 20 < 10 ? 16711680 : 16776960;
					}

					if (overheadTextColors[textIndex] == 7) {
						textColor = viewportDrawCount % 20 < 10 ? 255 : '\uffff';
					}

					if (overheadTextColors[textIndex] == 8) {
						textColor = viewportDrawCount % 20 < 10 ? '뀀' : 8454016;
					}

					int remainingCycle = 150 - overheadTextCyclesRemaining[textIndex];
					int colorIndex = overheadTextColors[textIndex];

					if (colorIndex == 9) {
						if (remainingCycle < 50) {
							textColor = remainingCycle * 1280 + 16711680;
						} else if (remainingCycle < 100) {
							textColor = 16776960 - (remainingCycle - 50) * 327680;
						} else {
							textColor = (remainingCycle - 100) * 5 + 65280;
						}
					} else if (colorIndex == 10) {
						if (remainingCycle < 50) {
							textColor = remainingCycle * 5 + 16711680;
						} else if (remainingCycle < 100) {
							textColor = 16711935 - (remainingCycle - 50) * 327680;
						} else {
							textColor = (remainingCycle - 100) * 327680 + 255 - (remainingCycle - 100) * 5;
						}
					} else if (colorIndex == 11) {
						if (remainingCycle < 50) {
							textColor = 16777215 - remainingCycle * 327685;
						} else if (remainingCycle < 100) {
							textColor = (remainingCycle - 50) * 327685 + 65280;
						} else {
							textColor = 16777215 - (remainingCycle - 100) * 327680;
						}
					}

					if (overheadTextColors[textIndex] == 12 && chatEffectGradientColors[textIndex] == null) {
						int textLength = currentText.length();
						chatEffectGradientColors[textIndex] = new int[textLength];

						for (int charIndex = 0; charIndex < textLength; charIndex++) {
							int colorRatio = (int) (64.0F * (float) charIndex / textLength);
							int colorIndexX = colorRatio << 10 | 896 | 64;
							chatEffectGradientColors[textIndex][charIndex] = ColorUtils.colorLookupTable[colorIndexX];
						}
					}

					if (overheadTextEffects[textIndex] == 0) {

						int spriteDrawX = this.viewportTempX;
						String editableForcedChat = currentText;
						if (currentText.startsWith("<sprite")) {
							try {
								int endIndex = currentText.indexOf('>');

								if (endIndex != -1) {
									int imageId = Integer.valueOf(currentText.substring(8, endIndex));
									editableForcedChat = editableForcedChat.substring(endIndex + 1);
									int textWidth = boldFontOSRS.getTextWidth(editableForcedChat) / 2;
									Sprite icon = ImageCache.get(imageId);
									int iconModY = icon.maxHeight - 1;
									icon.drawSprite(this.viewportTempX - textWidth - 1, (viewportTempY - iconModY));
									spriteDrawX += 11;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						boldFontOSRS.drawText(0, editableForcedChat, viewportTempY + 1 + viewportOffsetY, viewportOffsetX + spriteDrawX);
						boldFontOSRS.drawText(textColor, editableForcedChat, viewportTempY + viewportOffsetY, viewportOffsetX + spriteDrawX);
					}

					if (overheadTextEffects[textIndex] == 1) {
						boldFontOSRS.drawCenteredWave(currentText, viewportOffsetX + viewportTempX, viewportTempY + viewportOffsetY, textColor, 0, viewportDrawCount);
					}

					if (overheadTextEffects[textIndex] == 2) {
						boldFontOSRS.drawCenteredWaveWithShaking(currentText, viewportOffsetX + viewportTempX, viewportTempY + viewportOffsetY, textColor, 0, viewportDrawCount);
					}

					if (overheadTextEffects[textIndex] == 3) {
						boldFontOSRS.drawCenteredShakeWithVariance(currentText, viewportOffsetX + viewportTempX, viewportTempY + viewportOffsetY, textColor, 0, viewportDrawCount, 150 - overheadTextCyclesRemaining[textIndex]);
					}

					if (overheadTextEffects[textIndex] == 4) {
						remainingCycle = (150 - overheadTextCyclesRemaining[textIndex]) * (boldFontOSRS.stringWidth(currentText) + 100) / 150;
						Rasterizer2D.expandClip(viewportOffsetX + viewportTempX - 50, viewportOffsetY, viewportOffsetX + viewportTempX + 50, viewportOffsetY + viewportOffsetY);
						boldFontOSRS.drawBasicString(currentText, viewportOffsetX + viewportTempX + 50 - remainingCycle, viewportTempY + viewportOffsetY, textColor, 0);
						Rasterizer2D.setClip(viewportOffsetX, viewportOffsetY, viewportOffsetX + viewportOffsetX, viewportOffsetY + viewportOffsetY);
					}

					if (overheadTextEffects[textIndex] == 5) {
						int yOffset = 0;
						remainingCycle = 150 - overheadTextCyclesRemaining[textIndex];
						yOffset = 0;
						if (remainingCycle < 25) {
							yOffset = remainingCycle - 25;
						} else if (remainingCycle > 125) {
							yOffset = remainingCycle - 125;
						}

						Rasterizer2D.expandClip(viewportOffsetX, viewportTempY + viewportOffsetY - boldFontOSRS.ascent - 1, viewportOffsetX + viewportOffsetX, viewportTempY + viewportOffsetY + 5);

						boldFontOSRS.drawText(0, currentText, yOffset + viewportTempX + 1 + viewportOffsetY, viewportOffsetX + viewportTempX);
						boldFontOSRS.drawText(textColor, currentText, yOffset + viewportTempY + viewportOffsetY, viewportOffsetX + viewportTempX);

						Rasterizer2D.setClip(viewportOffsetX, viewportOffsetY, viewportOffsetX + viewportOffsetX, viewportOffsetY + viewportOffsetY);
					}
				} else {
					boldFontOSRS.drawCentered(currentText, viewportOffsetX + viewportTempX, viewportTempY + viewportOffsetY, 16776960, 0);
				}
			}
		}
	}


	private void delFriend(long l) {
		try {
			if (l == 0L)
				return;
			for (int i = 0; i < friendsCount; i++) {
				if (friendsListAsLongs[i] != l)
					continue;
				friendsCount--;
				needDrawTabArea = true;
				for (int j = i; j < friendsCount; j++) {
					friendsList[j] = friendsList[j + 1];
					friendsNodeIDs[j] = friendsNodeIDs[j + 1];
					friendsListAsLongs[j] = friendsListAsLongs[j + 1];
				}

				stream.createFrame(215);
				stream.writeQWord(l);
				break;
			}
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("18622, " + false + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	Sprite tabAreaFixed;
	Sprite[] tabAreaResizable = new Sprite[3];
	private boolean drawingTabArea = false;

	public void drawTabArea() {
		drawingTabArea = true;
		boolean fixedMode = !isResized();
		final int xOffset = !isResized() ? 516 : 0;
		final int yOffset = !isResized() ? 168 : 0;

		if (fixedMode) {
			tabAreaFixed.drawSprite(xOffset,yOffset);
			if (invOverlayInterfaceID == 0)
				drawTabs(xOffset,yOffset);

		} else {
			(stackTabs() ? tabAreaResizable[1] : tabAreaResizable[2]).drawSprite(
					Client.canvasWidth - (stackTabs() ? 231 : 462),
					Client.canvasHeight - (stackTabs() ? 73 : 37) );

			tabAreaResizable[0].drawSprite((Client.canvasWidth - 204),
					Client.canvasHeight - 275 - (stackTabs() ? 73 : 37));

			if (invOverlayInterfaceID == 0)
				drawTabs(xOffset,yOffset);
		}
		int y = stackTabs() ? 73 : 37;

		// Set open tab to one that has an interface
		if (tabInterfaceIDs[tabID] <= 0) {
			for (int i = tabID; i >= 0; i--) {
				if (tabInterfaceIDs[i] >= 1) {
					tabID = i;
					break;
				}
			}

			if (tabInterfaceIDs[tabID] <= 0)
				tabID = 3;
		}

		if (invOverlayInterfaceID != 0) {
			drawInterface(0, (fixedMode ? 31 + xOffset : canvasWidth - 197),
					RSInterface.interfaceCache[invOverlayInterfaceID],
					(fixedMode ? 37 + yOffset : canvasHeight - 275 - y + 10));
		} else if (Client.tabInterfaceIDs[Client.tabID] != -1) {
			try {
				drawInterface(0, (fixedMode ? 31 + xOffset : canvasWidth - 197),
						RSInterface.interfaceCache[Client.tabInterfaceIDs[Client.tabID]],
						(fixedMode ? 37 + yOffset : canvasHeight - 275 - y + 10));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (drawTabInterfaceHover != 0) {
			RSInterface parent = RSInterface.interfaceCache[drawTabInterfaceHoverParent];
			if (drawTabInterfaceHoverTimer == 0 || drawTabInterfaceHoverLast != drawTabInterfaceHover)
				drawTabInterfaceHoverTimer = System.currentTimeMillis();
			drawTabInterfaceHoverLast = drawTabInterfaceHover;
			if (System.currentTimeMillis() - drawTabInterfaceHoverTimer >= parent.hoverInterfaceDelay) {
				RSInterface hover = RSInterface.interfaceCache[drawTabInterfaceHover];
				int xOffset1 = (fixedMode ? 516 + xOffset : 0);
				int yOffset1 = (fixedMode ? 168 + yOffset : 0);
				int drawingAreaWidth = canvasWidth - xOffset1;
				int drawingAreaHeight = canvasHeight - yOffset1;
				int mouseX = MouseHandler.mouseX - xOffset1;
				int mouseY = MouseHandler.mouseY - yOffset1;
				int interfaceWidth = hover.width + 12; // Was drawing offscreen a bit, don't have time to proper fix

				if (mouseX + interfaceWidth > drawingAreaWidth) {
					mouseX = drawingAreaWidth - interfaceWidth;
				}

				if (mouseY + hover.height > drawingAreaHeight) {
					mouseY = drawingAreaHeight - hover.height;
				}


				drawInterface(0, mouseX, hover, mouseY, true);
			}
		} else {
			drawTabInterfaceHoverTimer = 0;
		}



		drawingTabArea = false;
	}

	Sprite[] sideIcons = new Sprite[15];
	Sprite[] redStones = new Sprite[6];



	private void drawTabs(int xOffset,int yOffset) {
		if (!isResized()) {
			final int[][] sideIconCoordinates = new int[][] { { 17, 17 }, { 49, 15 }, { 83, 15 }, { 113, 13 },
					{ 146, 10 }, { 180, 11 }, { 214, 15 }, { 14, 311 }, { 49, 314 }, { 82, 314 }, { 116, 310 },
					{ 148, 312 }, { 184, 311 }, { 216, 311 }, { 216, 311 } };
			final int[][] sideIconCoordinates1 = new int[][] { { 24, 8 }, { 49, 5}, { 79, 5 }, { 108, 3}, { 147, 5 },
					{ 176, 5 }, { 205, 8 }, { 22, 300 }, { 49, 304 }, { 77, 304 }, { 111, 303 }, { 147, 301 },
					{ 180, 303 }, { 204, 303 }, { 204, 303 } };
			if (Client.tabInterfaceIDs[Client.tabID] != -1) {
				if(getUserSettings().isOldGameframe() == false) {
					if (Client.tabID == 0)
						redStones[0].drawSprite(5 + xOffset, 0 + yOffset);
					if (Client.tabID == 1)
						redStones[4].drawSprite(43 + xOffset, 0 + yOffset);
					if (Client.tabID == 2)
						redStones[4].drawSprite(76 + xOffset, 0 + yOffset);
					if (Client.tabID == 3)
						redStones[4].drawSprite(109 + xOffset, 0 + yOffset);
					if (Client.tabID == 4)
						redStones[4].drawSprite(142 + xOffset, 0 + yOffset);
					if (Client.tabID == 5)
						redStones[4].drawSprite(175 + xOffset, 0 + yOffset);
					if (Client.tabID == 6)
						redStones[1].drawSprite(208 + xOffset, 0 + yOffset);
					if (Client.tabID == 7)
						redStones[2].drawSprite(5 + xOffset, 298 + yOffset);
					if (Client.tabID == 8)
						redStones[4].drawSprite(43 + xOffset, 298 + yOffset);
					if (Client.tabID == 9)
						redStones[4].drawSprite(76 + xOffset, 298 + yOffset);
					if (Client.tabID == 10)
						redStones[4].drawSprite(109 + xOffset, 298 + yOffset);
					if (Client.tabID == 11)
						redStones[4].drawSprite(142 + xOffset, 298 + yOffset);
					if (Client.tabID == 12)
						redStones[4].drawSprite(175 + xOffset, 298 + yOffset);
					if (Client.tabID == 13)
						redStones[3].drawSprite(208 + xOffset, 298 + yOffset);
				}else {
					if (Client.tabID == 0)
						redStones[1].drawSprite(14 + xOffset, 0 + yOffset);
					if (Client.tabID == 1)
						redStones[2].drawSprite(47 + xOffset, 0 + yOffset);
					if (Client.tabID == 2)
						redStones[2].drawSprite(74 + xOffset, 0 + yOffset);
					if (Client.tabID == 3)
						redStones[3].drawSprite(102 + xOffset, 0 + yOffset);
					if (Client.tabID == 4)
						redStones[2].drawSprite(144 + xOffset, 0 + yOffset);
					if (Client.tabID == 5)
						redStones[2].drawSprite(172 + xOffset, 0 + yOffset);
					if (Client.tabID == 6)
						redStones[0].drawSprite(201 + xOffset, 0 + yOffset);
					if (Client.tabID == 7)
						redStones[4].drawSprite(13 + xOffset, 296 + yOffset);
					if (Client.tabID == 8)
						redStones[2].drawSprite(46 + xOffset, 297 + yOffset);
					if (Client.tabID == 9)
						redStones[2].drawSprite(74 + xOffset, 298 + yOffset);
					if (Client.tabID == 10)
						redStones[3].drawSprite(102 + xOffset, 297 + yOffset);
					if (Client.tabID == 11)
						redStones[2].drawSprite(144 + xOffset, 296 + yOffset);
					if (Client.tabID == 12)
						redStones[2].drawSprite(171 + xOffset, 296 + yOffset);
					if (Client.tabID == 13)
						redStones[5].drawSprite(201 + xOffset, 298 + yOffset);
				}

			}
			for (int index = 0; index <= 14; index++) {
				if (Client.tabInterfaceIDs[index] > 0) {
					if (!getUserSettings().isOldGameframe()) {
						if (index != 13 || (index == 13 && pollActive)) {
							sideIcons[index].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
						} else {
							sideIcons[index + 1].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
						}
					} else {
						sideIcons[index].drawSprite(sideIconCoordinates1[index][0] + xOffset, sideIconCoordinates1[index][1] + yOffset);
					}
				}
			}
		} else {
			final int[][] sideIconOffsets = new int[][] { { 7, 8 }, { 4, 6 }, { 6, 7 }, { 3, 4 }, { 3, 2 }, { 4, 3 },
					{ 4, 6 }, { 5, 5 }, { 5, 6 }, { 5, 6 }, { 6, 3 }, { 5, 5 }, { 6, 4 }, { 5, 5 } };
			int x = Client.canvasWidth - (stackTabs() ? 231 : 462) + xOffset;
			int y = Client.canvasHeight - (stackTabs() ? 73 : 37) + yOffset;
			for (int tabIndex = 0; tabIndex < 14; tabIndex++) {
				if (Client.tabID == tabIndex) {
					redStones[4].drawSprite(x, y);
				}
				if (stackTabs()) {
					if (tabIndex != 6) {
						x += 33;
					} else if (tabIndex == 6) {
						y += 36;
						x = Client.canvasWidth - 231;
					}
				} else {
					x += 33;
				}
			}
			x = Client.canvasWidth - (stackTabs() ? 231 : 462) + xOffset;
			y = Client.canvasHeight - (stackTabs() ? 73 : 37) + yOffset;
			for (int index = 0; index < 14; index++) {
				if (Client.tabInterfaceIDs[index] != -1) {
					sideIcons[index].drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
				}
				if (stackTabs()) {
					if (index != 6) {
						x += 33;
					} else if (index == 6) {
						y += 36;
						x = Client.canvasWidth - 231;
					}
				} else {
					x += 33;
				}
			}
		}

		if (openInterfaceID == 25650) {
			if (!isResized()) {
				grandExchangeSprite4.drawSprite1(29 + xOffset, 37 + yOffset, 162 + (int) (50 * Math.sin(loopCycle / 15.0)));
			} else {
				grandExchangeSprite4.drawSprite1((canvasWidth - 197) + xOffset,
						(stackTabs() ? canvasHeight - 341 : canvasHeight - 305) + yOffset,
						162 + (int) (50 * Math.sin(loopCycle / 15.0)));
			}
		}
	}


	public void processMobChatText() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = maxPlayerCount;
			else
				j = playerIndices[i];
			Player player = players[j];
			if (player != null && player.overheadTextCyclesRemaining > 0) {
				player.overheadTextCyclesRemaining--;
				if (player.overheadTextCyclesRemaining == 0)
					player.spokenText = null;
			}
		}
		for (int k = 0; k < npcCount; k++) {
			int l = npcIndices[k];
			Npc npc = npcs[l];
			if (npc != null && npc.overheadTextCyclesRemaining > 0) {
				npc.overheadTextCyclesRemaining--;
				if (npc.overheadTextCyclesRemaining == 0)
					npc.spokenText = null;
			}
		}
	}


	private void addFriend(long l) {
		try {
			if (l == 0L)
				return;

			String username = StringUtils.fixName(StringUtils.nameForLong(l));

			if (username.equalsIgnoreCase(localPlayer.displayName)) {
				System.err.println("blocked from adding self as friend..");
				return;
			}
			if (friendsCount >= 100 && anInt1046 != 1) {
				pushMessage("Your friendlist is full.", 0, "");
				return;
			}
			if (friendsCount >= 200) {
				pushMessage("Your friendlist is full.", 0, "");
				return;
			}

			for (int i = 0; i < friendsCount; i++)
				if (friendsListAsLongs[i] == l) {

					return;
				}
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					pushMessage("Please remove " + username + " from your ignore list first", 0, "");
					return;
				}

			if (username.equals(localPlayer.displayName)) {
				return;
			} else {
				//friendsList[friendsCount] = s;
				//friendsListAsLongs[friendsCount] = l;
				//friendsNodeIDs[friendsCount] = 0;
				//friendsCount++;
				//needDrawTabArea = true;
				stream.createFrame(188);
				stream.writeQWord(l);
				return;
			}
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("15283, " + (byte) 68 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public int getCenterHeight(int z, int y, int x) {
		int worldX = x >> 7;
		int worldY = y >> 7;
		if (worldX < 0 || worldY < 0 || worldX > 103 || worldY > 103)
			return 0;
		int plane = z;
		if (plane < 3 && (tileFlags[1][worldX][worldY] & 2) == 2)
			plane++;
		int sizeX = x & 0x7f;
		int sizeY = y & 0x7f;
		int i2 = tileHeights[plane][worldX][worldY] * (128 - sizeX)
				+ tileHeights[plane][worldX + 1][worldY] * sizeX >> 7;
		int j2 = tileHeights[plane][worldX][worldY + 1] * (128 - sizeX)
				+ tileHeights[plane][worldX + 1][worldY + 1] * sizeX >> 7;
		return i2 * (128 - sizeY) + j2 * sizeY >> 7;
	}

	public static int[] myHeadAndJaw = new int[2];

	public void resetLogout() {
		if (settingManager.getRememberMe()) {
			AccountData.Companion.save();
		}

		setGameState(GameState.LOGIN_SCREEN);

		logger.debug("being logged out.. from: "+new Throwable().getStackTrace()[1].toString());
		logger.debug("Logging out..");
		firstLoginMessage = "Enter Username & Password";
		loggedIn = false;
		prayClicked = false;
		scene.reset_interactive_obj();
		Rasterizer2D.Rasterizer2D_clear();
		StaticSound.logout();
		captcha = null;
		captchaInput = "";
		frameMode(false);

		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		socketStream = null;
		PacketLog.clear();
		if (entityTarget != null) {
			entityTarget.stop();
		}
		// myUsername = "";
		// myPassword = "";
		unlinkMRUNodes();
		scene.clear();
		for (int i = 0; i < 4; i++)
			collisionMaps[i].init();
		System.gc();
		currentSong = -1;
		BroadcastManager.broadcasts = new Broadcast[1000];
		nextSong = -1;
		prevSong = 0;
		experienceCounter = 0;
		GameTimerHandler.getSingleton().stopAll();
		Preferences.save();

	}

	public void method45() {
		aBoolean1031 = true;
		for (int j = 0; j < 7; j++) {
			myAppearance[j] = -1;
			for (int k = 0; k < Js5List.getConfigSize(Js5ConfigType.IDENTKIT); k++) {
				if (IdentityKit.lookup(k).validStyle || IdentityKit.lookup(k).bodyPartId != j + (aBoolean1047 ? 0 : 7))
					continue;
				myAppearance[j] = k;
				break;
			}
		}
	}

	private void method46(int i, Buffer stream) {
		while (stream.bitPosition + 21 < i * 8) {
			int k = stream.readBits(14);
			if (k == 16383)
				break;
			if (npcs[k] == null)
				npcs[k] = new Npc(); // Teleports works fine if we create a new npc?
			Npc npc = npcs[k];
			npcIndices[npcCount++] = k;
			callbacks.postDeferred(new NpcSpawned(npc));
			npc.lastUpdateTick = loopCycle;
			int l = stream.readBits(5);
			if (l > 15)
				l -= 32;
			int i1 = stream.readBits(5);
			if (i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(1);
			int npcId = stream.readBits(14);
			npc.npcPetType = stream.readBits(2);
			npc.desc = NpcDefinition.get(npcId);
			int k1 = stream.readBits(1);
			if (k1 == 1)
				anIntArray894[anInt893++] = k;
			npc.size = npc.desc.size;
			npc.rotation = npc.desc.rotationSpeed;
			npc.walkSequence = npc.desc.walkingAnimation;
			npc.walkBackSequence = npc.desc.rotate180Animation;
			npc.walkLeftSequence = npc.desc.rotate90LeftAnimation;
			npc.walkRightSequence = npc.desc.rotate90RightAnimation;
			npc.idleSequence = npc.desc.standingAnimation;
			npc.anInt1538 = 0;
			npc.anInt1539 = 0;
			npc.setPos(localPlayer.pathX[0] + i1, localPlayer.pathY[0] + l, j1 == 1);
		}
		stream.finishBitAccess();
	}

	public int currentPercent = 0;
	public int targetPercent = 0;

	public void updateLoading() {
		if(loginScreen.getLoginState() != LoginState.LOADING) return;
		// Define the interpolation speed
		final int interpolationSpeed = 1;

		// Calculate the difference and decide the adjustment needed
		int difference = targetPercent - currentPercent;
		int adjustment;
		if (difference > 0) {
			adjustment = Math.min(interpolationSpeed, difference);
		} else if (difference < 0) {
			adjustment = Math.max(-interpolationSpeed, difference);
		} else {
			adjustment = 0;
		}

		// Update the current percentage
		currentPercent += adjustment;
	}

	protected static final void frameDrawHistory() {
		clock.mark();

		int var0;
		for(var0 = 0; var0 < 32; ++var0) {
			GameEngine.graphicsTickTimes[var0] = 0L;
		}

		for(var0 = 0; var0 < 32; ++var0) {
			GameEngine.clientTickTimes[var0] = 0L;
		}

		gameCyclesToDo = 0;
	}

	@Override
	public void doCycle() {
		loopCycle++;
		getCallbacks().tick();
		getCallbacks().post(ClientTick.INSTANCE);
		updateLoading();
		Js5System.doCycleJs5();
		ArchiveDiskActionHandler.processArchiveDiskActions();
		StaticSound.pulse();
		keyManager.prepareForNextCycle();
		keyHandler.processKeyEvents();
		synchronized(MouseHandler.instance) {
			MouseHandler.MouseHandler_currentButton = MouseHandler.currentButton;
			MouseHandler.mouseX = MouseHandler.MouseHandler_xVolatile;
			MouseHandler.mouseY = MouseHandler.MouseHandler_yVolatile;
			MouseHandler.MouseHandler_millis = MouseHandler.MouseHandler_lastMovedVolatile;
			MouseHandler.clickMode3 = MouseHandler.MouseHandler_lastButtonVolatile;
			MouseHandler.saveClickX = MouseHandler.MouseHandler_lastPressedXVolatile;
			MouseHandler.saveClickY = MouseHandler.MouseHandler_lastPressedYVolatile;
			MouseHandler.lastPressed = MouseHandler.MouseHandler_lastPressedTimeMillisVolatile;
			MouseHandler.MouseHandler_lastButtonVolatile = 0;
		}

		if (mouseWheel != null) {
			int var4 = mouseWheel.useRotation();
			mouseWheelRotation = var4;
		}

		if (gameState == 0) {
			load();
			frameDrawHistory();
		} else if (gameState == 5) {
			load();
			frameDrawHistory();
		} else if (gameState != 10 && gameState != 11) {
			if (gameState == 20) {
				processLoginScreenInput();
			} else if (gameState == 50) {
				processLoginScreenInput();
			} else if (gameState == 25) {
				loadRegion();
			} else if (gameState != 0) {
                if (clearLoginScreen) {
                    jagexNetThread.writePacket(true);
                }
            }
		} else {
			processLoginScreenInput();
		}

		if (gameState == 30) {
			mainGameProcessor();
		} else if (gameState == 40 || gameState == 45) {
			processLoginScreenInput();
		}

		getCallbacks().tickEnd();

	}

	public static void setZoom(int zoomValue) {
		int value = Ints.constrainToRange(zoomValue, 166, 900);
		Client.field625 = value;
		if (Client.field625 <= 0) {
			Client.field625 = 256;
		}

		Client.field626 = value - 51;
		if (Client.field626 <= 0) {
			Client.field626 = 205;
		}
	}

	private void showPrioritizedPlayers() {
		showPlayer(localPlayer, maxPlayerCount, true);

		//Draw the player we're interacting with
		//Interacting includes combat, following, etc.
		int interact = localPlayer.interactingEntity - 32768;
		if (interact > 0) {
			Player player = players[interact];
			showPlayer(player, interact, false);
		}
	}

	private void showOtherPlayers() {
		for (int l = 0; l < playerCount; l++) {
			Player player = players[playerIndices[l]];
			int index = playerIndices[l];

			//Don't draw interacting player as we've already drawn it on top
			int interact_index = (localPlayer.interactingEntity - 32768);
			if (interact_index > 0 && index == interact_index) {
				continue;
			}

			if (!showPlayer(player, index, false)) {
			}
		}
	}

	private boolean showPlayer(Player player, int i1, boolean flag) {
		if (player == null || !player.isVisible()) {
			return false;
		}

		player.isUnanimated = (low_detail && playerCount > 50 || playerCount > 200) && !flag && player.movementSequence == player.idleSequence;
		int j1 = player.x >> 7;
		int k1 = player.y >> 7;
		if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104) {
			return false;
		}
		long var4 = ObjectKeyUtil.calculateTag(0, 0, 0, false, i1);
		if (player.playerModel != null && loopCycle >= player.animationCycleStart && loopCycle < player.animationCycleEnd) {
			player.isUnanimated = false;
			player.lastUpdateTick = loopCycle;
			player.tileHeight = getCenterHeight(plane, player.y, player.x);
			scene.add_transformed_entity(plane, player.y, player, player.orientation, player.maxY, player.x, player.tileHeight, player.minX, player.maxX, i1, player.minY);
			return false;
		}
		if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
			if (tileCycleMap[j1][k1] == viewportDrawCount) {
				return false;
			}
			tileCycleMap[j1][k1] = viewportDrawCount;
		}
		player.lastUpdateTick = loopCycle;
		player.tileHeight = getCenterHeight(plane, player.y, player.x);
		scene.add_entity(plane, player.orientation, player.tileHeight, var4, player.y, 60, player.x, player, player.isWalking);
		return true;
	}

	private boolean promptUserForInput(RSInterface class9) {
		int j = class9.contentType;
		if (anInt900 == 2) {
			if (j == 201) {
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 1;
				aString1121 = "Enter name of friend to add to list";
			}
			if (j == 202) {
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 2;
				aString1121 = "Enter name of friend to delete from list";
			}
		}
		if (j == 205) {
			anInt1011 = 250;
			return true;
		}
		if (j == 501) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 4;
			aString1121 = "Enter name of player to add to list";
		}
		if (j == 502) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 5;
			aString1121 = "Enter name of player to delete from list";
		}
		if (j == 550) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 6;
			aString1121 = "Enter the name of the chat you wish to join";
		}
		if (j >= 300 && j <= 313) {
			int k = (j - 300) / 2;
			int j1 = j & 1;
			int i2 = myAppearance[k];
			if (i2 != -1) {
				do {
					if (j1 == 0 && --i2 < 0)
						i2 = Js5List.getConfigSize(Js5ConfigType.IDENTKIT) - 1;
					if (j1 == 1 && ++i2 >= Js5List.getConfigSize(Js5ConfigType.IDENTKIT))
						i2 = 0;
				} while (IdentityKit.lookup(i2).validStyle || IdentityKit.lookup(i2).bodyPartId != k + (aBoolean1047 ? 0 : 7));
				myAppearance[k] = i2;
				aBoolean1031 = true;
			}
		}
		if (j >= 314 && j <= 323) {
			int l = (j - 314) / 2;
			int k1 = j & 1;
			int j2 = anIntArray990[l];
			if (k1 == 0 && --j2 < 0)
				j2 = APPEARANCE_COLORS[l].length - 1;
			if (k1 == 1 && ++j2 >= APPEARANCE_COLORS[l].length)
				j2 = 0;
			anIntArray990[l] = j2;
			aBoolean1031 = true;
		}
		if (j == 324 && !aBoolean1047) {
			aBoolean1047 = true;
			method45();
		}
		if (j == 325 && aBoolean1047) {
			aBoolean1047 = false;
			method45();
		}
		if (j == 326) {
			stream.createFrame(101);
			stream.writeUnsignedByte(aBoolean1047 ? 0 : 1);
			for (int i1 = 0; i1 < 7; i1++)
				stream.writeUnsignedByte(myAppearance[i1]);

			for (int l1 = 0; l1 < 5; l1++)
				stream.writeUnsignedByte(anIntArray990[l1]);

			return true;
		}
		if (j == 613)
			canMute = !canMute;
		if (j >= 601 && j <= 612) {
			clearTopInterfaces();
			if (reportAbuseInput.length() > 0) {
				stream.createFrame(218);
				stream.writeQWord(StringUtils.longForName(reportAbuseInput));
				stream.writeUnsignedByte(j - 601);
				stream.writeUnsignedByte(canMute ? 1 : 0);
			}
		}
		return false;
	}

	public void method49(Buffer stream) {
		for (int j = 0; j < anInt893; j++) {
			int k = anIntArray894[j];
			Player player = players[k];
			int l = stream.readUnsignedByte();
			if ((l & 0x40) != 0)
				l += stream.readUnsignedByte() << 8;
			method107(l, k, stream, player);
		}
	}

	final void drawMapScenes(int arg0, int arg1, int arg2, int arg3, int arg4) {
		long var6 = scene.getBoundaryObjectTag(arg0, arg1, arg2);
		int var8;
		int var9;
		int var10;
		int var11;
		int var13;
		int var14;
		if (0L != var6) {
			var8 = scene.getObjectFlags(arg0, arg1, arg2, var6);
			var9 = var8 >> 6 & 3;
			var10 = var8 & 31;
			var11 = arg3;
			if (ViewportMouse.method5519(var6)) {
				var11 = arg4;
			}

			int[] var12 = minimapImage.pixels;
			var13 = 24624 + arg1 * 4 + 2048 * (103 - arg2);
			var14 = ViewportMouse.Entity_unpackID(var6);
			ObjectDefinition var15 = ObjectDefinition.lookup(var14);
			if (var15.mapscene != -1) {
				IndexedImage var16 = mapSceneSprites[var15.mapscene];
				if (var16 != null) {
					int var17 = (var15.sizeX * 4 - var16.width) / 2;
					int var18 = (var15.sizeY * 4 - var16.height) / 2;
					var16.draw(48 + arg1 * 4 + var17, var18 + 48 + 4 * (104 - arg2 - var15.sizeY));
				}
			} else {
				if (0 == var10 || 2 == var10) {
					if (var9 == 0) {
						var12[var13] = var11;
						var12[var13 + 512] = var11;
						var12[1024 + var13] = var11;
						var12[var13 + 1536] = var11;
					} else if (var9 == 1) {
						var12[var13] = var11;
						var12[1 + var13] = var11;
						var12[2 + var13] = var11;
						var12[3 + var13] = var11;
					} else if (var9 == 2) {
						var12[3 + var13] = var11;
						var12[var13 + 3 + 512] = var11;
						var12[var13 + 3 + 1024] = var11;
						var12[1536 + var13 + 3] = var11;
					} else if (3 == var9) {
						var12[1536 + var13] = var11;
						var12[1 + var13 + 1536] = var11;
						var12[2 + 1536 + var13] = var11;
						var12[1536 + var13 + 3] = var11;
					}
				}

				if (3 == var10) {
					if (var9 == 0) {
						var12[var13] = var11;
					} else if (1 == var9) {
						var12[var13 + 3] = var11;
					} else if (2 == var9) {
						var12[3 + var13 + 1536] = var11;
					} else if (3 == var9) {
						var12[1536 + var13] = var11;
					}
				}

				if (var10 == 2) {
					if (3 == var9) {
						var12[var13] = var11;
						var12[512 + var13] = var11;
						var12[1024 + var13] = var11;
						var12[1536 + var13] = var11;
					} else if (var9 == 0) {
						var12[var13] = var11;
						var12[var13 + 1] = var11;
						var12[var13 + 2] = var11;
						var12[3 + var13] = var11;
					} else if (1 == var9) {
						var12[var13 + 3] = var11;
						var12[512 + 3 + var13] = var11;
						var12[3 + var13 + 1024] = var11;
						var12[3 + var13 + 1536] = var11;
					} else if (2 == var9) {
						var12[var13 + 1536] = var11;
						var12[1 + 1536 + var13] = var11;
						var12[2 + 1536 + var13] = var11;
						var12[3 + var13 + 1536] = var11;
					}
				}
			}
		}

		var6 = scene.getGameObjectTag(arg0, arg1, arg2);
		if (0L != var6) {
			var8 = scene.getObjectFlags(arg0, arg1, arg2, var6);
			var9 = var8 >> 6 & 3;
			var10 = var8 & 31;
			var11 = ViewportMouse.Entity_unpackID(var6);
			ObjectDefinition var25 = ObjectDefinition.lookup(var11);
			int var20;
			if (var25.mapscene != -1) {
				IndexedImage var19 = mapSceneSprites[var25.mapscene];
				if (var19 != null) {
					var14 = (var25.sizeX * 4 - var19.width) / 2;
					var20 = (var25.sizeY * 4 - var19.height) / 2;
					var19.draw(48 + arg1 * 4 + var14, var20 + (104 - arg2 - var25.sizeY) * 4 + 48);
				}
			} else if (9 == var10) {
				var13 = 15658734;
				if (ViewportMouse.method5519(var6)) {
					var13 = 15597568;
				}

				int[] var21 = minimapImage.pixels;
				var20 = 2048 * (103 - arg2) + arg1 * 4 + 24624;
				if (0 != var9 && 2 != var9) {
					var21[var20] = var13;
					var21[1 + var20 + 512] = var13;
					var21[1024 + var20 + 2] = var13;
					var21[3 + 1536 + var20] = var13;
				} else {
					var21[1536 + var20] = var13;
					var21[1 + var20 + 1024] = var13;
					var21[512 + var20 + 2] = var13;
					var21[var20 + 3] = var13;
				}
			}
		}

		var6 = scene.getFloorDecorationTag(arg0, arg1, arg2);
		if (0L != var6) {
			var8 = ViewportMouse.Entity_unpackID(var6);
			ObjectDefinition var22 = ObjectDefinition.lookup(var8);
			if (var22.mapscene != -1) {
				IndexedImage var23 = mapSceneSprites[var22.mapscene];
				if (var23 != null) {
					var11 = (var22.sizeX * 4 - var23.width) / 2;
					int var24 = (var22.sizeY * 4 - var23.height) / 2;
					var23.draw(arg1 * 4 + 48 + var11, var24 + (104 - arg2 - var22.sizeY) * 4 + 48);
				}
			}
		}
	}

	private static void setHighMem() {
		SceneGraph.low_detail = false;

		lowMem = false;
		MapRegion.low_detail = false;
		ObjectDefinition.lowMem = false;
	}

	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * An array containing all the player's experience.
	 */
	public static double[] experience;
	public static int totalExperience;

	public static int getStaticLevelByExperience(int slot) {
		double exp = experience[slot];

		int points = 0;
		int output = 0;
		for (byte lvl = 1; lvl < 100; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}

	private static String[] args = null;
	public static Client instance;
	public static boolean runelite;

	private static int getVersion() {
		String version = System.getProperty("java.version");
		if(version.startsWith("1.")) {
			version = version.substring(2, 3);
		} else {
			int dot = version.indexOf(".");
			if(dot != -1) { version = version.substring(0, dot); }
		} return Integer.parseInt(version);
	}


// bro its still buildding
	public static JFrame appFrame = null;
	public static Container gameContainer = null;


	private static void setLoggingLevel(Level level) {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(level);
	}

	public static Client getClient(boolean runelite, String...args) {
		try {
//			try {
//				ClientDiscordRPC.initialize();
//			} catch (Exception e) {
//				System.err.println("Failed to establish Discord RPC.");
//				e.printStackTrace();
//			}

			System.out.println("Running Java version " + getVersion());
			Client.runelite = runelite;
			Client.args = args;
			StringBuilder windowTitleBuilder = new StringBuilder();
			windowTitleBuilder.append(Configuration.CLIENT_TITLE);
			server = Configuration.DEDICATED_SERVER_ADDRESS;
			setLoggingLevel(Level.INFO);

			if (args.length > 0) {
				StringBuilder configurationBuilder = new StringBuilder();
				configurationBuilder.append("[");

				for (int index = 0; index < args.length; index++) {
					configurationBuilder.append(args[index].replace("--", ""));
					if (index != args.length - 1)
						configurationBuilder.append(" ");

					if (args[index].startsWith("server=")) {
						server = args[index].replace("server=", "");
						System.out.println("Custom connection address set through run arguments: " + server);
					} else {
						switch (args[index]) {
							case "--debug":
							case "-db":
								setLoggingLevel(Level.DEBUG);
								System.out.println("Setting log level to debug.");
								break;

							case "--developer":
							case "-d":
								Configuration.developerMode = true;
								Configuration.cacheName = Configuration.CACHE_NAME_DEV;
								System.out.println("Developer mode enabled.");
								break;
							case "--local":
							case "-l":
							case "localhost":
							case "local":
								if (server.equals(Configuration.DEDICATED_SERVER_ADDRESS)) {
									server = "127.0.0.1";
									System.out.println("Localhost client enabled.");
								} else {
									throw new IllegalArgumentException("Cannot have custom ip and local enabled.");
								}
								break;
							case "test_server":
								port = Configuration.TEST_PORT;
								System.out.println("Test server enabled.");
								break;

							case "pack_data":
								Configuration.packIndexData = true;
								break;
							case "dump_maps":
								Configuration.dumpMaps = true;
								break;
							case "dump_animation_data":
								Configuration.dumpAnimationData = true;
								break;
							case "dump_data_lists":
								Configuration.dumpDataLists = true;
								break;
							default:
								throw new IllegalArgumentException("Run argument not recognized: " + args[index]);
						}
					}
				}

				// Build the client title with configuration arguments
				configurationBuilder.append("]");
				windowTitleBuilder.append(" ");
				windowTitleBuilder.append(configurationBuilder.toString().trim());
			}


			Configuration.clientTitle = windowTitleBuilder.toString();
			nodeID = 1;
			portOff = 0;
			setHighMem();
			isMembers = true;

			instance = new Client();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return Client.instance;
	}

	public void createProjectiles() {
		for (Projectile projectile = (Projectile) projectiles
				.first(); projectile != null; projectile = (Projectile) projectiles.next())
			if (projectile.plane != plane || loopCycle > projectile.cycleEnd)
				projectile.remove();
			else if (loopCycle >= projectile.cycleStart) {
				if (projectile.targetIndex > 0) {
					Npc npc = npcs[projectile.targetIndex - 1];
					if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0
							&& npc.y < 13312)
						projectile.setDestination(npc.x, npc.y,
								getCenterHeight(projectile.plane, npc.y, npc.x) - projectile.endHeight, loopCycle);
				}
				if (projectile.targetIndex < 0) {
					int index = -projectile.targetIndex;
					Player player;
					if (index == localPlayerIndex)
						player = localPlayer;
					else
						player = players[index];
					if (player != null && player.x >= 0 && player.x < 13312 && player.y >= 0
							&& player.y < 13312)
						projectile.setDestination(player.x, player.y,
								getCenterHeight(projectile.plane, player.y, player.x) - projectile.endHeight, loopCycle);
				}
				projectile.travel(tickDelta);

				scene.add_entity(plane, projectile.yaw, (int) projectile.z, -1, (int) projectile.y,
						60, (int) projectile.x, projectile, false);
			}
	}


	public static String capitalize(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s;
	}


	public void calcFlamesPosition() {
		char c = '\u0100';
		for (int j = 10; j < 117; j++) {
			int k = (int) (Math.random() * 100D);
			if (k < 50)
				anIntArray828[j + (c - 2 << 7)] = 255;
		}
		for (int l = 0; l < 100; l++) {
			int i1 = (int) (Math.random() * 124D) + 2;
			int k1 = (int) (Math.random() * 128D) + 128;
			int k2 = i1 + (k1 << 7);
			anIntArray828[k2] = 192;
		}

		for (int j1 = 1; j1 < c - 1; j1++) {
			for (int l1 = 1; l1 < 127; l1++) {
				int l2 = l1 + (j1 << 7);
				anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128]
						+ anIntArray828[l2 + 128]) / 4;
			}

		}

		anInt1275 += 128;
		if (anInt1275 > anIntArray1190.length) {
			anInt1275 -= anIntArray1190.length;
			int i2 = (int) (Math.random() * 12D);
			randomizeBackground(aBackgroundArray1152s[i2]);
		}
		for (int j2 = 1; j2 < c - 1; j2++) {
			for (int i3 = 1; i3 < 127; i3++) {
				int k3 = i3 + (j2 << 7);
				int i4 = anIntArray829[k3 + 128] - anIntArray1190[k3 + anInt1275 & anIntArray1190.length - 1] / 5;
				if (i4 < 0)
					i4 = 0;
				anIntArray828[k3] = i4;
			}

		}

		System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

		anIntArray969[c - 1] = (int) (Math.sin(loopCycle / 14D) * 16D + Math.sin(loopCycle / 15D) * 14D
				+ Math.sin(loopCycle / 16D) * 12D);
		if (anInt1040 > 0)
			anInt1040 -= 4;
		if (anInt1041 > 0)
			anInt1041 -= 4;
		if (anInt1040 == 0 && anInt1041 == 0) {
			int l3 = (int) (Math.random() * 2000D);
			if (l3 == 0)
				anInt1040 = 1024;
			if (l3 == 1)
				anInt1041 = 1024;
		}
	}

	public void resetAnimation(int i) {
		RSInterface class9 = RSInterface.interfaceCache[i];
		for (int j = 0; j < class9.children.length; j++) {
			if (class9.children[j] == -1)
				break;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j]];
			if (class9_1 == null)
				System.err.println("Null child of index " + j + " inside interface " + i);
			if (class9_1.type == 1)
				resetAnimation(class9_1.id);
			class9_1.currentFrame = 0;
			class9_1.anInt208 = 0;
		}
	}

	public void drawHeadIcon() {
		if (hintArrowType != 2)
			return;
		calcEntityScreenPos((hintIconX - baseX << 7) + anInt937, hintIconFloorPos * 2, (hintIconYpos - baseY << 7) + anInt938);
		if (viewportTempX > -1 && loopCycle % 20 < 10)
			headIconsHint[0].drawSprite(viewportTempX - 12, viewportTempY - 28);
	}

	private GameTimer gameTimer;

	private void mainGameProcessor() {
		callbacks.tick();
		callbacks.post(ClientTick.INSTANCE);

		timeOutHasLoggedMessages();

		if (gameTimer != null) {
			if (gameTimer.isCompleted()) {
				gameTimer.stop();
			}
		}


		spin();
		if (anInt1104 > 1)
			anInt1104--;
		if (anInt1011 > 0)
			anInt1011--;

		int packetsHandled = 0;
		while (parsePacket()) {
			if (packetsHandled++ >= 10) {
				break;
			}
		}

		if (!loggedIn)
			return;

		if (anInt1016 > 0)
			anInt1016--;
		//if (KeyHandler.keyArray[1] == 1 || KeyHandler.keyArray[2] == 1 || KeyHandler.keyArray[3] == 1 || KeyHandler.keyArray[4] == 1)
			//aBoolean1017 = true;
		if (aBoolean1017 && anInt1016 <= 0) {
			anInt1016 = 20;
			aBoolean1017 = false;
			stream.createFrame(86);
			stream.writeWord(camAngleX);
			stream.method432(camAngleY);
		}
		if (super.canvas.hasFocus() && !aBoolean954) {
			aBoolean954 = true;
			stream.createFrame(3);
			stream.writeUnsignedByte(1);
		}
		if (!super.canvas.hasFocus() && aBoolean954) {
			aBoolean954 = false;
			stream.createFrame(3);
			stream.writeUnsignedByte(0);
		}
		if (gameState != 30) {
			return;
		}

		processPendingSpawns();
		// method90();
		anInt1009++;
		if (anInt1009 > 750) {
			logger.debug("Dropping client due to packets not being received.");
			dropClient();
		}
		method114();
		method95();
		processMobChatText();
		tickDelta++;
		StaticSound.method4532();
		if (crossType != 0) {
			crossIndex += 20;
			if (crossIndex >= 400)
				crossType = 0;
		}
		if (atInventoryInterfaceType != 0) {
			atInventoryLoopCycle++;
			if (atInventoryLoopCycle >= 15) {
				if (atInventoryInterfaceType == 2)
					needDrawTabArea = true;
				if (atInventoryInterfaceType == 3)
					inputTaken = true;
				atInventoryInterfaceType = 0;
			}
		}
		if (activeInterfaceType != 0) {
			anInt989++;
			if (MouseHandler.mouseX > anInt1087 + 5 || MouseHandler.mouseX < anInt1087 - 5 || MouseHandler.mouseY > anInt1088 + 5
					|| MouseHandler.mouseY < anInt1088 - 5)
				aBoolean1242 = true;
			if (MouseHandler.instance.clickMode2 == 0) {
				if (activeInterfaceType == 2)
					needDrawTabArea = true;
				if (activeInterfaceType == 3)
					inputTaken = true;
				activeInterfaceType = 0;
				if (aBoolean1242 && anInt989 >= getDragSetting(draggingItemInterfaceId)) {
					lastActiveInvInterface = -1;
					processRightClick();

					// Bank interface adding items to another tab via dragging on the tab icons at the top
					if (Bank.isBankContainer(RSInterface.interfaceCache[draggingItemInterfaceId]) || draggingItemInterfaceId == Bank.SEARCH_CONTAINER) {
						//System.out.println("BANK TAB SLOT " + mouseX + ", " + mouseY);
						Point southWest, northEast;



						int xOffset = !isResized() ? 0 : (canvasWidth / 2) - 356;
						int yOffset = !isResized() ? 0: (canvasHeight / 2) - 230;

						southWest = new Point(68 + xOffset, 75 + yOffset);
						northEast = new Point(457 + xOffset, 41 + yOffset);
						int[] slots = new int[9];
						for (int i = 0; i < slots.length; i++)
							slots[i] = (41 * i) + (int) southWest.getX();
						for (int i = 0; i < slots.length; i++) {
							if (MouseHandler.mouseX >= slots[i] && MouseHandler.mouseX <= slots[i] + 42
									&& MouseHandler.mouseY >= northEast.getY() && MouseHandler.mouseY <= southWest.getY()) {

								if (draggingItemInterfaceId != Bank.SEARCH_CONTAINER) {
									RSInterface rsi = RSInterface.interfaceCache[58050 + i];
									if (rsi.isMouseoverTriggered) {
										continue;
									}
								}

								// Update client side to hide latency
								if (Bank.getCurrentBankTab() == 0) {
									OptionalInt fromTabOptional = Arrays.stream(Bank.ITEM_CONTAINERS).filter(id -> draggingItemInterfaceId == id).findFirst();
									if (fromTabOptional.isPresent()) {
										RSInterface fromTab = RSInterface.interfaceCache[fromTabOptional.getAsInt()];
										RSInterface toTab = RSInterface.interfaceCache[Bank.ITEM_CONTAINERS[i]];
										if (toTab.getInventoryContainerFreeSlots() > 0 && fromTab.id != toTab.id) {
											RSInterface.insertInventoryItem(fromTab, itemDraggingSlot, toTab);
										}
									}
								}

								if (draggingItemInterfaceId != Bank.SEARCH_CONTAINER) {
									stream.createFrame(214);
									stream.method433(draggingItemInterfaceId);
									stream.method424(0);
									stream.method433(itemDraggingSlot);
									stream.method431(1000 + i);
								} else {
									stream.createFrame(243);
									stream.writeWord(i);
									stream.writeWord(RSInterface.get(draggingItemInterfaceId).inventoryItemId[itemDraggingSlot]);
								}
								return;
							}
						}
					}

					try {
						RSInterface class9 = RSInterface.interfaceCache[draggingItemInterfaceId];
						if (class9 != null) {
							if (mouseInvInterfaceIndex != itemDraggingSlot && lastActiveInvInterface == draggingItemInterfaceId) {
								// Dragging item inside the same container

								int insertMode = 0;
								if (anInt913 == 1 && class9.contentType == 206)
									insertMode = 1;
								if (class9.inventoryItemId[mouseInvInterfaceIndex] <= 0)
									insertMode = 0;

								if (class9.aBoolean235) {
									// Move to empty slot?
									int l2 = itemDraggingSlot;
									int l3 = mouseInvInterfaceIndex;
									class9.inventoryItemId[l3] = class9.inventoryItemId[l2];
									class9.inventoryAmounts[l3] = class9.inventoryAmounts[l2];
									class9.inventoryItemId[l2] = -1;
									class9.inventoryAmounts[l2] = 0;
								} else if (insertMode == 1) {
									// Insert
									int i3 = itemDraggingSlot;
									for (int i4 = mouseInvInterfaceIndex; i3 != i4; ) {
										if (i3 > i4) {
											class9.swapInventoryItems(i3, i3 - 1);
											i3--;
										} else if (i3 < i4) {
											class9.swapInventoryItems(i3, i3 + 1);
											i3++;
										}
									}
								} else {
									// Swap
									class9.swapInventoryItems(itemDraggingSlot, mouseInvInterfaceIndex);
								}

								Bank.shiftTabs();
								stream.createFrame(214);
								stream.method433(draggingItemInterfaceId);
								stream.method424(insertMode);
								stream.method433(itemDraggingSlot);
								stream.method431(mouseInvInterfaceIndex);
							} else if (class9.allowInvDraggingToOtherContainers && lastActiveInvInterface != draggingItemInterfaceId) {
								if (lastActiveInvInterface != -1 && draggingItemInterfaceId != -1) {
									RSInterface draggingFrom = RSInterface.interfaceCache[draggingItemInterfaceId];
									RSInterface draggingTo = RSInterface.interfaceCache[lastActiveInvInterface];
									if (draggingTo != null && draggingFrom != null
											&& draggingTo.allowInvDraggingToOtherContainers
											&& draggingFrom.allowInvDraggingToOtherContainers) {
										int fromSlot = itemDraggingSlot;
										int toSlot = mouseInvInterfaceIndex;
										int insertMode = 0;
										if (anInt913 == 1 && class9.contentType == 206)
											insertMode = 1;

										if (insertMode == 1) {
											// insert
											if (draggingTo.getInventoryContainerFreeSlots() > 0) {
												RSInterface.insertInventoryItem(draggingFrom, fromSlot, draggingTo, toSlot);
											} else {
												return;
											}
										} else {
											//swap
											RSInterface.swapInventoryItems(draggingFrom, fromSlot, draggingTo, toSlot);
										}

										stream.createFrame(242);
										stream.writeWord(draggingTo.id);
										stream.writeWord(draggingFrom.id);
										stream.method424(insertMode);
										stream.writeWord(fromSlot);
										stream.writeWord(toSlot);
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if ((anInt1253 == 1 || menuHasAddFriend(menuOptionsCount - 1)) && menuOptionsCount > 2)
					openMenu(MouseHandler.saveClickX, MouseHandler.saveClickY);
				else if (menuOptionsCount > 0)
					processMenuActions(menuOptionsCount - 1);
				atInventoryLoopCycle = 10;
				clickMode3 = 0;
			}
		}
		if (SceneGraph.clickedTileX != -1) {
			if (fullscreenInterfaceID != -1) {
				return;
			}
			int k = SceneGraph.clickedTileX;
			int k1 = SceneGraph.clickedTileY;
			boolean flag = false;
			if (localPlayer.isAdminRights() && controlIsDown) {
				teleport(baseX + k, baseY + k1);
			} else {
				flag = doWalkTo(0, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, k1, true, k);
			}
			SceneGraph.clickedTileX = -1;
			if (flag) {
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 1;
				crossIndex = 0;
			}
		}
		if (clickMode3 == 1 && clickToContinueString != null) {
			clickToContinueString = null;
			inputTaken = true;
			clickMode3 = 0;
		}
		menu();
		if (MouseHandler.instance.clickMode2 == 1 || clickMode3 == 1)
			mouseClickCount++;
		if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
			if (anInt1501 < 50 && !isMenuOpen) {
				anInt1501++;
				if (anInt1501 == 50) {
					if (anInt1500 != 0) {
						inputTaken = true;
					}
					if (anInt1044 != 0) {
						needDrawTabArea = true;
					}
				}
			}
		} else if (anInt1501 > 0) {
			anInt1501--;
		}

		if (gameState == GameState.LOGGED_IN.getState()) {
			Camera.updateCamera();
			if (oculusOrbState != 1 || keyManager.lastTypedCharacter <= 0) {
				manageTextInputs();
			}

		}

		++MouseHandler.idleCycles;
		++KeyHandler.idleCycles;

		if (MouseHandler.idleCycles++ > 9000) {
			anInt1011 = 250;
			MouseHandler.idleCycles = 0;
			stream.createFrame(202);
		}

		anInt1010++;
		if (anInt1010 > 50)
			stream.createFrame(0);

		try {
			if (socketStream != null && stream.currentPosition > 0) {
				socketStream.queueBytes(stream.currentPosition, stream.payload);
				stream.currentPosition = 0;
				anInt1010 = 0;
			}
		} catch (IOException _ex) {
			dropClient();
		} catch (Exception exception) {
			resetLogout();
			if (Configuration.developerMode) {
				exception.printStackTrace();
			}
		}
	}

	public void method63() {
		SpawnedObject spawnedObject = (SpawnedObject) spawns.reverseGetFirst();
		for (; spawnedObject != null; spawnedObject = (SpawnedObject) spawns.reverseGetNext())
			if (spawnedObject.getLongetivity == -1) {
				spawnedObject.delay = 0;
				handleTemporaryObjects(spawnedObject);
			} else {
				spawnedObject.unlink();
			}

	}
	/**
	 * Runescape Loading Bar
	 *
	 * @param percentage
	 * @param s
	 * @param downloadSpeed
	 * @param secondsRemaining
	 * @trees
	 */
	public Sprite logo, loginBackground;


	public int getPixelAmt(int current, int pixels) {
		return (int) (pixels * .01 * current);
	}

	public void drawLoadingText(int percentage, String s) {
		loadingPercent = percentage;
		loadingText = s;
		targetPercent = loadingPercent;
	}

	public void method65(int width, int height, int mouseX, int mouseY, RSInterface class9, int i1, boolean flag, int j1) {
		int anInt992;
		if (aBoolean972)
			anInt992 = 32;
		else
			anInt992 = 0;
		aBoolean972 = false;
		if (mouseX >= width && mouseX < width + 16 && mouseY >= i1 && mouseY < i1 + 16) {
			class9.scrollPosition -= mouseClickCount * 4;
			if (flag) {
				needDrawTabArea = true;
			}
		} else if (mouseX >= width && mouseX < width + 16 && mouseY >= (i1 + height) - 16 && mouseY < i1 + height) {
			class9.scrollPosition += mouseClickCount * 4;
			if (flag) {
				needDrawTabArea = true;
			}
		} else if (mouseX >= width - anInt992 && mouseX < width + 16 + anInt992 && mouseY >= i1 + 16 && mouseY < (i1 + height) - 16 && mouseClickCount > 0) {
			int l1 = ((height - 32) * height) / j1;
			if (l1 < 8)
				l1 = 8;
			int i2 = mouseY - i1 - 16 - l1 / 2;
			int j2 = height - 32 - l1;
			class9.scrollPosition = ((j1 - height) * i2) / j2;
			if (flag)
				needDrawTabArea = true;
			aBoolean972 = true;
		}
	}

	private boolean clickObject(long object, int j, int k) {
		try {
			if (fullscreenInterfaceID != -1) {
				return false;
			}
		
			int objectFlag = scene.getObjectFlags(plane, j, k, object);
			int orientation = ObjectKeyUtil.getObjectOrientation(objectFlag);

			ObjectDefinition class46 = ObjectDefinition.lookup(ObjectKeyUtil.getObjectId(object));
			int i2;
			int j2;
			if (orientation == 0 || orientation == 2) {
				i2 = class46.sizeX;
				j2 = class46.sizeY;
			} else {
				i2 = class46.sizeY;
				j2 = class46.sizeX;
			}
			int k2 = class46.surroundings;
			if (orientation != 0) {
				k2 = (k2 << orientation & 0xf) + (k2 >> 4 - orientation);
			}
			doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, j2, 0, i2, k2, j, false, k);

			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			return true;
		} catch (Exception e) {
			System.err.println("Error while walking.");
			e.printStackTrace();
			return false;
		}
	}

	public void sendStringAsLong(String string) {
		stream.createFrame(60);
		stream.writeUnsignedByte(1 + string.length());
		stream.writeString(string);
	}

	public void sendString(int identifier, String text) {
		text = identifier + "," + text;
		stream.createFrame(127);
		stream.writeUnsignedByte(text.length() + 1);
		stream.writeString(text);
	}

	public void dropClient() {
		if (anInt1011 > 0) {
			resetLogout();
			return;
		}
		setGameState(GameState.CONNECTION_LOST);
		logger.debug("Dropping client.");

		Rasterizer2D.fillPixels(2, 229, 39, 0xffffff, 2);
		Rasterizer2D.drawPixels(37, 3, 3, 0, 227);
		frameMode(false);
		minimapState = 0;
		Preferences.save();
		destX = 0;
		RSSocket rsSocket = socketStream;
		loggedIn = false;
		loginFailures = 0;
		//login(myUsername, getPassword(), true);
		if (!loggedIn)
			resetLogout();
		try {
			rsSocket.close();
		} catch (Exception _ex) {
		}
	}

	public static String formatNumber(double number) {
		return NumberFormat.getInstance().format(number);
	}

	public int settings[];

	private void updateSettings() {
		settings[809] = Configuration.alwaysLeftClickAttack ? 1 : 0;
		settings[817] = Configuration.escapeCloseInterface ? 1 : 0;
	}

	public void onRealButtonClick(int buttonId) {
		Interfaces.taskInterface.actions.onButtonClick(buttonId);
	}


	private void processMenuActions(int menuActionIndex) {
		if (menuActionIndex >= 0) {
			int param0 = Client.menuArguments1[menuActionIndex];
			int param1 = Client.menuArguments2[menuActionIndex];
			int opcode = Client.menuOpcodes[menuActionIndex];
			int id = Client.menuIdentifiers[menuActionIndex];
			int var5 = 0;
			String option = Client.menuActions[menuActionIndex];
			String target = Client.menuTargets[menuActionIndex];

			RSRuneLiteMenuEntry menuEntry = null;

			for (int i = getMenuOptionCount() - 1; i >= 0; --i)
			{
				if (getMenuOpcodes()[i] == opcode
						&& getMenuIdentifiers()[i] == id
						&& getMenuArguments1()[i] == param0
						&& getMenuArguments2()[i] == param1
						&& (option != null && option.equals(getMenuOptions()[i]))
						&& (target != null && target.equals(getMenuTargets()[i]))
				)
				{
					menuEntry = rl$menuEntries[i];
					break;
				}
			}

			boolean isTemp = false;
			if (getTempMenuAction() != null)
			{
				isTemp = getTempMenuAction().getOpcode() == opcode &&
						getTempMenuAction().getIdentifier() == id &&
						getTempMenuAction().getOption().equals(option) &&
						getTempMenuAction().getTarget().equals(target) &&
						getTempMenuAction().getParam0() == param0 &&
						getTempMenuAction().getParam1() == param1;
			}

			if (menuEntry == null && isTemp)
			{
				int i;
				if (getMenuOptionCount() < 500)
				{
					i = getMenuOptionCount();
					setMenuOptionCount(getMenuOptionCount() + 1);
				}
				else
				{
					i = 0;
				}

				getMenuOpcodes()[i] = opcode;
				getMenuIdentifiers()[i] = id;
				getMenuOptions()[i] = option;
				getMenuTargets()[i] = target;
				getMenuArguments1()[i] = param0;
				getMenuArguments2()[i] = param1;
				getMenuForceLeftClick()[i] = false;
				menuEntry = rl$menuEntries[i];

				if (menuEntry == null)
				{
					menuEntry = rl$menuEntries[i] = newRuneliteMenuEntry(i);
				}
			}

			MenuOptionClicked event;
			if (menuEntry == null)
			{
				MenuEntry tmpEntry = createMenuEntry(option, target, id, opcode, param0, param1, -1, false);
				event = new MenuOptionClicked(tmpEntry);


			}
			else
			{

				log.info("Menu click op {} targ {} action {} id {} p0 {} p1 {}", option, target, opcode, id, param0, param1);
				event = new MenuOptionClicked(menuEntry);
				getCallbacks().post(event);

				if (menuEntry.getConsumer() != null)
				{
					try
					{
						menuEntry.getConsumer().accept(menuEntry);
					}
					catch (Exception ex)
					{
						getLogger().warn("exception in menu callback", ex);
					}
				}

				if (event.isConsumed())
				{
					return;
				}
			}



			/*
			 * The RuneScape client may deprioritize an action in the menu by incrementing the opcode with 2000,
			 * undo it here so we can get the correct opcode
			 */
			boolean decremented = false;
			if (opcode >= 2000)
			{
				decremented = true;
				opcode -= 2000;
			}

			if (printMenuActions)
			{
				log.info(
						"|MenuAction|: MenuOption={} MenuTarget={} Id={} " +
								"Opcode={}/{} " +
								"Param0={} Param1={} CanvasX={} CanvasY={}",
						event.getMenuOption(), event.getMenuTarget(), event.getId(),
						event.getMenuAction(), opcode + (decremented ? 2000 : 0),
						event.getParam0(), event.getParam1(), -1, -1
				);

				if (menuEntry != null)
				{
					log.info(
							"|MenuEntry|: Idx={} MenuOption={} " +
									"MenuTarget={} Id={} MenuAction={} " +
									"Param0={} Param1={} Consumer={}",
							menuEntry.getIdx(), menuEntry.getOption(),
							menuEntry.getTarget(), menuEntry.getIdentifier(), menuEntry.getType(),
							menuEntry.getParam0(), menuEntry.getParam1(), menuEntry.getConsumer()
					);
				}
			}

			doAction(menuActionIndex,event.getParam0(), event.getParam1(),
					event.getMenuAction() == UNKNOWN ? opcode : event.getMenuAction().getId(),
					event.getId(), -1, event.getMenuOption(), event.getMenuTarget(),
					-1, -1);
		}
	}

	private void doAction(int menuRow, int param0, int param1, int opcode, int id, int na, String option, String target, int canvasX, int canvasY) {

		if (inputDialogState != 0 && inputDialogState != 3) {
			inputDialogState = 0;
			inputTaken = true;
		}

		if (opcode >= 10_000) {
			opcode -= 10_000;
		}

		switch (param1) {
			case 42522:
				if (isResized()) {
					setConfigButton(menuRow, true);
					setConfigButton(23003, false);
					setConfigButton(23005, false);
					frameMode(false);
				}
				break;

			case 42523:
				if (!isResized()) {
					setConfigButton(menuRow, true);
					setConfigButton(23001, false);
					setConfigButton(23005, false);
					frameMode(true);
				}
				break;
		}

		if (opcode == 1100) {
			RSInterface button = RSInterface.interfaceCache[param1];
			button.setMenuVisible(button.isMenuVisible() ? false : true);
		}
		if (opcode == 474) {
			drawExperienceCounter = !drawExperienceCounter;
		}

		if (opcode == 8992) {
			/**
			 * for drop interface
			 */

			String messageToSearch = lastViewedDropTable;

			if (messageToSearch == null) {
				return;
			}

			NPCDropInfo info = NPCDropInfo.getEntry(messageToSearch);

			if (info == null) {
				return;
			}
			stream.createFrame(177);
			stream.writeWord(info.npcIndex);
			return;
		}


		if (opcode == 475) {
			stream.createFrame(185);
			stream.writeWord(-1);
			experienceCounter = 0L;
		}

		if(opcode==855){//wiki search
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 14;
			aString1121 = "Search the OSRS wiki (opens browser)";
		}

		if (opcode == 944) {//tele orb
			stream.createFrame(185);
			stream.writeWord(7000);
		}
		if (opcode == 945) {//tele orb lat teleport option
			stream.createFrame(185);
			stream.writeWord(7001);
		}

		if (opcode == 713) {//Money pouch
			//	inputTaken = true;
			//	messagePromptRaised = true;
			//	amountOrNameInput = "";
			//	promptInput = "";
			//	inputDialogState = 0;
			//	friendsListAction = 557;
			aString1121 = "Enter amount to withdraw";
			stream.createFrame(185);
			stream.writeWord(716);
		}

		if (opcode == 714) {//Money pouch
			stream.createFrame(185);
			stream.writeWord(714);
		}
		if (opcode == 715) {//Money pouch
			stream.createFrame(185);
			stream.writeWord(715);
		}


		if(opcode==852){
			launchURL("https://explv.github.io/");
		}
		if (opcode == 1850) {
			stream.createFrame(185);
			stream.writeWord(5100);
		}
		if (opcode == 769) {
			RSInterface d = RSInterface.interfaceCache[param1];
			RSInterface p = RSInterface.interfaceCache[id];
			if (!d.dropdown.isOpen()) {
				if (p.dropdownOpen != null) {
					p.dropdownOpen.dropdown.setOpen(false);
				}
				p.dropdownOpen = d;
			} else {
				p.dropdownOpen = null;
			}
			d.dropdown.setOpen(!d.dropdown.isOpen());
		} else if (opcode == 770) {
			RSInterface d = RSInterface.interfaceCache[param1];
			RSInterface p = RSInterface.interfaceCache[id];
			if (id >= d.dropdown.getOptions().length)
				return;
			d.dropdown.setSelected(d.dropdown.getOptions()[param0]);
			d.dropdown.setOpen(false);
			d.dropdown.getMenuItem().select(param0, d);
			try {
				SettingsManager.saveSettings(Client.instance);
			} catch (IOException io) {
				io.printStackTrace();
			}
			p.dropdownOpen = null;
		}
		if (opcode == 850) {
			if (tabInterfaceIDs[tabID] == 17200) {
				stream.createFrame(185);
				stream.writeWord(5200 + param1);
			}
		}
		if (opcode == 661) { // intid, slot, itemid;
			stream.createFrame(232);
			stream.method432(1);
			stream.writeWord(id);
		}
		if (opcode == 662) {
			stream.createFrame(232);
			stream.method432(2);
			stream.writeWord(id);
		}
		if (opcode == 663) {
			stream.createFrame(232);
			stream.method432(3);
			stream.writeWord(id);
		}
		if (opcode == 664) {
			stream.createFrame(232);
			stream.method432(4);
			stream.writeWord(id);
		}

		switch (opcode) {
			case 1500: // Toggle quick prayers
				prayClicked = !prayClicked;
				stream.createFrame(185);
				stream.writeWord(5000);
				break;

			case 1506: // Select quick prayers
				stream.createFrame(185);
				stream.writeWord(5001);
				setTab(5);
				break;
		}
		if (opcode == 1200) {
			stream.createFrame(185);
			stream.writeWord(param1);
			RSInterface item = RSInterface.interfaceCache[param1];
			RSInterface menu = RSInterface.interfaceCache[item.mOverInterToTrigger];
			menu.setMenuItem(item.getMenuItem());
			menu.setMenuVisible(false);
		}
		if (opcode >= 1700 && opcode <= 1710) {
			RSInterface button = RSInterface.get(param1);
			if (button.buttonListener != null)
				button.buttonListener.accept(param1);
			// Button Click Packet
			if (button.newButtonClicking) {
				stream.createFrame(184);
				stream.writeWord(param1);
				onRealButtonClick(param1);
			} else {
				stream.createFrame(185);
				int offset = param1 + (param1 - 58030) * 10 + (opcode - 1700);
				stream.writeWord(offset);
				Bank.handleButton(offset);
			}
		}
		if (opcode == 300) {
			stream.createFrame(141);
			stream.method432(param0);
			stream.writeWord(param1);
			stream.method432(id);
			stream.writeDWord(modifiableXValue);
		}
		if (opcode == 291) {
			stream.createFrame(140);
			stream.writeWord(param1);
			stream.method433(id);
			stream.method431(param0);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 582) {
			Npc npc = npcs[id];
			if (npc != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, npc.pathY[0], false, npc.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(57);
				stream.method432(anInt1285);
				stream.method432(id);
				stream.method431(anInt1283);
				stream.method432(anInt1284);
			}
		}
		if (opcode == 234) {
			boolean flag1 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, param1, false, param0);
			if (!flag1)
				flag1 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, param1, false, param0);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(236);
			stream.method431(param1 + baseY);
			stream.writeWord(id);
			stream.method431(param0 + baseX);
		}
		if (opcode == 62 && clickObject(id, param1, param0)) {
			stream.createFrame(192);
			stream.writeWord(anInt1284);
			stream.writeDWord(id);
			stream.method433(param1 + baseY);
			stream.method431(anInt1283);
			stream.method433(param0 + baseX);
			stream.writeWord(anInt1285);
		}
		if (opcode == 511) {
			boolean flag2 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, param1, false, param0);
			if (!flag2)
				flag2 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, param1, false, param0);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(25);
			stream.method431(anInt1284);
			stream.method432(anInt1285);
			stream.writeWord(id);
			stream.method432(param1 + baseY);
			stream.method433(anInt1283);
			stream.writeWord(param0 + baseX);
		}
		if (opcode == 74) {
			stream.createFrame(122);
			stream.writeWord(param1);
			stream.writeWord(param0);
			stream.writeWord(id);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 315) {
			RSInterface class9 = RSInterface.interfaceCache[param1];
			boolean flag8 = true;
			if (class9.type == RSInterface.TYPE_CONFIG || class9.id == 50009) { // Placeholder toggle
				class9.active = !class9.active;
			} else if (class9.type == RSInterface.TYPE_CONFIG_HOVER) {
				RSInterface.handleConfigHover(class9);
			}
			if (class9.contentType > 0)
				flag8 = promptUserForInput(class9);
			if (flag8) {
				SettingsTabWidget.settings(param1);
				switch (param1) {
					case 23003:
						Configuration.alwaysLeftClickAttack = !Configuration.alwaysLeftClickAttack;
						// savePlayerData();
						updateSettings();
						break;
					case 23005:
						Configuration.hideCombatOverlay = !Configuration.hideCombatOverlay;
						// savePlayerData();
						updateSettings();
						break;
					case 37708:
						if (coloredItemColor != 0xffffff) {
							inputTaken = true;
							inputDialogState = 0;
							messagePromptRaised = true;
							promptInput = "";
							friendsListAction = 25;// 25 = enter item name
							aString1121 = "Enter an item name to set this color to. (Name must match exact)";
						} else {
							pushMessage("You must select a color first...", 0, "");
						}
						break;
					case 37710:
						inputTaken = true;
						inputDialogState = 0;
						messagePromptRaised = true;
						promptInput = "";
						friendsListAction = 26;// 25 = enter item name
						aString1121 = "Enter a minimum item value to display on the ground.";
						break;
					case 37706:
						Robot robot2;
						PointerInfo pointer2;
						pointer2 = MouseInfo.getPointerInfo();
						Point coord2 = pointer2.getLocation();
						try {
							robot2 = new Robot();
							coord2 = MouseInfo.getPointerInfo().getLocation();
							Color color = robot2.getPixelColor((int) coord2.getX(), (int) coord2.getY());
							String hex2 = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
							int hex3 = Integer.parseInt(hex2, 16);
							// pushMessage("<shad="+hex3+">Yell Color set.</shad>", 0, "");
							coloredItemColor = hex3;
							RSInterface.interfaceCache[37707].message = "Current color chosen!";
							RSInterface.interfaceCache[37707].textColor = coloredItemColor;
						} catch (AWTException e) {
							e.printStackTrace();
						}
						break;

					case 62013:
						RSInterface input = RSInterface.interfaceCache[RSInterface.selectedItemInterfaceId + 1];
						RSInterface itemContainer = RSInterface.interfaceCache[RSInterface.selectedItemInterfaceId];
						if (RSInterface.selectedItemInterfaceId <= 0) {
							return;
						}
						if (input != null && itemContainer != null) {
							int amount = -1;
							try {
								amount = Integer.parseInt(input.message);
							} catch (NumberFormatException nfe) {
								pushMessage("The amount must be a non-negative numerical value.", 0, "");
								break;
							}
							if (itemContainer.itemSearchSelectedId < 0) {
								itemContainer.itemSearchSelectedId = 0;
							}
							if (itemContainer.itemSearchSelectedSlot < 0) {
								itemContainer.itemSearchSelectedSlot = 0;
							}
							stream.createFrame(124);
							stream.writeDWord(RSInterface.selectedItemInterfaceId);
							stream.writeDWord(itemContainer.itemSearchSelectedSlot);
							stream.writeDWord(itemContainer.itemSearchSelectedId - 1);
							stream.writeDWord(amount);
						}
						break;

					case 32013:
						RSInterface input2 = RSInterface.interfaceCache[RSInterface.selectedItemInterfaceId + 1];
						RSInterface itemContainer2 = RSInterface.interfaceCache[RSInterface.selectedItemInterfaceId];
						if (RSInterface.selectedItemInterfaceId <= 0) {
							return;
						}
						if (input2 != null && itemContainer2 != null) {
							int amount = -1;
							try {
								amount = Integer.parseInt(input2.message);
							} catch (NumberFormatException nfe) {
								pushMessage("The amount must be a non-negative numerical value.", 0, "");
								break;
							}
							if (itemContainer2.itemSearchSelectedId < 0) {
								itemContainer2.itemSearchSelectedId = 0;
							}
							if (itemContainer2.itemSearchSelectedSlot < 0) {
								itemContainer2.itemSearchSelectedSlot = 0;
							}
							stream.createFrame(124);
							stream.writeDWord(RSInterface.selectedItemInterfaceId);
							stream.writeDWord(itemContainer2.itemSearchSelectedSlot);
							stream.writeDWord(itemContainer2.itemSearchSelectedId - 1);
							stream.writeDWord(amount);
						}
						break;
					case 19144:
						sendFrame248(15106, 3213);
						resetAnimation(15106);
						inputTaken = true;
						break;
					default:
						if (RSInterface.get(param1) != null && RSInterface.get(param1).newButtonClicking) {
							stream.createFrame(184);
							stream.writeWord(param1);
						} else {
							RSInterface widget = RSInterface.get(param1);

							// Determine if a special attack button is being pressed
							if (widget.hasTooltip()) {
								String tooltip = widget.tooltip.toLowerCase();
								boolean isSpecialAttackButton = tooltip.contains("use") && tooltip.contains("special attack");
								if (isSpecialAttackButton) {
									// Send custom packet for special attacks
									stream.createFrame(209);
									stream.writeWord(param1);
									return;
								}
							}

							// If special attack button is not being pressed process normally
							stream.createFrame(185);
							stream.writeWord(param1);

							switch (param1) {
								case SettingsTabWidget.HIDE_LOCAL_PET_OPTIONS:
									SettingsTabWidget.toggleHidePetOption();
									break;
							}
						}

						if (param1 >= 61101 && param1 <= 61200) {
							int selected = param1 - 61101;
							for (int ii = 0, slot = -1; ii < Js5List.getConfigSize(Js5ConfigType.ITEM) && slot < 100; ii++) {
								ItemDefinition def = ItemDefinition.lookup(ii);

								if (def.name == null || def.notedTemplate == ii - 1 || def.notedID == ii - 1
									|| RSInterface.interfaceCache[61254].message.length() == 0) {
									continue;
								}

								if (def.name.toLowerCase()
									.contains(RSInterface.interfaceCache[61254].message.toLowerCase())) {
									slot++;
								}

								if (slot != selected) {
									continue;
								}

								long num = Long.valueOf(RSInterface.interfaceCache[61255].message.replaceAll(",", ""));

								if (num > Integer.MAX_VALUE) {
									num = Integer.MAX_VALUE;
								}

								stream.createFrame(149);
								stream.writeWord(def.id);
								stream.writeDWord((int) num);
								stream.writeUnsignedByte(variousSettings[1075]);
								break;
							}
						}

						break;

				}
			}
		}
		if (opcode == 561) {
			Player player = players[id];
			if (player != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, player.pathY[0], false,
					player.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1188 += id;
				if (anInt1188 >= 90) {
					stream.createFrame(136);
					anInt1188 = 0;
				}
				stream.createFrame(128);
				stream.writeWord(id);
			}
		}
		if (opcode == 745) {
			stream.createFrame(8);
			stream.writeDWord(id);
		}
		if (opcode == 20) {
			Npc class30_sub2_sub4_sub1_sub1_1 = npcs[id];
			if (class30_sub2_sub4_sub1_sub1_1 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_1.pathY[0],
					false, class30_sub2_sub4_sub1_sub1_1.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(155);
				stream.method431(id);
			}
		}
		if (opcode == 779) {
			Player class30_sub2_sub4_sub1_sub2_1 = players[id];
			if (class30_sub2_sub4_sub1_sub2_1 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_1.pathY[0],
					false, class30_sub2_sub4_sub1_sub2_1.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(153);
				stream.method431(id);
			}
		}
		if (opcode == 516) {
			if (!isMenuOpen) {
				scene.register_click(MouseHandler.saveClickY - 4, MouseHandler.saveClickX - 4, Client.instance.plane);
			} else {
				scene.register_click(param0 - 4, param1 - 4, Client.instance.plane);
			}
		}
		if (opcode == 1062) {
			anInt924 += baseX;
			if (anInt924 >= 113) {
				stream.createFrame(183);
				stream.writeDWordBigEndian(0xe63271);
				anInt924 = 0;
			}
			clickObject(id, param1, param0);
			stream.createFrame(228);
			stream.writeDWord(id);
			stream.method432(param1 + baseY);
			stream.writeWord(param0 + baseX);
		}
		if (opcode == 679 && !continuedDialogue) {
			stream.createFrame(40);
			stream.writeWord(param1);
			continuedDialogue = true;
		}
		if (opcode == Autocast.AUTOCAST_MENU_ACTION_ID) {
			RSInterface button = RSInterface.get(param1);
			stream.createFrame(5);
			stream.writeWord(button.autocastSpellId);
			stream.writeByte(button.autocastDefensive ? 1 : 0);
			if (Configuration.developerMode) {
				System.out.println("Autocast: " + button.autocastSpellId + ", defensive: " + button.autocastDefensive);
			}
		}
		if (opcode == 431) {
			stream.createFrame(129);
			stream.method432(param0);
			stream.writeWord(param1);
			stream.method432(id);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 337 || opcode == 42 || opcode == 792 || opcode == 322) {
			String s = menuActions[menuRow];
			int k1 = s.indexOf("@whi@");
			if (k1 != -1) {
				long l3 = StringUtils.longForName(s.substring(k1 + 5).trim());
				if (opcode == 337)
					addFriend(l3);
				if (opcode == 42)
					addIgnore(l3);
				if (opcode == 792)
					delFriend(l3);
				if (opcode == 322)
					delIgnore(l3);
			}
		}
		if (opcode == 1337) { // Placeholders
			inputString = "::placeholder-" + param0 + "-" + id;
			stream.createFrame(103);
			stream.writeUnsignedByte(inputString.length() - 1);
			stream.writeString(inputString.substring(2));
			inputString = "";
		}
		if (opcode == 53) {
			stream.createFrame(135);
			stream.method431(param0);
			stream.method432(param1);
			stream.method431(id);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 539) {
			stream.createFrame(16);
			stream.writeWord(id);
			stream.method433(param0);
			stream.method433(param1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}

		if (opcode == 9111 || opcode == 9112 || opcode == 9115) {

			Broadcast bc = BroadcastManager.getCurrentBroadcast();

			if (bc == null) {
				System.err.println("No broadcast found for this msg..");
				return;
			}
			if (opcode == 9115) {
				BroadcastManager.removeIndex(bc.index);
				return;
			}
			stream.createFrame(199);
			stream.writeByte(bc.index);
			return;
		}

		if (opcode == 484 || opcode == 6 || opcode == 9501) {
			String s1 = menuActions[menuRow];
			int l1 = s1.indexOf("@whi@");
			if (l1 != -1) {
				s1 = s1.substring(l1 + 5).trim();
				String s7 = StringUtils.fixName(StringUtils.nameForLong(StringUtils.longForName(s1)));
				boolean flag9 = false;
				for (int j3 = 0; j3 < playerCount; j3++) {
					Player class30_sub2_sub4_sub1_sub2_7 = players[playerIndices[j3]];
					if (class30_sub2_sub4_sub1_sub2_7 == null || class30_sub2_sub4_sub1_sub2_7.displayName == null
						|| !class30_sub2_sub4_sub1_sub2_7.displayName.equalsIgnoreCase(s7))
						continue;
//					doWalkTo(2, localPlayer.smallX[0], localPlayer.smallY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_7.smallY[0],
//							false, class30_sub2_sub4_sub1_sub2_7.smallX[0]);
					if (opcode == 484) {
						stream.createFrame(39);
						stream.method431(playerIndices[j3]);
					}
					if (opcode == 6 || opcode == 9501) {
						anInt1188 += id;
						if (anInt1188 >= 90) {
							stream.createFrame(136);
							anInt1188 = 0;
						}
						stream.createFrame(128);
						stream.writeWord(playerIndices[j3]);
					}
					flag9 = true;
					break;
				}

				if (!flag9)
					pushMessage("Unable to find " + s7, 0, "");
			}
		}
		if (opcode == 870) {
			stream.createFrame(53);
			stream.writeWord(param0);
			stream.method432(anInt1283);
			stream.method433(id);
			stream.writeWord(anInt1284);
			stream.method431(anInt1285);
			stream.writeWord(param1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 847) {
			stream.createFrame(87);
			stream.method432(id);
			stream.writeWord(param1);
			stream.method432(param0);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 626) {
			RSInterface class9_1 = RSInterface.interfaceCache[param1];
			spellSelected = 1;
			spellID = class9_1.id;
			anInt1137 = param1;
			spellUsableOn = class9_1.spellUsableOn;
			System.out.println("Spell id=" + spellID);
			itemSelected = 0;
			needDrawTabArea = true;
			spellID = class9_1.id;
			String s4 = class9_1.selectedActionName;
			if (s4.indexOf(" ") != -1)
				s4 = s4.substring(0, s4.indexOf(" "));
			String s8 = class9_1.selectedActionName;
			if (s8.indexOf(" ") != -1)
				s8 = s8.substring(s8.indexOf(" ") + 1);
			spellTooltip = s4 + " " + class9_1.spellName + " " + s8;
			// class9_1.sprite1.drawSprite(class9_1.anInt263, class9_1.anInt265,
			// 0xffffff);
			// class9_1.sprite1.drawSprite(200,200);
			// System.out.println("Sprite: " + class9_1.sprite1.toString());
			if (spellUsableOn == 16) {
				needDrawTabArea = true;
				tabID = 3;
				tabAreaAltered = true;
			}
			return;
		}
		if (opcode == 104) {
			RSInterface class9_1 = RSInterface.interfaceCache[param1];
			spellID = class9_1.id;
			if (!autocast) {
				autocast = true;
				autocastId = class9_1.id;
				stream.createFrame(185);
				stream.writeWord(class9_1.id);
			} else if (autocastId == class9_1.id) {
				autocast = false;
				autocastId = 0;
				stream.createFrame(185);
				stream.writeWord(6666); // reset server side
			} else if (autocastId != class9_1.id) {
				autocast = true;
				autocastId = class9_1.id;
				stream.createFrame(185);
				stream.writeWord(class9_1.id);
			}
		}
		if (opcode == 78) {
			stream.createFrame(117);
			stream.writeWord(param1);
			stream.writeWord(id);
			stream.method431(param0);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 27) {
			Player class30_sub2_sub4_sub1_sub2_2 = players[id];
			if (class30_sub2_sub4_sub1_sub2_2 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_2.pathY[0],
					false, class30_sub2_sub4_sub1_sub2_2.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt986 += id;
				if (anInt986 >= 54) {
					stream.createFrame(189);
					stream.writeUnsignedByte(234);
					anInt986 = 0;
				}
				stream.createFrame(73);
				stream.method431(id);
			}
		}
		if (opcode == 213) {
			boolean flag3 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, param1, false, param0);
			if (!flag3)
				flag3 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, param1, false, param0);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(79);
			stream.method431(param1 + baseY);
			stream.writeWord(id);
			stream.method432(param0 + baseX);
		}
		if (opcode == 632) {
			stream.createFrame(145);
			stream.method432(param1);
			stream.method432(param0);
			stream.method432(id);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 1050) {
			if (!runClicked) {
				runClicked = true;
				stream.createFrame(185);
				stream.writeWord(152);
			} else {
				runClicked = false;
				stream.createFrame(185);
				stream.writeWord(152);
			}
		}
		if (opcode == 1004) {
			if (tabInterfaceIDs[10] != -1) {
				needDrawTabArea = true;
				setSidebarInterface(14, 2449);
				tabID = 10;
				tabAreaAltered = true;
			}
		}
		if (opcode == 1003) {
			clanChatMode = 2;
			inputTaken = true;
		}
		if (opcode == 1002) {
			clanChatMode = 1;
			inputTaken = true;
		}
		if (opcode == 1001) {
			clanChatMode = 0;
			inputTaken = true;
		}
		if (opcode == 1000) {
			// chatButtonClickPosition = 4;
			chatTypeView = 11;
			inputTaken = true;
		}
		if (opcode == 999) {
			// chatButtonClickPosition = 0;
			chatTypeView = 0;
			inputTaken = true;
		}
		if (opcode == 998) {
			// chatButtonClickPosition = 1;
			chatTypeView = 5;
			inputTaken = true;
		}
		if (opcode == 1005) {
			// chatButtonClickPosition = 1;
			chatTypeView = 12;
			inputTaken = true;
		}
		if (opcode == 997) {
			publicChatMode = 3;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 996) {
			publicChatMode = 2;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 995) {
			publicChatMode = 1;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 994) {
			publicChatMode = 0;
			inputTaken = true;
		}
		if (opcode == 993) {
			// chatButtonClickPosition = 2;
			chatTypeView = 1;
			inputTaken = true;
		}
		if (opcode == 1006) {
			for (int index = 0; index < chatMessages.length; index++) {
				if (chatMessages[index] != null) {
					int type = chatTypes[index];
					if (type == 3 || type == 7 || type == 5 || type == 6) {
						chatMessages[index] = null;
						chatTypes[index] = 0;
						chatNames[index] = null;
					}
				}
			}
			inputTaken = true;
		}
		if (opcode == 992) {
			privateChatMode = 2;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 991) {
			privateChatMode = 1;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 990) {
			privateChatMode = 0;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 989) {
			// chatButtonClickPosition = 3;
			chatTypeView = 2;
			inputTaken = true;
		}
		if (opcode == 987) {
			tradeMode = 2;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 986) {
			tradeMode = 1;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 985) {
			tradeMode = 0;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (opcode == 984) {
			// chatButtonClickPosition = 5;
			chatTypeView = 3;
			inputTaken = true;
		}
		if (opcode == 983) {
			duelMode = 2;
			inputTaken = true;
		}
		if (opcode == 982) {
			duelMode = 1;
			inputTaken = true;
		}
		if (opcode == 981) {
			duelMode = 0;
			inputTaken = true;
		}




		if (opcode == 980) {
			// chatButtonClickPosition = 6;
			chatTypeView = 4;
			inputTaken = true;
		}
		if (opcode == 493) {
			stream.createFrame(75);
			stream.method433(param1);
			stream.method431(param0);
			stream.writeWord(id);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 652) {
			boolean flag4 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, param1, false, param0);
			if (!flag4)
				flag4 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, param1, false, param0);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(156);
			stream.method432(param0 + baseX);
			stream.method431(param1 + baseY);
			stream.method433(id);
		}
		if (opcode == 94) {
			boolean flag5 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, param1, false, param0);
			if (!flag5)
				flag5 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, param1, false, param0);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(181);
			stream.method431(param1 + baseY);
			stream.writeWord(id);
			stream.method431(param0 + baseX);
			stream.method432(anInt1137);
		}
		// clan chat
		if (opcode == 647) {
			stream.createFrame(213);
			stream.writeWord(param1);
			stream.writeWord(param0);
			switch (param1) {
				case 18304:
					if (param0 == 0) {
						inputTaken = true;
						inputDialogState = 0;
						messagePromptRaised = true;
						promptInput = "";
						friendsListAction = 8;
						aString1121 = "Enter your clan chat title";
					}
					break;
			}
		}
		switch (param1) {
			case 48612:
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 12;
				aString1121 = "Enter the name of the item you want to lookup";
				break;
			case 48615:
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 13;
				aString1121 = "Enter the name of the player you want to lookup";
				break;


			case 41504:
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 15;
				aString1121 = "Enter the name of the player you want to lookup";
				break;


			case 43720://staff tab
			case 43721://staff tab
			case 43722://staff tab
			case 43723://staff tab
			case 43724://staff tab
			case 43725://staff tab
			case 43726://staff tab
			case 43727://staff tab
			case 43728://staff tab
			case 43729://staff tab
			case 43730://staff tab // SHIT WAY BUT WORKS I GUESS
			case 43731://staff tab
			case 43732://staff tab
			case 43733://staff tab
			case 43734://staff tab
			case 43735://staff tab
			case 43736://staff tab
			case 43737://staff tab
			case 43738://staff tab
			case 43739://staff tab
			case 43740://staff tab
			case 43741://staff tab
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 16;
				aString1121 = "Enter the name of the player you want to punish";
				break;



		}
		if (opcode == 646) {
			Bank.handleButton(param1);
			stream.createFrame(185);
			stream.writeWord(param1);
			if (!clickConfigButton(param1)) {
				RSInterface class9_2 = RSInterface.interfaceCache[param1];
				if (!class9_2.ignoreConfigClicking) {
					if (class9_2.scripts != null && class9_2.scripts[0][0] == 5) {
						int i2 = class9_2.scripts[0][1];
						if (variousSettings[i2] != class9_2.anIntArray212[0]) {
							variousSettings[i2] = class9_2.anIntArray212[0];
							updateVarp(i2);
							needDrawTabArea = true;
						}

						//System.out.println(class9_2.id + ", " + i2 + ", " + variousSettings[i2] + ", " + class9_2.anIntArray212[0]);
					}
					switch (param1) {
						// clan chat
						case 18129:
							if (RSInterface.interfaceCache[18135].message.toLowerCase().contains("join")) {
								inputTaken = true;
								inputDialogState = 0;
								messagePromptRaised = true;
								promptInput = "";
								friendsListAction = 6;
								aString1121 = "Enter the name of the chat you wish to join";
							} else {
								sendString(0, "");
							}
							break;

						case 18132:
							openInterfaceID = 18300;
							break;

						case 18527:
							inputTaken = true;
							inputDialogState = 0;
							messagePromptRaised = true;
							promptInput = "";
							friendsListAction = 9;
							aString1121 = "Enter a name to add";
							break;

						case 18528:
							inputTaken = true;
							inputDialogState = 0;
							messagePromptRaised = true;
							promptInput = "";
							friendsListAction = 10;
							aString1121 = "Enter a name to add";
							break;
					}
				}
			}
		}
		if (opcode == 225) {
			Npc class30_sub2_sub4_sub1_sub1_2 = npcs[id];
			if (class30_sub2_sub4_sub1_sub1_2 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_2.pathY[0],
					false, class30_sub2_sub4_sub1_sub1_2.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1226 += id;
				if (anInt1226 >= 85) {
					stream.createFrame(230);
					stream.writeUnsignedByte(239);
					anInt1226 = 0;
				}
				stream.createFrame(17);
				stream.method433(id);
			}
		}
		if (opcode == 965) {
			Npc class30_sub2_sub4_sub1_sub1_3 = npcs[id];
			if (class30_sub2_sub4_sub1_sub1_3 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_3.pathY[0],
					false, class30_sub2_sub4_sub1_sub1_3.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1134++;
				if (anInt1134 >= 96) {
					stream.createFrame(152);
					stream.writeUnsignedByte(88);
					anInt1134 = 0;
				}
				stream.createFrame(21);
				stream.writeWord(id);
			}
		}
		if (opcode == 413) {
			Npc class30_sub2_sub4_sub1_sub1_4 = npcs[id];
			if (class30_sub2_sub4_sub1_sub1_4 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_4.pathY[0],
					false, class30_sub2_sub4_sub1_sub1_4.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(131);
				stream.method433(id);
				stream.method432(anInt1137);
			}
		}
		/*
		 * if (opcode == 200) clearTopInterfaces();
		 */
		if (opcode == 200) {
			// Trading post interface won't close automatically, not sure why
			if (openInterfaceID != 48599 && openInterfaceID != 48598 && openInterfaceID != 48600)
				clearTopInterfaces();
			else {
				stream.createFrame(185);
				stream.writeWord(15333);
			}
		}

		if (opcode == 201) {
			for (int index = 0; index < GrandExchange.grandExchangeItemBoxIds.length; index++)
				if (param1 == GrandExchange.grandExchangeItemBoxIds[index] + 1)
					openInterfaceID = GrandExchange.grandExchangeOfferStatusInterfaceIds[index];

			switch (param1) {

				case 25705:
				case 25557:
					openInterfaceID = 25000;
					break;

			}
		}

		if (opcode == 1025) {
			Npc class30_sub2_sub4_sub1_sub1_5 = npcs[id];
			if (class30_sub2_sub4_sub1_sub1_5 != null) {
				NpcDefinition entityDef = class30_sub2_sub4_sub1_sub1_5.desc;
				if (entityDef != null) {
					if (entityDef.transforms != null)
						entityDef = entityDef.morph();
					if (entityDef != null) {
						String s9 = null;
						if (entityDef.description != null)
							s9 = new String(entityDef.description);
						else if (entityDef.combatLevel == 0)
							s9 = "It's a " + entityDef.name + ".";
						if (s9 != null) {
							pushMessage(s9, 0, "");
						}
					}

					if (entityDef.combatLevel > 0) {
						stream.createFrame(137);
						stream.writeWord((int) id);
					}
				}
			}
		}
		if (opcode == 900) {
			clickObject(id, param1, param0);
			stream.createFrame(252);
			stream.writeDWord(id);
			stream.method431(param1 + baseY);
			stream.method432(param0 + baseX);
		}
		if (opcode == 412) {
			Npc class30_sub2_sub4_sub1_sub1_6 = npcs[id];
			if (class30_sub2_sub4_sub1_sub1_6 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_6.pathY[0],
					false, class30_sub2_sub4_sub1_sub1_6.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(72);
				stream.method432(id);
			}
		}
		if (opcode == 365) {
			Player class30_sub2_sub4_sub1_sub2_3 = players[id];
			if (class30_sub2_sub4_sub1_sub2_3 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_3.pathY[0],
					false, class30_sub2_sub4_sub1_sub2_3.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(249);
				stream.method432(id);
				stream.method431(anInt1137);
			}
		}
		if (opcode == 729) {
			Player class30_sub2_sub4_sub1_sub2_4 = players[id];
			if (class30_sub2_sub4_sub1_sub2_4 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_4.pathY[0],
					false, class30_sub2_sub4_sub1_sub2_4.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(39);
				stream.method431(id);
			}
		}
		if (opcode == 577) {
			Player class30_sub2_sub4_sub1_sub2_5 = players[id];
			if (class30_sub2_sub4_sub1_sub2_5 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_5.pathY[0],
					false, class30_sub2_sub4_sub1_sub2_5.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(139);
				stream.method431(id);
			}
		}
		if (opcode == 956 && clickObject(id, param1, param0)) {
			stream.createFrame(35);
			stream.method431(param0 + baseX);
			stream.method432(anInt1137);
			stream.method432(param1 + baseY);
			stream.writeDWord(id);
		}
		if (opcode == 567) {
			boolean flag6 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, param1, false, param0);
			if (!flag6)
				flag6 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, param1, false, param0);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(23);
			stream.method431(param1 + baseY);
			stream.method431(id);
			stream.method431(param0 + baseX);
		}
		if (opcode == 867) {
			if ((id & 3) == 0)
				anInt1175++;
			if (anInt1175 >= 59) {
				stream.createFrame(200);
				stream.createFrame(201);
				stream.writeWord(25501);
				anInt1175 = 0;
			}
			stream.createFrame(43);
			stream.method431(param1);
			stream.method432(id);
			stream.method432(param0);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 543) {
			stream.createFrame(237);
			stream.writeWord(param0);
			stream.method432(id);
			stream.writeWord(param1);
			stream.method432(anInt1137);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 606) {
			String s2 = menuActions[menuRow];
			int j2 = s2.indexOf("@whi@");
			if (j2 != -1)
				if (openInterfaceID == -1) {
					clearTopInterfaces();
					reportAbuseInput = s2.substring(j2 + 5).trim();
					canMute = false;
					for (int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++) {
						if (RSInterface.interfaceCache[i3] == null || RSInterface.interfaceCache[i3].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i3].parentID;
						break;
					}

				} else {
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
		}
		if (opcode == 491) {
			Player class30_sub2_sub4_sub1_sub2_6 = players[id];
			if (class30_sub2_sub4_sub1_sub2_6 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_6.pathY[0],
					false, class30_sub2_sub4_sub1_sub2_6.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(14);
				stream.method432(anInt1284);
				stream.writeWord(id);
				stream.writeWord(anInt1285);
				stream.method431(anInt1283);
			}
		}
		if (opcode == 639) {
			String s3 = menuActions[menuRow];
			int k2 = s3.indexOf("@whi@");
			if (k2 != -1) {
				long l4 = StringUtils.longForName(s3.substring(k2 + 5).trim());
				int k3 = -1;
				for (int i4 = 0; i4 < friendsCount; i4++) {
					if (friendsListAsLongs[i4] != l4)
						continue;
					k3 = i4;
					break;
				}

				if (k3 != -1 && friendsNodeIDs[k3] > 0) {
					System.out.println("SEND mSH");
					inputTaken = true;
					inputDialogState = 0;
					messagePromptRaised = true;
					promptInput = "";
					friendsListAction = 3;
					aLong953 = friendsListAsLongs[k3];
					aString1121 = "Enter message to send to " + friendsList[k3];
				}
			}
		}
		if (opcode == 1052) {
			stream.createFrame(185);
			stream.writeWord(154);
		}

		if (opcode == 1004) {
			if (tabInterfaceIDs[14] != -1) {
				needDrawTabArea = true;
				tabID = 14;
				tabAreaAltered = true;
			}
		}

		if (opcode == 454) {
			stream.createFrame(41);
			stream.writeWord(id);
			stream.method432(param0);
			stream.method432(param1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = param1;
			atInventoryIndex = param0;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[param1].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[param1].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (opcode == 696) {
			camAngleY = 0;
			camAngleX = 120;

		}
		if (opcode == 478) {
			Npc class30_sub2_sub4_sub1_sub1_7 = npcs[id];
			if (class30_sub2_sub4_sub1_sub1_7 != null) {
				doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_7.pathY[0],
					false, class30_sub2_sub4_sub1_sub1_7.pathX[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				if ((id & 3) == 0)
					anInt1155++;
				if (anInt1155 >= 53) {
					stream.createFrame(85);
					stream.writeUnsignedByte(66);
					anInt1155 = 0;
				}
				stream.createFrame(18);
				stream.method431(id);
			}
		}
		if (opcode == 113) {
			clickObject(id, param1, param0);
			stream.createFrame(70);
			stream.method431(param0 + baseX);
			stream.writeWord(param1 + baseY);
			stream.writeDWord(id);
		}
		if (opcode == 872) {
			clickObject(id, param1, param0);
			stream.createFrame(234);
			stream.method433(param0 + baseX);
			stream.writeDWord(id);
			stream.method433(param1 + baseY);
		}
		if (opcode == 502) {
			clickObject(id, param1, param0);
			stream.createFrame(132);
			stream.method433(param0 + baseX);
			stream.writeDWord(id);
			stream.method432(param1 + baseY);
		}
		if (opcode == 1126 || opcode == 1125) {
			RSInterface class9_4 = RSInterface.interfaceCache[param1];
			if (class9_4 != null) {
				stream.createFrame(134);
				stream.writeDWord(id);
				stream.writeDWord(class9_4.inventoryAmounts[param0]);
			}
		}
		if (opcode == 1130) {
			RSInterface class9_4 = RSInterface.interfaceCache[param1];
			if (class9_4 != null) {
				class9_4.itemSearchSelectedId = class9_4.inventoryItemId[param0];
				class9_4.itemSearchSelectedSlot = param0;
				RSInterface.selectedItemInterfaceId = class9_4.id;
			}
		}
		if (opcode == 169) {
			if (param1 == 58002) {
				anInt913 = 0;
				variousSettings[304] = 1;
				variousSettings[305] = 0;
			} else if (param1 == 18929) {
				anInt913 = 1;
				variousSettings[304] = 0;
				variousSettings[305] = 1;
			} else {
				RSInterface button = RSInterface.get(param1);
				if (button.newButtonClicking) {
					stream.createFrame(184);
					stream.writeWord(param1);
					onRealButtonClick(param1);
				} else {
					stream.createFrame(185);
					stream.writeWord(param1);
					if (button.buttonListener != null)
						button.buttonListener.accept(param1);
					if (clientData) {
						pushMessage("Client side button id: " + param1);
					}
				}

				RSInterface class9_3 = RSInterface.interfaceCache[param1];
				if (!class9_3.ignoreConfigClicking) {
					if (class9_3.scripts != null && class9_3.scripts[0][0] == 5) {
						int l2 = class9_3.scripts[0][1];
						variousSettings[l2] = 1 - variousSettings[l2];
						updateVarp(l2);
						needDrawTabArea = true;
					}
				}
			}
		}
		if (opcode == 447) {
			itemSelected = 1;
			anInt1283 = param0;
			anInt1284 = param1;
			anInt1285 = id;
			selectedItemName = ItemDefinition.lookup(id).name;
			spellSelected = 0;
			needDrawTabArea = true;
			return;
		}
		if (opcode == 1226) {
			int j1 = id >> 14 & 0x7fff;
			ObjectDefinition class46 = ObjectDefinition.lookup(j1);
			String s10;
			if (class46.description != null)
				s10 = new String(class46.description);
			else
				s10 = "It's a " + class46.name + ".";
			pushMessage(s10, 0, "");
		}
		if (opcode == 244) {
			boolean flag7 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, param1, false, param0);
			if (!flag7)
				flag7 = doWalkTo(2, localPlayer.pathX[0], localPlayer.pathY[0], 0, 1, 0, 1, 0, param1, false, param0);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(253);
			stream.method431(param0 + baseX);
			stream.method433(param1 + baseY);
			stream.method432(id);
		}
		if (opcode == 1448) {
			ItemDefinition itemDef_1 = ItemDefinition.lookup(id);
			String s6;
			s6 = "It's a " + itemDef_1.name + ".";
			pushMessage(s6, 0, "");
		}
		itemSelected = 0;
		spellSelected = 0;
		needDrawTabArea = true;

	}

	public static boolean removeRoofs = true, leftClickAttack = true, chatEffectsEnabled = true, drawOrbs = true;

	private void setConfigButtonsAtStartup() {
		Preferences.getPreferences().updateClientConfiguration();
		setConfigButton(23101, drawOrbs);
		setConfigButton(23109, splitPrivateChat);
		setConfigButton(23107, chatEffectsEnabled);
		setConfigButton(23103, informationFile.isRememberRoof() ? true : false);
		setConfigButton(23105, leftClickAttack);
		setConfigButton(23111, getUserSettings().isGameTimers());
		setConfigButton(23113, getUserSettings().isShowEntityTarget());
		setConfigButton(23115, informationFile.isRememberVisibleItemNames() ? true : false);
		setConfigButton(23118, shiftDrop ? true : false);
		setConfigButton(23001, true);
		setConfigButton(953, true);

		setDropDown(SettingsInterface.OLD_GAMEFRAME, getUserSettings().isOldGameframe());
		setDropDown(SettingsInterface.GAME_TIMERS, getUserSettings().isGameTimers());
		setDropDown(SettingsInterface.ANTI_ALIASING, getUserSettings().isAntiAliasing());
		setDropDown(SettingsInterface.GROUND_ITEM_NAMES, getUserSettings().isGroundItemOverlay());
		setDropDown(SettingsInterface.FOG, getUserSettings().isFog());
		setDropDown(SettingsInterface.SMOOTH_SHADING, getUserSettings().isSmoothShading());
		setDropDown(SettingsInterface.TILE_BLENDING, getUserSettings().isTileBlending());
		setDropDown(SettingsInterface.BOUNTY_HUNTER, getUserSettings().isBountyHunter());
		setDropDown(SettingsInterface.ENTITY_TARGET, getUserSettings().isShowEntityTarget());
		setDropDown(SettingsInterface.CHAT_EFFECT, getUserSettings().getChatColor());
		setDropDown(SettingsInterface.INVENTORY_MENU, getUserSettings().isInventoryContextMenu() ? 1 : 0);
		setDropDown(SettingsInterface.STRETCHED_MODE, getUserSettings().isStretchedMode());

		setDropDown(SettingsInterface.PM_NOTIFICATION, Preferences.getPreferences().pmNotifications ? 0 : 1);

		/** Sets the brightness level **/
		RSInterface class9_2 = RSInterface.interfaceCache[910];
		if (class9_2.scripts != null && class9_2.scripts[0][0] == 5) {
			int i2 = class9_2.scripts[0][1];
			if (variousSettings[i2] != class9_2.anIntArray212[0]) {
				variousSettings[i2] = class9_2.anIntArray212[0];
				updateVarp(i2);
				needDrawTabArea = true;
			}
		}
	}

	private boolean clickConfigButton(int i) {
		switch (i) {

			case 953:
				if (tabInterfaceIDs[11] != 23000) {
					tabInterfaceIDs[11] = 23000;
					setConfigButton(i, true);
					setConfigButton(959, false);
				}
				return true;

			case 959:
				if (tabInterfaceIDs[11] != 23100) {
					tabInterfaceIDs[11] = 23100;
					setConfigButton(i, true);
					setConfigButton(953, false);
				}
				return true;

			case 42502:
				setConfigButton(42502, true);
				setConfigButton(42503, false);
				setConfigButton(42504, false);
				setConfigButton(42505, false);
				break;
			case 42503:
				setConfigButton(42502, false);
				setConfigButton(42503, true);
				setConfigButton(42504, false);
				setConfigButton(42505, false);
				break;

			case 166026:
				//if (isResized()) {
				//	setConfigButton(i, true);
				//	setConfigButton(23003, false);
				//	setConfigButton(23005, false);
				//	setGameMode(ScreenMode.FIXED);
				//}

				return true;

			case 166027:
				//if (currentScreenMode != ScFreenMode.RESIZABLE) {
				//setConfigButton(i, true);
				//setConfigButton(23001, false);
				//setConfigButton(23005, false);
				//setGameMode(ScreenMode.RESIZABLE);
				//}
				pushMessage("Coming real soon",  0, "");
				return false;

			case 23005:
				//if (currentScreenMode != ScreenMode.FULLSCREEN) {
				//	setConfigButton(i, true);
				//	setConfigButton(23001, false);
				//	setConfigButton(23003, false);
				//	setGameMode(ScreenMode.FULLSCREEN);
				//}
				pushMessage("Coming real soon",  0, "");
				return true;

			case 23101:
				setConfigButton(i, drawOrbs = !drawOrbs);
				return true;

			case 23109:
				setConfigButton(i, splitPrivateChat = !splitPrivateChat);
				return true;

			case 23103:
				setConfigButton(i, removeRoofs = !removeRoofs);
				informationFile.setRememberRoof(informationFile.isRememberRoof() ? false : true);
				try {
					informationFile.write();
					System.out.println("Written to: " + informationFile.isRememberRoof());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case 23105:
				setConfigButton(i, leftClickAttack = !leftClickAttack);
				return true;

			case 23107:
				setConfigButton(i, chatEffectsEnabled = !chatEffectsEnabled);
				return true;

			case 23111:
				//setConfigButton(i, getUserSettings().setGameTimers(!getUserSettings().isGameTimers());
				return true;

			case 23113:
				//setConfigButton(i, showEntityTarget = !showEntityTarget);
				return true;

			case 23118:
				setConfigButton(i, shiftDrop = !shiftDrop);
				return true;

			case 23115:
				//setConfigButton(i, groundItemsOn = !groundItemsOn);
				informationFile.setRememberRoof(informationFile.isRememberVisibleItemNames() ? false : true);
				try {
					informationFile.write();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

		}

		return false;
	}

	private void setDropDown(Setting setting, Object value) {
		RSInterface widget = RSInterface.interfaceCache[setting.getInterfaceId()];
		if (widget == null) {
			throw new NullPointerException("Widget is null: " + setting.getInterfaceId());
		}
		if (widget.dropdown == null) {
			throw new NullPointerException("Widget dropdown is null: " + setting.getInterfaceId());
		}
		if (widget.dropdown.getOptions() == null) {
			throw new NullPointerException("Widget dropdown options is null: " + setting.getInterfaceId());
		}
		if (value instanceof Boolean) {
			boolean choice = (Boolean) value;
			widget.dropdown.setSelected(widget.dropdown.getOptions()[choice ? 0 : 1]);
		} else if (value instanceof Integer) {
			int choice = (int) value;
			widget.dropdown.setSelected(widget.dropdown.getOptions()[choice]);
		}
	}

	private void setConfigButton(int interfaceFrame, boolean configSetting) {
		int config = configSetting ? 1 : 0;
		anIntArray1045[interfaceFrame] = config;
		if (variousSettings[interfaceFrame] != config) {
			variousSettings[interfaceFrame] = config;
			needDrawTabArea = true;
			if (dialogID != -1)
				inputTaken = true;
		}
	}

	public void method70() {
		anInt1251 = 0;
		int j = (localPlayer.x >> 7) + baseX;
		int k = (localPlayer.y >> 7) + baseY;
		if (j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136)
			anInt1251 = 1;
		if (j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535)
			anInt1251 = 1;
		if (anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062)
			anInt1251 = 0;
	}

	@Override
	public void run() {
		if (drawFlames) {
			drawFlames();
		} else {
			try {
				super.run();
			} catch (OutOfMemoryError e) {
				//ClientWindow.popupMessage("An error has occurred that caused the game to crash.",
						//"Contact us in the Discord #support channel for help.");
				Misc.dumpHeap(true);
				e.printStackTrace();
				throw e;
			}
		}
	}

	public void insertMenuItemNoShift(String action, String target, int opcode, int identifier, int arg1, int arg2) {
		insertMenuItem(action, target, opcode, identifier, arg1, arg2, -1, false);
	}

	public void insertMenuItemNoShift(String action, int opcode, int identifier, int arg1, int arg2) {
		insertMenuItemNoShift(action, "", opcode, identifier, arg1, arg2);
	}

	public void insertMenuItemNoShift(String action, MenuAction opcode, int identifier, int arg1, int arg2) {
		insertMenuItemNoShift(action, "", opcode.getId(), identifier, arg1, arg2);
	}

	public void insertMenuItemNoShift(String action, int opcode, int identifier, int arg1) {
		insertMenuItemNoShift(action, opcode, identifier, arg1, -1);
	}

	public void insertMenuItemNoShift(String action, int opcode, int identifier) {
		insertMenuItemNoShift(action, opcode, identifier, -1);
	}

	public void insertMenuItemNoShift(String action, MenuAction opcode) {
		insertMenuItemNoShift(action, opcode.getId(), -1);
	}

	public void insertMenuItemNoShift(String action, int opcode) {
		insertMenuItemNoShift(action, opcode, -1);
	}

	public void insertMenuItemNoShift(String action) {
		insertMenuItemNoShift(action, -1);
	}

	public final void insertMenuItem(String action,
									 String target,
									 int opcode,
									 int identifier,
									 int arg1,
									 int arg2,
									 int unknown,
									 boolean shiftClick) {
		if (!isMenuOpen) {
			if (menuOptionsCount < 500) {
				menuActions[menuOptionsCount] = action;
				menuTargets[menuOptionsCount] = target;
				menuOpcodes[menuOptionsCount] = opcode;
				menuIdentifiers[menuOptionsCount] = identifier;
				menuArguments1[menuOptionsCount] = arg1;
				menuArguments2[menuOptionsCount] = arg2;
				menuShiftClick[menuOptionsCount] = shiftClick;
				++menuOptionsCount;
				onMenuOptionsChanged(0);
			}
		}
	}

	public void createMenu() {
		if (itemSelected == 0 && spellSelected == 0) {
			insertMenuItemNoShift((localPlayer.isAdminRights() && controlIsDown) ? "Teleport here" : "Walk here", MenuAction.WALK, -1, MouseHandler.mouseX, MouseHandler.mouseY);
		}
		long previous = -1;
		for (int cached = 0; cached < ViewportMouse.method1116(); cached++) {
			long current = ViewportMouse.entityTags[cached];
			int x = ViewportMouse.method7552(ViewportMouse.entityTags[cached]);
			int y = ViewportMouse.method134(cached);
			long var19 = ViewportMouse.entityTags[cached];
			int opcode = (int) (var19 >>> 14 & 3L);
			int uid = ViewportMouse.Entity_unpackID(ViewportMouse.entityTags[cached]);
			if (current == previous) {
				continue;
			}

			previous = current;
			if (opcode == 2 && scene.getObjectFlags(plane, x, y, previous) >= 0) {
				ObjectDefinition objectDef = ObjectDefinition.lookup(uid);
				if (objectDef.configs != null) {
					objectDef = objectDef.method580();
				}
				if (objectDef == null) {
					continue;
				}

				if (itemSelected == 1) {
					insertMenuItemNoShift("Use " + selectedItemName + " with @cya@" + objectDef.name, MenuAction.ITEM_USE_ON_GAME_OBJECT,ObjectKeyUtil.getObjectId(current), x, y);
				} else if (spellSelected == 1) {
					if ((spellUsableOn & 4) == 4) {
						insertMenuItemNoShift(spellTooltip + " @cya@" + objectDef.name, MenuAction.SPELL_CAST_ON_GAME_OBJECT,ObjectKeyUtil.getObjectId(current), x, y);
					}
				} else {
					if (objectDef.actions != null) {
						for (int type = 4; type >= 0; type--)
							if (objectDef.actions[type] != null) {
								int type0 = 0;
								if (type == 0) {
									type0 = 502;
								}
								if (type == 1) {
									type0 = 900;
								}
								if (type == 2) {
									type0 = 113;
								}
								if (type == 3) {
									type0 = 872;
								}
								if (type == 4) {
									type0 = 1062;
								}
								MenuAction action = MenuAction.of(type0);
								insertMenuItemNoShift(objectDef.actions[type] + " @cya@" + objectDef.name, action, ObjectKeyUtil.getObjectId(current), x, y);

							}

					}

					final String examineText = debugModels ? "Examine @cya@" + objectDef.name
							+ " (ID: " + ObjectKeyUtil.getObjectId(current)
							+ ", Models: " + Arrays.toString(objectDef.getModelIds()) + ")"
							: "Examine @cya@" + objectDef.name;
					insertMenuItemNoShift(examineText, MenuAction.EXAMINE_OBJECT, ObjectKeyUtil.getObjectId(current), x, y);
				}
			}
			if (opcode == 1) {
				Npc npc = npcs[uid];
				if (npc.desc.size == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
					for (int j2 = 0; j2 < npcCount; j2++) {
						Npc npc2 = npcs[npcIndices[j2]];
						if (npc2 != null && npc2 != npc && npc2.desc.size == 1 && npc2.x == npc.x && npc2.y == npc.y && npc2.isShowMenuOnHover()) {
							buildAtNPCMenu(npc2.desc, npcIndices[j2], y, x);
						}
					}

					for (int l2 = 0; l2 < playerCount; l2++) {
						Player player = players[playerIndices[l2]];
						if (player != null && player.x == npc.x && player.y == npc.y) {
							buildAtPlayerMenu(x, playerIndices[l2], player, y);
						}
					}

				}
				if (npc.isShowMenuOnHover()) {
					buildAtNPCMenu(npc.desc, uid, y, x);
				}
			}
			if (opcode == 0) {
				Player player = players[uid];
				if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
					for (int k2 = 0; k2 < npcCount; k2++) {
						Npc class30_sub2_sub4_sub1_sub1_2 = npcs[npcIndices[k2]];
						if (class30_sub2_sub4_sub1_sub1_2 != null && class30_sub2_sub4_sub1_sub1_2.desc.size == 1
								&& class30_sub2_sub4_sub1_sub1_2.x == player.x
								&& class30_sub2_sub4_sub1_sub1_2.y == player.y && class30_sub2_sub4_sub1_sub1_2.isShowMenuOnHover())
							buildAtNPCMenu(class30_sub2_sub4_sub1_sub1_2.desc, npcIndices[k2], y, x);
					}

					for (int i3 = 0; i3 < playerCount; i3++) {
						Player class30_sub2_sub4_sub1_sub2_2 = players[playerIndices[i3]];
						if (class30_sub2_sub4_sub1_sub2_2 != null && class30_sub2_sub4_sub1_sub2_2 != player
								&& class30_sub2_sub4_sub1_sub2_2.x == player.x
								&& class30_sub2_sub4_sub1_sub2_2.y == player.y)
							buildAtPlayerMenu(x, playerIndices[i3], class30_sub2_sub4_sub1_sub2_2, y);
					}

				}
				buildAtPlayerMenu(x, uid, player, y);
			}
			if (opcode == 3) {
				NodeDeque class19 = groundItems[plane][x][y];
				if (class19 != null) {
					for (TileItem item = (TileItem) class19.getFirst(); item != null; item = (TileItem) class19.getNext()) {
						ItemDefinition itemDef = ItemDefinition.lookup(item.id);
						if (itemSelected == 1) {
							insertMenuItemNoShift("Use", selectedItemName + " " + "->" + " " + colorStartTag(16748608) + itemDef.name, 16, item.id, x, y);
						} else if (spellSelected == 1) {
							if ((spellUsableOn & 1) == 1) {
								insertMenuItemNoShift("", spellTooltip + " " + "->" + " " + colorStartTag(16748608) + itemDef.name, 17, item.id, x, y);
							}
						} else {
							String[] var24 = itemDef.groundActions;

							for (int var34 = 4; var34 >= 0; --var34) {
								if (item.method622(var34)) {
									if (var24 != null && var24[var34] != null) {
										int var21 = 0;
										if (var34 == 0) {
											var21 = 652;
										}

										if (var34 == 1) {
											var21 = 567;
										}

										if (var34 == 2) {
											var21 = 234;
										}

										if (var34 == 3) {
											var21 = 244;
										}

										if (var34 == 4) {
											var21 = 213;
										}

										insertMenuItemNoShift(var24[var34], colorStartTag(16748608) + itemDef.name, var21, itemDef.id, x, y);
									} else if (var34 == 2) {
										insertMenuItemNoShift("Take", colorStartTag(16748608) + itemDef.name, 20, itemDef.id, x, y);
									}
								}
							}
							insertMenuItemNoShift("Examine @lre@" + itemDef.name,MenuAction.EXAMINE_ITEM_GROUND,item.id,x,y);
						}
					}
				}
			}
		}
	}

	@Override
	public void cleanUpForQuit() {
		if (StaticSound.pcmPlayer0 != null) {
			StaticSound.pcmPlayer0.shutdown();
		}
		if (StaticSound.pcmPlayer1 != null) {
			StaticSound.pcmPlayer1.shutdown();
		}
		Signlink.reporterror = false;
		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		ArchiveDiskActionHandler.waitForPendingArchiveDiskActions();
		try {
			Signlink.cacheData.close();

			for (int var3 = 0; var3 < Signlink.archiveCount; ++var3) {
				Signlink.cacheIndexes[var3].close();
			}

			Signlink.cacheMasterIndex.close();
			Signlink.uid.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		socketStream = null;

		mapBack = null;
		cacheSprite = null;
		cacheSprite1 = null;
		cacheSprite2 = null;
		cacheSprite3 = null;

		cacheInterface = null;
		mouseDetection = null;

		mapIcon7 = null;
		mapIcon8 = null;
		mapIcon6 = null;
		mapIcon5 = null;
		mapIcon9 = null;

		aStream_834 = null;

		stream = null;
		aStream_847 = null;
		inStream = null;
		regions = null;
		regionLandArchives = null;
		regionMapArchives = null;
		regionLandIds = null;
		regionLocIds = null;
		tileHeights = null;
		tileFlags = null;
		scene = null;
		collisionMaps = null;
		anIntArrayArray901 = null;
		travelDistances = null;
		bigX = null;
		bigY = null;
		aByteArray912 = null;

		tabAreaFixed = null;
		tabAreaResizable = null;
		compassImage = null;
		redStones = null;
		sideIcons = null;
		mapArea = null;

		/* Null pointers for custom sprites */
		multiOverlay = null;
		chatArea = null;
		chatButtons = null;
		mapArea = null;
		channelButtons = null;
		venomOrb = null;
		/**/

		hitMarks = null;
		headIcons = null;
		skullIcons = null;
		headIconsHint = null;
		mapDotItem = null;
		mapDotNPC = null;
		mapDotPlayer = null;
		mapDotFriend = null;
		mapFunctions = null;
		tileCycleMap = null;
		mapSceneSprites = null;
		players = null;
		playerIndices = null;
		anIntArray894 = null;
		aStreamArray895s = null;
		anIntArray840 = null;
		npcs = null;
		npcIndices = null;
		groundItems = null;
		spawns = null;
		projectiles = null;
		incompleteAnimables = null;
		menuArguments1 = null;
		menuArguments2 = null;
		menuOpcodes = null;
		menuIdentifiers = null;
		menuTargets = null;
		variousSettings = null;
		minimapHintX = null;
		minimapHintY = null;
		minimapHint = null;
		minimapImage = null;
		friendsList = null;
		friendsListAsLongs = null;
		friendsNodeIDs = null;

		nullLoader();
		ObjectDefinition.nullLoader();
		NpcDefinition.nullLoader();
		ItemDefinition.clear();
		FloorUnderlayDefinition.floorCache.clear();
		FloorOverlayDefinition.floorCache.clear();
		VariablePlayer.clear();
		((TextureProvider) Rasterizer3D.clips.textureLoader).clear();
		Js5List.models.clearFiles();
		Js5List.soundEffects.clearFiles();
		Js5List.textures.clearFiles();
		Js5List.musicTracks.clearFiles();
		Js5List.musicJingles.clearFiles();
		Js5List.sprites.clearFiles();
		Js5List.binary.clearFiles();
		Js5List.dbtableindex.clearFiles();
		Js5List.animations.clearFiles();
		Js5List.skeletons.clearFiles();
		Js5List.clientScript.clearFiles();
		Js5List.maps.clearFiles();
		IdentityKit.cached.clear();
		RSInterface.interfaceCache = null;
		DummyClass.cache = null;
		jagexNetThread.closeSocketStream();
		VariablePlayer.cached.clear();
		Player.mruNodes = null;
		System.gc();
	}

	public void printDebug() {
		System.out.println("============");
		System.out.println("flame-cycle:" + anInt1208);

		System.out.println("loop-cycle:" + loopCycle);
		System.out.println("draw-cycle:" + anInt1061);
		System.out.println("ptype:" + incomingPacket);
		System.out.println("psize:" + packetSize);
		if (socketStream != null)
			socketStream.printDebug();
		//MouseHandler.shouldDebug = true;
	}

	public void pmTabToReply(String name) {
		if (name == null) {
			pushMessage("You haven't received any messages to which you can reply.", 0, "");
			return;
		}
		for(int crown = 0; crown < 50; crown++) {
			String crownString = "@cr" + crown + "@";
			if(name.contains(crownString)) {
				name = name.replaceAll(crownString, "");
			}
		}

		long nameAsLong = StringUtils.longForName(name.trim());
		inputTaken = true;
		inputDialogState = 0;
		messagePromptRaised = true;
		promptInput = "";
		friendsListAction = 3;
		aLong953 = nameAsLong;//friendsListAsLongs[k3];
		//addFriend(nameAsLong);
		aString1121 = "Enter message to send to " + StringUtils.fixName(name.trim());//friendsList[k3];
	}

	public void pmTabToReply() {
		String name = null;
		for (int k = 0; k < 100; k++) {
			if (chatMessages[k] == null) {
				continue;
			}
			int l = chatTypes[k];
			if (l == 3 || l == 7) {
				name = chatNames[k];
				break;
			}
		}
		pmTabToReply(name);
	}

	private void manageTextInputs() {
		do {
			while (true) {
				label1776:
				do {
					while (true) {
						while (keyManager.hasNextKey()) {
							int j = keyManager.lastTypedCharacter;
							if (j == -1 || j == 96)
								break;
							if(devConsole.console_open) {
								devConsole.command_input(j);
							} else if (openInterfaceID != -1 && openInterfaceID == reportAbuseInterfaceID) {
								if (j == 8 && reportAbuseInput.length() > 0)
									reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
								if ((j >= 97 && j <= 122 || j >= 65 && j <= 90 || j >= 48 && j <= 57 || j == 32)
										&& reportAbuseInput.length() < 12)
									reportAbuseInput += (char) j;
							} else if (messagePromptRaised) {
								if (j >= 32 && j <= 122 && promptInput.length() < 80) {
									promptInput += (char) j;
									inputTaken = true;
								}
								if (j == 8 && promptInput.length() > 0) {
									promptInput = promptInput.substring(0, promptInput.length() - 1);
									inputTaken = true;
								}
								if (j == 13 || j == 10) {
									messagePromptRaised = false;
									inputTaken = true;
									if (friendsListAction == 1) {
										long l = StringUtils.longForName(promptInput);
										addFriend(l);
									}
									if (friendsListAction == 2 && friendsCount > 0) {
										long l1 = StringUtils.longForName(promptInput);
										delFriend(l1);
									}

									if (friendsListAction == 557 && promptInput.length() > 0) {//money pouch
										inputString = "::withdrawmp " + promptInput;
										sendPacket(103);
									}


									if (friendsListAction == 3 && promptInput.length() > 0) {
										stream.createFrame(126);
										stream.writeUnsignedByte(0);
										int k = stream.currentPosition;
										stream.writeQWord(aLong953);
										TextInput.method526(promptInput, stream);
										stream.writeBytes(stream.currentPosition - k);
										promptInput = TextInput.processText(promptInput);
										// promptInput = Censor.doCensor(promptInput);
										pushMessage(promptInput, 6, StringUtils.fixName(StringUtils.nameForLong(aLong953)));
										if (privateChatMode == 2) {
											privateChatMode = 1;
											stream.createFrame(95);
											stream.writeUnsignedByte(publicChatMode);
											stream.writeUnsignedByte(privateChatMode);
											stream.writeUnsignedByte(tradeMode);
										}
									}
									if (friendsListAction == 4 && ignoreCount < 100) {
										long l2 = StringUtils.longForName(promptInput);
										addIgnore(l2);
									}
									if (friendsListAction == 5 && ignoreCount > 0) {
										long l3 = StringUtils.longForName(promptInput);
										delIgnore(l3);
									}
									// clan chat
									if (friendsListAction == 6) {
										sendStringAsLong(promptInput);
									} else if (friendsListAction == 8) {
										sendString(1, promptInput);
									} else if (friendsListAction == 9) {
										sendString(2, promptInput);
									} else if (friendsListAction == 10) {
										sendString(3, promptInput);
									} else if (friendsListAction == 12) {
										sendString(5, promptInput);
									} else if (friendsListAction == 13) {
										sendString(6, promptInput);
									} else if (friendsListAction == 14) {//search wiki
										sendString(7, promptInput);
									} else if (friendsListAction == 15) {//player profile
										sendStringAsLong(promptInput);
									} else if (friendsListAction == 16) {//staff tab
										sendStringAsLong(promptInput);
									}

								}
							} else if (inputDialogState == 1) {
								if (j >= 48 && j <= 57 && amountOrNameInput.length() < 10) {
									amountOrNameInput += (char) j;
									inputTaken = true;
								}
								if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m")
										&& !amountOrNameInput.toLowerCase().contains("b")) && (j == 107 || j == 109) || j == 98) {
									amountOrNameInput += (char) j;
									inputTaken = true;
								}
								if (j == 8 && amountOrNameInput.length() > 0) {
									amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
									inputTaken = true;
								}
								if (j == 13 || j == 10) {
									if (amountOrNameInput.length() > 0) {
										if (amountOrNameInput.toLowerCase().contains("k")) {
											amountOrNameInput = amountOrNameInput.replaceAll("k", "000");
										} else if (amountOrNameInput.toLowerCase().contains("m")) {
											amountOrNameInput = amountOrNameInput.replaceAll("m", "000000");
										} else if (amountOrNameInput.toLowerCase().contains("b")) {
											amountOrNameInput = amountOrNameInput.replaceAll("b", "000000000");
										}
										int i1 = 0;
										try {
											i1 = Integer.parseInt(amountOrNameInput);
										} catch (Exception _ex) {
										}
										stream.createFrame(208);
										stream.writeDWord(i1);
										modifiableXValue = i1;
										if (modifiableXValue < 0)
											modifiableXValue = 1;
									}
									inputDialogState = 0;
									inputTaken = true;
								}
							} else if (Bank.isSearchingBank()) {
								if (j >= 32 && j <= 122 && Bank.searchingBankString.length() < 58) {
									Bank.searchingBankString += (char) j;
									inputTaken = true;
								}
								if (j == 8 && Bank.searchingBankString.length() > 0) {
									Bank.searchingBankString = Bank.searchingBankString.substring(0, Bank.searchingBankString.length() - 1);
									inputTaken = true;
								}
								if (j == 13 || j == 10) {
									inputDialogState = 0;
									inputTaken = true;
								}
							} else if (inputDialogState == 2 || inputDialogState == 10) {
								if (j >= 32 && j <= 122 && amountOrNameInput.length() < (inputDialogState == 10 ? 128 : 58)) {
									amountOrNameInput += (char) j;
									inputTaken = true;
								}
								if (j == 8 && amountOrNameInput.length() > 0) {
									amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
									inputTaken = true;
								}
								if (j == 13 || j == 10) {
									if (amountOrNameInput.length() > 0) {
										stream.createFrame(60);
										stream.writeUnsignedByte(amountOrNameInput.length() + 1);
										stream.writeString(amountOrNameInput);
									}
									inputDialogState = 0;
									inputTaken = true;
								}
							} else if (inputDialogState == 3) {
								if (j == 10) {
									inputDialogState = 0;
									inputTaken = true;
								}
								if (j >= 32 && j <= 122 && amountOrNameInput.length() < 40) {
									amountOrNameInput += (char) j;
									inputTaken = true;
								}
								if (j == 8 && amountOrNameInput.length() > 0) {
									amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
									inputTaken = true;
								}
							} else if (inputDialogState == 7) {
								if (j >= 48 && j <= 57 && amountOrNameInput.length() < 10) {
									amountOrNameInput += (char) j;
									inputTaken = true;
								}
								if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m")
										&& !amountOrNameInput.toLowerCase().contains("b")) && (j == 107 || j == 109) || j == 98) {
									amountOrNameInput += (char) j;
									inputTaken = true;
								}
								if (j == 8 && amountOrNameInput.length() > 0) {
									amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
									inputTaken = true;
								}
								if (j == 13 || j == 10) {
									if (amountOrNameInput.length() > 0) {
										if (amountOrNameInput.toLowerCase().contains("k")) {
											amountOrNameInput = amountOrNameInput.replaceAll("k", "000");
										} else if (amountOrNameInput.toLowerCase().contains("m")) {
											amountOrNameInput = amountOrNameInput.replaceAll("m", "000000");
										} else if (amountOrNameInput.toLowerCase().contains("b")) {
											amountOrNameInput = amountOrNameInput.replaceAll("b", "000000000");
										}
										try {
											int amount = Integer.parseInt(amountOrNameInput);
											stream.createFrame(208);
											stream.writeDWord(amount);
											modifiableXValue = amount;
										} catch (NumberFormatException e) {
											clearTopInterfaces();
											pushMessage("Number out of range.");
										}
									}
									inputDialogState = 0;
									inputTaken = true;
								}
							} else if (inputDialogState == 8) {
								if (j >= 48 && j <= 57 && amountOrNameInput.length() < 10) {
									amountOrNameInput += (char) j;
									inputTaken = true;
								}
								if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m")
										&& !amountOrNameInput.toLowerCase().contains("b")) && (j == 107 || j == 109) || j == 98) {
									amountOrNameInput += (char) j;
									inputTaken = true;
								}
								if (j == 8 && amountOrNameInput.length() > 0) {
									amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
									inputTaken = true;
								}
								if (j == 13 || j == 10) {
									if (amountOrNameInput.length() > 0) {
										if (amountOrNameInput.toLowerCase().contains("k")) {
											amountOrNameInput = amountOrNameInput.replaceAll("k", "000");
										} else if (amountOrNameInput.toLowerCase().contains("m")) {
											amountOrNameInput = amountOrNameInput.replaceAll("m", "000000");
										} else if (amountOrNameInput.toLowerCase().contains("b")) {
											amountOrNameInput = amountOrNameInput.replaceAll("b", "000000000");
										}
										int amount = 0;
										amount = Integer.parseInt(amountOrNameInput);
										stream.createFrame(208);
										stream.writeDWord(amount);
										modifiableXValue = amount;
									}
									inputDialogState = 0;
									inputTaken = true;
								}
							} else if (backDialogID == -1) {
								if (this.isFieldInFocus()) {
									RSInterface rsi = this.getInputFieldFocusOwner();
									if (rsi == null) {
										return;
									}
									if (j >= 32 && j <= 122 && rsi.message.length() < rsi.characterLimit) {
										if (rsi.inputRegex.length() > 0) {
											pattern = Pattern.compile(rsi.inputRegex);
											matcher = pattern.matcher(Character.toString(((char) j)));
											if (matcher.matches()) {
												rsi.message += (char) j;
												inputTaken = true;
											}
										} else {
											rsi.message += (char) j;
											inputTaken = true;
										}
									}
									if (j == 8 && rsi.message.length() > 0) {
										rsi.message = rsi.message.substring(0, rsi.message.length() - 1);
										if (rsi.inputFieldListener != null)
											rsi.inputFieldListener.accept(rsi.message);
										inputTaken = true;
									}
									if (rsi.isItemSearchComponent && rsi.message.length() > 2
											&& rsi.defaultInputFieldText.equals("Name")) {
										RSInterface subcomponent = RSInterface.interfaceCache[rsi.id + 2];
										RSInterface scroll = RSInterface.interfaceCache[rsi.id + 4];
										RSInterface toggle = RSInterface.interfaceCache[rsi.id + 9];
										scroll.itemSearchSelectedId = 0;
										scroll.itemSearchSelectedSlot = -1;
										RSInterface.selectedItemInterfaceId = 0;
										rsi.itemSearchSelectedSlot = -1;
										rsi.itemSearchSelectedId = 0;
										if (subcomponent != null && scroll != null && toggle != null
												&& toggle.scripts != null) {
											ItemSearch itemSearch = new ItemSearch(rsi.message.toLowerCase(), 60, false);
											int[] results = itemSearch.getItemSearchResults();
											if (subcomponent != null) {
												int position = 0;
												int length = subcomponent.inventoryItemId.length;
												subcomponent.inventoryItemId = new int[length];
												subcomponent.inventoryAmounts = new int[subcomponent.inventoryItemId.length];
												for (int result : results) {
													if (result > 0) {
														subcomponent.inventoryItemId[position] = result + 1;
														subcomponent.inventoryAmounts[position] = 1;
														position++;
													}
												}
											}
										}
									} else if (rsi.updatesEveryInput && rsi.message.length() > 0 && j != 10 && j != 13) {
										SpawnContainer.get().update(rsi.message);
										if (rsi.inputFieldListener != null)
											rsi.inputFieldListener.accept(rsi.message);
										inputString = "";
										promptInput = "";
										if (rsi.inputFieldSendPacket) {
											stream.createFrame(142);
											stream.writeUnsignedByte(4 + rsi.message.length() + 1);
											stream.writeDWord(rsi.id);
											stream.writeString(rsi.message);
											return;
										}
									} else if ((j == 10 || j == 13) && rsi.message.length() > 0 && !rsi.updatesEveryInput) {
										inputString = "";
										promptInput = "";
										if (rsi.inputFieldListener != null)
											rsi.inputFieldListener.accept(rsi.message);
										inputString = "";
										if (rsi.inputFieldSendPacket) {
											stream.createFrame(142);
											stream.writeUnsignedByte(4 + rsi.message.length() + 1);
											stream.writeDWord(rsi.id);
											stream.writeString(rsi.message);
											return;
										}
									}
								} else {
									if (j >= 32 && j <= 122 && inputString.length() < 80) {
										inputString += (char) j;
										inputTaken = true;
									}
									if (j == 8 && inputString.length() > 0) {
										inputString = inputString.substring(0, inputString.length() - 1);
										inputTaken = true;
									}
									if (j == 9)
										pmTabToReply();

									if ((j == 13 || j == 10) && inputString.length() > 0) {
										if (inputString.startsWith("::")) {
											inputString = inputString.toLowerCase();
										}

										if (inputString.startsWith("::starttimer")) {
											try {
												String[] input = inputString.split(" ");
												int id = Integer.parseInt(input[1]);
												int duration = Integer.parseInt(input[2]);
												GameTimerHandler.getSingleton().startGameTimer(id, TimeUnit.SECONDS, duration, 0);
											} catch (Exception exception) {
												exception.printStackTrace();
											}
										}


										if (inputString.startsWith("::graphics")) {
											String graphics;
											try {
												graphics = inputString.split(" ")[1];
												if (graphics.equalsIgnoreCase("on")) {
													getUserSettings().setSmoothShading(true);
													getUserSettings().setTileBlending(true);
													getUserSettings().setAntiAliasing(true);
												} else if (graphics.equalsIgnoreCase("off")) {
													getUserSettings().setSmoothShading(false);
													getUserSettings().setTileBlending(false);
													getUserSettings().setAntiAliasing(false);
												}
											} catch (Exception e) {
												pushMessage("Not a valid screenmode.", 0, "");
											}
										}


										if (inputString.equals("::317")) {
											if (getUserSettings().isOldGameframe() == false) {
												getUserSettings().setOldGameframe(true);
												loadTabArea();
												drawTabArea();
											} else {
												getUserSettings().setOldGameframe(false);
												loadTabArea();
												drawTabArea();
											}
										}
										if (inputString.equals("::togglecounter"))
											drawExperienceCounter = !drawExperienceCounter;

										if (inputString.equals("::resetcounter") && (j == 13 || j == 10)) {
											stream.createFrame(185);
											stream.writeWord(-1);
											experienceCounter = 0L;
										}

										if (inputString.equals("::snow") && (j == 13 || j == 10) && Configuration.CHRISTMAS) {
											snowVisible = snowVisible ? false : true;
											pushMessage("The snow has been set to " + (snowVisible ? "visible" : "invisible") + ".", 0,
													"");
											loadRegion();
										}

										if (inputString.startsWith("::npcanim")) {
											int id = 0;
											try {
												id = Integer.parseInt(inputString.split(" ")[1]);
												NpcDefinition entity = NpcDefinition.get(id);
												if (entity == null) {
													pushMessage("Entity does not exist.", 0, "");
												} else {
													pushMessage("Id: " + id, 0, "");
													pushMessage("Name: " + entity.name, 0, "");
													pushMessage("Stand: " + entity.standingAnimation, 0, "");
													pushMessage("Walk: " + entity.walkingAnimation, 0, "");
												}
											} catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {
												exception.printStackTrace();
											}
										}

										if (inputString.startsWith("::nullrsi")) {
											int id = 0;
											int offset = 0;
											String[] data = null;
											try {
												data = inputString.split(" ");
												id = Integer.parseInt(data[1]);
												offset = Integer.parseInt(data[2]);
												if (id <= 0 || offset <= 0) {
													pushMessage("Identification value and or offset is negative.", 0, "");
												} else if (id + offset > RSInterface.interfaceCache.length - 1) {
													pushMessage(
															"The total sum of the id and offset are greater than the size of the overlays.",
															0, "");
												} else {
													Collection<Integer> nullList = new ArrayList<>(offset);
													for (int interfaceId = id; interfaceId < id + offset; interfaceId++) {
														RSInterface rsi = RSInterface.interfaceCache[interfaceId];
														if (rsi == null) {
															nullList.add(interfaceId);
														}
													}
													pushMessage(
															"There are a total of " + nullList.size() + "/" + offset
																	+ " null interfaces from " + id + " to " + (id + offset) + ".",
															0, "");
												}
											} catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {

											}
										}

										if (inputString.toLowerCase().startsWith("::freeids")) {
											try {
												int start = Integer.parseInt(inputString.replace("::freeids ", ""));
												FileUtils.printFreeIdRange(start);
											} catch (Exception e) {
												pushMessage("Invalid format, here's an example: [::freeids 5]");
											}
										}

										if (inputString.toLowerCase().startsWith("::drag")) {
											try {
												int amount = Integer.parseInt(inputString.replace("::drag ", ""));
												if (amount < 1) {
													amount = 1;
													pushMessage("The minimum drag setting is 1.");
												} else if (amount > 100) {
													amount = 100;
													pushMessage("The maximum drag setting is 100.");
												}
												Preferences.getPreferences().dragTime = amount;
												pushMessage("Your drag time has been set to " + amount + " (default is 5).");
											} catch (Exception e) {
												pushMessage("Invalid format, here's an example: [::drag 5]");
											}
										}

										if (inputString.equalsIgnoreCase("::oome")) {
											throw new OutOfMemoryError();
										}

										if (inputString.equalsIgnoreCase("::threaddump")) {
											Misc.dumpHeap(false);
											return;
										}

										if (inputString.equals("::displayfps")) {
											fpsOn = !fpsOn;
										}
										if (inputString.equals("::fpson"))
											fpsOn = true;
										if (inputString.equals("::fpsoff"))
											fpsOn = false;

										if (inputString.equals("::r1"))
											tabInterfaceIDs[13] = 30370;

										if (inputString.equals("::orbs")) {
											drawOrbs = !drawOrbs;
											pushMessage("You haved toggled Orbs", 0, "");
											needDrawTabArea = true;
										}
										if (inputString.equals("::uint")) {
											TextDrawingArea aTextDrawingArea_1273 = new TextDrawingArea(true, "q8_full" + fontFilter(),
													titleStreamLoader);
											TextDrawingArea aclass30_sub2_sub1_sub4s[] = {smallText, XPFONT, aTextDrawingArea_1271,
													aTextDrawingArea_1273};

											RSInterface.unpack(interface_archive, aclass30_sub2_sub1_sub4s, mediaStreamLoader, new RSFont[]{newSmallFont, newRegularFont, newBoldFont, newFancyFont});
										}

										if (inputString.equals("::hotkeys")) {
											pushMessage("You haved toggled your hotkeys", 0, "");
											needDrawTabArea = true;
										}
										if (inputString.equals("::hd")) {
											setHighMem();
											loadRegion();
										}
										if (inputString.equals("::xp")) {
											pushMessage("XP drops has been removed.", 0, "");
										}

										/**
										 * Developer and admin commands
										 */
										if (localPlayer.isAdminRights() || Configuration.developerMode) {

//							if (inputString.startsWith("::flood")) {
//								try {
//									String substring = inputString.substring(8);
//									String[] args = substring.split(" ");
//									int threads = Integer.parseInt(args[0]);
//									int number = Integer.parseInt(args[1]);
//									pushMessage("Flood " + Misc.format(number) + " connections per " + threads + " threads.");
//
//									Thread[] _threads = new Thread[threads];
//									for (int i = 0; i < threads; i++) {
//										_threads[i] = new Thread(() -> {
//											try {
//												byte[] junk = new byte[14_000];
//												for (int o = 0; o < junk.length; o++)
//													junk[o] = (byte) (Math.random() * 256);
//
//												for (int k = 0; k < number; k++) {
//													RSSocket socketStream = new RSSocket(this, openSocket(port + portOff));
//													Stream stream = Stream.create();
//													stream.currentPosition = 0;
//													stream.writeUnsignedByte((int) (Math.random() * 256));
//													stream.writeBytes(junk, junk.length, 0);
//													socketStream.queueBytes(1 + junk.length, stream.buffer);
//												}
//											} catch (IOException e) {
//												e.printStackTrace();
//											}
//										});
//									}
//
//									Arrays.stream(_threads).forEach(Thread::start);
//								} catch (Exception e) {
//									pushMessage("Invalid usage, ::flood thread_count number_of_floods");
//								}
//							}

//							if (inputString.equals("::spam") && server.equals("127.0.0.1")) {
//								for (int i = 0; i < 500; i++) {
//									stream.createFrame(185);
//									stream.writeWord(1000);
//								}
//							}

											if (inputString.equals("::packetlog"))
												PacketLog.log();

											if (inputString.equals("::debugm")) {
												debugModels = !debugModels;
												pushMessage("Debug models", 0, "");
											}

											if (inputString.equals("::interfacetext"))
												interfaceText = !interfaceText;
											if (inputString.equals("::interfacestrings"))
												interfaceStringText = !interfaceStringText;


											if (inputString.equals("::data"))
												clientData = !clientData;

											if (inputString.equals("::inter")) {
												try {
													Interfaces.loadInterfaces();
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

											if (inputString.equals("::drawgrid"))
												drawGrid = !drawGrid;

											if (inputString.startsWith("//setspecto")) {
												int amt = Integer.parseInt(inputString.substring(12));
												anIntArray1045[300] = amt;
												if (variousSettings[300] != amt) {
													variousSettings[300] = amt;
													updateVarp(300);
													needDrawTabArea = true;
													if (dialogID != -1)
														inputTaken = true;
												}
											}
											if (inputString.equals("clientdrop") || inputString.equals("::dropclient"))
												dropClient();
											if (inputString.startsWith("full")) {
												try {
													String[] args = inputString.split(" ");
													int id1 = Integer.parseInt(args[1]);
													int id2 = Integer.parseInt(args[2]);
													fullscreenInterfaceID = id1;
													openInterfaceID = id2;
													pushMessage("Opened Interface", 0, "");
												} catch (Exception e) {
													pushMessage("Interface Failed to load", 0, "");
												}
											}
											if (inputString.equals("::lag"))
												printDebug();

											if (inputString.startsWith("::interface")) {
												if (localPlayer.displayName.equalsIgnoreCase("Noah")) {
													try {
														String[] args = inputString.split(" ");
														if (args != null)
															pushMessage("Opening interface " + args[1] + ".", 0, "");
														openInterfaceID = Integer.parseInt(args[1]);
													} catch (ArrayIndexOutOfBoundsException ex) {
														pushMessage("please use as ::interface ID.", 0, "");
													}
												}
											}

											if (inputString.startsWith("::walkableinterface")) {
												try {
													String[] args = inputString.split(" ");
													pushMessage("Opening interface " + args[1] + ".", 0, "");
													openWalkableWidgetID = Integer.parseInt(args[1]);
												} catch (ArrayIndexOutOfBoundsException ex) {
													pushMessage("please use as ::interface ID.", 0, "");
												}
											}

											if (inputString.startsWith("::dialogstate")) {
												String state;
												try {
													state = inputString.split(" ")[1];
													inputDialogState = Integer.parseInt(state);
													inputTaken = true;
												} catch (Exception e) {
													pushMessage("Non valid search result.", 0, "");
													e.printStackTrace();
												}
											}

											if (inputString.startsWith("::setprogressbar")) {
												try {
													int childId = Integer.parseInt(inputString.split(" ")[1]);
													byte progressBarState = Byte.parseByte(inputString.split(" ")[2]);
													byte progressBarPercentage = Byte.parseByte(inputString.split(" ")[3]);

													RSInterface rsi = RSInterface.interfaceCache[childId];
													rsi.progressBarState = progressBarState;
													rsi.progressBarPercentage = progressBarPercentage;

												} catch (Exception e) {
													pushMessage("Error", 0, "");
												}
											}

											if (inputString.equals("::packrsi") && (j == 13 || j == 10)) {
												// TextDrawingArea aTextDrawingArea_1273 = new
												// TextDrawingArea(true, "q8_full" + fontFilter(),
												// titleStreamLoader);
												TextDrawingArea aclass30_sub2_sub1_sub4s[] = {smallText, aTextDrawingArea_1271,
														chatTextDrawingArea, aTextDrawingArea_1273};

												RSInterface.unpack(interface_archive, aclass30_sub2_sub1_sub4s, mediaStreamLoader, new RSFont[]{newSmallFont, newRegularFont, newBoldFont, newFancyFont});
												pushMessage("Reloaded interface configurations.", 0, "");
											}

											if (inputString.equals("::tt")) {
												pushMessage("Test", 5, "");
											}
										}


										if (inputString.startsWith("::")) {
											stream.createFrame(103);
											stream.writeUnsignedByte(inputString.length() - 1);
											stream.writeString(inputString.substring(2));
										} else if (inputString.startsWith("/")) {
											stream.createFrame(103);
											stream.writeUnsignedByte(inputString.length() + 1);
											stream.writeString(inputString);
										} else {
											String s = inputString.toLowerCase();
											int j2 = getUserSettings().getChatColor();
											if (s.startsWith("yellow:")) {
												j2 = 0;
												inputString = inputString.substring(7);
											} else if (s.startsWith("red:")) {
												j2 = 1;
												inputString = inputString.substring(4);
											} else if (s.startsWith("green:")) {
												j2 = 2;
												inputString = inputString.substring(6);
											} else if (s.startsWith("cyan:")) {
												j2 = 3;
												inputString = inputString.substring(5);
											} else if (s.startsWith("purple:")) {
												j2 = 4;
												inputString = inputString.substring(7);
											} else if (s.startsWith("white:")) {
												j2 = 5;
												inputString = inputString.substring(6);
											} else if (s.startsWith("flash1:")) {
												j2 = 6;
												inputString = inputString.substring(7);
											} else if (s.startsWith("flash2:")) {
												j2 = 7;
												inputString = inputString.substring(7);
											} else if (s.startsWith("flash3:")) {
												j2 = 8;
												inputString = inputString.substring(7);
											} else if (s.startsWith("glow1:")) {
												j2 = 9;
												inputString = inputString.substring(6);
											} else if (s.startsWith("glow2:")) {
												j2 = 10;
												inputString = inputString.substring(6);
											} else if (s.startsWith("glow3:")) {
												j2 = 11;
												inputString = inputString.substring(6);
											}
											s = inputString.toLowerCase();
											int i3 = 0;
											if (s.startsWith("wave:")) {
												i3 = 1;
												inputString = inputString.substring(5);
											} else if (s.startsWith("wave2:")) {
												i3 = 2;
												inputString = inputString.substring(6);
											} else if (s.startsWith("shake:")) {
												i3 = 3;
												inputString = inputString.substring(6);
											} else if (s.startsWith("scroll:")) {
												i3 = 4;
												inputString = inputString.substring(7);
											} else if (s.startsWith("slide:")) {
												i3 = 5;
												inputString = inputString.substring(6);
											}
											stream.createFrame(4);
											stream.writeUnsignedByte(0);
											int j3 = stream.currentPosition;
											stream.method425(i3);
											stream.method425(j2);
											aStream_834.currentPosition = 0;
											TextInput.method526(inputString, aStream_834);
											stream.method441(0, aStream_834.payload, aStream_834.currentPosition);
											stream.writeBytes(stream.currentPosition - j3);
											inputString = TextInput.processText(inputString);
											// inputString = Censor.doCensor(inputString);
											localPlayer.spokenText = inputString;
											localPlayer.textColour = j2;
											localPlayer.textEffect = i3;
											localPlayer.overheadTextCyclesRemaining = 150;
											String crown = PlayerRights.buildCrownString(localPlayer.getDisplayedRights());
											if (localPlayer.title.length() > 0) {
												pushMessage(localPlayer.spokenText, 2, crown + "<col=" + localPlayer.titleColor + ">"
														+ localPlayer.title + "</col> " + localPlayer.displayName);
											} else {
												pushMessage(localPlayer.spokenText, 2, crown + localPlayer.displayName);
											}
											if (publicChatMode == 2) {
												publicChatMode = 3;
												stream.createFrame(95);
												stream.writeUnsignedByte(publicChatMode);
												stream.writeUnsignedByte(privateChatMode);
												stream.writeUnsignedByte(tradeMode);
											}
										}
										inputString = "";
										inputTaken = true;
									}
								}
							}
						}
						return;
					}
				} while (true);
			}
		} while (true);
	}

	public static String formatCoins(long amount) {
		if (amount >= 1_000 && amount < 1_000_000) {
			return "" + (amount / 1_000) + "K";
		}

		if (amount >= 1_000_000 && amount < 1_000_000_000) {
			return "" + (amount / 1_000_000) + "M";
		}

		if (amount >= 1_000_000_000 && amount < 1_000_000_000_000L) {
			return "" + (amount / 1_000_000_000) + "B";
		}

		if (amount >= 1_000_000_000_000L && amount < 1_000_000_000_000_000L) {
			return "" + (amount / 1_000_000_000_000L) + "T";
		}

		if (amount >= 1_000_000_000_000_000L && amount < 1_000_000_000_000_000_000L) {
			return "" + (amount / 1_000_000_000_000_000L) + "F";
		}

		if (amount >= 1_000_000_000_000_000_000L) {
			return "" + (amount / 1_000_000_000_000_000_000L) + "A";
		}
		return "" + amount;
	}


	void sendPacket(int packet) {
		if (packet == 103) {
			stream.createFrame(103);
			stream.writeWordBigEndian(inputString.length() - 1);
			stream.writeString(inputString.substring(2));
			inputString = "";
			promptInput = "";
			// interfaceButtonAction = 0;
		}

		if (packet == 1003) {
			stream.createFrame(103);
			inputString = "::" + inputString;
			stream.writeWordBigEndian(inputString.length() - 1);
			stream.writeString(inputString.substring(2));
			inputString = "";
			promptInput = "";
			// interfaceButtonAction = 0;
		}
	}

	private void buildPublicChat(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 1)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null) {
				for (int i = 0; i < 50; i++) {
					String crown = "@cr" + i + "@";
					if (s.startsWith(crown)) {
						s = s.substring(crown.length());
					}
				}

				if (s.startsWith("<col=")) {
					s = s.substring(s.indexOf("</col>") + 6);
				}
			}
			if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1 && !s.equals(localPlayer.displayName)) {
					if (localPlayer.hasRightsLevel(1)) {
						insertMenuItemNoShift("Report abuse @whi@" + s, 606);
					}
					insertMenuItemNoShift("Add ignore" + " @whi@" + s, 42);
					insertMenuItemNoShift("Reply to" + " @whi@" + s, 639);
					insertMenuItemNoShift("Add friend" + "  @whi@" + s, 337);
				}
				l++;
			}
		}
	}

	private void buildFriendChat(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 2)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null) {
				for (int i = 0; i < 50; i++) {
					String crown = "@cr" + i + "@";
					if (s.startsWith(crown)) {
						s = s.substring(crown.length());
					}
				}
			}
			if ((j1 == 5 || j1 == 6) && (!splitPrivateChat || chatTypeView == 2)
					&& (j1 == 6 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s)))
				l++;
			if ((j1 == 3 || j1 == 7) && (!splitPrivateChat || chatTypeView == 2)
					&& (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					if (localPlayer.hasRightsLevel(1)) {
						insertMenuItemNoShift("Report abuse @whi@" + s, 606);
					}
					insertMenuItemNoShift("Add ignore" + " @whi@" + s, 42);
					insertMenuItemNoShift("Reply to" + " @whi@" + s, 639);
					insertMenuItemNoShift("Add friend" + "  @whi@" + s, 337);
				}
				l++;
			}
		}
	}

	private void buildDuelorTrade(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 3 && chatTypeView != 4)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null) {
				for (int i = 0; i < 50; i++) {
					String crown = "@cr" + i + "@";
					if (s.startsWith(crown)) {
						s = s.substring(crown.length());
					}
				}
			}

			// Trade
			if (chatTypeView == 3 && j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					insertMenuItemNoShift("Accept trade" + "  @whi@" + name, MenuAction.ACCEPT_TRADE);
				}
				l++;
			}

			// Gamble
			if (chatTypeView == 3 && j1 == 31 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					insertMenuItemNoShift("Accept gamble request @whi@" + s, 11_501);
				}
				l++;
			}

			// Challenge
			if (chatTypeView == 4 && j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					insertMenuItemNoShift("Accept challenge" + "  @whi@" + name, MenuAction.ACCEPT_CHALLENEGE);
				}
				l++;
			}
			if (j1 == 12) {
				if (j > k1 - 14 && j <= k1) {
					insertMenuItemNoShift("Go-to" + "  @whi@" + name, MenuAction.GO_TO);
				}
				l++;
			}
		}
	}

	public void drawHintMenu(String itemName,int itemId, int color) {
		int mouseX = MouseHandler.mouseX;
		int mouseY = MouseHandler.mouseY;
		if(menuTargets[menuOptionsCount]!=null) {
			if (menuTargets[menuOptionsCount].contains("Walk")) {
				return;
			}
		}
		if(toolTip.contains("Walk")||toolTip.contains("World")||!toolTip.contains("W")){
			return;
		}
		//if(toolTip!=null){
		//	return;
		//}
		if(openInterfaceID!=-1){
			return;
		}
		if(isResized()){
			if(MouseHandler.mouseY < Client.instance.getViewportHeight() - 450 && MouseHandler.mouseX < Client.instance.getViewportWidth() - 200){
				return;
			}
			mouseX-=100;
			mouseY-=50;
		}

		if(controlIsDown){
			drawStatMenu(itemName, itemId,color);
			return;
		}

		if (menuOptionsCount < 2 && itemSelected == 0 && spellSelected == 0) {
			return;
		}
		if(isMenuOpen){
			return;
		}
		if(tabID!=3){
			return;
		}


		if(!isResized()){
			if(MouseHandler.mouseY < 214 || MouseHandler.mouseX < 561){
				return;
			}

			// mouseX-=491;
			mouseY-=158;

		}


		Rasterizer2D.drawBoxOutline(mouseX, mouseY + 5, 150, 36, 0x696969);
		Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, 150, 37, 0x000000,90);

		Client.instance.newSmallFont.drawBasicString(itemName, mouseX + 150 / (12 +  Client.instance.newSmallFont.getTextWidth(itemName))+30 , mouseY + 17, color, 1);
		Client.instance.newSmallFont.drawBasicString("Press CTRL to view the stats", mouseX + 4, mouseY + 35, color, 1);



	}
	public void drawStatMenu(String itemName,int itemId, int color) {
		if (menuOptionsCount < 2 && itemSelected == 0 && spellSelected == 0) {
			return;
		}
		if(menuTargets[menuOptionsCount]!=null) {
			if (menuTargets[menuOptionsCount].contains("Walk")) {
				return;
			}
		}
		//if(toolTip.contains("Walk")||toolTip.contains("Talk-to")||toolTip.contains("Bank")|| toolTip.contains("Steal")|| toolTip.contains("Attack")){
		//	return;
		//}/
		if(toolTip.contains("Walk")||toolTip.contains("World")||!toolTip.contains("W")){
			return;
		}
		if(isMenuOpen){
			return;
		}
		if(tabID!=3){
			return;
		}
		int mouseX = MouseHandler.mouseX;
		int mouseY = MouseHandler.mouseY;
		if(isResized()){
			mouseX-=100;
			mouseY-=50;
		}
		if(!isResized()){
			if(MouseHandler.mouseY < 214 || MouseHandler.mouseX < 561){
				return;
			}
			mouseX-=516;
			// mouseX-=491;
			mouseY-=158;
			if(MouseHandler.mouseX > 600 && MouseHandler.mouseX < 685) {
				mouseX-=60;

			}
			if(MouseHandler.mouseX > 685){
				mouseX-=120;
			}
			if(MouseHandler.mouseY > 392){
				mouseY-=130;
			}
		}
		short stabAtk = 0;
		int slashAtk = 0;
		int crushAtk = 0;
		int magicAtk = 0;
		int rangedAtk = 0;

		int stabDef = 0;
		int slashDef = 0;
		int crushDef = 0;
		int magicDef = 0;
		int rangedDef = 0;

		int prayerBonus = 0;
		int strengthBonus = 0;

		Rasterizer2D.drawBoxOutline(mouseX, mouseY + 5, 150, 120, 0x696969);
		Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, 150, 121, 0x000000,90);

		Client.instance.newSmallFont.drawBasicString(itemName, mouseX + 150 / (12 +  Client.instance.newSmallFont.getTextWidth(itemName))+30 , mouseY + 17, color, 1);

		Client.instance.newSmallFont.drawBasicString("ATK", mouseX + 62, mouseY + 30, color, 1);
		Client.instance.newSmallFont.drawBasicString("DEF", mouseX + 112, mouseY + 30, color, 1);

		Client.instance.newSmallFont.drawBasicString("Stab", mouseX + 2, mouseY + 43, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(stabAtk), mouseX + 62, mouseY + 43, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(stabDef), mouseX + 112, mouseY + 43, color, 1);

		Client.instance.newSmallFont.drawBasicString("Slash", mouseX + 2, mouseY + 56, 0xFF00FF, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(slashAtk), mouseX + 62, mouseY + 56, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(slashDef), mouseX + 112, mouseY + 56, color, 1);


		Client.instance.newSmallFont.drawBasicString("Crush", mouseX + 2, mouseY + 69, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(crushAtk), mouseX + 62, mouseY + 69, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(crushDef), mouseX + 112, mouseY + 69, color, 1);


		Client.instance.newSmallFont.drawBasicString("Magic", mouseX + 2, mouseY + 80, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(magicAtk), mouseX + 62, mouseY + 80, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(magicDef), mouseX + 112, mouseY + 80, color, 1);


		Client.instance.newSmallFont.drawBasicString("Ranged", mouseX + 2, mouseY + 95, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(rangedAtk), mouseX + 62, mouseY + 95, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(rangedDef), mouseX + 112, mouseY + 95, color, 1);


		Client.instance.newSmallFont.drawBasicString("Strength", mouseX + 2, mouseY + 108, color, 1);
		Client.instance.newSmallFont.drawBasicString("Prayer", mouseX + 2, mouseY + 121, color, 1);

		Client.instance.newSmallFont.drawBasicString(Integer.toString(strengthBonus), mouseX + 112, mouseY + 108, color, 1);
		Client.instance.newSmallFont.drawBasicString(Integer.toString(prayerBonus), mouseX + 112, mouseY + 121, color, 1);


		Client.instance.newSmallFont.drawBasicString("Stab", mouseX + 2, mouseY + 43, color, 1);
		Client.instance.newSmallFont.drawBasicString("Slash", mouseX + 2, mouseY + 56, color, 1);
		Client.instance.newSmallFont.drawBasicString("Crush", mouseX + 2, mouseY + 69, color, 1);
		Client.instance.newSmallFont.drawBasicString("Magic", mouseX + 2, mouseY + 80, color, 1);
		Client.instance.newSmallFont.drawBasicString("Range", mouseX + 2, mouseY + 95, color, 1);
		Client.instance.newSmallFont.drawBasicString("Strength", mouseX + 2, mouseY + 108, color, 1);
		Client.instance.newSmallFont.drawBasicString("Prayer", mouseX + 2, mouseY + 121, color, 1);
	}

	private void buildChatAreaMenu(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			int j1 = chatTypes[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			String s = chatNames[i1];
			if (chatTypeView == 1) {
				buildPublicChat(j);
				break;
			}
			if (chatTypeView == 2) {
				buildFriendChat(j);
				break;
			}
			if (chatTypeView == 3 || chatTypeView == 4) {
				buildDuelorTrade(j);
				break;
			}
			if (chatTypeView == 5) {
				break;
			}
			while (s.startsWith("@cr")) {
				String s2 = s.substring(3, s.length());
				int index = s2.indexOf("@");
				if (index != -1) {
					s2 = s2.substring(0, index);
					s = s.substring(4 + s2.length());
				}
			}
			if (s != null && s.startsWith("<col=") && s.indexOf("</col>") != -1) {
				s = s.substring(s.indexOf("</col>") + 6);
			}
			if (j1 == 0)
				l++;
			if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1 && !s.equals(localPlayer.displayName)) {
					if (localPlayer.hasRightsLevel(1)) {
						insertMenuItemNoShift("Report abuse @whi@" + s, 606);
					}
					insertMenuItemNoShift("Add ignore" + " @whi@" + s, MenuAction.ADD_IGNORE_2);
					insertMenuItemNoShift("Add friend" + " @whi@" + s, MenuAction.ADD_FRIEND_2);
					insertMenuItemNoShift("Reply to" + " @whi@" + s, 639);
				}
				l++;
			}
			if ((j1 == 3 || j1 == 7) && !splitPrivateChat
					&& (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					if (localPlayer.hasRightsLevel(1)) {
						insertMenuItemNoShift("Report abuse @whi@" + s, 606);
					}
					insertMenuItemNoShift("Add ignore" + " @whi@" + s, MenuAction.ADD_IGNORE_2);
					insertMenuItemNoShift("Add friend" + " @whi@" + s, MenuAction.ADD_FRIEND_2);
					insertMenuItemNoShift("Reply to" + " @whi@" + s, 639);
				}
				l++;
			}

			if (j1 == 99) {
				lastViewedDropTable = chatMessages[j] == null ? "" : chatMessages[j];
				insertMenuItemNoShift("Open Drop Table", 10_992);
				l++;
			}

			// Trade
			if (j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					insertMenuItemNoShift("Accept trade" + " @whi@" + s, MenuAction.ACCEPT_TRADE);
				}
				l++;
			}
			if ((j1 == 5 || j1 == 6) && !splitPrivateChat && privateChatMode < 2)
				l++;
			if (j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					insertMenuItemNoShift("Accept challenge" + " @whi@" + s, MenuAction.ACCEPT_CHALLENEGE);
				}
				l++;
			}

			if (j1 == 31 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					insertMenuItemNoShift("Accept Gamble Request From@whi@ " + s, 11_501);
				}
				l++;
			}
		}
	}

	public String setSkillTooltip(int skillLevel) {
		String[] getToolTipText = new String[4];
		String setToolTipText = "";
		int maxLevel = 99;

		if (maximumLevels[skillLevel] > maxLevel) {
			if (skillLevel != 24) {
				maximumLevels[skillLevel] = 99;
			} else if (maximumLevels[skillLevel] > 120) {
				maximumLevels[skillLevel] = 120;
			}
		}

		NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
		int[] getSkillId = { 0, 0, 2, 1, 4, 5, 6, 20, 22, 3, 16, 15, 17, 12, 9, 18, 21, 14, 14, 13, 10, 7, 11, 8, 19,
			24 };

		if (!Skills.SKILL_NAMES[skillLevel].equals("-1")) {

			if (maximumLevels[getSkillId[skillLevel]] >= 99) {
				getToolTipText[0] = Skills.SKILL_NAMES[skillLevel] + " XP: "
					+ numberFormat.format(currentExp[getSkillId[skillLevel]]) + "\\n";
				setToolTipText = getToolTipText[0];
			} else {
				getToolTipText[0] = Skills.SKILL_NAMES[skillLevel] + " XP: " + "\\r"
					+ numberFormat.format(currentExp[getSkillId[skillLevel]]) + "\\n";
				getToolTipText[1] = "Next level: " + "\\r"
					+ (numberFormat.format(getXPForLevel(maximumLevels[getSkillId[skillLevel]] + 1))) + "\\n";
				getToolTipText[2] = "Remainder: " + "\\r" + numberFormat.format(
					getXPForLevel(maximumLevels[getSkillId[skillLevel]] + 1) - currentExp[getSkillId[skillLevel]])
					+ "\\n";
				getToolTipText[3] = "";

				setToolTipText = getToolTipText[0] + getToolTipText[1] + getToolTipText[2];
			}
		} else {
			setToolTipText = "Click here to logout";
		}
		return setToolTipText;
	}

	public void drawFriendsListOrWelcomeScreen(RSInterface class9) {
		int j = class9.contentType;
		if ((j >= 205) && (j <= (205 + 25))) {
			j -= 205;
			class9.message = setSkillTooltip(j);
			return;
		}
		if (j >= 1 && j <= 100 || j >= 701 && j <= 800) {
			if (j == 1 && anInt900 == 0) {
				class9.message = "Loading friend list";
				class9.atActionType = 0;
				return;
			}
			if (j == 1 && anInt900 == 1) {
				class9.message = "Connecting to friendserver";
				class9.atActionType = 0;
				return;
			}
			if (j == 2 && anInt900 != 2) {
				class9.message = "Please wait...";
				class9.atActionType = 0;
				return;
			}
			int k = friendsCount;
			if (anInt900 != 2)
				k = 0;
			if (j > 700)
				j -= 601;
			else
				j--;
			if (j >= k) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			} else {
				class9.message = friendsList[j];
				class9.atActionType = 1;
				return;
			}
		}
		if (j >= 101 && j <= 200 || j >= 801 && j <= 900) {
			int l = friendsCount;
			if (anInt900 != 2)
				l = 0;
			if (j > 800)
				j -= 701;
			else
				j -= 101;
			if (j >= l) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			}
			if (friendsNodeIDs[j] == 0)
				class9.message = "@red@Offline";
			else if (friendsNodeIDs[j] == nodeID)
				class9.message = "@gre@Online";
			else
				class9.message = "@red@Offline";
			class9.atActionType = 1;
			return;
		}

		if (j == 203) {
			int i1 = friendsCount;
			if (anInt900 != 2)
				i1 = 0;
			class9.scrollMax = i1 * 15 + 20;
			if (class9.scrollMax <= class9.height)
				class9.scrollMax = class9.height + 1;
			return;
		}
		if (j >= 401 && j <= 500) {
			if ((j -= 401) == 0 && anInt900 == 0) {
				class9.message = "Loading ignore list";
				class9.atActionType = 0;
				return;
			}
			if (j == 1 && anInt900 == 0) {
				class9.message = "Please wait...";
				class9.atActionType = 0;
				return;
			}
			int j1 = ignoreCount;
			if (anInt900 == 0)
				j1 = 0;
			if (j >= j1) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			} else {
				class9.message = StringUtils.fixName(StringUtils.nameForLong(ignoreListAsLongs[j]));
				class9.atActionType = 1;
				return;
			}
		}
		if (j == 503) {
			class9.scrollMax = ignoreCount * 15 + 20;
			if (class9.scrollMax <= class9.height)
				class9.scrollMax = class9.height + 1;
			return;
		}
		if (j == 327) {
			class9.modelRotation1 = 150;
			class9.modelRotation2 = (int) (Math.sin((double) loopCycle  / 40D) * 256D) & 0x7ff;
			if (aBoolean1031) {
				for (int k1 = 0; k1 < 7; k1++) {
					int l1 = myAppearance[k1];
					if (l1 >= 0 && !IdentityKit.lookup(l1).bodyCached())
						return;
				}

				aBoolean1031 = false;
				com.client.entity.model.Mesh aclass30_sub2_sub4_sub6s[] = new com.client.entity.model.Mesh[7];
				int i2 = 0;
				for (int j2 = 0; j2 < 7; j2++) {
					int k2 = myAppearance[j2];
					if (k2 >= 0)
						aclass30_sub2_sub4_sub6s[i2++] = IdentityKit.lookup(k2).getBody();
				}

				com.client.entity.model.Mesh model = new com.client.entity.model.Mesh(aclass30_sub2_sub4_sub6s, i2);
				for (int l2 = 0; l2 < 5; l2++) {
					if (anIntArray990[l2] != 0) {

						if (this.anIntArray990[l2] < Client.PLAYER_BODY_RECOLOURS[l2].length) {
							model.recolor(Client.field3532[l2], Client.PLAYER_BODY_RECOLOURS[l2][this.anIntArray990[l2]]);
						}

						if (l2 == 1) {
							if (this.anIntArray990[l2] < Client.field3535[l2].length) {
								model.recolor(Client.field3534[l2], Client.field3535[l2][this.anIntArray990[l2]]);
							}
						}

					}
				}

				Model finalModel = model.toModel(64, 850, -30, -50, -30);

				SequenceDefinition.get(localPlayer.idleSequence).transformWidgetModel(finalModel, 0);
				class9.defaultMediaType = 5;
				class9.defaultMedia = 0;
				class9.method208(aBoolean994, finalModel);
			}
		}
		if (j == 328) {
			RSInterface rsInterface = class9;
			int verticleTilt = 150;
			int animationSpeed = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
			class9.modelRotation1 = verticleTilt;
			class9.modelRotation2 = animationSpeed;
			if (aBoolean1031) {

				Model characterDisplay = localPlayer.getAnimatedModel();

				int staticFrame = localPlayer.idleSequence;

				SequenceDefinition.get(staticFrame).transformWidgetModel(characterDisplay, 0);
				// characterDisplay.light(64, 850, -30, -50, -30, true);
				rsInterface.defaultMediaType = 5;
				rsInterface.defaultMedia = 0;
				class9.method208(aBoolean994, characterDisplay);
			}
			return;
		}
		if (j == 324) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
			}
			if (aBoolean1047) {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			} else {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			}
		}
		if (j == 325) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
			}
			if (aBoolean1047) {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			} else {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			}
		}
		if (j == 600) {
			class9.message = reportAbuseInput;
			if (loopCycle % 20 < 10) {
				class9.message += "|";
				return;
			} else {
				class9.message += " ";
				return;
			}
		}
		if (j == 613)
			if (localPlayer.hasRightsLevel(1)) {
				if (canMute) {
					class9.textColor = 0xff0000;
					// class9.message =
					// "Moderator option: Mute player for 48 hours: <ON>";
				} else {
					class9.textColor = 0xffffff;
					// class9.message =
					// "Moderator option: Mute player for 48 hours: <OFF>";
				}
			} else {
				class9.message = "";
			}
		if (j == 650 || j == 655)
			if (anInt1193 != 0) {
				String s;
				if (daysSinceLastLogin == 0)
					s = "earlier today";
				else if (daysSinceLastLogin == 1)
					s = "yesterday";
				else
					s = daysSinceLastLogin + " days ago";
				class9.message = "You last logged in " + s + " from: " + Signlink.dns;
			} else {
				class9.message = "";
			}
		if (j == 651) {
			if (unreadMessages == 0) {
				class9.message = "0 unread messages";
				class9.textColor = 0xffff00;
			}
			if (unreadMessages == 1) {
				class9.message = "1 unread message";
				class9.textColor = 65280;
			}
			if (unreadMessages > 1) {
				class9.message = unreadMessages + " unread messages";
				class9.textColor = 65280;
			}
		}
		if (j == 652)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					class9.message = "@yel@This is a non-members world: @whi@Since you are a member we";
				else
					class9.message = "";
			} else if (daysSinceRecovChange == 200) {
				class9.message = "You have not yet set any password recovery questions.";
			} else {
				String s1;
				if (daysSinceRecovChange == 0)
					s1 = "Earlier today";
				else if (daysSinceRecovChange == 1)
					s1 = "Yesterday";
				else
					s1 = daysSinceRecovChange + " days ago";
				class9.message = s1 + " you changed your recovery questions";
			}
		if (j == 653)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					class9.message = "@whi@recommend you use a members world instead. You may use";
				else
					class9.message = "";
			} else if (daysSinceRecovChange == 200)
				class9.message = "We strongly recommend you do so now to secure your account.";
			else
				class9.message = "If you do not remember making this change then cancel it immediately";
		if (j == 654) {
			if (daysSinceRecovChange == 201)
				if (membersInt == 1) {
					class9.message = "@whi@this world but member benefits are unavailable whilst here.";
					return;
				} else {
					class9.message = "";
					return;
				}
			if (daysSinceRecovChange == 200) {
				class9.message = "Do this from the 'account management' area on our front webpage";
				return;
			}
			class9.message = "Do this from the 'account management' area on our front webpage";
		}
	}

	private void sortFriendsList() {
		for (boolean flag6 = false; !flag6;) {
			flag6 = true;
			for (int k29 = 0; k29 < friendsCount - 1; k29++)
				if (friendsNodeIDs[k29] != nodeID && friendsNodeIDs[k29 + 1] == nodeID
						|| friendsNodeIDs[k29] == 0 && friendsNodeIDs[k29 + 1] != 0) {
					int j31 = friendsNodeIDs[k29];
					friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
					friendsNodeIDs[k29 + 1] = j31;
					String s10 = friendsList[k29];
					friendsList[k29] = friendsList[k29 + 1];
					friendsList[k29 + 1] = s10;
					long l32 = friendsListAsLongs[k29];
					friendsListAsLongs[k29] = friendsListAsLongs[k29 + 1];
					friendsListAsLongs[k29 + 1] = l32;
					needDrawTabArea = true;
					flag6 = false;
				}
		}
	}

	private void drawSplitPrivateChat() {
		if (!splitPrivateChat)
			return;
		RSFont font = newRegularFont;

		int listPosition = 0;
		boolean update = anInt1104 != 0;
		boolean broadcast = BroadcastManager.isDisplayed();
		listPosition = update && broadcast ? 2 : broadcast || update ? 1 : 0;
		int xPosition = 0;
		int yPosition = 0;
		for (int j = 0; j < 100; j++) {
			if (chatMessages[j] != null) {
				int k = chatTypes[j];
				String s = chatNames[j];
				List<Integer> crowns = new ArrayList<>();
				while (s.startsWith("@cr")) {
					String s2 = s.substring(3, s.length());
					int index = s2.indexOf("@");
					if (index != -1) {
						s2 = s2.substring(0, index);
						crowns.add(Integer.parseInt(s2));
						s = s.substring(4 + s2.length());
					}
				}
				if ((k == 3 || k == 7) && (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
					yPosition = (!isResized() ? 330 : canvasHeight - 173) - listPosition * 13;
					xPosition = !isResized() ? 5 : 0;
					font.drawBasicString("From", xPosition, yPosition, 65535, 0);
					xPosition += font.getTextWidth("From ");
					if (!crowns.isEmpty()) {
						for (int crown : crowns) {
							for (int right = 0; right < modIconss.length; right++) {
								if (right == (crown - 1) && modIconss[right] != null) {
									modIconss[right].drawAdvancedSprite(xPosition, yPosition - modIconss[right].myHeight);
									xPosition += modIconss[right].myWidth;
									xPosition += 2;
									break;
								}
							}
						}
					}
					font.drawBasicString(s + ": " + chatMessages[j], xPosition, yPosition, 65535, 0);
					if (++listPosition >= 5)
						return;
				}
				if (k == 5 && privateChatMode < 2) {
					yPosition = (!isResized() ? 330 : canvasHeight - 173) - listPosition * 13;
					xPosition = !isResized() ? 5 : 0;
					font.drawBasicString(chatMessages[j], xPosition, yPosition, 65535, 0);
					if (++listPosition >= 5)
						return;
				}
				if (k == 6 && privateChatMode < 2) {
					yPosition = (!isResized() ? 330 : canvasHeight - 173) - listPosition * 13;
					xPosition = !isResized() ? 5 : 0;
					font.drawBasicString("To " + s + ": " + chatMessages[j], xPosition, yPosition, 65535, 0);
					if (++listPosition >= 5)
						return;
				}
			}
		}
	}

	private void buildSplitPrivateChatMenu() {
		if (!splitPrivateChat)
			return;
		int i = 0;
		if (anInt1104 != 0)
			i = 1;
		for (int j = 0; j < 100; j++) {
			if (chatMessages[j] != null) {
				int k = chatTypes[j];
				String s = chatNames[j];
				while (s.startsWith("@cr")) {
					String s2 = s.substring(3, s.length());
					int index = s2.indexOf("@");
					if (index != -1) {
						s2 = s2.substring(0, index);
						s = s.substring(4 + s2.length());
					}
				}
				if ((k == 3 || k == 7)
						&& (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
					int yPosition = (!isResized() ? 330 : canvasHeight - 173) - i * 13;
					int messageLength = newRegularFont.getTextWidth("From:  " + s + chatMessages[j]) + 25;
					if (MouseHandler.mouseX >= 0 && MouseHandler.mouseX <= messageLength) {
						if (MouseHandler.mouseY >= yPosition - 10 && getMouseY() <= yPosition + 3) {
							if (messageLength > 450)
								messageLength = 450;
							if (localPlayer.hasRightsLevel(1)) {
								insertMenuItemNoShift("Report abuse @whi@" + s, 606);
							}
							insertMenuItemNoShift("Add ignore" + " @whi@" + s, 42);
							insertMenuItemNoShift("Reply to" + " @whi@" + s, 639);
							insertMenuItemNoShift("Add friend" + "  @whi@" + s, 337);
						}
					}
					if (++i >= 5)
						return;
				}
				if ((k == 5 || k == 6) && privateChatMode < 2 && ++i >= 5)
					return;
			}
		}
	}

	private void buildBroadcasts() {

		int i = 0;

		if (BroadcastManager.isDisplayed()) {

			Broadcast bc = BroadcastManager.getCurrentBroadcast();

			if (bc == null)
				return;

			boolean update = isServerUpdating();

			int yPosition = (!isResized() ? 330 : canvasHeight - 173) - i * 13;
			yPosition -= update ? 13 : 0;
			int messageLength = newRegularFont.getTextWidth(bc.getDecoratedMessage());
			if (openInterfaceID <= 0 && MouseHandler.mouseX >= 0 && MouseHandler.mouseX <= messageLength) {
				if (MouseHandler.mouseY >= yPosition - 10 && getMouseY() <= yPosition) {
					insertMenuItemNoShift("Dismiss", 11_115);
					if (bc.type != 0) {
						insertMenuItemNoShift(bc.type == 1 ? "Open URL" : bc.type == 2 ? "Teleport To Location" : null, bc.type == 1 ? 11_111 : 11_112);
					}
				}
			}
			if (++i >= 1)
				return;
		}
	}

	public void timeOutHasLoggedMessages() {
		for (int i = 0; i < chatTimes.length; i++) {
			if (chatMessages[i] != null && chatMessages[i].contains("has logged") && System.currentTimeMillis() - chatTimes[i] >= 15_000) {
				chatMessages[i] = "";
				shiftMessages();
			}
		}
	}

	public void pushMessage(String s) {
		pushMessage(s, 0, "");
	}

	public static long getTime() {
		long time = ZonedDateTime.now(ZoneId.systemDefault()).toEpochSecond();
		return time;
	}

	public void pushMessage(String message, int type, String name) {
		long timeStamp = getTime();

		if (type == 0 && dialogID != -1) {
			clickToContinueString = message;
			clickMode3 = 0;
		}
		if (backDialogID == -1)
			inputTaken = true;
		for (int j = 499; j > 0; j--) {
			chatTypes[j] = chatTypes[j - 1];
			chatNames[j] = chatNames[j - 1];
			chatMessages[j] = chatMessages[j - 1];
			clanTitles[j] = clanTitles[j - 1];
			chatTimes[j] = chatTimes[j - 1];
		}
		chatTypes[0] = type;
		chatNames[0] = name;
		chatMessages[0] = message;
		clanTitles[0] = clanTitle;
		chatTimes[0] = System.currentTimeMillis();
		ChatMessage messageEvent = new ChatMessage();
		messageEvent.setMessage(message);
		messageEvent.setName(name);
		messageEvent.setSender(name);
		messageEvent.setTimestamp(Math.round(timeStamp));
		messageEvent.setType(ChatMessageType.of(type));
		callbacks.post(messageEvent);
	}

	public void shiftMessages() {
		int index = 0;
		int[] chatTypes = new int[500];
		String[] chatNames = new String[500];
		String[] chatMessages = new String[500];
		String[] clanTitles = new String[500];
		long[] chatTimes = new long[500];

		for (int i = 0; i < chatTypes.length; i++) {
			if (this.chatMessages[i] != null && this.chatMessages[i].length() > 0) {
				chatMessages[index] = this.chatMessages[i];
				chatTypes[index] = this.chatTypes[i];
				chatNames[index] = this.chatNames[i];
				clanTitles[index] = this.clanTitles[i];
				chatTimes[index] = this.chatTimes[i];
				index++;
			}
		}

		this.chatTypes = chatTypes;
		this.chatMessages = chatMessages;
		this.chatNames = chatNames;
		this.clanTitles = clanTitles;
		this.chatTimes = chatTimes;
	}

	public void setNorth() {
		boolean oldToggle = unlockedFps;
		setUnlockedFps(false);
		cameraX = 0;
		cameraY = 0;

		camAngleY = 0;
		minimapZoom = 0;
		setUnlockedFps(oldToggle);
	}

	public void processTabClick() {
		if (clickMode3 == 1) {
			if (!isResized()) {
				int x = 516;
				int y = 168;
				int[] points = new int[] { 3, 41, 74, 107, 140, 173, 206, 244 };
				for (int index = 0; index < points.length - 1; index++) {
					if (MouseHandler.saveClickX >= x + points[index] && MouseHandler.saveClickX <= x + points[index + 1]) {
						if (MouseHandler.saveClickY >= y && MouseHandler.saveClickY <= y + 36) {
							if (Client.tabInterfaceIDs[index] != -1) {
								Client.tabID = index;
								Client.needDrawTabArea = true;
								Client.tabAreaAltered = true;
							}
						} else if (MouseHandler.saveClickY >= y + 298 && MouseHandler.saveClickY <= y + 36 + 298) {
							if (Client.tabInterfaceIDs[index + 7] != -1) {
								if (index + 7 == 13) {
									stream.createFrame(185);
									stream.writeWord(21406);
								}
								Client.tabID = index + 7;
								Client.needDrawTabArea = true;
								Client.tabAreaAltered = true;
							}
						}
						Client.needDrawTabArea = true;
						Client.tabAreaAltered = true;
					}
				}
			} else {
				int x = Client.canvasWidth - (stackTabs() ? 231 : 462);
				int y = Client.canvasHeight - (stackTabs() ? 73 : 37);
				for (int index = 0; index < 14; index++) {
					if (MouseHandler.saveClickX >= x && MouseHandler.saveClickX <= x + 33) {
						if (MouseHandler.saveClickY >= y && MouseHandler.saveClickY <= y + 36) {
							if (Client.tabInterfaceIDs[index] != -1) {
								Client.tabID = index;
								Client.needDrawTabArea = true;
								Client.tabAreaAltered = true;
							}
						} else if (stackTabs() && MouseHandler.saveClickY >= y + 36 && MouseHandler.saveClickY <= y + 36 + 36) {
							if (Client.tabInterfaceIDs[index + 7] != -1) {
								Client.tabID = index + 7;
								Client.needDrawTabArea = true;
								Client.tabAreaAltered = true;
							}
						}
					}
					x += 33;
				}
			}
		}
	}

	private void refreshMinimap(Sprite sprite, int j, int k) {
		int l = k * k + j * j;
		if (l > 4225 && l < 0x15f90) {
			int i1 = camAngleY + camAngleY & 0x7ff;
			int j1 = Model.SINE[i1];
			int k1 = Model.COSINE[i1];
			j1 = (j1 * 256) / (minimapZoom + 256);
			k1 = (k1 * 256) / (minimapZoom + 256);
			int l1 = j * j1 + k * k1 >> 16;
			int i2 = j * k1 - k * j1 >> 16;
			double d = Math.atan2(l1, i2);
		} else {
			markMinimap(sprite, k, j);
		}
	}

	public void rightClickChatButtons() {
		if (MouseHandler.mouseY >= canvasHeight - 22 && MouseHandler.mouseY <= canvasHeight) {
			if (MouseHandler.mouseX >= 5 && MouseHandler.mouseX <= 61) {
				insertMenuItemNoShift("View All",MenuAction.VIEW_ALL);
			} else if (MouseHandler.mouseX >= 71 && MouseHandler.mouseX <= 127) {
				insertMenuItemNoShift("View Game",MenuAction.VIEW_GAME);
			} else if (MouseHandler.mouseX >= 137 && MouseHandler.mouseX <= 193) {
				insertMenuItemNoShift("Hide public", MenuAction.HIDE_PUBLIC);
				insertMenuItemNoShift("Off public", MenuAction.OFF_PUBLIC);
				insertMenuItemNoShift("Friends public", MenuAction.FRIENDS_PUBLIC);
				insertMenuItemNoShift("On public", MenuAction.ON_PUBLIC);
				insertMenuItemNoShift("View public", MenuAction.VIEW_PUBLIC);
			} else if (MouseHandler.mouseX >= 203 && MouseHandler.mouseX <= 259) {
				insertMenuItemNoShift("Off private", MenuAction.OFF_PRIVATE);
				insertMenuItemNoShift("Friends private", MenuAction.FRIENDS_PRIVATE);
				insertMenuItemNoShift("On private", MenuAction.ON_PRIVATE);
				insertMenuItemNoShift("View private", MenuAction.VIEW_PRIVATE);
			} else if (MouseHandler.mouseX >= 269 && MouseHandler.mouseX <= 325) {
				insertMenuItemNoShift("Off clan chat", MenuAction.OFF_CLAN_CHAT);
				insertMenuItemNoShift("Friends clan chat", MenuAction.FRIENDS_CLAN_CHAT);
				insertMenuItemNoShift("On clan chat", MenuAction.ON_CLAN_CHAT);
				insertMenuItemNoShift("View clan chat", MenuAction.VIEW_CLAN_CHAT);
			} else if (MouseHandler.mouseX >= 335 && MouseHandler.mouseX <= 391) {
				insertMenuItemNoShift("Off trade", MenuAction.OFF_TRADE);
				insertMenuItemNoShift("Friends trade", MenuAction.FRIENDS_TRADE);
				insertMenuItemNoShift("On trade", MenuAction.ON_TRADE);
				insertMenuItemNoShift("View trade", MenuAction.VIEW_TRADE);
			} else if (MouseHandler.mouseX >= 404 && MouseHandler.mouseX <= 515) {
				insertMenuItemNoShift("Report Abuse", MenuAction.REPORT);
			}
		}
	}

	private boolean checkMainScreenBounds() {
		if (checkBounds(0, canvasWidth - (stackTabs() ? 231 : 462), canvasHeight - (stackTabs() ? 73 : 37),
				canvasWidth, canvasHeight)) {
			return false;
		}
		if (checkBounds(0, canvasWidth - 225, 0, canvasWidth, 170)) {
			return false;
		}
		if (checkBounds(0, canvasWidth - 204, canvasHeight - (stackTabs() ? 73 : 37) - 275, canvasWidth,
				canvasHeight)) {
			return false;
		}
		if (checkBounds(0, 0, canvasHeight - 168, 516, canvasHeight)) {
			return false;
		}
		return true;
	}

	public static boolean showChatComponents = true;
	public static boolean showTabComponents = true;
	public static boolean changeTabArea = false;
	public static boolean changeChatArea = false;

	public boolean getMousePositions() {
		if (mouseInRegion(canvasWidth - (canvasWidth <= 1000 ? 240 : 420),
				canvasHeight - (canvasWidth <= 1000 ? 90 : 37), canvasWidth, canvasHeight)) {
			return false;
		}
		if (showChatComponents) {
			if (changeChatArea) {
				if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 494
						&& MouseHandler.mouseY > canvasHeight - 175
						&& MouseHandler.mouseY < canvasHeight) {
					return true;
				} else {
					if (MouseHandler.mouseX > 494 && MouseHandler.mouseX < 515
							&& MouseHandler.mouseY > canvasHeight - 175
							&& MouseHandler.mouseY < canvasHeight) {
						return false;
					}
				}
			} else if (!changeChatArea) {
				if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 519
						&& MouseHandler.mouseY > canvasHeight - 175
						&& MouseHandler.mouseY < canvasHeight) {
					return false;
				}
			}
		}
		if (mouseInRegion(canvasWidth - 216, 0, canvasWidth, 172)) {
			return false;
		}
		if (!changeTabArea) {
			if (MouseHandler.mouseX > 0 && MouseHandler.mouseY > 0 && MouseHandler.mouseY < canvasWidth
					&& MouseHandler.mouseY < canvasHeight) {
				if (MouseHandler.mouseX >= canvasWidth - 242 && MouseHandler.mouseY >= canvasHeight - 335) {
					return false;
				}
				return true;
			}
			return false;
		}
		if (showTabComponents) {
			if (canvasWidth > 1000) {
				if (MouseHandler.mouseX >= canvasWidth - 420 && MouseHandler.mouseX <= canvasWidth
						&& MouseHandler.mouseY >= canvasHeight - 37
						&& MouseHandler.mouseY <= canvasHeight
						|| MouseHandler.mouseX > canvasWidth - 225 && MouseHandler.mouseX < canvasWidth
						&& MouseHandler.mouseY > canvasHeight - 37 - 274
						&& MouseHandler.mouseY < canvasHeight) {
					return false;
				}
			} else {
				if (MouseHandler.mouseX >= canvasWidth - 210 && MouseHandler.mouseX <= canvasWidth
						&& MouseHandler.mouseY >= canvasHeight - 74
						&& MouseHandler.mouseY <= canvasHeight
						|| MouseHandler.mouseX > canvasWidth - 225 && MouseHandler.mouseX < canvasWidth
						&& MouseHandler.mouseY > canvasHeight - 74 - 274
						&& MouseHandler.mouseY < canvasHeight) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkBounds(int type, int x1, int y1, int x2, int y2) {
		if (type == 0)
			return getMouseX() >= x1 && getMouseX() <= x2 && getMouseY() >= y1 && getMouseY() <= y2;
		else
			return Math.pow((x1 + type - x2), 2) + Math.pow((y1 + type - y2), 2) < Math.pow(type, 2);
	}

	public Point getOnScreenWidgetOffsets() {
		boolean newTab = true;
		int w = 512, h = 334;
		int x = (canvasWidth / 2) - (newTab ? 286 : 256);
		int y = (canvasHeight / 2) - 169;

		if (x + 65656 > (canvasWidth - 225)) {
			x = x - 90;
			if (x < 0) {
				x = 0;
			}
		}
		if (y + 565656 > (canvasHeight - 192)) {
			y = y - 83;
			if (y < 0) {
				y = 0;
			}
		}
		return new Point(x, y);
	}

	public boolean isFullscreenInterface(int id) {
		if(id == -1) {
			return false;
		}
		return false;
	}

	public static boolean stackTabs() {
		return !(canvasWidth >= 1100);
	}

	public void method3434() {
		menuOptionsCount = 0;
		isMenuOpen = false;
	}

	public void addCancelMenuEntry() {
		method3434();
		menuActions[0] = "Cancel";
		menuTargets[0] = "";
		menuOpcodes[0] = 1006;
		menuShiftClick[0] = false;
		menuOptionsCount = 1;
		onMenuOptionsChanged(0);
	}


	public void processRightClick() {

			if (activeInterfaceType != 0)
				return;
			addCancelMenuEntry();
			if (isResized()) {
				if (fullscreenInterfaceID != -1) {
					anInt886 = 0;
					anInt1315 = 0;
					buildInterfaceMenu((canvasWidth / 2) - 765 / 2,
							RSInterface.interfaceCache[fullscreenInterfaceID], MouseHandler.mouseX,
							(canvasHeight / 2) - 503 / 2, MouseHandler.mouseY, 0);
					if (anInt886 != anInt1026) {
						anInt1026 = anInt886;
					}
					if (anInt1315 != anInt1129) {
						anInt1129 = anInt1315;
					}
					return;
				}
			}
			buildSplitPrivateChatMenu();
			buildBroadcasts();
			anInt886 = 0;
			anInt1315 = 0;

			if (!isResized()) {
				if (isFullscreenInterface(openInterfaceID) ) {

					if(!isResized()) {
						buildInterfaceMenu(0, RSInterface.interfaceCache[openInterfaceID], MouseHandler.mouseX , 0, MouseHandler.mouseY - 4,0);
					} else {
						buildInterfaceMenu((int) getOnScreenWidgetOffsets().getX(), RSInterface.interfaceCache[openInterfaceID], MouseHandler.mouseX, (int) getOnScreenWidgetOffsets().getY(), MouseHandler.mouseY,0);
					}
				} else if (getMouseX() > 0 && getMouseY() > 0 && getMouseX() < 516 && getMouseY() < 343) {
					if (openInterfaceID != -1) {
						buildInterfaceMenu(0, RSInterface.interfaceCache[openInterfaceID], getMouseX(), 0, getMouseY(), 0);
					} else {
						createMenu();
					}
				}
			} else {
				if (checkMainScreenBounds()) {
					int interfaceX = (canvasWidth / 2) - 256 - 99;
					int interfaceY = (canvasHeight / 2) - 167 - 63;
					if (openInterfaceID != -1 && openInterfaceID != 44900) {
						buildInterfaceMenu(interfaceX, RSInterface.interfaceCache[openInterfaceID],
								getMouseX(), interfaceY, getMouseY(), 0);
					}

					if (openInterfaceID == -1 || getMouseX() < interfaceX || getMouseY() < interfaceY || getMouseX() > interfaceX + 516 || getMouseY() > interfaceY + 338) {
						createMenu();
					}
				}
			}

			if (anInt886 != anInt1026) {
				anInt1026 = anInt886;
			}
			if (anInt1315 != anInt1129) {
				anInt1129 = anInt1315;
			}

			anInt886 = 0;
			anInt1315 = 0;
			resetTabInterfaceHover();
			if (!isResized()) {
				if (getMouseX() > 516 && getMouseY() > 205 && getMouseX() < 765 && getMouseY() < 466) {
					if (invOverlayInterfaceID != 0) {
						buildInterfaceMenu(547, RSInterface.interfaceCache[invOverlayInterfaceID], getMouseX(), 205, getMouseY(),
								0);
					} else if (tabInterfaceIDs[tabID] != -1) {
						buildInterfaceMenu(547, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], getMouseX(), 205, getMouseY(),
								0);
					}
				}
			} else {
				int y = stackTabs() ? 73 : 37;
				if (getMouseX() > canvasWidth - 197 && getMouseY() > canvasHeight - 275 - y + 10
						&& getMouseX() < canvasWidth - 7 && getMouseY() < canvasHeight - y - 5) {
					if (invOverlayInterfaceID != 0 && !isFullscreenInterface(openInterfaceID)) {
						buildInterfaceMenu(canvasWidth - 197, RSInterface.interfaceCache[invOverlayInterfaceID],
								getMouseX(), canvasHeight - 275 - y + 10, getMouseY(), 0);
					} else if (tabInterfaceIDs[tabID] != -1 && !isFullscreenInterface(openInterfaceID)) {
						buildInterfaceMenu(canvasWidth - 197, RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
								getMouseX(), canvasHeight - 275 - y + 10, getMouseY(), 0);
					}
				}
			}
			if (anInt886 != anInt1048) {
				needDrawTabArea = true;
				tabAreaAltered = true;
				anInt1048 = anInt886;
			}
			if (anInt1315 != anInt1044) {
				needDrawTabArea = true;
				tabAreaAltered = true;
				anInt1044 = anInt1315;
			}
			anInt886 = 0;
			anInt1315 = 0;
			/* Chat area clicking */
			if (!isResized()) {
				if (getMouseX() > 0 && getMouseY() > 338 && getMouseX() < 490 && getMouseY() < 463) {
					if (backDialogID != -1)
						buildInterfaceMenu(20, RSInterface.interfaceCache[backDialogID], getMouseX(), 358, getMouseY(), 0);
					else if (getMouseY() < 463 && getMouseX() < 490)
						buildChatAreaMenu(getMouseY() - 338);
				}
			} else {
				if (getMouseX() > 0 && getMouseY() > canvasHeight - 165 && getMouseX() < 490 && getMouseY() < canvasHeight - 40) {
					if (backDialogID != -1)
						buildInterfaceMenu(20, RSInterface.interfaceCache[backDialogID], getMouseX(),
								canvasHeight - 145, getMouseY(), 0);
					else if (getMouseY() < canvasHeight - 40 && getMouseX() < 490)
						buildChatAreaMenu(getMouseY() - (canvasHeight - 165));
				}
			}
			if (backDialogID != -1 && anInt886 != anInt1039) {
				inputTaken = true;
				anInt1039 = anInt886;
			}
			if (backDialogID != -1 && anInt1315 != anInt1500) {
				inputTaken = true;
				anInt1500 = anInt1315;
			}
			/* Enable custom right click areas */
			if (anInt886 != anInt1026)
				anInt1026 = anInt886;
			anInt886 = 0;

			rightClickChatButtons();
			processMinimapActions();
			boolean finishedSorting = true;
			for (int menuIndex = 0; menuIndex < getMenuOptionCount() - 1; ++menuIndex) {
				if (getMenuOpcodes()[menuIndex] < 1000 && getMenuOpcodes()[menuIndex + 1] > 1000) {
					sortMenuEntries(menuIndex, menuIndex + 1);
					finishedSorting = false;
				}
			}
			if (finishedSorting && !isMenuOpen()) {
				getCallbacks().post(new PostMenuSort());
			}
	}

	private void processMinimapActions() {
		final boolean fixed = !isResized();
		if (fixed ? MouseHandler.mouseX >= 542 && MouseHandler.mouseX <= 579 && MouseHandler.mouseY >= 2 && MouseHandler.mouseY <= 38
			: MouseHandler.mouseX >= Client.canvasWidth - 180 && MouseHandler.mouseX <= Client.canvasWidth - 139
			&& MouseHandler.mouseY >= 0 && MouseHandler.mouseY <= 40) {
			HoverMenuManager.reset();
			insertMenuItemNoShift("Face North", MenuAction.FACE_NORTH);
		}
		if (counterHover && drawOrbs) {
			insertMenuItemNoShift("Reset @or1@XP total", 475);
			insertMenuItemNoShift(drawExperienceCounter ? "Hide @or1@XP drops" : "Show @or1@XP drops", 474);
		}
		if (worldHover && drawOrbs) {
			insertMenuItemNoShift("Fullscreen @or1@World Map", MenuAction.WORLD_MAP_FULLSCREEN);
			insertMenuItemNoShift("Floating @or1@World Map", MenuAction.WORLD_MAP);
		}

		if (runHover) {
			insertMenuItemNoShift("Toggle Run", MenuAction.TOGGLE_RUN);
		}
		if (prayHover && drawOrbs) {
			insertMenuItemNoShift("Setup Quick-Prayers", MenuAction.TOGGLE_QUICK_PRAYERS);
			insertMenuItemNoShift(prayClicked ? "Turn Quick Prayers off" : "Turn Quick Prayers on", MenuAction.SETUP_QUICK_PRAYER);
		}
	}

	private int method83(int i, int j, int k) {
		int l = 256 - k;
		return ((i & 0xff00ff) * l + (j & 0xff00ff) * k & 0xff00ff00)
				+ ((i & 0xff00) * l + (j & 0xff00) * k & 0xff0000) >> 8;
	}

	public boolean nameWhitespace() {
		if (myUsername != null && myUsername.startsWith(" ") || myUsername.endsWith(" ") || myUsername.contains("  ")) {
			firstLoginMessage = "Invalid username whitespace usage. Please try again.";
			return true;
		}
		return false;
	}
	public void login(String s, String s1, boolean flag) {
		if (loggedIn)
			return;
		if (Configuration.developerMode) {
			System.out.println("Attempting connection to " + server + ":" + port);
		}

		captcha = null;
		fogEnabled = false;
		fogOpacity = 0;
		firstLoginMessage = "Connecting to server..";

		resetTabInterfaceHover();

		try {
			if(myUsername.length() < 3) {
				firstLoginMessage = "Your username must be at least 3 characters long";
				return;
			}
			if (!flag) {
				drawLoginScreen();
			}
			setConfigButton(23103, informationFile.isRememberRoof() ? true : false);
			socketStream = new RSSocket( openSocket(port + portOff));
			long l = StringUtils.longForName(s);
			int i = (int) (l >> 16 & 31L);
			stream.currentPosition = 0;
			stream.writeUnsignedByte(14);
			stream.writeUnsignedByte(i);
			socketStream.queueBytes(2, stream.payload);
			for (int j = 0; j < 8; j++)
				socketStream.read();

			int responseCode = socketStream.read();
			int i1 = responseCode;
			if (responseCode == 0) {
				new Identity().createIdentity();
				try {
					new Identity().loadIdentity();
				} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
				}
				socketStream.flushInputStream(inStream.payload, 8);
				inStream.currentPosition = 0;
				aLong1215 = inStream.readQWord();

				// Pow request
				stream.currentPosition = 0;
				stream.writeUnsignedByte(19);
				socketStream.queueBytes(1, stream.payload);
				int powRequestReturnCode = socketStream.read();
				if (powRequestReturnCode == 60) {
					int size = ((socketStream.read()) << 8) + (socketStream.read() & 0xff);
					socketStream.flushInputStream(inStream.payload, size);
					inStream.currentPosition = 0;
					int randomValue = inStream.readUShort();
					int difficulty = inStream.readUShort();
					String seed = inStream.readNewString();

					// Pow process
					long start = System.currentTimeMillis();
					long answer = Hash.run(randomValue, difficulty, seed);
					long time = System.currentTimeMillis() - start;
					logger.debug("Took {}ms to finish POW.", time);

					// Pow check
					Buffer powStream = Buffer.create();
					powStream.writeByte(20); // Pow check opcode
					powStream.writeQWord(answer);
					this.socketStream.queueBytes(powStream.currentPosition, powStream.payload);

					int response = socketStream.read();
					if (response == 0) {
						socketStream.flushInputStream(inStream.payload, 8);
						inStream.currentPosition = 0;
						inStream.readQWord();
					}

					int ai[] = new int[4];
					ai[0] = (int) (Math.random() * 99999999D);
					ai[1] = (int) (Math.random() * 99999999D);
					ai[2] = (int) (aLong1215 >> 32);
					ai[3] = (int) aLong1215;
					stream.currentPosition = 0;
					stream.writeUnsignedByte(10);
					stream.writeDWord(ai[0]);
					stream.writeDWord(ai[1]);
					stream.writeDWord(ai[2]);
					stream.writeDWord(ai[3]);
					stream.writeDWord(2);
					stream.writeString(s);
					stream.writeString(s1);
					stream.writeString(captchaInput);
					stream.writeString(macAddress);
					stream.writeString(FingerPrint.getFingerprint());
					stream.doKeys();
					aStream_847.currentPosition = 0;
					if (flag)
						aStream_847.writeUnsignedByte(18);
					else
						aStream_847.writeUnsignedByte(16);
					aStream_847.writeUnsignedByte(stream.currentPosition + 36 + 1 + 1 + 2);
					aStream_847.writeUnsignedByte(255);
					aStream_847.writeWord(Configuration.CLIENT_VERSION);
					aStream_847.writeUnsignedByte(lowMem ? 1 : 0);
					for (int l1 = 0; l1 < 9; l1++)
						aStream_847.writeDWord(expectedCRCs[l1]);

					aStream_847.writeBytes(stream.payload, stream.currentPosition, 0);
					stream.encryption = new ISAACRandomGen(ai);
					for (int j2 = 0; j2 < 4; j2++)
						ai[j2] += 50;

					encryption = new ISAACRandomGen(ai);
					socketStream.queueBytes(aStream_847.currentPosition, aStream_847.payload);
					responseCode = socketStream.read();
				}
			}
			if (responseCode == 1) {
				try {
					Thread.sleep(2000L);
				} catch (Exception _ex) {
				}
				login(s, s1, flag);
				return;
			}
			if (responseCode == 2) {
				Arrays.stream(RSInterface.interfaceCache).filter(Objects::nonNull).forEach(rsi -> rsi.scrollPosition = 0);

				Preferences.getPreferences().updateClientConfiguration();
				Preferences.getPreferences().updateClientConfiguration();
				@SuppressWarnings("unused")
				int rights = socketStream.read();
				flagged = socketStream.read() == 1;
				logger.debug("Login accepted, rights={}, flagged={}", rights, flagged);
				aLong1220 = 0L;
				anInt1022 = 0;
				//mouseDetection.coordsIndex = 0;

				aBoolean954 = true;
				loggedIn = true;
				setGameState(GameState.LOGGING_IN);
				stream.currentPosition = 0;
				inStream.currentPosition = 0;
				incomingPacket = -1;
				dealtWithPacket = -1;
				previousPacket1 = -1;
				dealtWithPacketSize = -1;
				previousPacket2 = -1;
				previousPacketSize1 = -1;
				previousPacketSize2 = -1;
				packetSize = 0;
				anInt1009 = 0;
				anInt1104 = 0;
				anInt1011 = 0;
				hintArrowType = 0;
				menuOptionsCount = 0;
				isMenuOpen = false;
				fullscreenInterfaceID = -1;
				MouseHandler.idleCycles = 0;
				for (int j1 = 0; j1 < 500; j1++)
					chatMessages[j1] = null;

				itemSelected = 0;
				spellSelected = 0;
				setNorth();
				//setBounds();
				minimapState = 0;
				lastKnownPlane = -1;
				destX = 0;
				destY = 0;
				playerCount = 0;
				npcCount = 0;
				for (int i2 = 0; i2 < maxPlayers; i2++) {
					players[i2] = null;
					aStreamArray895s[i2] = null;
				}

				for (int k2 = 0; k2 < 16384; k2++)
					npcs[k2] = null;

				localPlayer = players[maxPlayerCount] = new Player();
				projectiles.clear();
				incompleteAnimables.clear();
				for (int l2 = 0; l2 < 4; l2++) {
					for (int i3 = 0; i3 < 104; i3++) {
						for (int k3 = 0; k3 < 104; k3++)
							groundItems[l2][i3][k3] = null;

					}

				}

				spawns = new NodeDeque();
				fullscreenInterfaceID = -1;
				anInt900 = 0;
				friendsCount = 0;
				dialogID = -1;
				backDialogID = -1;
				openInterfaceID = -1;
				invOverlayInterfaceID = 0;
				openWalkableWidgetID = -1;
				continuedDialogue = false;
				tabID = 3;
				inputDialogState = 0;
				isMenuOpen = false;
				messagePromptRaised = false;
				clickToContinueString = null;
				anInt1055 = 0;
				aBoolean1047 = true;
				method45();
				for (int j3 = 0; j3 < 5; j3++)
					anIntArray990[j3] = 0;

				for (int l3 = 0; l3 < 6; l3++) {
					playerOptions[l3] = null;
					playerOptionsHighPriority[l3] = false;
				}

				anInt1175 = 0;
				anInt1134 = 0;
				anInt986 = 0;
				anInt1288 = 0;
				anInt924 = 0;
				anInt1188 = 0;
				anInt1155 = 0;
				anInt1226 = 0;
				//setBounds();
				jagexNetThread.writePacket(false);
				drawChatArea();
				drawMinimap();
				drawTabArea();

				setGameState(GameState.LOGGED_IN);
				return;
			}
			if (responseCode == 3) {
				if (loginScreen.missingUsername() && loginScreen.missingPassword()) {
					firstLoginMessage = "Please enter a username & password.";
					return;
				} else if (loginScreen.missingUsername() && !loginScreen.missingPassword()) {
					firstLoginMessage = "Please enter a username.";
					return;
				}else if (loginScreen.missingPassword() && !loginScreen.missingUsername()) {
					firstLoginMessage = "Please enter a password.";
					return;
				}
				if (nameWhitespace()) {
					return;
				}
				firstLoginMessage = "Invalid username or password.";
				return;
			}
			if (responseCode == 4) {
				firstLoginMessage = "Your account has been disabled.";
				return;
			}
			if (responseCode == 5) {
				firstLoginMessage = "Your account is already logged in.";
				return;
			}
			if (responseCode == 6) {
				firstLoginMessage = Configuration.CLIENT_TITLE + " has been updated - restart the launcher!";
				return;
			}
			if (responseCode == 7) {
				firstLoginMessage = "This world is full.";
				return;
			}
			if (responseCode == 8) {
				firstLoginMessage = "Unable to connect.";
				return;
			}
			if (responseCode == 9) {
				firstLoginMessage = "Login limit exceeded.";
				return;
			}
			if (responseCode == 10) {
				firstLoginMessage = "Unable to connect. Bad session id.";
				return;
			}
			if (responseCode == 11) {
				firstLoginMessage = "Your username is too long.";
				return;
			}
			if (responseCode == 12) {
				firstLoginMessage = "Unknown error 12.";
				return;
			}
			if (responseCode == 13) {
				firstLoginMessage = "Too many connection attempts, please wait before trying again.";
				return;
			}
			if (responseCode == 14) {
				firstLoginMessage = Configuration.CLIENT_TITLE + " is currently being updated.";
				return;
			}
			if (responseCode == 15) {
				loggedIn = true;
				stream.currentPosition = 0;
				inStream.currentPosition = 0;
				incomingPacket = -1;
				dealtWithPacket = -1;
				previousPacket1 = -1;
				previousPacket2 = -1;
				packetSize = 0;
				anInt1009 = 0;
				anInt1104 = 0;
				menuOptionsCount = 0;
				isMenuOpen = false;
				longStartTime = System.currentTimeMillis();
				setGameState(GameState.LOGGED_IN);
				return;
			}
			if (responseCode == 16) {
				firstLoginMessage = "Unknown error 16.";
				return;
			}
			if (responseCode == 17) {
				firstLoginMessage = "Unknown error 17.";
				return;
			}
			if (responseCode == 20) {
				firstLoginMessage = "Unknown error 20.";
				return;
			}
			if (responseCode == 21) {
				for (int k1 = socketStream.read(); k1 >= 0; k1--) {

					drawLoginScreen();
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				login(s, s1, flag);
				return;
			}
			if (responseCode == 22) {
				firstLoginMessage = "Unknown error 22.";
				return;
			}
			if (responseCode == 23) {
				for (int k1 = socketStream.read(); k1 >= 0; k1--) {

					drawLoginScreen();
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				login(s, s1, flag);
				return;
			}
			if (responseCode == 24) {
				firstLoginMessage = "Unknown error 24.";
				return;
			}
			if (responseCode == 25) {
				firstLoginMessage = "Unknown error 25.";
				return;
			}
			if (responseCode == 26) {
				firstLoginMessage = "An error occurred while loading your file, contact support.";
				return;
			}
			if (responseCode == 27 || responseCode == 28) {
				try {
					int length = ((socketStream.read() & 0xFF) << 8) + socketStream.read();
					byte[] captchaData = new byte[length];
					for (int i12 = 0; i12 < length; i12++)
						captchaData[i12] = (byte) socketStream.read();
					captcha = new Sprite(BufferedImages.toBufferedImage(captchaData));
					captcha.setTransparency(45, 45, 45);
					firstLoginMessage = responseCode == 27 ? "Enter the captcha (case sensitive)." : "Incorrect, enter the captcha (case sensitive).";
					synchronized (CAPTCHA_LOCK) {
						captchaInput = "";
					}
				} catch (IOException e) {
					firstLoginMessage = "Captcha error occurred, contact staff.";
					e.printStackTrace();
				}

				return;
			}
			if (responseCode == -1) {
				if (i1 == 0) {
					if (loginFailures < 2) {
						try {
							Thread.sleep(2000L);
						} catch (Exception _ex) {
						}
						loginFailures++;
						login(s, s1, flag);
						return;
					}
				} else {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ignored) { }
					firstLoginMessage = "Error connecting to server, please try again.";
					return;
				}
			} else {
				return;
			}
		} catch (IOException _ex) {
			if (Configuration.developerMode)
				_ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug("Went to end of login method.");
		firstLoginMessage = "Xeros is being updated.";
	}


	// Original signature int clickType, int j, int k, int i1, int localY, int k1, int l1, int i2, int localX, boolean flag, int k2
	private boolean doWalkTo(int clickType, int localX, int localY, int j, int k, int i1, int k1, int l1, int i2, boolean flag, int k2) {
		byte byte0 = 104;
		byte byte1 = 104;
		for (int l2 = 0; l2 < byte0; l2++) {
			for (int i3 = 0; i3 < byte1; i3++) {
				anIntArrayArray901[l2][i3] = 0;
				travelDistances[l2][i3] = 0x5f5e0ff;
			}
		}
		int j3 = localX;
		int k3 = localY;
		anIntArrayArray901[localX][localY] = 99;
		travelDistances[localX][localY] = 0;
		int l3 = 0;
		int i4 = 0;
		bigX[l3] = localX;
		bigY[l3++] = localY;
		boolean flag1 = false;
		int j4 = bigX.length;
		int ai[][] = collisionMaps[plane].adjacencies;
		while (i4 != l3) {
			j3 = bigX[i4];
			k3 = bigY[i4];
			i4 = (i4 + 1) % j4;
			if (j3 == k2 && k3 == i2) {
				flag1 = true;
				break;
			}
			if (i1 != 0) {
				if ((i1 < 5 || i1 == 10) && collisionMaps[plane].obstruction_wall(k2, j3, k3, j, i1 - 1, i2)) {
					flag1 = true;
					break;
				}
				if (i1 < 10 && collisionMaps[plane].obstruction_decor(k2, i2, k3, i1 - 1, j, j3)) {
					flag1 = true;
					break;
				}
			}
			if (k1 != 0 && k != 0 && collisionMaps[plane].obstruction(i2, k2, j3, k, l1, k1, k3)) {
				flag1 = true;
				break;
			}
			int l4 = travelDistances[j3][k3] + 1;
			if (j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3] = 2;
				travelDistances[j3 - 1][k3] = l4;
			}
			if (j3 < byte0 - 1 && anIntArrayArray901[j3 + 1][k3] == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3] = 8;
				travelDistances[j3 + 1][k3] = l4;
			}
			if (k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3][k3 - 1] = 1;
				travelDistances[j3][k3 - 1] = l4;
			}
			if (k3 < byte1 - 1 && anIntArrayArray901[j3][k3 + 1] == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3][k3 + 1] = 4;
				travelDistances[j3][k3 + 1] = l4;
			}
			if (j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0 && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0
					&& (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3 - 1] = 3;
				travelDistances[j3 - 1][k3 - 1] = l4;
			}
			if (j3 < byte0 - 1 && k3 > 0 && anIntArrayArray901[j3 + 1][k3 - 1] == 0
					&& (ai[j3 + 1][k3 - 1] & 0x1280183) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0
					&& (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3 - 1] = 9;
				travelDistances[j3 + 1][k3 - 1] = l4;
			}
			if (j3 > 0 && k3 < byte1 - 1 && anIntArrayArray901[j3 - 1][k3 + 1] == 0
					&& (ai[j3 - 1][k3 + 1] & 0x1280138) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0
					&& (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3 + 1] = 6;
				travelDistances[j3 - 1][k3 + 1] = l4;
			}
			if (j3 < byte0 - 1 && k3 < byte1 - 1 && anIntArrayArray901[j3 + 1][k3 + 1] == 0
					&& (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0
					&& (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3 + 1] = 12;
				travelDistances[j3 + 1][k3 + 1] = l4;
			}
		}
		anInt1264 = 0;
		if (!flag1) {
			if (flag) {
				int i5 = 100;
				for (int k5 = 1; k5 < 2; k5++) {
					for (int i6 = k2 - k5; i6 <= k2 + k5; i6++) {
						for (int l6 = i2 - k5; l6 <= i2 + k5; l6++)
							if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && travelDistances[i6][l6] < i5) {
								i5 = travelDistances[i6][l6];
								j3 = i6;
								k3 = l6;
								anInt1264 = 1;
								flag1 = true;
							}

					}

					if (flag1)
						break;
				}

			}
			if (!flag1)
				return false;
		}
		i4 = 0;
		bigX[i4] = j3;
		bigY[i4++] = k3;
		int l5;
		for (int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != localX || k3 != localY; j5 = anIntArrayArray901[j3][k3]) {
			if (j5 != l5) {
				l5 = j5;
				bigX[i4] = j3;
				bigY[i4++] = k3;
			}
			if ((j5 & 2) != 0)
				j3++;
			else if ((j5 & 8) != 0)
				j3--;
			if ((j5 & 1) != 0)
				k3++;
			else if ((j5 & 4) != 0)
				k3--;
		}
		// if(cancelWalk) { return i4 > 0; }

		if (i4 > 0) {
			int k4 = i4;
			if (k4 > 25)
				k4 = 25;
			i4--;
			int k6 = bigX[i4];
			int i7 = bigY[i4];
			anInt1288 += k4;
			if (anInt1288 >= 92) {
				stream.createFrame(36);
				stream.writeDWord(0);
				anInt1288 = 0;
			}
			if (clickType == 0) {
				stream.createFrame(164);
				stream.writeUnsignedByte(k4 + k4 + 3);
			}
			if (clickType == 1) {
				stream.createFrame(248);
				stream.writeUnsignedByte(k4 + k4 + 3 + 14);
			}
			if (clickType == 2) {
				stream.createFrame(98);
				stream.writeUnsignedByte(k4 + k4 + 3);
			}
			stream.method433(k6 + baseX);
			destX = bigX[0];
			destY = bigY[0];
			for (int j7 = 1; j7 < k4; j7++) {
				i4--;
				stream.writeUnsignedByte(bigX[i4] - k6);
				stream.writeUnsignedByte(bigY[i4] - i7);
			}

			stream.method431(i7 + baseY);
			stream.method424(0);
			return true;
		}
		return clickType != 1;
	}
	public void updateNpcState(Buffer stream) {
		for (int j = 0; j < anInt893; j++) {
			int k = anIntArray894[j];
			Npc npc = npcs[k];
			int l = stream.readUShort();
			if((l & 0x100) != 0)
			{
				npc.anInt1543 = stream.readUnsignedByte();
				npc.anInt1545 = stream.readUnsignedByte();
				npc.anInt1544 = stream.readUnsignedByte();
				npc.anInt1546 = stream.readUnsignedByte();
				npc.initiate_movement = stream.readUShort() + loopCycle;
				npc.cease_movement = stream.readUShort() + loopCycle;
				npc.forceMovementDirection = stream.readUnsignedByte();
				npc.method446();
			}
			if ((l & 0x10) != 0) {
				int i1 = stream.method434();
				if (i1 == 65535)
					i1 = -1;
				int i2 = stream.readUnsignedByte();
				if (i1 == npc.sequence && i1 != -1) {
					int l2 = SequenceDefinition.get(i1).delayType;
					if (l2 == 1) {
						npc.sequenceFrame = 0;
						npc.sequenceFrameCycle = 0;
						npc.sequenceDelay = i2;
						npc.currentAnimationLoops = 0;
					}
					if (l2 == 2)
						npc.currentAnimationLoops = 0;
				} else if (i1 == -1 || npc.sequence == -1
					|| SequenceDefinition.get(i1).forcedPriority >= SequenceDefinition.get(npc.sequence).forcedPriority) {
					npc.sequence = i1;
					npc.sequenceFrame = 0;
					npc.sequenceFrameCycle = 0;
					npc.sequenceDelay = i2;
					npc.currentAnimationLoops = 0;
					npc.stationaryPathPosition = npc.smallXYIndex;
				}
			}
			if ((l & 8) != 0) {
				npc.currentHealth = stream.readUShort();
				npc.maxHealth = stream.readUShort();

				npc.addHitSplat(stream);
				npc.addHealthBar(stream);

			}
			if ((l & 0x80) != 0) {
				npc.readSpotAnimation(stream);
			}
			if ((l & 0x20) != 0) {
				npc.interactingEntity = stream.readUShort();
				if (npc.interactingEntity == 65535)
					npc.interactingEntity = -1;
			}
			if ((l & 1) != 0) {
				npc.spokenText = stream.readString();
				npc.overheadTextCyclesRemaining = 100;
			}
			if ((l & 0x40) != 0) {
				npc.currentHealth = stream.readUShort();
				npc.maxHealth = stream.readUShort();

				npc.addHitSplat(stream);
				npc.addHealthBar(stream);
			}
			if ((l & 2) != 0) {
				npc.desc = NpcDefinition.get(stream.method436());
				npc.size = npc.desc.size;
				npc.rotation = npc.desc.rotationSpeed;
				npc.walkSequence = npc.desc.walkingAnimation;
				npc.walkBackSequence = npc.desc.rotate180Animation;
				npc.walkLeftSequence = npc.desc.rotate90LeftAnimation;
				npc.walkRightSequence = npc.desc.rotate90RightAnimation;
				npc.idleSequence = npc.desc.standingAnimation;
			}
			if ((l & 4) != 0) {
				npc.anInt1538 = stream.method434();
				npc.anInt1539 = stream.method434();
			}
		}
	}

	private long debugDelay;

	public void buildAtNPCMenu(NpcDefinition entityDef, int i, int j, int k) {
		if (menuOptionsCount >= 400)
			return;
		if (entityDef.transforms != null)
			entityDef = entityDef.morph();
		if (entityDef == null)
			return;
		if (!entityDef.isInteractable)
			return;
		String s = entityDef.name;
		if (entityDef.combatLevel != 0)
			s = s + combatDiffColor(localPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel
					+ ")";
		HoverMenuManager.reset();
		if (itemSelected == 1) {
			insertMenuItemNoShift("Use " + selectedItemName + " with @yel@" + s,MenuAction.ITEM_USE_ON_NPC,i,k,j);
			return;
		}
		if (spellSelected == 1) {
			if ((spellUsableOn & 2) == 2) {
				insertMenuItemNoShift(spellTooltip + " @yel@" + s,MenuAction.SPELL_CAST_ON_NPC,i,k,j);
			}
		} else {
			for (int l = 4; l >= 0; l--)
				if (entityDef.actions[l] != null
						&& !entityDef.actions[l].equalsIgnoreCase("attack"))
				{
					MenuAction type = MenuAction.WALK;
					if (l == 0)
						type = MenuAction.NPC_FIRST_OPTION;
					if (l == 1)
						type = MenuAction.NPC_SECOND_OPTION;
					if (l == 2)
						type = MenuAction.NPC_THIRD_OPTION;
					if (l == 3)
						type = MenuAction.NPC_FOURTH_OPTION;
					if (l == 4)
						type = MenuAction.NPC_FIFTH_OPTION;

					insertMenuItemNoShift(entityDef.actions[l] + " @yel@" + s,type,i,k,j);
				}
		}
		if (entityDef.actions != null) {
			for (int i1 = 4; i1 >= 0; i1--) {
				if (entityDef.actions[i1] != null && entityDef.actions[i1].equalsIgnoreCase("attack")) {
					char c = '\0';
					if (Configuration.npcAttackOptionPriority == 3)
						continue;
					if (Configuration.npcAttackOptionPriority == 0 && entityDef.combatLevel > localPlayer.combatLevel
							|| Configuration.npcAttackOptionPriority == 1)
						c = '\u07D0';
					int type = MenuAction.WALK.getId();
					if (i1 == 0)
						type = 20 + c;
					if (i1 == 1)
						type = 412 + c;
					if (i1 == 2)
						type = 225 + c;
					if (i1 == 3)
						type = 965 + c;
					if (i1 == 4)
						type = 478 + c;
					insertMenuItemNoShift(entityDef.actions[i1] + " @yel@" + s,type,i,k,j);
				}
			}

		}
		insertMenuItemNoShift("Examine @yel@" + s,MenuAction.EXAMINE_NPC,i,k,j);
	}


	public void buildAtPlayerMenu(int tileX, int playerId, Player player, int tileY) {
		if (player == localPlayer)
			return;
		if (!player.visible)
			return;
		if (menuOptionsCount >= 400)
			return;
		String s;
		if (player.title.length() < 0)
			s = player.displayName + combatDiffColor(localPlayer.combatLevel, player.combatLevel) + " (level: "
					+ player.combatLevel + ")";
		else if (player.title.length() != 0)
			s = "@or1@" + player.title + "@whi@ " + player.displayName
					+ combatDiffColor(localPlayer.combatLevel, player.combatLevel) + " (level: " + player.combatLevel
					+ ")";
		else
			s = player.displayName + combatDiffColor(localPlayer.combatLevel, player.combatLevel) + " (level: "
					+ player.combatLevel + ")";
		HoverMenuManager.reset();
		if (itemSelected == 1) {
			Client.instance.insertMenuItemNoShift("Use " + selectedItemName + " with @whi@" + s,MenuAction.ITEM_USE_ON_PLAYER,playerId,tileX,tileY);
		} else if (spellSelected == 1) {
			if ((spellUsableOn & 8) == 8) {
				Client.instance.insertMenuItemNoShift(spellTooltip + " @whi@" + s,MenuAction.SPELL_CAST_ON_PLAYER,playerId,tileX,tileY);
			}
		} else {
			for (int type = 5; type >= 0; type--)
				if (playerOptions[type] != null) {
					char c = '\0';
					if (playerOptions[type].equalsIgnoreCase("attack")) {
						if (Configuration.playerAttackOptionPriority == 3)
							continue;
						if (Configuration.playerAttackOptionPriority == 0 && player.combatLevel > localPlayer.combatLevel
								|| Configuration.playerAttackOptionPriority == 1)
							c = '\u07D0';
						if (localPlayer.team != 0 && player.team != 0)
							if (localPlayer.team == player.team)
								c = '\u07D0';
							else
								c = '\0';
					} else if (playerOptionsHighPriority[type])
						c = '\u07D0';
					int type0 = 0;
					if (type == 0) {
						type0 = 561 + c;
					} else if (type == 1) {
						type0 = 779 + c;
					} else if (type == 2) {
						type0 = 27 + c;
					} else if (type == 3) {
						type0 = 577 + c;
					} else if (type == 4) {
						type0 = 729 + c;
					} else if (type == 5) {
						if (localPlayer.hasRightsBetween(0, 4)) {
							menuOpcodes[menuOptionsCount] = 745 + c;
						} else {
							continue;
						}
					} else if (type == 6) {
						type0 = 50 + c;
					} else if (type == 7) {
						type0 = 51 + c;
					}
					MenuAction action = MenuAction.of(type0);
					Client.instance.insertMenuItemNoShift(playerOptions[type] + " @whi@" + s, action, playerId, tileX, tileY);
				}
		}
	}

	private void handleTemporaryObjects(SpawnedObject spawnedObject) {
		long id = 0L;
		int key = -1;
		int type = 0;
		int orientation = 0;
		if (spawnedObject.group == 0)
			id = scene.getBoundaryObjectTag(spawnedObject.plane, spawnedObject.x, spawnedObject.y);
		if (spawnedObject.group == 1)
			id = scene.getWallDecorationTag(spawnedObject.plane, spawnedObject.x, spawnedObject.y);
		if (spawnedObject.group == 2)
			id = scene.getGameObjectTag(spawnedObject.plane, spawnedObject.x, spawnedObject.y);
		if (spawnedObject.group == 3)
			id = scene.getFloorDecorationTag(spawnedObject.plane, spawnedObject.x, spawnedObject.y);
		if (id != 0L) {
			int flags = scene.getObjectFlags(spawnedObject.plane, spawnedObject.x, spawnedObject.y, id);
			key = ObjectKeyUtil.getObjectId(id);
			type = ObjectKeyUtil.getObjectType(flags);
			orientation = ObjectKeyUtil.getObjectOrientation(flags);
		}
		spawnedObject.getPreviousId = key;
		spawnedObject.previousType = type;
		spawnedObject.previousOrientation = orientation;
	}

	public void objectFill(final int objectId, final int x1, final int y1, final int x2, final int y2, final int type,
						   final int face, final int height) {
		// if(height == height) // dunno your height variable but refactor
		// yourself
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				addObject(objectId, x, y, face, type, height);
			}
		}
	}

	public void addObject(int objectId, int x, int y, int face, int type, int height) {
		int mX = currentRegionX - 6;
		int mY = currentRegionY - 6;
		int x2 = x - (mX * 8);
		int y2 = y - (mY * 8);
		int i15 = 40 >> 2;
		int l17 = objectTypes[i15];
		if (y2 > 0 && y2 < 103 && x2 > 0 && x2 < 103) {
			requestSpawnObject(-1, objectId, face, l17, y2, type, height, x2, 0);
		}
	}


	public void load_objects() {
		/**
		 * @link addObject objectId, x, y, face, type, height
		 */

		addObject(0, 1544, 3687, 0, 10, 0);
		// Carts
		addObject(7029, 1656, 3542, 0, 10, 0); // Hos
		addObject(7029, 1591, 3621, 0, 10, 0); // Shay
		addObject(7029, 1760, 3710, 0, 10, 0); // Pisc
		addObject(7029, 1254, 3548, 0, 10, 0); // Qu
		addObject(7029, 1670, 3832, 0, 10, 0); // Arc
		addObject(7029, 1518, 3732, 0, 10, 0); // love
		// Abyss
		addObject(24101, 3039, 4834, 0, 10, 0);
		addObject(13642, 3039, 4831, 2, 10, 0);

		// Home objects

		addObject(2030, 1716, 3469, 1, 10, 0);
		addObject(2030, 1672, 3745, 3, 10, 0);

		addObject(28900, 1547, 3921, 1, 10, 0);
		addObject(172, 1619, 3659, 3, 10, 0); // ckey
		addObject(6552, 1647, 3676, 3, 10, 0); // anchients
		addObject(10321, 1663, 3635, 3, 10, 0); // molehole
		addObject(3828, 1845, 3810, 3, 10, 0); // kqtunnel
		addObject(882, 1422, 3586, 3, 10, 0); // gw
		// addObject(4150, 3089, 3495, 3, 10, 0); //warriors guild
		addObject(1738, 1661, 3529, 1, 10, 0); // Tavelery
		// addObject(4151, 3089, 3494, 1, 10, 0); //barrows
		addObject(20877, 1572, 3657, 1, 10, 0); // brimhaven
		addObject(26709, 1280, 3551, 1, 10, 0); // stronghold slayer
		addObject(2123, 1257, 3501, 3, 10, 0); // rellekka slayer
		// addObject(11803, 1650, 3619, 0, 10, 0); //icedung
		addObject(-1, 3008, 9550, 0, 10, 0); // iceexit
		addObject(2268, 3009, 9553, 0, 10, 0); // icedungexit
		addObject(29734, 1349, 3591, 0, 10, 0); // demonicentrance
		addObject(2823, 1792, 3708, 0, 10, 0); // mith entrance
		addObject(4152, 1547, 3570, 1, 10, 0); // corpent
		addObject(4153, 1547, 3567, 1, 10, 0); // corpext
		addObject(4154, 1476, 3688, 1, 10, 0); // lizards
		addObject(4154, 1454, 3693, 1, 10, 0); // lizards
		// addObject(4155, 3089, 3496, 1, 10, 0); //zulrah
		addObject(8356, 1626, 3680, 3, 10, 0); // spirittreekourend
		addObject(8356, 1268, 3561, 0, 10, 0); // spirittreeMountQ
		addObject(8356, 1315, 3619, 0, 10, 0); // spirittreeXeric
		addObject(8356, 1477, 3555, 0, 10, 0); // spirittreeHeros
		addObject(11835, 1213, 3539, 1, 10, 0); // tzhaar
		addObject(678, 1605, 3707, 3, 10, 0); // CorpPortal
		addObject(2544, 1672, 3557, 1, 10, 0); // Dagentrence

		// wildysigns
		addObject(14503, 1465, 3704, 2, 10, 0); // wildysign
		addObject(14503, 1470, 3704, 2, 10, 0); // wildysign
		addObject(14503, 1469, 3704, 2, 10, 0); // wildysign
		addObject(14503, 1468, 3704, 2, 10, 0); // wildysign
		// 2=s,1=e
		// RFD
		// Resource area
		addObject(9030, 3190, 3929, 1, 10, 0);
		addObject(9030, 3191, 3930, 1, 10, 0);
		addObject(9030, 3190, 3931, 1, 10, 0);


		// Barrows
		addObject(3610, 3550, 9695, 0, 10, 0);

		// Moonclan
		objectFill(0, 2097, 3854, 2097, 3954, 10, 0, 0);

		// addObject(11731, 3003, 3384, 0, 10, 0); //Gem Fally

		// Crafting guild
		// objectFill(-1, 2939, 3282, 2943, 3290, 10, 0, 0); // Crafting guild
		addObject(-1, 2938, 3285, 0, 10, 0);
		addObject(-1, 2937, 3284, 0, 10, 0);
		addObject(-1, 2942, 3291, 0, 10, 0);


		/**
		 * Shilo
		 */
		// obelsisk reg
		addObject(2153, 1506, 3869, 0, 10, 0); // fire
		addObject(2152, 1600, 3632, 0, 10, 0); // air
		addObject(2151, 1793, 3704, 0, 10, 0); // water
		addObject(2150, 1240, 3538, 0, 10, 0); // earth

		// Bank room
		addObject(1113, 2850, 2952, 0, 10, 0); // Chair
		objectFill(24101, 2851, 2952, 2853, 2952, 10, 0, 0); // Bank

		// Vet
		//addObject(0, 3189, 3784, 0, 10, 0);
		//addObject(0, 3189, 3782, 0, 10, 0);
		//addObject(0, 3188, 3783, 0, 10, 0);
		//addObject(0, 3191, 3784, 0, 10, 0);
		//addObject(-1, 3189, 3783, 0, 10, 0);
		/**
		 * @link objectFill objectId, SWX, SWY, NEX, NEY, type, face, height
		 */
		objectFill(-1, 3010, 3371, 3014, 3375, 10, 0, 0);
		objectFill(-1, 1748, 5322, 1755, 5327, 22, 0, 1);
		objectFill(14896, 3010, 3372, 3014, 3374, 10, 0, 0);
		objectFill(14896, 3011, 3371, 3013, 3371, 10, 0, 0);
		objectFill(14896, 3011, 3375, 3013, 3375, 10, 0, 0);

	}

	public static String fontFilter() {
		if (Configuration.newFonts) {
			return "_2";
		}
		return "";
	}

	public FileArchive interface_archive;
	public FileArchive mediaStreamLoader;

	private static GraphicsDefaults spriteIds;
	public static FontLoader fontLoader;

	void load() {
		try {
			int loadingProgress;
			if (Client.titleLoadingStage == 0) {
				drawLoadingText(1, "Starting game engine...");
				tileFlags = new byte[4][104][104];
				tileHeights = new int[4][105][105];
				scene = new SceneGraph(tileHeights);
				for (int j = 0; j < 4; j++) {
					collisionMaps[j] = new CollisionMap(104,104);
				}
				titleStreamLoader = loadArchive("title.dat");
				drawLoadingText(2, "Loading Fonts...");
				smallText = new TextDrawingArea(false, "p11_full" + fontFilter(), titleStreamLoader);
				XPFONT = new TextDrawingArea(true, "q8_full" + fontFilter(), titleStreamLoader);
				aTextDrawingArea_1271 = new TextDrawingArea(false, "p12_full" + fontFilter(), titleStreamLoader);
				chatTextDrawingArea = new TextDrawingArea(false, "b12_full", titleStreamLoader);
				drawLoadingText(3, "Loading Fonts...");
				aTextDrawingArea_1273 = new TextDrawingArea(true, "q8_full" + fontFilter(), titleStreamLoader);
				newSmallFont = new RSFont(false, "p11_full" + fontFilter(), titleStreamLoader);
				newRegularFont = new RSFont(false, "p12_full" + fontFilter(), titleStreamLoader);
				newBoldFont = new RSFont(false, "b12_full" + fontFilter(), titleStreamLoader);
				newFancyFont = new RSFont(true, "q8_full" + fontFilter(), titleStreamLoader);

				lato = new RSFont(true, "lato_full", titleStreamLoader);
				latoBold = new RSFont(true, "lato_bold_full", titleStreamLoader);
				kingthingsPetrock = new RSFont(true, "kingthings_petrock_full", titleStreamLoader);
				kingthingsPetrockLight = new RSFont(true, "kingthings_petrock_light_full", titleStreamLoader);

				interface_archive = loadArchive("interface.dat");
				this.mediaStreamLoader = loadArchive("media.dat");
				SpriteLoader1.loadSprites();
				cacheSprite1 = SpriteLoader1.sprites;
				SpriteLoader1.sprites = null;

				SpriteLoader2.loadSprites();
				cacheSprite2 = SpriteLoader2.sprites;
				SpriteLoader2.sprites = null;

				SpriteLoader3.loadSprites();
				cacheSprite3 = SpriteLoader3.sprites;
				SpriteLoader3.sprites = null;

				SpriteLoader4.loadSprites();
				cacheSprite4 = SpriteLoader4.sprites;
				SpriteLoader4.sprites = null;

				Client.titleLoadingStage = 20;
			} else if (Client.titleLoadingStage == 20) {
				drawLoadingText(4, "Prepared visibility map");
				Client.titleLoadingStage = 30;
			} else if (Client.titleLoadingStage == 30) {
				Js5List.animations = Js5System.createJs5(Js5ArchiveIndex.ANIMATIONS, false, true, true, false);
				Js5List.skeletons = Js5System.createJs5(Js5ArchiveIndex.SKELETONS, false, true, true, false);
				Js5List.configs = Js5System.createJs5(Js5ArchiveIndex.CONFIGS, true, false, true, false);
				Js5List.interfaces = Js5System.createJs5(Js5ArchiveIndex.INTERFACES, false, true, true, false);
				Js5List.soundEffects = Js5System.createJs5(Js5ArchiveIndex.SOUNDEFFECTS, false, true, true, false);
				Js5List.maps = Js5System.createJs5(Js5ArchiveIndex.MAPS, true, true, true, false);
				Js5List.musicTracks = Js5System.createJs5(Js5ArchiveIndex.MUSIC_TRACKS, true, true, true, false);
				Js5List.models = Js5System.createJs5(Js5ArchiveIndex.MODELS, false, true, true, false);
				Js5List.sprites = Js5System.createJs5(Js5ArchiveIndex.SPRITES, false, true, true, false); //here it creates the socket to read from the disk
				Js5List.textures = Js5System.createJs5(Js5ArchiveIndex.TEXTURES, false, true, true, false);
				Js5List.binary = Js5System.createJs5(Js5ArchiveIndex.BINARY, false, true, true, false);
				Js5List.musicJingles = Js5System.createJs5(Js5ArchiveIndex.MUSIC_JINGLES, false, true, true, false);
				Js5List.clientScript = Js5System.createJs5(Js5ArchiveIndex.CLIENTSCRIPT, false, true, true, false);
				Js5List.fonts = Js5System.createJs5(Js5ArchiveIndex.FONTS, true, false, true, false);
				Js5List.musicSamples = Js5System.createJs5(Js5ArchiveIndex.MUSIC_SAMPLES, false, true, true, false);
				Js5List.musicPatches = Js5System.createJs5(Js5ArchiveIndex.MUSIC_PATCHES, false, true, true, false);
				Js5List.archive17 = Js5System.createJs5(Js5ArchiveIndex.ARCHIVE_17, true, true, true, false);
				Js5List.worldmapGeography = Js5System.createJs5(Js5ArchiveIndex.WORLDMAP_GEOGRAPHY, false, true, true, false);
				Js5List.worldmap = Js5System.createJs5(Js5ArchiveIndex.WORLDMAP, false, true, true, false);
				Js5List.worldmapGround = Js5System.createJs5(Js5ArchiveIndex.WORLDMAP_GROUND, false, true, true, false);
				Js5List.dbtableindex = Js5System.createJs5(Js5ArchiveIndex.DBTABLEINDEX, false, true, true, true);
				drawLoadingText(5, "Connecting to update server");
				titleLoadingStage = 40;

			} else if (Client.titleLoadingStage == 40) {
				byte var24 = 0;
				loadingProgress = var24 + Js5List.animations.percentage() * 4 / 100;
				loadingProgress += Js5List.sprites.percentage() * 4 / 100;
				loadingProgress += Js5List.configs.percentage() * 2 / 100;
				loadingProgress += Js5List.interfaces.percentage() * 2 / 100;
				loadingProgress += Js5List.soundEffects.percentage() * 6 / 100;
				loadingProgress += Js5List.maps.percentage() * 4 / 100;
				loadingProgress += Js5List.musicTracks.percentage() * 2 / 100;
				loadingProgress += Js5List.models.percentage() * 55 / 100;
				loadingProgress += Js5List.sprites.percentage() * 2 / 100;
				loadingProgress += Js5List.textures.percentage() * 2 / 100;
				loadingProgress += Js5List.binary.percentage() * 2 / 100;
				loadingProgress += Js5List.musicJingles.percentage() * 2 / 100;
				loadingProgress += Js5List.clientScript.percentage() * 2 / 100;
				loadingProgress += Js5List.fonts.percentage() * 2 / 100;
				loadingProgress += Js5List.musicSamples.percentage() * 2 / 100;
				loadingProgress += Js5List.musicPatches.percentage() * 2 / 100;
				loadingProgress += Js5List.archive17.isLoading() && Js5List.archive17.isFullyLoaded() ? 1 : 0;
				loadingProgress += Js5List.worldmapGeography.percentage() / 100;
				loadingProgress += Js5List.worldmap.percentage() / 100;
				loadingProgress += Js5List.worldmapGround.percentage() / 100;
				loadingProgress += Js5List.dbtableindex.percentage() / 100;

				if (loadingProgress != 100) {
					if (loadingProgress != 0) {
						drawLoadingText(10, "Checking for updates - " + loadingProgress + "%");
					}
				} else {
					Js5System.init(Js5List.animations, "Animations");
					Js5System.init(Js5List.skeletons, "Skeletons");
					Js5System.init(Js5List.soundEffects, "Sound FX");
					Js5System.init(Js5List.maps, "Maps");
					Js5System.init(Js5List.musicTracks, "Music Tracks");
					Js5System.init(Js5List.models, "Models");
					Js5System.init(Js5List.sprites, "Sprites");
					Js5System.init(Js5List.textures, "Textures");
					Js5System.init(Js5List.musicJingles, "Music Jingles");
					Js5System.init(Js5List.musicSamples, "Music Samples");
					Js5System.init(Js5List.musicPatches, "Music Patches");
					Js5System.init(Js5List.worldmap, "World Map");
					Js5System.init(Js5List.worldmapGeography, "World Map Geography");
					Js5System.init(Js5List.worldmapGround, "World Map Ground");

					spriteIds = new GraphicsDefaults();
					spriteIds.decode(Js5List.archive17);
					drawLoadingText(15, "Loaded update list");
					Client.titleLoadingStage = 45;
				}
			} else if (Client.titleLoadingStage == 45) {
				StaticSound.setup(!Client.lowMem, Js5List.soundEffects, Js5List.musicTracks, Js5List.musicJingles, Js5List.musicSamples, Js5List.musicPatches);
				drawLoadingText(20, "Prepared sound engine");
				Client.titleLoadingStage = 50;
			} else if (Client.titleLoadingStage == 50) {
				loadingProgress = FontInfo.initialFonts().length;
				fontLoader = new FontLoader();
				fontLoader.createMap();
				if (fontLoader.fonts.size() != loadingProgress) {
					drawLoadingText(25,"Loading fonts - " + fontLoader.fonts.size() * 100 / loadingProgress + "%");
				} else {
					smallFontOSRS = fontLoader.fonts.get(FontInfo.SMALL_FONT);
					regularFontOSRS = fontLoader.fonts.get(FontInfo.REGULAR_FONT);
					boldFontOSRS = fontLoader.fonts.get(FontInfo.BOLD_FONT);
					fancyFontOSRS = fontLoader.fonts.get(FontInfo.FANCY_SMALL);
					fancyFontMediumOSRS = fontLoader.fonts.get(FontInfo.FANCY_MEDIUM);
					fancyFontLargeOSRS = fontLoader.fonts.get(FontInfo.FANCY_CAPS_LARGE);

					drawLoadingText(25, "Loading fonts");
					Client.titleLoadingStage = 60;
				}

			} else {
				int var3;
				if (Client.titleLoadingStage == 60) {
					drawLoadingText(30, "Loaded title screen");
					setGameState(5);
					//setStartupActions();
					Client.titleLoadingStage = 70;
				} else if (Client.titleLoadingStage == 70) {
					if (!Js5List.configs.isFullyLoaded()) {
						drawLoadingText(40, "Loading config - " + Js5List.configs.loadPercent() + "%");
					} else if (!Js5List.configs.isFullyLoaded()) {
						drawLoadingText(40, "Loading config - " + (80 + Js5List.clientScript.loadPercent() / 6) + "%");
					} else {
						Js5List.initConfigSizes();
						NpcDefinition.init(spriteIds.headIconArchive);

						if (Js5List.configs.isFullyLoaded()) {
							AreaDefinition.definitions = new AreaDefinition[Js5List.getConfigSize(Js5ConfigType.AREA)];

							for (int count = 0; count < Js5List.getConfigSize(Js5ConfigType.AREA); ++count) {
								byte[] fileData = Js5List.configs.takeFile(Js5ConfigType.AREA, count);
								AreaDefinition.definitions[count] = new AreaDefinition(count);
								if (fileData != null) {
									AreaDefinition.definitions[count].decode(new Buffer(fileData));
									AreaDefinition.definitions[count].init();
								}
							}
						}


						drawLoadingText(45, "Loaded config");
						Client.titleLoadingStage = 80;
					}
				} else if (Client.titleLoadingStage == 80) {
					loadingProgress = 0;

					if (mapSceneSprites == null) {
						mapSceneSprites = IndexedImage.loadImages(spriteIds.mapScenes, 0);
					} else {
						++loadingProgress;
					}
					if (crosses == null) {
						crosses = Sprite.getSprites(spriteIds.field4589, 0);
					} else {
						++loadingProgress;
					}
					++loadingProgress;
					drawLoadingText(50, "Loaded Sprites");
					++loadingProgress;
					++loadingProgress;
					++loadingProgress;
					++loadingProgress;
					drawLoadingText(52, "Loaded Sprites");
					++loadingProgress;
					++loadingProgress;
					++loadingProgress;
					++loadingProgress;
					++loadingProgress;
					++loadingProgress;
					drawLoadingText(54, "Loaded Sprites");
					++loadingProgress;

					++loadingProgress;
					drawLoadingText(55, "Loaded Sprites");
					if (loadingProgress < 14) {
						drawLoadingText(70, "Loading sprites - " + loadingProgress * 100 / 13 + "%");
					} else {
						int var1 = (int) (Math.random() * 21.0) - 10;
						int var2 = (int) (Math.random() * 21.0) - 10;
						var3 = (int) (Math.random() * 21.0) - 10;
						int var4 = (int) (Math.random() * 41.0) - 20;
						//mapSceneSprites[0].offsetColor(var1 + var4, var2 + var4, var4 + var3);
						drawLoadingText(60, "Loading sprites");
						Client.titleLoadingStage = 90;
					}
				} else if (Client.titleLoadingStage == 90) {
					if (!Js5List.textures.isFullyLoaded()) {
						drawLoadingText(65, "Loading textures - 0%");
					} else {
						TextureProvider textureProvider = new TextureProvider(Js5List.textures, Js5List.sprites, 20, 0.80000000000000004D, lowMem ? 64 : 128);
						Rasterizer3D.setTextureLoader(textureProvider);
						Rasterizer3D.setBrightness(0.80000000000000004D);
						textureProvider.getLoadedPercentage();
						drawLoadingText(69, "Loading textures");
						Client.titleLoadingStage = 95;
					}
				} else if (Client.titleLoadingStage == 95) {
					if (!Js5List.maps.isFullyLoaded()) {
						drawLoadingText(70, "Loading maps - " + Js5List.maps.percentage() + "%");
					} else {
						drawLoadingText(70, "Loading maps");
						Client.titleLoadingStage = 100;

					}
				} else if (Client.titleLoadingStage == 100) {
					drawLoadingText(72, "Loading textures");
					Client.titleLoadingStage = 110;
				} else if (Client.titleLoadingStage == 110) {
					drawLoadingText(74, "Loading input handler");
					Client.titleLoadingStage = 120;
				} else if (Client.titleLoadingStage == 120) {
					drawLoadingText(76, "Loaded wordpack");
					Client.titleLoadingStage = 130;
				} else if (Client.titleLoadingStage == 130) {
					drawLoadingText(77, "Loaded interfaces");
					TextDrawingArea allFonts[] = {smallText, aTextDrawingArea_1271, chatTextDrawingArea, aTextDrawingArea_1273};
					RSInterface.unpack(interface_archive, allFonts, mediaStreamLoader, new RSFont[]{newSmallFont, newRegularFont, newBoldFont, newFancyFont});
					Client.titleLoadingStage = 140;
				} else if (Client.titleLoadingStage == 140) {
					drawLoadingText(80, "Loaded world map");
					Client.titleLoadingStage = 150;
				} else if (Client.titleLoadingStage == 150) {
					try {
						HoverMenuManager.init();
						SettingsManager.loadSettings();
						ClientScripts.load();



						variousSettings[304] = 1;
						drawLoadingText(82, "Loading Engine");
						//drawLoadingText(10, "Loading Packed Sprites...");
						try {
							ItemDef.load();

							mapIcon7 = new Sprite(mediaStreamLoader, "mapfunction", 1);
							mapIcon8 = new Sprite(mediaStreamLoader, "mapfunction", 51);
							mapIcon6 = new Sprite(mediaStreamLoader, "mapfunction", 74);
							mapIcon5 = new Sprite(mediaStreamLoader, "mapfunction", 5);
							mapIcon9 = new Sprite(mediaStreamLoader, "mapfunction", 56);
							multiOverlay = new Sprite(mediaStreamLoader, "overlay_multiway", 0);
							grandExchangeSprite0 = new Sprite("Grand Exchange/0");
							grandExchangeSprite1 = new Sprite("Grand Exchange/1");
							grandExchangeSprite2 = new Sprite("Grand Exchange/2");
							grandExchangeSprite3 = new Sprite("Grand Exchange/3");
							grandExchangeSprite4 = new Sprite("Grand Exchange/4");
							drawLoadingText(84, "Loading Engine");
							moneyPouch = new Sprite("/Interfaces/Wiki/964");
							moneyPouch1 = new Sprite("/Interfaces/Wiki/965");
							moneyPouchCoins = new Sprite("/Interfaces/Wiki/963");
							xpbg1 = new Sprite("487");
							xpbg2 = new Sprite("488");
							eventIcon = new Sprite(mediaStreamLoader, "mapfunction", 72);
							bankDivider = new Sprite("bank_divider");
							minimapImage = new Sprite(512, 512);

							loginAsset0 = new Sprite("Login/remember0");
							loginAsset1 = new Sprite("Login/remember1");
							loginAsset2 = new Sprite("Login/remember2");
							loginAsset3 = new Sprite("Login/remember3");
							loginAsset4 = new Sprite("Login/logo");
							drawLoadingText(86, "Loading Engine");
							loginScreenBackground = new Sprite("/loginscreen/background2");
							logo2021 = new Sprite("/loginscreen/logo");
							loginScreenBackgroundCaptcha = new Sprite("/loginscreen/captcha_background");
							captchaExit = new Sprite("/loginscreen/captcha-exit");
							captchaExitHover = new Sprite("/loginscreen/captcha-exit-hover");

							cacheSprite = new Sprite[684];
							for (int i = 0; i < 684; i++) {
								cacheSprite[i] = new Sprite("Sprites/" + i);
							}
							drawLoadingText(87, "Loading Engine");
							xpSprite = new Sprite("medal");
							for (int i = 0; i < inputSprites.length; i++)
								inputSprites[i] = new Sprite("Interfaces/Inputfield/SPRITE " + (i + 1));

							for (int i = 0; i < tabAreaResizable.length; i++)
								tabAreaResizable[i] = new Sprite("Gameframe/resizable/tabArea " + i);

							loadTabArea();
							drawLoadingText(89, "Loading Engine");
							infinity = new Sprite("infinity");
							chatArea = new Sprite("Gameframe/chatarea");
							channelButtons = new Sprite("Gameframe/channelbuttons");
							venomOrb = new Sprite("orbs/venom");

							for (int index = 0; index < smallXpSprites.length; index++) {
								smallXpSprites[index] = new Sprite("expdrop/" + index);

							}

							for (int c1 = 0; c1 <= 3; c1++)
								chatButtons[c1] = new Sprite(mediaStreamLoader, "chatbuttons", c1);
							chatButtons[3] = new Sprite("1025_0");
							Sprite[] clanIcons = new Sprite[9];
							for (int index = 0; index < clanIcons.length; index++) {
								clanIcons[index] = new Sprite("Clan Chat/Icons/" + index);
							}
							drawLoadingText(90, "Loading Engine");
							Sprite[] iconPack = new Sprite[229];
							for (int index = 0; index < iconPack.length; index++) {
								iconPack[index] = new Sprite("icon_pack/" + index);
							}

							RSFont.unpackImages(modIconss, clanIcons, iconPack);

							mapEdge = new Sprite(mediaStreamLoader, "mapedge", 0);
							mapEdge.method345();

							try {
								for (int l3 = 0; l3 < 119; l3++)
									mapFunctions[l3] = new Sprite("MapFunctions/" + l3);
							} catch (Exception _ex) {
							}
							drawLoadingText(92, "Loading Engine");
							try {
								for (int i4 = 0; i4 < 20; i4++) {
									hitMarks[i4] = new Sprite(mediaStreamLoader, "hitmarks", i4);
								}
							} catch (Exception _ex) {
							}

							hitMarks[19] = new Sprite("heal_hitsplat");

							try {
								for (int h1 = 0; h1 < 6; h1++)
									headIconsHint[h1] = new Sprite(mediaStreamLoader, "headicons_hint", h1);
							} catch (Exception _ex) {
							}
							try {
								for (int j4 = 0; j4 < 18; j4++)
									headIcons[j4] = new Sprite(mediaStreamLoader, "headicons_prayer", j4);
								for (int j45 = 0; j45 < 3; j45++)
									skullIcons[j45] = new Sprite(mediaStreamLoader, "headicons_pk", j45);
							} catch (Exception _ex) {
							}
							for (int i = 0; i < minimapIcons.length; i++) {
								minimapIcons[i] = new Sprite("Mapicons/ICON " + i);
							}
							drawLoadingText(94, "Loading Engine");
							mapFlag = new Sprite(mediaStreamLoader, "mapmarker", 0);
							mapMarker = new Sprite(mediaStreamLoader, "mapmarker", 1);

							mapDotItem = new Sprite(mediaStreamLoader, "mapdots", 0);
							mapDotNPC = new Sprite(mediaStreamLoader, "mapdots", 1);
							mapDotPlayer = new Sprite(mediaStreamLoader, "mapdots", 2);
							mapDotFriend = new Sprite(mediaStreamLoader, "mapdots", 3);
							mapDotTeam = new Sprite(mediaStreamLoader, "mapdots", 4);

							mapDotClan = new Sprite(mediaStreamLoader, "mapdots", 5);
							new Sprite(mediaStreamLoader, "mapdots", 4);
							scrollBar1 = new Sprite(mediaStreamLoader, "scrollbar", 0);
							scrollBar2 = new Sprite(mediaStreamLoader, "scrollbar", 1);
							for (int i = 0; i < modIconss.length; i++) {
								modIconss[i] = new Sprite("Player/MODICONS " + i + "");
							}

							for (int index = 0; index < GameTimerHandler.TIMER_IMAGES.length; index++) {
								GameTimerHandler.TIMER_IMAGES[index] = new Sprite("GameTimer/TIMER " + index);
							}
							drawLoadingText(95, "Loading Engine");
							loadPlayerData();

							drawLoadingText(96, "Preparing game engine");
							new Sprite("Gameframe317/fixed/mapBack");
							new Sprite("Gameframe/fixed/mapBack");
							if (getUserSettings().isOldGameframe() == false) {
								mapBack = new Sprite("Gameframe/fixed/mapBack");
							} else {
								mapBack = new Sprite("Gameframe317/fixed/mapBack");
							}
							drawLoadingText(97, "Preparing game engine");
							for (int pixelY = 0; pixelY < 33; pixelY++) {
								int k6 = 999;
								int i7 = 0;
								for (int pixelX = 0; pixelX < 34; pixelX++) {
									if (mapBack.myPixels[pixelX + pixelY * mapBack.myWidth] == 0) {
										if (k6 == 999)
											k6 = pixelX;
										continue;
									}
									if (k6 == 999)
										continue;
									i7 = pixelX;
									break;
								}
								anIntArray968[pixelY] = k6;
								anIntArray1057[pixelY] = i7 - k6;
							}
							drawLoadingText(99, "Preparing game engine");

							for (int pixelY = 1; pixelY < 153; pixelY++) {
								int j7 = 999;
								int l7 = 0;
								for (int pixelX = 24; pixelX < 177; pixelX++) {
									if (mapBack.myPixels[pixelX + pixelY * mapBack.myWidth] == 0 && (pixelX > 34 || pixelY > 34)) {
										if (j7 == 999) {
											j7 = pixelX;
										}
										continue;
									}
									if (j7 == 999) {
										continue;
									}
									l7 = pixelX;
									break;
								}
								anIntArray1052[pixelY - 1] = j7 - 24;
								anIntArray1229[pixelY - 1] = l7 - j7;
							}
							drawLoadingText(99, "Preparing game engine");
							try {
								macAddress = GetNetworkAddress.GetAddress("mac");
								if (macAddress == null)
									macAddress = "";
								if (Configuration.developerMode) {
									System.out.println(macAddress);
								}
							} catch (Exception e) {
								e.printStackTrace();
								macAddress = "";
							}

							setConfigButtonsAtStartup();
							try {
								informationFile.read();
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (informationFile.isUsernameRemembered()) {
								myUsername = informationFile.getStoredUsername();
							}
							if (informationFile.isPasswordRemembered()) {
								setPassword(informationFile.getStoredPassword());
							}
							if (informationFile.isRememberRoof()) {
								removeRoofs = true;
							}
							drawLoadingText(100, "Preparing game engine");

							ObjectDefinition.clientInstance = this;
							NpcDefinition.clientInstance = this;

							if (Configuration.PRINT_EMPTY_INTERFACE_SECTIONS) {
								if (Configuration.developerMode) {
									RSInterface.printEmptyInterfaceSections();
								} else {
									System.err.println("PRINT_EMPTY_INTERFACE_SECTIONS is toggled but you must be in dev mode.");
								}
							}

							Preferences.load();

							Client.titleLoadingStage = 210;
							loginScreen.setup();

							loginScreen.setLoginState(LoginState.LOGIN);
							setGameState(GameState.LOGIN_SCREEN);
							return;
						} catch (Exception exception) {
							exception.printStackTrace();
							Signlink.reporterror("loaderror " + aString1049 + " " + anInt1079);
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	static int titleLoadingStage;
	public int loadingPercent;
	public String loadingText;
	public static boolean isLoading = true;

	public static Sprite LOGO = new Sprite(AssetUtils.INSTANCE.getResource("2240.png"));


	public void method91(Buffer stream, int i) {
		while (stream.bitPosition + 10 < i * 8) {
			int j = stream.readBits(11);
			if (j == 2047)
				break;
			if (players[j] == null) {
				players[j] = new Player();
				if (aStreamArray895s[j] != null)
					players[j].updatePlayer(aStreamArray895s[j]);
			}
			playerIndices[playerCount++] = j;
			Player player = players[j];
			player.lastUpdateTick = loopCycle;
			int k = stream.readBits(1);
			if (k == 1)
				anIntArray894[anInt893++] = j;
			int l = stream.readBits(1);
			int i1 = stream.readBits(5);
			if (i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(5);
			if (j1 > 15)
				j1 -= 32;
			player.setPos(localPlayer.pathX[0] + j1, localPlayer.pathY[0] + i1, l == 1);
		}
		stream.finishBitAccess();
	}

	public boolean inCircle(int circleX, int circleY, int clickX, int clickY, int radius) {
		return Math.pow((circleX + radius - clickX), 2)
				+ Math.pow((circleY + radius - clickY), 2) < Math.pow(radius, 2);
	}

	public boolean mouseMapPosition() {
		if (MouseHandler.mouseX >= canvasWidth - 21 && MouseHandler.mouseX <= canvasWidth && MouseHandler.mouseY >= 0
				&& MouseHandler.mouseY <= 21) {
			return false;
		}
		return true;
	}

	private void processMainScreenClick() {
		if (minimapState != 0)
			return;
		if (clickMode3 == 1) {
			int i = MouseHandler.saveClickX - 25 - 547;
			int j = MouseHandler.saveClickY - 5 - 3;
			if (isResized()) {
				i = MouseHandler.saveClickX - (canvasWidth - 182 + 24);
				j = MouseHandler.saveClickY - 8;
			}
			if (inCircle(0, 0, i, j, 76) && mouseMapPosition() && !runHover) {
				i -= 73;
				j -= 75;
				int k = camAngleY + camAngleY & 0x7ff;
				int i1 = Rasterizer3D.SINE[k];
				int j1 = Rasterizer3D.COSINE[k];
				i1 = i1 * (minimapZoom + 256) >> 8;
				j1 = j1 * (minimapZoom + 256) >> 8;
				int k1 = j * i1 + i * j1 >> 11;
				int l1 = j * j1 - i * i1 >> 11;
				int i2 = localPlayer.x + k1 >> 7;
				int j2 = localPlayer.y - l1 >> 7;
				if (localPlayer.isAdminRights() && controlIsDown) {
					teleport(baseX + i2, baseY + j2);
				} else {
					boolean flag1 = doWalkTo(1, localPlayer.pathX[0], localPlayer.pathY[0], 0, 0, 0, 0, 0, j2, true, i2);
					if (flag1) {
						stream.writeUnsignedByte(i);
						stream.writeUnsignedByte(j);
						stream.writeWord(camAngleY);
						stream.writeUnsignedByte(57);
						stream.writeUnsignedByte(camAngleY);
						stream.writeUnsignedByte(minimapZoom);
						stream.writeUnsignedByte(89);
						stream.writeWord(localPlayer.x);
						stream.writeWord(localPlayer.y);
						stream.writeUnsignedByte(anInt1264);
						stream.writeUnsignedByte(63);
					}
				}
			}
			anInt1117++;
			if (anInt1117 > 1151) {
				anInt1117 = 0;
				stream.createFrame(246);
				stream.writeUnsignedByte(0);
				int l = stream.currentPosition;
				if ((int) (Math.random() * 2D) == 0)
					stream.writeUnsignedByte(101);
				stream.writeUnsignedByte(197);
				stream.writeWord((int) (Math.random() * 65536D));
				stream.writeUnsignedByte((int) (Math.random() * 256D));
				stream.writeUnsignedByte(67);
				stream.writeWord(14214);
				if ((int) (Math.random() * 2D) == 0)
					stream.writeWord(29487);
				stream.writeWord((int) (Math.random() * 65536D));
				if ((int) (Math.random() * 2D) == 0)
					stream.writeUnsignedByte(220);
				stream.writeUnsignedByte(180);
				stream.writeBytes(stream.currentPosition - l);
			}
		}
	}
	/**
	 * Get the String residing on the clipboard.
	 *
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
	 */
	public static String getClipboardContents() {
		if (System.currentTimeMillis() - timer < 2000) {
			return "";
		}
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
				timer = System.currentTimeMillis();
			} catch (UnsupportedFlavorException | IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	static long timer = 0;

	private void teleport(int x, int y) {
		String text = "::tele " + x + " " + y;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	public static void teleportInterface() {
		if (System.currentTimeMillis() - timer < 400) {
			return;
		}
		timer = System.currentTimeMillis();
		String text = "::teleport";
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	public static void closeInterfacee() {
		String text = "::close";
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	public void closeInterface() {
		if (invOverlayInterfaceID != 0) {
			invOverlayInterfaceID = 0;
			needDrawTabArea = true;
			tabAreaAltered = true;
		}
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
		}
		if (inputDialogState != 0) {
			inputDialogState = 0;
			inputTaken = true;
		}
		if (this.isFieldInFocus()) {
			this.resetInputFieldFocus();
			Client.inputString = "";
		}
		fullscreenInterfaceID = -1;
		openInterfaceID = -1;
		continuedDialogue = false;
	}

	@Nullable
	@Override
	public NPC getFollower() {
		return null;
	}

	public static void continueDialogue() {
		if (System.currentTimeMillis() - timer < 400) {
			return;
		}
		timer = System.currentTimeMillis();
		String text = "::dialoguecontinuation continue";
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	public static void dialogueOptions(String option) {
		if (System.currentTimeMillis() - timer < 400) {
			return;
		}
		timer = System.currentTimeMillis();
		String text = option == "one" ? "::dialoguecontinuation option_one"
				: option == "two" ? "::dialoguecontinuation option_two"
				: option == "three" ? "::dialoguecontinuation option_three"
				: option == "four" ? "::dialoguecontinuation option_four"
				: "::dialoguecontinuation option_five";
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	private String interfaceIntToString(int j) {
		if (j < 0x3b9ac9ff)
			return String.valueOf(j);
		else
			return "*";
	}

	@Override
	public URL getCodeBase() {
		try {
			return new URL(server + ":" + (80 + portOff));
		} catch (Exception _ex) {
		}
		return null;
	}

	public void method95() {
		for (int j = 0; j < npcCount; j++) {
			int k = npcIndices[j];
			Npc npc = npcs[k];
			if (npc != null)
				entityUpdateBlock(npc);
		}
	}

	public void entityUpdateBlock(Entity entity) {
		if (entity.x < 128 || entity.y < 128 || entity.x >= 13184 || entity.y >= 13184) {
			entity.sequence = -1;
			entity.initiate_movement = 0;
			entity.cease_movement = 0;
			entity.clearSpotAnimations();
			entity.x = entity.pathX[0] * 128 + entity.size * 64;
			entity.y = entity.pathY[0] * 128 + entity.size * 64;
			entity.method446();
		}
		if (entity == localPlayer && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
			entity.sequence = -1;
			entity.initiate_movement = 0;
			entity.cease_movement = 0;
			entity.clearSpotAnimations();
			entity.x = entity.pathX[0] * 128 + entity.size * 64;
			entity.y = entity.pathY[0] * 128 + entity.size * 64;
			entity.method446();
		}
		if (entity.initiate_movement > loopCycle)
			method97(entity);
		else if (entity.cease_movement >= loopCycle)
			method98(entity);
		else
			method99(entity);
		method100(entity);
		entity.updateAnimation();
	}

	public void method97(Entity entity) {
		int i = entity.initiate_movement - loopCycle;
		int j = entity.anInt1543 * 128 + entity.size * 64;
		int k = entity.anInt1545 * 128 + entity.size * 64;
		entity.x += (j - entity.x) / i;
		entity.y += (k - entity.y) / i;
		entity.anInt1503 = 0;
		if (entity.forceMovementDirection == 0)
			entity.setTurnDirection(1024);
		if (entity.forceMovementDirection == 1)
			entity.setTurnDirection(1536);
		if (entity.forceMovementDirection == 2)
			entity.setTurnDirection(0);
		if (entity.forceMovementDirection == 3)
			entity.setTurnDirection(512);
	}

	public void method98(Entity entity) {
		if (entity.cease_movement == loopCycle || entity.sequence == -1 || entity.sequenceDelay != 0
				|| entity.sequenceFrameCycle + 1 > SequenceDefinition.get(entity.sequence).duration(entity.sequenceFrame)) {
			int i = entity.cease_movement - entity.initiate_movement;
			int j = loopCycle - entity.initiate_movement;
			int k = entity.anInt1543 * 128 + entity.size * 64;
			int l = entity.anInt1545 * 128 + entity.size * 64;
			int i1 = entity.anInt1544 * 128 + entity.size * 64;
			int j1 = entity.anInt1546 * 128 + entity.size * 64;
			entity.x = (k * (i - j) + i1 * j) / i;
			entity.y = (l * (i - j) + j1 * j) / i;
		}
		entity.anInt1503 = 0;
		if (entity.forceMovementDirection == 0)
			entity.setTurnDirection(1024);
		if (entity.forceMovementDirection == 1)
			entity.setTurnDirection(1536);
		if (entity.forceMovementDirection == 2)
			entity.setTurnDirection(0);
		if (entity.forceMovementDirection == 3)
			entity.setTurnDirection(512);
		entity.orientation = entity.getTurnDirection();
	}

	public void method99(Entity entity) {
		entity.movementSequence = entity.idleSequence;
		if (entity.smallXYIndex == 0) {
			entity.anInt1503 = 0;
			return;
		}
		if (entity.sequence != -1 && entity.sequenceDelay == 0) {
			SequenceDefinition animation = SequenceDefinition.get(entity.sequence);
			if (entity.stationaryPathPosition > 0 && animation.moveStyle == 0) {
				entity.anInt1503++;
				return;
			}
			if (entity.stationaryPathPosition <= 0 && animation.idleStyle == 0) {
				entity.anInt1503++;
				return;
			}
		}
		int i = entity.x;
		int j = entity.y;
		int k = entity.pathX[entity.smallXYIndex - 1] * 128 + entity.size * 64;
		int l = entity.pathY[entity.smallXYIndex - 1] * 128 + entity.size * 64;
		if (k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
			entity.x = k;
			entity.y = l;
			return;
		}
		if (i < k) {
			if (j < l)
				entity.setTurnDirection(1280);
			else if (j > l)
				entity.setTurnDirection(1792);
			else
				entity.setTurnDirection(1536);
		} else if (i > k) {
			if (j < l)
				entity.setTurnDirection(768);
			else if (j > l)
				entity.setTurnDirection(256);
			else
				entity.setTurnDirection(512);
		} else if (j < l)
			entity.setTurnDirection(1024);
		else
			entity.setTurnDirection(0);
		int i1 = entity.getTurnDirection() - entity.orientation & 0x7ff;
		if (i1 > 1024)
			i1 -= 2048;
		int j1 = entity.walkBackSequence;
		if (i1 >= -256 && i1 <= 256)
			j1 = entity.walkSequence;
		else if (i1 >= 256 && i1 < 768)
			j1 = entity.walkRightSequence;
		else if (i1 >= -768 && i1 <= -256)
			j1 = entity.walkLeftSequence;
		if (j1 == -1)
			j1 = entity.walkSequence;
		entity.movementSequence = j1;
		int k1 = 4;
		if (entity.orientation != entity.getTurnDirection() && entity.interactingEntity == -1 && entity.rotation != 0)
			k1 = 2;
		if (entity.smallXYIndex > 2)
			k1 = 6;
		if (entity.smallXYIndex > 3)
			k1 = 8;
		if (entity.anInt1503 > 0 && entity.smallXYIndex > 1) {
			k1 = 8;
			entity.anInt1503--;
		}
		if (entity.aBooleanArray1553[entity.smallXYIndex - 1])
			k1 <<= 1;
		if (k1 >= 8 && entity.movementSequence == entity.walkSequence && entity.runAnimIndex != -1)
			entity.movementSequence = entity.runAnimIndex;
		if (i < k) {
			entity.x += k1;
			if (entity.x > k)
				entity.x = k;
		} else if (i > k) {
			entity.x -= k1;
			if (entity.x < k)
				entity.x = k;
		}
		if (j < l) {
			entity.y += k1;
			if (entity.y > l)
				entity.y = l;
		} else if (j > l) {
			entity.y -= k1;
			if (entity.y < l)
				entity.y = l;
		}
		if (entity.x == k && entity.y == l) {
			entity.smallXYIndex--;
			if (entity.stationaryPathPosition > 0)
				entity.stationaryPathPosition--;
		}
	}

	public void method100(Entity entity) {
		if (entity.rotation == 0)
			return;
		if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
			Npc npc = npcs[entity.interactingEntity];
			if (npc != null) {
				int i1 = entity.x - npc.x;
				int k1 = entity.y - npc.y;
				if (i1 != 0 || k1 != 0)
					entity.setTurnDirection((int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff);
			}
		}
		if (entity.interactingEntity >= 32768) {
			int j = entity.interactingEntity - 32768;
			if (j == localPlayerIndex)
				j = maxPlayerCount;
			Player player = players[j];
			if (player != null) {
				int l1 = entity.x - player.x;
				int i2 = entity.y - player.y;
				if (l1 != 0 || i2 != 0)
					entity.setTurnDirection((int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff);
			}
		}
		if ((entity.anInt1538 != 0 || entity.anInt1539 != 0) && (entity.smallXYIndex == 0 || entity.anInt1503 > 0)) {
			int k = entity.x - (entity.anInt1538 - baseX - baseX) * 64;
			int j1 = entity.y - (entity.anInt1539 - baseY - baseY) * 64;
			if (k != 0 || j1 != 0) {
				entity.setTurnDirection((int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff);
			}
			entity.anInt1538 = 0;
			entity.anInt1539 = 0;
		}
		int l = entity.getTurnDirection() - entity.orientation & 0x7ff;
		if (l != 0) {
			if (l < entity.rotation || l > 2048 - entity.rotation)
				entity.orientation = entity.getTurnDirection();
			else if (l > 1024)
				entity.orientation -= entity.rotation;
			else
				entity.orientation += entity.rotation;
			entity.orientation &= 0x7ff;
			if (entity.movementSequence == entity.idleSequence && entity.orientation != entity.getTurnDirection()) {
				if (entity.standTurnAnimIndex != -1) {
					entity.movementSequence = entity.standTurnAnimIndex;
					return;
				}
				entity.movementSequence = entity.walkSequence;
			}
		}
	}

	private void drawGameScreen() {
		if (fullscreenInterfaceID != -1 && gameState == GameState.LOGGED_IN.getState()) {
			if (gameState == GameState.LOGGED_IN.getState()) {
				try {
					processWidgetAnimations(tickDelta, fullscreenInterfaceID);
					if (openInterfaceID != -1) {
						processWidgetAnimations(tickDelta, openInterfaceID);
					}
				} catch(Exception ex) {

				}
				tickDelta = 0;
				getCallbacks().post(ClientTick.INSTANCE);

				Rasterizer2D.Rasterizer2D_clear();
				jagexNetThread.writePacket(true);
				welcomeScreenRaised = true;
				if (openInterfaceID != -1) {
					RSInterface rsInterface_1 = RSInterface.interfaceCache[openInterfaceID];
					if (rsInterface_1.width == 512 && rsInterface_1.height == 334
							&& rsInterface_1.type == 0) {
						rsInterface_1.width = 765;
						rsInterface_1.height = 503;
					}
					try {
						drawInterface(0, 0, rsInterface_1, 8);
					} catch(Exception ex) {

					}
				}
				RSInterface rsInterface = RSInterface.interfaceCache[fullscreenInterfaceID];
				if (rsInterface.width == 512 && rsInterface.height == 334
						&& rsInterface.type == 0) {
					rsInterface.width = 765;
					rsInterface.height = 503;
				}
				try {
					drawInterface(0, 0, rsInterface, 8);
				} catch (Exception ex) {

				}

				if (!isMenuOpen) {
					processRightClick();
					drawTopLeftTooltip();
				}
			}
			drawCount++;
			return;
		}

		if (welcomeScreenRaised) {
			welcomeScreenRaised = false;
			inputTaken = true;
			tabAreaAltered = true;
		}
		if (backDialogID != -1) {
			try {
				processWidgetAnimations(tickDelta, backDialogID);
			} catch (Exception ex) {

			}
		}

		if (backDialogID == -1) {
			aClass9_1059.scrollPosition = chatAreaScrollLength - anInt1089 - 110;
			if (MouseHandler.mouseX >= 496 && MouseHandler.mouseX <= 511
					&& MouseHandler.mouseY > (!isResized() ? 345
					: canvasHeight - 158))
				method65(494, 110, MouseHandler.mouseX,
						MouseHandler.mouseY - (!isResized() ? 345
								: canvasHeight - 158),
						aClass9_1059, 0, false, chatAreaScrollLength);
			int i = chatAreaScrollLength - 110 - aClass9_1059.scrollPosition;
			if (i < 0) {
				i = 0;
			}
			if (i > chatAreaScrollLength - 110) {
				i = chatAreaScrollLength - 110;
			}
			if (anInt1089 != i) {
				anInt1089 = i;
				inputTaken = true;
			}
		}
		if (backDialogID != -1) {
			boolean flag2 = false;

			try {
				flag2 = processWidgetAnimations(tickDelta, backDialogID);
			} catch(Exception ex) {
				ex.printStackTrace();
			}

			if (flag2) {
				inputTaken = true;
			}
		}
		if (atInventoryInterfaceType == 3)
			inputTaken = true;
		if (activeInterfaceType == 3)
			inputTaken = true;
		if (clickToContinueString != null)
			inputTaken = true;
		if (isMenuOpen)
			inputTaken = true;
		if (inputTaken) {
			inputTaken = false;
		}
		if (gameState == GameState.LOGGED_IN.getState()) {
			int offsetX = isResized() ? 0 : 4;
			int offsetY = isResized() ? 0 : 4;
			drawEntities(offsetX, offsetY, !isResized() ? 512 : canvasWidth, !isResized() ? 334 : canvasHeight);
		}


		ObjectSound.updateObjectSounds();
		tickDelta = 0;
		getCallbacks().post(ClientTick.INSTANCE);
	}

	private boolean buildFriendsListMenu(RSInterface class9) {
		int i = class9.contentType;
		if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
			if (i >= 801)
				i -= 701;
			else if (i >= 701)
				i -= 601;
			else if (i >= 101)
				i -= 101;
			else
				i--;
			if (tabInterfaceIDs[tabID] != 42500) {
				insertMenuItemNoShift("Remove" + " @whi@" + friendsList[i], MenuAction.REMOVE2);
				insertMenuItemNoShift("Message" + " @whi@" + friendsList[i], MenuAction.MESSAGE2);
			}

			return true;
		}
		if (i >= 401 && i <= 500) {
			insertMenuItemNoShift("Remove" + " @whi@" + class9.message, MenuAction.REMOVE1);
			return true;
		} else {
			return false;
		}
	}

	private int hoverId;

	public void method104() {
		for (SpotAnimEntity spotAnimationEntity = (SpotAnimEntity) incompleteAnimables.last(); null != spotAnimationEntity; spotAnimationEntity = (SpotAnimEntity) incompleteAnimables.previous()) {
			if (plane == spotAnimationEntity.z && !spotAnimationEntity.expired) {
				if (loopCycle >= spotAnimationEntity.cycleStart) {
					spotAnimationEntity.step(tickDelta);
					if (spotAnimationEntity.expired) {
						spotAnimationEntity.unlink();
					} else {
						int radius = 60;
						int orientation = 0;
						boolean drawFrontTilesFirst = false;
						if (spotAnimationEntity instanceof RSRuneLiteObject)
						{
							RSRuneLiteObject rSRuneLiteObject = (RSRuneLiteObject)spotAnimationEntity;
							radius = rSRuneLiteObject.getRadius();
							orientation = rSRuneLiteObject.getOrientation();
							drawFrontTilesFirst = rSRuneLiteObject.drawFrontTilesFirst();
						}

						scene.add_entity(spotAnimationEntity.z, orientation, spotAnimationEntity.height, -1,
								spotAnimationEntity.y, radius, spotAnimationEntity.x,
								spotAnimationEntity, drawFrontTilesFirst);
					}
				}
			} else {
				spotAnimationEntity.unlink();
			}
		}
	}

	public int dropdownInversionFlag;

	public Sprite grandExchangeSprite0,
			grandExchangeSprite1,
			grandExchangeSprite2,
			grandExchangeSprite3,
			grandExchangeSprite4;

	private int interfaceDrawY;
	public boolean interfaceText = false;
	public boolean interfaceStringText = false;

	public static final int[] runeChildren = { 1202, 1203, 1209, 1210, 1211, 1218, 1219, 1220, 1227, 1228, 1234, 1235, 1236, 1243,
			1244, 1245, 1252, 1253, 1254, 1261, 1262, 1263, 1270, 1271, 1277, 1278, 1279, 1286, 1287, 1293,
			1294, 1295, 1302, 1303, 1304, 1311, 1312, 1318, 1319, 1320, 1327, 1328, 1329, 1336, 1337, 1343,
			1344, 1345, 1352, 1353, 1354, 1361, 1362, 1363, 1370, 1371, 1377, 1378, 1384, 1385, 1391, 1392,
			1393, 1400, 1401, 1407, 1408, 1410, 1417, 1418, 1424, 1425, 1426, 1433, 1434, 1440, 1441, 1442,
			1449, 1450, 1456, 1457, 1463, 1464, 1465, 1472, 1473, 1474, 1481, 1482, 1488, 1489, 1490, 1497,
			1498, 1499, 1506, 1507, 1508, 1515, 1516, 1517, 1524, 1525, 1526, 1533, 1534, 1535, 1547, 1548,
			1549, 1556, 1557, 1558, 1566, 1567, 1568, 1576, 1577, 1578, 1586, 1587, 1588, 1596, 1597, 1598,
			1605, 1606, 1607, 1616, 1617, 1618, 1627, 1628, 1629, 1638, 1639, 1640, 6007, 6008, 6011, 8673,
			8674, 12041, 12042, 12429, 12430, 12431, 12439, 12440, 12441, 12449, 12450, 12451, 12459, 12460,
			15881, 15882, 15885, 18474, 18475, 18478 };

	public static final int[] SOME_IDS = { 1196, 1199, 1206, 1215, 1224, 1231, 1240, 1249, 1258, 1267, 1274, 1283, 1573, 1290, 1299,
			1308, 1315, 1324, 1333, 1340, 1349, 1358, 1367, 1374, 1381, 1388, 1397, 1404, 1583, 12038, 1414,
			1421, 1430, 1437, 1446, 1453, 1460, 1469, 15878, 1602, 1613, 1624, 7456, 1478, 1485, 1494, 1503,
			1512, 1521, 1530, 1544, 1553, 1563, 1593, 1635, 12426, 12436, 12446, 12456, 6004, 18471,
			/* Ancients */
			12940, 12988, 13036, 12902, 12862, 13046, 12964, 13012, 13054, 12920, 12882, 13062, 12952, 13000,
			13070, 12912, 12872, 13080, 12976, 13024, 13088, 12930, 12892, 13096 };


	private static final int[] BOUNTY_INTERFACE_IDS = {
			21001, 28030, 28006, 28021, 28003, 28004, 28005, 28029,
			28023, 28024, 28025, 28026, 28027, 28028, 28001, 28022
	};

	public void drawInterface(int scrollPosition, int xPosition, RSInterface rsInterface, int yPosition) {
		drawInterface(scrollPosition, xPosition, rsInterface, yPosition, false);
	}

	public void drawInterface(int scrollPosition, int xPosition, RSInterface rsInterface, int yPosition, boolean inheritDrawingArea) {
		try {
			if (rsInterface.type != 0 || rsInterface.children == null)
				return;
			if (rsInterface.isMouseoverTriggered && anInt1026 != rsInterface.id && anInt1048 != rsInterface.id
					&& anInt1039 != rsInterface.id)
				return;

			int clipLeft = Rasterizer2D.xClipStart;
			int clipTop = Rasterizer2D.yClipStart;
			int clipRight = Rasterizer2D.xClipEnd;
			int clipBottom = Rasterizer2D.yClipEnd;

			if (!inheritDrawingArea) {
				Rasterizer2D.setDrawingArea(xPosition, yPosition, xPosition + rsInterface.width, yPosition + rsInterface.height);
			}
			Rasterizer3D.setupRasterizerClip();
			int childCount = rsInterface.children.length;
			for (int childId = 0; childId < childCount; childId++) {
				try {
					int _x = rsInterface.childX[childId] + xPosition;
					int _y = (rsInterface.childY[childId] + yPosition) - scrollPosition;
					RSInterface class9_1 = RSInterface.interfaceCache[rsInterface.children[childId]];

					_x += class9_1.anInt263;
					_y += class9_1.anInt265;

					if (class9_1.interfaceHidden)
						continue;

					if (!getUserSettings().isBountyHunter() && Arrays.stream(BOUNTY_INTERFACE_IDS).anyMatch(id -> id == class9_1.id)) {
						continue;
					}

					if (class9_1.contentType > 0)
						drawFriendsListOrWelcomeScreen(class9_1);

					if (class9_1.id != 28060 && class9_1.id != 28061) {
						for (int m5 = 0; m5 < SOME_IDS.length; m5++) {
							if (class9_1.id == SOME_IDS[m5] + 1) {
								if (m5 > 61) {
									drawBlackBox(_x + 1, _y);
								} else {
									drawBlackBox(_x, _y + 1);
								}
							}
						}
					}

					for (int r = 0; r < runeChildren.length; r++)
						if (class9_1.id == runeChildren[r])
							class9_1.modelZoom = 775;

					if (class9_1.type == 0) {
						if (class9_1.scrollPosition > class9_1.scrollMax - class9_1.height)
							class9_1.scrollPosition = class9_1.scrollMax - class9_1.height;
						if (class9_1.scrollPosition < 0)
							class9_1.scrollPosition = 0;
						drawInterface(class9_1.scrollPosition, _x, class9_1, _y, inheritDrawingArea || rsInterface.scrollMax > 0);

						// Hardcodes
						if (class9_1.scrollMax > class9_1.height) {
							// clan chat
							if (class9_1.id == 18143) {
								int clanMates = 0;
								for (int i = 18155; i < 18244; i++) {
									RSInterface line = RSInterface.interfaceCache[i];
									if (line.message.length() > 0) {
										clanMates++;
									}
								}
								class9_1.scrollMax = (clanMates * 14) + class9_1.height + 1;
							}
							if (class9_1.id == 18322 || class9_1.id == 18423) {
								int members = 0;
								for (int i = class9_1.id + 1; i < class9_1.id + 1 + 100; i++) {
									RSInterface line = RSInterface.interfaceCache[i];
									if (line != null && line.message != null) {
										if (line.message.length() > 0) {
											members++;
										}
									}
								}
								class9_1.scrollMax = (members * 14) + 1;
							}
							if (rsInterface.parentID == 49000 || rsInterface.parentID == 49100 || rsInterface.parentID == 51100
									|| rsInterface.parentID == 53100) {
								int scrollMax = class9_1.scrollMax;

								if (achievementCutoff > 1) {
									scrollMax = achievementCutoff * 65;
								} else {
									achievementCutoff = 282;
								}
								class9_1.scrollMax = scrollMax;
							}
							if (scrollbarVisible(class9_1)) {
								drawScrollbar(class9_1.height, class9_1.scrollPosition, _y, _x + class9_1.width,
										class9_1.scrollMax);
							}
						}

					} else if (class9_1.type != 1)
						if (class9_1.type == 2) {
							if (interfaceText) {
								newSmallFont.drawBasicString("Container: " + class9_1.id, _x, _y);
							}
							try {
								Bank.setupMainTab(class9_1, _x, _y);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// Item container
							if (class9_1.invAutoScrollHeight) {
								int lastRow = -1;
								int rowCount = 0;
								int i3 = 0;

								for (int l3 = 0; l3 < class9_1.height; l3++) {
									for (int l4 = 0; l4 < class9_1.width; l4++) {
										if (class9_1.inventoryItemId[i3] > 1) {
											if (lastRow != l3) {
												lastRow = l3;
												rowCount++;
											}
										}

										i3++;
									}
								}

								RSInterface scrollable = RSInterface.interfaceCache[class9_1.invAutoScrollInterfaceId];
								scrollable.scrollMax = scrollable.height + 1;
								int heightPerRow = class9_1.invSpritePadY + 32;
								if (heightPerRow * rowCount > scrollable.scrollMax) {
									scrollable.scrollMax += (heightPerRow * rowCount) - scrollable.scrollMax + scrollable.invAutoScrollHeightOffset;
								}
							}

							int i3 = 0;
							for (int l3 = 0; l3 < class9_1.height; l3++) {
								for (int l4 = 0; l4 < class9_1.width; l4++) {
									// System.out.println("id? " + class9_1.inv[i3]);
									int xPos = _x + l4 * (32 + class9_1.invSpritePadX);
									int yPos = _y + l3 * (32 + class9_1.invSpritePadY);
									if (i3 < 20) {
										xPos += class9_1.spritesX[i3];
										yPos += class9_1.spritesY[i3];
									}
									if (class9_1.inventoryItemId[i3] > 0) {
										int k6 = 0;
										int j7 = 0;
										int j9 = class9_1.inventoryItemId[i3] - 1;
										if (class9_1.id == 23231) {
											j9 = (class9_1.inventoryItemId[i3] & 0x7FFF) - 1;
										}
										if (xPos > Rasterizer2D.xClipStart - 32 && xPos < Rasterizer2D.xClipEnd && yPos > Rasterizer2D.yClipStart - 32
												&& yPos < Rasterizer2D.yClipEnd || activeInterfaceType != 0 && itemDraggingSlot == i3) {
											int l9 = 0;
											if (itemSelected == 1 && anInt1283 == i3 && anInt1284 == class9_1.id)
												l9 = 0xffffff;

											int itemSpriteOpacity = 256;
											/**
											 * Placeholder opacity editing
											 */
											if (class9_1.inventoryAmounts[i3] <= 0)
												itemSpriteOpacity = 100;

											Sprite itemSprite = null;

											if (openInterfaceID == 29875) {
												if (class9_1.inventoryItemId[i3] < 555
														|| class9_1.inventoryItemId[i3] > 567 && class9_1.inventoryItemId[i3] != 9076) {
													itemSpriteOpacity = 100;
												}
											}

											if (class9_1.id == 23121) {
												final boolean isLastBitEnabled = ((class9_1.inventoryItemId[i3] >> 15) & 0x1) == 1;
												if (isLastBitEnabled) {
													itemSpriteOpacity = 90;
												}
											}

											boolean smallSprite = openInterfaceID == 26000
													&& GrandExchange.isSmallItemSprite(class9_1.id) || class9_1.smallInvSprites;

											ItemDefinition itemDef = ItemDefinition.lookup(j9);
											if (smallSprite) {
												itemSprite = ItemDefinition.getSmallSprite(j9, class9_1.inventoryAmounts[i3]);
												if (itemDef.customSmallSpriteLocation != null)
												{
													itemSprite = new Sprite(itemDef.customSmallSpriteLocation);
												} else if (itemDef.customSpriteLocation != null)
												{
													itemSprite = new Sprite(itemDef.customSpriteLocation);
												}
											} else {
												itemSprite = ItemDefinition.getSprite(j9, class9_1.inventoryAmounts[i3], l9);
												if (itemDef.customSpriteLocation != null)
												{
													itemSprite = new Sprite(itemDef.customSpriteLocation);
												}
											}

											if (class9_1.id >= 32212 && class9_1.id <= 32212 + 11) {
												if (class9_1.inventoryItemId[i3] > 0) {
													if (class9_1.sprite2 != null) {
														class9_1.sprite2.drawSprite(xPos + k6 - 2, yPos + j7 - 2);
													}
												}
											}
											if (itemSprite != null) {
												if (activeInterfaceType != 0 && itemDraggingSlot == i3 && draggingItemInterfaceId == class9_1.id) {
													k6 = MouseHandler.mouseX - anInt1087;
													j7 = MouseHandler.mouseY - anInt1088;
													if (k6 < 5 && k6 > -5)
														k6 = 0;
													if (j7 < 5 && j7 > -5)
														j7 = 0;
													if (anInt989 < getDragSetting(class9_1.id)) {
														k6 = 0;
														j7 = 0;
													}
													itemSprite.drawTransparentSprite(xPos + k6, yPos + j7, 128);
													if (yPos + j7 < Rasterizer2D.yClipStart && rsInterface.scrollPosition > 0) {
														int i10 = (tickDelta * (Rasterizer2D.yClipStart - yPos - j7)) / 3;
														if (i10 > tickDelta * 10)
															i10 = tickDelta * 10;
														if (i10 > rsInterface.scrollPosition)
															i10 = rsInterface.scrollPosition;
														rsInterface.scrollPosition -= i10;
														anInt1088 += i10;
													}
													if (yPos + j7 + 32 > Rasterizer2D.yClipEnd
															&& rsInterface.scrollPosition < rsInterface.scrollMax
															- rsInterface.height) {
														int j10 = (tickDelta * ((yPos + j7 + 32) - Rasterizer2D.yClipEnd)) / 3;
														if (j10 > tickDelta * 10)
															j10 = tickDelta * 10;
														if (j10 > rsInterface.scrollMax - rsInterface.height
																- rsInterface.scrollPosition)
															j10 = rsInterface.scrollMax - rsInterface.height
																	- rsInterface.scrollPosition;
														rsInterface.scrollPosition += j10;
														anInt1088 -= j10;
													}
												} else if (atInventoryInterfaceType != 0 && atInventoryIndex == i3
														&& atInventoryInterface == class9_1.id) {
													itemSprite.drawTransparentSprite(xPos, yPos, 128);
												} else {
													// itemSprite.drawSprite(k5, j6);
													/**
													 * Draws item sprite
													 */
													// if u want the glow to render after the item (instead of before it)
													// then just move the if statement below after itemSprite.drawTransparentSprite(...)
													if(itemDef.glowColor != -1) {
														Rasterizer2D.renderGlow(xPos, yPos, itemDef.glowColor, 32);
													}
													itemSprite.drawTransparentSprite(xPos, yPos, itemSpriteOpacity);
												}
												if (class9_1.id == RSInterface.selectedItemInterfaceId
														&& class9_1.itemSearchSelectedSlot > -1
														&& class9_1.itemSearchSelectedSlot == i3) {
													for (int i = 32; i > 0; i--) {
														Rasterizer2D.method338(yPos + j7, i, 256 - Byte.MAX_VALUE, 0x395D84, i,
																xPos + k6);
													}
													Rasterizer2D.method338(yPos + j7, 32, 256, 0x395D84, 32, xPos + k6);
												}

												if (class9_1.forceInvStackSizes || !smallSprite && !class9_1.hideInvStackSizes) {
													if (class9_1.parentID < 58040 || class9_1.parentID > 58048) {
														if (itemSprite.maxWidth == 33 || class9_1.inventoryAmounts[i3] != 1) {
															if (class9_1.id != 23121 || class9_1.id == 23121 && ((class9_1.inventoryItemId[i3] >> 15) & 0x1) == 0) {
																if (class9_1.invAlwaysInfinity || class9_1.id == Interfaces.SHOP_CONTAINER && class9_1.inventoryAmounts[i3] == 1_000_000_000) {
																	infinity.drawSprite(xPos + k6, yPos + j7);
																} else {
																	int k10 = class9_1.inventoryAmounts[i3];
																	int xOffset = class9_1.forceInvStackSizes ? 10 : 0;
																	if (k10 >= 1)
																		smallText.method385(0xFFFF00, intToKOrMil(k10), yPos + 9 + j7,
																				xOffset+ xPos + k6);
																	smallText.method385(0, intToKOrMil(k10), yPos + 10 + j7, xOffset+ xPos + 1 + k6);
																	if (k10 > 99999 && k10 < 10000000) {
																		smallText.method385(0xFFFFFF, intToKOrMil(k10), yPos + 9 + j7,
																				xOffset+ xPos + k6);
																	} else if (k10 > 9999999) {
																		smallText.method385(0x00ff80, intToKOrMil(k10), yPos + 9 + j7,
																				xOffset+ xPos + k6);
																	} else {
																		smallText.method385(0xFFFF00, intToKOrMil(k10), yPos + 9 + j7,
																				xOffset+ xPos + k6);
																	}
																}
															}
														}
													}
												}
											}
										}
									} else if (class9_1.sprites != null && i3 < 20) {
										Sprite class30_sub2_sub1_sub1_1 = class9_1.sprites[i3];
										if (class30_sub2_sub1_sub1_1 != null)
											class30_sub2_sub1_sub1_1.drawSprite(xPos, yPos);
									}
									i3++;
								}
							}
						} else if (class9_1.type == RSInterface.TYPE_BACKGROUND) {
							if(class9_1.forceStyle == Background.ForceType.NONE) {
								if(class9_1.sprite2 == null) {
									class9_1.sprite2 = Background.INSTANCE.buildBackground(class9_1.width, class9_1.height, class9_1.thin,false);
									class9_1.sprite1 = Background.INSTANCE.buildBackground(class9_1.width, class9_1.height, class9_1.thin,true);
								}
							} else if(class9_1.forceStyle == Background.ForceType.OSRS) {
								class9_1.sprite2 = Background.INSTANCE.buildBackground(class9_1.width, class9_1.height, class9_1.thin,true);
								class9_1.sprite1 = Background.INSTANCE.buildBackground(class9_1.width, class9_1.height, class9_1.thin,true);

							} else if(class9_1.forceStyle == Background.ForceType.NEW) {
								class9_1.sprite2 = Background.INSTANCE.buildBackground(class9_1.width, class9_1.height, class9_1.thin,false);
								class9_1.sprite1 = Background.INSTANCE.buildBackground(class9_1.width, class9_1.height, class9_1.thin,false);
							}

							Sprite sprite;

							sprite = class9_1.sprite1;
							if(class9_1.sprite1 != null) {
								sprite.drawAdvancedSprite(_x, _y);
							}
						} else if (class9_1.type == RSInterface.TYPE_DIVIDER) {
							if(class9_1.sprite1 == null) {
								if(class9_1.forceStyle == Background.ForceType.NONE) {
									class9_1.sprite1 = Divider.INSTANCE.buildDevider(class9_1.across,class9_1.width,true);
								} else if(class9_1.forceStyle == Background.ForceType.OSRS) {
									class9_1.sprite1 = Divider.INSTANCE.buildDevider(class9_1.across,class9_1.width,true);
								} else if(class9_1.forceStyle == Background.ForceType.NEW) {
									class9_1.sprite1 = Divider.INSTANCE.buildDevider(class9_1.across,class9_1.width,false);
								}
							}

							Sprite sprite;



							sprite = class9_1.sprite1;

							sprite.drawAdvancedSprite(_x, _y);
						} else if (class9_1.type == RSInterface.TYPE_RASTERIZER) {

							boolean hover1 = class9_1.toggled && class9_1.outerBorderHover != 0;
							boolean hover2 = class9_1.toggled && class9_1.innerBorderHover != 0;
							boolean hover3 = class9_1.toggled && class9_1.backgroundHover != 0;

							if(class9_1.rasterizerType == Rasterizer.RasterizerType.NORMAL) {
								Rasterizer2D.drawBox(_x,_y, class9_1.width, class9_1.height, hover1 ? class9_1.outerBorderHover : class9_1.outerBorder);
								Rasterizer2D.drawBox(_x + 1, _y + 1, class9_1.width - 2, class9_1.height - 2, hover2 ? class9_1.innerBorderHover : class9_1.innerBorder);
								Rasterizer2D.drawBox(_x + 2, _y + 2, class9_1.width - 4, class9_1.height - 4, hover3 ? class9_1.backgroundHover : class9_1.background);
							} else if(class9_1.rasterizerType == Rasterizer.RasterizerType.OUTLINE) {
								Rasterizer2D.drawBoxOutline(_x, _y, class9_1.width, class9_1.height, hover1 ? class9_1.outerBorderHover : class9_1.outerBorder);
								Rasterizer2D.drawBoxOutline(_x + 1, _y + 1, class9_1.width - 2, class9_1.height - 2, hover2 ? class9_1.innerBorderHover : class9_1.innerBorder);
								Rasterizer2D.drawTransparentBox(_x + 2, _y + 2, class9_1.width - 4, class9_1.height - 4, hover3 ? class9_1.backgroundHover : class9_1.background,170);
							} else if(class9_1.rasterizerType == Rasterizer.RasterizerType.GRAY_SCALE) {
								Rasterizer2D.filterGrayscale(_x, _y , class9_1.width, class9_1.height, 200);
							} else if(class9_1.rasterizerType == Rasterizer.RasterizerType.BOX) {
								Rasterizer2D.drawTransparentBox(_x,_y, class9_1.width, class9_1.height, hover1 ? class9_1.outerBorderHover : class9_1.outerBorder,class9_1.opacity);
							}
						} else if (class9_1.type == RSInterface.TYPE_BUTTON) {
							Sprite sprite = null;
							if(class9_1.sprite2 == null && class9_1.disabledSpriteId != -1) {
								class9_1.sprite2 = ImageCache.get(class9_1.disabledSpriteId);
							}
							if(class9_1.sprite1 == null && class9_1.enabledSpriteId != -1) {
								class9_1.sprite1 = ImageCache.get(class9_1.enabledSpriteId);
							}

							if (class9_1.toggled && class9_1.sprite2 != null) {
								sprite = class9_1.sprite2;
							} else {
								sprite = class9_1.sprite1;
							}


							if (sprite != null) {
								sprite.drawAdvancedSprite(_x, _y);
							}

						} else if (class9_1.type == 3) {
							boolean flag = false;
							if (anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
								flag = true;
							int colour;
							if (interfaceIsSelected(class9_1)) {
								colour = class9_1.secondaryColor;
								if (flag && class9_1.secondaryHoverColor != 0)
									colour = class9_1.secondaryHoverColor;
							} else {
								colour = class9_1.textColor;
								if (flag && class9_1.defaultHoverColor != 0)
									colour = class9_1.defaultHoverColor;
							}
							if (class9_1.opacity == 0) {
								if (class9_1.filled)
									Rasterizer2D.drawBox(_x, _y, class9_1.width, class9_1.height, colour
									);
								else
									Rasterizer2D.drawBoxOutline(_x, _y, class9_1.width,
											class9_1.height, colour);
							} else if (class9_1.filled)
								Rasterizer2D.drawTransparentBox(_x, _y, class9_1.width, class9_1.height, colour,
										256 - (class9_1.opacity & 0xff));
							else
								Rasterizer2D.drawTransparentBoxOutline(_x, _y, class9_1.width, class9_1.height,
										colour, 256 - (class9_1.opacity & 0xff)
								);
						} else if (class9_1.type == 4 || class9_1.type == RSInterface.TYPE_TEXT_DRAW_FROM_LEFT) {
							TextDrawingArea textDrawingArea = class9_1.textDrawingAreas;
							String s = class9_1.message;
							if (interfaceStringText) {
								s = class9_1.id + "";
							}
							boolean flag1 = false;
							if (anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
								flag1 = true;
							int i4 = 0;
							if (interfaceIsSelected(class9_1)) {
								i4 = class9_1.secondaryColor;
								if (flag1 && class9_1.secondaryHoverColor != 0)
									i4 = class9_1.secondaryHoverColor;
								if (class9_1.aString228.length() > 0)
									s = class9_1.aString228;
							} else {
								i4 = class9_1.textColor;
								if (flag1 && class9_1.defaultHoverColor != 0) {
									i4 = class9_1.defaultHoverColor;
									if (i4 == 49152)
										i4 = 16777215;
								}
							}
							if (class9_1.atActionType == 6 && continuedDialogue) {
								s = "Please wait...";
								i4 = 255;
							}
							if ((class9_1.parentID == 1151) || (class9_1.parentID == 12855)) {
								switch (i4) {
									case 16773120:
										i4 = 0xFE981F;
										break;
									case 7040819:
										i4 = 0xAF6A1A;
										break;
								}
							}
							if (class9_1.hoverText != null && !class9_1.hoverText.isEmpty()) {
								if (MouseHandler.mouseX > _x
										&& MouseHandler.mouseX < _x + class9_1.textDrawingAreas.getTextWidth(class9_1.message)
										&& MouseHandler.mouseY > _y && MouseHandler.mouseY < _y + 15) {
									s = class9_1.hoverText;
									i4 = class9_1.hoverTextColor;
								}
							}
							if((backDialogID != -1 || dialogID != -1 || class9_1.message.contains("Click here to continue")) &&
									(rsInterface.id == backDialogID || rsInterface.id == dialogID )){
								if(i4 == 0xffff00)
									i4 = 255;
								if(i4 == 49152)
									i4 = 0xffffff;
							}
							for (int l6 = _y + textDrawingArea.anInt1497; s.length() > 0; l6 += textDrawingArea.anInt1497) {
								if (s.indexOf("%") != -1) {
									do {
										int k7 = s.indexOf("%1");
										if (k7 == -1)
											break;
										if (class9_1.id < 4000 || class9_1.id > 5000 && class9_1.id != 13921
												&& class9_1.id != 13922 && class9_1.id != 12171 && class9_1.id != 12172)
											s = s.substring(0, k7) + methodR(extractInterfaceValues(class9_1, 0))
													+ s.substring(k7 + 2);
										else
											s = s.substring(0, k7) + interfaceIntToString(extractInterfaceValues(class9_1, 0))
													+ s.substring(k7 + 2);
									} while (true);
									do {
										int l7 = s.indexOf("%2");
										if (l7 == -1)
											break;
										s = s.substring(0, l7) + interfaceIntToString(extractInterfaceValues(class9_1, 1))
												+ s.substring(l7 + 2);
									} while (true);
									do {
										int i8 = s.indexOf("%3");
										if (i8 == -1)
											break;
										s = s.substring(0, i8) + interfaceIntToString(extractInterfaceValues(class9_1, 2))
												+ s.substring(i8 + 2);
									} while (true);
									do {
										int j8 = s.indexOf("%4");
										if (j8 == -1)
											break;
										s = s.substring(0, j8) + interfaceIntToString(extractInterfaceValues(class9_1, 3))
												+ s.substring(j8 + 2);
									} while (true);
									do {
										int k8 = s.indexOf("%5");
										if (k8 == -1)
											break;
										s = s.substring(0, k8) + interfaceIntToString(extractInterfaceValues(class9_1, 4))
												+ s.substring(k8 + 2);
									} while (true);
								}
								int l8 = s.indexOf("\\n");
								String s1;
								if (l8 != -1) {
									s1 = s.substring(0, l8);
									s = s.substring(l8 + 2);
								} else {
									s1 = s;
									s = "";
								}
								RSFont font = null;
								if (textDrawingArea == smallText) {
									font = newSmallFont;
								} else if (textDrawingArea == aTextDrawingArea_1271) {
									font = newRegularFont;
								} else if (textDrawingArea == chatTextDrawingArea) {
									font = newBoldFont;
								} else if (textDrawingArea == aTextDrawingArea_1273) {
									font = newFancyFont;
								}
								if (rsInterface.parentID == 49100 || rsInterface.parentID == 51100 || rsInterface.parentID == 53100) {
									int parent = rsInterface.parentID == 49100 ? 49100 : rsInterface.parentID == 51100 ? 51100 : 53100;
									int subId = (class9_1.id - parent) % 100;
									if (subId > achievementCutoff) {
										continue;
									}
								}
								if (interfaceStringText) {
									s1 = "" + class9_1.id;
								}

								if (class9_1.type == RSInterface.TYPE_TEXT_DRAW_FROM_LEFT) {
									int width = font.getTextWidth(s1);
									font.drawBasicString(s1, _x - width, l6, i4, class9_1.textShadow ? 0 : -1);
								} else if (class9_1.centerText) {
									font.drawCenteredString(s1, _x + class9_1.width / 2, l6, i4, class9_1.textShadow ? 0 : -1);
								} else {
									font.drawBasicString(s1, _x, l6, i4, class9_1.textShadow ? 0 : -1);
								}
							}
						} else if (class9_1.type == 5) {
							Sprite sprite;
							if(interfaceIsSelected(class9_1) || class9_1.active) {
								sprite = class9_1.sprite2;
							} else {
								sprite = class9_1.sprite1;
							}

							if(spellSelected == 1 && class9_1.id == spellID && spellID != 0 && sprite != null) {
								if (spellID == 23619) {
									ImageCache.get(2145).drawAdvancedSprite(_x, _y);
								} else if(spellID == 23633) {
									ImageCache.get(2146).drawAdvancedSprite(_x, _y);
								} else if(spellID == 23603) {
									ImageCache.get(2147).drawAdvancedSprite(_x, _y);
								} else if(spellID == 23583) {
									ImageCache.get(2148).drawAdvancedSprite(_x, _y);
								} else {
									sprite.drawSprite(_x, _y, 0xffffff);
								}
							}

							if(sprite != null) {
								if (class9_1.drawsTransparent) {

									sprite.drawAdvancedSprite(_x, _y, class9_1.opacity);
								} else {
									sprite.drawSprite(_x, _y);
								}
							}
						} else if (class9_1.type == 6) {

							Rasterizer3D.drawImage(class9_1.width / 2 + _x, class9_1.height / 2 + _y);

							int i5 = Rasterizer3D.SINE[class9_1.modelRotation1] * class9_1.modelZoom >> 16;
							int l5 = Rasterizer3D.COSINE[class9_1.modelRotation1] * class9_1.modelZoom >> 16;
							boolean selected = interfaceIsSelected(class9_1);
							int emoteAnimation;
							if (selected)
								emoteAnimation = class9_1.enabledAnimationId;
							else
								emoteAnimation = class9_1.disabledAnimationId;
							Model model;
							try {
								if (emoteAnimation == -1) {
									model = class9_1.getAnimatedModel(null, -1, selected);
								} else {
									SequenceDefinition sequenceDefinition = SequenceDefinition.get(emoteAnimation);
									model = class9_1.getAnimatedModel(
											sequenceDefinition,
											class9_1.currentFrame,
											selected);
								}

								if (model != null) {
									model.calculateBoundsCylinder();
									model.renderonGpu = false;
									model.renderModel(class9_1.modelRotation2, 0, class9_1.modelRotation1, 0, i5, l5);
									model.renderonGpu = true;
								}

							} catch (Exception e) {
							}

							Rasterizer3D.clips.method1358();
						} else if (class9_1.type == 7) {
							TextDrawingArea textDrawingArea_1 = class9_1.textDrawingAreas;
							int k4 = 0;
							for (int j5 = 0; j5 < class9_1.height; j5++) {
								for (int i6 = 0; i6 < class9_1.width; i6++) {
									if (class9_1.inventoryItemId[k4] > 0) {
										ItemDefinition itemDef = ItemDefinition.lookup(class9_1.inventoryItemId[k4] - 1);
										String s2 = itemDef.name;
										if (itemDef.stackable || class9_1.inventoryAmounts[k4] != 1)
											s2 = s2 + " x" + intToKOrMilLongName(class9_1.inventoryAmounts[k4]);
										int i9 = _x + i6 * (115 + class9_1.invSpritePadX);
										int k9 = _y + j5 * (12 + class9_1.invSpritePadY);
										if (class9_1.centerText)
											textDrawingArea_1.method382(class9_1.textColor, i9 + class9_1.width / 2, s2, k9,
													class9_1.textShadow);
										else
											textDrawingArea_1.method389(class9_1.textShadow, i9, class9_1.textColor, s2, k9);
									}
									k4++;
								}
							}
						} else if (class9_1.type == 8
								&& (anInt1500 == class9_1.id || anInt1044 == class9_1.id || anInt1129 == class9_1.id)
								&& anInt1501 == 50 && !isMenuOpen) {
							if (class9_1.parentID == 3917) {
								return;
							}
							int boxWidth = 0;
							int boxHeight = 0;

							/**
							 * Skill tab hovers Remove "next level at" and "remaining" for xp if we're level
							 * 99.
							 */

							TextDrawingArea textDrawingArea_2 = aTextDrawingArea_1271;
							for (String s1 = class9_1.message; s1.length() > 0;) {
								if (s1.indexOf("%") != -1) {
									do {
										int k7 = s1.indexOf("%1");
										if (k7 == -1)
											break;
										s1 = s1.substring(0, k7) + interfaceIntToString(extractInterfaceValues(class9_1, 0))
												+ s1.substring(k7 + 2);
									} while (true);
									do {
										int l7 = s1.indexOf("%2");
										if (l7 == -1)
											break;
										s1 = s1.substring(0, l7) + interfaceIntToString(extractInterfaceValues(class9_1, 1))
												+ s1.substring(l7 + 2);
									} while (true);
									do {
										int i8 = s1.indexOf("%3");
										if (i8 == -1)
											break;
										s1 = s1.substring(0, i8) + interfaceIntToString(extractInterfaceValues(class9_1, 2))
												+ s1.substring(i8 + 2);
									} while (true);
									do {
										int j8 = s1.indexOf("%4");
										if (j8 == -1)
											break;
										s1 = s1.substring(0, j8) + interfaceIntToString(extractInterfaceValues(class9_1, 3))
												+ s1.substring(j8 + 2);
									} while (true);
									do {
										int k8 = s1.indexOf("%5");
										if (k8 == -1)
											break;
										s1 = s1.substring(0, k8) + interfaceIntToString(extractInterfaceValues(class9_1, 4))
												+ s1.substring(k8 + 2);
									} while (true);
								}
								int l7 = s1.indexOf("\\n");

								String s4;
								if (l7 != -1) {
									s4 = s1.substring(0, l7);
									s1 = s1.substring(l7 + 2);
								} else {
									s4 = s1;
									s1 = "";
								}
								int j10 = textDrawingArea_2.getTextWidth(s4);
								if (j10 > boxWidth) {
									boxWidth = j10;
								}
								boxHeight += textDrawingArea_2.anInt1497 + 1;
							}

							if (tabInterfaceIDs[tabID] == 17200) {
								return;
							}
							boxWidth += 6;
							boxHeight += 7;
							int xPos = (_x + class9_1.width) - 5 - boxWidth;
							int yPos = _y + class9_1.height + 5;
							if (xPos < _x + 5)
								xPos = _x + 5;
							if (xPos + boxWidth > xPosition + rsInterface.width)
								xPos = (xPosition + rsInterface.width) - boxWidth;
							if (yPos + boxHeight > yPosition + rsInterface.height)
								yPos = (_y - boxHeight);
							switch (class9_1.id) {
								case 9217:
								case 9220:
								case 9223:
								case 9226:
								case 9229:
								case 9232:
								case 9235:
								case 9238:
									xPos -= 80;
									break;
								case 9239:
									yPos -= 100;
									break;
							}
							if (class9_1.inventoryhover) {

								//System.out.println(canvasWidth - 648);
								if (xPos + boxWidth > canvasWidth - 8 - boxWidth + 100) {
									xPos = canvasWidth - 8 - boxWidth;
								}
								if (yPos + boxHeight > canvasHeight - 118 - boxHeight + 100
									&& yPos + boxHeight < canvasHeight - 118 - boxHeight + 120) {
									yPos = canvasHeight - 148 - boxHeight;
								} else if (yPos + boxHeight > canvasHeight - 118 - boxHeight + 100) {
									yPos = canvasHeight - 118 - boxHeight;
								}
							}
							Rasterizer2D.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0, boxWidth);
							Rasterizer2D.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
							String s2 = class9_1.message;
							for (int j11 = yPos + textDrawingArea_2.anInt1497 + 2; s2
									.length() > 0; j11 += textDrawingArea_2.anInt1497 + 1) {// anInt1497
								if (s2.indexOf("%") != -1) {
									do {
										int k7 = s2.indexOf("%1");
										if (k7 == -1)
											break;
										s2 = s2.substring(0, k7) + interfaceIntToString(extractInterfaceValues(class9_1, 0))
												+ s2.substring(k7 + 2);
									} while (true);
									do {
										int l7 = s2.indexOf("%2");
										if (l7 == -1)
											break;
										s2 = s2.substring(0, l7) + interfaceIntToString(extractInterfaceValues(class9_1, 1))
												+ s2.substring(l7 + 2);
									} while (true);
									do {
										int i8 = s2.indexOf("%3");
										if (i8 == -1)
											break;
										s2 = s2.substring(0, i8) + interfaceIntToString(extractInterfaceValues(class9_1, 2))
												+ s2.substring(i8 + 2);
									} while (true);
									do {
										int j8 = s2.indexOf("%4");
										if (j8 == -1)
											break;
										s2 = s2.substring(0, j8) + interfaceIntToString(extractInterfaceValues(class9_1, 3))
												+ s2.substring(j8 + 2);
									} while (true);
									do {
										int k8 = s2.indexOf("%5");
										if (k8 == -1)
											break;
										s2 = s2.substring(0, k8) + interfaceIntToString(extractInterfaceValues(class9_1, 4))
												+ s2.substring(k8 + 2);
									} while (true);
								}
								int l11 = s2.indexOf("\\n");
								String s5;
								if (l11 != -1) {
									s5 = s2.substring(0, l11);
									s2 = s2.substring(l11 + 2);
								} else {
									s5 = s2;
									s2 = "";
								}
								if (class9_1.centerText) {
									textDrawingArea_2.method382(yPos, xPos + class9_1.width / 2, s5, j11, false);
								} else {
									if (s5.contains("\\r")) {
										String text = s5.substring(0, s5.indexOf("\\r"));
										String text2 = s5.substring(s5.indexOf("\\r") + 2);
										textDrawingArea_2.method389(false, xPos + 3, 0, text, j11);
										int rightX = boxWidth + xPos - textDrawingArea_2.getTextWidth(text2) - 2;
										textDrawingArea_2.method389(false, rightX, 0, text2, j11);
									} else
										textDrawingArea_2.method389(false, xPos + 3, 0, s5, j11);
								}
							}

						} else if (class9_1.type == RSInterface.TYPE_HOVER || class9_1.type == RSInterface.TYPE_CONFIG_HOVER) {
							// Draw sprite
							boolean flag = false;

							if (class9_1.toggled) {
								class9_1.sprite1.drawAdvancedSprite(_x, _y, class9_1.opacity);
								flag = true;
								class9_1.toggled = false;
							} else {
								class9_1.sprite2.drawAdvancedSprite(_x, _y, class9_1.opacity);
							}

							// Draw text
							if (class9_1.message == null) {
								continue;
							}
							if (class9_1.centerText) {
								newRegularFont.drawCenteredString(class9_1.message, _x + class9_1.msgX, _y + class9_1.msgY,
										flag ? class9_1.defaultHoverColor : class9_1.textColor, 0x000000);

								// class9_1.rsFont.drawCenteredString(class9_1.message, _x + class9_1.msgX, _y +
								// class9_1.msgY,
								// flag ? class9_1.anInt216 : class9_1.textColor, 0);
							} else {
								newRegularFont.drawCenteredString(class9_1.message, _x + class9_1.msgX, _y + class9_1.msgY,
										flag ? class9_1.defaultHoverColor : class9_1.textColor, 0x000000);
								// class9_1.rsFont.drawBasicString(class9_1.message, _x + 5, _y + class9_1.msgY,
								// flag ? class9_1.anInt216 : class9_1.textColor, 0);
							}
						} else if (class9_1.type == RSInterface.TYPE_CONFIG) {
							Sprite sprite = class9_1.active ? class9_1.sprite2 : class9_1.sprite1;
							sprite.drawSprite(_x, _y);
						} else if (class9_1.type == RSInterface.TYPE_SLIDER) {
							Slider slider = class9_1.slider;
							if (slider != null) {
								slider.draw(_x, _y, 255);
							}
						} else if (class9_1.type == RSInterface.TYPE_DROPDOWN) {

							DropdownMenu d = class9_1.dropdown;

							int bgColour = class9_1.dropdownColours[2];
							int fontColour = 0xfe971e;
							int downArrow = 30;

							if (class9_1.hovered || d.isOpen()) {
								downArrow = 31;
								fontColour = 0xffb83f;
								bgColour = class9_1.dropdownColours[3];
							}

							Rasterizer2D.drawPixels(20, _y, _x, class9_1.dropdownColours[0], d.getWidth());
							Rasterizer2D.drawPixels(18, _y + 1, _x + 1, class9_1.dropdownColours[1], d.getWidth() - 2);
							Rasterizer2D.drawPixels(16, _y + 2, _x + 2, bgColour, d.getWidth() - 4);

							int xOffset = class9_1.centerText ? 3 : 16;
							if (rsInterface.id == 41900) {
								newRegularFont.drawCenteredString(d.getSelected(), _x + (d.getWidth() - xOffset) / 2, _y + 14,
										fontColour, 0);
							} else {
								newSmallFont.drawCenteredString(d.getSelected(), _x + (d.getWidth() - xOffset) / 2, _y + 14,
										fontColour, 0);
							}

							if (d.isOpen()) {
								// Up arrow
								cacheSprite3[29].drawSprite(_x + d.getWidth() - 18, _y + 2);

								Rasterizer2D.drawPixels(d.getHeight(), _y + 19, _x, class9_1.dropdownColours[0], d.getWidth());
								Rasterizer2D.drawPixels(d.getHeight() - 2, _y + 20, _x + 1, class9_1.dropdownColours[1],
										d.getWidth() - 2);
								Rasterizer2D.drawPixels(d.getHeight() - 4, _y + 21, _x + 2, class9_1.dropdownColours[3],
										d.getWidth() - 4);

								int yy = 2;
								for (int i = 0; i < d.getOptions().length; i++) {
									if (class9_1.dropdownHover == i) {
										if (class9_1.id == 28102) {
											Rasterizer2D.drawAlphaBox(_x + 2, _y + 19 + yy, d.getWidth() - 4, 13, 0xd0914d, 80);
										} else {
											Rasterizer2D.drawPixels(13, _y + 19 + yy, _x + 2, class9_1.dropdownColours[4],
													d.getWidth() - 4);
										}
										if (rsInterface.id == 41900) {
											newRegularFont.drawCenteredString(d.getOptions()[i],
													_x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xffb83f, 0);
										} else {
											newSmallFont.drawCenteredString(d.getOptions()[i],
													_x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xffb83f, 0);
										}

									} else {
										Rasterizer2D.drawPixels(13, _y + 19 + yy, _x + 2, class9_1.dropdownColours[3],
												d.getWidth() - 4);
										if (rsInterface.id == 41900) {
											newRegularFont.drawCenteredString(d.getOptions()[i],
													_x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xfe971e, 0);
										} else {
											newSmallFont.drawCenteredString(d.getOptions()[i],
													_x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xfe971e, 0);
										}

									}
									yy += 14;
								}
								drawScrollbar(d.getHeight() - 4, class9_1.scrollPosition, _y + 21, _x + d.getWidth() - 18,
										d.getHeight() - 5);

							} else {
								cacheSprite3[downArrow].drawSprite(_x + d.getWidth() - 18, _y + 2);
							}
						} else if (class9_1.type == RSInterface.TYPE_KEYBINDS_DROPDOWN) {

							DropdownMenu d = class9_1.dropdown;

							// If dropdown inverted, don't draw following 2 menus
							if (dropdownInversionFlag > 0) {
								dropdownInversionFlag--;
								continue;
							}

							Rasterizer2D.drawPixels(18, _y + 1, _x + 1, 0x544834, d.getWidth() - 2);
							Rasterizer2D.drawPixels(16, _y + 2, _x + 2, 0x2e281d, d.getWidth() - 4);
							newRegularFont.drawBasicString(d.getSelected(), _x + 7, _y + 15, 0xff8a1f, 0);
							cacheSprite3[82].drawSprite(_x + d.getWidth() - 18, _y + 2); // Arrow TODO

							if (d.isOpen()) {

								RSInterface.interfaceCache[class9_1.id - 1].active = true; // Alter stone colour

								int yPos = _y + 18;

								// Dropdown inversion for lower stones
								if (class9_1.inverted) {
									yPos = _y - d.getHeight() - 10;
									dropdownInversionFlag = 2;
								}

								Rasterizer2D.drawPixels(d.getHeight() + 12, yPos, _x + 1, 0x544834, d.getWidth() - 2);
								Rasterizer2D.drawPixels(d.getHeight() + 10, yPos + 1, _x + 2, 0x2e281d, d.getWidth() - 4);

								int yy = 2;
								int xx = 0;
								int bb = d.getWidth() / 2;

								for (int i = 0; i < d.getOptions().length; i++) {

									int fontColour = 0xff981f;
									if (class9_1.dropdownHover == i) {
										fontColour = 0xffffff;
									}

									if (xx == 0) {
										newRegularFont.drawBasicString(d.getOptions()[i], _x + 5, yPos + 14 + yy, fontColour,
												0x2e281d);
										xx = 1;

									} else {
										newRegularFont.drawBasicString(d.getOptions()[i], _x + 5 + bb, yPos + 14 + yy,
												fontColour, 0x2e281d);
										xx = 0;
										yy += 15;
									}
								}
							} else {
								RSInterface.interfaceCache[class9_1.id - 1].active = false;
							}
						} else if (class9_1.type == RSInterface.TYPE_ADJUSTABLE_CONFIG) {
							if (class9_1.id != 37010) {
								//Rasterizer2D.setDrawingArea(canvasHeight, 0, canvasWidth, 0);
							}
							if(class9_1.id == 37100) {
								if (_y < 41 || _y > 230) {
									return;
								}
							}
							Rasterizer2D.drawAlphaBox(_x, _y, class9_1.width, class9_1.height, class9_1.fillColor, class9_1.opacity);
							//Rasterizer2D.setDrawingArea(yPosition + class9_1.height, xPosition, xPosition + class9_1.width, yPosition);
							/**
							 int totalWidth = class9_1.width;
							 int spriteWidth = class9_1.sprite2.myWidth;
							 int totalHeight = class9_1.height;
							 int spriteHeight = class9_1.sprite2.myHeight;
							 Sprite behindSprite = class9_1.active ? class9_1.enabledAltSprite : class9_1.disabledAltSprite;

							 if (class9_1.toggled) {
							 behindSprite.drawSprite(_x, _y);
							 class9_1.sprite2.drawSprite(_x + (totalWidth / 2) - spriteWidth / 2,
							 _y + (totalHeight / 2) - spriteHeight / 2, class9_1.spriteOpacity);
							 class9_1.toggled = false;
							 } else {
							 behindSprite.drawSprite(_x, _y);
							 class9_1.sprite2.drawSprite(_x + (totalWidth / 2) - spriteWidth / 2,
							 _y + (totalHeight / 2) - spriteHeight / 2);
							 }
							 **/
						} else if (class9_1.type == 16) {
							drawInputField(class9_1, _x, _y, class9_1.width, class9_1.height);
						}else if (class9_1.type == RSInterface.TYPE_BOX) {
							// Draw outline
							Rasterizer2D.drawBox(_x - 2, _y - 2, class9_1.width + 4, class9_1.height + 4, 0x0e0e0c);
							Rasterizer2D.drawBox(_x - 1, _y - 1, class9_1.width + 2, class9_1.height + 2, 0x474745);
							// Draw base box
							if (class9_1.toggled) {
								Rasterizer2D.drawBox(_x, _y, class9_1.width, class9_1.height, class9_1.secondaryHoverColor);
								class9_1.toggled = false;
							} else {
								Rasterizer2D.drawBox(_x, _y, class9_1.width, class9_1.height, class9_1.hoverTextColor);
							}
						} else if (class9_1.type == 19) {
							if (class9_1.backgroundSprites.length > 1) {
								if (class9_1.sprite1 != null) {
									class9_1.sprite1.drawAdvancedSprite(_x, _y);
								}
							}
						} else if (class9_1.type == RSInterface.TYPE_STRING_CONTAINER) {
							int x = _x;
							int y = _y;

							// Set the scroll max based on the strings
							if (class9_1.scrollableContainerInterfaceId != 0) {
								RSInterface container = RSInterface.get(class9_1.scrollableContainerInterfaceId);
								int scrollMax = class9_1.invAutoScrollHeightOffset;
								for (String string : class9_1.stringContainer)
									scrollMax += class9_1.invSpritePadY;
								if (scrollMax > container.height + 1) {
									container.scrollMax = scrollMax;
								} else {
									container.scrollMax = container.height + 1;
								}

								container.scrollMax += container.stringContainerContainerExtraScroll;
							}

							// Draw the container
							for (String string : class9_1.stringContainer) {
								if (class9_1.centerText) {
									class9_1.font.drawCenteredString(string, x, y, class9_1.textColor, class9_1.textShadow ? 0 : -1);
								} else {
									class9_1.font.drawBasicString(string, x, y, class9_1.textColor, class9_1.textShadow ? 0 : -1);
								}
								y += class9_1.invSpritePadY;
							}
						} else if (class9_1.type == RSInterface.TYPE_HORIZONTAL_STRING_CONTAINER) {
							int x = _x;
							int y = _y;

							for (int i = 0; i < class9_1.stringContainer.size(); i++) {
								String string = class9_1.stringContainer.get(i);
								if (class9_1.centerText) {
									class9_1.font.drawCenteredString(string, x, y, class9_1.textColor, class9_1.textShadow ? 0 : -1);
								} else {
									class9_1.font.drawBasicString(string, x, y, class9_1.textColor, class9_1.textShadow ? 0 : -1);
								}
								x += 32 + class9_1.invSpritePadX;
								if ((i + 1) % class9_1.width == 0) {
									y += 32 + class9_1.invSpritePadY;
									x = _x;
								}
							}
						} else if (class9_1.type == RSInterface.TYPE_PROGRESS_BAR) {
							Rasterizer2D.drawPixels(class9_1.height, _y, _x, class9_1.fillColor, class9_1.width);
						} else if (class9_1.type == RSInterface.TYPE_PROGRESS_BAR_2021) {
							double percentage = class9_1.progressBar2021Percentage;
							int color = RSInterface.getRgbProgressColor(percentage);

							int progressBarWidth = (int) ((double) class9_1.width * percentage);
							Rasterizer2D.drawPixels(class9_1.height, _y, _x, color, progressBarWidth);
							Rasterizer2D.drawBorder(_x, _y, class9_1.width, class9_1.height, class9_1.fillColor);
						} else if (class9_1.type == RSInterface.TYPE_DRAW_BOX) {
							//DrawingArea.drawRoundedRectangle(_x, _y, class9_1.width, class9_1.height, class9_1.fillColor, class9_1.transparency, true, true);

							Rasterizer2D.drawTransparentBox(_x, _y, class9_1.width, class9_1.height, class9_1.fillColor, class9_1.opacity);
							Rasterizer2D.drawBorder(_x, _y, class9_1.width, class9_1.height, class9_1.borderColor);
						}

					if (interfaceText) {
						newSmallFont.drawString(class9_1.id + "", _x - 12, _y, 0xFFFFFFFF, 0, 256);
					}
				} catch (Exception | StackOverflowError e) {
					System.err.println(String.format("Error on interface child, parentId=%s, childIndex=%s, childId=%s", rsInterface.id, childId, rsInterface.children[childId]));
					e.printStackTrace();
				}
			}

			Bank.drawOnBank(rsInterface, 0, 0);

			if (!inheritDrawingArea) {
				Rasterizer2D.setDrawingArea(clipLeft, clipTop, clipRight, clipBottom);
			}
			if (rsInterface.id == 42000) {
				cacheSprite2[76].drawSprite1(24, 280, 200 + (int) (50 * Math.sin(loopCycle / 15.0)));
			}
			if (rsInterface.id == 16244) {
				if (MouseHandler.mouseX > 165 && MouseHandler.mouseX < 610 && MouseHandler.mouseY > 428 && MouseHandler.mouseY < 470) {
					Rasterizer2D.drawAlphaBox(165, 428, 444, 42, 0xffffff, 40);
				}
				// cacheSprite2[76].drawSprite1(24, 280,
				// 200 + (int) (50 * Math.sin(tick / 15.0)));
				// DrawingArea.drawBox(30, 400, 400, 400, 0xffffff);
			}
			if (tabID == 0) {
				// TODO blue spec bar
				// DrawingArea.drawBox(53, 250, 148, 24, 0xffffff);
			}

		} catch (Exception | StackOverflowError e) {
			System.err.println(String.format("Error on interface j=%d, xPosition=%d, id=%d, yPosition=%d", scrollPosition, xPosition, rsInterface.id, yPosition));
			e.printStackTrace();
		}
	}

	/**
	 * int state -1 for buy and sell buttons, 0 for buying, 1 for selling, 2 for
	 * canceled
	 * <p>
	 * int itemId The item that the player wants to buy
	 * <p>
	 * int currentlyBought/Sold How many items the player has bought/sold so far.
	 * <p>
	 * int totalAmount How many items the player wants to buy/sell in total.
	 */
	private int[][] grandExchangeInformation = new int[][] { { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 },
			{ -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 },
			{ -1, -1, -1, -1 },
			// { 0, 11802, 0, 10 },
			// { 0, 11804, 4, 14 },
			// { 2, 11826, 2, 2 },
			// { 1, 555, 47500, 52500 },
			// { 0, 560, 35000, 50000 },
			// { 1, 11847, 0, 1 },
			// { -1, -1, -1, -1 }
	};

	public final String methodR(int j) {
		if (j >= 0 && j < 10000)
			return String.valueOf(j);
		if (j >= 10000 && j < 10000000)
			return j / 1000 + "K";
		if (j >= 10000000 && j < 999999999)
			return j / 1000000 + "M";
		if (j >= 999999999)
			return "*";
		else
			return "?";
	}

	public void drawHoverBox(int xPos, int yPos, String text) {
		try {
			String[] results = text.split("\n");
			int height = (results.length * 16) + 3;
			int width;
			width = aTextDrawingArea_1271.getTextWidth(results[0]) + 6;
			for (int i = 1; i < results.length; i++)
				if (width <= aTextDrawingArea_1271.getTextWidth(results[i]) + 6)
					width = aTextDrawingArea_1271.getTextWidth(results[i]) + 6;
			Rasterizer2D.drawPixels(height, yPos, xPos, 0x544433, width);
			Rasterizer2D.fillPixels(xPos, width, height, 0, yPos);
			yPos += 14;
			for (int i = 0; i < results.length; i++) {
				aTextDrawingArea_1271.method389(false, xPos + 3, 0, results[i], yPos);
				yPos += 16;
			}
		}catch (Exception e) {}

	}

	public void drawBlackBox(int xPos, int yPos) {
		Rasterizer2D.drawBox(xPos - 2, yPos - 1, 1, 71, 0x726451);
		Rasterizer2D.drawBox(xPos + 174, yPos, 1, 69, 0x726451);
		Rasterizer2D.drawBox(xPos - 2, yPos - 2, 178, 1, 0x726451);
		Rasterizer2D.drawBox(xPos, yPos + 68, 174, 1, 0x726451);
		Rasterizer2D.drawBox(xPos - 1, yPos - 1, 1, 71, 0x2E2B23);
		Rasterizer2D.drawBox(xPos + 175, yPos - 1, 1, 71, 0x2E2B23);
		Rasterizer2D.drawBox(xPos, yPos - 1, 175, 1, 0x2E2B23);
		Rasterizer2D.drawBox(xPos, yPos + 69, 175, 1, 0x2E2B23);
		Rasterizer2D.drawTransparentBox(xPos, yPos, 174, 68, 0, 220);
	}

	public void loadTabArea() {
		if(getUserSettings().isOldGameframe() == false) {
			for (int i = 0; i < redStones.length; i++)
				redStones[i] = new Sprite("Gameframe/redstones/redstone" + i);

			for (int i = 0; i < sideIcons.length; i++)
				sideIcons[i] = new Sprite("Gameframe/sideicons/sideicon" + i);

			mapArea[0] = new Sprite("Gameframe/fixed/mapArea");
			mapArea[1] = new Sprite("Gameframe/fixed/mapBorder");
			mapArea[2] = new Sprite("Gameframe/resizable/mapArea");
			mapArea[3] = new Sprite("Gameframe/fixed/blackMapArea");
			mapArea[4] = new Sprite("Gameframe/resizable/mapBlack");
			mapArea[5] = new Sprite("Gameframe/fixed/topframe");
			mapArea[6] = new Sprite("Gameframe/fixed/chatborder");
			mapArea[7] = new Sprite("Gameframe/fixed/frame");

			tabAreaFixed = new Sprite("Gameframe/fixed/tabArea");
			compassImage = new Sprite("Gameframe/compassImage");
		}else {
			for (int i = 0; i < redStones.length; i++)
				redStones[i] = new Sprite("Gameframe317/redstones/redstone" + i);

			for (int i = 0; i < sideIcons.length; i++)
				sideIcons[i] = new Sprite("Gameframe317/sideicons/sideicon" + i);

			mapArea[0] = new Sprite("Gameframe/fixed/mapArea");
			mapArea[1] = new Sprite("Gameframe317/fixed/mapBorder");
			mapArea[2] = new Sprite("Gameframe317/resizable/mapArea");
			mapArea[3] = new Sprite("Gameframe317/fixed/blackMapArea");
			mapArea[4] = new Sprite("Gameframe317/resizable/mapBlack");
			mapArea[5] = new Sprite("Gameframe/fixed/topframe");
			mapArea[6] = new Sprite("Gameframe/fixed/chatborder");
			mapArea[7] = new Sprite("Gameframe/fixed/frame");

			tabAreaFixed = new Sprite("Gameframe317/fixed/tabArea");
			compassImage = new Sprite("Gameframe317/compassImage");
		}
	}
	public void randomizeBackground(IndexedImage background) {
		int j = 256;
		for (int k = 0; k < anIntArray1190.length; k++)
			anIntArray1190[k] = 0;

		for (int l = 0; l < 5000; l++) {
			int i1 = (int) (Math.random() * 128D * j);
			anIntArray1190[i1] = (int) (Math.random() * 256D);
		}
		for (int j1 = 0; j1 < 20; j1++) {
			for (int k1 = 1; k1 < j - 1; k1++) {
				for (int i2 = 1; i2 < 127; i2++) {
					int k2 = i2 + (k1 << 7);
					anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128]
							+ anIntArray1190[k2 + 128]) / 4;
				}

			}
			int ai[] = anIntArray1190;
			anIntArray1190 = anIntArray1191;
			anIntArray1191 = ai;
		}
		if (background != null) {
			int l1 = 0;
			for (int j2 = 0; j2 < background.subHeight; j2++) {
				for (int l2 = 0; l2 < background.subWidth; l2++)
					if (background.palettePixels[l1++] != 0) {
						int i3 = l2 + 16 + background.xOffset;
						int j3 = j2 + 16 + background.yOffset;
						int k3 = i3 + (j3 << 7);
						anIntArray1190[k3] = 0;
					}
			}
		}
	}

	private void method107(int i, int j, Buffer stream, Player player) {
		try {

			if ((i & 0x400) != 0) {
				player.anInt1543 = stream.method428();
				player.anInt1545 = stream.method428();
				player.anInt1544 = stream.method428();
				player.anInt1546 = stream.method428();
				player.initiate_movement = stream.method436() + loopCycle;
				player.cease_movement = stream.readUShortA() + loopCycle;
				player.forceMovementDirection = stream.method428();
				player.method446();
			}
			if ((i & 0x100) != 0) {
				player.readSpotAnimation(stream);

			}
			if ((i & 8) != 0) {
				int l = stream.method434();
				if (l == 65535)
					l = -1;
				int i2 = stream.readNegUByte();
				if (l == player.sequence && l != -1) {
					int i3 = SequenceDefinition.get(l).delayType;
					if (i3 == 1) {
						player.sequenceFrame = 0;
						player.sequenceFrameCycle = 0;
						player.sequenceDelay = i2;
						player.currentAnimationLoops = 0;
					}
					if (i3 == 2)
						player.currentAnimationLoops = 0;
				} else if (l == -1 || player.sequence == -1
						|| SequenceDefinition.get(l).forcedPriority >= SequenceDefinition.get(player.sequence).forcedPriority) {
					player.sequence = l;
					player.sequenceFrame = 0;
					player.sequenceFrameCycle = 0;
					player.sequenceDelay = i2;
					player.currentAnimationLoops = 0;
					player.stationaryPathPosition = player.smallXYIndex;
				}
			}
			if ((i & 4) != 0) {
				String textSpoken = stream.readString();

				// Only show flower poker chat to people within the area
				if (!player.inFlowerPokerArea() || localPlayer.inFlowerPokerChatProximity()) {
					player.spokenText = textSpoken;
					if (player.spokenText.charAt(0) == '~') {
						player.spokenText = player.spokenText.substring(1);
						if (player.lastForceChat == null || !player.lastForceChat.equals(textSpoken))
							pushMessage(player.spokenText, 2, player.displayName);
					} else if (player == localPlayer) {
						if (player.lastForceChat == null || !player.lastForceChat.equals(textSpoken))
							pushMessage(player.spokenText, 2, player.displayName);
					}
					player.textColour = 0;
					player.textEffect = 0;
					player.overheadTextCyclesRemaining = 150;
					player.lastForceChat = textSpoken;
				}
			}
			if ((i & 0x80) != 0) {
				int i1 = stream.method434();
				int rightsPrimaryValue = stream.readUnsignedByte();
				int j3 = stream.readNegUByte();
				int k3 = stream.currentPosition;
				PlayerRights rights = PlayerRights.forRightsValue(rightsPrimaryValue);
				if (player.displayName != null && player.visible) {
					long l3 = StringUtils.longForName(player.displayName);
					boolean flag = false;
					if (!rights.isStaffPosition()) {
						for (int i4 = 0; i4 < ignoreCount; i4++) {
							if (ignoreListAsLongs[i4] != l3)
								continue;
							flag = true;
							break;
						}
					}
					if (!flag && anInt1251 == 0)
						try {
							aStream_834.currentPosition = 0;
							stream.method442(j3, 0, aStream_834.payload);
							aStream_834.currentPosition = 0;
							String s = TextInput.method525(j3, aStream_834);

							// Only show flower poker chat to people within the area
							if (!player.inFlowerPokerArea() || localPlayer.inFlowerPokerChatProximity()) {
								player.spokenText = s;
								player.textColour = i1 >> 8;
								player.privelage = rightsPrimaryValue; // TODO remove privilege sending in send text player update flag
								player.textEffect = i1 & 0xff;
								player.overheadTextCyclesRemaining = 150;
								String crown = PlayerRights.buildCrownString(player.getDisplayedRights());
								String title = player.title != null && !player.title.isEmpty()
										? "<col=" + player.titleColor + ">" + player.title + "</col> "
										: "";
								pushMessage(s, 2, crown + title + player.displayName);
							}
						} catch (Exception exception) {
							exception.printStackTrace();
							Signlink.reporterror("cde2");
						}
				}
				stream.currentPosition = k3 + j3;
			}
			if ((i & 1) != 0) {
				player.interactingEntity = stream.method434();
				if (player.interactingEntity == 65535)
					player.interactingEntity = -1;
			}
			if ((i & 0x10) != 0) {
				int j1 = stream.readNegUByte();
				byte abyte0[] = new byte[j1];
				Buffer stream_1 = new Buffer(abyte0);
				stream.readBytes(j1, 0, abyte0);
				aStreamArray895s[j] = stream_1;
				player.updatePlayer(stream_1);
			}
			if ((i & 2) != 0) {
				player.anInt1538 = stream.method436();
				player.anInt1539 = stream.method434();
			}
			if ((i & 0x20) != 0) {
				player.currentHealth = stream.readUShort();
				player.maxHealth = stream.readUShort();

				player.addHitSplat(stream);
				player.addHealthBar(stream);

			}
			if ((i & 0x200) != 0) {
				player.currentHealth = stream.readUShort();
				player.maxHealth = stream.readUShort();

				player.addHitSplat(stream);
				player.addHealthBar(stream);
			}
		}catch (Exception e) {
			System.out.println("Error With: " + i);
		}

	}

	private void drawLoadingMessage(String messages) {
		int width = 0;
		for (String message : messages.split("<br>")) {
			int size = newRegularFont.getTextWidth(message);
			if(width <= newRegularFont.getTextWidth(message)) {
				width = size;
			}
		}

		int offset = isResized() ? 3 : 6;

		int height =  (12 * messages.split("<br>").length) + 4;

		Rasterizer2D.drawBox(offset,offset, width + 16, height + 6,0x000000);
		Rasterizer2D.drawBoxOutline(offset,offset, width + 16, height + 6,0xFFFFFF);

		int offsetY = 0;
		for (String message : messages.split("<br>")) {
			newRegularFont.drawCenteredString(message, offset + (width + 16) / 2, offset + 15 + offsetY,0xffffff,1);
			offsetY += 12;
		}

	}

	static int loadingType;
	static int mapsLoaded;
	static int totalMaps;

	public static int objectsLoaded;

	static int totalObjects;

	static int mouseWheelRotation;

	@Override
	public void draw(boolean redraw) {

		StaticSound.draw();
		callbacks.frame();
		updateCamera();

		if (gameState == 0) {
			this.drawInitial(loadingPercent, loadingText, false);
		} else if (gameState == 5) {
			drawLoginScreen();
		} else if (gameState != 10 && gameState != 11) {
			if (gameState == 20) {
				drawLoginScreen();
			} else if (gameState == 50) {
				drawLoginScreen();
			} else if (gameState == 25) {
				int percentage;
				if (loadingType == 1) {
					if (mapsLoaded > totalMaps) {
						totalMaps = mapsLoaded;
					}

					percentage = (totalMaps * 50 - mapsLoaded * 50) / totalMaps;
					drawLoadingMessage("Loading - please wait." + "<br>" + " (" + percentage + "%" + ")");
				} else if (loadingType == 2) {
					if (objectsLoaded > totalObjects) {
						totalObjects = objectsLoaded;
					}

					percentage = (totalObjects * 50 - objectsLoaded * 50) / totalObjects + 50;
					drawLoadingMessage("Loading - please wait." + "<br>" + " (" + percentage + "%" + ")");
				} else {
					drawLoadingMessage("Loading - please wait.");
				}
			} else if (gameState == 30) {
				drawGameScreen();
			} else if (gameState == 40) {
				drawLoadingMessage("Connection lost" + "<br>" + "Please wait - attempting to reestablish");
			} else if (gameState == 45) {
				drawLoadingMessage("Please wait...");
			}
		} else {
			drawLoginScreen();
		}

		if (gameState > 0) {
			rasterProvider.drawFull(0, 0);
		}

		mouseClickCount = 0;
	}

	private boolean isFriendOrSelf(String s) {
		if (s == null)
			return false;
		for (int i = 0; i < friendsCount; i++)
			if (s.equalsIgnoreCase(friendsList[i]))
				return true;
		return s.equalsIgnoreCase(localPlayer.displayName);
	}

	private static String combatDiffColor(int i, int j) {
		int k = i - j;
		if (k < -9)
			return "@red@";
		if (k < -6)
			return "@or3@";
		if (k < -3)
			return "@or2@";
		if (k < 0)
			return "@or1@";
		if (k > 9)
			return "@gre@";
		if (k > 6)
			return "@gr3@";
		if (k > 3)
			return "@gr2@";
		if (k > 0)
			return "@gr1@";
		else
			return "@yel@";
	}

	public static boolean centerInterface() {
		int minimumScreenWidth = 765, minimumScreenHeight = 610;
		if (canvasWidth >= minimumScreenWidth && canvasHeight >= minimumScreenHeight)
			return true;
		return false;
	}


	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public static int distanceToPoint(int x1, int y1, int x2, int y2) {
		int x = (int) Math.pow(x1 - x2, 2.0D);
		int y = (int) Math.pow(y1 - y2, 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}

	private void draw3dScreen() {
		if (!isResized()) {
			mapArea[1].drawSprite(516, 0);
			mapArea[7].drawSprite(0, 0);
		}
		if (snowVisible && Configuration.CHRISTMAS && !Client.instance.isResized()) {
			processWidgetAnimations(tickDelta, 11877);
			drawInterface(0, 0, RSInterface.interfaceCache[11877], 0);
		}

		if (getUserSettings().isShowEntityTarget()) {
			if (entityTarget != null) {
				entityTarget.draw();
			}
		}

		if (getUserSettings().isGameTimers()) {
			try {
				int startX = 516;
				int startY = !Client.instance.isResized() ? 294 : Client.canvasHeight - 209;
				GameTimerHandler.getSingleton().drawGameTimers(this, startX, startY);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		drawSplitPrivateChat();

		if (crossType == 1) {
			crosses[crossIndex / 100].drawSprite(crossX - 8, crossY - 8);
			anInt1142++;
			if (anInt1142 > 67) {
				anInt1142 = 0;
				stream.createFrame(78);
			}
		}
		if (crossType == 2) {
			crosses[4 + crossIndex / 100].drawSprite(crossX - 8, crossY - 8);
		}

		if (openWalkableWidgetID != -1) {
			processWidgetAnimations(tickDelta, openWalkableWidgetID);
			if (!Nightmare.instance.drawNightmareInterfaces(openWalkableWidgetID)) {
				RSInterface rsinterface = RSInterface.interfaceCache[openWalkableWidgetID];
				if (!isResized()) {
					drawInterface(0, 0, rsinterface, 0);
				} else {
					if (openWalkableWidgetID == 28000 || openWalkableWidgetID == 28020 || openWalkableWidgetID == 16210
							|| openWalkableWidgetID == 27500 || openWalkableWidgetID == 196) {
						/**
						 * Interfaces to draw at the top right corner nex to the minimap (Ex. Wildy
						 * Target)
						 **/
						drawInterface(0, canvasWidth - 730, rsinterface, 20);
					} else if (openWalkableWidgetID == 197) {
						drawInterface(0, canvasWidth - 530, rsinterface, -70);
					} else if (openWalkableWidgetID == 21100 || openWalkableWidgetID == 21119
							|| openWalkableWidgetID == 29230) {
						/**
						 * Interfaces to draw at top left corner (Ex. Pest Control)
						 **/
						drawInterface(0, 0, rsinterface, 0);
					} else if (openWalkableWidgetID == 201) {
						/** Duel arena interface **/
						drawInterface(0, canvasWidth - 510, rsinterface, -110);
					} else if (centerInterface() || openInterfaceID == 29230) {
						drawInterface(0, (canvasWidth / 2) - 356, rsinterface,
								!isResized() ? 0 : (canvasHeight / 2) - 230);
					} else {
						if (canvasWidth >= 900 && canvasHeight >= 650) {
							drawInterface(0, (canvasWidth / 2) - 356, RSInterface.interfaceCache[openWalkableWidgetID], !isResized() ? 0 : (canvasHeight / 2) - 230);
						} else {
							drawInterface(0, 0, RSInterface.interfaceCache[openWalkableWidgetID], 0);
						}
					}
				}
			}
		}

		BroadcastManager.display(this);

		if (anInt1055 == 1) {
			int x = !isResized() ? 476 : (canvasWidth - 30);
			int y = !isResized() ? 300 : 175;
			if (!isResized() && GameTimerHandler.getSingleton().hasActiveGameTimers()) {
				y -= 32;
			}
			multiOverlay.drawSprite(x, y);
		}

		if (fpsOn) {
			// char c = '\u01FB';
			int yPosition = 30;
			int xPosition = !isResized() ? 515 : canvasWidth - 222;
			int textColor = 0xffff00;
			if (super.fps < 15)
				textColor = 0xff0000;
			aTextDrawingArea_1271.method380("Fps: " + super.fps, xPosition, textColor, yPosition);
			yPosition += 15;
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			textColor = 0xffff00;
			if (j1 > 0x2000000 && lowMem)
				textColor = 0xff0000;
			aTextDrawingArea_1271.method380("Mem: " + j1 / 1000 + " mb", xPosition, textColor, yPosition);
			yPosition += 15;
		}

		int x = baseX + (localPlayer.x - 6 >> 7);
		int y = baseY + (localPlayer.y - 6 >> 7);
		int mapx = currentRegionX; // map region x
		int mapy = currentRegionY; // map region y
		if (clientData) {
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			aTextDrawingArea_1271.method385(0x00FF00, "Players Nearby: " + playerCount, 27, 5);
			aTextDrawingArea_1271.method385(0x00FF00, "Npcs Nearby: " + npcCount, 41, 5);

			if (mapx > 1000 || mapy > 1000) {
				aTextDrawingArea_1271.method385(0xffff00, "Current Region: " + mapx + ", " + mapy, 55, 5);
			} else {
				aTextDrawingArea_1271.method385(0xffff00, "Current Region: 0" + mapx + ", 0" + mapy, 55, 5);
			}
			for (int num = 0; num < regionLandIds.length; num++) {
				int[] flo = regionLandIds;
				aTextDrawingArea_1271.method385(0xffff00, "Floor map: " + Arrays.toString(flo), 69, 5);
			}
			for (int num = 0; num < regionLocIds.length; num++) {
				int[] obj = regionLocIds;
				aTextDrawingArea_1271.method385(0xffff00, "Object map: " + Arrays.toString(obj), 83, 5);
				// output: "Object map: "[1, 3, 5, 7, 9]"
			}

			aTextDrawingArea_1271.method385(0xffff00, "Map Data: " + regionLandIds[0] + ".dat", 97, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Fps: " + super.fps, 111, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Memory Used: " + j1/1024 + "MB", 125, 5);
			aTextDrawingArea_1271.method385(0xffff00,
					"Mouse: [" + MouseHandler.mouseX + ", " + MouseHandler.mouseY + "] ", 139, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Coordinates: X: " + x + ", Y: " + y, 153, 5);

			aTextDrawingArea_1271.method385(0xffff00,
					"Camera Position: X: " + cameraX + ", Y: " + cameraZ + ", Z: " + cameraY, 167, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Camera Curve: X: " + cameraYaw + ", Y: " + cameraPitch, 181,
					5);
			y = 181;
			y += 15;

		}

		if (openInterfaceID != -1) {
			processWidgetAnimations(tickDelta, openInterfaceID);
			RSInterface rsinterface = RSInterface.interfaceCache[openInterfaceID];
			if (!isResized())
				drawInterface(0, 0, rsinterface, 0);
			else
				drawInterface(0, (canvasWidth / 2) - 356, rsinterface, !isResized() ? 0 : (canvasHeight / 2) - 230);
		}
		method70();

		if (!isMenuOpen) {
			processRightClick();
			drawTopLeftTooltip();
		}

		if (drawGrid) {
			drawGrid();
		}


		if (anInt1104 != 0) {
			int j = anInt1104 / 50;
			int l = j / 60;
			j %= 60;
			int yPosition = !isResized() ? 329 : canvasHeight - 165;
			if (j < 10)
				aTextDrawingArea_1271.method385(0xffff00, "Xeros will be updating: " + l + ":0" + j, yPosition, 5);
			else
				aTextDrawingArea_1271.method385(0xffff00, "Xeros will be updating: " + l + ":" + j, yPosition, 5);

			anInt849++;
			if (anInt849 > 75) {
				anInt849 = 0;
				stream.createFrame(148);
			}
		}

		drawScreenBox();
		devConsole.draw_console();
	}

	private void addIgnore(long l) {
		try {
			if (l == 0L)
				return;

			String username = StringUtils.fixName(StringUtils.nameForLong(l));

			if (username.equalsIgnoreCase(localPlayer.displayName))
				return;

			if (ignoreCount >= 100) {
				pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					pushMessage(username + " is already on your ignore list", 0, "");
					return;
				}
			for (int k = 0; k < friendsCount; k++)
				if (friendsListAsLongs[k] == l) {
					pushMessage("Please remove " + username + " from your friend list first", 0, "");
					return;
				}

			//ignoreListAsLongs[ignoreCount++] = l;
			//needDrawTabArea = true;
			stream.createFrame(133);
			stream.writeQWord(l);
			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("45688, " + l + ", " + 4 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public void method114() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = maxPlayerCount;
			else
				j = playerIndices[i];
			Player player = players[j];
			if (player != null)
				entityUpdateBlock(player);
		}

	}

	private void processPendingSpawns() {
		for (SpawnedObject spawnedObject = (SpawnedObject) spawns
				.first(); spawnedObject != null; spawnedObject = (SpawnedObject) spawns
				.next()) {
			if (spawnedObject.getLongetivity > 0)
				spawnedObject.getLongetivity--;
			if (spawnedObject.getLongetivity == 0) {
				if (spawnedObject.getPreviousId < 0 || MapRegion.cached(spawnedObject.getPreviousId, spawnedObject.previousType)) {
					currentMapRegion.addPendingSpawnToScene(spawnedObject.plane, spawnedObject.group, spawnedObject.x,
							spawnedObject.y, spawnedObject.getPreviousId, spawnedObject.previousOrientation,
							spawnedObject.previousType);

					spawnedObject.remove();
				}
			} else {
				if (spawnedObject.delay > 0)
					spawnedObject.delay--;
				if (spawnedObject.delay == 0 && spawnedObject.x >= 1 && spawnedObject.y >= 1
						&& spawnedObject.x <= 102 && spawnedObject.y <= 102
						&& (spawnedObject.id < 0 || MapRegion.cached(spawnedObject.id, spawnedObject.type))) {

					currentMapRegion.addPendingSpawnToScene(spawnedObject.plane, spawnedObject.group, spawnedObject.x,
							spawnedObject.y, spawnedObject.id, spawnedObject.orientation, spawnedObject.type);

					spawnedObject.delay = -1;
					if (spawnedObject.id == spawnedObject.getPreviousId && spawnedObject.getPreviousId == -1)
						spawnedObject.remove();
					else if (spawnedObject.id == spawnedObject.getPreviousId
							&& spawnedObject.orientation == spawnedObject.previousOrientation
							&& spawnedObject.type == spawnedObject.previousType)
						spawnedObject.remove();
				}
			}
		}
	}

	final void openMenu(int x, int y) {
		final MenuOpened event = new MenuOpened();
		event.setMenuEntries(getMenuEntries());
		callbacks.post(event);

		if (event.isModified())
		{
			setMenuEntries(event.getMenuEntries());
		}

		int tempWidth = getFontBold12().getTextWidth("Choose Option");

		int var4;
		int var5;
		int realCount = 0;
		for (var4 = 0; var4 < getMenuOptionCount(); ++var4)
		{
			MenuEntry tempMenuEntry = getMenuEntries()[var4];
			if (tempMenuEntry.getParent() != null)
			{
				continue;
			}
			realCount++;
			String s = tempMenuEntry.getOption();
			if (!tempMenuEntry.getTarget().isEmpty())
			{
				s = s + " " + tempMenuEntry.getTarget();
			}

			if (tempMenuEntry.getType() == MenuAction.RUNELITE_SUBMENU)
			{
				s = s + " <col=ffffff><gt>";
			}
			tempWidth = Math.max(getFontBold12().getTextWidth(s), tempWidth);
		}

		tempWidth += 8;
		var4 = realCount * 15 + 22;
		var5 = x - tempWidth / 2;
		if (var5 + tempWidth > getCanvasWidth())
		{
			var5 = getCanvasWidth() - tempWidth;
		}

		if (var5 < 0)
		{
			var5 = 0;
		}

		int var6 = y;
		if (var4 + y > getCanvasHeight())
		{
			var6 = getCanvasHeight() - var4;
		}

		if (var6 < 0)
		{
			var6 = 0;
		}

		setMenuX(var5);
		setMenuY(var6);
		setMenuWidth(tempWidth);
		setMenuHeight(realCount * 15 + 22);

		isMenuOpen = true;

		setMenuScroll(0);
		menuScrollMax = 0;
		if (var4 > this.getCanvasHeight())
		{
			menuScrollMax = (var4 - this.getCanvasHeight() + 14) / 15;
		}
	}
	public void method117(Buffer stream) {
		stream.initBitAccess();
		int j = stream.readBits(1);
		if (j == 0)
			return;
		int k = stream.readBits(2);
		if (k == 0) {
			anIntArray894[anInt893++] = maxPlayerCount;
			return;
		}
		if (k == 1) {
			int l = stream.readBits(3);
			localPlayer.moveInDir(false, l);
			int k1 = stream.readBits(1);
			if (k1 == 1)
				anIntArray894[anInt893++] = maxPlayerCount;
			return;
		}
		if (k == 2) {
			int i1 = stream.readBits(3);
			localPlayer.moveInDir(true, i1);
			int l1 = stream.readBits(3);
			localPlayer.moveInDir(true, l1);
			int j2 = stream.readBits(1);
			if (j2 == 1)
				anIntArray894[anInt893++] = maxPlayerCount;
			return;
		}
		if (k == 3) {
			plane = stream.readBits(2);
			int j1 = stream.readBits(1);
			int i2 = stream.readBits(1);
			if (i2 == 1)
				anIntArray894[anInt893++] = maxPlayerCount;
			int k2 = stream.readBits(7);
			int l2 = stream.readBits(7);
			localPlayer.setPos(l2, k2, j1 == 1);
		}
	}

	public void nullLoader() {
		aBoolean831 = false;
		while (drawingFlames) {
			aBoolean831 = false;
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}
		}
		aBackgroundArray1152s = null;
		anIntArray850 = null;
		anIntArray851 = null;
		anIntArray852 = null;
		anIntArray853 = null;
		anIntArray1190 = null;
		anIntArray1191 = null;
		anIntArray828 = null;
		anIntArray829 = null;
	}

	public boolean processWidgetAnimations(int tick, int interfaceId) {
		boolean flag1 = false;
		RSInterface class9 = RSInterface.interfaceCache[interfaceId];
		for (int k = 0; k < class9.children.length; k++) {
			if (class9.children[k] == -1)
				break;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[k]];
			if (class9_1.type == 1)
				flag1 |= processWidgetAnimations(tick, class9_1.id);
			if (class9_1.type == 6 && (class9_1.disabledAnimationId != -1 || class9_1.enabledAnimationId != -1)) {
				boolean flag2 = interfaceIsSelected(class9_1);
				int l;
				if (flag2)
					l = class9_1.enabledAnimationId;
				else
					l = class9_1.disabledAnimationId;
				if (l != -1) {
					SequenceDefinition animation = SequenceDefinition.get(l);
					for (class9_1.anInt208 += tick; class9_1.anInt208 > animation.duration(class9_1.currentFrame);) {
						class9_1.anInt208 -= animation.duration(class9_1.currentFrame) + 1;
						class9_1.currentFrame++;
						if (class9_1.currentFrame >= animation.frameCount) {
							class9_1.currentFrame -= animation.frameCount;
							if (class9_1.currentFrame < 0 || class9_1.currentFrame >= animation.frameCount)
								class9_1.currentFrame = 0;
						}
						flag1 = true;
					}

				}
			}
		}

		return flag1;
	}

	private void delIgnore(long l) {
		try {
			if (l == 0L)
				return;
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					ignoreCount--;
					needDrawTabArea = true;
					System.arraycopy(ignoreListAsLongs, j + 1, ignoreListAsLongs, j, ignoreCount - j);

					stream.createFrame(74);
					stream.writeQWord(l);
					return;
				}

			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

    private FileArchive loadArchive(String name) throws IOException {
		int id = 0;
        if (name == "media.dat") {
            id = 0;
        } else if (name == "interface.dat") {
			id = 1;
		} else if (name == "title.dat") {
			id = 2;
        }
        FileArchive streamLoader = new FileArchive(AssetUtils.INSTANCE.getResource(name).openStream().readAllBytes());
        return streamLoader;
    }

	private int extractInterfaceValues(RSInterface widget, int id) {
		if (widget.scripts == null || id >= widget.scripts.length)
			return -2;
		try {
			int script[] = widget.scripts[id];
			int accumulator = 0;
			int counter = 0;
			int operator = 0;
			do {
				int instruction = script[counter++];
				int value = 0;
				byte next = 0;
				if (instruction == 0)
					return accumulator;
				if (instruction == 1)
					value = currentLevels[script[counter++]];
				if (instruction == 2)
					value = maximumLevels[script[counter++]];
				if (instruction == 3)
					value = currentExp[script[counter++]];
				if (instruction == 4) { // Check for item inside interface, interfaceId = ai[l++]
					RSInterface inventoryContainer = RSInterface.interfaceCache[script[counter++]];
					int item = script[counter++];
					if (item >= 0 && item < Js5List.getConfigSize(Js5ConfigType.ITEM)
						&& (!ItemDefinition.lookup(item).members || isMembers)) {
						int itemId = item + 1;
						int actualItemId = item;
						for (int slot = 0; slot < inventoryContainer.inventoryItemId.length; slot++) {
							if (inventoryContainer.inventoryItemId[slot] == itemId)
								value += inventoryContainer.inventoryAmounts[slot];
						}

						if (inventoryContainer.id == 3214) {
							RSInterface equipment = RSInterface.interfaceCache[1688];
							if (actualItemId == Items.NATURE_RUNE) {
								if (equipment.hasItem(Items.BRYOPHYTAS_STAFF)) {
									return Integer.MAX_VALUE;
								}
							}
							if (actualItemId == Items.FIRE_RUNE) {
								if (equipment.hasItem(Items.TOME_OF_FIRE)) {
									return Integer.MAX_VALUE;
								}
							}
						}

						if ((runePouch[0][0] + 1) == itemId) {
							value += runePouch[0][1];
						}
						if ((runePouch[1][0] + 1) == itemId) {
							value += runePouch[1][1];
						}
						if ((runePouch[2][0] + 1) == itemId) {
							value += runePouch[2][1];
						}
					}
				}
				if (instruction == 5) {
					value = variousSettings[script[counter++]];
				}
				if (instruction == 6)
					value = SKILL_EXPERIENCE[maximumLevels[script[counter++]] - 1];
				if (instruction == 7)
					value = (variousSettings[script[counter++]] * 100) / 46875;
				if (instruction == 8)
					value = localPlayer.combatLevel;
				if (instruction == 9) {
					for (int l1 = 0; l1 < Skills.SKILLS_COUNT; l1++)
						if (Skills.SKILLS_ENABLED[l1])
							value += maximumLevels[l1];
				}
				if (instruction == 10) {
					RSInterface equipmentContainer = RSInterface.interfaceCache[script[counter++]];
					int item = script[counter++] + 1;
					if (item >= 0 && item < Js5List.getConfigSize(Js5ConfigType.ITEM)
						&& (!ItemDefinition.lookup(item).members || isMembers)) {
						for (int stored = 0; stored < equipmentContainer.inventoryItemId.length; stored++) {
							if (equipmentContainer.inventoryItemId[stored] != item)
								continue;
							value = 0x3b9ac9ff;
							break;
						}

					}
				}
				if (instruction == 11)
					value = energy;
				if (instruction == 12)
					value = weight;
				if (instruction == 13) {
					int bool = variousSettings[script[counter++]];
					int shift = script[counter++];
					value = (bool & 1 << shift) == 0 ? 0 : 1;
				}
				if (instruction == 14) {
					int index = script[counter++];
					VariableBits bits = VariableBits.lookup(index);
					int setting = bits.baseVar;
					int low = bits.startBit;
					int high = bits.endBit;
					int mask = anIntArray1232[high - low];
					value = variousSettings[setting] >> low & mask;
				}
				if (instruction == 15)
					next = 1;
				if (instruction == 16)
					next = 2;
				if (instruction == 17)
					next = 3;
				if (instruction == 18)
					value = (localPlayer.x >> 7) + baseX;
				if (instruction == 19)
					value = (localPlayer.y >> 7) + baseY;
				if (instruction == 20)
					value = script[counter++];
				if (next == 0) {
					if (operator == 0)
						accumulator += value;
					if (operator == 1)
						accumulator -= value;
					if (operator == 2 && value != 0)
						accumulator /= value;
					if (operator == 3)
						accumulator *= value;
					operator = 0;
				} else {
					operator = next;
				}
			} while (true);
		} catch (Exception _ex) {
			return -1;
		}
	}

	public void drawTopLeftTooltip() {
		if (devConsole.console_open)
			return;

		if (fullscreenInterfaceID != -1) {
			return;
		}

		if (tabInterfaceIDs[tabID] == 17200 && menuActions[1].contains("ctivate")) {
			menuActions[1] = "Select";
			menuOpcodes[1] = 850;
			menuOptionsCount = 2;
		}
		if (menuOptionsCount < 2 && itemSelected == 0 && spellSelected == 0)
			return;
		String s;
		if (itemSelected == 1 && menuOptionsCount < 2)
			s = "Use " + selectedItemName + " with...";
		else if (spellSelected == 1 && menuOptionsCount < 2)
			s = spellTooltip + "...";
		else
			s = getMenuString(menuOptionsCount - 1);

		if(menuOptionsCount > 2) {
			s = s + colorStartTag(16777215) + " " + '/' + " " + (menuOptionsCount - 2) + " more options";
		}

		newBoldFont.drawString(s, 8, 19, 0xffffff, 0, 255);
	}

	static String colorStartTag(int var0) {
		return "<col=" + Integer.toHexString(var0) + ">";
	}

	static String getMenuString(final int menuIndex) {
		if (menuIndex < 0) {
			return "";
		} else {
			final String menuTarget = menuTargets[menuIndex];
			final String menuAction = menuActions[menuIndex];
			return menuTarget != null && !menuTarget.isEmpty()
					? menuAction + " " + menuTarget
					: menuAction;
		}
	}

	public String toolTip;

	private Sprite compassImage;
	private Sprite[] mapArea = new Sprite[8];

	private Sprite eventIcon;
	public Sprite bankDivider;

	private int[][] getCustomMapIcons() {
		return new int[][] {
				//map icons minimap icons map sprites
				/*
				0 gen store
				1 sword
				2 mage
				3 b axe
				4 med helm
				5 bank
				6 quest
				7 jewerlly
				8 mining
				9 furnace
				10 smithing
				11 dummy
				12 dungeon
				13 leave dung
				14 shortcut
				15 bstaff
				16 gray top
				17 gray legs
				18 small sword
				19 arrow
				20 shield
				21 altar
				22 herb
				23 ring
				24 red gem
				25 crafting chisel
				26 ccandle
				27 fishing rod
				28 fish spot
				29 green top
				30 potions
				31 silk
				32 kebab
				33 beer
				34 chisel?
				35 brown top
				36 woodcutting
				37 wheel
				38 bread
				39 fork and knife
				40 minigame
				41 water
				42 pot
				43 pink skirt
				44 bowl
				45 grain
				46 golden axe
				47 chain body
				48 gray rock, spices?
				49 red man
				50 bowl with red stuff
				51 agility
				52 apple
				53 slayer
				54 haircut
				55 shovel
				56 appearence mask editor
				57 travel arrow
				58 purple portal
				59 pot
				60 fence
				61 barrel
				62 butter
				63 water
				64 hunter
				65 vote
				66 leather
				67 house
				68 saw
				69 hammer and rock
				70 shortcut
				71 box
				72 gift box
				73 sand pit
				74 green achievement star
				75 pets
				76 bounty icon
				77 sword and shirt icon
				78 logs
				79 plants
				80 runecrafting
				81 rc rock
				82 grand exchange
				83 purple star
				84 brown star
				85 question mark tutorial icons
				86 bank
				87 lock
				88 wc
				89 mining
				90 fishing
				91 hunter
				92 smithing
				93 crafting
				94 cooking
				95 melee
				96 prayer
				97 dungeon
				98 ironman
				99 bounty hunter
				100 bond
				101 holiday gift
				102 bottle
				103 partyhat
				104 newspaper
				105 cup of tea
				106 bell
				107 red bottle
				108 whichs pot
				109 rope
				110 hand
				111 band aid
				112 ancient altar
				113 bowl
				 */
				// sprite id, x, y
			//	{0, 3084, 3494}, // general store
				{53, 3076, 3504}, // slayer area updated
			//	{94, 3067, 3472}, // wildy slayer area
				{30, 3080, 3492}, // pool icon
			//	{9, 3089, 3475}, // hespori entrance
			//	{98, 3092, 3480}, // ironman store
				//{77, 3110, 3495}, //supplies area
				{24, 3089, 3505}, //thieving red gem done
				//{66, 3091, 3470}, //thieving leather
				{38, 3095, 3501}, //thieving bakers stall done
				{31, 3098, 3505}, //thieving silk
				{34, 3096, 509}, //thieving silver done
				//{51, 3093, 3474}, //agility
				//{58, 3103, 3471}, //house portal
				{5, 3094, 3491}, //bank
			//	{5, 3119, 3506}, //bank
			//	{112, 3099, 3513}, //ancient altar
				{21, 3089, 3491}, // altar
			//	{57, 3087, 3494}, // travel arrow
			//	{28, 3125, 3490} //fishing
				/*{5, 3090, 3499}, // bank
                {5, 3077, 3517}, // bank
                {5, 3090, 3517}, // bank

                {56, 3094, 3516}, // teleport
                {56, 3097, 3500}, // teleport
                {77, 3097, 3507}, // runecraft
                {64, 3076, 3508}, // housing portal
                {19, 3085, 3515}, // altar
                {79, 3094, 3497}, // grand exchange*/

				// home
//				{5, 3097, 3490}, // bank
//				{5, 3089, 3507}, // bank wild
//				{79, 3091, 3495}, // grand exchange
//				{51, 3079, 3493}, // slayer
//				{0, 3080, 3511}, // general store
//				{56, 3086, 3496}, // teleport
//				{77, 3104, 3491}, // runecraft
//				{64, 3086, 3485}, // housing portal
				//{54, 2326, 3177}, // makeover
				//{44, 2329, 3162}, // star sprite
				//{19, 3086, 3510}, // altar
				//{72, 3089, 3512}, // bounty shop
				//{55, 3086, 3490}, // tutorial
				//{76, 2356, 3152}, // flax

				//skill area
				//{10, 3109, 3496}, // anvil
				//{9, 3109, 3500}, // furnace
				//{35, 3106, 3501}, // spinning wheel

				//{19, 3099, 3507}, // altar
				//{11, 3110, 3517}, // training dummies
				//{72, 2326, 3179}, // bounty
				//{38, 2326, 3175}, // achievement
		};
	}

	private void drawMinimap() {

		int xOffset = !isResized() ? 516 : 0;

		/** Black minimap **/
		if (minimapState == 2) {
			if (!isResized()) {
				mapArea[3].drawSprite(xOffset, 4);
			} else {
				mapArea[2].drawSprite(canvasWidth - 183, 0);
				mapArea[4].drawSprite(canvasWidth - 160, 8);
			}
			if(getUserSettings().isOldGameframe() && !isResized() ){
				compassImage.rotate(33, camAngleY, anIntArray1057, 256, anIntArray968,
					(!isResized() ? 28 + xOffset : 25), 4,
					(!isResized() ? 27 + xOffset : canvasWidth - 178), 33, 25);
			}else {
				compassImage.rotate(33, camAngleY, anIntArray1057, 256, anIntArray968,
					(!isResized() ? 25 : 25) , 4,
					(!isResized() ? 29 + xOffset : canvasWidth - 178) , 33, 25);
			}

			return;
		}

		// If not loaded in don't draw mapback
		if (gameState == GameState.LOGGED_IN.getState()) {

			int i = camAngleY + camAngleY & 0x7ff;
			int j = 48 + localPlayer.x / 32;
			int l2 = 464 - localPlayer.y / 32;

			int positionX = (!isResized() ? 9 : 7);
			int positionY = (!isResized() ? 51 + xOffset : canvasWidth - 160);

			minimapImage.rotate(151, i, anIntArray1229, 256 + minimapZoom, anIntArray1052, l2, positionX , positionY, 151, j);

			for (int[] icon : getCustomMapIcons()) {
				markMinimap(mapFunctions[icon[0]], ((icon[1] - baseX) * 4 + 2) - Client.localPlayer.x / 32, ((icon[2] - baseY) * 4 + 2) - Client.localPlayer.y / 32);
			}

			for (int j5 = 0; j5 < mapIconAmount; j5++) {
				int k = (minimapHintX[j5] * 4 + 2) - localPlayer.x / 32;
				int i3 = (minimapHintY[j5] * 4 + 2) - localPlayer.y / 32;
				markMinimap(minimapHint[j5], k, i3);
			}


			if (Configuration.HALLOWEEN) {
				switch (plane) {
					case 0:
						markMinimap(eventIcon, ((3088 - baseX) * 4 + 2) - localPlayer.x / 32,
							((3494 - baseY) * 4 + 2) - localPlayer.y / 32);
						markMinimap(eventIcon, ((2607 - baseX) * 4 + 2) - localPlayer.x / 32,
							((4773 - baseY) * 4 + 2) - localPlayer.y / 32);
						markMinimap(eventIcon, ((3103 - baseX) * 4 + 2) - localPlayer.x / 32,
							((3482 - baseY) * 4 + 2) - localPlayer.y / 32);
						break;
				}
			}
			if (Configuration.CHRISTMAS && Configuration.CHRISTMAS_EVENT) {
				switch (plane) {
					case 0:
						markMinimap(minimapIcons[0], ((3086 - baseX) * 4 + 2) - localPlayer.x / 32,
							((3498 - baseY) * 4 + 2) - localPlayer.y / 32);
						markMinimap(minimapIcons[0], ((2832 - baseX) * 4 + 2) - localPlayer.x / 32,
							((3798 - baseY) * 4 + 2) - localPlayer.y / 32);
						markMinimap(minimapIcons[0], ((2982 - baseX) * 4 + 2) - localPlayer.x / 32,
							((3643 - baseY) * 4 + 2) - localPlayer.y / 32);
						break;

					case 2:
						markMinimap(minimapIcons[0], ((2827 - baseX) * 4 + 2) - localPlayer.x / 32,
							((3810 - baseY) * 4 + 2) - localPlayer.y / 32);
						break;
				}
			}

			for (int k5 = 0; k5 < 104; k5++) {
				for (int l5 = 0; l5 < 104; l5++) {
					NodeDeque class19 = groundItems[plane][k5][l5];
					if (class19 != null) {
						int l = (k5 * 4 + 2) - localPlayer.x / 32;
						int j3 = (l5 * 4 + 2) - localPlayer.y / 32;
						markMinimap(mapDotItem, l, j3);
					}
				}
			}

			for (int i6 = 0; i6 < npcCount; i6++) {
				Npc npc = npcs[npcIndices[i6]];
				if (npc != null && npc.isVisible()) {
					NpcDefinition entityDef = npc.desc;
					if (entityDef.transforms != null)
						entityDef = entityDef.morph();
					if (entityDef != null && entityDef.isMinimapVisible && entityDef.isInteractable) {
						int i1 = npc.x / 32 - localPlayer.x / 32;
						int k3 = npc.y / 32 - localPlayer.y / 32;
						markMinimap(mapDotNPC, i1, k3);
					}
				}
			}

			for (int j6 = 0; j6 < playerCount; j6++) {
				Player player = players[playerIndices[j6]];
				if (player != null && player.isVisible()) {
					int j1 = player.x / 32 - localPlayer.x / 32;
					int l3 = player.y / 32 - localPlayer.y / 32;
					boolean flag1 = false;
					boolean flag3 = false;
					String clanname;
					for (int j3 = 0; j3 < clanList.length; j3++) {
						if (clanList[j3] == null)
							continue;
						clanname = clanList[j3];
						if (clanname.startsWith("<clan"))
							clanname = clanname.substring(clanname.indexOf(">") + 1, clanname.length());
						if (!clanname.equalsIgnoreCase(player.displayName))
							continue;
						flag3 = true;
						break;
					}
					long l6 = StringUtils.longForName(player.displayName);
					for (int k6 = 0; k6 < friendsCount; k6++) {
						if (l6 != friendsListAsLongs[k6] || friendsNodeIDs[k6] == 0)
							continue;
						flag1 = true;
						break;
					}
					boolean flag2 = false;
					if (localPlayer.team != 0 && player.team != 0 && localPlayer.team == player.team)
						flag2 = true;
					if (flag1)
						markMinimap(mapDotFriend, j1, l3);
					else if (flag3)
						markMinimap(mapDotClan, j1, l3);
					else if (flag2)
						markMinimap(mapDotTeam, j1, l3);
					else
						markMinimap(mapDotPlayer, j1, l3);
				}
			}

			if (hintArrowType != 0 && loopCycle % 20 < 10) {
				if (hintArrowType == 1 && hintArrowNpcIndex >= 0 && hintArrowNpcIndex < npcs.length) {
					Npc npc = npcs[hintArrowNpcIndex];
					if (npc != null) {
						int k1 = npc.x / 32 - localPlayer.x / 32;
						int i4 = npc.y / 32 - localPlayer.y / 32;
						refreshMinimap(mapMarker, i4, k1);
					}
				}
				if (hintArrowType == 2) {
					int l1 = ((hintIconX - baseX) * 4 + 2) - localPlayer.x / 32;
					int j4 = ((hintIconYpos - baseY) * 4 + 2) - localPlayer.y / 32;
					refreshMinimap(mapMarker, j4, l1);
				}
				if (hintArrowType == 10 && hintArrowPlayerIndex >= 0 && hintArrowPlayerIndex < players.length) {
					Player player = players[hintArrowPlayerIndex];
					if (player != null) {
						int i2 = player.x / 32 - localPlayer.x / 32;
						int k4 = player.y / 32 - localPlayer.y / 32;
						refreshMinimap(mapMarker, k4, i2);
					}
				}
			}

			if (destX != 0) {
				int j2 = (destX * 4 + 2) - localPlayer.x / 32;
				int l4 = (destY * 4 + 2) - localPlayer.y / 32;
				markMinimap(mapFlag, j2, l4); //764 1068
			}


		}

		Sprite wiki1 = new Sprite("/Interfaces/Wiki/2420");
		Sprite wiki2 = new Sprite("/Interfaces/Wiki/2421");

		Sprite teleOrb1 = new Sprite("/Interfaces/teleorb/1435");
		Sprite teleOrb2 = new Sprite("/Interfaces/teleorb/1436");
		Rasterizer2D.drawBox((!isResized() ? xOffset + 127 : canvasWidth - 88), (!isResized() ? 83 : 80), 3, 3,
				0xffffff);
		if (!isResized()) {

			mapArea[0].drawSprite(xOffset, 4);
			mapArea[5].drawSprite(xOffset, 0);
		} else
			mapArea[2].drawSprite(canvasWidth - 183, 0);

		compassImage.rotate(33, camAngleY, anIntArray1057, 256, anIntArray968,
			(!isResized() ? 25 : 25), 4,
			(!isResized() ? 29 + xOffset : canvasWidth - 178), 33, 25);
		if (drawOrbs)
			loadAllOrbs(!isResized() ? 0 + xOffset : canvasWidth - 217);
		if (!isResized()) {
			cacheSprite2[6].drawSprite(xOffset + 198 - 2, 17 + 89);//110
			if (worldHover) {
				cacheSprite2[1].drawSprite(xOffset + 202 - 2, 20 + 90);//111
			} else {
				cacheSprite2[0].drawSprite(xOffset + 202 - 2, 20 + 90);
			}
			//if (wikiHover) {
			//	wiki2.drawSprite(185, 153);

			//} else {
			//	wiki1.drawSprite(185, 153);

			//}

			if (teleOrbHover) {
				teleOrb2.drawSprite(xOffset + 192, 25);

			} else {
				teleOrb1.drawSprite(xOffset + 192, 25);

			}
		} else {


			cacheSprite2[6].drawSprite(canvasWidth - 35, 141);
			if (worldHover) {
				cacheSprite2[1].drawSprite(canvasWidth - 31, 145);
			} else {
				cacheSprite2[0].drawSprite(canvasWidth - 31, 145);
			}

			//	if (wikiHover) {
			//	wiki2.drawSprite(currentGameWidth - 40, 165);
			//	} else {
			//		wiki1.drawSprite(currentGameWidth - 40, 165);
			//	}

			if (teleOrbHover) {
				teleOrb2.drawSprite(canvasWidth - 100, 150);

			} else {
				teleOrb1.drawSprite(canvasWidth - 100, 150);

			}

		}

	}

	public static AbstractRasterProvider rasterProvider;

	private boolean hpHover = false;

	private void loadHpOrb(int xOffset) {
		int yOff = Configuration.osbuddyGameframe ? !isResized() ? 0 : -5
				: !isResized() ? 0 : -5;
		int xOff = Configuration.osbuddyGameframe ? !isResized() ? 0 : -6
				: !isResized() ? 0 : -6;
		String cHP = RSInterface.interfaceCache[4016].message;
		String mHP = RSInterface.interfaceCache[4017].message;
		int currentHP = Integer.parseInt(cHP);
		int maxHP = Integer.parseInt(mHP);
		int health = (int) (((double) currentHP / (double) maxHP) * 100D);
		int poisonType = variousSettings[Configs.POISON_CONFIG];
		int hover = poisonType == 0 ? 173 : 173;
		Sprite bg = cacheSprite3[hpHover ? hover : 172];
		Sprite fg = null;
		if (poisonType == 0) {
			fg = cacheSprite3[161];
		}
		if (poisonType == 1) {
			fg = cacheSprite3[162];
		}
		if (poisonType == 2) {
			fg = venomOrb;
		}
		bg.drawSprite(0 + xOffset - xOff, 41 - yOff);
		fg.drawSprite(27 + xOffset - xOff, 45 - yOff);
		if (getOrbFill(health) <= 26) {
			cacheSprite3[160].myHeight = getOrbFill(health);
		} else {
			cacheSprite3[160].myHeight = 26;
		}
		cacheSprite3[160].drawSprite(27 + xOffset - xOff, 45 - yOff);
		cacheSprite3[168].drawSprite(27 + xOffset - xOff, 45 - yOff);
		if (health > 1_000_000_000) {
			infinity.drawSprite(10 + xOffset - xOff, 59 - yOff);
		} else {
			smallText.method382(getOrbTextColor(health), 15 + xOffset - xOff, "" + cHP, 67 - yOff, true);
		}
	}

	public double fillHP;

	public Sprite worldMap1, worldMap2, worldMap3;

	private boolean specialHover;
	public int specialEnabled;
	public int specialAttack=100;

	private void drawSpecialOrb(int xOffset) {
		Sprite image = cacheSprite1[specialHover ? 8 : 7];
		Sprite fill = cacheSprite[specialEnabled == 0 ? 9 : 6];
		Sprite sword = cacheSprite[12];
		double percent = specialAttack  / (double) 100;
		//int percent = 100;
		boolean isFixed = !isResized();
		image.drawSprite((isFixed ? 37 : 37) + xOffset, isFixed ? 134 : 139);
		if(specialEnabled==1) {
			//fill.drawSprite((isFixed ? 60 : 133) + xOffset, isFixed ? 134 : 151);
			fill.drawSprite((isFixed ? 63 : 63) + xOffset, isFixed ? 137 : 141);
		}else{
			fill.drawSprite((isFixed ? 64 : 64) + xOffset, isFixed ? 138 : 143);
			//fill.drawSprite((isFixed ? 65 : 133) + xOffset, isFixed ? 139 : 151);
		}
		sword.drawSprite((isFixed ? 64 : 64) + xOffset, isFixed ? 138 : 143);

		smallText.method382(getOrbTextColor((int) (percent * 100)),
				(isFixed ? 53 : 53) + xOffset, specialAttack + "", isFixed ? 159 : 164,
				true);
	}

	private void loadPrayerOrb(int xOffset) {
		int yOff = Configuration.osbuddyGameframe ? !isResized() ? 10 : 2
				: !isResized() ? 0 : -5;
		int xOff = Configuration.osbuddyGameframe ? !isResized() ? -1 : -7
				: !isResized() ? -1 : -7;
		Sprite bg = cacheSprite1[prayHover ? 8 : 7];
		Sprite fg = prayClicked ? new Sprite("Gameframe/newprayclicked") : cacheSprite1[1];
		bg.drawSprite(0 + xOffset - xOff, 75 - yOff);
		fg.drawSprite(27 + xOffset - xOff, 79 - yOff);
		int level = Integer.parseInt(RSInterface.interfaceCache[4012].message.replaceAll("%", ""));
		int max = maximumLevels[5];
		double percent = level / (double) max;
		cacheSprite1[14].myHeight = (int) (26 * (1 - percent));
		cacheSprite1[14].drawSprite(27 + xOffset - xOff, 79 - yOff);
		if (percent <= .25) {
			cacheSprite1[10].drawSprite(30 + xOffset - xOff, 82 - yOff);
		} else {
			cacheSprite1[10].drawSprite(30 + xOffset - xOff, 82 - yOff);
		}
		if (level > 1_000_000_000) {
			infinity.drawSprite(11 + xOffset - xOff, 94 - yOff);
		} else {
			smallText.method382(getOrbTextColor((int) (percent * 100)), 14 + xOffset - xOff, level + "", 101 - yOff, true);
		}


	}

	private void loadRunOrb(int xOffset) {
		int current = Integer.parseInt(RSInterface.interfaceCache[22539].message.replaceAll("%", ""));
		int yOff = Configuration.osbuddyGameframe ? !isResized() ? 15 : 5
				: !isResized() ? 1 : -4;
		int xMinus = Configuration.osbuddyGameframe ? !isResized() ? 11 : 5
				: !isResized() ? -1 : -6;
		Sprite bg = cacheSprite1[runHover ? 8 : 7];
		boolean running = anIntArray1045[173] == 1;
		Sprite fg = cacheSprite1[running ? 4 : 3];
		bg.drawSprite(10 + xOffset - xMinus, 109 - yOff);
		fg.drawSprite(37 + xOffset - xMinus, 113 - yOff);
		int level = current;
		double percent = level / (double) 100;
		cacheSprite1[14].myHeight = (int) (26 * (1 - percent));
		cacheSprite1[14].drawSprite(37 + xOffset - xMinus, 113 - yOff);
		if (percent <= .25) {
			cacheSprite1[running ? 12 : 11].drawSprite(43 + xOffset - xMinus, 117 - yOff);
		} else {
			cacheSprite1[running ? 12 : 11].drawSprite(43 + xOffset - xMinus, 117 - yOff);
		}
		smallText.method382(getOrbTextColor((int) (percent * 100)), 25 + xOffset - xMinus, level + "", 135 - yOff,
				true);
	}

	private Sprite venomOrb;

	private boolean pouchHover;

	Sprite moneyPouch;
	Sprite moneyPouch1;
	Sprite moneyPouchCoins;

	private boolean wikiHover;
	private boolean teleOrbHover;

	private int getMoneyOrbColor(long amount) {
		if (amount >= 0 && amount <= 99999) {
			return 0xFFFF00;
		} else if (amount >= 100000 && amount <= 9999999) {
			return 0xFFFFFF;
		} else {
			return 0x00FF80;
		}
	}


	private void drawMoneyPouch(int xOffset) {

		if (pouchHover) {
			Rasterizer2D.fillCircle((!Client.instance.isResized() ? xOffset + 179 : canvasWidth - 49), (!Client.instance.isResized() ? 142 : 168), 15, 0x6E6D6D);
			moneyPouch.drawSprite(((!Client.instance.isResized() ? xOffset + 162 : canvasWidth - 65)), (!Client.instance.isResized() ? 127 : 153));
			moneyPouchCoins.drawSprite((!Client.instance.isResized() ? xOffset + 170 : canvasWidth - 57), (!Client.instance.isResized() ? 134 : 160));
			String amount = RSInterface.interfaceCache[8135].message;
			long getAmount = Long.parseLong(amount);
			smallText.method382(getMoneyOrbColor(getAmount), (!Client.instance.isResized() ? xOffset +  205 : canvasWidth - 22), formatCoins(getAmount) + "", (!Client.instance.isResized() ? 153 : 178), true);
		} else {

			Rasterizer2D.fillCircle((!Client.instance.isResized() ? xOffset + 179 : canvasWidth - 49), (!Client.instance.isResized() ? 142 : 168), 15, 0x6E6D6D);
			moneyPouch1.drawSprite(((!Client.instance.isResized() ? xOffset + 162 : canvasWidth - 65)), (!Client.instance.isResized() ? 127 : 153));
			moneyPouchCoins.drawSprite((!Client.instance.isResized() ? xOffset + 170 : canvasWidth - 57), (!Client.instance.isResized() ? 134 : 160));
			String amount = RSInterface.interfaceCache[8135].message;
			long getAmount = Long.parseLong(amount);
			smallText.method382(getMoneyOrbColor(getAmount), (!Client.instance.isResized() ? xOffset + 205 : canvasWidth - 22), formatCoins(getAmount) + "", (!Client.instance.isResized() ? 153 : 178), true);
		}
	}

	private void loadAllOrbs(int xOffset) {
		try {
			loadHpOrb(xOffset);
			loadPrayerOrb(xOffset);
			loadRunOrb(xOffset);
			drawSpecialOrb(xOffset);
			//drawMoneyPouch(xOffset);
		} catch (Exception e) {}

		if (drawExperienceCounter) {
			if (counterHover) {
				cacheSprite2[5].drawSprite(
					drawOrbs && !isResized() ? 0 + xOffset : canvasWidth - 211,
					!isResized() ? 21 : 25);
			} else {
				cacheSprite2[3].drawSprite(
					drawOrbs && !isResized() ? 0 + xOffset : canvasWidth - 211,
					!isResized() ? 21 : 25);
			}
		} else {
			if (counterHover) {
				cacheSprite2[4].drawSprite(
					drawOrbs && !isResized() ? 0 + xOffset : canvasWidth - 211,
					!isResized() ? 21 : 25);
			} else {
				cacheSprite2[2].drawSprite(
					drawOrbs && !isResized() ? 0 + xOffset : canvasWidth - 211,
					!isResized() ? 21 : 25);
			}
		}
		if (!isResized()) {
			// cacheSprite[worldHover ? 5 : 4].drawSprite(202, 20);
		} else {
			// cacheSprite[worldHover ? 3 : 2].drawSprite(Client.canvasWidth - 118,
			// 154);
		}
		if (Configuration.osbuddyGameframe) {
			//loadSpecialOrb(xOffset);
		}

	}

	public boolean runClicked = false;

	public int getOrbTextColor(int i) {
		if (i >= 75 && i <= 0x7fffffff)
			return 65280;
		if (i >= 50 && i <= 74)
			return 0xffff00;
		return i < 25 || i > 49 ? 0xff0000 : 0xFFB400;
	}

	private Sprite mapIcon9, mapIcon7, mapIcon8, mapIcon6, mapIcon5;

	public int getOrbFill(int statusInt) {
		if (statusInt <= Integer.MAX_VALUE && statusInt >= 97)
			return 0;
		else if (statusInt <= 96 && statusInt >= 93)
			return 1;
		else if (statusInt <= 92 && statusInt >= 89)
			return 2;
		else if (statusInt <= 88 && statusInt >= 85)
			return 3;
		else if (statusInt <= 84 && statusInt >= 81)
			return 4;
		else if (statusInt <= 80 && statusInt >= 77)
			return 5;
		else if (statusInt <= 76 && statusInt >= 73)
			return 6;
		else if (statusInt <= 72 && statusInt >= 69)
			return 7;
		else if (statusInt <= 68 && statusInt >= 65)
			return 8;
		else if (statusInt <= 64 && statusInt >= 61)
			return 9;
		else if (statusInt <= 60 && statusInt >= 57)
			return 10;
		else if (statusInt <= 56 && statusInt >= 53)
			return 11;
		else if (statusInt <= 52 && statusInt >= 49)
			return 12;
		else if (statusInt <= 48 && statusInt >= 45)
			return 13;
		else if (statusInt <= 44 && statusInt >= 41)
			return 14;
		else if (statusInt <= 40 && statusInt >= 37)
			return 15;
		else if (statusInt <= 36 && statusInt >= 33)
			return 16;
		else if (statusInt <= 32 && statusInt >= 29)
			return 17;
		else if (statusInt <= 28 && statusInt >= 25)
			return 18;
		else if (statusInt <= 24 && statusInt >= 21)
			return 19;
		else if (statusInt <= 20 && statusInt >= 17)
			return 20;
		else if (statusInt <= 16 && statusInt >= 13)
			return 21;
		else if (statusInt <= 12 && statusInt >= 9)
			return 22;
		else if (statusInt <= 8 && statusInt >= 7)
			return 23;
		else if (statusInt <= 6 && statusInt >= 5)
			return 24;
		else if (statusInt <= 4 && statusInt >= 3)
			return 25;
		else if (statusInt <= 2 && statusInt >= 1)
			return 26;
		else if (statusInt <= 0)
			return 27;
		return 0;
	}

	public Sprite xpSprite;
	public Sprite xpbg1;
	public Sprite xpbg2;

	private Queue<ExperienceDrop> experienceDrops = new LinkedList<>();

	private void processExperienceCounter() {
		if (loopCycle % 1 <= 1 && !experienceDrops.isEmpty()) {
			Collection<ExperienceDrop> remove = new ArrayList<>();
			for (ExperienceDrop drop : experienceDrops) {
				drop.pulse();
				if (drop.getYPosition() == -1) {
					experienceCounter += drop.getAmount();
					remove.add(drop);
				}
			}
			experienceDrops.removeAll(remove);
		}

		if (!drawExperienceCounter || openInterfaceID > -1) {
			return;
		}

		for (ExperienceDrop drop : experienceDrops) {
			String text = drop.toString();
			int x = (!isResized() ? 507 : canvasWidth - 246)
					- newSmallFont.getTextWidth(text);
			int y = drop.getYPosition() - 15;
			int transparency = 256;
			newSmallFont.drawString(text, x, y, 0xFFFFFF, 0x000000, 256);

			for (int skill : drop.getSkills()) {
				if(skill==22){
					continue;
				}

				Sprite sprite = smallXpSprites[skill];
				x -= sprite.myWidth + 3;
				y -= sprite.myHeight - 4;
				sprite.drawAdvancedSprite(x, y, transparency);
				y += sprite.myHeight - 4;
			}
		}

		String experience = NumberFormat.getInstance().format(experienceCounter);

		xpbg1.drawAdvancedSprite(!isResized() ? 395 : canvasWidth - 365, 6);
		xpbg2.drawSprite(!isResized() ? 398 : canvasWidth - 363, 9);

		newSmallFont.drawBasicString(experience, (!isResized() ? 510 : canvasWidth - 252)
				- newSmallFont.getTextWidth(experience), 24, 0xFFFFFF, 0x000000);
	}

	private boolean drawExperienceCounter = true;

	public void npcScreenPos(Entity entity, int i) {
		calcEntityScreenPos(entity.x, i, entity.y);
	}

	public void calcEntityScreenPos(int x, int vertical_offset, int y) {
		if (x < 128 || y < 128 || x > 13056 || y > 13056) {
			viewportTempX = -1;
			viewportTempY = -1;
			return;
		}
		int z = getCenterHeight(plane, y, x) - vertical_offset;
		x -= cameraX;
		z -= cameraY;
		y -= cameraZ;

		int sin_y = Rasterizer3D.SINE[cameraPitch];
		int cos_y = Rasterizer3D.COSINE[cameraPitch];

		int sin_x = Rasterizer3D.SINE[cameraYaw];
		int cos_x = Rasterizer3D.COSINE[cameraYaw];

		int a_x = y * sin_x + x * cos_x >> 16;
		int b_x = y * cos_x - x * sin_x >> 16;

		int a_y = z * cos_y - b_x * sin_y >> 16;
		int b_y = z * sin_y + b_x * cos_y >> 16;

		if (b_y >= 50) {
			viewportTempX = a_x * Client.viewportZoom / b_y + (Client.viewportWidth / 2);
			viewportTempY = a_y * Client.viewportZoom / b_y + (Client.viewportHeight / 2);
		} else {
			viewportTempX = -1;
			viewportTempY = -1;
		}
	}

	public void requestSpawnObject(int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2) {
		SpawnedObject spawnedObject = null;
		for (spawnedObject = (SpawnedObject) spawns
				.reverseGetFirst(); spawnedObject != null; spawnedObject = (SpawnedObject) spawns
				.reverseGetNext()) {
			if (spawnedObject.plane != l1 || spawnedObject.x != i2 || spawnedObject.y != j1
					|| spawnedObject.group != i1)
				continue;
			spawnedObject = spawnedObject;
			break;
		}

		if (spawnedObject == null) {
			spawnedObject = new SpawnedObject();
			spawnedObject.plane = l1;
			spawnedObject.group = i1;
			spawnedObject.x = i2;
			spawnedObject.y = j1;
			handleTemporaryObjects(spawnedObject);
			spawns.insertHead(spawnedObject);
		}
		spawnedObject.id = k;
		spawnedObject.type = k1;
		spawnedObject.orientation = l;
		spawnedObject.delay = j2;
		spawnedObject.getLongetivity = j;
	}

	private boolean interfaceIsSelected(RSInterface class9) {
		if (class9.anIntArray245 == null)
			return false;
		for (int i = 0; i < class9.anIntArray245.length; i++) {
			int j = extractInterfaceValues(class9, i);
			int k = class9.anIntArray212[i];
			if (class9.anIntArray245[i] == 2) {
				if (j >= k)
					return false;
			} else if (class9.anIntArray245[i] == 3) {
				if (j <= k)
					return false;
			} else if (class9.anIntArray245[i] == 4) {
				if (j == k)
					return false;
			} else if (j != k)
				return false;
		}

		return true;
	}

	public void doFlamesDrawing() {



	}

	public void method134(Buffer stream) {
		int j = stream.readBits(8);
		if (j < playerCount) {
			for (int k = j; k < playerCount; k++)
				anIntArray840[anInt839++] = playerIndices[k];

		}
		if (j > playerCount) {
			Signlink.reporterror(myUsername + " Too many players");
			throw new RuntimeException("eek");
		}
		playerCount = 0;
		for (int l = 0; l < j; l++) {
			int i1 = playerIndices[l];
			Player player = players[i1];
			player.index = i1;
			int j1 = stream.readBits(1);
			if (j1 == 0) {
				playerIndices[playerCount++] = i1;
				player.lastUpdateTick = loopCycle;
			} else {
				int k1 = stream.readBits(2);
				if (k1 == 0) {
					playerIndices[playerCount++] = i1;
					player.lastUpdateTick = loopCycle;
					anIntArray894[anInt893++] = i1;
				} else if (k1 == 1) {
					playerIndices[playerCount++] = i1;
					player.lastUpdateTick = loopCycle;
					int l1 = stream.readBits(3);
					player.moveInDir(false, l1);
					int j2 = stream.readBits(1);
					if (j2 == 1)
						anIntArray894[anInt893++] = i1;
				} else if (k1 == 2) {
					playerIndices[playerCount++] = i1;
					player.lastUpdateTick = loopCycle;
					int i2 = stream.readBits(3);
					player.moveInDir(true, i2);
					int k2 = stream.readBits(3);
					player.moveInDir(true, k2);
					int l2 = stream.readBits(1);
					if (l2 == 1)
						anIntArray894[anInt893++] = i1;
				} else if (k1 == 3)
					anIntArray840[anInt839++] = i1;
			}
		}
	}

	public Sprite loginAsset0;
	public Sprite loginAsset1;
	public Sprite loginAsset2;
	public Sprite loginAsset3;
	public Sprite loginAsset4;
	public String firstLoginMessage = "";


	public boolean rememberMeHover;
	long lastLogin = 0;
	long loadDelay = 0;
	private Sprite loginScreenBackground;
	private Sprite loginScreenBackgroundCaptcha;
	private Sprite captchaExit;
	private Sprite captchaExitHover;
	private Sprite logo2021;

	private Sprite captcha;

	public void drawLoginScreen() {
		loginScreen.draw();
	}

	private void drawFlames() {
		try {
			long l = System.currentTimeMillis();
			int i = 0;
			int j = 20;
			while (aBoolean831) {
				anInt1208++;
				if (++i > 10) {
					long l1 = System.currentTimeMillis();
					int k = (int) (l1 - l) / 10 - j;
					j = 40 - k;
					if (j < 5)
						j = 5;
					i = 0;
					l = l1;
				}
				try {
					Thread.sleep(j);
				} catch (Exception _ex) {
				}
			}
		} catch (Exception _ex) {
			drawingFlames = false;
		}
	}

	public void method137(Buffer stream, int j) {
		if (j == 84) {
			int k = stream.readUnsignedByte();
			int j3 = localX + (k >> 4 & 7);
			int i6 = localY + (k & 7);
			int l8 = stream.readUShort();
			int k11 = stream.readUShort();
			int l13 = stream.readUShort();// edit
			if (j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104) {
				NodeDeque class19_1 = groundItems[plane][j3][i6];
				if (class19_1 != null) {
					for (TileItem class30_sub2_sub4_sub2_3 = (TileItem) class19_1
							.reverseGetFirst(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (TileItem) class19_1
							.reverseGetNext()) {
						if (class30_sub2_sub4_sub2_3.id != (l8 & 0x7fff) || class30_sub2_sub4_sub2_3.quantity != k11)
							continue;
						class30_sub2_sub4_sub2_3.quantity = l13;
						class30_sub2_sub4_sub2_3.quantityChanged(l13);
						break;
					}

					this.updateItemPile(j3, i6);
				}
			}
			return;
		}
		if (j == 105) {
			int l = stream.readUnsignedByte();
			int k3 = localX + (l >> 4 & 7);
			int j6 = localY + (l & 7);
			int i9 = stream.readUShort();
			int l11 = stream.readUnsignedByte();
			int i14 = l11 >> 4 & 0xf;
			int i16 = l11 & 7;
			if (localPlayer.pathX[0] >= k3 - i14 && localPlayer.pathX[0] <= k3 + i14 && localPlayer.pathY[0] >= j6 - i14
					&& localPlayer.pathY[0] <= j6 + i14 && soundEffectVolume != 0 && aBoolean848 && !Client.lowMem
					&& soundCount < 50) {
				sound[soundCount] = i9;
				soundType[soundCount] = i16;
				soundDelay[soundCount] = Sounds.anIntArray326[i9];
				// aClass26Array1468[soundCount] = null;
				soundCount++;
			}
		}
		if (j == 215) {
			int i1 = stream.readUShortA();
			int l3 = stream.method428();
			int k6 = localX + (l3 >> 4 & 7);
			int j9 = localY + (l3 & 7);
			int i12 = stream.readUShortA();
			int j14 = stream.readUShort();
			if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != localPlayerIndex) {
				TileItem class30_sub2_sub4_sub2_2 = new TileItem();
				class30_sub2_sub4_sub2_2.id = i1;
				class30_sub2_sub4_sub2_2.quantity = j14;
				class30_sub2_sub4_sub2_2.quantityChanged(j14);

				if (groundItems[plane][k6][j9] == null)
					groundItems[plane][k6][j9] = new NodeDeque();
				groundItems[plane][k6][j9].insertHead(class30_sub2_sub4_sub2_2);
				this.updateItemPile(k6, j9);
			}
			return;
		}
		if (j == 156) {
			int j1 = stream.method426();
			int i4 = localX + (j1 >> 4 & 7);
			int l6 = localY + (j1 & 7);
			int k9 = stream.readUShort();
			if (i4 >= 0 && l6 >= 0 && i4 < 104 && l6 < 104) {
				NodeDeque class19 = groundItems[plane][i4][l6];
				if (class19 != null) {
					for (TileItem item = (TileItem) class19.reverseGetFirst(); item != null; item = (TileItem) class19.reverseGetNext()) {
						if (item.id != k9)
							continue;
						item.unlink();
						ItemDespawned itemDespawned = new ItemDespawned(scene.tileArray[plane][i4][l6], item);
						Client.instance.getCallbacks().post(itemDespawned);
						break;
					}

					if (class19.reverseGetFirst() == null) {
						groundItems[plane][i4][l6] = null;
					}
					this.updateItemPile(i4, l6);
				}
			}
			return;
		}
		if (j == 160) {
			int offset = stream.method428();
			int xLoc = localX + (offset >> 4 & 7);
			int yLoc = localY + (offset & 7);
			int objectTypeFace = stream.method428();
			int objectType = objectTypeFace >> 2;
			int objectFace = objectTypeFace & 3;
			int objectGenre = objectTypes[objectType];
			int animId = stream.readUShortA();
			if (xLoc >= 0 && yLoc >= 0 && xLoc < 103 && yLoc < 103) {
				if (objectGenre == 0) {// WallObject
					Wall wallObjectObject = scene.get_wall(plane, xLoc, yLoc);
					if (wallObjectObject != null) {
						int objectId = ObjectKeyUtil.getObjectId(wallObjectObject.uid);
						if (objectType == 2) {
							wallObjectObject.wall = new SceneObject(objectId, 4 + objectFace, 2, plane, xLoc, yLoc, animId, false, wallObjectObject.wall);
							wallObjectObject.corner = new SceneObject(objectId, objectFace + 1 & 3, 2, plane, xLoc, yLoc, animId, false, wallObjectObject.corner);
						} else {
							wallObjectObject.wall = new SceneObject(objectId, objectFace, objectType, plane, xLoc, yLoc, animId, false, wallObjectObject.wall);
						}
					}
				}
				if (objectGenre == 1) { // WallDecoration
					WallDecoration wallDecoration = scene.get_wall_decor(xLoc, yLoc, plane);
					if (wallDecoration != null)
						wallDecoration.node = new SceneObject(ObjectKeyUtil.getObjectId(wallDecoration.uid), 0, 4, plane, xLoc, yLoc, animId, false, wallDecoration.node);
				}
				if (objectGenre == 2) { // TiledObject
					InteractiveObject tiledObject = scene.get_interactive_object(xLoc, yLoc, plane);
					if (objectType == 11)
						objectType = 10;
					if (tiledObject != null)
						tiledObject.renderable = new SceneObject(ObjectKeyUtil.getObjectId(tiledObject.uid), objectFace, objectType, plane, xLoc, yLoc, animId, false, tiledObject.renderable);
				}
				if (objectGenre == 3) { // GroundDecoration
					GroundDecoration groundDecoration = scene.get_ground_decor(yLoc, xLoc, plane);
					if (groundDecoration != null)
						groundDecoration.node = new SceneObject(ObjectKeyUtil.getObjectId(groundDecoration.uid), objectFace, 22, plane, xLoc, yLoc, animId, false, groundDecoration.node);
				}
			}
			return;
		}
		if (j == 147) {
			int offset = stream.method428();
			int xLoc = localX + (offset >> 4 & 7);
			int yLoc = localY + (offset & 7);
			int playerIndex = stream.readUShort();
			byte byte0GreaterXLoc = stream.method430();
			int startDelay = stream.method434();
			byte byte1GreaterYLoc = stream.method429();
			int stopDelay = stream.readUShort();
			int objectTypeFace = stream.method428();
			int objectType = objectTypeFace >> 2;
			int objectFace = objectTypeFace & 3;
			int objectGenre = objectTypes[objectType];
			byte byte2LesserXLoc = stream.readSignedByte();
			int objectId = stream.readUShort();
			byte byte3LesserYLoc = stream.method429();
			Player player;
			if (playerIndex == localPlayerIndex)
				player = localPlayer;
			else
				player = players[playerIndex];
			if (player != null) {
				ObjectDefinition objectDefinition = ObjectDefinition.lookup(objectId);
				int sizeX;
				int sizeY;
				if (objectFace != 1 && objectFace != 3) {
					sizeX = objectDefinition.sizeX;
					sizeY = objectDefinition.sizeY;
				} else {
					sizeX = objectDefinition.sizeY;
					sizeY = objectDefinition.sizeX;
				}

				int left = xLoc + (sizeX >> 1);
				int right = xLoc + (sizeX + 1 >> 1);
				int top = yLoc + (sizeY >> 1);
				int bottom = yLoc + (sizeY + 1 >> 1);
				int[][] heights = tileHeights[plane];
				int mean = heights[left][bottom] + heights[right][top] + heights[left][top] + heights[right][bottom] >> 2;
				int middleX = (xLoc << 7) + (sizeX << 6);
				int middleY = (yLoc << 7) + (sizeY << 6);

				Model model = objectDefinition.getModel(objectType, objectFace, heights, middleX, mean, middleY);
				if (model != null) {
					requestSpawnObject(stopDelay + 1, -1, 0, objectGenre, yLoc, 0, plane, xLoc, startDelay + 1);
					player.animationCycleStart = startDelay + loopCycle;
					player.animationCycleEnd = stopDelay + loopCycle;
					player.playerModel = model;
					int playerSizeX = objectDefinition.sizeX;
					int playerSizeY = objectDefinition.sizeY;
					if (objectFace == 1 || objectFace == 3) {
						playerSizeX = objectDefinition.sizeY;
						playerSizeY = objectDefinition.sizeX;
					}
					player.field1117 = xLoc * 128 + playerSizeX * 64;
					player.field1123 = yLoc * 128 + playerSizeY * 64;
					player.tileHeight2 = getCenterHeight(plane, player.field1123, player.field1117);
					if (byte2LesserXLoc > byte0GreaterXLoc) {
						byte tmp = byte2LesserXLoc;
						byte2LesserXLoc = byte0GreaterXLoc;
						byte0GreaterXLoc = tmp;
					}
					if (byte3LesserYLoc > byte1GreaterYLoc) {
						byte tmp = byte3LesserYLoc;
						byte3LesserYLoc = byte1GreaterYLoc;
						byte1GreaterYLoc = tmp;
					}
					player.minX = xLoc + byte2LesserXLoc;
					player.maxX = xLoc + byte0GreaterXLoc;
					player.minY = yLoc + byte3LesserYLoc;
					player.maxY = yLoc + byte1GreaterYLoc;
				}
			}
		}
		if (j == 151) {
			int i2 = stream.method426();
			int l4 = localX + (i2 >> 4 & 7);
			int k7 = localY + (i2 & 7);
			int j10 = stream.method434();
			int k12 = stream.method428();
			int i15 = k12 >> 2;
			int k16 = k12 & 3;
			int l17 = objectTypes[i15];
			if (l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104)
				requestSpawnObject(-1, j10, k16, l17, k7, i15, plane, l4, 0);
			return;
		}
		if (j == 4) {
			int j2 = stream.readUnsignedByte();
			int i5 = localX + (j2 >> 4 & 7);
			int l7 = localY + (j2 & 7);
			int k10 = stream.readUShort();
			int l12 = stream.readUnsignedByte();
			int j15 = stream.readUShort();
			if (i5 >= 0 && l7 >= 0 && i5 < 104 && l7 < 104) {
				i5 = i5 * 128 + 64;
				l7 = l7 * 128 + 64;
				SpotAnimEntity class30_sub2_sub4_sub3 = new SpotAnimEntity(plane, loopCycle, j15, k10,
						getCenterHeight(plane, l7, i5) - l12, l7, i5);
				incompleteAnimables.insertHead(class30_sub2_sub4_sub3);
			}
			return;
		}
		if (j == 44) {
			int k2 = stream.method436();
			int j5 = stream.method439();
			int i8 = stream.readUnsignedByte();
			int l10 = localX + (i8 >> 4 & 7);
			int i13 = localY + (i8 & 7);
			if (l10 >= 0 && i13 >= 0 && l10 < 104 && i13 < 104) {
				TileItem class30_sub2_sub4_sub2_1 = new TileItem();
				class30_sub2_sub4_sub2_1.id = k2;
				class30_sub2_sub4_sub2_1.quantity = j5;
				class30_sub2_sub4_sub2_1.quantityChanged(j5);
				if (groundItems[plane][l10][i13] == null)
					groundItems[plane][l10][i13] = new NodeDeque();
				groundItems[plane][l10][i13].insertHead(class30_sub2_sub4_sub2_1);
				this.updateItemPile(l10, i13);
			}
			return;
		}
		if (j == 101) {
			int l2 = stream.readNegUByte();
			int k5 = l2 >> 2;
			int j8 = l2 & 3;
			int i11 = objectTypes[k5];
			int j13 = stream.readUnsignedByte();
			int k15 = localX + (j13 >> 4 & 7);
			int l16 = localY + (j13 & 7);
			if (k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104)
				requestSpawnObject(-1, -1, j8, i11, l16, k5, plane, k15, 0);
			return;
		}
		if (j == 117) {
			int offset = stream.readUnsignedByte();
			int sourceX = localX + (offset >> 4 & 7);
			int sourceY = localY + (offset & 7);
			int destX = sourceX + stream.readSignedByte();
			int destY = sourceY + stream.readSignedByte();
			int target = stream.readShort3();
			int gfxMoving = stream.readUShort();
			int startHeight = stream.readUnsignedByte() * 4;
			int endHeight = stream.readUnsignedByte() * 4;
			int startDelay = stream.readUShort();
			int speed = stream.readUShort();
			int initialSlope = stream.readUnsignedByte();
			int frontOffset = stream.readUnsignedByte();
			System.out.println("PROJECTILE");
			if (sourceX >= 0 && sourceY >= 0 && sourceX < 104 && sourceY < 104 && destX >= 0 && destY >= 0 && destX < 104 && destY < 104
					&& gfxMoving != 65535) {
				sourceX = sourceX * 128 + 64;
				sourceY = sourceY * 128 + 64;
				destX = destX * 128 + 64;
				destY = destY * 128 + 64;
				Projectile projectile = new Projectile(initialSlope, endHeight, startDelay + loopCycle,
						speed + loopCycle, frontOffset, plane, getCenterHeight(plane, sourceY, sourceX) - startHeight, sourceY, sourceX,
						target, gfxMoving);
				projectile.setDestination(destX, destY, getCenterHeight(plane, destY, destX) - endHeight,
						startDelay + loopCycle);
				projectiles.insertBack(projectile);
			}
		}
	}

	public void method139(Buffer stream) {
		try {
			stream.initBitAccess();
			int localNpcListSize = stream.readBits(8);
			if (localNpcListSize < npcCount) {
				for (int l = localNpcListSize; l < npcCount; l++)
					anIntArray840[anInt839++] = npcIndices[l];

			}
			if (localNpcListSize > npcCount) {
				Signlink.reporterror(myUsername + " Too many npcs");
				throw new RuntimeException("eek");
			}
			npcCount = 0;
			for (int i1 = 0; i1 < localNpcListSize; i1++) {
				int npcIndex = npcIndices[i1];
				Npc npc = npcs[npcIndex];
				npc.index = npcIndex;
				int k1 = stream.readBits(1);
				if (k1 == 0) {
					npcIndices[npcCount++] = npcIndex;
					npc.lastUpdateTick = loopCycle;
				} else {
					int l1 = stream.readBits(2);
					if (l1 == 0) {
						npcIndices[npcCount++] = npcIndex;
						npc.lastUpdateTick = loopCycle;
						anIntArray894[anInt893++] = npcIndex;
					} else if (l1 == 1) {
						npcIndices[npcCount++] = npcIndex;
						npc.lastUpdateTick = loopCycle;
						int i2 = stream.readBits(3);
						npc.moveInDir(false, i2);
						int k2 = stream.readBits(1);
						if (k2 == 1)
							anIntArray894[anInt893++] = npcIndex;
					} else if (l1 == 2) {
						npcIndices[npcCount++] = npcIndex;
						npc.lastUpdateTick = loopCycle;
						int j2 = stream.readBits(3);
						npc.moveInDir(true, j2);
						int l2 = stream.readBits(3);
						npc.moveInDir(true, l2);
						int i3 = stream.readBits(1);
						if (i3 == 1)
							anIntArray894[anInt893++] = npcIndex;
					} else if (l1 == 3)
						anIntArray840[anInt839++] = npcIndex;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean clickInRegion(int x1, int y1, int x2, int y2) {
		if (MouseHandler.saveClickX >= x1 && MouseHandler.saveClickX <= x2 && MouseHandler.saveClickY >= y1 && MouseHandler.saveClickY <= y2) {
			return true;
		}
		return false;
	}

	public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
		if (MouseHandler.mouseX >= x1 && MouseHandler.mouseX <= x2 && MouseHandler.mouseY >= y1 && MouseHandler.mouseY <= y2) {
			return true;
		}
		return false;
	}

	public boolean rememberPasswordHover;

	@SuppressWarnings("static-access")
	private void processLoginScreenInput() {
		loginScreen.handleInput();
	}

	private String loginScreenInput(String current, int l1, boolean flag1, int maxWidth, Runnable tab, Runnable enter) {
		if (l1 == 8 && current.length() > 0)
			current = current.substring(0, current.length() - 1);
		if (l1 == 9) {
			if (tab != null)
				tab.run();
		} else if (l1 == 10 || l1 == 13) {
			if (enter != null)
				enter.run();
			return current;
		}
		if (flag1)
			current += (char) l1;
		if (current.length() > maxWidth)
			current = current.substring(0, maxWidth);
		return current;
	}

	private void markMinimap(Sprite sprite, int xPosition, int yPosition) {
		int k = camAngleY + camAngleY & 0x7ff;
		int boundry = xPosition * xPosition + yPosition * yPosition;
		if (boundry > 5750)
			return;
		int i1 = Model.SINE[k];
		int j1 = Model.COSINE[k];
		i1 = (i1 * 256) / (minimapZoom + 256);
		j1 = (j1 * 256) / (minimapZoom + 256);
		int k1 = yPosition * i1 + xPosition * j1 >> 16;
		int l1 = yPosition * j1 - xPosition * i1 >> 16;

		try {
			int xOffset = !isResized() ? 516 : 0;
			if (!isResized())
				sprite.drawSprite(((97 + k1) - sprite.maxWidth / 2) + 30 + xOffset, 83 - l1 - sprite.maxHeight / 2 - 4 + 5);
			else
				sprite.drawSprite(((77 + k1) - sprite.maxWidth / 2) + 4 + (canvasWidth - 167) + xOffset,
						85 - l1 - sprite.maxHeight / 2 - 4);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public void updatePlayers(int i, Buffer stream) {
		anInt839 = 0;
		anInt893 = 0;

		method117(stream);
		method134(stream);
		method91(stream, i);
		method49(stream);


		for (int k = 0; k < anInt839; k++) {
			int l = anIntArray840[k];
			if (players[l].lastUpdateTick != loopCycle)
				players[l] = null;
		}

		if (stream.currentPosition != i) {
			Signlink.reporterror("Error packet size mismatch in getplayer pos:" + stream.currentPosition + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for (int i1 = 0; i1 < playerCount; i1++)
			if (players[playerIndices[i1]] == null) {
				Signlink.reporterror(myUsername + " null entry in pl list - pos:" + i1 + " size:" + playerCount);
				throw new RuntimeException("eek");
			}

	}

	private void setCameraPos(int j, int k, int l, int i1, int j1, int k1) {
		int l1 = 2048 - k & 0x7ff;
		int i2 = 2048 - j1 & 0x7ff;
		int j2 = 0;
		int k2 = 0;
		int l2 = j;
		if (l1 != 0) {
			int i3 = Model.SINE[l1];
			int k3 = Model.COSINE[l1];
			int i4 = k2 * k3 - (int) (l2 * i3) >> 16;
			l2 = k2 * i3 + (int) (l2 * k3) >> 16;
			k2 = i4;
		}
		if (i2 != 0) {
			int j3 = Model.SINE[i2];
			int l3 = Model.COSINE[i2];
			int j4 = (int) (l2 * j3) + j2 * l3 >> 16;
			l2 = (int) (l2 * l3) - j2 * j3 >> 16;
			j2 = j4;
		}
		cameraX = l - j2;
		cameraY = i1 - k2;
		cameraZ = k1 - l2;
		cameraPitch = k;
		cameraYaw = j1;
		onCameraPitchChanged(k);
	}

	public void updateStrings(String str, int i) {
		switch (i) {
			case 22499:
				sendFrame126(currentLevels[5]+"/"+ maximumLevels[5], 22499);
				break;
			case 49020:
				achievementCutoff = Integer.parseInt(str);
				break;
			case 1675:
				sendFrame126(str, 17508);
				break;// Stab
			case 1676:
				sendFrame126(str, 17509);
				break;// Slash
			case 1678:
				sendFrame126(str, 17511);
				break;// Magic
			case 1679:
				sendFrame126(str, 17512);
				break;// Range
			case 1680:
				sendFrame126(str, 17513);
				break;// Stab
			case 1681:
				sendFrame126(str, 17514);
				break;// Slash
			case 1682:
				sendFrame126(str, 17515);
				break;// Crush
			case 1683:
				sendFrame126(str, 17516);
				break;// Magic
			case 1684:
				sendFrame126(str, 17517);
				break;// Range
			case 1686:
				sendFrame126(str, 17518);
				break;// Strength
			case 1687:
				sendFrame126(str, 17519);
				break;// Prayer
		}
	}

	public void sendFrame126(String str, int i) {
		RSInterface component = RSInterface.interfaceCache[i];
		if (component != null) {
			component.message = str;
			if (component.type == 4 && component.atActionType == 1) {
				component.hoverText = str;
			}
			if (component.parentID == tabInterfaceIDs[tabID])
				needDrawTabArea = true;
		}
	}

	public void sendKeyboardAction(int action) {
		stream.createFrame(201);
		stream.writeByte(action);
	}

	public void sendFrame248(int interfaceID, int sideInterfaceID) {
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
		}
		if (inputDialogState != 0) {
			inputDialogState = 0;
			inputTaken = true;
		}
		openInterfaceID = interfaceID;
		invOverlayInterfaceID = sideInterfaceID;
		needDrawTabArea = true;
		tabAreaAltered = true;
		continuedDialogue = false;
	}

	private static final Comparator<ExperienceDrop> HIGHEST_POSITION = new Comparator<ExperienceDrop>() {

		@Override
		public int compare(ExperienceDrop o1, ExperienceDrop o2) {
			return Integer.compare(o2.getYPosition(), o1.getYPosition());
		}

	};

	private boolean parsePacket() {
		if (socketStream == null)
			return false;
		try {
			int i = socketStream.available();
			if (i == 0)
				return false;
			if (incomingPacket == -1) {
				socketStream.flushInputStream(inStream.payload, 1);
				incomingPacket = inStream.payload[0] & 0xff;
				if (encryption != null)
					incomingPacket = incomingPacket - encryption.getNextKey() & 0xff;
				packetSize = SizeConstants.packetSizes[incomingPacket];
				i--;
			}
			if (packetSize == -1)
				if (i > 0) {
					socketStream.flushInputStream(inStream.payload, 1);
					packetSize = inStream.payload[0] & 0xff;
					i--;
				} else {
					return false;
				}
			if (packetSize == -2)
				if (i > 1) {
					socketStream.flushInputStream(inStream.payload, 2);
					inStream.currentPosition = 0;
					packetSize = inStream.readUShort();
					i -= 2;
				} else {
					return false;
				}
			if (i < packetSize) {
//				String message = "";
//				message = String.format("Available=%d less than packet-size=%d, ", i, packetSize);
//				message += " - Packet: " + incomingPacket + ", Packet Size: " + packetSize
//						+ " - Previous packet: " + previousPacket1 + " Previous packet size: " + previousPacketSize1
//						+ ", 2nd Previous packet: " + previousPacket2 + ", 2nd Previous packet size: "
//						+ previousPacketSize2;
//				System.err.println(message);
				return false;
			}

			inStream.currentPosition = 0;
			socketStream.flushInputStream(inStream.payload, packetSize);
			anInt1009 = 0;

			previousPacketSize2 = previousPacketSize1;
			previousPacket2 = previousPacket1;

			previousPacketSize1 = dealtWithPacketSize;
			previousPacket1 = dealtWithPacket;

			dealtWithPacket = incomingPacket;
			dealtWithPacketSize = packetSize;

			PacketLog.add(incomingPacket, packetSize, inStream.payload);

			if (incomingPacket != 65 && incomingPacket != 81) // Ignore the update packets
				logger.debug("Parsing packet: " + incomingPacket);
			switch (incomingPacket) {
				case 13: // Client script
					String types = inStream.readString();
					Object[] arguments = new Object[types.length()];

					for (int index = 0; index < types.length(); index++) {
						if (types.charAt(index) == 's') {
							arguments[index] = inStream.readNullTerminatedString();
						} else { // 'i'
							arguments[index] = inStream.readDWord();
						}
					}

					int scriptId = inStream.readUShort();
					ClientScripts.execute(scriptId, arguments);
					incomingPacket = -1;
					return true;
				case 12:
					int soundId = inStream.readUShort();
					SoundType incomingSoundType = SoundType.values()[inStream.readUnsignedByte()];
					int entitySoundSource = inStream.readUShort();
					if (entitySoundSource == 0) {
						Sound.getSound().playSound(soundId, incomingSoundType, 0);
					} else {
						Entity entity;
						if (entitySoundSource >= Short.MAX_VALUE) {
							entitySoundSource -= Short.MAX_VALUE;
							entity = players[entitySoundSource];
						} else {
							entity = npcs[entitySoundSource];
						}

						if (entity != null) {
							Sound.getSound().playSound(soundId, incomingSoundType, localPlayer.getDistanceFrom(entity));
						} else {
							Sound.getSound().playSound(soundId, incomingSoundType, 0);
						}
					}
					incomingPacket = -1;
					return true;
				case 10:
					screenFlashDrawing = true;
					screenFlashOpacityDownward = false;
					screenFlashOpacity = 0d;
					screenFlashColor = inStream.readDWord();
					screenFlashMaxIntensity = inStream.readUnsignedByte();
					screenFlashAutoFadeOut = inStream.readUnsignedByte() == 0;
					incomingPacket = -1;
					return true;

				// Set strings inside string container
				case 5:
					int stringContainerId = inStream.readUShort();
					int strings = inStream.readUShort();
					RSInterface stringContainer = RSInterface.get(stringContainerId);
					Preconditions.checkState(stringContainer != null && stringContainer.stringContainer != null, "No string container at id: " + stringContainer);
					stringContainer.stringContainer.clear();
					for (int index = 0; index < strings; index++) {
						stringContainer.stringContainer.add(inStream.readString());
					}
					EventCalendar.getCalendar().onStringContainerUpdated(stringContainerId);
					incomingPacket = -1;
					return true;

				// Reset scroll position
				case 2:
					int resetScrollInterfaceId = inStream.readUShort();
					RSInterface.interfaceCache[resetScrollInterfaceId].scrollPosition = 0;
					incomingPacket = -1;
					return true;

				case 3:
					int setScrollMaxInterfaceId = inStream.readUShort();
					int scrollMax = inStream.readUShort();
					RSInterface.interfaceCache[setScrollMaxInterfaceId].scrollMax = scrollMax;
					incomingPacket = -1;
					return true;


				/**
				 * Progress Bar Update Packet
				 */
				case 77:
					while (inStream.currentPosition < packetSize) {
						int interfaceId = inStream.readDWord();
						byte progressBarState = inStream.readSignedByte();
						byte progressBarPercentage = inStream.readSignedByte();

						RSInterface rsInterface = RSInterface.interfaceCache[interfaceId];
						rsInterface.progressBarState = progressBarState;
						rsInterface.progressBarPercentage = progressBarPercentage;
					}
					incomingPacket = -1;
					return true;

				case 137:
					specialAttack = inStream.readUnsignedByte();
					incomingPacket = -1;
					return true;

				/**
				 * EntityTarget Update Packet
				 */
				case 222:
					byte entityState = (byte) inStream.readUnsignedByte();
					if (entityState != 0) {
						short entityIndex = (short) inStream.readUShort();
						short currentHealth = (short) inStream.readUShort();
						short maximumHealth = (short) inStream.readUShort();
						entityTarget = new EntityTarget(entityState, entityIndex, currentHealth, maximumHealth, newSmallFont);
					} else {
						if (entityTarget != null) {
							entityTarget.stop();
						}
						entityTarget = null;
					}
					incomingPacket = -1;
					return true;

				/**
				 * Timer Update Packet
				 */
				case 223:
					int timerId = inStream.readUnsignedByte();
					int secondsToAdd = inStream.readUShort();
					GameTimerHandler.getSingleton().startGameTimer(timerId, TimeUnit.SECONDS, secondsToAdd, 0);
					incomingPacket = -1;
					return true;

				case 224:
					timerId = inStream.readUShort();
					int itemId = inStream.readUShort();
					secondsToAdd = inStream.readUShort();
					GameTimerHandler.getSingleton().startGameTimer(timerId, TimeUnit.SECONDS, secondsToAdd, itemId);
					incomingPacket = -1;
					return true;

				case 11:
					long experience = inStream.readQWord();
					byte length = inStream.readSignedByte();
					int[] skills = new int[length];

					for (int j = 0; j < length; j++) {
						skills[j] = inStream.readSignedByte();
					}

					ExperienceDrop drop = new ExperienceDrop(experience, skills);

					if (!experienceDrops.isEmpty()) {
						List<ExperienceDrop> sorted = new ArrayList<ExperienceDrop>(experienceDrops);
						Collections.sort(sorted, HIGHEST_POSITION);
						ExperienceDrop highest = sorted.get(0);
						if (highest.getYPosition() >= ExperienceDrop.START_Y - 5) {
							drop.increasePosition(highest.getYPosition() - ExperienceDrop.START_Y + 20);
						}
					}

					experienceDrops.offer(drop);
					incomingPacket = -1;
					return true;

				case 81: // update player packet
					updatePlayers(packetSize, inStream);
					incomingPacket = -1;
					return true;

				case 176:
					daysSinceRecovChange = inStream.readNegUByte();
					unreadMessages = inStream.readUShortA();
					membersInt = inStream.readUnsignedByte();
					anInt1193 = inStream.method440();
					daysSinceLastLogin = inStream.readUShort();
					if (anInt1193 != 0 && openInterfaceID == -1) {
						Signlink.dnslookup(StringUtils.method586(anInt1193));
						clearTopInterfaces();
						char c = '\u028A';
						if (daysSinceRecovChange != 201 || membersInt == 1)
							c = '\u028F';
						reportAbuseInput = "";
						canMute = false;
						for (int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++) {
							if (RSInterface.interfaceCache[k9] == null || RSInterface.interfaceCache[k9].contentType != c)
								continue;
							openInterfaceID = RSInterface.interfaceCache[k9].parentID;

						}
					}
					incomingPacket = -1;
					return true;

				case 64:
					localX = inStream.readNegUByte();
					localY = inStream.method428();
					for (int j = localX; j < localX + 8; j++) {
						for (int l9 = localY; l9 < localY + 8; l9++)
							if (groundItems[plane][j][l9] != null) {
								groundItems[plane][j][l9] = null;
								this.updateItemPile(j, l9);

							}
					}
					for (SpawnedObject spawnedObject = (SpawnedObject) spawns
							.reverseGetFirst(); spawnedObject != null; spawnedObject = (SpawnedObject) spawns
							.reverseGetNext())
						if (spawnedObject.x >= localX && spawnedObject.x < localX + 8
								&& spawnedObject.y >= localY && spawnedObject.y < localY + 8
								&& spawnedObject.plane == plane)
							spawnedObject.getLongetivity = 0;
					incomingPacket = -1;
					return true;

				case 9:
					String text = inStream.readString();
					byte state = inStream.readSignedByte();
					byte seconds = inStream.readSignedByte();
					int drawingWidth = !Client.instance.isResized() ? 519 : Client.canvasWidth;
					int drawingHeight = !Client.instance.isResized() ? 338 : Client.canvasHeight;

					incomingPacket = -1;
					return true;

				case 185:
					int k = inStream.method436();
					RSInterface.interfaceCache[k].defaultMediaType = 3;
					if (localPlayer.npcDefinition == null)
						RSInterface.interfaceCache[k].defaultMedia = (localPlayer.appearanceColors[0] << 25)
								+ (localPlayer.appearanceColors[4] << 20) + (localPlayer.equipment[0] << 15)
								+ (localPlayer.equipment[8] << 10) + (localPlayer.equipment[11] << 5) + localPlayer.equipment[1];
					else
						RSInterface.interfaceCache[k].defaultMedia = (int) (0x12345678L + localPlayer.npcDefinition.npcId);
					incomingPacket = -1;
					return true;

				/* Clan message packet */
//				case 217:
//					try {
//						clanUsername = inStream.readString();
//						clanMessage = TextInput.processText(inStream.readString());
//						clanTitle = inStream.readString();
//						PlayerRights[] rights = PlayerRights.readRightsFromPacket(inStream);
//						channelRights = EnumSet.of(rights);
//						pushMessage(clanMessage, 12, clanUsername);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					incomingPacket = -1;
//					return true;

				case 107:
					Camera.isCameraUpdating = false;
					isCameraLocked = false;
					Camera.followCameraMode = false;
					Camera.staticCameraMode = false;
					Camera.staticCameraX = 0;
					Camera.staticCameraY = 0;
					Camera.staticCameraAltitudeOffset = 0;
					Camera.fixedCamera = false;
					Camera.cameraPitchStep = 0;
					Camera.cameraYawSpeed = 0;
					Camera.cameraInterpolationSpeed = 0;
					Camera.cameraMinimumStep = 0;
					Camera.cameraTargetX = 0;
					Camera.cameraTargetY = 0;
					Camera.cameraAltitudeOffset = 0;
					Camera.camera = null;
					Camera.cameraPitch = null;
					Camera.cameraYaw = null;

					for (int var20 = 0; var20 < 5; ++var20) {
						quakeDirectionActive[var20] = false;
					}
					xpCounter = 0;
					incomingPacket = -1;
					return true;

				case 72:
					int i1 = inStream.method434();
					RSInterface class9 = RSInterface.interfaceCache[i1];
					for (int k15 = 0; k15 < class9.inventoryItemId.length; k15++) {
						class9.inventoryItemId[k15] = -1;
						class9.inventoryItemId[k15] = 0;
					}
					incomingPacket = -1;
					return true;

				case 214:
					ignoreCount = packetSize / 8;
					for (int j1 = 0; j1 < ignoreCount; j1++)
						ignoreListAsLongs[j1] = inStream.readQWord();
					incomingPacket = -1;
					return true;

				case 166:
					isCameraLocked = true;
					Camera.isCameraUpdating = false;
					Camera.followCameraMode = false;
					Camera.cameraTargetX = inStream.readUnsignedByte() * 16384;
					Camera.cameraTargetY = inStream.readUnsignedByte() * 16384;
					Camera.cameraAltitudeOffset = inStream.readUShort();
					Camera.cameraMinimumStep = inStream.readUnsignedByte();
					Camera.cameraInterpolationSpeed = inStream.readUnsignedByte();
					if (Camera.cameraInterpolationSpeed >= 100) {
						cameraX = Camera.cameraTargetX * 128 + 64;
						cameraZ = Camera.cameraTargetY * 128 + 64;
						cameraY = getCenterHeight(plane, cameraZ, cameraX) - Camera.cameraAltitudeOffset;
					}
					incomingPacket = -1;
					return true;

				case 134:
					needDrawTabArea = true;
					int skillId = inStream.readUnsignedByte();
					int experience2 = inStream.method439();
					int currentLevel = inStream.readUnsignedByte();
					int xp = currentExp[skillId];

					currentExp[skillId] = experience2;
					currentLevels[skillId] = currentLevel;
					maximumLevels[skillId] = 1;
					xpCounter += currentExp[skillId] - xp;
					expAdded = currentExp[skillId] - xp;

					try {
						callbacks.post(new StatChanged(
								Skill.valueOf(Skills.SKILL_NAMES_ORDER[skillId].toUpperCase()),
								experience2,
								currentLevel,
								3
						));

					}catch (Exception ex) {}


					for (int k20 = 0; k20 < 98; k20++)
						if (experience2 >= SKILL_EXPERIENCE[k20])
							maximumLevels[skillId] = k20 + 2;
					updateSkillStrings(skillId);
					incomingPacket = -1;
					return true;

				case 71:
					int l1 = inStream.readUShort();
					int j10 = inStream.method426();
					if (l1 == 65535)
						l1 = -1;
					tabInterfaceIDs[j10] = l1;
					needDrawTabArea = true;
					tabAreaAltered = true;
					incomingPacket = -1;
					return true;

				case 74:
					int songId = inStream.method434();
					if (songId == 65535) {
						songId = -1;
					}
					if (songId != -1 || prevSong != 0) {
						if (songId != -1 && currentSong != songId && musicVolume != 0 && prevSong == 0) {
							// method58(10, musicVolume, false, songId);
						}
					} else {
						// method55(false);
					}
					currentSong = songId;
					incomingPacket = -1;
					return true;

				case 121:
					int j2 = inStream.method436();
					int k10 = inStream.readUShortA();
					if (musicEnabled && !lowMem) {
						nextSong = j2;
						songChanging = false;
						prevSong = k10;
					}
					incomingPacket = -1;
					return true;

				case 7:
					int componentId = inStream.readDWord();
					byte spriteIndex = inStream.readSignedByte();
					RSInterface component = RSInterface.interfaceCache[componentId];
					if (component != null) {
						if (component.backgroundSprites != null && spriteIndex <= component.backgroundSprites.length - 1) {
							Sprite sprite = component.backgroundSprites[spriteIndex];
							if (sprite != null) {
								component.sprite1 = component.backgroundSprites[spriteIndex];
							}
						}
					}
					incomingPacket = -1;
					return true;

				case 109:
					resetLogout();
					incomingPacket = -1;
					return false;

				case 70:
					int k2 = inStream.readSignedWord();
					int l10 = inStream.method437();
					int i16 = inStream.method434();
					RSInterface class9_5 = RSInterface.interfaceCache[i16];
					class9_5.anInt263 = k2;
					class9_5.anInt265 = l10;
					incomingPacket = -1;
					return true;

				case 243:
					int fog = inStream.readUnsignedByte();
					fogEnabled = fog == 1;
					fogOpacity = inStream.readDWord();
					incomingPacket = -1;
					return true;

				case 73:
				case 241:
					setGameState(GameState.LOADING);
					int mapRegionX = currentRegionX;
					int mapRegionY = currentRegionY;
					if (incomingPacket == 73) {
						mapRegionX = inStream.readUShortA();
						mapRegionY = inStream.readUShort();
						isInInstance = false;
					}
					if (incomingPacket == 241) {
						mapRegionY = inStream.readUShortA();
						System.out.println("mapRegionY: " + mapRegionY);
						inStream.initBitAccess();
						for (int z = 0; z < 4; z++) {
							for (int x = 0; x < 13; x++) {
								for (int y = 0; y < 13; y++) {
									int visible = inStream.readBits(1);
									if (visible == 1)
										constructRegionData[z][x][y] = inStream.readBits(26);
									else
										constructRegionData[z][x][y] = -1;
								}
							}
						}
						inStream.finishBitAccess();
						mapRegionX = inStream.readShort();
						isInInstance = true;
					}
					if (currentRegionX == mapRegionX && currentRegionY == mapRegionY && gameState == GameState.LOGGED_IN.getState()) {
						incomingPacket = -1;
						return true;
					}
					currentRegionX = mapRegionX;
					currentRegionY = mapRegionY;
					baseX = (currentRegionX - 6) * 8;
					baseY = (currentRegionY - 6) * 8;
					inTutorialIsland = (currentRegionX / 8 == 48 || currentRegionX / 8 == 49) && currentRegionY / 8 == 48;
					if (currentRegionX / 8 == 48 && currentRegionY / 8 == 148)
						inTutorialIsland = true;

					longStartTime = System.currentTimeMillis();

					setGameState(GameState.LOADING);

					if (incomingPacket == 73) {
						int regionCount = 0;
						for (int x = (currentRegionX - 6) / 8; x <= (currentRegionX + 6) / 8; x++) {
							for (int y = (currentRegionY - 6) / 8; y <= (currentRegionY + 6) / 8; y++)
								regionCount++;
						}
						regionLandArchives = new byte[regionCount][];
						regionMapArchives = new byte[regionCount][];
						regions = new int[regionCount];
						regionLandIds = new int[regionCount];
						regionLocIds = new int[regionCount];
						regionCount = 0;

						for (int x = (currentRegionX - 6) / 8; x <= (currentRegionX + 6) / 8; x++) {
							for (int y = (currentRegionY - 6) / 8; y <= (currentRegionY + 6) / 8; y++) {
								regions[regionCount] = (x << 8) + y;
								if (inTutorialIsland
										&& (y == 49 || y == 149 || y == 147 || x == 50 || x == 49 && y == 47)) {
									regionLandIds[regionCount] = -1;
									regionLocIds[regionCount] = -1;
									regionCount++;
								} else {
									int id = y + (x << 8);
									regions[regionCount] = id;
									regionLandIds[regionCount] = Js5List.maps.getGroupId("m" + x + "_" + y);
									regionLocIds[regionCount] = Js5List.maps.getGroupId("l" + x + "_" + y);
									regionCount++;
								}
							}
						}

					}
					if (incomingPacket == 241) {
						int totalLegitChunks = 0;
						int totalChunks[] = new int[676];
						for (int z = 0; z < 4; z++) {
							for (int x = 0; x < 13; x++) {
								for (int y = 0; y < 13; y++) {
									int tileBits = constructRegionData[z][x][y];
									if (tileBits != -1) {
										System.out.println("Set Bits");
										int xCoord = tileBits >> 14 & 0x3ff;
										int yCoord = tileBits >> 3 & 0x7ff;
										int mapRegion = (xCoord / 8 << 8) + yCoord / 8;
										for (int idx = 0; idx < totalLegitChunks; idx++) {
											if (totalChunks[idx] != mapRegion)
												continue;
											mapRegion = -1;

										}
										if (mapRegion != -1)
											totalChunks[totalLegitChunks++] = mapRegion;
									}
								}
							}
						}
						regionLandArchives = new byte[totalLegitChunks][];
						regionMapArchives = new byte[totalLegitChunks][];
						regions = new int[totalLegitChunks];
						regionLandIds = new int[totalLegitChunks];
						regionLocIds = new int[totalLegitChunks];

						try {
							for (int idx = 0; idx < totalLegitChunks; idx++) {
								int region = regions[idx] = totalChunks[idx];
								int constructedRegionX = region >> 8 & 0xff;
								int constructedRegionY = region & 0xff;
								regionLandIds[totalLegitChunks] = Js5List.maps.getGroupId("m" + constructedRegionX + "_" + constructedRegionY);
								regionLocIds[totalLegitChunks] = Js5List.maps.getGroupId("l" + constructedRegionX + "_" + constructedRegionY);
							}
						}catch (Exception e){}


					}
					int dx = baseX - previousAbsoluteX;
					int dy = baseY - previousAbsoluteY;
					previousAbsoluteX = baseX;
					previousAbsoluteY = baseY;
					for (int index = 0; index < 16384; index++) {
						Npc npc = npcs[index];
						if (npc != null) {
							for (int poiint = 0; poiint < 10; poiint++) {
								npc.pathX[poiint] -= dx;
								npc.pathY[poiint] -= dy;
							}
							npc.x -= dx * 128;
							npc.y -= dy * 128;
						}
					}
					for (int index = 0; index < maxPlayers; index++) {
						Player player = players[index];
						if (player != null) {
							for (int i31 = 0; i31 < 10; i31++) {
								player.pathX[i31] -= dx;
								player.pathY[i31] -= dy;
							}
							player.x -= dx * 128;
							player.y -= dy * 128;
						}
					}

					byte startX = 0;
					byte endX = 104;
					byte stepX = 1;
					if (dx < 0) {
						startX = 103;
						endX = -1;
						stepX = -1;
					}
					byte startY = 0;
					byte endY = 104;
					byte stepY = 1;
					if (dy < 0) {
						startY = 103;
						endY = -1;
						stepY = -1;
					}
					for (int x = startX; x != endX; x += stepX) {
						for (int y = startY; y != endY; y += stepY) {
							int shiftedX = x + dx;
							int shiftedY = y + dy;
							for (int plane = 0; plane < 4; plane++)
								if (shiftedX >= 0 && shiftedY >= 0 && shiftedX < 104 && shiftedY < 104)
									groundItems[plane][x][y] = groundItems[plane][shiftedX][shiftedY];
								else
									groundItems[plane][x][y] = null;
						}
					}
					for (SpawnedObject object = (SpawnedObject) spawns
							.reverseGetFirst(); object != null; object = (SpawnedObject) spawns
							.reverseGetNext()) {
						object.x -= dx;
						object.y -= dy;
						if (object.x < 0 || object.y < 0 || object.x >= 104
								|| object.y >= 104)
							object.unlink();
					}
					if (destX != 0) {
						destX -= dx;
						destY -= dy;
					}
					StaticSound.resetSoundCount();
					isCameraLocked = false;
					oculusOrbFocalPointX -= dx << 7;
					oculusOrbFocalPointY -= dy << 7;
					setGameState(GameState.LOADING);
					incomingPacket = -1;
					return true;

				case 208:
					int i3 = inStream.readUShort();
					if (i3 == 65535)
						i3 = -1; // Changed to unsigned short so need to manually make it -1
					if (i3 >= 0)
						resetAnimation(i3);
					openWalkableWidgetID = i3;
					incomingPacket = -1;
					return true;

				case 99:
					minimapState = inStream.readUnsignedByte();
					incomingPacket = -1;
					return true;

				case 75:
					int j3 = inStream.method436();
					int j11 = inStream.method436();
					RSInterface.interfaceCache[j11].defaultMediaType = 2;
					RSInterface.interfaceCache[j11].defaultMedia = j3;
					incomingPacket = -1;
					return true;

				case 114:
					anInt1104 = inStream.method434() * 30;
					//broadcastActive = false;
					broadcastTimer = 0;
					incomingPacket = -1;
					return true;

				case 60:
					localY = inStream.readUnsignedByte();
					localX = inStream.readNegUByte();
					while (inStream.currentPosition < packetSize) {
						int k3 = inStream.readUnsignedByte();
						method137(inStream, k3);
					}
					incomingPacket = -1;
					return true;

				case 35:
					int quakeDirection = inStream.readUnsignedByte();
					int quakeMagnitude = inStream.readUnsignedByte();
					int quakeAmplitude = inStream.readUnsignedByte();
					int fourPiOverPeriod = inStream.readUnsignedByte();
					quakeDirectionActive[quakeDirection] = true;
					this.quakeMagnitude[quakeDirection] = quakeMagnitude;
					this.quakeAmplitude[quakeDirection] = quakeAmplitude;
					this.fourPiOverPeriod[quakeDirection] = fourPiOverPeriod;
					cameraUpdateCounters[quakeDirection] = 0;
					incomingPacket = -1;
					return true;

				case 174:
					int id = inStream.readUShort();
					int type = inStream.readUnsignedByte();
					int delay = inStream.readUShort();
					if (soundEffectVolume != 0 && type != 0 && soundCount < 50) {
						sound[soundCount] = id;
						soundType[soundCount] = type;
						soundDelay[soundCount] = delay;
						// aClass26Array1468[soundCount] = null;
						soundCount++;
					}
					incomingPacket = -1;
					return true;

				case 104:
					int j4 = inStream.readNegUByte();
					int i12 = inStream.method426();
					String s6 = inStream.readString();
					if (j4 >= 1 && j4 <= playerOptions.length) {
						if (s6.equalsIgnoreCase("null"))
							s6 = null;
						playerOptions[j4 - 1] = s6;
						playerOptionsHighPriority[j4 - 1] = i12 == 0;
					}
					incomingPacket = -1;
					return true;

				case 78:
					destX = 0;
					incomingPacket = -1;
					return true;

				case 253:
					String s = inStream.readString();
					if (s.equals(":prayertrue:")) {
						prayClicked = true;
					} else if (s.equals(":prayerfalse:")) {
						prayClicked = false;
					} else if (s.equals(":spin")) {
						setSpinClick(true);
						spin();
						incomingPacket = -1;
						return true;
					} else if (s.equals(":resetpost:")) {
						RSInterface listingWidget = RSInterface.interfaceCache[48020];
						if (listingWidget != null) {
							listingWidget.scrollPosition = 0;
						}
					}
					else if (s.equals(":resetBox")) {
						reset();
						incomingPacket = -1;
						return true;
					} else if (s.endsWith(":gamblereq:")) {
						String name = s.substring(0, s.indexOf(":"));
						long nameToLongFormat = StringUtils.longForName(name);
						boolean isIgnored = false;
						for (int index = 0; index < ignoreCount; index++) {
							if (ignoreListAsLongs[index] != nameToLongFormat)
								continue;
							isIgnored = true;
						}
						if (!isIgnored && anInt1251 == 0)
							pushMessage("wishes to Gamble with you.", 31, name);
					} else if (s.endsWith(":tradereq:")) {
						String s3 = s.substring(0, s.indexOf(":"));
						long l17 = StringUtils.longForName(s3);
						boolean flag2 = false;
						for (int j27 = 0; j27 < ignoreCount; j27++) {
							if (ignoreListAsLongs[j27] != l17)
								continue;
							flag2 = true;

						}
						if (!flag2 && anInt1251 == 0)
							pushMessage("wishes to trade with you.", 4, s3);
					} else if (s.startsWith("//")) {
						s = s.replaceAll("//", "");
						pushMessage(s, 13, "");
					} else if (s.startsWith("/")) {
						s = s.replaceFirst("/", "");
						pushMessage(s, 11, "");
					} else if (s.endsWith("#url#")) {
						String link = s.substring(0, s.indexOf("#"));
						pushMessage("Join us at: ", 9, link);
					} else if (s.endsWith(":duelreq:")) {
						String s4 = s.substring(0, s.indexOf(":"));
						long l18 = StringUtils.longForName(s4);
						boolean flag3 = false;
						for (int k27 = 0; k27 < ignoreCount; k27++) {
							if (ignoreListAsLongs[k27] != l18)
								continue;
							flag3 = true;

						}
						if (!flag3 && anInt1251 == 0)
							pushMessage("wishes to duel with you.", 8, s4);
					} else if (s.endsWith(":chalreq:")) {
						String s5 = s.substring(0, s.indexOf(":"));
						long l19 = StringUtils.longForName(s5);
						boolean flag4 = false;
						for (int l27 = 0; l27 < ignoreCount; l27++) {
							if (ignoreListAsLongs[l27] != l19)
								continue;
							flag4 = true;

						}
						if (!flag4 && anInt1251 == 0) {
							String s8 = s.substring(s.indexOf(":") + 1, s.length() - 9);
							pushMessage(s8, 8, s5);
						}
					} else {
						pushMessage(s, 0, "");
					}
					incomingPacket = -1;
					return true;

				case 1:
					for (int k4 = 0; k4 < players.length; k4++)
						if (players[k4] != null)
							players[k4].sequence = -1;
					for (int j12 = 0; j12 < npcs.length; j12++)
						if (npcs[j12] != null)
							npcs[j12].sequence = -1;
					incomingPacket = -1;
					return true;

				case 50:
					long l4 = inStream.readQWord();
					int i18 = inStream.readUnsignedByte();
					//String s7 = StringUtils.fixName(StringUtils.nameForLong(l4));
					for (int k24 = 0; k24 < friendsCount; k24++) {
						if (l4 != friendsListAsLongs[k24])
							continue;
						if (friendsNodeIDs[k24] != i18) {
							friendsNodeIDs[k24] = i18;
							needDrawTabArea = true;
							if (i18 > 0)
								pushMessage(friendsList[k24] + " has logged in.", 5, "");
							if (i18 == 0)
								pushMessage(friendsList[k24] + " has logged out.", 5, "");
						}
						//s7 = null;
						break;
					}
					sortFriendsList();
					incomingPacket = -1;
					return true;

				case 18: // Add friend or ignore
					String displayName = inStream.readNullTerminatedString();
					long displayNameLong = StringUtils.longForName(displayName.toLowerCase());
					boolean friend = inStream.readSignedByte() == 1;
					int world = friend ? inStream.readUnsignedByte() : 0;

					if (friend) {
						friendsList[friendsCount] = displayName;
						friendsListAsLongs[friendsCount] = displayNameLong;
						friendsNodeIDs[friendsCount] = world;
						friendsCount++;
					} else {
						ignoreListAsLongs[ignoreCount++] = displayNameLong;
					}

					sortFriendsList();
					needDrawTabArea = true;
					incomingPacket = -1;
					return true;

				case 19: // Friend updates display name
					String oldDisplayName = inStream.readNullTerminatedString();
					String newDisplayName = inStream.readNullTerminatedString();
					long oldDisplayNameLong = StringUtils.longForName(oldDisplayName.toLowerCase());
					long newDisplayNameLong = StringUtils.longForName(newDisplayName.toLowerCase());

					for (int i22 = 0; i22 < friendsList.length; i22++) {
						if (friendsList[i22] == null)
							continue;
						if (friendsListAsLongs[i22] == oldDisplayNameLong) {
							friendsList[i22] = newDisplayName;
							friendsListAsLongs[i22] = newDisplayNameLong;
							needDrawTabArea = true;
							pushMessage(String.format("Friend '%s' updated display name to '%s'.", oldDisplayName, newDisplayName));
						}
					}

					sortFriendsList();
					incomingPacket = -1;
					return true;

				case 110:
					if (tabID == 12)
						needDrawTabArea = true;
					energy = inStream.readUnsignedByte();
					incomingPacket = -1;
					return true;

				case 254:
					hintArrowType = inStream.readUnsignedByte();
					if (hintArrowType == 1)
						hintArrowNpcIndex = inStream.readUShort();
					/**
					 * For static icons
					 */
					if (hintArrowType >= 2 && hintArrowType <= 6) {
						if (hintArrowType == 2) {
							anInt937 = 64;
							anInt938 = 64;
						}
						if (hintArrowType == 3) {
							anInt937 = 0;
							anInt938 = 64;
						}
						if (hintArrowType == 4) {
							anInt937 = 128;
							anInt938 = 64;
						}
						if (hintArrowType == 5) {
							anInt937 = 64;
							anInt938 = 0;
						}
						if (hintArrowType == 6) {
							anInt937 = 64;
							anInt938 = 128;
						}
						hintArrowType = 2;
						hintIconX = inStream.readUShort();
						hintIconYpos = inStream.readUShort();
						hintIconFloorPos = inStream.readUnsignedByte();
					}
					if (hintArrowType == 10)
						hintArrowPlayerIndex = inStream.readUShort();
					incomingPacket = -1;
					return true;

				case 248:
					int i5 = inStream.readUShortA();
					int k12 = inStream.readUShort();
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					if (i5 == 55000) {
						RSInterface.interfaceCache[55010].scrollPosition = 0;
						RSInterface.interfaceCache[55050].scrollPosition = 0;
					}
					openInterfaceID = i5;
					invOverlayInterfaceID = k12;
					needDrawTabArea = true;
					tabAreaAltered = true;
					continuedDialogue = false;
					incomingPacket = -1;
					return true;

				case 79:
					int j5 = inStream.method434();
					int l12 = inStream.readUShortA();
					RSInterface class9_3 = RSInterface.interfaceCache[j5];
					if (class9_3 != null && class9_3.type == 0) {
						if (l12 < 0)
							l12 = 0;
						if (l12 > class9_3.scrollMax - class9_3.height)
							l12 = class9_3.scrollMax - class9_3.height;
						class9_3.scrollPosition = l12;
					}
					incomingPacket = -1;
					return true;

				case 68:
					for (int k5 = 0; k5 < variousSettings.length; k5++)
						if (variousSettings[k5] != anIntArray1045[k5]) {
							variousSettings[k5] = anIntArray1045[k5];
							updateVarp(k5);
							needDrawTabArea = true;
						}
					incomingPacket = -1;
					return true;

				case 196:
					String l5 = inStream.readNullTerminatedString();
					long l5Long = StringUtils.longForName(l5.toLowerCase());
					inStream.readDWord();
					Pair<Integer, PlayerRights[]> rights = PlayerRights.readRightsFromPacket(inStream);
					boolean flag5 = false;
					if (!PlayerRights.hasRightsBetween(rights.getRight(), 1, 4)) {
						for (int l29 = 0; l29 < ignoreCount; l29++) {
							if (ignoreListAsLongs[l29] != l5Long)
								continue;
							flag5 = true;

						}
					}
					if (!flag5 && anInt1251 == 0)
						try {
							String s9 = TextInput.method525(packetSize - 4 - (l5.length() + 1) - rights.getLeft() - 1, inStream);
							String rightsString = PlayerRights.buildCrownString(rights.getRight());
							pushMessage(s9, 7, rightsString + l5);
							if (Preferences.getPreferences().pmNotifications && !appFrame.isFocused()) {
								Notify.create()
										.title(Configuration.CLIENT_TITLE + " private message from " + l5)
										.text(s9)
										.position(Pos.BOTTOM_RIGHT)
										.onAction( new ActionHandler<Notify>() {
											@Override
											public void handle(Notify value) {
												pmTabToReply(l5);
											}
										})
										.hideAfter(5000)
										.shake(250, 5)
										.darkStyle()      // There are two default themes darkStyle() and default.
										.showConfirm();   // You can use warnings and error as well.
							}
						} catch (Exception exception1) {
							exception1.printStackTrace();
							Signlink.reporterror("cde1");
						}
					incomingPacket = -1;
					return true;

				case 85:
					localY = inStream.readNegUByte();
					localX = inStream.readNegUByte();
					incomingPacket = -1;
					return true;

				case 24:
					tabID = inStream.readUnsignedByte();
					incomingPacket = -1;
					return true;

				case 246:
					int i6 = inStream.method434();
					int i13 = inStream.readUShort();
					int k18 = inStream.readUShort();
					if (k18 == 65535) {
						RSInterface.interfaceCache[i6].defaultMediaType = 0;
						incomingPacket = -1;
						return true;
					} else {
						ItemDefinition itemDef = ItemDefinition.lookup(k18);
						RSInterface.interfaceCache[i6].defaultMediaType = 4;
						RSInterface.interfaceCache[i6].defaultMedia = k18;
						RSInterface.interfaceCache[i6].modelRotation1 = itemDef.xan2d;
						RSInterface.interfaceCache[i6].modelRotation2 = itemDef.yan2d;
						RSInterface.interfaceCache[i6].modelZoom = (itemDef.zoom2d * 100) / i13;
						incomingPacket = -1;
						return true;
					}

				case 171:
					boolean flag1 = inStream.readUnsignedByte() == 1;
					int j13 = inStream.readUShort();
					if (RSInterface.interfaceCache[j13] != null) {
						RSInterface.interfaceCache[j13].isMouseoverTriggered = flag1;
						RSInterface.interfaceCache[j13].interfaceHidden = flag1;
					}
					incomingPacket = -1;
					return true;

				case 142:
					int j6 = inStream.readUShort();
					if (j6 != 0) {
						resetAnimation(j6);
					}
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					invOverlayInterfaceID = j6;
					needDrawTabArea = true;
					tabAreaAltered = true;
					openInterfaceID = -1;
					continuedDialogue = false;
					incomingPacket = -1;
					return true;

				case 126:
					try {
						text = inStream.readString();
						int frame = inStream.readUShortA();

						handleScrollPosition(text, frame);
						handleRunePouch(text, frame);

						if (text.startsWith("www.") || text.startsWith("http")) {
							launchURL(text);
							incomingPacket = -1;
							return true;
						}
						if (text.startsWith(":pollHeight")) {
							int rows = Integer.parseInt(text.split("-")[1]);
							RSInterface rsi = RSInterface.interfaceCache[21406];
							rsi.childY[0] = (rows * 14);
							incomingPacket = -1;
							return true;
						}
						if (text.startsWith(":pollOn")) {
							String option[] = text.split("-");
							pollActive = Boolean.parseBoolean(option[1]);
							incomingPacket = -1;
							return true;
						}
						updateStrings(text, frame);
						sendFrame126(text, frame);
						if (frame >= 18144 && frame <= 18244) {
							clanList[frame - 18144] = text;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					incomingPacket = -1;
					return true;

				case 170:
					try {
						text = inStream.readString();
						// broadcastActive = true;
						// broadcastMessage = text;
						// broadcastTimer = System.currentTimeMillis();
					} catch (Exception e) {
						e.printStackTrace();
					}
					incomingPacket = -1;
					return true;

				case 179: {
					try {
						int broadcastType = inStream.readUnsignedByte();

						int broadcastIndex = inStream.readUnsignedByte();

						if (broadcastType == -1) {
							BroadcastManager.removeIndex(broadcastIndex);
							return true;
						}

						Broadcast broadcast = new Broadcast();

						broadcast.type = broadcastType;

						broadcast.index = broadcastIndex;

						broadcast.message = inStream.readString();

						if (broadcastType == 1) {
							broadcast.url = inStream.readString();
						} else if (broadcastType == 2) {
							broadcast.x = inStream.readDWord();
							broadcast.y = inStream.readDWord();
							broadcast.z = inStream.readUnsignedByte();
						}
						broadcast.setExpireDelay();
						BroadcastManager.addBoradcast(broadcast);
					} catch (Exception e) {
						e.printStackTrace();
					}
					incomingPacket = -1;
					return true;
				}

				case 206:
					publicChatMode = inStream.readUnsignedByte();
					privateChatMode = inStream.readUnsignedByte();
					tradeMode = inStream.readUnsignedByte();
					inputTaken = true;
					incomingPacket = -1;
					return true;
				case 204:
					specialAttack = inStream.readSignedByte();
					specialEnabled = inStream.readSignedByte();
					incomingPacket = -1;
					return true;
				case 240:
					if (tabID == 12)
						needDrawTabArea = true;
					weight = inStream.readSignedWord();
					incomingPacket = -1;
					return true;

				case 8:
					int k6 = inStream.method436();
					int l13 = inStream.readUShort();
					RSInterface.interfaceCache[k6].defaultMediaType = 1;
					RSInterface.interfaceCache[k6].defaultMedia = l13;
					incomingPacket = -1;
					return true;

				case 122:
					int l6 = inStream.method436();
					int i14 = inStream.method436();
					int i19 = i14 >> 10 & 0x1f;
					int i22 = i14 >> 5 & 0x1f;
					int l24 = i14 & 0x1f;
					RSInterface.interfaceCache[l6].textColor = (i19 << 19) + (i22 << 11) + (l24 << 3);
					incomingPacket = -1;
					return true;

				case 53:
					needDrawTabArea = true;
					int i7 = inStream.readUShort();
					RSInterface widget = RSInterface.interfaceCache[i7];
					int j19 = inStream.readUShort();

					try {
						HashMap<Integer, net.runelite.api.Item> items = new HashMap<Integer, net.runelite.api.Item>();

						//System.out.println("Item container size: " + j19);
						for (int j22 = 0; j22 < j19; j22++) {
							int i25 = inStream.readUnsignedByte();
							if (i25 == 255) {
								i25 = inStream.method440();
							}
							widget.inventoryItemId[j22] = inStream.method436();
							widget.inventoryAmounts[j22] = i25;
							items.put(j22,new net.runelite.api.Item(widget.inventoryItemId[j22] == -1 ? -1 : widget.inventoryItemId[j22] - 1, widget.inventoryAmounts[j22]));
							//System.out.println(String.format("Added item [%d, %d] to container %d", widget.inv[j22], i25, i7));
						}
						for (int j25 = j19; j25 < widget.inventoryItemId.length; j25++) {
							widget.inventoryItemId[j25] = 0;
							widget.inventoryAmounts[j25] = 0;
						}
						ItemContainer itemContainer = new ItemContainer() {

							@Override
							public int getId() {
								return i7;
							}

							@NotNull
							@Override
							public net.runelite.api.Item[] getItems() {
								net.runelite.api.Item[] list = new net.runelite.api.Item[j19];
								for (Map.Entry<Integer, net.runelite.api.Item> item : items.entrySet()) {
									list[item.getKey()] = item.getValue();
								}
								return list;
							}

							@Nullable
							@Override
							public net.runelite.api.Item getItem(int slot) {
								return items.get(slot);
							}

							@Override
							public boolean contains(int itemId) {
								return items.values().stream().anyMatch(list -> list.getId() == itemId);
							}

							@Override
							public int count(int itemId) {
								return items.values().stream().filter(list -> list.getId() == itemId).findFirst().get().getQuantity();
							}

							@Override
							public int size() {
								return j19;
							}

							@Override
							public Node getNext() {
								return null;
							}

							@Override
							public Node getPrevious() {
								return null;
							}

							@Override
							public void unlink() {

							}

							@Override
							public long getHash() {
								return 0;
							}


						};


						containers.put(i7, itemContainer);
						ItemContainerChanged item = new ItemContainerChanged(i7, itemContainer);
						callbacks.post(item);
					} catch (Exception e) {
						System.err.println("Error in container " + i7 + ", length " + j19 + ", actual length " + widget.inventoryItemId.length);
						e.printStackTrace();
					}
					incomingPacket = -1;
					return true;

				case 230:
					int j7 = inStream.readUShortA();
					int j14 = inStream.readUShort();
					int k19 = inStream.readUShort();
					int k22 = inStream.method436();
					RSInterface.interfaceCache[j14].modelRotation1 = k19;
					RSInterface.interfaceCache[j14].modelRotation2 = k22;
					RSInterface.interfaceCache[j14].modelZoom = j7;
					incomingPacket = -1;
					return true;

				case 221:
					anInt900 = inStream.readUnsignedByte();
					needDrawTabArea = true;
					incomingPacket = -1;
					return true;

				case 177:
					isCameraLocked = true;
					Camera.isCameraUpdating = false;
					Camera.staticCameraMode = false;
					Camera.staticCameraX = inStream.readUnsignedByte();
					Camera.staticCameraY = inStream.readUnsignedByte();
					Camera.staticCameraAltitudeOffset = inStream.readUShort();
					Camera.cameraPitchStep = inStream.readUnsignedByte();
					Camera.cameraYawSpeed = inStream.readUnsignedByte();
					if (Camera.cameraYawSpeed >= 100) {
						int var20 = Camera.staticCameraX * 16384 + 64;
						int var5 = Camera.staticCameraY * 16384 + 64;
						int var22 = getCenterHeight(plane, var5, var20) - Camera.staticCameraAltitudeOffset;
						int var7 = var20 - cameraX;
						int var8 = var22 - cameraY;
						int var24 = var5 - cameraZ;
						int var10 = (int)Math.sqrt((double)(var24 * var24 + var7 * var7));
						cameraPitch = (int)(Math.atan2((double)var8, (double)var10) * 325.9490051269531D) & 2047;
						cameraYaw = (int)(Math.atan2((double)var7, (double)var24) * -325.9490051269531D) & 2047;
						if (cameraPitch < 128) {
							cameraPitch = 128;
						}

						if (cameraPitch > 383) {
							cameraPitch = 383;
						}
						onCameraPitchChanged(0);
					}
					incomingPacket = -1;
					return true;

				case 249:
					anInt1046 = inStream.method426();
					localPlayerIndex = inStream.method436();
					incomingPacket = -1;
					return true;

				case 65:
					updateNPCs(inStream, packetSize);
					incomingPacket = -1;
					return true;

				case 27:
					enterInputInChatString = inStream.readString();
					messagePromptRaised = false;
					inputDialogState = 1;
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 28:
					messagePromptRaised = false;
					inputDialogState = 1;
					//promptInput = "";
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 187:
					enterInputInChatString = inStream.readString();
					messagePromptRaised = false;
					inputDialogState = 2;
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 191:
					messagePromptRaised = false;
					inputDialogState = 7;
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 192:
					messagePromptRaised = false;
					inputDialogState = 8;
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 98:
					int componentID = inStream.readUShort();
					int normal = inStream.readUShort();
					int hover = inStream.readUShort();
					if (RSInterface.interfaceCache[componentID] != null) {
						RSInterface.interfaceCache[componentID].sprite1 = ImageCache.get(normal);
						RSInterface.interfaceCache[componentID].sprite2 = ImageCache.get(hover);
					}
					incomingPacket = -1;
					return true;

				case 100:
					int componentIDD = inStream.readUShort();
					int statee = inStream.readByte();
					if (RSInterface.interfaceCache[componentIDD] != null) {
						RSInterface.interfaceCache[componentIDD].clickingDisabled = statee == 1 ? true : false;
					}
					incomingPacket = -1;
					return true;



				case 102:
					int scene = inStream.readByte();
					int id1 = inStream.readUShort();
					String[] text1 = inStream.readString().split("&", 6);

					String[] temp_options = new String[] { null, null, null, null, null };
					for (int i2 = 0; i2 < text1.length; i2++) {
						temp_options[i2] = text1[i2];
					}

					if(scene == 0) {
						ObjectDefinition.lookup(id1).actions = temp_options;
					} else if(scene == 1) {
						NpcDefinition.get(id1).actions = temp_options;
					}
					incomingPacket = -1;
					return true;

				case 97:
					int interfaceId = inStream.readUShort();
					resetAnimation(interfaceId);
					if (invOverlayInterfaceID != 0) {
						invOverlayInterfaceID = 0;
						needDrawTabArea = true;
						tabAreaAltered = true;
					}
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}

					if (interfaceId == 15244) {
						openInterfaceID = 15767;
						fullscreenInterfaceID = 15244;
					}

					if (interfaceId == 44900) {
						openInterfaceID = 44900;
						fullscreenInterfaceID = 44900;
					}




					openInterfaceID = interfaceId;
					continuedDialogue = false;
					incomingPacket = -1;
					return true;

				case 15:
					autocast = inStream.readUnsignedByte() == 1 ? true : false;
					incomingPacket = -1;
					return true;

				case 218:
					int i8 = inStream.method438();
					dialogID = i8;
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 87:
					int j8 = inStream.method434();
					int l14 = inStream.method439();
					anIntArray1045[j8] = l14;
					Bank.onConfigChanged(j8, l14);
					Nightmare.instance.handleConfig(j8, l14);
					Autocast.getSingleton().onConfigChanged(j8, l14);
					DailyRewards.get().onConfigReceived(j8, l14);
					DonatorRewards.getInstance().onConfigChanged(j8, l14);
					if (variousSettings[j8] != l14) {
						QuestTab.onConfigChanged(j8, l14);
						MonsterDropViewer.onConfigChanged(j8, l14);
						variousSettings[j8] = l14;
						updateVarp(j8);
						needDrawTabArea = true;
						if (dialogID != -1)
							inputTaken = true;
					}
					incomingPacket = -1;
					return true;

				case 36:
					int k8 = inStream.method434();
					byte byte0 = inStream.readSignedByte();
					anIntArray1045[k8] = byte0;
					Bank.onConfigChanged(k8, byte0);
					EventCalendar.getCalendar().onConfigReceived(k8, byte0);
					Nightmare.instance.handleConfig(k8, byte0);
					Autocast.getSingleton().onConfigChanged(k8, byte0);
					DailyRewards.get().onConfigReceived(k8, byte0);
					DonatorRewards.getInstance().onConfigChanged(k8, byte0);
					if (variousSettings[k8] != byte0) {
						QuestTab.onConfigChanged(k8, byte0);
						MonsterDropViewer.onConfigChanged(k8, byte0);
						variousSettings[k8] = byte0;
						updateVarp(k8);
						needDrawTabArea = true;
						if (dialogID != -1)
							inputTaken = true;
					}
					incomingPacket = -1;
					return true;

				case 61:
					anInt1055 = inStream.readUnsignedByte();
					incomingPacket = -1;
					return true;

				case 197:
					String message = inStream.readString();
					int npcIndex = inStream.readDWord();
					NPCDropInfo info = new NPCDropInfo();
					info.message = message;
					info.npcIndex = npcIndex;
					NPCDropInfo.addEntry(info);
					System.err.println(" "+message+" - "+npcIndex);
					pushMessage(message, 99, "");
					incomingPacket = -1;
					return true;

				case PacketIdentifiers.SEND_ANIMATE_CHAT_INTERFACE:
					int l8 = inStream.readUShort();
					int i15 = inStream.readSignedWord();
					RSInterface class9_4 = RSInterface.interfaceCache[l8];
					class9_4.disabledAnimationId = i15;
					if (i15 == 591 || i15 == 588) {
						class9_4.modelZoom = 900; // anInt269
					}
					if (i15 == -1) {
						class9_4.currentFrame = 0;
						class9_4.anInt208 = 0;
					}
					incomingPacket = -1;
					return true;

				case PacketIdentifiers.CLOSE_INTERFACE:
					closeInterface();
					incomingPacket = -1;
					return true;

				case 34:
				needDrawTabArea = true;
				int i9 = inStream.readUShort();
				if (handledPacket34(i9)) {
					incomingPacket = -1;
					return true;
				}

				RSInterface class9_2 = RSInterface.interfaceCache[i9];
				while (inStream.currentPosition < packetSize) {
					int j20 = (i9 == 23231 ? inStream.readUnsignedByte() : inStream.readDWord());
					int i23 = inStream.readUShort(); //Item ID
					int l25 = inStream.readUnsignedByte(); // Amount
					if (l25 == 255)
						l25 = inStream.readDWord();
					if (j20 >= 0 && j20 < class9_2.inventoryItemId.length) {
						class9_2.inventoryItemId[j20] = i23;
						class9_2.inventoryAmounts[j20] = l25;
					}
					System.out.println(i9 + " / " + i23 + " / " + l25  + " / " + j20) ;
				}
				incomingPacket = -1;
				return true;

				case 4:
				case 44:
				case 84:
				case 101:
				case 105:
				case 117:
				case 147:
				case 151:
				case 156:
				case 160:
				case 215:
					method137(inStream, incomingPacket);
					incomingPacket = -1;
					return true;

				case 106:
					tabID = inStream.readNegUByte();
					needDrawTabArea = true;
					tabAreaAltered = true;
					incomingPacket = -1;
					return true;

				case 164:
					int j9 = inStream.method434();
					resetAnimation(j9);
					if (invOverlayInterfaceID != 0) {
						invOverlayInterfaceID = 0;
						needDrawTabArea = true;
						tabAreaAltered = true;
					}
					backDialogID = j9;
					inputTaken = true;
					openInterfaceID = -1;
					continuedDialogue = false;
					incomingPacket = -1;
					return true;

			}

			Signlink.reporterror("T1 - Packet: " + incomingPacket + ", Packet Size: " + packetSize
					+ " - Previous packet: " + previousPacket1 + " Previous packet size: " + previousPacketSize1
					+ ", 2nd Previous packet: " + previousPacket2 + ", 2nd Previous packet size: "
					+ previousPacketSize2);
			PacketLog.log();
			resetLogout();
		} catch (IOException _ex) {
			PacketLog.log();
			_ex.printStackTrace();
			dropClient();
		} catch (Exception exception) {
			PacketLog.log();
			exception.printStackTrace();
			String s2 = "T2 - " + incomingPacket + "," + previousPacket1 + "," + previousPacket2 + " - " + packetSize
					+ "," + (baseX + localPlayer.pathX[0]) + "," + (baseY + localPlayer.pathY[0]) + " - ";
			for (int j15 = 0; j15 < packetSize && j15 < 50; j15++)
				s2 = s2 + inStream.payload[j15] + ",";
			Signlink.reporterror(s2);
			resetLogout();
			//ClientWindow.errorOccurredMessage("t2");
		}
		return true;
	}

	public static final int INTERFACE_ID = 47000;
	public static final int BOXES64 = 28; // 28 * 64 boxes
	private boolean spinClick;
	private int spins;
	private int spinNum;

	public void setSpinClick(boolean spinClick) { this.spinClick = spinClick; }

	public void spin() {
		if (openInterfaceID != INTERFACE_ID || !spinClick) {
			return;
		}

		RSInterface items = RSInterface.interfaceCache[47100];
		RSInterface boxes = RSInterface.interfaceCache[47200];
		if (spins < 100) {
			shift(items, boxes, 8);
		}
		else if (spins < 200) {
			shift(items, boxes, 5);
		}
		else if (spins < 300) {
			shift(items, boxes, 4);
		}
		else if (spins < 400) {
			shift(items, boxes, 3);
		}
		else if (spins < 488) {
			shift(items, boxes, 2);
		}
		else if (spins < 562) {
			shift(items, boxes, 1);
		}
		else {
			spinComplete();
		}
	}

	private void shift(RSInterface items, RSInterface boxes, int shiftAmount) {
		items.childX[0] -= shiftAmount;
		for(int i=0; i<BOXES64; i++){ boxes.childX[i] -= shiftAmount; }
		spins++;
	}

	private void spinComplete() {
		// Reset
		spins = 0;
		spinClick = false;
		spinNum++;
		// Notify server: spin complete
		stream.createFrame(145);
		stream.method432(696969);
		stream.method432(0);
		stream.method432(0);
	}

	public boolean handledPacket34(int frame) {
		if (openInterfaceID != INTERFACE_ID) {
			return false;
		}

		RSInterface items = RSInterface.interfaceCache[frame];
		while (inStream.currentPosition < packetSize) {
			int slot = inStream.readSmart();
			int itemId = inStream.readUShort();


			inStream.readUnsignedByte();
			int amount = inStream.readDWord();

			if (slot >= 0 && slot < items.inventoryItemId.length) {
				items.inventoryItemId[slot] = itemId;
				items.inventoryAmounts[slot] = amount;

				//System.out.println("item: "+itemId+", amount: "+amount);
			}
		}
		return true;
	}

	public void reset() {
		//System.out.println("test1");
		if (spinClick) {
			return;
		}

		//System.out.println("test2");

		spinNum = 0;
		RSInterface items = RSInterface.interfaceCache[47100];
		RSInterface boxes = RSInterface.interfaceCache[47200];
		items.childX[0] = 0;
		int x = 0;
		for (int z=0; z<BOXES64; z++) {
			boxes.childX[z] = x;
			x += 2880;
		}
	}

	private String coloredItemName = "";
	private int coloredItemColor = 0xffffff;

	public int currentFog = 0;
	public int[] rainbowFog = {0xFF0000,0xFF7F00,0xFFFF00,0x00FF00,0x0000FF,0x4B0082,0x9400D3};
	public long lastFog=0;

	public boolean runHover;


	public static JagexNetThread jagexNetThread = new JagexNetThread();

	static int method1389(int var0) {
		return var0 * 3 + 600;
	}

	public static int field625 = 256;
	static int field626 = 205;
	static int field440 = 1;
	static int field630 = 32767;
	static short field488 = 1;
	static short field562 = 32767;

	static void setViewportShape(int var0, int var1, int var2, int var3, boolean var4) {
		if (var2 < 1) {
			var2 = 1;
		}

		if (var3 < 1) {
			var3 = 1;
		}

		int var5 = var3 - 334;
		int var6;
		if (var5 < 0) {
			var6 = Client.field625;
		} else if (var5 >= 100) {
			var6 = Client.field626;
		} else {
			var6 = (Client.field626 - Client.field625) * var5 / 100 + Client.field625;
		}

		int var7 = var3 * var6 * 512 / (var2 * 334);
		int var8;
		int var9;
		short var17;
		if (var7 < Client.field488) {
			var17 = Client.field488;
			var6 = var17 * var2 * 334 / (var3 * 512);
			if (var6 > Client.field630) {
				var6 = Client.field630;
				var8 = var3 * var6 * 512 / (var17 * 334);
				var9 = (var2 - var8) / 2;
				if (var4) {
					Rasterizer2D.resetClip();
					Rasterizer2D.drawItemBox(var0, var1, var9, var3, -16777216);
					Rasterizer2D.drawItemBox(var0 + var2 - var9, var1, var9, var3, -16777216);
				}

				var0 += var9;
				var2 -= var9 * 2;
			}
		} else if (var7 > Client.field562) {
			var17 = Client.field562;
			var6 = var17 * var2 * 334 / (var3 * 512);
			if (var6 < Client.field440) {
				var6 = Client.field440;
				var8 = var17 * var2 * 334 / (var6 * 512);
				var9 = (var3 - var8) / 2;
				if (var4) {
					Rasterizer2D.resetClip();
					Rasterizer2D.drawItemBox(var0, var1, var2, var9, -16777216);
					Rasterizer2D.drawItemBox(var0, var3 + var1 - var9, var2, var9, -16777216);
				}

				var1 += var9;
				var3 -= var9 * 2;
			}
		}

		viewportZoom = var3 * var6 / 334;
		if (var2 != Client.instance.getViewportWidth() || var3 != Client.instance.getViewportHeight()) {
			int[] var16 = new int[9];

			for (var9 = 0; var9 < var16.length; ++var9) {
				int var10 = var9 * 32 + 15 + 128;
				int var11 = method1389(var10);
				int var12 = Rasterizer3D.SINE[var10];
				int var14 = var3 - 334;
				if (var14 < 0) {
					var14 = 0;
				} else if (var14 > 100) {
					var14 = 100;
				}

				int var15 = (Client.zoomWidth - Client.zoomHeight) * var14 / 100 + Client.zoomHeight;
				int var13 = var11 * var15 / 256;
				var16[var9] = var13 * var12 >> 16;
			}

			SceneGraph.buildVisiblityMap(var16, 500, 800, var2 * 334 / var3, 334);
		}

		Client.viewportOffsetX = var0;
		Client.viewportOffsetY = var1;
		Client.viewportWidth = var2;
		Client.viewportHeight = var3;
	}

	public static int viewportWidth = 0;
	public static int viewportHeight = 0;
	static short zoomHeight = 256;
	static short zoomWidth = 320;
	public static int viewportOffsetX = 0;
	public static int viewportOffsetY = 0;

	public void drawEntities(int viewportOffsetX, int viewportOffsetY, int width, int height) {
		viewportDrawCount++;
		if (localPlayer.x >> 7 == destX && localPlayer.y >> 7 == destY) {
			destX = 0;
		}
		showPrioritizedPlayers();
		showOtherPlayers();

		showPrioritizedNPCs();
		showOtherNpcs();

		createProjectiles();
		method104();

		int viewportXOffset = getViewportXOffset();
		int viewportYOffset = getViewportYOffset();

		setViewportShape(viewportOffsetX, viewportOffsetY, width, height, true);

		Rasterizer2D.setClip(viewportXOffset, viewportYOffset, viewportXOffset + getViewportWidth(), getViewportHeight() + viewportYOffset);
		Rasterizer3D.setupRasterizerClip();
		Rasterizer2D.resetDepth();

		int var37 = camAngleX;
		if (Camera.cameraAltitudeAdjustment / 256 > var37) {
			var37 = Camera.cameraAltitudeAdjustment / 256;
		}

		if (quakeDirectionActive[4] && quakeAmplitude[4] + 128 > var37) {
			var37 = quakeAmplitude[4] + 128;
		}

		int var5 = camAngleY & 2047;
		int var6 = oculusOrbFocalPointX;
		int var7 = Camera.oculusOrbAltitude;
		int var8 = oculusOrbFocalPointY;
		int var9 = var37 * 3 + 600;
		int var12 = viewportHeight - 334;
		if (var12 < 0) {
			var12 = 0;
		} else if (var12 > 100) {
			var12 = 100;
		}

		int var13 = (Client.zoomWidth - Client.zoomHeight) * var12 / 100 + Client.zoomHeight;
		int var11 = var13 * var9 / 256;
		var12 = 2048 - var37 & 2047;
		var13 = 2048 - var5 & 2047;
		int var14 = 0;
		int var15 = 0;
		int var16 = var11;
		int var17;
		int var18;
		int var19;
		if (var12 != 0) {
			var17 = Rasterizer3D.SINE[var12];
			var18 = Rasterizer3D.COSINE[var12];
			var19 = var18 * var15 - var17 * var11 >> 16;
			var16 = var18 * var11 + var15 * var17 >> 16;
			var15 = var19;
		}

		if (var13 != 0) {
			var17 = Rasterizer3D.SINE[var13];
			var18 = Rasterizer3D.COSINE[var13];
			var19 = var17 * var16 + var14 * var18 >> 16;
			var16 = var18 * var16 - var14 * var17 >> 16;
			var14 = var19;
		}

		if (isCameraLocked) {
			Camera.targetCameraX = var6 - var14;
			Camera.targetCameraY = var7 - var15;
			Camera.targetCameraZ = var8 - var16;
			Camera.targetCameraPitch = var37;
			Camera.targetCameraYaw = var5;
		} else {
			cameraX = var6 - var14;
			cameraY = var7 - var15;
			cameraZ = var8 - var16;
			cameraPitch = var37;
			cameraYaw = var5;
			onCameraPitchChanged(0);
		}

		if (!isCameraLocked) {
			var11 = Camera.determineRoofHeight();
		} else {
			var11 = Camera.calculateCameraPlane();
		}

		var12 = cameraX;
		var13 = cameraY;
		var14 = cameraZ;
		var15 = cameraPitch;
		var16 = cameraYaw;

		for (var17 = 0; var17 < 5; ++var17) {
			if (quakeDirectionActive[var17]) {
				var18 = (int)(Math.random() * (double)(quakeMagnitude[var17] * 2 + 1) - (double) quakeMagnitude[var17] + Math.sin((double) cameraUpdateCounters[var17] * ((double) fourPiOverPeriod[var17] / 100.0D)) * (double) quakeAmplitude[var17]);
				if (var17 == 0) {
					cameraX += var18;
				}

				if (var17 == 1) {
					cameraY += var18;
				}

				if (var17 == 2) {
					cameraZ += var18;
				}

				if (var17 == 3) {
					cameraYaw = var18 + cameraYaw & 2047;
				}

				if (var17 == 4) {
					cameraPitch += var18;
					if (cameraPitch < 128) {
						cameraPitch = 128;
						onCameraPitchChanged(0);
					}

					if (cameraPitch > 383) {
						cameraPitch = 383;
						onCameraPitchChanged(0);
					}
				}
			}
		}

		int mouseX = MouseHandler.mouseX;
		int mouseY = MouseHandler.mouseY;
		if (MouseHandler.clickMode3 != 0) {
			mouseX = MouseHandler.saveClickX;
			mouseY = MouseHandler.saveClickY;
		}

		if (mouseX >= viewportXOffset && mouseX < viewportXOffset + getViewportWidth() && mouseY >= viewportYOffset && mouseY < getViewportHeight() + viewportYOffset) {
			int mouseXLoc = mouseX - viewportXOffset;
			int mouseYLoc = mouseY - viewportYOffset;
			ViewportMouse.setClick(mouseXLoc, mouseYLoc);
		} else {
			ViewportMouse.method3167();
		}

		Rasterizer2D.Rasterizer2D_clear();

		callbacks.post(BeforeRender.INSTANCE);
		Rasterizer3D.toggleZBuffering(Configuration.ZBUFF);
		Rasterizer3D.clips.viewportZoom = Client.viewportZoom;
		scene.render(cameraX, cameraZ, cameraYaw, cameraY, var11, cameraPitch);
		Rasterizer3D.toggleZBuffering(false);
		Rasterizer3D.clips.viewportZoom = Clips.get3dZoom();
		overheadTextCount = 0;
		updateEntities(Client.viewportOffsetX, Client.viewportOffsetY);
		rasterProvider.init();
		scene.reset_interactive_obj();
		drawHeadIcon();

		((TextureProvider) Rasterizer3D.clips.textureLoader).animate(tickDelta);

		draw3dScreen();

		drawMinimap();
		drawTabArea();
		drawChatArea();

		if (HoverMenuManager.showMenu) {
			HoverMenuManager.drawHintMenu();
		}

		if (isMenuOpen()) {
			BeforeMenuRender event = new BeforeMenuRender();
			getCallbacks().post(event);
			if(!event.isConsumed()) {
				drawOriginalMenu(255);
			}
		}



		processExperienceCounter();
		viewportInterfaceCallback();

		if (Client.isLoading && jagexNetThread.method1968(true, false) == 0) {
			Client.isLoading = false;
		}

		if (Client.isLoading) {
			Rasterizer2D.fillRectangle(0, 0, canvasWidth, canvasHeight, 0);
			drawLoadingMessage("Loading - please wait.");
		}


		cameraX = var12;
		cameraY = var13;
		cameraZ = var14;
		cameraPitch = var15;
		cameraYaw = var16;
		onCameraPitchChanged(0);

	}

	private void viewportInterfaceCallback() {
		if (!isResized()) {
			callbacks.drawInterface(WidgetID.FIXED_VIEWPORT_GROUP_ID, Collections.emptyList());
		} else {
			callbacks.drawInterface(WidgetID.RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX_GROUP_ID, Collections.emptyList());
		}
	}


	public void clearTopInterfaces() {
		stream.createFrame(130);
		if (invOverlayInterfaceID != 0) {
			invOverlayInterfaceID = 0;
			needDrawTabArea = true;
			continuedDialogue = false;
			tabAreaAltered = true;
		}
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
			continuedDialogue = false;
		}
		openInterfaceID = -1;
		fullscreenInterfaceID = -1;
	}
	private Sprite[] chatButtons;

	public static class423 archive5;
	public Client() {
        ArchiveLoader.archiveLoaders = new ArrayList<>(10);
		ArchiveLoader.archiveLoadersDone = 0;
		fullscreenInterfaceID = -1;
		oculusOrbSlowedSpeed = 40;
		oculusOrbNormalSpeed = 20;
		camFollowHeight = 50;
		firstLoginMessage = "Enter Username & Password";
		xpAddedPos = expAdded = 0;
		keyManager = new KeyEventProcessorImpl();
		xpLock = false;
		experienceCounter = 0;
		soundType = new int[50];
		soundDelay = new int[50];
		sound = new int[50];
		chatTypeView = 0;
		mapsLoaded = 0;
		totalMaps = 1;
		objectsLoaded = 0;
		totalObjects = 1;
		clanTitles = new String[500];
		archive5 = new class423(8, class421.field4606);
		clanChatMode = 0;
		channelButtonHoverPosition = -1;
		channelButtonClickPosition = 0;
		travelDistances = new int[104][104];
		friendsNodeIDs = new int[200];
		groundItems = new NodeDeque[4][104][104];
		aBoolean831 = false;
		aStream_834 = new Buffer(new byte[5000]);
		npcs = new Npc[16384];
		npcIndices = new int[16384];
		anIntArray840 = new int[1000];
		aStream_847 = Buffer.create();
		aBoolean848 = true;
		setGameState(GameState.STARTING);
		openInterfaceID = -1;
		currentExp = new int[Skills.SKILLS_COUNT];
		quakeDirectionActive = new boolean[5];
		drawFlames = false;
		reportAbuseInput = "";
		localPlayerIndex = -1;
		isMenuOpen = false;
		inputString = "";
		maxPlayers = 2048;
		maxPlayerCount = 2047;
		players = new Player[maxPlayers];
		playerIndices = new int[maxPlayers];
		anIntArray894 = new int[maxPlayers];
		aStreamArray895s = new Buffer[maxPlayers];
		anIntArrayArray901 = new int[104][104];
		aByteArray912 = new byte[16384];
		currentLevels = new int[Skills.SKILLS_COUNT];
		ignoreListAsLongs = new long[100];
		loadingError = false;
		fourPiOverPeriod = new int[5];
		tileCycleMap = new int[104][104];
		chatTypes = new int[500];
		chatNames = new String[500];
		chatMessages = new String[500];
		chatButtons = new Sprite[4];
		aBoolean954 = true;
		friendsListAsLongs = new long[200];
		currentSong = -1;
		drawingFlames = false;
		viewportTempX = -1;
		viewportTempY = -1;
		anIntArray968 = new int[33];
		anIntArray969 = new int[256];

		variousSettings = new int[25000];
		aBoolean972 = false;
		overheadTextLimit = 50;
		overheadTextXs = new int[overheadTextLimit];
		overheadTextYs = new int[overheadTextLimit];
		overheadTextAscents = new int[overheadTextLimit];
		overheadTextXOffsets = new int[overheadTextLimit];
		overheadTextColors = new int[overheadTextLimit];
		overheadTextEffects = new int[overheadTextLimit];
		overheadTextCyclesRemaining = new int[overheadTextLimit];
		overheadText = new String[overheadTextLimit];
		lastKnownPlane = -1;
		hitMarks = new Sprite[20];
		anIntArray990 = new int[5];
		aBoolean994 = false;
		amountOrNameInput = "";
		projectiles = new NodeDeque();
		aBoolean1017 = false;
		openWalkableWidgetID = -1;
		cameraUpdateCounters = new int[5];
		aBoolean1031 = false;
		mapFunctions = new Sprite[119];
		dialogID = -1;
		maximumLevels = new int[Skills.SKILLS_COUNT];
		anIntArray1045 = new int[25000];
		aBoolean1047 = true;
		anIntArray1052 = new int[152];
		anIntArray1229 = new int[152];
		incompleteAnimables = new NodeDeque();
		anIntArray1057 = new int[33];
		aClass9_1059 = new RSInterface();
		barFillColor = 0x4d4233;
		myAppearance = new int[7];
		minimapHintX = new int[1000];
		minimapHintY = new int[1000];
		friendsList = new String[200];
		inStream = Buffer.create();
		expectedCRCs = new int[9];
		menuIdentifiers = new int[2500];
		menuTargets = new String[2500];
		menuShiftClick = new boolean[2500];
		menuArguments1 = new int[2500];
		menuArguments2 = new int[2500];
		menuOpcodes = new int[2500];
		menuActions = new String[2500];
		headIcons = new Sprite[20];
		skullIcons = new Sprite[20];
		headIconsHint = new Sprite[20];
		tabAreaAltered = false;
		aString1121 = "";
		playerOptions = new String[6];
		playerOptionsHighPriority = new boolean[6];
		constructRegionData = new int[4][13][13];
		minimapHint = new Sprite[1000];
		inTutorialIsland = false;
		continuedDialogue = false;
		musicEnabled = true;
		needDrawTabArea = false;
		loggedIn = false;
		canMute = false;
		isInInstance = false;
		isCameraLocked = false;
		myUsername = "";
		setPassword("");
		captchaInput = "";
		genericLoadingError = false;
		reportAbuseInterfaceID = -1;
		spawns = new NodeDeque();
		camAngleX = 128;
		invOverlayInterfaceID = 0;
		stream = Buffer.create();
		menuTargets = new String[500];
		quakeAmplitude = new int[5];
		chatAreaScrollLength = 78;
		promptInput = "";
		modIconss = new Sprite[30];
		tabID = 3;
		inputTaken = false;
		songChanging = true;
		collisionMaps = new CollisionMap[4];
		aBoolean1242 = false;
		rsAlreadyLoaded = false;
		welcomeScreenRaised = true;
		messagePromptRaised = false;
		backDialogID = -1;
		bigX = new int[4000];
		bigY = new int[4000];
		chatTimes = new long[500];
	}

	public int getLocalPlayerX() {
		return baseX + (localPlayer.x - 6 >> 7);
	}

	public int getLocalPlayerY() {
		return baseY + (localPlayer.y - 6 >> 7);
	}

	public int xpCounter;
	public int expAdded;
	public int xpAddedPos;
	public boolean xpLock;

	private Sprite chatArea;
	private Sprite infinity;

	public String name;
	public String message;
	public int chatTypeView;
	public int clanChatMode;
	public int duelMode;
	public int autocastId = 0;
	public boolean autocast = false;

	public static Sprite[] cacheSprite, cacheSprite1, cacheSprite2, cacheSprite3, cacheSprite4;
	public static Sprite[] cacheInterface;

	private IndexedImage titleButton;
	private int ignoreCount;
	private long longStartTime;
	private int[][] travelDistances;
	private int[] friendsNodeIDs;
	private NodeDeque[][][] groundItems;
	private int[] anIntArray828;
	private int[] anIntArray829;
	private volatile boolean aBoolean831;

	private Buffer aStream_834;
	public Npc[] npcs;
	private int npcCount;
	public int[] npcIndices;
	private int anInt839;
	private int[] anIntArray840;
	private int dealtWithPacket;
	private int dealtWithPacketSize;
	private int previousPacket1;
	private int previousPacket2;
	private int previousPacketSize2;
	private int previousPacketSize1;
	private String clickToContinueString;
	private int privateChatMode;
	private Buffer aStream_847;
	private boolean aBoolean848;
	private static int anInt849;
	private int[] anIntArray850;
	private int[] anIntArray851;
	private int[] anIntArray852;
	private int[] anIntArray853;
	private static int anInt854;
	public int hintArrowType;
	public static int openInterfaceID;
	public int cameraX;
	public int cameraY;
	public int cameraZ;
	public int cameraPitch;
	public int cameraYaw;
	private final int[] currentExp;
	private Sprite mapFlag;
	private Sprite mapMarker;

	private final boolean[] quakeDirectionActive;
	private int weight;
	private MouseDetection mouseDetection;
	private volatile boolean drawFlames;
	private String reportAbuseInput;
	private int localPlayerIndex;
	public boolean isMenuOpen;
	private int anInt886;
	public static String inputString;
	private final int maxPlayers;
	public final int maxPlayerCount;
	public Player[] players;
	public int playerCount;
	private int[] playerIndices;
	private int anInt893;
	private int[] anIntArray894;
	private Buffer[] aStreamArray895s;
	private int viewRotationOffset;
	private int friendsCount;
	private int anInt900;
	private int[][] anIntArrayArray901;
	private byte[] aByteArray912;
	private int anInt913;
	private int crossX;
	private int crossY;
	private int crossIndex;
	private int crossType;
	public int plane;
	public final int[] currentLevels;
	private int anInt924;
	private final long[] ignoreListAsLongs;
	private boolean loadingError;
	private final int[] fourPiOverPeriod;
	private int[][] tileCycleMap;
	private Sprite aClass30_Sub2_Sub1_Sub1_931;
	private Sprite aClass30_Sub2_Sub1_Sub1_932;
	public int hintArrowPlayerIndex;
	private int hintIconX;
	private int hintIconYpos;
	private int hintIconFloorPos;
	private int anInt937;
	private int anInt938;
	private long[] chatTimes;
	private int[] chatTypes;
	private String[] chatNames;
	private String[] chatMessages;
	public int tickDelta;
	public SceneGraph scene;

	private int menuX;
	private int menuY;
	private int menuWidth;
	private int menuHeight;
	private long aLong953;
	private boolean aBoolean954;
	private long[] friendsListAsLongs;
	private int currentSong;
	private static int nodeID = 1;
	static int portOff;
	static boolean clientData;
	private static boolean isMembers = true;
	private static boolean lowMem;
	private volatile boolean drawingFlames;
	public int viewportTempX;
	public int viewportTempY;
	private final int[] field758 = { 0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff };
	private final int[] anIntArray968;
	private final int[] anIntArray969;

	public int variousSettings[];
	private boolean aBoolean972;
	private final int overheadTextLimit;
	private final int[] overheadTextXs;
	private final int[] overheadTextYs;
	private final int[] overheadTextAscents;
	private final int[] overheadTextXOffsets;
	private final int[] overheadTextColors;


	public static int overheadTextCount;

	public static int[][] chatEffectGradientColors;
	private final int[] overheadTextEffects;
	private final int[] overheadTextCyclesRemaining;
	private final String[] overheadText;

	private int lastKnownPlane;
	private int anInt986;
	private Sprite[] hitMarks;
	private int anInt989;
	private final int[] anIntArray990;
	private final boolean aBoolean994;
	private int cinematicCamXViewpointLoc;
	private int cinematicCamYViewpointLoc;
	private int cinematicCamZViewpointLoc;
	private int anInt998;
	private int anInt999;
	private ISAACRandomGen encryption;
	private Sprite mapEdge;
	public static final int[][] APPEARANCE_COLORS = {
			{6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193},
			{8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239},
			{25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003},
			{4626, 11146, 6439, 12, 4758, 10270},
			{4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574, 15909, 32689, 130770, 947, 60359, 32433, 4960, 76770, 491770}
	};

	public static final short[] field3532 = new short[]{6798, 8741, 25238, 4626, 4550};

	public static final short[][] PLAYER_BODY_RECOLOURS = new short[][]{{6798, 107, 10283, 16, 4797, 7744, 5799, 4634, -31839, 22433, 2983, -11343, 8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010}, {8741, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533, 25239, 8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010}, {25238, 8742, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533, 8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010}, {4626, 11146, 6439, 12, 4758, 10270}, {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574, 17050, 0, 127, -31821, -17991}};

	public static final short[] field3534 = new short[]{-10304, 9104, -1, -1, -1};

	public static final short[][] field3535 = new short[][]{{6554, 115, 10304, 28, 5702, 7756, 5681, 4510, -31835, 22437, 2859, -11339, 16, 5157, 10446, 3658, -27314, -21965, 472, 580, 784, 21966, 28950, -15697, -14002}, {9104, 10275, 7595, 3610, 7975, 8526, 918, -26734, 24466, 10145, -6882, 5027, 1457, 16565, -30545, 25486, 24, 5392, 10429, 3673, -27335, -21957, 192, 687, 412, 21821, 28835, -15460, -14019}, new short[0], new short[0], new short[0]};


	private Sprite multiOverlay;
	public String amountOrNameInput;
	private int anInt1005;
	private int daysSinceLastLogin;
	private int packetSize;
	private int incomingPacket;
	private int anInt1009;
	private int anInt1010;
	private int anInt1011;
	private NodeDeque projectiles;
	public int oculusOrbFocalPointX;
	public int oculusOrbFocalPointY;
	private int anInt1016;
	private boolean aBoolean1017;
	public int openWalkableWidgetID;
	private static final int[] SKILL_EXPERIENCE;
	private int minimapState;
	private int anInt1022;

	private Sprite scrollBar1;
	private Sprite scrollBar2;
	private int anInt1026;
	public final int[] cameraUpdateCounters;
	private boolean aBoolean1031;
	private static Sprite[] mapFunctions;
	static int baseX;
	static int baseY;
	private int previousAbsoluteX;
	private int previousAbsoluteY;
	public int loginFailures;
	private int anInt1039;
	private int anInt1040;
	private int anInt1041;
	private int dialogID;
	public final int[] maximumLevels;
	private final int[] anIntArray1045;
	private int anInt1046;
	private boolean aBoolean1047;
	public TextDrawingArea smallText;
	public TextDrawingArea XPFONT;
	public TextDrawingArea aTextDrawingArea_1271;
	public TextDrawingArea chatTextDrawingArea;
	public TextDrawingArea aTextDrawingArea_1273;
	public RSFont newSmallFont;
	public RSFont newRegularFont;
	public RSFont newBoldFont;
	public RSFont newFancyFont;

	/**
	 * New fonts
	 */
	public static RSFont lato, latoBold, kingthingsPetrock, kingthingsPetrockLight;

	private int anInt1048;
	private String aString1049;
	private static int anInt1051;
	private final int[] anIntArray1052;
	private FileArchive titleStreamLoader;
	private int anInt1055;
	public NodeDeque incompleteAnimables;
	private final int[] anIntArray1057;
	public final RSInterface aClass9_1059;

	private static int anInt1061;
	private final int barFillColor;
	private int friendsListAction;
	private final int[] myAppearance;
	private int mouseInvInterfaceIndex;
	private int lastActiveInvInterface;

	public int currentRegionX;
	public int currentRegionY;
	private int mapIconAmount;
	private int[] minimapHintX;
	private IndexedImage[] mapSceneSprites;
	private int[] minimapHintY;
	private Sprite mapDotItem;
	private Sprite mapDotNPC;
	private Sprite mapDotPlayer;
	private Sprite mapDotFriend;
	private Sprite mapDotTeam;
	private Sprite mapDotClan;
	private int anInt1079;

	private String[] friendsList;
	private Buffer inStream;
	private int draggingItemInterfaceId;
	private int itemDraggingSlot;
	private int activeInterfaceType;
	private int anInt1087;
	private int anInt1088;
	public static int anInt1089;
	private final int[] expectedCRCs;
	public static String[] menuTargets;
	public static String[] menuActions;
	public static int[] menuIdentifiers;
	public static boolean[] menuShiftClick;
	public static int[] menuArguments1;
	public static int[] menuArguments2;
	public static int[] menuOpcodes;
	private Sprite[] headIcons;
	private Sprite[] skullIcons;
	private Sprite[] headIconsHint;

	public static boolean tabAreaAltered;

	private int anInt1104;

	private static int anInt1117;
	private int membersInt;
	private String aString1121;
	private String enterInputInChatString;
	public static Player localPlayer;
	public boolean broadcastActive;
	public long broadcastTimer;
	public String broadcastMessage;
	private final String[] playerOptions;
	private final boolean[] playerOptionsHighPriority;
	private final int[][][] constructRegionData;
	public final static int[] tabInterfaceIDs = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	private int cameraOffsetY;
	public int menuOptionsCount;
	private static int anInt1134;
	public int spellSelected;
	private int anInt1137;
	private int spellUsableOn;
	private String spellTooltip;
	private Sprite[] minimapHint;
	private boolean inTutorialIsland;
	private static int anInt1142;
	private int energy;
	private boolean continuedDialogue;
	private Sprite[] crosses;
	private boolean musicEnabled;
	private IndexedImage[] aBackgroundArray1152s;
	public static boolean needDrawTabArea;
	private int unreadMessages;
	private static int anInt1155;
	private static boolean fpsOn;
	private static boolean drawGrid;
	public static boolean loggedIn;
	private boolean canMute;
	private boolean isInInstance;
	public boolean isCameraLocked;
	public static int loopCycle;

	private int daysSinceRecovChange;
	private RSSocket socketStream;
	public static int minimapZoom;
	private static int anInt1175;
	private boolean genericLoadingError;
	private final int[] objectTypes = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	private int reportAbuseInterfaceID;
	private NodeDeque spawns;
	private int[] anIntArray1180;
	private int[] anIntArray1181;
	private int[] anIntArray1182;
	private byte[][] regionLandArchives;
	public int camAngleX;

	public int camAngleDY;
	public int camAngleDX;
	private static int anInt1188;
	private int invOverlayInterfaceID;
	private int[] anIntArray1190;
	private int[] anIntArray1191;
	public static Buffer stream;
	private int anInt1193;
	private boolean splitPrivateChat = true;
	private String[] clanList = new String[100];

	private final int[] quakeAmplitude;
	public static final int[] SHIRT_SECONDARY_COLORS = {9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145,
			58654, 5027, 1457, 16565, 34991, 25486};
	private static boolean flagged;
	private int anInt1208;
	public int camAngleY;
	public static int chatAreaScrollLength;
	private String promptInput;
	private int mouseClickCount;
	public int[][][] tileHeights;
	private long aLong1215;

	public final Sprite[] modIconss;
	private long aLong1220;
	public static int tabID;
	public int hintArrowNpcIndex;
	public static boolean inputTaken;
	public static int inputDialogState;
	private static int anInt1226;
	private int nextSong;
	private boolean songChanging;
	private final int[] anIntArray1229;
	public CollisionMap[] collisionMaps;
	public static int anIntArray1232[];
	private int[] regions;
	private int[] regionLandIds;
	private int[] regionLocIds;
	private int anInt1237;
	private int anInt1238;
	public final int anInt1239 = 100;
	private boolean aBoolean1242;
	private int atInventoryLoopCycle;
	private int atInventoryInterface;
	private int atInventoryIndex;
	private int atInventoryInterfaceType;
	private byte[][] regionMapArchives;
	private int tradeMode;
	private int gameMode;
	private int chatEffects;
	private int anInt1251;
	private final boolean rsAlreadyLoaded;
	private int anInt1253;
	private boolean welcomeScreenRaised;
	private boolean messagePromptRaised;
	public byte[][][] tileFlags;
	private int prevSong;
	private int destX;
	private int destY;
	private Sprite minimapImage;
	public Sprite backgroundFix;
	private int anInt1264;
	private int viewportDrawCount;
	private int localX;
	private int localY;
	public static int oculusOrbSlowedSpeed;
	public static int oculusOrbNormalSpeed;
	public static int camFollowHeight;
	public int oculusOrbState;
	private int anInt1275;
	public static int backDialogID;
	private int cameraOffsetX;
	private int[] bigX;
	private int[] bigY;
	public int itemSelected;
	private int anInt1283;
	private int anInt1284;
	private int anInt1285;
	private String selectedItemName;
	private int publicChatMode;
	private static int anInt1288;
	public static int anInt1290;
	public static String server = Configuration.DEDICATED_SERVER_ADDRESS;
	public static int port = Configuration.PORT;
	public static boolean controlIsDown;
	public int drawCount;
	public int fullscreenInterfaceID;
	public int anInt1044;// 377
	public int anInt1129;// 377
	public int anInt1315;// 377
	public int anInt1500;// 377
	public int anInt1501;// 377
	public int[] fullScreenTextureArray;

	public String myUsername = "";
	public String myPassword;

	private static final Object CAPTCHA_LOCK = new Object();
	private String captchaInput;

	public void setPassword(String password) {
		myPassword = password;
	}

	public String getPassword() {
		return myPassword;
	}

	public void launchURL(String url) {
		if (server.equals("173.185.70.167")) {
			javax.swing.JOptionPane.showMessageDialog(this, "Staff just tried to direct you to: " + url);
			return;
		}
		if (!url.toLowerCase().startsWith("http")) {
			url = "http://" + url;
		}
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				@SuppressWarnings("rawtypes")
				Class fileMgr = Class.forName("com.apple.eio.FileManager");
				@SuppressWarnings("unchecked")
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows"))
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			else { // assume Unix or Linux
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					Desktop.getDesktop().browse(new URI(url));
				}
			}
		} catch (Exception e) {
			pushMessage("Failed to open URL.", 0, "");
			System.err.println("Failed to launcher url on operating system " + osName);
			e.printStackTrace();
		}
	}
	private void drawInputField(RSInterface child, int xPosition, int yPosition, int width, int height) {
		int clickX = MouseHandler.saveClickX, clickY = MouseHandler.saveClickY;
		Sprite[] inputSprites = this.inputSprites;
		int xModification = 0, yModification = 0;
		for (int row = 0; row < width; row += 12) {
			if (row + 12 > width)
				row -= 12 - (width - row);
			inputSprites[6].drawSprite(xModification <= 0 ? xPosition + row : xPosition + xModification, yPosition);
			for (int collumn = 0; collumn < height; collumn += 12) {
				if (collumn + 12 > height)
					collumn -= 12 - (height - collumn);
				inputSprites[6].drawSprite(xPosition + row,
						yModification <= 0 ? yPosition + collumn : yPosition + yModification);
			}
		}
		inputSprites[1].drawSprite(xPosition, yPosition);
		inputSprites[0].drawSprite(xPosition, yPosition + height - 8);
		inputSprites[2].drawSprite(xPosition + width - 4, yPosition);
		inputSprites[3].drawSprite(xPosition + width - 4, yPosition + height - 8);
		xModification = 0;
		yModification = 0;
		for (int top = 0; top < width; top += 8) {
			if (top + 8 > width)
				top -= 8 - (width - top);
			inputSprites[5].drawSprite(xPosition + top, yPosition);
			inputSprites[5].drawSprite(xPosition + top, yPosition + height - 1);
		}
		for (int bottom = 0; bottom < height; bottom += 8) {
			if (bottom + 8 > height)
				bottom -= 8 - (height - bottom);
			inputSprites[4].drawSprite(xPosition, yPosition + bottom);
			inputSprites[4].drawSprite(xPosition + width - 1, yPosition + bottom);
		}
		String message = child.message;
		if (aTextDrawingArea_1271.getTextWidth(message) > child.width - 10)
			message = message.substring(message.length() - (child.width / 10) - 1, message.length());
		if (child.displayAsterisks)
			this.aTextDrawingArea_1271.method389(false, (xPosition + 4), child.textColor,
					new StringBuilder().append("").append(StringUtils.passwordAsterisks(message))
							.append(((!child.isInFocus ? 0 : 1) & (loopCycle % 40 < 20 ? 1 : 0)) != 0 ? "|" : "")
							.toString(),
					(yPosition + (height / 2) + 6));
		else
			this.aTextDrawingArea_1271.method389(false, (xPosition + 4), child.textColor,
					new StringBuilder().append("").append(message)
							.append(((!child.isInFocus ? 0 : 1) & (loopCycle % 40 < 20 ? 1 : 0)) != 0 ? "|" : "")
							.toString(),
					(yPosition + (height / 2) + 6));


		boolean clicked = false;
		if (drawingTabArea && !isResized()) {
			int x = 516;
			int y = 168;
			clicked = clickX >= xPosition + x && clickX <= xPosition + x + child.width && clickY >= yPosition + y && clickY <= yPosition + y + child.height;
		} else {
			clicked = clickX >= xPosition && clickX <= xPosition + child.width && clickY >= yPosition && clickY <= yPosition + child.height;
		}

		if (clicked && !child.isInFocus && getInputFieldFocusOwner() != child) {
			if ((MouseHandler.instance.clickMode2 == 1 && !isMenuOpen)) {
				RSInterface.currentInputFieldId = child.id;
				setInputFieldFocusOwner(child);
				if (child.message != null && child.message.equals(child.defaultInputFieldText))
					child.message = "";
				if (child.message == null)
					child.message = "";
			}
		}
	}
	public void setInputFieldFocusOwner(RSInterface owner) {
		for (RSInterface rsi : RSInterface.interfaceCache)
			if (rsi != null)
				if (rsi == owner)
					rsi.isInFocus = true;
				else
					rsi.isInFocus = false;
	}

	public RSInterface getInputFieldFocusOwner() {
		for (RSInterface rsi : RSInterface.interfaceCache)
			if (rsi != null)
				if (rsi.isInFocus)
					return rsi;
		return null;
	}

	public void resetInputFieldFocus() {
		for (RSInterface rsi : RSInterface.interfaceCache)
			if (rsi != null)
				rsi.isInFocus = false;
		RSInterface.currentInputFieldId = -1;
	}

	public boolean isFieldInFocus() {
		if (openInterfaceID == -1 && invOverlayInterfaceID <= 0) {
			return false;
		}
		for (RSInterface rsi : RSInterface.interfaceCache) {
			if (rsi != null) {
				if (rsi.type == 16 && rsi.isInFocus) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean scrollbarVisible(RSInterface widget) {
		if (widget.id == 55010) {
			if (RSInterface.interfaceCache[55024].message.length() <= 0) {
				return false;
			}
		} else if (widget.id == 55050) {
			if (RSInterface.interfaceCache[55064].message.length() <= 0) {
				return false;
			}
		}
		return true;
	}

	public void mouseWheelDragged(int i, int j) {
		this.camAngleDY += i * 3;
		this.camAngleDX += (j << 1);
	}

	public static int anInt1401 = 256;
	private int musicVolume = 255;
	public static long aLong1432;
	private final int[] sound;
	public int soundCount;
	private final int[] soundDelay;
	private final int[] soundType;
	private static int soundEffectVolume = 127;
	public static int[] anIntArray385 = new int[] { 12800, 12800, 12800, 12800, 12800, 12800, 12800, 12800, 12800,
			12800, 12800, 12800, 12800, 12800, 12800, 12800 };
	public static boolean LOOP_MUSIC = false;

	public static final void sleep(long time) {
		if (time > 0L) {
			if (time % 10L != 0L) {
				threadSleep(time);
			} else {
				threadSleep(time - 1L);
				threadSleep(1L);
			}
		}
	}

	private static final void threadSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
		}
	}

	static {
		SKILL_EXPERIENCE = new int[99];
		int i = 0;
		for (int j = 0; j < 99; j++) {
			int l = j + 1;
			int i1 = (int) (l + 300D * Math.pow(2D, l / 7D));
			i += i1;
			SKILL_EXPERIENCE[j] = i / 4;
		}
		anIntArray1232 = new int[32];
		i = 2;
		for (int k = 0; k < 32; k++) {
			anIntArray1232[k] = i - 1;
			i += i;
		}
	}

	public static String md5Hash(String md5) {
		try {

			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}

			return sb.toString();

		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public int getLevelForXPSlayer(int exp) {
		int points = 0;
		int output;
		if (exp > 104273167)
			return 120;
		for (int lvl = 1; lvl <= 120; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public void updateSkillStrings(int i) {
		switch (i) {
			case 0:
				sendFrame126("" + currentLevels[0] + "", 4004);
				sendFrame126("" + getLevelForXP(currentExp[0]) + "", 4005);
				sendFrame126("" + currentExp[0] + "", 4044);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[0]) + 1) + "", 4045);
				break;

			case 1:
				sendFrame126("" + currentLevels[1] + "", 4008);
				sendFrame126("" + getLevelForXP(currentExp[1]) + "", 4009);
				sendFrame126("" + currentExp[1] + "", 4056);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[1]) + 1) + "", 4057);
				break;

			case 2:
				sendFrame126("" + currentLevels[2] + "", 4006);
				sendFrame126("" + getLevelForXP(currentExp[2]) + "", 4007);
				sendFrame126("" + currentExp[2] + "", 4050);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[2]) + 1) + "", 4051);
				break;

			case 3:
				sendFrame126("" + currentLevels[3] + "", 4016);
				sendFrame126("" + getLevelForXP(currentExp[3]) + "", 4017);
				sendFrame126("" + currentExp[3] + "", 4080);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[3]) + 1) + "", 4081);
				break;

			case 4:
				sendFrame126("" + currentLevels[4] + "", 4010);
				sendFrame126("" + getLevelForXP(currentExp[4]) + "", 4011);
				sendFrame126("" + currentExp[4] + "", 4062);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[4]) + 1) + "", 4063);
				break;

			case 5:
				sendFrame126("" + currentLevels[5] + "", 4012);
				sendFrame126("" + getLevelForXP(currentExp[5]) + "", 4013);
				sendFrame126("" + currentExp[5] + "", 4068);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[5]) + 1) + "", 4069);
				sendFrame126("" + currentLevels[5] + "/" + getLevelForXP(currentExp[5]) + "", 687);// Prayer
				// frame
				break;

			case 6:
				sendFrame126("" + currentLevels[6] + "", 4014);
				sendFrame126("" + getLevelForXP(currentExp[6]) + "", 4015);
				sendFrame126("" + currentExp[6] + "", 4074);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[6]) + 1) + "", 4075);
				break;

			case 7:
				sendFrame126("" + currentLevels[7] + "", 4034);
				sendFrame126("" + getLevelForXP(currentExp[7]) + "", 4035);
				sendFrame126("" + currentExp[7] + "", 4134);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[7]) + 1) + "", 4135);
				break;

			case 8:
				sendFrame126("" + currentLevels[8] + "", 4038);
				sendFrame126("" + getLevelForXP(currentExp[8]) + "", 4039);
				sendFrame126("" + currentExp[8] + "", 4146);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[8]) + 1) + "", 4147);
				break;

			case 9:
				sendFrame126("" + currentLevels[9] + "", 4026);
				sendFrame126("" + getLevelForXP(currentExp[9]) + "", 4027);
				sendFrame126("" + currentExp[9] + "", 4110);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[9]) + 1) + "", 4111);
				break;

			case 10:
				sendFrame126("" + currentLevels[10] + "", 4032);
				sendFrame126("" + getLevelForXP(currentExp[10]) + "", 4033);
				sendFrame126("" + currentExp[10] + "", 4128);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[10]) + 1) + "", 4129);
				break;

			case 11:
				sendFrame126("" + currentLevels[11] + "", 4036);
				sendFrame126("" + getLevelForXP(currentExp[11]) + "", 4037);
				sendFrame126("" + currentExp[11] + "", 4140);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[11]) + 1) + "", 4141);
				break;

			case 12:
				sendFrame126("" + currentLevels[12] + "", 4024);
				sendFrame126("" + getLevelForXP(currentExp[12]) + "", 4025);
				sendFrame126("" + currentExp[12] + "", 4104);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[12]) + 1) + "", 4105);
				break;

			case 13:
				sendFrame126("" + currentLevels[13] + "", 4030);
				sendFrame126("" + getLevelForXP(currentExp[13]) + "", 4031);
				sendFrame126("" + currentExp[13] + "", 4122);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[13]) + 1) + "", 4123);
				break;

			case 14:
				sendFrame126("" + currentLevels[14] + "", 4028);
				sendFrame126("" + getLevelForXP(currentExp[14]) + "", 4029);
				sendFrame126("" + currentExp[14] + "", 4116);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[14]) + 1) + "", 4117);
				break;

			case 15:
				sendFrame126("" + currentLevels[15] + "", 4020);
				sendFrame126("" + getLevelForXP(currentExp[15]) + "", 4021);
				sendFrame126("" + currentExp[15] + "", 4092);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[15]) + 1) + "", 4093);
				break;

			case 16:
				sendFrame126("" + currentLevels[16] + "", 4018);
				sendFrame126("" + getLevelForXP(currentExp[16]) + "", 4019);
				sendFrame126("" + currentExp[16] + "", 4086);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[16]) + 1) + "", 4087);
				break;

			case 17:
				sendFrame126("" + currentLevels[17] + "", 4022);
				sendFrame126("" + getLevelForXP(currentExp[17]) + "", 4023);
				sendFrame126("" + currentExp[17] + "", 4098);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[17]) + 1) + "", 4099);
				break;

			case 18:
				sendFrame126("" + currentLevels[18] + "", 12166);
				sendFrame126("" + getLevelForXPSlayer(currentExp[18]) + "", 12167);
				sendFrame126("" + currentExp[18] + "", 12171);
				sendFrame126("" + getXPForLevel(getLevelForXPSlayer(currentExp[18]) + 1) + "", 12172);
				break;

			case 19:
				sendFrame126("" + currentLevels[19] + "", 13926);
				sendFrame126("" + getLevelForXP(currentExp[19]) + "", 13927);
				sendFrame126("" + currentExp[19] + "", 13921);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[19]) + 1) + "", 13922);
				break;

			case 20: // runecraft
				sendFrame126("" + currentLevels[20] + "", 4152);
				sendFrame126("" + getLevelForXP(currentExp[20]) + "", 4153);
				sendFrame126("" + currentExp[20] + "", 4157);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[20]) + 1) + "", 4159);
				break;

			// hunter is 21
		}
	}


	/**
	 * Runelite
	 */
	public DrawCallbacks drawCallbacks;
	@javax.inject.Inject
	private Callbacks callbacks;

	private int gpuFlags;

	@Override
	public Callbacks getCallbacks() {
		return callbacks;
	}

	@Override
	public DrawCallbacks getDrawCallbacks() {
		return drawCallbacks;
	}

	@Override
	public void setDrawCallbacks(DrawCallbacks drawCallbacks) {
		this.drawCallbacks = drawCallbacks;
	}

	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public String getBuildID() {
		return "1";
	}

	@Override
	public List<net.runelite.api.Player> getPlayers() {
		return Arrays.asList(players);
	}

	@Override
	public List<NPC> getNpcs() {
		List<NPC> npcs = new ArrayList<NPC>(npcCount);
		for (int i = 0; i < npcCount; ++i)
		{
			npcs.add(this.npcs[npcIndices[i]]);
		}
		return npcs;
	}

	@Override
	public RSNPC[] getCachedNPCs() {
		return npcs;
	}

	@Override
	public RSPlayer[] getCachedPlayers() {
		return players;
	}

	@Override
	public int getLocalInteractingIndex() {
		return 0;
	}

	@Override
	public void setLocalInteractingIndex(int idx) {

	}

	@Override
	public RSNodeDeque getTilesDeque() {
		return null;
	}

	@Override
	public RSNodeDeque[][][] getGroundItemDeque() {
		return groundItems;
	}

	@Override
	public RSNodeDeque getProjectilesDeque() {
		return null;
	}

	@Override
	public RSNodeDeque getGraphicsObjectDeque() {
		return null;
	}

	@Override
	public String getUsername() {
		return myUsername;
	}

	@Override
	public int getBoostedSkillLevel(Skill skill) {
		int[] boostedLevels = getBoostedSkillLevels();
		return boostedLevels[skill.ordinal()];
	}

	@Override
	public int getRealSkillLevel(Skill skill) {
		return maximumLevels[skill.ordinal()];
	}

	@Override
	public int getTotalLevel() {
		int totalLevel = 0;

		int[] realLevels = getRealSkillLevels();
		int lastSkillIdx = Skill.CONSTRUCTION.ordinal();

		for (int i = 0; i < realLevels.length; i++)
		{
			if (i <= lastSkillIdx)
			{
				totalLevel += realLevels[i];
			}
		}

		return totalLevel;
	}

	@Override
	public MessageNode addChatMessage(ChatMessageType type, String name, String message, String sender) {
		return null;
	}

	@Override
	public MessageNode addChatMessage(ChatMessageType type, String name, String message, String sender,
									  boolean postEvent) {
		return null;
	}
	public int gameState = -1;
	@Override
	public GameState getGameState() {
		return GameState.of(gameState);
	}

	@Override
	public CinematicState getCinematicState() {
		return null;
	}

	@Override
	public int getRSGameState() {
		return gameState;
	}

	@Override
	public void setRSGameState(int state) {
		if (state != gameState) {

			//jagexNetThread.writePacket(true);

			if (gameState == 25) {
				Client.loadingType = 0;
				Client.mapsLoaded = 0;
				Client.totalMaps = 1;
				Client.objectsLoaded = 0;
				Client.totalObjects = 1;
			}

			if (state != 5 && state != 10) { // L: 1270

				if (state == 20) { // L: 1278
					setupLoginScreen();
				} else if (state == 11) { // L: 1282
					setupLoginScreen();
				} else if (state == 50) { // L: 1285
					setupLoginScreen();
				} else if (clearLoginScreen) { // L: 1290
					jagexNetThread.writePacket(true);
					StaticSound.clear();
					StaticSound.midiPcmStream.clearAll(); // L: 1146
					StaticSound.midiPcmStream.setPcmStreamVolume(0);
					clearLoginScreen = false;
				}
			} else {
				setupLoginScreen();
			}

			gameStateChanged(0);
			gameState = state;

		}
	}

	public static boolean clearLoginScreen;

	public static void setupLoginScreen() {
		StaticSound.update();

	}


	public static void gameStateChanged(int idx) {
		GameState gameState = Client.instance.getGameState();
		Client.instance.getLogger().debug("Game state changed: {}", gameState);
		GameStateChanged gameStateChange = new GameStateChanged();
		gameStateChange.setGameState(gameState);
		Client.instance.getCallbacks().post(gameStateChange);

		if (gameState == GameState.LOGGED_IN) {
			if (Client.instance.getLocalPlayer() == null) {
				return;
			}

		} else if (gameState == GameState.LOGIN_SCREEN) {
			//Laod Varbits
		}
	}

	@Override
	public void setCheckClick(boolean checkClick) {
		scene.clicked = checkClick;
	}

	@Override
	public void setMouseCanvasHoverPositionX(int x) {
		MouseHandler.mouseX = x;
	}

	@Override
	public void setMouseCanvasHoverPositionY(int y) {
		MouseHandler.mouseY = y;
	}

	@Override
	public void setGameState(GameState state) {
		gameState = state.getState();
		GameStateChanged event = new GameStateChanged();
		event.setGameState(state);
		if(callbacks != null) {
			callbacks.post(event);
		}


	}

	@Override
	public void setCinematicState(CinematicState gameState) {

	}

	@Override
	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	@Override
	public void stopNow() {
	}

	@Override
	public void setUsername(String name) {
		myUsername = name;
	}
	@Override
	public void setOtp(String otp) {

	}

	@Override
	public int getCurrentLoginField() {
		return 0;
	}

	@Override
	public int getLoginIndex() {
		return 0;
	}

	@Override
	public AccountType getAccountType() {
		return AccountType.NORMAL;
	}

	@Override
	public int getFPS() {
		return fps;
	}

	@Override
	public int getCameraX() {
		return this.cameraX;
	}

	@Override
	public int getCameraY() {
		return this.cameraZ;
	}

	@Override
	public int getCameraZ() {
		return this.cameraY;
	}

	@Override
	public int getCameraPitch() {
		return cameraPitch;
	}

	@Override
	public void setCameraPitch(int cameraPitch) {
		this.cameraPitch = cameraPitch;
	}

	@Override
	public int getCameraYaw() {
		return cameraYaw;
	}

	@Override
	public int getWorld() {
		return 1;
	}

	@Override
	public int getCanvasHeight() {
		return canvasHeight;
	}

	@Override
	public int getCanvasWidth() {
		return canvasWidth;
	}

	@Override
	public int getViewportHeight() {
		return viewportHeight;
	}

	@Override
	public int getViewportWidth() {
		return viewportWidth;

	}

	@Override
	public int getViewportXOffset() {
		return viewportOffsetX;
	}

	@Override
	public int getViewportYOffset() {
		return viewportOffsetY;
	}

	@Override
	public net.runelite.api.Point getMouseCanvasPosition() {
		return new net.runelite.api.Point(MouseHandler.mouseX, MouseHandler.mouseY);
	}

	@Override
	public int[][][] getTileHeights() {
		return tileHeights;
	}

	@Override
	public byte[][][] getTileSettings() {
		return tileFlags;
	}

	@Override
	public int getPlane() {
		return plane;
	}

	@Override
	public SceneGraph getScene() {
		return scene;
	}

	@Override
	public RSPlayer getLocalPlayer() {
		return localPlayer;
	}

	@Override
	public int getLocalPlayerIndex() {
		return 0;
	}

	@Override
	public int getNpcIndexesCount() {
		return 0;
	}

	@Override
	public int[] getNpcIndices() {
		return new int[0];
	}

	@Override
	public ItemComposition getItemComposition(int id) {
		return ItemDefinition.lookup(id);
	}

	@Override
	public ItemComposition getItemDefinition(int id) {
		return ItemDefinition.lookup(id);
	}

	@Override
	public RSSpritePixels[] getSprites(IndexDataBase source, int archiveId, int fileId)
	{
		RSAbstractArchive rsSource = (RSAbstractArchive) source;
		byte[] configData = rsSource.getConfigData(archiveId, fileId);
		if (configData == null)
		{
			return null;
		}

		decodeSprite(configData);

		int indexedSpriteCount = getIndexedSpriteCount();
		int maxWidth = getIndexedSpriteWidth();
		int maxHeight = getIndexedSpriteHeight();
		int[] offsetX = getIndexedSpriteOffsetXs();
		int[] offsetY = getIndexedSpriteOffsetYs();
		int[] widths = getIndexedSpriteWidths();
		int[] heights = getIndexedSpriteHeights();
		byte[][] spritePixelsArray = getSpritePixels();
		int[] indexedSpritePalette = getIndexedSpritePalette();

		RSSpritePixels[] array = new RSSpritePixels[indexedSpriteCount];

		for (int i = 0; i < indexedSpriteCount; ++i)
		{
			int width = widths[i];
			int height = heights[i];

			byte[] pixelArray = spritePixelsArray[i];
			int[] pixels = new int[width * height];

			RSSpritePixels spritePixels = createSpritePixels(pixels, width, height);
			spritePixels.setMaxHeight(maxHeight);
			spritePixels.setMaxWidth(maxWidth);
			spritePixels.setOffsetX(offsetX[i]);
			spritePixels.setOffsetY(offsetY[i]);

			for (int j = 0; j < width * height; ++j)
			{
				pixels[j] = indexedSpritePalette[pixelArray[j] & 0xff];
			}

			array[i] = spritePixels;
		}

		setIndexedSpriteOffsetXs(null);
		setIndexedSpriteOffsetYs(null);
		setIndexedSpriteWidths(null);
		setIndexedSpriteHeights(null);
		setIndexedSpritePalette(null);
		setSpritePixels(null);

		return array;
	}


	@Override
	public RSArchive getIndexSprites() {
		return Js5List.sprites;
	}

	@Override
	public RSArchive getIndexScripts() {
		return null;
	}

	@Override
	public RSArchive getIndexConfig() {
		return null;
	}

	@Override
	public RSArchive getMusicTracks() {
		return null;
	}

	@Override
	public int getBaseX() {
		return baseX;
	}

	@Override
	public int getBaseY() {
		return baseY;
	}

	@Override
	public int getMouseCurrentButton() {
		return 0;
	}

	@Override
	public int getSelectedSceneTileX() {
		return scene.clickedTileX;
	}

	@Override
	public void setSelectedSceneTileX(int selectedSceneTileX) {
		scene.clickedTileX = selectedSceneTileX;
	}

	@Override
	public int getSelectedSceneTileY() {
		return scene.clickedTileY;
	}

	@Override
	public void setSelectedSceneTileY(int selectedSceneTileY) {
		scene.clickedTileY = selectedSceneTileY;
	}

	@Override
	public Tile getSelectedSceneTile() {
		int tileX = SceneGraph.hoverX;
		int tileY = SceneGraph.hoverY;

		if (tileX == -1 || tileY == -1)
		{
			return null;
		}

		return getScene().getTiles()[getPlane()][tileX][tileY];

	}

	@Override
	public boolean isDraggingWidget() {
		return false;
	}

	@Override
	public RSWidget getDraggedWidget() {
		return null;
	}

	@Override
	public RSWidget getDraggedOnWidget() {
		return null;
	}

	@Override
	public void setDraggedOnWidget(net.runelite.api.widgets.Widget widget) {
	}

	@Override
	public RSWidget[][] getWidgets() {
		return new RSWidget[0][];
	}

	@Override
	public RSWidget[] getGroup(int groupId) {
		return new RSWidget[0];
	}

	@Override
	public int getTopLevelInterfaceId() {
		return openInterfaceID;
	}

	@Override
	public RSWidget[] getWidgetRoots() {
		return null;
	}

	@Override
	public RSWidget getWidget(WidgetInfo widget) {
		int groupId = widget.getGroupId();
		int childId = widget.getChildId();

		return getWidget(groupId, childId);
	}

	@Override
	public RSWidget getWidget(int groupId, int childId) {
		return null;
	}

	@Override
	public RSWidget getWidget(int packedID) {
		return null;
	}

	@Override
	public int[] getWidgetPositionsX() {
		return null;
	}

	@Override
	public int[] getWidgetPositionsY() {
		return null;
	}

	@Override
	public boolean isMouseCam() {
		return Configuration.MOUSE_CAM;
	}

	@Override
	public int getCamAngleDX() {
		return camAngleDX;
	}

	@Override
	public void setCamAngleDX(int angle) {
		camAngleDX = angle;
	}

	@Override
	public int getCamAngleDY() {
		return camAngleDY;
	}

	@Override
	public void setCamAngleDY(int angle) {
		camAngleDY = angle;
	}

	@Override
	public RSWidget createWidget() {
		return null;
	}

	@Override
	public void revalidateWidget(net.runelite.api.widgets.Widget w) {

	}

	@Override
	public void revalidateWidgetScroll(net.runelite.api.widgets.Widget[] group, net.runelite.api.widgets.Widget w, boolean postEvent) {

	}

	@Override
	public int getEntitiesAtMouseCount() {
		return ViewportMouse.entityCount;
	}

	@Override
	public void setEntitiesAtMouseCount(int i) {
		ViewportMouse.entityCount = i;
	}

	@Override
	public long[] getEntitiesAtMouse() {
		return ViewportMouse.entityTags;
	}

	@Override
	public int getViewportMouseX() {
		return ViewportMouse.viewportMouseX;
	}

	@Override
	public int getViewportMouseY() {
		return ViewportMouse.viewportMouseY;
	}

	@Override
	public int getEnergy() {
		return Integer.parseInt(RSInterface.interfaceCache[22539].message.replaceAll("%", ""));
	}

	@Override
	public int getWeight() {
		return 0;
	}

	@Override
	public String[] getPlayerOptions() {
		return null;
	}

	@Override
	public boolean[] getPlayerOptionsPriorities() {
		return null;
	}

	@Override
	public int[] getPlayerMenuTypes() {
		return null;
	}

	@Override
	public int getMouseX() {
		return MouseHandler.mouseX;
	}

	@Override
	public int getMouseY() {
		return MouseHandler.mouseY;
	}

	@Override
	public int getMouseX2() {
		return scene.clickScreenX;
	}

	@Override
	public int getMouseY2() {
		return scene.clickScreenY;
	}

	@Override
	public boolean containsBounds(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		return scene.inBounds(var0, var1, var2, var3, var4, var5, var6, var7);
	}

	@Override
	public boolean isCheckClick() {
		return SceneGraph.clicked;
	}

	@Override
	public RSWorld[] getWorldList() {
		return null;
	}

	@Override
	public void addRSChatMessage(int type, String name, String message, String sender) {

	}

	@Override
	public RSObjectComposition getRSObjectComposition(int objectId) {
		return null;
	}

	@Override
	public RSNPCComposition getRSNpcComposition(int npcId) {
		return null;
	}



	@Override
	public net.runelite.rs.api.RSFont getFontBold12() {
		return boldFontOSRS;
	}


	@Override
	public void rasterizerDrawHorizontalLine(int x, int y, int w, int rgb) {
		Rasterizer2D.drawHorizontalLine(x, y, w, rgb);
	}

	@Override
	public void rasterizerDrawHorizontalLineAlpha(int x, int y, int w, int rgb, int a) {
		Rasterizer2D.drawTransparentHorizontalLine(x, y, w, rgb, a);
	}

	@Override
	public void rasterizerDrawVerticalLine(int x, int y, int h, int rgb) {
		Rasterizer2D.drawVerticalLine(x, y, h, rgb);
	}

	@Override
	public void rasterizerDrawVerticalLineAlpha(int x, int y, int h, int rgb, int a) {
		Rasterizer2D.drawTransparentVerticalLine(x, y, h, rgb, a);
	}


	@Override
	public void rasterizerDrawGradient(int x, int y, int w, int h, int rgbTop, int rgbBottom) {
		Rasterizer2D.drawTransparentGradientBox(x, y, w, h, rgbTop, rgbBottom, 255);
	}

	@Override
	public void rasterizerDrawGradientAlpha(int x, int y, int w, int h, int rgbTop, int rgbBottom, int alphaTop, int alphaBottom) {
		Rasterizer2D.rasterizerDrawGradientAlpha(x, y, w, h, rgbTop, rgbBottom, alphaTop, alphaBottom);
	}
	@Override
	public void rasterizerFillRectangleAlpha(int x, int y, int w, int h, int rgb, int a) {
		Rasterizer2D.drawTransparentBox(x,y,w,h,rgb,a);
	}

	@Override
	public void rasterizerDrawRectangle(int x, int y, int w, int h, int rgb) {
		Rasterizer2D.drawBoxOutline(x, y, w, h, rgb);
	}

	@Override
	public void rasterizerDrawRectangleAlpha(int x, int y, int w, int h, int rgb, int a) {
		Rasterizer2D.drawTransparentBoxOutline(x, y, w, h, rgb, a);
	}


	@Override
	public void rasterizerDrawCircle(int x, int y, int r, int rgb) {

	}

	@Override
	public void rasterizerDrawCircleAlpha(int x, int y, int r, int rgb, int a) {

	}

	@Override
	public RSEvictingDualNodeHashTable getHealthBarCache() {
		return HealthBarDefinition.cached;
	}

	@Override
	public RSEvictingDualNodeHashTable getHealthBarSpriteCache() {
		return HealthBarDefinition.cachedSprites;
	}

	@Override
	public int getMapAngle() {
		return camAngleY;
	}

	@Override
	public void setCameraYawTarget(int cameraYawTarget) {
		camAngleY = cameraYawTarget;
	}

	@Setter
	private boolean resized = false;

	@Override
	public boolean isResized() {
		return resized;
	}

	@Override
	public int getRevision() {
		return 1;
	}

	@Override
	public int[] getMapRegions() {
		return regions;
	}

	@Override
	public int[][][] getInstanceTemplateChunks() {
		return constructRegionData;
	}

	@Override
	public int[][] getXteaKeys() {
		return null;
	}

	@Override
	public int getCycleCntr() {
		return 0;
	}

	@Override
	public void setChatCycle(int value) {

	}

	@Override
	public int[] getVarps() {
		return settings;
	}

	@Override
	public RSVarcs getVarcs() {
		return null;
	}

	@Override
	public Map<Integer, Object> getVarcMap() {
		return null;
	}

	@Override
	public int getVar(VarPlayer varPlayer) {
		return 0;
	}

	@Override
	public int getVar(Varbits varbit) {
		return 0;
	}

	@Override
	public int getVar(VarClientInt varClientInt) {
		return 0;
	}

	@Override
	public String getVar(VarClientStr varClientStr) {
		return null;
	}

	@Override
	public int getVarbitValue(int varbitId) {
		return 0;
	}

	@Override
	public int getVarcIntValue(int varcIntId) {
		return 0;
	}

	@Override
	public String getVarcStrValue(int varcStrId) {
		return null;
	}

	@Override
	public void setVar(VarClientStr varClientStr, String value) {
	}

	@Override
	public void setVar(VarClientInt varClientStr, int value) {
	}

	@Override
	public void setVarbit(Varbits varbit, int value) {
	}

	@Override
	public VarbitComposition getVarbit(int id) {
		return null;
	}

	@Override
	public int getVarbitValue(int[] varps, int varbitId) {
		return 0;
	}

	@Override
	public int getVarpValue(int[] varps, int varpId) {
		return 0;
	}

	@Override
	public int getVarpValue(int i) {
		return 0;
	}

	@Override
	public void setVarbitValue(int[] varps, int varbit, int value) {
	}

	@Override
	public void queueChangedVarp(int varp) {
	}

	@Override
	public RSNodeHashTable getWidgetFlags() {
		return null;
	}

	@Override
	public RSNodeHashTable getComponentTable() {
		return null;
	}

	@Override
	public RSGrandExchangeOffer[] getGrandExchangeOffers() {
		return null;
	}

	@Override
	public boolean isPrayerActive(Prayer prayer) {
		return false;
	}

	@Override
	public int getSkillExperience(Skill skill) {

		int[] experiences = getSkillExperiences();

		if (skill == Skill.OVERALL)
		{
			logger.debug("getSkillExperience called for {}!", skill);
			return (int) getOverallExperience();
		}

		int idx = skill.ordinal();

		// I'm not certain exactly how needed this is, but if the Skill enum is updated in the future
		// to hold something else that's not reported it'll save us from an ArrayIndexOutOfBoundsException.
		if (idx >= experiences.length)
		{
			return -1;
		}

		return experiences[idx];
	}

	@Override
	public long getOverallExperience() {
		return IntStream.of(currentExp).sum();
	}

	@Override
	public void refreshChat() {
	}

	@Override
	public Map<Integer, ChatLineBuffer> getChatLineMap() {
		return null;
	}

	@Override
	public RSIterableNodeHashTable getMessages() {
		return null;
	}

	@Override
	public ObjectComposition getObjectDefinition(int objectId) {
		return ObjectDefinition.lookup(objectId);
	}

	@Override
	public NPCComposition getNpcDefinition(int npcId) {
		return NpcDefinition.get(npcId);
	}

	@Override
	public StructComposition getStructComposition(int structID) {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getStructCompositionCache() {
		return null;
	}

	@Override
	public RSWorldMapElement[] getMapElementConfigs() {
		return null;
	}

	@Override
	public RSIndexedSprite[] getMapScene() {
		return null;
	}

	public Sprite[] minimapDot = new Sprite[7];


	@Override
	public RSSpritePixels[] getMapDots() {
		return null;
	}

	@Override
	public int getGameCycle() {
		return loopCycle;
	}

	@Override
	public RSSpritePixels[] getMapIcons() {
		return null;
	}

	@Override
	public RSIndexedSprite[] getModIcons() {
		return null;
	}

	@Override
	public void setRSModIcons(RSIndexedSprite[] modIcons) {

	}

	@Override
	public void setModIcons(IndexedSprite[] modIcons) {

	}

	@Override
	public RSIndexedSprite createIndexedSprite() {
		return null;
	}

	@Override
	public RSSpritePixels createSpritePixels(int[] pixels, int width, int height) {
		return new Sprite(pixels,width,height);
	}

	@Override
	public int getDestinationX() {
		return destX;
	}

	@Override
	public int getDestinationY() {
		return destY;
	}

	@Override
	public RSSoundEffect[] getAudioEffects() {
		return new RSSoundEffect[0];
	}

	@Override
	public int[] getQueuedSoundEffectIDs() {
		return new int[0];
	}

	@Override
	public int[] getSoundLocations() {
		return new int[0];
	}

	@Override
	public int[] getQueuedSoundEffectLoops() {
		return new int[0];
	}

	@Override
	public int[] getQueuedSoundEffectDelays() {
		return new int[0];
	}

	@Override
	public int getQueuedSoundEffectCount() {
		return 0;
	}

	@Override
	public void setQueuedSoundEffectCount(int queuedSoundEffectCount) {

	}

	@Override
	public void queueSoundEffect(int id, int numLoops, int delay) {

	}

	@Override
	public LocalPoint getLocalDestinationLocation()
	{
		int sceneX = getDestinationX();
		int sceneY = getDestinationY();
		if (sceneX != 0 && sceneY != 0)
		{
			return LocalPoint.fromScene(sceneX, sceneY);
		}
		return null;
	}

	@Override
	public List<net.runelite.api.Projectile> getProjectiles() {
		List<net.runelite.api.Projectile> projectileList = new ArrayList<>();
		for (net.runelite.api.Projectile projectile = (net.runelite.api.Projectile) projectiles
				.reverseGetFirst(); projectile != null; projectile = (net.runelite.api.Projectile) projectiles.reverseGetNext()) {
			projectileList.add(projectile);
		}
		return projectileList;
	}

	@Override
	public List<GraphicsObject> getGraphicsObjects() {
		List<GraphicsObject> list = new ArrayList<>();
		for (GraphicsObject projectile = (GraphicsObject) incompleteAnimables
				.reverseGetFirst(); projectile != null; projectile = (GraphicsObject) incompleteAnimables.reverseGetNext()) {
			list.add(projectile);
		}
		return list;
	}

	@Override
	public RuneLiteObject createRuneLiteObject() {
		return new com.client.scene.object.RuneLiteObject();
	}

	RSEvictingDualNodeHashTable newEvictingDualNodeHashTable(int var1) {
		return new EvictingDualNodeHashTable(var1);
	}

	public RSEvictingDualNodeHashTable tmpModelDataCache = newEvictingDualNodeHashTable(16);

	@Nullable
	@Override
	public RSModelData loadModelData(int var0)
	{
		RSModelData modelData = (RSModelData) this.tmpModelDataCache.get(var0);

		if (modelData == null)
		{
			modelData = getModelData(Js5List.models, var0, 0);
			if (modelData == null)
			{
				return null;
			}

			this.tmpModelDataCache.put((RSDualNode) modelData, (long) var0);
		}

		return modelData.newModelData(modelData, true, true, true, true);
	}


	com.client.entity.model.Mesh newModelData(RSModelData[] var1, int var2) {
		return new com.client.entity.model.Mesh((com.client.entity.model.Mesh[]) var1,var2);
	}

	RSModelData[] getModelDataArray() {
		return ObjectDefinition.meshData;
	}

	@Override
	public RSModelData mergeModels(ModelData[] var0, int var1)
	{
		return newModelData(Arrays.copyOf(var0, var1, getModelDataArray().getClass()), var1);
	}




	@Override
	public RSModelData mergeModels(ModelData... var0)
	{
		return mergeModels(var0, var0.length);
	}

	@Override
	public RSModel loadModel(int id)
	{
		return loadModel(id, null, null);
	}

	public RSModel loadModel(int id, short[] colorToFind, short[] colorToReplace)
	{
		RSModelData modeldata = getModelData(getObjectDefinition_modelsArchive(), id, 0);

		if (colorToFind != null)
		{
			for (int i = 0; i < colorToFind.length; ++i)
			{
				modeldata.rs$recolor(colorToFind[i], colorToReplace[i]);
			}
		}

		return modeldata.toModel(modeldata.getAmbient() + 64, modeldata.getContrast() + 850, -30, -50, -30);
	}

	@Override
	public Animation loadAnimation(int id)
	{
		return getSequenceDefinition(id);
	}

	@Override
	public int getMusicVolume() {
		return 0;
	}

	@Override
	public void setMusicVolume(int volume) {
	}

	@Override
	public void playSoundEffect(int id) {

	}

	@Override
	public void playSoundEffect(int id, int x, int y, int range) {
	}

	@Override
	public void playSoundEffect(int id, int x, int y, int range, int delay) {
	}

	@Override
	public void playSoundEffect(int id, int volume) {

	}

	@Override
	public RSAbstractRasterProvider getBufferProvider() {
		return rasterProvider;
	}

	@Override
	public int getMouseIdleTicks() {
		return MouseHandler.idleCycles;
	}

	@Override
	public long getMouseLastPressedMillis() {
		return MouseHandler.lastPressed;
	}

	public long getClientMouseLastPressedMillis() {
		return MouseHandler.lastPressed;
	}

	public void setClientMouseLastPressedMillis(long mills) {
		MouseHandler.lastPressed = mills;
	}

	@Override
	public int getKeyboardIdleTicks() {
		return KeyHandler.idleCycles;
	}

	@Override
	public void changeMemoryMode(boolean lowMemory) {
		setLowMemory(lowMemory);
		setSceneLowMemory(lowMemory);
		setAudioHighMemory(true);
		setObjectDefinitionLowDetail(lowMemory);
		if (getGameState() == GameState.LOGGED_IN)
		{
			setGameState(1);
		}
	}

	public HashMap<Integer, ItemContainer> containers = new HashMap<Integer, ItemContainer>();

	@Override
	public ItemContainer getItemContainer(InventoryID inventory) {
		return containers.get(inventory.getId());
	}

	@Override
	public ItemContainer getItemContainer(int id) {
		return containers.get(id);
	}

	@Override
	public RSNodeHashTable getItemContainers() {
		return null;
	}

	@Override
	public RSItemComposition getRSItemDefinition(int itemId) {
		return ItemDefinition.lookup(itemId);
	}

	@Override
	public SpritePixels createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable, boolean noted, int scale) {
		assert isClientThread() : "createItemSprite must be called on client thread";

		int zoom = get3dZoom();
		set3dZoom(scale);
		try {
			return createItemSprite(itemId, quantity, border, shadowColor, stackable, noted);
		} finally {
			set3dZoom(zoom);
		}
	}

	public RSSpritePixels createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable, boolean noted) {
		assert isClientThread() : "createItemSprite must be called on client thread";
		return createRSItemSprite(itemId, quantity, border, shadowColor, stackable, noted);
	}

	@Override
	public RSSpritePixels createRSItemSprite(int itemId, int quantity, int thickness, int borderColor, int stackable, boolean noted) {
		return ItemDefinition.getSprite(itemId, quantity, thickness, borderColor, stackable, noted);
	}

	@Override
	public void decodeSprite(byte[] data) {
		SpriteData.decode(data);
	}

	@Override
	public int getIndexedSpriteCount() {
		return SpriteData.spriteCount;
	}

	@Override
	public int getIndexedSpriteWidth() {
		return SpriteData.spriteWidth;
	}

	@Override
	public int getIndexedSpriteHeight() {
		return SpriteData.spriteHeight;
	}

	@Override
	public int[] getIndexedSpriteOffsetXs() {
		return SpriteData.xOffsets;
	}

	@Override
	public void setIndexedSpriteOffsetXs(int[] indexedSpriteOffsetXs) {
		SpriteData.xOffsets = indexedSpriteOffsetXs;
	}

	@Override
	public int[] getIndexedSpriteOffsetYs() {
		return SpriteData.yOffsets;
	}

	@Override
	public void setIndexedSpriteOffsetYs(int[] indexedSpriteOffsetYs) {
		SpriteData.yOffsets = indexedSpriteOffsetYs;
	}

	@Override
	public int[] getIndexedSpriteWidths() {
		return SpriteData.spriteWidths;
	}

	@Override
	public void setIndexedSpriteWidths(int[] indexedSpriteWidths) {
		SpriteData.spriteWidths = indexedSpriteWidths;
	}

	@Override
	public int[] getIndexedSpriteHeights() {
		return SpriteData.spriteHeights;
	}

	@Override
	public void setIndexedSpriteHeights(int[] indexedSpriteHeights) {
		SpriteData.spriteHeights = indexedSpriteHeights;
	}

	@Override
	public byte[][] getSpritePixels() {
		return SpriteData.pixels;
	}

	@Override
	public void setSpritePixels(byte[][] spritePixels) {
		SpriteData.pixels = spritePixels;
	}

	@Override
	public int[] getIndexedSpritePalette() {
		return SpriteData.spritePalette;
	}

	@Override
	public void setIndexedSpritePalette(int[] indexedSpritePalette) {
		SpriteData.spritePalette = indexedSpritePalette;
	}

	@Override
	public int getIntStackSize() {
		return 0;
	}

	@Override
	public void setIntStackSize(int stackSize) {
	}

	@Override
	public int[] getIntStack() {
		return null;
	}

	@Override
	public int getStringStackSize() {
		return 0;
	}

	@Override
	public void setStringStackSize(int stackSize) {
	}

	@Override
	public String[] getStringStack() {
		return null;
	}

	@Override
	public RSFriendSystem getFriendManager() {
		return null;
	}

	@Override
	public RSWidget getScriptActiveWidget() {
		return null;
	}

	@Override
	public RSWidget getScriptDotWidget() {
		return null;
	}

	@Override
	public RSScriptEvent createRSScriptEvent(Object... args) {
		return null;
	}

	@Override
	public void runScriptEvent(RSScriptEvent event) {

	}

	@Override
	public RSEvictingDualNodeHashTable getScriptCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getRSStructCompositionCache() {
		return null;
	}

	@Override
	public RSStructComposition getRSStructComposition(int id) {
		return null;
	}

	@Override
	public RSParamComposition getRSParamComposition(int id) {
		return null;
	}

	@Override
	public void setMouseLastPressedMillis(long time) {

	}

	@Override
	public int getRootWidgetCount() {
		return 0;
	}

	@Override
	public int getWidgetClickX() {
		return 0;
	}

	@Override
	public int getWidgetClickY() {
		return 0;
	}

	@Override
	public int getStaffModLevel() {
		return 0;
	}

	@Override
	public int getTradeChatMode() {
		return 0;
	}

	@Override
	public int getPublicChatMode() {
		return 0;
	}

	@Override
	public int getClientType() {
		return 0;
	}

	@Override
	public boolean isOnMobile() {
		return false;
	}

	@Override
	public boolean hadFocus() {
		return false;
	}

	@Override
	public int getMouseCrossColor() {
		return 0;
	}

	@Override
	public void setMouseCrossColor(int color) {

	}


	@Override
	public boolean getShowMouseOverText() {
		return false;
	}

	@Override
	public void setShowMouseOverText(boolean showMouseOverText) {

	}

	@Override
	public int[] getDefaultRotations() {
		return new int[0];
	}

	@Override
	public boolean getShowLoadingMessages() {
		return false;
	}

	@Override
	public void setShowLoadingMessages(boolean showLoadingMessages) {

	}

	@Override
	public void setStopTimeMs(long time) {

	}

	@Override
	public void clearLoginScreen(boolean shouldClear) {

	}

	@Override
	public void setLeftTitleSprite(SpritePixels background) {

	}

	@Override
	public void setRightTitleSprite(SpritePixels background) {

	}

	@Override
	public RSBuffer newBuffer(byte[] bytes) {
		return null;
	}

	@Override
	public RSVarbitComposition newVarbitDefinition() {
		return null;
	}

	@Override
	public boolean[] getPressedKeys() {
		return keyHandler.pressedKeys;
	}


	public boolean lowMemoryMusic = false;
	@Override
	public void setLowMemory(boolean lowMemory) {
		this.lowMem = lowMemory;
	}

	@Override
	public void setSceneLowMemory(boolean lowMemory) {
		MapRegion.low_detail = lowMemory;
		SceneGraph.low_detail = lowMemory;
		((TextureProvider) Rasterizer3D.clips.textureLoader).setTextureSize(lowMem ? 64 : 128);

	}

	@Override
	public void setAudioHighMemory(boolean highMemory) {
		lowMemoryMusic = highMemory;
	}

	@Override
	public void setObjectDefinitionLowDetail(boolean lowDetail) {
		ObjectDefinition.lowMem = lowDetail;
	}


	@Override
	public boolean isFriended(String name, boolean mustBeLoggedIn) {
		return false;
	}

	@Override
	public RSFriendsChat getFriendsChatManager() {
		return null;
	}

	@Override
	public RSLoginType getLoginType() {
		return null;
	}

	@Override
	public RSUsername createName(String name, RSLoginType type) {
		return null;
	}

	@Override
	public int rs$getVarbit(int varbitId) {
		return 0;
	}

	@Override
	public RSEvictingDualNodeHashTable getVarbitCache() {
		return null;
	}

	@Override
	public FriendContainer getFriendContainer() {
		return null;
	}

	@Override
	public NameableContainer<Ignore> getIgnoreContainer() {
		return null;
	}

	@Override
	public RSClientPreferences getPreferences() {
		return null;
	}

	@Override
	public int getCameraPitchTarget() {
		return camAngleX;
	}

	@Override
	public void setCameraPitchTarget(int pitch) {
		camAngleX = pitch;
	}

	@Override
	public void setPitchSin(int v) {
		scene.pitchSineY = v;
	}

	@Override
	public void setPitchCos(int v) {
		scene.pitchCosineY = v;
	}

	@Override
	public void setYawSin(int v) {
		scene.yawSineX = v;
	}

	@Override
	public void setYawCos(int v) {
		scene.yawCosineX = v;
	}

	static int lastPitch = 128;
	static int lastPitchTarget = 128;

	@Override
	public void setCameraPitchRelaxerEnabled(boolean enabled) {

		if (pitchRelaxEnabled == enabled)
		{
			return;
		}
		pitchRelaxEnabled = enabled;
		if (!enabled)
		{
			int pitch = getCameraPitchTarget();
			if (pitch > STANDARD_PITCH_MAX)
			{
				setCameraPitchTarget(STANDARD_PITCH_MAX);
			}
		}

	}

	@Override
	public void setInvertYaw(boolean state) {
		invertYaw = state;
	}

	@Override
	public void setInvertPitch(boolean state) {
		invertPitch = state;
	}

	@Override
	public RSWorldMap getRenderOverview() {
		return null;
	}

	private static boolean stretchedEnabled;

	private static boolean stretchedFast;

	private static boolean stretchedIntegerScaling;

	private static boolean stretchedKeepAspectRatio;

	private static double scalingFactor;

	private static Dimension cachedStretchedDimensions;

	private static Dimension cachedRealDimensions;

	@Override
	public boolean isStretchedEnabled()
	{
		return stretchedEnabled;
	}

	@Override
	public void setStretchedEnabled(boolean state)
	{
		stretchedEnabled = state;
	}

	@Override
	public boolean isStretchedFast()
	{
		return stretchedFast;
	}

	@Override
	public void setStretchedFast(boolean state)
	{
		stretchedFast = state;
	}

	@Override
	public void setStretchedIntegerScaling(boolean state)
	{
		stretchedIntegerScaling = state;
	}

	@Override
	public void setStretchedKeepAspectRatio(boolean state)
	{
		stretchedKeepAspectRatio = state;
	}

	@Override
	public void setScalingFactor(int factor)
	{
		scalingFactor = 1 + (factor / 100D);
	}

	@Override
	public double getScalingFactor()
	{
		return scalingFactor;
	}

	@Override
	public Dimension getRealDimensions()
	{
		if (!isStretchedEnabled())
		{
			return getCanvas().getSize();
		}

		if (cachedRealDimensions == null)
		{
			if (isResized())
			{
				Container canvasParent = getCanvas().getParent();

				int parentWidth = canvasParent.getWidth();
				int parentHeight = canvasParent.getHeight();

				int newWidth = (int) (parentWidth / scalingFactor);
				int newHeight = (int) (parentHeight / scalingFactor);

				if (newWidth < Constants.GAME_FIXED_WIDTH || newHeight < Constants.GAME_FIXED_HEIGHT)
				{
					double scalingFactorW = (double)parentWidth / Constants.GAME_FIXED_WIDTH;
					double scalingFactorH = (double)parentHeight / Constants.GAME_FIXED_HEIGHT;
					double scalingFactor = Math.min(scalingFactorW, scalingFactorH);

					newWidth = (int) (parentWidth / scalingFactor);
					newHeight = (int) (parentHeight / scalingFactor);
				}

				cachedRealDimensions = new Dimension(newWidth, newHeight);
			}
			else
			{
				cachedRealDimensions = Constants.GAME_FIXED_SIZE;
			}
		}

		return cachedRealDimensions;
	}

	@Override
	public Dimension getStretchedDimensions()
	{
		if (cachedStretchedDimensions == null)
		{
			Container canvasParent = getCanvas().getParent();

			int parentWidth = canvasParent.getWidth();
			int parentHeight = canvasParent.getHeight();

			Dimension realDimensions = getRealDimensions();

			if (stretchedKeepAspectRatio)
			{
				double aspectRatio = realDimensions.getWidth() / realDimensions.getHeight();

				int tempNewWidth = (int) (parentHeight * aspectRatio);

				if (tempNewWidth > parentWidth)
				{
					parentHeight = (int) (parentWidth / aspectRatio);
				}
				else
				{
					parentWidth = tempNewWidth;
				}
			}

			if (stretchedIntegerScaling)
			{
				if (parentWidth > realDimensions.width)
				{
					parentWidth = parentWidth - (parentWidth % realDimensions.width);
				}
				if (parentHeight > realDimensions.height)
				{
					parentHeight = parentHeight - (parentHeight % realDimensions.height);
				}
			}

			cachedStretchedDimensions = new Dimension(parentWidth, parentHeight);
		}

		return cachedStretchedDimensions;
	}

	@Override
	public void invalidateStretching(boolean resize)
	{
		cachedRealDimensions = null;
		cachedStretchedDimensions = null;

		if (resize && isResized())
		{
			/*
				Tells the game to run resizeCanvas the next frame.

				This is useful when resizeCanvas wouldn't usually run,
				for example when we've only changed the scaling factor
				and we still want the game's canvas to resize
				with regards to the new maximum bounds.
			 */
			setResizeCanvasNextFrame(true);
		}
	}

	@Override
	public void changeWorld(World world) {

	}

	@Override
	public RSWorld createWorld() {
		return null;
	}

	@Override
	public void setAnimOffsetX(int animOffsetX) {

	}

	@Override
	public void setAnimOffsetY(int animOffsetY) {

	}

	@Override
	public void setAnimOffsetZ(int animOffsetZ) {

	}

	@Override
	public RSSpritePixels drawInstanceMap(int z) {
		return null;
	}

	@Override
	public void setMinimapReceivesClicks(boolean minimapReceivesClicks) {

	}

	@Override
	public void runScript(Object... args) {

	}

	@Override
	public ScriptEvent createScriptEvent(Object... args) {
		return null;
	}

	@Override
	public boolean hasHintArrow() {
		return false;
	}

	@Override
	public HintArrowType getHintArrowType() {
		return null;
	}

	@Override
	public void clearHintArrow() {

	}

	@Override
	public void setHintArrow(WorldPoint point) {

	}

	@Override
	public void setHintArrow(net.runelite.api.Player player) {

	}

	@Override
	public void setHintArrow(NPC npc) {

	}

	@Override
	public WorldPoint getHintArrowPoint() {
		return null;
	}

	@Override
	public net.runelite.api.Player getHintArrowPlayer() {
		return null;
	}

	@Override
	public NPC getHintArrowNpc() {
		return null;
	}

	@Override
	public boolean isInterpolatePlayerAnimations() {
		return false;
	}

	@Override
	public void setInterpolatePlayerAnimations(boolean interpolate) {

	}

	@Override
	public boolean isInterpolateNpcAnimations() {
		return false;
	}

	@Override
	public void setInterpolateNpcAnimations(boolean interpolate) {

	}

	@Override
	public boolean isInterpolateObjectAnimations() {
		return false;
	}

	@Override
	public void setInterpolateObjectAnimations(boolean interpolate) {

	}

	@Override
	public boolean isInterpolateWidgetAnimations() {
		return false;
	}

	@Override
	public void setInterpolateWidgetAnimations(boolean interpolate) {

	}

	@Override
	public boolean isInInstancedRegion() {
		return false; //TODO:
	}

	@Override
	public int getItemPressedDuration() {
		return 0;
	}

	@Override
	public void setItemPressedDuration(int duration) {

	}

	@Override
	public int getFlags() {
		return 0;
	}


	@Override
	public void setIsHidingEntities(boolean state)
	{

	}

	@Override
	public void setOthersHidden(boolean state)
	{

	}

	@Override
	public void setOthersHidden2D(boolean state)
	{

	}

	@Override
	public void setFriendsHidden(boolean state)
	{

	}

	@Override
	public void setFriendsChatMembersHidden(boolean state)
	{

	}

	@Override
	public void setIgnoresHidden(boolean state)
	{

	}

	@Override
	public void setLocalPlayerHidden(boolean state)
	{

	}

	@Override
	public void setLocalPlayerHidden2D(boolean state)
	{

	}

	@Override
	public void setNPCsHidden(boolean state)
	{

	}

	@Override
	public void setNPCsHidden2D(boolean state)
	{

	}

	@Override
	public void setHideSpecificPlayers(List<String> players)
	{

	}


	@Override
	public void setHiddenNpcIndices(List<Integer> npcIndices)
	{

	}

	@Override
	public List<Integer> getHiddenNpcIndices()
	{
		return null;
	}

	@Override
	public void setPetsHidden(boolean state)
	{

	}

	@Override
	public void setAttackersHidden(boolean state)
	{

	}

	@Override
	public void setProjectilesHidden(boolean state)
	{

	}

	@Override
	public void setDeadNPCsHidden(boolean state)
	{

	}

	@Override
	public void addHiddenNpcName(String npc)
	{

	}

	@Override
	public void removeHiddenNpcName(String npc)
	{

	}


	@Override
	public void setBlacklistDeadNpcs(Set<Integer> blacklist) {

	}

	public boolean addEntityMarker(int x, int y, RSRenderable entity)
	{

		return true;
	}

	public boolean shouldDraw(Object entity, boolean drawingUI)
	{


		return true;
	}

	private static boolean invertPitch;
	private static boolean invertYaw;

	@Override
	public RSCollisionMap[] getCollisionMaps() {
		return collisionMaps;
	}

	@Override
	public int getPlayerIndexesCount() {
		return 0;
	}

	@Override
	public int[] getPlayerIndices() {
		return playerIndices;
	}

	@Override
	public int[] getBoostedSkillLevels() {
		return currentLevels;
	}

	@Override
	public int[] getRealSkillLevels() {
		return maximumLevels;
	}

	@Override
	public int[] getSkillExperiences() {
		return currentExp;
	}

	@Override
	public int[] getChangedSkills() {
		return new int[0];
	}

	int changedSkillsCount;

	@Override
	public int getChangedSkillsCount() {
		return changedSkillsCount;
	}

	@Override
	public void setChangedSkillsCount(int i) {
		changedSkillsCount = i;
	}

	@Override
	public void queueChangedSkill(Skill skill) {
		int[] skills = getChangedSkills();
		int count = getChangedSkillsCount();
		skills[++count - 1 & 31] = skill.ordinal();
		setChangedSkillsCount(count);
	}



	public static final Map<Integer, SpritePixels> spriteOverrides = new HashMap<Integer, SpritePixels>();


	public static final Map<Integer, SpritePixels> widgetSpriteOverrides = new HashMap<Integer, SpritePixels>();

	@Override
	public Map<Integer, SpritePixels> getSpriteOverrides() {
		return spriteOverrides;
	}

	@Override
	public Map<Integer, SpritePixels> getWidgetSpriteOverrides() {
		return spriteOverrides;
	}


	@Override
	public void setCompass(SpritePixels SpritePixels) {

	}

	@Override
	public RSEvictingDualNodeHashTable getWidgetSpriteCache() {
		return null;
	}

	int tickCount;

	@Override
	public int getTickCount() {
		return tickCount;
	}

	@Override
	public void setTickCount(int tickCount) {
		this.tickCount = tickCount;
	}


	@Override
	public void setInventoryDragDelay(int delay) {

	}


	@Override
	public boolean isHdMinimapEnabled() {
		return scene.hdMinimapEnabled;
	}

	@Override
	public void setHdMinimapEnabled(boolean enabled) {
		scene.hdMinimapEnabled = enabled;
	}

	@Override
	public EnumSet<WorldType> getWorldType() {
		return EnumSet.of(WorldType.MEMBERS);
	}

	@Override
	public int getOculusOrbState() {
		return oculusOrbState;
	}

	@Override
	public void setOculusOrbState(int state) {
		oculusOrbState = state;
	}

	@Override
	public void setOculusOrbNormalSpeed(int speed) {
		oculusOrbNormalSpeed = speed;
	}

	@Override
	public int getOculusOrbFocalPointX() {
		return oculusOrbFocalPointX;
	}

	@Override
	public int getOculusOrbFocalPointY() {
		return oculusOrbFocalPointY;
	}

	@Override
	public void setOculusOrbFocalPointX(int xPos) {
		oculusOrbFocalPointX = xPos;
	}

	@Override
	public void setOculusOrbFocalPointY(int yPos) {
		oculusOrbFocalPointY = yPos;
	}

	private static RSTileItem lastItemDespawn;

	@Override
	public RSTileItem getLastItemDespawn() {
		return lastItemDespawn;
	}


	@Override
	public void setLastItemDespawn(RSTileItem lastItemDespawn)
	{

		lastItemDespawn = lastItemDespawn;
	}

	@Override
	public void openWorldHopper() {

	}

	@Override
	public void hopToWorld(World world) {

	}

	@Override
	public void setSkyboxColor(int skyboxColor) {
		scene.skyboxColor = skyboxColor;
	}

	@Override
	public int getSkyboxColor() {
		return scene.skyboxColor;
	}

	@Override
	public boolean isGpu() {
		return (gpuFlags & 1) == 1;
	}

	@Override
	public void setGpuFlags(int gpuFlags) {
		this.gpuFlags = gpuFlags;
	}

	@Override
	public void setExpandedMapLoading(int chunks) {

	}

	@Override
	public int getExpandedMapLoading() {
		return 0;
	}

	@Override
	public int getGpuFlags() {
		return gpuFlags;
	}

	@Override
	public int get3dZoom() {
		return Rasterizer3D.clips.viewportZoom;
	}

	@Override
	public int set3dZoom(int zoom) {
		Rasterizer3D.clips.viewportZoom = zoom;
		return zoom;
	}

	@Override
	public int getCenterX() {
		return getViewportWidth() / 2;
	}

	@Override
	public int getCenterY() {
		return getViewportHeight() / 2;
	}

	@Override
	public int getCameraX2() {
		return SceneGraph.xCameraPos;
	}

	@Override
	public int getCameraY2() {
		return SceneGraph.zCameraPos;
	}

	@Override
	public int getCameraZ2() {
		return SceneGraph.yCameraPos;
	}

	@Override
	public RSTextureProvider getTextureProvider() {
		return ((TextureProvider) Rasterizer3D.clips.textureLoader);
	}

	@Override
	public int[][] getOccupiedTilesTick() {
		return new int[0][];
	}

	@Override
	public RSEvictingDualNodeHashTable getObjectDefinitionModelsCache() {
		return null;
	}

	@Override
	public int getCycle() {
		return scene.cycle;
	}

	@Override
	public void setCycle(int cycle) {
		scene.cycle = cycle;
	}

	@Override
	public boolean[][][][] getVisibilityMaps() {
		return scene.visibilityMap;
	}

	@Override
	public RSEvictingDualNodeHashTable getCachedModels2() {
		return null;
	}

	@Override
	public void setRenderArea(boolean[][] renderArea) {
		scene.renderArea = renderArea;
	}

	@Override
	public void setCameraX2(int cameraX2) {
		scene.xCameraPos = cameraX2;
	}

	@Override
	public void setCameraY2(int cameraY2) {
		scene.zCameraPos = cameraY2;
	}

	@Override
	public void setCameraZ2(int cameraZ2) {
		scene.yCameraPos = cameraZ2;
	}

	@Override
	public void setScreenCenterX(int screenCenterX) {
		scene.screenCenterX = screenCenterX;
	}

	@Override
	public void setScreenCenterZ(int screenCenterZ) {
		scene.screenCenterZ = screenCenterZ;
	}

	@Override
	public void setScenePlane(int scenePlane) {
		scene.currentRenderPlane = scenePlane;
	}

	@Override
	public void setMinTileX(int i) {
		scene.minTileX = i;
	}

	@Override
	public void setMinTileZ(int i) {
		scene.minTileZ = i;
	}

	@Override
	public void setMaxTileX(int i) {
		scene.maxTileX = i;
	}

	@Override
	public void setMaxTileZ(int i) {
		scene.maxTileZ = i;
	}

	@Override
	public int getTileUpdateCount() {
		return scene.tileUpdateCount;
	}

	@Override
	public void setTileUpdateCount(int tileUpdateCount) {
		scene.tileUpdateCount = tileUpdateCount;
	}

	@Override
	public boolean getViewportContainsMouse() {
		return false;
	}

	@Override
	public int getRasterizer3D_clipMidX2() {
		return Clips.getClipMidX2();
	}

	@Override
	public int getRasterizer3D_clipNegativeMidX() {
		return Rasterizer3D.clips.clipNegativeMidX;
	}

	@Override
	public int getRasterizer3D_clipNegativeMidY() {
		return Rasterizer3D.clips.clipNegativeMidY;
	}

	@Override
	public int getRasterizer3D_clipMidY2() {
		return Clips.getClipMidY2();
	}
	@Override
	public void checkClickbox(net.runelite.api.Model model, int orientation, int pitchSin, int pitchCos, int yawSin,
							  int yawCos, int x, int y, int z, long hash) {

	}

	@Override
	public RSWidget getIf1DraggedWidget() {
		return null;
	}

	@Override
	public int getIf1DraggedItemIndex() {
		return 0;
	}

	@Override
	public void setSpellSelected(boolean selected) {

	}

	@Override
	public RSEnumComposition getRsEnum(int id) {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getItemCompositionCache() {
		return null;
	}

	@Override
	public RSSpritePixels[] getCrossSprites() {
		return crosses;
	}


	@Override
	public EnumComposition getEnum(int id) {
		return null;
	}

	private static final int MENU_BORDER_OUTER_2010 = 0x6D6A5B;
	private static final int MENU_BORDER_INNER_2010 = 0x524A3D;
	private static final int MENU_PADDING_2010 = 0x2B2622;
	private static final int MENU_BACKGROUND_2010 = 0x2B271C;
	private static final int MENU_TEXT_2010 = 0xC6B895;
	private static final int MENU_HEADER_GRADIENT_TOP_2010 = 0x322E22;
	private static final int MENU_HEADER_GRADIENT_BOTTOM_2010 = 0x090A04;
	private static final int ORIGINAL_BG = 0x5D5447;

	@Override
	public void draw2010Menu(int alpha)
	{
		int x = getMenuX();
		int y = getMenuY();
		int w = getMenuWidth();
		int h = getMenuHeight();

		draw2010MenuNest(null, x, y, w, h, getMenuScroll(), alpha);

	}

	private void draw2010MenuNest(MenuEntry parent, int x, int y, int w, int h, int scroll, int alpha)
	{
		// Outside border
		rasterizerDrawHorizontalLineAlpha(x + 2, y, w - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 2, y + h - 1, w - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x, y + 2, h - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + w - 1, y + 2, h - 4, MENU_BORDER_OUTER_2010, alpha);

		// Padding
		rasterizerDrawRectangleAlpha(x + 1, y + 5, w - 2, h - 6, MENU_PADDING_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 1, y + 17, w - 2, MENU_PADDING_2010, alpha);
		rasterizerDrawCircleAlpha(x + 2, y + h - 3, 0, MENU_PADDING_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 3, y + h - 3, 0, MENU_PADDING_2010, alpha);

		// Header
		rasterizerDrawGradientAlpha(x + 2, y + 1, w - 4, 16, MENU_HEADER_GRADIENT_TOP_2010, MENU_HEADER_GRADIENT_BOTTOM_2010, alpha, alpha);
		rasterizerFillRectangleAlpha(x + 1, y + 1, 2, 4, MENU_PADDING_2010, alpha);
		rasterizerFillRectangleAlpha(x + w - 3, y + 1, 2, 4, MENU_PADDING_2010, alpha);

		// Inside border
		rasterizerDrawHorizontalLineAlpha(x + 2, y + 18, w - 4, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 3, y + h - 3, w - 6, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + 2, y + 18, h - 21, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + w - 3, y + 18, h - 21, MENU_BORDER_INNER_2010, alpha);

		// Options background
		rasterizerFillRectangleAlpha(x + 3, y + 19, w - 6, h - 22, MENU_BACKGROUND_2010, alpha);

		// Corner insets
		rasterizerDrawCircleAlpha(x + 1, y + 1, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 2, y + 1, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + 1, y + h - 2, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 2, y + h - 2, 0, MENU_BORDER_OUTER_2010, alpha);

		RSFontOSRS font = (RSFontOSRS) getFontBold12();
		String option = parent == null ? "Choose Option" : parent.getTarget();
		font.drawTextLeftAligned(option, x + 3, y + 14, MENU_TEXT_2010, -1);

		MenuEntry[] entries = getMenuEntries();

		int mouseX = getMouseX();
		int mouseY = getMouseY();
		int realCount = 0;
		for (int i = 0; i < entries.length; i++)
		{
			if (entries[i].getParent() == parent)
			{
				realCount++;
			}
		}
		int realIdx = 0;
		for (int i = 0; i < entries.length; i++)
		{
			MenuEntry entry = entries[i];
			if (entry.getParent() == parent && realCount - 1 - realIdx >= scroll)
			{
				int rowY = y + (realCount - 1 - realIdx - scroll) * 15 + 31;
				realIdx++;
				String s = entry.getOption();
				if (!entry.getTarget().isEmpty())
				{
					s = s + " " + entry.getTarget();
				}

				if (entry.getType() == MenuAction.RUNELITE_SUBMENU)
				{
					s = s + " <col=ffffff><gt>";
				}

				font.drawTextLeftAligned(s, x + 3, rowY, MENU_TEXT_2010, -1);
				if (mouseX > x && mouseX < w + x && mouseY > rowY - 13 && mouseY < rowY + 3)
				{
					rasterizerFillRectangleAlpha(x + 3, rowY - 12, w - 6, 15, 0xffffff, 80);
					if (entry.getType() == MenuAction.RUNELITE_SUBMENU)
					{
						setSubmenuIdx(i);
						initSubmenu(x + w, rowY - 31);
					}
				}
				if (getSubmenuIdx() == i)
				{
					this.draw2010MenuNest(entry, getSubmenuX(), getSubmenuY(), getSubmenuWidth(), getSubmenuHeight(), getSubmenuScroll(), alpha);
				}
			}
		}

	}

	void initSubmenu(int x, int y)
	{
		if (getSubmenuIdx() != -1)
		{
			RSRuneLiteMenuEntry rootEntry = (RSRuneLiteMenuEntry) getMenuEntries()[getSubmenuIdx()];
			String s = rootEntry.getOption();
			if (!rootEntry.getTarget().isEmpty())
			{
				s = s + " " + rootEntry.getTarget();
			}

			if (rootEntry.getType() == MenuAction.RUNELITE_SUBMENU)
			{
				s = s + " <col=ffffff><gt>";
			}
			int tempWidth = getFontBold12().getTextWidth(s);
			int realCount = 0;

			for (MenuEntry me : getMenuEntries())
			{
				if (me.getParent() == null) continue;
				if (((RSRuneLiteMenuEntry)me.getParent()).getIdx() == rootEntry.getIdx())
				{
					realCount++;
					tempWidth = Math.max(getFontBold12().getTextWidth(
							(me.getTarget().length() > 0 ? me.getOption() + " " + me.getTarget() : me.getOption())
					), tempWidth);
				}
			}
			tempWidth += 8;
			int var4 = realCount * 15 + 22;
			int var5 = x/* - tempWidth / 2*/;
			if (var5 + tempWidth > getCanvasWidth())
			{
				//var5 = client.getCanvasWidth() - tempWidth;
				var5 = x - tempWidth - getMenuWidth();
			}

			if (var5 < 0)
			{
				var5 = 0;
			}

			int var6 = y;
			if (var4 + y > getCanvasHeight())
			{
				var6 = getCanvasHeight() - var4;
			}

			if (var6 < 0)
			{
				var6 = 0;
			}

			setSubmenuX(var5);
			setSubmenuY(var6);
			setSubmenuWidth(tempWidth);
			setSubmenuHeight(realCount * 15 + 22);

			setSubmenuScroll(0);
			setSubmenuScrollMax(0);
			if (var4 > this.getCanvasHeight())
			{
				setSubmenuScrollMax(var4 - this.getCanvasHeight());
			}
		}
	}

	@Override
	public int[] getGraphicsPixels() {
		return null;
	}

	@Override
	public int getGraphicsPixelsWidth() {
		return 0;
	}

	@Override
	public int getGraphicsPixelsHeight() {
		return 0;
	}

	@Override
	public void rasterizerFillRectangle(int x, int y, int w, int h, int rgb) {
		Rasterizer2D.drawBox(x,y,w,h,rgb);
	}

	@Override
	public int getStartX() {
		return 0;
	}

	@Override
	public int getStartY() {
		return 0;
	}

	@Override
	public int getEndX() {
		return 0;
	}

	@Override
	public int getEndY() {
		return 0;
	}

	@Override
	public void drawOriginalMenu(int alpha)
	{
		int x = getMenuX();
		int y = getMenuY();
		int w = getMenuWidth();
		int h = getMenuHeight();
		drawOriginalMenuNest(null, x, y, w, h, getMenuScroll(), alpha);
	}

	private void drawOriginalMenuNest(MenuEntry parent, int x, int y, int w, int h, int scroll, int alpha)
	{
		if (alpha != 255)
		{
			rasterizerFillRectangleAlpha(x, y, w, h, ORIGINAL_BG, alpha);
			rasterizerFillRectangleAlpha(x + 1, y + 1, w - 2, 16, 0, alpha);
			rasterizerDrawRectangleAlpha(x + 1, y + 18, w - 2, h - 19, 0, alpha);
		}
		else
		{
			rasterizerFillRectangle(x, y, w, h, ORIGINAL_BG);
			rasterizerFillRectangle(x + 1, y + 1, w - 2, 16, 0);
			rasterizerDrawRectangle(x + 1, y + 18, w - 2, h - 19, 0);
		}

		RSFontOSRS font = (RSFontOSRS) getFontBold12();
		String option = parent == null ? "Choose Option" : parent.getTarget();
		font.drawTextLeftAligned(option, x + 3, y + 14, ORIGINAL_BG, -1);

		MenuEntry[] entries = getMenuEntries();

		int mouseX = getMouseX();
		int mouseY = getMouseY();
		int realCount = 0;
		for (int i = 0; i < entries.length; i++)
		{
			if (entries[i].getParent() == parent)
			{
				realCount++;
			}
		}
		int realIdx = 0;
		for (int i = 0; i < entries.length; i++)
		{
			MenuEntry entry = entries[i];
			if (entry.getParent() == parent && realCount - 1 - realIdx >= scroll)
			{
				int rowY = y + (realCount - 1 - realIdx - scroll) * 15 + 31;
				realIdx++;
				String s = entry.getOption();
				if (!entry.getTarget().isEmpty())
				{
					s = s + " " + entry.getTarget();
				}

				if (entry.getType() == MenuAction.RUNELITE_SUBMENU)
				{
					s = s + " <col=ffffff><gt>";
				}
				int highlight = 0xFFFFFF;
				if (mouseX > x && mouseX < w + x && mouseY > rowY - 13 && mouseY < rowY + 3)
				{
					highlight = 0xFFFF00;
					if (entry.getType() == MenuAction.RUNELITE_SUBMENU)
					{
						setSubmenuIdx(i);
						initSubmenu(x + w, rowY - 31);
					}
				}
				font.drawTextLeftAligned(s, x + 3, rowY, highlight, 0);
				if (getSubmenuIdx() == i)
				{
					this.drawOriginalMenuNest(entry, getSubmenuX(), getSubmenuY(), getSubmenuWidth(), getSubmenuHeight(), getSubmenuScroll(), alpha);
				}
			}
		}
	}


	@Override
	public void resetHealthBarCaches() {
		getHealthBarCache().reset();
		getHealthBarSpriteCache().reset();
	}

	@Override
	public void resetHitsplatCaches() {
		HitSplatDefinition.cached.clear();
		HitSplatDefinition.spritesCached.clear();
		HitSplatDefinition.fontsCached.clear();

	}

	@Override
	public boolean getRenderSelf() {
		return false;
	}

	@Override
	public void setRenderSelf(boolean enabled) {

	}

	@Override
	public void invokeMenuAction(String option, String target, int identifier, int opcode, int param0, int param1) {

	}

	@Override
	public RSMouseRecorder getMouseRecorder() {
		return null;
	}

	@Override
	public String getSelectedSpellName() {
		return null;
	}

	@Override
	public void setSelectedSpellName(String name) {

	}

	@Override
	public boolean getSpellSelected() {
		return false;
	}

	@Override
	public RSSoundEffect getTrack(RSAbstractArchive indexData, int id, int var0) {
		return null;
	}

	@Override
	public RSRawPcmStream createRawPcmStream(RSRawSound audioNode, int var0, int volume) {
		return null;
	}

	@Override
	public RSPcmStreamMixer getSoundEffectAudioQueue() {
		return null;
	}

	@Override
	public RSArchive getIndexCache4() {
		return null;
	}

	@Override
	public RSDecimator getSoundEffectResampler() {
		return null;
	}

	@Override
	public void setMusicTrackVolume(int volume) {

	}

	@Override
	public void setViewportWalking(boolean viewportWalking) {

	}

	@Override
	public void playMusicTrack(int var0, RSAbstractArchive var1, int var2, int var3, int var4, boolean var5) {

	}

	@Override
	public RSMidiPcmStream getMidiPcmStream() {
		return null;
	}

	@Override
	public int getCurrentTrackGroupId() {
		return 0;
	}

	@Override
	public String getSelectedSpellActionName() {
		return null;
	}

	@Override
	public int getSelectedSpellFlags() {
		return 0;
	}

	@Override
	public void setHideFriendAttackOptions(boolean yes) {

	}

	@Override
	public void setHideFriendCastOptions(boolean yes) {

	}

	@Override
	public void setHideClanmateAttackOptions(boolean yes) {
	}

	@Override
	public void setHideClanmateCastOptions(boolean yes) {

	}

	@Override
	public void setUnhiddenCasts(Set<String> casts) {

	}

	@Override
	public void addFriend(String name) {

	}

	@Override
	public void removeFriend(String name) {

	}

	@Override
	public void setModulus(BigInteger modulus) {

	}

	@Override
	public BigInteger getModulus() {
		return null;
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	@Override
	public void setAllWidgetsAreOpTargetable(boolean value) {

	}

	@Override
	public void insertMenuItem(String action, String target, int opcode, int identifier, int argument1, int argument2,
							   boolean forceLeftClick) {

	}

	@Override
	public void setSelectedItemID(int id) {

	}

	@Override
	public int getSelectedItemWidget() {
		return 0;
	}

	@Override
	public void setSelectedItemWidget(int widgetID) {

	}

	@Override
	public int getSelectedItemSlot() {
		return 0;
	}

	@Override
	public void setSelectedItemSlot(int idx) {

	}

	@Override
	public int getSelectedSpellWidget() {
		return 0;
	}

	@Override
	public int getSelectedSpellChildIndex() {
		return 0;
	}

	@Override
	public void setSelectedSpellWidget(int widgetID) {

	}

	@Override
	public void setSelectedSpellChildIndex(int index) {

	}

	@Override
	public void scaleSprite(int[] canvas, int[] pixels, int color, int pixelX, int pixelY, int canvasIdx,
							int canvasOffset, int newWidth, int newHeight, int pixelWidth, int pixelHeight, int oldWidth) {

	}

	@Override
	public void promptCredentials(boolean clearPass) {

	}

	@Override
	public RSVarpDefinition getVarpDefinition(int id) {
		return null;
	}

	@Override
	public RSTileItem newTileItem() {
		return new TileItem();
	}

	@Override
	public RSNodeDeque newNodeDeque() {
		return new NodeDeque();
	}

	@Override
	public void updateItemPile(int localX, int localY) {
		updateItemPile(plane,localX,localY);
	}

	@Override
	public void setHideDisconnect(boolean dontShow) {

	}

	@Override
	public void setTempMenuEntry(MenuEntry entry) {
		if (entry == null || tempMenuAction == null)
		{
			return;
		}

		tempMenuAction.setOption(entry.getOption());
		tempMenuAction.setOpcode(entry.getType().getId());
		tempMenuAction.setIdentifier(entry.getIdentifier());
		tempMenuAction.setParam0(entry.getParam0());
		tempMenuAction.setParam1(entry.getParam1());
	}

	@Override
	public void setShowMouseCross(boolean show) {

	}

	@Override
	public int getDraggedWidgetX() {
		return 0;
	}

	@Override
	public int getDraggedWidgetY() {
		return 0;
	}

	@Override
	public int[] getChangedSkillLevels() {
		return new int[0];
	}

	@Override
	public void setMouseIdleTicks(int cycles) {
		MouseHandler.idleCycles = cycles;
	}

	@Override
	public void setKeyboardIdleTicks(int cycles) {
		KeyHandler.idleCycles = cycles;
	}

	@Override
	public void setGeSearchResultCount(int count) {
	}

	@Override
	public void setGeSearchResultIds(short[] ids) {

	}

	@Override
	public void setGeSearchResultIndex(int index) {

	}

	@Override
	public void setComplianceValue(String key, boolean value) {

	}

	@Override
	public boolean getComplianceValue(String key) {
		return false;
	}

	@Override
	public boolean isMirrored() {
		return false;
	}

	@Override
	public void setMirrored(boolean isMirrored) {

	}

	@Override
	public boolean isComparingAppearance() {
		return false;
	}

	@Override
	public void setComparingAppearance(boolean comparingAppearance) {

	}

	@Override
	public void setLoginScreen(SpritePixels pixels) {

	}

	@Override
	public void setShouldRenderLoginScreenFire(boolean val) {

	}

	@Override
	public boolean shouldRenderLoginScreenFire() {
		return false;
	}

	@Override
	public boolean isKeyPressed(int keycode) {
		boolean[] pressedKeys = getPressedKeys();
		return pressedKeys[keycode];
	}

	@Override
	public int getFollowerIndex() {
		return 0;
	}

	@Override
	public int isItemSelected() {
		return 0;
	}

	@Override
	public String getSelectedItemName() {
		return null;
	}

	@Override
	public RSWidget getMessageContinueWidget() {
		return null;
	}

	@Override
	public void setMusicPlayerStatus(int var0) {

	}

	@Override
	public void setMusicTrackArchive(RSAbstractArchive var0) {

	}

	@Override
	public void setMusicTrackGroupId(int var0) {

	}

	@Override
	public void setMusicTrackFileId(int var0) {

	}

	@Override
	public void setMusicTrackBoolean(boolean var0) {

	}

	@Override
	public void setPcmSampleLength(int var0) {

	}

	@Override
	public int[] getChangedVarps() {
		return new int[0];
	}

	@Override
	public int getChangedVarpCount() {
		return 0;
	}

	@Override
	public void setChangedVarpCount(int changedVarpCount) {

	}

	@Override
	public void setOutdatedScript(String outdatedScript) {

	}

	@Override
	public List<String> getOutdatedScripts() {
		return null;
	}

	@Override
	public RSFrames getFrames(int frameId) {
		return null;
	}

	@Override
	public RSSpritePixels getMinimapSprite() {
		return minimapImage;
	}

	@Override
	public void setMinimapSprite(SpritePixels spritePixels) {

	}

	@Override
	public void drawObject(int z, int x, int y, int randomColor1, int randomColor2) {

	}

	@Override
	public RSScriptEvent createScriptEvent() {
		return null;
	}

	@Override
	public void runScript(RSScriptEvent ev, int ex, int var2) {

	}

	@Override
	public void setHintArrowTargetType(int value) {

	}

	@Override
	public int getHintArrowTargetType() {
		return 0;
	}

	@Override
	public void setHintArrowX(int value) {

	}

	@Override
	public int getHintArrowX() {
		return 0;
	}

	@Override
	public void setHintArrowY(int value) {

	}

	@Override
	public int getHintArrowY() {
		return 0;
	}

	@Override
	public void setHintArrowOffsetX(int value) {

	}

	@Override
	public void setHintArrowOffsetY(int value) {

	}

	@Override
	public void setHintArrowNpcTargetIdx(int value) {

	}

	@Override
	public int getHintArrowNpcTargetIdx() {
		return 0;
	}

	@Override
	public void setHintArrowPlayerTargetIdx(int value) {

	}

	@Override
	public int getHintArrowPlayerTargetIdx() {
		return 0;
	}

	@Override
	public RSSequenceDefinition getSequenceDefinition(int id) {
		return (RSSequenceDefinition) SequenceDefinition.get(id);
	}

	@Override
	public RSIntegerNode newIntegerNode(int contents) {
		return null;
	}

	@Override
	public RSObjectNode newObjectNode(Object contents) {
		return null;
	}

	@Override
	public RSIterableNodeHashTable newIterableNodeHashTable(int size) {
		return null;
	}

	@Override
	public RSVarbitComposition getVarbitComposition(int id) {
		return null;
	}

	@Override
	public RSAbstractArchive getSequenceDefinition_skeletonsArchive() {
		return null;
	}

	@Override
	public RSAbstractArchive getSequenceDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getSequenceDefinition_animationsArchive() {
		return null;
	}

	@Override
	public RSAbstractArchive getNpcDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getObjectDefinition_modelsArchive() {
		return Js5List.models;
	}

	@Override
	public RSAbstractArchive getObjectDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getItemDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getKitDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getKitDefinition_modelsArchive() {
		return null;
	}

	@Override
	public RSAbstractArchive getSpotAnimationDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getSpotAnimationDefinition_modelArchive() {
		return null;
	}

	@Override
	public RSBuffer createBuffer(byte[] initialBytes) {
		return null;
	}

	@Override
	public RSSceneTilePaint createSceneTilePaint(int swColor, int seColor, int neColor, int nwColor, int texture, int rgb, boolean isFlat) {
		return null;
	}

	@Override
	public long[] getCrossWorldMessageIds() {
		return null;
	}

	@Override
	public int getCrossWorldMessageIdsIndex() {
		return 0;
	}

	@Override
	public RSClanChannel[] getCurrentClanChannels() {
		return new RSClanChannel[0];
	}

	@Override
	public RSClanSettings[] getCurrentClanSettingsAry() {
		return new RSClanSettings[0];
	}

	@Override
	public RSClanChannel getClanChannel() {
		return null;
	}

	@Override
	public RSClanChannel getGuestClanChannel() {
		return null;
	}

	@Override
	public RSClanSettings getClanSettings() {
		return null;
	}

	@Override
	public RSClanSettings getGuestClanSettings() {
		return null;
	}

	@Override
	public ClanRank getClanRankFromRs(int rank) {
		return null;
	}

	@Override
	public RSIterableNodeHashTable readStringIntParameters(RSBuffer buffer, RSIterableNodeHashTable table) {
		return null;
	}

	@Override
	public int getRndHue() {
		return 0;
	}

	@Override
	public short[][][] getTileUnderlays() {
		return scene.getUnderlayIds();
	}

	@Override
	public short[][][] getTileOverlays() {
		return scene.getOverlayIds();
	}

	@Override
	public byte[][][] getTileShapes() {
		return scene.getTileShapes();
	}

	@Override
	public RSSpotAnimationDefinition getSpotAnimationDefinition(int id) {
		return null;
	}

	@Override
	public RSModelData getModelData(RSAbstractArchive var0, int var1, int var2) {
		return com.client.entity.model.Mesh.getModel(var1);
	}

	@Override
	public boolean isCameraLocked() {
		return isCameraLocked;
	}

	@Override
	public boolean getCameraPitchRelaxerEnabled() {
		return pitchRelaxEnabled;
	}

	public static boolean unlockedFps;
	public long delayNanoTime;
	public long lastNanoTime;
	public static double tmpCamAngleY;
	public static double tmpCamAngleX;

	@Override
	public boolean isUnlockedFps() {
		return unlockedFps;
	}

	@Override
	public long getUnlockedFpsTarget() {
		return delayNanoTime;
	}

	public void updateCamera()
	{
		if (unlockedFps)
		{
			long nanoTime = System.nanoTime();
			long diff = nanoTime - this.lastNanoTime;
			this.lastNanoTime = nanoTime;

			if (this.getGameState() == GameState.LOGGED_IN)
			{
				this.interpolateCamera(diff);
			}
		}
	}

	public static final int STANDARD_PITCH_MIN = 128;
	public static final int STANDARD_PITCH_MAX = 383;
	public static final int NEW_PITCH_MAX = 512;

	public void interpolateCamera(long var1)
	{
		double angleDX = diffToDangle(getCamAngleDY(), var1);
		double angleDY = diffToDangle(getCamAngleDX(), var1);

		tmpCamAngleY += angleDX / 2;
		tmpCamAngleX += angleDY / 2;
		tmpCamAngleX = Doubles.constrainToRange(tmpCamAngleX, Perspective.UNIT * STANDARD_PITCH_MIN, getCameraPitchRelaxerEnabled() ? Perspective.UNIT * NEW_PITCH_MAX : Perspective.UNIT * STANDARD_PITCH_MAX);

		int yaw = toCameraPos(tmpCamAngleY);
		int pitch = toCameraPos(tmpCamAngleX);

		setCameraYawTarget(yaw);
		setCameraPitchTarget(pitch);
	}

	public static int toCameraPos(double var0)
	{
		return (int) (var0 / Perspective.UNIT) & 2047;
	}


	public static double diffToDangle(int var0, long var1)
	{
		double var2 = var0 * Perspective.UNIT;
		double var3 = (double) var1 / 2.0E7D;

		return var2 * var3;
	}

	@Override
	public void posToCameraAngle(int var0, int var1) {
		tmpCamAngleY = var0 * Perspective.UNIT;
		tmpCamAngleX = var1 * Perspective.UNIT;
	}

	public static void onCameraPitchTargetChanged(int idx)
	{
		int newPitch = instance.getCameraPitchTarget();
		int pitch = newPitch;
		if (pitchRelaxEnabled)
		{
			// This works because the vanilla camera movement code only moves %2
			if (lastPitchTarget > STANDARD_PITCH_MAX && newPitch == STANDARD_PITCH_MAX)
			{
				pitch = lastPitchTarget;
				if (pitch > NEW_PITCH_MAX)
				{
					pitch = NEW_PITCH_MAX;
				}
				instance.setCameraPitchTarget(pitch);
			}
		}
		lastPitchTarget = pitch;
	}

	public void onCamAngleDXChange()
	{
		if (invertPitch && getMouseCurrentButton() == 4 && isMouseCam())
		{
			setCamAngleDX(-getCamAngleDX());
		}
	}

	public void onCamAngleDYChange()
	{
		if (invertYaw && getMouseCurrentButton() == 4 && isMouseCam())
		{
			setCamAngleDY(-getCamAngleDY());
		}
	}

	public static void onCameraPitchChanged(int idx)
	{
		int newPitch = instance.getCameraPitch();
		int pitch = newPitch;
		if (pitchRelaxEnabled)
		{
			// This works because the vanilla camera movement code only moves %2
			if (lastPitch > STANDARD_PITCH_MAX && newPitch == STANDARD_PITCH_MAX)
			{
				pitch = lastPitch;
				if (pitch > NEW_PITCH_MAX)
				{
					pitch = NEW_PITCH_MAX;
				}
				instance.setCameraPitch(pitch);
			}
		}
		lastPitch = pitch;
	}

	@Override
	public RSClanChannel getClanChannel(int clanId) {
		return null;
	}

	@Override
	public RSClanSettings getClanSettings(int clanId) {
		return null;
	}

	@Override
	public void setUnlockedFps(boolean unlock) {
		unlockedFps = unlock;
	}

	@Override
	public void setUnlockedFpsTarget(int fps) {
		delayNanoTime = fps;
	}

	@Override
	public net.runelite.api.Deque<AmbientSoundEffect> getAmbientSoundEffects() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getEnumDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getFloorUnderlayDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getFloorOverlayDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getHitSplatDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getHitSplatDefinitionSpritesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getHitSplatDefinitionDontsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getInvDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getItemDefinitionModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getItemDefinitionSpritesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getKitDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getNpcDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getNpcDefinitionModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getObjectDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getObjectDefinitionModelDataCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getObjectDefinitionEntitiesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getParamDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getPlayerAppearanceModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSequenceDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSequenceDefinitionFramesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSequenceDefinitionModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSpotAnimationDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSpotAnimationDefinitionModlesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getVarcIntCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getVarpDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getFontsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSpriteMasksCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSpritesCache() {
		return null;
	}

	@Override
	public RSIterableNodeHashTable createIterableNodeHashTable(int size) {
		return null;
	}

	@Override
	public int getSceneMaxPlane() {
		return 0;
	}

	@Override
	public void setIdleTimeout(int id) {

	}

	@Override
	public int getIdleTimeout() {
		return 0;
	}

	boolean canZoomMap = false;

	@Override
	public void setMinimapZoom(boolean minimapZoom) {
		this.canZoomMap = minimapZoom;
	}

	public boolean splatStyles;

	@Override
	public void setStylesActive(boolean active) {
		splatStyles = active;
	}

	@Override
	public double getMinimapZoom() {
		return minimapZoom;
	}

	@Override
	public boolean isMinimapZoom() {
		return canZoomMap;
	}

	@Override
	public void setMinimapZoom(double zoom) {
		minimapZoom = (int) zoom;
	}

	public static int viewportZoom;

	@Override
	public void setScale(int scale) {
		viewportZoom = scale;
	}

	@Override
	public int getMouseWheelRotation() {
		return mouseWheelRotation;
	}

	@Override
	public int getScale() {
		return viewportZoom;
	}

	public boolean newclickInRegion(int x1, int y1, Sprite drawnSprite) {
		return  MouseHandler.clickMode3 == 1 && (MouseHandler.saveClickX >= x1 && MouseHandler.saveClickX <= x1 + drawnSprite.myWidth && MouseHandler.saveClickY >= y1
			&& MouseHandler.saveClickY <= y1 + drawnSprite.myHeight);
	}

	public boolean newmouseInRegion(int x1, int y1, Sprite drawnSprite) {
		return MouseHandler.mouseX >= x1 && MouseHandler.mouseX <= x1 + drawnSprite.myWidth && MouseHandler.mouseY >= y1
			&& MouseHandler.mouseY <= y1 + drawnSprite.myHeight;
	}

	public boolean newmouseInRegion(int x1, int y1, int x2, int y2) {
		return  MouseHandler.mouseX >= x1 && MouseHandler.mouseX <= x1 + x2 && MouseHandler.mouseY >= y1
			&& MouseHandler.mouseY <= y1 + y2;
	}

	public boolean newclickInRegion(int x1, int y1, int x2, int y2) {
		return  MouseHandler.clickMode3 == 1 && (MouseHandler.saveClickX >= x1 && MouseHandler.saveClickX <= x1 + x2 && MouseHandler.saveClickY >= y1
			&& MouseHandler.saveClickY <= y1 + y2);
	}


	@Override
	public long getAccountHash() {
		return 0L;
	}

	//Menu

	private static int tmpMenuOptionsCount;

	public static RSRuneLiteMenuEntry newBareRuneliteMenuEntry() {
		return new RuneLiteMenuEntry();
	}

	public static RSRuneLiteMenuEntry newRuneliteMenuEntry(final int idx) {
		return new RuneLiteMenuEntry(idx);
	}

	public void sendStringClient(String text, int interfaceId) {

		if (RSInterface.interfaceCache[interfaceId] == null) {
			return;
		}

		RSInterface.interfaceCache[interfaceId].message = text;
	}

	@Override
	public void setDialogState(String prompt, int i, String value) {
		enterInputInChatString = prompt;
		inputDialogState = i;
		messagePromptRaised = true;
		amountOrNameInput = value;
	}

	@Override
	public String getInputValue() {
		return amountOrNameInput;
	}


	@Override
	public void openChatOption(int firstChildId, int optionAmount, String title, String[] options) {
		// Send title
		sendStringClient(title, firstChildId - 1);

		// Send options.
		for (int i = 0; i < options.length; i++) {
			sendStringClient(options[i], firstChildId + i);
		}

		// Send chatbox interface
		resetAnimation(firstChildId - 2);
		if (backDialogID != -1) {
			backDialogID = -1;
		}
		backDialogID = firstChildId - 2;

		continuedDialogue = false;
	}


	@Override
	public MenuEntry createMenuEntry(int idx)
	{
		if (getMenuOptionCount() >= 500)
		{
			throw new IllegalStateException();
		}
		else
		{
			if (idx < 0)
			{
				idx = getMenuOptionCount() + idx + 1;
				if (idx < 0)
				{
					throw new IllegalArgumentException();
				}
			}

			RSRuneLiteMenuEntry menuEntry;
			if (idx < getMenuOptionCount())
			{
				RSRuneLiteMenuEntry tmpEntry = rl$menuEntries[getMenuOptionCount()];
				if (tmpEntry == null)
				{
					tmpEntry = rl$menuEntries[getMenuOptionCount()] = newRuneliteMenuEntry(getMenuOptionCount());
				}

				for (int i = getMenuOptionCount(); i > idx; rl$menuEntries[i].setIdx(i--))
				{
					getMenuOptions()[i] = getMenuOptions()[i - 1];
					getMenuTargets()[i] = getMenuTargets()[i - 1];
					getMenuIdentifiers()[i] = getMenuIdentifiers()[i - 1];
					getMenuOpcodes()[i] = getMenuOpcodes()[i - 1];
					getMenuArguments1()[i] = getMenuArguments1()[i - 1];
					getMenuArguments2()[i] = getMenuArguments2()[i - 1];
					getMenuForceLeftClick()[i] = getMenuForceLeftClick()[i - 1];

					rl$menuEntries[i] = rl$menuEntries[i - 1];
				}

				setMenuOptionCount(getMenuOptionCount() + 1);
				tmpMenuOptionsCount = getMenuOptionCount();

				menuEntry = tmpEntry;
				rl$menuEntries[idx] = tmpEntry;

				tmpEntry.setIdx(idx);
			}
			else
			{
				if (idx != getMenuOptionCount())
				{
					throw new IllegalArgumentException();
				}

				menuEntry = rl$menuEntries[getMenuOptionCount()];

				if (menuEntry == null)
				{
					menuEntry = rl$menuEntries[getMenuOptionCount()] = newRuneliteMenuEntry(getMenuOptionCount());
				}

				setMenuOptionCount(getMenuOptionCount() + 1);
				tmpMenuOptionsCount = getMenuOptionCount();
			}

			menuEntry.setOption("");
			menuEntry.setTarget("");
			menuEntry.setIdentifier(0);
			menuEntry.setType(MenuAction.RUNELITE);
			menuEntry.setParam0(0);
			menuEntry.setParam1(0);
			menuEntry.setItemId(-1);
			menuEntry.setParent(null);
			menuEntry.setConsumer(null);

			return menuEntry;
		}
	}

	@Override
	public MenuEntry createMenuEntry(String option, String target, int identifier, int opcode, int param1, int param2, boolean forceLeftClick) {
		RSRuneLiteMenuEntry menuEntry = newBareRuneliteMenuEntry();

		menuEntry.setOption(option);
		menuEntry.setTarget(target);
		menuEntry.setIdentifier(identifier);
		menuEntry.setType(MenuAction.of(opcode));
		menuEntry.setParam0(param1);
		menuEntry.setParam1(param2);
		menuEntry.setParent(null);
		menuEntry.setConsumer(null);
		menuEntry.setForceLeftClick(forceLeftClick);

		return menuEntry;
	}


	public MenuEntry createMenuEntry(String option, String target, int identifier, int opcode, int param1, int param2, int itemId, boolean forceLeftClick)
	{
		RSRuneLiteMenuEntry menuEntry = newBareRuneliteMenuEntry();

		menuEntry.setOption(option);
		menuEntry.setTarget(target);
		menuEntry.setIdentifier(identifier);
		menuEntry.setType(MenuAction.of(opcode));
		menuEntry.setParam0(param1);
		menuEntry.setParam1(param2);
		menuEntry.setParent(null);
		menuEntry.setConsumer(null);
		menuEntry.setForceLeftClick(forceLeftClick);
		menuEntry.setItemId(itemId);

		return menuEntry;
	}


	private static final RSRuneLiteMenuEntry[] rl$menuEntries = new RSRuneLiteMenuEntry[500];

	@Override
	public MenuEntry[] getMenuEntries()
	{
		RSRuneLiteMenuEntry[] menuEntries = Arrays.copyOf(rl$menuEntries, Client.instance.getMenuOptionCount());

		for (RSRuneLiteMenuEntry menuEntry : menuEntries)
		{
			if (menuEntry.getOption() == null)
			{
				menuEntry.setOption("null");
			}

			if (menuEntry.getTarget() == null)
			{
				menuEntry.setTarget("null");
			}
		}

		return menuEntries;
	}


	@Override
	public int getMenuOptionCount() {
		return menuOptionsCount;
	}

	private static RSMenuAction tempMenuAction;

	public static void onMenuOptionsChanged(int idx)
	{
		int tmpOptionsCount = tmpMenuOptionsCount;
		int optionCount = Client.instance.getMenuOptionCount();

		tmpMenuOptionsCount = optionCount;

		if (optionCount < tmpOptionsCount)
		{
			for (int i = optionCount; i < tmpOptionsCount; ++i)
			{
				RSRuneLiteMenuEntry entry = rl$menuEntries[i];
				if (entry == null)
				{
					logger.error("about to crash: opcnt:{} tmpopcnt:{} i:{}", optionCount, tmpOptionsCount, i);
				}
				rl$menuEntries[i].setParent(null);
				rl$menuEntries[i].setConsumer(null);
			}
		}
		else if (optionCount == tmpOptionsCount + 1)
		{
			if (Client.instance.getMenuOptions()[tmpOptionsCount] == null)
			{
				Client.instance.getMenuOptions()[tmpOptionsCount] = "null";
			}

			if (Client.instance.getMenuTargets()[tmpOptionsCount] == null)
			{
				Client.instance.getMenuTargets()[tmpOptionsCount] = "null";
			}

			if (rl$menuEntries[tmpOptionsCount] == null)
			{
				rl$menuEntries[tmpOptionsCount] = newRuneliteMenuEntry(tmpOptionsCount);
			}
			else
			{
				rl$menuEntries[tmpOptionsCount].setConsumer(null);
				rl$menuEntries[tmpOptionsCount].setParent(null);
			}

			MenuEntryAdded menuEntryAdded = new MenuEntryAdded(
					rl$menuEntries[tmpOptionsCount]
			);

			Client.instance.getCallbacks().post(menuEntryAdded);

			if (menuEntryAdded.isModified() && Client.instance.getMenuOptionCount() == optionCount)
			{
				Client.instance.getMenuOptions()[tmpOptionsCount] = menuEntryAdded.getOption();
				Client.instance.getMenuTargets()[tmpOptionsCount] = menuEntryAdded.getTarget();
				Client.instance.getMenuIdentifiers()[tmpOptionsCount] = menuEntryAdded.getIdentifier();
				Client.instance.getMenuOpcodes()[tmpOptionsCount] = menuEntryAdded.getType();
				Client.instance.getMenuArguments1()[tmpOptionsCount] = menuEntryAdded.getActionParam0();
				Client.instance.getMenuArguments2()[tmpOptionsCount] = menuEntryAdded.getActionParam1();
				Client.instance.getMenuForceLeftClick()[tmpOptionsCount] = menuEntryAdded.isForceLeftClick();
			}
		}
	}

	@Override
	public RSMenuAction getTempMenuAction() {
		return tempMenuAction;
	}

	public void setMenuEntries(MenuEntry[] menuEntries) {
		boolean var2 = false;

		if (getTempMenuAction() != null && getMenuOptionCount() > 0)
		{
			var2 = getTempMenuAction().getParam0() == getMenuArguments1()[getMenuOptionCount() - 1] &&
					getTempMenuAction().getParam1() == getMenuArguments2()[getMenuOptionCount() - 1] &&
					getTempMenuAction().getOption().equals(getMenuOptions()[getMenuOptionCount() - 1]) &&
					getTempMenuAction().getIdentifier() == getMenuIdentifiers()[getMenuOptionCount() - 1] &&
					getTempMenuAction().getOpcode() == getMenuOpcodes()[getMenuOptionCount() - 1];
		}

		for (int i = 0; i < menuEntries.length; ++i)
		{
			RSRuneLiteMenuEntry menuEntry = (RSRuneLiteMenuEntry) menuEntries[i];
			if (menuEntry.getIdx() != i)
			{
				sortMenuEntries(menuEntry.getIdx(), i);
			}
		}

		setMenuOptionCount(menuEntries.length);
		tmpMenuOptionsCount = menuEntries.length;

		if (var2 && getMenuOptionCount() > 0)
		{
			getTempMenuAction().setParam0(getMenuArguments1()[getMenuOptionCount() - 1]);
			getTempMenuAction().setParam1(getMenuArguments2()[getMenuOptionCount() - 1]);
			getTempMenuAction().setOption(getMenuOptions()[getMenuOptionCount() - 1]);
			getTempMenuAction().setIdentifier(getMenuIdentifiers()[getMenuOptionCount() - 1]);
			getTempMenuAction().setOpcode(getMenuOpcodes()[getMenuOptionCount() - 1]);
			getCallbacks().post(WidgetPressed.INSTANCE);
		}
	}

	public void sortMenuEntries(int left, int right)
	{
		String menuOption = getMenuOptions()[left];
		getMenuOptions()[left] = getMenuOptions()[right];
		getMenuOptions()[right] = menuOption;

		String menuTarget = getMenuTargets()[left];
		getMenuTargets()[left] = getMenuTargets()[right];
		getMenuTargets()[right] = menuTarget;

		int menuIdentifier = getMenuIdentifiers()[left];
		getMenuIdentifiers()[left] = getMenuIdentifiers()[right];
		getMenuIdentifiers()[right] = menuIdentifier;

		int menuOpcode = getMenuOpcodes()[left];
		getMenuOpcodes()[left] = getMenuOpcodes()[right];
		getMenuOpcodes()[right] = menuOpcode;

		int menuArguments1 = getMenuArguments1()[left];
		getMenuArguments1()[left] = getMenuArguments1()[right];
		getMenuArguments1()[right] = menuArguments1;

		int menuArgument2 = getMenuArguments2()[left];
		getMenuArguments2()[left] = getMenuArguments2()[right];
		getMenuArguments2()[right] = menuArgument2;

		boolean menuForceLeftClick = getMenuForceLeftClick()[left];
		getMenuForceLeftClick()[left] = getMenuForceLeftClick()[right];
		getMenuForceLeftClick()[right] = menuForceLeftClick;

		RSRuneLiteMenuEntry var2 = rl$menuEntries[left];
		rl$menuEntries[left] = rl$menuEntries[right];
		rl$menuEntries[right] = var2;

		rl$menuEntries[left].setIdx(left);
		rl$menuEntries[right].setIdx(right);
	}

	public void setMenuOptionCount(int count) {
		this.menuOptionsCount = count;
	}

	public String[] getMenuOptions() {
		return menuActions;
	}

	public String[] getMenuTargets() {
		return menuTargets;
	}

	public int[] getMenuIdentifiers() {
		return menuIdentifiers;
	}

	public int[] getMenuOpcodes() {
		return menuOpcodes;
	}

	public int[] getMenuArguments1() {
		return menuArguments1;
	}

	public int[] getMenuArguments2() {
		return menuArguments2;
	}

	public boolean[] getMenuForceLeftClick() {
		return menuShiftClick;
	}

	public boolean isMenuOpen() {
		return isMenuOpen;
	}

	public int getMenuX() {
		return menuX;
	}

	public int getMenuY() {
		return menuY;
	}

	public int getMenuHeight() {
		return menuHeight;
	}

	public int getMenuWidth() {
		return menuWidth;
	}


	@Override
	public int getLeftClickOpensMenu() {
		return 0;
	}

	private static boolean printMenuActions = false;

	@Override
	public void setPrintMenuActions(boolean b) {
		printMenuActions = b;
	}

	@Override
	public void setMenuX(int x) {
		menuX = x;
	}

	@Override
	public void setMenuY(int y) {
		menuY = y;
	}

	@Override
	public void setMenuHeight(int h) {
		menuHeight = h;
	}

	@Override
	public void setMenuWidth(int w) {
		menuWidth = w;
	}


	/**
	 * Sub-menu
	 */

	private int clickedIdx = -1;

	private int submenuX = 0;

	private int submenuY = 0;

	private int submenuWidth = 0;

	private int submenuHeight = 0;

	private int submenuScroll = 0;

	private static int submenuScrollMax = 0;

	private int submenuIdx = -1;

	private int menuScroll = 0;

	private static int menuScrollMax = 0;


	public boolean isMenuScrollable() {
		return menuScrollMax > 0;
	}

	public int getMenuScroll()
	{
		return menuScroll;
	}

	public void setMenuScroll(int s) {
		menuScroll = Ints.constrainToRange(s, 0, menuScrollMax);
	}

	@Override
	public int getSubmenuX() {
		return submenuX;
	}

	@Override
	public MenuEntry getClickedMenuEntry() {
		MenuEntry toRet = null;
		if (clickedIdx != -1) {
			toRet = rl$menuEntries[clickedIdx];
			clickedIdx = -1;
		}
		return toRet;
	}

	@Override
	public int getSubmenuY() {
		return submenuY;
	}

	@Override public int getSubmenuWidth()
	{
		return submenuWidth;
	}

	@Override
	public int getSubmenuHeight() {
		return submenuHeight;
	}

	@Override
	public int getSubmenuScroll()
	{
		return submenuScroll;
	}

	@Override
	public int getSubmenuIdx() {
		return submenuIdx;
	}

	@Override
	public void setSubmenuX(int x) {
		submenuX = x;
	}

	@Override
	public void setSubmenuY(int y) {
		submenuY = y;
	}

	@Override
	public void setSubmenuWidth(int w) {
		submenuWidth = w;
	}

	@Override
	public void setSubmenuHeight(int h) {
		submenuHeight = h;
	}

	@Override
	public void setSubmenuScroll(int s) {
		submenuScroll = Ints.constrainToRange(s, 0, submenuScrollMax);
	}

	@Override
	public void setSubmenuScrollMax(int max) {
		submenuScrollMax = max;
	}

	public void setSubmenuIdx(int idx) {
		submenuIdx = idx;
	}
	/**
	 * End of sub-menu
	 */



}
