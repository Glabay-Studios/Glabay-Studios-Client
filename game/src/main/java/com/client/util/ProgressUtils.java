package com.client.util;

import me.tongfei.progressbar.ProgressBar;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ProgressUtils {

    public static ProgressBar progress(String task, int amt) {
        return new ProgressBar(task, (long) amt);
    }


}
