/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DemoTest {

    public static final String BSSID = "99:99:99:99:99:99";

    @Before
    public void setUp() {
        Demo.INSTANCE.clear();
    }

    @After
    public void tearDown() {
        Demo.INSTANCE.clear();
    }

    @Test
    public void testGetSSID() throws Exception {
        assertEquals("SSID-01", Demo.INSTANCE.getSSID("123"));
        assertEquals("SSID-02", Demo.INSTANCE.getSSID(""));
        assertEquals("SSID-03", Demo.INSTANCE.getSSID("567"));
        assertEquals("SSID-01", Demo.INSTANCE.getSSID("123"));
    }

    @Test
    public void testGetBSSID() throws Exception {
        assertEquals("99:99:99:01:01:01", Demo.INSTANCE.getBSSID(BSSID, "123"));
        assertEquals("99:99:99:02:02:02", Demo.INSTANCE.getBSSID(BSSID, ""));
        assertEquals("99:99:99:03:03:03", Demo.INSTANCE.getBSSID(BSSID, "567"));
        assertEquals("99:99:99:01:01:01", Demo.INSTANCE.getBSSID(BSSID, "123"));
    }

}