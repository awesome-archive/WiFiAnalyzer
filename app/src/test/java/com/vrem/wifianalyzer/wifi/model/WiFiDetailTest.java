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

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class WiFiDetailTest {
    private static final int FREQUENCY = 2435;
    private static final int LEVEL = -40;
    private static final String VENDOR_NAME = "VendorName";
    private static final String WPA = "WPA";
    private static final String SSID = "xyzSSID";
    private static final String BSSID = "xyzBSSID";

    private WiFiSignal wiFiSignal;
    private WiFiAdditional wiFiAdditional;
    private WiFiDetail fixture;

    @Before
    public void setUp() {
        wiFiAdditional = new WiFiAdditional(VENDOR_NAME, false);
        wiFiSignal = new WiFiSignal(FREQUENCY, FREQUENCY, WiFiWidth.MHZ_20, LEVEL);
        fixture = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal, wiFiAdditional);
    }

    @Test
    public void testWiFiDetail() throws Exception {
        // validate
        assertEquals(wiFiSignal, fixture.getWiFiSignal());
        assertEquals(wiFiAdditional, fixture.getWiFiAdditional());
        assertEquals(SSID, fixture.getSSID());
        assertEquals(BSSID, fixture.getBSSID());
        assertEquals(WPA, fixture.getCapabilities());
        assertEquals(SSID + " (" + BSSID + ")", fixture.getTitle());
        assertEquals(Security.WPA, fixture.getSecurity());
        assertFalse(fixture.isHidden());
    }

    @Test
    public void testGetTitleWithEmptySSID() throws Exception {
        // setup
        fixture = new WiFiDetail(StringUtils.EMPTY, BSSID, WPA, wiFiSignal);
        // validate
        assertEquals("*** (" + BSSID + ")", fixture.getTitle());
    }

    @Test
    public void testEquals() throws Exception {
        // setup
        WiFiDetail other = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        // setup
        WiFiDetail other = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }

    @Test
    public void testCompareTo() throws Exception {
        // setup
        WiFiDetail other = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal);
        // execute & validate
        assertEquals(0, fixture.compareTo(other));
    }

    @Test
    public void testIsHidden() throws Exception {
        // setup
        fixture = new WiFiDetail(StringUtils.EMPTY, BSSID, WPA, wiFiSignal);
        // execute & validate
        assertTrue(fixture.isHidden());
    }

    @Test
    public void testWiFiDetailCopyConstructor() throws Exception {
        // setup
        WiFiDetail expected = new WiFiDetail(StringUtils.EMPTY, BSSID, WPA, wiFiSignal);
        // execute
        WiFiDetail actual = new WiFiDetail(expected, expected.getWiFiAdditional());
        // validate
        assertEquals(expected, actual);
        assertEquals(expected.getSSID(), actual.getSSID());
        assertEquals(expected.getBSSID(), actual.getBSSID());
        assertEquals(expected.getCapabilities(), actual.getCapabilities());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getSecurity(), actual.getSecurity());
        assertEquals(expected.isHidden(), actual.isHidden());
        assertEquals(expected.getWiFiAdditional(), actual.getWiFiAdditional());
        assertEquals(expected.getWiFiSignal(), actual.getWiFiSignal());
    }

}