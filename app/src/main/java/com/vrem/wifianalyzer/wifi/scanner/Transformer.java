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

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.model.WiFiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transformer {
    protected WiFiConnection transformWifiInfo(WifiInfo wifiInfo) {
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1) {
            return WiFiConnection.EMPTY;
        }
        return new WiFiConnection(
            WiFiUtils.convertSSID(wifiInfo.getSSID()),
            wifiInfo.getBSSID(),
            WiFiUtils.convertIpAddress(wifiInfo.getIpAddress()),
            wifiInfo.getLinkSpeed());
    }

    protected List<String> transformWifiConfigurations(List<WifiConfiguration> configuredNetworks) {
        List<String> results = new ArrayList<>();
        if (configuredNetworks != null) {
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                results.add(WiFiUtils.convertSSID(wifiConfiguration.SSID));
            }
        }
        return Collections.unmodifiableList(results);
    }

    protected List<WiFiDetail> transformCacheResults(List<CacheResult> cacheResults) {
        List<WiFiDetail> results = new ArrayList<>();
        if (cacheResults != null) {
            for (CacheResult cacheResult : cacheResults) {
                ScanResult scanResult = cacheResult.getScanResult();
                WiFiWidth wiFiWidth = WiFiWidth.find(scanResult.channelWidth);
                WiFiSignal wiFiSignal = new WiFiSignal(scanResult.frequency, wiFiWidth, cacheResult.getLevelAverage());
                WiFiDetail wiFiDetail = new WiFiDetail(scanResult.SSID, scanResult.BSSID, scanResult.capabilities, wiFiSignal);
                results.add(wiFiDetail);
            }
        }
        return Collections.unmodifiableList(results);
    }

    public WiFiData transformToWiFiData(List<CacheResult> cacheResults, WifiInfo wifiInfo, List<WifiConfiguration> configuredNetworks) {
        List<WiFiDetail> wiFiDetails = transformCacheResults(cacheResults);
        WiFiConnection wiFiConnection = transformWifiInfo(wifiInfo);
        List<String> wifiConfigurations = transformWifiConfigurations(configuredNetworks);
        return new WiFiData(wiFiDetails, wiFiConnection, wifiConfigurations);
    }

}
