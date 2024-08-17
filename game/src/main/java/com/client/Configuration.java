package com.client;

import java.time.LocalDateTime;

public class Configuration {

	public static boolean MOUSE_CAM = true;

    public static final boolean ZBUFF = false;
    public static int SERVICES_PORT = 43594;

	/**
	 * Client version is a number that will tell the server whether
	 * the player has the most up-to-date client, otherwise they
	 * will receive an error on login to update their client.
	 */
	public static final int CLIENT_VERSION = 230;


	/**
	 * The server version. The cache path is append with a _v1/2/3 etc for the version number
	 * to prevent overwriting older version caches.
	 * This should only be changed when a new server is launched, otherwise change {@link Configuration#CLIENT_VERSION}.
	 */
	public static final int SERVER_VERSION = 2;


	public static final String CLIENT_TITLE = "Xeros";
	public static final String WEBSITE = "Xeros.io";
	public static final String DEDICATED_SERVER_ADDRESS = "127.0.0.1";
	public static final String DEDICATED_JS5_SERVER_ADDRESS = "127.0.0.1";
	public static final int PORT = 52777;
	public static final int TEST_PORT = 43594;
	public static final int CACHE_FOLDER_VERSION = 0;
	public static final String CACHE_NAME = "." + CLIENT_TITLE.toLowerCase() + (CACHE_FOLDER_VERSION != 0 ? "_" + CACHE_FOLDER_VERSION : "") + "_v" + SERVER_VERSION;
	public static final String CACHE_NAME_DEV = CACHE_NAME + "_dev";
	public static boolean developerMode;
	public static boolean packIndexData;
	public static boolean dumpMaps;
	public static boolean dumpAnimationData;
	public static boolean dumpDataLists;
	public static boolean newFonts; // TODO text offsets (i.e. spacing between characters) are incorrect, needs automatic fix from kourend
	public static String cacheName = CACHE_NAME;
	public static String clientTitle = "";

	public static final String ERROR_LOG_DIRECTORY = "error_logs/";

	/**
	 * Attack option priorities 0 -> Depends on combat level 1 -> Always right-click
	 * 2 -> Left-click where available 3 -> Hidden
	 */
	public static int playerAttackOptionPriority;
	public static int npcAttackOptionPriority = 2;

	public static final boolean DUMP_SPRITES = false;
	public static final boolean PRINT_EMPTY_INTERFACE_SECTIONS = false;

	public static boolean playerNames;

	/**
	 * Seasonal Events
	 */
	public static boolean HALLOWEEN;
	public static boolean CHRISTMAS;
	public static boolean CHRISTMAS_EVENT;
	public static boolean EASTER;

	public static boolean osbuddyGameframe;

	public static int xpPosition;
	public static boolean escapeCloseInterface;
	public static boolean alwaysLeftClickAttack;
	public static boolean hideCombatOverlay;
}
