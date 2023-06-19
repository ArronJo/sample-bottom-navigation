package com.snc.sample.bottom_navigation.binding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.databinding.BindingAdapter;

/**
 * Data Binding Adapter
 *
 * https://developer.android.com/topic/libraries/data-binding/binding-adapters.html#automatic-setting
 */
@SuppressWarnings("unused")
public class CSBindingAdapter {

    /*
    package androidx.databinding.adapters.ImageViewBindingAdapter.class

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(imageUri));
        }
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }
     */

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.get().load(url).error(error).into(view);
    }

    @BindingAdapter("imgRes")
    public static void setImageDrawable(ImageView view, int drawableId) {
        if (drawableId != 0) {
            view.setImageResource(drawableId);
        }
    }

    // app:imgDrawable="@{imgDrawable}"
    @BindingAdapter("imgDrawable")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        if (null == drawable) {
            return;
        }
        view.setImageDrawable(drawable);
    }
}
