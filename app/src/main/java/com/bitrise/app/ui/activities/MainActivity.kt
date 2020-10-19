package com.bitrise.app.ui.activities

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.core.text.inSpans
import androidx.databinding.DataBindingUtil
import com.bitrise.app.BuildConfig
import com.bitrise.app.R
import com.bitrise.app.databinding.ActivityMainBinding
import com.bitrise.app.ui.extensions.appendClickableText
import com.bitrise.app.viewModels.MainActivityViewModel
import com.bitrise.app.viewModels.models.StartActivityModel
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
        binding.textViewFindToken.text =
            SpannableStringBuilder(getString(R.string.copy_get_token_message))
                .color(ContextCompat.getColor(this, R.color.colorAccent)) {
                    appendClickableText(
                        " " + getString(R.string.copy_here) + "!",
                        true,
                        ::startWebView
                    )
                }
        binding.textViewFindToken.movementMethod = LinkMovementMethod.getInstance();
    }

    private fun startWebView() {
        startActivity(
            StartActivityModel(WebViewActivity::class.java,
                Bundle().apply {
                    putString(
                        WEB_VIEW_URL_PARAM,
                        getString(R.string.link_token_generator)
                    )
                }
            )
        )
    }

    private fun initViewModel() {
        binding.viewModel = viewModel
        subscribeViewModel(viewModel, binding.root)
    }
}