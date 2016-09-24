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

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.model.WiFiUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Transformer {

    final static String SSID_FORMAT = "SSID-%02d";
    private static int Count = 0;
    private final Map<String, String> cache = new TreeMap<>();

    WiFiConnection transformWifiInfo(WifiInfo wifiInfo) {
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1) {
            return WiFiConnection.EMPTY;
        }
        return new WiFiConnection(
            WiFiUtils.convertSSID(wifiInfo.getSSID()),
            wifiInfo.getBSSID(),
            WiFiUtils.convertIpAddress(wifiInfo.getIpAddress()),
            wifiInfo.getLinkSpeed());
    }

    List<String> transformWifiConfigurations(List<WifiConfiguration> configuredNetworks) {
        List<String> results = new ArrayList<>();
        if (configuredNetworks != null) {
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                results.add(getDemoSSID(WiFiUtils.convertSSID(wifiConfiguration.SSID)));
            }
        }
        return Collections.unmodifiableList(results);
    }

    List<WiFiDetail> transformCacheResults(List<CacheResult> cacheResults) {
        List<WiFiDetail> results = new ArrayList<>();
        if (cacheResults != null) {
            for (CacheResult cacheResult : cacheResults) {
                ScanResult scanResult = cacheResult.getScanResult();
                WiFiSignal wiFiSignal = new WiFiSignal(scanResult.frequency, getWiFiWidth(scanResult), cacheResult.getLevelAverage());
                String demoSSID = getDemoSSID(scanResult.SSID);
                String demoBSSID = getDemoBSSID(scanResult.BSSID, demoSSID);
                WiFiDetail wiFiDetail = new WiFiDetail(demoSSID, demoBSSID, scanResult.capabilities, wiFiSignal);
                results.add(wiFiDetail);
            }
        }
        return Collections.unmodifiableList(results);
    }

    private WiFiWidth getWiFiWidth(ScanResult scanResult) {
        try {
            Field declaredField = scanResult.getClass().getDeclaredField(Fields.channelWidth.name());
            return WiFiWidth.find((int) declaredField.get(scanResult));
        } catch (Exception e) {
            return WiFiWidth.MHZ_20;
        }
    }

    public WiFiData transformToWiFiData(List<CacheResult> cacheResults, WifiInfo wifiInfo, List<WifiConfiguration> configuredNetworks) {
        List<WiFiDetail> wiFiDetails = transformCacheResults(cacheResults);
        WiFiConnection wiFiConnection = transformWifiInfo(wifiInfo);
        List<String> wifiConfigurations = transformWifiConfigurations(configuredNetworks);
        return new WiFiData(wiFiDetails, wiFiConnection, wifiConfigurations);
    }

    String getDemoSSID(@NonNull String SSID) {
        String demoSSID = cache.get(SSID);
        if (demoSSID == null) {
            demoSSID = String.format(SSID_FORMAT, Count++);
            cache.put(SSID, demoSSID);
        }
        return demoSSID;
    }

    String getDemoBSSID(@NonNull String BSSID, @NonNull String demoSSID) {
        String replacement = demoSSID.substring(demoSSID.length() - 2);
        return BSSID.substring(0, BSSID.length() - 8)
            + replacement + ":" + replacement + ":" + replacement;
    }

    private enum Fields {
        /*
                centerFreq0,
                centerFreq1,
        */
        channelWidth
    }
}
