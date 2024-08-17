package com.client.utilities;

import com.client.Configuration;
import com.client.sign.Signlink;
import com.sun.management.HotSpotDiagnosticMXBean;

import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Misc {

	private static final DecimalFormat NUMBER_FORMAT_2 = new DecimalFormat("#.##");
	private static final DecimalFormat NUMBER_FORMAT_0 = new DecimalFormat("#");

	static {
		NUMBER_FORMAT_2.setRoundingMode(RoundingMode.DOWN);
		NUMBER_FORMAT_0.setRoundingMode(RoundingMode.DOWN);
	}

    public static String formatNumber(double number) {
        return NumberFormat.getInstance().format(number);
    }
	
	private static String OS = null;

	public static String getOsName() {
		if (OS == null) {
			OS = System.getProperty("os.name");
		}
		return OS;
	}

	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	public static String getUnderscoredNumber(int number) {
		String string = String.valueOf(number);
		for (int i = string.length() - 3; i > 0; i -= 3) {
			string = string.substring(0, i) + "_" + string.substring(i);
		}
		return string;
	}

	public static String format(long amount) {
		if (amount >= 1_000_000_000) {
			return NUMBER_FORMAT_2.format(((double) amount) / 1_000_000_000d) + "B";
		} else if (amount >= 100_000_000) {
			return NUMBER_FORMAT_0.format(((double) amount) / 100_000_000d) + "M";
		} else if (amount >= 1_000_000) {
			return NUMBER_FORMAT_2.format(((double) amount) / 1_000_000d) + "M";
		} else if (amount >= 100_000) {
			return NUMBER_FORMAT_0.format(((double) amount) / 1_000d) + "K";
		} else if (amount >= 1_000) {
			return NUMBER_FORMAT_2.format(((double) amount) / 1_000d) + "K";
		}

		return "" + amount;
	}

	public static void dumpHeap(boolean live) {
		try {
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
					server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
			String outputFile = Signlink.getCacheDirectory() + Configuration.ERROR_LOG_DIRECTORY +
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_hh-mm")) + ".hprof";
			mxBean.dumpHeap(outputFile, live);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}