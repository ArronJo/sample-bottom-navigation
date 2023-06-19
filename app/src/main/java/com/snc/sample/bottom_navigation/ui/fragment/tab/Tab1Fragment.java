package com.snc.sample.bottom_navigation.ui.fragment.tab;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.ui.activity.MainActivity;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.fragment.BaseFragment;
import com.snc.zero.util.ActivityUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

/**
 * Tab 1 Fragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class Tab1Fragment extends BaseFragment {
    //private static final String TAG = Tab1Fragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tab_1, container, false);
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

        AppCompatButton button = view.findViewById(R.id.button);
        button.setOnClickListener(view1 -> {
            if (getParentActivity() instanceof MainActivity) {
                ((MainActivity) getParentActivity()).showTitleWeb();
            }
        });

        AppCompatButton button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(view1 -> {
            if (getParentActivity() instanceof MainActivity) {
                ((MainActivity) getParentActivity()).showTitleWeb2();
            }
        });

        AppCompatButton button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(view1 -> {
            if (getParentActivity() instanceof MainActivity) {
                ((MainActivity) getParentActivity()).showTitleWeb3();
            }
        });

        AppCompatButton button4 = view.findViewById(R.id.button4);
        button4.setOnClickListener(view1 -> {
            if (getParentActivity() instanceof MainActivity) {
                ((MainActivity) getParentActivity()).showPopup();
            }
        });

    }

}
