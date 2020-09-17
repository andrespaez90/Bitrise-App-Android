package com.andrespaez.bitrise.ui.items

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.andrespaez.bitrise.databinding.ItemAppListBinding
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.ui.adapters.list.models.GenericItemView

class AppDescriptionViewItem(context: Context) : GenericItemView<AppModel> {

    override lateinit var data: AppModel

    private val binding = ItemAppListBinding.inflate(LayoutInflater.from(context))

    init {
        binding.root.layoutParams =
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
    }

    override fun bind(data: AppModel) {
        this.data = data
        binding.model = this.data
    }

    override fun setSelected(isSelected: Boolean) {
        binding.root.isSelected = isSelected
    }

    override fun getView(): View = binding.root

    fun setOnClickListener(action: (AppModel) -> Unit) {
        binding.root.setOnClickListener { action(data) }
    }
}