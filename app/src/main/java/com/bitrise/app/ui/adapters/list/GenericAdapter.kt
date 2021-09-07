package com.bitrise.app.ui.adapters.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitrise.app.ui.adapters.list.models.CategoryItem
import com.bitrise.app.ui.adapters.list.models.GenericCategoryItem
import com.bitrise.app.ui.adapters.list.models.GenericItem
import com.bitrise.app.ui.adapters.list.models.GenericItemView
import com.bitrise.app.ui.factories.GenericAdapterFactory

class GenericAdapter(
    private val factory: GenericAdapterFactory,
    private var categoryEnable: Boolean = false,
    val defaultCategory: String = "Other",

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var items: MutableList<GenericItem<*>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item: GenericItemView<*> = factory.onCreateViewHolder(parent, viewType)
        val view: View = item.getView() ?: item as View
        return ItemViewHolder(view, item)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder as ItemViewHolder
        view.itemModel.binds(getItem(position).data)
    }

    protected fun getItem(position: Int): GenericItem<*> = this.items[position]

    override fun getItemCount(): Int =
        if (this::items.isInitialized) items.size else 0

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    /**
     * Set Configuration
     */

    fun setCategory(categoryEnable: Boolean) {
        this.categoryEnable = categoryEnable
        notifyDataSetChanged()
    }

    /**
     * Set Items
     */

    fun setItems(newItems: List<GenericItem<Any>>) {
        items = mutableListOf()
        newItems.forEach { addNewItem(it) }
        notifyDataSetChanged()
    }

    private fun addNewItem(genericItem: GenericItem<*>) {
        if (categoryEnable && genericItem is GenericCategoryItem<*>) {
            addItemInCategory(genericItem)
        } else items.add(genericItem)
    }

    private fun addItemInCategory(item: GenericCategoryItem<*>) {
        val categoryName = item.categoryName ?: defaultCategory
        val categoryIndex = indexOfCategory(categoryName)
        if (categoryIndex == -1) items.add(categoryIndex + 1, item)
        else items[addCategoryItem(createCategoryItem(categoryName)) + 1] = item
    }

    private fun indexOfCategory(categoryName: String): Int =
        if (this::items.isInitialized) {
            items.indexOfFirst { it is CategoryItem && it.categoryName == categoryName }
        } else -1


    /**
     * Add Category
     */

    private fun addCategoryItem(categoryItem: CategoryItem): Int {
        val categoryIndex: Int = if (this::items.isInitialized) {
            items.indexOfFirst { it is CategoryItem && it >= categoryItem }
        } else items.size
        items.add(if (categoryIndex == -1) items.size else categoryIndex, categoryItem)
        return categoryIndex
    }

    /**
     * Add Item
     */

    fun addItem(item: GenericItem<*>) {
        addNewItem(item)
        notifyDataSetChanged()
    }

    /**
     * Remove Item
     */

    open fun remove(item: GenericItem<*>) {
        val index = items.indexOf(item)
        if (index != -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
            if (categoryEnable) removeEmptyCategory(index)
        }
    }

    fun removeItemAtPosition(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
            if (categoryEnable) removeEmptyCategory(position)
        }
    }

    private fun removeEmptyCategory(index: Int) {
        var prevItem: GenericItem<*>? = null
        var nextItem: GenericItem<*>? = null
        if (index < items.size) {
            nextItem = items[index]
        }
        if (index > 0) {
            prevItem = items[index - 1]
        }

        if (prevItem != null && prevItem is CategoryItem && (nextItem == null || nextItem is CategoryItem)) {
            items.removeAt(index - 1)
            notifyItemRemoved(index - 1)
        }
    }

    /**
     * Generic
     */

    protected open fun createCategoryItem(categoryName: String): CategoryItem =
        CategoryItem(categoryName)

    /**
     * Item Wrapper
     */

    private class ItemViewHolder(
        itemView: View,
        val itemModel: GenericItemView<*>,
    ) : RecyclerView.ViewHolder(itemView)
}