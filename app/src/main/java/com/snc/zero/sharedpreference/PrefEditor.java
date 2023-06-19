package com.snc.zero.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.snc.zero.log.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

/**
 * SharedPreferences
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class PrefEditor {
	private static final String TAG = PrefEditor.class.getSimpleName();

	private SharedPreferences mPreference;
	private SharedPreferences.OnSharedPreferenceChangeListener mListener;

	public PrefEditor(Context context) {
		try {
			/*
			 * uses-sdk:minSdkVersion 23 cannot be smaller than version 24 declared in library
			 * Suggestion:
			 *   use a compatible library with a minSdk of at most 23,
			 *   or increase this project's minSdk version to at least 24,
			 *   or use tools:overrideLibrary="androidx.security.identity.credential" to force usage (may lead to runtime failures)
			 */
			KeyGenParameterSpec spec =  new KeyGenParameterSpec.Builder(
					MasterKey.DEFAULT_MASTER_KEY_ALIAS,
					KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
					.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
					.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
					.setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
					.build();

			MasterKey masterKey = new MasterKey.Builder(context)
					.setKeyGenParameterSpec(spec)
					.build();

			mPreference = EncryptedSharedPreferences.create(
					context.getApplicationContext(),
					context.getPackageName() + ".spref",
					masterKey,
					EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
					EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
			);

			//mPreference = context.getSharedPreferences(context.getPackageName() + ".pref", Context.MODE_PRIVATE);

		} catch (Exception e) {
			Logger.e(TAG, e);
		}
	}

	public boolean contains(String key) {
		return mPreference.contains(key);
	}

	public String getString(String key, String defValue) {
		if (!contains(key)) {
			return defValue;
		}
		try {
			return mPreference.getString(key, defValue);
		} catch (ClassCastException e) {
			return defValue;
		}
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = mPreference.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
		mPreference.registerOnSharedPreferenceChangeListener(listener);
		mListener = listener;
	}

	@SuppressWarnings("unused")
	public void unregisterOnSharedPreferenceChangeListener() {
		if (null == mListener) {
			return;
		}
		mPreference.unregisterOnSharedPreferenceChangeListener(mListener);
	}

}
