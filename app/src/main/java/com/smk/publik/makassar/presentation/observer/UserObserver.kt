package com.smk.publik.makassar.presentation.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.auth.FirebaseUser
import com.smk.publik.makassar.datastore.User
import com.smk.publik.makassar.domain.State
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
        viewModel.localUser.observe(owner, {
            when(it) {
                is State.Idle -> view.onLocalUserIdle()
                is State.Loading -> view.onLocalUserLoading()
                is State.Success -> view.onLocalUserSuccess(it.data)
                is State.Failed -> view.onLocalUserFailed(it.throwable)
            }
        })
        viewModel.register.observe(owner, {
            when(it) {
                is State.Idle -> view.onRegisterIdle()
                is State.Loading -> view.onRegisterLoading()
                is State.Success -> view.onRegisterSuccess(it.data)
                is State.Failed -> view.onRegisterFailed(it.throwable)
            }
        })
        viewModel.emailVerify.observe(owner, {
            when(it) {
                is State.Idle -> view.onSendingEmailVerificationIdle()
                is State.Loading -> view.onSendingEmailVerificationLoading()
                is State.Success -> view.onSendingEmailVerificationSuccess()
                is State.Failed -> view.onSendingEmailVerificationFailed(it.throwable)
            }
        })
        viewModel.login.observe(owner, {
            when(it) {
                is State.Idle -> view.onLoginIdle()
                is State.Loading -> view.onLoginLoading()
                is State.Success -> view.onLoginSuccess(it.data)
                is State.Failed -> view.onLoginFailed(it.throwable)
            }
        })
        viewModel.sendForgot.observe(owner, {
            when(it) {
                is State.Idle -> view.onSendForgotPasswordIdle()
                is State.Loading -> view.onSendForgotPasswordLoading()
                is State.Success -> view.onSendForgotPasswordSuccess()
                is State.Failed -> view.onSendForgotPasswordFailed(it.throwable)
            }
        })
        viewModel.verifyCodePassword.observe(owner, {
            when(it) {
                is State.Idle -> view.onVerifyCodePasswordIdle()
                is State.Loading -> view.onVerifyCodePasswordLoading()
                is State.Success -> view.onVerifyCodePasswordSuccess(it.data)
                is State.Failed -> view.onVerifyCodePasswordFailed(it.throwable)
            }
        })
        viewModel.changePassword.observe(owner, {
            when(it) {
                is State.Idle -> view.onChangePasswordIdle()
                is State.Loading -> view.onChangePasswordLoading()
                is State.Success -> view.onChangePasswordSuccess()
                is State.Failed -> view.onChangePasswordFailed(it.throwable)
            }
        })
    }

    interface Interfaces {
        fun onLocalUserIdle() {}
        fun onLocalUserLoading() {}
        fun onLocalUserFailed(e: Throwable) {}
        fun onLocalUserSuccess(user: User?) {}

        fun onRegisterIdle() {}
        fun onRegisterLoading() {}
        fun onRegisterFailed(e: Throwable) {}
        fun onRegisterSuccess(user: FirebaseUser?) {}

        fun onSendingEmailVerificationIdle() {}
        fun onSendingEmailVerificationLoading() {}
        fun onSendingEmailVerificationFailed(e: Throwable) {}
        fun onSendingEmailVerificationSuccess() {}

        fun onLoginIdle() {}
        fun onLoginLoading() {}
        fun onLoginFailed(e: Throwable) {}
        fun onLoginSuccess(user: FirebaseUser?) {}

        fun onSendForgotPasswordIdle() {}
        fun onSendForgotPasswordLoading() {}
        fun onSendForgotPasswordFailed(e: Throwable) {}
        fun onSendForgotPasswordSuccess() {}

        fun onVerifyCodePasswordIdle() {}
        fun onVerifyCodePasswordLoading() {}
        fun onVerifyCodePasswordFailed(e: Throwable) {}
        fun onVerifyCodePasswordSuccess(code: String) {}

        fun onChangePasswordIdle() {}
        fun onChangePasswordLoading() {}
        fun onChangePasswordFailed(e: Throwable) {}
        fun onChangePasswordSuccess() {}
    }
}