/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.TurboFilterList;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class LogDebugReject {

    public static void debugReject() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        TurboFilterList turboFilterList = loggerContext.getTurboFilterList();
        TurboFilter turboFilter = new TurboFilter() {
            @Override
            public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable) {
                if (level.levelStr.equalsIgnoreCase("debug")) {
                    return FilterReply.DENY;
                }
                return FilterReply.ACCEPT;
            }
        };
        turboFilterList.add(turboFilter);
    }
}
