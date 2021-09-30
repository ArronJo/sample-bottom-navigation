package com.snc.sample.bottom_navigation.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.snc.sample.bottom_navigation.BuildConfig;
import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.sharedpreference.PrefConst;
import com.snc.sample.bottom_navigation.ui.fragment.coach.Coach1Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.coach.Coach2Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.tab.Tab1Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.tab.Tab2Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.tab.Tab3Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.onboarding.OnBoardingFragment;
import com.snc.sample.bottom_navigation.ui.fragment.splash.SplashFragment;
import com.snc.zero.activity.BaseActivity;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.fragment.BaseFragment;
import com.snc.zero.fragment.helper.FragmentHelper;
import com.snc.zero.log.Logger;
import com.snc.zero.permission.RPermission;
import com.snc.zero.permission.RPermissionListener;
import com.snc.zero.resource.ResourceUtil;
import com.snc.zero.sharedpreference.PrefEditor;
import com.snc.zero.util.ActivityUtil;
import com.snc.zero.util.BackPressedUtil;
import com.snc.zero.util.PackageUtil;
import com.snc.zero.util.StringUtil;
import com.snc.zero.widget.statusbar.UIStatusBar;
import com.snc.zero.widget.viewpager.PageItem;
import com.snc.zero.widget.viewpager.ViewPager2Adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * MainActivity
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FragmentHelper mFragmentHelper;
    private SplashFragment mSplashFragment;
    private OnBoardingFragment mOnboardingFragment;

    //////////////////////////////////////////////

    @SuppressLint("NonConstantResourceId")
    private boolean showNavigationFromItemId(int itemId) {
        switch (itemId) {
            case R.id.nav_my_contract: showNavigation(0); return true;
            case R.id.nav_product: showNavigation(1); return true;
            case R.id.nav_menu: showNavigation(2); return true;
            default: return false;
        }
    }

    private void showNavigation(int position) {
        showNavigation();
        mFragmentHelper.show(position);
    }

    //////////////////////////////////////////////

    private BaseFragment getCurrentFragment() {
        if (mFragmentHelper.getCurrentFragment() instanceof BaseFragment) {
            return (BaseFragment) mFragmentHelper.getCurrentFragment();
        }
        return null;
    }

    //////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentHelper = new FragmentHelper(getActivity(), R.id.nav_host);

        initNavigation();
        hideNavigation();

        if (BuildConfig.FEATURE_SPLASH) {
            showSplashFragment();
        } else if (BuildConfig.FEATURE_ONBOARDING) {
            PrefEditor pref = new PrefEditor(getContext());
            if (!"true".equals(pref.getString(PrefConst.DID_ON_BOARDING, ""))) {
                showOnBoardingFragment();
                return;
            }
            //doCheckPermissions();
            startApplication();
        } else {
            startApplication();
        }
    }

    //////////////////////////////////////////////

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            if (KeyEvent.KEYCODE_BACK == keyCode) {
                if (null != mSplashFragment) {
                    return true;
                }

                if (null != getCurrentFragment()) {
                    if (getCurrentFragment().onKeyDown(keyCode, event)) {
                        return true;
                    }
                }

                if (BackPressedUtil.onBackPressed(getActivity())) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //////////////////////////////////////////////

    @SuppressLint("NonConstantResourceId")
    private void initNavigation() {
        final List<Fragment> fragmentList = new ArrayList<>();
        BaseFragment tab1Fragment = new Tab1Fragment();
        BaseFragment tab2Fragment = new Tab2Fragment();
        BaseFragment tab3Fragment = new Tab3Fragment();

        tab1Fragment.setFragmentHelper(mFragmentHelper);
        tab2Fragment.setFragmentHelper(mFragmentHelper);
        tab3Fragment.setFragmentHelper(mFragmentHelper);

        fragmentList.add(tab1Fragment);
        fragmentList.add(tab2Fragment);
        fragmentList.add(tab3Fragment);

        mFragmentHelper.setFragmentList(fragmentList);

        final BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setItemTextColor(ResourceUtil.getColorStateList(getContext(), R.color.selector_black));
        //navigationView.setOnNavigationItemSelectedListener(item -> {
        //    Logger.i(TAG, "Selected Item = " + item);
        //    return showNavigationFromItemId(item.getItemId());
        //});
        navigationView.setOnItemSelectedListener(item -> {
            Logger.i(TAG, "Selected Item = " + item);
            return showNavigationFromItemId(item.getItemId());
        });

        PrefEditor pref = new PrefEditor(getContext());
        String loginInfo = pref.getString(PrefConst.GET_LOGIN_INFO, "");
        if (StringUtil.isEmpty(loginInfo)) {
            navigationView.setSelectedItemId(R.id.nav_product);
        } else {
            navigationView.setSelectedItemId(R.id.nav_my_contract);
        }
    }

    private void hideNavigation() {
        findViewById(R.id.nav_title).setVisibility(View.GONE);
        findViewById(R.id.nav_view).setVisibility(View.GONE);
        findViewById(R.id.nav_host).setVisibility(View.GONE);
    }

    private void showNavigation() {
        //findViewById(R.id.nav_title).setVisibility(View.VISIBLE);
        findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        findViewById(R.id.nav_host).setVisibility(View.VISIBLE);
    }

    //////////////////////////////////////////////

    private void showSplashFragment() {
        Logger.i(TAG, "showSplashFragment()");
        final SplashFragment splashFragment = new SplashFragment();
        splashFragment.setInteractionListener((fragment, command, params) -> {
            if ("remove".equalsIgnoreCase(command)) {
                try {
                    mFragmentHelper.remove(mSplashFragment);
                    mSplashFragment = null;

                    if (BuildConfig.FEATURE_ONBOARDING) {
                        PrefEditor pref = new PrefEditor(getContext());
                        if (!"true".equals(pref.getString(PrefConst.DID_ON_BOARDING, ""))) {
                            showOnBoardingFragment();
                            return;
                        }
                    }

                    startApplication();

                } catch (Exception e) {
                    Logger.e(TAG, e);

                    postDelayed(() -> ActivityUtil.killProcess(getActivity()), 500);
                }
            }
        });

        mFragmentHelper.show(R.id.base_layout, splashFragment);

        mSplashFragment = splashFragment;
    }

    //////////////////////////////////////////////

    private void showOnBoardingFragment() {
        Logger.i(TAG, "showOnBoardingFragment()");
        final OnBoardingFragment onboardingFragment = new OnBoardingFragment();
        onboardingFragment.setInteractionListener((fragment, command, params) -> {
            if ("remove".equalsIgnoreCase(command)) {
                //mFragmentHelper.remove(onboardingFragment);
                doCheckPermissions();
            }
        });

        mFragmentHelper.show(R.id.base_layout, onboardingFragment);

        mOnboardingFragment = onboardingFragment;
    }

    //////////////////////////////////////////////

    private void showCoachGuide() {
        UIStatusBar.setBlackColorOnBackground(getActivity());

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_coach_guide, getRootView());
        final View coachGuideView = view.findViewById(R.id.coachGuide);
        final ViewPager2 viewPager2 = coachGuideView.findViewById(R.id.viewPager2);

        final List<PageItem> items = new ArrayList<>();
        final Coach1Fragment coach1 = new Coach1Fragment();
        final Coach2Fragment coach2 = new Coach2Fragment();
        items.add(new PageItem(coach1));
        items.add(new PageItem(coach2));

        final ViewPager2Adapter adapter = new ViewPager2Adapter(getActivity());
        adapter.setItems(items);
        viewPager2.setAdapter(adapter);

        View child = viewPager2.getChildAt(0);
        if (child instanceof RecyclerView) {
            child.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        ImageView iv = coachGuideView.findViewById(R.id.close);
        iv.setOnClickListener(v1 -> {
            PrefEditor pref = new PrefEditor(getContext());
            pref.putString(PrefConst.DID_ON_COACH_GUIDE, "true");

            getRootView().removeView(coachGuideView);

            UIStatusBar.setWhiteColorOnBackground(getActivity());
        });
    }

    //////////////////////////////////////////////

    private void doCheckPermissions() {
        List<String> permissions = new ArrayList<>();
        // Dangerous Permission
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        doCheckPermissions(permissions);
    }

    private void doCheckPermissions(List<String> permissions) {
        RPermission.with(getContext())
                .setPermissionListener(new RPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Logger.i(TAG, "onPermissionGranted()");

                        PrefEditor pref = new PrefEditor(getContext());
                        pref.putString(PrefConst.DID_ON_BOARDING, "true");

                        if (BuildConfig.FEATURE_ONBOARDING) {
                            if (null != mOnboardingFragment) {
                                mFragmentHelper.remove(mOnboardingFragment);
                                mOnboardingFragment = null;
                            }
                        }

                        startApplication();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions, int status) {
                        Logger.e(TAG, "onPermissionDenied()..." + deniedPermissions.toString());

                        if (0 == status) {
                            doCheckPermissions(deniedPermissions);
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

    //////////////////////////////////////////////

    private void startApplication() {
        showNavigation();

        if (BuildConfig.FEATURE_COACH) {
            PrefEditor pref = new PrefEditor(getContext());
            if (!"true".equals(pref.getString(PrefConst.DID_ON_COACH_GUIDE, ""))) {
                showCoachGuide();
            }
        }
    }

    //////////////////////////////////////////////

}