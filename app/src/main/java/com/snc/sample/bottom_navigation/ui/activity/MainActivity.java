package com.snc.sample.bottom_navigation.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.snc.sample.bottom_navigation.BuildConfig;
import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.sharedpreference.PrefConst;
import com.snc.sample.bottom_navigation.ui.fragment.coach.Coach1Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.coach.Coach2Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.onboarding.OnBoardingFragment;
import com.snc.sample.bottom_navigation.ui.fragment.popup.PopupFragment;
import com.snc.sample.bottom_navigation.ui.fragment.splash.SplashFragment;
import com.snc.sample.bottom_navigation.ui.fragment.tab.Tab1Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.tab.Tab2Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.tab.Tab3Fragment;
import com.snc.sample.bottom_navigation.ui.fragment.web.WebFragment;
import com.snc.zero.activity.BaseActivity;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.fragment.BaseFragment;
import com.snc.zero.fragment.helper.FragmentHelper;
import com.snc.zero.fragment.helper.TabHelper;
import com.snc.zero.log.Logger;
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

    private TabHelper mTabHelper;

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

    private void showNavigation(int tabPosition) {
        showNavigation();
        mTabHelper.showTab(tabPosition);
    }

    //////////////////////////////////////////////

    private void makeNavigationTabs() {
        final List<Fragment> fragmentList = new ArrayList<>();

        BaseFragment tab1Fragment = new Tab1Fragment();
        tab1Fragment.setMessageListener((fragment, command, params) -> {

        });
        fragmentList.add(tab1Fragment);

        BaseFragment tab2Fragment = new Tab2Fragment();
        tab2Fragment.setMessageListener((fragment, command, params) -> {

        });
        fragmentList.add(tab2Fragment);

        BaseFragment tab3Fragment = new Tab3Fragment();
        tab3Fragment.setMessageListener((fragment, command, params) -> {

        });
        fragmentList.add(tab3Fragment);

        mTabHelper = new TabHelper(getActivity(), R.id.nav_host);
        mTabHelper.setFragmentList(fragmentList);
    }

    //////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentHelper = new FragmentHelper(getActivity());

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

                // Tab 이외 Fragment 에 이벤트 전달
                if (mFragmentHelper.size() > 0) {
                    if (mFragmentHelper.getCurrentFragment() instanceof BaseFragment) {
                        if (((BaseFragment) mFragmentHelper.getCurrentFragment()).onKeyDown(keyCode, event)) {
                            return true;
                        }
                    }
                }

                // 현재 Tab Fragment 에 이벤트 전달
                if (null != mTabHelper.getCurrentTab()) {
                    if (((BaseFragment) mTabHelper.getCurrentTab()).onKeyDown(keyCode, event)) {
                        return true;
                    }
                }

                // 앱 종료 처리
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
        makeNavigationTabs();

        final BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setItemTextColor(ResourceUtil.getColorStateList(getContext(), R.color.selector_black));
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
        splashFragment.setFragmentHelper(mFragmentHelper);
        splashFragment.setMessageListener((fragment, command, params) -> {
            if ("remove".equalsIgnoreCase(command)) {
                try {
                    mFragmentHelper.remove(fragment);
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
        onboardingFragment.setFragmentHelper(mFragmentHelper);
        onboardingFragment.setMessageListener((fragment, command, params) -> {
            if ("remove".equalsIgnoreCase(command)) {
                //mFragmentHelper.remove(onboardingFragment);
                doCheckPermissions();
            }
        });

        mFragmentHelper.show(R.id.base_layout, onboardingFragment);

        mOnboardingFragment = onboardingFragment;
    }

    private void hideOnBoardingFragment() {
        if (null != mOnboardingFragment) {
            mFragmentHelper.remove(mOnboardingFragment);
            mOnboardingFragment = null;
        }
    }

    //////////////////////////////////////////////

    public void showTitleWeb() {
        Logger.i(TAG, "showTitleWeb()");

        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("title", "네이버");
        intent.putExtra("url", "https://m.naver.com");
        startActivity(intent);
    }

    public void showTitleWeb2() {
        Logger.i(TAG, "showTitleWeb2()");

        final WebFragment titleWebFragment = new WebFragment();
        titleWebFragment.setFragmentHelper(mFragmentHelper);
        titleWebFragment.setTitle("네이버");
        titleWebFragment.setUrl("https://m.naver.com");
        titleWebFragment.setMessageListener((fragment, command, params) -> {
            if ("finish".equalsIgnoreCase(command))
            mFragmentHelper.remove(fragment);
        });

        mFragmentHelper.show(R.id.base_layout, titleWebFragment);
    }

    public void showTitleWeb3() {
        Logger.i(TAG, "showTitleWeb3()");

        Intent intent = new Intent(getActivity(), WebFragmentActivity.class);
        intent.putExtra("title", "네이버");
        intent.putExtra("url", "https://m.naver.com");
        startActivity(intent);
    }

    public void showPopup() {
        Logger.i(TAG, "showPopup()");

        final PopupFragment popupFragment = new PopupFragment();
        popupFragment.setFragmentHelper(mFragmentHelper);
        popupFragment.setTitle("네이버");
        popupFragment.setUrl("https://m.naver.com");

        mFragmentHelper.show(R.id.base_layout, popupFragment);
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

            hideCoachGuide(coachGuideView);

            UIStatusBar.setWhiteColorOnBackground(getActivity());
        });
    }

    private void hideCoachGuide(View view) {
        getRootView().removeView(view);
    }

    //////////////////////////////////////////////

    private void doCheckPermissions() {
        List<String> permissions = new ArrayList<>();
        // Dangerous Permission
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        doCheckPermissions(permissions);
    }

    private void doCheckPermissions(List<String> permissions) {
        doCheckPermissions(permissions, new RPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Logger.i(TAG, "onPermissionGranted()");

                PrefEditor pref = new PrefEditor(getContext());
                pref.putString(PrefConst.DID_ON_BOARDING, "true");

                if (BuildConfig.FEATURE_ONBOARDING) {
                    hideOnBoardingFragment();
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
        });
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