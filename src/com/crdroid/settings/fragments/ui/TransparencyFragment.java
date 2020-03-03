/*
 * Copyright (C) 2016-2019 crDroid Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crdroid.settings.fragments.ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.crdroid.settings.preferences.CustomSeekBarPreference;
import com.crdroid.settings.preferences.colorpicker.ColorPickerPreference;

import com.android.internal.logging.nano.MetricsProto;

public class TransparencyFragment extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String PREF_TRANSPARENT_VOLUME_DIALOG = "transparent_volume_dialog";
    private static final String PREF_TRANSPARENT_ACTIONS_DIALOG = "transparent_actions_dialog";

    private CustomSeekBarPreference mVolumeDialogAlpha;
    private CustomSeekBarPreference mActionsDialogAlpha;

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CRDROID_SETTINGS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.transparency_layout);

        PreferenceScreen prefSet = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        // Volume dialog alpha
        mVolumeDialogAlpha =
                (CustomSeekBarPreference) findPreference(PREF_TRANSPARENT_VOLUME_DIALOG);
        int volumeDialogAlpha = Settings.System.getInt(resolver,
                Settings.System.TRANSPARENT_VOLUME_DIALOG, 100);
        mVolumeDialogAlpha.setValue(volumeDialogAlpha / 1);
        mVolumeDialogAlpha.setOnPreferenceChangeListener(this);

        // Power menu alpha
        mActionsDialogAlpha =
                (CustomSeekBarPreference) findPreference(PREF_TRANSPARENT_ACTIONS_DIALOG);
        int actionsDialogAlpha = Settings.System.getInt(resolver,
                Settings.System.TRANSPARENT_ACTIONS_DIALOG, 100);
        mActionsDialogAlpha.setValue(actionsDialogAlpha / 1);
        mActionsDialogAlpha.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mVolumeDialogAlpha) {
            int alpha = (Integer) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.TRANSPARENT_VOLUME_DIALOG, alpha * 1);
            return true;
        } else if (preference == mActionsDialogAlpha) {
            int alpha = (Integer) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.TRANSPARENT_ACTIONS_DIALOG, alpha * 1);
            return true;
        }
        return false;
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Settings.System.putIntForUser(resolver,
                Settings.System.TRANSPARENT_VOLUME_DIALOG, 100, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.TRANSPARENT_ACTIONS_DIALOG, 100, UserHandle.USER_CURRENT);
    }
}
