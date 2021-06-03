package com.snc.sample.bottom_navigation.ui.fragment.onboarding;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.sharedpreference.PrefConst;
import com.snc.sample.bottom_navigation.ui.fragment.bottomsheet.PermissionBottomSheetFragment;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.fragment.BaseFragment;
import com.snc.zero.log.Logger;
import com.snc.zero.permission.RPermission;
import com.snc.zero.permission.RPermissionListener;
import com.snc.zero.resource.ResourceUtil;
import com.snc.zero.sharedpreference.PrefEditor;
import com.snc.zero.util.ActivityUtil;
import com.snc.zero.util.PackageUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * OnBoarding Fragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class OnBoardingFragment extends BaseFragment {
    private static final String TAG = OnBoardingFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        final LottieAnimationView animView = view.findViewById(R.id.animation_view);
        final Button button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            animView.pauseAnimation();

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            PermissionBottomSheetFragment bottomSheetFragment = new PermissionBottomSheetFragment();
            bottomSheetFragment.setCancelable(true);
            bottomSheetFragment.setOnClickListener((dialog, which) -> checkPermission());
            bottomSheetFragment.setOnCancelListener(dialog -> checkPermission());
            bottomSheetFragment.show(fragmentManager, "permission_notice");
        });
    }

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        // Dangerous Permission
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        checkPermission(permissions, null);
    }

    private void checkPermission(List<String> permissions, String rationalMessage) {
        RPermission.with(getContext())
                .setRationalMessage(rationalMessage)
                .setPermissionListener(new RPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Logger.i(TAG, "onPermissionGranted()");

                        PrefEditor pref = new PrefEditor(requireContext());
                        pref.putString(PrefConst.DID_ON_BOARDING, "true");

                        if (null != getOnFragmentInteractionListener()) {
                            getOnFragmentInteractionListener().onInteraction(getFragment(), "remove", null);
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions, int status) {
                        Logger.e(TAG, "onPermissionDenied()..." + deniedPermissions.toString());

                        if (RPermission.SHOW_REQUEST_RATIONALE == status) {
                            checkPermission(deniedPermissions, ResourceUtil.getString(requireContext(), R.string.permission_rational_message));
                        } else {
                            showPermissionNotice();
                        }
                    }
                })
                .setPermissions(permissions)
                .check();
    }

    private void showPermissionNotice() {
        DialogBuilder.with(getActivity())
                .setMessage(R.string.permission_notice_message)
                .setPositiveButton(R.string.permission_notice_goto_settings, (dialog, which) -> {
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.parse("package:" + PackageUtil.getPackageName(getContext())));
                            startActivity(intent);
                        } catch (Exception e) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            startActivity(intent);
                        }
                    })
                .setNegativeButton(R.string.app_exit, (dialog, which) -> postDelayed(() -> ActivityUtil.killProcess(getActivity()), 500))
                .show();
    }

}
