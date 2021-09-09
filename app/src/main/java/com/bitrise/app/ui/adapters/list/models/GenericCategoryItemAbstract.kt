package com.bitrise.app.ui.adapters.list.models

class GenericCategoryItemAbstract(
    override var data: Any,
    override val categoryName: String,
    override val type: Int = 0,
    override val weight: Int = 0,
) : GenericCategoryItem<Any> {

    override fun compareTo(var1: GenericCategoryItem<*>): Int {
        return categoryName.compareTo(var1.categoryName)
    }
}