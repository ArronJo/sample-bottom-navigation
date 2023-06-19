package com.snc.sample.bottom_navigation.ui.fragment.settings;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.snc.sample.bottom_navigation.R;
import com.snc.zero.log.Logger;
import com.snc.zero.sharedpreference.PrefEditor;
import com.snc.zero.util.PackageUtil;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Settings Preference Fragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class SettingPreferenceFragment extends PreferenceFragmentCompat {
    private static final String TAG = SettingPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey);

        init();
    }

    private void init() {
        PrefEditor pref = new PrefEditor(getContext());
        pref.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> Logger.i(TAG, "sharedPreferences change: key[" + key + "]"));


        try {
            String displayVersion = PackageUtil.getPackageDisplayVersion(getContext());
            Preference versionPref = (Preference) findPreference("versionPref");
            if (null != versionPref) {
                versionPref.setTitle(displayVersion);
                if ("".equals(displayVersion)) {
                    versionPref.setDependency("최신 버전으로 업데이트 하세요.");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    // Preference의 클릭을 감지합니다. 보통 일반 Preference를 버튼대용으로 쓰기 위해 사용합니다.
    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        Logger.i(TAG,"클릭된 Preference 의 key 는 "+key);
        return false;
    }
}
