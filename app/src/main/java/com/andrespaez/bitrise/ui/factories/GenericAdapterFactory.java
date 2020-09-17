package com.andrespaez.bitrise.ui.factories;

import android.view.ViewGroup;

import com.andrespaez.bitrise.ui.adapters.list.models.GenericItemView;

public abstract class GenericAdapterFactory {

    public static final int TYPE_HEADER = 1001;
    public static final int TYPE_ITEM = 1002;
    public static final int TYPE_FOOTER = 1003;
    public static final int TYPE_CATEGORY = 1004;
    public static final int TYPE_DIVIDER = 1005;

    public GenericAdapterFactory() {
    }

    public abstract GenericItemView onCreateViewHolder(ViewGroup var1, int var2);

}
