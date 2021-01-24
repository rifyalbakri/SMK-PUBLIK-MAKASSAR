package com.smk.publik.makassar.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.smk.publik.makassar.R
import com.smk.publik.makassar.databinding.ActivitySplashBinding
import com.smk.publik.makassar.datastore.User
import com.smk.publik.makassar.presentation.observer.UserObserver
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @Author Joseph Sanjaya on 06/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class SplashActivity : AppCompatActivity(R.layout.activity_splash), UserObserver.Interfaces {
    private val mViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(UserObserver(this, mViewModel, this))
    }

    override fun onStart() {
        if(mViewModel.isLoggedIn) mViewModel.getLocalUserData()
        else launchLogin()
        super.onStart()
    }

    private fun launchLogin() {
        finish()
        ActivityUtils.startActivity(AccountActivity.createLoginIntent(this))
    }

    override fun onLocalUserSuccess(user: User?) {
        if(user == null) launchLogin()
        else launchLogin()
        super.onLocalUserSuccess(user)
    }

    override fun onLocalUserFailed(e: Throwable) {
        launchLogin()
        super.onLocalUserFailed(e)
    }
}