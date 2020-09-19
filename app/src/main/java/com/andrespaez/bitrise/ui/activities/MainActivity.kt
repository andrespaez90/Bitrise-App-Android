package com.andrespaez.bitrise.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.andrespaez.bitrise.BuildConfig
import com.andrespaez.bitrise.R
import com.andrespaez.bitrise.databinding.ActivityMainBinding
import com.andrespaez.bitrise.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        initViews()
        initViewModel()
    }

    private fun initViews() {
        binding.lifecycleOwner = this
        binding.textViewVersion.text = BuildConfig.VERSION_NAME
    }

    private fun initViewModel() {
        binding.viewModel = viewModel
        subscribeViewModel(viewModel, binding.root)
    }
}