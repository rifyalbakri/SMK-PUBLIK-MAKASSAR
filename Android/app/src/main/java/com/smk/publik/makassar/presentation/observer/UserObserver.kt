package com.smk.publik.makassar.presentation.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.auth.FirebaseUser
import com.smk.publik.makassar.datastore.User
import com.smk.publik.makassar.domain.UserState
import com.smk.publik.makassar.domain.Users
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel

/**
 * @Author Joseph Sanjaya on 20/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class UserObserver(
    private val view: Interfaces, private val viewModel: UserViewModel, private val owner: LifecycleOwner
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        observeUser()
        observeUpdateUser()
        observeRegister()
        observeGetData()
    }

    private fun observeUser() {
        viewModel.mUser.observe(owner, {
            view.onGetUser(it)
        })
    }

    private fun observeUpdateUser() {
        viewModel.mNewUserData.observe(owner, {
            view.onUpdateUser(it)
        })
    }

    private fun observeRegister() {
        viewModel.mRegister.observe(owner, {
            when(it) {
                is UserState.RegisterOrLogin.Loading -> view.onRegisterStart()
                is UserState.RegisterOrLogin.Cancelled -> view.onRegisterCancelled()
                is UserState.RegisterOrLogin.Success -> view.onRegisterSuccess(it.user)
                is UserState.RegisterOrLogin.Failed -> view.onRegisterFailed(it.e)
            }
        })
    }

    private fun observeGetData() {
        viewModel.mUserData.observe(owner, {
            when(it) {
                is UserState.Data.Loading -> view.onFetchingUserData()
                is UserState.Data.Success -> view.onFetchUserDataSuccess(it.user)
                is UserState.Data.Failed -> view.onFetchUserDataFailed(it.e)
            }
        })
    }

    interface Interfaces {
        fun onGetUser(user: User?) {}
        fun onUpdateUser(newUserData: User?) {}

        fun onRegisterStart() {}
        fun onRegisterCancelled() {}
        fun onRegisterFailed(e: Throwable) {}
        fun onRegisterSuccess(user: FirebaseUser?) {}

        fun onFetchingUserData() {}
        fun onFetchUserDataSuccess(data: Users?) {}
        fun onFetchUserDataFailed(e: Throwable) {}
    }
}