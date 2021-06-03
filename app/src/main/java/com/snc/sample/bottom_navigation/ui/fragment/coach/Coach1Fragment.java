package com.snc.sample.bottom_navigation.ui.fragment.coach;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snc.sample.bottom_navigation.R;
import com.snc.zero.fragment.BaseFragment;
import com.snc.zero.resource.ResourceUtil;

import androidx.annotation.NonNull;

/**
 * Coach1 Fragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class Coach1Fragment extends BaseFragment {
	//private static final String TAG = Coach1Fragment.class.getSimpleName();

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		final View view = inflater.inflate(R.layout.fragment_coach1, container, false);

		final TextView mainTextView = view.findViewById(R.id.main_text);
		final SpannableStringBuilder ssb = new SpannableStringBuilder(mainTextView.getText());
		final String keyword = "동해 물과 백두산";
		int start = mainTextView.getText().toString().indexOf(keyword);
		int end = start + keyword.length();
		if (start >= 0) {
			ssb.setSpan(new ForegroundColorSpan(ResourceUtil.getColor(getContext(), R.color.colorAccent)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		mainTextView.setText(ssb);
		return view;
	}

}
