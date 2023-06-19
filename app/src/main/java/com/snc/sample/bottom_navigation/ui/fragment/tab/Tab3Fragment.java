package com.snc.sample.bottom_navigation.ui.fragment.tab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.databinding.FragmentTab3Binding;
import com.snc.sample.bottom_navigation.ui.activity.SettingsActivity;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.fragment.BaseFragment;
import com.snc.zero.util.ActivityUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * Tab 3 Fragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class Tab3Fragment extends BaseFragment {
    //private static final String TAG = Tab3Fragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return binding(inflater, container);
    }

    @SuppressLint("SetTextI18n")
    private View binding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        FragmentTab3Binding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_3, container, false);
        binding.bottomLayout.button.setText("text");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @SuppressLint("AddJavascriptInterface")
    private void init(View view) {
        final ViewGroup contentView = view.findViewById(R.id.contentView);
        if (null == contentView) {
            DialogBuilder.with(getActivity())
                    .setMessage("The contentView does not exist.")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> ActivityUtil.finish(getActivity()))
                    .show();
            return;
        }

        ImageView ivSettings = contentView.findViewById(R.id.settings);
        ivSettings.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });
    }
}
