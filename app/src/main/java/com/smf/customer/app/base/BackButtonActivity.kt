package com.smf.customer.app.base

import android.view.MenuItem


abstract class BackButtonActivity<T : BaseViewModel> : BaseActivity<T>() {


    override fun observer() {
        super.observer()
        viewModel.showLoading.observe(this) { state ->
            supportActionBar?.setDisplayHomeAsUpEnabled(!state)
        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // API 5+ solution
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}