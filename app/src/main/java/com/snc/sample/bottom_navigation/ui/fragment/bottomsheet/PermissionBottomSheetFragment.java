package com.snc.sample.bottom_navigation.ui.fragment.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.snc.sample.bottom_navigation.R;
import com.snc.zero.fragment.bottomsheet.CSBottomSheetFragment;
import com.snc.zero.log.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Permission Guide BottomSheetDialogFragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class PermissionBottomSheetFragment extends CSBottomSheetFragment {
    private static final String TAG = PermissionBottomSheetFragment.class.getSimpleName();

    private DialogInterface.OnClickListener mOnClickListener;

    public PermissionBottomSheetFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_permission_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        Button button = view.findViewById(R.id.button);
        if (null != button) {
            button.setOnClickListener(v -> {
                dismiss();

                if (null != mOnClickListener) {
                    mOnClickListener.onClick(getDialog(), 1);
                }
            });
        }
    }

    public void setOnClickListener(DialogInterface.OnClickListener listener) {
        mOnClickListener = listener;
    }

}
