package com.bitrise.app.ui.adapters.list;


import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitrise.app.ui.adapters.list.models.CategoryItem;
import com.bitrise.app.ui.adapters.list.models.GenericCategoryItem;
import com.bitrise.app.ui.adapters.list.models.GenericItem;
import com.bitrise.app.ui.adapters.list.models.GenericItemView;
import com.bitrise.app.ui.factories.GenericAdapterFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GenericAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String defaultCategory;

    protected List<GenericItem> items;

    protected GenericAdapterFactory factory;

    protected boolean categoryEnable;

    private String other;

    public GenericAdapter(GenericAdapterFactory factory) {
        this.defaultCategory = "Otro";
        this.categoryEnable = false;
        this.factory = factory;
        this.items = new ArrayList();
        this.other = "Otro";
    }

    public GenericAdapter(GenericAdapterFactory factory, boolean categoryenable) {
        this(factory);
        this.categoryEnable = categoryenable;
    }

    public GenericAdapter(GenericAdapterFactory factory, String other) {
        this.defaultCategory = "Otro";
        this.categoryEnable = false;
        this.factory = factory;
        this.items = new ArrayList();
        this.other = other;
        this.categoryEnable = true;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GenericItemView item = this.factory.onCreateViewHolder(parent, viewType);

        View view = item.getView() != null ? item.getView() : (View) item;

        return new ItemViewHolder(view, item);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder item = (ItemViewHolder) holder;
        item.itemModel.bind(getItem(position).getData());
    }

    protected GenericItem getItem(int position) {
        return (GenericItem) this.items.get(position);
    }

    public int getItemViewType(int position) {
        return this.getItem(position).getType();
    }

    public int getItemCount() {
        return this.items != null ? this.items.size() : 0;
    }

    public void setCategoryEnable(boolean categoryEnable) {
        this.categoryEnable = categoryEnable;
        this.notifyDataSetChanged();
    }

    public void setItems(List<? extends GenericItem> items) {
        this.items = new ArrayList();
        if (items != null) {
            int i = 0;

            for (int size = items.size(); i < size; ++i) {
                GenericItem genericItem = (GenericItem) items.get(i);
                this.addNewItem(genericItem);
            }
        } else {
            this.items = new ArrayList();
        }

        this.notifyDataSetChanged();
    }

    private void addNewItem(GenericItem genericItem) {
        if (this.categoryEnable && genericItem instanceof GenericCategoryItem) {
            GenericCategoryItem categoryItem = (GenericCategoryItem) genericItem;
            this.addCategories(categoryItem);
        } else {
            this.items.add(genericItem);
        }

    }

    private void addCategories(GenericCategoryItem item) {
        String category = TextUtils.isEmpty(item.getCategoryName()) ? this.other : item.getCategoryName();
        int categoryIndex = this.indexCategoryOf(category);
        if (categoryIndex != -1) {
            this.items.add(categoryIndex + 1, item);
        } else {
            GenericCategoryItem newCategory = this.getCategoryItem(category);
            int indexNewCategory = this.getNewCategoryIndex(newCategory);
            this.items.add(indexNewCategory, newCategory);
            this.items.add(indexNewCategory + 1, item);
        }

    }

    protected CategoryItem getCategoryItem(String categoryName) {
        return new CategoryItem(categoryName);
    }

    private int indexCategoryOf(String category) {
        int i = 0;

        for (int size = this.items.size(); i < size; ++i) {
            GenericItem item = this.getItem(i);
            if (item.getType() == 1004) {
                GenericCategoryItem categoryItem = (GenericCategoryItem) item;
                if (categoryItem.getData().equals(category)) {
                    return i;
                }
            }
        }

        return -1;
    }

    private int getNewCategoryIndex(GenericCategoryItem newCategory) {
        int itemSize = this.items.size();

        for (int i = 0; i < itemSize; ++i) {
            GenericItem item = this.getItem(i);
            if (item.getType() == 1004) {
                GenericCategoryItem categoryItem = (GenericCategoryItem) item;
                if (categoryItem.compareTo(newCategory) >= 0) {
                    return i;
                }
            }
        }

        return itemSize;
    }

    public void updateCategories() {
        this.removeCategories();
        this.setItems(new ArrayList(this.items));
    }

    private void removeCategories() {
        Iterator itemIterator = this.items.iterator();

        while (itemIterator.hasNext()) {
            GenericItem item = (GenericItem) itemIterator.next();
            if (item.getType() == 1004) {
                itemIterator.remove();
            }
        }

    }

    public void update(GenericItem item) {
        int index = this.items.indexOf(item);
        if (index != -1) {
            this.items.set(index, item);
            this.notifyItemChanged(index);
        }

    }

    public void remove(GenericItem item) {
        int index = this.items.indexOf(item);
        if (index != -1) {
            this.items.remove(index);
            this.notifyItemRemoved(index);
            this.removeEmptyCategory(index);
        }

    }

    private void removeEmptyCategory(int index) {
        if (this.categoryEnable) {
            GenericItem prevItem = null;
            GenericItem nextItem = null;
            if (index < this.items.size()) {
                nextItem = (GenericItem) this.items.get(index);
            }

            if (index > 0) {
                prevItem = (GenericItem) this.items.get(index - 1);
            }

            if (prevItem != null && prevItem instanceof CategoryItem && (nextItem == null || nextItem instanceof CategoryItem)) {
                this.items.remove(index - 1);
                this.notifyItemRemoved(index - 1);
            }
        }

    }

    public void removeItemAtPosition(int position) {
        if (position >= 0 && position < this.items.size()) {
            this.items.remove(position);
            this.notifyItemRemoved(position);
        }

    }

    public void removeItemsAtPosition(int position, int itemCount) {
        if (position >= 0 && position < this.items.size()) {
            for (int i = 0; i < itemCount; ++i) {
                this.items.remove(position);
            }

            this.notifyItemRangeRemoved(position, itemCount);
        }

    }

    public void addItem(GenericItem item) {
        this.addNewItem(item);
        this.notifyDataSetChanged();
    }

    public void addItems(List<? extends GenericItem> items) {
        if (items != null) {
            int i = 0;

            for (int size = items.size(); i < size; ++i) {
                this.addNewItem((GenericItem) items.get(i));
            }

            this.notifyDataSetChanged();
        }

    }

    public void addItems(int index, List<? extends GenericItem> itemsToAdd) {
        if (itemsToAdd == null) {
            itemsToAdd = new ArrayList();
        }

        this.items.addAll(index, (Collection) itemsToAdd);
        this.notifyItemInserted(index);
    }

    public void addItem(int index, GenericItem item) {
        if (this.items == null) {
            this.items = new ArrayList();
        }

        this.items.add(index, item);
        this.notifyItemInserted(index);
    }

    public void clearAll() {
        if (this.items != null) {
            this.items.clear();
            this.notifyDataSetChanged();
        }

    }

    public void clear() {
        if (this.items != null) {
            Iterator i = this.items.iterator();

            while (i.hasNext()) {
                GenericItem itemView = (GenericItem) i.next();
                if (itemView.getType() != 1001 && itemView.getType() != 1003) {
                    i.remove();
                }
            }

            this.notifyDataSetChanged();
        }

    }

    public int getItemPosition(GenericItem item) {
        int itemPosition = -1;
        if (this.items != null && !this.items.isEmpty()) {
            itemPosition = this.items.indexOf(item);
        }

        return itemPosition;
    }

    public GenericItem getItemAtPosition(int position) {
        GenericItem genericItem = null;
        if (this.items != null && this.items.size() > position) {
            genericItem = this.getItem(position);
        }

        return genericItem;
    }

    public void notifyItemWithDataChanged(Object data) {
        GenericItem genericItem = this.getItemWithData(data);
        if (genericItem != null) {
            this.notifyItemChanged(this.getItemPosition(genericItem));
        }

    }

    public GenericItem getItemWithData(Object data) {
        if (this.items != null) {
            Iterator i = this.items.iterator();

            while (i.hasNext()) {
                GenericItem itemView = (GenericItem) i.next();
                if (itemView.getData() == data) {
                    return itemView;
                }
            }
        }

        return null;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private GenericItemView itemModel;

        private ItemViewHolder(@NonNull View itemView, GenericItemView itemModel) {
            super(itemView);
            this.itemModel = itemModel;
        }
    }
}