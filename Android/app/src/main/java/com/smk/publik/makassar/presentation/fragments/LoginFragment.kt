package com.smk.publik.makassar.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import coil.load
import com.afollestad.vvalidator.form
import com.blankj.utilcode.util.SnackbarUtils
import com.google.firebase.auth.FirebaseUser
import com.smk.publik.makassar.R
import com.smk.publik.makassar.databinding.FragmentLoginBinding
import com.smk.publik.makassar.interfaces.ActivityInterfaces
import com.smk.publik.makassar.interfaces.BaseOnClickView
import com.smk.publik.makassar.presentation.observer.UserObserver
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel
import com.smk.publik.makassar.utils.inline.createSnackBar
import com.smk.publik.makassar.utils.inline.loadingDialog
import com.smk.publik.makassar.utils.inline.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * @Author Joseph Sanjaya on 27/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class LoginFragment : Fragment(R.layout.fragment_login), BaseOnClickView, UserObserver.Interfaces {
    private val loading by lazy { requireContext().loadingDialog }
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private var mActivityInterfaces: ActivityInterfaces? = null
    private val mViewModel: UserViewModel by viewModel()

    private val mValidator by lazy {
        form {
            input(binding.etEmail) {
                isNotEmpty().description("Email tidak boleh kosong!")
            }
            input(binding.etPassword) {
                isNotEmpty().description("Password tidak boleh kosong!")
            }
        }
    }

    override fun onStart() {
        mActivityInterfaces?.onToolbarChanges("Login", true, isHide = true)
        super.onStart()
    }

    override fun onClick(p0: View?) {
        when(p0) {
            binding.btnLogin -> if(mValidator.validate().success()) mViewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            else binding.root.createSnackBar(mValidator.validate().errors().firstOrNull()?.description).showError()
            binding.tvSignUp -> {
                mActivityInterfaces?.onFragmentChanges(RegisterFragment(), isBackstack = true, isAnimate = true)
                mViewModel.resetLoginState()
            }
        }
        super.onClick(p0)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(UserObserver(this, mViewModel, viewLifecycleOwner))
        binding.ivLogo.load(R.drawable.ic_big_logo)
        binding.listener = this
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onLoginLoading() {
        loading.first.show()
        super.onLoginLoading()
    }

    override fun onLoginFailed(e: Throwable) {
        loading.first.dismiss()
        binding.root.createSnackBar(e.message.toString()).showError()
        super.onLoginFailed(e)
    }

    override fun onLoginSuccess(user: FirebaseUser?) {
        loading.first.dismiss()
        binding.root.createSnackBar("Success, login ${user?.uid}").showSuccess()
        super.onLoginSuccess(user)
    }

    override fun onSendForgotPasswordLoading() {
        loading.first.show()
        super.onSendForgotPasswordLoading()
    }

    override fun onSendForgotPasswordSuccess() {
        loading.first.dismiss()
        binding.root.createSnackBar("Success, silahkan cek email anda").showSuccess()
        super.onSendForgotPasswordSuccess()
    }

    override fun onSendForgotPasswordFailed(e: Throwable) {
        loading.first.dismiss()
        binding.root.createSnackBar(e.message.toString()).showError()
        super.onSendForgotPasswordFailed(e)
    }

    override fun onAttach(context: Context) {
        if(context is ActivityInterfaces) mActivityInterfaces = context
        super.onAttach(context)
    }

    override fun onDetach() {
        if(loading.first.isShowing) loading.first.dismiss()
        mActivityInterfaces = null
        super.onDetach()
    }
}