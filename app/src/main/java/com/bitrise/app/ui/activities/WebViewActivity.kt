package com.bitrise.app.ui.activities

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.webkit.*
import androidx.databinding.DataBindingUtil
import com.bitrise.app.R
import com.bitrise.app.databinding.ActivityWebViewBinding

const val WEB_VIEW_URL_PARAM = "url_param"
const val WEB_VIEW_TITLE_PARAM = "title_param"
const val WEB_VIEW_TOOLBAR = "title_param"

class WebViewActivity : BaseActivity() {

    private val REQUEST_FILES = 1001

    private val DRIVE_URL = "https://docs.google.com/gview?embedded=true&url="

    private lateinit var binding: ActivityWebViewBinding

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        initViews()
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        initToolbar()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.allowUniversalAccessFromFileURLs = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.builtInZoomControls = true
    }

    private fun initToolbar() {
        if (intent?.extras?.getBoolean(WEB_VIEW_TOOLBAR, false) == true) {
            binding.toolbar.title = intent?.extras?.getString(WEB_VIEW_TITLE_PARAM) ?: ""
            super.setupActionBar(binding.toolbar, true)
        } else {
            binding.toolbar.visibility = View.GONE
        }
    }

    private fun setupWebView() {
        binding.webView.loadUrl(getUrl())
        binding.webView.webViewClient = getWebViewClient()
        binding.webView.webChromeClient = getWebChromeClient()
    }

    private fun getUrl(): String {
        val bundle = intent.extras
        if (bundle != null) {
            var url = bundle.getString(WEB_VIEW_URL_PARAM) ?: ""
            if (url.contains(".pdf")) {
                url = DRIVE_URL + url
            }
            return url
        }
        return "barnes.com.co"
    }

    private fun getWebViewClient(): WebViewClient {

        return object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = GONE
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                showError(binding.root, error?.description?.toString() ?: "Server Error")
            }

            @SuppressWarnings("deprecation")
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                showError(binding.root, description)
            }
        }
    }

    private fun getWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {

            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                this@WebViewActivity.filePathCallback = filePathCallback
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                startActivityForResult(intent, REQUEST_FILES)
                return true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_FILES && resultCode == RESULT_OK && intent != null) {
            val result = intent.data
            result?.let { filePathCallback?.onReceiveValue(arrayOf(it)) }
        } else {
            filePathCallback?.onReceiveValue(null)
        }
    }

    /**
     * Toolbar Options
     */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_web_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.share_action) {
            shareUrl()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareUrl() {
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getUrl())
            type = "text/plain"
        }
        startActivity(intent)
    }

}