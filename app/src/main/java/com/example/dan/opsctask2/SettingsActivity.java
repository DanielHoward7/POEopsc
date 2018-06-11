package com.example.dan.opsctask2;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by Dan on 5/11/2018.
 */

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainSettingsFragment()).commit();
    }

    public static class MainSettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            bindSummaryValue(findPreference("key_name"));
            bindSummaryValue(findPreference("key_email"));
            bindSummaryValue(findPreference("key_height"));
            bindSummaryValue(findPreference("key_weight"));
            bindSummaryValue(findPreference("key_weight_goal"));
            bindSummaryValue(findPreference("key_steps_goal"));

        }
    }

    public static void bindSummaryValue(Preference preference){
      preference.setOnPreferenceChangeListener(listener);
      listener.onPreferenceChange(preference,
              PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    public static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index > 0 ? listPreference.getEntries()[index] : null);
            } else if (preference instanceof EditTextPreference) {
                preference.setSummary(stringValue);
            }



            return true;
        }
    };

}
