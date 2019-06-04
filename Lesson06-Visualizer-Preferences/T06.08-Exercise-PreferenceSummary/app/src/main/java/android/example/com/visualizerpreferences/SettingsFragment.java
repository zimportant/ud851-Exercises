package android.example.com.visualizerpreferences;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

// TODO (1) Implement OnSharedPreferenceChangeListener
public class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_visualizer);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
		SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        int numberOfPreferences = getPreferenceScreen().getPreferenceCount();

        for (int i = 0; i < numberOfPreferences; ++i) {
            Preference preference = preferenceScreen.getPreference(i);
            updateSummaryByValue(preference, sharedPreferences);
        }
    }

    void updateSummaryByValue(Preference preference, SharedPreferences sharedPreferences) {
		if (preference instanceof ListPreference) {
			String value = sharedPreferences.getString(preference.getKey(), "");
			setPreferenceSummary(preference, value);
		}
	}

    void setPreferenceSummary(Preference preference, String value) {
    	if (preference instanceof ListPreference) {
    	    ListPreference listPreference = (ListPreference) preference;
    	    int preferenceIndex = listPreference.findIndexOfValue(value);
    	    if (preferenceIndex >= 0) {
    	    	CharSequence summary = listPreference.getEntries()[preferenceIndex];
    	        listPreference.setSummary(summary);
            }
        }
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	Preference preference = findPreference(key);
		updateSummaryByValue(preference, sharedPreferences);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

}