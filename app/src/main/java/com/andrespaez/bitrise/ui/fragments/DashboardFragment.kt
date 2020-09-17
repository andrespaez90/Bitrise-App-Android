package com.andrespaez.bitrise.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrespaez.bitrise.R
import com.andrespaez.bitrise.databinding.FragmentDashboardBinding
import com.andrespaez.bitrise.network.models.AppModel
import com.andrespaez.bitrise.ui.adapters.list.GenericAdapter
import com.andrespaez.bitrise.ui.adapters.list.models.GenericItemAbstract
import com.andrespaez.bitrise.ui.extensions.hideKeyboard
import com.andrespaez.bitrise.ui.factories.AppListFactory
import com.andrespaez.bitrise.ui.factories.ITEM_APP_SELECTOR
import com.andrespaez.bitrise.ui.items.DividerItemDecoration
import com.andrespaez.bitrise.viewModels.home.DashboardViewModel
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
        (binding.recyclerViewList.adapter as GenericAdapter).setItems(
            newApps.sortedBy { it.isDisabled }
                .map { GenericItemAbstract(it, ITEM_APP_SELECTOR) }
        )
    }

}