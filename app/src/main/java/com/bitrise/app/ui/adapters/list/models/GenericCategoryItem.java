package com.bitrise.app.ui.adapters.list.models;

public interface GenericCategoryItem<T> extends GenericItem<T> {

    String getCategoryName();

    int compareTo(GenericCategoryItem var1);
}
