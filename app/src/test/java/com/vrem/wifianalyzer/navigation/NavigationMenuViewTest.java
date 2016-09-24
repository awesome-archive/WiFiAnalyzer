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

package com.vrem.wifianalyzer.navigation;

import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NavigationMenuViewTest {
    private MainActivity mainActivity;
    private NavigationMenuView fixture;
    private NavigationView navigationView;

    @Before
    public void setUp() throws Exception {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        fixture = mainActivity.getNavigationMenuView();
        navigationView = fixture.getNavigationView();
    }

    @After
    public void tearDown() throws Exception {
        fixture.setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
    }

    @Test
    public void testNavigationMenuView() throws Exception {
        // execute
        Menu menu = navigationView.getMenu();
        // validate
        assertEquals(NavigationMenu.values().length, menu.size());

        for (NavigationGroup navigationGroup : NavigationGroup.values()) {
            for (NavigationMenu navigationMenu : navigationGroup.navigationMenu()) {
                MenuItem actual = menu.getItem(navigationMenu.ordinal());
                assertEquals(navigationGroup.ordinal(), actual.getGroupId());
                assertEquals(mainActivity.getResources().getString(navigationMenu.getTitle()), actual.getTitle());
                assertEquals(navigationMenu.ordinal(), actual.getItemId());
                assertEquals(navigationMenu.ordinal(), actual.getOrder());
            }
        }
    }

    @Test
    public void testGetCurrentMenuItem() throws Exception {
        // setup
        MenuItem expected = navigationView.getMenu().getItem(NavigationMenu.ACCESS_POINTS.ordinal());
        // execute
        MenuItem actual = fixture.getCurrentMenuItem();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCurrentNavigationMenu() throws Exception {
        // execute
        NavigationMenu actual = fixture.getCurrentNavigationMenu();
        // validate
        assertEquals(NavigationMenu.ACCESS_POINTS, actual);
    }

    @Test
    public void testSetCurrentNavigationMenu() throws Exception {
        // setup
        NavigationMenu expected = NavigationMenu.CHANNEL_GRAPH;
        // execute
        fixture.setCurrentNavigationMenu(expected);
        // validate
        assertEquals(expected, fixture.getCurrentNavigationMenu());
    }

    @Test
    public void testFindCurrentNavigationMenu() throws Exception {
        // setup
        NavigationMenu expected = NavigationMenu.CHANNEL_RATING;
        // execute
        NavigationMenu actual = fixture.findNavigationMenu(expected.ordinal());
        // validate
        assertEquals(expected, actual);
    }

}