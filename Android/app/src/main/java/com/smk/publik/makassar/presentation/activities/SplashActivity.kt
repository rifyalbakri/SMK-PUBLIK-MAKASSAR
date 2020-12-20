package com.smk.publik.makassar.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.auth.FirebaseUser
import com.smk.publik.makassar.databinding.ActivitySplashBinding
import com.smk.publik.makassar.datastore.User
import com.smk.publik.makassar.domain.Users
import com.smk.publik.makassar.presentation.observer.UserObserver
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel
import com.smk.publik.makassar.utils.inline.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @Author Joseph Sanjaya on 06/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class SplashActivity : AppCompatActivity(), UserObserver.Interfaces {

    private val binding by viewBinding(ActivitySplashBinding::inflate)
    private val mViewModel: UserViewModel by viewModel()
    private var mUsers: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycle.addObserver(UserObserver(this, mViewModel, this))
        binding.btnCrash.setOnClickListener {
            mViewModel.registerNew("sanjayajosep@gmail.com", "test1234")
        }
        binding.btnShow.setOnClickListener {
            mViewModel.getUserData(mUsers?.userId.toString())
        }
        mViewModel.getUser()
    }

    override fun onUpdateUser(newUserData: User?) {
        mViewModel.getUser()
        super.onUpdateUser(newUserData)
    }

    override fun onGetUser(user: User?) {
        mUsers = user
        ToastUtils.showShort(user?.userId)
        super.onGetUser(user)
    }

    override fun onRegisterStart() {
        ToastUtils.showShort("Registering...")
        super.onRegisterStart()
    }

    override fun onRegisterSuccess(user: FirebaseUser?) {
        ToastUtils.showShort("Success")
        super.onRegisterSuccess(user)
    }

    override fun onRegisterFailed(e: Throwable) {
        ToastUtils.showShort(e.message)
        super.onRegisterFailed(e)
    }

    override fun onFetchingUserData() {
        ToastUtils.showShort("Fetching...")
        super.onFetchingUserData()
    }

    override fun onFetchUserDataFailed(e: Throwable) {
        ToastUtils.showShort(e.message)
        super.onFetchUserDataFailed(e)
    }

    override fun onFetchUserDataSuccess(data: Users?) {
        ToastUtils.showShort(data?.email)
        super.onFetchUserDataSuccess(data)
    }
}