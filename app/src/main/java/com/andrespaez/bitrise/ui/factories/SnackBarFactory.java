package com.andrespaez.bitrise.ui.factories;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.andrespaez.bitrise.R;
import com.andrespaez.bitrise.ui.models.ColoredSnackbar;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class SnackBarFactory {

    public static final String TYPE_INFO = "type_info";
    public static final String TYPE_ERROR = "type_error";

    public static Snackbar getSnackBar(@SnackBarType String snackBarType, @NonNull View view, @StringRes int stringId, int duration) {
        Snackbar snackbar = Snackbar.make(view, stringId, duration);
        return createSnackBar(snackBarType, snackbar);
    }

    public static Snackbar getSnackBar(@SnackBarType String snackBarType, @NonNull View view, @NonNull CharSequence text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        return createSnackBar(snackBarType, snackbar);
    }

    public static Snackbar getSnackBar(@ColorInt int color, @NonNull View view, @NonNull CharSequence text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        return createSnackBar(color, snackbar);
    }

    public static Snackbar getSnackBarWithAction(@SnackBarType String snackBarType, @NonNull View view,
                                                 @NonNull CharSequence text, String actionText,
                                                 View.OnClickListener onClickListener) {
        return createSnackBar(snackBarType, Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(!TextUtils.isEmpty(actionText) ? actionText : "Reintentar", onClickListener)
                .setActionTextColor(Color.WHITE));
    }

    private static Snackbar createSnackBar(@SnackBarType String snackBarType, Snackbar snackbar) {
        switch (snackBarType) {
            case TYPE_INFO:
                return ColoredSnackbar.draw(snackbar, ContextCompat.getColor(snackbar.getContext(), R.color.colorAccent));
            case TYPE_ERROR:
                return ColoredSnackbar.draw(snackbar, ContextCompat.getColor(snackbar.getContext(), R.color.colorSecondary));
            default:
                return snackbar;
        }
    }

    private static Snackbar createSnackBar(int color, Snackbar snackbar) {
        return ColoredSnackbar.draw(snackbar, color);
    }

    @Retention(SOURCE)
    @StringDef({TYPE_INFO, TYPE_ERROR})
    public @interface SnackBarType {
    }
}