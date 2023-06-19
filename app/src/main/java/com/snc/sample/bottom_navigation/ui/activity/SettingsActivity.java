package com.snc.sample.bottom_navigation.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.databinding.ActivitySettingsBinding;
import com.snc.sample.bottom_navigation.ui.fragment.settings.SettingPreferenceFragment;
import com.snc.zero.activity.BaseActivity;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.fragment.helper.FragmentHelper;
import com.snc.zero.log.Logger;
import com.snc.zero.util.ActivityUtil;

import androidx.databinding.DataBindingUtil;

/**
 * SettingsActivity
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class SettingsActivity extends BaseActivity {
    private static final String TAG = WebActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding(getActivity());
        init();

        FragmentHelper fragmentHelper = new FragmentHelper(getActivity());
        fragmentHelper.show(R.id.contentView, new SettingPreferenceFragment());
    }

    @SuppressLint("SetTextI18n")
    private void binding(Activity activity) {
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(activity, R.layout.activity_settings);
        binding.topLayout.title.setText("text");
        binding.topLayout.rightText.setText("text");
    }

    private void init() {
        final ViewGroup contentView = findViewById(R.id.contentView);
        if (null == contentView) {
            DialogBuilder.with(getActivity())
                    .setMessage("The contentView does not exist.")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> ActivityUtil.finish(getActivity()))
                    .show();
            return;
        }

        ImageView closeView = findViewById(R.id.close);
        closeView.setOnClickListener(v1 -> {
            Logger.i(TAG, "onclick : closeView");
            finish();
        });

        ImageView backView = findViewById(R.id.back);
        backView.setOnClickListener(v1 -> {
            Logger.i(TAG, "onclick : backView");
            finish();
        });
    }
}