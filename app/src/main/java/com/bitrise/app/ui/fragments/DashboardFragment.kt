package com.bitrise.app.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitrise.app.R
import com.bitrise.app.databinding.FragmentDashboardBinding
import com.bitrise.app.network.models.AppModel
import com.bitrise.app.ui.adapters.list.GenericAdapter
import com.bitrise.app.ui.adapters.list.models.GenericItemAbstract
import com.bitrise.app.ui.extensions.hideKeyboard
import com.bitrise.app.ui.factories.AppListFactory
import com.bitrise.app.ui.factories.ITEM_APP_SELECTOR
import com.bitrise.app.ui.items.DividerItemDecoration
import com.bitrise.app.viewModels.home.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

    private val viewModel: DashboardViewModel by viewModels()

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_dashboard,
            null,
            true
        )
        initViews()
        subscribeViewModel()
        initListeners()
        return binding.root
    }

    private fun initViews() {
        binding.lifecycleOwner = this
        binding.recyclerViewList.run {
            adapter = GenericAdapter(AppListFactory {
                if (it is AppModel) viewModel.openBuildsApp(it)
            })
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    resources.getDimensionPixelSize(
                        R.dimen.spacing_micro
                    )
                )
            )
        }
    }

    private fun initListeners() {
        binding.layoutRefresh.setOnRefreshListener { viewModel.updateApps() }
        binding.editTextSearch.addTextChangedListener(afterTextChanged = { viewModel.findApp(it.toString()) })
        binding.editTextSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    v.hideKeyboard()
                    return true
                }
                return false
            }
        })
    }

    private fun subscribeViewModel() {
        viewModel.startActivity().observe(viewLifecycleOwner, ::startActivity)
        viewModel.loaderState().observe(viewLifecycleOwner, binding.layoutRefresh::setRefreshing)
        viewModel.onAppsChange.observe(viewLifecycleOwner, ::updateApps)
    }

    private fun updateApps(newApps: List<AppModel>) {
        (binding.recyclerViewList.adapter as GenericAdapter).items =
            newApps.sortedBy { it.isDisabled }
                .map { GenericItemAbstract(it, ITEM_APP_SELECTOR) }
    }

}