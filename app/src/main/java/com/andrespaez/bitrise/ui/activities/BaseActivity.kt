package com.andrespaez.bitrise.ui.activities

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.andrespaez.bitrise.ui.factories.SnackBarFactory
import com.andrespaez.bitrise.ui.fragments.BaseFragment
import com.andrespaez.bitrise.ui.views.Loading
import com.andrespaez.bitrise.viewModels.AndroidViewModel
import com.andrespaez.bitrise.viewModels.models.DialogParams
import com.andrespaez.bitrise.viewModels.models.FinishActivityModel
import com.andrespaez.bitrise.viewModels.models.StartActionModel
import com.andrespaez.bitrise.viewModels.models.StartActivityModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

open class BaseActivity : AppCompatActivity() {

    private lateinit var loadingView: Loading

    private var pendingForClose: MutableList<BaseFragment> = ArrayList()

    private var pendingForOpen: MutableList<BaseFragment> = ArrayList()

    private var paused: Boolean = false

    /**
     * Override
     */

    @CallSuper
    override fun onResume() {
        super.onResume()
        paused = false
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        paused = true
    }

    /**
     * Fragment
     */

    protected fun replaceFragment(fragment: BaseFragment) {
        if (paused || isFinishing) {

            pendingForOpen.add(fragment)

        } else {

            val ft = supportFragmentManager.beginTransaction()

            if (supportFragmentManager.backStackEntryCount >= 0) {

                ft.setCustomAnimations(fragment.enter, fragment.exit, fragment.popEnter, fragment.popExit)
            }

            ft.replace(fragment.container, fragment, fragment.name)

            findViewById<View>(fragment.container).visibility = View.VISIBLE

            ft.commit()
        }
    }


    /**
     * ViewModel
     */

    protected fun subscribeViewModel(viewModel: AndroidViewModel, root: View) {
        viewModel.loaderState().observe(this, Observer { showLoading(it) })
        viewModel.startActivity().observe(this, Observer { startActivity(it) })
        viewModel.startAction().observe(this, Observer { startAction(it) })
        viewModel.closeView().observe(this, Observer { this.close(it) })
        viewModel.showAlertDialog().observe(this, Observer { this.showAlertDialog(it) })
        viewModel.snackBarMessage().observe(this, Observer { event -> event.show(root) })
    }

    protected open fun showLoading(showing: Boolean) {
        initLoading()
        loadingView.setState(showing)
    }

    private fun initLoading() {
        if (!this::loadingView.isInitialized) {
            loadingView = Loading(this)
            this.addContentView(
                loadingView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            )
        }
    }

    fun setupActionBar(toolbar: Toolbar) {
        setupActionBar(toolbar, true)
    }

    protected fun setupActionBar(toolbar: Toolbar, showButtonBack: Boolean) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showButtonBack)
            actionBar.setDisplayShowHomeEnabled(showButtonBack)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    protected fun startActivity(startActivityModel: StartActivityModel) {
        val intent = Intent(baseContext, startActivityModel.activity)
        startActivityModel.bundle?.let { intent.putExtras(it) }
        intent.flags = startActivityModel.flags
        startActivityForResult(intent, startActivityModel.code)
    }

    protected fun startAction(startActionModel: StartActionModel) {
        val intent = Intent(startActionModel.action)
        startActionModel.uri?.let { intent.setData(it) }
        startActionModel.bundle?.let { intent.putExtras(it) }

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, startActionModel.code)
        }
    }

    protected fun close(finishActivityModel: FinishActivityModel) {
        if (finishActivityModel.intent != null) {
            setResult(finishActivityModel.code, finishActivityModel.intent)
        } else {
            setResult(finishActivityModel.code)
        }
        finish()
    }

    protected fun showAlertDialog(params: DialogParams) {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle(params.title)
            .setMessage(params.message)
            .setIcon(params.icon)
            .setCancelable(params.cancelable)
            .setPositiveButton(params.positiveText, params.positiveAction)
        if (params.negativeText > 0 && params.negativeAction != null) {
            alertDialogBuilder.setNegativeButton(params.negativeText, params.negativeAction)
        }
        if (params.neutralText > 0 && params.neutralAction != null) {
            alertDialogBuilder.setNeutralButton(params.neutralText, params.neutralAction)
        }
        alertDialogBuilder.create().show()
    }

    fun showMessage(@SnackBarFactory.SnackBarType type: String, view: View, message: String) {
        SnackBarFactory.getSnackBar(type, view, message, Snackbar.LENGTH_LONG).show()
    }

    fun showMessage(@SnackBarFactory.SnackBarType type: String, view: View, message: String, duration: Int) {
        SnackBarFactory.getSnackBar(type, view, message, duration).show()
    }

    fun showMessage(@ColorInt color: Int, view: View, message: String, duration: Int) {
        SnackBarFactory.getSnackBar(color, view, message, duration).show()
    }

    fun showError(view: View, message: String) {
        showMessage(SnackBarFactory.TYPE_ERROR, view, message, Snackbar.LENGTH_LONG)
    }

    fun showInfoMessage(view: View, message: String) {
        showMessage(SnackBarFactory.TYPE_INFO, view, message, Snackbar.LENGTH_LONG)
    }

}

