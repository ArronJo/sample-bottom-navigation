package com.snc.zero.fragment.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.snc.zero.log.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * CSBottomSheetFragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class CSBottomSheetFragment extends BottomSheetDialogFragment {
   private static final String TAG = CSBottomSheetFragment.class.getSimpleName();

   private DialogInterface.OnCancelListener mOnCancelListener;

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       Logger.i(TAG, "onViewCreated");
       super.onViewCreated(view, savedInstanceState);
   }

   @Override
   public void onDismiss(@NonNull DialogInterface dialog) {
       Logger.i(TAG, "onDismiss");
       super.onDismiss(dialog);
   }

   @Override
   public void onCancel(@NonNull DialogInterface dialog) {
       Logger.i(TAG, "onCancel");
       super.onCancel(dialog);

       if (null != mOnCancelListener) {
           mOnCancelListener.onCancel(dialog);
       }
   }

   public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
       mOnCancelListener = listener;
   }

}
