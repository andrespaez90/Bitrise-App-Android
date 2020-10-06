package com.bitrise.app.ui.binding

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.bitrise.app.R
import com.bitrise.app.network.models.Owners

object OrganizationBinding {

    @JvmStatic
    @BindingAdapter("organization_owners")
    fun setOrganizationOwners(container: LinearLayout, owners: List<Owners>?) {
        container.removeAllViews()
        owners?.map {
            container.addView(ImageView(container.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    context.resources.getDimensionPixelOffset(R.dimen.spacing_huge),
                    context.resources.getDimensionPixelOffset(R.dimen.spacing_huge)
                ).apply {
                    setMargins(
                        context.resources.getDimensionPixelOffset(R.dimen.spacing_micro),
                        context.resources.getDimensionPixelOffset(R.dimen.spacing_empty),
                        context.resources.getDimensionPixelOffset(R.dimen.spacing_micro),
                        context.resources.getDimensionPixelOffset(R.dimen.spacing_empty)
                    )
                }

                setImageResource(R.mipmap.ic_default_avatar)

            })
        }
    }
}