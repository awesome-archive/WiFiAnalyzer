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

import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import java.util.Map;
import java.util.TreeMap;

public class Scanner {
    private final Map<String, UpdateNotifier> updateNotifiers;
    private final WifiManager wifiManager;
    private final Transformer transformer;
    private Cache cache;
    private PeriodicScan periodicScan;

    public Scanner(@NonNull WifiManager wifiManager, @NonNull Handler handler, @NonNull Settings settings, @NonNull Transformer transformer) {
        this.wifiManager = wifiManager;
        this.updateNotifiers = new TreeMap<>();
        this.transformer = transformer;
        this.setCache(new Cache());
        this.periodicScan = new PeriodicScan(this, handler, settings);
    }

    public void update() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (wifiManager.startScan()) {
            cache.add(wifiManager.getScanResults());
            WiFiData wiFiData = transformer.transformToWiFiData(cache.getScanResults(), wifiManager.getConnectionInfo(), wifiManager.getConfiguredNetworks());
            for (String key : updateNotifiers.keySet()) {
                UpdateNotifier updateNotifier = updateNotifiers.get(key);
                updateNotifier.update(wiFiData);
            }
        }
    }

    public void addUpdateNotifier(@NonNull UpdateNotifier updateNotifier) {
        String key = updateNotifier.getClass().getName();
        updateNotifiers.put(key, updateNotifier);
    }

    public void pause() {
        periodicScan.stop();
    }

    public boolean isRunning() {
        return periodicScan.isRunning();
    }

    public void resume() {
        periodicScan.start();
    }

    protected PeriodicScan getPeriodicScan() {
        return periodicScan;
    }

    protected void setPeriodicScan(@NonNull PeriodicScan periodicScan) {
        this.periodicScan = periodicScan;
    }

    protected void setCache(@NonNull Cache cache) {
        this.cache = cache;
    }

    protected Map<String, UpdateNotifier> getUpdateNotifiers() {
        return updateNotifiers;
    }

}
