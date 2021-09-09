package com.bitrise.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bitrise.app.R
import com.bitrise.app.databinding.FragmentProfileBinding
import com.bitrise.app.network.models.Organization
import com.bitrise.app.network.models.Profile
import com.bitrise.app.network.models.UserActivity
import com.bitrise.app.ui.adapters.list.GenericAdapter
import com.bitrise.app.ui.adapters.list.models.GenericItemAbstract
import com.bitrise.app.ui.factories.AppListFactory
import com.bitrise.app.ui.factories.ITEM_ORGANIZATION
import com.bitrise.app.ui.factories.ITEM_USER_ACTIVITY
import com.bitrise.app.viewModels.home.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_profile,
            null,
            true
        )
        binding.lifecycleOwner = this
        initView()
        initViewModel()
        return binding.root
    }

    private fun initView() {
        binding.recyclerViewUserActivity.run {
            adapter = GenericAdapter(AppListFactory())
        }
        binding.recyclerViewOrganizations.run {
            adapter = GenericAdapter(AppListFactory())
        }
    }

    private fun initViewModel() {
        viewModel.updateUserProfile(requireActivity().intent.extras?.get("profile") as Profile?)
        binding.viewModel = viewModel
        viewModel.startActivity().observe(viewLifecycleOwner, ::startActivity)
        viewModel.onOrganizationsChange.observe(viewLifecycleOwner, ::updateOrganizations)
        viewModel.onActivityChange.observe(viewLifecycleOwner, ::updateRecentActivity)
    }

    private fun updateOrganizations(organizations: List<Organization>) {
        binding.containerOrganizations.isVisible = organizations.isNotEmpty()
        (binding.recyclerViewOrganizations.adapter as GenericAdapter).items =
            organizations.map { GenericItemAbstract(it, ITEM_ORGANIZATION) }
    }

    private fun updateRecentActivity(userActivity: List<UserActivity>) {
        binding.containerActivity.isVisible = userActivity.isNotEmpty()
        (binding.recyclerViewUserActivity.adapter as GenericAdapter).items =
            userActivity.map { GenericItemAbstract(it, ITEM_USER_ACTIVITY) }
    }
}