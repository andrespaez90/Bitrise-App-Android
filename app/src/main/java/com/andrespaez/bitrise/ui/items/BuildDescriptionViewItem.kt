package com.andrespaez.bitrise.ui.items

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.andrespaez.bitrise.databinding.ItemBuildsBinding
import com.andrespaez.bitrise.network.models.BuildsModel
import com.andrespaez.bitrise.ui.adapters.list.models.GenericItemView

class BuildDescriptionViewItem(context: Context) : GenericItemView<BuildsModel> {

    override lateinit var data: BuildsModel

    private val binding = ItemBuildsBinding.inflate(LayoutInflater.from(context))

    init {
        binding.root.layoutParams =
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
    }

    override fun bind(data: BuildsModel) {
        this.data = data
        binding.model = this.data
    }

    override fun setSelected(isSelected: Boolean) {
        binding.root.isSelected = isSelected
    }

    override fun getView(): View = binding.root

    fun setOnClickListener(action: (BuildEvents) -> Unit) {
        binding.root.setOnClickListener { action(BuildEvents.ViewBuild(data)) }
        binding.buttonAbort.setOnClickListener { action(BuildEvents.AbortBuild(data)) }
    }
}

sealed class BuildEvents {
    class AbortBuild(val data: BuildsModel) : BuildEvents()
    class ViewBuild(val data: BuildsModel) : BuildEvents()
}
